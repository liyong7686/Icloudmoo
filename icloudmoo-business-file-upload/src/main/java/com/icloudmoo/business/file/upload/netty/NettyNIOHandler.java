package com.icloudmoo.business.file.upload.netty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.icloudmoo.business.file.upload.deal.CmdConst;
import com.icloudmoo.business.file.upload.deal.Command;
import com.icloudmoo.business.file.upload.deal.DataReader;
import com.icloudmoo.business.file.upload.util.MessageTransferDecoder;
import com.icloudmoo.business.file.upload.vo.TransferRequest;
import com.icloudmoo.business.file.upload.vo.TransferResponse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyNIOHandler extends ChannelInboundHandlerAdapter {
    
    private static Logger logger = Logger.getLogger(NettyNIOHandler.class);
    
    private TransferRequest request;
    private TransferResponse response;
    private Integer fileSize;
    private StringBuilder fileContent;

    /*
     * 客户端连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("--------client--conecting-------------");

        fileSize = 0;
        request = new TransferRequest();
        response = new TransferResponse();
        fileContent = new StringBuilder();

        logger.info("----------client--connected!------------");
    }

    /*
     * 接收上传信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean flag = false;
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() < 4) {
            return;
        }

        String hexStr = ByteBufUtil.hexDump(buf);
        // 封装请求消息内容
        String requstString = MessageTransferDecoder.decodeString(hexStr, "ISO-8859-1");
        // 解析请求报文并验证协议格式
        if (requstString.contains(CmdConst.UPLOAD)) {
            String requestHeadString = requstString;

            // 本次传输的内容包含了部分文件内容
            if (requstString.split("\r\r\n").length > 1) {
                // 请求头
                requestHeadString = requstString.split("\r\r\n")[0] + "\r\r\n";

                // 请求的部分文件内容
                String requestContentString = requstString.split("\r\r\n")[1];

                // 将第一次传的多余的消息存入到真实的文件内容
                byte[] content = requestContentString.getBytes("ISO-8859-1");

                fileContent.append(MessageTransferDecoder.bytes2HexString(content));

                fileSize = fileSize + content.length;

            }
            InputStream input = new ByteArrayInputStream(requestHeadString.getBytes());

            flag = DataReader.readRequest(input, request, response);

            writeResponse(ctx);
        }
        else {
            byte[] arry = MessageTransferDecoder.hexStr2ByteArray(hexStr);
            fileSize = fileSize + arry.length;
            fileContent.append(hexStr);
            // 判断文件是否已经传完
            if (request != null && request.getFileSize() != null) {
                if (fileSize >= request.getFileSize()) {
                    byte[] data = MessageTransferDecoder.hexStr2ByteArray(fileContent.toString());
                    InputStream inputStream = new ByteArrayInputStream(data);

                    long t = System.currentTimeMillis();
                    flag = Command.execCmd(request, inputStream, response);

                    t = System.currentTimeMillis() - t;
                    if (flag) {
                        writeResponse(ctx);
                    }
                    showCmdInfo(request, t, flag, ctx);

                }

            }
        }
    }

    /**
     * @description 记录日志
     */
    private void showCmdInfo(TransferRequest request, long time, boolean flag, ChannelHandlerContext ctx) {
        String host = ctx.channel().localAddress().toString();
        if (flag)
            logger.info(host + " 执行命令" + request.getCmd() + "成功，耗时" + time + "ms");
        else
            logger.info(host + " 执行命令" + request.getCmd() + "失败，耗时" + time + "ms");
    }

    /**
     * TODO: 回复结果
     * @param ctx
     * @throws IOException
     */
    private void writeResponse(ChannelHandlerContext ctx) throws IOException {
        DataWriter.writeResponse(ctx, response);

    }
}
