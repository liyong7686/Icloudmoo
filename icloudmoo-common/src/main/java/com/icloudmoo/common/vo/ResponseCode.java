/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * ResposeCode.java
 * @Date 2015年10月23日 下午5:47:17
 * guguihe
 */
package com.icloudmoo.common.vo;

/**
 * TODO:返回值
 * 
 * @author guguihe
 * @Date 2015年10月23日 下午5:47:17
 */
public interface ResponseCode {
    // =============================业务执行成功============================
    // 请求执行成功
    int SUCCESS = 10000;
    // 身份验证成功
    int VALID_SUCCESS = 10001;
    // 身份验证待审核
    int VALID_DOING = 10002;
    // 有新版本需要更新
    int VERSION_UPDATE = 10003;

    // =============================业务执行失败============================
    // 请求执行失败
    int FAIL = -10000;
    // SESSION验证失败
    int SESSION_VALID_FAIL = -10001;
    // 身份验证失败
    int VALID_FAIL = -10002;

    // 优惠券兑换码有误！
    int COUPON_FAIL = -10003;
    // 身份认证正在进行中，请不要重复提交
    int VALID_REDOING = -10004;
    // 身份认证已经通过，请不要重复提交
    int VALID_REDONE = -10005;
    
    // 身份认证已被别人使用
    int VALID_CID_REDONE = -10006;
    
    // 用户姓名错误
    int VALID_USERNAME_ERROR = -10007;
}
