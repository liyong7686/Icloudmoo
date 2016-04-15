package com.icloudmoo.business.file.upload.netty;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.icloudmoo.business.file.upload.vo.TransferResponse;

import io.netty.channel.ChannelHandlerContext;

/**
 * @description 写数据流类
 * @author liyong
 * @date 2013-2-20
 */
public class DataWriter {
    private static Logger logger = Logger.getLogger(DataWriter.class);

    /**
     * 反馈文件上传结果
     */
    public static void writeResponse(ChannelHandlerContext ctx, TransferResponse response) {
        StringBuilder result = new StringBuilder("status:").append(response.getStatus());

        if (!StringUtils.isEmpty(response.getPath())) {
            result.append("\r\n").append("path:").append(response.getPath()).append("\r\r\r\n");
        }
        else {
            result.append("\r\r\r\n");
        }
        logger.info("回复结果：" + result.toString());
        ctx.channel().writeAndFlush(result.toString());

    }

}
