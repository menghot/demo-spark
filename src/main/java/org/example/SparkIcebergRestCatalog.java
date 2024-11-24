package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkIcebergRestCatalog {
    public static void main(String[] args) {

//        System.out.println(org.apache.hadoop.fs.s3a.S3AFileSystem.class);

        SparkConf conf = new SparkConf();
        if (System.getenv("SPARK_AUTH_SECRET") != null) {
            System.out.println("----SPARK_AUTH_SECRET-----" + System.getenv("SPARK_AUTH_SECRET"));
            conf.set("spark.authenticate.secret", System.getenv("SPARK_AUTH_SECRET"));
        }

        //System.setProperty("aws.region", "eu-west-1");
        //System.setProperty("s3.region", "eu-west-1");

        SparkSession spark = SparkSession
                .builder()
                .appName("SparkAppRestCatalog")
                .master("local")


//        SparkSession.builder()
//                .appName("Iceberg with MinIO and RESTCatalog")
//                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
//                .config("spark.sql.catalog.my_catalog", "org.apache.iceberg.spark.SparkCatalog")
//                .config("spark.sql.catalog.my_catalog.catalog-impl", "org.apache.iceberg.rest.RESTCatalog")
//                .config("spark.sql.catalog.my_catalog.uri", "http://<rest-catalog-host>:8181") // REST catalog endpoint
//                .config("spark.sql.catalog.my_catalog.warehouse", "s3a://my-bucket/warehouse") // MinIO bucket
//                .config("spark.sql.catalog.my_catalog.io-impl", "org.apache.iceberg.aws.s3.S3FileIO")
//                .config("spark.hadoop.fs.s3a.access.key", "<minio-access-key>")
//                .config("spark.hadoop.fs.s3a.secret.key", "<minio-secret-key>")
//                .config("spark.hadoop.fs.s3a.endpoint", "http://<minio-host>:9000")
//                .config("spark.hadoop.fs.s3a.path.style.access", "true")
//                .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false") // Set true if using HTTPS
//                .getOrCreate()

                .config(conf)
//                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .config("spark.hadoop.fs.s3a.endpoint", "http://192.168.80.241:9000")
                //.config("spark.hadoop.fs.s3a.region", "ods")
                .config("spark.hadoop.fs.s3a.access.key", "w2Xaege1JZSCpop1Dqd9")
                .config("spark.hadoop.fs.s3a.secret.key", "yWvk6CGD9FofHE78prTeDn3w3vrqgTtGStz2TnNq")
                .config("spark.hadoop,fs.s3a.path.style.access", "true")
                .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")

                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                //.config("spark.sql.catalog.spark_catalog.type", "rest")
                .config("spark.sql.catalog.spark_catalog.s3.endpoint", "http://192.168.80.241:9000")
                .config("spark.sql.catalog.spark_catalog.io-impl", "org.apache.iceberg.aws.s3.S3FileIO")
                .config("spark.sql.catalog.spark_catalog.catalog-impl", "org.apache.iceberg.rest.RESTCatalog")
                .config("spark.sql.catalog.spark_catalog.uri", "http://192.168.80.241:8181")
                .config("spark.sql.catalog.spark_catalog.default-namespace", "ods")
                .config("spark.sql.catalog.spark_catalog.warehouse", "s3a://hive/warehouse")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
                //.enableHiveSupport()

                .getOrCreate();

        //spark.sparkContext().setLogLevel("DEBUG");

        spark.sql("show catalogs").show();
        spark.sql("show schemas").show();
        spark.sql("create schema if not exists ods");
        spark.sql("show schemas").show();

        // Define Iceberg table
        String tableName = "ods.sample_table";

        // Create Iceberg table
         spark.sql("CREATE or replace TABLE " + tableName + " (id INT, data STRING) USING iceberg");

        // Write data to the table
        spark.sql("INSERT INTO " + tableName + " VALUES (1, 'a'), (2, 'b'), (3, 'c')");

        // Read data from the table
        Dataset<Row> df = spark.sql("SELECT * FROM " + tableName);
        df.show();

        spark.sql("create view ods.v_sample_table as select * from ods.sample_table");
        //spark.sql("select * from ods.v_sample_table").show();
        
        spark.close();
    }
}
