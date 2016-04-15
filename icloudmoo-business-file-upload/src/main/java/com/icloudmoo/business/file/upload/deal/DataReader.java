package com.icloudmoo.business.file.upload.deal;

import java.io.IOException;
import java.io.InputStream;

import com.icloudmoo.business.file.upload.vo.TransferRequest;
import com.icloudmoo.business.file.upload.vo.TransferResponse;

/**
 * @description 数据流读取类
 * @author liyong
 * @date 2013-2-20
 */
public class DataReader {
    /**
     * @description 根据数据流返回对象
     * @param input
     * @return TransferRequest
     * @throws IOException
     */
    public static boolean readRequest(InputStream input, TransferRequest request, TransferResponse response) {
        return readUploadRequest(input, request, response);
    }

    /**
     * @description 解析上传文件报文
     * @param
     * @return
     * @throws
     */
    private static boolean readUploadRequest(InputStream input, TransferRequest request, TransferResponse response) {
        try {

            String tcpStr = readString(input, new byte[] { '\r', '\r', '\n' }).replace("\r\n", ";");

            String[] tcpArray = tcpStr.split(";");

            for (int i = 0; i < tcpArray.length; i++) {
                String[] pkv = tcpArray[i].split(":");

                switch (pkv[0]) {
                    case "cmd":
                        request.setCmd(pkv.length > 1 ? (pkv[1]) : "");
                        break;
                    case "name":
                        request.setFileName(pkv.length > 1 ? (pkv[1]) : "");
                        break;
                    case "type":
                        request.setFileType(pkv.length > 1 ? (pkv[1]) : "");
                        break;
                    case "size":
                        request.setFileSize(pkv.length > 1 ? Integer.valueOf(pkv[1]) : 0);
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatus(CmdConst.STATUS_TCP_FORMAT_ERROR);
            return false;
        }

        return true;

    }

    /**
     * @description 根据数据流返回string参数
     * @param input
     * @return String
     * @throws IOException
     */
    public static String readString(InputStream input, byte[] endFlag) throws IOException {
        boolean flag = true;
        int index = 0;
        byte[] param = new byte[1024];
        byte[] pre = new byte[1];
        byte[] temp = new byte[endFlag.length];

        do {
            input.read(pre);
            for (int i = 0; i < endFlag.length; i++) {
                if (pre[0] != endFlag[i]) {
                    for (int j = 0; j < i; j++) {
                        param[index] = temp[j];
                        index++;
                    }

                    param[index] = pre[0];
                    index++;

                    break;
                }
                else {
                    temp[i] = pre[0];
                    if (i == (endFlag.length - 1)) {
                        flag = false;
                        break;
                    }
                    else
                        input.read(pre);
                }
            }

        }
        while (flag);

        return new String(param, 0, index, "GBK");
    }

    /**
     * @description 返回文件内容
     * @param input
     * @param length
     * @return byte[]
     * @throws IOException
     */
    public static byte[] readBytes(InputStream input, int length) throws IOException {
        if (length > 0) {
            byte buf[] = new byte[length];
            input.read(buf, 0, length);
            return buf;
        }
        else {
            return null;
        }
    }

}
