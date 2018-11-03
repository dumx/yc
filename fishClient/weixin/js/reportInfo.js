//防止点击添加图片就更换图片
$(".plus_b").click(function(){
    $("#busFile").click();
});


//识别营业执照
//上传营业执照照片
function changeBus() {
    $("#busFile").change(function (e) {
        var file = this.files[0];
        var r = new FileReader();
        r.readAsDataURL(file);
        $(r).load(function () {
            var aa="<img class='newPic orientPic4' src="+this.result+">";
                aa+="<img src='../images/remove2.png' class='picRemove' onclick='picRemoveBus();' >";
            $('.picBus').html(aa);
            /*$("#busFile").prev().hide();
            $('.picBus').append('<img class="newPic" src="' + this.result + '" alt="" />');
            $('.picBus').append('<img src="../images/remove2.png" class="picRemove" onclick="picRemoveBus();">');*/
        });
        //根据手机相册内照片的EXIF属性判断，EXIF属性里面有个Orientation的值，来确定照片是否需要旋转
        EXIF.getData(e.target.files[0], function() {
            var Orientation=EXIF.getTag(this, "Orientation"),spin_n;
            console.log('营业执照方向值='+Orientation);
            if(Orientation==3){//180°
                spin_n=180;
            }else if(Orientation==6){//顺时针90°
                spin_n=90;
            }else if(Orientation==8){//逆时针90°
                spin_n=-90;
            }
            $("img.orientPic4").css({
                "transform":"rotate("+ spin_n +"deg)",
                "-moz-transform":"rotate("+ spin_n +"deg)",
                "-ms-transform":"rotate("+ spin_n +"deg)",
                "-o-transform":"rotate("+ spin_n +"deg)",
                "-webkit-transform":"rotate("+ spin_n +"deg)"
            });
        });


        var fileObj = document.getElementById("busFile").files[0]; // 获取文件对象
        //创建formdata对象，formData用来存储表单的数据，表单数据时以键值对形式存储的。
        var formData = new FormData();
        formData.enctype = "multipart/form-data";
        formData.append('file', fileObj);
        $.ajax({
            url: "/api/sitepeo/uploadBusin",
            type: "post",
            dataType: "json",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (json_data) {
                if (json_data.data) {
                    $("#businessAtt").val(json_data.data.fileId);
                    $("#validDate").val(json_data.data.busCertif.validDate);
                    $("#busLicense").val(json_data.data.busCertif.creditCode);
                    $("#address").val(json_data.data.busCertif.address);
                    $("#siteName").val(json_data.data.busCertif.commpany);
                }
            },
            error: function (err) {
                console.log(err.statusText);
            }
        });
    });
}

//删除营业执照照片
function picRemoveBus() {
    var aa = `
        <img class="plus_b" src="../images/plus.png">
        <input type="file" name="busFile" id="busFile" accept="image/*" onclick="changeBus();" data-required="true" data-descriptions="busFile"/>
    `;
    $('.picBus').html(aa);
    $("#busFile").val("");
    $("#address").val("");
    $("#busLicense").val("");
    // 点击添加图片触发input的onchange事件
    setTimeout(function () {
        $('.plus_b').click(function () {
            $("#busFile").click();
        });
    }, 200);
}
// 点击添加图片触发input的onchange事件
$('.plus').click(function () {
    $("#innerPic").click();
});
$('.plus2').click(function () {
    $("#outerPic").click();
});



//获取手机验证码
/*获取验证码*/
var isPhone = 1;

function getCode(e) {
    checkPhone(); //验证手机号码
    if (isPhone) {
        resetCode(); //倒计时
    } else {
        $('#mobile').focus();
    }

}

//验证手机号码
function checkPhone() {
    var phone = $('#bossPhone').val();
    var pattern = /^[1][3,4,5,7,8][0-9]{9}$/;
    isPhone = 1;
    if (phone == '') {
        layer.msg('请输入手机号');
        isPhone = 0;
        return;
    } else if (!pattern.test(phone)) {
        layer.msg('请输入正确的手机号');
        isPhone = 0;
        return;
    } else {
        $.ajax({
            url: appUrl + '/sendMessage/siteRegister?mobile=' + phone,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    $("#phoneCode").val(data.data);
                } else if (data.status == 1) {
                    layer.msg(data.data);
                } else {
                    layer.msg('获取验证码失败！');
                }
            },
            error: function (err) {
                console.log(err);
            }
        })
    }
}

//倒计时
function resetCode() {
    $('#J_getCode').hide();
    $('#J_second').html('60');
    $('#J_resetCode').show();
    var second = 60;
    var timer = null;
    timer = setInterval(function () {
        second -= 1;
        if (second > 0) {
            $('#J_second').html(second);
        } else {
            clearInterval(timer);
            $('#J_getCode').show();
            $('#J_resetCode').hide();
        }
    }, 1000);
}


$("#reportInfoForm").mvalidate({
    type: 1,//验证类型,类型1：弹出提示信息，类型2：未通过验证的表单下面显示提示文字
    onKeyup: true,//输入放开键盘的时候,是否需要验证
    sendForm: false,//表单通过验证的时候，是否需要提交表单
    firstInvalidFocus: false,//未通过验证的第一个表单元素，是否要获取焦点
    valid: function () {//点击"下一步"按钮的时候，若表单通过验证，就触发该函数！
        finishPost();
    },
    descriptions: {//输入域通过data-descriptions="name"对应到descriptions中属性名等于name的函数
        bossName: {
            required: '请输入姓名'
        },
        bossPhone: {
            required: '请输入手机号',
            pattern : '您输入的手机号格式不正确'
        },
        messageCode: {
            required: '请输入验证码'
        },
        busFile:{
            required: '请上照片'
        }
    }
});

function finishPost() {
    if(!$("#village").val()){
        layer.open({
            title: [
                '提示信息',
                'background-color:#1AA094; color:#fff;'
            ],
            content: '小区/村不能为空',
            btn: '确定'
        });
    }
    var jsonstr = JSON.stringify($("#reportInfoForm").serializeJson());
    $.ajax({
        url: appurl + "/sitepeo/save",
        data: jsonstr,
        type: 'put',
        contentType: 'application/json',
        success: function (data) {
            console.info(data);
            if (data.status == 0) {
                location.href = "tips2.html";
            } else {
                layer.open({
                    title: [
                        '提示信息',
                        'background-color:#1AA094; color:#fff;'
                    ],
                    content: data.data,
                    btn: '确定'
                });
            }
        },
        error: function (err) {
            layer.msg(err.responseText.errors[0].errmsg);
            console.log(err);
        }
    });
}
