<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>任务排期</title>
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
			$('#loading').hide();	//等待框隐藏
			$('#file_upload').uploadify({
				'uploader'    : '/jlyw/FileUploadServlet.do?method=9',
				'method'    :'POST',	//需要传参数必须改为GET，默认POST
				'queueSizeLimit': 100,	//1,//一次只能传一个文件
				'buttonImage' : '../uploadify3.1/selectfiles.png',
				'fileTypeDesc'  : '支持格式:xls', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
				'fileTypeExts'   : '*.xls;',   //允许的格式
				'removeCompleted':false,	//上传成功后不自动清除
				onUploadSuccess: function (file, data, response) { 
					if(response == true){
						var retData = eval("("+data+")");
						if(retData.IsOK == false){
							$.messager.alert('提示',retData.msg,'error');
							$('#file_upload').uploadify('stop');
							return false;
						}else{
							$('#file_upload').uploadify('cancel', file.id, false);	//false表示触发onUploadCancel 事件将会触发（从队列中正常清除）
							if(retData.msg!=null&&retData.msg.length>0)
								$.messager.alert('提示',retData.msg,'info');
						}
					}else{
						$.messager.alert('提示','服务器未响应！','error');
						$('#file_upload').uploadify('stop');
						return false;
					}
				},
				onUploadStart:function(file){
					$('#loading-msg').text('校验、处理文件：'+file.name);
					$('#loading').show();	//显示等待框  
				},
				onQueueComplete: function(queueData){
					//CloseWaitingDlg();
					$('#loading').hide();	//等待框隐藏
				}
			 });
				
			$('#task-table').datagrid({
				title:'我的未完成任务列表',
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
			        {title:'委托单信息',colspan:8,align:'center'},
					{field:'Urgent',title:'是否加急',width:60,rowspan:2,align:'center',
						formatter:function(value,rowData,rowIndex){
							if (0 == value){
								return '<span style="color:red;">加急</span>';
							} else {
								return "";
							}
						}

					},
					{field:'IsOverdue',title:'是否超期',width:60,rowspan:2,align:'center',
						formatter:function(value,rowData,rowIndex){
							if (true == value){
								return '<span style="color:red;" title="超期天数：'+rowData.OverdueDays+'天">已超期</span>';
							} else {
								return "";
							}
						}
					},
					{title:'超期预警',colspan:2,align:'center'},
					{title:'任务信息',colspan:4,align:'center'}
				],[
					{field:'CommissionCode',title:'委托单号',width:100,sortable:true,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:100,align:'center'},
					{field:'Quantity',title:'器具数量',width:60,align:'center'},
					{field:'RecordQuantity',title:'已做器具数量',width:70,align:'center'},
					{field:'FinishQuantity',title:'可完工器具数量',width:70,align:'center'},
					{field:'EffectQuantity',title:'有效器具数量',width:70,align:'center'},
					{field:'CustomerName',title:'委托单位',width:150,sortable:true,align:'center'},
					{field:'CustomerContactor',title:'联系人',width:60,align:'center'},
					{field:'CustomerContactorTel',title:'联系电话',width:80,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,sortable:true,align:'center'},
					{field:'PromiseDate',title:'承诺检出日期',width:80,align:'center'},
					{field:'IsOverdueWarningShort',title:'15天',width:60,align:'center',
						 formatter:function(value,rowData,rowIndex){
							if (true == value){
								return "<span style='color:red;' title='距离超期还有："+rowData.OverdueWarningDays+"天'>告警</span>";
							} else {
								return "";
							}
						}
					},
					{field:'IsOverdueWarningLong',title:'30天',width:60,align:'center',
						 formatter:function(value,rowData,rowIndex){
							if (true == value){
								return "<span style='color:red;' title='距离超期还有："+rowData.OverdueWarningDays+"天'>告警</span>";
							} else {
								return "";
							}
						}
					},
					{field:'ProjectName',title:'检验项目名称',width:100,align:'center'},
					{field:'AlloteeName',title:'检验人员',width:60,align:'center'},
					{field:'AssignTime',title:'分配时间',width:100,align:'center'},
					{field:'Remark',title:'备注',width:100,align:'center'}
					
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
				$.messager.alert('提示！',"请选择一个检验任务！",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#TaskId').val(selectedRow.TaskId);
			$('#TaskForm').submit();
			$('#loading').show();	
			//var win = window.open("","DoTask","height=850,width=1050,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			//var win = window.open("","DoTask","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			//if(win.location.href=="about:blank"){	//窗口不存在
			//	$('#TaskForm').submit();
			//}else{
			//	$.messager.alert('提示！',"不能同时打开多个委托单检验窗口，如需切换请先关闭当前打开的委托单窗口！",'info', function(){win.focus();});
			//}
		}
		function doEditCertificate(){
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('提示！',"请选择一个检验任务！",'info');
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
//				$.messager.alert('提示！',"请选择一个检验任务！",'info');
//				return false;
//			}
//			$("#upload_excel_window").window('open');
//			$("#file_upload").uploadify("settings", "uploader", '/jlyw/FileUploadServlet.do?method=9&CommissionSheetId='+selectedRow.CommissionSheetId);
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('提示！',"请选择一个检验任务！",'info');
				return false;
			}
			$('#Code1').val(selectedRow.CommissionCode);
			$('#Pwd1').val(selectedRow.CommissionPwd);
			$('#CommissionSheetId1').val(selectedRow.CommissionSheetId);
			$('#TestForm').submit();

		}
		function doCloseUploadExcelWindow(){
			$('#file_upload').uploadify('cancel','*', false); 	//删除未上传的文件队列
			$("#upload_excel_window").window('close');	
		}
		function doUploadExcels(){
			$('#loading-msg').text('^_^');
			//上传
  			$('#file_upload').uploadify('upload','*'); 
		}
	</script>
</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.png" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		服务器处理中，请稍后...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="任务查询" />
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
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">确定检验</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doOpenUploadExcelWindow()">上传原始记录</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="doEditCertificate()">修改证书</a>
			</td>
			<td style="text-align:right;padding-right:4px">
				<label>委托单号：</label><input type="text" id="Search_CommissionNumber" value="<%= request.getParameter("commissionsheetcode")==null?"":request.getParameter("commissionsheetcode") %>" style="width:120px" />&nbsp;<label>委托单位：</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询我未完成的检验任务" id="btnHistorySearch" onclick="doSearchTaskList()">查询任务</a>
			</td>
		</tr>
	</table>
</div>

<div id="upload_excel_window" class="easyui-window" closed="true" modal="true" title="上传原始记录Excel文件" iconCls="icon-save" style="width:550px;height:450px;padding:5px;">
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
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doUploadExcels()" >确认提交</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseUploadExcelWindow()">取消</a>
		</div>
	</div>
</div>

</DIV></DIV>
</body>
</html>
