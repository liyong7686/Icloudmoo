package com.icloudmoo.common.vo;

import java.util.List;


/**
 * TODO:
 * 
 * @author liyong
 * @Date 2015年12月18日 下午4:02:20
 */
public class SendMessage extends ValueObject {
    public static final String JMSTYPE_SMS = "sendSmsMessage";
    public static final String JMSTYPE_PUSH = "pushMessage";
    
    public static final String ANDROID = "01";
    public static final String IOS = "02";
    public static final String PAD = "03";
    public static final String WP = "04";
    public static final String OTHER = "99";

    public static final String TYPE_SINGLE = "single";
    public static final String TYPE_LIST = "list";
    public static final String TYPE_TAG = "tag";

    public static final String SMS_VALIDCODE = "ValidCode";
    public static final String SMS_NOTICE = "notice";

    public static final String SOURCE_GHOME = "GHOME";
    public static final String SOURCE_GKEEPER = "GKEEPER";

    /**
     * 
     */
    private static final long serialVersionUID = 8978690986830333197L;

    private String pushType;
    private MessageContent content;
    private String channel;
    private String source;
    private List<MessageTarget> targets;
    private String mobiles;

    /**
     * 
     */
    public SendMessage() {
    }

    /**
     * 构造器
     * 
     * @param jmsType  消息发送类型
     * @param content  推送内容
     * @param targets  推送目标
     * @param pushType 推送方式
     * @param source   推送源
     */
    public SendMessage(String jmsType, MessageContent content, List<MessageTarget> targets, String pushType,
            String source) {
        setJmsType(jmsType);
        setContent(content);
        setSource(source);
        setPushType(pushType);
        setTargets(targets);
    }

    /**
     * 构造器
     * 
     * @param jmsType 消息发送类型
     * @param content 推送内容
     * @param targets  推送目标
     * @param pushType  推送方式
     * @param source  推送源
     */
    public SendMessage(String jmsType, MessageContent content, String mobiles, String pushType) {
        setJmsType(jmsType);
        setContent(content);
        setMobiles(mobiles);
        setPushType(pushType);
    }

    public MessageContent getContent() {
        return content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<MessageTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<MessageTarget> targets) {
        this.targets = targets;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

}
