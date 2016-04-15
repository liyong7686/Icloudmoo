package com.icloudmoo.business.file.upload.vo;

import com.icloudmoo.common.vo.ValueObject;

/**
 * @description TODO
 * @date 2013-2-20
 */
public class TransferResponse extends ValueObject {
	private static final long serialVersionUID = 1L;

	/**
	 * 处理结果状态
	 */
	private String status = "200";

	/**
	 * 顺序编号
	 */
	private String serialNo;

	/**
	 * session
	 */
	private String session;
	
	private String path;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
