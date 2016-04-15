package com.icloudmoo.common.controller.databind;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.icloudmoo.common.annotation.EncryptRequest;
import com.icloudmoo.common.exception.FrameworkException;
import com.icloudmoo.common.redis.suport.RedisUtil;
import com.icloudmoo.common.util.CodingUtil;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.util.RSACoder;
import com.icloudmoo.common.vo.BeanInitUtils;
import com.icloudmoo.common.vo.CodeDesc;
import com.icloudmoo.common.vo.Constants;
import com.icloudmoo.common.vo.ResponseCode;

/**
 * @ClassName: RequestConverter
 * @Description: TODO 用于加密数据请求的解析器
 * @author 
 * @date 2015年9月17日 下午3:58:57
 *
 */
public class EncryptRequestResolver implements HandlerMethodArgumentResolver {

	public EncryptRequestResolver() {
		super();
	}

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(EncryptRequest.class) ;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		// 加密数据解密后的最终对象
		RequestObject req = new RequestObject();
		InputStream is = null;
		try {
			// 取得POST提交的字符串
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			is = request.getInputStream();
			String reqLines = IOUtils.toString(is);
			if (StringUtils.isEmpty(reqLines)) {
				return req;
			}

			// 解密前的请求内容
			req.setOriginalBody(reqLines);
			/*
			// logger.info("Original request body: " + reqLines);
			String des3PrivateKey = ConfigureUtil
					.getProperty("des3_private_key_"+request.getAttribute(Constants.HEAD_SYS)+"_"
							+ request.getAttribute(Constants.HEAD_VERSION));
			if (!parameter.hasParameterAnnotation(NormalRequest.class)) {
				// 强解密
				if (parameter.hasParameterAnnotation(EnhanceEncryptReq.class)) {
					// 使用RSA私钥解密
					reqLines = RSACoder.decryptByPrivateKey(reqLines);
				} else {
					// 使用3DES解密
					reqLines = CodingUtil.des3Decode(reqLines, des3PrivateKey);
				}
			}*/
			// 解密后的请求内容
			//logger.info("Decoded request body: " + reqLines);
			req.setDecodedBody(reqLines);
			// 获取真实参数对象
			Type tempType = parameter.getGenericParameterType();
			if (tempType instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) tempType;
				Type[] types = paramType.getActualTypeArguments();
				Gson gson = BeanInitUtils.getGson();
				JsonObject jobj = gson.fromJson(reqLines, JsonObject.class);
				JsonObject head = jobj.getAsJsonObject("head");
				Map<String, String> headMap = new HashMap<>();
				headMap.put(Constants.HEAD_SYS,request.getAttribute(Constants.HEAD_SYS).toString());
				for (Entry<String, JsonElement> key : head.entrySet()) {
					headMap.put(key.getKey(), key.getValue().getAsString());
				}

				// 保存userId，sysId方便使用
				request.setAttribute(Constants.REQUEST_USERID,headMap.get(Constants.REQUEST_USERID));
				// 验证头
				/*if (!parameter.hasParameterAnnotation(NoTokenValidate.class)) {
					CodeDesc validStatus = validateToken(headMap,des3PrivateKey);
					if (validStatus.getCode() < 0) {
						req.setParseError(validStatus.getCode(),
								validStatus.getDesc());
						return req;
					}
					// 验证sessionId
					validStatus = validateSession(headMap);
					if (validStatus.getCode() < 0) {
						req.setParseError(validStatus.getCode(),
								validStatus.getDesc());
						return req;
					}
				}*/
				req.setRequestHead(headMap);
				if (ArrayUtils.isEmpty(types)) {
					req.setRequestData(jobj);
				} else {
					Class pt = (Class) types[0];
					req.setRequestData(gson.fromJson(jobj, pt));
				}

				return req;
			} else {
				return reqLines;
			}
		} catch (Exception e) {
			logger.error("加密数据转换失败！！", e);
			req.setParseError(ResponseCode.FAIL, "请求执行失败：" + e.getMessage());
			return req;
		}

	}

	/**
	 * @Title: validateToken @Description: 验证请求的合法性 @param @param headMap @param @throws
	 *         FrameworkException 参数说明 @return void 返回类型 @throws
	 */
	private CodeDesc validateToken(Map<String, String> headMap,
			String des3PrivateKey) throws FrameworkException {
		String token = headMap.get(Constants.REQUEST_TOKEN);
		CodeDesc status = new CodeDesc();
		status.setValues(ResponseCode.SUCCESS, "Token验证成功！！");
		if (StringUtils.isEmpty(token)) {
			status.setCode(ResponseCode.FAIL);
			status.setDesc("非法请求，TOKEN数据为空！！");
		}

		String original = headMap.get(Constants.REQUEST_SESSIONID)
				+ des3PrivateKey + headMap.get(Constants.REQUEST_USERID)
				+ headMap.get(Constants.REQUEST_TIMESTAMP);
		try {
			String codedStr = CodingUtil.md5(original);
			if (!StringUtils.equalsIgnoreCase(token, codedStr)) {
				status.setCode(ResponseCode.FAIL);
				status.setDesc("非法请求：TOKEN验证失败！！");
			}
		} catch (Exception e) {
			status.setCode(ResponseCode.FAIL);
			status.setDesc("TOKEN验证失败！！");
		}

		return status;
	}

	/**
	 * @Title: validateToken @Description: 验证请求的合法性 @param @param headMap @param @throws
	 *         FrameworkException 参数说明 @return void 返回类型 @throws
	 */
	private CodeDesc validateSession(Map<String, String> headMap) {
		String userId = headMap.get(Constants.REQUEST_USERID);
		CodeDesc status = new CodeDesc();
		status.setValues(ResponseCode.SUCCESS, "sessionId验证成功！！");
		if (StringUtils.isEmpty(userId)) {
			status.setValues(ResponseCode.FAIL, "非法请求，userId数据为空！！");
		}

		String sessionId = headMap.get(Constants.REQUEST_SESSIONID);
		String sysSessionId = RedisUtil
				.redisQueryObject(Constants.SESSIONID_FLAG +headMap.get(Constants.HEAD_SYS)+"_"+userId);
		if (!StringUtils.equalsIgnoreCase(sysSessionId, sessionId)) {
			status.setValues(ResponseCode.SESSION_VALID_FAIL,
					"非法请求：sessionId验证失败！！");
		}

		return status;
	}
}
