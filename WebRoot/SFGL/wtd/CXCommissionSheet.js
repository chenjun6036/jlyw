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
		{id:0,name:'强制检定'},
		{id:1,name:'非强制检定'}
	];
	var products1 = [
		{id:0,name:'是'},
		{id:1,name:'否'}
	];
	var products2 = [
		{id:0,name:'需要修理'},
		{id:1,name:'无需修理'}
	];
	var products3 = [
		{id:1,name:'检定'},
		{id:2,name:'校准'},
		{id:3,name:'检测'},
		{id:4,name:'检验'}
	];
	var lastIndex;
	
	
	$('#table6').datagrid({
		width:1000,
		height:500,
		title:'委托单信息',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//		collapsible:true,
		url:'/jlyw/CommissionSheetServlet.do?method=9',
		queryParams:{'CommissionStatus':'1'},
		remoteSort:true,	//默认按服务器上的排序规则
		//idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
			{field:'CommissionTypeName',title:'委托形式',width:70,align:'center',sortable:true},
			{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(rowData.CommissionType == 2 || rowData.CommissionType == '2')
					{
						return rowData.LocaleCommissionDate;
					}
					return value;
				}
			},
			{field:'Status',title:'委托单状态',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'LocaleCommissionCode',title:'现场委托书号',width:100,align:'center'},
			{field:'Quantity',title:'台/件数',width:70,align:'center'},
			{field:'FinishQuantity',title:'完工器具数量',width:70,align:'center'},
			{field:'EffectQuantity',title:'有效器具数量',width:70,align:'center'},
			{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
			{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
			{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
			{field:'Model',title:'型号规格',width:80,align:'center'},
			{field:'Range',title:'测量范围',width:80,align:'center'},
			{field:'Accuracy',title:'精度等级',width:80,align:'center'},
			{field:'Manufacturer',title:'制造厂商',width:80,align:'center'},
			{field:'MandatoryInspection',title:'强制检验',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['MandatoryInspection']=0;
						return "强制检定";
					}
					else
					{
						rowData['MandatoryInspection']=1;
						return "非强制检定";
					}
					
				}},
			{field:'Urgent',title:'是否加急',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Urgent']=0;
						return "是";
					}
					else
					{
						rowData['Urgent']=1;
						return "否";
					}
					
				}},
			{field:'Trans',title:'是否转包',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Trans']=0;
						return "是";
					}
					else
					{
						rowData['Trans']=1;
						return "否";
					}
					
				}},
			{field:'SubContractor',title:'转包方',width:80,align:'center'},
			{field:'Appearance',title:'外观附件',width:80,align:'center'},
			{field:'Repair',title:'需修理否',width:60,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0 || value == '0')
					{
						rowData['Repair']=0;
						return "需要修理";
					}
					else
					{
						rowData['Repair']=1;
						return "无需修理";
					}
					
				}},
			{field:'ReportType',title:'报告形式',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 1 || value == '1')
					{
						rowData['ReportType']=1;
						return "检定";
					}
					if(value == 2 || value == '2')
					{
						rowData['ReportType']=2;
						return "校准";
					}
					else
					{	rowData['ReportType']=3;
						return "检验";
					}
				}},
			{field:'OtherRequirements',title:'其他要求',width:80,align:'center'},
			{field:'FinishLocation',title:'存放位置',width:80,align:'center'},
			{field:'Allotee',title:'派定人',width:80,align:'center'},
			{field:'Remark',title:'备注',width:180,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:"#table6-search-toolbar",
		rowStyler:function(rowIndex, rowData){
			if(rowData.Status == 10){	//已注销
				return 'color:#FF0000'
			}else if(rowData.Status >= 3){	//已完工(包括已结账、已结束)
				return 'color:#339900';	
			}else if(rowData.IsSubContract==true || rowData.FinishQuantity == rowData.EffectQuantity){	//可以完工的器具
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
				//$.messager.alert('提示',"该委托单尚未完工确认，不能打印证书！",'info');
				$('#oneprint-execute-now').hide();
				//return false;
			}else{			
				 $('#oneprint-execute-now').show();
			}
			
			if(rowData.Remark!=''&&rowData.Remark.length>0&&rowData.Remark.indexOf("一证多件")>=0){//判断是否存在一证多件
				var confirmresult=confirm("该委托单包含了一证多件的证书，是否继续？");
				if(confirmresult == false)
					return false;
			}
			
			doOpenSelectCertificatePrintWindow(rowData.Id);
		}
	});
	
	$('#OriginalRecordTable').datagrid({
			title:'原始记录和证书信息',
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
					{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
					{field:'Staff',title:'检/校人员',width:70,align:'center'},
					{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
					{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.ExcelDoc==""){
								return "";
							}else{
								if(rowData.ExcelPdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
								}
							}
						}},
					{field:'CertificateId',title:'证书文件',width:120,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.CertificateDoc==""){
								return "";
							}else{
								if(rowData.CertificatePdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该证书' ><span style='color: #0033FF'>"+rowData.CertificateCode+"</span></a>"
								}
							}
						}
					},
					{field:'VerifierName',title:'核验人',width:70,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.VerifyResult == ""){		//尚未审批
									return "<span title='尚未核验'>"+value+"</span>";
								}
								else if(rowData.VerifyResult == "1" || rowData.VerifyResult == 1 ){ //核验通过
									return "<span style='color: #0033FF' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
								}else{	//核验未通过
									return "<span style='color: #FF0000' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：未通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
								}
							}						
						}
					},
					{field:'AuthorizerName',title:'批准人',width:70,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.AuthorizeResult == ""){		//尚未审批
									return "<span title='尚未签字'>"+value+"</span>";
								}
								else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //批准通过
									return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{	//批准未通过
									return "<span style='color: #FF0000' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：未通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
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
		$.messager.alert('提示',"请选择要查看的委托单！",'info');
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
//完工确认
function doConfirm(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要完工确认的委托单！",'info');
		return false;
	}
	var tempCustomer = rows[0].CustomerName;
	for(var i=0;i<rows.length;i++){
		if(rows[i].CustomerName!=tempCustomer){
			$.messager.alert('提示','所选委托单不属于同一个委托单位,不能同时完工确认！','warning');
			return false;
			
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity < rows[i].EffectQuantity){
			$.messager.alert('提示',"委托单："+rows[i].Code+" 不能完工(完工器具数量少于有效器具数量)！",'info');
			return false;
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity > rows[i].EffectQuantity){
			$.messager.alert('提示',"委托单："+rows[i].Code+" 不能完工(完工器具数量多于有效器具数量)！",'info');
			return false;
		}
	}
	var Ids = "";
	for(var i=0;i<rows.length;i++){
		Ids = Ids + rows[i].Id + ";";
	}
	$('#CommissionSheetId').val(Ids);	//
	
	var result = confirm("您确定要进行完工确认吗?");
	if(result == false){
		return false;
	}
	//alert($("#FinishLocation").val());
	
	if($("#FinishLocation").val()==""){
		var result = confirm("器具存放位置为空，您确定要继续完工确认吗?");
		if(result == false){
			return false;
		}		
	}
	var count=0;
	for(var i = 0; i < rows.length; i++){
		
		count = getInt(count) + getInt(rows[i].ZGCerCount);
	}
	
	if(getInt(count)>0){
		var result = confirm("所选委托单还需要提交 "+ count +" 份纸质原始记录，您确定要继续完工确认吗?");
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
		   		$.messager.alert('提示',result.msg,'info');
				$('#table6').datagrid('reload');
		   }else{
		   		$.messager.alert('提交失败！',result.msg,'error');
		   }
		 }
	});
}

