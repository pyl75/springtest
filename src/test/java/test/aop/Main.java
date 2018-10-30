package test.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.beanFactoryPostProcessor.User;

public class Main {
    public static void main(String[] args) {
        ApplicationContext bf = new ClassPathXmlApplicationContext("test/aop/aop.xml");
        TestBean test = (TestBean) bf.getBean("test");
        test.test();
    }
}
