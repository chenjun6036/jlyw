<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>查看反馈信息</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
        <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
		<script>
		function sub(){
			$('#change').form('submit',{
				url: '/jlyw/CrmServlet.do?method=2',
				onSubmit:function(){ return $('#change').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
		}
		$(function(){
		$('#handleMan').combobox({
		valueField:'name',
		textField:'name',
				onSelect:function(rec)
				{
					$('#jobNum').val(rec.jobNum);
					$('#id').val(rec.id);
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
					$('#handleMan').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});});
			
		$(function(){
			$('#customerName').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){},
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
								$('#customerName').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 500);
				}
			});});
		function cancel(){
			$('#query1').form('clear');
		}
		
		function search2()
			{
			$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=1';
			$('#table1').datagrid('options').queryParams={'CustomerName':encodeURI($('#customerName').combobox('getValue')),'StartDate':encodeURI($('#startDate').datebox('getValue')),'EndDate':encodeURI($('#endDate').datebox('getValue'))/* ,'ComplainAbout':encodeURI($('#complainAbout').combobox('getValue')) */};
			$('#table1').datagrid('reload');
			}
		$(function(){
		$('#table1').datagrid({
				title:'投诉信息',
				width:850,
				height:500,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=1',
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'编号',width:50,align:'center'},
					{field:'CreateTime',title:'投诉时间',width:180,align:'center'},
					{field:'CustomerName',title:'投诉单位',width:100,align:'center'},
					/* {field:'ComplainAbout',title:'投诉对象',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['ComplainAbout']=0;
							    return "部门";
							}
							else if(value == 1 || value == '1')
							{
								rowData['ComplainAbout']=1;
								return "检验员";
							}
							else if(value == 2 || value == '2')
							{
								rowData['ComplainAbout']=2;
								return "证书";
							}else if(value == 3 || value == '3')
							{
								rowData['ComplainAbout']=3;
								return "收费";
							}else if(value == 4 || value == '4')
							{
								rowData['ComplainAbout']=4;
								return "其它";
							}
						}}, */
					{field:'CustomerContactorName',title:'投诉人',width:80,align:'center'},
					/* {field:'HandleLevel',title:'处理级别',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['CustomerType']=0;
							    return "高";
							}
							else if(value == 1 || value == '1')
							{
								rowData['CustomerType']=1;
								return "中";
							}
							else if(value == 2 || value == '2')
							{
								rowData['CustomerType']=2;
								return "低";
							}
						}}, */
						{field:'ActulStartTime',title:'实际开始时间',width:80,align:'center'},
						{field:'ActulEndTime',title:'实际结束时间',width:80,align:'center'},
						{field:'Feedback',title:'反馈内容',width:80,align:'center'},
						{field:'Method',title:'处理方法',width:80,align:'center'},
						{field:'PlanStartTime',title:'计划开始时间',width:80,align:'center'},
						{field:'PlanEndTime',title:'计划结束时间',width:80,align:'center'},
						{field:'HandleMan',title:'负责人',width:80,align:'center'},
						{field:'CreateSysUserName',title:'创建者',width:80,align:'center'},
						{field:'ReturnVisitType',title:'回访信息类型',width:80,align:'center'},
						{field:'ReturnVisitInfo',title:'回访信息',width:80,align:'center'},
						{field:'Mark',title:'客户评分',width:80,align:'center'},
						{field:'CustomerRequiredTime',title:'客户要求完成时间',width:80,align:'center'},
						{field:'Analysis',title:'问题分析',width:80,align:'center'},
						//{field:'Method',title:'投诉人',width:80,align:'center'},
						{field:'Remark',title:'备注',width:80,align:'center'},
						
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 1)
							{
								rowData['Status']=1;
								return "未开始";
							}
							else if(value == 2)
								return "进行中";
							else if(value == 3)
								return "已处理";
							else if(value == 4)
								return "已结束";
							else if(value == 5)
								return "待回访";							
						}}
					//{field:'HandleLevel',title:'',width:100,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:['-',{
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
				/* $('#customerName2').val(rowData.CustomerName);
				$('#complainAbout2').combobox('setValue',rowData.ComplainAbout);//rowData.ComplainAbout);
				$('#status').combobox('setValue',rowData.Status);
				$('#feedback').val(rowData.Feedback);
				$('#customerContactorName2').val(rowData.CustomerContactorName);
				$('#handleLevel').combobox('setValue',rowData.HandleLevel);
				$('#startDate2').datebox('setValue',rowData.PlanStartTime);
				$('#endDate2').datebox('setValue',rowData.PlanEndTime);
				$('#customerRequiredTime').datebox('setValue',rowData.CustomerRequiredTime);
				$('#feedbackId').val(rowData.Id); */
				}
			});
			});
	 function cancel2()
	{
		$('#handleMan').combobox('setValue',"");
		$('#status2').combobox('setValue',"");
		$('#method').val("");
	} 
			
			
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value=" 反馈信息统计" />
		</jsp:include>
	</DIV>
<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=4">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	<form method="post" id="query1">
	<table style="width:850px;padding:20px;" class="easyui-panel" title="查询反馈信息">
		
		<tr>
			<td width="20%" align="right">投&nbsp;诉&nbsp;单&nbsp;位：</td>
			<td align="left" colspan="3">
			<input name="CustomerName" id="customerName" style="width:321px" class="easyui-combobox" ></input>
			</td>
			
			<td></td>
		</tr>
		<!-- <tr>
			<td align="right">投诉对象：</td>
			<td><select id="complainAbout" name="ComplainAbout" class="easyui-combobox">
			<option value="0">部门</option>
			<option value="1">检验员</option>
			<option value="2">证书</option>
			<option value="3">收费</option>
			<option value="4">其它</option>
			</select></td>
			<td></td>
		</tr> -->
		<tr>
			<td align="right" style="width:20%">投诉时间:从</td>
			<td align="left" style="width:30%">
				<input class="easyui-datebox" name="StartDate" id="startDate" type="text" />&nbsp;&nbsp;&nbsp;&nbsp;-到-
			</td>
			<td align="left" style="width:20%">
				<input class="easyui-datebox" name="EndDate" id="endDate" type="text"  />
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="search2()">查询</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cancel()">重置</a></td>
			<td ></td>
		</tr>
	</table>
	</form>
	</div>
<br/>
<table id="table1"></table>
	<br/>
</DIV>
</DIV>
</body>
</html>
