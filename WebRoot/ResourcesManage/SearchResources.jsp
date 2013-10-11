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
     <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		    var lastIndex;
			$("#resourcesname").combobox({
				url:'/jlyw/SysResourcesServlet.do?method=1',
						valueField:'name',
						textField:'name',
						onSelect:function(record){
							//$("#MappingUrl").val(record.url);
							//$("#ResourcesId").val(record.id);
						},
						onChange:function(newValue, oldValue){
							
						}
					
			});
			
			$('#resources').datagrid({
				title:'系统资源信息',
//				iconCls:'icon-save',
				singleSelect:true, 				
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/SysResourcesServlet.do?method=3',
				sortName: 'Id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'系统资源名称',width:150},
					{field:'MappingUrl',title:'对应资源URL',width:250},
					{field:'Description',title:'系统资源描述',width:150}
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					  text:'修改',
					  iconCls:'icon-edit',
					  handler:function(){
						var select = $('#resources').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#ff').show();
						
						$('#Id').val(select.Id);
						$('#Name').val(select.Name);
						$('#Description').val(select.Description);
						$('#MappingUrl').val(select.MappingUrl);
						
						id = select.id;
					}else{
						$.messager.alert('warning','请选择一行数据','warning');
			}
					}
				},'-',{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						var rows = $('#resources').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('警告','确认删除吗？',function(r){
								if(r){
									for(var i=rows.length-1; i>=0; i--){
										$.ajax({
											type:'POST',
											url:'/jlyw/SysResourcesServlet.do?method=5',
											data:'id='+rows[i].Id,
											dataType:"json",
											success:function(data, textStatus){
											
												//$.messager.alert('提示',data.msg,'info');
											}
										});
									}
									$('#resources').datagrid('reload');
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
						$('#resources').datagrid('endEdit', lastIndex);
						$('#resources').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
				}
			});
		});
		function closed(){
			$('#edit').window('close');
		}
		function edit(){
			$('#ff').form('submit', {
				url:'/jlyw/SysResourcesServlet.do?method=4',
				success : function(data) {
					var result = eval("("+data+")");
		   			alert(result.msg);
		   			if(result.IsOK)
		   				close();
		   			$('#resources').datagrid('reload');
				},
				onSubmit : function() {
					return $(this).form('validate');
				}
			});
		}
		function query()
		{
			$('#resources').datagrid('options').url='/jlyw/SysResourcesServlet.do?method=3';
			$('#resources').datagrid('options').queryParams={'queryname':encodeURI($('#resourcesname').combobox('getText'))};
			//$('#resources').datagrid('clearSelections');
			$('#resources').datagrid('reload');
		}
	</script>

</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="系统资源查删改" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<table width="98%" height="90%" border="0">
		<tr >
			<td style="padding-top:15px; padding-right:12px; vertical-align:bottom;" align="right">
			<form id="ffEE" method="post">
				<div>
					
					<label id="label_dd" >系统资源名称:</label>
					<div id="div-resourcesname" style="display:inline">
						<input name="resourcesname"  id="resourcesname" style="width:200px;"></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
				</div>
			</form>
			</td>
		</tr>
		<tr height="650px">
			<td>
				<table id="resources" iconCls="icon-edit" ></table>
			</td>
		</tr>
	</table>
	<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 320;height: 250;" 
    iconCls="icon-edit" closed="true" modal="true" maximizable="false" minimizable="false" collapsible="false">
    	<div id="ee">
		
			<form id="ff" method="post">
			<input type="hidden" name="Id" id="Id" value="" />
				<div>
					<label >系统资源名称：</label><input id="Name" style="width:200px"  name="Name" class="easyui-validatebox" required="true"></input>
				 </div>
				 <div>
					<label >对应资源 URL：</label><input id="MappingUrl" style="width:200px"  name="MappingUrl" class="easyui-validatebox" required="true"></input>
				 </div>
				 <div >
				  <table>
					 <tr>
					 	<td>
							<label style="valign:top" >系统资源描述：</label>
						</td>
					<td>
							<textarea id="Description" style="width:200px;height:100px"  name="Description" ></textarea>
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
