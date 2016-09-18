package main.java.com.filter.clients;

//import util.properties packages

import main.java.com.filter.Coder;
import main.java.com.filter.extract.KeyWordExtractor;
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
//        put("entity", "true");
//        put("type", "true");
//        put("long", "1283102390123");
    }};

    private static Coder codec = new Coder();

    public static void main(String[] args) throws Exception{


        String txt = "SLFP Minister Wijith Wijayamuni Zoysa said yesterday that it was the former president Mahinda Rajapaksa " +
                "who governed the country using executive powers sidelining the premier and going against democratic values. He made " +
                "these remarks in response to a statement made by former President and Kurunegala District MP Mahinda Rajapaksa that the" +
                " country should be governed either by the President or the Prime Minister without contradicting each other. Addressing a " +
                "media briefing at the SLFP headquarters, the Minister said the prime minister of the previous governments had been kept as " +
                "a puppet by the executive president. “It was the Executive which dominated the country though the sovereignty of the " +
                "government must be based on the executive, legislation and the judiciary. These three branches of governance should work " +
                "together and equally,” the Minister said. He said the three branches of sovereignty operate independently under the ‘Yahapalanaya’ government today.";


        tags = KeyWordExtractor.extract(txt, tags);

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


        Producer<byte[], byte[]> producer = new KafkaProducer<byte[], byte[]>(props);
        System.out.println(tags);
        for(int i = 0; i < 2; i++){
            byte[] encoded = codec.encode(txt.getBytes(), tags);
            producer.send(new ProducerRecord<byte[],byte[]>(topicName,encoded, encoded));

            //FilterWrapper wrappedMsg = codec.decode(encoded);
            //System.out.println(new String(wrappedMsg.getData()));
        }
        System.out.println("Messages sent successfully");
        producer.close();
    }
}
