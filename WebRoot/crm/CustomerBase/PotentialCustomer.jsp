<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>Ǳ�ڿͻ���Ϣ����</title>
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
    <script >
    
    $(function(){
		$('#rid').combobox({
		valueField:'id',
		textField:'name',
		editable:false,
		onSelect:function(rec)
		{
			$('#insideContactorId').combobox('reload','/jlyw/CustomerContactorServlet.do?method=9&RegionId='+rec.id);
		//$('#industryId').val(rec.Id);
			$('#insideContactorId').combobox({
			valueField:'ContactorId',
			textField:'Name',
			editable:false,
			onSelect:function(rec)
			{
			},
			onLoadSuccess: function () 
			{
			$('#insideContactorId').combobox('clear');
				var data=$('#insideContactorId').combobox('getData');
				$('#insideContactorId').combobox('select',data[0].ContactorId);
				
			}
			});
			

		}
		});
	});
    $(function(){
		$('#industry1').combobox({
		url:'/jlyw/CrmServlet.do?method=11',
		valueField:'Id',
		textField:'Name',
		editable:false,
		onSelect:function(rec)
		{
		$('#industryId').val(rec.Id);
		}
		});
	});
	function cancel(){
		$('#frm_add_customer').form('clear');
		}
		
	function savereg(){
		$('#frm_add_customer').form('submit',{
			url: '/jlyw/CrmServlet.do?method=41',
			onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('��ʾ',result.msg,'info');
		   		if(result.IsOK){
		   			$('#table1').datagrid('reload');
		   			cancel();
		   			}
		   		}
		});
	}
		
	function getBrief(){
		$('#brief').val(makePy($('#name').val()));
	}
		
	$(function(){
	$('#cla').combobox({
		multiple:'true'
		});
	$('#frm_add_customer').form('validate');
	});
		
	function check()
	{
	$('#checkCode').val($('#code').val());
	$('#frm_code').form('submit',{
		url: '/jlyw/CrmServlet.do?method=38',
		//onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		 success:function(data){
		 var result = eval("("+data+")");
		 {
		 	if(result.IsOk==false)
		   	{
		   		$.messager.alert('��ʾ',result.msg,'info');
		   		$('#code').val('');
		   	}
		  }
		   				
		 }
});
		
}
    </script>
	<script>
	$(function()
	{
		$('#region').combobox({
		onSelect: function(rec)
		{$('#regionId').val(rec.id);}
		});
	});
	function getBrief1(){$('#brief1').val(makePy($('#customerName').val()));}
	$(function(){
		$('#industry').combobox({
		url:'/jlyw/CrmServlet.do?method=11',
		valueField:'Id',
		textField:'Name',
		editable:false,
		onSelect:function(rec)
		{
		$('#industryId').val(rec.Id);
		}
		});
	});
	
