package com.icloudmoo.common.base.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.icloudmoo.common.exception.FrameworkDAOException;
import com.icloudmoo.common.vo.PaginatedList;
import com.icloudmoo.common.vo.ParameterInfo;

/**
 * @ClassName: BaseDaoSupport
 * @Description: 数据层基础实现类
 * @author 信息管理部-gengchong
 * @date 2015年8月17日 上午10:22:40
 * 
 */
public abstract class BaseDaoSupport implements BaseDAO {

	final protected Log logger = LogFactory.getLog(getClass());

	// @Fields jdbcTemplate : 读写数据源

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	// @Fields jdbcReadTemplate : 只读数据源

	@Autowired
	protected JdbcTemplate jdbcReadTemplate;
	
	   /**
     * @param <T>
     * @description 分页获取数据
     * @param countSql
     *            查询数据总数的SQL语句
     * @param countParams
     *            查询数据总数的SQL参数
     * @param dataSql
     *            查询数据的SQL语句
     * @param dataParams
     *            查询数据的SQL参数
     * @param rowMapper
     *            数据实体转换
     * @param pageSize
     *            每页数据量
     * @param pageNumber
     *            当前页码
     * @return 分页获取到的数据
     * @throws FrameworkDAOException
     *             PaginatedList
     */
    @SuppressWarnings("unchecked")
    protected PaginatedList<?> queryForPaginatedMap(String countSql,
            List<?> countParams, String dataSql, List<?> dataParams,
            RowMapper<?> rowMapper, int pageSize, int pageNumber, boolean read)
            throws FrameworkDAOException {
        if (StringUtils.isEmpty(countSql) || StringUtils.isEmpty(dataSql)
                || null == rowMapper) {
            throw new FrameworkDAOException("必要条件为空，无法进行查询！！");
        }

        logger.info("Count SQL: " + countSql);
        logger.info("Data SQL: " + dataSql);
        long start = System.currentTimeMillis();

        try {
            int count = CollectionUtils.isEmpty(countParams) ? (read
                    ? getJdbcReadTemplate()
                    : getJdbcTemplate())
                    .queryForObject(countSql, Integer.class) : (read
                    ? getJdbcReadTemplate()
                    : getJdbcTemplate()).queryForObject(countSql,
                    countParams.toArray(), Integer.class);

            List<?> datas = CollectionUtils.isEmpty(dataParams) ? (read
                    ? getJdbcReadTemplate()
                    : getJdbcTemplate()).query(dataSql, rowMapper) : (read
                    ? getJdbcReadTemplate()
                    : getJdbcTemplate()).query(dataSql, dataParams.toArray(),
                    rowMapper);

            @SuppressWarnings("rawtypes")
            PaginatedList pl = new PaginatedList();
            pl.setPageCount((int) Math.round(((double) count / pageSize)));
            pl.setPageCount(0 == pl.getPageCount() ? 1 : pl.getPageCount());

            if (pageNumber < 1) {
                pageNumber = 1;
            }
            pl.setPageSize(pageSize);
            pl.setPageNumber(pageNumber);
            pl.setStartRownum(pageSize * (pageNumber - 1) + 1);
            pl.setEndRownum(pl.getStartRownum() + pageSize - 1);
            pl.setRowCount(count);
            pl.setResults(datas);
            logger.info("分页查询耗时：" + (System.currentTimeMillis() - start)
                    + "毫秒！");
            return pl;
        } catch (Exception e) {
            logger.error("分页查询数据异常", e);
            throw new FrameworkDAOException(e);
        }
    }

