(function($) {
    $.fn.serializeJson = function() {
        var serializeObj = {};
        var array = this.serializeArray();
        var str = this.serialize();
        $(array).each(
            function() {
                if (serializeObj[this.name]) {
                    if ($.isArray(serializeObj[this.name])) {
                        serializeObj[this.name].push(this.value);
                    } else {
                        serializeObj[this.name] = [
                            serializeObj[this.name], this.value ];
                    }
                } else {
                    serializeObj[this.name] = this.value;
                }
            });
        return serializeObj;
    };
})(jQuery);
/**
 * 页面动作枚举
 */
var pageAction = {};

/**
 * 新增
 */
pageAction.add = "add";

/**
 * 编辑
 */
pageAction.update = "update";

/**
 * 查看
 */
pageAction.view = "view";

/**
 * 其他
 */
pageAction.other = "other";

/**
 * 删除
 */
pageAction.del = "del";

/**
 * 拓展日期格式�?
 */
Date.prototype.format = function(format) {
    var date = {
        "M+" : this.getMonth() + 1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth() + 3) / 3),
        "S+" : this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '')
            .substr(4 - RegExp.$1.length));
    }
    for ( var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k]
                : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}


/**
 * 把时间戳格式化成yyyy-MM-dd hh:mm:ss格式
 *
 * @param timestamp
 * @returns
 */
function formatTimestamp(timestamp, format) {
    if (timestamp == "" || timestamp == undefined)
        return "";
    var d = new Date(timestamp); // 根据时间戳生成的时间对象
    var formatStr = "";
    if (format != undefined) {
        formatStr = d.format(format);
    } else {
        formatStr = d.format('yyyy-MM-dd hh:mm:ss');
    }
    return formatStr;
}

function formatTimestamp2(timestamp, format) {
    if (timestamp == "" || timestamp == undefined)
        return "";
    var d = new Date(timestamp); // 根据时间戳生成的时间对象
    var formatStr = "";
    if (format != undefined) {
        formatStr = d.format(format);
    } else {
        formatStr = d.format('yyyy-MM-dd hh:mm');
        if("NaN-aN-aN aN:aN"==formatStr){
            return new Date(parseInt(timestamp)).format('yyyy-MM-dd hh:mm')
        }
    }
    return formatStr;
}

function formatTimestamp3(timestamp, format) {
    if (timestamp == "" || timestamp == undefined)
        return "";
    var d = new Date(timestamp); // 根据时间戳生成的时间对象
    var formatStr = "";
    if (format != undefined) {
        formatStr = d.format(format);
    } else {
        formatStr = d.format('yyyy-MM-dd');
        if("NaN-aN-aN"==formatStr){
            return new Date(parseInt(timestamp)).format('yyyy-MM-dd')
        }
    }
    return formatStr;
}
/**
 * 给日期增加天�?
 * @param date
 * @param dayNumber
 * @returns {Date}
 */
function addDay(date,dayNumber){
    date=date?date:new Date();
    var ms=dayNumber*(1000*60*60*24);
    return new Date(date.getTime()+ms);
}
function strToDate(str){
    var value=Date.parse(str);
    return new Date(value);
}

function dayInterval(d1,d2){
    var date1=new Date(d1.replace(/-/g, "/"));
    var date2=new Date(d2.replace(/-/g, "/"));
    var days = date2.getTime() - date1.getTime();
    var time = parseInt(days / (1000 * 60 * 60 * 24));
    return time;
}


/**
 * 拓展post方法，统�?拼接应用程序�?+url
 */
$.extend({
    postSimpleJsonAjax : function(url, jsonData, callFn) {
        $.ajax({
            url : url,
            type : "post",
            data : jsonData,
            dataType : "json",
            success : callFn
        });
    },
    postJsonAjax : function(url, jsonData, callFn) {
        var ajaxData = JSON.stringify(jsonData);
        $.ajax({
            url : url,
            type : "post",
            data : ajaxData,
            dataType : "json",
            contentType : "application/json",
            error : function(a,b,c) {//请求失败处理函数
                console.info(a);
                console.info(b);
                console.info(c);
            },
            success : callFn,
            complete:function(request,status){
                var responseJSON=request.responseJSON;
                if(responseJSON!=null&&responseJSON.status=='300'){
                    alert('系统超时');
                    if(top.location!=self.location){
                        top.location.href='${root}/login';
                    }else{
                        window.location.href='${root}/login';
                    }
                }
            }
        });
    },
    jsonToUrlParam:function(param, key){
        var paramStr="";
        if(param instanceof String||param instanceof Number||param instanceof Boolean){
            paramStr+="&"+key+"="+encodeURIComponent(param);
        }else{
            $.each(param,function(i){
                var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
                paramStr+='&'+$.jsonToUrlParam(this, k);
            });
        }
        return paramStr.substr(1);
    }
});

/**
 * 获取html的url参数
 *
 * @param name
 * @returns
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

$(document).keydown(function (e) {
    var doPrevent;
    if (e.keyCode == 8||e.keyCode==13) {   //注：8为Backspace键，13为Enter�?
        var d = e.srcElement || e.target;
        if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA'|| d.tagName.toUpperCase() == 'TD') {
            doPrevent = d.readOnly || d.disabled;
        }
        else
            doPrevent = true;
    }
    else
        doPrevent = false;

    if (doPrevent)
        e.preventDefault();
});


/**
 * 加载表单数据
 */
