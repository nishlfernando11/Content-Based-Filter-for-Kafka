package com.filter.layer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Irosha
 * Date: 9/1/16
 * Time: 9:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterImpl implements Filter {

    KafkaConsumer<String, String> consumer = null;

    public KafkaConsumer FilterConsumer(String topic, boolean contentEnable) {

        String topicName = topic;
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        if(contentEnable){
            consumer = new KafkaConsumer<String, String>(props);
            consumer.subscribe(Arrays.asList(topicName));
        }else{

        }
        return consumer;
    }
}
