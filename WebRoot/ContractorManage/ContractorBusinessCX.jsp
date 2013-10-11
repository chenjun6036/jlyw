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
			title:'ת��ҵ���ѯ',
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
				{field:'Code',title:'ί�е���',width:100,align:'center'},
				{field:'SubContractorName',title:'ת������λ����',width:180,align:'center'},
				{field:'SubContractorContactor',title:'ת������ϵ��',width:80,align:'center'},
				{field:'SubContractorContactorTel',title:'��ϵ�˵绰',width:80,align:'center'},
				{field:'SubContractDate',title:'ת��ʱ��',width:80,align:'center'},
				{field:'Handler',title:'ת����',width:80,align:'center'},
				{field:'TotalFee',title:'ת������',width:80,align:'center'},
				{field:'ReceiveDate',title:'����ʱ��',width:80,align:'center'},
				{field:'Receiver',title:'������',width:80,align:'center'},
				{field:'Remark',title:'��ע',width:80,align:'center'},
				{field:'Attachment',title:'����',width:80,align:'center'}
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
