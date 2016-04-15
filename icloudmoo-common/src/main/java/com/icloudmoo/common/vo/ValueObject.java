/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * ValueObject.java
 * @Date 2015年12月18日 上午11:22:41
 * liyong
 */
package com.icloudmoo.common.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * TODO:
 * 
 * @author liyong
 * @Date 2015年12月18日 上午11:22:41
 */
public class ValueObject implements Serializable, Cloneable{
    /**
     * 默认的序列化ID
     */
    private static final long serialVersionUID = 1L;
    
    public String jmsType;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.DEFAULT_STYLE);
    }

    public String getJmsType() {
        return jmsType;
    }

    public void setJmsType(String jmsType) {
        this.jmsType = jmsType;
    }
}
