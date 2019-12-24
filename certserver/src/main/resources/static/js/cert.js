$(document).ready(function() {

    //用户区块链注册
    $("body").delegate("#tab-1 .register","click", function(){
        var  userId = $.trim($("#tab-1 .userId").val());
        var  userName = $.trim($("#tab-1 .userName").val());
        if(userId == ""){
            swal("提示!", "请输入用户ID", "warning");
            return false;
        }
        if(userName == ""){
            swal("提示!", "请输入用户姓名", "warning");
            return false;
        }
         $.ajax({
            type:"post",
            url:"/certWeb/userRegister",
            async:false,
            data:{
                userId:userId,
                userName:userName,
            },
            success:function(data){
                console.log(data);
                if(data.status==0) {
                    $("#tab-1 .status").html("<i class=\"fa fa-spin fa-refresh fa-2x\"></i>");
                    setTimeout("$('#tab-1 .status').html('注册成功')",4000);
                }else {
                    $("#tab-1 .status").html("<i class=\"fa fa-spin fa-refresh fa-2x\"></i>");
                    setTimeout("$(\"#tab-1 .status\").html(\"注册失败\")",4000);
                   /* $("#tab-1 .status").html("注册失败");*/
                }
            }
        });
    })

    //证书上传
    $("body").delegate("#tab-2 .certUpload","click", function(){
        var  certId = $.trim($("#tab-2 .certId").val());
        var  certName = $.trim($("#tab-2 .certName").val());
        var  userId = $.trim($("#tab-2 .userId").val());
        var  school = $.trim($("#tab-2 .school").val());
        var  policy = $.trim($("#tab-2 .policy").val());
        var  certMessage = $.trim($("#tab-2 .certMessage").val());

        if(certId == ""){
            swal("提示!", "请输入证书ID", "warning");
            return false;
        }
        if(certName == ""){
            swal("提示!", "请输入证书名称", "warning");
            return false;
        }

        if(userId == ""){
            swal("提示!", "请输入用户ID", "warning");
            return false;
        }
        if(school == ""){
            swal("提示!", "请输入学校", "warning");
            return false;
        }
        if($("#tab-2").find(":radio:checked").val() == "2" && policy == ""){
            swal("提示!", "请输入属性加密策略", "warning");
            return false;
        }
        if(certMessage == ""){
            swal("提示!", "请输入证书内容", "warning");
            return false;
        }

        $.ajax({
            type:"post",
            url:"/certWeb/uploadCert",
            async:false,
            data:{
                certId: certId,
                userId: userId,
                certName: certName,
                school: school,
                policy: policy,
                certMessage: certMessage
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-2 .status").html("<i class=\"fa fa-spin fa-refresh fa-2x\"></i>");
                    setTimeout("$(\"#tab-2 .status\").html(\"上传成功\")",4000);
                    /*$("#tab-2 .status").html("上传成功");*/
                }else {
                    $("#tab-2 .status").html("<i class=\"fa fa-spin fa-refresh fa-2x\"></i>");
                    setTimeout("$(\"#tab-2 .status\").html(\"上传失败\")",4000);
                    /*$("#tab-2 .status").html("上传失败");*/
                }
            }
        });
    })


    //查询用户
    $("body").delegate("#tab-3 .queryUser","click", function(){
        var  userId = $.trim($("#tab-3 .userId").val());
        if(userId == ""){
            swal("提示!", "请输入用户ID", "warning");
            return false;
        }
        $.ajax({
            type:"post",
            url:"/certWeb/queryUser",
            async:false,
            data:{
                userId:userId,
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-3 .status").html(data.data);
                }else {
                    $("#tab-3 .status").html(data.msg);
                }
            }
        });
    })

    //证书查询
    $("body").delegate("#tab-4 .queryCert","click", function(){
        var  certId = $.trim($("#tab-4 .certId").val());
        if(certId == ""){
            swal("提示!", "请输入证书ID", "warning");
            return false;
        }
        $.ajax({
            type:"post",
            url:"/certWeb/queryCert",
            async:false,
            data:{
                certId:certId,
                prvkey: $.trim($("#tab-4 .prvkey").val())
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-4 .status").html(data.data);
                }else {
                    $("#tab-4 .status").html(data.msg);
                }
            }
        });
    })

    //用户证书查询
    $("body").delegate("#tab-5 .queryCertWithUserId","click", function(){
        var  userId = $.trim($("#tab-5 .userId").val());
        var  certId = $.trim($("#tab-5 .certId").val());
        if(userId == ""){
            swal("提示!", "请输入用户ID", "warning");
            return false;
        }
        if(certId == ""){
            swal("提示!", "请输入证书ID", "warning");
            return false;
        }
        $.ajax({
            type:"post",
            url:"/certWeb/queryCertWithUserId",
            async:false,
            data:{
                userId:userId,
                certId:certId,
                prvkey: $.trim($("#tab-5 .prvkey").val())
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-5 .status").html(data.data);
                }else {
                    $("#tab-5 .status").html(data.msg);
                }
            }
        });
    })

    //Hash验证
    $("body").delegate("#tab-6 .computeHash","click", function(){
        var  message = $.trim($("#tab-6 .message").val());
        if(message == ""){
            swal("提示!", "请输入证书明文", "warning");
            return false;
        }
        $.ajax({
            type:"post",
            url:"/certWeb/computeHash",
            async:false,
            data:{
                message:message
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-6 .status").html(data.data);
                }else {
                    $("#tab-6 .status").html(data.msg);
                }
            }
        });
    })

    //属性加密策略
    $("body").delegate("#tab-2 .i-checks","ifChanged", function(event){
        if($(this).prop("checked")){
            if($(this).val()=="1"){
                $("#tab-2 .policy"). attr("readonly","readonly");
            }else{
                $("#tab-2 .policy").attr("readonly",false);
            }
        }
    })

    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });

})