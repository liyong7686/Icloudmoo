package com.icloudmoo.common.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO:读取配置参数工具类
 * @author liyong
 * @Date 2015年12月18日 上午11:41:13
 */
public class ConfigureUtil {
    private static final Properties properties = new Properties();

    static {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("systemConfig.properties");
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * @description 获取属性值
     * @return String
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        return StringUtils.defaultIfEmpty(properties.getProperty(key),
                defaultValue);
    }

    /**
     * @description 获取属性值
     * @return String
     */
    public static int getInt(String key, int defaultValue) {
        String result = getProperty(key, "NULL");
        if (StringUtils.isNumeric(result)) {
            return Integer.valueOf(result);
        } else {
            return defaultValue;
        }
    }

    /**
     * @description 获取属性值
     * @return String
     */
    public static long getLong(String key, long defaultValue) {
        String result = getProperty(key, "NULL");
        if (StringUtils.isNumeric(result)) {
            return Long.valueOf(result);
        } else {
            return defaultValue;
        }
    }

    /**
     * @description 获取属性值
     * @return String
     */
    public static float getFloat(String key, float defaultValue) {
        String result = getProperty(key, "NULL");
        if (StringUtils.equalsIgnoreCase("null", result)) {
            return defaultValue;
        } else {
            try {
                return Float.valueOf(result);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    /**
     * @description 获取属性值
     * @return String
     */
    public static double getDouble(String key, double defaultValue) {
        String result = getProperty(key, "NULL");
        if (StringUtils.equalsIgnoreCase("null", result)) {
            return defaultValue;
        } else {
            try {
                return Double.valueOf(result);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    
    /**
     * @description 获取属性值
     * @return Integer[]
     */
    public static Integer[] getArray(String key) {
        String property = getProperty(key);
        if (StringUtils.isEmpty(property)) {
            return null;
        }

        String[] tempArray = StringUtils.split(property, ",");
        Integer[] results = new Integer[tempArray.length];
        int index = 0;
        try {
            for (String temp : tempArray) {
                results[index++] = Integer.valueOf(temp);
            }
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
        }

        return results;
    }

    /**
     * @description 获取属性值
     * @return Integer[]
     */
    public static String[] getStringArray(String key) {
        String property = getProperty(key);
        if (StringUtils.isEmpty(property)) {
            return null;
        }

        return StringUtils.split(property, ",");
    }

    /**
     * @description 设置属性的值
     */
    public static void setProperty(String key, String value) {
        try {
            properties.setProperty(key, value);
            FileOutputStream out = new FileOutputStream(Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("systemConfig.properties").getPath());
            properties.store(out, "update property:" + key);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
