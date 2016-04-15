/**
 * copywrite 2013-2018 万科物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 */
package com.icloudmoo.common.vo;

import java.util.List;

/**
 * @description TODO
 * @author gengchong
 * @date 2013-9-6
 */
public class ParameterInfo extends ValueObject {

    private static final long serialVersionUID = 1L;

    private Integer count = 0;
    private String type;
    private Integer pageSize = 20;
    private Integer pageNo = 1;
    private String status;
    private String account;
    private String name;
    private String content;
    private String keyWord;
    private String mobile;
    private List<String> dataRightList;
    private String dataRight;
    private String region;
    private String orgKey;
    private String company;
    private String city;
    private String project;
    private String fromDate;
    private String toDate;
    private Integer parentId;
    private String parentCode;
    private Integer projectId;
    private String projectCode;
    private String skillCode;
    private Integer id;
    private Float longitude;
    private Float latitude;
    private Float distance;
    private String code;
    private int activityId;
    private Integer userId;
    private String userType;
    private Integer reportUserId;

    private String buildingCode;// 楼栋编码
    private Integer workOrderId;
    private String ids;
    private Float amount;
    /**
     * 房屋编码
     */
    private String houseId;
    private String houseCode;

    private String cityCode;//城市编码
    /**
     * 主题Id
     */
    private String topicId;

    /**
     * 业务类型 : GHOME:享家APP消息 GKEEPER:享当家APP消息
     */
    private String businessType;
    private String msgId;
    private Integer objectId;

    private String fileExcelPath; // 保存excelFile的路径

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getCount() {
        return count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getDataRightList() {
        return dataRightList;
    }

    public void setDataRightList(List<String> dataRightList) {
        this.dataRightList = dataRightList;
    }

    public String getDataRight() {
        return dataRight;
    }

    public void setDataRight(String dataRight) {
        this.dataRight = dataRight;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOrgKey() {
        return orgKey;
    }

    public void setOrgKey(String orgKey) {
        this.orgKey = orgKey;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the houseCode
     */
    public String getHouseCode() {
        return houseCode;
    }

    /**
     * @param houseCode
     *            the houseCode to set
     */
    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    /**
     * @return the businessType
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * @param businessType
     *            the businessType to set
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @return the msgId
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * @param msgId
     *            the msgId to set
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType
     *            the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the buildingCode
     */
    public String getBuildingCode() {
        return buildingCode;
    }

    /**
     * @param buildingCode
     *            the buildingCode to set
     */
    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    /**
     * @return the houseId
     */
    public String getHouseId() {
        return houseId;
    }

    /**
     * @param houseId
     *            the houseId to set
     */
    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    /**
     * @return the fileExcelPath
     */
    public String getFileExcelPath() {
        return fileExcelPath;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return the skillCode
     */
    public String getSkillCode() {
        return skillCode;
    }

    /**
     * @param skillCode
     *            the skillCode to set
     */
    public void setSkillCode(String skillCode) {
        this.skillCode = skillCode;
    }

    public void setFileExcelPath(String fileExcelPath) {
        this.fileExcelPath = fileExcelPath;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    /**
     * @return the reportUserId
     */
    public Integer getReportUserId() {
        return reportUserId;
    }

    /**
     * @param reportUserId the reportUserId to set
     */
    public void setReportUserId(Integer reportUserId) {
        this.reportUserId = reportUserId;
    }

    /**
     * @return the cityCode
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * @param cityCode the cityCode to set
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * @return the ids
     */
    public String getIds() {
        return ids;
    }

    /**
     * @param ids the ids to set
     */
    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * @return the objectId
     */
    public Integer getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    /**
     * @return the amount
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    
}
