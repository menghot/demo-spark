package org.example;

import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;

public class SparkApi {

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("SparkApi")
                .master("local")
                .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.spark_catalog.type", "hadoop")
                .config("spark.sql.catalog.spark_catalog.warehouse", "warehouse")
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions,org.example.SqlTracerExtensions")
                .getOrCreate();

        // Create ods.another_table using DataFrame API (if not already created)
        StructType anotherTableSchema = new StructType()
                .add("id", DataTypes.IntegerType)
                .add("data", DataTypes.StringType);

        Row[] sampleData = new Row[]{
                RowFactory.create(1, "Data 1"),
                RowFactory.create(2, "Data 2"),
                RowFactory.create(3, "Data 3")
        };


        Dataset<Row> anotherTableData = spark.createDataFrame(Arrays.asList(sampleData), anotherTableSchema);
        anotherTableData.write().mode(SaveMode.Overwrite).format("iceberg").saveAsTable("ods.another_table");

        // Create ods.my_iceberg_table using DataFrame API (if not already created)
        StructType icebergTableSchema = new StructType()
                .add("id", DataTypes.IntegerType)
                .add("data", DataTypes.StringType);

        Dataset<Row> icebergTableData = spark.createDataFrame(Arrays.asList(sampleData), icebergTableSchema);
        icebergTableData.write().mode(SaveMode.Overwrite).format("iceberg").saveAsTable("ods.my_iceberg_table");

        // Perform insert operation into ods.my_iceberg_table from ods.another_table
        Dataset<Row> insertedData = spark.table("ods.another_table");
        insertedData.write().mode(SaveMode.Append).insertInto("ods.my_iceberg_table");

        // Show the inserted data in ods.my_iceberg_table
        spark.table("ods.my_iceberg_table").show();

        // Stop Spark session
        spark.stop();
    }
}
