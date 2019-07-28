<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>深圳市案件知识体系展示</title>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/jquery.js"></script>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/js.js"></script>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/echarts.min.js"></script>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/jquery.liMarquee.js"></script>
    <script type="text/javascript" src="${root}/resources/js/knowledgeBase/jquery.cxselect.min.js"></script>
    <script type="text/javascript" src="${root}/resources/js/util.js"></script>

    <link rel="stylesheet" href="${root}/resources/css/comon0.css">
    <style type="text/css">
        .textWrap{
            float: left;
            overflow:hidden;
            text-overflow:ellipsis;
            white-space:nowrap;
        }
    </style>
</head>
<body>
    <div style="background:#000d4a url(${root}/resources/img/knowledgeBase/bg.jpg) center top;">
        <%--<div class="loading">
            <div class="loadbox"> <img src="${root}/resources/img/knowledgeBase/loading.gif"> 页面加载中... </div>
        </div>--%>
        <div class="back"></div>
        <div class="head" onclick="location.href='retrievalPage'">
            <div class="weather"><span id="showTime"></span></div>
            <h1>深圳市案件知识体系展示</h1>

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
                                <div class="numtxt" id="sum">${sum} </div>
                            </div>
                            <div class="pulll_right zhibiao">
                                <div class="zb2 commonType" onclick="caseType(this,'civil')"><span>民事</span><div id="zb1"></div></div>
                                <div class="zb1 commonType" onclick="caseType(this,'criminal')"><span>刑事</span><div id="zb2"></div></div>
                                <div class="zb3 commonType" onclick="caseType(this,'administration')"><span>行政</span><div id="zb3"></div></div>
                            </div>
                        </div>
                    </div>
                    <div class="boxall">
                        <div class="alltitle" id="zy">Top 5争议焦点（${caseReaso}）</div>
                        <table class="table1" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <thead>
                            <tr>
                                <th scope="col">排名</th>
                                <th scope="col">内容</th>
                                <th scope="col">频次</th>
                            </tr>
                            </thead>
                            <tbody id="argue">
                            <%--<c:forEach var="argueVo" items="${argueVos}" varStatus="status">
                                <tr>
                                    <td><span>${status.count}</span></td>
                                    <td>${argueVo.argueContent}</td>
                                    <td>${argueVo.frequency}</td>
                                </tr>
                            </c:forEach>--%>
                            </tbody>
                        </table>
                    </div>
                </li>
                <li>
                    <div class="boxall" >
                        <div class="alltitle" id="ay" name="civil">Top 5案由 ( 民事 )</div>
                        <div class="navboxall" id="echart4"></div>

                    </div>
                    <div class="boxall">
                        <div class="alltitle" id="yjft">Top 5依据法条分布（${caseReaso}）</div>
                        <div class="navboxall">
                            <div class="wraptit">
                                <span>法律条文</span><span>频次</span>
                            </div>
                            <div class="wrap">
                                <%--<ul id="lawData">--%>
                                    <%--&lt;%&ndash;<c:forEach var="lawVo" items="${lawVos}">--%>
                                        <%--<li>--%>
                                            <%--<p>--%>
                                                <%--<span class="textWrap">${lawVo.law}</span>--%>
                                                <%--<span class="textWrap">${lawVo.frequency}</span>--%>
                                            <%--</p>--%>
                                        <%--</li>--%>
                                    <%--</c:forEach>&ndash;%&gt;--%>
                                <%--</ul>--%>
                            </div>
                        </div>
                    </div>

                </li>
            </ul>

        </div>
    </div>

    <script>
        $(function () {
            getArgueAndReasonAndLaw('${ayCode}')

            var djHeight = ($('body').height()-145)/2;
            $('.boxall').css('height',djHeight+'px');

            zb1('${civilNum}','${sum}');
            zb2('${criminalNum}','${sum}');
            zb3('${administrationNum}','${sum}');

            var caseReason=new Array();
            var data=new Array();
            if(${countList}){
                var list=JSON.parse('${countList}');
                console.info(list);
                for(var i=0;i<list.length;i++){
                    if(i<5){
                        caseReason.push(list[i].name);
                        var obj=new Object();
                        obj.ayCode=list[i].value;
                        obj.value=list[i].num;
                        data.push(obj);
                    }
                }
            }
            echarts_4(caseReason,data);
        });

        function echarts_4(caseReason,data) {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('echart4'));

            option = {
                title:{show:false},
                tooltip: {
                    show: false,
                    trigger: 'item',
                    backgroundColor: 'rgba(0,0,0,0.7)', // 背景
                    padding: [10, 10], //内边距
                },
                grid: {
                    // borderWidth: 0,
                    top: 5,
                    left:5,
                    right:75,
                    bottom: 5,
                    // textStyle: {
                    //     color: "#fff"
                    // }
                },
                yAxis: [{
                    type: 'category',
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        show: false,
                        lineStyle: {
                            color: '#363e83',
                        }
                    },
                    axisLabel: {
                        show:false,
                        inside: false,
                        textStyle: {
                            color:'red'|| '#bac0c0',
                            fontWeight: 'normal',
                            fontSize: '12',
                        },
                        // formatter:function(val){
                        //     return val.split("").join("\n")
                        // },
                    },
                    data: caseReason,
                }, {
                    type: 'category',
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show:true,
                        inside: false,
                        textStyle: {
                            color:'#fff',
                            fontWeight: 'normal',
                            fontSize: '16',
                        },
                        formatter:function(val){
                            return  '${val}';
                        },
                    },
                    splitArea: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                    data: [500,400,300,200,100],

                }],
                xAxis: {
                    type: 'value',
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        show: false,
                        lineStyle: {
                            color: '#2f3640',
                        }
                    },
                    splitLine: {
                        show: false,
                        lineStyle: {
                            color: '#2f3640 ',
                        }
                    },
                    axisLabel: {
                        show: false,
                        textStyle: {
                            color: '#bac0c0',
                            fontWeight: 'normal',
                            fontSize: '12',
                        },
                        formatter: '{value}个',
                    },
                },
                series: [{
                    name: '',
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            show: true,
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: '#00c0e9'
                            }, {
                                offset: 1,
                                color: '#3b73cf'
                            }]),
                            barBorderRadius: 50,
                            borderWidth: 0,
                        },
                        emphasis: {
                            shadowBlur: 15,
                            shadowColor: 'rgba(105,123, 214, 0.7)'
                        }
                    },
                    zlevel: 2,
                    barWidth: '30%',
                    data: data,
                    label: {
                        normal: {
                            color:'#fff',
                            show: true,
                            // position: 'center',
                            position: [0, '-110%'],
                            textStyle: {
                                fontSize: 16,
                                fontWeight: 'bold',
                                fontFamily:'PingFangSC'
                            },
                            formatter:function(a,b){

                                //console.log(a,b);
                                return a.name;
                            }
                        }
                    },
                },
                    {
                        name: '',
                        type: 'bar',
                        yAxisIndex: 1,
                        zlevel: 1,
                        itemStyle: {
                            normal: {
                                color: '#121847', //柱子背景色
                                borderWidth: 0,
                                shadowBlur: {
                                    shadowColor: 'rgba(255,255,255,0.31)',
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowOffsetY: 2,
                                },
                            }
                        },
                        label: {
                            normal: {
                                color:'#fff',
                                show: false,
                                // position: 'center',
                                position: [0, '-100%'],
                                textStyle: {
                                    fontSize: 16,
                                    fontWeight: 'bold'
                                },
                                formatter:1212|| '{b}\n{c}%'
                            }
                        },
                        barWidth: '20%',
                        data: [30, 30, 30, 30, 30]
                    }
                ]
            }


            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
            myChart.off('click');
            //案由点击事件
            myChart.on('click', function (params) {
                $('#zy').text('Top 5争议焦点'+'( '+params.name+' )');
                $('#yjft').text('Top 5依据法条分布'+'( '+params.name+' )');
                getArgueAndReasonAndLaw(params.data.ayCode);
            });
        }
        //案件类型饼状图点击
        function caseType(e,caseType) {
            var wenText = $(e).find('span').text();
            $('#ay').text('Top 5案由 ( '+wenText+' )');
            if(wenText){
                if(wenText.indexOf("民事")>-1){
                    wenText="civil";
                }else if(wenText.indexOf("刑事")>-1){
                    wenText="criminal";
                }else if(wenText.indexOf("行政")>-1){
                    wenText="administration";
                }
            }
            getAy(wenText);
        }


        function selectedOption(){
            var overall=$("#overall  option:selected").val();
            var caseType=$("#ay").attr("name");
            getCaseCountData(overall,caseType);
        }

        function getCaseCountData(overall){
            $.ajax({
                url: "${root}/knowledge/findCaseCountByDate?overall="+overall,
                type : "post",
                dataType : "json",
                contentType : "application/json",
                success: function (result) {
                    zb1(0,0);
                    zb2(0,0);
                    zb3(0,0);
                    $("#sum").html(0);
                    if(result!=null&&result!=""){
                        zb1(result.civil,result.sum);
                        zb2(result.criminal,result.sum);
                        zb3(result.administration,result.sum);
                        $("#sum").html(result.sum);
                    }
                }
            });
        }
        function getAy(caseType){
            $.ajax({
                url: "${root}/knowledge/findCaseCaseReasonInfoByCaseType?caseType="+caseType,
                type : "post",
                dataType : "json",
                contentType : "application/json",
                success: function (result) {
                    echarts_4("","");
                    if(result.success&&result.subject){
                        var names=new Array();
                        var data=new Array();
                        $.each(result.subject,function(index,value){
                            if(index<5){
                                names.push(value.name);
                                var obj=new Object();
                                obj.ayCode=value.value;
                                obj.value=value.num;
                                data.push(obj);
                            }
                        });
                        echarts_4(names,data);
                    }
                }
            });
        }


        function getArgueAndReasonAndLaw(caseReason){
            $.ajax({
                url: "${root}/knowledge/ggetArgueAndReasonAndLawByAy?aydm="+caseReason,
                type : "post",
                dataType : "json",
                contentType : "application/json",
                success: function (result) {
                    if(result){
                        var argueVos=result.argueVos;
                        if(argueVos){
                            var htmlStr = '';
                            $("#argue").html("");
                            var i=1;
                            $.each(argueVos,function(index,value){
                                htmlStr += '<tr>';
                                htmlStr += '<td><span>'+i+'</span></td>';
                                htmlStr += '<td>'+isNullReturnVal(value.argueContent)+'</td>';
                                htmlStr += '<td>'+isNullReturnVal(value.frequency)+'</td>';
                                htmlStr += '</tr>';
                                i++;
                            });
                            $("#argue").html(htmlStr);
                        }
                        var lawVos=result.lawVos;
                        if(lawVos){
                            var htmlStr = '<ul id="lawData">';
                            $.each(lawVos,function(index,value){
                                htmlStr += '<li title="'+value.law+'"><p>';
                                htmlStr += '<span class="textWrap">'+isNullReturnVal(value.law)+'</span>';
                                htmlStr += '<span class="textWrap">'+isNullReturnVal(value.frequency)+'</span>';
                                htmlStr += '</p></li>';
                            });
                            htmlStr+='</ul>';
//                            $("#lawData").html("");
//                            $(".str_move.str_move_clone ul").html("");
                            $(".wrap").html(htmlStr);
                            $('.wrap').liMarquee({
                                direction: 'up',
                                runshort: false,
                                scrollamount: 20
                            });
//                         $(".str_move.str_move_clone ul").html(htmlStr);
                        }
                    }
                }
            });
        }

    </script>


</body>
</html>
