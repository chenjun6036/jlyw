<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>分配投诉处理任务</title>
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
		function conf(){
			$('#frm_ignore').form('submit',{
				url: '/jlyw/CrmServlet.do?method=42',
				onSubmit:function(){ //return $('#frm_ignore').form('validate');
				},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOk){
		   					$('#table1').datagrid('reload');
		   					cancel3();
		   				}
		   					
		   		 }
			});
		}
		
		function sub(){
			$('#frm_edit').form('submit',{
				url: '/jlyw/CrmServlet.do?method=2',
				onSubmit:function(){ return $('#frm_edit').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOk == true){
		   					$('#table1').datagrid('reload');
		   					$('#frm_edit').hide();
		   					$('#edit').window('close');
		   				}
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
			$('#customerName1').combobox({
				valueField:'name',
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
								var newValue = $('#customerName1').combobox('getText');
								$('#customerName1').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 500);
				}
			});});
		function cancel(){
			$('#query1').form('clear');
		}
		
		function search2()
			{
			$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=1';
			$('#table1').datagrid('options').queryParams={'Status':encodeURI('1'),'CustomerName':encodeURI($('#customerName1').combobox('getValue')),'StartDate':encodeURI($('#startDate1').datebox('getValue')),'EndDate':encodeURI($('#endDate1').datebox('getValue'))};
			$('#table1').datagrid('reload');
			}
		$(function(){
		$('#table1').datagrid({
				title:'待分配投诉信息',
				width:700,
				height:500,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=1',
				queryParams:{'Status':encodeURI('1')},
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'编号',width:50,align:'center'},
					
					{field:'CustomerName',title:'投诉单位',width:278,align:'center'},/* 
					{field:'ComplainAbout',title:'投诉对象',width:100,align:'center',
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
					{field:'CreateTime',title:'投诉时间',width:180,align:'center'},
					{field:'CustomerRequiredTime',title:'要求完成时间',width:100,align:'center'},
					/* ,
					{field:'HandleLevel',title:'处理级别',width:80,align:'center',
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
					/* {field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "未";
							}
							else if(value == 1|| value == '1')
							{
								rowData['Status']=1; return "未开始";
								//return '<span style="color:red">注销</span>';
							}
							else 
							{
								rowData['Status']=2; return "进行中";
							}
							
						}} */
					//{field:'HandleLevel',title:'',width:100,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'分配任务',
					iconCls:'icon-edit',
					handler:function(){
					$('#frm_edit').form('clear');
						var select = $('#table1').datagrid('getSelected');
						if(select)
						{
						$('#edit').window('open');
						$('#frm_edit').show();
						$('#feedbackId').val(select.Id);
						$('#frm_edit').form('load',select);
						//$('#form1').form('validate');
					}
					else
					{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'不予处理',
						iconCls:'icon-remove',
						handler:function(){
 							var selected = $('#table1').datagrid('getSelected');
 							if(selected){
 								$('#logoff').window('open');
								//$('#ff1').show();
 								$('#ignoreId').val(selected.Id);
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
								$('#table1').datagrid('options').queryParams={'Status':encodeURI('1'),'CustomerName':encodeURI($('#customerName1').combobox('getValue')),'StartDate':encodeURI($('#startDate1').datebox('getValue')),'EndDate':encodeURI($('#endDate1').datebox('getValue'))};
								
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
	 function cancel2()
	{
		$('#handleMan').combobox('setValue',"");
		$('#jobNum').val('');
		$('#planStartTime').datebox('clear');
		$('#planEndTime').datebox('clear');
		$('#method').val("");
	} 
	function cancel3(){
		$('#logoff').window('close');
	}		
	
	function reject(){
		$('#logoff').window('open');
		$('#ignoreId').val($('#feedbackId').val());
		$('#edit').window('close');
		$('#table1').datagrid('reload');
	}
			
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="分配投诉处理任务" />
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
	<table style="width:700px;padding:20px;" class="easyui-panel" title="查询投诉单">
		
		<tr>
			<td width="20%" align="right">投&nbsp;诉&nbsp;单&nbsp;位：</td>
			<td align="left" colspan="3">
			<input name="CustomerName1" id="customerName1" style="width:321px" class="easyui-combobox" ></input>
			</td>
			
			<td></td>
		</tr>
		<tr>
			<td align="right" style="width:20%">投诉时间:从</td>
			<td align="left" style="width:30%">
				<input class="easyui-datebox" name="StartDate1" id="startDate1" type="text" />&nbsp;&nbsp;&nbsp;&nbsp;-到-
			</td>
			<td align="left" style="width:20%">
				<input class="easyui-datebox" name="EndDate1" id="endDate1" type="text"  />
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
<div id="edit" class="easyui-window" title="分配任务" style="padding: 10px;width: 730px;height: 480px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
<form id="frm_edit" method="post">
	<table  id="table2" style="width:700px; height:180px;  padding-left:20px;" class="easyui-panel" title=" ">
		<tr >
			<td align="right">投诉人所属单位：</td>
			<td align="left" colspan="3">
			<input id="customerName" name="CustomerName" type="text" style="width:465px" readonly="readonly" class="easyui-validatebox"/></td>
		</tr>
		<tr >
			<!-- <td align="right" style="width：20%">投&nbsp;&nbsp;诉&nbsp;&nbsp;对&nbsp;&nbsp;象：</td>
			<td align="left"  style="width：30%">
			<select id="complainAbout" name="ComplainAbout" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>部门</option>
					<option value='1'>检验员</option>
					<option value='2'>证书</option>
					<option value='3'>收费</option>
					<option value='4'>其它</option>
				</select>
				</td> -->
            <td align="right">投&nbsp;&nbsp;&nbsp;诉&nbsp;&nbsp;&nbsp;人：</td>
			<td align="left" ><input id=customerContactorName name="CustomerContactorName" type="text" readonly="readonly" class="easyui-validatebox" /></td>
			
		
		</tr>
		
		<!-- <tr >
			<td align="right">处&nbsp;&nbsp;理&nbsp;&nbsp;级&nbsp;&nbsp;别：</td>
			<td align="left" >
				<select id="handleLevel" name="HandleLevel" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>高</option>
					<option value='1'>中</option>
					<option value='2'>低</option>
				</select>
			</td>
			<td align="right" style="width：20%">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
			<td align="left"  style="width：30%">
			<select id="status" name="Status" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>未开始</option>
					<option value='1'>进行中</option>
					<option value='2'>已结束</option>
				</select>
			</td>
		</tr> -->
		<!-- <tr >
			<td align="right" style="width：20%">计&nbsp;划&nbsp;开&nbsp;始&nbsp;时&nbsp;间：</td>
			<td align="left" style="width：30%"><input class="easyui-datebox" name="StartDate" id="startDate" type="text" /></td>
			<td align="right" style="width：20%">计划结束时间：</td>
			<td align="left" style="width：30%"><input class="easyui-datebox" name="EndDate" id="endDate" type="text" /></td>
		</tr> -->
		<tr >
		<td align="right">客户要求结完成时间：</td>
			<td width="168"  align="left"><input name="CustomerRequiredTime" id="customerRequiredTime" type="text" readonly="readonly"/></td>
		</tr>
		
		
		<tr >
			<td align="right">投诉内容：</td>
			<td align="left"  colspan="3">
			<textarea id="feedback" name="Feedback" cols="65" rows="3" readonly="readonly"></textarea> 
			</td>
		</tr>
		</table>
		<table style="width:700px; height:200px;" class="easyui-panel" title="">
		<tr >
			<td align="right" style="width：20%">计&nbsp;划&nbsp;开&nbsp;始&nbsp;时&nbsp;间：</td>
			<td align="left" style="width：30%"><input class="easyui-datebox" name="PlanStartTime" id="planStartTime" type="text" /></td>
			<td align="right" style="width：20%">计划结束时间：</td>
			<td align="left" style="width：30%"><input class="easyui-datebox" name="PlanEndTime" id="planEndTime" type="text" /></td>
		</tr>
		<tr >
            <td align="right">处理负责人：</td>
            
			<td align="left" >
			<input id="handleMan" name="HandleMan" type="text" class="easyui-combobox" required="true"/>
			</td>
			<td align="right">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td><input id="jobNum" name="JobNum" readonly="readonly" style="border:none; width:150px"/>
			<input id="id" name="Id"  type="hidden"/>
			</td>
			
			<!-- <td align="right" style="width：20%">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：</td> -->
			<!-- <td align="left"  style="width：30%">
			<select id="status2" name="Status2" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>未开始</option>
					<option value='1'>进行中</option>
					<option value='2'>已结束</option>
				</select>
			</td> -->
		</tr>
		<!-- <tr>
		<td align="right">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
		<td><input id="jobNum" name="JobNum" style="border:none; width:150px"/>
		<input id="id" name="Id"  type="hidden"/>
		</td>
		</tr> -->
		<tr >
			<td align="right">处理办法：</td>
			<td align="left"  colspan="3">
			<textarea id="method" name="Method" cols="65" rows="3"></textarea> 
			<input id="feedbackId" name="FeedbackId" class="easyui-validatebox" type="hidden"/>
			</td>
		</tr>
		
		<tr >	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="sub()">提交</a></td>
			<td><a class="easyui-linkbutton" icon="icon-remove" name="Reject" href="javascript:void(0)" onclick="reject()">不予处理</a></td>
			<td><a class="easyui-linkbutton" style="position: relative;left:40px;" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel2()">重置</a></td>
		</tr>
		</table>
	</form>
	</div>
	<div id="logoff" class="easyui-window" title="确认" style="padding: 10px;width: 250px;height:135px;"
		iconCls="icon-confirm" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
		<form id="frm_ignore" method="post">
		<input id="ignoreId" name="IgnoreId" type="hidden"/></td>
		<table>
		<tr>
		<td colspan="3">确定不予处理此投诉？确定后此投诉状态将直接变为“已结束”！</td>
		</tr>
		<tr>
		<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td><a class="easyui-linkbutton" icon="icon-ok" name="" href="javascript:void(0)" onclick="conf()">确认</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-cancel" name="" href="javascript:void(0)" onclick="cancel3()">取消</a></td>
		</tr>
		</table>
		</form>
		</div>
	<br/>
</DIV>
</DIV>
</body>
</html>
