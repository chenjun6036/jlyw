$(function(){
	$("#CustomerName").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
			$("#CustomerTel").val(record.tel);
			$("#CustomerAddress").val(record.address);
			$("#CustomerZipCode").val(record.zipCode);
			$("#ContactPerson").val(record.contactor);
			$("#ContactorTel").val(record.contactorTel);
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
			$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
		}
	});
	var products = [
		{id:0,name:'ǿ�Ƽ춨'},
		{id:1,name:'��ǿ�Ƽ춨'}
	];
	var products1 = [
		{id:0,name:'��'},
		{id:1,name:'��'}
	];
	var products2 = [
		{id:0,name:'��Ҫ����'},
		{id:1,name:'��������'}
	];
	var products3 = [
		{id:1,name:'�춨'},
		{id:2,name:'У׼'},
		{id:3,name:'���'},
		{id:4,name:'����'}
	];
	var lastIndex;
	
	
	$('#table6').datagrid({
		width:1000,
		height:500,
		title:'ί�е���Ϣ',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//		collapsible:true,
		url:'/jlyw/CommissionSheetServlet.do?method=9',
		queryParams:{'CommissionStatus':'1'},
		remoteSort:true,	//Ĭ�ϰ��������ϵ��������
		//idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
			{field:'CommissionTypeName',title:'ί����ʽ',width:70,align:'center',sortable:true},
			{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
			{field:'CommissionDate',title:'ί������',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(rowData.CommissionType == 2 || rowData.CommissionType == '2')
					{
						return rowData.LocaleCommissionDate;
					}
					return value;
				}
			},
			{field:'Status',title:'ί�е�״̬',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'LocaleCommissionCode',title:'�ֳ�ί�����',width:100,align:'center'},
			{field:'Quantity',title:'̨/����',width:70,align:'center'},
			{field:'FinishQuantity',title:'�깤��������',width:70,align:'center'},
			{field:'EffectQuantity',title:'��Ч��������',width:70,align:'center'},
			{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
			{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
			{field:'ApplianceCode',title:'�������',width:80,align:'center'},
			{field:'AppManageCode',title:'������',width:80,align:'center'},
			{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
			{field:'Range',title:'������Χ',width:80,align:'center'},
			{field:'Accuracy',title:'���ȵȼ�',width:80,align:'center'},
			{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
			{field:'MandatoryInspection',title:'ǿ�Ƽ���',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['MandatoryInspection']=0;
						return "ǿ�Ƽ춨";
					}
					else
					{
						rowData['MandatoryInspection']=1;
						return "��ǿ�Ƽ춨";
					}
					
				}},
			{field:'Urgent',title:'�Ƿ�Ӽ�',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Urgent']=0;
						return "��";
					}
					else
					{
						rowData['Urgent']=1;
						return "��";
					}
					
				}},
			{field:'Trans',title:'�Ƿ�ת��',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Trans']=0;
						return "��";
					}
					else
					{
						rowData['Trans']=1;
						return "��";
					}
					
				}},
			{field:'SubContractor',title:'ת����',width:80,align:'center'},
			{field:'Appearance',title:'��۸���',width:80,align:'center'},
			{field:'Repair',title:'�������',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Repair']=0;
						return "��Ҫ����";
					}
					else
					{
						rowData['Repair']=1;
						return "��������";
					}
					
				}},
			{field:'ReportType',title:'������ʽ',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 1 || value == '1')
					{
						rowData['ReportType']=1;
						return "�춨";
					}
					if(value == 2 || value == '2')
					{
						rowData['ReportType']=2;
						return "У׼";
					}
					else
					{	rowData['ReportType']=3;
						return "����";
					}
				}},
			{field:'OtherRequirements',title:'����Ҫ��',width:80,align:'center'},
			{field:'FinishLocation',title:'���λ��',width:80,align:'center'},
			{field:'Allotee',title:'�ɶ���',width:80,align:'center'},
			{field:'Remark',title:'��ע',width:180,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:"#table6-search-toolbar",
		rowStyler:function(rowIndex, rowData){
			if(rowData.Status == 10){	//��ע��
				return 'color:#FF0000'
			}else if(rowData.Status >= 3){	//���깤(�����ѽ��ˡ��ѽ���)
				return 'color:#339900';	
			}else if(rowData.IsSubContract==true || rowData.FinishQuantity == rowData.EffectQuantity){	//�����깤������
				return 'color:#0033CC';
			}
		},
		onSelect:function(rowIndex, rowData){
			if(rowData.Status >= 3){
				$('#FinishLocation').val(rowData.FinishLocation);
				$('#FinishComCode').val(rowData.Code);
			}
		},
		onDblClickRow : function(rowIndex, rowData){
			if(rowData.Status < 3){
				//$.messager.alert('��ʾ',"��ί�е���δ�깤ȷ�ϣ����ܴ�ӡ֤�飡",'info');
				$('#oneprint-execute-now').hide();
				//return false;
			}else{			
				 $('#oneprint-execute-now').show();
			}
			
			if(rowData.Remark!=''&&rowData.Remark.length>0&&rowData.Remark.indexOf("һ֤���")>=0){//�ж��Ƿ����һ֤���
				var confirmresult=confirm("��ί�е�������һ֤�����֤�飬�Ƿ������");
				if(confirmresult == false)
					return false;
			}
			
			doOpenSelectCertificatePrintWindow(rowData.Id);
		}
	});
	
	$('#OriginalRecordTable').datagrid({
			title:'ԭʼ��¼��֤����Ϣ',
			height:300,
			singleSelect:false, 
			fit: false,
			nowrap: false,
			striped: true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
			remoteSort: true,
			idField:'OriginalRecordId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{field:'ApplianceStandardName',title:'���߱�׼����',width:80,sortable:true,align:'center'},
					{field:'Staff',title:'��/У��Ա',width:70,align:'center'},
					{field:'WorkDate',title:'��/У����',width:80,align:'center'},
					{field:'ExcelId',title:'ԭʼ��¼Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.ExcelDoc==""){
								return "";
							}else{
								if(rowData.ExcelPdf == ""){
									return "<span style='color: #FF0000'>δ���</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>�����</span></a>"
								}
							}
						}},
					{field:'CertificateId',title:'֤���ļ�',width:120,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.CertificateDoc==""){
								return "";
							}else{
								if(rowData.CertificatePdf == ""){
									return "<span style='color: #FF0000'>δ���</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='������ظ�֤��' ><span style='color: #0033FF'>"+rowData.CertificateCode+"</span></a>"
								}
							}
						}
					},
					{field:'VerifierName',title:'������',width:70,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.VerifyResult == ""){		//��δ����
									return "<span title='��δ����'>"+value+"</span>";
								}
								else if(rowData.VerifyResult == "1" || rowData.VerifyResult == 1 ){ //����ͨ��
									return "<span style='color: #0033FF' title='����ʱ�䣺"+rowData.VerifyTime+"\r\n��������ͨ��\r\n��ע��"+rowData.VerifyRemark+"'>"+value+"</span>";
								}else{	//����δͨ��
									return "<span style='color: #FF0000' title='����ʱ�䣺"+rowData.VerifyTime+"\r\n��������δͨ��\r\n��ע��"+rowData.VerifyRemark+"'>"+value+"</span>";
								}
							}						
						}
					},
					{field:'AuthorizerName',title:'��׼��',width:70,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.AuthorizeResult == ""){		//��δ����
									return "<span title='��δǩ��'>"+value+"</span>";
								}
								else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //��׼ͨ��
									return "<span style='color: #0033FF' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����ͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{	//��׼δͨ��
									return "<span style='color: #FF0000' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����δͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
							}
						}
					}
				]
			],
			pagination:false,
			rownumbers:true	
		});
	
});



