<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>费用分配</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script>
$(function(){		
	$("#ProjectSelect").combobox({
		valueField:'appStdProId',
		textField:'appStdProName',
		editable:false,
		panelHeight:'auto', 
		onSelect:function(record){
			$("#AlloteeSelect").combobox('clear');
			$("#AlloteeSelect").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType=0&ApplianceSpeciesId='+record.appStdNameId);	//检验人员
//				$("#AlloteeSelect").combobox('loadData', record.AlloteeList);
		}
	});
		
	$('#allot_info_table').datagrid({
		title:'检验项目信息',
//			iconCls:'icon-save',
//			width:900,
		height:200,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/TaskAssignServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		//idField:'userid',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'AppStdNameProTeamName',title:'检验项目名称',width:120,sortable:true},
			{field:'AlloteeName',title:'检验人员',width:80},
			{field:'AssignerName',title:'分配人',width:80},
			{field:'AssignTime',title:'分配时间',width:80},
			{field:'TestFee',title:'检测费',width:70},
			{field:'RepairFee',title:'修理费',width:70},
			{field:'MaterialFee',title:'材料费',width:70},
			{field:'CarFee',title:'交通费',width:70},
			{field:'DebugFee',title:'调试费',width:70},
			{field:'OtherFee',title:'其他费',width:70},
			{field:'TotalFee',title:'总费用',width:70},
			{field:'LastFeeAssignerName',title:'上次费用分配人',width:80},
			{field:'LastFeeAssignTime',title:'上次费用分配时间',width:80}
		]],
		pagination:false,
		rownumbers:true	,
		toolbar:[{
			text:'注销',
			iconCls:'icon-remove',
			handler:function(){
				var row = $("#allot_info_table").datagrid('getSelected');
				if(row){
					var result = confirm("该操作不可逆，您确定要注销该任务吗？");
					if(result == false){
						return false;
					}
					$.ajax({
							type: "post",
							url: "/jlyw/TaskAssignServlet.do?method=9",
							data: {"TaskId":row.TaskId},
							dataType: "json",	//服务器返回数据的预期类型
							beforeSend: function(XMLHttpRequest){
								//ShowLoading();
							},
							success: function(data, textStatus){
								if(data.IsOK){
									$('#allot_info_table').datagrid('reload');
									//加载原始记录信息
									$('#OriginalRecord').datagrid('reload');
										
									//加载转包业务信息
									$('#subcontract_info_table').datagrid('reload');
								}else{
									$.messager.alert('注销失败！',data.msg,'error');
								}
							},
							complete: function(XMLHttpRequest, textStatus){
								//HideLoading();
							},
							error: function(){
								//请求出错处理
							}
					});
				}else{
					$.messager.alert('提示！',"请先选择一条记录",'info');
				}
			}
		},'-',{
			text:'添加检验员',
			iconCls:'icon-add',
			handler:function(){
				$("#task_assign_window").window('open');
			}
		}],
		onSelect:function(rowIndex, rowData){
			//load 表单
			$('#FeeSubmitForm').form('load', rowData);
		}	
	});
	$('#OriginalRecord').datagrid({
		title:'原始记录信息',
		height:250,
		singleSelect:true, 
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
				{title:'检验信息',colspan:7,align:'center'},
				{title:'费用信息',colspan:7,align:'center'}
			],[
				{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
				{field:'Staff',title:'检/校人员',width:80,align:'center'},
				{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
				{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
					formatter:function(value, rowData, rowIndex){
						if(value==""){
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
								return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
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
	$('#subcontract_info_table').datagrid({
		title:'转包业务信息',
//		iconCls:'icon-save',
//		width:900,
		height:200,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//		collapsible:true,
		url:'/jlyw/SubContractServlet.do?method=3',
//		sortName: 'userid',
// 		sortOrder: 'desc',
		remoteSort: false,
		idField:'SubContractId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{title:'转包方信息',colspan:3,align:'center'},
				{title:'转包业务信息',colspan:8,align:'center'}
			],[
				{field:'SubContractorName',title:'转包方',width:80,sortable:true,align:'center'},
				{field:'SubContractorContactor',title:'联系人',width:60,align:'center'},
				{field:'SubContractorContactorTel',title:'联系电话',width:80,align:'center'},
				{field:'SubContractDate',title:'转包时间',width:80,align:'center'},
				{field:'Handler',title:'转包人',width:80,align:'center'},
				{field:'TotalFee',title:'转包费用',width:80,align:'center'},
				{field:'ReceiveDate',title:'接收时间',width:80,align:'center'},
				{field:'Receiver',title:'接收人',width:80,align:'center'},
				{field:'Remark',title:'备注信息',width:100,align:'center'},
				{field:'LastEditor',title:'最后编辑人',width:80,align:'center'},
				{field:'LastEditTime',title:'最后编辑时间',width:80,align:'center'}
			]
		],
		pagination:false,
		rownumbers:true	
	});
});
function doLoadCommissionSheet(){	//查找委托单
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#allot_info_table').datagrid('options').queryParams={'CommissionId':''};
			$('#allot_info_table').datagrid('loadData',{total:0,rows:[]});
			
			$("#CommissionSheetId").val();
			$("#ProjectSelect").combobox('clear');
			$("#ProjectSelect").combobox('loadData',[]);
			$("#AlloteeSelect").combobox('clear');
			$("#AlloteeSelect").combobox('loadData',[]);
			
			//$("#Ness").removeAttr("checked");	//去勾选
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				if(result.CommissionObj.CommissionStatus >= 3){
					$.messager.alert('提示！',"该委托单不能进行费用分配",'info');
					return false;
				}
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//勾选
				}
				
				//加载任务分配信息
				$('#allot_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#allot_info_table').datagrid('reload');
				
				//加载原始记录信息
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#OriginalRecord').datagrid('reload');
					
				//加载转包业务信息
				$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#subcontract_info_table').datagrid('reload');
				
				
				
				$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
				//加载检验项目及检验人员信息
				$("#ProjectSelect").combobox('reload','/jlyw/TaskAssignServlet.do?method=2&CommissionId='+$('#CommissionId').val());
				
			}else{
				$.messager.alert('查询失败！',result.msg,'error');
			}
		}
	});  
}
function doTaskAllotee(){   	//提交任务分配表单
	$("#TaskAlloteeForm").form('submit', {
		url:'/jlyw/TaskAssignServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			//判断选择的检验项目是否有效
			var projectValue = $('#ProjectSelect').combobox('getValue');
			if(projectValue==''){
				$.messager.alert('提示！',"请选择一个有效的检验项目",'info');
				return false;
			}
			//判断选择的人员是否有效
			var alloteeValue = $('#AlloteeSelect').combobox('getValue');
			if(alloteeValue == ''){
				$.messager.alert('提示！',"请选择一个有效的检验人员",'info');
				return false;
			}
			var alloteeChecked = false;
			var alloteeAllData = $('#AlloteeSelect').combobox('getData');
			if(alloteeAllData != null && alloteeAllData.length > 0){
				for(var i=0; i<alloteeAllData.length; i++)
				{
					if(alloteeValue==alloteeAllData[i].id){
						alloteeChecked = true;
						break;
					}
				}
			}
			if(!alloteeChecked){
				$.messager.alert('提示！',"请选择一个有效的检验人员",'info');
				return false;
			}
			
			
			//判断检验项目是否存在
			var allTask = $('#allot_info_table').datagrid('getRows');
			for(var i=0; i<allTask.length; i++){
				if(projectValue == allTask[i].AppStdNameProTeamId && alloteeValue == allTask[i].AlloteeId){
					$.messager.alert('提示！',"一个检验项目不能重复分配给同一个人，如需要重新分配，请先注销已分配的检验项目！",'info');
					return false;
				}
			}
			return $("#TaskAlloteeForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$('#task_assign_window').window('close');
				//重新加载任务分配信息
				$('#allot_info_table').datagrid('reload');
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});  
} 
function doFeeSubmit(){   	//提交费用分配
	$("#FeeSubmitForm").form('submit', {
		url:'/jlyw/TaskAssignServlet.do?method=10',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			//判断选择的检验项目是否有效
			var taskIdValue = $('#TaskId').val();
			if(taskIdValue==''){
				$.messager.alert('提示！',"请选择一条检验项目",'info');
				return false;
			}
			return $("#FeeSubmitForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//重新加载任务分配信息
				$('#allot_info_table').datagrid('reload');
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
			<jsp:param name="TitleName" value="费用分配" />
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
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
				title="委托单信息" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<table width="1000px" id="table1">
				<tr>
				  <td width="77" align="right">委托形式：</td>
				  <td width="188"  align="left">
						<select name="CommissionType" style="width:152px">
							<option value="1">送样检测</option>
							<option value="5">其它业务</option>
							<option value="6">自检业务</option>
							<option value="7">现场带回</option>
							<option value="3">公正计量</option>
							<option value="4">形式评价</option>
							<option value="2">现场检测</option>
						</select>
				  </td>
				  <td width="65" align="right">委托日期：</td>
				  <td width="650"  align="left"><input style="width:151px;" name="CommissionDate" id="CommissionDate" type="text" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">委托单位：</td>
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" style="width:152px;" /></td>
                <td width="64" align="right">&nbsp;电&nbsp;&nbsp;话：</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">&nbsp;地&nbsp;&nbsp;址：</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>
				<td width="65" align="right">邮政编码：</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">联&nbsp;系&nbsp;人：</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" /></td>
				<td align="right">手机号码：</td>
				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">证书单位：</td>
				<td align="left"><input name="SampleFrom" id="SampleFrom" type="text" /></td>
                <td align="right">开票单位：</td>
				<td align="left"><input name="BillingTo" id="BillingTo" type="text" /></td>
			</tr>
		</table><br/>
 		 <table id="table2" width="1000">
			<tr>
				<td width="77" align="right">器具名称：</td>
                <td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" /></td>
                <td width="64" align="right">型号规格：</td>
			  <td width="171"  align="left"><input id="Model" name="Model" type="text" /></td>
				<td width="64" align="right">出厂编号：</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>
				<td width="65" align="right">管理编号：</td>
			  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">制 造 厂：</td>
				<td align="left"><input id="Manufacturer" name="Manufacturer" type="text"  /></td>
				<td align="right">数&nbsp;&nbsp;&nbsp;量：</td>
				<td align="left"><input id="Quantity" name="Quantity" type="text"/>件</td>
				<td align="right">是否强检：</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option selected="selected" value="1" >非强制检定</option>
						<option value="0">强制检定</option>
					</select>
				</td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox" />加&nbsp;&nbsp;急</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">外观附件：</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text"  /></td>
				<td align="right">其他要求：</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text"  /></td>
				<td align="right"></td>
				<td align="left"></td>
                <td align="left"></td>
				<td align="left">&nbsp;</td>
			</tr>
	 	 </table><br/>
		</form>
		</div><br/>
		
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;+position:relative;"
				title="检测费用分配" collapsible="false"  closable="false">
			<table id="allot_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<br />
			<form id="FeeSubmitForm" method="post">
			<input type="hidden" id="TaskId" name="TaskId" value="" />
			<table width="950">
				<tr>
				    <td width="60" align="right" >检验项目:</td>
					<td width="150" align="left"><input type="text" id="AppStdNameProTeamName" name="AppStdNameProTeamName" style="width:120px;" readonly="readonly" value="" /></td>
					<td width="60" align="right" >检验人员:</td>
					<td width="168"  align="left"><input id="AlloteeName" name="AlloteeName" type="text" style="width:120px;" readonly="readonly" value="" /></td>
					<td width="80" align="right" >检测费:</td>
					<td width="150" align="left"><input id="TestFee" name="TestFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
					<td width="60" align="right" >修理费:</td>
					<td width="150"  align="left"><input id="RepairFee" name="RepairFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
				</tr>
				<tr>
					<td align="right" >材料费:</td>
					<td align="left"><input id="MaterialFee" name="MaterialFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
					<td align="right" >交通费:</td>
					<td align="left"><input id="CarFee" name="CarFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
					<td align="right" >调试费:</td>
					<td align="left"><input id="DebugFee" name="DebugFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
					<td align="right" >其他费:</td>
					<td align="left"><input id="OtherFee" name="OtherFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
				</tr>
				<tr>
					<td colspan="4" align="right" style="padding-top:15px;"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doFeeSubmit()">录入费用信息</a>&nbsp;&nbsp;</td>
					<td colspan="4" align="left">  </td>
				</tr>
			 </table>
		  </form>
		</div><br/>
		<table id="OriginalRecord" iconCls="icon-tip" width="1000px" height="250px"></table><br/>
		<table id="subcontract_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>

</DIV></DIV>
<div id="task_assign_window" class="easyui-window" closed="true" modal="true" title="添加检验人员" iconCls="icon-save" style="width:550px;height:200px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form id="TaskAlloteeForm" method="post">
				<input type="hidden" id="CommissionSheetId" name="CommissionSheetId" value="" />
				<br/>检验项目：<select id="ProjectSelect" name="ProjectSelect" style="width:152px"></select>&nbsp;&nbsp;
				检验人员：<select id="AlloteeSelect" class="easyui-combobox" name="AlloteeSelect" style="width:152px;" valueField="id" textField="name" panelHeight="150" ></select>
			</form>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doTaskAllotee()" >确定</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#task_assign_window').window('close');">取消</a>
		</div>
	</div>
</div>
</body>
</html>
