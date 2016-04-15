package com.icloudmoo.business.msg.sender.push.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.business.msg.sender.ISender;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.MessageContent;
import com.icloudmoo.common.vo.MessageTarget;
import com.icloudmoo.common.vo.SendMessage;

/**
 * TODO:个推消息推送实现
 * 
 * @author guguihe
 * @Date 2015年10月20日 下午5:33:22
 */
public class GeTuiPushSender implements ISender {
    private static Logger logger = Logger.getLogger(GeTuiPushSender.class);

    // private static String appId = "cISkqDJv9M7X4BRMMBy3F8";
    // private static String appkey = "gmfCt6soA99yvRbm3GjwK8";
    // private static String master = "j2c6ietGxt5brjuyPpT7m4";
    // private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    // private static long offlineTime = 8640000;

    private static String appId = null;
    private static String appkey = null;
    private static String master = null;
    private static String host = null;
    private static long offlineTime = 86400000;

    /**
     * @description 单推
     * 
     * @param msg
     *            消息对象
     */
    public static void singlePush(SendMessage msg) {
        if (null == msg || CollectionUtils.isEmpty(msg.getTargets()) || null == msg.getContent()) {
            return;
        }

        try {
            boolean success = setPushConfig(msg.getSource());
            if (!success) {
                return;
            }
            IGtPush push = new IGtPush(host, appkey, master);
            boolean isConnected = push.connect();
            if (isConnected) {
                List<MessageTarget> targets = msg.getTargets();
                MessageTarget pushTo = targets.get(0);
                MessageContent msgContent = msg.getContent();
                ITemplate template = getTransmissionTemplate(msgContent);
                SingleMessage message = new SingleMessage();
                message.setOffline(true);
                message.setOfflineExpireTime(offlineTime);
                message.setData(template);
                Target target = new Target();
                target.setAppId(appId);
                String clientId = pushTo.getClientId();
                if (StringUtils.isEmpty(clientId)) {
                    return;
                }
                target.setClientId(clientId);
                IPushResult ret = null;
                switch (pushTo.getOs()) {
                    case SendMessage.ANDROID:
                        if (clientId.length() == 32) {
                            ret = push.pushMessageToSingle(message, target);
                        }
                        break;
                    case SendMessage.IOS:
                        if (clientId.length() == 64) {
                            ret = push.pushAPNMessageToSingle(appId, target.getClientId(), message);
                        }
                        break;
                    case SendMessage.WP:
                        break;
                    default:
                        break;
                }

                logger.info("使用“个推”推送消息结果：" + (ret == null ? "失败！！" : ret.getResponse().toString()));
            }
        }
        catch (Exception e) {
            logger.error("使用“个推”推送消息失败！", e);
        }
    }

    /**
     * TODO: 设置APPID推送配置
     * 
     * @param source
     */
    private static boolean setPushConfig(String source) {
        boolean success = Boolean.TRUE;
        if (StringUtils.equals(SendMessage.SOURCE_GHOME, source)) {
            appId = ConfigureUtil.getProperty("getui.ghome.appId");
            appkey = ConfigureUtil.getProperty("getui.ghome.appkey");
            master = ConfigureUtil.getProperty("getui.ghome.master");
            host = ConfigureUtil.getProperty("getui.ghome.host");
            offlineTime = ConfigureUtil.getLong("getui.ghome.offlineTime", 86400000);
        }
        else if (StringUtils.equals(SendMessage.SOURCE_GKEEPER, source)) {
            appId = ConfigureUtil.getProperty("getui.gkeeper.appId");
            appkey = ConfigureUtil.getProperty("getui.gkeeper.appkey");
            master = ConfigureUtil.getProperty("getui.gkeeper.master");
            host = ConfigureUtil.getProperty("getui.gkeeper.host");
            offlineTime = ConfigureUtil.getLong("getui.gkeeper.offlineTime", 86400000);
        }
        else {
            logger.error("未定义的推送源！！");
            success = Boolean.FALSE;
        }

        return success;
    }

