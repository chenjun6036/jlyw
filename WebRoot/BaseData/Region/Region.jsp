<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>地区管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"  src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){			
			$('#table2').datagrid({
				title:'地区信息查询',
				width:900,
				height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'/jlyw/RegionServlet.do?method=3',
				//sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'地区',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1||rowData.Status=="1")
							return '<span style="color:red">' + value + '</span>';
						else
							return value;
					}},
					{field:'Code',title:'地区代码',width:100,align:'center'},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==0||value=="0"){
							rowData['Status']=0;
							return "正常";
						}
						else if(value==1||value=="1"){
							rowData['Status']=1;
							return '<span style="color:red">注销</span>';
						}
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'新增',
						iconCls:'icon-add',
						handler:function(){
							$('#edit').window('open');
							$('#form1').show();
							$('#form1').form('clear');
							var clickname="新增";
							$('#edit').panel({title:clickname});
						}
					},'-',{
						text:'修改',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){
								$('#edit').window('open');
								$('#form1').show();
								var clickname="修改";
								$('#edit').panel({title:clickname});
								$('#form1').form('load',select);
								$('#form1').form('validate');
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
						text:'注销',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){
								$.messager.confirm('提示','确认注销吗？',function(r){
								if(r){
									$.ajax({
									type:'POST',
									url:'/jlyw/RegionServlet.do?method=4',
									data:"id="+select.Id,
									dataType:"html",
									success:function(data){
										$('#table2').datagrid('reload');
									}
								});
								}
							});	
							}
							else
							{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				}]
			});
			
		});
		
		function Submit(){
			$('#form1').form('submit',{
				url: '/jlyw/RegionServlet.do?method=1',
				onSubmit:function(){ return $('#form1').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
					$('#table2').datagrid('reload');
		   		 }
			});
		}
		
		function closed(){
			$('#edit').dialog('close');
		}
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/RegionServlet.do?method=3';
			$('#table2').datagrid('options').queryParams={'Status':encodeURI($('#status').combobox('getValue'))};
			$('#table2').datagrid('reload');
		}
			
		</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="地区信息管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		
        <div>
			<br />
			<table id="table1" style="width:900px">
				<tr>
					<td align="right">状态：</td>
				   <td width="300" align="left"><select id="status" name="status" class="easyui-combobox" style="width:152px" panelHeight="auto"  editable="false">
									<option value="0">正常</option>
									<option value="1">注销</option>
								</select>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询
					</a></td>
				</tr>
			</table>
		</div>

		<table id="table2" style="height:500px; width:900px"></table>
		
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 500px;height: 150px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
						<tr>
							<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
							<td align="left"><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">拼音简码：</td>
							<td align="left"><input id="Brief" name="Brief" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">地区代码：</td>
							<td align="left"><input id="Code" name="Code" class="easyui-validatebox" required="true"/></td>
                            <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
                            <td>
								<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">正常</option>
									<option value="1">过时</option>
								</select>
							</td>
                        </tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="Submit()">提交</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</DIV>
</DIV>
</body>
</html>
