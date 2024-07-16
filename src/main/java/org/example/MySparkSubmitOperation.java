package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.deploy.SparkSubmitOperation;

public class MySparkSubmitOperation implements SparkSubmitOperation {

    public MySparkSubmitOperation() {
        System.out.println(">>>>>>>>>>>> init MySparkSubmitOperation >>>>>>>>>> ");
    }

    @Override
    public void kill(String submissionId, SparkConf conf) {

    }

    @Override
    public void printSubmissionStatus(String submissionId, SparkConf conf) {
        System.out.println("--------printSubmissionStatus------------" + submissionId);
    }

    @Override
    public boolean supports(String master) {
        return true;
    }
}