$(function(){
	$("#customerName1").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				$('#customerId1').val(record.id);
				
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
					$('#customerName1').combobox('reload','/jlyw/CrmServlet.do?method=13&CustomerName='+newValue);
			}, 400);
		}
	});
	});

	

	
	///////////////////////////////////////////////////////////////////
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=14';
		$('#table1').datagrid('options').queryParams={'CustomerName':encodeURI($('#customerName1').combobox('getValue')),'CustomerId':/* encodeURI */($('#customerId1').val())};
		$('#table1').datagrid('reload');
	}

		function cancel1(){
			$('#frm_add_conInfo').form('clear');
		}
		
		function Add(){
			$('#frm_add_conInfo').form('submit',{
				url: '/jlyw/CrmServlet.do?method=7',
				onSubmit:function(){ return $('#frm_add_conInfo').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
		}

		$(function(){
		$('#table1').datagrid({
				title:'Ǳ�ڿͻ���Ϣ',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=15',
				//sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CustomerName',title:'��λ����',width:150,align:'center'},
					{field:'Address',title:'��λ��ַ',width:150,align:'center'},
					{field:'Region',title:'����',width:50,align:'center'},
					{field:'NameEn',title:'Ӣ������',width:100,align:'center'},
					{field:'From',title:'��Դ',width:50,align:'center',
					formatter:function(value,rowData,rowIndex)
					{
						if(value=='1'||value==1)
						{
							rowData['From']=1;
							return '�绰';
						}
						else if(value=='2'||value==2)
						{
							rowData['From']=2;
							return '����';
						}
						else if(value=='3'||value==3)
						{
							rowData['From']=3;
							return '����';
						}
						else if(value=='4'||value==4)
						{
							rowData['From']=4;
							return '����';
						}
					}},
					{field:'Intension',title:'��������',width:100,align:'center',
					formatter:function(value,rowData,rowIndex)
					{
						if(value=='1'||value==1)
						{
							rowData['Intension']=1;
							return '��ȷ��������';
						}
						else if(value=='2'||value==2)
						{
							rowData['Intension']=2;
							return '������ϵ��������';
						}
						else if(value=='3'||value==3)
						{
							rowData['Intension']=3;
							return '������������ȷ';
						}
						else if(value=='4'||value==4)
						{
							rowData['Intension']=4;
							return '���Ծܾ�';
						}
					}},
					{field:'Industry',title:'��ҵ',width:80,align:'center'},
					{field:'IndustryId',title:'ID',width:80,align:'center',hidden:true},
					{field:'RegionId',title:'Id',width:80,align:'center',hidden:true},
					{field:'Brief1',title:'',width:80,align:'center',hidden:true},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table1').datagrid('getSelected');
						if(select){
						$('#editPotential').window('open');
						$('#form2').show();
						$('#form2').form('load',select);
						$('#form2').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'����Ϊ��ʽ�ͻ�',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table1').datagrid('getSelected');
							if(selected){
							
									$('#AddPotentialToFormer').window('open');
									$('#frm_add_customer').show();
									$('#frm_add_customer').form('load',selected);
									$('#name').val(selected.CustomerName);
									$('#brief').val(selected.Brief1);
									$('#del_id').val(selected.Id);
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
							$('#par').val(encodeURI($('#customerName1').combobox('getValue')));
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
			
		function Sub(){
		closed();
			$('#form2').form('submit',{
				url: '/jlyw/CrmServlet.do?method=16',
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
		function closed()
		{
			$('#editPotential').dialog('close');
			$('#del').dialog('close');
			//$('#table1').datagrid('reload');
		}
		function del(){
			closed();
			$('#ff1').form('submit',{
				url:'/jlyw/CrmServlet.do?method=17',
				onSubmit:function(){
				//if($('#')){}
				},
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
		
	$(function()
{
	$("#payType").combobox(
	{
	onSelect:function()
	{
	    if($("#payType").combobox('getValue')=='2'||$("#payType").combobox('getValue')==2)
	    {
	    	$("#accountCycle").combobox({
	    	required:true
	    	});
	    	$('#frm_add_customer').form('validate');
	    }
	    else 
	    {
	    	$("#accountCycle").combobox({
	    	required:false
	    	});
	    	$('#frm_add_customer').form('validate');
	    }
	}
	});
}
);
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="Ǳ�ڿͻ���Ϣ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=3">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="��ѯǱ�ڿͻ���Ϣ">
		<tr>
			<td width="20%" align="right">��&nbsp;λ&nbsp;��&nbsp;�ƣ�</td>
			<td align="left" >
			<input name="CustomerName1" id="customerName1" style="width:310px" class="easyui-combobox" ></input>
			<input id="customerId1" name="CustomerId1" type="hidden"/>
			</td>
			<td align="center">��λ��ַ��
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="query()">��ѯ</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cancel1()">����</a></td>
			<td ></td>
		</tr>
	</table>
	</form>
<table id="table1"></table>
	<br />
	 <div id="editPotential" class="easyui-window" title="�޸�" style="padding: 10px;width: 600px;height: 300px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
						<tr>
							<td align="right" style="width:100px;">��λ���ƣ�</td>
							<td align="left" ><input id="customerName" name="CustomerName" class="easyui-validatebox"  required="true" onchange="getBrief1()"/>
							<input id="Id" name="Id" type="hidden"/>
                    		</td>
                    		<td align="right" style="width:100px;">ƴ�����룺</td>
							<td align="left"><input id="brief1" name="Brief1" class="easyui-validatebox"  required="true"/>
							</td>
                    	</tr>
                    	
                        <tr>
                        	<td align="right">��λ����Ӣ�ģ�</td>
							<td align="left"><input id="nameEn" name="NameEn" class="easyui-validatebox" required="true" />
                    		</td>
							<td align="right">������</td>
							<td align="left">
							<select id="region" name="Region" class="easyui-combobox" required="true" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2"  editable="false">
							</select><input id="regionId" name="RegionId" type="hidden"/>
							</td>
						</tr>
		
					<tr>
                        <td align="right">��λ��ַ��</td>
							<td align="left"><input id="address" name="Address" class="easyui-validatebox"  required="true"/>
                   		</td>
					</tr>
						
					<tr>
                        <td align="right">Ǳ�ڿͻ���Դ��</td>
							<td align="left">
							<select id="from" name="From" class="easyui-combobox"  required="true" panelHeight="auto">
							<option value="1">�绰</option>
							<option value="2">����</option>
							<option value="3">����</option>
							<option value="4">����</option>
							</select>
          					</td>
							<td align="right">��������</td>
							<td align="left">
							<select id="intension" name="Intension" class="easyui-combobox" required="true" panelHeight="auto">
							<option value="1">��ȷ��������</option>
							<option value="2">������ϵ�������� </option>
							<option value="3">������������ȷ</option>
							<option value="4">���Ծܾ�</option>
							</select>
							</td>
						</tr>
						<tr>
 
							<td align="right">��ҵ��</td>
							<td align="left">
							<select id="industry" name="Industry" class="easyui-combobox" required="true" panelHeight="auto" mode="remote" editable="false">
							</select>
							<input id="industryId" Name="IndustryId" type="hidden"/>
							</td>
						</tr>
						
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="Sub()">�ύ</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	<br />
	<div id="AddPotentialToFormer" class="easyui-window" title="ȷ��" style="width:700px;height:750px;" iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false">
        
        <form id="frm_add_customer" method="post">
	<table >
		<tr height="30px">
			<td align="right" style="width��20%">��λ���ƣ�<input type="hidden" id="del_id" name="Del_id"/></td>
			<td align="left"  style="width��30%"><input id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            <td align="right" style="width��20%">ƴ�����룺</td>
			<td align="left"  style="width��30%"><input id="brief" name="Brief" type="text" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
		<td align="right">���ʽ��</td>
		<td><select style="width:145px" id="payVia" name="PayVia" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">�ֽ�</option>
		<option value="2">֧Ʊ</option>
		<option value="3">����</option>
		<option value="4">POS��</option>
		<option value="5">����</option>
		</select></td>
		<td align="right">�������ͣ�</td>
		<td><select style="width:145px" id="payType" name="PayType" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">������</option>
		<option value="2">���ڽ���</option>
		<option value="3">Ԥ����</option>
		<option value="4">����</option>
		</select>
		</td>
		</tr>
		<tr><td align="right">�������ڣ�</td>
		<td><select style="width:145px" id="accountCycle" name="AccountCycle" class="easyui-combobox" editable="false" required="false">
		<option value="1">һ����</option>
		<option value="2">������</option>
		<option value="3">������</option>
		<option value="4">�ĸ���</option>
		<option value="5">�����</option>
		<option value="6">������</option>
		<option value="7">�߸���</option>
		<option value="8">�˸���</option>
		<option value="9">�Ÿ���</option>
		<option value="10">ʮ����</option>
		<option value="11">ʮһ����</option>
		<option value="12">ʮ������</option>
		</select></td>
		<td align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
			<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr height="30px">
			<td align="right">��λ���ͣ�</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:145px" required="true" panelHeight="auto" editable="false">
					<option value='0'>������ҵ</option>
					<option value='1'>������ҵ</option>
					<option value='2'>���������ҵ</option>
					<option value='3'>��Ӫ��ҵ</option>
				</select>
			</td>
			<td align="right">��λ��ַ��</td>
			<td align="left"><input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr  height="30px">
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
			<td align="left">
				<select id="rid" name="RegionId" class="easyui-combobox" style="width:145px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
			</td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
			<td align="left" ><input id="zcd" name="ZipCode" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr  height="30px">
			<td align="right">�ڲ���ϵ�ˣ�</td>
			<td align="left">
				<select id="insideContactorId" name="InsideContactorId" panelHeight="auto" class="easyui-combobox" style="width:145px" required="true" editable="false" ></select>
			</td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ɫ��</td>
			<td align="left" ><select style="width:145px" id="role" name="Role" type="text" class="easyui-combobox" required="true" panelHeight="auto">
			<option value="1" selected="selected">A</option>
			<option value="2">B</option>
			</select></td>
		</tr>
		<tr height="3px;"><td align="left" colspan="5" >-------------------------------------------------------------------------------------------------------------</td>
		</tr>
		<tr height="30px">
			
			<td align="right">��λ���룺</td>
			<td align="left" ><input id="code" name="Code" type="text" class="easyui-validatebox" onchange="check()"/></td>
			<td align="right">��ַӢ�ģ�</td>
			<td align="left"><input id="addren" name="AddressEn" type="text" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">Ӣ�����ƣ�</td>
			<td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:486px" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">��ϵ�绰��</td>
			<td align="left"><input id="tel" name="Tel" type="text" class="easyui-validatebox"/></td>
			<td align="right">��λ���棺</td>
			<td align="left"><input id="fax" name="Fax" type="text" class="easyui-validatebox" /></td>
		</tr>
		<tr>
			
			<td align="right">��ϵ�˺���1��</td>
			<td align="left"><input id="contactortel1" name="ContactorTel1" type="text" class="easyui-validatebox"/></td>
			<td align="right">��ϵ�˺���2��</td>
			<td align="left"><input id="contactortel2" name="ContactorTel2" type="text" /></td>
		</tr>
		<tr>
			
		</tr>
		<tr height="30px">
			<td align="right">��ҵ���ࣺ</td>
			<td align="left">
				<input id="cla" name="Classification" class="easyui-combobox" style="width:145px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
			</td>
			<td align="right">��λ״̬��</td>
			<td align="left">
				<select id="sta" name="Status" class="easyui-combobox" style="width:145px" panelHeight="auto" editable="false">
						<option value='0' selected>����</option>
						<option value='1'>ע��</option>
				 </select></td>
		</tr>
		<tr height="30px">
        	<td align="right">�������У�</td>
			<td align="left"><input id="accBank" name="AccountBank" type="text" class="easyui-validatebox" /></td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
			<td align="left"><input id="acc" name="Account" type="text" class="easyui-validatebox" /></td>
        </tr>
        <tr>
			<td align="right">���ö�ȣ�</td>
			<td align="left"><input id="crdamount" name="CreditAmount" type="text" class="easyui-numberbox" /></td>
			<td align="right">���������ֵ��</td>
		<td><input class="easyui-numberbox" id="serviceFeeLimitation" name="ServiceFeeLimitation" /></td>
   		</tr>
		
		<tr>
		
		<td align="right">�ͻ���ֵ�ȼ���</td>
		<td><select style="width:145px" id="customerValueLevel" name="CustomerValueLevel" class="easyui-combobox"  editable="false">
		<option value="1">1��</option>
		<option value="2">2��</option>
		<option value="3">3��</option>
		<option value="4">4��</option>
		<option value="5">5��</option>
		<option value="6">6��</option>
		<option value="7">7��</option>
		<option value="8">8��</option>
		<option value="9">9��</option>
		<option value="10">10��</option>
		
		</select>
		
		</td>
		<td align="right">��ҵ�ȼ���</td>
		<td>
		<select style="width:145px" class="easyui-combobox" id="customerLevel" name="CustomerLevel"  panelHeight="auto" ediable="false">
		<option value="1">VIP�ͻ�</option>
		<option value="2">�ص�ͻ�</option>
		<option value="3">��Ҫ�ͻ�</option>
		<option value="4">һ��ͻ�</option>
		<option value="5">����ͻ�</option>
		</select>
		
		</td >
		</tr>
		<tr>
		
		<td align="right">�䶯���ƣ�</td>
		<td><select style="width:145px" id="trendency" name="Trendency" class="easyui-combobox"  editable="false">
		<option value="1">��������</option>
		<option value="2">��������</option>
		<option value="3">ά����״</option>
		</select></td>
		<td align="right">������ҵ��</td>
		<td><select style="width:145px" id="industry1" name="Industry" class="easyui-combobox" ></select>
		<input type="hidden" id="industryId" name="IndustryId"/></td>
		</tr>
		<tr>
		<td align="right">��ֵ����ֵ��</td>
		<td><input class="easyui-numberbox" id="outputExpectation" name="OutputExpectation" /></td>
		</tr>
		<tr>
		<td align="right">�ҳ϶ȣ�</td>
		<td><input name="Loyalty" class="easyui-numberbox"></input></td>
		<td align="right">����ȣ�</td>
		<td><input name="Satisfaction" class="easyui-numberbox"></input></td>
		</tr>
		<tr height="30px">
			<td align="right">�³�Ҫ��</td>
			<td align="left"  colspan="4"><textarea id="fldema" name="FieldDemands" cols="56" rows="1" ></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">����Ҫ��</td>
			<td align="left"  colspan="4"><textarea id="spcdema" name="SpecialDemands" cols="56" rows="1"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">֤��Ҫ��</td>
			<td align="left"  colspan="4"><textarea id="cerdema" name="CertificateDemands" cols="56" rows="1"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
			<td align="left" colspan="4"><textarea id="rmk" name="Remark" cols="56" rows="1"></textarea> </td>
		</tr>
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">���</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
		</tr>
	</table>
	</form>
        </div>
</DIV>
</DIV>
</body>
</html>
