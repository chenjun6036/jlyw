<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>一个角色 授予多个用户</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		$('#username').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
		$('#username1').combobox({
		//	url:'/jlyw/CustomerServlet.do?method=6',
			onSelect:function(){},
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
		$("#rolename").combobox({
			valueField:'name',
			textField:'name',
			required:true,
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
				$(this).combobox('reload','/jlyw/RoleServlet.do?method=8&QueryName='+encodeURI(newValue));
			}
		});
		
			$('#role').datagrid({
				title:'角色信息',
				width:900,
				height:300,
				pagination:true,
				//rownumbers:true,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/RoleServlet.do?method=2',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'角色名称',width:150},
					{field:'Description',title:'角色描述',width:150}
					
				]],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//正常
						return 'color:#000000';
					}else 
						return 'color:#FF0000';	
					
				},
				onDblClickRow:function(rowIndex, rowData)
				{			     	
					var clickname=rowData.Name;
					clickname="角色 '"+clickname+"' 的用户";
						
					$('#roleuser').datagrid({title:clickname,url:""});
					$('#roleuser').datagrid('loadData',{"total":0,"rows":[]});
					$('#roleuser').datagrid('options').url='/jlyw/RoleServlet.do?method=9';
					$('#roleuser').datagrid('options').queryParams={'roleId':encodeURI(rowData.Id)};
					$('#roleuser').datagrid('reload');
				}
				
			});
			$('#roleuser').datagrid({
				title:'角色用户',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/RoleServlet.do?method=9',
				
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'JobNum',title:'工号',width:80,sortable:true},
					{field:'Name',title:'姓名',width:120},
					{field:'userName',title:'用户名',width:100},
					{field:'JobTitle',title:'行政职务',width:120}
				]],
				toolbar:"#user-search-toolbar"
				
			});
			$('#users').datagrid({
				title:'用户信息',
//				iconCls:'icon-save',
				//width:800,
				//height:350,
				pagination:true,
				//rownumbers:true,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/UserServlet.do?method=0',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'JobNum',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				
				columns:[[
					
					{field:'JobNum',title:'工号',width:80,sortable:true},
					{field:'Name',title:'姓名',width:120},
					{field:'userName',title:'用户名',width:100},
					{field:'JobTitle',title:'行政职务',width:120}
				]],
				
				toolbar:"#table-search-toolbar"
				
			});
		});
		
		function query()
		{
			$('#role').datagrid('options').url='/jlyw/RoleServlet.do?method=2';
			$('#role').datagrid('options').queryParams={'queryname':encodeURI($('#rolename').combobox('getValue'))};
			$('#role').datagrid('clearSelections');
			$('#role').datagrid('reload');
		}
		function addup()
		{
			var rows = $('#users').datagrid('getSelections');
						
			var row = $('#roleuser').datagrid('getRows');
			var index = row.length;
			
			for(var i=0; i<rows.length; i++){
				var j;
				for(j=0; j<index; j++)
				{
					if(rows[i].JobNum==row[j].JobNum){
						alert("选择了重复的用户");
						break;
					}
				}
				if(index!=j)
					continue;
				$('#roleuser').datagrid('insertRow', {
					index: index,
					row:{
						userroleId:-1,
						Id:rows[i].Id,
						JobNum:rows[i].JobNum,
						Name:rows[i].Name,
						userName:[i].userName,
						JobTitle:rows[i].JobTitle
					}
				});
				index++;
			}
			$('#users').datagrid('clearSelections');
		}
		function query1()
		{
			$('#users').datagrid('options').url='/jlyw/UserServlet.do?method=0';
			$('#users').datagrid('options').queryParams={'queryname':encodeURI($('#username').combobox('getValue'))};
			$('#users').datagrid('clearSelections');
			$('#users').datagrid('reload');
		}
		function saveGrant()
		{
			var select = $('#role').datagrid('getSelected');
			var rows = $('#roleuser').datagrid('getRows');
			var length = rows.length;
			$('#sub_role').val(select.Id);
			
			var users ="";
			if(length==0)
			{
				$.messager.alert('提示',"请至少选择一个用户",'info');
				return;
			}
			for(var i=length-1; i>=0; i--){
					users = users + rows[i].Id + "|";
			}
			$('#sub_users').val(users);
			$('#frm_role_user').form('submit',{
					url: '/jlyw/RoleServlet.do?method=10',
					onSubmit:function(){ return $('#frm_role_user').form('validate');},
					success:function(data){
						var result = eval("("+data+")");
							alert(result.msg);
						$('#roleuser').datagrid('options').queryParams={'roleId':encodeURI(select.Id)};
						$('#roleuser').datagrid('reload');
					}
			});
		}
		function cancelGrant()
		{
			var rows = $('#roleuser').datagrid('getSelections');
			var length = rows.length;
			if(length==0)
			{
				$.messager.alert('警告',"请至少选择一个用户",'warning');
				return;
			}
			for(var i=length-1; i>=0; i--){
				if(rows[i].userroleId < 0){
					var index = $('#roleuser').datagrid('getRowIndex', rows[i]);
					$('#roleuser').datagrid('deleteRow', index);	
				}else{
					$.ajax({
						type:'POST',
						url:'/jlyw/RoleServlet.do?method=7',         
						data:{"roleId":rows[i].roleId,"userId":rows[i].Id},
						dataType:"json",
						success:function(data){
							
						}
						
					});
					var index = $('#roleuser').datagrid('getRowIndex', rows[i].Id);
					$('#roleuser').datagrid('deleteRow', index);
				}
			}
			$('#roleuser').datagrid('clearSelections');
		}
		function queryuser()
		{
		    var row = $('#role').datagrid('getSelected');
			
			if(row==null||row.length==0)
			{
				$.messager.alert('提示',"请至少选择一个角色",'info');
				return;
			}
			$('#roleuser').datagrid('options').url='/jlyw/UserServlet.do?method=13';
			$('#roleuser').datagrid('options').queryParams={'queryname':encodeURI($('#username1').combobox('getValue')),'roleId':row.Id};
			//$('#roleuser').datagrid('clearSelections');
			$('#roleuser').datagrid('reload');
		}
	</script>

