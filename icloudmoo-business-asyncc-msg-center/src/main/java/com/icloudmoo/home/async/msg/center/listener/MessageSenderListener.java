package com.icloudmoo.home.async.msg.center.listener;

import java.util.Calendar;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.icloudmoo.business.msg.sender.factory.SenderFactory;
import com.icloudmoo.business.msg.sender.factory.SmsSenderHelper;
import com.icloudmoo.common.redis.suport.RedisUtil;
import com.icloudmoo.common.util.GbdDateUtils;
import com.icloudmoo.common.vo.SendMessage;
import com.icloudmoo.common.vo.ValueObject;
/**
 * TODO:
 * @author liyong
 * @Date 2015年12月18日 下午3:59:02
 */
@Component("messageSenderListener")
public class MessageSenderListener extends AbstractListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ValueObject object = ((ValueObject) (((ObjectMessage) message).getObject()));
                switch (object.getJmsType()) {
                    case "pushMessage":
                        pushMessage(object);
                        break;
                    case "sendSmsMessage":
                        sendSmsMessage(object);
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

    /**
     * TODO:推送消息
     * @param object
     */
    private void pushMessage(ValueObject object) {
        SendMessage message = (SendMessage) object;
        if (null == message.getContent() || CollectionUtils.isEmpty(message.getTargets())) {
            logger.error("推送消息失败，必要参数不完全！");
            return;
        }
        try {
            logger.error("推送消息 start ！");
            SenderFactory.getPushSender(SenderFactory.PUSH_GETUI).sendMessage(message);
            //TODO 保存推送的版本信息
            logger.info("save11 推送消息成功" + message.getContent());
        }
        catch (Exception e) {
            logger.error("推送消息失败！", e);
        }
    }
    
    /**
     * TODO: 推送消息接口
     * @param object
     */
    private void sendSmsMessage(ValueObject object) {
        SendMessage message = (SendMessage) object;
        if (null == message.getContent() || StringUtils.isEmpty(message.getMobiles())) {
            logger.error("推送消息失败，必要参数不完全！");
            return;
        }

        //发送短信方法
        boolean isSuccess = SmsSenderHelper.getHelper().sendSms(message);
        if (StringUtils.equals(SendMessage.SMS_VALIDCODE, message.getPushType()) && checkSendLimit(message)) {
            return;
        }
        logger.info("接收到信息,并保存短信消息成功！" + message.getContent());//不管是失败还是陈功都保存
       
    }

    /**
     * TODO: 检查短信通道发送限制
     * 1、1个手机1分钟不得超过1次验证码。
     * 2、1个手机1小时不得超过3次验证码。
     * 3、1个手机24小时不得超过6次验证码
     * 
     * @param message
     * @return
     */
    private boolean checkSendLimit(SendMessage message) {
        // 一个手机一分钟内不能发送超过一次验证码
        String minute = GbdDateUtils.format(Calendar.getInstance().getTime(), "yyyyMMddHHmm");
        Integer minutCount = RedisUtil.redisQueryObject(message.getMobiles() + message.getChannel() + minute);
        if (null == minutCount) {
            minutCount = 1;
        }

        if (minutCount > 2) {
            logger.error("同一个手机在一分钟内不能发送超过一次短信验证码！");
            return Boolean.TRUE;
        }

        minutCount += 1;
        RedisUtil.redisSaveObject(message.getMobiles() + message.getChannel() + minute, minutCount, 1);

        // 一个手机一小时内不能发送超过3次验证码
        String hours = GbdDateUtils.format(Calendar.getInstance().getTime(), "yyyyMMddHH");
        Integer hoursCount = RedisUtil.redisQueryObject(message.getMobiles() + message.getChannel() + hours);
        if (null == hoursCount) {
            hoursCount = 1;
        }

        if (hoursCount > 4) {
            logger.error("同一个手机一小时内不能发送超过3次验证码");
            return Boolean.TRUE;
        }

        hoursCount += 1;
        RedisUtil.redisSaveObject(message.getMobiles() + message.getChannel() + hours, hoursCount, 1 * 60);

        // 一个手机一天内不能发送超过6次验证码
        String date = GbdDateUtils.format(Calendar.getInstance().getTime(), "yyyyMMdd");
        Integer dateCount = RedisUtil.redisQueryObject(message.getMobiles() + message.getChannel() + date);
        if (null == dateCount) {
            dateCount = 1;
        }

        if (dateCount > 7) {
            logger.error("同一个手机24小时内不能发送超7次验证码");
            return Boolean.TRUE;
        }

        dateCount += 1;
        RedisUtil.redisSaveObject(message.getMobiles() + message.getChannel() + date, dateCount, 24 * 60);

        return Boolean.FALSE;
    }
}
