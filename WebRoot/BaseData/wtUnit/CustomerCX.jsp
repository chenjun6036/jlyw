<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>ί�е�λ��Ϣ��ѯ</title>
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
		$(function(){
			$('#queryname').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
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
								var newValue = $('#queryname').combobox('getText');
								$('#queryname').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);

					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
		
			$('#cla').combobox({
				multiple:'true'
			});
			
			$('#queryClassi').combobox({
				multiple:'true'
			});
			
			$('#InsideContactor').combobox({
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
			$('#queryInsideContactor').combobox({
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
				title:'��λ��Ϣ',
				width:900,
				height:500,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CustomerServlet.do?method=2',
				sortName:'Id',
				sortOrder:'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'��λ����',width:200,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'��λ����Ӣ��',width:100,align:'center'},
					{field:'Brief',title:'ƴ������',width:80,align:'center'},
					{field:'RegionId',title:'����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						rowData['RegionId']=value;
						var datas = $('#rid').combobox('getData');
						for(var i = 0; i < datas.length; i++)
						{
							if(datas[i].id==value)
								return datas[i].name;
						}
					}},
					{field:'CustomerType',title:'��λ����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['CustomerType']=0;
							    return "��λ";
							}
							else if(value == 1 || value == '1')
							{
								rowData['CustomerType']=1;
								return "����";
							}
							else if(value == 2 || value == '2')
							{
								rowData['CustomerType']=2;
								return "����ע�ᵥλ";
							}
						}},
					{field:'Code',title:'��λ����',width:70,align:'center'},
					{field:'Address',title:'��λ��ַ',width:80,align:'center'},
					{field:'AddressEn',title:'��λ��ַӢ��',width:100,align:'center'},
					{field:'Tel',title:'��λ�绰',width:60,align:'center'},
					{field:'Fax',title:'��λ����',width:80,align:'center'},
					{field:'ZipCode',title:'�ʱ�',width:60,align:'center'},
					{field:'Status',title:'��λ״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">ע��</span>';
							}
							
						}},
					{field:'CancelDate',title:'ע��ʱ��',width:100,align:'center'},
					{field:'CancelReason',title:'ע��ԭ��',width:100,align:'center'},
					{field:'Balance',title:'��λ���',width:80,align:'center'},
					{field:'AccountBank',title:'��������',width:90,align:'center'},
					{field:'Account',title:'�˺�',width:120,align:'center'},
					{field:'InsideContactor',title:'�ڲ���ϵ��',width:90,align:'center'},
					{field:'Classification',title:'��ҵ����',width:125,align:'center'},
					{field:'FieldDemands',title:'�³�Ҫ��',width:80,align:'center'},
					{field:'CertificateDemands',title:'֤��Ҫ��',width:80,align:'center'},
					{field:'SpecialDemands',title:'����Ҫ��',width:80,align:'center'},
					{field:'CreditAmount',title:'���ö��',width:80,align:'center'},
					{field:'Remark',title:'��ע',width:80,align:'center'},
					{field:'Contactor',title:'��ϵ��',width:80,align:'center'},
					{field:'ContactorTel1',title:'�ֻ�����1',width:80,align:'center'},
					{field:'ContactorTel2',title:'�ֻ�����2',width:80,align:'center'},
					{field:'ModifyDate',title:'����޸�����',width:100,align:'center'},
					{field:'Modificator',title:'�޸���',width:100,align:'center'}
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
						
						/*$('#name').val(select.Name);
						$('#brief').val(select.Brief);
						$('#nameEn').val(select.NameEn);
						$('#customerType').combobox('setValue',select.CustomerType);
						$('#code').val(select.Code);
						$('#rid').combobox('setValue',select.RegionId);
						$('#zcd').val(select.ZipCode);
						$('#addr').val(select.Address);
						$('#addren').val(select.AddressEn);
						$('#tel').val(select.Tel);
						$('#fax').val(select.Fax);
						$('#con').val(select.Contactor);
						$('#contactortel1').val(select.ContactorTel1);
						$('#contactortel2').val(select.ContactorTel2);
						$('#cla').combobox('setValue',select.Classification);
						$('#sta').combobox('setValue',select.Status);
						$('#acc').val(select.Account);
						$('#accBank').val(select.AccountBank);
						$('#crdamount').val(select.CreditAmount);
						$('#fldema').val(select.FieldDemands);
						$('#spcdema').val(select.SpecialDemands);
						$('#cerdema').val(select.CertificateDemands);
						$('#rmk').val(select.Remark);
						$('#mid').val(select.ModificatorId);
						$('#cdate').val(select.CommissionDate);
						
						$('#InsideContactor').combobox('setValue',select.InsideContactor);*/
						$('#edit_Id').val(select.Id);
						$('#form1').form('load',select);
						$('#cla').combobox('setValue',select.Classification);
						$('#form1').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected){
								$('#del').window('open');
								$('#ff1').show();
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
								myExport();
						}
				}],
				onClickRow:function(rowIndex, rowData){
					$('#contactors').datagrid('options').url='/jlyw/CustomerContactorServlet.do?method=2';
					$('#contactors').datagrid('options').queryParams={'CustomerId':rowData.Id};
					$('#CustomerId').val(rowData.Id);
					$('#contactors').datagrid('reload');
				}
			});
			
			$('#contactors').datagrid({
				title:'��ϵ����Ϣ',
				width:900,
				height:300,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'',
				//sortName:'Id',
				//sortOrder:'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'����',width:200,align:'center'},
					{field:'Cellphone1',title:'��ϵ�绰1',width:100,align:'center'},
					{field:'Cellphone2',title:'��ϵ�绰2',width:100,align:'center'},
					{field:'Email',title:'��������',width:80,align:'center'},
					{field:'LastUse',title:'���ʹ��ʱ��',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'����',
						iconCls:'icon-add',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select==null){ 
								$.messager.alert('��ʾ','��ѡ��һ��ί�е�λ','info');
								return;
							}
							$('#edit_con').window('open');
							$('#form2').show();
							$('#form2').form('clear');
							var customer = $('#table2').datagrid('getSelected');
							$('#CustomerId').val(customer.Id);
							$('#edit_con').panel({title:"����"});
						}
				},'-',{
						text:'�޸�',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select){
							$('#edit_con').window('open');
							$('#form2').show();
							$('#form2').form('load',select);
							$('#edit_con').panel({title:"�޸�"});
							$('#form2').form('validate');
						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
						text:'ɾ��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select){
								$.messager.confirm('��ʾ','ȷ��ɾ����',function(r){
									if(r){
										$.ajax({
										type:'POST',
										url:'/jlyw/CustomerContactorServlet.do?method=3',
										data:"id="+select.Id,
										dataType:"html",
										success:function(data){
											$('#contactors').datagrid('reload');
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
				}]
			});
			
			$('#queryname').combobox('setValue',"");
			$('#queryInsideContactor').combobox('setValue',"");
		});
		
		function closed(){
			$('#edit').dialog('close');
			$('#del').dialog('close');
			$('#edit_con').dialog('close');
		}
		
		function edit(){
			$('#form1').form('submit',{
				url: '/jlyw/CustomerServlet.do?method=3',
				onSubmit:function(){return $('#form1').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#table2').datagrid('reload');		
		   		}
			});
		}
		
		function SubmitContactor(){
			$('#form2').form('submit',{
				url: '/jlyw/CustomerContactorServlet.do?method=1',
				onSubmit:function(){return $('#form2').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#contactors').datagrid('reload');		
		   		}
			});
		}
		
		function query()
		{
			$('#table2').datagrid('options').url='/jlyw/CustomerServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').combobox('getValue')),'queryZipCode':encodeURI($('#queryZipCode').val()),'queryAddress':encodeURI($('#queryAddress').val()),'queryTel':encodeURI($('#queryTel').val()),'queryInsideContactor':encodeURI($('#queryInsideContactor').combobox('getValue')),'queryClassi':encodeURI($('#queryClassi').combobox('getValues')),'queryCredit':encodeURI($('#queryCredit').val()),'queryContactor':encodeURI($('#queryContactor').val()),'queryContactorTel':encodeURI($('#queryContactorTel').val())};
			$('#table2').datagrid('reload');
			
			$('#queryname').combobox('setValue',"");
			$('#queryInsideContactor').combobox('setValue',"");
		}
		
		function del(){
			$('#ff1').form('submit',{
				url:'/jlyw/CustomerServlet.do?method=4',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
			   		$.messager.alert('��ʾ',result.msg,'info');
			   		if(result.IsOK)
			   		 closed();
			   		$('#table2').datagrid('reload');	
				}
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#name').val()));
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
<form id="frm_export" method="post" action="/jlyw/CustomerServlet.do?method=7">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ί�е�λ��Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div border="true" style="width:900px;overflow:hidden;position:relative;">
		<div>
			<br />
			<br />
			<table id="table1" style="width:900px">
            <form id="query">
				<tr>
					<td align="right">��λ���ƣ�</td>
				  	<td align="left"><input class="easyui-combobox" style="width:152px" id="queryname" name="queryName" url="" panelHeight="150px"></input></td>
                    <td align="right">�������룺</td>
				  	<td align="left"><input class="easyui-validate" id="queryZipCode" name="queryZipCode"></input></td>
                    <td align="right">��λ��ַ��</td>
				  	<td align="left"><input class="easyui-validate" id="queryAddress" name="queryAddress"></input></td>
				</tr>
                <tr>
					<td align="right">�绰���棺</td>
				  	<td align="left"><input class="easyui-validate" id="queryTel" name="queryTel"></input></td>
                    <td align="right">�ڲ���ϵ�ˣ�</td>
				  	<td align="left"><input id="queryInsideContactor" name="queryInsideContactor" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    <td align="right">���ࣺ</td>
				  	<td align="left"><input id="queryClassi" name="queryClassi" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/></td>
                     <td width="100"><a href="javascript:void(0)" onclick="$('#query').form('clear');" class="easyui-linkbutton" iconCls="icon-reload">����</a></td>
				</tr>
                <tr>
					<td align="right">���öȣ�</td>
				  	<td align="left"><input class="easyui-numberbox" id="queryCredit" name="queryCredit"></input></td>
                    <td align="right">��ϵ�ˣ�</td>
				  	<td align="left"><input id="queryContactor" name="queryContactor" class="easyui-validatebox"/></td>
                    <td align="right">��ϵ�˵绰��</td>
				  	<td align="left"><input id="queryContactorTel" name="queryContactorTel" class="easyui-validatebox"/></td>
					<td width="100"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">��ѯ</a></td>
				</tr>
                </form>
			</table>
		</div>
		

		<table id="table2" style="height:500px; width:900px"></table>
        <br/>
        <table id="contactors" style="height:300px; width:900px"></table>
		

		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 900;height: 500;"
        iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="edit_Id" name="edit_Id" type="hidden"/>
						<tr height="30px">
                            <td align="right" style="width��20%">��λ���ƣ�</td>
                            <td align="left"  style="width��30%"><input id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                            <td align="right" style="width��20%">ƴ�����룺</td>
                            <td align="left"  style="width��30%"><input id="brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">Ӣ�����ƣ�</td>
                            <td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:465px" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��λ���ͣ�</td>
                            <td align="left" >
                                <select id="customerType" name="CustomerType" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
                                    <option value='0'>��λ</option>
                                    <option value='1'>����</option>
                                    <option value='2'>����ע�ᵥλ</option>
                                </select>
                            </td>
                            <td align="right">��λ���룺</td>
                            <td align="left" ><input id="code" name="Code" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr  height="30px">
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
                            <td align="left">
                                <select id="rid" name="RegionId" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
                            </td>
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
                            <td align="left" ><input id="zcd" name="ZipCode" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��λ��ַ��</td>
                            <td align="left"><input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">��ַӢ�ģ�</td>
                            <td align="left"><input id="addren" name="AddressEn" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��ϵ�绰��</td>
                            <td align="left"><input id="tel" name="Tel" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">��λ���棺</td>
                            <td align="left"><input id="fax" name="Fax" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right" rowspan="2">��&nbsp;ϵ&nbsp;�ˣ�</td>
                            <td align="left" rowspan="2"><input id="con" name="Contactor" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">��ϵ�˺���1��</td>
                            <td align="left"><input id="contactortel1" name="ContactorTel1" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">��ϵ�˺���2��</td>
                            <td align="left"><input id="contactortel2" name="ContactorTel2" type="text" /></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��ҵ���ࣺ</td>
                            <td align="left">
                                <input id="cla" name="Classification" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
                            </td>
                            <td align="right">��λ״̬��</td>
                            <td align="left">
                                <select id="sta" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
                                        <option value='0' selected>����</option>
                                        <option value='1'>ע��</option>
                                 </select></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�������У�</td>
                            <td align="left"><input id="accBank" name="AccountBank" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">��&nbsp;&nbsp;�ţ�</td>
                            <td align="left"><input id="acc" name="Account" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">���ö�ȣ�</td>
                            <td align="left"><input id="crdamount" name="CreditAmount" class="easyui-numberbox" required="true"/></td>
                            <td align="right">�ڲ���ϵ�ˣ�</td>
                            <td align="left"><input id="InsideContactor" name="InsideContactor" class="easyui-combobox" style="width:150px" valueField="name" textField="name" panelHeight="150px"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�³�Ҫ��</td>
                            <td align="left"  colspan="3"><textarea id="fldema" name="FieldDemands" cols="55" rows="2" ></textarea> </td>
                        </tr>
                        <tr height="30px">
                            <td align="right">����Ҫ��</td>
                            <td align="left"  colspan="3"><textarea id="spcdema" name="SpecialDemands" cols="55" rows="2"></textarea> </td>
                        </tr>
                        <tr height="30px">
                            <td align="right">֤��Ҫ��</td>
                            <td align="left"  colspan="3"><textarea id="cerdema" name="CertificateDemands" cols="55" rows="2"></textarea> </td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
                            <td align="left" colspan="3"><textarea id="rmk" name="Remark" cols="55" rows="2"></textarea> </td>
                        </tr>
                        <tr height="50px">	
                            <td></td>
                            <td><a class="easyui-linkbutton" icon="icon-ok" name="Edit" href="javascript:void(0)" onclick="edit()">�޸�</a></td>
                            <td></td>
                            <td><a class="easyui-linkbutton" icon="icon-cancel" name="Cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
                        </tr>
					</table>
				</div>
			</form>
		</div>
		
        <div id="edit_con" class="easyui-window" title="�޸�" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
                    <input id="CustomerId" name="CustomerId" type="hidden"/>
						<tr>
							<td align="right">������</td>
							<td align="left" colspan="3"><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
							<td align="right">��ϵ�绰1��</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
							<td align="right">��ϵ�绰2��</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
						<tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="SubmitContactor()">�ύ</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
        
		<div id="del" class="easyui-window" title="ע��" style="width:280px;height:130px;" 
    iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false">
    <form id="ff1" method="post">
    	<table>
    	<input id="del_id" name="del_id" type="hidden"/>
    	<tr>
    		<td>ע��ԭ��</td>
    		<td><input id="reason" class="easyui-combobox" name="reason" url="/jlyw/ReasonServlet.do?method=0&type=22" style="width:170px;" valueField="name" textField="name" panelHeight="auto" /></td>
    	</tr>
    	<tr height="40px">	
			<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="del()">ע��</a></td>
			<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
		</tr>
    	</table>
        </form>
        </div>
        </div>
    </DIV>
    </DIV>
</body>
</html>
