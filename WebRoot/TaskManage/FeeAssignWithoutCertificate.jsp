<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>转包费/技术服务费录入</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script>
	$(function(){
		$("#FeeAssignForm-FeeAllotee").combobox({
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
				$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}
		});
		var lastIndex;
		$('#fee_assign_table').datagrid({
			title:'转包费/技术服务费录入',
			singleSelect:false, 
			height: 300,
//			width:800,
			nowrap: false,
			striped: true,
			url:'/jlyw/CertificateFeeAssignServlet.do?method=4',
			remoteSort: false,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{field:'FeeAlloteeName',title:'姓名',width:70,sortable:true,align:'center'},
					{field:'TestFee',title:'检定费',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'RepairFee',title:'修理费',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'MaterialFee',title:'材料费',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'CarFee',title:'交通费',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'DebugFee',title:'调试费',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OtherFee',title:'其它费',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'Remark',title:'备注',width:80,align:'center',editor:{
						type:'text'
					}}
				]
			],
			onClickRow:function(rowIndex){
				$('#fee_assign_table').datagrid('endEdit', lastIndex);
				$('#fee_assign_table').datagrid('beginEdit', rowIndex);
				lastIndex = rowIndex;
			},
			rownumbers:true	,
			pagination:false,
			toolbar:"#fee_assign_table-toolbar"
		});
		
		doLoadCommissionSheet();
	});
	function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':''};
				$('#fee_assign_table').datagrid('loadData',{total:0,rows:[]});
				
				$("#FeeAssignForm-CommissionSheetId").val('');				
				//$("#Ness").removeAttr("checked");	//去勾选
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载任务分配信息
					$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':result.CommissionObj.CommissionId};
					$('#fee_assign_table').datagrid('reload');
					
					$("#FeeAssignForm-CommissionSheetId").val(result.CommissionObj.CommissionId);
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	
	
	/******************         产值分配相关函数             ***********************/
	function doDelFeeAssignRecord(){	//删除所选的费用记录
		var result = confirm("您确定要删除所选费用记录吗？");
		if(result == false){
			return false;
		}
		$('#fee_assign_table').datagrid('acceptChanges');
		var rows = $('#fee_assign_table').datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
			var index = $('#fee_assign_table').datagrid('getRowIndex', rows[i]);
			$('#fee_assign_table').datagrid('deleteRow', index);
		}
	}
	function doAddAnAllotee(){	//添加一个费用分配人
		if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==""){
			var result = confirm("确定费用所分配人员为空吗？");
			if(result == false){
				return false;
			}
		}
		var rows = $('#fee_assign_table').datagrid('getRows');
		for(var i = 0; i<rows.length; i++){
			if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==rows[i].FeeAlloteeName){
				$.messager.alert('提示！','已存在:'+ $('#FeeAssignForm-FeeAllotee').combobox('getValue') + '的产值分配，不能重复添加！' ,'info');
				return false;
			}
		}
		$('#fee_assign_table').datagrid('insertRow', {
			index:rows.length,
			row:{
				FeeAlloteeName:$('#FeeAssignForm-FeeAllotee').combobox('getValue'),
				TestFee:getInt(0),
				RepairFee:getInt(0),
				MaterialFee:getInt(0),
				CarFee:getInt(0),
				DebugFee:getInt(0),
				OtherFee:getInt(0),
				Remark:''
			}
		});
	}
	function doFeeAssign(){	//确认提交费用分配
		$("#FeeAssignForm").form('submit', {
			url:'/jlyw/CertificateFeeAssignServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;	
				if($('#FeeAssignForm-CommissionSheetId').val()==""){
					$.messager.alert('提示！','请先选择一个有效的委托单！','info');
					return false;
				}			
				$('#fee_assign_table').datagrid('acceptChanges');
				var rows = $("#fee_assign_table").datagrid("getRows");						
				$('#FeeAssignForm-FeeAssignInfo').val(JSON.stringify(rows));
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$.messager.alert('提示！','费用添加成功！','info');
				}else{
					$.messager.alert('提交失败！',result.msg,'error');
				}
			}
		});
	}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="转包费/技术服务费录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" />
					</td>

					<td width="10%" align="right">密  码：</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="history.go(-1)">返回</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
		<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br />
		
		<table id="fee_assign_table" iconCls="icon-tip" width="1005px"></table>
		<br />
		<form id="FeeAssignForm" method="post">
			<input type="hidden" name="CommissionSheetId" id="FeeAssignForm-CommissionSheetId"  />
			<input type="hidden" name="FeeAssignInfo" id="FeeAssignForm-FeeAssignInfo" />
		</form>
		
<div id="fee_assign_table-toolbar" style="padding:2px;">
		<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
				    <td style="text-align:left;padding-left:2px"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" href="javascript:void(0)" onClick="doFeeAssign()">保存费用分配</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" href="javascript:void(0)" onClick="doDelFeeAssignRecord()">删除</a>
					</td>
					<td style="text-align:right;padding-right:2px">
					选择员工：<select name="FeeAllotee" id="FeeAssignForm-FeeAllotee" style="width:152px"></select>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" plain="true" onClick="doAddAnAllotee()">新增费用分配</a>
					</td>
				</tr>
		</table>			
</div>

</DIV></DIV>
</body>
</html>
