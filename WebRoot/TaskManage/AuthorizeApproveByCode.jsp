<%@ page contentType="text/html; charset=gb2312" language="java" import="com.jlyw.hibernate.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>原始记录、证书代签</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/waitingDlg.js"></script>
	<script>
	$(function(){
				 
		$('#OriginalRecord').datagrid({
			title:'原始记录信息',
			height:350,
			singleSelect:false, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			idField:'OriginalRecordId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'器具信息',colspan:8,align:'center'},
					{title:'检验信息',colspan:8,align:'center'},
					{title:'费用信息',colspan:7,align:'center'}
				],[
					{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
					{field:'Model',title:'规格型号',width:60,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
					{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
					{field:'ManageCode',title:'管理编号',width:80,align:'center'},
					{field:'Quantity',title:'器具数量',width:60,align:'center'},
					
					{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
					{field:'Staff',title:'检/校人员',width:80,align:'center'},
					{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
					{field:'CertificateCode',title:'证书编号',width:120,align:'center'},
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
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该证书' ><span style='color: #0033FF'>已完成</span></a>"
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
					},
					
					{field:'TestFee',title:'检测费',width:60,align:'center'},
					{field:'RepairFee',title:'修理费',width:60,align:'center'},
					{field:'MaterialFee',title:'材料费',width:60,align:'center'},
					{field:'CarFee',title:'交通费',width:60,align:'center'},
					{field:'DebugFee',title:'调试费',width:60,align:'center'},
					{field:'OtherFee',title:'其他费用',width:60,align:'center'},
					{field:'TotalFee',title:'总计费用',width:60,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			toolbar:[{
				text:'刷新',
				iconCls:'icon-reload',
				handler:function(){
					$('#OriginalRecord').datagrid('reload');
				}
			}]
		});
		
		doLoadCommissionSheet();	//加载委托单
	});
	function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
				$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});					
				
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载原始记录信息
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#OriginalRecord').datagrid('reload');
					
								
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	
   	 function doSubmitAuthorizeByBatch(ExecutorResult){   	//提交签字结果
			$('#ExecutorResult').val(ExecutorResult);
			var selectedRows = $('#OriginalRecord').datagrid('getSelections');
			if(selectedRows.length == 0){
				$.messager.alert('提示！',"请选择一个签字任务！",'info');
				return false;
			}
			
			var oRecordIds = "";
			for(var i=0; i<selectedRows.length; i++){
				oRecordIds = oRecordIds + selectedRows[i].OriginalRecordId +";";
			}
			$('#OriginalRecordIds').val(oRecordIds);
			
			
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
												
						//重新加载信息
						$('#OriginalRecord').datagrid('reload');
						
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
			var selectedRows = $('#OriginalRecord').datagrid('getSelections');
			if(selectedRows.length == 0){
				$.messager.alert('提示！',"请选择一个签字任务！",'info');
				return false;
			}
			if(selectedRows.length > 1){
				$.messager.alert('提示！',"授权签字(立即执行)一次只能选择一个签字任务！",'info');
				return false;
			}
			
			var oRecordIds = "";
			for(var i=0; i<selectedRows.length; i++){
				oRecordIds = oRecordIds + selectedRows[i].OriginalRecordId +";";
			}
			$('#OriginalRecordIds').val(oRecordIds);
			
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
												
						//重新加载信息
						$('#OriginalRecord').datagrid('reload');
						
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
			<jsp:param name="TitleName" value="原始记录、证书代签" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right">密  码：</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
	
		<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br/>
	
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;"
				title="原始记录" collapsible="false"  closable="false">
			<table id="OriginalRecord" iconCls="icon-tip" width="1000px" height="350px"></table>
			<br />
			<form id="Authorize-submit-form" method="post">
			<input type="hidden" name="OriginalRecordIds" id="OriginalRecordIds"  />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<input type="hidden" name="FromAuthByCode" value="true" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="16%" align="right">备注信息：</td>
					<td colspan="4" rowspan="2" align="left"><textarea id="ExecuteMsg" style="width:350px;height:80px"  name="ExecuteMsg" class="easyui-validatebox" ></textarea></td>
				</tr>
				<tr >
					<td></td>
				</tr>
				<tr style="padding-top:15px" >
				  <td height="39"  align="right"  style="padding-right:10px;"></td>
				  <td width="19%"  align="center" ><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAuthorizeByBatch(1)">授权签字(后台)</a></td>
			      <td width="21%"  align="center" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAuthorizeByExecuteNow(1)" title="立即执行授权签字，将签名写入证书文件并重新生成证书PDF">授权签字(立即执行)</a></td>
			      <td width="15%"  align="center" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitAuthorizeByBatch(0)">驳回</a></td>
			      <td width="29%"  align="left" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="javascript:history.go(-1)">返回</a></td>
			  </tr>
		  </table>
		  </form>
		</div>		

</DIV></DIV>
</body>
</html>
