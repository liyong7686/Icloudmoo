package com.icloudmoo.business.file.upload.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/****
 * 对象和数组转换器
 * @author ninghp
 *
 */
public class ByteObjConverter {
    /** 日志 */
    private static Logger logger = Logger.getLogger(ByteObjConverter.class);

    /**
     * 将对象转为字节数组
     * 
     * @param bytes
     * @return
     */
    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        }
        catch (Exception e) {
            logger.info("对象转字节流失败" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将字节数组转为对象
     * 
     * @param bytes
     * @return
     */
    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        }
        catch (Exception e) {
            logger.info("字节流转对象失败" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

}
