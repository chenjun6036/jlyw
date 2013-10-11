<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>内部联系信息管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
        <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
$(function(){
	$("#customerName1").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				$("#customerId1").val(record.id);
				
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
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $("#customerName1").combobox('getText');
					$("#customerName1").combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}, 400);
		}
	});
	});

	

	
	///////////////////////////////////////////////////////////////////
	$(function(){
	$("#contactorName").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#jobNum").val(record.jobNum);
				if($("#contactorId").val()!=record.id)
				$("#contactorId").val(record.id);
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
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $("#contactorName").combobox('getText');
					$("#contactorName").combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	} );
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=8';
		$('#table1').datagrid('options').queryParams={'CustomerId':encodeURI($('#customerName1').combobox('getText'))};
		$('#table1').datagrid('reload');
	}

		function cancel(){
			$('#frm_add_conInfo').form('clear');
		}
		
		
		$(function(){
		$('#table1').datagrid({
				title:'内部联系人信息',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=8',
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'编号',width:50,align:'center'},
					
					{field:'CustomerName',title:'联系单位',width:150,align:'center'},
					{field:'Address',title:'单位地址',width:200,align:'center'},
					{field:'ContactorName',title:'联系人',width:100,align:'center'},
					{field:'JobNum',title:'联系人工号',width:80,align:'center'},
					{field:'Cellphone1',title:'联系人电话',width:80,align:'center'},
					{field:'Role',title:'角色',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 1 || value == '1')
							{
								rowData['Role']=1;
							    return "A";
							}
							else
							{
								rowData['Role']=2;
								return "B";
							}
							
						}},
					{field:'ContactorId',title:'Id',width:80,align:'center',hidden:true},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table1').datagrid('getSelected');
						if(select){
						$('#editInsideContactor').window('open');
						$('#form2').show();
						$('#Id').val(select.Id);
						$('#form2').form('load',select);
						$('#form2').form('validate');
					}else{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table1').datagrid('getSelected');
							if(selected){
								$('#del').window('open');
								$('#ff1').show();
								$('#del_id').val(selected.Id);
						}
						else{
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'导出',
						iconCls:'icon-save',
						handler:function(){
							ShowWaitingDlg("正在导出，请稍后......");
							$('#par').val($('#customerId1').val());
							$('#frm_export').form('submit',{
								success:function(data){
									var result = eval("("+ data +")");
									if(result.IsOk)
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
				onClickRow:function(rowIndex, rowData){
			
				}
			});
			});
			
		function SubmitContactor(){
		closed();
			$('#form2').form('submit',{
				url: '/jlyw/CrmServlet.do?method=9',
				onSubmit:function(){return $('#form2').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#table1').datagrid('reload');		
		   		}
			});
		}
		function closed()
		{
			$('#editInsideContactor').dialog('close');
			$('#del').dialog('close');
			//$('#table1').datagrid('reload');
		}
		function del(){
		closed();
			$('#ff1').form('submit',{
				url:'/jlyw/CrmServlet.do?method=10',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
			   		$.messager.alert('提示',result.msg,'info');
			   		if(result.IsOK)
			   		 closed();
			   		$('#table1').datagrid('reload');	
				}
			});
		}
		function cancel()
		{
		$('#query1').form('clear');
		}
		
	s
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="内部联系信息维护" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	</div>
	<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=2">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="查询内部联系人信息">
		
		<tr>
			<td width="20%" align="right">联&nbsp;系&nbsp;单&nbsp;位：</td>
			<td align="left" >
			<input name="CustomerName1" id="customerName1" style="width:310px" class="easyui-combobox" ></input>
			<input id="customerId1" name="CustomerId1" type="hidden"/>
			</td>
			<td align="center">单位地址：
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="query()">查询</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cancel()">重置</a></td>
			<td ></td>

		</tr>
		
	</table>
	</form>
<table id="table1"></table>
	<br />
	 <div id="editInsideContactor" class="easyui-window" title="修改" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					
						<tr>
							<td align="right">联系公司：</td>
							<td align="left" colspan="3"><input id="customerName" name="CustomerName" class="easyui-validatebox"  />
							<input id="Id" name="Id" type="hidden"/>
                    <input id="contactorId" name="ContactorId" type="hidden"/>
                    </td>
                        </tr>
                        <tr>
							<td align="right">联系人：</td>
							<td align="left"><input id="contactorName" name="ContactorName" class="easyui-combobox" required="true"/></td>
							<td align="right">联系人工号：</td>
							<td align="left"><input id="jobNum" name="JobNum" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
							<td align="right">角色：</td>
							<td align="left"><select id="role" name="Role" class="easyui-combobox" required="true">
							<option value="1" >A</option>
							<option value="2">B</option>
							</select></td>
							
						</tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="SubmitContactor()">提交</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	<br />
	<div id="del" class="easyui-window" title="确认删除" style="width:280px;height:130px;" 
    iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false">
    <form id="ff1" method="post">
    	<table>
    	<tr>
    	<td></td>
    	<td>确认删除吗？</td>
    	</tr>
    	<tr height="40px">	
			<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="del()">确认</a>
			<input id="del_id" name="Del_id" type="hidden"/>
			</td>
			<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
		</tr>
    	</table>
        </form>
        </div>
</DIV>
</DIV>
</body>
</html>
