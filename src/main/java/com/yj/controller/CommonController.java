package com.yj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author 29029
 */
@Controller
@RequestMapping("commonController")
public class CommonController {

    /**
     * 验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        //创建对象
        VerifyCode vc = new VerifyCode();
        //获取图片对象
        BufferedImage bi = vc.getImage();
        //获得图片的文本内容
        String code = vc.getText();
        // 将系统生成的文本内容保存到session中
        request.getSession().setAttribute("code", code);
        System.out.println(code);
        //向浏览器输出图片
        VerifyCode.output(bi, response.getOutputStream());
    }

    /**
     * 跳转到注册页
     * @return
     */
    @RequestMapping("/reg")
    public String reg(){
        return "register";
    }

    /**
     * 跳转到登录页
     * @return
     */
    @RequestMapping("/log")
    public String log(){
        return "login";
    }

    /**
     * 跳转到index页面
     * @return
     */
    @RequestMapping("/succ")
    public String succ(){
        return "success";
    }

    /**
     * 修改密码
     * @param id
     * @return
     */
    @RequestMapping("/upPass/{id}")
    public String upPass(@PathVariable Integer id){
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        return "Update_Pass";
    }

    /**
     *
     */
    @RequestMapping("/forget")
    public String forgetPass(){
        return "forgetPass";
    }
}
