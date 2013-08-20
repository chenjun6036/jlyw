$().ready(function(){
	LoadStatistic();
});

function LoadStatistic(){
	if($('#DivHint').is(':visible')) return;
	$('#DivHint').show();
	$.ajax({
		url:'../Statistics/DoAjaxStatisticCount.aspx',
		type:'get',
		dataType:'json',
		error:function(e){
			$('#DivHint').hide();
			//alert(e);//显示错误信息
		},
		success:
			function(json){
				$('#DivHint').hide();
				switch(json.Result){
					case 'Success':
						for(var key in json){
							if(key.indexOf('link')>=0) $('#'+key).html(json[key]);
						}
						break;
					default:
						//alert(json.Info);//显示错误信息
						if(json.Info.indexOf('您必须登录')>=0) location.href = 'Login.aspx?ReturnUrl=Right.aspx';
						break;
				}
			}
	});
}