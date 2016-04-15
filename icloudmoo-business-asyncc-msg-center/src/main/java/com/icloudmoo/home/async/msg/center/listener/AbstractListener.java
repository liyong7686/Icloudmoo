package com.icloudmoo.home.async.msg.center.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * TODO:
 * @author liyong
 * @Date 2015年12月18日 下午3:51:47
 */
public class AbstractListener implements InitializingBean{
    
    protected Log logger = LogFactory.getLog(getClass());   

    @Override
    public void afterPropertiesSet() throws Exception {
       
    }
}
