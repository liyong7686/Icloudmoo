
package com.icloudmoo.common.vo;

/**
 * @ClassName: Constants
 * @Description: TODO（用一句话描述这个类的用途）
 * @author 信息管理部-gengchong
 * @date 2015年8月24日 下午2:59:48
 * 
 */
public class Constants extends ValueObject {
	// @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）

	private static final long serialVersionUID = 1L;
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_DELETE = "0";
	public static final String READ = "1";
	public static final String WRITE = "2";
	public static final String COMPANY_FLAG = "G";
	// @Fields DEFAULT_CHARSET : 默认编码
	public static final String DEFAULT_CHARSET = "UTF-8";
	// @Fields REQUEST_TOKEN : 请求TOKEN
	public static final String REQUEST_TOKEN = "token";
	public static final String REQUEST_SESSIONID = "sessionId";
	public static final String REQUEST_VERSION = "version";
	public static final String REQUEST_DEVICEINFO = "deviceInfo";
	public static final String REQUEST_TIMESTAMP = "timestamp";
	public static final String REQUEST_USERID = "userId";
	public static final String REQUEST_SYSID = "sysId";

	public static final String SYS_GHOME = "ghome";
	public static final String SYS_GKEEPER = "gkeeper";

	public static final String DES3_PRIVATE_KEY = "3des_key";
	public static final String USER_AGENT = "enjoy_link";

	public static final String SESSIONID_FLAG = "S_F_";

	public static final String DATA_NORMAL = "01";
	public static final String DATA_DELETED = "00";
	public static final String DATA_AUDIT = "02"; // 已审核
	public static final String DATA_FAIL = "03"; // 取消
	public static final String DATA_PUBLISH = "02"; // 已发布
	public static final String DATA_OTHER = "99"; // 其它

	public static final String VERIFY_DOING = "01";
	public static final String VERIFY_FAIL = "03";
	public static final String VERIFY_DONE = "02";
	public static final String VERIFY_REDONE = "04";
	public static final String VERIFY_REDOING = "05";
	public static final String VERIFY_USER_REPEAT = "06";
	public static final String VERIFY_USERNAME_FAIL = "07";

	public static final String SKILL_DISPATCH_ORDER = "ST000901";
	public static final String SKILL_CLOSE_ORDER = "ST000902";

	public static final String SEX_FEMALE = "f";
	public static final String SEX_MAN = "m";

	// 优惠券已使用
	public static final String COUPON_USED = "02";

	public static final String RETURN_BODY = "return_body";

	public static final String LOG_ACCESS_TIME = "LOG_ACCESS_TIME";

	public static final String HEAD_VERSION = "app_version";
	public static final String HEAD_SYS = "app_sys";

	// 收费管理 账单状态，gbd.gbd_house_bill 表状态
	public static final String HOUSE_BILL_STATUS_AUDIT = "01"; // 待审核
	public static final String HOUSE_BILL_STATUS_NOPAY = "02"; // 待支付
	public static final String HOUSE_BILL_STATUS_NOWRITEOFF = "03"; // 未销账
	public static final String HOUSE_BILL_STATUS_RECEIVALE = "04"; // 已收款

	public static final String USER_VALIDATE_REG = "01";
	public static final String USER_VALIDATE_ADD = "02";

	public static final String SCORE_TYPE_ALL = "00";

	public static final String TYPE_HOME = "HOME";
	public static final String TYPE_PUBLIC = "PUBLIC";

	public static final String USER_GHOME = "01";
	public static final String USER_GKEEPER = "02";

	/** 帖子类型 **/
	public static final String TOPIC_TYPE_ACTIVITY = "05"; // 帖子类型--活动
	public static final String TOPIC_TYPE_SHARE = "01"; // 帖子类型--分享
	public static final String TOPIC_TYPE_HELP = "02"; // 帖子类型--帮助
	public static final String TOPIC_TYPE_TRANSFER = "03"; // 帖子类型--转让
	public static final String TOPIC_TYPE_CONVENE = "04"; // 帖子类型--召集
	public static final String TOPIC_TYPE_OTHER = "99"; // 帖子类型--其他

	public static final String QUERY_ALL = "02";

}
