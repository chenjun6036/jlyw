<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>����Ͷ�ߴ�������</title>
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
		   				$.messager.alert('��ʾ',result.msg,'info');
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
		   				$.messager.alert('��ʾ',result.msg,'info');
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
				title:'������Ͷ����Ϣ',
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
					//{field:'Id',title:'���',width:50,align:'center'},
					
					{field:'CustomerName',title:'Ͷ�ߵ�λ',width:278,align:'center'},/* 
					{field:'ComplainAbout',title:'Ͷ�߶���',width:100,align:'center',
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
					{field:'CreateTime',title:'Ͷ��ʱ��',width:180,align:'center'},
					{field:'CustomerRequiredTime',title:'Ҫ�����ʱ��',width:100,align:'center'},
					/* ,
					{field:'HandleLevel',title:'������',width:80,align:'center',
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
					/* {field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "δ";
							}
							else if(value == 1|| value == '1')
							{
								rowData['Status']=1; return "δ��ʼ";
								//return '<span style="color:red">ע��</span>';
							}
							else 
							{
								rowData['Status']=2; return "������";
							}
							
						}} */
					//{field:'HandleLevel',title:'',width:100,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'��������',
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
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'���账��',
						iconCls:'icon-remove',
						handler:function(){
 							var selected = $('#table1').datagrid('getSelected');
 							if(selected){
 								$('#logoff').window('open');
								//$('#ff1').show();
 								$('#ignoreId').val(selected.Id);
 						}
 						else{
 							$.messager.alert('��ʾ','��ѡ��һ������','warning');
 						}
					}
				},'-',{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
								ShowWaitingDlg("���ڵ��������Ժ�......");
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
										$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
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
			<jsp:param name="TitleName" value="����Ͷ�ߴ�������" />
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
	<table style="width:700px;padding:20px;" class="easyui-panel" title="��ѯͶ�ߵ�">
		
		<tr>
			<td width="20%" align="right">Ͷ&nbsp;��&nbsp;��&nbsp;λ��</td>
			<td align="left" colspan="3">
			<input name="CustomerName1" id="customerName1" style="width:321px" class="easyui-combobox" ></input>
			</td>
			
			<td></td>
		</tr>
		<tr>
			<td align="right" style="width:20%">Ͷ��ʱ��:��</td>
			<td align="left" style="width:30%">
				<input class="easyui-datebox" name="StartDate1" id="startDate1" type="text" />&nbsp;&nbsp;&nbsp;&nbsp;-��-
			</td>
			<td align="left" style="width:20%">
				<input class="easyui-datebox" name="EndDate1" id="endDate1" type="text"  />
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
<div id="edit" class="easyui-window" title="��������" style="padding: 10px;width: 730px;height: 480px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
<form id="frm_edit" method="post">
	<table  id="table2" style="width:700px; height:180px;  padding-left:20px;" class="easyui-panel" title=" ">
		<tr >
			<td align="right">Ͷ����������λ��</td>
			<td align="left" colspan="3">
			<input id="customerName" name="CustomerName" type="text" style="width:465px" readonly="readonly" class="easyui-validatebox"/></td>
		</tr>
		<tr >
			<!-- <td align="right" style="width��20%">Ͷ&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;��</td>
			<td align="left"  style="width��30%">
			<select id="complainAbout" name="ComplainAbout" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>����</option>
					<option value='1'>����Ա</option>
					<option value='2'>֤��</option>
					<option value='3'>�շ�</option>
					<option value='4'>����</option>
				</select>
				</td> -->
            <td align="right">Ͷ&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;�ˣ�</td>
			<td align="left" ><input id=customerContactorName name="CustomerContactorName" type="text" readonly="readonly" class="easyui-validatebox" /></td>
			
		
		</tr>
		
		<!-- <tr >
			<td align="right">��&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;��</td>
			<td align="left" >
				<select id="handleLevel" name="HandleLevel" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>��</option>
					<option value='1'>��</option>
					<option value='2'>��</option>
				</select>
			</td>
			<td align="right" style="width��20%">״&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
			<td align="left"  style="width��30%">
			<select id="status" name="Status" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>δ��ʼ</option>
					<option value='1'>������</option>
					<option value='2'>�ѽ���</option>
				</select>
			</td>
		</tr> -->
		<!-- <tr >
			<td align="right" style="width��20%">��&nbsp;��&nbsp;��&nbsp;ʼ&nbsp;ʱ&nbsp;�䣺</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="StartDate" id="startDate" type="text" /></td>
			<td align="right" style="width��20%">�ƻ�����ʱ�䣺</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="EndDate" id="endDate" type="text" /></td>
		</tr> -->
		<tr >
		<td align="right">�ͻ�Ҫ������ʱ�䣺</td>
			<td width="168"  align="left"><input name="CustomerRequiredTime" id="customerRequiredTime" type="text" readonly="readonly"/></td>
		</tr>
		
		
		<tr >
			<td align="right">Ͷ�����ݣ�</td>
			<td align="left"  colspan="3">
			<textarea id="feedback" name="Feedback" cols="65" rows="3" readonly="readonly"></textarea> 
			</td>
		</tr>
		</table>
		<table style="width:700px; height:200px;" class="easyui-panel" title="">
		<tr >
			<td align="right" style="width��20%">��&nbsp;��&nbsp;��&nbsp;ʼ&nbsp;ʱ&nbsp;�䣺</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="PlanStartTime" id="planStartTime" type="text" /></td>
			<td align="right" style="width��20%">�ƻ�����ʱ�䣺</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="PlanEndTime" id="planEndTime" type="text" /></td>
		</tr>
		<tr >
            <td align="right">�������ˣ�</td>
            
			<td align="left" >
			<input id="handleMan" name="HandleMan" type="text" class="easyui-combobox" required="true"/>
			</td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
			<td><input id="jobNum" name="JobNum" readonly="readonly" style="border:none; width:150px"/>
			<input id="id" name="Id"  type="hidden"/>
			</td>
			
			<!-- <td align="right" style="width��20%">״&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;̬��</td> -->
			<!-- <td align="left"  style="width��30%">
			<select id="status2" name="Status2" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>δ��ʼ</option>
					<option value='1'>������</option>
					<option value='2'>�ѽ���</option>
				</select>
			</td> -->
		</tr>
		<!-- <tr>
		<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
		<td><input id="jobNum" name="JobNum" style="border:none; width:150px"/>
		<input id="id" name="Id"  type="hidden"/>
		</td>
		</tr> -->
		<tr >
			<td align="right">����취��</td>
			<td align="left"  colspan="3">
			<textarea id="method" name="Method" cols="65" rows="3"></textarea> 
			<input id="feedbackId" name="FeedbackId" class="easyui-validatebox" type="hidden"/>
			</td>
		</tr>
		
		<tr >	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="sub()">�ύ</a></td>
			<td><a class="easyui-linkbutton" icon="icon-remove" name="Reject" href="javascript:void(0)" onclick="reject()">���账��</a></td>
			<td><a class="easyui-linkbutton" style="position: relative;left:40px;" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel2()">����</a></td>
		</tr>
		</table>
	</form>
	</div>
	<div id="logoff" class="easyui-window" title="ȷ��" style="padding: 10px;width: 250px;height:135px;"
		iconCls="icon-confirm" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
		<form id="frm_ignore" method="post">
		<input id="ignoreId" name="IgnoreId" type="hidden"/></td>
		<table>
		<tr>
		<td colspan="3">ȷ�����账���Ͷ�ߣ�ȷ�����Ͷ��״̬��ֱ�ӱ�Ϊ���ѽ�������</td>
		</tr>
		<tr>
		<td colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td><a class="easyui-linkbutton" icon="icon-ok" name="" href="javascript:void(0)" onclick="conf()">ȷ��</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-cancel" name="" href="javascript:void(0)" onclick="cancel3()">ȡ��</a></td>
		</tr>
		</table>
		</form>
		</div>
	<br/>
</DIV>
</DIV>
</body>
</html>
