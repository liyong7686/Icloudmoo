package com.icloudmoo.business.msg.sender.factory;

import org.apache.log4j.Logger;

import com.icloudmoo.business.msg.sender.ISender;
import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.business.msg.sender.push.impl.GeTuiPushSender;
import com.icloudmoo.business.msg.sender.sms.impl.ChuangRuiSmsSender;
import com.icloudmoo.business.msg.sender.sms.impl.RonglianSmsSender;
import com.icloudmoo.business.msg.sender.sms.impl.YiMeiSmsSender;

/**
 * TODO:消息发送工厂类
 * 
 * @author guguihe
 * @Date 2015年10月20日 下午5:36:53
 */
public class SenderFactory {

    /**
     * 个推推送服务
     */
    public static final int PUSH_GETUI = 1;

    /**
     * 融联短信服务
     */
    public static final int SMS_RONGLIAN = 1;

    /**
     * 亿美软通短信服务
     */
    public static final int SMS_YIMEI = 2;

    /**
     * 创瑞短信服务
     */
    public static final int SMS_CHUANGRUI = 3;

    private static final Logger logger = Logger.getLogger(SenderFactory.class);

    /**
     * TODO: 获取推送服务
     * 
     * @param sender
     * @return
     */
    public static ISender getPushSender(int sender) {
        ISender pushSender = null;
        switch (sender) {
            case PUSH_GETUI:
                pushSender = new GeTuiPushSender();
                break;
            default:
                break;
        }

        return pushSender;
    }

    /**
     * TODO: 获取短信发送服务
     * 
     * @param sender
     * @return
     */
    public static ISmsSender getSmsSender(int sender) {
        ISmsSender smsSender = null;
        switch (sender) {
            case SMS_RONGLIAN:
                smsSender = new RonglianSmsSender();
                break;
            case SMS_YIMEI:
                try {
                    smsSender = new YiMeiSmsSender();
                }
                catch (Exception e) {
                    logger.error(e);
                }
                break;
            case SMS_CHUANGRUI:
                smsSender = new ChuangRuiSmsSender();
                break;
            default:
                break;
        }

        return smsSender;
    }
}
