<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>转包管理->查询委托单</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
<link href="../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />

<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src="../Inc/JScript/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="../JScript/json2.js"></script>
<script type="text/javascript" src="../JScript/RandGenerator.js"></script>
<script type="text/javascript" src="../JScript/StatusInfo.js"></script>

<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>

<script>
	$(function(){
		$("#CustomerName").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
			},
			onChange:function(newValue, oldValue){
				var allData = $(this).combobox('getData');
				if(allData != null && allData.length > 0){
					for(var i=0; i<allData.length; i++)
					{
						if(newValue==allData[i].name){
							return false;
						}
					}
				}
				$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}
		});
	
		$('#table6').datagrid({
			width:1000,
			height:500,
			title:'转包业务查询',
			singleSelect:true, 
			nowrap: false,
			striped: true,
	//				collapsible:true,
			url:'/jlyw/SubContractServlet.do?method=4',
			remoteSort: false,
			//idField:'id',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:'Code',title:'委托单号',width:100,align:'center'},
				{field:'SubContractorName',title:'转包方单位名称',width:180,align:'center'},
				{field:'SubContractorContactor',title:'转包方联系人',width:80,align:'center'},
				{field:'SubContractorContactorTel',title:'联系人电话',width:80,align:'center'},
				{field:'SubContractDate',title:'转包时间',width:80,align:'center'},
				{field:'Handler',title:'转包人',width:80,align:'center'},
				{field:'TotalFee',title:'转包费用',width:80,align:'center'},
				{field:'ReceiveDate',title:'接收时间',width:80,align:'center'},
				{field:'Receiver',title:'接收人',width:80,align:'center'},
				{field:'Remark',title:'备注',width:80,align:'center'},
				{field:'Attachment',title:'附件',width:80,align:'center'}
			]],
			pagination:true,
			rownumbers:true,
			toolbar:"#table6-search-toolbar"
		});
	
	});

function doLoadHistoryCommission()
{
	
	$('#table6').datagrid('options').url='/jlyw/SubContractServlet.do?method=4';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'DateFrom':$('#History_BeginDate').datebox('getValue'),'DateEnd':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}
function doLook(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要查看的委托单！",'info');
		return false;
	}
	
	for(var i=0; i<rows.length; i++){
		$('#DoConfirmForm-Code').val(rows[i].Code);
		$('#DoConfirmForm-Pwd').val(rows[i].Pwd);
		$('#DoConfirmForm').submit();
	}
}



</script>
</head>

<body>
<form method="post" action="/jlyw/ContractorManage/ContractorBusiness.jsp" id="DoConfirmForm">
	<input type="hidden" name="Code" id="DoConfirmForm-Code" value="" />
	<input type="hidden" name="Pwd" id="DoConfirmForm-Pwd" value="" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="转包管理->查询委托单" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" />
					</td>

					<td width="10%" align="right">委托单位：</td>
					<td width="22%" align="left">
						<select name="CustomerName" id="CustomerName" style="width:125px" ></select>
					</td>
					
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryCommission()">查询</a></td>
					
				</tr >
		</table>
		</form>
		</div> 
	<table id="table6" style="height:300px; width:1000px">
	</table>
	<div id="table6-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doLook()" title="查看转包信息">查看转包信息</a>
				</td>
				<td style="text-align:right;padding-right:2px">
					<label>器具名称：</label><input type="text" id="History_ApplianceName" value="" style="width:120px" />&nbsp;<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;<label>截止时间：</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询所选委托单位的历史送检记录" id="btnHistorySearch" onclick="doLoadHistoryCommission()">查询历史记录</a>
				</td>
			</tr>
		</table>
	</div>
	
	
	</DIV>
</DIV>
</body>
</html>
