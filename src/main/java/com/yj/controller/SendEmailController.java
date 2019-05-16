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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @version 1.0
 * @author 29029
 */
@Controller
@RequestMapping("/email")
public class SendEmailController {

    @Autowired
    private IUserService userService;

    /**
     *   设置服务器
     */
    private static String KEY_SMTP = "mail.smtp.host";

    /**
     *    163发件服务器
     */
    private static String VALUE_SMTP = "smtp.163.com";

    /**
     *     服务器验证
     */
    private static String KEY_PROPS = "mail.smtp.auth";
    private static boolean VALUE_PROPS = true;

    /**
     *     服务器验证
     */
    private String sendUser ="18173707896@163.com";
    private String sendUname ="18173707896@163.com";
    private String sendPwd = "Qwer0528";

    /**
     * 建立会话
     */
    private MimeMessage message;
    private Session s;

    /**
     * 构造函数
     */
    public  SendEmailController() {
        Properties props = System.getProperties();
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable","true");
        props.setProperty(KEY_SMTP, VALUE_SMTP);
        props.put(KEY_PROPS, "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        s =  Session.getInstance(props, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendUname, sendPwd);
            }});
        s.setDebug(true);
        message = new MimeMessage(s);
    }


    /**
     * 发送邮件
     *
     * @param headName
     *            邮件头文件名
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @throws Exception
     */
    public void doSendHtmlEmail(String headName, String sendHtml,
                                String receiveUser) throws Exception {
        InternetAddress from = new InternetAddress(sendUser,"管理员");
        message.setFrom(from);
        InternetAddress to = new InternetAddress(receiveUser);
        message.setRecipient(Message.RecipientType.TO, to);
        message.setSubject(headName);
        String content = sendHtml.toString();
        message.setContent(content, "text/html;charset=UTF-8");
        message.saveChanges();
        Transport transport = s.getTransport("smtp");
        transport.connect(VALUE_SMTP, sendUname, sendPwd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


    @RequestMapping(value="/sendForgetPasswordAuthCodeEmail",produces="text/html;charset=utf8",method=RequestMethod.POST)
    @ResponseBody
    public String sendForgetPasswordAuthCodeEmail(String email, HttpServletRequest request)throws Exception{
        int num = 1;
        if (email == null || email == ""){
            num = 2;
        }else {
            //验证邮箱唯一
            User user = userService.getByEmail(email);
            if (user !=null ){
                num = 3;
            }else {
                String emailauthcode = checkCode();
                System.out.println(emailauthcode);
                //发送邮件
                SendEmailController se = new SendEmailController();
                try {
                    se.doSendHtmlEmail("注册新用户", "<P>您此次的邮箱验证码是-->"+emailauthcode +"</p>", ""+email+"");
                } catch (Exception e) {
                    e.printStackTrace();
                    num = 0;
                }
                //将验证码存放到session中
                request.getSession().setAttribute("emailcode", emailauthcode );
            }
        }
        return rep(num);
    }

    /**
     * scheme 前缀
     * serverName 本机
     * serverPort  端口
     * serverPort  最终路径
     */
    @RequestMapping(value="/UpdateUserPass")
    @ResponseBody
    public String updateUserPass(User user,HttpServletRequest request, HttpServletResponse response){
        Integer state=1;
        System.out.println(user);
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = "/commonController/upPass/"+user.getId();
        String url = scheme+"://"+serverName+":"+serverPort+contextPath;
        SendEmailController se = new SendEmailController();
        try {
            se.doSendHtmlEmail("修改密码", "尊敬的用户,点击此处修改密码<a href="+url+">修改密码</a>，有效时间60s(写着玩的)", ""+user.getEmail()+"");
        } catch (Exception e) {
            e.printStackTrace();
            state=0;
        }
        return rep(state);
    }

    public String checkCode() {
        //创建对象
        VerifyCode vc = new VerifyCode();
        //获取图片对象
        BufferedImage bi = vc.getImage();
        //获得图片的文本内容
        String code = vc.getText();
        System.out.println(code);
        return code;
    }


    @RequestMapping(value="/checkeForgetPasswordVcode",produces="text/html;charset=utf8")
    @ResponseBody
    public String checkeForgetPasswordVcode(String checkedCode, HttpServletRequest request, ModelMap map) throws Exception{
        if (checkedCode == null || checkedCode == ""){
            map.put("check","验证码为空");
        }else {
            if (request.getSession().getAttribute("emailcode") == null || request.getSession().getAttribute("emailcode") == ""){
                map.put("check", "该邮箱尚未验证，请点击发送验证码");
            }else {
                if (checkedCode.equals(request.getSession().getAttribute("emailcode").toString())){
                    map.put("check","验证码正确");
                }else {
                    map.put("check", "验证码错误");
                }
            }

        }

        return new Gson().toJson(map);
    }

    public String rep(Integer flag){
        Map<String, Object> map;
        map = new HashMap<String, Object>(10);
        if (flag == 1) {
            map.put("msg", "操作成功");
        } else if (flag == 0){
            map.put("msg", "操作失败");
        }else if (flag.equals(3)){
            map.put("msg","邮箱已存在请重新输入");
        }else if (flag == 2){
            map.put("msg","请输入正确的邮箱");
        }
        return new Gson().toJson(map);
    }


}