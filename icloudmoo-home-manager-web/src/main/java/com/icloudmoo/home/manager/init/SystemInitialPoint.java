package com.icloudmoo.home.manager.init;

import javax.jms.Destination;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.icloudmoo.common.jms.suport.JmsUtil;
import com.icloudmoo.common.redis.suport.RedisUtil;


/**
 * TODO:系统初始化节点
 * @author liyong
 * @Date 2015年12月18日 上午11:43:31
 */
@Service("systemInitialPoint")
public class SystemInitialPoint implements InitializingBean{

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private Destination messageSenderDestination;
    
    @Autowired
    private Destination employeeDestination;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public void afterPropertiesSet() throws Exception {
       logger.info("初始化JMS服务。。。");
       JmsUtil.initJmsTemplate(jmsTemplate,messageSenderDestination,employeeDestination);
        
        logger.info("初始化Redis服务。。。");
        RedisUtil.initRedisTemplate(redisTemplate);
        
    }
    
    

}
