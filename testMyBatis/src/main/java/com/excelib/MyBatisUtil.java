package com.excelib;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
/**
 * 使用Mybatis原生方式查询
 */
public class MyBatisUtil{
    private final static  SqlSessionFactory sqlSessionFactory;
    static {
        String resource = "mybatis-org-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }
}
