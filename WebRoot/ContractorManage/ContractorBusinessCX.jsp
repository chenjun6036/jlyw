<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ת������->��ѯί�е�</title>
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
<script type="text/javascript" src="../WebPrint/printCommisionSheet.js"></script>

		
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/jlyw/WebPrint/install_lodop.exe"></embed>
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
		{id:0,name:'ǿ�Ƽ춨'},
		{id:1,name:'��ǿ�Ƽ춨'}
	];
	var products1 = [
		{id:0,name:'��'},
		{id:1,name:'��'}
	];
	var products2 = [
		{id:0,name:'��Ҫ����'},
		{id:1,name:'��������'}
	];
	var products3 = [
		{id:1,name:'�춨'},
		{id:2,name:'У׼'},
		{id:3,name:'����'}
	];
	var lastIndex;
	
	
	$('#table6').datagrid({
		width:1000,
		height:500,
		title:'ί�е���Ϣ',
		singleSelect:true, 
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
			{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
			{field:'CommissionDate',title:'ί������',width:80,align:'center'},
			{field:'Status',title:'ί�е�״̬',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
			{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
			{field:'ApplianceCode',title:'�������',width:80,align:'center'},
			{field:'AppManageCode',title:'������',width:80,align:'center'},
			{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
			{field:'Range',title:'������Χ',width:80,align:'center'},
			{field:'Accuracy',title:'���ȵȼ�',width:80,align:'center'},
			{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
			{field:'Quantity',title:'̨/����',width:70,align:'center'},
			{field:'MandatoryInspection',title:'ǿ�Ƽ���',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['MandatoryInspection']=0;
						return "ǿ�Ƽ춨";
					}
					else
					{
						rowData['MandatoryInspection']=1;
						return "��ǿ�Ƽ춨";
					}
					
				}},
			{field:'Urgent',title:'�Ƿ�Ӽ�',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Urgent']=0;
						return "��";
					}
					else
					{
						rowData['Urgent']=1;
						return "��";
					}
					
				}},
			{field:'Trans',title:'�Ƿ�ת��',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Trans']=0;
						return "��";
					}
					else
					{
						rowData['Trans']=1;
						return "��";
					}
					
				}},
			{field:'SubContractor',title:'ת����',width:80,align:'center'},
			{field:'Appearance',title:'��۸���',width:80,align:'center'},
			{field:'Repair',title:'�������',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Repair']=0;
						return "��Ҫ����";
					}
					else
					{
						rowData['Repair']=1;
						return "��������";
					}
					
				}},
			{field:'ReportType',title:'������ʽ',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 1 || value == '1')
					{
						rowData['ReportType']=1;
						return "�춨";
					}
					if(value == 2 || value == '2')
					{
						rowData['ReportType']=2;
						return "У׼";
					}
					else
					{	rowData['ReportType']=3;
						return "����";
					}
				}},
			{field:'OtherRequirements',title:'����Ҫ��',width:80,align:'center'},
			{field:'Location',title:'���λ��',width:80,align:'center'},
			{field:'Allotee',title:'�ɶ���',width:80,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:"#table6-search-toolbar"
	});
	
});

function doLoadHistoryCommission()
{
	
	$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=8';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}
function doLook(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ�鿴��ί�е���",'info');
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
			<jsp:param name="TitleName" value="ת������->��ѯί�е�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" />
					</td>

					<td width="10%" align="right">ί�е�λ��</td>
					<td width="22%" align="left">
						<select name="CustomerName" id="CustomerName" style="width:125px" ></select>
					</td>
					
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryCommission()">��ѯ</a></td>
					
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
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doLook()" title="�鿴ת����Ϣ">�鿴ת����Ϣ</a>
				</td>
				<td style="text-align:right;padding-right:2px">
					<label>�������ƣ�</label><input type="text" id="History_ApplianceName" value="" style="width:120px" />&nbsp;<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;<label>��ֹʱ�䣺</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��ѡί�е�λ����ʷ�ͼ��¼" id="btnHistorySearch" onclick="doLoadHistoryCommission()">��ѯ��ʷ��¼</a>
				</td>
			</tr>
		</table>
	</div>
	
	
	</DIV>
</DIV>
</body>
</html>
