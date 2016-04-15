/**
 * @Title: AbstractBusinessController.java
 * @Package com.gemdale.gmap.common.controller.support
 * @Description: TODO（用一句话描述这个文件的用途）
 * @Copyright: Copyright (c) 2015-2018 此代码属于金地物业信息管理部，在未经允许的情况下禁止复制传播
 * @Company:金地物业
 * @author 信息管理部-gengchong
 * @date 2015年8月19日 下午1:56:39
 */
package com.icloudmoo.common.controller.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.icloudmoo.common.controller.databind.RequestObject;
import com.icloudmoo.common.vo.ReturnBody;


/**
 * @ClassName: AbstractBusinessController
 * @Description: TODO（用一句话描述这个类的用途）
 * @author 信息管理部-gengchong
 * @date 2015年8月19日 下午1:56:39
 *
 */
public class AbstractBusinessController implements BeanFactoryAware {

    protected Log logger = LogFactory.getLog(getClass());

    /*
     * <p>Title: setBeanFactory</p>
     * <p>Description: </p>
     * 
     * @param arg0
     * 
     * @throws BeansException
     * 
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.
     * springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory arg0) throws BeansException {
        // TODO Auto-generated method stub

    }

    public <T> ReturnBody setFailBody(RequestObject<T> requestObj) {
        ReturnBody returnBody = new ReturnBody();
        returnBody.setCode(requestObj.getParseError().getCode());
        returnBody.setDesc(requestObj.getParseError().getDesc());
        return returnBody;
    }
}