	/**
	 * @param <T>
	 * @description 分页获取数据
	 * @param countSql
	 *            查询数据总数的SQL语句
	 * @param countParams
	 *            查询数据总数的SQL参数
	 * @param dataSql
	 *            查询数据的SQL语句
	 * @param dataParams
	 *            查询数据的SQL参数
	 * @param rowMapper
	 *            数据实体转换
	 * @param pageSize
	 *            每页数据量
	 * @param pageNumber
	 *            当前页码
	 * @return 分页获取到的数据
	 * @throws FrameworkDAOException
	 *             PaginatedList
	 */
	@SuppressWarnings("unchecked")
	protected <T> PaginatedList<T> queryForPaginatedList(String countSql,
			List<?> countParams, String dataSql, List<?> dataParams,
			RowMapper<T> rowMapper, int pageSize, int pageNumber, boolean read)
			throws FrameworkDAOException {
		if (StringUtils.isEmpty(countSql) || StringUtils.isEmpty(dataSql)
				|| null == rowMapper) {
			throw new FrameworkDAOException("必要条件为空，无法进行查询！！");
		}

		logger.info("Count SQL: " + countSql);
		logger.info("Data SQL: " + dataSql);
		long start = System.currentTimeMillis();

		try {
			int count = CollectionUtils.isEmpty(countParams) ? (read
					? getJdbcReadTemplate()
					: getJdbcTemplate())
					.queryForObject(countSql, Integer.class) : (read
					? getJdbcReadTemplate()
					: getJdbcTemplate()).queryForObject(countSql,
					countParams.toArray(), Integer.class);

			List<?> datas = CollectionUtils.isEmpty(dataParams) ? (read
					? getJdbcReadTemplate()
					: getJdbcTemplate()).query(dataSql, rowMapper) : (read
					? getJdbcReadTemplate()
					: getJdbcTemplate()).query(dataSql, dataParams.toArray(),
					rowMapper);

			@SuppressWarnings("rawtypes")
			PaginatedList pl = new PaginatedList();
			pl.setPageCount((int) Math.round(((double) count / pageSize)));
			pl.setPageCount(0 == pl.getPageCount() ? 1 : pl.getPageCount());

			if (pageNumber < 1) {
				pageNumber = 1;
			}
			pl.setPageSize(pageSize);
			pl.setPageNumber(pageNumber);
			pl.setStartRownum(pageSize * (pageNumber - 1) + 1);
			pl.setEndRownum(pl.getStartRownum() + pageSize - 1);
			pl.setRowCount(count);
			pl.setResults(datas);
			logger.info("分页查询耗时：" + (System.currentTimeMillis() - start)
					+ "毫秒！");
			return pl;
		} catch (Exception e) {
			logger.error("分页查询数据异常", e);
			throw new FrameworkDAOException(e);
		}
	}

	/**
	 * @description 分页获取数据
	 * @param countSql
	 *            查询数据总数的SQL语句
	 * @param countParams
	 *            查询数据总数的SQL参数
	 * @param dataSql
	 *            查询数据的SQL语句
	 * @param dataParams
	 *            查询数据的SQL参数
	 * @param rowMapper
	 *            数据实体转换
	 * @param pageSize
	 *            每页数据量
	 * @param pageNumber
	 *            当前页码
	 * @return 分页获取到的数据
	 * @throws FrameworkDAOException
	 *             PaginatedList
	 */
	@SuppressWarnings("rawtypes")
	protected PaginatedList queryForPaginatedList(String dataSql,
			List dataParams, RowMapper rowMapper, int pageSize, int pageNumber,
			boolean read) throws FrameworkDAOException {
		if (StringUtils.isEmpty(dataSql) || null == rowMapper) {
			throw new FrameworkDAOException("必要条件为空，无法进行查询！！");
		}

		String temp = dataSql;
		temp = temp.toLowerCase();
		String countSql = "select count(*) "
				+ dataSql.substring(temp.indexOf("from"));
		dataSql += " limit " + String.valueOf((pageNumber - 1) * pageSize)
				+ "," + String.valueOf(pageSize);
		return queryForPaginatedList(countSql, dataParams, dataSql, dataParams,
				rowMapper, pageSize, pageNumber, read);
	}

	/**
	 * @description 分页获取group by数据 @param @return PaginatedList @throws
	 */
	@SuppressWarnings("rawtypes")
	protected PaginatedList queryForPaginatedListByGroup(String dataSql,
			List dataParams, RowMapper rowMapper, int pageSize, int pageNumber,
			boolean read) throws FrameworkDAOException {
		String countSql = "select count(*) from (" + dataSql + ") as temp";
		dataSql += " limit " + String.valueOf((pageNumber - 1) * pageSize)
				+ "," + String.valueOf(pageSize);
		return queryForPaginatedList(countSql, dataParams, dataSql, dataParams,
				rowMapper, pageSize, pageNumber, read);
	}

