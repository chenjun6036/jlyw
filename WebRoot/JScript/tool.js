
function NoSubmitAgain(t) {//防止重复提交,t是相对应的按钮id+#，例如按钮id="button" 此参数是'#button'
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