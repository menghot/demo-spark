package org.example;

import org.apache.spark.deploy.history.BasicEventFilterBuilder;
import org.apache.spark.scheduler.SparkListenerApplicationEnd;
import org.apache.spark.scheduler.SparkListenerApplicationStart;

public class MyEventFilterBuilder extends BasicEventFilterBuilder {

    public MyEventFilterBuilder() {
        System.out.println(">>>>>>>>>>>>>>>>>>MyEventFilterBuilder>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void onApplicationEnd(SparkListenerApplicationEnd applicationEnd) {
        System.out.println(">>>>>>>>>>>>>>>>>>MyEventFilterBuilder>>>>>>>>>>>>>>>>>>> applicationEnd" );

        super.onApplicationEnd(applicationEnd);
    }

    @Override
    public void onApplicationStart(SparkListenerApplicationStart applicationStart) {
        System.out.println(">>>>>>>>>>>>>>>>>>MyEventFilterBuilder>>>>>>>>>>>>>>>>>>> applicationStart" );
        super.onApplicationStart(applicationStart);
    }
}
