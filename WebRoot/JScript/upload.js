 var c = $(window.parent.document.body);
 $(function(){
	try{
		$('#uploaded_file_table').datagrid({
			title:'���ϴ����ļ�',			
			iconCls:'icon-tip',
			idField:'_id',
			fit:true,
			singleSelect:true,			
			rownumbers:true,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:"filename",title:"�ļ���",width:130,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='������ظ��ļ�' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				{field:"length",title:"��С",width:60,align:"left"},
				{field:"uploadDate",title:"�ϴ�ʱ��",width:120,align:"left"},
				{field:"uploadername",title:"�ϴ���",width:60,align:"left"}
			]],
			toolbar:[{
				text:'ɾ��',
				iconCls:'icon-remove',
				handler:function(){
					var row = $('#uploaded_file_table').datagrid('getSelected');
					if(row == null){
						$.messager.alert('��ʾ',"��ѡ��Ҫɾ�����ļ���",'info');
						return false;
					}
					var rowIndex = $('#uploaded_file_table').datagrid('getRowIndex', row._id);	//��ֹ��ʱ��datagridˢ���Ժ��ȡ��row����֮ǰ�ľ����ݣ�������û�й�ѡ����row��Ϊnull��
					if(rowIndex < 0){
						$.messager.alert('��ʾ',"��ѡ��Ҫɾ�����ļ���",'info');
						return false;
					}
					var result = confirm("��ȷ��Ҫɾ�� "+row.filename+" ��");
					if(result == false){
						return false;
					}
					//����ɾ���ļ�
					$.ajax({
							type: "post",
							url: "/jlyw/FileServlet.do?method=0",
							data: {"FileId":row._id,"FileType":row.filetype},
							dataType: "json",	//�������������ݵ�Ԥ������
							beforeSend: function(XMLHttpRequest){
							},
							success: function(data, textStatus){
								if(data.IsOK){
									$.messager.alert('��ʾ','ɾ���ɹ���','info');
									$('#uploaded_file_table').datagrid('reload');
								}else{
									$.messager.alert('ɾ���ļ�ʧ�ܣ�',data.msg,'error');
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
			},'-',{
				text:'ˢ��',
				iconCls:'icon-reload',
				handler:function(){
					$('#uploaded_file_table').datagrid('reload');
				}	
			}]
		});
	}catch(e){}
 });
 function waiting() { 
          waiting("���ڴ������Ժ�...");
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
*�ϴ�������
*�ȴ����֣�Ĭ�ϣ� ��ʾ�Ի�����
*�ļ�������ͨ���ļ��б����uploaded_file_table���Ĳ�����datagrid('options').queryParams.FilesetName����ȡ
*�ļ��ϴ��ؼ���ID��Ĭ��Ϊfile_upload
**/
 function doUploadByDefault(){
 	doUploadByUploadify($('#uploaded_file_table').datagrid('options').queryParams.FilesetName, 'file_upload');
 }
 /**
*�ϴ��������ȴ����֣�Ĭ�ϣ� ��ʾ�ȴ��Ի����ǣ���
*filesetname:�ļ�������
*uploadifyId:�ļ��ϴ��ؼ���ID
**/
 function doUploadByUploadify(filesetname,uploadifyId){
	 doUploadByUploadify( filesetname, uploadifyId, true);
 }
  /**
*�ϴ�����(�ȴ����֣�Ĭ��)��
*filesetname:�ļ�������
*uploadifyId:�ļ��ϴ��ؼ���ID
*isShowDlg:�Ƿ���ʾ�ȴ��Ի���
**/
 function doUploadByUploadify(filesetname,uploadifyId,isShowDlg){
	try{
		if(filesetname == null || filesetname == ''){
			$.messager.alert('��ʾ','δָ���ϴ����ļ��������ļ�����','info');
			return false;
		}
		if(uploadifyId == null || uploadifyId == ''){
			$.messager.alert('��ʾ','�ϴ��ؼ�IDδָ����','info');
			return false;
		}
		uploadifyUpload( null, filesetname, uploadifyId, isShowDlg);
	}catch(e){
	}
 }
 /**
*�ϴ�������
*waitingStr:�ȴ�����
*filesetname:�ļ�������
*uploadifyId:�ļ��ϴ��ؼ���ID
*isShowDlg:�Ƿ���ʾ�ȴ��Ի���
**/
 function uploadifyUpload(waitingStr, filesetname, uploadifyId, isShowDlg){
		if(filesetname == null || filesetname == ''){
			return false;
		}
		if(uploadifyId == null || uploadifyId == ''){
			return false;
		}
		var num = $('#'+uploadifyId).uploadifySettings('queueSize');
		if (num == 0) { //û��ѡ���ļ�
			$.messager.alert('��ʾ','��ѡ��Ҫ�ϴ����ļ���','info');
			return false;
		}
		
		//���� scriptData �Ĳ���  
        $('#'+uploadifyId).uploadifySettings('scriptData',{'FilesetName':filesetname});
		
		//�ϴ�  
  		$('#'+uploadifyId).uploadifyUpload(); 
		 
		 var str="���ڷ�������ִ��, ���Ժ�...";
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
		var maxHeight = $(document).height();	//���ֲ�߶�
		if(maxHeight < $(document).scrollTop()+$(window).height()){
			maxHeight = $(document).scrollTop()+$(window).height()
		}
		var newLeft = $(document).scrollLeft() + ($(window).width()-200)*0.5;//�ȴ��Ի�����󶥵�����
		var newTop = $(document).scrollTop() + ($(window).height()-50)*0.5;//�ȴ��Ի�����󶥵�����
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
		shield.id = "WaitingDivShield";	//�ȴ����ֲ�
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
		alertFram.id="WaitingDivAlertFram";	//�ȴ��Ի���
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
			strHtml+="���ڷ�������ִ��, ���Ժ�...";
		} 
		else{
			strHtml+=message;
		}
	   strHtml+=" </p></div>";
	
		strHtml+="<iframe src=\"\" style=\"position:absolute; visibility:inherit;top:0px; left:0px; width:150px; height:100px; z-index:-1; filter='progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';\"></iframe>";  //�Ӹ�iframe��ֹ��drowdownlist�ȿؼ���ס
	
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
		var ad = setInterval("doAlpha()",1);	//����Ч��
	
//		eSrc.blur();
		document.body.onselectstart = function(){return false;}
		document.body.oncontextmenu = function(){return false;}
	}catch(e){}
}

 //����ҳ����һЩ����Ŀؼ�
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