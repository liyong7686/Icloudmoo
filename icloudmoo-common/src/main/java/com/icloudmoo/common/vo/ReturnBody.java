/**
 * @Title: ResponseBody.java
 * @Package com.gemdale.gmap.common.vo
 * @Description: TODO（用一句话描述这个文件的用途）
 * @Copyright: Copyright (c) 2015-2018 此代码属于金地物业信息管理部，在未经允许的情况下禁止复制传播
 * @Company:金地物业
 * @author 信息管理部-guguihe
 * @date 2015年9月16日 下午5:34:46
 */
package com.icloudmoo.common.vo;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.cglib.core.ReflectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icloudmoo.common.annotation.BusinessVo;
import com.icloudmoo.common.annotation.EncryptResponse;

/**
 * @ClassName: ResponseBody
 * @Description: TODO（用一句话描述这个类的用途）
 * @author 信息管理部-guguihe
 * @date 2015年9月16日 下午5:34:46
 * 
 */
@EncryptResponse
public class ReturnBody implements Serializable {

	private static final long serialVersionUID = -4741161624320621334L;

	// @Fields code :返回值
	private int code = ResponseCode.SUCCESS;

	// @Fields descript : 返回描述
	private String desc = "请求执行成功！";
	
	/**
	 * 检查点编码，用于特殊检查点的处理
	 */
	@JsonIgnore
	private String checkpointCode;

	// @Fields result : 返回结果
	private Object result;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setResponseCode(CodeDesc codeDesc) {
		this.code = codeDesc.getCode();
		this.desc = codeDesc.getDesc();
	}

	public Object getResult() {
		try {
			Object temp = transToVo(result);
			if (null != temp) {
				return temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getCheckpointCode() {
        return checkpointCode;
    }

    public void setCheckpointCode(String checkpointCode) {
        this.checkpointCode = checkpointCode;
    }

    private Object transToVo(Object result) throws Exception {
		if (null != result) {
			if (result instanceof Collection) {
				Collection<?> c = (Collection<?>) result;
				List<Object> tempList = new ArrayList<>(c.size());
				for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
					Object temp = iter.next();
					tempList.add(getVoFields(temp.getClass(), temp));
				}

				return tempList;
			} else if (result instanceof Object[]) {
				Object[] o = (Object[]) result;
				List<Object> tempList = new ArrayList<>(o.length);
				for (Object oo : o) {
					tempList.add(getVoFields(oo.getClass(), oo));
				}

				return tempList;
			} else if (result instanceof Map) {
				Map<Object, Object> m = (Map<Object, Object>) result;
				Map<Object, Object> tempMap = new HashMap<>();
				for (Map.Entry<Object, Object> entry : m.entrySet()) {
					Object type = entry.getValue();
					if (type instanceof Collection) {
						tempMap.put(entry.getKey(), transToVo(type));
					} else {
						if (type != null && entry.getValue().getClass() != null
								&& entry.getValue() != null) {
							tempMap.put(
									entry.getKey(),
									getVoFields(entry.getValue().getClass(),
											entry.getValue()));
						}
					}
				}

				return tempMap;
			} else {
				Class<?> clazz = (Class<?>) result.getClass();
				return getVoFields(clazz, result);
			}
		}

		return null;
	}

	private Object getVoFields(Class<?> claxx, Object obj) throws Exception {
		BusinessVo businessVo = claxx.getAnnotation(BusinessVo.class);
		if (null != businessVo) {
			PropertyDescriptor[] beanInfos = ReflectUtils.getBeanGetters(obj
					.getClass());
			String[] vos = businessVo.vos();
			Map<String, Object> voMap = new HashMap<>();
			String fieldName = null;
			Method readMethod = null;
			Object value = null;
			// 未定义VOS时检测igs
			if (ArrayUtils.isEmpty(vos)) {
				String[] igs = businessVo.igs();
				Set<String> igsSet = new HashSet<>(Arrays.asList(igs));
				for (PropertyDescriptor beanInfo : beanInfos) {
					fieldName = beanInfo.getName();
					if (igsSet.contains(fieldName)) {
						continue;
					}

					readMethod = beanInfo.getReadMethod();
					value = readMethod.invoke(obj);
					if (value != null && Date.class.equals(readMethod.getReturnType())) {
						value = DateFormatUtils.format((Date) value,
								StringUtils.isEmpty(businessVo.format())
										? "yyyy-MM-dd HH:mm:ss"
										: businessVo.format());
					}

					voMap.put(fieldName, value);
				}

				igsSet.clear();
			} else {
				Set<String> igsSet = new HashSet<>(Arrays.asList(vos));
				for (PropertyDescriptor beanInfo : beanInfos) {
					fieldName = beanInfo.getName();
					if (igsSet.contains(fieldName)) {
						readMethod = beanInfo.getReadMethod();
						value = readMethod.invoke(obj);
						if (value != null &&Date.class.equals(readMethod.getReturnType())) {
							value = DateFormatUtils.format((Date) value,
									StringUtils.isEmpty(businessVo.format())
											? "yyyy-MM-dd HH:mm:ss"
											: businessVo.format());
						}

						voMap.put(fieldName, value);
					}
				}

				igsSet.clear();
			}

			return voMap;
		}

		return obj;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setSuccess() {
		setCode(ResponseCode.SUCCESS);
		setDesc("请求执行成功！");
	}

	public void setFail() {
		setCode(ResponseCode.FAIL);
		setDesc("请求执行失败！");
	}
}
