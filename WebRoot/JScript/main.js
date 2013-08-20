// JavaScript Document
// 首页（Common/Main.htm 脚本）
$(function(){
	//window.onbeforeunload = function() { 
//		return "弹出提示信息:是否确认离开本系统！";
//	}
/*	window.onunload = function(){	//页面关闭后要做的事
		$.ajax({
			type: "post",
			url: "/jlyw/UserServlet.do?method=5&time=" + new Date().getTime(),
			dataType: "json",	//服务器返回数据的预期类型
			beforeSend: function(XMLHttpRequest){
				//ShowLoading();
			},
			success: function(data, textStatus){
			},
			complete: function(XMLHttpRequest, textStatus){
				//HideLoading();
			},
			error: function(){
				//请求出错处理
			}
		});
	}*/

	showDate();
	doBottWin();	//关闭下边区域
	checkMsg('1');
	setInterval("checkMsg('0')", 120000);	//每隔2分钟发送一个心跳包
	setInterval('showDate()', 1000);
	$('#loading').hide();
//	playSound();
});

function checkMsg(isLogin) {
	$.ajax({
			type: "post",
			url: "/jlyw/HeartBeatServlet.do",
			data: {"_time":new Date().getTime(), "IsLogin":isLogin},
			dataType: "json",	//服务器返回数据的预期类型
			beforeSend: function(XMLHttpRequest){
				//ShowLoading();
			},
			success: function(data, textStatus){
				if(data.IsOnline){
					if(data.IsNewMsg){
						$.messager.show({
							title:'您有新的消息！',
							width:300,
							height:150,
							timeout:10000,	//10秒钟
							msg:data.NewMsg
						});
						playSound();
					}
					
				}else{
					window.location.href = "/jlyw/index.jsp";
				}
			},
			complete: function(XMLHttpRequest, textStatus){
				//HideLoading();
			},
			error: function(){
				//请求出错处理
			}
	});
}

function playSound() {
	//alert($("#snd").attr("src"));
	$("#snd").attr("src", "/jlyw/sound/alert.wav");
}

function showDate()
{
	var dt=new Date();
	var day;
	switch(dt.getDay())
	{
	case 0:
	day="日";
	break;
	case 1:
	day="一";
	break;
	case 2:
	day="二";
	break;
	case 3:
	day="三";
	break;
	case 4:
	day="四";
	break;
	case 5:
	day="五";
	break;
	case 6:
	day="六";
	break;
	}
	$("#myDate").text("星期"+day+" "+dt.getFullYear()+"-"+(dt.getMonth() + 1)+"-"+dt.getDate()+" "+dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds());
}

//安全退出系统
function doLogout(){
	var result = confirm("弹出提示信息:是否确认退出本系统？");
	if(result == false){
		return false;
	}
	$.ajax({
		type: "post",
		url: "/jlyw/UserServlet.do?method=5&time=" + new Date().getTime(),
		dataType: "json",	//服务器返回数据的预期类型
		beforeSend: function(XMLHttpRequest){
			//ShowLoading();
		},
		success: function(data, textStatus){
			window.location.href="/jlyw/";
		},
		complete: function(XMLHttpRequest, textStatus){
			//HideLoading();
		},
		error: function(){
			//请求出错处理
		}
	});
}

//打开消息框中的连接
function doOpenUrlFromMessager(href){
	$("#MainFrame",parent.document.body).attr("src",href);
}