<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���۵���ѯ</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
     <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	 <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
     <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
     <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
     <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script>
		$(function (){
			$('#Search_Appli').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				valueField:'standardname',
				textField:'standardname',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
					}
			});
			$('#Customer').combobox({
				valueField:'name',
				textField:'name',
				onChange:function(newValue,oldValue){
					var allData = $(this).combobox('getData');
					if(allData !=null && allData.length >0){
						for(var i=0;i<allData.length; i++){
							if(newValue == allData[i].name){
								return false;
							}
						}
					}
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){
						var newValue = $('#Customer').combobox('getText');
						$('#Customer').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					},700);
				}
			});
			
			$('#table2').datagrid({
				title:'���۵���Ϣ��ѯ',
				height:500,
				width:800,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/QuotationServlet.do?method=2',
				
				idField:'num',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'num',title:'���',width:180,align:'center'},
					{field:'CustomerName',title:'ѯ�۵�λ����',width:180,align:'center'},
					{field:'Contactor',title:'ѯ�۵�λ��ϵ��',width:120,align:'center'},
					{field:'ContactorTel',title:'��ϵ�˵绰',width:100,align:'center'},
					{field:'CarCost',title:'��ͨ��',width:80,align:'center'},
					{field:'OfferDate',title:'����ʱ��',width:80,align:'center'},
					{field:'OffererId',title:'������',width:80,align:'center',editor:'text'},
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
								return '<span style="color:red;">ע��</span>'
;
							}
						}
					},
					
	//				{field:'Cost',title:'����',width:80,align:'center'},	
					{field:'Version',title:'�汾��',width:80,align:'center'},		
					{field:'Remark',title:'��ע',width:80,align:'center'},
					{field:'Detail',title:'��Ŀ����',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							$('#quotationId').val(rowData.num);
							return "<a href='javascript:void(0)' class='easyui-linkbutton' iconCls='icon-search' onclick=\"item_Manage('" + rowData.num + "');\" >�鿴</a>";  
						}
						
					}
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//zhengchang
						return 'color:#000000'
					}else if(rowData.Status == 1){	//zhuxiao
						return 'color:#FF0000';	
					}else{
						return 'color:#000000';
					}
				},
				toolbar:[{
					text:'ѯ�۵�λ��Ϣ�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#number').val(select.num);
							$('#ContactorTel').val(select.ContactorTel);
							$('#Contactor').val(select.Contactor);
							$('#Customername').val(select.CustomerName);
							
							$('#quotation_edit').window('open');	
						}else{
							$.messager.alert('warning','��ѡ��һ������','warning');
						}
					}
				},'-',{
					text:'���۵���Ŀ�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#quotationId').val(select.num);
							$('#table4').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
							$('#table4').datagrid('options').queryParams={'quotationId':encodeURI($('#quotationId').val())};
							$('#table4').datagrid('reload');
							$('#item_edit').window('open');	
						}else{
							$.messager.alert('warning','��ѡ��һ������','warning');
						}
					}
				},'-',{
					text:'ע��',
					iconCls:'icon-remove',
					handler:function(){
							var rows = $('#table2').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('����','ȷ��ע����',function(r){
								if(r){
									for(var i=rows.length-1; i>=0; i--){
										$.ajax({
											type:'POST',
											url:'/jlyw/QuotationServlet.do?method=4',
											data:'id='+rows[i].num,
											dataType:"json",
											success:function(data, textStatus){
												alert(data.msg);
											}
										});
									}
									$('#table2').datagrid('reload');
								}
								});
							}else{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
					text:'��ӡ��ѡ���۵�',
					iconCls:'icon-print',
					handler:function(){
							var row = $('#table2').datagrid('getSelected');
							
							if(row)
							{
								$('#quotationId1').val(row.num);
								$('#printquotation').submit();
								
				
							}else{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
					text:'������ѡ���۵�',
					iconCls:'icon-save',
					handler:function(){
					  var row = $('#table2').datagrid('getSelected');
							
					   if(row){
							ShowWaitingDlg("���ڵ��������Ժ�......");
							$('#paramsStr').val(row.num);
							$('#contactorcel').val(row.ContactorTel);
							$('#contactor').val(row.Contactor);
							$('#customername').val(row.CustomerName);
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
					  }else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
					  }
					  
				}	
			}]
			});
			
		});
		
		//�����Ŀ
		$(function(){
			$('#table_appli').datagrid({
		//		title:'�ܼ�������Ϣ',
				width:750,
				height:450,
				singleSelect:true,
				fit: false,
				nowrap: true,
				striped: true,
				rownumbers:false,
				loadMsg:'���ݼ�����......',
//				url:'/jlyw/QuotationServlet.do?method=0',
				remoteSort: false,
				frozenColumns:[[
					{field:'ck',checkbox:true},
					{field:'TargetApplianceId',title:'�ܼ����߱��',width:100,sortable:true,align:'center'}
				]],
				columns:[[
					{field:'StandardNameName',title:'���߱�׼����',width:100,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'Accuracy',title:'����',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Fee',title:'��׼����',width:80,align:'center'}
				]],
				pagination:true,
				toolbar:"#table_appli-search-toolbar"
			});
		});
		
		//���۵���Ŀ
		$(function(){
			var lastIndex;
			$('#table4').datagrid({
				width:780,
				height:430,
             	singleSelect:true, 
                nowrap: false,
                striped: true,
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Id',title:'���',width:40,align:'center'},
					
					{field:'StandardName',title:'�ܼ����߱�׼����',width:120,align:'center'},
					{field:'CertificateName',title:'�ܼ�����֤������',width:120,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'Accuracy',title:'����',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Quantity',title:'̨����',width:80,align:'center',editor:'text'},
					{field:'CertType',title:'֤������',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value=='1'){
								rowData['CertType']="1";
								return "�춨";
							}
							else if(value == '4'){
								rowData['CertType']="4";
								return "����";
							}
							else if(value == '2'){
								rowData['CertType']="2";
								return "У׼";
							}
							else{
								rowData['CertType']="3";
								return "���";
							}
						}
					},
					{field:'SiteTest',title:'�ֳ����',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value=='0'){
								rowData['SiteTest'] ="0";
								return "��";
							}
							else{
								rowData['SiteTest'] ="1";
								return "��";
							}
						}
					},
					{field:'MinCost',title:'��������',width:80,align:'center',
						formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
						
					}},
					{field:'MaxCost',title:'�������',width:80,align:'center',
						formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
						
					}},
					{field:'Remark',title:'��ע',width:80,align:'center'}
				]],
				rownumbers:false,
				pagination:false,
				toolbar:[{
					text:'ɾ����Ŀ',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("ȷ��Ҫ�Ƴ���Щ��Ŀ��");
						if(result == false){
							return false;
						}
						var row = $('#table4').datagrid('getSelected');
						if(!row){
							$.messager.alert('��ʾ','��ѡ����Ҫ�Ƴ�����Ŀ��','info');
							return false;
						}
						else{
							var rows = $('#table4').datagrid('getSelections');
							var length = rows.length;
							for(var i=length-1; i>=0; i--){
								var index = $('#table4').datagrid('getRowIndex', rows[i]);
								$('#table4').datagrid('deleteRow', index);
							}
						}
					}
				},'-',{
					text:'�޸���Ŀ',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table4').datagrid('getSelected');
						if(select){
							$('#quo_edit').window('open');
							$('#form1').show();
							
							
							
							//֤�鳣������
							$('#CertificateName').combobox({
								url:'/jlyw/AppliancePopularNameServlet.do?method=7&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
							});
							
							//
							$('#Model1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=1&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'

							});
							//
							$('#Range1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
	
							});
							//
							$('#Accuracy1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'

							});
							$('#Accuracy1').combobox('setValue',select.Accuracy);
							$('#Range1').combobox('setValue',select.Range);
							$('#Model1').combobox('setValue',select.Model);
																						
							$('#Application').val(select.StandardName);
							$('#Quantity').val(select.Quantity);
							$('#SiteTest').val(select.SiteTest);
							$('#CertificateName').combobox('setValue',select.CertificateName);
							$('#CertType').val(select.CertType);
							$('#Remark').val(select.Remark);
					}else{
						$.messager.alert('����','��ѡ��һ������','warning');
						}
					}
				},'-',{
					text:'�����Ŀ',
					iconCls:'icon-add',
					handler:function(){	
						$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
						$('#table_appli').datagrid('reload');
						$('#item_add').window('open');
						$('#form3').show();
					}
				},'-',
				{
					text:'ȷ���޸�',
					iconCls:'icon-ok',
					handler:function(){
						var result = confirm("ȷ��Ҫ�޸��𣿣�ע���ı��۵����ָ�������");
						if(result == false){
							return false;
						}
						var rows = $('#table4').datagrid('getRows');
						if(rows.length == 0){
							$.messager.alert('��ʾ','�ñ��۵���ĿΪ�գ��������ɱ��۵���','info');
							return false;
						}
						
						$('#form2').form('submit',{
							url:'/jlyw/QuotationServlet.do?method=3',
							onSubmit:function(){
								var selects = $('#table2').datagrid('getSelected');
								if(selects.length==0){
									$.messager.alert('��ʾ','����ѡ�񱨼۵���','info');
									return false;
								}
								
								$("#quotationId").val(JSON.stringify(selects));
								$("#Item").val(JSON.stringify(rows));
								
								return $('#form2').form('validate');
							},
							success:function(data){
								var result = eval("("+data+")");
								alert(result.msg);
								if(result.IsOK){
									$('#item_edit').window('close');
									cancel();
									$('#table2').datagrid('reload');
								}
							}
						});
						$('#item_edit').window('close');
						
					}	
				}]
			});
		})	
		

		//���ݱ��۵���ź�ѯ�۵�λ��ѯ���۵�
		function query(){
			$('#table2').datagrid('options').url='/jlyw/QuotationServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'QuotationId':encodeURI($('#QuotationId').val()),'Customer':encodeURI($('#Customer').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getText')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getText')),'QuoStatus':encodeURI($('#QuoStatus').val())};
			$('#table2').datagrid('reload');
			$('#QuotationId').val('');
		}
		
		//�޸ı��۵���Ŀ
		function edit(){
			if($('#Quantity').val()==''){
				$.messager.alert('��ʾ','�뽫������д������','info');
			}
			else{
				if($('#Quantity').val()=='0'){
					$.messager.alert('��ʾ','���������0��̨������','info');
					return false;
				}
				var row = $('#table4').datagrid('getSelected');
				var index= $('#table4').datagrid('getRowIndex',row);
				var name_obj=document.getElementById("SiteTest");
				var certype_obj=document.getElementById("CertType");
				
				$.ajax({   //�����ܼ����ߣ��ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ�֮��Ĳ��Ҽ��������Сֵ��
					type:'POST',
					url:'/jlyw/TargetApplianceServlet.do?method=13',
					data:{'Application':encodeURI($('#Application').val()),'Model':encodeURI($('#Model1').combobox('getText')),'Range':encodeURI($('#Range1').combobox('getText')),'Accuracy':encodeURI($('#Accuracy1').combobox('getText'))},
					dataType:"json",
					success:function(data, textStatus){
						if(data.IsOK){//����ȡ�óɹ�		
						 			
							$('#table4').datagrid('updateRow',{
								index:index,
								row:{
									CertificateName:$('#CertificateName').combobox('getText'),
									Model:$('#Model1').combobox('getText'),
									Accuracy:$('#Accuracy1').combobox('getText'),
									Range:$('#Range1').combobox('getText'),
									Quantity:$('#Quantity').val(),
									CertType:certype_obj.options[certype_obj.selectedIndex].value,
									SiteTest:name_obj.options[name_obj.selectedIndex].value,
									MinCost: data.MinFee,
									MaxCost: data.MaxFee,
									Remark:$('#Remark').val()
								}
							});							
							$('#quo_edit').window('close');
							
						}else{//����ȡ��ʧ��
							$.messager.alert('����',data.msg,'error');
						}
					}
				});
		
				
				
			}
		}
		
		//ȡ�����۵��޸�
		function cancel(){
			$('#Quantity').val('');
			$('#Remark').val('');
			
			$('#quo_edit').window('close');
		}
		
		function item_Manage(number){
			$('#quotationId').val(number);
			$('#table4').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
			$('#table4').datagrid('options').queryParams={'quotationId':encodeURI($('#quotationId').val())};
			$('#table4').datagrid('reload');
			$('#item_edit').window('open');	
		}
		
		//��ѯ�ܼ�����
		function query_appli(){
			$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
			$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getText'))};
			$('#table_appli').datagrid('reload');
		}
		
		//�������۵���Ŀ�������ݵ���Ϣ������д
		function Add_QuotaItem(){
			
			var row=$('#table_appli').datagrid('getSelected');
			if(row){
				
									
				//֤�鳣������
				$('#CertificateName1').combobox({
					url:'/jlyw/AppliancePopularNameServlet.do?method=7&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Model').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=1&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Range').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Accuracy').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				$('#Accuracy').combobox('setValue',row.Accuracy);
				$('#Range').combobox('setValue',row.Range);
				$('#Model').combobox('setValue',row.Model);
				var rows=$('#table_appli').datagrid('getSelections');
				for(var i=0; i<rows.length; i++){
					$('#Application1').val(rows[i].StandardNameName);
				}
				$('#add').window('open');
			}
			else{
				$.messager.alert('��ʾ','��ѡ��һ������!','info');
			}
		}
		
		//�����۵���Ŀ����datagrid
		function edit_item(){	
			if($('#Quantity1').val()==''){
				$.messager.alert('��ʾ','�뽫������д������','info');
			}
			else{
				if($('#Quantity1').val()=='0'){
					$.messager.alert('��ʾ','���������0��̨������','info');
					return false;
				}																
				var table_appli_row = $('#table_appli').datagrid('getSelected');
				var table4_rows = $('#table4').datagrid('getRows');
				var index = $('#table4').datagrid('getRows').length;
				var name_obj=document.getElementById("SiteTest1");
				var certype_obj=document.getElementById("CertType1");
				
				for(var j=0;j<index;j++){
					if(table4_rows[j].StandardName==table_appli_row.StandardNameName && table4_rows[j].Model==$('#Model').combobox('getText') && table4_rows[j].Range==$('#Range').combobox('getText') && table4_rows[j].Accuracy==$('#Accuracy').combobox('getText'))
					{
						$.messager.alert('��ʾ','ѡ���������ܼ����ߣ�','warning');
						return false;
					}
				}
								
				$.ajax({   //�����ܼ����ߣ��ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ�֮��Ĳ��Ҽ��������Сֵ��
					type:'POST',
					url:'/jlyw/TargetApplianceServlet.do?method=13',
					data:{'Application':encodeURI($('#Application1').val()),'Model':encodeURI($('#Model').combobox('getText')),'Range':encodeURI($('#Range').combobox('getText')),'Accuracy':encodeURI($('#Accuracy').combobox('getText'))},
					dataType:"json",
					success:function(data, textStatus){
						if(data.IsOK){//����ȡ�óɹ�							
							$('#table4').datagrid('insertRow',{
								index:index,
								row:{
									CertificateName:$('#CertificateName1').combobox('getText'),
									StandardName: table_appli_row.StandardNameName,
									TargetApplianceId: table_appli_row.TargetApplianceId,
									Model:$('#Model').combobox('getText'),
									Accuracy:$('#Accuracy').combobox('getText'),
									Range:$('#Range').combobox('getText'),
									Quantity:$('#Quantity1').val(),
									CertType:certype_obj.options[certype_obj.selectedIndex].value,
									SiteTest:name_obj.options[name_obj.selectedIndex].value,
									MinCost: data.MinFee,
									MaxCost: data.MaxFee,
									Remark:$('#Remark1').val()
								}
							});							
							
							$('#add').dialog('close');
							$('#item_add').dialog('close');
							$('#table_appli').datagrid('clearSelections');
							$('#table2').datagrid('reload');
						}else{//����ȡ��ʧ��
							$.messager.alert('����',data.msg,'error');
						}
					}
				});
			
			
				
			}
		}
		
		//ȡ�������۵���Ŀ����datagrid��
		function cancel_item(){
			$('#Quantity1').val('');
			$('#CertificateName1').val('');
			$('#CertType1').val('');
			$('#Remark1').val('');
			$('#add').window('close');
		}
		
		//�༭ѯ�۵�λ��Ϣ
		function edit_Quotation(){	
		
			$('#quotation_edit_form').form('submit',{
				url:'/jlyw/QuotationServlet.do?method=10',
				onSubmit:function(){
					
					return $('#quotation_edit_form').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					
					if(result.IsOK){
						$.messager.alert('��ʾ��','�޸ĳɹ���','info');
						$('#quotation_edit').window('close');
						$('#table2').datagrid('reload');
					}else{
						$.messager.alert('����',result.msg,'error');
					}
				}
			});
			
		}
		
		function cancel_Quotation(){
			$('#quotation_edit').window('close');
		}
	</script>
