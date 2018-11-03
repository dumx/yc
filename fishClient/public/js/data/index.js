
/**  默认加载  **/
$(function () {
    // var code=$("code");
    var process={
        init:function(){
            this.swiper();
            this.public();
            var wxopenId=$.cookie("wxopenId");
            if(wxopenId==undefined||wxopenId=="null" ||wxopenId == null){
                this.getBase();
                this.geyOauth();
            }
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
        public:function () {
            /** 上拉加载更多 **/
            $('.main').infinite().on("infinite", function () {
                $('#loading').show();
                setTimeout(function() {
                    var app='<a href="detail/index.html" class="img">'+
                        '<img src="public/images/list/c-1.jpg" />'+
                        '<div class="tag">'+
                        '<div class="title">'+
                        '<b>简花·单品鲜花月套餐</b>'+
                        '<i>¥800</i>'+
                        '</div>'+
                        '<div class="tips">一周一花，每月4次，精选一周当季花材，品种随机</div>'+
                        '</div>'+
                        '</a>'
                    for(var i=0; i<1;i++){
                        $('.contentList').append(app);
                    }
                    $('#loading').hide();
                },2000)
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

    window.onload = function(){
        process.init();
    }


})
