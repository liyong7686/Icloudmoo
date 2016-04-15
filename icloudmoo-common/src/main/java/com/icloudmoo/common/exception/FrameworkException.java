package com.icloudmoo.common.exception;

public class FrameworkException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 保存原始异常对象
	 */
	public FrameworkException() {
		super();
	}

	public FrameworkException(String arg0) {		
		super(arg0);
	}

	public FrameworkException(String arg0, Throwable arg1) {		
		super(arg0, arg1);

	}

	public FrameworkException(Throwable arg0) {
		super(arg0);

	}
	public Throwable getInitialCause(){
			Throwable cause=this.getCause();
			//如果上级对象为null
			if(cause!=null){
				if(cause instanceof FrameworkException){
					return ((FrameworkException)cause).getInitialCause();
				}
				return cause;
			}
			else{
				return this;
			}
		
	}
}
