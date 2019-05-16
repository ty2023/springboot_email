package com.yj.controller;

import com.google.gson.Gson;

import com.yj.entity.User;
import com.yj.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author 29029
 */
@Controller
@RequestMapping("userController")
public class UserController {

    @Autowired
    private IUserService userService;


    @RequestMapping(value = "/login",produces="text/html;charset=utf8")
    @ResponseBody
    public String login(String username, String password, String checkCode, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>(10);
        String str=(String) request.getSession().getAttribute("code");
        if (!checkCode.equalsIgnoreCase(str)){
            map.put("f","false");
            map.put("msg","验证码错误");
        }else {
            User user = userService.login(username, password);
            if (user != null){
                request.getSession().setAttribute("Login_User",user);
                map.put("f","true");
                map.put("msg","登录成功");
            }else {
                map.put("f","false");
                map.put("msg","登录失败，用户名密码错误");
            }
        }
        return new Gson().toJson(map);
    }


    @RequestMapping(value = "/getByUserName",produces="text/html;charset=utf8")
    @ResponseBody
    public String getByUserName(String username,ModelMap map){
        User user = userService.getByUserName(username);
        if (user == null) {
            map.put("userName", "用户名可用");
        } else {
            map.put("userName", "用户名已存在");
        }
        return new Gson().toJson(map);
    }

    @RequestMapping(value = "/registerUser",produces="text/html;charset=utf8")
    @ResponseBody
    public String register(User user,HttpServletRequest request){
        Map<String, String> map = new HashMap<>(10);
        if (request.getSession().getAttribute("emailcode") == null || request.getSession().getAttribute("emailcode") == ""){
            map.put("f","false");
            map.put("m","邮箱属于未验证状态，请重新注册");
        }else {
            Integer insertUser = userService.insertUser(user);
            if (insertUser == 1){
                map.put("f","true");
                map.put("m","注册成功，自动进入登录页面");
            }else {
                map.put("f","false");
                map.put("m","注册失败");
            }
        }
        return new Gson().toJson(map);
    }

    @RequestMapping(value = "/getUserPass",produces="text/html;charset=utf8",method = RequestMethod.POST)
    @ResponseBody
    public String getUserPass(String username,ModelMap map,HttpServletRequest request){
        User user = userService.getByUserName(username);
        if (user != null){
            request.getSession().setAttribute("update_user_pass",user);
            map.put("userEmail",user.getEmail());
            map.put("userId",user.getId());
        }else {
            map.put("ff","该用户不存在");
        }
        return new Gson().toJson(map);
    }

    @RequestMapping(value = "/updateUserPassWord",produces="text/html;charset=utf8")
    @ResponseBody
    public String updateUserPassWord(Integer id,String password){
        Map<String, Object> map;
        map = new HashMap<String, Object>(10);
        int i = userService.updatePassword(password, id);
        if (i > 0){
            map.put("f","true");
            map.put("up","修改成功,即将返回登录页");
        }else {
            map.put("f","false");
            map.put("up","修改失败");
        }
        return new Gson().toJson(map);
    }

}