</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="一个角色授予多个用户" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


<div border="true" style="width:900px;overflow:hidden;+position:relative;">
    <div id="p" class="easyui-panel" style="width:900px;height:100px;"
				title="查询条件" collapsible="false"  closable="false">
	<table width="100%" height="100%">
		<tr height="3%">
			<td width="100%" style="overflow:visible" align="right">
				<div align="left">
					<label id="label_dd" for="dd">角色名:</label>
					<div id="div_username" style="display:inline">
						<input id="rolename" type="text" name="rolename" style="width:200px;"></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
				</div>
			</td>
		</tr>
	</table> 	
  </div>
<div style="width:900px;height:400px;">	
	<table id="role" iconCls="icon-edit" width="900px" height="400px" singleSelect="true" ></table>
</div>

<div   border="true" style="width:900px;height:300px;overflow:hidden;">
	<table id="roleuser" class="easyui-datagrid" iconCls="icon-edit"></table>
	
</div>	
<div border="true" style="width:900px;height:400px;overflow:hidden;">
	<table id="users" class="easyui-datagrid" iconCls="icon-edit"></table>
</div>
<form id="frm_role_user" method="post">
<input id="sub_role" name="roleId" type="hidden"/>
<input id="sub_users" name="users" type="hidden"/>
</form>	
</div>
</DIV>
</DIV>
<div id="table-search-toolbar" style="padding:2px">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addup()">提交选择的用户</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>用户姓名：</label><input id="username" class="easyui-combobox" name="username"  url="" style="width:150px;" valueField="name" textField="name" panelHeight="auto" ></input>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query1()"  plain="true">查询用户</a>
					</td>
				</tr>
			</table>
</div>

<div id="user-search-toolbar" style="padding:2px">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveGrant()">保存</a>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="cancelGrant()">注销</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>用户姓名：</label><input id="username1" class="easyui-combobox" name="username1" url="" style="width:150px;" valueField="name" textField="name" panelHeight="auto" ></input>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="queryuser()"  plain="true">查询用户</a>
					</td>
				</tr>
			</table>
</div>
</body>
</html>
