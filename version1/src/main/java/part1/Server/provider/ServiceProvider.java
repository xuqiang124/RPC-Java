package part1.Server.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/16 17:35
 */
//本地服务存放器
//维护一个服务注册表，存储接口名称和对应的服务层实现
public class ServiceProvider {
    //集合中存放服务的实例，key 是接口的完整类名， value 是服务的具体实现对象
    private Map<String,Object> interfaceProvider;

    public ServiceProvider(){
        this.interfaceProvider=new HashMap<>();
    }
    //本地注册服务

    // 接收一个服务实现对象
    // 获取该对象的所有接口
    // 将每个接口名称和对应的服务实现存入map
    public void provideServiceInterface(Object service){
        String serviceName=service.getClass().getName();
        Class<?>[] interfaceName=service.getClass().getInterfaces();

        for (Class<?> clazz:interfaceName){
            interfaceProvider.put(clazz.getName(),service);
        }

    }
    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
