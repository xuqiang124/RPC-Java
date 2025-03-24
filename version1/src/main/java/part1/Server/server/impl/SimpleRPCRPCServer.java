package part1.Server.server.impl;


import lombok.AllArgsConstructor;
import part1.Server.server.RpcServer;
import part1.Server.server.work.WorkThread;
import part1.Server.provider.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/12 11:37
 */
@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvide;
    @Override
    public void start(int port) {
        try {
            // 创建一个服务器套接字，监听端口
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("服务器启动了");
            while (true) {
                //如果没有连接，会堵塞在这里
                // 等待客户端连接
                // 有连接的时候，接受连接并且创建一个socket进行通信
                Socket socket = serverSocket.accept();
                //有连接，创建一个新的线程执行处理
                new Thread(new WorkThread(socket,serviceProvide)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
    }
}
