package test.replacedMethod;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.lookup.app.GetBeanTest;

public class Main {
    public static void main(String[] args) {
        ApplicationContext bf = new ClassPathXmlApplicationContext("test/replacemethod/replaceMethodTest.xml");
        TestChangeMethod test = (TestChangeMethod) bf.getBean("testChangeMethod");
        test.changeMe();
    }
}

