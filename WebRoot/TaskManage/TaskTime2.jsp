<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>批量上传原始记录</title>
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
				
			
			
			doLoadCommissionSheet();
			
		});

		function doOpenUploadExcelWindow(){
			//$("#upload_excel_window").window('open');
			//$("#file_upload").uploadify("settings", "uploader", '/jlyw/FileUploadServlet.do?method=9&CommissionSheetId='+$('#CommissionSheetId').val());
		}
		function doCloseUploadExcelWindow(){
			$('#file_upload').uploadify('cancel','*', false); 	//删除未上传的文件队列
			//$("#upload_excel_window").window('close');	
		}
		function doUploadExcels(){
			$('#loading-msg').text('^_^');
			//上传
			$("#file_upload").uploadify("settings", "uploader", '/jlyw/FileUploadServlet.do?method=9&CommissionSheetId='+$('#CommissionSheetId').val());
  			$('#file_upload').uploadify('upload','*'); 
		}
		
		function detail(){
			$('#DetailForm_Code').val($('#Code').val());
			$('#DetailForm').submit();
		}

		function doLoadCommissionSheet(){	//查找委托单
			$("#SearchForm").form('submit', {
				url:'/jlyw/CommissionSheetServlet.do?method=3',
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					$("#CommissionSheetForm").form('clear');
					
					if($('#Code').val()=='' || $('#Pwd').val() == ''){
						$.messager.alert('提示！',"委托单无效！",'info');
						return false;
					}
					return $("#SearchForm").form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK){
						$("#CommissionSheetForm").form('load',result.CommissionObj);
						if(result.CommissionObj.Ness == 0){
							$("#Ness").attr("checked",true);	//勾选
						}			
						//doOpenUploadExcelWindow();					
					}else{
						$.messager.alert('查询失败！',result.msg,'error');
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
		服务器处理中，请稍后...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="批量上传原始记录" />
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
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">确定检验</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doOpenUploadExcelWindow()">上传原始记录</a>
			</td>
			<td style="text-align:right;padding-right:4px">
				<label>委托单号：</label><input type="text" id="Search_CommissionNumber" value="" style="width:120px" />&nbsp;<label>委托单位：</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询我未完成的检验任务" id="btnHistorySearch" onclick="doSearchTaskList()">查询任务</a>
			</td>
		</tr>
	</table>
</div>
-->
<div id="upload_excel_window" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:5px;height:330px" title="上传原始记录Excel文件" collapsible="false"  closable="false" border="false">
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
			<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="detail()" >查看委托单附件等详细信息</a>
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doUploadExcels()" >确认提交</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseUploadExcelWindow()">取消</a>
		</div>
	</div>
</div>

</DIV></DIV>
</body>
</html>
