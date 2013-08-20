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
			{field:'TestFee',title:'�����',width:100,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				},editor:"numberbox"
			},
			{field:'RepairFee',title:'ά�޷�',width:100,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				},editor:"numberbox"
			},
			{field:'MaterialFee',title:'���Ϸ�',width:100,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				},editor:"numberbox"
			},
			{field:'CarFee',title:'��ͨ��',width:100,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				},editor:"numberbox"
			},
			{field:'DebugFee',title:'���Է�',width:100,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				},editor:"numberbox"
			},
			{field:'OtherFee',title:'������',width:100,align:'right',
				formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				},editor:"numberbox"
			},
			{field:'TotalFee',title:'�ܼ������',width:100,align:'left',formatter:function(val,rec){
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
			text:'��ɱ༭',
			iconCls:'icon-save',
			handler:function(){
				var rowData = $('#table5').datagrid('getSelected');
				var index = $('#table5').datagrid('getRowIndex',rowData);
				//var edit = $('#table5').datagrid('getEditor',{'index':index,'field':'TotalFee'});
				
				TotalFee=getFloat(rowData.TestFee)+getFloat(rowData.RepairFee)+getFloat(rowData.MaterialFee)+getFloat(rowData.CarFee)+getFloat(rowData.DebugFee)+getFloat(rowData.OtherFee);
				
				//alert($('#table5').datagrid('getValue',edit));
				//$('#table5').datagrid('setValue',edit,TotalFee);
				$('#table5').datagrid('acceptChanges');
			}
		},'-',{
			text:'ѡ��δ����ļ�¼',
			iconCls:'icon-redo',
			handler:function(){
				var rows = $("#table5").datagrid("getRows");
				for(var i = 0; i<rows.length; i++){
					if(rows[i].CommissionNumber != null && rows[i].CommissionNumber.length > 0){
						$("#table5").datagrid("unselectRow",i);
					}else{
						$("#table5").datagrid("selectRow",i);
					}
				}	
		
			}
		},'-',{
			text:'�����嵥',
			iconCls:'icon-save',
			handler:function(){
				doDenerate();
			}
		}],
		onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		},
		onAfterEdit:function(index,rowData,changes){
			var TotalFee=getFloat(rowData.TestFee)+getFloat(rowData.RepairFee)+getFloat(rowData.MaterialFee)+getFloat(rowData.CarFee)+getFloat(rowData.DebugFee)+getFloat(rowData.OtherFee);
			var allRows = $(this).datagrid("getRows");
			if(index != null && index >= 0 && index < allRows.length){
				var vRow = allRows[index];
				vRow.TotalFee = TotalFee;
				$(this).datagrid("updateRow", {index:index, row:vRow});
			}

			$('#this').datagrid('refreshRow', index);   
		},
		onClickRow:function(rowIndex){
			
//					if (lastIndex != rowIndex){
				$('#table5').datagrid('endEdit', lastIndex);
				$('#table5').datagrid('beginEdit', rowIndex);
//					}
			lastIndex = rowIndex;
		}

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
		idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
			{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
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
				}},
			{field:'TotalFee',title:'�ܼ������',width:100,align:'left',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			
			{field:'CommissionDate',title:'ί������',width:80,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
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
		if(rows[i].Status!=3){
			$.messager.alert('��ʾ','��ί�е���δ�깤ȷ�ϣ�','warning');
			break;
		}
			
		for(j=0; j<index; j++)
		{
			if(rows[i].Code==row[j].Code)
			{
				$.messager.alert('��ʾ','ѡ�������е�ί�е���','warning');
				break;
			}
		}
		
		if(index!=j)
			continue;
		$('#table5').datagrid('insertRow', {
			index: index,
			row:{
				Id:rows[i].Id,
				CustomerName:rows[i].CustomerName,
				Code:rows[i].Code,
				ApplianceName:rows[i].ApplianceName,
				TestFee:rows[i].TestFee,
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
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}

function doDenerate(){
	var rows = $("#table6").datagrid("getRows");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ�깤ȷ�ϵ�ί�е���",'info');
		return false;
	}
	var result = confirm("��ȷ��Ҫ�����깤ȷ����?");
	if(result == false){
		return false;
	}
	$('#DetailList').val("");
	var lists;
	for(var i=0; i<rows.length; i++){
		lists = lists + rows[i].Id + "|";
	}
	$('#DetailList').val(lists);
	 $('#Genarate').form('submit',{
		//url: '/jlyw/CommissionSheetServlet.do?method=5',
		onSubmit:function(){ return $('#Confirm').form('validate');},
		success:function(data){
		   var result = eval("("+data+")");
		   
		   alert(result.msg);
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
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code1" class="easyui-validatebox" name="Code" style="width:150px;" />
					</td>

					<td width="10%" align="right">ί�е�λ��</td>
					<td width="22%" align="left">
						<select name="CustomerName" id="CustomerName" style="width:125px">
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
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="AddRecordFromHistory()" title="�ύѡ�е�ί�е�">�ύѡ�е�ί�е�</a>
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
	
	
	
	</DIV>
</DIV>
</body>
</html>
