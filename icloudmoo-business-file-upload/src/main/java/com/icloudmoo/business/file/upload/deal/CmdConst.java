package com.icloudmoo.business.file.upload.deal;

/**
 * @description 图像传输命令常量
 * @date 2013-2-20
 */
public class CmdConst {
	public static final String UPLOAD = "upload";

	/**
	 * 成功
	 */
	public static final String STATUS_SUCCESS = "200";
	/**
	 * 协议格式错误
	 */
	public static final String STATUS_TCP_FORMAT_ERROR = "501";
	/**
	 * 会话超时失效
	 */
	public static final String STATUS_SESSION_TIMEOUT = "502";
	
	/**
	 * 会话超时失效
	 */
	public static final String STATUS_NOLOGIN_OR_SESSION_TIMEOUT = "523";
	/**
	 * 用户名或密码校验失败
	 */
	public static final String STATUS_LOGIN_FAILTURE = "521";
	/**
	 * 权限不足
	 */
	public static final String STATUS_ACCESS_PERMISSION = "522";
	/**
	 * 文件格式错误
	 */
	public static final String STATUS_FILE_FORMAT_ERROR = "551";
	public static final String STATUS_FILE_SAVE_ERROR = "552";
}
