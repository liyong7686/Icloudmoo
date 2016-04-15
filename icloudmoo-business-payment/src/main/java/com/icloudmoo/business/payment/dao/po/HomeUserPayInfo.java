package com.icloudmoo.business.payment.dao.po;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.icloudmoo.common.vo.ValueObject;

/**
 * TODO:
 * @author CodeCreator
 * @Date 2015-11-25 14:54:49
 */
@Table(name = "ghome.ghome_user_pay_info")
public class HomeUserPayInfo extends ValueObject {
    @Transient
    public static final String BILLTYPE_USER = "01";
    @Transient
    public static final String BILLTYPE_HOUSE = "02";

    @Transient
    public static final String WXPAY = "02";
    @Transient
    public static final String ALIPAY = "01";

    @Transient
    public static final String STATUS_UNPAY = "01";
    @Transient
    public static final String STATUS_SUCCESS = "02";
    @Transient
    public static final String STATUS_FAIL = "03";
    @Transient
    private static final long serialVersionUID = 2426663085L;

    /**
     * 主键
     */
    @Id
    private java.lang.Integer payId;

    /**
     * 订单类型，01：客户订单，02：房屋订单
     */
    @Transient
    private String billType;

    /**
     * 支付订单号
     */
    private java.lang.String orderNo;

    /**
     * 订单id
     */
    private java.lang.Integer billId;

    /**
     * 用户id
     */
    private java.lang.Integer userId;
    
    private Integer userCouponId;
    
    /**
     * 优惠券ID
     */
    private java.lang.Integer couponId;

    /**
     * 支付金额
     */
    private java.lang.Float amount;

    /**
     * 原始金额
     */
    private java.lang.Float originalAmount;

    /**
     * 优惠券优惠金额
     */
    private java.lang.Float couponAmount;

    /**
     * 支付渠道 01:支付宝 02:微信
     */
    private java.lang.String channel;
    
    /**
     * 业务类型
     */
    @Transient
    private String objectType;
    
    @Transient
    private Integer objectId;

    /**
     * 渠道订单号
     */
    private java.lang.String channelOrderNo;

    /**
     * 状态 01：待支付 02：支付成功 03：支付失败
     */
    private java.lang.String status;
    
    /**
     * 状态 01：待支付 02：支付成功 03：支付失败
     */
    private String appStatus;

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    /**
     * 描述信息
     */
    private java.lang.String description;

    /**
     * 创建时间
     */
    private java.util.Date createDate;

    /**
     * 修改时间
     */
    private java.util.Date modifyDate;

    public java.lang.Float getAmount() {
        return amount;
    }

    public void setAmount(java.lang.Float amount) {
        this.amount = amount;
    }

    public java.lang.String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(java.lang.String orderNo) {
        this.orderNo = orderNo;
    }

    public java.util.Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(java.util.Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public java.lang.Integer getBillId() {
        return billId;
    }

    public void setBillId(java.lang.Integer billId) {
        this.billId = billId;
    }

    public java.lang.String getChannel() {
        return channel;
    }

    public void setChannel(java.lang.String channel) {
        this.channel = channel;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public java.lang.Float getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(java.lang.Float originalAmount) {
        this.originalAmount = originalAmount;
    }

    public java.lang.Float getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(java.lang.Float couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(java.lang.String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public java.lang.Integer getPayId() {
        return payId;
    }

    public java.lang.Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(java.lang.Integer couponId) {
        this.couponId = couponId;
    }

    public void setPayId(java.lang.Integer payId) {
        this.payId = payId;
    }

    public java.lang.Integer getUserId() {
        return userId;
    }

    public void setUserId(java.lang.Integer userId) {
        this.userId = userId;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public Integer getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(Integer userCouponId) {
        this.userCouponId = userCouponId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public java.util.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
}