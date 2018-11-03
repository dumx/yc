//将表单序列化成json
$.fn.serializeJson = function () {
    var serializeObj = {};
    var array = this.serializeArray();
    var str = this.serialize();
    $(array).each(function () {
        var val_trim = this.value;
        if (isNaN(this.value)) {
            val_trim = $.trim(this.value);
        }
        if (serializeObj[this.name]) {
            if ($.isArray(serializeObj[this.name])) {
                serializeObj[this.name].push(val_trim);
            } else {
                serializeObj[this.name] = [serializeObj[this.name], val_trim];
            }
        } else {
            serializeObj[this.name] = val_trim;
        }
    });
    return serializeObj;
};
// 点击图片触发input的onchange事件
$('#pic').click(function () {
    $('#file').click();
});
$("#file").change(function () {
    //获取文件对象，files是文件选取控件的属性，存储的是文件选取控件选取的文件对象，类型是一个数组
    var fileObj = document.getElementById("file").files[0]; //  js获取文件对象
    //创建formdata对象，formData用来存储表单的数据，表单数据时以键值对形式存储的。
    var formData = new FormData();
    formData.enctype = "multipart/form-data";
    formData.append('file', fileObj);
    $.ajax({
        url: appUrl + "/sitepeo/uploadImg",
        type: "post",
        dataType: "json",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (json_data) {
            if (json_data.data) {
                $("#idCard").val(json_data.data.idCard.idCard);
                $("#name").val(json_data.data.idCard.username);
            }
        },
        error: function (err) {
            console.log(err.statusText);
        }
    });
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
    var phone = $('#mobile').val();
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
            url: appUrl + '/sendMessage/memberRegister?mobile=' + phone,
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
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

//验证验证码
/*function isEqual() {
    if ($("#phoneCode").val() != $("#code").val()) {
        layer.msg("您输入的验证码不正确！");
    }
}*/

//获取地址
function getCity() {
    $.ajax({
        url: appUrl + "/region/selectLinkage",
        type: "get",
        success: function (data) {
            var html = "<option value=''>请选择</option>";
            if ((data.data).length > 0) {
                $.each(data.data, function (i, list) {
                    html += `
                            	<option value=${list.id}>${list.name}</option>
                            `;
                });
                $("#city").html(html);
            }
        }, error: function (err) {
            console.log(err);
        }
    })
};
getCity();

//获取县区
function getCounty() {
    //市局切换时清空县局
    var html = "<option value=''>请选择</option>";
    $("#county").html('');
    $("#town").html('');
    $("#village").html('');
    $("#subofficeSite").html('');
    $("#address").val("");
    if ($("#city").val() != "") {
        $.ajax({
            url: appUrl + "/region/selectLinkage",
            type: "get",
            data: "regionId=" + $("#city").val(),
            success: function (data) {
                var html = "<option value=''>请选择</option>";
                if ((data.data).length > 0) {
                    $.each(data.data, function (i, list) {
                        html += `
                                	<option value=${list.id}>${list.name}</option>
                                `;
                    })
                    $("#county").html(html);
                }
            }
        })
    } else {//市局为空把县局，村庄和支局全部清空
        $("#county").html("");
        $("#town").html("");
        $("#village").html("");
        $("#subofficeSite").html("");
    }
}

//获取乡镇
function getTown() {
//县局切换时清空支局
    var html = "<option value=''>请选择</option>";
    $("#town").html('');
    $("#village").html('');
    $("#subofficeSite").html('');
    $("#address").val("");
    if ($("#county").val() != "") {
        $.ajax({
            url: appUrl + "/region/selectLinkage",
            type: "get",
            data: {"regionId": $("#county").val()},
            success: function (data) {
                if ((data.data).length > 0) {
                    $.each(data.data, function (i, list) {
                        html += `
                            	<option value=${list.id} >${list.name}</option>
                            `;
                    });
                    $("#town").html(html);
                }
            }
        })
    } else {//县局为空把村庄和支局全部清空
        $("#town").html('');
        $("#village").html('');
        $("#subofficeSite").html('');
    }
}

//获取村庄
function getVillage() {
//县局切换时清空支局
    var html = "<option value=''>请选择</option>";
    $("#village").html('');
    $("#subofficeSite").html('');
    $("#address").val("");
    if ($("#town").val() != "") {
        $.ajax({
            url: appUrl + "/region/selectLinkage",
            type: "get",
            data: {"regionId": $("#town").val()},
            success: function (data) {
                if ((data.data).length > 0) {
                    $.each(data.data, function (i, list) {
                        if(list.site){
                            html += `
                            	<option value=${list.id} siteid=${list.site.id} sitename=${list.site.siteName}>${list.name}</option>
                            `;
                        }else {
                            html += `
                            	<option value=${list.id}>${list.name}</option>
                            `;
                        }
                    });
                    $("#village").html(html);
                }
            }
        })
    } else {//县局为空把村庄和支局全部清空
        $("#village").html('');
        $("#subofficeSite").html('');
    }
}

//获取村庄
function getSite() {
    if ($("#village").val() != "" && $("#village option:selected").attr("siteId")) {
        $("#subofficeSite").html('<option value=' + $("#village option:selected").attr("siteId") + '>' + $("#village option:selected").attr("siteName") + '</option>');
    } else {
        $("#subofficeSite").val("");
    }
    var aa = $("#city option:selected").text() + $("#county option:selected").text() + $("#town option:selected").text() + $("#village option:selected").text();
    $("#address").val(aa);
}

//提交
function finishPost() {
    $.ajax({
        url: appUrl + "/members/register",
        data: JSON.stringify($("#registerForm").serializeJson()),
        type: 'put',
        contentType: 'application/json',
        success: function (data) {
            if (data.status == 0) {
                location.href = "tips.html";
            } else {
                layer.open({
                    title: [
                        '提示信息',
                        'background-color:#1AA094; color:#fff;'
                    ],
                    content:data.data,
                    btn: '我知道了'
                });
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
}

//先验证再提交
$("#registerForm").mvalidate({
    type: 1,//验证类型,类型1：弹出提示信息，类型2：未通过验证的表单下面显示提示文字
    onKeyup: true,//输入放开键盘的时候,是否需要验证
    sendForm: false,//表单通过验证的时候，是否需要提交表单
    firstInvalidFocus: false,//未通过验证的第一个表单元素，是否要获取焦点
    valid: function () {//点击"下一步"按钮的时候，若表单通过验证，就触发该函数！
        finishPost();
    },
    descriptions: {//输入域通过data-descriptions="name"对应到descriptions中属性名等于name的函数
        name: {
            required: '请输入姓名'
        },
        idCard: {
            required: '请输入身份证号'
        },
        mobile: {
            required: '请输入手机号',
            pattern:"您输入的手机号格式不正确"
        },
        mobileMessageCode: {
            required: '请输入验证码'
        },
        regionId: {
            required: '请选择小区/村'
        },
        siteId: {
            required: '请选择所属支局'
        },
        referrerName: {
            required: '请输入姓名或名称'
        }
    }
});