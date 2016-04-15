package com.icloudmoo.home.async.msg.center.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.stereotype.Component;

import com.icloudmoo.common.vo.ValueObject;
import com.icloudmoo.home.user.dao.vo.EmployeeInfoParam;

/**
 * TODO:
 * @author liyong
 * @Date 2015年12月18日 下午3:54:26
 */
@Component("employeeMessageListener")
public class EmployeeMessageListener extends AbstractListener implements MessageListener {


    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ValueObject object = ((ValueObject) (((ObjectMessage) message).getObject()));
                switch (object.getJmsType()) {
                    case "UserLogin":
                        afterUserLogin(object); //更新用户详情
                        break;

                    default:
                        break;
                }
            }
        }
        catch (Exception e) {
            logger.error("消息处理异常", e);
        }
        
    }

    //用户登录后异步更新用户详情
    private void afterUserLogin(ValueObject object) {
        try {
            //TODO
            EmployeeInfoParam param = (EmployeeInfoParam) object;
            
            logger.info("save afterUserLogin 用户登录后异步更新用户详情:" + param.toString());
            
        }
        catch (Exception e) {
            logger.error("更新用户详情失败！！", e);
        }
    }
    
}
