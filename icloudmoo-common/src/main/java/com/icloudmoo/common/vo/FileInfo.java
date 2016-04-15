/**
 * 
 */
package com.icloudmoo.common.vo;

import java.io.Serializable;

/**
 * @description：
 *
 * @author Guguihe
 *
 * @date 2015年5月13日 下午6:25:04
 */
public class FileInfo implements Serializable {
	
	private static final long serialVersionUID = -1576089136035069517L;
	
	private String path;
	private String fileName;
	private String contentType;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
