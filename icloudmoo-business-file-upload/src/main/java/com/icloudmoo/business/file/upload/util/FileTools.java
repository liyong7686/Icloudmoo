package com.icloudmoo.business.file.upload.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.icloudmoo.common.util.ConfigureUtil;


/**
 * @description 文件操作工具类
 * @author gengchong
 * @date 2013-2-18
 */
public class FileTools {

    private static Logger logger = Logger.getLogger(FileTools.class);
    private static Set pathPool = new HashSet();
    private static Object lockObject = new Object();
    private static String uploadImgRootPath;

    static {
        uploadImgRootPath = ConfigureUtil.getProperty("rootImgPath");
    }

    /**
     * @description 根据文件名称加载文件信息
     * @param fileName
     * @return byte[]
     */
    public static byte[] loadFile(String fileName) {
        byte data[] = (byte[]) null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName),
                    Integer.valueOf(ConfigureUtil.getProperty("fileBufferSize")));
            data = new byte[bis.available()];
            bis.read(data);
            bis.close();
        }
        catch (Exception e) {
            logger.error((new StringBuilder("load file error: ")).append(fileName).toString(), e);
        }
        return data;
    }

    /**
     * @description 存储文件
     * @param byte[]
     * @return String
     */
    public static String writeFile(InputStream input, int length, String format, String oriFileName) {
        format = "jpg";
        String fileName;
        String flag = oriFileName.indexOf("_") > 0 ? "WH_" : "";
        Date date = new Date();
        String path = new StringBuilder(uploadImgRootPath).append((new SimpleDateFormat("yyyy/MM/dd/HH")).format(date))
                .append("/").toString();
        checkDir(path);
        synchronized (lockObject) {
            ImageSerialNumber.incCount();
            for (fileName = (new StringBuilder(String.valueOf(path))).append(flag).append(oriFileName).append("_")
                    .append(date.getTime()).append("_").append(ImageSerialNumber.getCount()).append(".").append(format)
                    .toString(); (new File(fileName))
                            .exists(); fileName = (new StringBuilder(String.valueOf(path))).append(flag)
                                    .append(oriFileName).append("_").append(System.currentTimeMillis()).append("_")
                                    .append(ImageSerialNumber.getCount()).append(".").append(format).toString());
        }
        int bufferSize = Integer.valueOf(ConfigureUtil.getProperty("fileBufferSize"));
        byte[] buffers = new byte[bufferSize];
        int total = 0;
        int read = 0;

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
            while (total < length) {
                read = input.read(buffers, 0, bufferSize);
                total = total + read;
                if (read < 0) {
                    break;
                }
                bos.write(buffers, 0, read);
                bos.flush();
            }
            bos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error((new StringBuilder("write file error: ")).append(fileName).toString());
            try {
                input.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException((new StringBuilder("write file error: ")).append(fileName).toString(), e);
        }
        return fileName.replaceAll(uploadImgRootPath, "");
    }

    /**
     * @description 检查当前文件目录是否存在
     */
    private static void checkDir(String path) {
        if (!pathPool.contains(path))
            synchronized (pathPool) {
                if (!pathPool.contains(path)) {
                    File dir = new File(path);
                    dir.mkdirs();
                    if (dir.exists())
                        pathPool.add(path);
                }
                if (pathPool.size() > 10000)
                    pathPool.clear();
            }
    }

    /**
     * @description 删除文件
     */
    public static void deleteFile(Collection fileNames) {
        for (Iterator iterator = fileNames.iterator(); iterator.hasNext();) {
            String fileName = (String) iterator.next();
            if (fileName != null && !"".equals(fileName))
                (new File(fileName)).delete();
        }

    }

    /**
     * @description 根据文件名取得文件后缀名
     * @author: 唐辉
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (fileName == null)
            return "";
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index);
        }
        else {
            return "";
        }
    }

    /**
     * @description 创建文件夹
     * @author: 唐辉
     * @param dirPath
     * @return
     */
    public static boolean mkDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * @description 复制文件
     * @author: 唐辉
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile) {
        // 创建目标文件所在的目录
        mkDir(destFile.getParent());

        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024 * 1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
