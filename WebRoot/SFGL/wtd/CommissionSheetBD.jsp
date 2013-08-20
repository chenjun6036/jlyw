<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>����ί�е�</title>
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



function doPrintCommissionSheet(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	
	for(var i=0; i<rows.length; i++){
		Preview1(rows[i].PrintObj);
	}
}
function doPrintLabel(){  //�򿪴�ӡ��ǩ����
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	if(rows.length>1){
		$.messager.alert('��ʾ',"ֻ��ѡ���ӡһ��ί�е������߱�ǩ��",'info');
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


function doPrintLabelSubmit(){  //��ӡ��ǩ
	if(!$("#LabelPrint_form").form('validate'))
		return false ;
	var rows = $("#table6").datagrid("getSelections");	
	
	if(getInt($('#RangeFrom').val())<1||getInt($('#RangeFrom').val())>getInt($('#RangeEnd').val())||getInt($('#RangeEnd').val())>rows[0].PrintObj.Quantity){
		$.messager.alert('��ʾ',"��ӡ���߷�Χ����ȷ��",'info');
		return false;
	}
	if(getInt($('#RangeFrom').val())==getInt($('#RangeEnd').val())&&getInt($('#AttachmentNum').val())>1){
		
	}
				
	var result = confirm("��ȷ��Ҫ���д˲�����");
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
			<jsp:param name="TitleName" value="����ί�е�" />
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
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintCommissionSheet()" title="��ӡί�е�">��ӡί�е�</a><a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintLabel()" >��ӡ��ǩ</a>
				</td>
				<td style="text-align:right;padding-right:2px">
					<label>�������ƣ�</label><input type="text" id="History_ApplianceName" value="" style="width:120px" />&nbsp;<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;<label>��ֹʱ�䣺</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��ѡί�е�λ����ʷ�ͼ��¼" id="btnHistorySearch" onclick="doLoadHistoryCommission()">��ѯ��ʷ��¼</a>
				</td>
			</tr>
		</table>
	</div>
	
	
<div id="LabelPrint_window" class="easyui-window" modal="true" title="ѡ���ӡ��Χ" style="width:350px; padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="LabelPrint_form" >
<table>
	<tr>
	  <td>
	  <div style="height:30px">
	  ���߷�Χ��<input class="easyui-numberbox" id="RangeFrom" name="RangeFrom" type="text" style="width:40px" required="true"/>--<input class="easyui-numberbox" id="RangeEnd" name="RangeEnd" type="text" style="width:40px" required="true"/>&nbsp;&nbsp;
	  &nbsp;&nbsp;��������<input class="easyui-numberbox" id="AttachmentNum" name="AttachmentNum" type="text" style="width:40px" required="true"/>
	  </div>
	  </td>
 </tr>
 <tr>
	   <td align="center" height="45px" valign="bottom">
	  <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-print"  href="javascript:void(0)" onclick="doPrintLabelSubmit()">ȷ�ϴ�ӡ</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#LabelPrint_window').window('close');" >�ر�</a>
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
