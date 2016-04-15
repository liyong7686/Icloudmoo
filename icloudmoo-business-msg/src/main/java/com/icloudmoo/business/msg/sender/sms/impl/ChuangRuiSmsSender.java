package com.icloudmoo.business.msg.sender.sms.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.business.msg.sender.factory.SenderFactory;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.Constants;
import com.icloudmoo.common.vo.SendMessage;

import ytx.org.apache.http.HttpResponse;
import ytx.org.apache.http.NameValuePair;
import ytx.org.apache.http.client.HttpClient;
import ytx.org.apache.http.client.entity.UrlEncodedFormEntity;
import ytx.org.apache.http.client.methods.HttpPost;
import ytx.org.apache.http.impl.client.DefaultHttpClient;
import ytx.org.apache.http.message.BasicNameValuePair;
import ytx.org.apache.http.util.EntityUtils;

/**
 * TODO:创瑞短信发送实现类
 * 
 * @author guguihe
 * @Date 2015年10月20日 下午5:30:46
 */
public class ChuangRuiSmsSender implements ISmsSender {

    private final Logger logger = Logger.getLogger(getClass());

    private final String serverUrl = ConfigureUtil.getProperty("sms.chuangrui.url");
    private final String userName = ConfigureUtil.getProperty("sms.chuangrui.username");
    private final String userPassword = ConfigureUtil.getProperty("sms.chuangrui.password");

    /**
     * 
     */
    public ChuangRuiSmsSender() {

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
            throw new MessageSendException("创瑞短信通道发送短信失败：消息为空！！");
        }

        logger.info("使用创瑞短信平台发送短信。");
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(serverUrl);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("name", userName));
        nvps.add(new BasicNameValuePair("pwd", userPassword));
        nvps.add(new BasicNameValuePair("mobile", message.getMobiles()));
        String content = "欢迎进入享家，您的验证码是@，如非本人操作，请忽略此短信。【智慧享联】";
        if (SendMessage.SMS_VALIDCODE.equals(message.getPushType())) {
            content = StringUtils.replace(content, "@", message.getContent().getContent());
        }
        nvps.add(new BasicNameValuePair("content", content));
        // 预定发送时间
        nvps.add(new BasicNameValuePair("stime", ""));
        // 公司签名
        nvps.add(new BasicNameValuePair("sign", ""));
        nvps.add(new BasicNameValuePair("type", "pt"));
        nvps.add(new BasicNameValuePair("extno", ""));
        boolean report = false;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, Constants.DEFAULT_CHARSET));
            HttpResponse response = httpclient.execute(post);
            String responseStr = EntityUtils.toString(response.getEntity(), Constants.DEFAULT_CHARSET);
            if (StringUtils.isEmpty(responseStr)) {
                throw new MessageSendException("没有返回结果！！");
            }

            String[] responseCodes = StringUtils.split(responseStr, ",");
            report = "0".equals(responseCodes[0]);
            if (report) {
                logger.info("创瑞短信平台发送短信：发送成功！");
            }
            else {
                logger.info("创瑞短信平台发送短信：发送失败！" + responseStr);
            }
        }
        catch (Exception e) {
            logger.error("创瑞短信平台发送短信失败！", e);
            throw new MessageSendException("创瑞短信平台发送短信失败！", e);
        }

        return report;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gemdale.business.sms.sender.ISmsSender#getSendReport()
     */
    @Override
    public void getSendReport() throws MessageSendException {
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
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gemdale.business.msg.sender.ISmsSender#getSenderType()
     */
    @Override
    public int getSenderType() {
        return SenderFactory.SMS_CHUANGRUI;
    }
}
