/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageController;

import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Lukas
 */
public class JmsAmqManager implements MessageListener{
    private final String ACTIVEMQ_URI = "tcp://localhost:61616";
    private ActiveMQConnectionFactory cnFactory;
    private Destination destination;
    private Session session;
    private Connection connection;
    private MessageConsumer consumer;
    
    private void connect(String queue) throws JMSException{
        if(cnFactory ==null){
            cnFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URI);
            connection = cnFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queue);
            consumer = session.createConsumer(destination);
        }
    }
    
    public void close() throws JMSException{
        if(session != null) session.close();
        if(connection != null) connection.close();
        if(consumer != null) consumer.close();
        cnFactory = null;
    }

    public void produce(String queue, String msg) throws JMSException, Exception {
        //Skapa uppkoppling
        connect(queue);
        //Skapa producer-objekt för att skicka meddelande
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        try{
            //Skapa meddelande
            TextMessage message = session.createTextMessage(msg);
            //Skicka meddelande
            producer.send(message);    
        }finally{
            producer.close();
            close();
        }
    }
    
    public String consume (String queue) throws JMSException{
         connect(queue);
     try{
         TextMessage message = (TextMessage) consumer.receive(1000);
         if (message != null)
             return message.getText();
     }finally{
        close();
    } 
       return "Finns inga meddelanden i kön";
    }
    
    public void autoConsume(String queue) throws JMSException{
        //Koppla upp    
        connect(queue);
        //Starta lyssnare på tråd med detta objekt
        consumer.setMessageListener(this);
    }
    
    @Override
    public void onMessage(Message msg){
        try{
            System.out.println(((TextMessage)msg).getText());
            //För att det inte ska gå så fort
            TimeUnit.MILLISECONDS.sleep(1500);
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
