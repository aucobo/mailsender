package com.aucobo.worker.mailsender.config;


import com.aucobo.worker.mailsender.MailSenderApplication;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Fabian on 17.07.2017.
 */
public class Config {

    public static Properties props;
    public static String applicationName;
    public static String bootstrapServersConfig;
    public static String sourceTopic;
    public static String groupId;
    public static String clientId;
    public static String deserializer;
    public static String autoOffsetReset;
    public static String mailHost;
    public static String mailPort;
    public static String starttls;
    public static String mailFrom;
    public static String mailSubject;
    public static String mailTo;
    public static String username;
    public static String password;
    public static String auth;

    private static Logger logger = Logger.getLogger(Config.class);
    private static Config instance;


    private Config() {
        props = new Properties();
        try {
            props.load(MailSenderApplication.class.getClassLoader().getResourceAsStream("application.properties"));
            applicationName = props.getProperty("aucobo.kafka.application_name");
            bootstrapServersConfig = props.getProperty("aucobo.kafka.bootstrap_servers_config");
            sourceTopic = props.getProperty("aucobo.kafka.source_topic");
            groupId = props.getProperty("aucobo.kafka.group_id");
            clientId = props.getProperty("aucobo.kafka.client_id");
            deserializer = props.getProperty("aucobo.kafka.key_deserializer");
            autoOffsetReset = props.getProperty("aucobo.kafka.auto_offset_reset");
            mailHost = props.getProperty("mail.smtp.host");
            mailPort = props.getProperty("mail.smtp.port");
            starttls = props.getProperty("mail.smtp.starttls.enable");
            mailFrom = props.getProperty("mail.from");
            mailSubject = props.getProperty("mail.subject");
            mailTo = props.getProperty("mail.to");
            username = props.getProperty("mail.smtp.user");
            password = props.getProperty("mail.smtp.password");
            auth = props.getProperty("mail.smtp.auth");


            String propsString = "\n---- PROPERTIES ----\n";

            for (String s : props.stringPropertyNames()) {
                propsString = propsString + "[" + s + "] => " + props.getProperty(s) + "\n";
            }

            propsString = propsString + "-----------------------------";
            logger.info(propsString);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static Config getInstance() {
        if (null == Config.instance) {
            Config.instance = new Config();
        }
        return Config.instance;
    }
}
