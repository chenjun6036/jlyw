<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>授权签字任务列表</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/waitingDlg.js"></script>
	<script>
		$(function(){			
			$('#task-table').datagrid({
				title:'待签字的原始记录和证书',
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/OriginalRecordServlet.do?method=7',
				queryParams:{'CommissionNumber':encodeURI($('#Search_CommissionNumber').val())},
//				sortName: 'id',
//				sortOrder: 'desc',
				remoteSort: true,
				idField:'OriginalRecordId',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {field:'CertificateCode',title:'证书编号',width:120,rowspan:2,align:'center'},
					{title:'委托单信息',colspan:3,align:'center'},
					{title:'器具信息',colspan:5,align:'center'},
					{title:'检验信息',colspan:7,align:'center'}
				],[
					{field:'CommissionCode',title:'委托单号',width:100,align:'center'},
					{field:'CustomerName',title:'委托单位',width:120,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					
					
					{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
					{field:'Model',title:'规格型号',width:60,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
					{field:'Quantity',title:'器具数量',width:60,align:'center'},
					
					{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
					{field:'Staff',title:'检/校人员',width:80,align:'center'},
					{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
					{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.ExcelDoc==""){
								return "";
							}else{
								if(rowData.ExcelPdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
								}
							}
						}},
					{field:'CertificateId',title:'证书文件',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.CertificateDoc==""){
								return "";
							}else{
								if(rowData.CertificatePdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
								}
							}
						}
					},
					{field:'VerifierName',title:'核验人',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.VerifyResult == ""){		//尚未审批
									return "<span title='尚未核验'>"+value+"</span>";
								}
								else if(rowData.VerifyResult == "1" || rowData.VerifyResult == 1 ){ //核验通过
									return "<span style='color: #0033FF' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
								}else{	//核验未通过
									return "<span style='color: #FF0000' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：未通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
								}
							}
						}
					},
					{field:'AuthorizerName',title:'批准人',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.AuthorizeResult == ""){		//尚未审批
									return "<span title='尚未签字'>"+value+"</span>";
								}
								else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //批准通过
									if(rowData.IsAuthBgRuning==true){
										return "<span style='color: #009933' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过(后台签字执行中...)\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
									}else{
										return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
									}
								}else{	//批准未通过
									return "<span style='color: #FF0000' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：未通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
							}
						}
					}
					
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
		function doTask()	//查看明细
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('提示！',"请选择一条签字任务！",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#OriginalRecordId').val(selectedRow.OriginalRecordId);
			$('#AuthorizeForm').submit();
		}
		function doSearchTaskList(ownTask)
		{
			$('#task-table').datagrid('options').queryParams={'CommissionNumber':encodeURI($('#Search_CommissionNumber').val()),'CustomerName':encodeURI($('#Search_CustomerName').val()),'OwnTask':ownTask};
			$('#task-table').datagrid('reload');
		}
		
		//打开批量签字对话框
		function doOpenBatchAuthWindow(){
			var selectedRows = $('#task-table').datagrid('getSelections');
			if(selectedRows.length == 0){
				$.messager.alert('提示！',"请选择一个签字任务！",'info');
				return false;
			}
			if(selectedRows.length == 1){
				 $('#authorize-execute-now').show();
			}else{
				$('#authorize-execute-now').hide();
			}
			var oRecordIds = "";
			for(var i=0; i<selectedRows.length; i++){
				oRecordIds = oRecordIds + selectedRows[i].OriginalRecordId +";";
			}
			$('#OriginalRecordIds').val(oRecordIds);
			$("#batch_auth_window").window('open');	
		}
		function doCloseBatchAuthWindow(){
			$("#batch_auth_window").window('close');	
		}
		function doSubmitAuthorizeByBatch(ExecutorResult){   	//提交签字结果
			$('#ExecutorResult').val(ExecutorResult);
			if($('#OriginalRecordIds').val()==""){
				$.messager.alert('提示！',"请选择一个签字任务！",'info');
				return false;
			}
			$("#Authorize-submit-form").form('submit', {
				url:'/jlyw/OriginalRecordServlet.do?method=12',	//批量授权签字
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					
					var vResult = $("#Authorize-submit-form").form('validate');
					if(vResult == false){
						return false;
					}else{
						ShowWaitingDlg("正在执行，请稍候...");
						return true;
					}
				},
				success:function(data){
					CloseWaitingDlg();
						
					var result = eval("("+data+")");
					if(result.IsOK){
						$.messager.alert('提示',result.msg,'info');
						
						doCloseBatchAuthWindow();
						
						//重新加载信息
						$('#task-table').datagrid('reload');
						
						//清空退样申请表单的“备注”信息
						$('#ExecuteMsg').val('');
						
						
					}else{
						$.messager.alert('提交失败！',result.msg,'error');
					}
				}
			});  
	   }
	   
	   function doSubmitAuthorizeByExecuteNow(ExecutorResult){   	//提交签字结果(立即执行)
			$('#ExecutorResult').val(ExecutorResult);
			if($('#OriginalRecordIds').val()==""){
				$.messager.alert('提示！',"请选择一个签字任务！",'info');
				return false;
			}
			$("#Authorize-submit-form").form('submit', {
				url:'/jlyw/OriginalRecordServlet.do?method=8',	//批量授权签字
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					
					var vResult = $("#Authorize-submit-form").form('validate');
					if(vResult == false){
						return false;
					}else{
						ShowWaitingDlg("正在执行，请稍候...");
						return true;
					}
				},
				success:function(data){
					CloseWaitingDlg();
						
					var result = eval("("+data+")");
					if(result.IsOK){
						$.messager.alert('提示','操作成功！','info');
						
						doCloseBatchAuthWindow();
						
						$('#task-table').datagrid('unselectAll');	//取消所有选择
						
						//重新加载信息
						$('#task-table').datagrid('reload');
						
						//清空退样申请表单的“备注”信息
						$('#ExecuteMsg').val('');
					}else{
						$.messager.alert('提交失败！',result.msg,'error');
					}
				}
			});  
	   }
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="原始记录、证书签字任务查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<table id="task-table" iconCls="icon-search"></table>

	<form method="post" action="/jlyw/TaskManage/AuthorizeApprove.jsp" id="AuthorizeForm">
		<input type="hidden" name="Code" id="Code" value="" />
		<input type="hidden" name="Pwd" id="Pwd" value="" />
		<input type="hidden" name="OriginalRecordId" id="OriginalRecordId" value="" />
	</form>
	<div id="task-table-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">查看明细</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" title="对选中的证书进行授权签字" iconCls="icon-add" plain="true" onclick="doOpenBatchAuthWindow()">授权签字</a>
					</td>
				<td style="text-align:right;padding-right:4px">
					<label>委托单号：</label><input type="text" id="Search_CommissionNumber" value="<%= request.getParameter("commissionsheetcode")==null?"":request.getParameter("commissionsheetcode") %>" style="width:120px" />&nbsp;<label>委托单位：</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<!--<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询所有要签字的原始记录和证书" id="btnHistorySearch" onclick="doSearchTaskList('false')">查询所有任务</a>&nbsp;--><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询需要我签字的原始记录和证书" id="btnHistorySearch" onclick="doSearchTaskList('true')">查询任务</a>
				</td>
			</tr>
		</table>
	</div>
	
