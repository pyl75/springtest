package com.excelib.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, //确定要拦截的对象
        method = "update", //确定要拦截的方法
        args = {MappedStatement.class,Object.class} //拦截方法的参数
    )})
public class MyPlugin implements Interceptor {
    Properties properties=null;
    /**
     * 拦截方法的处理
     * @param invocation 责任链对象
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.err.println("before ....");
        //如果当前代理是一个非代理对象，那么它就会调用真实拦截对象的方法，如果不是他会回调下一个代理对象的代理接口的方法
        Object result = invocation.proceed();
        System.err.println("after .....");
        return result;
    }

    /**
     * 生成对象的代理，这里常用MyBatis提供的Plugin类的wrap方法
     * @param target 被代理的对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor){
            System.err.println("调用生成代理对象"+target.getClass());
            return Plugin.wrap(target,this);
        }
       return target;
    }

    /**
     * 获取配置文件的属性，我们在MyBatis的配置文件里面去配置
     * @param properties 是MyBatis配置的参数
     */
    @Override
    public void setProperties(Properties properties) {
        System.err.println(properties.get("dbType"));
        this.properties = properties;
    }
}
