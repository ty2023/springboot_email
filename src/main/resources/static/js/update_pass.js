function Update_Pass() {
    var pass1 = $('#password1').val();
    var pass2 = $('#password2').val();
    if (pass1 != pass2){
        $('#span_pass1').html("密码不一致");
        $('#span_pass2').html("密码不一致");
    }else {
        if (pass1 == "" || pass2 ==""){
            alert("密码不能为空");
            return false;
        }
        var id = $('#userId').val();
        debugger
        $.post('/userController/updateUserPassWord',{'id':id,'password':pass1},function (data) {
            if (data.f){
                alert(data.up);
                location.href='/commonController/log';
            } else{
                alert(data.up);
                window.location.reload();
            }
        },"JSON");
    }
}