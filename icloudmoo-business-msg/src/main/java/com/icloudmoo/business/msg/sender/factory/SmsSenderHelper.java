package com.icloudmoo.business.msg.sender.factory;

import com.icloudmoo.business.msg.sender.strategy.ISmsChannelStrategy;
import com.icloudmoo.business.msg.sender.strategy.impl.SmsQueueStrategy;
import com.icloudmoo.business.msg.sender.strategy.impl.SmsRandomStrategy;
import com.icloudmoo.business.msg.sender.strategy.impl.SmsSequenceStrategy;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.SendMessage;

/**
 * TODO:使用短信通道切换策略发送短信
 * 
 * @author guguihe
 * @Date 2015年10月22日 上午11:12:22
 */
public class SmsSenderHelper {

    private ISmsChannelStrategy strategy;

    /**
     * TODO:静态司令部类，用来生成单例
     * 
     * @author guguihe
     * @Date 2015年10月22日 上午11:15:01
     */
    private static final class SmsSenderCreater {
        public static final SmsSenderHelper SENDER_HELPER = new SmsSenderHelper();
    }

    private SmsSenderHelper() {
        strategy = getSmsChannelStrategy();
        if (null == strategy) {
            throw new RuntimeException("无法发送短信：获取策略失败！！");
        }
    }

    /**
     * TODO: 获取单例的Helper对象
     * 
     * @return
     */
    public static SmsSenderHelper getHelper() {
        return SmsSenderCreater.SENDER_HELPER;
    }

    /**
     * TODO: 使用短信通道切换策略进行短信发送
     * 
     * @param message
     *            短信内容
     */
    public boolean sendSms(SendMessage message) {
        return strategy.sendMessage(message);
    }

    /**
     * TODO: 获取配置的短信通道切换策略
     * 
     * @return
     */
    private ISmsChannelStrategy getSmsChannelStrategy() {
        int strategyType = ConfigureUtil.getInt("sms.channel.strategy", 0);
        ISmsChannelStrategy strategy = null;
        switch (strategyType) {
            case ISmsChannelStrategy.QUEUE:
                strategy = new SmsQueueStrategy();
                break;
            case ISmsChannelStrategy.RANDOM:
                strategy = new SmsRandomStrategy();
                break;
            case ISmsChannelStrategy.SEQUENCE:
                strategy = new SmsSequenceStrategy();
                break;
            default:
                break;
        }

        return strategy;
    }

    public static void main(String[] args) {
        getHelper().sendSms(null);
    }
}
