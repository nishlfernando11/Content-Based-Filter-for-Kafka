/**
 *
 */
package com.filter.clients;

import QueryEvaluator.QueryEvaluator;
import QueryEvaluator.ast.StringExpression;
import com.filter.clients.Interfaces.IContentConsumer;
import org.ahocorasick.trie.Trie;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;

import static com.filter.clients.ContentFilter.buildInterestTrie;
import static com.filter.clients.ContentFilter.*;

/**
 * This class manages the Content consumer filter process
 */
public class ContentConsumer implements IContentConsumer{

    public static void main(String[] args) throws Exception {
        /*
        version 1: single interest list
        System.out.println("Starting consumer...Enter your interests.. Press 'end' to terminate.");

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
        */

        /*
        version 3: interests are collected from a NOA query
        */
        System.out.println("Starting consumer...");
        Scanner sc = new Scanner(System.in);
        String expression = "";
        if(args.length > 0 && args[0].equals("-f")){
            while(sc.hasNextLine()){
                expression += sc.nextLine();
            }
            System.out.println(expression);

        } else{
            System.out.println("Insert an subscription expression:");
            expression = sc.nextLine();
        }
        System.out.println("exp :" +expression);


        System.out.println("what: " +Arrays.asList(args));
        start(Arrays.asList(args), expression);

    }

    /**
     * Simple method to call filter messages for the collected interests
     * @param topics topics that consumer is subscribed to
     * @param contentArray interests of the consumer as content
     * @throws Exception should be handled and make sure it is thread safe
     * @version 1.0 all interests collected and evaluated with or(|) when matching
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

        // print the topic name
        System.out.println("Subscribed to topics " + topics );

        // build the Trie for each consumer interests
        Trie trie = buildInterestTrie((ArrayList)contentArray);

        while (true) {
            boolean isMatch=false;
            ConsumerRecords<String, String> records = consumer.poll(200);
            for (ConsumerRecord<String, String> record : records){
                String res = record.value();

                //  or match applied
                isMatch= orMatch(trie,res);
                if(isMatch){
                    System.out.println(res);
                }
            }
        }
    }

    /**
     * This method is called for consumer interests that come in three categories as OR, NOT, AND.
     * This method evaluates the messages for all three categories in NOT, OR, AND order of precedence
     * @param topics topics that consumer is subscribed to
     * @param interest interests of consumer
     * @throws Exception should be handled and make sure it is thread safe
     * @version 2.0 interests collected separately for the three categories
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
     * This Method is to call filter messages for the collected interests as a NOA expression
     * NOA expression contains &, | , ! and strings according to the string expression grammar
     * @param topics topics that consumer is subscribed to
     * @param expression interests of consumer are taken as a NOA expression
     * @throws Exception should be handled and make sure it is thread safe
     * @version 3.0 interests are collected in one NOA query as an expression
     */

    public static void start(List<String> topics, String expression) throws Exception {

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

        long endtime1, endtime2, endtime3,endtime4;

        // Kafka Consumer subscribes list of topics here.
        consumer.subscribe(topics);

        // print the topic name
        System.out.println("Subscribed to topics " + topics );
        //System.out.println("exp1 " + expression );

        // long curtime = System.nanoTime();

        // Parse the NOA expression and interpret
        StringExpression ast= QueryEvaluator.parseExpression(expression);

        //endtime1 = System.nanoTime();
        // System.out.println("parse time: "+(endtime1 - curtime)/1000000 + " millisecs");

        // endtime1 = System.nanoTime();
		
        // generate TrieList for all sub expressions in interpreted NOA expression
        Map<Integer,Trie> triesList = QueryEvaluator.generateTries(ast);

        // endtime2 = System.nanoTime();
        // System.out.println("tries time: "+(endtime2 - endtime1)/1000000 + " millisecs");
        //System.out.println("exp " + ast );
        // int no = 1;
        // int i = 1;
        // long sum = 0;
        while (true) {
            boolean isMatch=false;
            ConsumerRecords<String, String> records = consumer.poll(200);
            for (ConsumerRecord<String, String> record : records){
                String res = record.value();

                // filter each message against the subscription of interests
                // endtime3 = System.nanoTime();
                boolean b =  QueryEvaluator.filter(res, ast, triesList);
                // endtime4 = System.nanoTime();
                // System.out.println("filter time: "+(endtime4 - endtime3) + " nanosecs & " + (endtime4-endtime3)/1000000+" millisecs");
                // sum  = (sum +(endtime4 - endtime3))/1000000;
                // System.out.println(i+ " sum "+sum+"; cum avg filter time: "+  (sum/i)+" millisecs");
                if(b){
				    // System.out.println(no+ " "+ res);
                    System.out.println(res);
                    // no++;
                }
                // i++;
            }
        }
    }


}
