<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��������</title>
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
							if(retData.msg!=null&&retData.msg.length>0)
								$.messager.alert('��ʾ',retData.msg,'info');
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
				
			$('#task-table').datagrid({
				title:'�ҵ�δ��������б�',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/TaskAssignServlet.do?method=4',
				queryParams:{'CommissionNumber':encodeURI($('#Search_CommissionNumber').val())},
//				sortName: 'id',
//				sortOrder: 'desc',
				remoteSort: true,
				idField:'TaskId',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {title:'ί�е���Ϣ',colspan:8,align:'center'},
					{field:'Urgent',title:'�Ƿ�Ӽ�',width:60,rowspan:2,align:'center',
						formatter:function(value,rowData,rowIndex){
							if (0 == value){
								return '<span style="color:red;">�Ӽ�</span>';
							} else {
								return "";
							}
						}

					},
					{field:'IsOverdue',title:'�Ƿ���',width:60,rowspan:2,align:'center',
						formatter:function(value,rowData,rowIndex){
							if (true == value){
								return '<span style="color:red;" title="����������'+rowData.OverdueDays+'��">�ѳ���</span>';
							} else {
								return "";
							}
						}
					},
					{title:'����Ԥ��',colspan:2,align:'center'},
					{title:'������Ϣ',colspan:4,align:'center'}
				],[
					{field:'CommissionCode',title:'ί�е���',width:100,sortable:true,align:'center'},
					{field:'ApplianceName',title:'��������',width:100,align:'center'},
					{field:'Quantity',title:'��������',width:60,align:'center'},
					{field:'RecordQuantity',title:'������������',width:70,align:'center'},
					{field:'FinishQuantity',title:'���깤��������',width:70,align:'center'},
					{field:'EffectQuantity',title:'��Ч��������',width:70,align:'center'},
					{field:'CustomerName',title:'ί�е�λ',width:150,sortable:true,align:'center'},
					{field:'CustomerContactor',title:'��ϵ��',width:60,align:'center'},
					{field:'CustomerContactorTel',title:'��ϵ�绰',width:80,align:'center'},
					{field:'CommissionDate',title:'ί������',width:80,sortable:true,align:'center'},
					{field:'PromiseDate',title:'��ŵ�������',width:80,align:'center'},
					{field:'IsOverdueWarningShort',title:'15��',width:60,align:'center',
						 formatter:function(value,rowData,rowIndex){
							if (true == value){
								return "<span style='color:red;' title='���볬�ڻ��У�"+rowData.OverdueWarningDays+"��'>�澯</span>";
							} else {
								return "";
							}
						}
					},
					{field:'IsOverdueWarningLong',title:'30��',width:60,align:'center',
						 formatter:function(value,rowData,rowIndex){
							if (true == value){
								return "<span style='color:red;' title='���볬�ڻ��У�"+rowData.OverdueWarningDays+"��'>�澯</span>";
							} else {
								return "";
							}
						}
					},
					{field:'ProjectName',title:'������Ŀ����',width:100,align:'center'},
					{field:'AlloteeName',title:'������Ա',width:60,align:'center'},
					{field:'AssignTime',title:'����ʱ��',width:100,align:'center'},
					{field:'Remark',title:'��ע',width:100,align:'center'}
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#task-table-search-toolbar",
				onLoadSuccess:function(data){
					if(data.rows.length > 0){
						$(this).datagrid('selectRow', 0);
					}
				}

			});
			
		});
		function doTask()
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#TaskId').val(selectedRow.TaskId);
			$('#TaskForm').submit();
			$('#loading').show();	
			//var win = window.open("","DoTask","height=850,width=1050,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			//var win = window.open("","DoTask","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			//if(win.location.href=="about:blank"){	//���ڲ�����
			//	$('#TaskForm').submit();
			//}else{
			//	$.messager.alert('��ʾ��',"����ͬʱ�򿪶��ί�е����鴰�ڣ������л����ȹرյ�ǰ�򿪵�ί�е����ڣ�",'info', function(){win.focus();});
			//}
		}
		function doEditCertificate(){
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
				return false;
			}
			$('#EditCode').val(selectedRow.CommissionCode);
			$('#EditPwd').val(selectedRow.CommissionPwd);
			$('#EditTaskId').val(selectedRow.TaskId);
			$('#EditTaskForm').submit();
			$('#loading').show();	
		}
		function doSearchTaskList()
		{
			$('#task-table').datagrid('options').queryParams={'CommissionNumber':encodeURI($('#Search_CommissionNumber').val()),'CustomerName':encodeURI($('#Search_CustomerName').val())};
			$('#task-table').datagrid('reload');
		}
		function doOpenUploadExcelWindow(){
			//var selectedRow = $('#task-table').datagrid('getSelected');
//			if(selectedRow == null){
//				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
//				return false;
//			}
//			$("#upload_excel_window").window('open');
//			$("#file_upload").uploadify("settings", "uploader", '/jlyw/FileUploadServlet.do?method=9&CommissionSheetId='+selectedRow.CommissionSheetId);
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
				return false;
			}
			$('#Code1').val(selectedRow.CommissionCode);
			$('#Pwd1').val(selectedRow.CommissionPwd);
			$('#CommissionSheetId1').val(selectedRow.CommissionSheetId);
			$('#TestForm').submit();

		}
		function doCloseUploadExcelWindow(){
			$('#file_upload').uploadify('cancel','*', false); 	//ɾ��δ�ϴ����ļ�����
			$("#upload_excel_window").window('close');	
		}
		function doUploadExcels(){
			$('#loading-msg').text('^_^');
			//�ϴ�
  			$('#file_upload').uploadify('upload','*'); 
		}
	</script>
