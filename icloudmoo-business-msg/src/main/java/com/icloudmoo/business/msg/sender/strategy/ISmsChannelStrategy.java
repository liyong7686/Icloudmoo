package com.icloudmoo.business.msg.sender.strategy;

import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.common.vo.SendMessage;

/**
 * TODO:短信通道切换策略接口
 * 
 * @author guguihe
 * @Date 2015年10月22日 上午11:06:50
 */
public interface ISmsChannelStrategy {

    /**
     * 随机使用通道
     */
    int RANDOM = 0;

    /**
     * 顺序轮换通道
     */
    int SEQUENCE = 1;

    /**
     * 按队列顺序使用通道，除非第一个使用的通道挂掉，否则不切换
     */
    int QUEUE = 2;

    /**
     * TODO: 获取发送接口
     * 
     * @return
     */
    ISmsSender getSender();

    /**
     * TODO: 发送短信
     * 
     * @return
     */
    boolean sendMessage(SendMessage msg);

    /**
     * TODO: 初始化所有可用的发送接口
     */
    void initSenders();

    /**
     * TODO: 把发送服务置为错误状态
     * 
     * @param senderType
     */
    void setSenderError(int senderType);

    /**
     * TODO: 增加发送服务，也可用于恢复错误的推送服务
     * 
     * @param senderType
     */
    void addSender(int senderType);
}
