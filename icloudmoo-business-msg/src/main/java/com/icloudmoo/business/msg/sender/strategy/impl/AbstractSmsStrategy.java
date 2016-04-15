/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * AbstractSmsStrategy.java
 * @Date 2015年10月23日 上午9:16:53
 * guguihe
 */
package com.icloudmoo.business.msg.sender.strategy.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.business.msg.sender.factory.SenderFactory;
import com.icloudmoo.business.msg.sender.strategy.ISmsChannelStrategy;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.SendMessage;

/**
 * TODO:
 * 
 * @author guguihe
 * @Date 2015年10月23日 上午9:16:53
 */
public abstract class AbstractSmsStrategy implements ISmsChannelStrategy {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 发送报告状态
     */
    protected Map<Integer, Integer> senderReports = new HashMap<>(3);

    /**
     * 所有可用的短信发送通道
     */
    protected Map<Integer, ISmsSender> senders = new HashMap<>(3);

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.msg.sender.strategy.ISmsChannelStrategy#getSender()
     */
    @Override
    public ISmsSender getSender() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.msg.sender.strategy.ISmsChannelStrategy#sendMessage(
     * com.gemdale.business.msg.vo.SendMessage)
     */
    @Override
    public boolean sendMessage(SendMessage msg) {
        boolean isSuccess = false;
        try {
            ISmsSender sender = getSender();
            if(null == sender){
                throw new RuntimeException("无法获取短信通道，请检查短信通道是否正常！");
            }
            msg.setChannel(getSenderChannel(sender.getSenderType()));
            isSuccess = sender.sendMessage(msg);
            // 通道发送不成功时，记录该通道不成功一次，发送成功时，重置记录次数
            int failCount = isSuccess ? 0 : (senderReports.get(sender.getSenderType()) + 1);
            // 连续五次发送不成功，则认为该通道已经失效
            if (failCount > 5) {
                setSenderError(sender.getSenderType());
                // 发送短信通知
                return isSuccess;
            }

            senderReports.put(sender.getSenderType(), failCount);
        }
        catch (MessageSendException e) {
            logger.error("短信发送失败！！", e);
        }

        return isSuccess;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.msg.sender.strategy.ISmsChannelStrategy#initSenders(
     * )
     */
    @Override
    public void initSenders() {
        // 短信通道
        Integer[] senderTypes = ConfigureUtil.getArray("sms.senders");
        for (Integer senderType : senderTypes) {
            addSender(senderType);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gemdale.business.msg.sender.strategy.ISmsChannelStrategy#
     * setSenderError(int)
     */
    @Override
    public void setSenderError(int senderType) {
        ISmsSender sender = senders.get(senderType);
        if (null != sender) {
            senders.remove(senderType);
            senderReports.remove(senderType);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.msg.sender.strategy.ISmsChannelStrategy#addSender(
     * int)
     */
    @Override
    public void addSender(int senderType) {
        ISmsSender sender = SenderFactory.getSmsSender(senderType);
        if (null != sender) {
            senders.put(senderType, sender);
            // 默认发送失败次数全部为零
            senderReports.put(senderType, 0);
        }
    }

    /**
     * TODO: 获取短信发送通道名称
     * 
     * @param senderType
     * @return
     */
    private String getSenderChannel(int senderType) {
        String senderChannel = StringUtils.EMPTY;
        switch (senderType) {
            case SenderFactory.SMS_CHUANGRUI:
                senderChannel = "创瑞短信平台";
                break;
            case SenderFactory.SMS_RONGLIAN:
                senderChannel = "容联云通讯短信平台";
                break;
            case SenderFactory.SMS_YIMEI:
                senderChannel = "亿美短信平台";
                break;
            default:
                break;
        }

        return senderChannel;
    }
}
