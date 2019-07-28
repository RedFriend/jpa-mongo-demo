<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>知识库检索与展示</title>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/jquery.js"></script>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/echarts.min.js"></script>
    <script type="text/javascript" src="${root}/resources/js/util.js"></script>

    <link rel="stylesheet" href="${root}/resources/css/comon0.css">
    <style>
        .addColor{
            background-size: 100% 100%;
            background-color: #2cacff;
            color: #27323A;
        }
    </style>
</head>
<body>
<div style="background:#000d4a url(${root}/resources/img/knowledgeBase/bg.jpg) center top;">
    <%--<div class="loading">
        <div class="loadbox"> <img src="${root}/resources/img/knowledgeBase/loading.gif"> 页面加载中... </div>
    </div>--%>
    <div class="back"></div>
    <div class="head" onclick="location.href='caseDimensionPage'">
        <div class="weather"><span id="showTime"></span></div>
        <h1>知识库检索与展示</h1>

    </div>
    <script>
        var t = null;
        t = setTimeout(time, 1000);/*開始运行*/
        function time() {
            clearTimeout(t);/*清除定时器*/
            dt = new Date();
            var y = dt.getFullYear();
            var mt = dt.getMonth() + 1;
            var day = dt.getDate();
            var h = dt.getHours();
            var m = dt.getMinutes();
            var s = dt.getSeconds();
            document.getElementById("showTime").innerHTML = y + "年" + mt + "月" + day + "日" + h + "时" + m + "分" + s + "秒";
            t = setTimeout(time, 1000);
        }

    </script>
    <div class="mainbox">
        <ul class="clearfix">
            <li>
                <div class="boxall">
                    <div class="clearfix navboxall" style="height: 100%">
                        <div class="pulll_left num">
                            <div class="numbt">总体情况
                                <select name="" class="weidu" onchange="selectedOption()" id="overall">
                                    <option value="1">本周</option>
                                    <option value="2">本月</option>
                                    <option value="3">本季度</option>
                                    <option value="4">今年</option>
				                    <option value="5" selected="selected">全部</option>
                                </select>
                            </div>
                            <div class="numtxt" id="sum"> </div>
                        </div>
                        <div class="pulll_right zhibiao">
                            <div class="zb1 commonType" onclick="caseType(this)"><button type="button" class="x-btn xbtn1 addColor" onclick="clickBtn(this)">争议焦点</button><span class="yiji">一级维度</span><div id="zb1"></div></div>
                            <div class="zb2 commonType" onclick="caseType(this)"><button type="button" class="x-btn xbtn2" onclick="clickBtn(this)">案件情节</button><span class="erji">二级维度</span><div id="zb2"></div></div>
                            <div class="zb3 commonType" onclick="caseType(this)"><button type="button" class="x-btn xbtn3" onclick="clickBtn(this)">文书模板</button><span class="sanji">三级维度</span><div id="zb3"></div></div>
                        </div>
                    </div>
                </div>
                <!--文书模板部分-->
                <div class="boxall template" style="display: none">
                    <div class="alltitle">模板列表</div>
                    <table class="table1" width="100%" border="0" cellspacing="0" cellpadding="0">
                        <thead>
                        <tr>
                            <th scope="col">排名</th>
                            <th scope="col">文书名称</th>
                            <th scope="col">使用次数</th>
                            <th scope="col">制作人</th>
                        </tr>
                        </thead>
                        <tbody id="templateData">

                        </tbody>
                    </table>
                </div>
            </li>
            <!--文书模板部分-->
            <li class="template backOpacity">
                <iframe class="iframepdf" src="" frameborder="0" style="width:100%;height:99%;border:none"></iframe>
            </li>
            <!--非模板统计-->
            <li class="noTemplate">
                <div class="boxall" >
                    <div class="alltitle" id="ay">案由维度统计</div>
                    <div class="navboxall" id="echart4"></div>    <!--案由维度统计-->
                </div>
            </li>
            <li style="width:100%" class="noTemplate">
                <div class="boxall">
                    <div class="alltitle">维度信息<span class="weiduMsg">${titleInfo}</span></div>
                    <div class="table1 ulList type1">
                        <ul id="dimension1">
                            <c:forEach var="dimensionVos" items="${dimensionVos}" >
                                <li vl="${dimensionVos.id}">${dimensionVos.dimension}</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="table1 ulList type2">
                        <ul id="dimension2">
                            <c:forEach var="dimensionVosTwo" items="${dimensionVosTwo}" >
                                <li vl="${dimensionVosTwo.id}">${dimensionVosTwo.dimension}</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="table1 ulList type3">
                        <ul id="dimension3">
                            <c:forEach var="dimensionVosThree" items="${dimensionVosThree}" >
                                <li vl="${dimensionVosThree.id}">${dimensionVosThree.dimension}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>


            </li>
        </ul>

    </div>
