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

import java.util.Arrays;
import java.util.Properties;

public class FilterConsumer {

    private static Coder codec = new Coder();

    private static FilterExpression falseExpression = new MvelExpression("entity==1");
    private static FilterExpression trueExpression = new MvelExpression("entity=='ORDER' && type == 'CREATE'");

    public static void main(String[] args) throws Exception {

        //Kafka consumer configuration settings
        String topicName = "mytopic";
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
        consumer.subscribe(Arrays.asList(topicName, "test1"));

        //print the topic name
        System.out.println("Subscribed to topic " + topicName);
        int i = 0;

        while (true) {
            ConsumerRecords<byte[], byte[]> records = consumer.poll(200);
            for (ConsumerRecord<byte[], byte[]> record : records){
                byte[] res = record.value();
                int len = res.length;
                FilterWrapper wrappedMsg = codec.decode(record.value());

                String recv = new String(wrappedMsg.getData());

                if(trueExpression.isInteresting(wrappedMsg)){
                    System.out.println(recv);
                }
//              print the offset,key and value for the consumer records.
                //System.out.printf("offset = %d, key = %s, value = %s\n",record.offset(), record.key(), wrappedMsg.getData());
            }
        }
    }
}