</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.png" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		�����������У����Ժ�...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�����ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	
		<table id="task-table" iconCls="icon-search"></table>
		<form method="post" action="/jlyw/TaskManage/ComSheetInspect.jsp" id="TaskForm">
		  <input type="hidden" name="Code" id="Code" value="" />
		  <input type="hidden" name="Pwd" id="Pwd" value="" />
		  <input type="hidden" name="TaskId" id="TaskId" value="" />
		</form>
		<form method="post" action="/jlyw/TaskManage/TaskTime2.jsp" id="TestForm" target="_blank">
		  <input type="hidden" name="Code" id="Code1" value="" />
		  <input type="hidden" name="Pwd" id="Pwd1" value="" />
		   <input type="hidden" name="CommissionSheetId" id="CommissionSheetId1" value="" />
		</form>
		<form method="post" action="/jlyw/TaskManage/EditCertificate.jsp" id="EditTaskForm">
		  <input type="hidden" name="Code" id="EditCode" value="" />
		  <input type="hidden" name="Pwd" id="EditPwd" value="" />
		  <input type="hidden" name="TaskId" id="EditTaskId" value="" />
		</form>

<div id="task-table-search-toolbar" style="padding:2px 0">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">ȷ������</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doOpenUploadExcelWindow()">�ϴ�ԭʼ��¼</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="doEditCertificate()">�޸�֤��</a>
			</td>
			<td style="text-align:right;padding-right:4px">
				<label>ί�е��ţ�</label><input type="text" id="Search_CommissionNumber" value="<%= request.getParameter("commissionsheetcode")==null?"":request.getParameter("commissionsheetcode") %>" style="width:120px" />&nbsp;<label>ί�е�λ��</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��δ��ɵļ�������" id="btnHistorySearch" onclick="doSearchTaskList()">��ѯ����</a>
			</td>
		</tr>
	</table>
</div>

<div id="upload_excel_window" class="easyui-window" closed="true" modal="true" title="�ϴ�ԭʼ��¼Excel�ļ�" iconCls="icon-save" style="width:550px;height:450px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<table width="450px"; height="300px">
				<tr>
					<td height="300" valign="top" align="left" style="overflow:hidden">
						<div class="easyui-panel" fit="true" collapsible="false"  closable="false">
							<input id="file_upload" type="file" name="file_upload" />
						</div>								</td>
				</tr>
			</table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doUploadExcels()" >ȷ���ύ</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseUploadExcelWindow()">ȡ��</a>
		</div>
	</div>
</div>

</DIV></DIV>
</body>
</html>