//打印证书(多个委托单)
function PrintCertificate(){	
		$("#CustomerName").combobox("clear");
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
		return false;
	}
	
	for(var i=0;i<rows.length;i++){
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity < rows[i].EffectQuantity){
			$.messager.alert('提示',"委托单："+rows[i].Code+" 不能打印证书(完工器具数量少于有效器具数量)！",'info');
			return false;
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity > rows[i].EffectQuantity){
			$.messager.alert('提示',"委托单："+rows[i].Code+" 不能打印证书(完工器具数量多于有效器具数量)！",'info');
			return false;
		}
	}
	
	var idRowsStr = "";
	
	var comCodesRemark='';
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].Id + ";";
		
		if(rows[i].Remark!=''&&rows[i].Remark.length>0&&rows[i].Remark.indexOf("一证多件")>=0){//判断是否存在一证多件
			if(comCodesRemark=''&&comCodesRemark.length>0)
				comCodesRemark=rows[i].Code;
			else
				comCodesRemark = ',' + comCodesRemark + rows[i].Code;
		}
			
	}
	
	if(comCodesRemark!=''&&comCodesRemark.length>0){
	var confirmresult=confirm("委托单" + comCodesRemark + "包含了一证多件的证书，是否继续？");
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
					var newTitle = "打印预览区：共 0 份证书";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{
					var newTitle = "打印预览区：共 "+ data.Certificates.length +" 份证书";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
				}
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
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

//打印原始记录(多个委托单)
function PrintOriginalRecordExcel(){	
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
		return false;
	}
	
	for(var i=0;i<rows.length;i++){
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity < rows[i].EffectQuantity){
			$.messager.alert('提示',"委托单："+rows[i].Code+" 不能打印证书(完工器具数量少于有效器具数量)！",'info');
			return false;
		}
		if(rows[i].IsSubContract==false && rows[i].FinishQuantity > rows[i].EffectQuantity){
			$.messager.alert('提示',"委托单："+rows[i].Code+" 不能打印证书(完工器具数量多于有效器具数量)！",'info');
			return false;
		}
	}
	var comCodesRemark='';
	var idRowsStr = "";
	for(var i = 0; i < rows.length; i++){
		idRowsStr = idRowsStr + rows[i].Id + ";";
		if(rows[i].Remark!=''&&rows[i].Remark.length>0&&rows[i].Remark.indexOf("一证多件")>=0){//判断是否存在一证多件
			if(comCodesRemark=''&&comCodesRemark.length>0)
				comCodesRemark=rows[i].Code;
			else
				comCodesRemark = ',' + comCodesRemark + rows[i].Code;
		}
			
	}
	
	if(comCodesRemark!=''&&comCodesRemark.length>0){
	var confirmresult=confirm("委托单" + comCodesRemark + "包含了一证多件的证书，是否继续？");
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
					var newTitle = "打印预览区：共 0 份原始记录";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{
					var newTitle = "打印预览区：共 "+ data.Certificates.length +" 份原始记录";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
				}
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
			}
		}
	});	
}
//打开窗口：查看一个委托单的原始记录信息
function doOpenSelectCertificatePrintWindow(comSheetId){
	$("#select_certificate_print_window").window('open');
	//加载原始记录信息
	$('#OriginalRecordTable').datagrid('options').queryParams={'CommissionId':comSheetId};
	$('#OriginalRecordTable').datagrid('reload');
}
//关闭窗口
function doCloseSelectCertificatePrintWindow(){
	$("#select_certificate_print_window").window('close');
}
//打印证书(一个委托单下所选择的多份证书)
function PrintCertificateByOneCommissionSheet(){	
	$("#CustomerName").combobox("clear");

	var rows = $("#OriginalRecordTable").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的证书！",'info');
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
					var newTitle = "打印预览区：共 0 份证书";
					$('#p2').panel({title:newTitle});
					$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{
					var newTitle = "打印预览区：共 "+ data.Certificates.length +" 份证书";
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
					var newTitle = "打印预览区：共 0 份证书";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{
					var newTitle = "打印预览区：共 "+ data.Certificates.length +" 份证书";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/CertificatePrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/CertificatePrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					doCloseSelectCertificatePrintWindow();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
			}
		}
	});	
		
}
//打印原始记录(一个委托单下所选择的多份原始记录)
function PrintOriginalRecordExcelByOneCommissionSheet(){	
	var rows = $("#OriginalRecordTable").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的原始记录！",'info');
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
					var newTitle = "打印预览区：共 0 份原始记录";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{
					var newTitle = "打印预览区：共 "+ data.Certificates.length +" 份原始记录";
					$('#p2').panel({title:newTitle});
					PrintSubmit('/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp',JSON.stringify(data.Certificates));
					//$("#PdfPrintFrame").attr("src","/jlyw/SFGL/wtd/OriginalRecordExcelPrint.jsp?CertificationId="+JSON.stringify(data.Certificates));
					doCloseSelectCertificatePrintWindow();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
			}
		}
	});	
}