	/*
	 * <p>Title: save</p> <p>Description: </p>
	 * 
	 * @param obj
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see com.gemdale.gmap.common.dao.support.BaseDAO#save(java.lang.Object)
	 */
	public <T> Integer save(T obj) throws FrameworkDAOException {

		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateInsertSQLByObject(obj);
			final String sql = sqlResult.get("sql").toString();
			logger.info("sql:" + sql);
			@SuppressWarnings("unchecked")
			final LinkedList<Object> valueList = (LinkedList<Object>) sqlResult
					.get("valueList");
			KeyHolder keyHolder = new GeneratedKeyHolder();

			getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < valueList.size(); i++) {
						ps.setObject(i + 1, valueList.get(i));
					}
					return ps;
				}
			}, keyHolder);

			return keyHolder.getKey().intValue();

		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gemdale.gmap.common.dao.support.BaseDAO#saveAll(java.util.List)
	 */
	public <T> void saveAll(List<T> list) throws FrameworkDAOException {
		try {
			if (CollectionUtils.isEmpty(list)) {
				throw new FrameworkDAOException("要保存的对象不存在！");
			}

			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateInsertSQLByList(list);
			final String sql = sqlResult.get("sql").toString();
			logger.info("sql:" + sql);
			@SuppressWarnings("unchecked")
			final LinkedList<Object> valueList = (LinkedList<Object>) sqlResult
					.get("valueList");

			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					@SuppressWarnings("unchecked")
					LinkedList<Object> tempValueList = (LinkedList<Object>) valueList
							.get(i);
					for (int j = 0; j < tempValueList.size(); j++) {
						ps.setObject(j + 1, tempValueList.get(j));
					}
				}

				public int getBatchSize() {
					return valueList.size();
				}
			});

		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: update</p> <p>Description: </p>
	 * 
	 * @param obj
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see com.gemdale.gmap.common.dao.support.BaseDAO#update(java.lang.Object)
	 */
	@Override
	public <T> void update(T obj) throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateUpdateSQLByObject(obj);
			@SuppressWarnings("unchecked")
			final LinkedList<Object> valueList = (LinkedList<Object>) sqlResult
					.get("valueList");
			logger.info("更新语句：" + sqlResult.get("sql").toString());
			getJdbcTemplate().update(sqlResult.get("sql").toString(),
					valueList.toArray());
		} catch (Exception e) {
			throw new FrameworkDAOException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.gemdale.gmap.common.dao.support.BaseDAO#updateEntity(java.lang.Object, java.lang.String[], java.lang.Object[], java.lang.String[])
	 */
	@Override
	public <T> void updateEntity(Class<T> t, String[] fields, String[] wheres, Object[] values)
	        throws FrameworkDAOException {
	    try{
	        String updateSql = GenerateSQLUtil.generateUpdateSql(t, fields, wheres);
	        logger.info("UPDATE SQL：" + updateSql);
	        getJdbcTemplate().update(updateSql, values);
	    }catch(Exception e){
	        throw new FrameworkDAOException(e);
	    }
	}

	@Override
	public <T> void updateCount(Class<T> t, String column, Integer id,
			Integer count) throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateUpdateCountSQLByClass(t, column);
			getJdbcTemplate().update(sqlResult.get("sql").toString(),
					new Object[]{count, id});
		} catch (Exception e) {
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: findById</p> <p>Description: </p>
	 * 
	 * @param t
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findById(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public <T> T findById(Class<T> t, Serializable id)
			throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateFindSQLByClass(t);
			List<T> result = getJdbcTemplate().query(
					sqlResult.get("sql").toString(), new Object[]{id},
					new ObjectRowMapper<T>(t));
			return CollectionUtils.isEmpty(result) ? null : result.get(0);
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: findEntityByCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param column
	 * 
	 * @param value
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findEntityByCondition(java.
	 * lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T> T findEntityByCondition(Class<T> tc, String column, Object value)
			throws FrameworkDAOException {
		try {
			return findEntityByCondition(tc, new String[]{column},
					new Object[]{value});
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: findEntityByCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param columns
	 * 
	 * @param values
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findEntityByCondition(java.
	 * lang.Class, java.lang.String[], java.lang.Object[])
	 */
	public <T> T findEntityByCondition(Class<T> t, String[] columns,
			Object[] values) throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateFindEntitySQLByClass(t, columns);
			List<T> result = getJdbcTemplate().query(
					sqlResult.get("sql").toString(), values,
					new ObjectRowMapper<T>(t));
			return CollectionUtils.isEmpty(result) ? null : result.get(0);
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: findEntityByCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param columns
	 * 
	 * @param values
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findEntityByCondition(java.
	 * lang.Class, java.lang.String[], java.lang.Object[])
	 */
	public <T> List<T> findListByCondition(Class<T> t, String columns,
			Object values) throws FrameworkDAOException {
		try {
			return findListByCondition(t, new String[]{columns},
					new Object[]{values});
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: findEntityByCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param columns
	 * 
	 * @param values
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findEntityByCondition(java.
	 * lang.Class, java.lang.String[], java.lang.Object[])
	 */
	public <T> List<T> findListByCondition(Class<T> t, String[] columns,
			Object[] values) throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateFindEntitySQLByClass(t, columns);
			return getJdbcTemplate().query(sqlResult.get("sql").toString(),
					values, new ObjectRowMapper<T>(t));
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}
	}

	/*
	 * <p>Title: findEntityByListCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param columns
	 * 
	 * @param values
	 * 
	 * @param orderBy
	 * 
	 * @param orderType
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findEntityByListCondition(
	 * java.lang.Class, java.lang.String[], java.lang.Object[],
	 * java.lang.String, java.lang.String)
	 */
	public <T> List<T> findEntityByListCondition(Class<T> t, String[] columns,
			Object[] values, String orderBy, String orderType)
			throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateFindEntitySQLByClass(t, null == columns
							? new String[]{}
							: columns);
			String sql = sqlResult.get("sql").toString();
			sql = StringUtils.isEmpty(orderBy) ? sql : sql + " ORDER BY "
					+ orderBy;
			sql = StringUtils.isEmpty(orderType) ? sql : sql + " " + orderType;

			return getJdbcTemplate().query(sql,
					null == values ? new Object[]{} : values,
					new ObjectRowMapper<T>(t));
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}

	}
	
	
	   /* (non-Javadoc)
	 * @see com.gemdale.gmap.common.dao.support.BaseDAO#findEntityByListCondition(java.lang.Class, java.lang.String[], java.lang.Object[], java.lang.String, java.lang.String)
	 */
	public <T> List<T> findEntityUnequalByListCondition(Class<T> t, String column,
	            Object value, String orderBy, String orderType)
	            throws FrameworkDAOException {
	        try {
	            Map<String, Object> sqlResult = GenerateSQLUtil
	                    .generateFindEntityUnequalSQLByClass(t, column);
	            String sql = sqlResult.get("sql").toString();
	            sql = StringUtils.isEmpty(orderBy) ? sql : sql + " ORDER BY "
	                    + orderBy;
	            sql = StringUtils.isEmpty(orderType) ? sql : sql + " " + orderType;

	            return getJdbcTemplate().query(sql,new Object[]{value},
	                    new ObjectRowMapper<T>(t));
	        } catch (Exception e) {
	            logger.error(e);
	            throw new FrameworkDAOException(e);
	        }

	    }

	/*
	 * <p>Title: deleteEntityByCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param cloumn
	 * 
	 * @param value
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#deleteEntityByCondition(java.
	 * lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T> void deleteEntityByCondition(Class<T> tc, String column,
			Object value) throws FrameworkDAOException {
		try {
			deleteEntityByCondition(tc, new String[]{column},
					new Object[]{value});
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}

	}

	/*
	 * <p>Title: deleteEntityByCondition</p> <p>Description: </p>
	 * 
	 * @param tc
	 * 
	 * @param columns
	 * 
	 * @param values
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#deleteEntityByCondition(java
	 * .lang.Class, java.lang.String[], java.lang.Object[])
	 */
	public <T> void deleteEntityByCondition(Class<T> tc, String[] columns,
			Object[] values) throws FrameworkDAOException {
		try {
			Map<String, Object> sqlResult = GenerateSQLUtil
					.generateDeleteSQLByClass(tc, columns);
			String sql = sqlResult.get("sql").toString();
			getJdbcTemplate().update(sql, values);
		} catch (Exception e) {
			logger.error(e);
			throw new FrameworkDAOException(e);
		}

	}

	/*
	 * <p>Title: findForPage</p> <p>Description: </p>
	 * 
	 * @param param
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findForPage(com.gemdale.gmap.
	 * common.vo.ParameterInfo)
	 */
	public <T> PaginatedList<T> findForPage(ParameterInfo param)
			throws FrameworkDAOException {
		return null;
	}

	/*
	 * <p>Title: findForPage</p> <p>Description: </p>
	 * 
	 * @param param
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findForPage(java.util.Map)
	 */
	public <T> PaginatedList<T> findForPage(Map<String, Object> param)
			throws FrameworkDAOException {
		return null;
	}

	/*
	 * <p>Title: findForApp</p> <p>Description: </p>
	 * 
	 * @param param
	 * 
	 * @return
	 * 
	 * @throws FrameworkDAOException
	 * 
	 * @see
	 * com.gemdale.gmap.common.dao.support.BaseDAO#findForApp(com.gemdale.gmap.
	 * common.vo.ParameterInfo)
	 */
	public <T> List<T> findForApp(ParameterInfo param)
			throws FrameworkDAOException {
		return null;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @return the jdbcReadTemplate
	 */
	public JdbcTemplate getJdbcReadTemplate() {
		return jdbcReadTemplate;
	}

	/**
	 * @param jdbcReadTemplate
	 *            the jdbcReadTemplate to set
	 */
	public void setJdbcReadTemplate(JdbcTemplate jdbcReadTemplate) {
		this.jdbcReadTemplate = jdbcReadTemplate;
	}

}
