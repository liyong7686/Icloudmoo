package com.icloudmoo.home.user.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icloudmoo.common.controller.support.AbstractManagerController;
import com.icloudmoo.common.jms.suport.JmsUtil;
import com.icloudmoo.common.redis.suport.RedisUtil;
import com.icloudmoo.common.util.RandomUtil;
import com.icloudmoo.common.vo.MessageContent;
import com.icloudmoo.common.vo.MessageTarget;
import com.icloudmoo.common.vo.SendMessage;
import com.icloudmoo.common.vo.SystemUserVo;
import com.icloudmoo.home.user.dao.vo.EmployeeInfoParam;

/**
 * @author liyong
 * @Date 2015年12月21日 上午9:45:46
 */
@Controller
@RequestMapping("/home")
public class UserLoginController extends AbstractManagerController {

    /**
     * 登陆管理后台
     * @param userVo
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/loginHome", method = RequestMethod.POST)
    public Map<String, Object> loginEnhome(SystemUserVo userVo, HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
           
            /*
            if (null == userVo || StringUtils.isEmpty(userVo.getAccount()) || StringUtils.isEmpty(userVo.getPassword())
                    || StringUtils.isEmpty(userVo.getValidateCode())) {
                throw new BusinessServiceException("非法登录，条件不完全！");
            }

            // 验证码校验
            String valicode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
            if (!StringUtils.equalsIgnoreCase(valicode, userVo.getValidateCode())) {
                throw new BusinessServiceException("登录失败，验证码不正确！");
            }

            WorkdeskSystemUser user = new WorkdeskSystemUser();
            user.setAccount(userVo.getAccount());
            user = workdeskSystemUserBo.findForLogin(user);
            if (null == user) {
                throw new BusinessServiceException("用户不存在!");
            }

            if (!userVo.getPassword().equalsIgnoreCase(user.getPasswd())) {
                throw new BusinessServiceException("密码错误!");
            }

            // 用户登录成功，信息保存到session当中
            SystemUserVo sessionUserVo = new SystemUserVo();
            BeanInitUtils.copyProperties(user, sessionUserVo);
            sessionUserVo.setDataRight(workdeskRelationBo.getUserOrgString(userVo, null, null));
            session.setAttribute(DESKTOP_SESSION, sessionUserVo);
           */
            
            testMsg();
            logger.info("推送信息成功！");
            
            session.setAttribute(DESKTOP_SESSION, null);
            result.put(SUCCESS, true);
        }
        catch (Exception e) {
            logger.error(null, e);
            result.put(MSG, e.getMessage());
            result.put(SUCCESS, false);
        }

        return result;
    }

    /**
     * 登出管理后台
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logoutHome", method = RequestMethod.POST)
    public Map<String, Object> logoutEnhome(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            session.removeAttribute(DESKTOP_SESSION);
            result.put(SUCCESS, true);
        }
        catch (Exception e) {
            logger.error(null, e);
            result.put(MSG, e.getMessage());
            result.put(SUCCESS, false);
        }

        return result;
    }
    
    //测试发送消息的方法
    public static void testMsg() {
        try {
            
           //客户登陆后异步更新客户信息
            EmployeeInfoParam userInfoParam = employeeInfos();
            JmsUtil.pushToEmployeeQueue(userInfoParam);  
            /* 
            // 发送短信通知客户
            String validCode = RandomUtil.generateString(4);
            String mobile = "13113609569";
            RedisUtil.redisSaveObject(mobile, validCode, 2);
            MessageContent msgContent = new MessageContent("", validCode, MessageContent.TYPE_GHOME_USER, validCode);
            SendMessage msg = new SendMessage("sendSmsMessage", msgContent, mobile, SendMessage.SMS_VALIDCODE);
            JmsUtil.pushToMsgSenderQueue(msg); 
        */
            
            //发送个推消息
           /* MessageTarget targets = testMessageTargetDdata();            
            MessageContent msgContent = new MessageContent("您的服务区域已通过审核。",
                    "您申请的服务区域【】已经通过审核。", MessageContent.TYPE_GKEEPER_USER, "1234123123213");
            SendMessage msg = new SendMessage("pushMessage", msgContent, Arrays.asList(targets),
                    SendMessage.TYPE_SINGLE, SendMessage.SOURCE_GKEEPER);
            JmsUtil.pushToMsgSenderQueue(msg);           
           */
          
            /*
            //发送推送消息 List
            List<MessageTarget> targets = testMessageTargetDdataList();
            if (CollectionUtils.isNotEmpty(targets)) {
                MessageContent pushMsg = new MessageContent("您报名或关注的活动已经被取消。", "活动取消原因：XXXXXXXX",
                        MessageContent.TYPE_ACTIVITY, "1231231232Id");
                SendMessage msg = new SendMessage("pushMessage", pushMsg, targets, SendMessage.TYPE_LIST,
                        SendMessage.SOURCE_GHOME);
                JmsUtil.pushToMsgSenderQueue(msg);
            }*/
            
            
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
        
    }
    
    public static EmployeeInfoParam employeeInfos(){
        EmployeeInfoParam emp = new EmployeeInfoParam();
        emp.setAppVersion("1212");
        emp.setChannel("1212");
        emp.setClientId("12121221");
        emp.setContent("该账户成功后更新该账号的信息!");
        emp.setJmsType("UserLogin");
        return emp;
    }
    public static MessageTarget testMessageTargetDdata(){
        MessageTarget targets = new MessageTarget();
        targets.setUserId(123123);
        targets.setUserName("张三");
        targets.setProjectCode("1232222Code");
        targets.setProjectName("projectName");
        targets.setOs("01");
        targets.setClientId("31a75c7b0e83ed76a0de4de2643beea8");
        targets.setJmsType("pushMessage");
        return targets;
    }
    public static List<MessageTarget> testMessageTargetDdataList(){
        List<MessageTarget>  list = new ArrayList<MessageTarget>();
        for (int i = 0; i < 5; i++) {
            MessageTarget targets = new MessageTarget();
            targets.setUserId(i*100+1000);
            targets.setUserName("张三");
            targets.setProjectCode("1232222Code");
            targets.setProjectName("projectName");
            targets.setOs("01");
            targets.setClientId("31a75c7b0e83ed76a0de4de2643beea8");
            targets.setJmsType("pushMessage");
            list.add(targets);
        }
        return list;
    }
    
}
