<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>������Ϣ����</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/letter.js"></script>
	<script>
$(function(){
	$("#customerName1").combobox({
		valueField:'name',
		textField:'name',
		//required:true,
		onSelect:function(record){
				$("#address1").val(record.address);
				$('#customerId1').val(record.id);
				/////////////////////////
				$('#insideContactor1').combobox(
				{
				valueField:'ContactorName',
				textField:'ContactorName',
				url:'/jlyw/CrmServlet.do?method=39&CustomerId='+record.id,
				
				onSelect:function(r)
				{
					$('#insideContactorId1').val(r.ContactorId);
					//$('#jobNum').val(r.JobNum);
				},
				onLoadSuccess:function()
				{
				
					var data=$('#insideContactor1').combobox('getData');
					$('#insideContactor1').combobox('setValue',data[0].name);
				}
				
				});///////////////////
				
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
	$(function(){
	$("#customerName").combobox({
		valueField:'name',
		textField:'name',
		//required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				$('#customerId').val(record.id);
				$('#insideContactor').combobox('setValue',"");
				////////////////////////////////////////
				$('#insideContactor').combobox(
				{
				valueField:'ContactorName',
				textField:'ContactorName',
				url:'/jlyw/CrmServlet.do?method=39&CustomerId='+record.id,
				
				onSelect:function(r)
				{
					$('#insideContactorId').val(r.ContactorId);
					$('#jobNum').val(r.JobNum);
				},
				onLoadSuccess:function()
				{
				
					var data=$('#insideContactor').combobox('getData');
					$('#insideContactor').combobox('setValue',data[0].name);
				}
				
				}
);
	/////////////////////////////////////////////////////////////////////
				
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
	
	
	
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=21';
		$('#table1').datagrid('options').queryParams={'CustomerId':/* encodeURI */($('#customerId1').val()),'InsideContactorId':$('#insideContactorId1').val(),'Year':$('#year1').val()};
		$('#table1').datagrid('reload');
	}
		$(function(){
		$('#table1').datagrid({
				title:'�ڲ���ϵ���б�',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				//url:'/jlyw/CrmServlet.do?method=18',
				sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'���',width:50,align:'center'},
					
					{field:'CustomerName',title:'ί�е�λ',width:150,align:'center'},
					{field:'InsideContactor',title:'�ڲ���ϵ��',width:100,align:'center'},
					{field:'Year',title:'�������',width:100,align:'center'},
					{field:'Fee',title:'���ö��',width:80,align:'center'},
					{field:'LastEditor',title:'���༭��',width:80,align:'center'},
					{field:'LastEditTime',title:'���༭ʱ��',width:80,align:'center'},
					{field:'Remark',title:'��ע',width:80,align:'center'},
					{field:'CustomerAddress',title:'ID',width:80,align:'center',hidden:true},
					{field:'JobNum',title:'ID',width:80,align:'center',hidden:true},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'����',
						iconCls:'icon-add',
						handler:function(){
								$('#addIterm').window('open');
								$('#form2').show();
								//$('#del_id').val(selected.Id);
					}
				}
				,'-',{
						text:'�޸�',
						iconCls:'icon-remove',
						handler:function(){
						var select = $('#table1').datagrid('getSelected');
						if(select){
						$('#changeIterm').window('open');
						$('#form3').show();
						$('#id').val(select.Id);
						$('#form3').form('load',select);
						$('#form3').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ɾ��',
						iconCls:'icon-add',
						handler:function(){
						//jConfirm('���������޸ĳɹ�,�Ƿ�����޸�?','��ʾ��Ϣ',function(r){if(r){}else{history.back();}});
						
						var select = $('#table1').datagrid('getSelected');
						if(select){
								$('#delId').val(select.Id);
								var re=confirm('ȷ��ɾ����');
								if(re==true)
								{
								del();
								}
								}
					else
						$.messager.alert('��ʾ','��ѡ��һ������','warning');	
					}
				}],
				onClickRow:function(rowIndex, rowData){
			
				}
			});
			});
			
		function Add(){
		//closed();
			$('#form2').form('submit',{
				url: '/jlyw/CrmServlet.do?method=20',
				onSubmit:function(){return $('#form2').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#table1').datagrid('reload');		
		   		}
			});
		}
		function Add1(){
		//closed();
			$('#form3').form('submit',{
				url: '/jlyw/CrmServlet.do?method=22',
				onSubmit:function(){return $('#form3').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#table1').datagrid('reload');		
		   		}
			});
		}
		function closed()
		{
			$('#addIterm').dialog('close');
			//$('#del').dialog('close');
			//$('#table1').datagrid('reload');
		}
		function del(){
		//closed();
			$('#delForm').form('submit',{
				url:'/jlyw/CrmServlet.do?method=23',
				//onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
			   		$.messager.alert('��ʾ',result.msg,'info');
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
			<jsp:param name="TitleName" value="�ڲ���ϵ�˷��÷���" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="���ݿͻ���ѯ�ڲ���ϵ��">
		
		<tr>
			<td width="20%" align="right">ί�е�λ��</td>
			<td align="left" >
			<input name="CustomerName1" id="customerName1" style="width:200px" class="easyui-combobox" ></input>
			<input id="customerId1" name="CustomerId1" type="hidden"/>
			</td>
			
			<td align="right">��λ��ַ��
			</td>
			<td><input id="address1" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
		<td align="right">�ڲ���ϵ�ˣ�
			</td>
			<td><input style="width:150px" id="insideContactor1" Name="InsideContactor1" class="easyui-combobox" panelHeight="auto">
			
			</input>
			<input type="hidden" id="insideContactorId1" name="InsideContactorId1"/>
			</td>
			<td align="right">������ݣ�</td>
			<td><input id="year1" name="Year1"/></td>
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
	 <div id="addIterm" class="easyui-window" title="�޸�" style="padding: 10px;width: 500px;height: 350px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
                       <tr>
                       <td>��ϵ��λ��
                       </td>
                       <td><input id="customerName" name="CustomerName" class="easyui-combobox" required="true"/>
                       <input type="hidden" id="customerId" name="CustomerId" />
                       </td>
                       <td>��λ��ַ��</td>
                       <td><input id="address" Name="Address"/></td>
                       </tr>
                        <tr>
							<td align="right">��ϵ�ˣ�</td>
							<td align="left"><input style="width:150px" id="insideContactor" name="InsideContactor" class="easyui-combobox" required="true" panelHeight="auto"/>
							<input  type="hidden" id="insideContactorId" name="InsideContactorId"/>
							</td>
							<td align="right">��ϵ�˹��ţ�</td>
							<td align="left"><input id="jobNum" name="JobNum" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td>�����ȣ�</td>
						<td><input id="fee" name="Fee" required="true" class="easyui-validatebox"/></td>
						<td>������ݣ�</td>
						<td><input id="year" name="Year" required="true" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td>
						��ע��
						</td>
						<td><textarea id="remark" name="Remark" ></textarea></td>
						</tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="Add()">�ύ</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	<br />
	<div id="changeIterm" class="easyui-window" title="�޸�" style="padding: 10px;width: 500px;height: 350px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form3" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
                       <tr>
                       <td>��ϵ��λ��
                       </td>
                       <td><input id="customerName" name="CustomerName" class="easyui-combobox" required="true"/>
                       <input type="hidden" id="customerId" name="CustomerId" />
                       </td>
                       <td>��λ��ַ��</td>
                       <td>
                       <input id="customerAddress" Name="CustomerAddress"/>
                       <input type="hidden" id="id" name="Id"/>
                       </td>
                       </tr>
                        <tr>
							<td align="right">��ϵ�ˣ�</td>
							<td align="left"><input style="width:150px" id="insideContactor" name="InsideContactor" class="easyui-combobox" required="true" panelHeight="auto"/>
							<input  type="hidden" id="insideContactorId" name="InsideContactorId"/>
							</td>
							<td align="right">��ϵ�˹��ţ�</td>
							<td align="left"><input id="jobNum" name="JobNum" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td>�����ȣ�</td>
						<td><input id="fee" name="Fee" required="true" class="easyui-validatebox"/></td>
						<td>������ݣ�</td>
						<td><input id="year" name="Year" required="true" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td>
						��ע��
						</td>
						<td><textarea id="remark" name="Remark" ></textarea></td>
						</tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="Add1()">�ύ</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		<form  method="post" id="delForm"><input type="hidden" id="delId" name="DelId" /></form>
</DIV>
</DIV>
</body>
</html>
