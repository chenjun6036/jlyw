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
				url:'/jlyw/CrmServlet.do?method=25',
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
					{field:'CustomerTypeName',title:'��λ����',width:80,align:'center'},
					{field:'CustomerType',hidden:true},
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
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return '<span style="color:red">ע��</span>';
							}
							else 
							{
								rowData['Status']=-1;
							    return "δ֪";
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
					{field:'Contactor',title:'�ⲿ��ϵ��',width:80,align:'center'},
					{field:'ContactorTel1',title:'�ֻ�����1',width:80,align:'center'},
					{field:'ContactorTel2',title:'�ֻ�����2',width:80,align:'center'},
					
					////////////////////////////////////////////////////////////////
					{field:'PayVia',title:'֧����ʽ',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['PayVia']=1;
							return '�ֽ�';
						}
						else if(value==2||value=='2')
						{
							rowData['PayVia']=2;
							return '֧Ʊ';
						}
						else if(value==3||value=='3')
						{
							rowData['PayVia']=3;
							return '����';
						}
						else if(value==4||value=='4')
						{
							rowData['PayVia']=4;
							return 'POS��';
						}
						else 
						{
							rowData['PayVia']=5;
							return '����';
						}
					}
					},
					{field:'PayType',title:'��������',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['PayType']=1;
							return '������';
						}
						else if(value==2||value=='2')
						{
							rowData['PayType']=2;
							return '���ڽ���';
						}
						else if(value==3||value=='3')
						{
							rowData['Ԥ����']=3;
							return '����';
						}
						else if(value==4||value=='4')
						{
							rowData['����']=4;
							return 'POS��';
						}
						
					}},
					{field:'AccountCycle',title:'�������ڣ��£�',width:100,align:'center'},
					{field:'CustomerValueLevel',title:'�ͻ���ֵ�ȼ�',width:100,align:'center'},
					{field:'CustomerLevel',title:'����ҵ�ȼ�',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['CustomerLevel']=1;
							return 'VIP�ͻ�';
						}
						else if(value==2||value=='2')
						{
							rowData['CustomerLevel']=2;
							return '�ص�ͻ�';
						}
						else if(value==3||value=='3')
						{
							rowData['CustomerLevel']=3;
							return '��Ҫ�ͻ�';
						}
						else if(value==4||value=='4')
						{
							rowData['CustomerLevel']=4;
							return 'һ��ͻ�';
						}
						else if(value==5||value=='5')
						{
							rowData['CustomerLevel']=5;
							return '����ͻ�';
						}
					}},
					{field:'Trendency',title:'�䶯����',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['Trendency']=1;
							return '��������';
						}
						else if(value==2||value=='2')
						{
							rowData['Trendency']=2;
							return '��������';
						}
						else if(value==3||value=='3')
						{
							rowData['Trendency']=3;
							return 'ά����״';
						}
					
					}},
					{field:'OutputExpectation',title:'��ֵ����ֵ',width:100,align:'center'},
					{field:'ServiceFeeLimitation',title:'���������ֵ',width:100,align:'center'},
					{field:'Industry',hidden:true},
					{field:'IndustryName',title:'������ҵ',width:100,align:'center'},
					////////////////////////////////////////////////////////////////
					{field:'Modificator',title:'�޸���',width:100,align:'center'},
					{field:'ModifyDate',title:'����޸�����',width:200,align:'center'}
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
						$('#frm_edit_customer').show();
						$('#edit_id').val(select.Id);
						$('#frm_edit_customer').form('load',select);
						$('#cla').combobox('setValue',select.Classification);
						$('#customerLevel').combobox('setValue',select.CustomerLevel);
						//if($('#payVia').combobox('getValue').trim()=="")
						
						$('#frm_edit_customer').form('validate');
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
				title:'�ⲿ��ϵ����Ϣ',
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
					{field:'CurJob',title:'ְҵ',width:100,align:'center'},
					{field:'Cellphone2',title:'��ϵ�绰2',width:100,align:'center'},
					{field:'Birthday',title:'����',width:100,align:'center'},
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
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select)
							{
							$('#log_off_con').window('open');
							$('#frm_log_off_con').show();
							$('#frm_log_off_con').form('clear');
							$('#frm_log_off_con').form('load',select);
							var select2 = $('#table2').datagrid('getSelected');
							$('#formerDep').val(select2.Name);
							$('#curJob2').val("");
							$('#formerJob').val(select.CurJob);
							//$('#edit_con').panel({title:"�޸�"});
							$('#frm_log_off_con').form('validate');
						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						/* 
						text:'ע��',
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
							} */
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
			$('#log_off_con').dialog('close');
			
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
			$('#table2').datagrid('options').url='/jlyw/CrmServlet.do?method=25';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').combobox('getText')),'queryZipCode':encodeURI($('#queryZipCode').val()),'queryAddress':encodeURI($('#queryAddress').val()),'queryTel':encodeURI($('#queryTel').val()),'queryInsideContactor':encodeURI($('#queryInsideContactor').combobox('getText')),'queryClassi':encodeURI($('#queryClassi').combobox('getValues')),'queryCredit':encodeURI($('#queryCredit').val()),'queryContactor':encodeURI($('#queryContactor').val()),'queryContactorTel':encodeURI($('#queryContactorTel').val())};
			$('#table2').datagrid('reload');
			
			//$('#queryname').combobox('setValue',"");
			$('#queryInsideContactor').combobox('setValue',"");
		}
		
		function del(){
			$('#ff1').form('submit',{
				url:'/jlyw/CrmServlet.do?method=27',
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
		
		function savereg(){
		$('#frm_edit_customer').form('submit',{
			url: '/jlyw/CrmServlet.do?method=26',
			onSubmit:function(){ return $('#frm_edit_customer').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('��ʾ',result.msg,'info');
		   		if(result.IsOk){
		   			$("#edit").dialog('close');
		   			$('#table2').datagrid('reload');
		   			cancel();
		   			}
		   		}
		});
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
	    	$('#frm_edit_customer').form('validate');
	    }
	    else 
	    {
	    	$("#accountCycle").combobox({
	    	required:false
	    	});
	    	$('#frm_edit_customer').form('validate');
	    }
	}
	});
}
);
function Resets()
{
$('#query').form('clear');
}

