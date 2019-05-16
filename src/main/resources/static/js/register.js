$(function () {
    //发送邮件并且判断邮箱是否唯一
    $('#checkEmail').click(function () {
        var email = $('#email').val();
        $.post('/email/sendForgetPasswordAuthCodeEmail',{'email':email},function (data) {
            alert(data.msg);
        },"JSON");
    });

    //验证验证码是否正确
    $('#CheckedCode').blur(function () {
        var CheckedCode=$('#CheckedCode').val();
        $.post('/email/checkeForgetPasswordVcode',{'checkedCode':CheckedCode},function (data) {
            $('#span_email').html(data.check);
        },"JSON")
    });

    //验证用户名是否唯一
    $('#username').blur(function () {
        var param = new Object();
        if ($('#username').val() == null ||$('#username').val() == ''){
            $('#span_name').html("用户名不能为空");
            return false;
        }
        param.username = $('#username').val();
        $.post('/userController/getByUserName',param,function (data) {
            $('#span_name').html(data.userName);
        },"JSON");
    });
})

//提交表单
function submitbtn() {
    var param = new Object();
    param.username = $('#username').val();
    param.password = $('#password').val();
    param.email = $('#email').val();
    param.age = $('#age').val();
    param.sex = $('input:radio:checked').val();
    if (panduan()){
        $.post('/userController/registerUser',param,function (data){
            if(data.f){
                alert(data.m);
                location.href='/commonController/log';
            }else{
                alert(data.m);
                window.location.reload();
            }
        },"JSON");
    }else {
        alert("星号标记项不能为空");
    }
}

function panduan() {
    var username = $('#username').val();
    var password = $('#password').val();
    var email = $('#email').val();
    var age = $('#age').val();
    var sex = $('input:radio:checked').val();
    if (username == ""||password == ""||email == ""||age == ""||sex == ""){
        return false;
    }
    return true;
}
