package com.excelib.service.Impl;

import com.excelib.User;
import com.excelib.UserRowMapper;
import com.excelib.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

public class UserServiceImpl implements UserService {

    private JdbcTemplate jdbcTemplate;
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("insert into user(name,age,sex)values(?,?,?)",
                new Object[]{user.getName(),user.getAge(),user.getSex()},
                new int[]{Types.VARCHAR,Types.INTEGER,Types.VARCHAR});
        //throw new RuntimeException("testTransaction");
    }


    @Override
    public List<User> getUsers() {
        List<User> list = jdbcTemplate.query("select * from user", new UserRowMapper());
        return list;
    }
}
