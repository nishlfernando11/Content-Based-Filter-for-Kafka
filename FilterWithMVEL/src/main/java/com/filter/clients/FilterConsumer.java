/**
 *
 */
package com.filter.clients;

import com.filter.Coder;
import com.filter.exp.FilterExpression;
import com.filter.exp.MvelExpression;
import com.filter.model.FilterWrapper;
import org.ahocorasick.trie.Trie;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;

import static com.filter.clients.FilterConsumerAlgo.buildInterestTrie;
import static com.filter.clients.FilterConsumerAlgo.*;

/**
 * This class manages the Content consumer filter process
 */
public class FilterConsumer {

    //private static Coder codec = new Coder();
//    private static FilterExpression trueExpression;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting consumer...Enter your interests.. Press 'end' to terminate.");

        /*
        version 1: single interest list

        //String[] topic = args;
        Scanner in = new Scanner(System.in);
        ArrayList<String> list = new ArrayList<String>();
        while(true){
            String input = in.nextLine();
            if(input.equalsIgnoreCase("end")) break;
            list.add(input);
        }
        //String[] content = {args[3],args[4]};
        start(Arrays.asList(args), list);
        */

        /*
        version 2: three interest lists for  not, and, or
        */
        System.out.println("Starting consumer...Enter your interests..");

        Map<String, List<String>> interest = new HashMap<String, List<String>>();
        ArrayList<String> list_not = new ArrayList<String>();
        ArrayList<String> list_and = new ArrayList<String>();
        ArrayList<String> list_or = new ArrayList<String>();

        Scanner scn = new Scanner(System.in);
        System.out.println("Enter your NOT interests.. Press 'ok' to terminate");
        while(true){
            String input = scn.nextLine();
            if(input.equalsIgnoreCase("ok")) break;
            list_not.add(input);
        }
        System.out.println("Enter your AND interests.. Press 'ok' to terminate");
        while(true){
            String input = scn.nextLine();
            if(input.equalsIgnoreCase("ok")) break;
            list_and.add(input);
        }
        System.out.println("Enter your OR interests.. Press 'ok' to terminate");
        while(true){
            String input = scn.nextLine();
            if(input.equalsIgnoreCase("ok")) break;
            list_or.add(input);
        }
        interest.put("NOT",list_not);
        interest.put("AND",list_and);
        interest.put("OR",list_or);


        //String[] content = {args[3],args[4]};
        start(Arrays.asList(args), interest);
    }

    /**
     * This method is called for consumer interests that come in three categories as OR, NOT, AND.
     * This method evaluates the messages for all three categories in NOT, OR, AND order of precedence
     * @param topics topics that consumer is subscribed to
     * @param interest interests of consumer
     * @throws Exception should be handled and make sure it is thread safe
     */
    public static void start(List<String> topics, Map<String, List<String>> interest) throws Exception {

        // Kafka consumer configuration settings
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        //group id is set to "test"
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        // Kafka Consumer subscribes list of topics here.
        consumer.subscribe(topics);

        System.out.println("Subscribed to topics " + topics );

        // build the Trie for each consumer interests considering types
        Trie trie_not = buildInterestTrie((ArrayList)interest.get("NOT"));
        Trie trie_or = buildInterestTrie((ArrayList)interest.get("OR"));
        Trie trie_and = buildInterestTrie((ArrayList)interest.get("AND"));


        while (true) {
            boolean isNotMatch=false;
            boolean isOrMatch=false;
            boolean isAndMatch=false;

            ConsumerRecords<String, String> records = consumer.poll(200);
            for (ConsumerRecord<String, String> record : records){
                String res = record.value();
                int len = res.length();

                /*
                matching operates in the following order for the three interest categories
                First filter not interested messages using NOT
                Second filter any one of given interests using OR
                Third filter all given interest using AND
                 */
                isNotMatch= notMatch(trie_not,res);
                if(isNotMatch){
                    isOrMatch =  orMatch(trie_or,res);
                    isAndMatch = andMatch(trie_and,res);
                    if(isOrMatch || isAndMatch ){
                            System.out.println(res);
                    }
                }

            }
        }
    }

    /**
     * Simple method to call filter messages for the collected interests
     * @param topics topics that consumer is subscribed to
     * @param contentArray interests of the consumer as content
     * @throws Exception should be handled and make sure it is thread safe
     */
    public static void start(List<String> topics, ArrayList<String> contentArray) throws Exception {

        // Kafka consumer configuration settings
        //String topicName = topic;
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        // Kafka Consumer subscribes list of topics here.
        consumer.subscribe(topics);

//        StringBuilder builder = new StringBuilder();
//        for(int x = 0; x < contentArray.size(); x++){
//            builder.append(contentArray.get(x));
//            if(x != contentArray.size()-1)builder.append(" || ");
//        }
//        System.out.println(builder.toString());
//
//        trueExpression = new MvelExpression(builder.toString());

        // print the topic name
        System.out.println("Subscribed to topics " + topics );

        // build the Trie for each consumer interests
        Trie trie = buildInterestTrie((ArrayList)contentArray);

        while (true) {
            boolean isMatch=false;
            ConsumerRecords<String, String> records = consumer.poll(200);
            for (ConsumerRecord<String, String> record : records){
                String res = record.value();
                int len = res.length();
                //  or match applied
                isMatch= notMatch(trie,res);
                if(isMatch){
                    System.out.println(res);
                 }

//                FilterWrapper wrappedMsg = codec.decode(record.value());


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
//                try{
////                    for(int x = 0; x < contentArray.size(); x++){
////                        isMatch = FilterConsumerAlgo.match(res,contentArray.get(x));
////                        if(isMatch){
////                            System.out.println(res);
////                        }
//                    }
//                    if(trueExpression.isInteresting(wrappedMsg)){
//                        String recv = new String(wrappedMsg.getData());
//                        System.out.println(recv);
//                    }
//                }catch (NullPointerException e){
//
//                }

//              print the offset,key and value for the consumer records.
                //System.out.printf("offset = %d, key = %s, value = %s\n",record.offset(), record.key(), wrappedMsg.getData());
            }
        }
    }

}
