package com.icloudmoo.business.file.upload;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO:图片服务器，  启动类     
 * @author liyong  
 * @Date 2015年12月22日 下午2:11:35
 */
public class Transfer {
    private static Logger logger = Logger.getLogger(Transfer.class);
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        // 进行spring的系统初始化化工作,进行必要的数据初始化
       new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("图片传输模块启动成功！");
    }
}
