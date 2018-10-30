package com.excelib;

import com.excelib.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) act.getBean("userService");
        User user = new User();
        user.setName("张三");
        user.setAge(18);
        user.setSex("男");
        userService.save(user);
        List<User> users = userService.getUsers();
        System.out.println("++++++++++得到所有的User");
        for (User tmpUser: users) {
            System.out.println(tmpUser.toString());
        }
    }
}
