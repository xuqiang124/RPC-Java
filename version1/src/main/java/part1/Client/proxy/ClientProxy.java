package part1.Client.proxy;


import lombok.AllArgsConstructor;
import part1.Client.IOClient;
import part1.common.Message.RpcRequest;
import part1.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/6 16:49
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request
    private String host;
    private int port;

    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        // method.getDeclaringClass() 获取声明该方法的类; .getName() 获取该类的完整类名（包含包名）
        // 例如，如果方法来自 com.example.UserService 接口，那么 interfaceName 就会是 "com.example.UserService"
        RpcRequest request=RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsType(method.getParameterTypes())
                .build();
        //IOClient.sendRequest 和服务端进行数据传输
        RpcResponse response= IOClient.sendRequest(host,port,request);
        return response.getData();
    }
    // <T> 表示这是一个泛型方法，T 是一个类型参数， 返回类型T表示与类型参数T一致， Class<T> clazz 参数表示传入的是 T 类型的 Class 对象
    public <T>T getProxy(Class<T> clazz){
        // clazz.getClassLoader() 获取类加载器
        // new Class[]{clazz} 创建一个包含 clazz 的数组, 表示代理类需要实现的所有接口， 代理类会实现所有这些接口中定义的方法
        // this 表示当前的 InvocationHandler 实例， 当代理对象的方法被调用的时候， 会调用this的invoke方法
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}
