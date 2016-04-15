/**
 * copywrite 2015-2020 金地物业
 * 不能修改和删除上面的版权声明
 * 此代码属于数据与信息中心部门编写，在未经允许的情况下不得传播复制
 * SmsQueueStrategy.java
 * @Date 2015年10月22日 上午11:36:48
 * guguihe
 */
package com.icloudmoo.business.msg.sender.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.icloudmoo.business.msg.sender.ISmsSender;

/**
 * TODO:队列切换策略，按队列顺序使用通道，除非第一个使用的通道挂掉，否则不切换
 * 
 * @author guguihe
 * @Date 2015年10月22日 上午11:36:48
 */
public class SmsRandomStrategy extends AbstractSmsStrategy {

    /**
     * 
     */
    public SmsRandomStrategy() {
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
        Set<Integer> keys = senders.keySet();
        if(keys.size() <= 0){
            throw new RuntimeException("短信通道已全部失效，请检查！！");
        }
        List<Integer> keyList = new ArrayList<>(keys);
        int key = RandomUtils.nextInt(keyList.size());
        return senders.get(keyList.get(key));
    }

}
