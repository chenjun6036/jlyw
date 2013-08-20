<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>机构名称管理</title>
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
				title:'机构信息查询',
				width:900,
				height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'/jlyw/AddressServlet.do?method=3',
				//sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'机构名称',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1||rowData.Status=="1")
							return '<span style="color:red">' + value + '</span>';
						else
							return value;
					}},
					{field:'Address',title:'机构地址',width:100,align:'center'},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},
					{field:'AddressEn',title:'机构地址英文',width:80,align:'center'},
					{field:'HeadName',title:'台头',width:80,align:'center'},
					{field:'HeadNameEn',title:'台头英文',width:80,align:'center'},
					{field:'Tel',title:'联系电话',width:80,align:'center'},
					{field:'Fax',title:'传真',width:80,align:'center'},
					{field:'ZipCode',title:'邮政编码',width:80,align:'center'},
					{field:'ComplainTel',title:'投诉电话',width:80,align:'center'},
					{field:'WebSite',title:'网址',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==0||value=="0"){
							rowData['Status']=0;
							return "正常";
						}
						else if(value==1||value=="1"){
							rowData['Status']=1;
							return '<span style="color:red">过时</span>';
						}
					}}
				]],
				pagination:false,
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
									url:'/jlyw/AddressServlet.do?method=5',
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
				url: '/jlyw/AddressServlet.do?method=4',
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
			
		</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="机构信息管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		
		<table id="table2" style="height:500px; width:900px"></table>
		
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 500px;height: 250px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
						<tr>
							<td align="right">机构名称：</td>
							<td align="left"><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">拼音简码：</td>
							<td align="left"><input id="Brief" name="Brief" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">机构地址：</td>
							<td align="left"><input id="Address" name="Address" class="easyui-validatebox" required="true"/></td>
                            <td align="right">地址英文：</td>
							<td align="left"><input id="AddressEn" name="AddressEn" class="easyui-validatebox"/></td>
						</tr>
						<tr>
							<td align="right">台头名称：</td>
							<td align="left"><input id="HeadName" name="HeadName" class="easyui-validatebox" required="true"/></td>
							<td align="right">台头英文：</td>
							<td align="left"><input id="HeadNameEn" name="HeadNameEn" class="easyui-validatebox"/></td>
                            </tr>
                        <tr>
                            <td align="right">联系电话：</td>
							<td align="left"><input id="Tel" name="Tel" class="easyui-validate"/></td>
                            <td align="right">传&nbsp;&nbsp;&nbsp;&nbsp;真：</td>
							<td align="left"><input id="Fax" name="Fax" class="easyui-validate"/></td>
						</tr>
                        <tr>
                            <td align="right">邮政编码：</td>
							<td align="left"><input id="ZipCode" name="ZipCode" class="easyui-validate"/></td>
                            <td align="right">投诉电话：</td>
							<td align="left"><input id="ComplainTel" name="ComplainTel" class="easyui-validate"/></td>
						</tr>
                        <tr>
                        	<td align="right">网&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
							<td align="left"><input id="WebSite" name="WebSite" class="easyui-validate"/></td>
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
