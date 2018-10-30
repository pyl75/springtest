package com.excelib;

import com.excelib.dao.UserMapper;
import com.excelib.plugin.PageParams;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMapper {
    static SqlSessionFactory sqlSessionFactory = null;
    static {
        sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
    }
    @Test
    public void testAdd(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = new User();
            user.setName("tom");
            user.setAge(new Integer(3));
            userMapper.insertUser(user);
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void testGet(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUser(7);
            System.out.println(user.toString());
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = new User();
            user.setId(7);
            user.setAge(new Integer(3));
            userMapper.updateUser(user);
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDelete(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.deleteUser(3);
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

    /**
     * Spring Mybatis 测试
     */
    @Test
    public void testSpringMyBatis(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = userMapper.getUser(17);
        System.out.println(user.toString());
    }

    @Test
    public void testSpringUpdate(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = new User();
        user.setId(17);
        user.setName("Jack");
        user.setAge(new Integer(8));
        userMapper.updateUser(user);
    }


    @Test
    public void queryUserWithAnnotation(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = userMapper.queryUserWithAnnotation(17);
        System.out.println(user.toString());
    }

    @Test
    public void queryUserWithSqlProvider(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = userMapper.queryUserWithSqlProvider(17);
        System.out.println(user.toString());
    }

    @Test
    public void updateUserWithAnnotation(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        userMapper.updateUserWithAnnotation(17,"Bob",23);
    }

    @Test
    public void testPagingPlugin(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        PageParams pageParams = new PageParams();
        pageParams.setPage(0);
        pageParams.setPageSize(2);
        User user = new User();
        user.setName("Jack");
        User result = userMapper.getUserByCondition(user,pageParams);
        System.out.println(result.toString());
    }
}
