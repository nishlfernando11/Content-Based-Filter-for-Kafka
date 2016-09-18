package main.java.com.filter.layer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Created with IntelliJ IDEA.
 * User: Irosha
 * Date: 9/1/16
 * Time: 7:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Filter {

    public KafkaConsumer FilterConsumer(String topic, boolean contentEnable);

}
