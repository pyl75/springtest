package test.customtag;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext bf = new ClassPathXmlApplicationContext("test/customtag/test.xml");
        //id由标签指定不是由bean的属性指定的
        User user = (User) bf.getBean("testbean");
        System.out.println(user.getUserName()+","+user.getEmail());
    }
}
