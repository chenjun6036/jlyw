<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���ɷ����嵥</title>
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
	$('#table5').datagrid({
		title:'ѡ�е�ί�е���Ϣ',
		width:1000,
		height:300,
		showFooter:true,
		singleSelect:false, 
		nowrap: false,
		striped: true,
		sortName: 'ApplianceCode',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
			{field:'ApplianceName',title:'��������',width:80,align:'center'},
			{field:'CommissionDate',title:'ί������',width:80,align:'center'},
			{field:'Status',title:'ί�е�״̬',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'TotalFee',title:'�ܼ������',width:60,align:'left',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			},
			{field:'TestFee',title:'����',width:60,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			},
			{field:'RepairFee',title:'ά�޷�',width:60,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			},
			{field:'MaterialFee',title:'���Ϸ�',width:60,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			},
			{field:'CarFee',title:'��ͨ��',width:60,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			},
			{field:'DebugFee',title:'���Է�',width:60,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			},
			{field:'OtherFee',title:'������',width:60,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}
			}
			
			

		]],
		pagination:false,
		rownumbers:true,
		toolbar:[{
			text:'ɾ��',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("��ȷ��Ҫ�Ƴ���Щί�е���Ϣ��");
				if(result == false){
					return false;
				}
				var rows = $('#table5').datagrid('getSelections');
				var length = rows.length;
				for(var i=length-1; i>=0; i--){
					var index = $('#table5').datagrid('getRowIndex', rows[i]);
					$('#table5').datagrid('deleteRow', index);
				}
			}
		},'-',{
			text:'�����嵥',
			iconCls:'icon-save',
			handler:function(){
				doDenerate();
			}
		},'-',{
			text:'��ӡ�����嵥',
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
			text:'��ӡ�����굥',
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
	
	
	$('#table6').datagrid({
		width:1000,
		height:500,
		
		title:'���깤ȷ�ϵ�ί�е���Ϣ',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=0',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
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
			{field:'TotalFee',title:'�ܼ������',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'�����',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'ά�޷�',width:60,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'���Ϸ�',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'��ͨ��',width:60,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'���Է�',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'������',width:60,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}}
			
			

		]],
		pagination:true,
		rownumbers:true,
		rowStyler:function(rowIndex, rowData){
			if(rowData.GenerateTag == 1){	//�����ɹ��嵥
				return 'color:#FF0000'
			}else{
				return 'color:#000000';
			}
		},
		toolbar:"#table6-search-toolbar"
	});
		
	var nowDate = new Date();
	$("#CommissionDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	$('#Code1').val("");
	$('#Code').val("");
	$('#CommissionSheetId').val("");
});



function AddRecordFromHistory(){
	var rows = $('#table6').datagrid('getSelections');
	
	var row=$('#table5').datagrid('getRows');
	var index=row.length;
	for(var i=0; i<rows.length; i++){
		var j;
		
		for(j=0; j<index; j++)
		{
			if(rows[i].Code==row[j].Code)
			{
				$.messager.alert('��ʾ','ѡ�������е�ί�е���','warning');
				
				break;
			}
			if(rows[i].CustomerName!=row[j].CustomerName)
			{
				$.messager.alert('��ʾ','��ѡί�е�������ͬһ��ί�е�λ��','warning');
				
				break;
			}
		}		
		if(index!=j)
			continue;
		if(rows[i].Status!=3){
			$.messager.alert('��ʾ','��ί�е���δ�깤ȷ�ϣ�','warning');
			break;
		}
		
		$('#table5').datagrid('insertRow', {
			index: index,
			row:{
				Id:rows[i].Id,
				CustomerName:rows[i].CustomerName,
				Code:rows[i].Code,
				ApplianceName:rows[i].ApplianceName,
				TestFee:rows[i].TestFee,
				Status:rows[i].Status,
				RepairFee:rows[i].RepairFee,
				MaterialFee:rows[i].MaterialFee,
				CarFee:rows[i].CarFee,
				DebugFee:rows[i].DebugFee,
				OtherFee:rows[i].OtherFee,
				TotalFee:rows[i].TotalFee,
				CommissionDate:rows[i].CommissionDate
			
				
			}
		});
		index++;
	}
	$('#table6').datagrid('clearSelections');
}
function doLoadHistoryCommission()
{
	
	$('#table6').datagrid('options').url='/jlyw/DetailListComServlet.do?method=0';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code1').val(),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}

function doDenerate(){
	var rows = $("#table5").datagrid("getRows");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��ί�е���",'info');
		return false;
	}
	var result = confirm("��ȷ��Ҫ���������嵥��?");
	if(result == false){
		return false;
	}
	
	$('#DetailList').val("");
	$("#DetailList").val(JSON.stringify(rows));
 
    $('#Generate').form('submit',{
		url: '/jlyw/DetailListComServlet.do?method=1',
		onSubmit:function(){ return $('#Genarate').form('validate');},
		success:function(data){
		   var result = eval("("+data+")");
		  // alert(result.DetailListId);
		  if(result.IsOK){
		   	$('#DetailListId').val(result.DetailListId);
			$('#DetailListId1').val(result.DetailListId);	
			$('#DetailListId2').val(result.DetailListId);	
		  }		
		  $.messager.alert('��ʾ',result.msg,'info');
		 }
	});
}


</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="���ɷ����嵥" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;" title="��ѯ����" collapsible="false"  closable="false">
			<form id="SearchForm" method="post">
				<table width="850px" id="table1">
					<tr>
						<td width="60%" align="right" >
						ί�е���ţ�<input id="Code1" class="easyui-validatebox" name="Code" style="width:150px;" />&nbsp;
						ί�е�λ��<select name="CustomerName" id="CustomerName" style="width:200px" ></select>&nbsp;
						</td >
						
						<td align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryCommission()">��ѯ</a>  &nbsp;&nbsp;���ɵ��嵥�ţ�<input id="DetailListId2" name="DetailListId"  />
						</td>
						
					</tr  >
			   </table>
			
			</form>
		</div>
		 
	<table id="table6" style="height:300px; width:1000px">
	</table>
	
	<div id="table6-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-ok" name="print" onclick="AddRecordFromHistory()" title="�ύѡ�е�ί�е�">�ύѡ�е�ί�е�</a>
				</td>
				<td style="text-align:right;padding-right:2px">
					<label>�������ƣ�</label><input type="text" id="History_ApplianceName" value="" style="width:120px" />&nbsp;<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;<label>��ֹʱ�䣺</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��ѡί�е�λ����ʷ�ͼ��¼" id="btnHistorySearch" onclick="doLoadHistoryCommission()">��ѯ��ʷ��¼</a>
				</td>
			</tr>
		</table>
	</div>
	<table id="table5" style="height:300px; width:1000px" >
	</table>
	
	<form id="Generate" method="post">
	<input type="hidden" name="DetailList" id="DetailList" value="" />
  	</form>
	
	<form id="formLook" method="post" action="/jlyw/DetailListComServlet.do?method=5" target="FeePrintFrame">
			
		<input id="DetailListId" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
	</form>
	<form id="formLook1" method="post" action="/jlyw/DetailListComServlet.do?method=6" target="FeePrintFrame1">
		
		<input id="DetailListId1" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
	</form>
	<iframe id="FeePrintFrame" name="FeePrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	<iframe id="FeePrintFrame1" name="FeePrintFrame1" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	
	
	</DIV>
</DIV>
</body>
</html>
