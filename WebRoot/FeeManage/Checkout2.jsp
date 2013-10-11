<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>结账管理</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/StatusInfo.js"></script>
	<script type="text/javascript" src="../JScript/tool.js"></script>
	<script>
	
$(function(){
	
	nowDate = new Date();
	/*$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));*/
	$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	$("#CustomerName").combobox({
		valueField:'id',
		textField:'name',
		onSelect:function(record){},
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
		width:498,
		height:350,
		
		title:'费用清单信息',
		singleSelect:true, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=21&DetailListId=""',
		sortName: 'DetailListLastEditTime',
		remoteSort: false,
		sortOrder:'dec',
		idField:'DetailListId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DetailListCode',title:'费用清单号',width:100,align:'center',sortable:true},
			{field:'Customer',title:'委托单位',width:120,align:'center',sortable:true},
			{field:'TotalFee',title:'总检验费用',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},			
			{field:'TestFee',title:'检验费',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:60,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:60,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:60,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:60,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}}
			
		]],
		pagination:true,
		rownumbers:true,
		onSelect:function(rowIndex, rowData)
		{
			var DetailListId=rowData.DetailListId;
			var DetailListCode=rowData.DetailListCode;

			$('#table6').datagrid('options').url='/jlyw/DetailListComServlet.do?method=3';
			$('#table6').datagrid('options').queryParams={'DetailListCode':DetailListCode};
			$('#table6').datagrid('reload');
		}
	});
	
	$('#table7').datagrid({
		width:1000,
		height:350,
		
		title:'需结账的委托单信息',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'',
		remoteSort: false,
		//idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DetailListCode',title:'费用清单号',width:100,align:'center'},
			{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'委托单位',width:140,align:'center'},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
			{field:'Status',title:'委托单状态',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'TotalFee',title:'总检验费用',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'检验费',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:50,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:50,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:50,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'FinishLocation',title:'存放位置',width:50,align:'right'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:[{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var rows = $('#table7').datagrid('getSelections');
				var count = $('#Count').datagrid('getRows');
				var TestFee = getFloat(count[0].TestFee);
				var RepairFee = getFloat(count[0].RepairFee);
				var MaterialFee = getFloat(count[0].MaterialFee);
				var CarFee = getFloat(count[0].CarFee);
				var DebugFee = getFloat(count[0].DebugFee);
				var OtherFee = getFloat(count[0].OtherFee);
				var TotalFee = getFloat(count[0].TotalFee);
				for(var i = 0; i  < rows.length; i++){
					$('#table7').datagrid('deleteRow',$('#table7').datagrid('getRowIndex',rows[i]));
					TestFee = TestFee - getFloat(rows[i].TestFee);
					RepairFee = RepairFee - getFloat(rows[i].RepairFee);
					MaterialFee = MaterialFee - getFloat(rows[i].MaterialFee);
					CarFee = CarFee - getFloat(rows[i].CarFee);
					DebugFee = DebugFee - getFloat(rows[i].DebugFee);
					OtherFee = OtherFee - getFloat(rows[i].OtherFee);
					TotalFee = TotalFee - getFloat(rows[i].TotalFee);
				}
				$('#Count').datagrid('updateRow',{
					index:0,
					row:{
						TotalFee:TotalFee,
						TestFee:TestFee,
						RepairFee:RepairFee,
						MaterialFee:MaterialFee,
						CarFee:CarFee,
						DebugFee:DebugFee,
						OtherFee:OtherFee
					}
				});
				$('#TotalFee').val(TotalFee);
				$('#table7').datagrid('unselectAll');
			}
		},'-',{
			text:'打印费用清单',
			iconCls:'icon-print',
			handler:function(){
		
				if($('#DetailListId').val() == ''||$('#DetailListId').val().length==0){
					$.messager.alert('提示','请选择需要查看的清单','error');
				}
				else{
					$('#formLook').submit();
				}
				
			}
		},'-',{
			text:'打印费用详单',
			iconCls:'icon-print',
			handler:function(){
		
				if($('#DetailListId1').val() == ''||$('#DetailListId1').val().length==0){
					$.messager.alert('提示','请选择需要查看的清单','error');
				}
				else{
					$('#formLook1').submit();
				}
				
			}
		}]
	});
	
	$('#table6').datagrid({
		width:500,
		height:350,
		
		title:'委托单信息',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DetailListCode',title:'费用清单号',width:100,align:'center',sortable:true},
			{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'委托单位',width:160,align:'center',sortable:true},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
			{field:'Status',title:'委托单状态',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'TotalFee',title:'总检验费用',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TestFee',title:'检验费',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:50,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:50,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:50,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:50,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'FinishLocation',title:'存放位置',width:50,align:'right'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:[{
			text:'加入结账列表',
			iconCls:'icon-add',
			handler:function(){
				var rows = $('#table6').datagrid('getSelections');
				var count = $('#Count').datagrid('getRows');
				var TestFee = getFloat(count[0].TestFee);
				var RepairFee = getFloat(count[0].RepairFee);
				var MaterialFee = getFloat(count[0].MaterialFee);
				var CarFee = getFloat(count[0].CarFee);
				var DebugFee = getFloat(count[0].DebugFee);
				var OtherFee = getFloat(count[0].OtherFee);
				var TotalFee = getFloat(count[0].TotalFee);
				for(var i = 0; i < rows.length; i++){
					var row = $('#table7').datagrid('getRows');
					if(row.length!=0&&row[0].CustomerId!=rows[i].CustomerId){
						$.messager.alert('提示','委托单' + rows[i].Code + '的委托单位与列表中的委托单位不同，不能同时结账！','warning');
						$('#table6').datagrid('unselectRow',$('#table6').datagrid('getRowIndex',rows[i]));
						return;
					}
					for(var j = 0; j < row.length; j++){
						if(row[j].Code==rows[i].Code){
							$.messager.alert('提示','委托单' + rows[i].Code + '已加入结账列表，不可重复加入！','warning');
							$('#table6').datagrid('unselectRow',$('#table6').datagrid('getRowIndex',rows[i]));
							return;;
						}
					}
					$('#table7').datagrid('appendRow',rows[i]);
					TestFee = TestFee + getFloat(rows[i].TestFee);
					RepairFee = RepairFee + getFloat(rows[i].RepairFee);
					MaterialFee = MaterialFee + getFloat(rows[i].MaterialFee);
					CarFee = CarFee + getFloat(rows[i].CarFee);
					DebugFee = DebugFee + getFloat(rows[i].DebugFee);
					OtherFee = OtherFee + getFloat(rows[i].OtherFee);
					TotalFee = TotalFee + getFloat(rows[i].TotalFee);

				}
				$('#table6').datagrid('unselectAll');
				$('#Count').datagrid('updateRow',{
					index:0,
					row:{
						TotalFee:TotalFee,
						TestFee:TestFee,
						RepairFee:RepairFee,
						MaterialFee:MaterialFee,
						CarFee:CarFee,
						DebugFee:DebugFee,
						OtherFee:OtherFee
					}
				});
				$('#TotalFee').val(TotalFee);
				$('#FeeTotal').form('validate');
			}
		}]
	});
	
		$('#Count').datagrid({
				//title:'选中的委托单信息',
				width:1000,
				height:50,
				fit:false,
				showFooter:true,
				singleSelect:true, 
				nowrap: false,
				striped: true,
				remoteSort: false,
				columns:[[
					{field:'TotalFee',title:'总费用',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TestFee',title:'检测费',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'RepairFee',title:'维修费',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'MaterialFee',title:'材料费',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'CarFee',title:'交通费',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'DebugFee',title:'调试费',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OtherFee',title:'其他费',width:60,align:'center',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					}			
		
				]],
				pagination:false,
				rownumbers:true
			}); 
		$('#Count').datagrid('insertRow',{
			row:{
				TotalFee:0.0,
				TestFee:0.0,
				RepairFee:0.0,
				MaterialFee:0.0,
				CarFee:0.0,
				DebugFee:0.0,
				OtherFee:0.0
			}
		});
		
	});

	function doLoadHistoryDeList()
	{
		$('#table4').datagrid('options').url='/jlyw/DetailListComServlet.do?method=21';
		$('#table4').datagrid('options').queryParams={'FeeCode':$('#FeeCode').val(),'CustomerId':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val()};
		$('#table4').datagrid('reload');
		
		$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=4';
		$('#table6').datagrid('options').queryParams={'CustomerId':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue')),'Status':encodeURI(3)};
		$('#table6').datagrid('reload');
	}
	function acount(){
		 $('#FeeTotal').form('submit',{
			url: '/jlyw/CustomerAccountServlet.do?method=3',
			onSubmit:function(){ 
				var rows = $('#table7').datagrid('getRows');
				if(rows==null||rows.length==0){
					return false;
				}
				var ChequeFee = getFloat($('#ChequePaid').val());
				var CashFee = getFloat($('#CashPaid').val());
				var AcountFee= getFloat($('#AcountFee').val());
				if(ChequeFee+CashFee+AcountFee!=getFloat($('#TotalFee').val())){
					$.messager.alert('提示','支付总额与总费用不等，请确认！','info');
					return false;
				}
				var selectedRows = $('#table7').datagrid('getRows');
				$('#CommissionSheets').val(JSON.stringify(selectedRows));
				return $('#FeeTotal').form('validate');
			},
			success:function(data){
			   var result = eval("("+data+")");
			   //$("#Customer_Balance").val(result.balance);
			   //$('#CustomerAccount').datagrid('reload');
			     $.messager.alert('提示',result.msg,'info');
				 var rows = $('#table7').datagrid('getRows');
				 for(var i = 0; i < rows.length; i++){
					 var row = rows[i];
					 var index = $('#table7').datagrid('getRowIndex',row);
					 row.DetailListCode = result.DetailListCode;
					 $("#table7").datagrid("updateRow", {index:index, row:row});
				 }
				 NoSubmitAgain('#btn_checkout');
				 $('#DetailListId').val(result.DetailListId);
				 $('#DetailListId1').val(result.DetailListId);	
			 }
		});
	}
	
	function account(){
		$('#AcountFee').val($('#TotalFee').val());
		$('#CashPaid').val(0);
		$('#ChequePaid').val(0);
	}
	function cash(){
		$('#AcountFee').val(0);
		$('#CashPaid').val($('#TotalFee').val());
		$('#ChequePaid').val(0);
	}
	function cheque(){
		$('#AcountFee').val(0);
		$('#CashPaid').val(0);
		$('#ChequePaid').val($('#TotalFee').val());
	}
				

</script>
</head>

<body>

 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="结账管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div style="+position:relative;" >
<table style="padding:0px; margin:0px">
<tr>
<td colspan="2">
<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="12%" align="right" >费用清单编号：</td>
					<td width="15%" align="left" >
						<input id="FeeCode" class="easyui-validatebox" name="FeeCode" style="width:150px;" />
					</td>
					
					<td width="10%" align="right">委托单位：</td>
					<td width="20%" align="left">
						<select name="CustomerName" id="CustomerName" style="width:180px"/>
					</td>
					<td width="10%" align="right" >委托单编号：</td>
					<td width="15%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" />
					</td>
                    <td></td>
				</tr >
                <tr >
					<td colspan="2"></td>
					<td width="10%" align="right">起始时间：</td>
					<td width="20%" align="left">
						<input name="DateFrom" id="DateFrom" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
					<td width="10%" align="right" >结束时间：</td>
					<td width="15%" align="left" >
						<input name="DateEnd" id="DateEnd" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td width="10%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryDeList()">查询</a></td>
				</tr >
		</table>
		</form>
		</div> 
</td>
</tr>
<tr>
  <td>
      <table id="table4" style="height:350px; width:450px"><!--清单信息-->
			</table>
  </td>
  <td>
            <table id="table6" style="height:350px; width:450px"><!--委托单信息-->
			</table>
  </td>
</tr>
<tr>
<td colspan="2">
 			<table id="table7" style="height:350px; width:1000px"><!--需要结账的委托单信息-->
			</table>
</td>
</tr>
<tr>
<td colspan="2">
 			<table id="Count" style="height:50px; width:1000px">
			</table>
</td>
</tr>
<tr>
  <td colspan="2">
         <div id="p2" class="easyui-panel" style="width:1000px;padding:10px;"
				title="操作区" collapsible="false"  closable="false">
<!--			<table id="discount_info_table" iconCls="icon-tip" ></table>-->
			<form id="FeeTotal" method="post">
			<input type="hidden" name="CommissionSheets" id="CommissionSheets"/>
			<input id="TotalFee" name="TotalFee" type="hidden" />
			<table width="950px" >
				<tr>
					<td width="6%" align="right" style="padding-top:10px;">
						 发票号：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
	                     <input id="InvoiceCode" class="easyui-validatebox" name="InvoiceCode" type="text" required="true" />
					</td>
						 
					<td width="6%" align="right" style="padding-top:10px;">
						<span onclick="account()"> 账户抵扣费用：</span>
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="AcountFee" class="easyui-numberbox" name="AcountFee"  precision="2" type="text" required="true" value="0"/>元
					</td>
				</tr>
		
				<tr >
				    <td width="6%" align="right" style="padding-top:10px;">
						 <span onclick="cash()">现金支付费用：</span>
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="CashPaid" class="easyui-numberbox" name="CashPaid"  precision="2" type="text" required="true"  value="0"/>元
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						 <span onclick="cheque()">支票支付费用：</span>
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="ChequePaid" class="easyui-numberbox" name="ChequePaid"  precision="2" type="text" required="true" value="0"/>元
					</td>					
				</tr>
                <tr>
                	<td  align="right" style="padding-top:10px;" colspan="4">
	                     <a id="btn_checkout" class="easyui-linkbutton" iconCls="icon-sum" href="javascript:acount()">结账</a>
					</td>
                </tr>
		  </table>
		  </form>
		</div>
  </td>
</tr>
</table>

</div>
<form id="formLook" method="post" action="/jlyw/DetailListComServlet.do?method=5" target="FeePrintFrame">
			
	<input id="DetailListId" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
</form>
<form id="formLook1" method="post" action="/jlyw/DetailListComServlet.do?method=6" target="FeePrintFrame1">
	
	<input id="DetailListId1" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
</form>
<iframe id="FeePrintFrame" name="FeePrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
<iframe id="FeePrintFrame1" name="FeePrintFrame1" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</DIV></DIV>
</body>
</html>
