/**
 * 
 */
package com.icloudmoo.business.payment.utils.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.icloudmoo.common.util.ConfigureUtil;

/**
 * @description：
 * @author Guguihe
 * @date 2015年4月16日 下午7:11:00
 */
public class AlipayUtils {
//    public static final String PARTNER = "2088911147538878";
//    public static final String SELLER = "vlsd_tanbao@163.com";
    public static final String PARTNER = ConfigureUtil.getProperty("alipay.partner");
    public static final String SELLER = ConfigureUtil.getProperty("alipay.seller");
    public static final String RSA_PRIVATE;

    static {
        RSA_PRIVATE = SignUtils.loadRsaKey("pkcs8_private_key.pem");
    }

    /**
     * @param subject
     *            商品名称
     * @param body
     *            商品详情
     * @param price
     *            商品价格
     * @return
     */
    public static String getOrderInfo(String subject, String body, String price, String orderNo) {
        StringBuffer orderInfo = new StringBuffer();
        // 签约合作者身份ID
        orderInfo.append("partner=" + "\"" + PARTNER + "\"");

        // 签约卖家支付宝账号
        orderInfo.append("&seller_id=" + "\"" + SELLER + "\"");

        // 商户网站唯一订单号
        orderInfo.append("&out_trade_no=" + "\"" + orderNo + "\"");

        // 商品名称
        orderInfo.append("&subject=" + "\"" + subject + "\"");

        // 商品详情
        orderInfo.append("&body=" + "\"" + body + "\"");

        // 商品金额
        orderInfo.append("&total_fee=" + "\"" + price + "\"");

        // 服务器异步通知页面路径
        orderInfo.append("&notify_url=" + "\"" + ConfigureUtil.getProperty("alipay.notify.url") + "\"");

        // 服务接口名称， 固定值
        orderInfo.append("&service=\"mobile.securitypay.pay\"");

        // 支付类型， 固定值
        orderInfo.append("&payment_type=\"1\"");

        // 参数编码， 固定值
        orderInfo.append("&_input_charset=\"utf-8\"");

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo.append("&it_b_pay=\"30m\"");

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo.append("&return_url=\"m.alipay.com\"");

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo.toString();
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     * 
     */
    public static String getOutTradeNo(String type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        int ri = r.nextInt(1000);
        key += String.format("%03d", ri);
        return 'E' + type + key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     * 
     * @param content
     *            待签名订单信息
     */
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     * 
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * @param subject
     *            商品名称
     * @param body
     *            商品详情
     * @param price
     *            商品价格
     * @return
     */
    public static String getPayParamString(String subject, String body, String price, String orderNo) {
        // 订单
        String orderInfo = getOrderInfo(subject, body, price, orderNo);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        return orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
    }
}
