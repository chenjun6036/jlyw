<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>反馈信息管理</title>
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
		//required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				$('#customerId1').val(record.id);
				
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
					var newValue = $('#customerName1').combobox('getText');
					$('#customerName1').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}, 400);
		}
	});
	});
	///////////////////////////////////////////////////////////////////
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=19';
		$('#table1').datagrid('options').queryParams={'CustomerId':/* encodeURI */($('#customerId1').val()),'Status':$('#status').combobox('getValue')};
		$('#table1').datagrid('reload');
	}
		$(function(){
		$('#table1').datagrid({
				title:'服务费用统计信息',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=18',
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'编号',width:50,align:'center'},
					
					{field:'CustomerName',title:'委托单位',width:150,align:'center'},
					{field:'BillNum',title:'票据号',width:100,align:'center'},
					{field:'CreateTime',title:'创建时间',width:100,align:'center'},
					{field:'CreateSysUser',title:'创建者',width:80,align:'center'},
					{field:'PaidTime',title:'支付日期',width:80,align:'center'},
					{field:'PaidVia',title:'支付方式',width:80,align:'center',
					formatter:function(value,rowData,index)
					{
					if(value=='0'||value==0)
					{
						rowData['PaiVia']=0;
						return '现金';
					}
					else if(value=='1'||value==1)
					{
						rowData['PaiVia']=1;
						return '信用卡';
					}
					else if(value=='2'||value==2)
					{
						rowData['PaiVia']=2;
						return '其它';
					}
					}
					},
					{field:'PaidSysUser',title:'支付人',width:80,align:'center'},
					{field:'PaidSubject',title:'费用科目',width:80,align:'center'},
					{field:'Money',title:'金额',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,align:'center'},
					{field:'Status',title:'审核状态',width:80,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value=='1'||value==1)
						{
							rowData['Status']=1;
							return '<span style="color:green">正在审核</span>';
						}
						if(value=='2'||value==2)
						{
							rowData['Status']=2;
							return '已通过';
						}
						if(value=='3'||value==3)
						{
							rowData['Status']=3;
							return '<span style="color:red">未通过</span>';
						}
					}
					},
					//{field:'Attachment',title:'附件',width:80,align:'center'},
					//{field:'ContactorId',title:'Id',width:80,align:'center',hidden:true},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				toolbar:[{
						text:'导出',
						iconCls:'icon-save',
						handler:function(){
							ShowWaitingDlg("正在导出，请稍后......");
							$('#par').val(JSON.stringify($('#table1').datagrid('options').queryParams));
							$('#frm_export').form('submit',{
								success:function(data){
									var result = eval("("+ data +")");
									if(result.IsOK)
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
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="服务费用统计" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	</div>
<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=5">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="查询服务费用信息">
		
		<tr>
			<td width="20%" align="right">联系单位：</td>
			<td align="left" >
			<input name="CustomerName1" id="customerName1" style="width:200px" class="easyui-combobox" ></input>
			<input id="customerId1" name="CustomerId1" type="hidden"/>
			</td>
			
			<td align="center">单位地址：
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
		<td align="right">审核状态：
			</td>
			<td><select id="status" Name="Status" class="easyui-combobox" panelHeight="auto">
			<option value="4" >全部</option>
			<option value="1">正在审核</option>
			<option value="2">已通过</option>
			<option vaule="3">未通过</option>
			</select>
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
</DIV>
</DIV>
</body>
</html>
