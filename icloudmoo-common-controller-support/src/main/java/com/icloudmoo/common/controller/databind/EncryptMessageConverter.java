package com.icloudmoo.common.controller.databind;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.icloudmoo.common.annotation.EncryptResponse;
import com.icloudmoo.common.util.CodingUtil;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.util.RSACoder;
import com.icloudmoo.common.vo.Constants;

/**
 * @ClassName: GsonHttpMessageConverter
 * @Description: TODO 用于返回加密数据的解析器
 * @author 
 * @date 2015年9月17日 下午5:48:07
 *
 */
public class EncryptMessageConverter extends MappingJackson2HttpMessageConverter {

    @Autowired
    private HttpServletRequest request;
    
    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        try {
            Object appRes = object.getClass().getAnnotation(EncryptResponse.class);
          //  appRes = (null == appRes) ? object.getClass().getAnnotation(EnhanceEncryptResponse.class) : appRes;
            // 需要加密返回的对象标识
            if (null != appRes) {
            	request.setAttribute(Constants.RETURN_BODY, object);
                String result = objectMapper.writeValueAsString(object);
                logger.info("Original response JSON: " + result);
                // 强加密请求使用RSA加密
              /*  if (appRes instanceof EnhanceEncryptResponse) {
                    result = RSACoder.encryptByPrivateKey(result);
                }
                else {
                    // 使用3DES加密数据
                    String des3PrivateKey = ConfigureUtil.getProperty("des3_private_key_" +request.getAttribute(Constants.HEAD_SYS)+"_"+ request.getAttribute(Constants.HEAD_VERSION));
                    result = CodingUtil.des3Encode(result, des3PrivateKey);
                }*/

                IOUtils.write(result, outputMessage.getBody(), Constants.DEFAULT_CHARSET);
                //logger.info("Encoded response JSON: " + result);
            }
            else {
                JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
                JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(),
                        encoding);
                writePrefix(generator, object);
                Class<?> serializationView = null;
                FilterProvider filters = null;
                Object value = object;
                JavaType javaType = null;
                if (object instanceof MappingJacksonValue) {
                    MappingJacksonValue container = (MappingJacksonValue) object;
                    value = container.getValue();
                    serializationView = container.getSerializationView();
                    filters = container.getFilters();
                }
                if (ClassUtils.hasMethod(ObjectMapper.class, "setDefaultPrettyPrinter", PrettyPrinter.class)
                        && type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
                    javaType = getJavaType(type, null);
                }
                ObjectWriter objectWriter;
                if (serializationView != null) {
                    objectWriter = this.objectMapper.writerWithView(serializationView);
                }
                else if (filters != null) {
                    objectWriter = this.objectMapper.writer(filters);
                }
                else {
                    objectWriter = this.objectMapper.writer();
                }
                if (javaType != null) {
                    objectWriter = objectWriter.withType(javaType);
                }
                objectWriter.writeValue(generator, value);

                writeSuffix(generator, object);
                generator.flush();
            }
        }
        catch (Exception ex) {
            throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
        }
    }
}
