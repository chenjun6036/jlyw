<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>出车费用分配</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
    <script type="text/javascript" src="../JScript/upload.js"></script>
<script>
$(function(){		
	
	$("#DriverName").combobox({
	//	url:'/jlyw/CustomerServlet.do?method=5',
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
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $('#DriverName').combobox('getText');
					$('#DriverName').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 700);
			
		}
	});	
	$("#StaffName").combobox({
	//	url:'/jlyw/CustomerServlet.do?method=5',
		valueField:'name',
		textField:'name',
		onSelect:function(record){		
			$("#StaffId").val(record.id);
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
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $('#StaffName').combobox('getText');
					$('#StaffName').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 700);
			
		}
	});	
	$('#allot_info_table').datagrid({
		title:'费用分配信息',
//			iconCls:'icon-save',
		width:985,
		height:200,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
//		url:'/jlyw/TaskAssignServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		//idField:'userid',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DrivingVehicleId',title:'出车记录编号',width:120,sortable:true},
			{field:'StaffName',title:'员工姓名',width:120},
			{field:'StaffFee',title:'承担费用',width:120}
			//{field:'Remark',title:'备注',width:120}
		]],
		pagination:false,
		rownumbers:true	,
		toolbar:"#allot-toolbar",
		
		onSelect:function(rowIndex, rowData){
			
		},
		onLoadSuccess:function(data){
			computeFee();
		}		
	});
	
	$('#driving_table').datagrid({
		title:'出车记录信息',
//		iconCls:'icon-save',
		width:600,
		height:350,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//		collapsible:true,
		url:'/jlyw/VehicleMissionServlet.do?method=2',
	    sortName: 'BeginDate',
 		sortOrder: 'desc',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
	         {field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'Id',title:'出车记录编号',width:80,sortable:true,align:'center'},
				{field:'VehicleLicence',title:'车牌号',width:80,sortable:true,align:'center'},
				{field:'DriverName',title:'司机',width:80,align:'center'},
				{field:'BeginDate',title:'出车时间',width:150,align:'center'},
				{field:'People',title:'坐车的员工',width:200,align:'center'},
				{field:'AssemblingPlace',title:'集合地点',width:120,align:'center'},
				{field:'Kilometers',title:'行驶公里数',width:80,align:'center'},
				{field:'TotalFee',title:'出车费用',width:80,align:'center'},
				{field:'Status',title:'是否已结算',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value == 0 || value =="0"){
								return '未结算';
							}
							if(value == 1 || value =="1"){
								return '已结算';
							}
							
							return "未结算";
						}
				},
				{field:'SettlementName',title:'结算人',width:100,align:'center'},
				{field:'SettlementTime',title:'结算时间',width:150,align:'center'}
				
			]
		],
		rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//未结算
						return 'color:#000000'
					}else if(rowData.Status == 1){	//已结算
						return 'color:#FF0033';	
					}
		},
		pagination:true,
		rownumbers:true	,
		toolbar:[{
			text:'结算',
			iconCls:'icon-money',
			handler:function(){
				var result = confirm("您确定要结算吗？");
				if(result == false){
					return false;
				}
				//var rows = $('#driving_table').datagrid('getRows');
				var recordIds="";
//				for(var i=0; i<rows.length; i++){
//					recordIds = recordIds + rows[i].Id + "|";				
//				}
				recordIds=JSON.stringify($('#driving_table').datagrid('options').queryParams);
				$.ajax({
					type: "post",
					url: "/jlyw/VehicleMissionServlet.do?method=7",
					data: {"recordIds":recordIds},
					dataType: "json",	//服务器返回数据的预期类型
					beforeSend: function(XMLHttpRequest){
						//ShowLoading();
					},
					success: function(data, textStatus){
						if(data.IsOK){
							$('#driving_table').datagrid('reload');
							$.messager.alert('提示','结算成功！','info');
							
						}else{
							$.messager.alert('提示',data.msg,'error');
						}
					},
					complete: function(XMLHttpRequest, textStatus){
						//HideLoading();
					},
					error: function(){
						//请求出错处理
					}
				});
			}
		},'-',{
			text:'结算恢复未结算',
			iconCls:'icon-money',
			handler:function(){
				var result = confirm("您确定要恢复未结算吗？");
				if(result == false){
					return false;
				}
				var rowSelected = $('#driving_table').datagrid('getSelected');		
				if(rowSelected == null){
					$.messager.alert('提示','请选择一行数据','info');
					return false;
				}
				var recordIds=rowSelected.Id;
//				for(var i=0; i<rows.length; i++){
//					recordIds = recordIds + rows[i].Id + "|";				
//				}
				//recordIds=JSON.stringify($('#driving_table').datagrid('options').queryParams);
				$.ajax({
					type: "post",
					url: "/jlyw/VehicleMissionServlet.do?method=10",
					data: {"recordIds":recordIds},
					dataType: "json",	//服务器返回数据的预期类型
					beforeSend: function(XMLHttpRequest){
						//ShowLoading();
					},
					success: function(data, textStatus){
						if(data.IsOK){
							$('#driving_table').datagrid('reload');
							$.messager.alert('提示','恢复成功！','info');
							
						}else{
							$.messager.alert('提示',data.msg,'error');
						}
					},
					complete: function(XMLHttpRequest, textStatus){
						//HideLoading();
					},
					error: function(){
						//请求出错处理
					}
				});
			}
		},'-',{
			text:'导出出车记录',
			iconCls:'icon-download',
			handler:function(){
				drivingExport();				
			}
		},'-',{
			text:'导出费用信息',
			iconCls:'icon-download',
			handler:function(){
				drivingFeeExport();				
			}
		},'-',{
			text:'查看所选记录对应的任务',
			iconCls:'icon-search',
			handler:function(){
				var rowSelected = $('#driving_table').datagrid('getSelected');		
				if(rowSelected != null){
				   var titlecode ="出车记录编号“"+rowSelected.Id+"”对应的现场任务信息";
					$('#table2').datagrid({title:titlecode});
					
					$('#table2').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=11';
					$('#table2').datagrid('options').queryParams={'DrivingVehicleId':rowSelected.Id};
					
					$('#table2').datagrid('reload');						
					
				}else{
					$.messager.alert('提示','请选择一行数据','info');
				}	
			  }
		}],
		onSelect:function(rowIndex, rowData){
			$('#DrivingVehicleId').val(rowData.Id);
			$('#DrivingVehicleId1').val(rowData.Id);
			$('#Kilometers').val(rowData.Kilometers);
			$('#Status').val(rowData.Status);
			$('#allot_info_table').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=4';
			$('#allot_info_table').datagrid('options').queryParams={'DrivingVehicleId':encodeURI(rowData.Id)};
			$('#allot_info_table').datagrid('reload');
		}
	});
	
	$('#table2').datagrid({
		title:'现场任务信息',
		width:400,
		height:350,
		singleSelect:true, 
		nowrap: false,
		striped: true,
	//	url:'/jlyw/LocaleMissionServlet.do?method=8',
		sortName: 'CreateDate',
		remoteSort: false,
		sortOrder:'dec',
		idField:'Id',
		pageSize:10,
		
		columns:[[
			{field:'CreateDate',title:'创建时间',width:80,align:'center',sortable:true},
			{field:'CreatorName',title:'创建人',width:60,align:'center',sortable:true},
			{field:'Code',title:'委托书号',width:80,align:'center',sortable:true},
			{field:'Name',title:'单位名称',width:160,align:'center',sortable:true},
			{field:'Region',title:'所在地区',width:80,align:'center',sortable:true},
			
			{field:'Status',title:'处理状态',width:90,align:'center',sortable:true,
				formatter:function(value, rowData, rowIndex){
					if(value == 0 || value =="0"){
						return '未分配';
					}
					if(value == 1 || value =="1"){
						return '已分配';
					}
					if(value == 2 || value =="2"){
						return "已完成";
					}
					if(value ==3 || value =="3"){
						return "已注销";
					}
					if(value == 4 || value =="4"){
						return "未核定";
					}
					if(value == 5 || value =="5"){
						return "已核定";
					}
					return "已注销";
				}
			},
			{field:'MissionDesc',title:'器具信息',width:120,align:'center'},
			{field:'Department',title:'业务部门',width:80,align:'center'},
			{field:'SiteManagerName',title:'检测负责人',width:80,align:'center'},
			{field:'Staffs',title:'人员',width:180,align:'center'},
			{field:'TentativeDate',title:'暂定日期',width:80,align:'center',sortable:true},					
			
			{field:'CheckDate',title:'核定日期',width:80,align:'center',sortable:true},
			{field:'ExactTime',title:'确定日期',width:80,align:'center',sortable:true},
			{field:'VehicleLisences',title:'乘车信息',width:120,align:'center'},
		
			{field:'Address',title:'单位地址',width:120,align:'center'},
			{field:'ZipCode',title:'邮编',width:60,align:'center'},
			{field:'Contactor',title:'联系人',width:80,align:'center'},
			{field:'Tel',title:'单位电话',width:100,align:'center'},
			{field:'ContactorTel',title:'联系人电话',width:100,align:'center'},
			{field:'Remark',title:'备注',width:180,align:'center'},
			{field:'Feedback',title:'反馈',width:180,align:'center'}
			
		]],
		pagination:true,
		rownumbers:true,
		rowStyler:function(rowIndex, rowData){
			if(rowData.Status == 3){	//已注销
				return 'color:#FF00FF'
			}else if(rowData.Status == 1){	//已分配
				return 'color:#FF0033';	
			}else if(rowData.Status == 2){	//已完工
				return 'color:#000000';	
			}else if(rowData.Status == 5){	//负责人已核定
				return 'color:#339900';	
			}
			else if(rowData.Status==4&&rowData.Tag == 1){	//未核定；；；已到暂定日期可是未安排
				return 'color:#FFFF33';
			}else if(rowData.Status==4&&rowData.Tag == 2){	//未核定；；；未到暂定日期
				return 'color:#0000FF';
			}else{
				return 'color:#000000';
			}
		}
	});
			
});
function computeFee(){	//计算总费用
		
		var rows = $('#allot_info_table').datagrid('getRows');
		
		
		var TotalFee=0.0;
		for(var i=0; i<rows.length; i++){
			TotalFee=TotalFee+getFloat(rows[i].StaffFee);
			
		}
		
		$('#TotalFee').val(TotalFee);
				
	
}
function doLoadDrivingVehicle(){	//查找出车记录
				
		//加载出车记录
		$("#SearchForm").form('validate');
		var History_BeginDate=$('#History_BeginDate').datebox('getText');
		var History_EndDate=$('#History_EndDate').datebox('getText');
		$('#driving_table').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=2';
		$('#driving_table').datagrid('options').queryParams={'DriverName':encodeURI($("#DriverName").combobox('getText')),'History_BeginDate':encodeURI(History_BeginDate),'History_EndDate':encodeURI(History_EndDate),'License':encodeURI($("#License").val()),'MissionStatus':encodeURI($("#MissionStatus").val())};
		$('#driving_table').datagrid('reload');
				
	
}
function addFee(){   	//增加一项费用分配
	
	$("#FeeSubmitForm1").form('submit', {
		url:'/jlyw/VehicleMissionServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($('#Status').val()=='1'||$('#Status').val()==1){
				$.messager.alert('提示！',"此出车记录已结算，不能在进行此操作！",'info');
				return false;
			}	
			if($('#DrivingVehicleId1').val()==''||$('#DrivingVehicleId1').val().length==0){
				$.messager.alert('提示！',"请选择一个有效的出车记录",'info');
				return false;
			}
			var StaffName = $('#StaffName').combobox('getText');
			
			if(StaffName==''){
				$.messager.alert('提示！',"请选择一个有效的员工",'info');
				return false;
			}
			var rows = $('#allot_info_table').datagrid('getRows');//禁止添加重复员工
			for(var i=0; i<rows.length; i++){
				if(rows[i].StaffName==StaffName){
					$.messager.alert('提示！',"此员工已经分配了费用，不能重复分配！",'info');
					return false;
				}	
			}		
			var StaffFee = $('#StaffFee').val();
			if(StaffFee == ''){
				$.messager.alert('提示！',"请选择一个有效的分配费用",'info');
				return false;
			}
			
			return $("#FeeSubmitForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				
				$('#allot_info_table').datagrid('reload');
				$.messager.alert('提示！','提交成功！','info');
				
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});  
}
function removeFee(){
	if($('#Status').val()=='1'||$('#Status').val()==1){
				$.messager.alert('提示！',"此出车记录已结算，不能在进行此操作！",'info');
				return false;
	}	
	var row = $("#allot_info_table").datagrid('getSelected');
	if(row){
		var result = confirm("该操作不可逆，您确定要注销吗？");
		if(result == false){
			return false;
		}
		$.ajax({
				type: "post",
				url: "/jlyw/VehicleMissionServlet.do?method=5",
				data: {"Id":row.Id},
				dataType: "json",	//服务器返回数据的预期类型
				beforeSend: function(XMLHttpRequest){
					//ShowLoading();
				},
				success: function(data, textStatus){
					if(data.IsOK){
						$('#allot_info_table').datagrid('reload');
						$.messager.alert('提示！','删除成功！','info');
						computeFee();
					}else{
						$.messager.alert('注销失败！',data.msg,'error');
					}
				},
				complete: function(XMLHttpRequest, textStatus){
					//HideLoading();
				},
				error: function(){
					//请求出错处理
				}
		});
	}else{
		$.messager.alert('提示！',"请先选择一条记录",'info');
	}

} 
function doFeeSubmit(){   	//提交费用分配
	$("#FeeSubmitForm").form('submit', {
		url:'/jlyw/VehicleMissionServlet.do?method=6',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($('#Status').val()=='1'||$('#Status').val()==1){
				$.messager.alert('提示！',"此出车记录已结算，不能在进行此操作！",'info');
				return false;
			}	
			if($('#DrivingVehicleId').val()==''||$('#DrivingVehicleId').val().length==0){
				$.messager.alert('提示！',"请选择一个有效的出车记录",'info');
				return false;
			}
			
			return $("#FeeSubmitForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//重新加载任务分配信息
				$('#driving_table').datagrid('reload');
				$.messager.alert('提示！','更新成功！','info');;
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});  
}  

function drivingExport(){
	ShowWaitingDlg("正在导出，请稍后......");
	$('#paramsStr').val(JSON.stringify($('#driving_table').datagrid('options').queryParams));
	$('#frm_export').form('submit',{
		success:function(data){
			var result = eval("("+ data +")");
			if(result.IsOK)
			{
				$('#filePath').val(result.Path);
				$('#frm_down').submit();
				CloseWaitingDlg();
			}
			else
			{
				$.messager.alert('提示','导出失败，请重试！','warning');
				CloseWaitingDlg();
			}
		}
	});
}
function drivingFeeExport(){
	ShowWaitingDlg("正在导出，请稍后......");
	$('#paramsStr1').val(JSON.stringify($('#driving_table').datagrid('options').queryParams));
	$('#frm_export1').form('submit',{
		success:function(data){
			var result = eval("("+ data +")");
			if(result.IsOK)
			{
				$('#filePath').val(result.Path);
				$('#frm_down').submit();
				CloseWaitingDlg();
			}
			else
			{
				$.messager.alert('提示','导出失败，请重试！','warning');
				CloseWaitingDlg();
			}
		}
	});
}
</script>
</head>

<body>
<form id="frm_export" method="post" action="/jlyw/VehicleMissionServlet.do?method=8">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_export1" method="post" action="/jlyw/VehicleMissionServlet.do?method=9">
<input id="paramsStr1" name="paramsStr" type="hidden" />
</form>

<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="出车费用分配" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

      <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="出车记录查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="950px" id="table1">
				<tr >
					<td align="center" ><label>车辆牌号：</label><input id="License" name="License" type="text" style="width:100px" />	&nbsp;			
					<label>司机：</label><input id="DriverName" name="DriverName" type="text" style="width:100px" />&nbsp;
					<select name="MissionStatus" id="MissionStatus" style="width:100px"><option value="" selected="selected">全部</option><option value="0" >未结算</option> <option value="1" >已结算</option></select>&nbsp;
					<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" name="History_BeginDate" type="text" style="width:100px" />&nbsp;
					<lable>结束时间：</label><input class="easyui-datebox" id="History_EndDate" name="History_EndDate" type="text" style="width:100px" />            </td>
					
					<td width="15%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadDrivingVehicle()">查询</a></td>
					
				</tr >
		</table>
		</form>
		</div>
		<table width="1005px" >
    	 <tr>
			  <td>
				 <table id="driving_table" iconCls="icon-tip" width="600" height="350px"></table>
			  </td>
			  <td>
				<table id="table2" style="height:350px; width:400px"><!--现场任务信息-->
				</table>
			  </td>
		  </tr>
		</table>
	
		
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;+position:relative;"
				title="费用分配" collapsible="false"  closable="false">
			<form id="FeeSubmitForm" method="post">
			
			<table id="allot_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br/>
			
			<input type="hidden" id="DrivingVehicleId" name="DrivingVehicleId" value="" />
			<input type="hidden" id="Status" name="Status" value="" />
			<table width="950">
				
				<tr height="40px">
					<td width="200" align="center" >行驶里程数：<input id="Kilometers" name="Kilometers" type="text" class="easyui-numberbox" precision="2" style="width:120px;" required="true"  /></td>
					
					<td width="200" align="center" >总费用：<input id="TotalFee" name="TotalFee" type="text" style="width:110px" class="easyui-numberbox" precision="2" required="true" />元</td>
					<td align="left" ><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doFeeSubmit()">确定</a>&nbsp;&nbsp;</td>
					
				</tr>
			 </table>
		  </form>
		</div><br/>
<div id="allot-toolbar" style="padding:2px;">
		<form id="FeeSubmitForm1" method="post">
		<input type="hidden" id="DrivingVehicleId1" name="DrivingVehicleId" value="" />
		<table cellpadding="0" cellspacing="0" style="width:100%">
		
				<tr>
				    <td style="text-align:left;padding-left:2px"><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" href="javascript:void(0)" onClick="removeFee()">删除</a></td>
					<td style="text-align:right;padding-right:2px">
					选择员工：<input id="StaffName" name="StaffName" type="text" style="width:122px;"  /><input id="StaffId" name="StaffId" type="hidden" style="width:120px;"   />&nbsp;&nbsp;
					费用：<input id="StaffFee" name="StaffFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" plain="true" onClick="addFee()">添加</a>
					</td>
				</tr>
		</table>
		</form>
			
</div>

		

</DIV></DIV>

</body>
</html>
