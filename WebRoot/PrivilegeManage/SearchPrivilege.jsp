<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<title></title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		    var lastIndex;
			$("#ResourcesName").combobox({
						url:'/jlyw/SysResourcesServlet.do?method=1',
						valueField:'id',
						textField:'name',
						editable:false,
						onSelect:function(record){
							$("#MappingUrl").val(record.url);
							$("#ResourcesId").val(record.id);
						},
						onChange:function(newValue, oldValue){
							
						}
			});
			$("#privilegename").combobox({
				valueField:'name',
				textField:'name',
				required:true,
				width:200,
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
			
			$('#privilege').datagrid({
				title:'权限信息',
//				iconCls:'icon-save',
				singleSelect:true, 
				
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/PrivilegeServlet.do?method=2',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'权限名称',width:300},
					{field:'ResourcesName',title:'对应资源名称',width:300},
					{field:'MappingUrl',title:'对应资源URL',width:150},
					{field:'Parameters',title:'资源URL的参数',width:150},
					{field:'Description',title:'权限描述',width:150}
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					  text:'修改',
					  iconCls:'icon-edit',
					  handler:function(){
						var select = $('#privilege').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#ff').show();
						
						$('#Id').val(select.Id);
						$('#Name').val(select.Name);
						$('#ResourcesName').combobox('setValue',select.ResourcesName);
						$('#Description').val(select.Description);
						$('#MappingUrl').val(select.MappingUrl);
						$('#Parameters').val(select.Parameters);
						$('#ResourcesId').val(select.ResourcesId);
						
						id = select.id;
					}else{
						$.messager.alert('warning','请选择一行数据','warning');
			}
					}
				},'-',{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						var rows = $('#privilege').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('警告','确认删除吗？',function(r){
								if(r){
									for(var i=rows.length-1; i>=0; i--){
										$.ajax({
											type:'POST',
											url:'/jlyw/PrivilegeServlet.do?method=4',
											data:'id='+rows[i].Id,
											dataType:"json",
											success:function(data, textStatus){
												
												alert(data.msg);
											}
										});
									}
									$('#privilege').datagrid('reload');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
					if (lastIndex != rowIndex){
						$('#privilege').datagrid('endEdit', lastIndex);
						$('#privilege').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
				}
			});
		});
		function closed(){
			$('#edit').dialog('close');
		}
		function edit(){
			$('#ff').form('submit', {
				url:'/jlyw/PrivilegeServlet.do?method=3',
				success : function(data) {
					var result = eval("("+data+")");
		   			alert(result.msg);
		   			if(result.IsOK)
		   				close();
		   			$('#privilege').datagrid('reload');
				},
				onSubmit : function() {
					return $(this).form('validate');
				}
			});
		}
		function query()
		{
			$('#privilege').datagrid('options').url='/jlyw/PrivilegeServlet.do?method=2';
			$('#privilege').datagrid('options').queryParams={'queryname':encodeURI($('#privilegename').combobox('getValue'))};
			$('#privilege').datagrid('clearSelections');
			$('#privilege').datagrid('reload');
		}
	</script>

</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="权限查删改" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<table width="98%" height="90%" border="0">
		<tr >
			<td style="padding-top:15px; padding-right:12px; vertical-align:bottom;" align="right">
			<form id="ffEE" method="post">
				<div>
					
					<label id="label_dd" >权限名称:</label>
					<div id="privilegename" style="display:inline">
						<input name="privilegename"  id="privilegename" style="width:200px;"></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
				</div>
			</form>
			</td>
		</tr>
		<tr height="650px">
			<td>
				<table id="privilege" iconCls="icon-edit" ></table>
			</td>
		</tr>
	</table>
	<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 320;height: 250;" 
    iconCls="icon-edit" closed="true" modal="true" maximizable="false" minimizable="false" collapsible="false">
    	<div id="ee">
		
			<form id="ff" method="post">
			<input type="hidden" name="Id" id="Id" value="" />
				<div>
					<label >权限名称：</label><input id="Name" style="width:200px"  name="Name" class="easyui-validatebox" required="true"></input>
				 </div>
				 <div>
					<label >资源名称：</label><input id="ResourcesName" style="width:202px"  name="ResourcesName" class="easyui-combobox" required="true"></input>
				 </div>
				 <div>
					<label >资源 URL：</label><input id="MappingUrl" style="width:200px"  name="MappingUrl" class="easyui-validatebox" readonly="readonly"><input id="ResourcesId" style="width:200px"  name="ResourcesId" type="hidden" class="easyui-validatebox" required="true">
				 </div>
				 <div >
					<label >参&nbsp;&nbsp;&nbsp;&nbsp;数：</label><input id="Parameters" style="width:200px"  name="Parameters" ></input>
				 </div>
				 <div >
					 <table>
					 <tr>
					 	<td>
							<label style="valign:top" >权限描述：</label>
						</td>
						<td>
							<textarea id="Description" style="width:200px;height:100px"  name="Description"  ></textarea>
						</td>
					</tr>
				</table>
				 </div>
			 </form>
    	</div>
	    <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="edit()">修改</a>
	    <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="closed()">关闭</a>
    </div>

</div>
</div>	
</body>
</html>
