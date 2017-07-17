package com.aucobo.worker.mailsender;

import com.aucobo.worker.mailsender.config.Config;
import com.aucobo.worker.mailsender.service.MailSender;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Properties;


/**
 * Created by Fabian on 17.07.2017.
 */
public class MailSenderApplication {

    private static Logger logger = Logger.getLogger(MailSenderApplication.class);

    public static void main(String[] args) throws MessagingException {

        Config config = Config.getInstance();
        MailSender sender = MailSender.getInstance();

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServersConfig);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.groupId);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, config.clientId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.deserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.deserializer);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(config.sourceTopic));

        boolean running = true;
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                logger.info(record.offset() + " -> " + record.value());
                sender.send(record.value());
            }
        }
    }
}
