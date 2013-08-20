<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<title>原因管理</title>
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
			$("#reasontype").combobox({
				url:'/jlyw/ReasonServlet.do?method=3',
				valueField:'Type',
				textField:'TypeName',
				editable:false,
				onSelect:function(record){
					//$("#MappingUrl").val(record.url);
					//$("#ResourcesId").val(record.id);
				},
				onChange:function(newValue, oldValue){
					
				}
					
			});
			
			$("#ReasonType").combobox({
				url:'/jlyw/ReasonServlet.do?method=3',
				valueField:'Type',
				textField:'TypeName',
				editable:false,
				onSelect:function(record){
					//$("#MappingUrl").val(record.url);
					//$("#ResourcesId").val(record.id);
				},
				onChange:function(newValue, oldValue){
					
				}
					
			});
			
			$('#reason-table').datagrid({
				title:'原因信息',
//				iconCls:'icon-save',
				singleSelect:true, 				
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/ReasonServlet.do?method=1',
				sortName: 'Type',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[			
					{field:'Type',title:'原因类型',width:100,align:'center',sortable:true,
						formatter:function(value,rowData,rowIndex){
							if (value=="11"){
								return "退样申请";
							} else if (value=="12"){
								return "超期申请";
							}else if (value=="13"){
								return "折扣申请";
							}else if (value=="21"){
								return "注销转包方";
							}else if (value=="22"){
								return "注销委托单位";
							}
						}
					},
					{field:'Reason',title:'原因描述',width:200,align:'center'},
					{field:'LastUse',title:'上次使用时间',width:150,align:'center'},
					{field:'Count',title:'使用次数',width:60,align:'center'}
					
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
							$('#form1').form('validate');
						}
					},'-',{
						text:'修改',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#reason-table').datagrid('getSelected');
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
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						var rows = $('#reason-table').datagrid('getSelected');
							if(rows)
							{
								$.messager.confirm('警告','确认删除吗？',function(r){
								if(r){
									
									$.ajax({
										type:'POST',
										url:'/jlyw/ReasonServlet.do?method=2',
										data:'id='+rows.Id,
										dataType:"json",
										success:function(data, textStatus){
											
											$.messager.alert('提示',data.msg,'info');
										}
									});
									
									$('#reason-table').datagrid('reload');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				}]
			});
		});
		
		function Submit(){
			$('#form1').form('submit',{
				url: '/jlyw/ReasonServlet.do?method=4',
				onSubmit:function(){ return $('#form1').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
					$('#reason-table').datagrid('reload');
		   		 }
			});
		}
		
		function closed(){
			$('#edit').window('close');
		}

		function query()
		{
			$('#reason-table').datagrid('options').url='/jlyw/ReasonServlet.do?method=1';
			$('#reason-table').datagrid('options').queryParams={'queryname':encodeURI($('#reasontype').combobox('getValue'))};
			//$('#reason-table').datagrid('clearSelections');
			$('#reason-table').datagrid('reload');
		}
	</script>

</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="原因管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<table width="98%" height="90%" border="0">
		<tr >
			<td style="padding-top:15px; padding-right:12px; vertical-align:bottom;" align="right">
			<form id="ffEE" method="post">
				<div>
					
					<label id="label_dd" >原因:</label>
					<div id="div-reasontype" style="display:inline">
						<input name="reasontype"  id="reasontype" style="width:200px;"></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>
				</div>
			</form>
			</td>
		</tr>
		<tr height="650px">
			<td>
				<table id="reason-table" iconCls="icon-edit" ></table>
			</td>
		</tr>
	</table>
	
	<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 500px;height: 150px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
						<tr>
							<td align="right">原因名称：</td>
							<td align="left"><input id="Name" name="Reason" class="easyui-validatebox" required="true"/></td>
						</tr>
                        <tr>
                       	    <td align="right">类&nbsp;&nbsp;&nbsp;&nbsp;型：</td>
                            <td>
								<select id="ReasonType" name="Type" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
								</select>
							</td>
                            <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
                            <td>
								<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">正常</option>
									<option value="1">注销</option>
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
</div>	
</body>
</html>
