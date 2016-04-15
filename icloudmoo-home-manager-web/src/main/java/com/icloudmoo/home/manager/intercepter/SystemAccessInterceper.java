package com.icloudmoo.home.manager.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.icloudmoo.common.util.ConfigureUtil;

/**
 * TODO:
 * @author liyong
 * @Date 2015年12月18日 上午11:39:45
 */
@Repository
public class SystemAccessInterceper extends HandlerInterceptorAdapter {
    private final Logger logger = Logger.getLogger(getClass());
    
    //配置不需要验证的地方
    private String[] noFilters = ConfigureUtil.getStringArray("no_filter_setting");

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        boolean isFilter = true;
        if (!ArrayUtils.isEmpty(noFilters)) {
            for (String u : noFilters) {
                if (uri.contains(u)) {
                    isFilter = false;
                    break;
                }
            }
        }
        else {
            isFilter = false;
        }

        if (isFilter) {
            // Session
            Object obj = request.getSession().getAttribute("DESKTOP_SESSION");
            if (null == obj) {
                logger.info("登录超时！！");
                response.sendRedirect(ConfigureUtil.getProperty("home_page"));
                return false;
            }
            else {
                request.setAttribute("LOG_ACCESS_TIME", System.currentTimeMillis());
            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        super.afterCompletion(request, response, handler, ex);
        Object obj = request.getAttribute("LOG_ACCESS_TIME");
        if (null != obj) {
            long accessTime = (long) obj;
            logger.info("Deal Request  " + request.getRequestURI() + "  consume  "
                    + (System.currentTimeMillis() - accessTime) + "  ms");
        }
    }
}
