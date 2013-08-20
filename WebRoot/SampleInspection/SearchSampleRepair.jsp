<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>样品修理信息查询</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script>
		$(function(){
		    var lastIndex;		
			$('#RepairRecord').datagrid({
			    width:900,
				height:500,
				title:'样品修理信息',
//				iconCls:'icon-save',
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'repairrecord.json',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'id',
				columns:[[
					{field:'id',title:'修理编号',width:80,sortable:true,align:'center'},
					{field:'commissionid',title:'委托单编号',width:80,align:'center'},
					{field:'consumername',title:'委托单位',width:120,align:'center'},
					{field:'appliancename',title:'器具名称',width:100,align:'center'},
					{field:'item',title:'修理项',width:80,align:'center'},
					{field:'assemblyname',title:'配件名称',width:100,align:'center'},
					{field:'assemblyfee',title:'配件费用',width:80,align:'center'},
					{field:'fee',title:'修理费用',width:80,align:'center'},
					{field:'date',title:'修理时间',width:100,align:'center'}
                ]],
				pagination:true,
				rownumbers:true
				
			});

		});
		function ok(){
			 $('#allot').form('submit',{
				//url: 'userAdd.action',
				onSubmit:function(){ return $('#allot').form('validate');},
		   		success:function(){
			   		 close1();
		   		 }
			});
		}
	</script>
</head>

<body>
	<br />
<div  align="center" style="width:900px;height:40px;" >
	     <h2>样品修理信息查询</h2>
</div>
   <br />
<div >
     <div id="p" class="easyui-panel" style="width:900px;height:90px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
			<table width="850px" id="table1">
				<tr>
					<td width="10%" align="right">委托单编号：</td>
					<td width="22%" align="left">
						<input id="id" class="easyui-combobox" name="type" url="commissionid.json" style="width:150px;" valueField="id" textField="text" panelHeight="auto" >
					</td>
					<td width="10%" align="right"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="ok()">查询</a></td>
				    <td width="21%" align="left"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="goback()">重置</a></td>
				</tr>
				
		</table>
		</div>
		<br />
		
      <div style="width:900px;height:500px;">
	     <table id="RepairRecord" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>

</div>
</body>
</html>
