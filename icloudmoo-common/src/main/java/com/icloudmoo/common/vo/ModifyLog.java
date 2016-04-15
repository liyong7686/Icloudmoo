/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * ModifyLog.java
 * @Date 2015年12月18日 下午4:07:51
 * liyong
 */
package com.icloudmoo.common.vo;

/**
 * TODO:
 * 
 * @author liyong
 * @Date 2015年12月18日 下午4:07:51
 */
public class ModifyLog {
    private String modifyField;
    private Object beforeValue;
    private Object afterValue;
    /**
     * @return the modifyField
     */
    public String getModifyField() {
        return modifyField;
    }
    /**
     * @param modifyField the modifyField to set
     */
    public void setModifyField(String modifyField) {
        this.modifyField = modifyField;
    }
    /**
     * @return the beforeValue
     */
    public Object getBeforeValue() {
        return beforeValue;
    }
    /**
     * @param beforeValue the beforeValue to set
     */
    public void setBeforeValue(Object beforeValue) {
        this.beforeValue = beforeValue;
    }
    /**
     * @return the afterValue
     */
    public Object getAfterValue() {
        return afterValue;
    }
    /**
     * @param afterValue the afterValue to set
     */
    public void setAfterValue(Object afterValue) {
        this.afterValue = afterValue;
    }

    
}
