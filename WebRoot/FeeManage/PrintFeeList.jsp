<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>打印费用清单</title>
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
		
		title:'费用清单信息',
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
			{field:'DetailListCode',title:'费用清单号',width:100,align:'center',sortable:true},
			{field:'DetailListStatus',title:'费用清状态',width:100,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['DetailListStatus']=0;
						return '<span style="color:red;">未结账</span>';
					}
					else
					{
						rowData['DetailListStatus']=2;
						return "已结账";
					}
					
				}},
			{field:'Customer',title:'委托单位',width:180,align:'center',sortable:true},
			{field:'DetailListLastEditTime',title:'清单最后编辑时间',width:160,align:'center',sortable:true},
			{field:'TotalFee',title:'总检验费用',width:100,align:'left',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'检验费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:100,align:'right',formatter:function(val,rec){
					
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
			clickname="对应清单号'"+DetailListCode+"' 的委托单信息";
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
		
		title:'费用清单对应的委托单信息',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=3',
		remoteSort: false,
		idField:'id',
		
		columns:[[
			{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
			{field:'Status',title:'委托单状态',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'TotalFee',title:'总检验费用',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'检验费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}}
			
			
			
		]],
		pagination:true,
		rownumbers:true,
		toolbar:[{
			text:'预览费用清单',
			iconCls:'icon-print',
			handler:function(){
		
				if($('#DetailListId').val() == ''){
					$.messager.alert('提示','请选择需要查看的清单','error');
				}
				else{
					$('#formLook').submit();
				}
				
			}
		},'-',{
			text:'预览费用详单',
			iconCls:'icon-print',
			handler:function(){
		
				if($('#DetailListId1').val() == ''){
					$.messager.alert('提示','请选择需要查看的清单','error');
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
			<jsp:param name="TitleName" value="打印费用清单" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td  align="right" width="32%" >
					费用清单编号：<input id="FeeCode" class="easyui-validatebox" name="FeeCode" style="width:150px;" />
					</td>
					<td  align="right" width="30%" >			
					委托单位：<select name="CustomerName" id="CustomerName" style="width:150px"/>
					</td>
					<td  align="right" width="30%" >		
					委托单编号：<input id="Code" class="easyui-validatebox" name="Code" style="width:120px;" />
					 </td>
					
				</tr >
				<tr >
					<td  align="right" width="32%" >	
					委托单起始时间：<input name="DateFrom" id="DateFrom" type="text" style="width:154px;" class="easyui-datebox" />
					</td>
					<td  align="right" width="30%" >		
					委托单结束时间：<input name="DateEnd" id="DateEnd" type="text" style="width:150px;" class="easyui-datebox" />
					</td>
					<td  align="center" width="30%" >		
					<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryDeList()">查询</a>                    </td>
					
				</tr >
		</table>
		</form>
		</div> 
		<table id="table4" style="height:350px; width:1000px"><!--清单信息-->
		</table>
		<table id="table6" style="height:350px; width:1000px"><!--委托单信息-->
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
