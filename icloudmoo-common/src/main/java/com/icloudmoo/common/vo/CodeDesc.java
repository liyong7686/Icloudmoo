/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * ValidStatus.java
 * @Date 2015年10月15日 下午5:17:46
 * guguihe
 */
package com.icloudmoo.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO: 数据验证状态
 * 
 * @author guguihe
 * @Date 2015年10月15日 下午5:17:46
 */
public class CodeDesc extends ValueObject {

    /**
     * 
     */
    private static final long serialVersionUID = -9043254667494875088L;

    private int code;

    private String desc;
    
    private String name;
    
    private String businessCode;
    
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getCode() {
        return code;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setValues(int code, String desc) {
        this.setCode(code);
        this.setDesc(desc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringBuilder.getDefaultStyle());
    }
}
