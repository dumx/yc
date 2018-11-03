
/** 获取倒计时参数  **/
var clock = '';
var nums = 60;
var btn;

/**  默认加载  **/
$(function () {
    var  process={
        init:function(){
            this.chaeckToken();
        },
        chaeckToken:function () {
            var openId=$.cookie("wxopenId");
            var accessToken=$.cookie("accessToken");
            var refreshToken=$.cookie("refreshToken");
            $.ajax({
                type: "GET",
                url: "/apis/wx/checkToken?accessToken="+accessToken+"&openId="+openId,
                dataType: "json",
                success: function(data){
                    //token 过期刷新 token
                    if(data==40001){
                        $.ajax({
                            type: "GET",
                            url: "/apis/wx/refreshToken?refreshToken="+refreshToken,
                            dataType: "json",
                            success: function(refreshData){
                                $.cookie("accessToken",refreshData);
                                getUserData();
                            }
                        });
                    }else{
                        //直接获取用户信息
                        getUserData();
                    }
                }
            });
        },
        //获取微信用户个人信息如头像,昵称
        // getUserData:function () {
        //     var openId=$.cookie("wxopenId");
        //     var accessToken=$.cookie("accessToken");
        //     $.ajax({
        //         type: "GET",
        //         url: "/apis/wx/userInfo?accessToken="+accessToken+"&openId="+openId,
        //         dataType: "json",
        //         success: function(data){
        //             var img="<img src="+data.headImg+" />"
        //             $("#headImg").html(img);
        //             $("#nickname").html(data.nickname);
        //         }
        //     });
        // }

    };

    function getUserData(){
        var openId=$.cookie("wxopenId");
        var accessToken=$.cookie("accessToken");
        $.ajax({
            type: "GET",
            url: "/apis/wx/userInfo?accessToken="+accessToken+"&openId="+openId,
            dataType: "json",
            success: function(data){
                var img="<img src="+data.headImg+" />"
                $("#headImg").html(img);
                $("#nickname").html(data.nickname);
            }
        });
    }

    window.onload = function(){
        process.init();
    }


})


