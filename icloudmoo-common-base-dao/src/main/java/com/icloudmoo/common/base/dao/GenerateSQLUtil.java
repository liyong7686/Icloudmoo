package com.icloudmoo.common.base.dao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: GenerateSQLUtil
 * @Description: TODO（用一句话描述这个类的用途）
 * @author 信息管理部-gengchong
 * @date 2015年8月17日 上午10:49:51
 * 
 */
public class GenerateSQLUtil {

    /**
     * @Title: generateInsertSQLByObject @Description:
     *         TODO(这里用一句话描述这个方法的作用) @param @param
     *         object @param @return 参数说明 @return String 返回类型 @throws
     */
    public static Map<String, Object> generateInsertSQLByObject(Object object) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> metaDataMap = getMetaDataByObject(object);

        @SuppressWarnings("unchecked")
        LinkedList<String> columnList = (LinkedList<String>) metaDataMap.get("columnList");

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        StringBuilder value = new StringBuilder(" VALUES(");
        sql.append(metaDataMap.get("tableName") + "(");
        for (String column : columnList) {
            sql.append(column + ",");
            value.append("?,");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ")");
        value = new StringBuilder(value.substring(0, value.length() - 1) + ")");

        sql.append(value);

        result.put("sql", sql.toString());
        result.put("valueList", metaDataMap.get("valueList"));

