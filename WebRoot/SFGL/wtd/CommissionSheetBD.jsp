<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>补打委托单</title>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
<link href="../../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />

<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src="../../Inc/JScript/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="../../JScript/json2.js"></script>
<script type="text/javascript" src="../../JScript/RandGenerator.js"></script>
<script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
<script type="text/javascript" src="../../JScript/NumberChanger.js"></script>

<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>
<script type="text/javascript" src="../../WebPrint/printer.js"></script>
<script type="text/javascript" src="../../WebPrint/printCommisionSheet.js"></script>

<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
 
<script>
$(function(){
	$("#CustomerName").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
			$("#CustomerTel").val(record.tel);
			$("#CustomerAddress").val(record.address);
			$("#CustomerZipCode").val(record.zipCode);
			$("#ContactPerson").val(record.contactor);
			$("#ContactorTel").val(record.contactorTel);
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
	var products = [
		{id:0,name:'强制检定'},
		{id:1,name:'非强制检定'}
	];
	var products1 = [
		{id:0,name:'是'},
		{id:1,name:'否'}
	];
	var products2 = [
		{id:0,name:'需要修理'},
		{id:1,name:'无需修理'}
	];
	var products3 = [
		{id:1,name:'检定'},
		{id:2,name:'校准'},
		{id:3,name:'检验'}
	];
	var lastIndex;
	
	
	$('#table6').datagrid({
		width:1000,
		height:500,
		title:'委托单信息',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/CommissionSheetServlet.do?method=8',
		remoteSort: false,
		//idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
			{field:'Status',title:'委托单状态',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
			{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
			{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
			{field:'Model',title:'型号规格',width:80,align:'center'},
			{field:'Range',title:'测量范围',width:80,align:'center'},
			{field:'Accuracy',title:'精度等级',width:80,align:'center'},
			{field:'Manufacturer',title:'制造厂商',width:80,align:'center'},
			{field:'Quantity',title:'台/件数',width:70,align:'center'},
			{field:'MandatoryInspection',title:'强制检验',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['MandatoryInspection']=0;
						return "强制检定";
					}
					else
					{
						rowData['MandatoryInspection']=1;
						return "非强制检定";
					}
					
				}},
			{field:'Urgent',title:'是否加急',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Urgent']=0;
						return "是";
					}
					else
					{
						rowData['Urgent']=1;
						return "否";
					}
					
				}},
			{field:'Trans',title:'是否转包',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Trans']=0;
						return "是";
					}
					else
					{
						rowData['Trans']=1;
						return "否";
					}
					
				}},
			{field:'SubContractor',title:'转包方',width:80,align:'center'},
			{field:'Appearance',title:'外观附件',width:80,align:'center'},
			{field:'Repair',title:'需修理否',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Repair']=0;
						return "需要修理";
					}
					else
					{
						rowData['Repair']=1;
						return "无需修理";
					}
					
				}},
			{field:'ReportType',title:'报告形式',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 1 || value == '1')
					{
						rowData['ReportType']=1;
						return "检定";
					}
					if(value == 2 || value == '2')
					{
						rowData['ReportType']=2;
						return "校准";
					}
					else
					{	rowData['ReportType']=3;
						return "检验";
					}
				}},
			{field:'OtherRequirements',title:'其他要求',width:80,align:'center'},
			{field:'Location',title:'存放位置',width:80,align:'center'},
			{field:'Allotee',title:'派定人',width:80,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:"#table6-search-toolbar"
	});
	
});



function doPrintCommissionSheet(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
		return false;
	}
	
	for(var i=0; i<rows.length; i++){
		Preview1(rows[i].PrintObj);
	}
}
function doPrintLabel(){  //打开打印标签窗口
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
		return false;
	}
	if(rows.length>1){
		$.messager.alert('提示',"只能选择打印一份委托单的器具标签！",'info');
		return false;	
	}
	$("#LabelPrint_form").form('clear');	
	$('#LabelPrint_window').window('open');
	if(rows.length==1){		
		$('#RangeFrom').val(1);	
		$('#RangeEnd').val(rows[0].PrintObj.Quantity);	
		$('#AttachmentNum').val(1);		
	}
}


function doPrintLabelSubmit(){  //打印标签
	if(!$("#LabelPrint_form").form('validate'))
		return false ;
	var rows = $("#table6").datagrid("getSelections");	
	
	if(getInt($('#RangeFrom').val())<1||getInt($('#RangeFrom').val())>getInt($('#RangeEnd').val())||getInt($('#RangeEnd').val())>rows[0].PrintObj.Quantity){
		$.messager.alert('提示',"打印器具范围不正确！",'info');
		return false;
	}
	if(getInt($('#RangeFrom').val())==getInt($('#RangeEnd').val())&&getInt($('#AttachmentNum').val())>1){
		
	}
				
	var result = confirm("您确定要进行此操作吗？");
	if(result == false){
		return false;
	}
	
	if(rows.length==1){
		for(var i=getInt($('#RangeFrom').val());i<=getInt($('#RangeEnd').val());i++){
			var WYID = rows[0].PrintObj.Code + "-" + i + "/" + rows[0].PrintObj.Quantity;
			//console.info(WYID);
			if(getInt($('#RangeFrom').val())==getInt($('#RangeEnd').val())&&getInt($('#AttachmentNum').val())>1){				
				for(var j=0;j<getInt($('#AttachmentNum').val());j++){
				   var tempWYID=WYID;
				   tempWYID = tempWYID + "-" + (j+1);
				   RealPrintSFS(rows[0].PrintObj,tempWYID);
				}
			}else{
				RealPrintSFS(rows[0].PrintObj,WYID);
			}
		}
	}
}

function doLoadHistoryCommission()
{
	
	$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=8';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}




</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="补打委托单" />
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
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintCommissionSheet()" title="打印委托单">打印委托单</a><a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintLabel()" >打印标签</a>
				</td>
				<td style="text-align:right;padding-right:2px">
					<label>器具名称：</label><input type="text" id="History_ApplianceName" value="" style="width:120px" />&nbsp;<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;<label>截止时间：</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询所选委托单位的历史送检记录" id="btnHistorySearch" onclick="doLoadHistoryCommission()">查询历史记录</a>
				</td>
			</tr>
		</table>
	</div>
	
	
<div id="LabelPrint_window" class="easyui-window" modal="true" title="选择打印范围" style="width:350px; padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="LabelPrint_form" >
<table>
	<tr>
	  <td>
	  <div style="height:30px">
	  器具范围：<input class="easyui-numberbox" id="RangeFrom" name="RangeFrom" type="text" style="width:40px" required="true"/>--<input class="easyui-numberbox" id="RangeEnd" name="RangeEnd" type="text" style="width:40px" required="true"/>&nbsp;&nbsp;
	  &nbsp;&nbsp;附件数：<input class="easyui-numberbox" id="AttachmentNum" name="AttachmentNum" type="text" style="width:40px" required="true"/>
	  </div>
	  </td>
 </tr>
 <tr>
	   <td align="center" height="45px" valign="bottom">
	  <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-print"  href="javascript:void(0)" onclick="doPrintLabelSubmit()">确认打印</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#LabelPrint_window').window('close');" >关闭</a>
	  </div>
	  </td>	
</tr>
</table>
</form>	
</div >	
	
	
	</DIV>
</DIV>
</body>
</html>
