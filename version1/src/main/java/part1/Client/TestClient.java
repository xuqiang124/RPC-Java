package part1.Client;


import part1.Client.proxy.ClientProxy;
import part1.common.service.UserService;
import part1.common.pojo.User;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/6 18:39
 */
public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy=new ClientProxy("127.0.0.1",9999);
        // java类对象： 在 Java 中，每个类都有一个对应的 Class 对象，类名.class 可以获取到这个类的所有信息（类名、方法、属性等）
        // 例如，UserService.class 会返回 UserService 类的 Class 对象，通过这个对象可以获取到这个类的所有信息、方法、返回类型、参数等，进行反射操作
        UserService proxy=clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
