 var c = $(window.parent.document.body);
 $(function(){
	try{
		$('#uploaded_file_table').datagrid({
			title:'已上传的文件',			
			iconCls:'icon-tip',
			idField:'_id',
			fit:true,
			singleSelect:true,			
			rownumbers:true,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:"filename",title:"文件名",width:130,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='点击下载该文件' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				{field:"length",title:"大小",width:60,align:"left"},
				{field:"uploadDate",title:"上传时间",width:120,align:"left"},
				{field:"uploadername",title:"上传人",width:60,align:"left"}
			]],
			toolbar:[{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					var row = $('#uploaded_file_table').datagrid('getSelected');
					if(row == null){
						$.messager.alert('提示',"请选择要删除的文件！",'info');
						return false;
					}
					var rowIndex = $('#uploaded_file_table').datagrid('getRowIndex', row._id);	//防止有时候datagrid刷新以后获取的row还是之前的旧数据（界面上没有勾选，但row不为null）
					if(rowIndex < 0){
						$.messager.alert('提示',"请选择要删除的文件！",'info');
						return false;
					}
					var result = confirm("您确定要删除 "+row.filename+" 吗？");
					if(result == false){
						return false;
					}
					//请求删除文件
					$.ajax({
							type: "post",
							url: "/jlyw/FileServlet.do?method=0",
							data: {"FileId":row._id,"FileType":row.filetype},
							dataType: "json",	//服务器返回数据的预期类型
							beforeSend: function(XMLHttpRequest){
							},
							success: function(data, textStatus){
								if(data.IsOK){
									$.messager.alert('提示','删除成功！','info');
									$('#uploaded_file_table').datagrid('reload');
								}else{
									$.messager.alert('删除文件失败！',data.msg,'error');
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
			},'-',{
				text:'刷新',
				iconCls:'icon-reload',
				handler:function(){
					$('#uploaded_file_table').datagrid('reload');
				}	
			}]
		});
	}catch(e){}
 });
 function waiting() { 
          waiting("正在处理，请稍后...");
 } 
 function waiting(str) { 
          parent.document.getElementById('waiting201200000').style.visibility='visible'; 
		  var temp=parent.document.getElementById('display');
		  temp.innerHTML="<p style='color:#CCCCCC'>"+str+"</p>";
 } 
  function completed() { 
		//$(window.parent.document.body).getElementById('waiting2').style.visibility='hidden'; 
		parent.document.getElementById('waiting201200000').style.visibility='hidden'; 
 } // JavaScript Document


/**
*上传附件：
*等待文字：默认； 显示对话框：是
*文件集名称通过文件列表（表格uploaded_file_table）的参数（datagrid('options').queryParams.FilesetName）获取
*文件上传控件的ID：默认为file_upload
**/
 function doUploadByDefault(){
 	doUploadByUploadify($('#uploaded_file_table').datagrid('options').queryParams.FilesetName, 'file_upload');
 }
 /**
*上传附件（等待文字：默认； 显示等待对话框：是）：
*filesetname:文件集名称
*uploadifyId:文件上传控件的ID
**/
 function doUploadByUploadify(filesetname,uploadifyId){
	 doUploadByUploadify( filesetname, uploadifyId, true);
 }
  /**
*上传附件(等待文字：默认)：
*filesetname:文件集名称
*uploadifyId:文件上传控件的ID
*isShowDlg:是否显示等待对话框
**/
 function doUploadByUploadify(filesetname,uploadifyId,isShowDlg){
	try{
		if(filesetname == null || filesetname == ''){
			$.messager.alert('提示','未指定上传的文件所属的文件集！','info');
			return false;
		}
		if(uploadifyId == null || uploadifyId == ''){
			$.messager.alert('提示','上传控件ID未指定！','info');
			return false;
		}
		uploadifyUpload( null, filesetname, uploadifyId, isShowDlg);
	}catch(e){
	}
 }
 /**
*上传附件：
*waitingStr:等待文字
*filesetname:文件集名称
*uploadifyId:文件上传控件的ID
*isShowDlg:是否显示等待对话框
**/
 function uploadifyUpload(waitingStr, filesetname, uploadifyId, isShowDlg){
		if(filesetname == null || filesetname == ''){
			return false;
		}
		if(uploadifyId == null || uploadifyId == ''){
			return false;
		}
		var num = $('#'+uploadifyId).uploadifySettings('queueSize');
		if (num == 0) { //没有选择文件
			$.messager.alert('提示','请选择要上传的文件！','info');
			return false;
		}
		
		//设置 scriptData 的参数  
        $('#'+uploadifyId).uploadifySettings('scriptData',{'FilesetName':filesetname});
		
		//上传  
  		$('#'+uploadifyId).uploadifyUpload(); 
		 
		 var str="正在服务器上执行, 请稍候...";
		 if(waitingStr != null && waitingStr != '' && typeof(waitingStr)!="undefined"){
		 	str = waitingStr;
		 }
		 if(isShowDlg == true){
			// waiting(str) ;
		 	ShowWaitingDlg(str);
		 }
} 

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

 //隐藏页面上一些特殊的控件
function HideElement(){
    var HideElementTemp = new Array('IMG','SELECT','OBJECT','IFRAME');
    for(var j=0;j<HideElementTemp.length;j++){
        try{
                var strElementTagName=HideElementTemp[j];
                for(i=0;i<document.all.tags(strElementTagName).length; i++){
			        var objTemp = document.all.tags(strElementTagName)[i];
			        if(!objTemp||!objTemp.offsetParent)
					         continue;
                   //objTemp.style.visibility="hidden";
		           objTemp.disabled="disabled";
                }
        }
        catch(e){}
    }
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