function loadFormData(json, formId) {

    if (!formId) {
        formId = getFirstForm();
    }
    $('#' + formId).form('load', json);
}






/**
 * 格式化金额，把分转为为元
 *
 * @param v
 * @param rec
 * @returns {Number}
 */
function formatAmt(v, rec) {
    if (v == undefined || isNaN(v)) {
        return 0;
    }
    return decimalRound(v / 100,2);
}
function formatAmt2(v, rec) {
    if (v == undefined || isNaN(v)) {
        return "0.00";
    }
    return decimalRound(v / 100,2);
}

/**
 * 格式化金额，把分转为为万�?
 *
 * @param v
 * @param rec
 * @returns {Number}
 */
function formatAmt3(v) {
    if (v == undefined || isNaN(v)) {
        return 0;
    }
    return decimalRound(v / 1000000,2);
}

/**
 * tips显示列的数据
 */
var tips = function(value, size) {

    if (value == undefined) {
        return "";
    }
    value = value + "";
    var val = value ? value : "";
    if (value && value.length > size) {
        val = value.substr(0, size) + "...";
    }
    return '<span class="easyui-tooltip" title="' + value + '">' + val
        + '<span/>';
}



function parseGridPager(flag){
    var win_h = $(window).height();
    var sear_h = $(".searForm").height();
    var len = 0;
    if (flag == 1) {
        len = 36;
    } else {
        len = 16;
    }
    var dat_tab_h = win_h - sear_h - len - 10 - 2;

    $(".datagrid .datagrid-wrap").css({
        "height" : dat_tab_h + "px"
    });
    $(".datagrid .datagrid-wrap .datagrid-view").css({
        "height" : dat_tab_h - 31 + "px"
    });
}



function getRequest() {
    var url = decodeURI(window.location.search);
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

function gridMachining(data, obj) {

    return;
    if (data.total == 0) {
        var body = obj.data().datagrid.dc.body2;
        body
            .find('table tbody')
            .html(
            '<tr><td width="'
            + body.width()
            + '"style="color:#FF0000;height: 36px; text-align: center;">未查询到相关数据</td></tr>');
    }
}

/**
 * js 小数四舍五入
 * @param num
 * @param v
 * @returns {Number}
 */
function decimalRound(num, v) {

    var vv = Math.pow(10, v);
    var f= Math.round(num * vv) / vv;
    var s = f.toString();
    var rs = s.indexOf('.');

    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + v) {
        s += '0';
    }
    return s;
}

/**
 * 转义html字符用于显示特殊字符
 * @param sHtml
 * @returns
 */
function html2Escape(sHtml) {
    return sHtml.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
}

/**
 * 获取字符长度，中文占两个字符
 * @param val
 * @returns {Number}
 */
function getByteLen(val) {
    var len = 0;
    for (var i = 0; i < val.length; i++) {
        var a = val.charAt(i);
        if (a.match(/[^\x00-\xff]/ig) != null)
        {
            len += 2;
        }
        else
        {
            len += 1;
        }
    }
    return len;
}

var colWidth=Number(0);
function resize(gridWith,colNumber){
    console.info("当前文档宽度�?"+$(window).width())
    var totalWidth=$(window).width()-gridWith;
    if(totalWidth>0){
        colWidth=Number(decimalRound(totalWidth/colNumber,1));
    }
}

var actionRootPath = "/risk-control-web";
/**
 * 获取action URL
 */
function getActionUrl(url) {
    return actionRootPath + url;
}

/**
 * 获取第一个表单id
 *
 * @returns
 */
function getFirstForm() {
    return $("form:first").attr("id");
}

/**
 * 显示物业选择�?
 */
function showMcComboGrid(domId, onChangeHandel, idFieldName, isShowNotUser) {
    if (idFieldName == undefined || idFieldName == null) {
        idFieldName = "id"
    }
    if (isShowNotUser == undefined || isShowNotUser == null) {
        isShowNotUser = true;
    }
    $('#' + domId).combogrid(
        {
            // pagination : true,
            panelWidth : 250,
            url : getActionUrl("/company/findList?isShowNotUser="
                + isShowNotUser),
            idField : idFieldName,
            textField : 'mcName',
            mode : 'remote',
            columns : [ [ {
                field : 'mcName',
                title : '公司名称',
                align : 'right',
                width : "30%",
                formatter : function(value, rec) {
                    return tips(value, 6);
                }
            }, {
                field : 'mcPresident',
                title : '法人姓名',
                align : 'right',
                width : "30%",
                formatter : function(value, rec) {
                    return tips(value, 6);
                }
            }, {
                field : 'mcPreMobile',
                title : '电话',
                align : 'right',
                width : "40%",
                formatter : function(value, rec) {
                    return tips(value, 6);
                }
            } ] ],
            onChange : function(newValue, oldValue) {

                if (onChangeHandel != undefined && onChangeHandel != null) {
                    var record = $('#' + domId).combogrid('grid').datagrid(
                        'getSelected');
                    onChangeHandel(record);
                }
            }
        });
}

/**
 * 弹出错误信息
 */
function alertError(msg) {
    alertInfo(msg, "error", function () {
        parent.$("#dlId" + getQueryString("dlId")).show();
    });
}

function isNullReturnVal(infoVal){
    var val="";
    if(infoVal !=null && infoVal !="" && infoVal !=undefined){
        val=infoVal;
    }
    return val;
}