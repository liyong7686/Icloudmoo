package com.icloudmoo.business.file.upload.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/***
 * 
 * @author ninghp
 *
 */
public class ByteBufToBytes {
    private ByteBuf tempBuf;

    /** 是否已经读完 */
    private boolean end = true;

    public ByteBufToBytes() {
    }

    public ByteBufToBytes(int length) {
        tempBuf = Unpooled.buffer(length);
    }

    /**
     * 判断是否已经将ByteBuf读完
     * 
     * @param datas
     */
    public void reading(ByteBuf datas) {
        datas.readBytes(tempBuf, datas.readableBytes());
        if (this.tempBuf.writableBytes() != 0) {
            end = false;
        }
        else {
            end = true;
        }
    }

    public boolean isEnd() {
        return end;
    }

    public byte[] readFull() {
        if (end) {
            byte[] contentByte = new byte[this.tempBuf.readableBytes()];
            this.tempBuf.readBytes(contentByte);
            this.tempBuf.release();
            return contentByte;
        }
        else {
            return null;
        }
    }

    /**
     * 将ByteBuf转为字节数组
     * 
     * @param datas
     * @return
     */
    public byte[] read(ByteBuf datas) {
        byte[] bytes = new byte[datas.readableBytes()];
        datas.readBytes(bytes);
        return bytes;
    }
}