</div>
<script type="text/javascript" src="${root}/resources/js/knowledgeBase/js.js"></script>
<script type="text/javascript" src="${root}/resources/js/knowledgeBase/jquery.liMarquee.js"></script>
<script type="text/javascript" src="${root}/resources/js/knowledgeBase/jquery.cxselect.min.js"></script>
<script>

    $(function () {
        var djHeight = ($('body').height()-145)/2;
        $('.boxall').css('height',djHeight+'px');
        var rightHeight = $('body').height()-130;
        $('.backOpacity').css('height',rightHeight+'px');

        var dimensionOne=0;
        var dimensionTwo=0;
        var dimensionThree=0;
        var caseReasons=new Array();
        var data1=new Array();
        var data2=new Array();
        var data3=new Array();

        if(${countlist}){
            var countlist=JSON.parse('${countlist}');
            for(var i=0;i<countlist.length;i++){
                if(countlist[i].name.indexOf("一级")>-1){
                    dimensionOne=countlist[i].num;
                }else if(countlist[i].name.indexOf("二级")>-1){
                    dimensionTwo=countlist[i].num;
                }else if(countlist[i].name.indexOf("三级")>-1){
                    dimensionThree=countlist[i].num;
                }
            }
            var sum=dimensionOne+dimensionTwo+dimensionThree;
        }
        if(${ayDimensionVos}){
            var ayDimensionVos=JSON.parse('${ayDimensionVos}');
            for(var j=0;j<ayDimensionVos.length;j++){
                caseReasons.push(ayDimensionVos[j].ay);
                var one=new Object();
                var two=new Object();
                var three=new Object();
                one.name=ayDimensionVos[j].ayCode;
                one.value=ayDimensionVos[j].num1;
                two.name=ayDimensionVos[j].ayCode;
                two.value=ayDimensionVos[j].num2;
                three.name=ayDimensionVos[j].ayCode;
                three.value=ayDimensionVos[j].num3;
                data1.push(one);
                data2.push(two);
                data3.push(three);
            }
        }

        zb1(dimensionOne,sum);
        zb2(dimensionTwo,sum);
        zb3(dimensionThree,sum);
        $("#sum").html(sum);
        echarts_4(caseReasons,data1,data2,data3);

        //案件维度点击
        $(".type1").delegate("li","click",function(){
            $('.type1 li').removeClass('activeClass');
            $(this).addClass('activeClass');
            $('.weiduMsg').html($('.type1').find('.activeClass').text());

            var id=$(this).attr("vl");
            getDimensionData(id,null,2);
        });

        $('.type2').delegate("li","click",function(){
            $('.type2 li').removeClass('activeClass');
            $(this).addClass('activeClass');
            var ds = $('.type1').find('.activeClass').text();
            var ds1 = $('.type2').find('.activeClass').text();
            $('.weiduMsg').html(ds+'>'+ds1);

            var id=$(this).attr("vl");
            getDimensionData(id,null,3);
        });

    });

    function echarts_4(caseReasons,data1,data2,data3) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart4'));
        var juanzong = {
            normal: {
                show: true,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: '#FFFF00'
                }, {
                    offset: 1,
                    color: '#00E6FF'
                }]),
                barBorderRadius: 0,
                borderWidth: 0
            }
        };
        var yingyan = {
            normal: {
                show: true,
                color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [{
                    offset: 0,
                    color: '#00C6FF'
                }, {
                    offset: 1,
                    color: '#0072FF'
                }]),
                barBorderRadius: 0,
                borderWidth: 0,
            }
        };
        var duoyuan = {
            normal: {
                show: true,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: '#cd6dff'
                }, {
                    offset: 1,
                    color: '#26c0c0'
                }]),
                barBorderRadius: [5,5,0,0],
                borderWidth: 0
            }
        };
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            // backgroundColor:'#eef9f0',
            legend: {
                data: ["一级维度", "二级维度",'三级维度'],
                top:0,
                right:40,
                textStyle:{
                    color:'#fff'
                }
            },
            grid: {
                left: '3%',
                right: '5%',
                bottom: 20,
                top:30,
                containLabel: true
            },
            xAxis: [{
                data: caseReasons,
                splitLine: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: '#fff', // 颜色
                    }
                },
                axisLabel: {
                    interval: 0,
                    rotate: 25,
                    textStyle: {
                        color: '#fff'
                    },
                }
            }],
            yAxis: [{
                type: 'value',
                name: '',
                axisLine: {
                    lineStyle: {
                        color: '#fff', // 颜色
                    }
                },
                axisLabel: {
                    textStyle: {
                        color: '#fff'
                    },
                }
            }],
            series: [{
                type: 'bar',
                barWidth: "30%",
                name: '一级维度',
                stack:'案件维度',
                itemStyle: juanzong,
                label: {
                    normal: {
                        show:true,
                        position:'inside',
                        textStyle: {
                            color: '#000'
                        }
                    }
                },
                data:data1
            }, {
                type: 'bar',
                barWidth: "30%",
                name: '二级维度',
                stack:'案件维度',
                itemStyle: yingyan,
                label: {
                    normal: {
                        show:true,
                        position:'inside',
                        textStyle: {
                            color: '#fff'
                        }
                    }
                },
                data: data2
            }, {
                type: 'bar',
                barWidth: "30%",
                name: '三级维度',
                itemStyle: duoyuan,
                stack:'案件维度',
                label: {
                    normal: {
                        show:true,
                        position:'inside',
                        textStyle: {
                            color: '#fff'
                        }
                    }
                },
                data: data3
            }]
        };


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        window.addEventListener("resize",function(){
            myChart.resize();
        });
        //案由点击事件
        /*myChart.on('click', function (params) {//点击柱子时触发
            console.log(params);
            getDimensionData(null,params.data.name,1);
        });*/
        myChart.off('click');
        myChart.getZr().on('click', function (params) {   //点击柱子周边时也可以触发
            var pointInPixel= [params.offsetX, params.offsetY];
            if (myChart.containPixel('grid',pointInPixel)) {
                var xIndex=myChart.convertFromPixel({seriesIndex:0},[params.offsetX, params.offsetY])[0];
                var ds = option.series[0].data[xIndex];//myChart.getOption().series[0].data[xIndex]
                getDimensionData(null,ds.name,1);
            }
        })
    }

    //案件类型饼状图点击
    function caseType(e) {
        var wenText = $(e).find('span').text();
        // $('#ay').text('Top 5案由 ( '+wenText+' )');
        if(wenText){
            if(wenText.indexOf("民事")>-1){
                getTemplateData(1);
            }else if(wenText.indexOf("刑事")>-1){
                getTemplateData(2);
            }else if(wenText.indexOf("行政")>-1){
                getTemplateData(3);
            }
        }
    }

    function selectedOption(){
        var type="";
        $("button").each(function(){
            if($(this).hasClass('addColor')){
                var c=$(this).text();
                if(c.indexOf("争议焦点")>-1){
                    type="1";
                }else if(c.indexOf("案件情节")>-1){
                    type="2";
                }else if(c.indexOf("文书模板")>-1){
                    type="3";
                }
            }
        });
        getDimensionCountData(type);
    }


    //按钮点击
    function clickBtn(e){;

        $("button").each(function(){
            if($(this).hasClass("addColor")){
                $(this).removeClass("addColor");
            }
        });
        $(e).addClass("addColor");

        var indexText = $(e).text();
        if(indexText=='争议焦点' || indexText=='案件情节'){
            $('.yiji').text('一级维度');
            $('.erji').text('二级维度');
            $('.sanji').text('三级维度');
            $('.noTemplate').show();
            $('.template').hide();
            if(indexText=='争议焦点'){
                getDimensionCountData(1);
            }else{
                getDimensionCountData(2);
            }

        }else if(indexText=='文书模板'){
            $('.yiji').text('民事');
            $('.erji').text('刑事');
            $('.sanji').text('行政');
            $('.noTemplate').hide();
            $('.template').show();
            getCaseCountData();
        }
    }

    /**
     *按维度统计
     * @param indexText
     */
    function getDimensionCountData(indexText){
        var overall=$("#overall  option:selected").text();
        $.ajax({
            url: "${root}/knowledge/getDimensionDataByTypeAndDate?overall="+overall+"&type="+indexText,
            type : "post",
            dataType : "json",
            contentType : "application/json",
            success: function (result) {
                zb1(0,0);
                zb2(0,0);
                zb3(0,0);
                $("#sum").html(0);
                if(result.subject!=null&&result.subject!=""){
                    var civil="";
                    var criminal="";
                    var administration="";
                    var sum="";
                    $.each(result.subject,function(i,v){
                        if(v.name.indexOf("一级")>-1){
                            civil= v.num;
                        }else if(v.name.indexOf("二级")>-1){
                            criminal= v.num;
                        }else if(v.name.indexOf("三级")>-1){
                            administration= v.num;
                        }
                    });
                    sum=civil+criminal+administration;
                    zb1(civil,sum);
                    zb2(criminal,sum);
                    zb3(administration,sum);
                    $("#sum").html(sum);
                    ayDimension();
                }
            }
        });
    }

    function ayDimension(){
        $.ajax({
            url: "${root}/knowledge/getAyDimensionCount",
            type : "post",
            dataType : "json",
            contentType : "application/json",
            success: function (result) {
                echarts_4("","","","");
                if(result.subject){
                    var caseReason=new Array();
                    var data1=new Array();
                    var data2=new Array();
                    var data3=new Array();
                    $.each(result.subject,function(i,v){
                        caseReason.push(v.ay);
                        var one=new Object();
                        var two=new Object();
                        var three=new Object();
                        one.name=v.ayCode;
                        one.value=v.num1;
                        two.name=v.ayCode;
                        two.value=v.num2;
                        three.name=v.ayCode;
                        three.value=v.num3;
                        data1.push(one);
                        data2.push(two);
                        data3.push(three);
                    });
                    echarts_4(caseReason,data1,data2,data3);
                }
            }
        });
    }

    /**
    *按案件类型统计
     */
    function getCaseCountData(){
        $.ajax({
            url: "${root}/knowledge/getCaseCountData",
            type : "post",
            dataType : "json",
            contentType : "application/json",
            success: function (result) {
                zb1(0,0);
                zb2(0,0);
                zb3(0,0);
                $("#sum").html(0);
                if(result.subject!=null&&result.subject!=""){
                    var civil="";
                    var criminal="";
                    var administration="";
                    $.each(result.subject,function(i,v){
                        if(v.name.indexOf("民事")>-1){
                            civil= v.num;
                        }else if(v.name.indexOf("刑事")>-1){
                            criminal= v.num;
                        }else if(v.name.indexOf("行政")>-1){
                            administration= v.num;
                        }
                    });
                    var sum=civil+criminal+administration;
                    zb1(civil,sum);
                    zb2(criminal,sum);
                    zb3(administration,sum);
                    $("#sum").html(sum);
                    getTemplateData(1);//民事
                }
            }
        });
    }

    function getTemplateData(type){
        $.ajax({
            url: "${root}/knowledge/getTemplateData?type="+type,
            type : "post",
            dataType : "json",
            contentType : "application/json",
            success: function (result) {
                if(result){
                    $("#templateData").html("");
                    var htmlStr = '';
                    $.each(result,function(i,v){
                        if(i<5){
                            htmlStr += '<tr>';
                            htmlStr += '<td><span>'+(++i)+'</span></td>';
                            htmlStr += '<td><a href="javaScript:void(0)" onclick=documentData("'+ v.url+'") >'+isNullReturnVal(v.name)+'</a></td>';
                            htmlStr += '<td>'+isNullReturnVal(v.num)+'</td>';
                            htmlStr += '<td>'+isNullReturnVal(v.maker)+'</td>';
                            htmlStr += '</tr>';
                        }
                    });
                    $("#templateData").html(htmlStr);
                }
            }
        });
    }

    function documentData(url){
        $('.iframepdf').attr('src',url);
    }


    /**
    *维度明细（列表）
    * @param dimensionId
    * @param caseReason
    * @param level
     */
    function getDimensionData(dimensionId,caseReason,level){
        var $ul="";
        if(level==1){
            $ul=$("#dimension1");
        }else if(level==2){
            $ul=$("#dimension2");
        }else if(level==3){
            $ul=$("#dimension3");
        }
        $.ajax({
            url: "${root}/knowledge/getDimensionData?caseReason="+caseReason+"&dimensionId="+dimensionId+"&level="+level,
            type : "post",
            dataType : "json",
            contentType : "application/json",
            success: function (result) {
                if(result.subject){
                    $ul.html("");
                    var htmlStr = '';
                    $.each(result.subject,function(i,v){
                        htmlStr += '<li vl="'+v.id+'"> '+ isNullReturnVal(v.dimension)+'</li>';
                    });
                    $ul.html(htmlStr);
                    if(level==1){
                        $("#dimension2").html("");
                        $("#dimension3").html("");
                        $(".weiduMsg").html("");
                    }
                }
            }
        });
    }

</script>


</body>
</html>
