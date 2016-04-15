
package com.icloudmoo.home.user.dao.vo;

import com.icloudmoo.common.vo.ValueObject;

/**
 * TODO:
 * 
 * @author gengchong
 * @date 2015年11月23日 上午10:16:48
 */
public class EmployeeInfoParam extends ValueObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer employeeId;
    private String mobile;
    private String content;
    private String appVersion;
    private String clientId;
    private String deviceToken;
    private String sessionId;
    private String mobileOs;
    private String mobileVersion;
    private String channel;

    private String userIp;
    private Float latitude;
    private Float longitude;

    /**
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId
     *            the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion
     *            the appVersion to set
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId
     *            the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * @param deviceToken
     *            the deviceToken to set
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     *            the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the mobileOs
     */
    public String getMobileOs() {
        return mobileOs;
    }

    /**
     * @param mobileOs
     *            the mobileOs to set
     */
    public void setMobileOs(String mobileOs) {
        this.mobileOs = mobileOs;
    }

    /**
     * @return the mobileVersion
     */
    public String getMobileVersion() {
        return mobileVersion;
    }

    /**
     * @param mobileVersion
     *            the mobileVersion to set
     */
    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the userIp
     */
    public String getUserIp() {
        return userIp;
    }

    /**
     * @param userIp
     *            the userIp to set
     */
    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    /**
     * @return the latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

}
