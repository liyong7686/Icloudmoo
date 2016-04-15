package com.icloudmoo.business.file.upload.netty;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

@ChannelHandler.Sharable
public class NettyMessageEncoder extends MessageToMessageEncoder<CharSequence> {
    private final Charset charset;

    public NettyMessageEncoder() {
        this(Charset.defaultCharset());
    }

    public NettyMessageEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }

    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        if (msg.length() == 0) {
            return;
        }

        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), this.charset));
    }
}