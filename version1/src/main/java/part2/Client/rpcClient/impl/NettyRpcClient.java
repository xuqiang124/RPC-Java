package part2.Client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part2.Client.netty.nettyInitializer.NettyClientInitializer;
import part2.common.Message.RpcRequest;
import part2.common.Message.RpcResponse;
import part2.Client.rpcClient.RpcClient;
import io.netty.channel.ChannelFutureListener;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/5/2 19:40
 */
public class NettyRpcClient implements RpcClient {
    private String host;
    private int port;
    //bootstrap对象（负责启动客户端，建立与服务器的连接，管理连接的生命周期，处理连接关闭），配置参数
    private static final Bootstrap bootstrap;
    // netty中的线程池，创建一个线程组，用于处理io
    private static final EventLoopGroup eventLoopGroup;
    public NettyRpcClient(String host,int port){
        this.host=host;
        this.port=port;
    }
    //netty客户端初始化 ； 静态代码块，在类加载时执行，只执行一次； 初始化是为了配置netty网络通信所需要的各种资源，用来之后建立连接、发送请求、返回响应
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup) // 设置线程组
                .channel(NioSocketChannel.class) // 设置通道类型
                .handler(new NettyClientInitializer()); //NettyClientInitializer这里 配置netty对消息的处理机制
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            //connect发起连接操作； ChannelFuture 是 Netty 中的一个重要接口，代表一个异步操作的结果
            //创建一个channelFuture对象，代表这一个操作事件，sync方法表示堵塞直到connect完成
            ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
            //channel表示一个连接的单位，类似socket
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            //sync()堵塞，等待netty io线程完成操作之后再关闭
            channel.closeFuture().sync();
            // 阻塞的获得结果（在get处会阻塞，直到NettyClientHandler 设置响应结果），通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // 响应处理完成后，通道关闭，closeFuture().sync() 解除阻塞
            // AttributeKey是线程隔离的，不会由线程安全问题。
            // 当前场景下选择堵塞获取结果
            // 其它场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addListener...
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get(); // 根据key获取channel中的RpcResponse对象

            // 另一种监听器方式来异步获取结果。添加监听器
            // channelFuture.addListener(new ChannelFutureListener() {
            //     @Override
            //     public void operationComplete(ChannelFuture future) throws Exception {
            //         if (future.isSuccess()) {
            //             // 发送成功
            //             System.out.println("消息发送成功");
            //         } else {
            //             // 发送失败
            //             System.out.println("消息发送失败: " + future.cause());
            //         }
            //     }
            // });

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
