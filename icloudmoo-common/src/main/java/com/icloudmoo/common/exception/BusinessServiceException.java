package com.icloudmoo.common.exception;


public class BusinessServiceException extends FrameworkException {
	private static final long serialVersionUID = 1L;

	public BusinessServiceException() {
		super();
	}

	public BusinessServiceException(String arg0) {
		super(arg0);
	}

	public BusinessServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BusinessServiceException(Throwable arg0) {
		super(arg0);
	}

}