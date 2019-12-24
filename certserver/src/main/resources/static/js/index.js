$(document).ready(function() {
    //秘钥申请
    $("body").delegate("#tab-1 .apply","click", function(){
        var  attribute = $.trim($("#tab-1 .set_attribute").val());
        if(attribute == ""){
            swal("提示!", "请输入属性", "warning");
            return false;
        }
         $.ajax({
            type:"post",
            url:"/Cpabe/keygen",
            async:false,
            data:{
               attrStr:attribute,
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-1 .get_secret_key").html(data.data);
                }else {
                     swal("提示!", "属性格式错误，生成秘钥失败", "warning");
                }
            }
        });
    })


    //属性加密
    $("body").delegate("#tab-2 .enc","click", function(){
        var  cert = $.trim($("#tab-2 .cert").val());
        var  policy = $.trim($("#tab-2 .policy").val());
        if(cert == ""){
            swal("提示!", "请输入证书", "warning");
            return false;
        }
        if(policy == ""){
            swal("提示!", "请输入属性加密策略", "warning");
            return false;
        }
        $.ajax({
            type:"post",
            url:"/Cpabe/enc",
            async:false,
            data:{
                cert:cert,
                policy:policy
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-2 .get_secret_key").html(data.data);
                }else {
                    swal("提示!", "属性加密失败", "warning");
                }
            }
        });
    })

    //属性解密
    $("body").delegate("#tab-3 .dec","click", function(){
        var  prvkey = $.trim($("#tab-3 .prvkey").val());
        var  cert_aes = $.trim($("#tab-3 .cert_aes").val());
        if(prvkey == ""){
            swal("提示!", "请输入私钥", "warning");
            return false;
        }
        if(cert_aes == ""){
            swal("提示!", "请输入证书密文", "warning");
            return false;
        }
        $.ajax({
            type:"post",
            url:"/Cpabe/dec",
            async:false,
            data:{
                prvkey:prvkey,
                cert_aes:cert_aes
            },
            success:function(data){
                if(data.status==0) {
                    $("#tab-3 .get_secret_key").html(data.data);
                }else {
                    swal("提示!", "属性解密失败", "warning");
                }
            }
        });
    })

    //复制秘钥
    $("#tab-1 .copy_secret_key ,#tab-2 .copy_secret_key ,#tab-3 .copy_secret_key").click(function(){
        new Clipboard(this);
        return false;
    })
})