package main.java.com.filter.clients;

//import util.properties packages

import main.java.com.filter.Coder;
import main.java.com.filter.extract.KeyWordExtractor;
import main.java.com.filter.model.FilterWrapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
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


    private static Map<String,String> tags = null;

    private static Coder codec = new Coder();

    public String topic;

    public FilterProducer(String topic){
        this.setTopic(topic);
    }

    public static void main(String args[]){

        BufferedReader br = null;
        String topic = args[0];
        String fileName = args[1];
        FilterProducer producer = new FilterProducer(topic);

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("D:\\"+fileName));
            long start = System.nanoTime();

            while ((sCurrentLine = br.readLine()) != null) {
                try {
                    producer.send(sCurrentLine);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            long stop = System.nanoTime();
            System.out.println(stop - start);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) throws Exception{

//
//        String txt = "SLFP Minister Wijith Wijayamuni Zoysa said yesterday that it was the former president Mahinda Rajapaksa " +
//                "who governed the country using executive powers sidelining the premier and going against democratic values. He made " +
//                "these remarks in response to a statement made by former President and Kurunegala District MP  that the" +
//                " country should be governed either by Mahinda Rajapaksa the President or the Prime Minister without contradicting each other. Addressing a " +
//                "media briefing at the SLFP headquarters, the Minister said the prime minister of the previous governments had been kept as " +
//                "a puppet by the executive president. “It was the Executive which dominated the country though the sovereignty of the " +
//                "government must be based on the executive, legislation and the judiciary. These three branches of governance should work " +
//                "together and equally,” the Minister said. He said the three branches of sovereignty operate independently under the ‘Yahapalanaya’ government today.";



        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        Producer<byte[], byte[]> producer = new KafkaProducer<byte[], byte[]>(props);


        tags = KeyWordExtractor.extract(message, tags);
        //System.out.println(tags);
        long start = System.nanoTime();
        int iret = 2;
        byte[] encoded = codec.encode(message.getBytes(), tags);
        producer.send(new ProducerRecord<byte[],byte[]>(getTopic(),encoded, encoded));
//        for(int i = 0; i < iret; i++){
//            byte[] encoded = codec.encode(message.getBytes(), tags);
//            producer.send(new ProducerRecord<byte[],byte[]>(getTopic(),encoded, encoded));
//
//            //FilterWrapper wrappedMsg = codec.decode(encoded);
//            //System.out.println(new String(wrappedMsg.getData()));
//        }
        long stop = System.nanoTime();
//        System.out.println("Decode & Filter: " + iret + " calls, time taken" +
//                " for each call:" + ((stop - start) * 1.0 / iret / 1000) + " micro secs");
//        System.out.println("Messages sent successfully");
        producer.close();
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
