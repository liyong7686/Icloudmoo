package com.icloudmoo.business.msg.sender.sms.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.business.msg.sender.factory.SenderFactory;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.SendMessage;

/**
 * TODO:融联云通讯短信发送实现类
 * 
 * @author guguihe
 * @Date 2015年10月20日 下午5:29:55
 */
public class RonglianSmsSender implements ISmsSender {

    private final Logger logger = Logger.getLogger(getClass());

    private CCPRestSmsSDK restAPI = null;

    /**
     * 
     */
    public RonglianSmsSender() {
        restAPI = new CCPRestSmsSDK();
        // ******************************注释*********************************************
        // *初始化服务器地址和端口 *
        // *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
        // *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
        // *******************************************************************************
        restAPI.init(ConfigureUtil.getProperty("sms.ronglian.url"), ConfigureUtil.getProperty("sms.ronglian.port"));

        // ******************************注释*********************************************
        // *初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN *
        // *ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
        // *参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。 *
        // *******************************************************************************
        restAPI.setAccount(ConfigureUtil.getProperty("sms.ronglian.sid"),
                ConfigureUtil.getProperty("sms.ronglian.token"));

        // ******************************注释*********************************************
        // *初始化应用ID *
        // *测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID *
        // *应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
        // *******************************************************************************
        restAPI.setAppId(ConfigureUtil.getProperty("sms.ronglian.appid"));
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
            throw new MessageSendException("融联短信通道发送短信失败：消息为空！！");
        }
        
        logger.info("使用融联短信平台发送短信。");
        // ******************************注释****************************************************************
        // *调用发送模板短信的接口发送短信 *
        // *参数顺序说明： *
        // *第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号 *
        // *第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。 *
        // *系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
        // *第三个参数是要替换的内容数组。 *
        // **************************************************************************************************

        // **************************************举例说明***********************************************************************
        // *假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
        // *
        // *result = restAPI.sendTemplateSMS("13800000000","1" ,new
        // String[]{"6532","5"}); *
        // *则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入 *
        // *********************************************************************************************************************
        boolean result = true;
        switch (message.getPushType()) {
            case SendMessage.SMS_VALIDCODE:
                result = sendValidCodeSms(message);
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * TODO: 发送验证码类短信
     * 
     * @param message
     */
    private boolean sendValidCodeSms(SendMessage message) {
        Map<String, Object> result = restAPI.sendTemplateSMS(message.getMobiles(), "1",
                StringUtils.split(message.getContent().getContent(), "$$"));
        boolean resultFlag = "000000".equals(result.get("statusCode"));
        if (resultFlag) {
            // 正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                logger.info("融联短信通道短信发送报告：" + key + " = " + object);
            }
        }
        else {
            // 异常返回输出错误码和错误信息
            logger.info("融联短信通道错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }

        return resultFlag;
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
        return SenderFactory.SMS_RONGLIAN;
    }
}
