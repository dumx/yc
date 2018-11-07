
/**  默认加载  **/
$(function () {
    var process={
        init:function(){
            this.swiper();
            this.getIndexInfo();
            var wxopenId=$.cookie("wxopenId");
            if(wxopenId==undefined||wxopenId=="null" ||wxopenId == null){
                this.getBase();
                this.getOauth();
            }
            this.checkMobile();
        },
        swiper:function () {
            /*  轮播效果加载   */
            var t=$(".swiper-wrapper img").length;
            if(t<2){
            }else {
                //首页加载
                $(".swiper-container").swiper({
                    loop: true,
                    autoplayDisableOnInteraction: false,
                    autoplay: 3000
                });
            }
        },
        //加载首页相关信息
        getIndexInfo:function () {
            $.ajax({
                type: "GET",
                url: "/apis/weixin/getIndexInfo?type=ZHENG,LIU",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                //设置token
                // beforeSend: function (request) {
                //     request.setRequestHeader(tokenKey, $.cookie("token"));
                // },
                success: function(data){
                    if(data.status=="success"){
                        var result=data.data;
                        //正钓门票
                        var beginTime = new Date(result[0].dayTime).format("yyyy-MM-dd");
                        $("#zhengName").html(result[0].name);
                        $("#zhengTime").html(beginTime+"   "+result[0].beginHour+":00"+"-"+result[0].endHour+":00");
                        $("#zhengMoney").html("¥"+result[0].totalFee);
                        $("#zhengRemarke").html(result[0].remark);

                        //溜鱼时间
                        var endTime = new Date(result[1].dayTime).format("yyyy-MM-dd");
                        $("#liuName").html(result[1].name);
                        $("#liuTime").html(endTime+"   "+result[1].beginHour+":00"+"-"+result[1].endHour+":00");
                        $("#liuMoney").html(result[1].totalFee);
                        $("#liuRemarke").html(result[1].remark);
                    }

                }
            });
        },

        //验证是否绑定手机号
         checkMobile:function () {
            $.ajax({
                type: "GET",
                url: "/apis/wx/checkMobile?openId="+$.cookie("wxopenId"),
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                //设置token
                // beforeSend: function (request) {
                //     request.setRequestHeader(tokenKey, $.cookie("token"));
                // },
                success: function(data){
                    if(data==false){
                        alertOneInput('请先绑定手机号','','确定');
                    }
                }
            });
        },
        //详情弹窗
        getBase:function () {
            //截取url路径的参数
            function getUrlParam(url, name) {
                var pattern = new RegExp("[?&]" + name + "\=([^&]+)", "g");
                var matcher = pattern.exec(url);
                var items = null;
                if (null != matcher) {
                    try {
                        items = decodeURIComponent(decodeURIComponent(matcher[1]));
                    } catch (e) {
                        try {
                            items = decodeURIComponent(matcher[1]);
                        } catch (e) {
                            items = matcher[1];
                        }
                    }
                }
                return items;
            }
            var code = getUrlParam(location.href, "code");//获取路径的id参数
            if(code==null){
                var url='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx95e739344708f031&redirect_uri='+encodeURIComponent(fromurl)+'&response_type=code&scope=snsapi_userinfo&state=STATE%23wechat_redirect&connect_redirect=1#wechat_redirect';
                location.href=url;
            }
            $.cookie("code",code);
            console.info("第一次获取 code"+code);
        },

        //获取授权信息
         getOauth:function () {
            $.ajax({
                type: "GET",
                url: "/apis/wx/oauth?code="+$.cookie("code"),
                dataType: "json",
                success: function(data){
                    var date = new Date();
                    date.setTime(date.getTime() + 120 * 60 * 1000);//token过期时间,第一个60分钟
                    // if($.cookie("accessToken")!=null){
                        var openId = data.openId;
                        var accessToken=data.accessToken;
                        var refreshToken=data.refreshToken;
                        var yctoken=data.token;
                        var mobile=data.mobile;
                        $.cookie("wxopenId",openId,date);
                        $.cookie("accessToken",accessToken,date);
                        $.cookie("refreshToken",refreshToken,date);
                        //将本系统生成的 token 放入 cookie 进行后续的接口验证
                        $.cookie("token",yctoken);
                        if(mobile==null||mobile==""){
                            alertOneInput('请先绑定手机号','','确定');
                        }
                    // }
                }
            });
        },

    };
    //页面初始化执行函数
    window.onload = function(){
        process.init();
    }
})