function doLook(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ�鿴��ί�е���",'info');
		return false;
	}
	
	for(var i=0; i<rows.length; i++){
		$('#Code').val(rows[i].Code);
		$('#Pwd').val(rows[i].Pwd);
		$('#doConfirmForm').submit();
	}
}
function doLoadHistoryCommission()
{
	$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=9';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#SearchForm-Code').val(),'CommissionStatus':$('#CommissionStatus').val(),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue'),'localeCode':$('#localeCode').val()};
	$('#table6').datagrid('reload');
}
//�깤ȷ��
function doConfirm(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ�깤ȷ�ϵ�ί�е���",'info');
		return false;
	}
	var tempCustomer = rows[0].CustomerName;
	for(var i=0;i<rows.length;i++){
		if(rows[i].CustomerName!=tempCustomer){
			$.messager.alert('��ʾ','��ѡί�е�������ͬһ��ί�е�λ,����ͬʱ�깤ȷ�ϣ�','warning');
			return false;
			
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity < rows[i].EffectQuantity){
			$.messager.alert('��ʾ',"ί�е���"+rows[i].Code+" �����깤(�깤��������������Ч��������)��",'info');
			return false;
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity > rows[i].EffectQuantity){
			$.messager.alert('��ʾ',"ί�е���"+rows[i].Code+" �����깤(�깤��������������Ч��������)��",'info');
			return false;
		}
	}
	var Ids = "";
	for(var i=0;i<rows.length;i++){
		Ids = Ids + rows[i].Id + ";";
	}
	$('#CommissionSheetId').val(Ids);	//
	
	var result = confirm("��ȷ��Ҫ�����깤ȷ����?");
	if(result == false){
		return false;
	}
	//alert($("#FinishLocation").val());
	
	if($("#FinishLocation").val()==""){
		var result = confirm("���ߴ��λ��Ϊ�գ���ȷ��Ҫ�����깤ȷ����?");
		if(result == false){
			return false;
		}		
	}
	var count=0;
	for(var i = 0; i < rows.length; i++){
		
		count = getInt(count) + getInt(rows[i].ZGCerCount);
	}
	
	if(getInt(count)>0){
		var result = confirm("��ѡί�е�����Ҫ�ύ "+ count +" ��ֽ��ԭʼ��¼����ȷ��Ҫ�����깤ȷ����?");
		if(result == false){
			return false;
		}
	}
	
	$('#Confirm').form('submit',{
		url: '/jlyw/CommissionSheetServlet.do?method=5',
		onSubmit:function(){ return $('#Confirm').form('validate');},
		success:function(data){
		   var result = eval("("+data+")");
		   if(result.IsOK){
		   		$.messager.alert('��ʾ',result.msg,'info');
				$('#table6').datagrid('reload');
		   }else{
		   		$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
		   }
		 }
	});
}

