package main.java.com.filter.clients;

/**
 * Created with IntelliJ IDEA.
 * User: Irosha
 * Date: 8/31/16
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */

import main.java.com.filter.Coder;
import main.java.com.filter.exp.FilterExpression;
import main.java.com.filter.exp.MvelExpression;
import main.java.com.filter.model.FilterWrapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;

public class FilterConsumer {

    private static Coder codec = new Coder();
    private static FilterExpression trueExpression;


    public static void start(List<String> topics, ArrayList<String> contentArray) throws Exception {

        //Kafka consumer configuration settings
        //String topicName = topic;
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<byte[], byte[]>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(topics);

        StringBuilder builder = new StringBuilder();
        for(int x = 0; x < contentArray.size(); x++){
            builder.append(contentArray.get(x));
            if(x != contentArray.size()-1)builder.append(" || ");
        }
        System.out.println(builder.toString());

        trueExpression = new MvelExpression(builder.toString());
        //print the topic name
        System.out.println("Subscribed to topic " + topics);
        int i = 0;

        while (true) {
            ConsumerRecords<byte[], byte[]> records = consumer.poll(200);
            for (ConsumerRecord<byte[], byte[]> record : records){
                byte[] res = record.value();
                int len = res.length;
                FilterWrapper wrappedMsg = codec.decode(record.value());


//                Map<String, String> tagSet = wrappedMsg.getTags();
//
//                try{
//                    for(String content : contentArray){
//                        if(tagSet.containsKey(content)){
//                            String recv = new String(wrappedMsg.getData());
//                            System.out.println(recv);
//                            break;
//                        }
//                }}catch (NullPointerException e){
//
//                }
                try{
                    if(trueExpression.isInteresting(wrappedMsg)){
                        String recv = new String(wrappedMsg.getData());
                        System.out.println(recv);
                    }
                }catch (NullPointerException e){

                }

//              print the offset,key and value for the consumer records.
                //System.out.printf("offset = %d, key = %s, value = %s\n",record.offset(), record.key(), wrappedMsg.getData());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting consumer...Enter your interests.. Press 'end' to terminate.");
        //String[] topic = args;
        Scanner scn = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<String>();
        while(true){
            String input = scn.nextLine();
            if(input.equalsIgnoreCase("end")) break;
            list.add(input);
        }
        //String[] content = {args[3],args[4]};
        start(Arrays.asList(args), list);
    }
}
