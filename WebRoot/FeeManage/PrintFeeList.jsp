<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ӡ�����嵥</title>
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
<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
<script type="text/javascript" src="../JScript/StatusInfo.js"></script>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/jlyw/WebPrint/install_lodop.exe"></embed>
</object> 
<script type="text/javascript" src="../WebPrint/printer.js"></script>
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
	
	

	var lastIndex;
	
	$('#table4').datagrid({
		width:1000,
		height:350,
		
		title:'�����嵥��Ϣ',
		singleSelect:true, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=2&DetailListId=""',
		sortName: 'DetailListLastEditTime',
		remoteSort: false,
		sortOrder:'dec',
		idField:'DetailListId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DetailListCode',title:'�����嵥��',width:100,align:'center',sortable:true},
			{field:'DetailListStatus',title:'������״̬',width:100,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['DetailListStatus']=0;
						return '<span style="color:red;">δ����</span>';
					}
					else
					{
						rowData['DetailListStatus']=2;
						return "�ѽ���";
					}
					
				}},
			{field:'Customer',title:'ί�е�λ',width:180,align:'center',sortable:true},
			{field:'DetailListLastEditTime',title:'�嵥���༭ʱ��',width:160,align:'center',sortable:true},
			{field:'TotalFee',title:'�ܼ������',width:100,align:'left',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'�����',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'ά�޷�',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'���Ϸ�',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'��ͨ��',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'���Է�',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'������',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}}
			
			
		]],
		pagination:true,
		rownumbers:true,
		
		onDblClickRow:function(rowIndex, rowData)
		{
			
		},
		onSelect:function(rowIndex, rowData)		
		{
			var DetailListId=rowData.DetailListId;
			var DetailListCode=rowData.DetailListCode;
			clickname="��Ӧ�嵥��'"+DetailListCode+"' ��ί�е���Ϣ";
			//$('#table6').datagrid({title:clickname});

			$('#table6').datagrid('options').url='/jlyw/DetailListComServlet.do?method=3';
			$('#table6').datagrid('options').queryParams={'DetailListCode':DetailListCode};
			$('#table6').datagrid({title:clickname});
			//$('#table6').datagrid('reload');
			$('#DetailListId').val(DetailListId);
			$('#DetailListId1').val(DetailListId);				
		}
	});
	
	$('#table6').datagrid({
		width:1000,
		height:350,
		
		title:'�����嵥��Ӧ��ί�е���Ϣ',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=3',
		remoteSort: false,
		idField:'id',
		
		columns:[[
			{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
			{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
			{field:'CommissionDate',title:'ί������',width:80,align:'center'},
			{field:'Status',title:'ί�е�״̬',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'TotalFee',title:'�ܼ������',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'�����',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'ά�޷�',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'���Ϸ�',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'��ͨ��',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'���Է�',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'������',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}}
			
			
			
		]],
		pagination:true,
		rownumbers:true,
		toolbar:[{
			text:'Ԥ�������嵥',
			iconCls:'icon-print',
			handler:function(){
		
				if($('#DetailListId').val() == ''){
					$.messager.alert('��ʾ','��ѡ����Ҫ�鿴���嵥','error');
				}
				else{
					$('#formLook').submit();
				}
				
			}
		},'-',{
			text:'Ԥ�������굥',
			iconCls:'icon-print',
			handler:function(){
		
				if($('#DetailListId1').val() == ''){
					$.messager.alert('��ʾ','��ѡ����Ҫ�鿴���嵥','error');
				}
				else{
					$('#formLook1').submit();
				}
				
			}
		}]
	});
	
	$('#Code').val("");
	$('#CommissionSheetId').val("");
});


function doLoadHistoryDeList()
{
	
	$('#table4').datagrid('options').url='/jlyw/DetailListComServlet.do?method=2';
	$('#table4').datagrid('options').queryParams={'FeeCode':$('#FeeCode').val(),'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue'))};
	$('#table4').datagrid('reload');
}


</script>
</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ӡ�����嵥" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td  align="right" width="32%" >
					�����嵥��ţ�<input id="FeeCode" class="easyui-validatebox" name="FeeCode" style="width:150px;" />
					</td>
					<td  align="right" width="30%" >			
					ί�е�λ��<select name="CustomerName" id="CustomerName" style="width:150px"/>
					</td>
					<td  align="right" width="30%" >		
					ί�е���ţ�<input id="Code" class="easyui-validatebox" name="Code" style="width:120px;" />
					 </td>
					
				</tr >
				<tr >
					<td  align="right" width="32%" >	
					ί�е���ʼʱ�䣺<input name="DateFrom" id="DateFrom" type="text" style="width:154px;" class="easyui-datebox" />
					</td>
					<td  align="right" width="30%" >		
					ί�е�����ʱ�䣺<input name="DateEnd" id="DateEnd" type="text" style="width:150px;" class="easyui-datebox" />
					</td>
					<td  align="center" width="30%" >		
					<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryDeList()">��ѯ</a>                    </td>
					
				</tr >
		</table>
		</form>
		</div> 
		<table id="table4" style="height:350px; width:1000px"><!--�嵥��Ϣ-->
		</table>
		<table id="table6" style="height:350px; width:1000px"><!--ί�е���Ϣ-->
		</table>
		<form id="formLook" method="post" action="/jlyw/DetailListComServlet.do?method=5" target="FeePrintFrame" >
			
			<input id="DetailListId" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
			<input id="DetailListType" name="DetailListType"  style="width:152px;" type="hidden" value="1"/>
		</form>
		<form id="formLook1" method="post" action="/jlyw/DetailListComServlet.do?method=6" target="FeePrintFrame1">
			
			<input id="DetailListId1" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
			<input id="DetailListType" name="DetailListType"  style="width:152px;" type="hidden" value="1"/>
		</form>
		<iframe id="FeePrintFrame" name="FeePrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
		<iframe id="FeePrintFrame1" name="FeePrintFrame1" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	</DIV>
</DIV>
</body>
</html>