        return result;
    }

    /**
     * @Title: generateInsertSQLByObject
     * @Description: 生成对象更新sql语句
     * @param object
     * @return Map<String,Object> 返回类型
     * @throws Exception
     */
    public static Map<String, Object> generateInsertSQLByList(List<?> list) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> metaDataMap = getMetaDataByList(list);

        @SuppressWarnings("unchecked")
        LinkedList<String> columnList = (LinkedList<String>) metaDataMap.get("columnList");

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        StringBuilder value = new StringBuilder(" VALUES(");
        sql.append(metaDataMap.get("tableName") + "(");
        for (String column : columnList) {
            sql.append(column + ",");
            value.append("?,");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ")");
        value = new StringBuilder(value.substring(0, value.length() - 1) + ")");

        sql.append(value);

        result.put("sql", sql.toString());
        result.put("valueList", metaDataMap.get("valueList"));

        return result;
    }

    /**
     * @Title: generateUpdateSQLByObject
     * @Description: 生成对象更新sql语句
     * @param object
     * @return Map<String,Object> 返回类型
     * @throws Exception
     */
    public static Map<String, Object> generateUpdateSQLByObject(Object object) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> metaDataMap = getMetaDataByObject(object);
        @SuppressWarnings("unchecked")
        LinkedList<String> columnList = (LinkedList<String>) metaDataMap.get("columnList");
        @SuppressWarnings("unchecked")
        LinkedList<Object> valueList = (LinkedList<Object>) metaDataMap.get("valueList");
        LinkedList<Object> realValueList = new LinkedList<Object>();

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(metaDataMap.get("tableName") + " SET ");
        for (int i = 0; i < valueList.size(); i++) {
            Object value = valueList.get(i);
            String column = columnList.get(i);
            if (null != value && !StringUtils.equals(column, metaDataMap.get("primaryKey").toString())) {
                sql.append(column + " = ?,");
                realValueList.add(value);
            }
        }
        sql = new StringBuilder(
                sql.substring(0, sql.length() - 1) + " WHERE " + metaDataMap.get("primaryKey") + " = ? ");
        realValueList.add(metaDataMap.get("primaryKeyValue"));

        result.put("sql", sql.toString());
        result.put("valueList", realValueList);

        return result;
    }

    /**
     * @Title: generateFindSQLByClass
     * @Description: 生成查询单条记录sql
     * @param object
     * @return Map<String,Object> 返回类型
     * @throws Exception
     */
    public static <T> Map<String, Object> generateFindSQLByClass(Class<T> t) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> metaDataMap = getMetaDataByClass(t);

        StringBuilder sql = new StringBuilder("SELECT * FROM " + metaDataMap.get("tableName"));
        sql.append(" WHERE " + metaDataMap.get("primaryKey") + " = ? ");

        result.put("sql", sql.toString());
        return result;
    }

    /**
     * @Title: generateDeleteSQLByClass
     * @Description: 生成物理删除SQL
     * @param t
     * @throws Exception
     * 参数说明
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public static <T> Map<String, Object> generateDeleteSQLByClass(Class<T> t, String[] columns) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> metaDataMap = getMetaDataByClass(t);

        StringBuilder sql = new StringBuilder("DELETE FROM " + metaDataMap.get("tableName"));
        sql.append(" WHERE 1 = 1 ");
        for (String column : columns) {
            sql.append("AND " + column + " = ? ");
        }
        result.put("sql", sql.toString());
        return result;
    }

    /**
     * @Title: generateFindEntitySQLByClass
     * @Description: 生成根据条件查询的sql
     * @param @param t
     * @param @param columns
     * @param @return
     * @param @throws Exception 参数说明
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public static <T> Map<String, Object> generateFindEntitySQLByClass(Class<T> t, String[] columns) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> metaDataMap = getMetaDataByClass(t);

        StringBuilder sql = new StringBuilder("SELECT * FROM " + metaDataMap.get("tableName"));
        sql.append(" WHERE 1 = 1 ");

        for (String column : columns) {
            sql.append("AND " + column + " = ? ");
        }

        result.put("sql", sql.toString());
        return result;
    }

    /**
     * TODO:
     * 
     * @param t
     * @param columns
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Object> generateFindEntityUnequalSQLByClass(Class<T> t, String column)
            throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> metaDataMap = getMetaDataByClass(t);

        StringBuilder sql = new StringBuilder("SELECT * FROM " + metaDataMap.get("tableName"));
        sql.append(" WHERE " + column + " != ? ");

        result.put("sql", sql.toString());
        return result;
    }

    /**
     * TODO: 生成更新的SQL语句
     * 
     * @param t
     * @param fields
     * @throws Exception
     */
    public static <T> String generateUpdateSql(Class<T> t, String[] fields, String[] wheres) throws Exception {
        Map<String, Object> metaDataMap = getMetaDataByClass(t);
        StringBuilder sql = new StringBuilder("UPDATE " + metaDataMap.get("tableName") + " SET");
        for (String field : fields) {
            sql.append(' ');
            sql.append(field);
            sql.append("= ?,");
        }

        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(" WHERE 1 = 1");
        for (String where : wheres) {
            sql.append(" AND ");
            sql.append(where);
            sql.append("= ?,");
        }

        sql.deleteCharAt(sql.lastIndexOf(","));
        return sql.toString();
    }

    /**
     * @description 更新字段数量SQL
     * @param
     * @return Map<String,Object>
     * @throws
     */
    public static <T> Map<String, Object> generateUpdateCountSQLByClass(Class<T> t, String column) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> metaDataMap = getMetaDataByClass(t);
        StringBuilder sql = new StringBuilder("UPDATE " + metaDataMap.get("tableName"));
        sql.append(" SET " + column + "=" + column + "+ ? WHERE " + metaDataMap.get("primaryKey") + " = ? ");
        result.put("sql", sql.toString());
        return result;
    }

    /**
     * @Title: getMetaData
     * @Description: 获取对象元数据
     * @param object
     * @return Map<String,Object> 返回类型
     * @throws Exception
     */
    private static Map<String, Object> getMetaDataByObject(Object object) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Class<?> cls = object.getClass();
        LinkedList<String> columnList = new LinkedList<String>();
        LinkedList<Object> valueList = new LinkedList<Object>();

        // 获取表名称
        if (cls.isAnnotationPresent(Table.class)) {
            result.put("tableName", cls.getAnnotation(Table.class).name());
        }

        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            // 获取主键字段
            if (field.isAnnotationPresent(Id.class)) {
                result.put("primaryKey", fieldName);
                field.setAccessible(true);
                result.put("primaryKeyValue", field.get(object));
            }
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            // 获取数据库字段，及对应的值
            columnList.add(fieldName);
            field.setAccessible(true);
            valueList.add(field.get(object));
        }

        result.put("columnList", columnList);
        result.put("valueList", valueList);
        return result;
    }

    /**
     * @Title: getMetaData
     * @Description: 获取对象元数据
     * @param object
     * @return Map<String,Object> 返回类型
     * @throws Exception
     */
    private static Map<String, Object> getMetaDataByList(List<?> list) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Class<?> cls = list.get(0).getClass();
        LinkedList<String> columnList = new LinkedList<String>();
        LinkedList<Object> valueList = new LinkedList<Object>();
        // 获取表名称
        if (cls.isAnnotationPresent(Table.class)) {
            result.put("tableName", cls.getAnnotation(Table.class).name());
        }

        Field[] fields = cls.getDeclaredFields();
        // 获取数据库字段
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            columnList.add(field.getName());
        }

        // 获取字段对应的值
        for (Object object : list) {
            LinkedList<Object> tmpValueList = new LinkedList<Object>();
            for (String fieldName : columnList) {
                Field field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                tmpValueList.add(field.get(object));
            }
            valueList.add(tmpValueList);
        }
        result.put("columnList", columnList);
        result.put("valueList", valueList);
        return result;
    }

    /**
     * @Title: getMetaData
     * @Description: 获取PO元数据信息
     * @param @param t
     * @param @return
     * @param @throws Exception 参数说明
     * @return Map<String,Object> 返回类型
     * @throws
     */
    private static <T> Map<String, Object> getMetaDataByClass(Class<T> t) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 获取表名称
        if (t.isAnnotationPresent(Table.class)) {
            result.put("tableName", t.getAnnotation(Table.class).name());
        }
        // 获取主键字段
        Field[] fields = t.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                result.put("primaryKey", field.getName());
                break;
            }
        }
        return result;
    }

}
