<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�ػ���Ϣͳ��</title>
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
				title:'�ػ�ͳ����Ϣ',
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
					//{field:'Id',title:'���',width:50,align:'center'},
					
					{field:'CustomerName',title:'�ػ���λ',width:150,align:'center'},
					/* {field:'Priority',title:'���ȼ�',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value=='0'||value==0)
						{
							rowData['Priority']=0;
							return '��';
						}
						if(value=='1'||value==1)
						{
							rowData['Priority']=1;
							return '��';
						}
						if(value=='2'||value==2)
						{
							rowData['Priority']=2;
							return '��';
						}
					}
					}, */
					
					{field:'Time',title:'�ػ�����',width:80,align:'center'},
					{field:'Way',title:'�ػ���ʽ',width:80,align:'center',
					formatter:function(value,rowData,index)
					{
					if(value=='0'||value==0)
					{
						rowData['Way']=0;
						return '�绰';
					}
					else if(value=='1'||value==1)
					{
						rowData['Way']=1;
						return '����';
					}
					else if(value=='2'||value==2)
					{
						rowData['Way']=2;
						return '��Ʒ����';
					}
					else if(value=='3'||value==2)
					{
						rowData['Way']=3;
						return '����';
					}
					else if(value=='4'||value==4)
					{
						rowData['Way']=4;
						return '����';
					}
					}
					},
					{field:'CareDutySysUser',title:'�ػ�������',width:80,align:'center'},
					//{field:'Representative',title:'�ͻ�����',width:80,align:'center'},
					{field:'CareContactor',title:'�ػ���ϵ��',width:80,align:'center'},
					{field:'Fee',title:'�������',width:80,align:'center'},
					{field:'Remark',title:'��ע',width:80,align:'center'},
					/* {field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value=='3'||value==3)
						{
							rowData['Status']=3;
							return '<span style="color:red">����</span>';
						}
						if(value=='0'||value==0)
						{
							rowData['Status']=0;
							return 'δ����';
						}
						if(value=='1'||value==1)
						{
							rowData['Status']=1;
							return '������';
						}
						if(value=='2'||value==2)
						{
							rowData['Status']=2;
							return '<span style="color:green">�Ѵ���</span>';
						}
					}
					}, */
					
					{field:'CreateSysUser',title:'������',width:80,align:'center'},
					{field:'CreateTime',title:'����ʱ��',width:100,align:'center'},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
							ShowWaitingDlg("���ڵ��������Ժ�......");
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
			<jsp:param name="TitleName" value="�ػ���Ϣ" />
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
	<table style="width:750px;padding:20px;" class="easyui-panel" title="��ѯ�ػ���Ϣ">
		
		<tr>
			<td width="20%" align="right">��ϵ��λ��</td>
			<td align="left" >
			<input name="CustomerName" id="customerName" style="width:200px" class="easyui-combobox" ></input>
			</td>
			
			<td align="right">��λ��ַ��
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
		<!-- <td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��
			</td>
			<td><select id="status" Name="Status" class="easyui-combobox" panelHeight="auto">
					<option value='0'>δ����/δ��ʼ</option>
					<option value='1'>������</option>
					<option value='2'>�Ѵ���/�ѽ���</option>
					<option value='3'>/����</option>
			</select>
			</td> -->
			 <td align="right">��&nbsp;��&nbsp;��&nbsp;ʽ:</td>
			<td align="left" >
			<select id="way" name="Way" type="text" class="easyui-combobox" panelHeight="auto">
					<option value="">ȫ��</option>
					<option value='0'>�绰</option>
					<option value='1'>����</option>
					<option value='2'>��Ʒ</option>
					<option value='3'>����</option>
					<option value='4'>����</option>
			</select>
			</td>
			<td align="right">��ҵ���ࣺ</td>
		<td>
		<select class="easyui-combobox" id="customerLevel" name="CustomerLevel">
		<option value="" selected="true">ȫ��</option>
		<option value="1">VIP�ͻ�</option>
		<option value="2">�ص�ͻ�</option>
		<option value="3">��Ҫ�ͻ�</option>
		<option value="4">һ��ͻ�</option>
		<option value="5">����ͻ�</option>
		</select>
		
		</td>
		</tr>
				<tr >
		<td align="right">�ػ�ʱ�䣺</td>
			<td  align="left">
			<input class="easyui-datebox" name="StartTime" id="startTime" type="text" />
			</td>
			<td align="right">--&nbsp;��&nbsp;--&nbsp;</td>
			<td  align="left">
			<input class="easyui-datebox" name="EndTime" id="endTime" type="text"  />
			</td>
		</tr>
		<tr>
		<td align="right">�ػ������ˣ�
		</td>
		<td>
		<input id="careDutySysUser" name="CareDutySysUser" class="easyui-combobox"/>
		</td>
		<td align="right">�ػ���ϵ�ˣ�<br />&nbsp;
			</td>
			<td>
			<input id="careContactor" name="CareContactor" class="easyui-validatebox" style="width:145px;"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="query()">��ѯ</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cancel()">����</a></td>
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
