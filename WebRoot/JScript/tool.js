
function NoSubmitAgain(t) {//��ֹ�ظ��ύ,t�����Ӧ�İ�ťid+#�����簴ťid="button" �˲�����'#button'
	var i=1;
	$(t).attr("disabled",true);
	var timer=setInterval(function(){
								   i++;
								   if(i>2){
									   $(t).attr("disabled",false);
									   i=1;
									   clearInterval(timer)
						  }
			   },1000) 
}