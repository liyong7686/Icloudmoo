package com.icloudmoo.common.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.icloudmoo.common.exception.FrameworkDAOException;
import com.icloudmoo.common.vo.PaginatedList;
import com.icloudmoo.common.vo.ParameterInfo;


/**
 * @ClassName: BaseDAO
 * @Description: 数据访问层公共接口
 * @author 信息管理部-gengchong
 * @date 2015年8月17日 上午9:40:54
 * 
 */
public interface BaseDAO {

    /**
     * @Title: save
     * @Description: 添加对象到数据库
     * @param obj
     * @return Integer 返回类型
     * @throws FrameworkDAOException
     */
    <T> Integer save(T obj) throws FrameworkDAOException;

    /**
     * @description 批量保存 @param @return void @throws
     */
    <T> void saveAll(List<T> list) throws FrameworkDAOException;

    /**
     * @Title: saveOrUpdate
     * @Description: 保存或修改数据对象
     * @param obj
     * @return T 返回类型
     * @throws FrameworkDAOException
     */
    <T> void update(T obj) throws FrameworkDAOException;

    /**
     * TODO: 使用指定的字段更新指定属性
     * 
     * @param obj
     *            需要更新的对象
     * @param fields
     *            需要更新的字段
     * @param wheres
     *            字段查询条件
     * @param values
     *            需要更新的的字段值
     * 
     * @throws FrameworkDAOException
     */
    <T> void updateEntity(Class<T> obj, String[] fields, String[] wheres, Object[] values) throws FrameworkDAOException;

    /**
     * @description 字段数量值增加
     * @param
     * @return void
     * @throws
     */
    <T> void updateCount(Class<T> t, String column, Integer id, Integer count) throws FrameworkDAOException;

    /**
     * @Title: findById @Description: TODO(这里用一句话描述这个方法的作用) @param @param
     *         t @param @param
     *         id @param @return @param @throws FrameworkDAOException
     *         参数说明 @return
     *         T 返回类型 @throws
     */
    <T> T findById(Class<T> t, Serializable id) throws FrameworkDAOException;

    /**
     * @Title: findForPage
     * @Description: 分页查询码数据
     * @param param
     * @return PaginatedList<T> 返回类型
     * @throws FrameworkDAOException
     */
    <T> PaginatedList<T> findForPage(ParameterInfo param) throws FrameworkDAOException;

    /**
     * @Title: findForPage
     * @Description: 分页查询码数据
     * @param param
     * @return PaginatedList<T> 返回类型
     * @throws FrameworkDAOException
     */
    <T> PaginatedList<T> findForPage(Map<String, Object> param) throws FrameworkDAOException;

    /**
     * @Title: findForApp
     * @Description: App专用数据列表查询
     * @param param
     * @return List<T> 返回类型
     * @throws FrameworkDAOException
     */
    <T> List<T> findForApp(ParameterInfo param) throws FrameworkDAOException;

    /**
     * @Title: findEntityByCondition
     * @Description: 根据条件获取单条记录
     * @param tc
     * @param cloumn
     * @param value
     * @return T 返回类型
     * @throws FrameworkDAOException
     */
    <T> T findEntityByCondition(Class<T> tc, String cloumn, Object value) throws FrameworkDAOException;

    /**
     * @Title: findEntityByCondition
     * @Description: 根据条件获取单条记录
     * @param tc
     * @param cloumn
     * @param value
     * @return T 返回类型
     * @throws FrameworkDAOException
     */
    <T> List<T> findListByCondition(Class<T> t, String columns, Object values) throws FrameworkDAOException;

    /**
     * @Title: findEntityByCondition
     * @Description: 根据条件获取单条记录
     * @param tc
     * @param cloumn
     * @param value
     * @return T 返回类型
     * @throws FrameworkDAOException
     */
    <T> List<T> findListByCondition(Class<T> t, String[] columns, Object[] values) throws FrameworkDAOException;

    /**
     * @Title: findEntityByCondition
     * @Description: 根据条件获取单条记录
     * @param tc
     * @param cloumn
     * @param value
     * @return T 返回类型
     * @throws FrameworkDAOException
     */
    <T> T findEntityByCondition(Class<T> tc, String[] columns, Object[] values) throws FrameworkDAOException;

    /**
     * @Title: findEntityByCondition
     * @Description: 根据条件获取单条记录
     * @param tc
     * @param cloumn
     * @param value
     * @return T 返回类型
     * @throws FrameworkDAOException
     */
    <T> List<T> findEntityByListCondition(Class<T> tc, String[] columns, Object[] values, String orderBy,
            String orderType) throws FrameworkDAOException;

    /**
     * @Title: deleteEntityByCondition @Description: 物理删除记录 @param tc @param
     *         cloumn @param value @throws FrameworkDAOException 参数说明 @return
     *         void 返回类型 @throws
     */
    <T> void deleteEntityByCondition(Class<T> tc, String column, Object value) throws FrameworkDAOException;

    /**
     * @Title: deleteEntityByCondition @Description: 物理删除记录 @param tc @param
     *         columns @param values @throws FrameworkDAOException 参数说明 @return
     *         T 返回类型 @throws
     */
    <T> void deleteEntityByCondition(Class<T> tc, String[] columns, Object[] values) throws FrameworkDAOException;

}
