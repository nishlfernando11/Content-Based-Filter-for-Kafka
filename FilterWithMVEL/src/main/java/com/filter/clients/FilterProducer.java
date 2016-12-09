package com.filter.clients;

//import util.properties packages

import com.filter.Coder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

//import simple producer packages
//import KafkaProducer packages
//import ProducerRecord packages

/**
 * This class manages the Content producer message sending process
 */
public class FilterProducer {


    private static Map<String,String> tags = null;
    public static Producer<String, String> kafkaProducer = null;
    public static String topic;
    //    private static Coder codec = new Coder();

    public FilterProducer(String topic){
        this.setTopic(topic);
    }

    public static void main(String args[]){

        BufferedReader br = null;
        String topicName = args[0];
        String fileName = args[1];
        FilterProducer producer = new FilterProducer(topicName);

        setTopic(topicName);
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        if(kafkaProducer == null) kafkaProducer = new KafkaProducer<String, String>(props);

        try {
            String sCurrentLine;
            // change the local path to message stream
            br = new BufferedReader(new FileReader("C:\\Users\\user\\Documents\\GitHub\\Content-Based-Filter-for-Kafka\\"+fileName));
            long start = System.nanoTime();

            long messageCount = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                try {
                    kafkaProducer.send(new ProducerRecord<String,String>(getTopic(),sCurrentLine, sCurrentLine));
                    System.out.println(++messageCount);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            long stop = System.nanoTime();
            System.out.println(stop - start);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }

    /**
     * gets the Topic name
     * @return the Topic name
     */
    public static String getTopic() {
        return topic;
    }

    /**
     * sets the Topic name
     * @param topicName Topic name
     */
    public static void setTopic(String topicName) {
        topic= topicName;
    }
}
