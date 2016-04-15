package com.icloudmoo.business.file.upload.netty;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.icloudmoo.common.util.ConfigureUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TODO:
 * @author liyong
 * @Date 2015年12月22日 下午2:15:28
 */
public class NettyNIOServer {
    private static Logger logger = Logger.getLogger(NettyNIOServer.class);

    private static final int PORT = Integer.valueOf(ConfigureUtil.getProperty("socketPort"));
    private EventLoopGroup bossgroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 图片服务器启动入口
     * 
     * @throws Exception
     */
    public void statNettyServer() throws Exception {

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossgroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_TIMEOUT, 100)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(80920))
                    .option(ChannelOption.SO_REUSEADDR, true)

            .localAddress(new InetSocketAddress(PORT)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("encoder", new NettyMessageEncoder());
                    pipeline.addLast(new NettyNIOHandler());
                }
            });

            /**
             * Bind server to accept connections
             */
            ChannelFuture f = b.bind().sync();

            logger.info("==============图片服务器启动完成=============");
            f.channel().closeFuture().sync();
        }
        catch (Exception e) {
            logger.error("图片服务器启动异常", e);
        }

    }

    /**
     * 图片服务器关闭入口
     */
    public void shutDown() {
        try {
            workerGroup.shutdownGracefully().sync();
            bossgroup.shutdownGracefully().sync();
            logger.info("==============图片服务器关闭完成=============");
        }
        catch (InterruptedException e) {
            logger.error("图片服务器关闭异常", e);
        }
    }

}
