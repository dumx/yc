
/** 获取倒计时参数  **/
var clock = '';
var nums = 60;
var btn;

/**  默认加载  **/
$(function () {
    var  process={
        init:function(){
            this.public();
			//已开始
            getTab(1);
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
            // $('.weui-tab').on("click", function () {
            //     var id=$(this).attr('id');
            //     alert(id);
            // });
		   // /** 上拉加载更多 **/
            // $('#tab1,#tab2,#tab3,#tab4').infinite().on("infinite", function () {
			// 	var id=$(this).attr('id');
			//     if(id=="tab1"){
			// 		 var app='<div class="order-con">'+
            //                      '<div class="title">'+
            //                         '<b class="num">WS19992002300222</b>'+
            //                         '<i class="state">已完成</i>'+
            //                      '</div>'+
            //                      '<div class="list">'+
            //                         '<b><img src="../public/images/icon1.jpg" /></b>'+
            //                         '<i>'+
            //                            '<p>产品名称</p>'+
            //                            '<p class="num">订购数量b2套件</p>'+
			// 						 '</i>'+
			// 					 '</div>'+
			// 					   '<div class="bot">'+
			// 					      '<b class="price">¥1600.00</b>'+
			// 					      '<i class="data">2016-03-28</i>'+
			// 					   '</div>'+
			// 					 '</div>'
           //
			// 			  for(var i=0; i<5;i++){
			// 			   $('#tab1 .order-content').append(app);
			// 			  }
			// 	 }
			// 	 else if(id=="tab2"){
			// 		 var app='<div class="order-con">'+
            //                      '<div class="title">'+
            //                         '<b class="num">WS19992002300222</b>'+
            //                         '<a href="../detail/order-pay.html" class="pay">去付款</a>'+
            //                      '</div>'+
            //                      '<div class="list">'+
            //                         '<b><img src="../public/images/icon1.jpg" /></b>'+
            //                         '<i>'+
            //                            '<p>产品名称</p>'+
            //                            '<p class="num">订购数量b2套件</p>'+
			// 						 '</i>'+
			// 					 '</div>'+
			// 					   '<div class="bot">'+
			// 					      '<b class="price">¥1600.00</b>'+
			// 					      '<i class="data">2016-03-28</i>'+
			// 					   '</div>'+
			// 					 '</div>'
           //
			// 			  for(var i=0; i<5;i++){
			// 			   $('#tab2 .order-content').append(app);
			// 			  }
			// 	 }
			// 	 else if(id=="tab3"){
			// 		 var app='<div class="order-con">'+
            //                      '<div class="title">'+
            //                         '<b class="num">WS19992002300222</b>'+
            //                         '<i>待发货</i>'+
            //                      '</div>'+
            //                      '<div class="list">'+
            //                         '<b><img src="../public/images/icon1.jpg" /></b>'+
            //                         '<i>'+
            //                            '<p>产品名称</p>'+
            //                            '<p class="num">订购数量b2套件</p>'+
			// 						 '</i>'+
			// 					 '</div>'+
			// 					   '<div class="bot">'+
			// 					      '<b class="price">¥1600.00</b>'+
			// 					      '<i class="data">2016-03-28</i>'+
			// 					   '</div>'+
			// 					 '</div>'
           //
			// 			  for(var i=0; i<5;i++){
			// 			   $('#tab3 .order-content').append(app);
			// 			  }
			// 	 }
			// 	 else{
			// 		 var app='<div class="order-con">'+
            //                      '<div class="title">'+
            //                         '<b class="num">WS19992002300222</b>'+
            //                         '<i class="look">查物流</i>'+
            //                      '</div>'+
            //                      '<div class="list">'+
            //                         '<b><img src="../public/images/icon1.jpg" /></b>'+
            //                         '<i>'+
            //                            '<p>产品名称</p>'+
            //                            '<p class="num">订购数量b2套件</p>'+
			// 						 '</i>'+
			// 					 '</div>'+
			// 					   '<div class="bot">'+
			// 					      '<b class="price">¥1600.00</b>'+
			// 					      '<i class="data">2016-03-28</i>'+
			// 					   '</div>'+
			// 					 '</div>'
           //
			// 			  for(var i=0; i<5;i++){
			// 			   $('#tab4 .order-content').append(app);
			// 			  }
			// 	 }
           //
            // });
        }
    };

    window.onload = function(){
        process.init();
    }


})


//tab 切换事件
function getTab(id) {
	if(id==1){
        $('#tab1 .order-content').html("");
		//已开始
        getStartOrderList();
    }else if(id==2){
        $('#tab2 .order-content').html("");
		//未开始
        getUnStartOrderList();
	}else{
        $('#tab3 .order-content').html("");
    	//鱼获

	}
}


