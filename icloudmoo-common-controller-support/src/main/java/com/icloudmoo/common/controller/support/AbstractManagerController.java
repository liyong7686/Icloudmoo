package com.icloudmoo.common.controller.support;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.icloudmoo.common.exception.FrameworkException;
import com.icloudmoo.common.vo.SystemUserVo;

/**
 * TODO:
 * 
 * @author liyong
 * @Date 2015年12月17日 下午5:49:18
 */
public class AbstractManagerController  implements BeanFactoryAware{
    protected Log logger = LogFactory.getLog(getClass());

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String MSG = "msg";
    public static final String DESKTOP_SESSION = "DESKTOP_SESSION";
    public static final String DATA_TAG = "data";
    public static final String DATA_COUNT = "count";
    public static final String TOTALCOUNT = "totalCount";
    public static final String UPDATE = "UPDATE";

    @Autowired
    protected HttpServletRequest servletRequest;


    @Override
    public void setBeanFactory(BeanFactory arg0) throws BeansException {
        
    }

    /**
     * 获取登录用户
     * 
     * @return
     */
    public SystemUserVo getLoginUser() throws FrameworkException {
        Object obj = servletRequest.getSession().getAttribute(DESKTOP_SESSION);
        if (null == obj) {
           // throw new FrameworkException("登录时间超时！！");
        }
        return (SystemUserVo) obj;
    }
}