//预下单接口
function onIndexUnifiedOrder(){
    $.ajax({
        type: "POST",
        url: "/apis/order/unifiedOrders/weixin",
        dataType: "json",
        data: JSON.stringify({
            "openId": $.cookie("wxopenId"),
            // "orderNo": $.cookie("wxopenId"), //后台填充
            // "notifyUrl": $("#notifyUrl").val(),//后台填充
            "subject": $("#liuName").html(),
            "totalFee": $("#liuMoney").html(),
            "spbillCreateIp": "123.123.1.123",
            //溜鱼不需要座位
            "seatNo":null,
            "type":"LIU"
        }),
        contentType: "application/json; charset=utf-8",
        //设置token
        beforeSend: function (request) {
            request.setRequestHeader(tokenKey, $.cookie("token"));
        },
        success: function(data){
            //成功后则调起微信支付进行支付操作
            var result=data.data;
            console.info(result);
            if(data.status=="success"){
                $("#appId").val(result.appid);
                $("#timeStamp").val(result.timestamp);
                $("#nonceStr").val(result.nonceStr);
                $("#package").val(result.prepayid);
                $("#paySign").val(result.sign);
                //调用微信支付
                onIndexBridgeReady();
            }else{
                // $.toast("未授权");
                // $.toptip('授权失败,请重新授权!',2000, 'error');
                $.alert("网络异常!请稍后再试!");
            }
        }
    });
}

//使用内置函数调用微信支付
function onIndexBridgeReady(){
    WeixinJSBridge.invoke('getBrandWCPayRequest',{
        "appId" :  $("#appId").val(),
        "timeStamp": $("#timeStamp").val(),
        "nonceStr" : $("#nonceStr").val(),
        "package" : "prepay_id="+$("#package").val()+"",
        "signType" : "MD5",
        "paySign" : $("#paySign").val()
    },function(res){
        // alert(res.err_msg);
        if(res.err_msg == "get_brand_wcpay_request:ok" ) {
            $.toptip('支付成功!',2000, 'success');
            // window.location.href="${obj.sendUrl}";
        }else{
            $.toptip('支付失败!',2000, 'error');
            // window.location.href="/mobile/front/payment?id=${goodsOrder.payId}";
        }
    });
}

//带输入框弹窗
function alertOneInput(title, input1,onOK) {
    var config;
    if (typeof text === 'object') {
        config = text;
    } else {
        config = {
            title: title,
            input1: input1,
            onOK: onOK,
            empty: false  //allow empty
        };
    }
    $.modal({
        text: '<input type="text" class="weui-input weui-prompt-input" id="weui-prompt-input" value="' + (config.input1 || '') + '" />',
        title: title,
        buttons: [{
                text: onOK,
                className: "primary",
                onClick: function () {
                    if($("#weui-prompt-input").val()!=""){
                        var boolean=isPhone($("#weui-prompt-input").val());
                        if(boolean==false){
                            $.alert("手机号格式不正确!");
                            location.reload();
                        } else{
                            //执行保存手机号操作
                            saveMobile();
                            console.info($("#weui-prompt-input").val());
                        }
                    }else{
                        $.alert("手机号不能为空!");
                        location.reload();
                        // $.toptip('手机号不能为空',2000, 'error');
                    }
                }
            }
        ]
    });
}

/**
 * 手机号码校验
 * @param value
 * @returns {*}
 */

function isPhone(value) {
    var phone = value;
    var patterns = /^1[3,5,6,8,7,4]\d{9}$/;
    if (!patterns.test(phone)) {
        $.alert('手机号码错误');
        return false;
    }
    return true;

}

//保存用户手机号
function saveMobile() {
    //<button onclick="alertTwoInput('测试描述','标题','请输入name','请输入pass','取消','确定');">测试</button>
    alertOneInput('请输入手机号','','确定');
    $.ajax({
        type: "POST",
        url: "/apis/wx/saveMobile",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            "openId": "og9a21KfQaZ_xSA-VtZC1W9MuaK0",
            "mobile": $("#weui-prompt-input").val()
        }),
        success: function(data){
            console.info(data);
            if(data.status=="success"){
                $.cookie("mobile",data.data.mobile);
                $.closeModal();
                $.toptip('手机号保存成功',2000, 'success');
            }
        }
    });
}

Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}







