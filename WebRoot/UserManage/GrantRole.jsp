<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>给用户授权角色</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/ExportToExcel.js"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
    <script type="text/javascript" src="../JScript/upload.js"></script>
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
			
			$('#user').datagrid({
				title:'用户信息',
				width:900,
				height:300,
				pagination:true,
				//rownumbers:true,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/UserServlet.do?method=0',
				remoteSort: false,
				idField:'JobNum',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'JobNum',title:'工号',width:80,sortable:true},
					{field:'Name',title:'姓名',width:120},
					{field:'userName',title:'用户名',width:100},
					{field:'AdministrationPost',title:'行政职务',width:120}
				]],
				toolbar:[{
					text:'导出所选用户权限',
					iconCls:'icon-save',
					handler:function(){
						var select = $('#user').datagrid('getSelected');
						if(select){
							$('#sub_user').val(select.Id);
							
							ShowWaitingDlg("正在导出，请稍后......");
							$('#paramsStr').val(select.Id);
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
							
						}else{
							$.messager.alert('警告','请选择一个用户','warning');
						}
					}
				},'-',{
					text:'导出全部用户权限',
					iconCls:'icon-save',
					handler:function(){
					
						ShowWaitingDlg("正在导出，请稍后......");
						$('#paramsStr').val("");
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
				}],
				onDblClickRow:function(rowIndex, rowData)
				{	var clickname=rowData.Name;
					clickname="用户 '"+clickname+"' 的角色";
					$('#userrole').datagrid({title:clickname,url:""});
					$('#userrole').datagrid('loadData',{"total":0,"rows":[]});
					$('#userrole').datagrid('options').url='/jlyw/RoleServlet.do?method=6';
					$('#userrole').datagrid('options').queryParams={'userId':encodeURI(rowData.Id)};
					$('#userrole').datagrid('reload');
					
					
				},
				onLoadSuccess:function(data){
					//$('#user').datagrid('selectRow',0);
					//$('#userrole').datagrid('reload',{'userId':encodeURI($('#user').datagrid('getRows')[0].Id)});
				}
			});
			$('#userrole').datagrid({
				title:'用户角色',
				//width:700,
				//height:350,
				//pagination:true,
				//rownumbers:true,
				//singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/RoleServlet.do?method=6',
				singleSelect:true, 
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'角色名称',width:150},
					{field:'Description',title:'角色描述',width:150}
					
				]],
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						var select = $('#user').datagrid('getSelected');
						if(select){
							$('#sub_user').val(select.Id);
							var roles = "";
							var rows = $('#userrole').datagrid('getRows');
							if(rows.length==0)
							{
								return;
							}
							for(var i=0; i<rows.length; i++){
								roles = roles + rows[i].Id + "|";
							}
							$('#sub_roles').val(roles);
							$('#frm_user_role').form('submit',{
								url: '/jlyw/RoleServlet.do?method=5',
								onSubmit:function(){ return $('#frm_user_role').form('validate');},
		   						success:function(data){
		   							var result = eval("("+data+")");
		   								alert(result.msg);
		   						}
							});
							$('#userrole').datagrid('clearSelections');
							$('#userrole').datagrid('reload');
						}else{
							$.messager.alert('警告','请选择一个用户','warning');
						}
					}
				},'-',{
					text:'注销',
					iconCls:'icon-cancel',
					handler:function(){
						var rows = $('#userrole').datagrid('getSelections');
						var length = rows.length;
						if(length==0)
						{
							$.messager.alert('警告',"请选择一个角色",'warning');
							return;
						}
						$.ajax({
							type:'POST',
							url:'/jlyw/RoleServlet.do?method=7',
							data:"userId=" + rows[0].userId + "&roleId=" + rows[0].Id,
							dataType:"json",
							success:function(data){
								var result = eval("("+data+")");
		   							alert(result.msg);
							}
						});
						$('#userrole').datagrid('clearSelections');
						$('#userrole').datagrid('reload');
					}
				}
				]
			});
			$('#roles').datagrid({
				title:'角色信息',
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
				url:'/jlyw/RoleServlet.do?method=2',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				
				columns:[[
					
					{field:'Name',title:'角色名称',width:150},
					{field:'Parent',title:'父角色名称',width:80},
					{field:'Description',title:'角色描述',width:150}
					
				]],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//正常
						return 'color:#000000';
					}else 
						return 'color:#FF0000';	
					
				},
				toolbar:[{
					text:'提交选择的角色',
					iconCls:'icon-add',
					handler:function(){
						var rows = $('#roles').datagrid('getSelections');
						
						var row = $('#userrole').datagrid('getRows');
						var index = row.length;
			
						for(var i=0; i<rows.length; i++){
							var j;
							for(j=0; j<index; j++)
							{
								if(rows[i].Id==row[j].Id)
								{
									$.messager.alert('提示','选择了已有的角色','warning');
									break;
								}
							}
							if(index!=j)
								continue;
							$('#userrole').datagrid('insertRow', {
							index: index,
							row:{
								Id:rows[i].Id,
								Name:rows[i].Name,
								Description:rows[i].Description
								
							}
							});
							index++;
							}
							$('#roles').datagrid('clearSelections');
						}
				}
				]
				
			});
		});
		
		function query()
		{
			$('#user').datagrid('options').url='/jlyw/UserServlet.do?method=0';
			$('#user').datagrid('options').queryParams={'queryname':encodeURI($('#username').combobox('getValue'))};
			$('#user').datagrid('clearSelections');
			$('#user').datagrid('reload');
		}
	</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="给用户授权角色" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<form id="frm_export" method="post" action="/jlyw/UserServlet.do?method=14">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<div border="true" style="width:900px;overflow:hidden;position:relative;">
    <div id="p" class="easyui-panel" style="width:900px;height:100px;"
				title="查询条件" collapsible="false"  closable="false">
	<table width="100%" height="100%">
		<tr height="3%">
			<td width="100%" style="overflow:visible" align="right">
				<div align="left">
					<label id="label_dd" for="dd">用户姓名:</label>
					<div id="div_username" style="display:inline">
						<input id="username" class="easyui-combobox" name="username"  url="" style="width:150px;" valueField="name" textField="name" panelHeight="auto" ></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
				</div>
			</td>
		</tr>
	</table> 	
  </div>
<div style="width:900px;height:300px;">	
	<table id="user" iconCls="icon-edit" width="900px" height="300px" singleSelect="true" ></table>
</div>

<div   border="true" style="width:900px;height:200px;overflow:hidden;">
	<table id="userrole" class="easyui-datagrid" iconCls="icon-edit"></table>
	
</div>	
<div border="true" style="width:900px;height:300px;overflow:hidden;">
	<table id="roles" class="easyui-datagrid" iconCls="icon-edit"></table>
</div>
<form id="frm_user_role" method="post">
<input id="sub_user" name="userId" type="hidden"/>
<input id="sub_roles" name="roles" type="hidden"/>
</form>	
</div>


</DIV></DIV>
</body>
</html>