//��ӡ֤��(���ί�е�)
function PrintCertificate(){	
		$("#CustomerName").combobox("clear");
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	
	for(var i=0;i<rows.length;i++){
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity < rows[i].EffectQuantity){
			$.messager.alert('��ʾ',"ί�е���"+rows[i].Code+" ���ܴ�ӡ֤��(�깤��������������Ч��������)��",'info');
			return false;
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity > rows[i].EffectQuantity){
			$.messager.alert('��ʾ',"ί�е���"+rows[i].Code+" ���ܴ�ӡ֤��(�깤��������������Ч��������)��",'info');
			return false;
		}
	}
	
	var idRowsStr = "";
	
	var comCodesRemark='';
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].Id + ";";
		
		if(rows[i].Remark!=''&&rows[i].Remark.length>0&&rows[i].Remark.indexOf("һ֤���")>=0){//�ж��Ƿ����һ֤���
			if(comCodesRemark=''&&comCodesRemark.length>0)
				comCodesRemark=rows[i].Code;
			else
				comCodesRemark = ',' + comCodesRemark + rows[i].Code;
		}
			
	}
	
	if(comCodesRemark!=''&&comCodesRemark.length>0){
	var confirmresult=confirm("ί�е�" + comCodesRemark + "������һ֤�����֤�飬�Ƿ������");
		if(confirmresult == false)
			return false;
	}
	
	$.ajax({
		type:'POST',
		url:'/jlyw/OriginalRecordServlet.do?method=9',
		data:{"CommissionIds":idRowsStr},
		dataType:"json",
		success:function(data, textStatus){
			if(data.IsOK){
				if(data.Certificates.length <= 0){
					var newTitle = "��ӡԤ�������� 0 ��֤��";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{
					var newTitle = "��ӡԤ�������� "+ data.Certificates.length +" ��֤��";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
				}
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		}
	});	
	

}
function PrintSubmit(url,data){
		//var url = '/jlyw/SFGL/wtd/CertificatePrint.jsp';
	/*	document.getElementById('PdfPrintFrame').contentWindow.document.designMode = "on";
		document.getElementById('PdfPrintFrame').contentWindow.document.contentEditable = true;
		document.getElementById('PdfPrintFrame').contentWindow.document.open();
		var html = '<form action="'+url+'" method="post" target="_parent" id="postData_form">'+
                 	'<input id="CertificationId" name="CertificationId" type="hidden" value=\''+data+'\'/>'+
                	'</form>';
        document.getElementById('PdfPrintFrame').contentWindow.document.writeln(html);
        document.getElementById('PdfPrintFrame').contentWindow.document.close();
		document.getElementById('PdfPrintFrame').contentWindow.document.getElementById('postData_form').submit();*/
		document.getElementById('postData_form').action=url;
		$('#CertificationId').val(data);
		document.getElementById('postData_form').submit();
	
}

//��ӡԭʼ��¼(���ί�е�)
function PrintOriginalRecordExcel(){	
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	
	for(var i=0;i<rows.length;i++){
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity < rows[i].EffectQuantity){
			$.messager.alert('��ʾ',"ί�е���"+rows[i].Code+" ���ܴ�ӡ֤��(�깤��������������Ч��������)��",'info');
			return false;
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity > rows[i].EffectQuantity){
			$.messager.alert('��ʾ',"ί�е���"+rows[i].Code+" ���ܴ�ӡ֤��(�깤��������������Ч��������)��",'info');
			return false;
		}
	}
	var comCodesRemark='';
	var idRowsStr = "";
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].Id + ";";
		if(rows[i].Remark!=''&&rows[i].Remark.length>0&&rows[i].Remark.indexOf("һ֤���")>=0){//�ж��Ƿ����һ֤���
			if(comCodesRemark=''&&comCodesRemark.length>0)
				comCodesRemark=rows[i].Code;
			else
				comCodesRemark = ',' + comCodesRemark + rows[i].Code;
		}
			
	}
	
	if(comCodesRemark!=''&&comCodesRemark.length>0){
	var confirmresult=confirm("ί�е�" + comCodesRemark + "������һ֤�����֤�飬�Ƿ������");
		if(confirmresult == false)
			return false;
	}
	
	$.ajax({
		type:'POST',
		url:'/jlyw/OriginalRecordServlet.do?method=16',
		data:{"CommissionIds":idRowsStr},
		dataType:"json",
		success:function(data, textStatus){
			if(data.IsOK){
				if(data.Certificates.length <= 0){
					var newTitle = "��ӡԤ�������� 0 ��ԭʼ��¼";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{
					var newTitle = "��ӡԤ�������� "+ data.Certificates.length +" ��ԭʼ��¼";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
				}
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		}
	});	
}
//�򿪴��ڣ��鿴һ��ί�е���ԭʼ��¼��Ϣ
function doOpenSelectCertificatePrintWindow(comSheetId){
	$("#select_certificate_print_window").window('open');
	//����ԭʼ��¼��Ϣ
	$('#OriginalRecordTable').datagrid('options').queryParams={'CommissionId':comSheetId};
	$('#OriginalRecordTable').datagrid('reload');
}
//�رմ���
function doCloseSelectCertificatePrintWindow(){
	$("#select_certificate_print_window").window('close');
}
//��ӡ֤��(һ��ί�е�����ѡ��Ķ��֤��)
function PrintCertificateByOneCommissionSheet(){	
	$("#CustomerName").combobox("clear");

	var rows = $("#OriginalRecordTable").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��֤�飡",'info');
		return false;
	}
	var idRowsStr = "";
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].OriginalRecordId + ";";
	}

	$.ajax({
		type:'POST',
		url:'/jlyw/OriginalRecordServlet.do?method=10',
		data:{"OriginalRecordIds":idRowsStr},
		dataType:"json",
		success:function(data, textStatus){
			if(data.IsOK){
				/*if(data.Certificates.length <= 0){
					var newTitle = "��ӡԤ�������� 0 ��֤��";
					$('#p2').panel({title:newTitle});
					$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{
					var newTitle = "��ӡԤ�������� "+ data.Certificates.length +" ��֤��";
					$('#p2').panel({title:newTitle});
					var i=0
					var timer=setInterval(function(){
					   $("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+data.Certificates[i].FileId);
					   i++;
					   if(i>=data.Certificates.length){
						  
						   i=1;
						   clearInterval(timer)
						}
			 	   },13000);
					doCloseSelectCertificatePrintWindow();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");*/
				if(data.Certificates.length <= 0){
					var newTitle = "��ӡԤ�������� 0 ��֤��";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{
					var newTitle = "��ӡԤ�������� "+ data.Certificates.length +" ��֤��";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					doCloseSelectCertificatePrintWindow();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		}
	});	
		
}
//��ӡԭʼ��¼(һ��ί�е�����ѡ��Ķ��ԭʼ��¼)
function PrintOriginalRecordExcelByOneCommissionSheet(){	
	var rows = $("#OriginalRecordTable").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ԭʼ��¼��",'info');
		return false;
	}
	
	var idRowsStr = "";
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].OriginalRecordId + ";";
	}
	
	$.ajax({
		type:'POST',
		url:'/jlyw/OriginalRecordServlet.do?method=17',
		data:{"OriginalRecordIds":idRowsStr},
		dataType:"json",
		success:function(data, textStatus){
			if(data.IsOK){
				if(data.Certificates.length <= 0){
					var newTitle = "��ӡԤ�������� 0 ��ԭʼ��¼";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{
					var newTitle = "��ӡԤ�������� "+ data.Certificates.length +" ��ԭʼ��¼";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					doCloseSelectCertificatePrintWindow();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		}
	});	
}

function queryreset(){
	$('#SearchForm').form('clear');
	$('#CommissionStatus').val("1");
}
//��ӡ�ϸ�֤��ǩ(���ί�е�)
function  PrintLabel(){	
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	var idRowsStr = "";
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].Id + ";";
	}
	$.ajax({
		type:'POST',
		url:'/jlyw/OriginalRecordServlet.do?method=19',
		data:{"CommissionIds":idRowsStr},
		dataType:"json",
		success:function(data, textStatus){
			if(data.IsOK){
				if(data.PrintArray.length <= 0){				
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{							
					$('#PrintObj').val(JSON.stringify(data.PrintArray));
				
					$('#formLook').submit();
				}
				
				
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		}
	});	
		
}

//��ӡ�ϸ�֤��ǩ(һ��ί�е�����ѡ��Ķ��ԭʼ��¼)
function PrintLabelByOneCommissionSheet(){	
	var rows = $("#OriginalRecordTable").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ԭʼ��¼��",'info');
		return false;
	}
	var idRowsStr = "";
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].OriginalRecordId + ";";
	}
	
	$.ajax({
		type:'POST',
		url:'/jlyw/OriginalRecordServlet.do?method=20',
		data:{"OriginalRecordIds":idRowsStr},
		dataType:"json",
		success:function(data, textStatus){
			if(data.IsOK){
				if(data.PrintArray.length <= 0){				
					$.messager.alert('��ʾ',"ί�е���δ����֤���ί�е���δ�깤ȷ�ϣ�",'info');
					return false;
				}else{							
					$('#PrintObj').val(JSON.stringify(data.PrintArray));
				
					$('#formLook').submit();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		}
	});	
}

//�깤���λ���޸�
function updateLocation(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ�깤ȷ�ϵ�ί�е���",'info');
		return false;
	}
	if(rows.length >1){
		$.messager.alert('��ʾ',"��ѡ��һ��ί�е���",'info');
		return false;
	}
	
	var result = confirm("��ȷ��Ҫ�����޸��깤���λ����?");
	if(result == false){
		return false;
	}
	//alert($("#FinishLocation").val());
	
	if($("#FinishLocation").val()==""){
		var result = confirm("���ߴ��λ��Ϊ�գ���ȷ��Ҫ�����깤ȷ����?");
		if(result == false){
			return false;
		}		
	}

	
	$('#Confirm').form('submit',{
		url: '/jlyw/CommissionSheetServlet.do?method=21',
		onSubmit:function(){ return $('#Confirm').form('validate');},
		success:function(data){
		   var result = eval("("+data+")");
		   if(result.IsOK){
		   		//$.messager.alert('��ʾ',result.msg,'info');
				$('#table6').datagrid('reload');
		   }else{
		   		$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
		   }
		 }
	});
}

