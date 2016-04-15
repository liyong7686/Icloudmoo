package com.icloudmoo.business.web.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.Constants;


/**
 * @author liyong
 */
@Repository
public class SystemAccessInterceper extends HandlerInterceptorAdapter {

    private final String checkpointName = "DO_CHECKPOINT";
    private final String shareType = "enjoylink_share";
    String[] noFilters = ConfigureUtil.getStringArray("nofilter.urls");

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        String userAgent = request.getHeader("User-Agent");
        String type = request.getParameter("shareType");
        String requestUri = request.getRequestURI();
        if (isFilter(requestUri)) {
            return true;
        }
        logger.info("User-Agent:" + userAgent);
        if (!StringUtils.equalsIgnoreCase(shareType, type) 
              && (!StringUtils.startsWith(userAgent, Constants.USER_AGENT)
                   || 2 > StringUtils.indexOfAny(userAgent, "|"))) {
            logger.info("非法请求接口！！");
            return false;
        }

        try {
            if (StringUtils.contains(userAgent, "|")) {
                String[] agts = userAgent.split("\\|");
                request.setAttribute(Constants.HEAD_SYS, agts[0]);
                request.setAttribute(Constants.HEAD_VERSION, agts[1]);
            }
            request.setAttribute(Constants.LOG_ACCESS_TIME, System.currentTimeMillis());
            return super.preHandle(request, response, handler);
        }
        catch (Exception e) {
            logger.error("拦截器验证失败！", e);
            return false;
        }
    }

    /**
     * TODO: 检查是否需要过滤
     * @param uri
     * @return
     */
    private boolean isFilter(String uri) {
        if (org.apache.commons.lang3.ArrayUtils.isEmpty(noFilters)) {
            return Boolean.TRUE;
        }

        for (String noFilter : noFilters) {
            if (StringUtils.contains(uri, noFilter)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
        Object obj = request.getAttribute(Constants.LOG_ACCESS_TIME);
        if (null != obj) {
            long accessTime = (long) obj;
            logger.info("处理请求" + request.getRequestURI() + "耗时" + (System.currentTimeMillis() - accessTime) + "毫秒！");
            /*  try {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                BusinessCheckPoint checkPoint = handlerMethod.getMethodAnnotation(BusinessCheckPoint.class);
                if (null != checkPoint) {
                    String checkpointKey = checkPoint.checkName();
                    Object rb = request.getAttribute(Constants.RETURN_BODY);
                    Object userId = request.getAttribute(Constants.REQUEST_USERID);
                    // 业务处理结果为正确时。发送积分或者信用累积
                    if (null != rb && null != userId && StringUtils.isNotEmpty(userId.toString())) {
                        ReturnBody returnBody = (ReturnBody) rb;
                        // 检查点检测，如果是检查点则发送消息
                        if (returnBody.getCode() > 0) {
                            // 发送检查点消息
                            CodeDesc codeDesc = new CodeDesc();
                            codeDesc.setJmsType(checkpointName);
                            codeDesc.setName(checkpointKey);
                            codeDesc.setBusinessCode(returnBody.getCheckpointCode());
                            codeDesc.setUserId(Integer.valueOf(userId.toString()));
                            JmsUtil.pushToUserQueue(codeDesc);
                        }
                    }
                }
            }
            catch (Exception e) {
                logger.error("发送积分消息失败！", e);
            }*/
        }
    }

}
