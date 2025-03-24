package part2.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import part2.common.Message.RpcResponse;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/26 17:29
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    // SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter的子类，用于处理入站数据，是netty中用来处理服务器响应的处理器。
    // 用来接收来自服务器的RpcResponse对象消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception { //用于读取服务端返回的数据
        // 创建一个AttributeKey，用于在channel中存储RpcResponse对象
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
        // 将服务端返回的response设置到channel的属性中， 之后可以通过channel获取对应的数据
        ctx.channel().attr(key).set(response);
        ctx.channel().close();
    }

    // 用于捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
