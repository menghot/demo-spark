package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkIcebergHiveCatalog {
    public static void main(String[] args) {
        //System.out.println(org.apache.hadoop.fs.s3a.S3AFileSystem.class);
        SparkConf conf = new SparkConf();
        if (System.getenv("SPARK_AUTH_SECRET") != null) {
            System.out.println("----SPARK_AUTH_SECRET-----" + System.getenv("SPARK_AUTH_SECRET"));
            conf.set("spark.authenticate.secret", System.getenv("SPARK_AUTH_SECRET"));
        }

        SparkSession spark = SparkSession
                .builder()
                .appName("HiveApp")
                .master("local")

                //.config("spark.master", "spark://192.168.80.241:7077")
                //.config("spark.max.cores", "2")
                //.config("spark.executor.cores", "2")
                //.config("spark.dynamicAllocation.maxExecutors", "2")
                //.config("spark.dynamicAllocation.enabled", "true")
                // spark.submit.waitForCompletion

                .config(conf)
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .config("spark.hadoop.fs.s3a.endpoint", "http://192.168.80.241:9000")
                .config("spark.hadoop.fs.s3a.access.key", "w2Xaege1JZSCpop1Dqd9")
                .config("spark.hadoop.fs.s3a.secret.key", "yWvk6CGD9FofHE78prTeDn3w3vrqgTtGStz2TnNq")
                .config("spark.hadoop,fs.s3a.path.style.access", "true")

                .config("spark.sql.catalog.spark_catalog.type", "hive")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hive")
                .config("spark.sql.catalog.spark_catalog.uri", "thrift://192.168.80.241:9083")
                .config("hive.metastore.uris", "thrift://192.168.80.241:9083")
                .config("spark.sql.catalog.spark_catalog.warehouse", "s3a://hive/warehouse")
                .config("spark.sql.defaultCatalog", "spark_catalog")

                //.enableHiveSupport()
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .getOrCreate();

        //spark.sparkContext().setLogLevel("DEBUG");

        spark.sql("show catalogs").show();

        spark.sql("show schemas").show();

        spark.sql("create schema if not exists ods");

        spark.sql("show tables").show();

        // Define Iceberg table
        String tableName = "ods.sample_table";

        // Create Iceberg table
        spark.sql("CREATE or replace TABLE " + tableName + " (id INT, data STRING) USING iceberg");

        // Write data to the table
        spark.sql("INSERT INTO " + tableName + " VALUES (1, 'a'), (2, 'b'), (3, 'c')");

        // Read data from the table
        Dataset<Row> df = spark.sql("SELECT * FROM " + tableName);
        df.show();

        spark.sql("create or replace view ods.v_sample_table as select * from ods.sample_table");

        spark.sql("select * from ods.v_sample_table").show();

        spark.close();
    }
}
