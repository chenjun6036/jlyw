//显示或消除等待（加载中）图层
function ShowWaitingDlg(message){
    try{
//		HideElement();
		
		//by zhan
		var maxHeight = $(document).height();	//遮罩层高度
		if(maxHeight < $(document).scrollTop()+$(window).height()){
			maxHeight = $(document).scrollTop()+$(window).height()
		}
		var newLeft = $(document).scrollLeft() + ($(window).width()-200)*0.5;//等待对话框的左顶点坐标
		var newTop = $(document).scrollTop() + ($(window).height()-50)*0.5;//等待对话框的左顶点坐标
		if(newLeft <= 0){
			newLeft = "35%";
		}else{
			newLeft += "px";
		}
		if(newTop <= 0){
			newTop = "30%";
		}else{
			newTop += "px";
		}
		//by zhan:end

		var eSrc=(document.all)?window.event.srcElement:arguments[1];
		var shield = document.createElement("DIV");
		shield.id = "WaitingDivShield";	//等待遮罩层
		shield.style.position = "absolute";
		shield.style.left = "0px";
		shield.style.top = "0px";
		shield.style.width = "100%";
		shield.style.height = maxHeight+"px";
		//shield.style.height = ((document.documentElement.clientHeight>document.documentElement.scrollHeight)?document.documentElement.clientHeight:document.documentElement.scrollHeight)+"px";
		//shield.style.height =document.body.scrollHeight;
		shield.style.background = "#333";
		shield.style.textAlign = "center";
		shield.style.zIndex = "10000";
		shield.style.filter = "alpha(opacity=0)";
		shield.style.opacity = 0;
	
		var alertFram = document.createElement("DIV");
		var height="50px";
		alertFram.id="WaitingDivAlertFram";	//等待对话框
		alertFram.style.position = "absolute";
		alertFram.style.width = "200px";
		alertFram.style.height = height;
		alertFram.style.left = newLeft; //"35%";
		alertFram.style.top = newTop; //"30%";
	   // alertFram.style.marginLeft = "-225px" ;
	   // alertFram.style.marginTop = -75+document.documentElement.scrollTop+"px";
		alertFram.style.background = "#fff";
		alertFram.style.textAlign = "center";
		alertFram.style.lineHeight = height;
		alertFram.style.zIndex = "10001";
	
	   var strHtml ="<div style=\"width:100%; border:#58a3cb solid 1px; text-align:center;padding-top:10px;background-color:#FFFFFF \">";
	   strHtml+=" <img src=\"/jlyw/images/loading32.gif\"><br><p style=\"font-size: 12px\">";
	   if (typeof(message)=="undefined"){
			strHtml+="正在服务器上执行, 请稍候...";
		} 
		else{
			strHtml+=message;
		}
	   strHtml+=" </p></div>";
	
		strHtml+="<iframe src=\"\" style=\"position:absolute; visibility:inherit;top:0px; left:0px; width:150px; height:100px; z-index:-1; filter='progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';\"></iframe>";  //加个iframe防止被drowdownlist等控件挡住
	
		alertFram.innerHTML=strHtml;
		document.body.appendChild(alertFram);
		document.body.appendChild(shield);
	
		this.setOpacity = function(obj,opacity){
			if(opacity>=1)opacity=opacity/100;
			try{ obj.style.opacity=opacity; }catch(e){}
			try{ 
				if(obj.filters.length>0&&obj.filters("alpha")){
				obj.filters("alpha").opacity=opacity*100;
				}else{
				obj.style.filter="alpha(opacity=\""+(opacity*100)+"\")";
				}
			}
			catch(e){}
		}
	
		var c = 0;
		this.doAlpha = function(){
		if (++c > 20){clearInterval(ad);return 0;}
			setOpacity(shield,c);
		}
		var ad = setInterval("doAlpha()",1);	//渐变效果
	
//		eSrc.blur();
		document.body.onselectstart = function(){return false;}
		document.body.oncontextmenu = function(){return false;}
	}catch(e){}
}

function CloseWaitingDlg(){
    try{
		var shield= document.getElementById("WaitingDivShield");
		var alertFram= document.getElementById("WaitingDivAlertFram");
		if(shield!=null) {
			document.body.removeChild(shield);
		}
		if(alertFram!=null) {
			document.body.removeChild(alertFram);
		}
		document.body.onselectstart = function(){return true};
		document.body.oncontextmenu = function(){return true};
	}catch(e){}
}