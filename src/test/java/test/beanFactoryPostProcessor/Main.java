package test.beanFactoryPostProcessor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext bf = new ClassPathXmlApplicationContext("test/beanFactoryPostProcessor/beanFactoryPostProcessor.xml");
        User user = (User) bf.getBean("testUser");
        System.out.println(user.getUserName()+","+user.getEmail());
    }
}
