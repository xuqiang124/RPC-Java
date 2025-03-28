package part3.Client.serviceCenter;

import java.net.InetSocketAddress;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/5/3 21:42
 */
//服务中心接口
public interface ServiceCenter {
    //  查询：根据服务名查找地址; InetSocketAddress类代表一个网络地址：ip+端口
    InetSocketAddress serviceDiscovery(String serviceName);
}
