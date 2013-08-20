<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	 <title>�û���Ϣ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	<link rel="stylesheet" type="text/css" href="demo.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script>
		$(function(){
		    var lastIndex;
			
			
			$('#test').datagrid({
				title:'�û���Ϣ',
//				iconCls:'icon-save',
				width:700,
				height:350,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				sortName: 'userid',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'userid',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {title:'�û���Ϣ',colspan:5},
					{field:'opt',title:'�������',width:100,align:'center', rowspan:2,
						formatter:function(value,rec){
							return '<span style="color:red">δ����</span>';
						}
					}
				],[
					{field:'userid',title:'�û����',width:120,sortable:true,editor:'text'},
					{field:'name',title:'����',width:150,editor:'text'},
					{field:'username',title:'�û���',width:150,editor:'text'},
					{field:'password',title:'����',width:150,editor:'text'},
					{field:'department',title:'����',width:120,editor:'text'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'append',
					iconCls:'icon-add',
					handler:function(){
						$('#test').datagrid('endEdit', lastIndex);
						$('#test').datagrid('appendRow',{
							userid:'',
							name:'',
							username:'',
							password:'',
							department:'',
							opt:'δ����'
						});
						lastIndex = $('#test').datagrid('getRows').length-1;
						$('#test').datagrid('selectRow', lastIndex);
						$('#test').datagrid('beginEdit', lastIndex);
					}
				},'-',{
					  text:'modify',
					  iconCls:'icon-edit',
					  handler:function(){
						var select = $('#test').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#ff').show();
						
						$('#name').val(select.name);
						$('#username').val(select.username);
						$('#department').val(select.department);
						$('#password').val(select.password);
						$('#apassword').val(select.password);
						id = select.id;
					}else{
						$.messager.alert('warning','��ѡ��һ������','warning');
			}
					}
				},'-',{
					text:'delete',
					iconCls:'icon-remove',
					handler:function(){
						var row = $('#test').datagrid('getSelected');
						if (row){
							var index = $('#test').datagrid('getRowIndex', row);
							$('#test').datagrid('deleteRow', index);
						}
					}
				},'-',{
					text:'accept',
					iconCls:'icon-save',
					handler:function(){
						$('#test').datagrid('acceptChanges');
					}
				},'-',{
					text:'reject',
					iconCls:'icon-undo',
					handler:function(){
						$('#test').datagrid('rejectChanges');
					}
				},'-',{
					text:'GetChanges',
					iconCls:'icon-search',
					handler:function(){
						var rows = $('#test').datagrid('getChanges');
						alert('changed rows: ' + rows.length + ' lines');
					}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
					if (lastIndex != rowIndex){
						$('#test').datagrid('endEdit', lastIndex);
						$('#test').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
				}
			});
		});
	</script>

</head>

<body class="easyui-layout" border="false" style="padding:5px">
<div region="center" border="false" style="background:#fafafa;overflow:hide; padding:5px">
	<table width="100%" height="100%">
		<tr>
			<td width="100%" style="padding-top:15px; padding-right:12px; vertical-align:bottom; overflow:visible" align="right">
			<form id="ff" method="post">
				<div>
					
					<label for="type">����:</label>
					<input class="easyui-combobox" name="type" url="combobox_log_type.json" valueField="id" textField="text" panelHeight="auto"></input>
					&nbsp;&nbsp;&nbsp;
					
					<label id="label_dd" for="dd">�û���:</label>
					<div id="div_username" style="display:inline">
						<input id="dd" class="easyui-combobox" name="type" url="datagrid_data2.json"  valueField="id" textField="text" panelHeight="auto" ></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:$('#ff').form('submit',{
								url:'',
								onSubmit:function(){
									return $(this).form('validate');
								},
								success:function(data){
								}
							});" class="easyui-linkbutton" iconCls="icon-search">Search</a>
				</div>
			</form>
			</td>
		</tr>
		<tr height="100%">
			<td width="100%">
				<table id="test" iconCls="icon-edit" singleSelect="true" ></table>
			</td>
		</tr>
	</table>
 <div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 300;height: 200;" 
    iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
    	<div id="ee">
		
    <form id="ff" method="post">
		    	 <div>
	           		 ��&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; ��:<input class="easyui-validatebox" type="text" id="name" name="name" required="true"></input>
	        	 </div>
	        	 <div>
	        	 	��&nbsp; �� &nbsp;��:<input class="easyui-numberbox" type="text" id="username" name="username" required="true"></input>
	        	 </div>
	        	 <div>
	        	 	��������:<input class="easyui-validatebox" type="text" id="department" name="department" required="true"></input>
	        	 </div>
	    		 <div>
	    			��&nbsp; &nbsp;&nbsp;&nbsp; &nbsp; ��:<input class="easyui-validatebox" type="password" id="password" name="password" required="true"/>
	    		 </div>
	    		 <div>
	    		 	ȷ������:<input class="easyui-validatebox" type="password" id="apassword" name="apassword" required="true"/>
	    		 </div>
	 </form>
    	</div>
	    <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="edit()">�޸�</a>
	    <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="close2()">ȡ��</a>
    </div>	

</div>	
</body>
</html>
