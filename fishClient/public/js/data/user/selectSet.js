
/** 获取倒计时参数  **/
var clock = '';
var nums = 60;
var btn;

/**  默认加载  **/
$(function () {
    var  process={
        init:function(){
            this.public();
        },
        public:function () {
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
                        $("#name").val(result[0].name);
                        $("#totalMoney").val(result[0].totalFee);
                    }

                }
            });
        },

    };
    window.onload = function(){
        process.init();
    }


})

//预下单接口
function onUnifiedOrder(){
    console.info($("#totalMoney").val() / 100);
    var selectSeat=$("#selectSet").val()
    if(selectSeat==null && selectSeat==""){
        return;
    }
    $.ajax({
        type: "POST",
        url: "/apis/order/unifiedOrders/weixin",
        dataType: "json",
        data: JSON.stringify({
            "openId": $.cookie("wxopenId"),
            // "orderNo": $.cookie("wxopenId"), //后台填充
            // "notifyUrl": $("#notifyUrl").val(),//后台填充
            "subject": $("#name").val(),
            "totalFee": $("#totalMoney").val() / 100,
            "spbillCreateIp": "123.123.1.123",
            //获取选中的座位号
            "seatNo":selectSeat,
            "type":"ZHENG"
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
                onBridgeReady();
            }else{
                $.alert(data.data);
            }
        }
    });
}

//使用内置函数调用微信支付
function onBridgeReady(){
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


