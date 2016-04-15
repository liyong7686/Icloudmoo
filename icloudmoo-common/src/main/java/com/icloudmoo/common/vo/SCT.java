/**
 *2010-3-1下午03:53:41
 *txy
 */
package com.icloudmoo.common.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.icloudmoo.common.util.RandomUtil;

/**
 * 
 * @author Guguihe
 * @date 2010-3-31
 * @version 1.0
 */
public interface SCT {

    String DES3_PRIVATE_KEY = "";

    String ALIPAY_PUBLIC_KEY = "";

    String CHAR_ENCODING = "UTF-8";

    String ALIPAY_SIGN_TYPE = "Rsa";

    String SESSION_ID = "DESKTOP_SESSION";

    String FORMAT_DATE_TIME_MILLI = "yyyyMMddHHmmssSSS";

    String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    String FORMAT_DATE_ONLY = "yyyy-MM-dd";

    // 数据状态--删除
    public static final String STATUS_DELETE = "0";

    // 数据状态--正常
    public static final String STATUS_NORMAL = "1";

    /**
     * 字段占位符
     */
    String FIELD_PATTERN = "[%]{1}\\{[\\D]*\\}";

    /**
     * 参数占位符
     */
    String PARAM_PATTERN = "[\\$]{1}\\{[\\D]*\\}";

    /**
     * 参数选择占位符
     */
    String PARAM_SELECTOR_PATTERN = "\\[.*[^\\[]*\\]";

    String PARAM_ALL_PATTERN = "[\\[\\]]";

    String SYSTEM_UPDATE_TAG = "SYS_U_T";

    String SYSTEM_UPDATE_ID = "WorkdeskSystemUpdate";

    class TIMECODE_GENARATOR {
        public static String genarate() {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME_MILLI);
            return sdf.format(Calendar.getInstance().getTime()) + RandomUtil.generateString(3);
        }
    }
}
