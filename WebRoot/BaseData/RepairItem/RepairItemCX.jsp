<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>器具修理项管理</title>
	 <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
     <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
     <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
     <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	 <script>
		$(function(){
		    var lastIndex;
			$('#table2').datagrid({
				title:'器具修理项信息查询',
				width:800,
				height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'repairItem_data.json',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Id',title:'编号',width:40,align:'center'},
					{field:'StandardNameId',title:'器具标准名称编号',width:120,align:'center'},
					{field:'StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'Item',title:'修理内容',width:120,align:'center'},
					{field:'LastUse',title:'上次使用时间',width:100,align:'center'},
					{field:'Count',title:'使用次数',width:100,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#from1').show();
						
						$('#StandardNameId').val(select.StandardNameId);
						$('#StandardName').val(select.StandardName);
						$('#Item').val(select.Item);
						
						id = select.Id; 
					}else{
						$.messager.alert('warning','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select)
							{
								var rows = $('#table2').datagrid('getSelections');
								var length = rows.length;
								for(var i=length-1; i>=0; i--){
									var index = $('#table2').datagrid('getRowIndex', rows[i]);
									$('#table2').datagrid('deleteRow', index);
								}
							}else{
								$.messager.alert('warning','请选择一行数据','warning');
							}
						}
				}]
			});
		});
		function cancel(){
			$('#edit').dialog('close');
		}
		function edit(){}
		</script>
</head>

<body class="easyui-layout">
	<div style="width:800px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:800px">
				<tr>
					<td width="272" align="right">器具标准名称编号：</td>
			      <td width="168" align="left"><input name="SpecificationCode" type="text" /></td>
					<td width="111" align="right">器具标准名称：</td>
				  <td width="168" align="left"><input name="nameCn" type="text" /></td>
					<td width="57"><a href="javascript:$('#ff').form('submit',{
										url:'',
										onSubmit:function(){
											return $(this).form('validate');
										},
										success:function(data){
										}
									});" class="easyui-linkbutton" iconCls="icon-search">Search
					</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:800px"></table>
		
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="form1">
				<div>
					<table id="table3" style="padding: 10px;width: 800;height: 500;">
						<tr>
							<td align="right">器具标准名Id&nbsp;&nbsp;：</td>
							<td align="left"><input id="StandardNameId" name="StandardNameId" type="text" /></td>
							<td align="right">器具标准&nbsp;&nbsp;<br />名&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
							<td align="left"><input id="StandardName" name="StandardName" type="text" /></td>
						</tr>
						<tr>
							<td align="right">修理内容：</td>
							<td align="left" colspan="3"><textarea id="Item" name="Item" cols="50" rows="4"></textarea></td>
						</tr>
						<tr>	
							<td></td>
							<td align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">修改</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
	</div>
</body>
</html>