<div id="batch_auth_window" class="easyui-window" closed="true" modal="true" title="证书授权签字" iconCls="icon-save" style="width:600px;height:250px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form id="Authorize-submit-form" method="post">
			<input type="hidden" name="OriginalRecordIds" id="OriginalRecordIds"  />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<table width="500" style="margin-left:20px" >
				<tr>
					<td width="22%" align="right">备注信息：</td>
					<td width="78%" colspan="3" rowspan="2" align="left"><textarea id="ExecuteMsg" style="width:300px;height:80px"  name="ExecuteMsg" class="easyui-validatebox" ></textarea></td>
				</tr>
				<tr >
					<td></td>
				</tr>
		  </table>
		  </form>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAuthorizeByBatch(1)" title="在后台执行授权签字，将签名写入证书文件中">授权签字(后台)</a>&nbsp;&nbsp;&nbsp;
			<span id="authorize-execute-now"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAuthorizeByExecuteNow(1)" title="立即执行授权签字，将签名写入证书文件并重新生成证书PDF">授权签字(立即执行)</a>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitAuthorizeByBatch(0)">驳回</a>&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onclick="doCloseBatchAuthWindow()">取消</a>&nbsp;&nbsp;&nbsp;
		</div>
	</div>
</div>

	</DIV>
</DIV>
</body>
</html>
