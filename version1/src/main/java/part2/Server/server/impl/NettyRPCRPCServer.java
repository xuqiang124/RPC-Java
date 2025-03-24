package part2.Server.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import part2.Server.netty.nettyInitializer.NettyServerInitializer;
import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/26 14:01
 */
@AllArgsConstructor
public class NettyRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        // netty 服务线程组boss负责建立连接， work负责具体的请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.println("netty服务端启动了");
        try {
            //启动netty服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //初始化
            serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    //设置服务器套接字选项，指定服务器在accept连接请求时，队列中最多可以容纳128个连接请求；option设置的是服务端用于接收进来的连接，也就是boosGroup线程。
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置子通道选项，指定子通道在读写操作时，是否启用心跳机制，保持连接活跃；childOption设置的是服务端用于处理已建立连接的通道，也就是workGroup线程。
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //NettyClientInitializer这里 配置netty对消息的处理机制
                    .childHandler(new NettyServerInitializer(serviceProvider));
            //同步堵塞; 调用bind之后会创建一个NioServerSocketChannel， 然后开始监听指定的端口。
            ChannelFuture channelFuture=serverBootstrap.bind(port).sync();
            //死循环监听
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
