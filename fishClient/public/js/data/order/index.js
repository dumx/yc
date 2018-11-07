
/** 获取倒计时参数  **/
var clock = '';
var nums = 60;
var btn;

/**  默认加载  **/
$(function () {
    var  process={
        init:function(){
            this.public();
            this.getOrderList();

        },
        public:function () {
			/**  查看物流 **/
           $('html').on('click','.look',function(){
			  var content='<div class="order-address">顺丰快递：SF192929239239</div>'
			  $.modal({
                title: "查看物流",
                text: content,
                buttons: [
                    { text: "确定", onClick: function(){
                      

                    } },
                ]
            })
		   })
		   /** 上拉加载更多 **/
            $('#tab1,#tab2,#tab3,#tab4').infinite().on("infinite", function () {
				var id=$(this).attr('id');
			    if(id=="tab1"){
					 var app='<div class="order-con">'+
                                 '<div class="title">'+
                                    '<b class="num">WS19992002300222</b>'+
                                    '<i class="state">已完成</i>'+
                                 '</div>'+
                                 '<div class="list">'+
                                    '<b><img src="../public/images/icon1.jpg" /></b>'+
                                    '<i>'+
                                       '<p>产品名称</p>'+
                                       '<p class="num">订购数量b2套件</p>'+
									 '</i>'+
								 '</div>'+
								   '<div class="bot">'+
								      '<b class="price">¥1600.00</b>'+
								      '<i class="data">2016-03-28</i>'+
								   '</div>'+
								 '</div>'	 
	
						  for(var i=0; i<5;i++){
						   $('#tab1 .order-content').append(app);
						  }
				 }
				 else if(id=="tab2"){
					 var app='<div class="order-con">'+
                                 '<div class="title">'+
                                    '<b class="num">WS19992002300222</b>'+
                                    '<a href="../detail/order-pay.html" class="pay">去付款</a>'+
                                 '</div>'+
                                 '<div class="list">'+
                                    '<b><img src="../public/images/icon1.jpg" /></b>'+
                                    '<i>'+
                                       '<p>产品名称</p>'+
                                       '<p class="num">订购数量b2套件</p>'+
									 '</i>'+
								 '</div>'+
								   '<div class="bot">'+
								      '<b class="price">¥1600.00</b>'+
								      '<i class="data">2016-03-28</i>'+
								   '</div>'+
								 '</div>'	 
	
						  for(var i=0; i<5;i++){
						   $('#tab2 .order-content').append(app);
						  }
				 }
				 else if(id=="tab3"){
					 var app='<div class="order-con">'+
                                 '<div class="title">'+
                                    '<b class="num">WS19992002300222</b>'+
                                    '<i>待发货</i>'+
                                 '</div>'+
                                 '<div class="list">'+
                                    '<b><img src="../public/images/icon1.jpg" /></b>'+
                                    '<i>'+
                                       '<p>产品名称</p>'+
                                       '<p class="num">订购数量b2套件</p>'+
									 '</i>'+
								 '</div>'+
								   '<div class="bot">'+
								      '<b class="price">¥1600.00</b>'+
								      '<i class="data">2016-03-28</i>'+
								   '</div>'+
								 '</div>'	 
	
						  for(var i=0; i<5;i++){
						   $('#tab3 .order-content').append(app);
						  }
				 }
				 else{
					 var app='<div class="order-con">'+
                                 '<div class="title">'+
                                    '<b class="num">WS19992002300222</b>'+
                                    '<i class="look">查物流</i>'+
                                 '</div>'+
                                 '<div class="list">'+
                                    '<b><img src="../public/images/icon1.jpg" /></b>'+
                                    '<i>'+
                                       '<p>产品名称</p>'+
                                       '<p class="num">订购数量b2套件</p>'+
									 '</i>'+
								 '</div>'+
								   '<div class="bot">'+
								      '<b class="price">¥1600.00</b>'+
								      '<i class="data">2016-03-28</i>'+
								   '</div>'+
								 '</div>'	 
	
						  for(var i=0; i<5;i++){
						   $('#tab4 .order-content').append(app);
						  }
				 }
               
            });
        },
        //获取订单信息
        getOrderList:function () {
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


