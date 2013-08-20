$(function(){
	$('#file_upload').uploadify({
		'script'    : '/jlyw/FileUploadServlet.do',
		'scriptData':{'method':'4','FileType':'101'},	//method必须放在这里，不然会与其他的参数连着，导致出错
		'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
		'queueSizeLimit': 1,//一次只能传一个文件
		'buttonImg' : '../uploadify/selectfiles.png',
		'fileDesc'  : '支持格式:xls', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
		'fileExt'   : '*.xls;',   //允许的格式
		onComplete: function (event,ID,fileObj,response,data) {  
			var retData = eval("("+response+")");
			if(retData.IsOK == false){
				$.messager.alert('提示',retData.msg,'error');
			}else{
				if(retData.msg!=null&&retData.msg.length>0){
					$.messager.alert('提示',retData.msg,'info');
				}else{
					$.messager.alert('提示',"文件上传成功！",'info');
				}
				$('#OriginalRecord').datagrid('reload');
				$('#file_upload_window').window('close');
			}
		},
		onAllComplete: function(event,data){
			CloseWaitingDlg();
		}
	 });
	
	$('#SpecificationId').combobox({
		valueField:'id',
		textField:'name_num',
		onSelect:function(record){
			var rows = $('#table_Specification').datagrid('getRows');
			var index = rows.length;
			for(var i=0;i<index;i++){
				if(rows[i].id==record.id){
					$.messager.alert('提示！',"不能重复添加！",'info');
					return false;
				}
			}
			$('#table_Specification').datagrid('insertRow', {
					index: index,
					row: record
				});		
		},
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
			$(this).combobox('reload','/jlyw/SpecificationServlet.do?method=9&SpecificationName='+newValue);
		}
	});	
	$('#table_Specification').datagrid({
		title:'技术规范信息查询',
		fit:false,
		width:600,
		height:200,
		singleSelect:false, 
		nowrap: false,
		striped: true,
		//url:'/jlyw/SpecificationServlet.do?method=2',
		sortName: 'id',
		remoteSort: false,
		singleSelect:true, 
		idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'specificationcode',title:'技术规范编号',width:100,align:'center'},
			{field:'namecn',title:'技术规范名称(中文)',width:120,align:'center'},
			{field:'type',title:'类别',width:80,align:'center'},
			{field:'localecode',title:'所内编号',width:80,align:'center'},
			{field:'issuedate',title:'发布日期',width:80,align:'center'},
			{field:'effectivedate',title:'实施日期',width:80,ealign:'center'},
			{field:'repealdate',title:'废止日期',width:80,align:'center'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:"#table_Specification_toolbar"
	});
	$('#StandardId').combobox({
		valueField:'id',
		textField:'name_num',
		onSelect:function(record){
			var rows = $('#table_Standard').datagrid('getRows');
			var index = rows.length;
			for(var i=0;i<index;i++){
				if(rows[i].id==record.id){
					$.messager.alert('提示！',"不能重复添加！",'info');
					return false;
				}
			}
			$('#table_Standard').datagrid('insertRow', {
					index: index,
					row: record
				});		
		},
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
			$(this).combobox('reload','/jlyw/StandardServlet.do?method=9&StandardName='+newValue);
		}
	});	
	$('#table_Standard').datagrid({
		title:'计量标准信息查询',
		fit:false,
		width:600,
		height:200,
		singleSelect:true, 
		nowrap: false,
		striped: true,
		//url:'/jlyw/StandardServlet.do?method=2',
		sortName: 'id',
		remoteSort: false,
		singleSelect:true, 
		idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'slocalecode',title:'所内编号',width:80,align:'center'},
			{field:'certificatecode',title:'计量标准证书号',width:100,align:'center'},
			{field:'name',title:'计量标准名称',width:80,align:'center'},				
			{field:'standardcode',title:'计量标准代码',width:80,align:'center'},
			{field:'projectcode',title:'项目编号',width:80,align:'center'},
			{field:'issuedby',title:'发证单位',width:120,align:'center'},
			{field:'issuedate',title:'发证日期',width:100,align:'center'},
			{field:'validdate',title:'有效期',width:100,align:'center'},
			{field:'range',title:'测量范围',width:80,align:'center'},
			{field:'uncertain',title:'不确定度误差',width:120,align:'center'},
			{field:'warnslot',title:'有效期预警天数',width:120,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value==null)
						return "";
					return value+"天";
				}},
			{field:'handler',title:'项目负责人',width:80,align:'center'},
			{field:'projecttype',title:'项目类型',width:120,align:'center'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:"#table_Standard_toolbar"
	});
	$('#StandardApplianceId').combobox({
		valueField:'id',
		textField:'name_num',
		onSelect:function(record){
			var rows = $('#table_StandardAppliance').datagrid('getRows');
			var index = rows.length;
			for(var i=0;i<index;i++){
				if(rows[i].id==record.id){
					$.messager.alert('提示！',"不能重复添加！",'info');
					return false;
				}
			}
			$('#table_StandardAppliance').datagrid('insertRow', {
					index: index,
					row: record
				});		
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
			$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=14&QueryName='+newValue);
		}
	});	
	$('#table_StandardAppliance').datagrid({  //标准器具信息
		title:'标准器具',
		fit:false,
		width:600,
		height:200,
		singleSelect:false, 
		nowrap: false,
		striped: true,
		url:'',
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'name',title:'器具名称',width:100,align:'center'},
			{field:'model',title:'型号规格',width:100,align:'center'},
			{field:'range',title:'测量范围',width:80,ealign:'center'},
			{field:'uncertain',title:'不确定度',width:50,align:'center'},
			{field:'release',title:'出厂信息',width:120,align:'center'},
			{field:'permanentcode',title:'固定资产编号',width:120,align:'center'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:"#table_StandardAppliance_toolbar"
	});
	
	var OriginalRecordLastSelectedId = null;	//最后一次所选择的记录Id
	$('#OriginalRecord').datagrid({
		title:'原始记录信息',
		height:350,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/OriginalRecordServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: true,
		idField:'OriginalRecordId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'CertificateCode',title:'证书编号',width:120,rowspan:2,align:'center'},
				{title:'器具信息',colspan:8,align:'center'},
				{title:'检验信息',colspan:7,align:'center'},
				{title:'费用信息',colspan:7,align:'center'}
			],[
				{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
				{field:'Model',title:'规格型号',width:60,align:'center'},
				{field:'Range',title:'测量范围',width:80,align:'center'},
				{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
				{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
				{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
				{field:'ManageCode',title:'管理编号',width:80,align:'center'},
				{field:'Quantity',title:'器具数量',width:60,align:'center'},
				
				{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
				{field:'Staff',title:'检/校人员',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(rowData.StaffChecked){
							return value;
						}else{
							return "<span style='color: #FF0000' title='尚未核定'>"+value+"</span>";
						}
					}
				},
				{field:'WorkDate',title:'检/校日期',width:80,align:'center'},				
				{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.ExcelDoc == ""){
							return "";
						}else{
							if(rowData.ExcelPdf == ""){
								return "<span style='color: #FF0000'>未完成</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
							}
						}
					}},
				{field:'CertificateId',title:'证书文件',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.CertificateDoc==""){
							return "";
						}else{
							if(rowData.CertificatePdf == ""){
								return "<span style='color: #FF0000'>未完成</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该证书' ><span style='color: #0033FF'>已完成</span></a>"
							}
						}
					}
				},
				{field:'VerifierName',title:'核验人',width:80,align:'center',
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
				{field:'AuthorizerName',title:'批准人',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
							return "";
						}else{
							if(rowData.AuthorizeResult == ""){		//尚未审批
								return "<span title='尚未签字'>"+value+"</span>";
							}
							else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //批准通过
								if(rowData.IsAuthBgRuning==true){
									return "<span style='color: #009933' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过(后台签字执行中...)\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{
									return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
							}else{	//批准未通过
								return "<span style='color: #FF0000' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：未通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
							}
						}
					}
				},
				
				{field:'TestFee',title:'检测费',width:60,align:'center'},
				{field:'RepairFee',title:'修理费',width:60,align:'center'},
				{field:'MaterialFee',title:'材料费',width:60,align:'center'},
				{field:'CarFee',title:'交通费',width:60,align:'center'},
				{field:'DebugFee',title:'调试费',width:60,align:'center'},
				{field:'OtherFee',title:'其他费用',width:60,align:'center'},
				{field:'TotalFee',title:'总计费用',width:60,align:'center'}
			]
		],
		pagination:false,
		rownumbers:true	,
		onSelect:function(rowIndex, rowData){
			if(rowData.OriginalRecordId == OriginalRecordLastSelectedId){
				$(this).datagrid("unselectAll");
				OriginalRecordLastSelectedId = null;
			}else{
				OriginalRecordLastSelectedId = rowData.OriginalRecordId;
			}
		},
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#OriginalRecord').datagrid('reload');
			}
		},'-',{
			text:'注销',
			iconCls:'icon-cancel',
			handler:function(){
				var rows = $("#OriginalRecord").datagrid("getSelections");
				if(rows.length == 0){
					$.messager.alert('提示',"请选择要注销的原始记录！",'info');
					return false;
				}
				if(rows.length > 1){
					$.messager.alert('提示',"一次最多只能注销一张原始记录！",'info');
					return false;
				}
				
				var result = confirm("该操作不可撤销，您确定要注销该原始记录吗？");
				if(result == false){
					return false;
				}
				$.ajax({
						type: "post",
						url: "/jlyw/OriginalRecordServlet.do?method=2",
						data: {"OriginalRecordId":rows[0].OriginalRecordId},
						dataType: "json",	//服务器返回数据的预期类型
						beforeSend: function(XMLHttpRequest){
						},
						success: function(data, textStatus){
							if(data.IsOK){
								$("#OriginalRecord").datagrid("unselectAll");
								$("#OriginalRecord").datagrid("reload");
								
							}else{
								$.messager.alert('提交失败！',data.msg,'error');
							}
						},
						complete: function(XMLHttpRequest, textStatus){
							//HideLoading();
						},
						error: function(){
							//请求出错处理
						}
				});
			}
		},'-',{
			text:'更新Excel配置文件',
			iconCls:'icon-download',
			handler:function(){
				doUpdateExcelXml();
			}
		},'-',{
			text:'编辑原始记录',
			iconCls:'icon-edit',
			handler:function(){
				doLoadOrEditOriginalRecord(1);
			}
		},'-',{
			text:'下载原始记录',
			iconCls:'icon-download',
			handler:function(){
				doLoadOrEditOriginalRecord(0);
			}
		},'-',{
			text:'脱机文件上传',
			iconCls:'icon-upload',
			handler:function(){
				var row = $("#OriginalRecord").datagrid("getSelected");
				if(row == null){
					$.messager.alert('提示',"请选择一条原始记录！",'info');
					return false;
				}
				$('#file_upload_window').window('open');
			}
		},'-',{
			text:'返回',
			iconCls:'icon-undo',
			handler:function(){
				javascript:history.go(-1);
			}
		},'-',{
			text:'跳转到核验人和产值分配页面',
			iconCls:'icon-redo',
			handler:function(){
			
				$('#CheckAndFeeForm').submit();	
				
			}
		}]
	});
	$('#Appliance').datagrid({
		title:'选择受检器具',
		width:600,
		height:300,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/TargetApplianceServlet.do?method=5',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		//idField:'userid',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'StandardNameName',title:'标准名称',width:80,sortable:true,align:'center'},
				{field:'TargetApplianceName',title:'受检器具名称',width:80,align:'center'},
				{field:'Model',title:'型号规格',width:65,align:'center'},
				{field:'Range',title:'测量范围',width:80,align:'center'},
				{field:'Accuracy',title:'准确度等级',width:65,align:'center'},
				{field:'Fee',title:'检定费',width:60,align:'center'},
				{field:'SRFee',title:'小修费用',width:60,align:'center'},
				{field:'MRFee',title:'中修费用',width:60,align:'center'},
				{field:'LRFee',title:'大修费用',width:60,align:'center'},
				{field:'TargetApplianceRemark',title:'备注',width:80,align:'center'}
			]
		],
		pagination:true,
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		onSelect:function(rowIndex, rowData){
			//赋值隐藏字段
			$("#AddOriginalForm input[name='Model']:first").val(rowData.Model);
			$("#AddOriginalForm input[name='Range']:first").val(rowData.Range);
			$("#AddOriginalForm input[name='Accuracy']:first").val(rowData.Accuracy);
			$("#AddOriginalForm input[name='TargetApplianceId']:first").val(rowData.TargetApplianceId);
			setAddOriginalFormTestFee();	//检定费
			setAddOriginalFormRepairFee();	//修理费
			
			//模板文件刷新
			$('#template_file_grid_new').datagrid('options').queryParams={'FilesetName':rowData.StandardNameFilesetName};
			$('#template_file_grid_new').datagrid('reload');
			
			//常用名称刷新
			$("#AddOriginalForm-ApplianceName").combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=5&StandardNameId='+rowData.StandardNameId);
			
			//制造厂刷新
			$("#AddOriginalForm-Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);	
			
			//核验员刷新
			$("#AddOriginalForm-VerifyUser").combobox('reload','/jlyw/TaskAssignServlet.do?method=7&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);
		},
		toolbar:"#function-toolbar"
	});
   
   
   $('#template_file_grid').datagrid({
		title:'选择原始记录模板',
		singleSelect:true, 
		fit: true,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/FileDownloadServlet.do?method=5',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		idField:'_id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'filename',title:'文件名',width:150,sortable:true,align:'center'},
				{field:'length',title:'大小',width:50,align:'center'},
				{field:'uploadDate',title:'上传日期',width:80,align:'center'},
				{field:'uploadername',title:'上传人',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}]
	});
   $('#template_file_grid_new').datagrid({
		title:'选择原始记录模板',
		width:200,
		height:300,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/FileDownloadServlet.do?method=5',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		idField:'_id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'filename',title:'文件名',width:100,sortable:true,align:'center'},
				{field:'length',title:'大小',width:50,align:'center'},
				{field:'uploadDate',title:'上传日期',width:80,align:'center'},
				{field:'uploadername',title:'上传人',width:60,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid_new').datagrid('reload');
			}	
		}]
	});
	
	$('#template_doc_file_grid').datagrid({
		title:'证书模板',
		singleSelect:true, 
		fit: true,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/FileDownloadServlet.do?method=6',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		idField:'_id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'filename',title:'文件名',width:150,sortable:true,align:'center'},
				{field:'length',title:'大小',width:50,align:'center'},
				{field:'uploadDate',title:'上传日期',width:80,align:'center'},
				{field:'uploadername',title:'上传人',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}],
		pagination:true
	});
	
	$("#VerifyAndAuthorizeForm-Authorizer").combobox({
		valueField:'id',
		textField:'name',
		required:true,
		editable:false,
		onLoadSuccess:function(){
			//默认选中第一个
			try{
				var rows = $(this).combobox('getData');
				$(this).combobox('select', rows[0].id);
			}catch(ex){}
		}
	});
	
	$("#FeeAssignForm-FeeAllotee").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
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
			$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
		}
	});
	$("#FeeAssignFormBySheet-FeeAllotee").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
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
			$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
		}
	});
	var lastIndex;
	$('#fee_assign_table').datagrid({
		title:'产值分配',
		singleSelect:true, 
		height: 200,
		width:500,
		nowrap: false,
		striped: true,
		url:'/jlyw/CertificateFeeAssignServlet.do?method=0',
		remoteSort: false,
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'FeeAlloteeName',title:'姓名',width:70,sortable:true,align:'center'},
				{field:'TestFee',title:'检定费',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'RepairFee',title:'修理费',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'MaterialFee',title:'材料费',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'CarFee',title:'交通费',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'DebugFee',title:'调试费',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'OtherFee',title:'其它费',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}}
			]
		],
		onLoadSuccess : function(data){
			if(data.total == 0 && $("#fee_info_temp_form input[name='CertificateCode']:first").val() != ""){
				$(this).datagrid('insertRow', {
					index: 0,
					row:{
						FeeAlloteeName:$("#fee_info_temp_form input[name='Staff']:first").val(),//选择fee_info_temp_form下第一个名为Staff的input的值
						TestFee:getInt($("#fee_info_temp_form input[name='TestFee']:first").val()),
						RepairFee:getInt($("#fee_info_temp_form input[name='RepairFee']:first").val()),
						MaterialFee:getInt($("#fee_info_temp_form input[name='MaterialFee']:first").val()),
						CarFee:getInt($("#fee_info_temp_form input[name='CarFee']:first").val()),
						DebugFee:getInt($("#fee_info_temp_form input[name='DebugFee']:first").val()),
						OtherFee:getInt($("#fee_info_temp_form input[name='OtherFee']:first").val())
					}
				});
			}
		},
		onClickRow:function(rowIndex){
			$('#fee_assign_table').datagrid('endEdit', lastIndex);
			$('#fee_assign_table').datagrid('beginEdit', rowIndex);
			lastIndex = rowIndex;
		},
		rownumbers:true	,
		pagination:false,
		toolbar:[{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("您确定要删除所选的产值分配记录吗？");
				if(result == false){
					return false;
				}
				$('#fee_assign_table').datagrid('acceptChanges');
				var rows = $('#fee_assign_table').datagrid('getSelections');
				for(var i=0; i<rows.length; i++){
					var index = $('#fee_assign_table').datagrid('getRowIndex', rows[i]);
					$('#fee_assign_table').datagrid('deleteRow', index);
				}
			}
		}]
	});
	
	var lastIndexBySheet;
	$('#fee_assign_table_by_sheet').datagrid({
		title:'产值分配',
		singleSelect:true, 
		height: 250,
		width:560,
		nowrap: false,
		striped: true,
		remoteSort: false,
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'FeeAlloteeName',title:'姓名',width:70,sortable:true,align:'center'},
				{field:'TestFee',title:'检定费额度',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'RepairFee',title:'修理费额度',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'MaterialFee',title:'材料费额度',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'CarFee',title:'交通费额度',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'DebugFee',title:'调试费额度',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'OtherFee',title:'其它费额度',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}}
			]
		],
		onClickRow:function(rowIndex){
			$('#fee_assign_table_by_sheet').datagrid('endEdit', lastIndexBySheet);
			$('#fee_assign_table_by_sheet').datagrid('beginEdit', rowIndex);
			lastIndexBySheet = rowIndex;
		},
		rownumbers:true	,
		pagination:false,
		toolbar:[{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("您确定要删除所选的产值分配记录吗？");
				if(result == false){
					return false;
				}
				$('#fee_assign_table_by_sheet').datagrid('acceptChanges');
				var rows = $('#fee_assign_table_by_sheet').datagrid('getSelections');
				for(var i=0; i<rows.length; i++){
					var index = $('#fee_assign_table_by_sheet').datagrid('getRowIndex', rows[i]);
					$('#fee_assign_table_by_sheet').datagrid('deleteRow', index);
				}
			}
		}]
	});
	
	//器具标准名
	$("#function-toolbar-StandardName").combobox({
		valueField:'standardname',
		textField:'standardname',
		//required:true,
		onSelect:function(record){
			doSearchTargetAppliance();
			$("#standardNameIdstandardNameId").val(record.id);
			$("#function-toolbar-Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType=0&ApplianceSpeciesId='+record.id);	//型号规格
			$("#function-toolbar-Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType=0&ApplianceSpeciesId='+record.id);	//测量范围
			$("#function-toolbar-Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType=0&ApplianceSpeciesId='+record.id);	
			
		},
		onChange:function(newValue, oldValue){
			var allData = $(this).combobox('getData');
			if(allData != null && allData.length > 0){
				for(var i=0; i<allData.length; i++)
				{
					if(newValue==allData[i].standardname){
						return false;
					}
				}
			}
			$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
		}
	});
	$("#function-toolbar-Model").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
			doSearchTargetAppliance();
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
			if($("#function-toolbar-StandardName").combobox('getValue')==null||$("#function-toolbar-StandardName").combobox('getValue').length==0){
				$.messager.alert('提示！',"器具标准名不能为空！",'info');
				$(this).combobox('clear');
				return false;
			}
			$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=7&Type=1&standardNameId=' + $("#standardNameIdstandardNameId").val() + '&QueryName=' + encodeURI(newValue));
		}

	});
	$("#function-toolbar-Range").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
			doSearchTargetAppliance();
		},
		onChange:function(newValue, oldValue){
			if($("#function-toolbar-StandardName").combobox('getValue')==null||$("#function-toolbar-StandardName").combobox('getValue').length==0){
				$.messager.alert('提示！',"器具标准名不能为空！",'info');
				$(this).combobox('clear');
				return false;
			}
			var allData = $(this).combobox('getData');
			if(allData != null && allData.length > 0){
				for(var i=0; i<allData.length; i++)
				{
					if(newValue==allData[i].name){
						return false;
					}
				}
			}
			$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=7&Type=2&standardNameId=' + $("#standardNameIdstandardNameId").val() + '&QueryName=' + encodeURI(newValue));
		}
	});
	$("#function-toolbar-Accuracy").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
			doSearchTargetAppliance();
		},
		onChange:function(newValue, oldValue){
			if($("#function-toolbar-StandardName").combobox('getValue')==null||$("#function-toolbar-StandardName").combobox('getValue').length==0){
				$.messager.alert('提示！',"器具标准名不能为空！",'info');
				$(this).combobox('clear');
				return false;
			}
			var allData = $(this).combobox('getData');
			if(allData != null && allData.length > 0){
				for(var i=0; i<allData.length; i++)
				{
					if(newValue==allData[i].name){
						return false;
					}
				}
			}
			$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=7&Type=3&standardNameId=' + $("#standardNameIdstandardNameId").val() + '&QueryName=' + encodeURI(newValue));
		}
	});
	
	$('#history_certificate_grid').datagrid({
		title:'历史证书信息',
		singleSelect:true, 
		fit: false,
		width:620,
		height:450,
		nowrap: false,
		striped: true,
		url:'/jlyw/OriginalRecordServlet.do?method=18',
		remoteSort: true,
		idField:'OriginalRecordId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'CertificateCode',title:'证书编号',width:60,align:'center'},
				{field:'CustomerName',title:'委托单位',width:60,align:'center'},				
				{field:'ApplianceStandardName',title:'器具标准名',width:80,sortable:true,align:'center'},
				{field:'ApplianceName',title:'常用名称',width:60,align:'center'},
				{field:'Model',title:'型号规格',width:60,align:'center'},
				{field:'Range',title:'测量范围',width:60,align:'center'},
				{field:'Accuracy',title:'准确度等级',width:60,align:'center'},
				
				{field:'Staff',title:'检校人员',width:60,align:'center'},
				{field:'WorkDate',title:'检校日期',width:60,align:'center'},
				{field:'Verifier',title:'核验人',width:60,align:'center'},
				{field:'WorkType',title:'工作性质',width:60,align:'center'},
				{field:'WorkLocation',title:'工作地点',width:60,align:'center'},
				{field:'Manufacturer',title:'制造单位',width:60,align:'center'},
				{field:'ApplianceCode',title:'出厂编号',width:60,align:'center'},
				{field:'ManageCode',title:'管理编号',width:60,align:'center'},
				{field:'Temp',title:'环境温度',width:60,align:'center'},
				{field:'Humidity',title:'相对湿度',width:60,align:'center'},
				{field:'Pressure',title:'大气压',width:60,align:'center'},
				{field:'OtherCond',title:'其它',width:60,align:'center'},
				{field:'StdOrStdAppUsageStatus',title:'标准(器具)状态检查',width:60,align:'center'},
				{field:'AbnormalDesc',title:'异常情况说明',width:60,align:'center'},
				{field:'Conclusion',title:'结论',width:60,align:'center'},
				{field:'Quantity',title:'器具数量',width:60,align:'center'},
				{field:'RepairLevel',title:'修理级别',width:60,align:'center'},
				{field:'MaterialDetail',title:'配件明细',width:60,align:'center'},
				{field:'TestFee',title:'检定费',width:60,align:'center'},
				{field:'RepairFee',title:'修理费',width:60,align:'center'},
				{field:'CarFee',title:'交通费',width:60,align:'center'},
				{field:'DebugFee',title:'调试费',width:60,align:'center'},
				{field:'MaterialFee',title:'配件费',width:60,align:'center'},
				{field:'OtherFee',title:'其他费',width:60,align:'center'},
				{field:'FirstIsUnqualified',title:'首检是否合格',width:60,align:'center'},
				{field:'Remark',title:'备注',width:60,align:'center'},
				
				{field:'Mandatory',title:'是否强管',width:60,align:'center'},
				{field:'MandatoryCode',title:'强管唯一编号',width:60,align:'center'},
				{field:'MandatoryPurpose',title:'强管类型',width:60,align:'center'},
				{field:'MandatoryItemType',title:'项别',width:60,align:'center'},
				{field:'MandatoryType',title:'种别',width:60,align:'center'},
				{field:'MandatoryApplyPlace',title:'使用安装地点',width:60,align:'center'}
				
			]
		],
		rownumbers:true	,
		pagination:true,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:"#load_from_history_certificate-toolbar"
	});
	
	$("#load_from_history_certificate-toolbar-WorkStaff").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
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
			$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
		}
	});
	
	//检校日期默认当天
	var nowDate = new Date();
	$("#AddOriginalForm-WorkDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	
	//$('#loading').hide();

});
function doLoadCommissionSheet(fromByCode){	//查找委托单
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
			$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
			
			if(fromByCode){	//从ComSheetInspectByCode.jsp页面调用
				$('#AddOriginalForm-TaskId').combobox('loadData',[]);
			}
			
			if($('#Code').val()=='' || $('#Pwd').val() == ''){
				$.messager.alert('提示！',"委托单无效！",'info');
				return false;
			}
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//勾选
				}
				
				//加载原始记录信息
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#OriginalRecord').datagrid('reload');
				
				//加载受检器具信息
				$('#Appliance').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#Appliance').datagrid('reload');
				
				//hidden字段
				$('#TheCommissionId').val($('#CommissionId').val());
				
				
				if(fromByCode){	//从ComSheetInspectByCode.jsp页面调用
					//加载检验项目信息
					$('#AddOriginalForm-TaskId').combobox('reload', '/jlyw/TaskAssignServlet.do?method=5&CommissionId='+$('#CommissionId').val());
				}
				
				//加载新增原始记录的参数值
				$("#AddOriginalForm").form('load',result.CommissionObj);
				if(result.CommissionObj.ReportType == 1 || result.CommissionObj.ReportType == '1'){
					$("#AddOriginalForm-WorkType option[value='检定']").attr("selected", true);
					//$("#AddOriginalForm-Conclusion").combobox("setValue", "合格");
				}else if(result.CommissionObj.ReportType == 2 || result.CommissionObj.ReportType == '2'){
					$("#AddOriginalForm-WorkType option[value='校准']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 3 || result.CommissionObj.ReportType == '3'){
					$("#AddOriginalForm-WorkType option[value='检测']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 4 || result.CommissionObj.ReportType == '4'){
					$("#AddOriginalForm-WorkType option[value='检验']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkType option[value='']").attr("selected", true);
				}
				
			
				if(result.CommissionObj.CommissionType == 2 || result.CommissionObj.CommissionType == '2'){
					$("#AddOriginalForm-WorkLocation option[value='被测仪器使用现场']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkLocation option[value='本所实验室']").attr("selected", true);
				}
				
				if(result.CommissionObj.Mandatory == 1 || result.CommissionObj.Mandatory == '1'){
					$("#AddOriginalForm-Mandatory option[value='否']").attr("selected", true);
				}else{
					$("#AddOriginalForm-Mandatory option[value='是']").attr("selected", true);
				}
				
				$("#AddOriginalForm input[name='Quantity']:first").val("1");
				
				//$("#AddOriginalForm-FirstIsUnqualified").combobox("select", "合格");
									
			}else{
				$.messager.alert('查询失败！',result.msg,'error');
			}
		}
	});  
}


