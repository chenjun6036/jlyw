// JavaScript Document
// ��ҳ��Common/Main.htm �ű���
$(function(){
	//window.onbeforeunload = function() { 
//		return "������ʾ��Ϣ:�Ƿ�ȷ���뿪��ϵͳ��";
//	}
/*	window.onunload = function(){	//ҳ��رպ�Ҫ������
		$.ajax({
			type: "post",
			url: "/jlyw/UserServlet.do?method=5&time=" + new Date().getTime(),
			dataType: "json",	//�������������ݵ�Ԥ������
			beforeSend: function(XMLHttpRequest){
				//ShowLoading();
			},
			success: function(data, textStatus){
			},
			complete: function(XMLHttpRequest, textStatus){
				//HideLoading();
			},
			error: function(){
				//���������
			}
		});
	}*/

	showDate();
	doBottWin();	//�ر��±�����
	checkMsg('1');
	setInterval("checkMsg('0')", 120000);	//ÿ��2���ӷ���һ��������
	setInterval('showDate()', 1000);
	$('#loading').hide();
//	playSound();
});

function checkMsg(isLogin) {
	$.ajax({
			type: "post",
			url: "/jlyw/HeartBeatServlet.do",
			data: {"_time":new Date().getTime(), "IsLogin":isLogin},
			dataType: "json",	//�������������ݵ�Ԥ������
			beforeSend: function(XMLHttpRequest){
				//ShowLoading();
			},
			success: function(data, textStatus){
				if(data.IsOnline){
					if(data.IsNewMsg){
						$.messager.show({
							title:'�����µ���Ϣ��',
							width:300,
							height:150,
							timeout:10000,	//10����
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
				//���������
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
	day="��";
	break;
	case 1:
	day="һ";
	break;
	case 2:
	day="��";
	break;
	case 3:
	day="��";
	break;
	case 4:
	day="��";
	break;
	case 5:
	day="��";
	break;
	case 6:
	day="��";
	break;
	}
	$("#myDate").text("����"+day+" "+dt.getFullYear()+"-"+(dt.getMonth() + 1)+"-"+dt.getDate()+" "+dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds());
}

//��ȫ�˳�ϵͳ
function doLogout(){
	var result = confirm("������ʾ��Ϣ:�Ƿ�ȷ���˳���ϵͳ��");
	if(result == false){
		return false;
	}
	$.ajax({
		type: "post",
		url: "/jlyw/UserServlet.do?method=5&time=" + new Date().getTime(),
		dataType: "json",	//�������������ݵ�Ԥ������
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
			//���������
		}
	});
}

//����Ϣ���е�����
function doOpenUrlFromMessager(href){
	$("#MainFrame",parent.document.body).attr("src",href);
}