//已开始
function getStartOrderList() {
    $('#tab1 .order-content').html("");
    $.ajax({
        type: "GET",
        url: "/apis/weixin/getStartOrderList?userId=4",
        dataType: "json",
        success: function(data){
            console.info(data);
            var result=data.data;
            for(var i=0;i<result.length;i++){
                //判断是正钓还是溜鱼
                if(result[i].orderType=="ZHENG"){
                    var beginTime = new Date(result[i].beginTime).format("yyyy-MM-dd hh");
                    var endTime = new Date(result[i].endTime).format("hh");
                    var app='<div class="order-con">'+
                        '<div class="title">'+
                        '<b class="num">'+result[i].orderNo+'</b>'+
                        '<i class="state">已开始</i>'+
                        '</div>'+
                        '<div class="list">'+
                        '<b><img src="../public/images/list/zheng.png" /></b>'+
                        '<i>'+
                        '<p>'+result[i].subject+'</p>'+
                        '<p class="num">您的钓位号码:'+result[i].seatNo+'</p>'+
                        '</i>'+
                        '</div>'+
                        '<div class="bot">'+
                        '<b class="price">¥'+result[i].totalFee /100+'</b>'+
                        '<i class="data">'+beginTime+':00'+'-'+endTime+':00'+'</i>'+
                        '</div>'+
                        '</div>';
                    $('#tab1 .order-content').append(app);

                }else{
                    var beginTime = new Date(result[i].beginTime).format("yyyy-MM-dd hh");
                    var endTime = new Date(result[i].endTime).format("hh");
                    var app='<div class="order-con">'+
                        '<div class="title">'+
                        '<b class="num">'+result[i].orderNo+'</b>'+
                        '<i class="state">已开始</i>'+
                        '</div>'+
                        '<div class="list">'+
                        '<b><img src="../public/images/list/fan.png" /></b>'+
                        '<i>'+
                        '<p>'+result[i].subject+'</p>'+
                        '<p class="num">'+result[i].configRemark+'</p>'+
                        '</i>'+
                        '</div>'+
                        '<div class="bot">'+
                        '<b class="price">¥'+result[i].totalFee /100+'</b>'+
                        '<i class="data">'+beginTime+':00'+'-'+endTime+':00'+'</i>'+
                        '</div>'+
                        '</div>'
                    $('#tab1 .order-content').append(app);
                }
            }


        }
    });
}

//未开始
function getUnStartOrderList() {
    $.ajax({
        type: "GET",
        url: "/apis/weixin/getUnStartOrderList?userId=4",
        dataType: "json",
        success: function(data){
            console.info(data);
            var result=data.data;
            for(var i=0;i<result.length;i++){
                //判断是正钓还是溜鱼
                if(result[i].orderType=="ZHENG"){
                    var beginTime = new Date(result[i].beginTime).format("yyyy-MM-dd hh");
                    var endTime = new Date(result[i].endTime).format("hh");
                    var app='<div class="order-con">'+
                        '<div class="title">'+
                        '<b class="num">'+result[i].orderNo+'</b>'+
                        '<i class="state" style="color: red">未开始</i>'+
                        '</div>'+
                        '<div class="list">'+
                        '<b><img src="../public/images/list/zheng.png" /></b>'+
                        '<i>'+
                        '<p>'+result[i].subject+'</p>'+
                        '<p class="num">您的钓位号码:'+result[i].seatNo+'</p>'+
                        '</i>'+
                        '</div>'+
                        '<div class="bot">'+
                        '<b class="price">¥'+result[i].totalFee /100+'</b>'+
                        '<i class="data">'+beginTime+':00'+'-'+endTime+':00'+'</i>'+
                        '</div>'+
                        '</div>';
                    $('#tab2 .order-content').append(app);

                }else{
                    var beginTime = new Date(result[i].beginTime).format("yyyy-MM-dd hh");
                    var endTime = new Date(result[i].endTime).format("hh");
                    var app='<div class="order-con">'+
                        '<div class="title">'+
                        '<b class="num">'+result[i].orderNo+'</b>'+
                        '<i class="state" style="color: red">未开始</i>'+
                        '</div>'+
                        '<div class="list">'+
                        '<b><img src="../public/images/list/fan.png" /></b>'+
                        '<i>'+
                        '<p>'+result[i].subject+'</p>'+
                        '<p class="num">'+result[i].configRemark+'</p>'+
                        '</i>'+
                        '</div>'+
                        '<div class="bot">'+
                        '<b class="price">¥'+result[i].totalFee /100+'</b>'+
                        '<i class="data">'+beginTime+':00'+'-'+endTime+':00'+'</i>'+
                        '</div>'+
                        '</div>'
                    $('#tab2 .order-content').append(app);
                }
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

