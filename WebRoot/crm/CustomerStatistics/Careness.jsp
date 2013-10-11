<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>关怀信息统计</title>
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
	$("#customerName").combobox({
		valueField:'name',
		textField:'name',
		//required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				//$('#customerId1').val(record.id);
				
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
					var newValue = $('#customerName').combobox('getText');
					$('#customerName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}, 400);
		}
	});
	});
	///////////////////////////////////////////////////////////////////
	$(function(){
	$("#careDutySysUser").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				//$("#jobNum1").val(record.jobNum);
				//$('#careDutyManId').val(record.id);
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
					var newValue = $('#careDutySysUser').combobox('getText');
					$('#careDutySysUser').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	});
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=28';
		$('#table1').datagrid('options').queryParams={'CustomerName':encodeURI($('#customerName').combobox('getValue')),/* 'Status':$('#status').combobox('getValue'), */'CustomerLevel':$('#customerLevel').combobox('getValue'),'CareContactor':encodeURI($('#careContactor').val()),'CareDutySysUser':encodeURI($('#careDutySysUser').combobox('getValue')),'StartTime':$('#startTime').datebox('getValue'),'EndTime':$('#endTime').datebox('getValue'),'Way':$('#way').combobox('getValue')};
		$('#table1').datagrid('reload');
	}
		$(function(){
		$('#table1').datagrid({
				title:'关怀统计信息',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=28',
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'编号',width:50,align:'center'},
					
					{field:'CustomerName',title:'关怀单位',width:150,align:'center'},
					/* {field:'Priority',title:'优先级',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value=='0'||value==0)
						{
							rowData['Priority']=0;
							return '低';
						}
						if(value=='1'||value==1)
						{
							rowData['Priority']=1;
							return '中';
						}
						if(value=='2'||value==2)
						{
							rowData['Priority']=2;
							return '高';
						}
					}
					}, */
					
					{field:'Time',title:'关怀日期',width:80,align:'center'},
					{field:'Way',title:'关怀方式',width:80,align:'center',
					formatter:function(value,rowData,index)
					{
					if(value=='0'||value==0)
					{
						rowData['Way']=0;
						return '电话';
					}
					else if(value=='1'||value==1)
					{
						rowData['Way']=1;
						return '短信';
					}
					else if(value=='2'||value==2)
					{
						rowData['Way']=2;
						return '礼品赠送';
					}
					else if(value=='3'||value==2)
					{
						rowData['Way']=3;
						return '宴请';
					}
					else if(value=='4'||value==4)
					{
						rowData['Way']=4;
						return '其它';
					}
					}
					},
					{field:'CareDutySysUser',title:'关怀负责人',width:80,align:'center'},
					//{field:'Representative',title:'客户代表',width:80,align:'center'},
					{field:'CareContactor',title:'关怀联系人',width:80,align:'center'},
					{field:'Fee',title:'服务费用',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,align:'center'},
					/* {field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value=='3'||value==3)
						{
							rowData['Status']=3;
							return '<span style="color:red">废弃</span>';
						}
						if(value=='0'||value==0)
						{
							rowData['Status']=0;
							return '未处理';
						}
						if(value=='1'||value==1)
						{
							rowData['Status']=1;
							return '进行中';
						}
						if(value=='2'||value==2)
						{
							rowData['Status']=2;
							return '<span style="color:green">已处理</span>';
						}
					}
					}, */
					
					{field:'CreateSysUser',title:'创建者',width:80,align:'center'},
					{field:'CreateTime',title:'创建时间',width:100,align:'center'},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'导出',
						iconCls:'icon-save',
						handler:function(){
							ShowWaitingDlg("正在导出，请稍后......");
							$('#table1').datagrid('options').queryParams={'CustomerName':encodeURI($('#customerName').combobox('getValue')),/* 'Status':$('#status').combobox('getValue'), */'CustomerLevel':$('#customerLevel').combobox('getValue'),'CareContactor':encodeURI($('#careContactor').val()),'CareDutySysUser':encodeURI($('#careDutySysUser').combobox('getValue')),'StartTime':$('#startTime').datebox('getValue'),'EndTime':$('#endTime').datebox('getValue'),'Way':$('#way').combobox('getValue')};
							
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
			<jsp:param name="TitleName" value="关怀信息" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=13">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="查询关怀信息">
		
		<tr>
			<td width="20%" align="right">联系单位：</td>
			<td align="left" >
			<input name="CustomerName" id="customerName" style="width:200px" class="easyui-combobox" ></input>
			</td>
			
			<td align="right">单位地址：
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
		<!-- <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：
			</td>
			<td><select id="status" Name="Status" class="easyui-combobox" panelHeight="auto">
					<option value='0'>未处理/未开始</option>
					<option value='1'>进行中</option>
					<option value='2'>已处理/已结束</option>
					<option value='3'>/废弃</option>
			</select>
			</td> -->
			 <td align="right">关&nbsp;怀&nbsp;方&nbsp;式:</td>
			<td align="left" >
			<select id="way" name="Way" type="text" class="easyui-combobox" panelHeight="auto">
					<option value="">全部</option>
					<option value='0'>电话</option>
					<option value='1'>短信</option>
					<option value='2'>礼品</option>
					<option value='3'>宴请</option>
					<option value='4'>其它</option>
			</select>
			</td>
			<td align="right">企业分类：</td>
		<td>
		<select class="easyui-combobox" id="customerLevel" name="CustomerLevel">
		<option value="" selected="true">全部</option>
		<option value="1">VIP客户</option>
		<option value="2">重点客户</option>
		<option value="3">重要客户</option>
		<option value="4">一般客户</option>
		<option value="5">特殊客户</option>
		</select>
		
		</td>
		</tr>
				<tr >
		<td align="right">关怀时间：</td>
			<td  align="left">
			<input class="easyui-datebox" name="StartTime" id="startTime" type="text" />
			</td>
			<td align="right">--&nbsp;到&nbsp;--&nbsp;</td>
			<td  align="left">
			<input class="easyui-datebox" name="EndTime" id="endTime" type="text"  />
			</td>
		</tr>
		<tr>
		<td align="right">关怀负责人：
		</td>
		<td>
		<input id="careDutySysUser" name="CareDutySysUser" class="easyui-combobox"/>
		</td>
		<td align="right">关怀联系人：<br />&nbsp;
			</td>
			<td>
			<input id="careContactor" name="CareContactor" class="easyui-validatebox" style="width:145px;"/>
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
