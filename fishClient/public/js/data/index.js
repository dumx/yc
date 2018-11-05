
/**  默认加载  **/
$(function () {
    var process={
        init:function(){
            this.swiper();
            // this.public();
            var wxopenId=$.cookie("wxopenId");
            if(wxopenId==undefined||wxopenId=="null" ||wxopenId == null){
                this.getBase();
                this.geyOauth();
            }
            // 10 微信支付接口
            // 10.1 发起一个支付请求
            // document.querySelector('#chooseWXPay').onclick = function () {
            //     // 注意：此 Demo 使用 2.7 版本支付接口实现，建议使用此接口时参考微信支付相关最新文档。
            //     wx.chooseWXPay({
            //         timestamp: 1414723227,
            //         nonceStr: 'noncestr',
            //         package: 'addition=action_id%3dgaby1234%26limit_pay%3d&bank_type=WX&body=innertest&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F120.204.206.246%2Fcgi-bin%2Fmmsupport-bin%2Fnotifypay&out_trade_no=1414723227818375338&partner=1900000109&spbill_create_ip=127.0.0.1&total_fee=1&sign=432B647FE95C7BF73BCD177CEECBEF8D',
            //         signType: 'MD5', // 注意：新版支付接口使用 MD5 加密
            //         paySign: 'bd5b1933cda6e9548862944836a9b52e8c9a2b69'
            //     });
            // };

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
        // public:function () {
        //     /** 上拉加载更多 **/
        //     $('.main').infinite().on("infinite", function () {
        //         $('#loading').show();
        //         setTimeout(function() {
        //             var app='<a href="detail/index.html" class="img">'+
        //                 '<img src="public/images/list/c-1.jpg" />'+
        //                 '<div class="tag">'+
        //                 '<div class="title">'+
        //                 '<b>简花·单品鲜花月套餐</b>'+
        //                 '<i>¥800</i>'+
        //                 '</div>'+
        //                 '<div class="tips">一周一花，每月4次，精选一周当季花材，品种随机</div>'+
        //                 '</div>'+
        //                 '</a>'
        //             for(var i=0; i<1;i++){
        //                 $('.contentList').append(app);
        //             }
        //             $('#loading').hide();
        //         },2000)
        //     });
        // },
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
         geyOauth:function () {
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
                        $.cookie("wxopenId",openId,date);
                        $.cookie("accessToken",accessToken,date);
                        $.cookie("refreshToken",refreshToken,date);
                        //将本系统生成的 token 放入 cookie 进行后续的接口验证
                        $.cookie("token",yctoken);
                    // }
                }
            });
        }

    };

    function getPackage()
    {
        var banktype = "WX";
        var body = getBody(); // 商品名称信息，这里由测试网页填入。
        var fee_type = "1";	 // 费用类型，这里1为默认的人民币
        var input_charset = "GBK"; 	// 字符集，这里将统一使用GBK
        var out_trade_no = getOrderSerial(); // 订单号，商户需要保证该字段对于本商户的唯一性
        var total_fee = getTotalFee();	// 总金额。
        var partner = getPartnerId(); // 商户注册是分配的partnerId
        var notify_url = "http://test/APIController/payNotify"; // 支付成功后将通知该地址
        var spbill_create_ip = getClientIP(); // 用户浏览器的ip，这个需要在前端获取。这里使用127.0.0.1测试值
        var partnerKey = getPartnerKey(); // 这个值和以上其他值不一样是：
        // 签名需要它，而最后组成的传输字符串不能含有它。
        // 这个key是需要商户好好保存的。

        // 首先第一步：对原串进行签名，注意这里不要对任何字段进行编码。
        // 这里是将参数按照key=value进行字典排序后组成下面的字符串,在这个字符串最后拼接上key=XXXX。
        // 由于这里的字段固定，因此只需要按照这个顺序进行排序即可。
        var signString = "bank_type="+banktype
            +"&body="+body
            +"&fee_type="+fee_type
            +"&input_charset="+input_charset
            +"&notify_url="+notify_url
            +"&out_trade_no="+out_trade_no
            +"&partner="+partner
            +"&spbill_create_ip="+spbill_create_ip
            +"&total_fee="+total_fee
            +"&key="+partnerKey;
        var md5SignValue = ("" + CryptoJS.MD5(signString)).toUpperCase();

        // 然后第二步，对每个参数进行url转码。
        // 如果您的程序是用js，那么需要使用encodeURIComponent函数进行编码。
        banktype = encodeURIComponent(banktype);
        body = encodeURIComponent(body);
        fee_type = encodeURIComponent(fee_type);
        input_charset = encodeURIComponent(input_charset);
        notify_url = encodeURIComponent(notify_url);
        out_trade_no = encodeURIComponent(out_trade_no);
        partner = encodeURIComponent(partner);
        spbill_create_ip = encodeURIComponent(spbill_create_ip);
        total_fee = encodeURIComponent(total_fee);

        // 然后最后一步，这里按照key＝value除了sign外进行字典序排序后组成下列的字符串，
        // 最后再串接sign=value
        var completeString = "bank_type="+banktype
            +"&body="+body
            +"&fee_type="+fee_type
            +"&input_charset="+input_charset
            +"&notify_url="+notify_url
            +"&out_trade_no="+out_trade_no
            +"&partner="+partner
            +"&spbill_create_ip="+spbill_create_ip
            +"&total_fee="+total_fee;
        completeString = completeString + "&sign="+md5SignValue;

        oldPackageString = completeString;// 记住package，方便最后进行整体签名时取用

        return completeString;
    }

    //页面初始化执行函数
    window.onload = function(){
        process.init();
    }
})

//预下单接口
function onUnifiedOrder(){
    $.ajax({
        type: "POST",
        url: "/apis/order/unifiedOrders/weixin",
        dataType: "json",
        data: JSON.stringify({
            "openId": $.cookie("wxopenId"),
            // "orderNo": $.cookie("wxopenId"), //后台填充
            // "notifyUrl": $("#notifyUrl").val(),//后台填充
            "subject": "门票",
            "totalFee": 1,
            "spbillCreateIp": "123.123.1.123",
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
        alert(res.err_msg);
        if(res.err_msg == "get_brand_wcpay_request:ok" ) {
            // window.location.href="${obj.sendUrl}";
        }else{
            // window.location.href="/mobile/front/payment?id=${goodsOrder.payId}";
        }
    });
}