    /**
     * @description 列表推送
     * 
     * @param msg
     *            消息
     */
    public static void listPush(SendMessage msg) {
        if (null == msg || CollectionUtils.isEmpty(msg.getTargets()) || null == msg.getContent()) {
            return;
        }

        try {
            System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
            boolean success = setPushConfig(msg.getSource());
            if (!success) {
                return;
            }
            final IGtPush push = new IGtPush(host, appkey, master);
            boolean isConnected = push.connect();
            if (isConnected) {
                List<MessageTarget> msgTargets = msg.getTargets();
                MessageContent msgContent = msg.getContent();
                ITemplate template = getTransmissionTemplate(msgContent);
                ListMessage message = new ListMessage();
                message.setData(template);
                message.setOffline(true);
                message.setOfflineExpireTime(offlineTime);
                Map<String, List<String>> clientMap = splitOsClientTarget(msgTargets);
                IPushResult ret = null;
                String taskId = null;
                for (Map.Entry<String, List<String>> entry : clientMap.entrySet()) {
                    switch (entry.getKey()) {
                        case SendMessage.ANDROID:
                            List<Target> targets = new ArrayList<Target>();
                            Target target = null;
                            for (String clientId : entry.getValue()) {
                                if (clientId.length() == 32) {
                                    target = new Target();
                                    target.setAppId(appId);
                                    target.setClientId(clientId);
                                    targets.add(target);
                                }
                            }
                            if (CollectionUtils.isNotEmpty(targets)) {
                                taskId = push.getContentId(message);
                                ret = push.pushMessageToList(taskId, targets);
                            }
                            break;
                        case SendMessage.IOS:
                            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                                taskId = push.getAPNContentId(appId, message);
                                ret = push.pushAPNMessageToList(appId, taskId, entry.getValue());
                            }
                            break;
                        case SendMessage.WP:
                            break;
                        default:
                            break;
                    }
                }

                logger.info("使用“个推”推送消息结果：" + (ret == null ? "失败！！" : ret.getResponse().toString()));
            }
        }
        catch (Exception e) {
            logger.error("使用“个推”推送消息失败！", e);
        }
    }

    /**
     * 标签推送
     * 
     * @param msg
     *            消息
     */
    public static void tagPush(SendMessage msg) {
        try {
            boolean success = setPushConfig(msg.getSource());
            if (!success) {
                return;
            }
            final IGtPush push = new IGtPush(host, appkey, master);
            boolean isConnected = push.connect();
            if (isConnected) {
                List<MessageTarget> msgTargets = msg.getTargets();
                MessageContent msgContent = msg.getContent();
                ITemplate template = getTransmissionTemplate(msgContent);
                AppMessage message = new AppMessage();
                message.setData(template);
                message.setOffline(true);
                message.setOfflineExpireTime(offlineTime);

                List<String> appIdList = new ArrayList<String>();
                List<String> phoneTypeList = new ArrayList<String>();
                List<String> tagList = new ArrayList<>();
                Map<String, List<String>> clientMap = splitOsClientTarget(msgTargets);
                for (Map.Entry<String, List<String>> entry : clientMap.entrySet()) {
                    tagList.addAll(entry.getValue());
                }

                appIdList.add(appId);
                phoneTypeList.add("ANDROID");
                phoneTypeList.add("IOS");

                message.setAppIdList(appIdList);
                message.setPhoneTypeList(phoneTypeList);
                message.setTagList(tagList);
                IPushResult ret = push.pushMessageToApp(message);
                logger.info("使用“个推”推送消息结果：" + ret.getResponse().toString());
            }
        }
        catch (Exception e) {
            logger.error("使用“个推”推送消息失败！", e);
        }
    }

    /**
     * @description 透传消息
     * 
     * @param content
     *            消息内容
     * @return
     * @throws Exception
     */
    public static TransmissionTemplate getTransmissionTemplate(MessageContent content) throws Exception {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTransmissionType(2);// 1:收到通知立即启动应用 2:收到通知不启动应用
        template.setTransmissionContent(content.toString());
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        // template.setPushInfo("", 1, "", "", "", "", "", "");

        // **********APN简单推送********//
        APNPayload apnpayload = new APNPayload();
        SimpleAlertMsg alertMsg = new SimpleAlertMsg(content.getTitle());
        apnpayload.setAlertMsg(alertMsg);
        apnpayload.addCustomMsg("msg", content.toString());
        apnpayload.setBadge(1);
        // apnpayload.setContentAvailable(1);
        // apnpayload.setCategory("ACTIONABLE");
        template.setAPNInfo(apnpayload);

        // ************APN高级推送*******************//
        // APNPayload apnpayload = new APNPayload();
        // apnpayload.setBadge(4);
        // apnpayload.setSound("test2.wav");
        // apnpayload.setContentAvailable(1);
        // apnpayload.setCategory("ACTIONABLE");
        // APNPayload.DictionaryAlertMsg alertMsg = new
        // APNPayload.DictionaryAlertMsg();
        // alertMsg.setBody("body");
        // alertMsg.setActionLocKey("ActionLockey");
        // alertMsg.setLocKey("LocKey");
        // alertMsg.addLocArg("loc-args");
        // alertMsg.setLaunchImage("launch-image");
        // // IOS8.2以上版本支持
        // alertMsg.setTitle("Title");
        // alertMsg.setTitleLocKey("TitleLocKey");
        // alertMsg.addTitleLocArg("TitleLocArg");
        //
        // apnpayload.setAlertMsg(alertMsg);
        // template.setAPNInfo(apnpayload);

        return template;
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
        if (null == message) {
            throw new MessageSendException("消息实体为空，无法推送！！");
        }

        switch (message.getPushType()) {
            case SendMessage.TYPE_SINGLE:
                singlePush(message);
                break;
            case SendMessage.TYPE_LIST:
                listPush(message);
                break;
            case SendMessage.TYPE_TAG:
                tagPush(message);
                break;
            default:
                logger.debug("No send type to be done!");
                break;
        }

        return true;
    }

    /**
     * TODO: 分离出各种操作系统的clientId
     * 
     * @param targets
     *            消息推送目标
     * @return
     */
    private static Map<String, List<String>> splitOsClientTarget(List<MessageTarget> targets) {
        Map<String, List<String>> clientMap = new HashMap<>(3);
        clientMap.put(SendMessage.ANDROID, new ArrayList<>());
        clientMap.put(SendMessage.IOS, new ArrayList<>());
        clientMap.put(SendMessage.WP, new ArrayList<>());
        clientMap.put(SendMessage.PAD, new ArrayList<>());
        clientMap.put(SendMessage.OTHER, new ArrayList<>());
        for (MessageTarget target : targets) {
            clientMap.get(target.getOs()).add(target.getClientId());
        }

        return clientMap;
    }

    public static void main(String[] args) {
        SendMessage msg = new SendMessage();
        MessageTarget target = new MessageTarget();
        target.setClientId("985639c9ea1a3414fdb932d71c748481cc58fec3783938377f3505af69a1480f");
        target.setOs(SendMessage.IOS);
        msg.setTargets(Arrays.asList(target));
        MessageContent content = new MessageContent("fuck", "shit", MessageContent.TYPE_YELLOWPAGE, "28");
        msg.setContent(content);
        msg.setPushType(SendMessage.TYPE_SINGLE);
        singlePush(msg);
    }
}
