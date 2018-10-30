package test.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectService {
    public void sayHello(String name){
        System.err.println("hello "+name);
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object instance = Class.forName(ReflectService.class.getName()).newInstance();
        Method method = Class.forName("test.reflect.ReflectService").getMethod("sayHello", String.class);
        method.invoke(instance,"zhangsan");
    }
}