function SelectLeft(){
	var rows = $('#table6').datagrid('getRows');
	var selections = $('#table6').datagrid('getSelections');
	$('#table6').datagrid('selectAll');
	for(var i = 0; i < selections.length; i++){
		$('#table6').datagrid('unselectRow',$('#table6').datagrid('getRowIndex',selections[i]));
	}
}

function CertificateList(){
	var rows = $('#table6').datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert('��ʾ',"��ѡ��һ��ί�е���",'info');
		return false;
	}
	if(rows.length>1){
		$.messager.alert('��ʾ',"��ѡ���˶��ί�е�����ȷ������һ����",'info');
		return false;
	}
	var rowData = rows[0];
	if(rowData.Status < 3){
		//$.messager.alert('��ʾ',"��ί�е���δ�깤ȷ�ϣ����ܴ�ӡ֤�飡",'info');
		$('#oneprint-execute-now').hide();
		//return false;
	}else{			
		 $('#oneprint-execute-now').show();
	}
	
	if(rowData.Remark!=''&&rowData.Remark.length>0&&rowData.Remark.indexOf("һ֤���")>=0){//�ж��Ƿ����һ֤���
		var confirmresult=confirm("��ί�е�������һ֤�����֤�飬�Ƿ������");
		if(confirmresult == false)
			return false;
	}
	
	doOpenSelectCertificatePrintWindow(rowData.Id);
}