function doLoadOrEditOriginalRecord(type)  //下载或编辑原始记录:type为0时下载原始记录，1时在weboffice编辑原始记录
{
	if(type == 1 || type =="1"){	//编辑原始记录
		$('#downloadfile-submit-form-DownloadType').val("1");
	}else{
		$('#downloadfile-submit-form-DownloadType').val("0");
	}
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		if(type==1 || type=="1"){
			$("#AddOriginalForm-OriginalRecordId").val('');
			$("#AddOriginalForm-WorkStaffId").val('');
			$("#AddOriginalForm input[name='Quantity']:first").val('1');	//重置器具数量
			
			$("#AddOriginalForm-StandardAppliancesString").val('');//在无关联标准器具、规范等的情况下，选择的标准器具
			$("#AddOriginalForm-StandardsString").val('');//在无关联标准器具、规范等的情况下，选择的计量标准
			$("#AddOriginalForm-SpecificationsString").val('');//在无关联标准器具、规范等的情况下，选择的技术规范
			
			$("#add_original_record_window").window("open");
			
			$("#function-toolbar-StandardName").combobox('clear');
			$("#function-toolbar-Model").combobox('loadData',[]);
			$("#function-toolbar-Range").combobox('clear');
			$("#function-toolbar-Range").combobox('loadData',[]);
			$("#function-toolbar-Accuracy").combobox('clear');
			$("#function-toolbar-Accuracy").combobox('loadData',[]);
//			$("#AddOriginalForm").form('validate');
			return false;
		}else{
			$.messager.alert('提示！','请先选择一条原始记录！','info');
			return false;
		}
		
	}
	$('#weboffice-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	$('#weboffice-submit-form-StaffId').val(row.StaffId);
	$('#downloadfile-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	if(row.ExcelId != "" && row.ExcelDoc != ""){		//已有原始记录
		if(type == 1 || type =="1"){	//编辑原始记录
			$('#weboffice-submit-form-TemplateFileId').val('');
			$('#weboffice-submit-form-FileName').val(row.ExcelFileName);
			$('#weboffice-submit-form-Version').val(row.ExcelVersion);
			doOpenEditXlsWebOfficeWindow();
		}else{	//下载原始记录
			$('#downloadfile-submit-form-TemplateFileId').val("");
			$('#downloadfile-submit-form').submit();
		}
	}else{	//没有原始记录
		if(row.CertificateDoc == ""){	//选择原始记录模板
			doOpenSelecteTemplateWindow(row.TemplateFilesetName);
		}else{	//直接编辑证书
			$('#makecertificate-submit-form-XlsTemplateFileId').val('');
			$('#makecertificate-submit-form-OriginalRecordId').val(row.OriginalRecordId);
			$('#makecertificate-submit-form-FileName').val(row.CertificateFileName);
			$('#makecertificate-submit-form-Version').val(row.CertificateVersion);
			$('#makecertificate-submit-form-StaffId').val(row.StaffId);
			doOpenEditDocWebOfficeWindow();
		}
		
	}
}
function doOpenSelecteTemplateWindow(filesetname)	//打开选择原始记录模板文件的窗口
{
	$("#select_xls_template_window").window('open');
	$('#template_file_grid').datagrid('options').queryParams={'FilesetName':filesetname};
	$('#template_file_grid').datagrid('reload');
}
function doSelecteXlsTemplate()	//选择原始记录模板文件
{
	var row = $('#template_file_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！',"请选择一个模板！",'info');
		return false;
	}
	doCloseSelectedXlsTemplateWindow();
	if($("#downloadfile-submit-form-DownloadType").val() == "1" || $("#downloadfile-submit-form-DownloadType").val() == 1){	//编辑原始记录
		$('#weboffice-submit-form-TemplateFileId').val(row._id);
		$('#weboffice-submit-form-FileName').val(row.filename);
		$('#weboffice-submit-form-Version').val("-1");
		doOpenEditXlsWebOfficeWindow();
	}else{	//下载原始记录
		$('#downloadfile-submit-form-TemplateFileId').val(row._id);
		$('#downloadfile-submit-form').submit();
	}
}
function doCloseSelectedXlsTemplateWindow()	//关闭选择模板文件的窗口
{
	$('#select_xls_template_window').window('close');
}
function doOpenEditXlsWebOfficeWindow()	//打开编辑Weboffice窗口
{
	var win = window.open("","DoEditXlsWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//窗口不存在
		$('#weboffice-submit-form').submit();
		return true;
	}else{
		$.messager.alert('提示！',"不能同时打开多个原始记录编辑窗口，如需切换请先关闭当前打开的原始记录编辑窗口！",'info', function(){win.focus();});
		return false;
	}
}
function doUploadExcel(type)	//脱机文件上传, type：0：暂存;1：提交
{
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一条原始记录！','info');
		return false;
	}
	if(row.CertificatePdf != ""){
		var result = confirm("该原始记录已经生成证书，您确定要重新上传吗？");
		if(result == false){
			return false;
		}
	}
	if(type == 1 || type == '1'){	//提交
		//设置 scriptData 的参数  
		$('#file_upload').uploadifySettings('scriptData',{'method':'4','OriginalRecordId':row.OriginalRecordId,'Version':row.ExcelVersion});
	}else{	//暂存
		//设置 scriptData 的参数  
		$('#file_upload').uploadifySettings('scriptData',{'method':'5','OriginalRecordId':row.OriginalRecordId,'Version':row.ExcelVersion});
	}
	doUploadByUploadify('filesetname', 'file_upload', true);	//上传文件:此处的filesetname实际上没有用到
}

