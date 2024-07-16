package org.example;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.udf;

public class SparkIcebergHadoopCatalog {

    private static final Logger log = LoggerFactory.getLogger(SparkIcebergHadoopCatalog.class);

    public static void main(String[] args) {
        // Create a Spark session

        SparkSession spark = SparkSession
                .builder()
                .appName(SparkIcebergHadoopCatalog.class.getName())
                //.master("local")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hadoop")
                .config("spark.sql.catalog.spark_catalog.warehouse", "warehouse0000")

                // config
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions,org.example.SparkSQLExtensions")
                //.config("spark.extraListeners", "io.openlineage.spark.agent.OpenLineageSparkListener")

                .getOrCreate();

        //spark.sparkContext().setLogLevel("DEBUG");
        UserDefinedFunction sleepUDF = udf((Integer duration) -> {
            try {
                log.info("---------------> sleep now");
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("---------------> sleep done");

            return "Slept for " + duration + " milliseconds";
        }, DataTypes.StringType);

        spark.udf().register("sleepUDF", sleepUDF);

        spark.sql("create schema if not exists ods");
        spark.sql("drop table if exists ods.my_iceberg_table purge ");
        spark.sql("drop table if exists ods.my_iceberg_table2 purge ");
        spark.sql("create table IF NOT EXISTS ods.my_iceberg_table  (id int, data string) using iceberg");
        spark.sql("create table IF NOT EXISTS ods.my_iceberg_table2 (id int, data string) using iceberg");
        spark.sql("insert into ods.my_iceberg_table2 values (1,'simon')");

        log.info("-----count-----> " + spark.sql("select * from ods.my_iceberg_table2").count());
        spark.sql("MERGE INTO ods.my_iceberg_table  t using ods.my_iceberg_table2 s on t.id = s.id when matched then update set t.data = sleepUDF(100000)" +
                " when not matched then insert (id, data) values (s.id, s.data)");

        //spark.sql("select sleepUDF(100000)").show();
        spark.stop();
    }
}
