package com.excelib;

import static org.junit.Assert.assertTrue;

import com.excelib.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public  void testSave( ) {
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) act.getBean("userService");
        User user = new User();
        user.setName("张三");
        user.setAge(18);
        user.setSex("男");
        userService.save(user);
    }
    @Test
    public void testGet(){
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) act.getBean("userService");
        List<User> users = userService.getUsers();
        System.out.println("++++++++++得到所有的User");
        for (User tmpUser: users) {
            System.out.println(tmpUser.toString());
        }
    }
}
