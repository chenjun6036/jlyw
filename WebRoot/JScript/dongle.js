function checkDongle(pid,pin,username)
{
	try 
	{
		//�ҵ���
		var token_count = 0;
		token_count = ET299.FindToken(pid);
		
		if(token_count==0){
			alert("����������ڣ�����룡");
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
			alert("��֤���û��������ʧ�ܣ���ȷ�ϣ�");
			return false;
		}
		
	}
	catch(err)
	{
		LogoutWithOutConfirm();
		alert("�������ʧ�ܣ�"+err);
		return false;
	}	
	ET299.CloseToken();
}

//ǿ���˳�ϵͳ
function LogoutWithOutConfirm(){
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