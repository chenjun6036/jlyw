<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�鿴������Ϣ</title>
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
		   				$.messager.alert('��ʾ',result.msg,'info');
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
				title:'Ͷ����Ϣ',
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
					//{field:'Id',title:'���',width:50,align:'center'},
					{field:'CreateTime',title:'Ͷ��ʱ��',width:180,align:'center'},
					{field:'CustomerName',title:'Ͷ�ߵ�λ',width:100,align:'center'},
					/* {field:'ComplainAbout',title:'Ͷ�߶���',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['ComplainAbout']=0;
							    return "����";
							}
							else if(value == 1 || value == '1')
							{
								rowData['ComplainAbout']=1;
								return "����Ա";
							}
							else if(value == 2 || value == '2')
							{
								rowData['ComplainAbout']=2;
								return "֤��";
							}else if(value == 3 || value == '3')
							{
								rowData['ComplainAbout']=3;
								return "�շ�";
							}else if(value == 4 || value == '4')
							{
								rowData['ComplainAbout']=4;
								return "����";
							}
						}}, */
					{field:'CustomerContactorName',title:'Ͷ����',width:80,align:'center'},
					/* {field:'HandleLevel',title:'������',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['CustomerType']=0;
							    return "��";
							}
							else if(value == 1 || value == '1')
							{
								rowData['CustomerType']=1;
								return "��";
							}
							else if(value == 2 || value == '2')
							{
								rowData['CustomerType']=2;
								return "��";
							}
						}}, */
						{field:'ActulStartTime',title:'ʵ�ʿ�ʼʱ��',width:80,align:'center'},
						{field:'ActulEndTime',title:'ʵ�ʽ���ʱ��',width:80,align:'center'},
						{field:'Feedback',title:'��������',width:80,align:'center'},
						{field:'Method',title:'������',width:80,align:'center'},
						{field:'PlanStartTime',title:'�ƻ���ʼʱ��',width:80,align:'center'},
						{field:'PlanEndTime',title:'�ƻ�����ʱ��',width:80,align:'center'},
						{field:'HandleMan',title:'������',width:80,align:'center'},
						{field:'CreateSysUserName',title:'������',width:80,align:'center'},
						{field:'ReturnVisitType',title:'�ط���Ϣ����',width:80,align:'center'},
						{field:'ReturnVisitInfo',title:'�ط���Ϣ',width:80,align:'center'},
						{field:'Mark',title:'�ͻ�����',width:80,align:'center'},
						{field:'CustomerRequiredTime',title:'�ͻ�Ҫ�����ʱ��',width:80,align:'center'},
						{field:'Analysis',title:'�������',width:80,align:'center'},
						//{field:'Method',title:'Ͷ����',width:80,align:'center'},
						{field:'Remark',title:'��ע',width:80,align:'center'},
						
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 1)
							{
								rowData['Status']=1;
								return "δ��ʼ";
							}
							else if(value == 2)
								return "������";
							else if(value == 3)
								return "�Ѵ���";
							else if(value == 4)
								return "�ѽ���";
							else if(value == 5)
								return "���ط�";							
						}}
					//{field:'HandleLevel',title:'',width:100,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:['-',{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
							ShowWaitingDlg("���ڵ��������Ժ�......");
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
										$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
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
			<jsp:param name="TitleName" value=" ������Ϣͳ��" />
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
	<table style="width:850px;padding:20px;" class="easyui-panel" title="��ѯ������Ϣ">
		
		<tr>
			<td width="20%" align="right">Ͷ&nbsp;��&nbsp;��&nbsp;λ��</td>
			<td align="left" colspan="3">
			<input name="CustomerName" id="customerName" style="width:321px" class="easyui-combobox" ></input>
			</td>
			
			<td></td>
		</tr>
		<!-- <tr>
			<td align="right">Ͷ�߶���</td>
			<td><select id="complainAbout" name="ComplainAbout" class="easyui-combobox">
			<option value="0">����</option>
			<option value="1">����Ա</option>
			<option value="2">֤��</option>
			<option value="3">�շ�</option>
			<option value="4">����</option>
			</select></td>
			<td></td>
		</tr> -->
		<tr>
			<td align="right" style="width:20%">Ͷ��ʱ��:��</td>
			<td align="left" style="width:30%">
				<input class="easyui-datebox" name="StartDate" id="startDate" type="text" />&nbsp;&nbsp;&nbsp;&nbsp;-��-
			</td>
			<td align="left" style="width:20%">
				<input class="easyui-datebox" name="EndDate" id="endDate" type="text"  />
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="search2()">��ѯ</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cancel()">����</a></td>
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
