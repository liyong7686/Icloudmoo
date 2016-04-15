package com.icloudmoo.home.async.msg.center;

import javax.jms.Destination;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;

import com.icloudmoo.common.jms.suport.JmsUtil;
import com.icloudmoo.common.redis.suport.RedisUtil;
/**
 * TODO:
 * @author liyong
 * @Date 2015年12月18日 下午3:26:36
 */
public class Start {
    
    private static Logger logger = Logger.getLogger(Start.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        // 进行spring的系统初始化化工作,进行必要的数据初始化
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // 获取接口,并提供服务
        // 初始化REDIS服务
        logger.info("初始化REDIS服务...");
        RedisUtil.initRedisTemplate((RedisTemplate) context.getBean("redisTemplate"));
        // 初始化JMS
        logger.info("初始化JMS服务...");
        JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
        Destination messageSenderDestination = (Destination) context.getBean("messageSenderDestination");
        Destination employeeDestination = (Destination) context.getBean("employeeDestination");
        JmsUtil.initJmsTemplate(jmsTemplate,messageSenderDestination, employeeDestination);
        logger.info("app异步处理平台启动成功！");
    }
}
