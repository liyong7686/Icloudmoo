package com.icloudmoo.business.msg.sender;

import java.util.List;

import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.common.vo.SendMessage;


/**
 * TODO:短信发送接口
 */
public interface ISmsSender extends ISender {

    /**
     * TODO:获取当前使用的短信发送通道
     * @return
     */
    int getSenderType();

    /**
     * TODO: 获取短信发送报告
     */
    void getSendReport() throws MessageSendException;

    /**
     * TODO: 获取短信发送回执
     * @param message
     * @return
     */
    List<SendMessage> getSmsReceipt(SendMessage message) throws MessageSendException;
}
