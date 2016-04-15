package com.icloudmoo.common.vo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
/**
 * 专门处理使用反射将一个对象实例化的类
 * @author liyong
 * @Date 2015年12月18日 下午6:04:42
 */
public class BeanInitUtils {
    public static ThreadPoolExecutor POOLEXECUTOR = new ThreadPoolExecutor(5, 20, 2, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(10));

    public static final String SETTER = "set";
    public static final String GETTER = "get";

    private static final Logger logger = Logger.getLogger(BeanInitUtils.class);

    /**
     * @Title: copyProperties 
     * @Description: 属性值复制 
     * @param source 
     * @param 
     * @param target 
     * @param ignoreProperties
     * 参数说明 @return void 返回类型
     *  @throws
     */
    public static void copyProperties(Object source, Object target, String[] ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /**
     * @Title: copyProperties @Description: 属性值复制 @param @param
     *         source @param @param target 参数说明 @return void 返回类型 @throws
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * @description 给一个实例设置属性值
     * @param bean
     *            实例
     * @param fieldName
     *            属性名称
     * @param value
     *            属性值
     * @param datePattern
     *            当属性为Date时，转换字符串为Date需要的数据格式
     */
    public static void setProperty(Object bean, String fieldName, Object value, String datePattern) {
        if (null == bean || StringUtils.isEmpty(fieldName) || null == value) {
            logger.debug("Can not set property value to an empty name!");
            return;
        }

        Class<?> c = bean.getClass();
        try {
            Field field = c.getDeclaredField(fieldName);
            Class<?> fieldType = field.getType();
            Method setMedhod = c.getDeclaredMethod(getMethodName(fieldName, SETTER), fieldType);
            if (!fieldType.equals(value.getClass())) {
                String type = fieldType.getSimpleName();
                switch (type) {
                    case "String":
                        value = String.valueOf(value);
                        break;
                    case "Integer":
                    case "int":
                        value = Integer.valueOf(String.valueOf(value));
                        break;
                    case "Float":
                    case "float":
                        value = Float.valueOf(String.valueOf(value));
                        break;
                    case "Double":
                    case "double":
                        value = Double.valueOf(String.valueOf(value));
                        break;
                    case "short":
                    case "Short":
                        value = Short.valueOf(String.valueOf(value));
                        break;
                    case "Byte":
                    case "byte":
                        value = Byte.valueOf(String.valueOf(value));
                        break;
                    case "Long":
                    case "long":
                        value = Long.valueOf(String.valueOf(value));
                        break;
                    case "Boolean":
                    case "boolean":
                        value = Boolean.valueOf(String.valueOf(value));
                        break;
                    case "Date":
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        sdf.applyPattern(datePattern);
                        value = sdf.parse(String.valueOf(value));
                        break;
                    default:
                        value = String.valueOf(value);
                        break;
                }
            }
            setMedhod.invoke(bean, value);
        }
        catch (Exception e) {
            logger.error("为Bean赋值异常", e);
        }
    }

    /**
     * @Title: getMethodName @Description: 根据字段获取方法名 @param @param
     *         field @param @param getOrSet @param @return 参数说明 @return String
     *         返回类型 @throws
     */
    public static String getMethodName(String field, String getOrSet) {
        char first = field.charAt(0);
        String prefix = String.valueOf(first).toUpperCase();
        String subfix = field.substring(1);
        return getOrSet + prefix + subfix;
    }

    /**
     * @Title: getFieldFromMethod @Description: 根据方法名获取字段 @param @param
     *         methodName @param @return 参数说明 @return String 返回类型 @throws
     */
    public static String getFieldFromMethod(String methodName) {
        String fieldName = methodName.substring(3);
        String prefix = fieldName.substring(0, 2);
        String subfix = fieldName.substring(2);
        return prefix.toLowerCase() + subfix;
    }

    /**
     * @Title: getGson @Description: Bean 转换成JSON @param @return @param @throws
     *         JsonParseException 参数说明 @return Gson 返回类型 @throws
     */
    public static Gson getGson() throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder().enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls()
                .registerTypeAdapter(java.util.Date.class, new GsonDateDeserializer())
                .registerTypeAdapter(java.lang.Integer.class, new GsonIntegerDeserializer());
        return gsonBuilder.create();
    }

    /**
     * 给Hibernfate查询出来的真实对象设置值，以达到保存的效果
     * 
     * @param realObject
     *            Hibernfate查询出来的真实对象
     * @param updateObject
     *            需要设置值的对象
     * @throws Exception
     */
    public static List<ModifyLog> setIfNotEmpty(Object realObject, Object updateObject) throws Exception {
        Class updateClass = updateObject.getClass();
        Class realClass = realObject.getClass();
        Method[] umethods = updateClass.getMethods();
        Object value = null;
        Method setMethod = null;
        Method getMethod = null;
        Object realValue = null;
        ModifyLog log = null;
        List<ModifyLog> logs = new ArrayList<ModifyLog>(10);
        for (Method method : umethods) {
            if (Modifier.toString(method.getModifiers()).contains("static") || !method.getName().startsWith("get")
                    || StringUtils.equals("getClass", method.getName())) {
                continue;
            }

            value = method.invoke(updateObject);
            if (null == value) {
                continue;
            }

            if (value instanceof String) {
                if (StringUtils.isEmpty(value.toString())) {
                    continue;
                }
            }

            try {
                getMethod = realClass.getMethod(method.getName());
                realValue = getMethod.invoke(realObject);
                // 两者值相同时不需要设置
                if (value.equals(realValue)) {
                    continue;
                }

                setMethod = realClass.getMethod(StringUtils.replaceOnce(method.getName(), "get", "set"),
                        value.getClass());
                if (null == method) {
                    continue;
                }

                setMethod.invoke(realObject, value);
                if (!StringUtils.equals("setBaseType", setMethod.getName())) {
                    log = new ModifyLog();
                    log.setBeforeValue(realValue);
                    log.setModifyField(BeanInitUtils.getFieldFromMethod(method.getName()));
                    log.setAfterValue(value);
                    logs.add(log);
                }
            }
            catch (Exception e) {
            }
        }

        return logs;
    }

    /**
     * 设置EXT的远程排序
     * 
     * @param sql
     * @param sortParam
     * @throws JsonMappingException
     */
    public static void setExtRemotOrder(StringBuilder sql, String sortParam) throws JsonMappingException {
        if (StringUtils.isNotEmpty(sortParam)) {
            Gson gson = BeanInitUtils.getGson();
            JsonArray jarray = gson.fromJson(sortParam, JsonArray.class);
            JsonElement jele = null;
            JsonObject jobj = null;
            sql.append("order by ");
            for (Iterator<JsonElement> iter = jarray.iterator(); iter.hasNext();) {
                jele = iter.next();
                jobj = jele.getAsJsonObject();
                sql.append(jobj.get("property").getAsString()).append(" ").append(jobj.get("direction").getAsString())
                        .append(" ");
            }
        }
    }
}
