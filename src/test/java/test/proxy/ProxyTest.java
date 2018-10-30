package test.proxy;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;

import java.io.File;
import java.io.FileOutputStream;

public class ProxyTest {
    @Test
    public void testJdkProxy(){
        UserService userService = new UserServiceImpl();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(userService);
        UserService proxy = (UserService) myInvocationHandler.getProxy();
        proxy.sayHello();
    }
    @Test
    public void testCglibProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(new MethodInterceptorImpl());
        UserService proxy = (UserService) enhancer.create();
        proxy.sayHello();
        System.out.println(proxy);
    }
}
