package org.example;

import org.apache.spark.deploy.SparkSubmitOperation;

import java.util.ServiceLoader;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class ServiceLoaderTest {


    public static void main(String[] args) {
        ServiceLoader<SparkSubmitOperation> serviceLoader = ServiceLoader.load(SparkSubmitOperation.class);
        System.out.println(serviceLoader.stream().count());


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
        String rawPassword = "cracn123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }
}
