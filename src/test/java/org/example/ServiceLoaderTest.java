package org.example;

import org.apache.spark.deploy.SparkSubmitOperation;

import java.util.ServiceLoader;

public class ServiceLoaderTest {


    public static void main(String[] args) {
        ServiceLoader<SparkSubmitOperation> serviceLoader = ServiceLoader.load(SparkSubmitOperation.class);
        System.out.println(serviceLoader.stream().count());
    }
}
