package com.yj.service;

import com.yj.entity.User;
import org.apache.ibatis.annotations.Param;
/**
 * @version 1.0
 * @author 29029
 */
public interface IUserService {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 根据邮箱查询该用户    用于判断邮箱是否一致
     * @param email
     * @return
     */
    User getByEmail(String email);

    /**
     * 修改密码
     * @param password
     * @param id
     * @return
     */
    int updatePassword(@Param("password") String password, @Param("id") Integer id);

    /**
     * 注册用户
     * @param user
     * @return
     */
    Integer insertUser(User user);

    /**
     * 根据用户名查询用户  用于判断用户名是否唯一
     * @param username
     * @return
     */
    User getByUserName(String username);
}
