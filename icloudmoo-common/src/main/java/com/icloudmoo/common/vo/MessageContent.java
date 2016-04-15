/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * MessageContent.java
 * @Date 2015年12月18日 下午4:05:49
 * liyong
 */
package com.icloudmoo.common.vo;

import com.google.gson.Gson;

/**
 * TODO:
 * 
 * @author liyong
 * @Date 2015年12月18日 下午4:05:49
 */
public class MessageContent extends ValueObject{
 // 享家推荐类别
    public static final String TYPE_TOPIC = "01";
    public static final String TYPE_ANNO = "02";
    public static final String TYPE_COUPON = "03";
    public static final String TYPE_ACTIVITY = "04";
    public static final String TYPE_GHOME_USER = "05";
    public static final String TYPE_BILL = "06";
    public static final String TYPE_YELLOWPAGE = "07";
    public static final String TYPE_WORKORDER = "51";
    public static final String TYPE_REPORT = "09";
    public static final String TYPE_PAYMENT = "10";
    public static final String TYPE_ACTIVITY_LIST = "11";

    // 享当家推荐类别
    public static final String TYPE_GKEEPER_USER = "50";
    
    public static final String TYPE_OTHER = "99";

    /**
     * 
     */
    private static final long serialVersionUID = 7546376517434382859L;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息实体类型
     */
    private String type;

    /**
     * 消息关联对象ID
     */
    private String objectId;

    /**
     * 
     */
    public MessageContent() {
    }

    /**
     * 构造方法
     * 
     * @param title
     *            消息标题
     * @param content
     *            消息内容
     * @param type
     *            消息类型
     * @param objectId
     *            消息关联对象ID
     */
    public MessageContent(String title, String content, String type, String objectId) {
        setTitle(title);
        setContent(content);
        setType(type);
        setObjectId(objectId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gemdale.gmap.common.vo.ValueObject#toString()
     */
    @Override
    public String toString() {
        Gson gson = BeanInitUtils.getGson();
        return gson.toJson(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
