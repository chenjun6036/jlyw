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
	///////////////////////////////////////////////////////////////////
	$(function(){
	$("#insideContactor").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				//$("#jobNum1").val(record.jobNum);
				$('#insideContactorId').val(record.id);
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
					var newValue = $('#insideContactor').combobox('getText');
					$('#insideContactor').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	});
	///////////////////////////////////////////////////////////////////
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=31';
		$('#table1').datagrid('options').queryParams={'InsideContactorId':/* encodeURI */($('#insideContactorId').val()),'StartDate':$('#startDate').datebox('getValue'),'EndDate':$('#endDate').datebox('getValue')};
		$('#table1').datagrid('reload');
	}
		$(function(){
		$('#table1').datagrid({
				title:'内部联系人产值分配统计信息',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=31',
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
					{field:'InsideContactor',title:'联系人',width:100,align:'center'},
					{field:'Year',title:'分配年份',width:100,align:'center'},
					{field:'Fee',title:'分配额度',width:80,align:'center'},
					{field:'LastEditTime',title:'最后编辑日期',width:80,align:'center'},
					{field:'LastEditor',title:'最后编辑人',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,align:'center'},
					//{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
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
			
		function closed()
		{
			$('#editInsideContactor').dialog('close');
			$('#del').dialog('close');
			//$('#table1').datagrid('reload');
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
			<jsp:param name="TitleName" value="查询内部联系人产值分配信息" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=12">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="联系人产值分配信息">
		
		<tr>
			<td width="20%" align="right">内部联系人姓名：</td>
			<td align="left" >
			<input name="InsideContactor" id="insideContactor" style="width:200px" class="easyui-combobox" ></input>
			<input id="insideContactorId" name="InsideContactorId" type="hidden"/>
			</td>
			
			<td align="right">工号：
			</td>
			<td><input id="jobNum" class="easyui-validatebox"/>
			</td>
		</tr>
		 <tr height="30px">
                	<td align="right">时间：</td>
				  	<td align="left">
						<input name="StartDate" id="startDate" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="EndDate" id="endDate" style="width:152px;" class="easyui-datebox" />
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
