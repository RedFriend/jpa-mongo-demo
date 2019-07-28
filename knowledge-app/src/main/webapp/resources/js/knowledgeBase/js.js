

function zb1(civilNum,sum) {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('zb1'));
    option = {
        series: [{

            type: 'pie',
            radius: ['70%', '90%'],
            center:['50%','40%'],
            color:'#49bcf7',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: civilNum,   //圆环图中间显示的数字
                name: '总案件数',
                label: {
                    normal: {
                        formatter: civilNum +'',    //圆环图
                        textStyle: {
                            fontSize: 20,
                            color:'#fff',
                        }
                    }
                }
            }, {
                value: sum,
                name: '民事案件数',
                label: {
                    normal: {
                        formatter : function (params){
                            return '占比'+Math.round( civilNum/sum*100)+ '%'   //圆环图中间显示的占比
                        },
                        textStyle: {
                            color: '#f9f9f9',
                            fontSize: 13
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
    myChart.setOption(option);
    window.addEventListener("resize",function(){
        myChart.resize();
    });
}
function zb2(criminalNum,sum) {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('zb2'));
    option = {

//animation: false,
        series: [{
            type: 'pie',
            radius: ['70%', '90%'],
            center:['50%','40%'],
            color:'#cdba00',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: criminalNum,
                name: '刑事案件数',
                label: {
                    normal: {
                        formatter: criminalNum +'',
                        textStyle: {
                            fontSize: 20,
                            color:'#fff',
                        }
                    }
                }
            }, {
                value: sum,
                name: '总案件数',
                label: {
                    normal: {
                        formatter : function (params){
                            return '占比'+Math.round( criminalNum/sum*100)+ '%'
                        },
                        textStyle: {
                            color: '#f9f9f9',
                            fontSize: 13
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
    myChart.setOption(option);
    window.addEventListener("resize",function(){
        myChart.resize();
    });
}
function zb3(administrationNum,sum) {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('zb3'));
    option = {
        series: [{

            type: 'pie',
            radius: ['70%', '90%'],
            center:['50%','40%'],
            color:'#62c98d',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: administrationNum,
                name: '行政案件数',
                label: {
                    normal: {
                        formatter: administrationNum +'',
                        textStyle: {
                            fontSize: 20,
                            color:'#fff',
                        }
                    }
                }
            }, {
                value: sum,
                name: '总案件数',
                label: {
                    normal: {
                        formatter : function (params){
                            return '占比'+Math.round( administrationNum/sum*100)+ '%'
                        },
                        textStyle: {
                            color: '#f9f9f9',
                            fontSize: 13
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
    myChart.setOption(option);
    window.addEventListener("resize",function(){
        myChart.resize();
    });
}

function zb4(implementNum,sum) {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('zb4'));
    option = {
        series: [{

            type: 'pie',
            radius: ['70%', '90%'],
            center:['50%','40%'],
            color:'#f78c44',
            label: {
                normal: {
                    position: 'center'
                }
            },
            data: [{
                value: implementNum,
                name: '执行案件数',
                label: {
                    normal: {
                        formatter: implementNum +'',
                        textStyle: {
                            fontSize: 20,
                            color:'#fff',
                        }
                    }
                }
            }, {
                value: sum,
                name: '总案件数',
                label: {
                    normal: {
                        formatter : function (params){
                            return '占比'+Math.round( implementNum/sum*100)+ '%'
                        },
                        textStyle: {
                            color: '#f9f9f9',
                            fontSize: 13
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        color: 'rgba(255,255,255,.2)'
                    },
                    emphasis: {
                        color: '#fff'
                    }
                },
            }]
        }]
    };
    myChart.setOption(option);
    window.addEventListener("resize",function(){
        myChart.resize();
    });
}