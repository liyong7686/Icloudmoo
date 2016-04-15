package com.icloudmoo.business.payment.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icloudmoo.business.payment.bo.HomePaymentBo;
import com.icloudmoo.business.payment.dao.po.HomeUserPayInfo;
import com.icloudmoo.business.payment.utils.alipay.AlipayNotify;
import com.icloudmoo.business.payment.utils.wxpay.WXUtils;
import com.icloudmoo.common.annotation.EncryptRequest;
import com.icloudmoo.common.controller.databind.RequestObject;
import com.icloudmoo.common.controller.support.AbstractBusinessController;
import com.icloudmoo.common.exception.BusinessServiceException;
import com.icloudmoo.common.vo.Constants;
import com.icloudmoo.common.vo.ReturnBody;
import com.icloudmoo.common.vo.ValueObject;


/**
 * 支付信息接口
 * @author liyong
 * @Date 2015年12月21日 上午11:48:04
 */
@Controller
@RequestMapping("/payment")
public class HomePaymentController extends AbstractBusinessController{

    private static final String wxpay_fail = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[FAIL]]></return_msg></xml>";
    private static final String wxpay_success = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    private static final String alipay_success = "success";
    private static final String alipay_fail = "fail";
    
    @Autowired
    private HomePaymentBo homePaymentBo;
    
    /**
     * 获取支付请求参数 
     * @param reqObj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPayParamInfo", method = RequestMethod.POST)
    public ReturnBody getPayParamInfo(@EncryptRequest RequestObject<HomeUserPayInfo> reqObj) {
        if (!reqObj.isSuccess()) {
            return setFailBody(reqObj);
        }

        ReturnBody result = new ReturnBody();
        try {
            HomeUserPayInfo payInfo = reqObj.getRequestData();
            
            //模拟测试数据
            payInfo.setObjectId(1000);
            payInfo.setObjectType("01");
            payInfo.setChannel("02");
            payInfo.setDescription("测试订单的描述信息");
            payInfo.setAmount(0.001f);
            
            String userId = reqObj.getRequestHead().get(Constants.REQUEST_USERID);
            payInfo.setUserId(Integer.valueOf(userId));
            Map<String, Object> resultInfo = homePaymentBo.getPayParamInfo(payInfo);
            result.setResult(resultInfo);
            result.setSuccess();
        }
        catch (Exception e) {
            logger.error("获取支付字符串失败！", e);
            result.setFail();
            if (StringUtils.isNotEmpty(e.getMessage())) {
                result.setDesc(e.getMessage());
            }
        }
        return result;
    }
    
    
    /**
     * TODO: 支付宝支付回调
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/alipayCallback")
    public void alipayCallback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("----------------------接收到支付宝回调请求---------------------------");
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getB ytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        try {
            String out_trade_no = request.getParameter("out_trade_no");
            // 交易状态
            String trade_status = request.getParameter("trade_status");
            logger.info("----------------------支付宝回调请求状态：" + trade_status + "，单号：" + out_trade_no
                    + "---------------------------");
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考) 验证成功
            if (AlipayNotify.verify(params)) {
                logger.info("----------------------支付宝回调请求参数验证通过---------------------------");
                ValueObject paymentInfo = null;
                paymentInfo = homePaymentBo.findByTradeNo(out_trade_no);
                if (null == paymentInfo) {
                    throw new BusinessServiceException("未找到支付记录！！");
                }
                if (trade_status.equals("TRADE_FINISHED")) {
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序
                    // 注意：
                    // 该种交易状态只在两种情况下出现
                    // 1、开通了普通即时到账，买家付款成功后。
                    // 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
                }
                else if (trade_status.equals("TRADE_SUCCESS")) {
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序
                    // 更新支付记录状态
                    // 注意：
                    // 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
                    // 更新支付状态
                   // enjoylinkPaymentBo.modifyPaymentResult(params);
                }
                else if (trade_status.equals("WAIT_BUYER_PAY")) {

                }
                else if (trade_status.equals("TRADE_CLOSED")) {
                    // 更新支付记录状态
                  //  enjoylinkPaymentBo.modifyPaymentResult(params);
                }

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                response.getWriter().println(alipay_success); // 请不要修改或删除
                // ////////////////////////////////////////////////////////////////////////////////////////
            }
            else {// 验证失败
                logger.info("支付宝回调请求参数验证失败!");
                response.getWriter().println(alipay_fail);
            }
        }
        catch (Exception e) {
            logger.error("支付宝回调请求发生异常!",e);
            try {
                response.getWriter().println(alipay_fail);
            }
            catch (IOException e1) {
                logger.error("支付宝回调接口发生错误！！", e);
            }
        }
    }

    /**
     * TODO: 微信支付回调
     * @param request
     * @param response
     */
    @RequestMapping(value = "/wxpayCallback")
    public void wxpayCallback(HttpServletRequest request, HttpServletResponse response) {
        InputStream is = null;
        try {
            is = request.getInputStream();
            String result = IOUtils.toString(is);
            logger.info("返回结果XML：" + result);
            Map<String, String> xmlResult = WXUtils.transferXml(result);
            for (Map.Entry<String, String> entry : xmlResult.entrySet()) {
                logger.info("KEY: " + entry.getKey() + "-----------Value: " + entry.getValue());
            }

            String returnCode = xmlResult.get("return_code");
            logger.info("----------------------接收到微信回调请求(return_code:" + returnCode + ")---------------------------");
            if (StringUtils.equalsIgnoreCase("SUCCESS", returnCode)) {
                String resultCode = xmlResult.get("result_code");
                logger.info( "----------------------接收到微信回调请求(resultCode:" + resultCode + ")---------------------------");
                String out_trade_no = xmlResult.get("out_trade_no");
                logger.info("----------------------接收到微信回调请求(out_trade_no:" + out_trade_no
                        + ")---------------------------");
                if (StringUtils.equalsIgnoreCase("SUCCESS", resultCode)) {
                    xmlResult.put("trade_status", "TRADE_SUCCESS");
                   // enjoylinkPaymentBo.modifyPaymentResult(xmlResult);
                }
                else {
                    xmlResult.put("trade_status", "TRADE_CLOSED");
                   // enjoylinkPaymentBo.modifyPaymentResult(xmlResult);
                }

                response.getWriter().print(wxpay_success);
            }
            else {
                response.getWriter().print(wxpay_fail);
            }
        }
        catch (Exception e) {
            logger.error("获取微信参数错误！！", e);
            try {
                response.getWriter().print(wxpay_fail);
            }
            catch (IOException e1) {
                logger.error("获取微信参数错误！！", e);
            }
        }
    }

}
