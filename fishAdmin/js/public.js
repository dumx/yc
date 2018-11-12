/*
 *
 *   鱼池管理端框架全局公共变量
 *
*/

//根地址
var baseUrl = window.location.origin+"/admin";

//tokenKey值
var tokenKey = "Authentication";
//图片 url 前缀
var url = "http://localhost:8888/static";

//时间戳转日期
function fmtDate(obj) {
    if (!obj) {
        return "";
    }
    var date = new Date(obj);
    var y = 1900 + date.getYear();
    var M = "0" + (date.getMonth() + 1);
    var d = "0" + date.getDate();

    var h = "0" + date.getHours();
    var m = "0" + date.getMinutes();
    var s = "0" + date.getSeconds();
    return y + "-" + M.substring(M.length - 2, M.length) + "-" + d.substring(d.length - 2, d.length) + " " + h.substring(h.length - 2, h.length) + ":" + m.substring(m.length - 2, m.length) + ":" + s.substring(s.length - 2, s.length);
}

function formatLocalDateTime(obj) {
    return obj ? obj.year + "-"
        + (obj.monthValue + 100 + "").substring(1) + "-"
        + (obj.dayOfMonth + 100 + "").substring(1) + " "
        + (obj.hour + 100 + "").substring(1) + ":"
        + (obj.minute + 100 + "").substring(1) + ":"
        + (obj.second + 100 + "").substring(1) : "";
}

function logout(){
    localStorage.removeItem("userInfo");
    location.href="login.html";
}

function reLogin(){
    parent.layer.msg("您的身份信息已过期,请重新登录");
    console.info(baseUrl);
    top.location.href = baseUrl + "/login.html";
}

function showMsgBox(type, msg) {
    Command: toastr[type](msg)
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
}