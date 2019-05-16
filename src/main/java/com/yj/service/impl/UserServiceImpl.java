package com.yj.service.impl;

import com.yj.dao.UserMapper;
import com.yj.entity.User;
import com.yj.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @version 1.0
 * @author 29029
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User login(String username, String password) {
        return userMapper.login(username,password);
    }

    @Override
    public User getByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    @Override
    public int updatePassword(String password, Integer id) {
        return userMapper.updatePassword(password,id);
    }

    @Override
    public Integer insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public User getByUserName(String username) {
        return userMapper.getByUserName(username);
    }
}
