package com.icloudmoo.business.payment.bo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icloudmoo.business.payment.dao.po.HomeUserPayInfo;
import com.icloudmoo.business.payment.utils.alipay.AlipayUtils;
import com.icloudmoo.business.payment.utils.wxpay.WXUtils;
import com.icloudmoo.common.bo.AbstractBusinessObject;
import com.icloudmoo.common.exception.BusinessServiceException;

/**
 * 支付信息
 * 
 * @author liyong
 * @Date 2015年12月21日 下午3:08:42
 */
@Service("ghomeUserPayInfoBo")
public class HomePaymentBo extends AbstractBusinessObject {

    /**
     * TODO: 获取支付参数，保存支付订单
     * 
     * @param params
     * @return
     * @throws BusinessServiceException
     */
    @Transactional(rollbackFor = { Exception.class, RuntimeException.class })
    public Map<String, Object> getPayParamInfo(HomeUserPayInfo payInfo) throws BusinessServiceException {
        if (null == payInfo || null == payInfo.getUserId() || null == payInfo.getObjectId()
                || StringUtils.isEmpty(payInfo.getObjectType()) || StringUtils.isEmpty(payInfo.getChannel())) {
            throw new BusinessServiceException("参数不完全，无法执行！");
        }

        String result = StringUtils.EMPTY;
        String orderNo = StringUtils.EMPTY;
        try {
            // 处理优惠券
            // processCoupon(payInfo, true);
            switch (payInfo.getChannel()) {
                case HomeUserPayInfo.ALIPAY:
                    orderNo = AlipayUtils.getOutTradeNo(HomeUserPayInfo.ALIPAY);
                    payInfo.setOrderNo(orderNo);
                    payInfo.setChannel(HomeUserPayInfo.ALIPAY);
                    payInfo.setDescription("支付宝订单,支付金额：" + payInfo.getAmount());
                    result = AlipayUtils.getPayParamString("支付订单", payInfo.getDescription(), payInfo.getAmount().toString(), orderNo);
                    logger.info("response result:" + result.toString());
                    break;
                case HomeUserPayInfo.WXPAY:
                    orderNo = AlipayUtils.getOutTradeNo(HomeUserPayInfo.WXPAY);
                    payInfo.setChannel(HomeUserPayInfo.WXPAY);
                    payInfo.setOrderNo(orderNo);
                    payInfo.setDescription("微信支付订单,支付金额：" + payInfo.getAmount());
                    result = WXUtils.genPayReq(orderNo, payInfo.getAmount().toString(), payInfo.getDescription());
                    break;
                default:
                    throw new BusinessServiceException("不支持的支付类型！！");
            }

            // 保存支付信息

            Map<String, Object> temp = new HashMap<>(2);
            temp.put("payParam", result);
            temp.put("orderNo", orderNo);
            return temp;
        }
        catch (Exception e) {
            logger.error("获取支付参数失败！", e);
            throw new BusinessServiceException(e.getMessage(), e);
        }
    }

    public HomeUserPayInfo findByTradeNo(String tradeNo) throws BusinessServiceException {
        if (StringUtils.isEmpty(tradeNo)) {
            throw new BusinessServiceException("无法查询交易信息，交易号为空！");
        }

        try {
            // return
            // ghomeUserPayInfoDaoImpl.findEntityByCondition(GhomeUserPayInfo.class,
            // new String[] { "orderNo", "status" }, new Object[] { tradeNo,
            // GhomeUserPayInfo.STATUS_UNPAY });

            HomeUserPayInfo userPayInfo = new HomeUserPayInfo();

            return userPayInfo;
        }
        catch (Exception e) {
            logger.error("查询支付记录失败！", e);
            throw new BusinessServiceException("查询支付记录失败！", e);
        }
    }

}
