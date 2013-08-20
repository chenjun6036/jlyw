<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>������Ϣ��ѯ</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
 	<script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
		    var lastIndex;
			
			$('#Limit').numberbox();
			$('#FuelFee').numberbox();
			
			$('#Driver').combobox({
				//	url:'/jlyw/CustomerServlet.do?method=6',
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
						$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
					}
				});
			
			$('#table2').datagrid({
				title:'������Ϣ��ѯ',
				width:900,
				height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'/jlyw/VehicleServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Licence',title:'���ƺ�',width:80,align:'center'},
					{field:'Limit',title:'��������',width:100,align:'center'},
					{field:'Brand',title:'����Ʒ��',width:100,align:'center'},
					{field:'Model',title:'�����ͺ�',width:80,align:'center'},
					{field:'FuelFee',title:'�ٹ����ͺ�',width:80,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return "ά��";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Status']=2;
								return '<span style="color:red">���ϻ�ע��</span>';
							}
					}},
					{field:'Driver',title:'˾��',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#edit').window('open');
							$('#form1').show();
															
							$('#Licence').val(select.Licence);
							$('#Limit').val(select.Limit);
							$('#Model').val(select.Model);
							$('#Brand').val(select.Brand);
							$('#FuelFee').val(select.FuelFee);
							$('#Status').combobox('setValue',select.Status);
							$('#Driver').combobox('setValue',select.Driver);
							$('#Id').val(select.Id);
							$('#form1').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){
								$.messager.confirm('��ʾ','ȷ�ϱ�����',function(r){
								if(r){
									$.ajax({
									type:'POST',
									url:'/jlyw/VehicleServlet.do?method=4',
									data:"id="+select.Id,
									dataType:"html",
									success:function(data){
										$('#table2').datagrid('reload');
									}
								});
								}
							});	
							}
							else
							{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
								myExport();
						}
				}]
			});
			
		});
		
		function edit(){
			$('#form1').form('submit',{
				url: '/jlyw/VehicleServlet.do?method=3',
				onSubmit:function(){ return $('#form1').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
					$('#table2').datagrid('reload');
		   		 }
			});
		}
		
		function closed(){
			$('#edit').dialog('close');
		}
			
		function query()
		{
			$('#table2').datagrid('options').url='/jlyw/VehicleServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').val())};
			$('#table2').datagrid('reload');
		}	
		
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#table2').datagrid('options').queryParams));
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
			
		</script>

</head>

<body>
<form id="frm_export" method="post" action="/jlyw/VehicleServlet.do?method=5">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="������Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<br />
			<table id="table1" style="width:900px">
				<tr>
					<td align="right">���ƺţ�</td>
				  <td width="168" align="left"><input id="queryname" name="Name" type="text"></input></td>
					<td width="100" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">��ѯ
					</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:900px"></table>
		
		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
						<tr>
							<td align="right">��&nbsp;��&nbsp;�ţ�</td>
							<td align="left"><input id="Licence" name="Licence" class="easyui-validatebox" required="true"/></td>
							<td align="right">����������</td>
							<td align="left"><input id="Limit" name="Limit" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">����Ʒ�ƣ�</td>
							<td align="left"><input id="Brand" name="Brand" class="easyui-validatebox" required="true"/></td>
                            <td align="right">�����ͺţ�</td>
							<td align="left"><input id="Model" name="Model" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">�ٹ����ͺģ�</td>
							<td align="left"><input id="FuelFee" name="FuelFee" class="easyui-validatebox" required="true"/></td>
							<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
							<td align="left">
								<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">����</option>
									<option value="1">ά��</option>
									<option value="2">���ϡ�ע��</option>
								</select>
							</td>
                            </tr>
                            <tr>
                            <td align="right">˾&nbsp;&nbsp;&nbsp;����</td>
							<td align="left" colspan="3"><input id="Driver" name="Driver" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
						</tr>
						
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">�޸�</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</DIV>
</DIV>
</body>
</html>
