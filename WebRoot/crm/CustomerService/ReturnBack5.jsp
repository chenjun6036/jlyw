<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�ط���Ϣ����</title>
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
			$('#frm_edit').form('submit',{
				url: '/jlyw/CrmServlet.do?method=44',
				onSubmit:function(){ return $('#frm_edit').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				if(result.IsOk)
		   				{
		   					$('#edit').window('close');
		   					$('#table1').datagrid('reload');
		   					//$('#table1').datagrid('reload');
		   					}
		   		 }
			});
		}
			
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
		function cance(){
			$('#query1').form('clear');
		}
		
		function search1()
			{
			$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=1';
			$('#table1').datagrid('options').queryParams={'Status':encodeURI('4'),'CustomerName':encodeURI($('#customerName1').combobox('getValue')),'StartDate':encodeURI($('#startDate').datebox('getValue')),'EndDate':encodeURI($('#endDate').datebox('getValue'))};
			$('#table1').datagrid('reload');
			}
		$(function(){
		$('#table1').datagrid({
				title:'�ѽ���Ͷ����Ϣ',
				width:700,
				height:500,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=1',
				queryParams:{'Status':encodeURI('5'),'flag':1},
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CustomerName',title:'Ͷ�ߵ�λ',width:150,align:'center'},
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
					{field:'CreateTime',title:'Ͷ��ʱ��',width:100,align:'center'},
					{field:'CustomerRequiredTime',title:'�ͻ�Ҫ�����ʱ��',width:104,align:'center'},
					{field:'HandleMan',title:'������',width:60,align:'center'},
					{field:'ActulEndTime',title:'ʵ�����ʱ��',width:80,align:'center'},
					{field:'Mark',title:'�ͻ�����',width:56,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'�༭',
					iconCls:'icon-edit',
					handler:function(){
					//$('#frm_edit').form('clear');
						var select = $('#table1').datagrid('getSelected');
						//if(select)
						{
						$('#edit').window('open');
						$('#frm_show').show();
						$('#frm_show').form('load',select);
						$('#frm_edit').show();
						$('#feedbackId').val(select.Id);
						$('#frm_edit').form('load',select);
						//$('#form1').form('validate');
					}
					//else
					{
					//	$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
								ShowWaitingDlg("���ڵ��������Ժ�......");
							$('#par').val(JSON.stringify($('#table1').datagrid('options').queryParams));
							$('#table1').datagrid('options').queryParams={'Status':encodeURI('4'),'CustomerName':encodeURI($('#customerName1').combobox('getValue')),'StartDate':encodeURI($('#startDate').datebox('getValue')),'EndDate':encodeURI($('#endDate').datebox('getValue'))};
							
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
			
		function cancel()
		{
		$('#frm_edit').form('clear');
		}
			
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ط���Ϣ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
			<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=4">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
	<div>
	<form method="post" id="query1">
	<table style="width:700px;padding:20px;" class="easyui-panel" title="��ѯͶ�ߵ�">
		
		<tr>
			<td width="20%" align="right">Ͷ&nbsp;��&nbsp;��&nbsp;λ:</td>
			<td align="left" colspan="3">
			<input name="CustomerName1" id="customerName1" style="width:321px" class="easyui-combobox" ></input>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right" style="width:20%">Ͷ&nbsp;��&nbsp;ʱ&nbsp;��:</td>
			<td align="left" style="width:30%">
				<input class="easyui-datebox" name="StartDate" id="startDate" type="text" />&nbsp;&nbsp;&nbsp;&nbsp;-��-
			</td>
			<td align="left" style="width:20%">
				<input class="easyui-datebox" name="EndDate" id="endDate" type="text"  />
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="search1()">��ѯ</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cance()">����</a></td>
			<td ></td>
		</tr>
	</table>
	</form>
	</div>
<br/>
<table id="table1"></table>
<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 755px;height: 600px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
<form id="frm_show" method="post">
	<table  id="table2" style="width:700px; height:200px; padding-top:10px; padding-left:20px" class="easyui-panel" title=" ">
		<tr >
			<td align="center">Ͷ����������λ��</td>
			<td align="left" >
			<input id="customerName" name="CustomerName" type="text" style="width:220px" readonly="readonly"/>
			</td>
			<td align="right">Ͷ&nbsp;&nbsp;��&nbsp;&nbsp;�ˣ�</td>
			<td align="left" ><input id=customerContactorName name="CustomerContactorName" type="text" readonly="readonly" /></td>
		</tr>
		<!-- <tr>
			<td align="right" style="width��20%">Ͷ&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;��</td>
			<td align="left"  style="width��30%">
			<select id="complainAbout" name="ComplainAbout" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>����</option>
					<option value='1'>����Ա</option>
					<option value='2'>֤��</option>
					<option value='3'>�շ�</option>
					<option value='4'>����</option>
				</select>
            <td align="right">��&nbsp;��&nbsp;��&nbsp;��</td>
			<td align="left" >
				<select id="handleLevel" name="HandleLevel" class="easyui-combobox" style="width:152px"  panelHeight="auto">
					<option value='0'>��</option>
					<option value='1'>��</option>
					<option value='2'>��</option>
				</select>
			</td>
		</tr> -->
		<tr>
			<td align="right">�ͻ�Ҫ�����ʱ�䣺</td>
			<td width="168"  align="left">
			<input readonly="readonly" name="CustomerRequiredTime" id="customerRequiredTime" type="text"  />
			</td>
			<td align="right" style="width��20%">�ƻ���ʼʱ�䣺</td>
			<td align="left" style="width��30%"><input readonly="readonly" name="PlanStartTime" id="PlanStartTime" type="text" /></td>
		</tr>
		<tr >
			<td align="right">ʵ�����ʱ�䣺</td>
			<td width="168"  align="left">
			<input readonly="readonly" name="ActulEndTime" id="actulEndTime" type="text"  />
			</td>
			<td align="left" style="width��20%">�ƻ�����ʱ�䣺</td>
			<td align="left" style="width��30%"><input readonly="readonly" name="PlanEndTime" id="PlanEndTime" type="text" /></td>
		</tr>
		<tr >
			<td align="right">Ͷ�����ݣ�</td>
			<td align="left" >
			<textarea id="feedback" readonly="readonly" name="Feedback" cols="25" rows="3"></textarea> 
			<td align="right">����취��</td>
			<td align="left" >
			<textarea id="method" readonly="readonly" name="Method" cols="25" rows="3"></textarea> 
			</td>
			</td>
		</tr>
		<tr>
            <td align="right">��&nbsp;��&nbsp;��&nbsp;��&nbsp;�ˣ�</td>
			<td align="left" >
			<input id="handleMan"  name="HandleMan" type="text" readonly="readonly" />
			</td>
			<td align="right">���ţ�</td>
		<td align="left"><input id="jobNum" readonly="readonly" name="JobNum" style=" width:150px"/>
		</td>
		</tr>
		<!-- <tr>
		<td align="right">ʵ�����ʱ�䣺</td>
			<td width="168"  align="left">
			<input class="easyui-datebox" name="ActulEndTime" id="actulEndTime" type="text"  />
			</td>
		</tr> -->
		<tr >
			<td align="right">Ͷ�߽����</td>
			<td align="left"  colspan="3"><textarea id="remark" name="Remark" cols="55" rows="3" readonly="readonly"></textarea>
			</td>
			
		</tr>
	<tr>
		<td align="right">�ͻ����֣�</td>
		<td><input id="mark" name="Mark"  readonly="readonly"/></td>
	</tr>

			<tr >
			<td align="right" >�Ľ������</td>
			<td align="left"  colspan="3"><textarea id="analysis" name="Analysis" cols="55" rows="3" readonly="readonly"></textarea> 
			</td>
			
		</tr>
	</table>
	</form>
	
	
	<form id="frm_edit" method="post">
	<table style="width:700px; height:100px; padding-top:10px; padding-left:20px" class="easyui-panel" title="">
		<!-- <tr>
			<td align="right" style="width��20%">�ط���Ϣ���ͣ�</td>
			<td align="left"  style="width��30%">
			<select id="ReturnVisitType" name="ReturnVisitType" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>����</option>
					<option value='1'>����</option>
					<option value='2'>����</option>
					<option value='3'>��Ͷ��</option>
					<option value='4'>��������</option>
					<option value='5'>����</option>
				</select>
			</td>
		</tr> -->

		<tr >
			<td align="right" colspan="2">�ط���Ϣ��</td>
			<td align="left"  colspan="2"><textarea id="returnVisitInfo" name="ReturnVisitInfo" class="easyui-validatebox" cols="55" rows="3" required="true"></textarea> 
			
			<input id="feedbackId" type="hidden" name="FeedbackId"/>
			</td>
			
		</tr>

		<tr height="50px">	
			<td></td>
			
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="sub()">�ύ</a></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
		</tr>
		
	</table>
	</form>
	</div>
	<br/>
</DIV>
</DIV>
</body>
</html>
