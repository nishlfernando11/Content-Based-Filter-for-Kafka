package com.filter.clients;

//import util.properties packages

import main.java.com.filter.Coder;
import main.java.com.filter.model.FilterWrapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//import simple producer packages
//import KafkaProducer packages
//import ProducerRecord packages


/**
 * Created with IntelliJ IDEA.
 * User: Irosha
 * Date: 8/30/16
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterProducer {

    private static Map<String,String> tags = new HashMap<String, String>() {{
        put("entity", "ORDER");
        put("type", "CREATE");
        put("long", "1283102390123");
    }};

    private static Coder codec = new Coder();

    public static void main(String[] args) throws Exception{

        // Check arguments length value
        String topicName = null;
        if(args.length == 0){
            topicName = "mytopic";
        }else{
            //Assign topicName to string variable
            topicName = args[0].toString();
        }




        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

//
//        //Assign localhost id
//        props.put("bootstrap.servers", "localhost:9092");
//
//        //Set acknowledgements for producer requests.
//        props.put("acks", "all");
//
//        //If the request fails, the producer can automatically retry,
//        props.put("retries", 0);
//
//        //Specify buffer size in config
//        props.put("batch.size", 16384);
//
//        //Reduce the no of requests less than 0
//        props.put("linger.ms", 1);
//
//        //The buffer.memory controls the total amount of memory available to the producer for buffering.
//        props.put("buffer.memory", 33554432);
//
//        props.put("key.serializer",
//                "org.apache.kafka.common.serializa-tion.StringSerializer");
//
//        props.put("value.serializer",
//                "org.apache.kafka.common.serializa-tion.StringSerializer");

        Producer<byte[], byte[]> producer = new KafkaProducer<byte[], byte[]>(props);

        for(int i = 0; i < 10; i++){
            byte[] encoded = codec.encode("22".getBytes(), tags);
            System.out.println(new String(encoded).getBytes());
            producer.send(new ProducerRecord<byte[],byte[]>(topicName,encoded, encoded));

            FilterWrapper wrappedMsg = codec.decode(encoded);
            //System.out.println(new String(wrappedMsg.getData()));
        }
        System.out.println("Message sent successfully");
        producer.close();
    }
}
