<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�����ϴ�ԭʼ��¼</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	<link rel="stylesheet" type="text/css" href="../css/loading.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify3.1/uploadify.css" />
	<script type="text/javascript" src="../uploadify3.1/jquery.uploadify-3.1.js"></script>
	<script>
		$(function(){
			$('#loading').hide();	//�ȴ�������
			$('#file_upload').uploadify({
				'uploader'    : '/jlyw/FileUploadServlet.do?method=9',
				'method'    :'POST',	//��Ҫ�����������ΪGET��Ĭ��POST
				'queueSizeLimit': 100,	//1,//һ��ֻ�ܴ�һ���ļ�
				'buttonImage' : '../uploadify3.1/selectfiles.png',
				'fileTypeDesc'  : '֧�ָ�ʽ:xls', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
				'fileTypeExts'   : '*.xls;',   //����ĸ�ʽ
				'removeCompleted':false,	//�ϴ��ɹ����Զ����
				onUploadSuccess: function (file, data, response) { 
					if(response == true){
						var retData = eval("("+data+")");
						if(retData.IsOK == false){
							$.messager.alert('��ʾ',retData.msg,'error');
							$('#file_upload').uploadify('stop');
							return false;
						}else{
							$('#file_upload').uploadify('cancel', file.id, false);	//false��ʾ����onUploadCancel �¼����ᴥ�����Ӷ��������������
						}
					}else{
						$.messager.alert('��ʾ','������δ��Ӧ��','error');
						$('#file_upload').uploadify('stop');
						return false;
					}
				},
				onUploadStart:function(file){
					$('#loading-msg').text('У�顢�����ļ���'+file.name);
					$('#loading').show();	//��ʾ�ȴ���  
				},
				onQueueComplete: function(queueData){
					//CloseWaitingDlg();
					$('#loading').hide();	//�ȴ�������
				}
			 });
				
			
			
			doLoadCommissionSheet();
			
		});

		function doOpenUploadExcelWindow(){
			//$("#upload_excel_window").window('open');
			//$("#file_upload").uploadify("settings", "uploader", '/jlyw/FileUploadServlet.do?method=9&CommissionSheetId='+$('#CommissionSheetId').val());
		}
		function doCloseUploadExcelWindow(){
			$('#file_upload').uploadify('cancel','*', false); 	//ɾ��δ�ϴ����ļ�����
			//$("#upload_excel_window").window('close');	
		}
		function doUploadExcels(){
			$('#loading-msg').text('^_^');
			//�ϴ�
			$("#file_upload").uploadify("settings", "uploader", '/jlyw/FileUploadServlet.do?method=9&CommissionSheetId='+$('#CommissionSheetId').val());
  			$('#file_upload').uploadify('upload','*'); 
		}
		
		function detail(){
			$('#DetailForm_Code').val($('#Code').val());
			$('#DetailForm').submit();
		}

		function doLoadCommissionSheet(){	//����ί�е�
			$("#SearchForm").form('submit', {
				url:'/jlyw/CommissionSheetServlet.do?method=3',
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					$("#CommissionSheetForm").form('clear');
					
					if($('#Code').val()=='' || $('#Pwd').val() == ''){
						$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
						return false;
					}
					return $("#SearchForm").form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK){
						$("#CommissionSheetForm").form('load',result.CommissionObj);
						if(result.CommissionObj.Ness == 0){
							$("#Ness").attr("checked",true);	//��ѡ
						}			
						//doOpenUploadExcelWindow();					
					}else{
						$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
					}
				}
			});  
		}
	</script>
</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		�����������У����Ժ�...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�����ϴ�ԭʼ��¼" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="DetailForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="DetailForm_Code" type="hidden" name="Code"/>
        </form>
	 <form id="SearchForm" method="post" >
		<input id="Code" type="hidden" name="Code" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" style="width:150px;" />
		<input id="Pwd" type="hidden" name="Pwd" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" style="width:150px;" />
		<input id="CommissionSheetId" type="hidden" name="CommissionSheetId" value="<%= request.getParameter("CommissionSheetId")==null?"":request.getParameter("CommissionSheetId") %>" style="width:150px;" />
     </form>
	 <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	
<!--<div id="task-table-search-toolbar" style="padding:2px 0">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">ȷ������</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doOpenUploadExcelWindow()">�ϴ�ԭʼ��¼</a>
			</td>
			<td style="text-align:right;padding-right:4px">
				<label>ί�е��ţ�</label><input type="text" id="Search_CommissionNumber" value="" style="width:120px" />&nbsp;<label>ί�е�λ��</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��δ��ɵļ�������" id="btnHistorySearch" onclick="doSearchTaskList()">��ѯ����</a>
			</td>
		</tr>
	</table>
</div>
-->
<div id="upload_excel_window" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:5px;height:330px" title="�ϴ�ԭʼ��¼Excel�ļ�" collapsible="false"  closable="false" border="false">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<table width="900px"; height="240px">
				<tr>
					<td height="220px" valign="top" align="left" style="overflow:hidden">
						<div class="easyui-panel" fit="true" collapsible="false"  closable="false">
							<input id="file_upload" type="file" name="file_upload" />
						</div>								
					</td>
				</tr>
			</table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="detail()" >�鿴ί�е���������ϸ��Ϣ</a>
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doUploadExcels()" >ȷ���ύ</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseUploadExcelWindow()">ȡ��</a>
		</div>
	</div>
</div>

</DIV></DIV>
</body>
</html>
