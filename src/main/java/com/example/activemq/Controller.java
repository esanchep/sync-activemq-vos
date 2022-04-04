package com.example.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@RestController
@RequestMapping
public class Controller {

    @GetMapping
    String test() throws JMSException {
        JmsTemplate jmsTemplate = initialize();

        jmsTemplate.send("to_kts", session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("test");
            return textMessage;
        });

        Message message = jmsTemplate.receive("from_kts");
        return ((TextMessage) message).getText();
    }

    private JmsTemplate initialize() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(false);
        jmsTemplate.setReceiveTimeout(5000);
        jmsTemplate.setDeliveryPersistent(false);
        return jmsTemplate;
    }

}
