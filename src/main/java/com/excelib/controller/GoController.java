package com.excelib.controller;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class GoController implements EnvironmentAware {
    private final Logger logger = Logger.getLogger(GoController.class);
    //处理Head类型的"/"请求
    @RequestMapping(value = {"/"},method = {RequestMethod.HEAD})
    public String head(){
        return "go.jsp";
    }
    //处理Get类型的"/index" 和"/"请求
    @RequestMapping(value = {"/index","/"},method = {RequestMethod.GET})
    public String index(Model model){
        logger.info("=======processed by index ======");
        //返回msg参数
        model.addAttribute("msg","Go Go Go！");
        return "go";
    }

    private Environment environment = null;
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
