package com.excelib.service;

import com.excelib.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(User user);
    public List<User> getUsers();
}
