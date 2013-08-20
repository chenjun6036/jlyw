<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>员工资质信息管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
     <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
     <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
     <script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	 <script>
		$(function(){
		    var lastIndex;
			$('#table2').datagrid({
				title:'员工资质信息查询',
				width:800,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/UserQualServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'EmpName',title:'姓名',width:80,align:'center'},
					{field:'JobNum',title:'工号',width:80,align:'center'},
					{field:'Type',title:'类型',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value == 0||value == '0')
						{
							rowData['Type']=0;
							return "检定员";
						}
						else if(value == 1||value == '1')
						{
							rowData['Type']=1;
							return "检验员";
						}
						else if(value == 2||value == '2')
						{
							rowData['Type']=2;
							return "计量标准考评员";
						}
						else if(value == 3||value == '3')
						{
							rowData['Type']=3;
							return "许可证考评员";
						}
						else
							return value;
					}},
					{field:'QualNum',title:'证书号',width:100,align:'center'},
					{field:'AuthItems',title:'授权项目',width:100,align:'center'},
					{field:'AuthDate',title:'发证时间',width:100,align:'center'},
					{field:'Expiration',title:'有效期',width:80,align:'center'},
					{field:'AuthDept',title:'发证机关',width:100,align:'center'},
					{field:'Remark',title:'备注',width:80,align:'center'}
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
						$('#form1').show();
						
						$('#EmpName').val(select.EmpName);
						$('#JobNum').val(select.JobNum);
						$('#Type').combobox('setValue',select.Type);
						$('#QualNum').val(select.QualNum);
						$('#AuthItems').val(select.AuthItems);
						$('#AuthDate').datebox('setValue',select.AuthDate);
						$('#Expiration').datebox('setValue',select.Expiration);
						$('#AuthDept').val(select.AuthDept);
						$('#Remark').val(select.Remark);
						
						$('#Id').val(select.Id);
					}else{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){
							$.messager.confirm('提示','确认注销吗？',function(r){
							if(r){	
								$.ajax({
									type:'POST',
									url:'/jlyw/UserQualServlet.do?method=4',
									data:'id='+select.Id,
									dataType:"json",
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
		function closed(){
			$('#edit').dialog('close');
		}
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/UserQualServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'JobNum':encodeURI($('#jobnum').val()),'Name':encodeURI($('#name').combobox('getValue'))};
			$('#table2').datagrid('reload');
		}
		
		function edit(){
			$('#form1').form('submit',{
				url:'/jlyw/UserQualServlet.do?method=3',
				onSubmit:function(){
					var time1 = $('#AuthDate').datebox('getValue');
					var time2 = $('#Expiration').datebox('getValue');
					if(time2<time1)
					{
						$.messager.alert('提示',"有效期早于发证日期，输入错误，请重新输入！",'warning');
						return false;
					}
					return $('#form1').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					alert(result.msg);
					if(result.IsOK)
						closed();
					$('#table2').datagrid('reload');
				}
			});
		}
		
		$(function(){
			$('#name').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
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
		});
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="人员资质信息查询管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:800px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:800px">
				<tr>
					<td width="331" align="right">工号：</td>
			      <td width="172" align="left"><input id="jobnum" name="JobNum" type="text" /></td>
					<td width="48" align="right">姓名：</td>
				  <td width="168" align="left"><input id="name" name="Name" class="easyui-combobox" valueField="name" textField="name" url="" mode="remote" panelHeight="150px" style="width:152px"/></td>
					<td width="57"><a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:800px"></table>
		
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="form1" method="post">
				<div>
					<table id="table3" style="padding: 10px;width: 800;height: 500;">
					<input id="Id" name="Id" type="hidden"/>
						<tr>
							<td align="right">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
							<td align="left"><input id="EmpName" name="EmpName" type="text" editable="false" readonly="readonly"/></td>
							<td align="right">工&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
							<td align="left"><input id="JobNum" name="JobNum" type="text" editable="false" readonly="readonly"/></td>
						</tr>
						<tr>
							<td align="right">类&nbsp;&nbsp;&nbsp;&nbsp;型：</td>
							<td align="left">
								<select id="Type" name="Type" class="easyui-combobox" style="width:152px" editable="false" panelHeight="auto">
									<option value="0">检定员</option>
									<option value="1">检验员</option>
									<option value="2">计量标准考评员</option>
									<option value="3">许可证考评员</option>
								</select>
							</td>
							<td align="right">证&nbsp;书&nbsp;号：</td>
							<td align="left"><input id="QualNum" name="QualNum" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">授权项目：</td>
							<td align="left"><input id="AuthItems" name="AuthItems" type="text"/></td>
							<td align="right">发证时间：</td>
							<td align="left"><input id="AuthDate" name="AuthDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
						</tr>
						<tr>
							<td align="right">有&nbsp;效&nbsp;期：</td>
							<td align="left"><input id="Expiration" name="Expiration" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
							<td align="right">发证机关：</td>
							<td align="left"><input id="AuthDept" name="AuthDept" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
							<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
						</tr>
						<tr>	
							<td></td>
							<td align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">修改</a></td>
							<td></td>
							
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
							<td></td>
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
