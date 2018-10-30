package com.excelib.dao;

import com.excelib.User;
import com.excelib.plugin.PageParams;
import com.excelib.sqlProvider.SqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

public interface UserMapper {
    public void insertUser(User user);
    public User getUser(Integer id);
    public User getUserByCondition(@Param("user") User user, PageParams pageParams);
    public void updateUser(User user);
    public void deleteUser(Integer id);

    /**
     * 标识提供sql语句的类和方法，mybatis会调用该类的方法获取sql语句，生成MappedStatement注册
     */
    @SelectProvider(type = SqlProvider.class,method = "getQueryUserSql")
    public User queryUserWithSqlProvider(@Param("id") Integer id);

    /**
     * 使用注解配置sql语句
     */
    @Select("select * from user where id = #{id}")
    public User queryUserWithAnnotation(@Param("id") Integer id);

    /**
     * 添加<script> 可以则sql语句可以使用动态标签
     */
    @Select({"<script>",
            "        update user\n" +
            "        <set>\n" +
            "            <if test=\"name!=null and name !=''\">\n" +
            "                name = #{name},\n" +
            "            </if>\n" +
            "            <if test=\"age!=null and age !=''\">\n" +
            "                age = #{age}\n" +
            "            </if>\n" +
            "        </set>\n" +
            "        where id = #{id}\n",
            "</script>"})
    public void updateUserWithAnnotation(@Param("id") Integer id,@Param("name")String name,@Param("age")Integer age);

}
