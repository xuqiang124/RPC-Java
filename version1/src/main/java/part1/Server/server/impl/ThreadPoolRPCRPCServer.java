package part1.Server.server.impl;


import part1.Server.server.RpcServer;
import part1.Server.server.work.WorkThread;
import part1.Server.provider.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/19 15:30
 */
// 通过线程池管理和执行请求处理任务，以提高并发处理能力；
// 避免高并发环境下，每个请求都创建一个线程最后导致性能问题
public class ThreadPoolRPCRPCServer implements RpcServer {
    private final ThreadPoolExecutor threadPool; //比simple多了一个线程池参数，管理工作线程
    private ServiceProvider serviceProvider;

    // 构造方法1
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider){
        threadPool=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        this.serviceProvider= serviceProvider;
    }

    // 构造方法2。参数为：服务的map类， 核心线程数（设置为cpu核心数）， 最大线程数， 非核心线程存活时间，时间单位， 工作队列
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider, int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue){

        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        System.out.println("服务端启动了");
        try {
            ServerSocket serverSocket=new ServerSocket();
            while (true){
                Socket socket= serverSocket.accept();
                threadPool.execute(new WorkThread(socket,serviceProvider));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}
