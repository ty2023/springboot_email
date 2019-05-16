var getCodeUrl = "/commonController/checkCode";//获取验证码地址
$('#checkCode_img1').click(function () {
    this.src = getCodeUrl;
});

function loginSubmit() {
    var username = $('#username').val();
    var password = $('#password').val();
    var CheckedCode=$('#checkCode').val();
    if (username == "" || password == ""){
        alert("用户名密码不能为空！");
    } else {
        $.post('/userController/login',{'username':username,'password':password,'checkCode':CheckedCode},function (data) {
            if (data.f){
                alert(data.msg);
                location.href='/commonController/succ';
            }else {
                alert(data.msg);
                window.location.reload();
            }
        },"JSON")
    }
}