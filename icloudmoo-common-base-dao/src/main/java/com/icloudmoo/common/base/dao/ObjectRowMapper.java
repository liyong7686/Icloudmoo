package com.icloudmoo.common.base.dao;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import com.icloudmoo.common.vo.BeanInitUtils;


/**
 * @ClassName: ObjectRowMapper
 * @Description: 数据库对象映射为JAVA BEAN
 * @author 信息管理部-gengchong
 * @date 2015年8月17日 上午10:02:01
 *
 * @param <T>
 */
public class ObjectRowMapper<T> implements RowMapper<T> {

    protected Log logger = LogFactory.getLog(getClass());
    /**
     * 需要映射的对象类型
     */
    private Class<T> entiryClass;

    /**
     * 有可能数据表中的列名与对象属性名称不一致，所以为这些不一致的对应关系留出一个参数，其中，每一组数据中的第一位为数据表中的列名称，
     * 第二位为该列对应的对象属性名称
     */
    private String[][] propertiesMapper;

    /**
     * 创建一个新的实例 ObjectRowMapper.
     */
    public ObjectRowMapper() {
    }

    /**
     * 创建一个新的实例 ObjectRowMapper.
     * 
     * @param t
     */
    public ObjectRowMapper(Class<T> t) {
        this.entiryClass = t;
    }

    /**
     * 创建一个新的实例 ObjectRowMapper.
     * 
     * @param t
     * @param propertiesMapper
     */
    public ObjectRowMapper(Class<T> t, String[][] propertiesMapper) {
        this.propertiesMapper = propertiesMapper;
        this.entiryClass = t;
    }

    /*
     * <p>Title: mapRow</p> <p>Description: </p>
     * 
     * @param rs
     * 
     * @param arg1
     * 
     * @return
     * 
     * @throws SQLException
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public T mapRow(ResultSet rs, int index) throws SQLException {
        if (null == entiryClass) {
            return null;
        }
        // 使用反射方式，调用对象的set方法将列数据设置到对象中
        try {
            T result = (T) entiryClass.newInstance();
            ResultSetMetaData md = rs.getMetaData();
            Map<String, String> columnNames = getRsColumns(md);

            // 因为查询的时候可能不会是单表中字段，有可能多表连接同时只需要部分表，每次新建VO对象比较麻烦，此处，rowMapper对象可以直接转为Ma对象返回。
            // update by @tanghui 2015-11-03 14:25
            if (entiryClass.equals(HashMap.class)) {
                Object object = new Object();
                for (String field : columnNames.keySet()) {
                    ((HashMap<String, Object>) result).put(field, this.getRsValue(object.getClass(), rs, field));
                }
                return result;
            }
            else {
                Method[] methods = entiryClass.getMethods();
                String methodName = null;
                String fieldName = null;
                Class<?>[] paramClasses = null;
                Object value = null;
                String[] mapper = null;
                for (Method method : methods) {
                    methodName = method.getName();
                    paramClasses = method.getParameterTypes();
                    if (methodName.startsWith("set") && !ArrayUtils.isEmpty(paramClasses) && paramClasses.length == 1) {
                        fieldName = BeanInitUtils.getFieldFromMethod(methodName);
                        mapper = getPropertyMapper(fieldName);
                        fieldName = (null == mapper ? fieldName : mapper[0]);
                        try {
                            if (columnNames.containsKey(fieldName)) {
                                value = getRsValue(paramClasses[0], rs, fieldName);
                                method.invoke(result, value);
                            }
                        }
                        catch (Exception e) {
                            logger.error("数据设置错误，对象：" + entiryClass.getName() + "，属性：" + fieldName + "，错误信息："
                                    + e.getMessage());
                        }
                    }
                }
                columnNames.clear();
                return result;
            }
        }
        catch (Exception e) {
            logger.error("ORM 数据结果映射到BO异常", e);
        }
        return null;
    }

    /**
     * @Title: getRsColumns
     * @Description: 获取返回结果的元数据信息
     * @param @param
     *            md
     * @return Map<String,String> 返回类型
     * @throws Exception
     */
    private Map<String, String> getRsColumns(ResultSetMetaData md) throws Exception {
        int count = md.getColumnCount();
        Map<String, String> result = new HashMap<>(count);
        String temp = null;
        for (int i = 1; i <= count; i++) {
            temp = md.getColumnLabel(i);
            result.put(temp, temp);
        }

        return result;
    }

    /**
     * @Title: getRsValue
     * @Description: 获取数据集中的值
     * @param @param
     *            type
     * @param @param
     *            rs
     * @param fieldName
     * @return Object 返回类型
     * @throws Exception
     */
    private Object getRsValue(Class<?> type, ResultSet rs, String fieldName) throws Exception {
        Object result = null;
        switch (type.getSimpleName()) {
            case "String":
                result = rs.getString(fieldName);
                break;
            case "long":
            case "Long":
                result = rs.getLong(fieldName);
                break;
            case "int":
            case "Integer":
                result = rs.getInt(fieldName);
                break;
            case "float":
            case "Float":
                result = rs.getFloat(fieldName);
                break;
            case "Double":
            case "double":
                result = rs.getDouble(fieldName);
                break;
            case "short":
            case "Short":
                result = rs.getShort(fieldName);
                break;
            case "byte":
            case "Byte":
                result = rs.getByte(fieldName);
                break;
            case "BigDecimal":
                result = rs.getBigDecimal(fieldName);
                break;
            case "Date":
                result = rs.getTimestamp(fieldName);
                break;
            case "boolean":
            case "Boolean":
                result = rs.getBoolean(fieldName);
                break;
            default:
                result = rs.getObject(fieldName);
                break;
        }

        return result;
    }

    /**
     * @description 查找特殊属性与列名的对应关系 @param @return @throws
     */
    private String[] getPropertyMapper(String field) {
        if (null == propertiesMapper || propertiesMapper.length < 1) {
            return null;
        }

        String[] result = null;
        for (String[] temps : propertiesMapper) {
            if (StringUtils.equals(field, temps[1])) {
                result = temps;
                break;
            }
        }

        return result;
    }

}
