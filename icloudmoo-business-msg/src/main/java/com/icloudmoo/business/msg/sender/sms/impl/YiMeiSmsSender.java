package com.icloudmoo.business.msg.sender.sms.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.business.msg.sender.factory.SenderFactory;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.MessageContent;
import com.icloudmoo.common.vo.SendMessage;

import cn.emay.sdk.client.api.Client;
import cn.emay.sdk.client.api.MO;
import cn.emay.sdk.client.api.StatusReport;

/**
 * TODO:亿美软通短信发送实现类
 * 
 * @author guguihe
 * @Date 2015年10月20日 下午5:31:41
 */
public class YiMeiSmsSender implements ISmsSender {

    private final Logger logger = Logger.getLogger(getClass());

    private Client client = null;

    /**
     * @throws Exception
     * 
     */
    public YiMeiSmsSender() throws Exception {
        client = new Client(ConfigureUtil.getProperty("sms.yimei.serialno"),
                ConfigureUtil.getProperty("sms.yimei.key"));
        // 注册系列号
        client.registEx(ConfigureUtil.getProperty("sms.yimei.password"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.sms.sender.ISender#sendMessage(com.gemdale.business.
     * sms.vo.SendMessage)
     */
    @Override
    public boolean sendMessage(SendMessage message) throws MessageSendException {
        if (null == message || null == message.getContent() || StringUtils.isEmpty(message.getMobiles())) {
            throw new MessageSendException("亿美软通短信通道发送短信失败：消息为空！！");
        }
        
        logger.info("使用亿美短信平台发送短信。");
        String[] mobiles = StringUtils.split(message.getMobiles(), ",");
        int report = client.sendSMS(mobiles, message.getContent().getContent(), 1);
        return 0 == report;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gemdale.business.sms.sender.ISmsSender#getSendReport()
     */
    @Override
    public void getSendReport() throws MessageSendException {
        try {
            List<StatusReport> list = client.getReport();
            if (!CollectionUtils.isEmpty(list)) {
                for (StatusReport report : list) {
                    logger.info("亿美软通短信平台短信发送报告：" + report.getMobile() + ":" + report.getReportStatus());
                }
            }
            // TODO 保存到数据库或者显示到控制台
        }
        catch (Exception e) {
            logger.error("亿美软通短信平台短信发送报告获取失败！", e);
            throw new MessageSendException("亿美软通短信平台短信发送报告获取失败！", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.sms.sender.ISmsSender#getSmsReceipt(com.gemdale.
     * business.sms.vo.SendMessage)
     */
    @Override
    public List<SendMessage> getSmsReceipt(SendMessage message) throws MessageSendException {
        try {
            List<MO> list = client.getMO();
            List<SendMessage> msgs = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list)) {
                SendMessage temp = null;
                for (MO mo : list) {
                    temp = new SendMessage();
                    MessageContent content = new MessageContent();
                    content.setContent(mo.getSmsContent());
                    temp.setContent(content);
                    temp.setMobiles(mo.getMobileNumber());
                    msgs.add(temp);
                }
            }

            return msgs;
        }
        catch (Exception e) {
            logger.error("亿美软通短信平台获取短信上行失败！", e);
            throw new MessageSendException("亿美软通短信平台获取短信上行失败！", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gemdale.business.msg.sender.ISmsSender#getSenderType()
     */
    @Override
    public int getSenderType() {
        return SenderFactory.SMS_YIMEI;
    }
}
