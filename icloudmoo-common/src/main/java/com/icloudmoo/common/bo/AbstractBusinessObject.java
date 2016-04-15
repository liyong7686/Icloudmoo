package com.icloudmoo.common.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.icloudmoo.common.exception.BusinessServiceException;

/**
 * @author gh
 * @date 2013-1-10
 * @version
 */
public abstract class AbstractBusinessObject
		implements BeanNameAware, DisposableBean, InitializingBean, BeanFactoryAware {
	protected Log logger = LogFactory.getLog(getClass());
	protected BeanFactory context;
	protected String name;

	public void setBeanName(String name) {
		this.name = name;
	}

	public void destroy() throws Exception {
	}

	/**
	 * Default implementation of InitializingBean
	 */
	public void afterPropertiesSet() throws BusinessServiceException {
		logger.debug("业务对象" + name + "初始化完成...");
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.context = beanFactory;
	}

}