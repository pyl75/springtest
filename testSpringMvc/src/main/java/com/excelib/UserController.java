package com.excelib;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class UserController extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArrayList<User> users = new ArrayList<>();
        User userA = new User();
        User userB = new User();
        userA.setUsername("张三");
        userA.setAge(27);
        userB.setUsername("李四");
        userB.setAge(37);
        users.add(userA);
        users.add(userB);
        ModelAndView modelAndView = new ModelAndView("userlist");
        modelAndView.addObject("users",users);
        return modelAndView;
    }
}
