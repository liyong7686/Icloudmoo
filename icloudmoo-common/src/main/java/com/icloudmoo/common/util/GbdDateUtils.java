package com.icloudmoo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class GbdDateUtils {

	/**
	 * 默认日期格式
	 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 默认日期时间格式
	 */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String format(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		return sdf.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 获取日期开始时间，从一天的零点开始
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date getBeginDate(String date) {
		try {
			Date temp = parseDate(date, DATE_TIME_PATTERN);
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		} catch (Exception e) {
			throw new RuntimeException("日期转换失败！！", e);
		}
	}

	/**
	 * 获取日期开始时间，从一天的零点开始
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date getBeginDate(String date, String pattern) {
		try {
			Date temp = parseDate(date, pattern);
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		} catch (Exception e) {
			throw new RuntimeException("日期转换失败！！", e);
		}
	}

	/**
	 * 获取日期结束时间，即一天的结束时间
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date getEndDate(String date) {
		try {
			Date temp = parseDate(date, DATE_TIME_PATTERN);
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			return c.getTime();
		} catch (Exception e) {
			throw new RuntimeException("日期转换失败！！", e);
		}
	}

	/**
	 * 获取日期结束时间，即一天的结束时间
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date getEndDate(String date, String pattern) {
		try {
			Date temp = parseDate(date, pattern);
			Calendar c = Calendar.getInstance();
			c.setTime(temp);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			return c.getTime();
		} catch (Exception e) {
			throw new RuntimeException("日期转换失败！！", e);
		}
	}

	/**
	 * 将日期字符串转换成日期对象
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(DATE_TIME_PATTERN);
			return sdf.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("日期转换失败！！", e);
		}
	}

	/**
	 * 将日期字符串转换成日期对象
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(pattern);
			return sdf.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("日期转换失败！！", e);
		}
	}

}