function Reset()
{
$('#frm_edit_customer').form('clear');
}


function LogOffContactor(){
			$('#frm_log_off_con').form('submit',{
				url: '/jlyw/CustomerContactorServlet.do?method=3',
				onSubmit:function(){return $('#frm_log_off_con').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#contactors').datagrid('reload');		
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
			<br /><form id="query">
			<table id="table1" style="width:900px">
            
				<tr>
					<td align="right">��λ���ƣ�</td>
				  	<td align="left"><input class="easyui-combobox" style="width:152px" id="queryname" name="queryName"  panelHeight="150px"></input></td>
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
				  	<td align="left"><input id="queryClassi" name="queryClassi" class="easyui-combobox" style="width:152px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/></td>
                     <td width="100"><a href="javascript:void(0)" onclick="Resets()" class="easyui-linkbutton" iconCls="icon-reload">����</a></td>
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
               
			</table> </form>
		</div>
		

		<table id="table2" style="height:500px; width:900px"></table>
        <br/>
        <table id="contactors" style="height:300px; width:900px"></table>
		

		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 900;height: 500;"
        iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_customer" method="post">
	<table >
		<tr height="30px">
			<td align="right" style="width��20%">��λ���ƣ�<input type="hidden" id="edit_id" name="Edit_id"/></td>
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
		<td align="right">�ⲿ��ϵ�ˣ�</td>
			<td align="left"><input id="con" name="Contactor" type="text" style="border:none;" class="easyui-validatebox" readonly="readonly" /></td>
		</tr>
		
		<tr height="30px">
			<td align="right">��λ���ͣ�</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:145px" required="true" panelHeight="auto" valueField="id" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=29" editable="false">
					
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
						<option value='0'>����</option>
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
		<td><select style="width:145px" id="industry" name="Industry" class="easyui-combobox" ></select>
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
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">�޸�</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="Reset()">����</a></td>
		</tr>
	</table>
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
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">��ϵ�绰1��</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                        	<td align="right">ְҵ��</td>
							<td align="left"><input id="curJob" name="CurJob" class="easyui-validatebox"/></td>
							
							<td align="right">��ϵ�绰2��</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
							
							<td align="right">���գ�</td>
							<td align="left"><input id="birthday" name="Birthday" class="easyui-datebox" editable="false"/></td>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
						</tr>	
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
        
        <div id="log_off_con" class="easyui-window" title="ע��" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			
			<form id="frm_log_off_con" method="post">
			<table id="table4" iconCls="icon-edit" >
						<tr>
							<td align="right">������<input type="hidden" id="Id" name="Id" />
                    								<input id="CustomerId1" name="CustomerId1" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">ԭ��λ��</td>
							<td align="left"><input id="formerDep" name="FormerDep" class="easyui-validatebox" /></td>
                        </tr>
                        <tr>
                        	<td align="right">ԭְλ��</td>
							<td align="left"><input id="formerJob" name="FormerJob" class="easyui-validatebox"/></td>
							
							<td align="right">�ֵ�λ��</td>
							<td align="left"><input id="curDep" name="CurDep" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
							<td align="right">��ְλ��</td>
							<td align="left"><input id="curJob2" name="CurJob" class="easyui-validatebox"/></td>
						</tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="LogOffContactor()">ȷ��</a></td>
							<td></td>
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