</head>

<body >
<form id="frm_export" method="post" action="/jlyw/QuotationServlet.do?method=9">
<input id="paramsStr" name="paramsStr" type="hidden" />
<input id="customername" name="customername" type="hidden" />
<input id="contactortel" name="contactortel" type="hidden" />
<input id="contactor" name="contactor" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="���۵���ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<div  align="center" style="width:800px;padding:10px" class="easyui-panel" title="ͳ������" collapsible="false"  closable="false">
			<table id="table1" style="width:780px;">
				<tr>
						
					<td align="left">
					���۵���ţ�<input id="QuotationId" name="QuotationId" type="text" style="width:120px;"/>&nbsp;
				    ѯ�۵�λ��<input id="Customer" name="Customer" type="text" style="width:120px;"/>&nbsp;
					<select name="QuoStatus" id="QuoStatus" style="width:100px"><option value="" selected="selected">ȫ��</option><option value="0">����</option><option value="1" >ע��</option></select>&nbsp;
					</td>
					</tr>
				<tr>
					<td align="left">
					&nbsp;&nbsp;��ʼʱ�䣺<input name="date1" id="dateTimeFrom" type="text" style="width:123px;"  class="easyui-datebox" >&nbsp;					
					����ʱ�䣺<input name="date2" id="dateTimeEnd" type="text" style="width:122px;"  class="easyui-datebox" >&nbsp;
					
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
					</td>
					
				</tr>
			</table>
	   </div>
	   
	   <div style="+position:relative;">
			<table id="table2" iconCls="icon-search" > </table>	
		</div>
		
		<div id="quo_edit" class="easyui-window" title="�޸ı��۵���Ŀ" style="padding:10px;width:280px;height:320px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" >
			<form id="form1" method="post">
				<table id="table3">
					<tr height="25px">
						<td align="right">�ܼ�����:</td>
						<td align="left"><input id="Application" name="Application" type="text" readonly="readonly"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">�ͺŹ��:</td>
						<td align="left"><input id="Model1" name="Model" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">������Χ:</td>
						<td align="left"><input id="Range1" name="Range" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">׼ȷ�ȵȼ�:</td>
						<td align="left"><input id="Accuracy1" name="Accuracy" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px">
						<td align="right">̨/��&nbsp;��:</td>
						<td align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox" value="1" required="true" /></td>
					</tr>
					<tr height="25px">
						<td align="right">�ֳ�����:</td>
						<td align="left">
							<select id="SiteTest" name="SiteTest" style="width:152px">   
								<option value="1" selected="selected">��</option>
								<option value="0">��</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">�ܼ�����&nbsp;<br />֤������:</td>
						<td align="left"><input id="CertificateName" name="CertificateName" type="text" style="width:152px"//></td>
					</tr>
					<tr height="25px">
						<td align="right">֤������:</td>
						<td align="left">
							<select id="CertType" name="CertType" style="width:152px" />
								<option value="1" selected="selected">�춨</option>
								<option value="4">����</option>
								<option value="2">У׼</option>
								<option value="3">���</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע:</td>
						<td align="left"><input id="Remark" name="Remark" type="text" /></td>
					</tr>
					<tr height="30px">	
						<td align="right" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">ȷ��</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">ȡ��</a></td>
					
					</tr>
				</table>
				
			</form>
		</div>
		
		<div id="item_edit" class="easyui-window" title="���۵���Ŀ��Ϣ�鿴" style="width: 800px;height: 500px;"
		iconCls="icon-search" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" name="form2" method="post">
				<input id="quotationId" name="quotationId" type="hidden" />
				<input id="Item" name="Item" type="hidden" /> 
				<table id="table4" iconCls="icon-search" style="height:430px; width:780px"></table>	
			</form>
		</div>
		
		<div id="item_add" class="easyui-window" title="�����Ŀ" style="width:800px;height:500px" iconCls="icon-search" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			
				<table id="table_appli" iconCls="icon-search"> </table>	
			
		</div>
		
		
		<div id="table_appli-search-toolbar" style="padding:2px 0">
			<form id="form3" name="form3" method="post">
				<table cellpadding="0" cellspacing="0" style="width:100%">
					<tr>
						<td style="padding-left:2px">
							<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="Add_QuotaItem()">���뱨�۵���Ŀ</a>
						</td>
						<td align="right">�ܼ����߱�׼��:</td>
						<td width="21%"><input id="Search_Appli" name="Search_Appli" type="text" style="width:152px" /></td>
						<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query_appli()">��ѯ</a></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="add" class="easyui-window" title="��ӱ��۵���Ŀ" style="width:280px;height:320px;padding:10px"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true" >
			<form id="form4" method="post">
				<table id="table5">
					<tr height="25px">
						<td align="right">�ܼ�����:</td>
						<td align="left"><input id="Application1" name="Application" type="text" readonly="readonly"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">�ͺŹ��:</td>
						<td align="left"><input id="Model" name="Model" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">������Χ:</td>
						<td align="left"><input id="Range" name="Range" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">׼ȷ�ȵȼ�:</td>
						<td align="left"><input id="Accuracy" name="Accuracy" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px">
						<td align="right">̨/��&nbsp;��:</td>
						<td align="left"><input id="Quantity1" name="Quantity" type="text" class="easyui-numberbox" value="1" required="true" /></td>
					</tr>
					<tr height="25px">
						<td align="right">�ֳ�����:</td>
						<td align="left">
							<select id="SiteTest1" name="SiteTest" style="width:152px">   
								<option value="1" selected="selected">��</option>
								<option value="0">��</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">�ܼ�����&nbsp;<br />֤������:</td>
						<td align="left"><input id="CertificateName1" name="CertificateName" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px">
						<td align="right">֤������:</td>
						<td align="left">
							<select id="CertType1" name="CertType" style="width:152px" />
								<option value="1" selected="selected">�춨</option>
								<option value="4">����</option>
								<option value="2">У׼</option>
								<option value="3">���</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע:</td>
						<td align="left"><input id="Remark1" name="Remark" type="text" /></td>
					</tr>
					<tr height="30px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit_item()">ȷ��</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_item()">ȡ��</a></td>
						
					</tr>
				</table>
				
			</form>
		</div>
		
		<div id="quotation_edit" class="easyui-window" title="���۵�������Ϣ��Ŀ" style="width:280px;padding:10px"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true" >
			<form id="quotation_edit_form" method="post">
			<input id="number" name="number" type="hidden" readonly="readonly"/>
				<table >
					<tr height="35px">
						<td align="right">ѯ�۵�λ:</td>
						<td align="left"><input id="Customername" name="CustomerName" class="easyui-validatebox" required="true" type="text" readonly="readonly"/></td>
					</tr>
					<tr height="35px" >
						<td align="right">��λ��ϵ��:</td>
						<td align="left"><input id="Contactor" name="Contactor" class="easyui-validatebox" required="true"   type="text" style="width:152px"/></td>
					</tr>
					<tr height="35px" >
						<td align="right">��ϵ�˵绰:</td>
						<td align="left"><input id="ContactorTel" name="ContactorTel" class="easyui-numberbox" required="true"  type="text" style="width:152px"/></td>
					</tr>
					
					<tr height="40px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit_Quotation()">ȷ��</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_Quotation()">�ر�</a></td>
						
					</tr>
				</table>
				
			</form>
		</div>
		
		<form id="printquotation" name="printquotation" method="post" action="/jlyw/QuotationServlet.do?method=7" target="QuotPrintFrame">
				<input id="quotationId1" name="quotationId" type="hidden" />
				
		</form>
		
		
	
		<iframe id="QuotPrintFrame" name="QuotPrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	</div></DIV>
</body>

</html>
