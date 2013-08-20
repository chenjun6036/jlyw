function checkDongle(pid,pin,username)
{
	try 
	{
		//找到锁
		var token_count = 0;
		token_count = ET299.FindToken(pid);
		
		if(token_count==0){
			alert("软件狗不存在，请插入！");
			return false;
		}
		var i =1;
		for(i = 1; i <= token_count; i++){
			if((ET299.OpenToken(pid, i) & 0x0FFFF).toString(16)=='0'){
				if((ET299.VerifyPIN(0, pin) & 0x0FFFF).toString(16)=='0'){
					alert("fdasdddds");
				var user_name = ET299.Read(0, 0, 6);
				if(username==user_name){
					break;;
				}
				alert("ewewe");
				ET299.CloseToken();
			}
			}
		}
		
		if(i > token_count){
			alert("验证该用户的软件狗失败，请确认！");
			return false;
		}
		
	}
	catch(err)
	{
		LogoutWithOutConfirm();
		alert("打开软件狗失败！"+err);
		return false;
	}	
	ET299.CloseToken();
}

//强制退出系统
function LogoutWithOutConfirm(){
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