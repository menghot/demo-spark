package org.example;

import org.apache.spark.sql.SparkSession;

public class SparkIcebergMultipleCatalogsAndOpenLineage {


    public static void main(String[] args) throws InterruptedException {
        // Create a Spark session
        SparkSession spark = SparkSession
                .builder()
                .config("spark.master", "local")
                .appName("IcebergWriteExample")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hadoop")
                .config("spark.sql.catalog.spark_catalog.warehouse", "warehouse")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")

                .config("spark.sql.catalog.local", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.local.type", "hadoop")
                .config("spark.sql.catalog.local.warehouse", "warehouse_local")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")

                .config("spark.sql.catalog.local2", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.local2.type", "hadoop")
                .config("spark.sql.catalog.local2.warehouse", "warehouse_local2")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                .config("spark.extraListeners", "io.openlineage.spark.agent.OpenLineageSparkListener")

                .getOrCreate();

        spark.sql("use local");
        spark.sql("create schema if not exists localods");
        spark.sql("create table IF NOT EXISTS localods.my_iceberg_table2  (id int, data string) using iceberg");
        spark.sql("show schemas").show();
        spark.sql("show catalogs").show();
        spark.sql("show tables from localods").show();

        // spark.sparkContext().setLogLevel("DEBUG");

        spark.sql("use spark_catalog");
        spark.sql("show catalogs").show();
        spark.sql("show schemas").show();

        spark.sql("create schema if not exists ods");
        spark.sql("drop table if exists ods.my_iceberg_table purge ");
        spark.sql("drop table if exists ods.my_iceberg_table2 purge ");
        spark.sql("create table IF NOT EXISTS ods.my_iceberg_table  (id int, data string) using iceberg");
        spark.sql("create table IF NOT EXISTS ods.my_iceberg_table2 (id int, data string) using iceberg");

        spark.sql("MERGE INTO ods.my_iceberg_table  t using ods.my_iceberg_table2 s on t.id = s.id when matched then update set t.data = s.data");

        spark.sql("use local");
        spark.sql("show catalogs").show();

        spark.sql("use spark_catalog");
        spark.sql("show catalogs").show();

        spark.sql("select * from local.localods.my_iceberg_table2").show();

        spark.sql("create schema if not exists local2.localods");
        spark.sql("create table IF NOT EXISTS  local2.localods.my_iceberg_table22  (id int, data string) using iceberg");

        spark.sql("show catalogs").show();

        // Stop the Spark session

        Thread.sleep(100000000);
        spark.stop();
    }
}
