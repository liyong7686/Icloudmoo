package com.icloudmoo.business.msg.exception;

/**
 * TODO:
 * @author liyong222
 * @Date 2015年12月18日 下午4:54:06
 */
public class MessageSendException extends Exception{
    /**
     * 
     */
    private static final long serialVersionUID = -6032879786258189842L;

    /**
     * 
     */
    public MessageSendException() {
        super();
    }

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(Throwable t) {
        super(t);
    }

    public MessageSendException(String message, Throwable t) {
        super(message, t);
    }
}
