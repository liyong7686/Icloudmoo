package com.icloudmoo.common.redis.suport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

/**
 * TODO:缓存工具类
 * @author liyong
 * @Date 2015年12月18日 上午11:04:55
 */
public class RedisUtil {
    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * TODO: 初始化RedisTemplate
     */
    public static void initRedisTemplate(final RedisTemplate<String, Object> sprintRedisTemplate) {
        redisTemplate = sprintRedisTemplate;
    }

    /**
     * @param <T>
     * @description 获取 redis中的对象
     * @param
     * @return void
     * @throws
     */
    public static <T> T redisQueryObject(final RedisTemplate<String, Object> redisTemplate, final String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (T) ops.get(key);
    }

    /**
     * @description 删除redis中指定的key
     * @param
     * @return void
     * @throws
     */
    public static void redisDeleteKey(final RedisTemplate<String, Object> redisTemplate, final String key) {
        redisTemplate.delete(key);
    }

    /**
     * @description 保存对象到redis
     * @param key
     * @return object
     * @throws
     */
    public static void redisSaveObject(final RedisTemplate<String, Object> redisTemplate, final String key,
            final Object object) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.getAndSet(key, object);
    }

    /**
     * @description 保存对象到redis
     * @param key
     * @return object
     * @throws
     */
    public static void redisSaveObject(final RedisTemplate<String, Object> redisTemplate, final String key,
            final Object object, int time) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, object, time, TimeUnit.MINUTES);
    }

    /**
     * @description redis保存数据到list
     * @param
     * @return void
     * @throws
     */
    public static void redisSaveList(final RedisTemplate<String, Object> redisTemplate, final String key,
            final Object object, int count) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        ops.leftPush(key, object);
        if (0 < count)
            ops.trim(key, 0, count);
    }

    /**
     * @param <T>
     * @description 获取 redis中的list对象
     * @param
     * @return
     * @throws
     */
    public static <T> List<T> redisQueryList(final RedisTemplate<String, Object> redisTemplate, final String key,
            Class<T> claxx) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        List<Object> tempList = ops.range(key, 0, -1);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }

        List<T> resultList = new ArrayList<>();
        for (Object serl : tempList) {
            resultList.add(claxx.cast(serl));
        }
        tempList.clear();
        return resultList;
    }

    /**
     * @description redis 删除列表中的对象
     * @param
     * @return void
     * @throws
     */
    public static void redisDelListValue(final RedisTemplate<String, Object> redisTemplate, final String key,
            final Serializable value) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        ops.remove(key, 0, value);
    }

    /**
     * @param <T>
     * @description 获取 redis中的对象
     * @param
     * @return void
     * @throws
     */
    public static <T> T redisQueryObject(final String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (T) ops.get(key);
    }

    /**
     * @description 删除redis中指定的key
     * @param
     * @return void
     * @throws
     */
    public static void redisDeleteKey(final String key) {
        redisTemplate.delete(key);
    }

    /**
     * @description 保存对象到redis
     * @param key
     * @return object
     * @throws
     */
    public static void redisSaveObject(final String key, final Object object) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.getAndSet(key, object);
    }

    /**
     * @description 保存对象到redis
     * @param key
     * @return object
     * @throws
     */
    public static void redisSaveObject(final String key, final Object object, int time) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, object, time, TimeUnit.MINUTES);
    }

    /**
     * @description redis保存数据到list
     * @param
     * @return void
     * @throws
     */
    public static void redisSaveList(final String key, final Object object, int count) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        ops.leftPush(key, object);
        if (0 < count)
            ops.trim(key, 0, count);
    }

    /**
     * @param <T>
     * @description 获取 redis中的list对象
     * @param
     * @return
     * @throws
     */
    public static <T> List<T> redisQueryList(final String key, Class<T> claxx) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        List<Object> tempList = ops.range(key, 0, -1);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }

        List<T> resultList = new ArrayList<>();
        for (Object serl : tempList) {
            resultList.add(claxx.cast(serl));
        }
        tempList.clear();
        return resultList;
    }

    /**
     * @description redis 删除列表中的对象
     * @param
     * @return void
     * @throws
     */
    public static void redisDelListValue(final String key, final Serializable value) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        ops.remove(key, 0, value);
    }

}
