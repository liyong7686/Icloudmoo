/**
 * @Title: RequestObject.java
 * @Package com.gemdale.gmap.common.controller.databind
 * @Description: TODO（用一句话描述这个文件的用途）
 * @Copyright: Copyright (c) 2015-2018 此代码属于金地物业信息管理部，在未经允许的情况下禁止复制传播
 * @Company:金地物业
 * @author 信息管理部-guguihe
 * @date 2015年9月17日 下午2:24:19
 */
package com.icloudmoo.common.controller.databind;

import java.io.Serializable;
import java.util.Map;

import com.icloudmoo.common.vo.CodeDesc;

/**
 * @ClassName: RequestObject
 * @Description: TODO（用一句话描述这个类的用途）
 * @author 信息管理部-guguihe
 * @date 2015年9月17日 下午2:24:19
 *
 */
public class RequestObject<T> implements Serializable {

    private static final long serialVersionUID = -5868911619010434045L;

    // @Fields originalBody : 原始請求字符串
    private String originalBody;

    // @Fields originalBody : 请求转换错误信息
    private CodeDesc parseError;

    // @Fields originalBody : 请求转换是否成功
    private boolean isSuccess = true;

    // @Fields decodedBody : 解密后的请求字符串
    private String decodedBody;

    // @Fields requestHead : 请求头部对象
    private Map<String, String> requestHead;

    // @Fields requestData : 请求的数据结构
    private T requestData;

    public String getOriginalBody() {
        return originalBody;
    }

    public void setOriginalBody(String originalBody) {
        this.originalBody = originalBody;
    }

    public String getDecodedBody() {
        return decodedBody;
    }

    public void setDecodedBody(String decodedBody) {
        this.decodedBody = decodedBody;
    }

    public Map<String, String> getRequestHead() {
        return requestHead;
    }

    public void setRequestHead(Map<String, String> requestHead) {
        this.requestHead = requestHead;
    }

    public T getRequestData() {
        return requestData;
    }

    public CodeDesc getParseError() {
        return parseError;
    }

    public void setParseError(CodeDesc parseError) {
        this.parseError = parseError;
        setSuccess(false);
    }

    public void setParseError(int code, String desc) {
        CodeDesc status = new CodeDesc();
        status.setCode(code);
        status.setDesc(desc);
        this.parseError = status;
        setSuccess(false);
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFail() {
        return !isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