/***
*编辑证书相关的函数：doMakeCertificateDoc
*doOpenSelecteDocTemplateWindow
*doSelecteDocTemplateWithoutWebOffice
*doCloseSelectedDocTemplateWindow
***/
function doOpenEditDocWebOfficeWindow()	//打开编辑证书的Weboffice窗口
{
	var win = window.open("","DoEditDocWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//窗口不存在
		$('#makecertificate-submit-form').submit();
		return true;
	}else{
		$.messager.alert('提示！',"不能同时打开多个证书编辑窗口，如需切换请先关闭当前打开的证书编辑窗口！",'info', function(){win.focus();});
		return false;
	}
}
function doOpenSelecteDocTemplateWindow()	//打开选择证书模板文件的窗口
{
	$("#select_doc_template_window").window('open');
}
function doMakeCertificateDoc()	//直接编辑证书
{
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一条原始记录！','info');
		return false;
	}
	if(row.ExcelDoc != ""){
		$.messager.alert('提示！','该记录已上传原始记录Excel文件，不能直接编制证书！','info');
		return false;
	}
	
	$('#makecertificate-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	if(row.CertificateId != "" && row.CertificateDoc != ""){
		$('#makecertificate-submit-form-Version').val(row.CertificateVersion);
	}else{
		$('#makecertificate-submit-form-Version').val("-1");
	}
	$('#makecertificate-submit-form-FileName').val(row.CertificateFileName);
	$('#makecertificate-submit-form-StaffId').val(row.StaffId);
	$('#makecertificate-submit-form-XlsTemplateFileId').val('');
	
	doOpenEditDocWebOfficeWindow();  //打开编辑证书的窗口
}
function doSelecteDocTemplate()	//选择证书模板文件
{
	var row = $('#template_doc_file_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！',"请选择一个模板文件！",'info');
		return false;
	}
	//编辑证书
	$('#makecertificate-submit-form-TemplateFileId').val(row._id);
	$('#makecertificate-submit-form-FileName').val(row.filename);
	$('#makecertificate-submit-form-Version').val("-1");

	var tempResult = doOpenEditDocWebOfficeWindow();
	if(tempResult){		//成功打开编辑证书的窗口
		doCloseSelectedDocTemplateWindow();
		try{
			$("#add_original_record_window").window("close");
		}catch(e){}
	}
	
	
