package com.aucobo.worker.mailsender.service;

import com.aucobo.worker.mailsender.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * Created by Fabian on 17.07.2017.
 */
public class MailSender {

    private static Logger logger = Logger.getLogger(MailSender.class);
    private static MailSender instance;
    private static Config config;


    private MailSender() {
        config = Config.getInstance();

    }

    public static MailSender getInstance() {
        if (null == MailSender.instance) {
            MailSender.instance = new MailSender();
        }
        return MailSender.instance;
    }


    public static void send(String content) throws MessagingException {
        Properties props = config.props;
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.username, config.password);
            }
        });


        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(config.username));
        email.addRecipients(Message.RecipientType.TO, InternetAddress.parse(config.mailTo));
        email.setSubject(config.mailSubject);
        email.setText(prettyJson(content));

        Transport.send(email);
        logger.info("SENT EMAIL.");
    }

    private static String prettyJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        return gson.toJson(je);
    }
}
