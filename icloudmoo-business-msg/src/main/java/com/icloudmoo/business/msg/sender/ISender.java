
package com.icloudmoo.business.msg.sender;

import com.icloudmoo.business.msg.exception.MessageSendException;
import com.icloudmoo.common.vo.SendMessage;

public interface ISender {

    /**
     * TODO: 发送消息
     * @param message 消息
     * @return
     * @throws MessageSendException 异常
     */
    boolean sendMessage(SendMessage message) throws MessageSendException;

}