function queryreset(){
	$('#SearchForm').form('clear');
	$('#CommissionStatus').val("1");
}
//打印合格证标签(多个委托单)
function  PrintLabel(){	
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
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
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{							
					$('#PrintObj').val(JSON.stringify(data.PrintArray));
				
					$('#formLook').submit();
				}
				
				
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
			}
		}
	});	
		
}

//打印合格证标签(一个委托单下所选择的多份原始记录)
function PrintLabelByOneCommissionSheet(){	
	var rows = $("#OriginalRecordTable").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的原始记录！",'info');
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
					$.messager.alert('提示',"委托单尚未生成证书或委托单尚未完工确认！",'info');
					return false;
				}else{							
					$('#PrintObj').val(JSON.stringify(data.PrintArray));
				
					$('#formLook').submit();
				}
				$("#OriginalRecordTable").datagrid("clearSelections");
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
			}
		}
	});	
}

//完工存放位置修改
function updateLocation(){
	var rows = $("#table6").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要完工确认的委托单！",'info');
		return false;
	}
	if(rows.length >1){
		$.messager.alert('提示',"请选择一份委托单！",'info');
		return false;
	}
	
	var result = confirm("您确定要进行修改完工存放位置吗?");
	if(result == false){
		return false;
	}
	//alert($("#FinishLocation").val());
	
	if($("#FinishLocation").val()==""){
		var result = confirm("器具存放位置为空，您确定要继续完工确认吗?");
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
		   		//$.messager.alert('提示',result.msg,'info');
				$('#table6').datagrid('reload');
		   }else{
		   		$.messager.alert('提交失败！',result.msg,'error');
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
		$.messager.alert('提示',"请选择一个委托单！",'info');
		return false;
	}
	if(rows.length>1){
		$.messager.alert('提示',"您选择了多个委托单，请确认其中一个！",'info');
		return false;
	}
	var rowData = rows[0];
	if(rowData.Status < 3){
		//$.messager.alert('提示',"该委托单尚未完工确认，不能打印证书！",'info');
		$('#oneprint-execute-now').hide();
		//return false;
	}else{			
		 $('#oneprint-execute-now').show();
	}
	
	if(rowData.Remark!=''&&rowData.Remark.length>0&&rowData.Remark.indexOf("一证多件")>=0){//判断是否存在一证多件
		var confirmresult=confirm("该委托单包含了一证多件的证书，是否继续？");
		if(confirmresult == false)
			return false;
	}
	
	doOpenSelectCertificatePrintWindow(rowData.Id);
}