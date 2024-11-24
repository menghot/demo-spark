package org.example;

import org.apache.spark.deploy.SparkSubmitOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ServiceLoader;


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
