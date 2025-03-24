package part1.Server;


import part1.Server.server.RpcServer;
import part1.common.service.Impl.UserServiceImpl;
import part1.common.service.UserService;
import part1.Server.server.impl.SimpleRPCRPCServer;
import part1.Server.provider.ServiceProvider;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/11 19:39
 */
public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        // 注册对应的服务
        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        // 新建一个rpcSever，启动后开始监听9999端口的请求
        RpcServer rpcServer=new SimpleRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
