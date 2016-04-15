/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * SmsQueueStrategy.java
 * @Date 2015年10月22日 上午11:36:48
 * guguihe
 */
package com.icloudmoo.business.msg.sender.strategy.impl;

import com.icloudmoo.business.msg.sender.ISmsSender;
import com.icloudmoo.common.util.ConfigureUtil;

/**
 * TODO:队列切换策略，按队列顺序使用通道，除非第一个使用的通道挂掉，否则不切换
 * 
 * @author guguihe
 * @Date 2015年10月22日 上午11:36:48
 */
public class SmsSequenceStrategy extends AbstractSmsStrategy {

    /**
     * 
     */
    public SmsSequenceStrategy() {
        initSenders();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gemdale.business.msg.sender.strategy.ISmsChannelStrategy#getSender()
     */
    @Override
    public ISmsSender getSender() {
        if (senders.size() <= 0) {
            throw new RuntimeException("短信通道已全部失效，请检查！！");
        }
        
        // 短信通道切换顺序
        Integer[] queue = ConfigureUtil.getArray("sms.strategy.queue");
        ISmsSender sender = null;
        for (Integer q : queue) {
            sender = senders.get(q);
            if (null != sender) {
                break;
            }
        }

        return sender;
    }

}