/*	$('#makecertificate-submit-form-TemplateFileId').val(row._id);
	$('#makecertificate-submit-form').form('submit', {
		url:'/jlyw/FileUploadServlet.do?method=8',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
				ShowWaitingDlg("正在生成证书，请稍候...");
		},
		success:function(data){
			CloseWaitingDlg();
			var result = eval("("+data+")");
			if(result.IsOK){
				$('#OriginalRecord').datagrid('reload');
				$.messager.alert('提示！','证书生成完成！','info');
				doCloseSelectedDocTemplateWindow();
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});*/	
}
function doCloseSelectedDocTemplateWindow()	//关闭选择证书模板文件的窗口
{
	$('#select_doc_template_window').window('close');
}


/**
* openXlsWin:增加原始记录时点击确定按钮,如果参数为true（回调自己的时候传递该参数值），则不请求服务器
* bEditXls:为true时表示编辑原始记录，为false时表示直接编制证书
**/
function doAddOriginalRecord(openXlsWin, bEditXls)
{
	
	if($('#AddOriginalForm-FirstIsUnqualified').val()=="合格"){//首检合格
		if($('#AddOriginalForm-UnqualifiedReason').val()!=""||$('#AddOriginalForm-RepairLevel').val()!=""||$("#AddOriginalForm input[name='MaterialDetail']:first").val()!=""){
			$.messager.alert('提示！',"首检合格时，不合格原因、修理级别和配件明细均不能输入任何信息！",'info');
			return false;
		}
		
		if(!($("#AddOriginalForm input[name='MaterialFee']:first").val()==null||$("#AddOriginalForm input[name='MaterialFee']:first").val().length==0||getInt($("#AddOriginalForm input[name='MaterialFee']:first").val())==0)){
			$.messager.alert('提示！',"首检合格时，配件费应为0或者不填！",'info');
			return false;
		}
		
	}else{//首检不合格
		if($('#AddOriginalForm-UnqualifiedReason').val()==""||$('#AddOriginalForm-UnqualifiedReason').val()==null){
			$.messager.alert('提示！',"首检不合格时，不合格原因必填！",'info');
			return false;
		}
	}
	
	if($("#AddOriginalForm-OriginalRecordId").val() != ""){	//已添加原始记录，打开编辑窗口即可	
		if(bEditXls){
			var row = $('#template_file_grid_new').datagrid('getSelected');
			if(row == null){	//未选择原始记录模板文件
				return true;
			}
			$('#weboffice-submit-form-OriginalRecordId').val($("#AddOriginalForm-OriginalRecordId").val());
			$('#weboffice-submit-form-StaffId').val($("#AddOriginalForm-WorkStaffId").val());
			$('#weboffice-submit-form-TemplateFileId').val(row._id);
			$('#weboffice-submit-form-FileName').val(row.filename);
			$('#weboffice-submit-form-Version').val("-1");
			
			$('#weboffice-submit-form-VerifierName').val($('#AddOriginalForm-VerifyUser').combobox('getValue'));
			var tempResult = doOpenEditXlsWebOfficeWindow();
			if(tempResult){		//成功打开编辑原始记录的窗口
				$("#add_original_record_window").window("close");
			}
			return true;
		}else{	//直接编制证书
			var row = $('#template_file_grid_new').datagrid('getSelected');
			if(row == null){	//未选择原始记录模板文件
				$('#makecertificate-submit-form-XlsTemplateFileId').val('');
			}else{
				$('#makecertificate-submit-form-XlsTemplateFileId').val(row._id);
			}
			$('#makecertificate-submit-form-OriginalRecordId').val($("#AddOriginalForm-OriginalRecordId").val());
			$('#makecertificate-submit-form-StaffId').val($("#AddOriginalForm-WorkStaffId").val());
			$('#makecertificate-submit-form-Version').val("-1");
			$('#makecertificate-submit-form-FileName').val("");
			var tempResult = doOpenEditDocWebOfficeWindow();
			if(tempResult){		//成功打开编辑证书的窗口
				$("#add_original_record_window").window("close");
			}
		}
		
		return true;
	}
	if(openXlsWin){
		return true;
	}
	$("#AddOriginalForm").form('submit', {
		url:'/jlyw/OriginalRecordServlet.do?method=1',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($('#TheCommissionId').val()==''){
				$.messager.alert('提示！',"委托单无效！",'info');
				return false;
			}
			return $("#AddOriginalForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$('#OriginalRecord').datagrid('reload');
				
				//记录添加的原始记录ID
				$("#AddOriginalForm-OriginalRecordId").val(result.OriginalRecordId);
				$("#AddOriginalForm-WorkStaffId").val(result.WorkStaffId);
				
				doAddOriginalRecord(true, bEditXls);	//再次调用（打开在线编辑）
			}else{
				$.messager.alert('查询失败！',result.msg,'error');
			}
		}
	});  
}
function doSearchTargetAppliance(){	//按条件查询受检器具信息
	$('#Appliance').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val(),'StandardName':encodeURI($('#function-toolbar-StandardName').combobox('getValue')),'Model':encodeURI($('#function-toolbar-Model').combobox('getValue')),'Range':encodeURI($('#function-toolbar-Range').combobox('getValue')),'Accuracy':encodeURI($('#function-toolbar-Accuracy').combobox('getValue'))};
	$('#Appliance').datagrid('reload');
}
//更新原始记录Excel的配置文件
function doUpdateExcelXml(){
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一条原始记录！','info');
		return false;
	}
	
	var result = confirm("您确定要将该原始记录Excel的配置文件替换为当前使用的版本吗？");
	if(result == false){
		return false;
	}
	$.ajax({
		type: "post",
		url: "/jlyw/OriginalRecordServlet.do?method=11",
		data: {"OriginalRecordId":row.OriginalRecordId},
		dataType: "json",	//服务器返回数据的预期类型
		beforeSend: function(XMLHttpRequest){
		},
		success: function(data, textStatus){
			if(data.IsOK){
				$.messager.alert('提示！','更新成功！','info');
			}else{
				$.messager.alert('提交失败！',data.msg,'error');
			}
		},
		complete: function(XMLHttpRequest, textStatus){
			//HideLoading();
		},
		error: function(){
			//请求出错处理
		}
	});
}


