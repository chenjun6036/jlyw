<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
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
		$("#privilegename").combobox({
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
					$(this).combobox('reload','/jlyw/PrivilegeServlet.do?method=8&QueryName='+encodeURI(newValue));
				}
			});	     
		$("#privilegename1").combobox({
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
					$(this).combobox('reload','/jlyw/PrivilegeServlet.do?method=8&QueryName='+encodeURI(newValue));
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
				height:400,
				pagination:true,
				//rownumbers:true,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/RoleServlet.do?method=2',
				remoteSort: false,
				idField:'JobNum',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'角色名称',width:150},
					{field:'Parent',title:'父角色名称',width:150},
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
					
					clickname="角色 '"+clickname+"' 的权限";
					$('#roleprivilege').datagrid({title:clickname,url:""});
					$('#roleprivilege').datagrid('loadData',{"total":0,"rows":[]});
					$('#roleprivilege').datagrid('options').url='/jlyw/PrivilegeServlet.do?method=6';
					$('#roleprivilege').datagrid('options').queryParams={'roleId':encodeURI(rowData.Id)};
					$('#roleprivilege').datagrid('reload');
					
				},
				onLoadSuccess:function(data){
					//$('#user').datagrid('selectRow',0);
					//$('#roleprivilege').datagrid('reload',{'roleId':encodeURI($('#role').datagrid('getRows')[0].Id)});
				}
			});
			$('#roleprivilege').datagrid({
				title:'角色权限',
				fit: true,
                nowrap: false,
                striped: true,
                singleSelect:true, 
                rownumbers:true,
				//url:'/jlyw/PrivilegeServlet.do?method=6',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'权限名称',width:150,sortable:true},
					{field:'Description',title:'权限描述',width:150}
				]],
				
				toolbar:"#privileges-search-toolbar"
				
			});
			$('#privileges').datagrid({
				title:'权限信息',
				pagination:true,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/PrivilegeServlet.do?method=2',
				remoteSort: false,
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				
				columns:[[		
					{field:'Name',title:'权限名称',width:150},
					{field:'ResourcesName',title:'对应资源名称',width:150},
					{field:'MappingUrl',title:'对应资源URL',width:150},
					{field:'Parameters',title:'资源URL的参数',width:150},
					{field:'Description',title:'权限描述',width:150}
				]],
				
				toolbar:"#table-search-toolbar"
				
			});
		});
		function addup()
		{
			var rows = $('#privileges').datagrid('getSelections');
						
			var row = $('#roleprivilege').datagrid('getRows');
			var index = row.length;
			
			for(var i=0; i<rows.length; i++){
				var j;
				for(j=0; j<index; j++)
				{
					if(rows[i].Id==row[j].Id)
						break;
				}
				if(index!=j)
					continue;
				$('#roleprivilege').datagrid('insertRow', {
					index: index,
					row:{
						RolePrivilegeId:-1,
						Id:rows[i].Id,
						Name:rows[i].Name,
						Description:rows[i].Description
					}
				});
				index++;
			 }
			  $('#privileges').datagrid('clearSelections');
		}
		function query()
		{
			$('#role').datagrid('options').url='/jlyw/RoleServlet.do?method=2';
			$('#role').datagrid('options').queryParams={'queryname':encodeURI($('#rolename').combobox('getValue'))};
			$('#role').datagrid('clearSelections');
			$('#role').datagrid('reload');
		}
		function query1()
		{
			$('#privileges').datagrid('options').url='/jlyw/PrivilegeServlet.do?method=2';
			$('#privileges').datagrid('options').queryParams={'queryname':encodeURI($('#privilegename').combobox('getValue'))};
			$('#privileges').datagrid('clearSelections');
			$('#privileges').datagrid('reload');
		}
		function saveGrant()
		{
			var select = $('#role').datagrid('getSelected');
			if(select){
				$('#sub_role').val(select.Id);
				var privileges = "";
				var rows = $('#roleprivilege').datagrid('getRows');
				if(rows.length==0)
				{
					return;
				}
				for(var i=0; i<rows.length; i++){
					privileges = privileges + rows[i].Id + "|";
				}
				$('#sub_privileges').val(privileges);
				$('#frm_role_privilege').form('submit',{
					url: '/jlyw/PrivilegeServlet.do?method=5',
					onSubmit:function(){ return $('#frm_role_privilege').form('validate');},
					success:function(data){
						var result = eval("("+data+")");
							alert(result.msg);
						$('#roleprivilege').datagrid('options').queryParams={'roleId':encodeURI(select.Id)};
						$('#roleprivilege').datagrid('reload');
					}
				});
			}else{
				$.messager.alert('提示','请选择一个用户','info');
			}
						
						
		}
		function cancelGrant()
		{
			var rows = $('#roleprivilege').datagrid('getSelections');
			var length = rows.length;
			if(length==0)
			{
				$.messager.alert('提示',"请至少选择一个权限",'info');
				return;
			}
			for(var i=length-1; i>=0; i--){
				if(rows[i].RolePrivilegeId < 0){
					var index = $('#roleprivilege').datagrid('getRowIndex', rows[i]);
					$('#roleprivilege').datagrid('deleteRow', index);
				}else{
					$.ajax({
						type:'POST',
						url:'/jlyw/PrivilegeServlet.do?method=7',
						data:{"RolePrivilegeId":rows[i].RolePrivilegeId},
						dataType:"json",
						async:false,
						success:function(data){
						}
					});
					var index = $('#roleprivilege').datagrid('getRowIndex', rows[i]);
					$('#roleprivilege').datagrid('deleteRow', index);
				  }
				 
				  $('#roleprivilege').datagrid('clearSelections');
			}
		}
		function queryprivilege()
		{
		    var row = $('#role').datagrid('getSelected');
			
			if(row==null||row.length==0)
			{
				$.messager.alert('提示',"请至少选择一个角色",'info');
				return;
			}
			$('#roleprivilege').datagrid('options').url='/jlyw/RoleServlet.do?method=11';
			$('#roleprivilege').datagrid('options').queryParams={'queryname':encodeURI($('#privilegename1').combobox('getValue')),'roleId':row.Id};
			$('#roleprivilege').datagrid('clearSelections');
			$('#roleprivilege').datagrid('reload');
		}
	</script>

</head>

<body >

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="给角色授权" />
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
	<table id="roleprivilege" class="easyui-datagrid" iconCls="icon-edit"></table>
	
</div>	
<div border="true" style="width:900px;height:400px;overflow:hidden;">
	<table id="privileges" class="easyui-datagrid" iconCls="icon-edit"></table>
</div>
<form id="frm_role_privilege" method="post">
<input id="sub_role" name="roleId" type="hidden"/>
<input id="sub_privileges" name="privileges" type="hidden"/>
</form>	
</div>


</DIV></DIV>
<div id="table-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addup()">提交选择的权限</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>权限名称：</label><input name="privilegename"  id="privilegename" style="width:200px"></input>&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询权限" id="SearchLocaleMission" onclick="query1()">查询权限</a>
					</td>
				</tr>
			</table>
</div>
<div id="privileges-search-toolbar" style="padding:2px">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveGrant()">保存</a>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="cancelGrant()">注销</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>权限名称：</label><input id="privilegename1"  name="privilegename1" style="width:200px;"  ></input>&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="queryprivilege()" title="查询权限"  plain="true">查询权限</a>
					</td>
				</tr>
			</table>
</div>
</body>
</html>
