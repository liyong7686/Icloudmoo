package com.icloudmoo.common.jms.suport;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.icloudmoo.common.vo.ValueObject;

/**
 * TODO:消息推送
 * @author liyong
 * @Date 2015年12月18日 上午11:20:14
 */
public class JmsUtil {

    private static JmsTemplate jmsTemplate;
    
    /**
     * 员工消息通道
     */
    private static Destination employeeDestination;

    /**
     * 消息发送通道
     */
    private static Destination messageSenderDestination;

    /**
     * TODO: 初始化RedisTemplate和消息通道，第一个值为jmsTemplate，第二个值为userDestination，
     * 第三个值为messageSenderDestination，以后可以扩展
     */
    public static void initJmsTemplate(final JmsTemplate springJmsTemplate, final Destination... destination) {
        jmsTemplate = springJmsTemplate;
        if (destination.length > 0) {
            messageSenderDestination = destination[0];
        }
        
        if (destination.length > 1) {
            employeeDestination = destination[1];
        }
    }
    
    /**
     * 向用户消息队列中推送消息
     * 
     * @param object
     * @param destination
     * @throws JMSException
     */
    public static void pushToEmployeeQueue(final ValueObject object) throws JMSException {
        if (null == employeeDestination) {
            throw new JMSException("未初始化employeeDestination！！");
        }

        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(object);
                return message;
            }
        };
        jmsTemplate.send(employeeDestination, messageCreator);
    }
    
    /**
     * 向用户消息队列中推送消息
     * 
     * @param object
     * @param destination
     * @throws JMSException
     */
    public static void pushToWorkOrderQueue(JmsTemplate jmsTemplate,Destination workOrderDestination, final ValueObject object) throws JMSException {
        if (null == workOrderDestination) {
            throw new JMSException("未初始化workOrderDestination！！");
        }

        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(object);
                return message;
            }
        };
        jmsTemplate.send(workOrderDestination, messageCreator);
    }

    /**
     * 向消息发送队列中推送消息
     * 
     * @param object
     * @param destination
     * @throws JMSException
     */
    public static void pushToMsgSenderQueue(final ValueObject object) throws JMSException {
        if (null == messageSenderDestination) {
            throw new JMSException("未初始化messageSenderDestination！！");
        }

        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(object);
                return message;
            }
        };
        jmsTemplate.send(messageSenderDestination, messageCreator);
    }
    
    /**
     * 向消息发送队列中推送消息
     * 
     * @param object
     * @param destination
     * @throws JMSException
     */
    public static void pushToMsgSenderQueue(JmsTemplate jmsTemplate,Destination messageSenderDestination,final ValueObject object) throws JMSException {
        if (null == messageSenderDestination) {
            throw new JMSException("未初始化messageSenderDestination！！");
        }

        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(object);
                return message;
            }
        };
        jmsTemplate.send(messageSenderDestination, messageCreator);
    }
}