/******************         历史证书导入相关函数            ***********************/
function doOpenHistoryCertificateWindow(){		//打开历史证书信息的窗口
	var row = $('#Appliance').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一个器具标准名！','info');
		return false;
	}
	$('#load_from_history_certificate-toolbar-Model').val('');
	$('#load_from_history_certificate-toolbar-Range').val('');
	$('#load_from_history_certificate-toolbar-Accuracy').val('');
	
	$('#load_from_history_certificate').window('open');
	//加载历史证书信息
	$('#history_certificate_grid').datagrid('options').queryParams={'StandardNameId':encodeURI(row.StandardNameId),'Model':encodeURI($('#load_from_history_certificate-toolbar-Model').val()),'Range':encodeURI($('#load_from_history_certificate-toolbar-Range').val()),'Accuracy':encodeURI($('#load_from_history_certificate-toolbar-Accuracy').val()),'WorkStaff':encodeURI($("#load_from_history_certificate-toolbar-WorkStaff").combobox("getValue"))};
	$('#history_certificate_grid').datagrid('reload');
	
}
function doSearchHistoryCertificate(){
	var row = $('#Appliance').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一个器具标准名！','info');
		return false;
	}
	$('#history_certificate_grid').datagrid('options').queryParams={'StandardNameId':encodeURI(row.StandardNameId),'Model':encodeURI($('#load_from_history_certificate-toolbar-Model').val()),'Range':encodeURI($('#load_from_history_certificate-toolbar-Range').val()),'Accuracy':encodeURI($('#load_from_history_certificate-toolbar-Accuracy').val()),'WorkStaff':encodeURI($("#load_from_history_certificate-toolbar-WorkStaff").combobox("getValue"))};
	$('#history_certificate_grid').datagrid('reload');
}
function doLoadHistoryCertificate(){	//从历史证书导入数据
	var row = $('#history_certificate_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一个历史证书信息！','info');
		return false;
	}
	//加载新增原始记录的参数值
	$("#AddOriginalForm-ApplianceName").combobox('setValue', row.ApplianceName);	//常用名称
	$("#AddOriginalForm-WorkType option[value='"+row.WorkType+"']").attr("selected", true);	//工作性质
	$("#AddOriginalForm-WorkLocation option[value='"+row.WorkLocation+"']").attr("selected", true);	//工作地点
	
	$("#AddOriginalForm-Manufacturer").combobox('setValue', row.Manufacturer);	//制造单位
	$("#AddOriginalForm input[name='ApplianceCode']:first").val(row.ApplianceCode);
	$("#AddOriginalForm input[name='ApplianceManageCode']:first").val(row.ManageCode);
	$("#AddOriginalForm input[name='Temp']:first").val(row.Temp);	//环境温度
	$("#AddOriginalForm input[name='Humidity']:first").val(row.Humidity);
	$("#AddOriginalForm input[name='Pressure']:first").val(row.Pressure);
	$("#AddOriginalForm input[name='OtherCond']:first").val(row.OtherCond);	//其它
	$("#AddOriginalForm-StdOrStdAppUsageStatus").combobox('setValue', row.StdOrStdAppUsageStatus);
	$("#AddOriginalForm input[name='AbnormalDesc']:first").val(row.AbnormalDesc);
	$("#AddOriginalForm-Conclusion").combobox('setValue', row.Conclusion);//结论
	$("#AddOriginalForm-VerifyUser").combobox('setValue', row.Verifier);	//核验员
	$("#AddOriginalForm input[name='Quantity']:first").val(row.Quantity);	//器具数量
	$("#AddOriginalForm-RepairLevel option[value='"+row.RepairLevel+"']").attr("selected", true);	// 修理级别
	
	$("#AddOriginalForm input[name='MaterialDetail']:first").val(row.MaterialDetail);
	$("#AddOriginalForm input[name='CarFee']:first").val(row.CarFee);	//交通费
	$("#AddOriginalForm input[name='DebugFee']:first").val(row.DebugFee);
	$("#AddOriginalForm input[name='MaterialFee']:first").val(row.MaterialFee);
	$("#AddOriginalForm input[name='OtherFee']:first").val(row.OtherFee);//其他费
	setAddOriginalFormTestFee();	//检定费
	setAddOriginalFormRepairFee();	//修理费
	
	$("#AddOriginalForm-Mandatory option[value='"+row.Mandatory+"']").attr("selected", true);	//是否强管
	$("#AddOriginalForm input[name='MandatoryCode']:first").val(row.MandatoryCode);	//强管唯一性号
	$("#AddOriginalForm-MandatoryPurpose option[value='"+row.MandatoryPurpose+"']").attr("selected", true);	//强管类型/用途
	$("#AddOriginalForm input[name='MandatoryItemType']:first").val(row.MandatoryItemType);	//强管项目对应项别
	$("#AddOriginalForm input[name='MandatoryType']:first").val(row.MandatoryType);	//强管项目对应种别
	$("#AddOriginalForm input[name='MandatoryApplyPlace']:first").val(row.MandatoryApplyPlace);	//使用安装地点
	
	$("#AddOriginalForm-FirstIsUnqualified option[value='"+row.FirstIsUnqualified+"']").attr("selected", true);	// 首检是否合格
	$("#AddOriginalForm input[name='UnqualifiedReason']:first").val(row.UnqualifiedReason);	//受检不合格原因
	$("#AddOriginalForm input[name='Remark']:first").val(row.Remark);
	
	$("#function-toolbar-StandardName").combobox('setValue',row.ApplianceStandardName);
	$("#function-toolbar-Model").combobox('setValue',row.Model);	//型号规格
	$("#function-toolbar-Range").combobox('setValue',row.Range);
	$("#function-toolbar-Accuracy").combobox('setValue',row.Accuracy);
	doSearchTargetAppliance();
	$('#load_from_history_certificate').window('close');
				
}
/*******       调整修理级别后更改费用          *********/
//设置AddOriginalForm的检定费
function setAddOriginalFormTestFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='TestFee']:first").val('');//检定费
			return false;
		}else{
			$("#AddOriginalForm input[name='TestFee']:first").val(row.Fee);//检定费
		}
	}catch(ex){}
}
//设置AddOriginalForm的修理费
function setAddOriginalFormRepairFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//修理费
			return false;
		}
		var level = $("#AddOriginalForm-RepairLevel").val();
		if(level == "小"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.SRFee);//修理费
		}else if(level == "中"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.MRFee);//修理费
		}else if(level == "大"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.LRFee);//修理费
		}else{
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//修理费
		}
	}catch(ex){}
}
//下面的方法是2012-11-30之后加的
function selectTechnicalDocsAndStandards(){//打开选择规程标准的窗口
	$('#selectTechnicalDocsAndStandards_window').window('open');
}
function DeleteStdAppRecord(){//删除标准器具
	var row = $('#table_StandardAppliance').datagrid('getSelected');
	if(row == null){
		$.messager.alert("提示","请选择标准器具！","info");
	}else{
		var index = $('#table_StandardAppliance').datagrid('getRowIndex', row);
		$('#table_StandardAppliance').datagrid('deleteRow', index);
	}	
}
function DeleteStdRecord(){//删除计量标准
	var row = $('#table_Standard').datagrid('getSelected');
	if(row == null){
		$.messager.alert("提示","请选择计量标准！","info");
	}else{
		var index = $('#table_Standard').datagrid('getRowIndex', row);
		$('#table_Standard').datagrid('deleteRow', index);
	}	
}
function DeleteSpecificationRecord(){//删除技术规范
	var row = $('#table_Specification').datagrid('getSelected');
	if(row == null){
		$.messager.alert("提示","请选择技术规范！","info");
	}else{
		var index = $('#table_Specification').datagrid('getRowIndex', row);
		$('#table_Specification').datagrid('deleteRow', index);
	}	
}
function submitDocsAndStandards(){//提交选择的技术规范
	var rows1 = $('#table_StandardAppliance').datagrid('getRows');
	var rows2 = $('#table_Standard').datagrid('getRows');
	var rows3 = $('#table_Specification').datagrid('getRows');
	if((rows1==null)&&(rows2==null)&&(rows3==null)){
		$.messager.alert("提示","请选择技术规范、计量标准和标准器具中的一项或者多项！","info");
	}else{
		$('#AddOriginalForm-StandardAppliancesString').val(JSON.stringify(rows1));
		$('#AddOriginalForm-StandardsString').val(JSON.stringify(rows2));
		$('#AddOriginalForm-SpecificationsString').val(JSON.stringify(rows3));
		$('#selectTechnicalDocsAndStandards_window').window('close');
	}
}

