$(function(){
	$('#file_upload').uploadify({
		'script'    : '/jlyw/FileUploadServlet.do',
		'scriptData':{'method':'4','FileType':'101'},	//method������������Ȼ���������Ĳ������ţ����³���
		'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
		'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
		'buttonImg' : '../uploadify/selectfiles.png',
		'fileDesc'  : '֧�ָ�ʽ:xls', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
		'fileExt'   : '*.xls;',   //����ĸ�ʽ
		onComplete: function (event,ID,fileObj,response,data) {  
			var retData = eval("("+response+")");
			if(retData.IsOK == false){
				$.messager.alert('��ʾ',retData.msg,'error');
			}else{
				if(retData.msg!=null&&retData.msg.length>0){
					$.messager.alert('��ʾ',retData.msg,'info');
				}else{
					$.messager.alert('��ʾ',"�ļ��ϴ��ɹ���",'info');
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
					$.messager.alert('��ʾ��',"�����ظ���ӣ�",'info');
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
		title:'�����淶��Ϣ��ѯ',
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
			{field:'specificationcode',title:'�����淶���',width:100,align:'center'},
			{field:'namecn',title:'�����淶����(����)',width:120,align:'center'},
			{field:'type',title:'���',width:80,align:'center'},
			{field:'localecode',title:'���ڱ��',width:80,align:'center'},
			{field:'issuedate',title:'��������',width:80,align:'center'},
			{field:'effectivedate',title:'ʵʩ����',width:80,ealign:'center'},
			{field:'repealdate',title:'��ֹ����',width:80,align:'center'}
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
					$.messager.alert('��ʾ��',"�����ظ���ӣ�",'info');
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
		title:'������׼��Ϣ��ѯ',
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
			{field:'slocalecode',title:'���ڱ��',width:80,align:'center'},
			{field:'certificatecode',title:'������׼֤���',width:100,align:'center'},
			{field:'name',title:'������׼����',width:80,align:'center'},				
			{field:'standardcode',title:'������׼����',width:80,align:'center'},
			{field:'projectcode',title:'��Ŀ���',width:80,align:'center'},
			{field:'issuedby',title:'��֤��λ',width:120,align:'center'},
			{field:'issuedate',title:'��֤����',width:100,align:'center'},
			{field:'validdate',title:'��Ч��',width:100,align:'center'},
			{field:'range',title:'������Χ',width:80,align:'center'},
			{field:'uncertain',title:'��ȷ�������',width:120,align:'center'},
			{field:'warnslot',title:'��Ч��Ԥ������',width:120,align:'center',
				formatter:function(value,rowData,rowIndex){
					if(value==null)
						return "";
					return value+"��";
				}},
			{field:'handler',title:'��Ŀ������',width:80,align:'center'},
			{field:'projecttype',title:'��Ŀ����',width:120,align:'center'}
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
					$.messager.alert('��ʾ��',"�����ظ���ӣ�",'info');
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
	$('#table_StandardAppliance').datagrid({  //��׼������Ϣ
		title:'��׼����',
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
			{field:'name',title:'��������',width:100,align:'center'},
			{field:'model',title:'�ͺŹ��',width:100,align:'center'},
			{field:'range',title:'������Χ',width:80,ealign:'center'},
			{field:'uncertain',title:'��ȷ����',width:50,align:'center'},
			{field:'release',title:'������Ϣ',width:120,align:'center'},
			{field:'permanentcode',title:'�̶��ʲ����',width:120,align:'center'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:"#table_StandardAppliance_toolbar"
	});
	
	var OriginalRecordLastSelectedId = null;	//���һ����ѡ��ļ�¼Id
	$('#OriginalRecord').datagrid({
		title:'ԭʼ��¼��Ϣ',
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
				{field:'CertificateCode',title:'֤����',width:120,rowspan:2,align:'center'},
				{title:'������Ϣ',colspan:8,align:'center'},
				{title:'������Ϣ',colspan:7,align:'center'},
				{title:'������Ϣ',colspan:7,align:'center'}
			],[
				{field:'ApplianceStandardName',title:'���߱�׼����',width:80,sortable:true,align:'center'},
				{field:'Model',title:'����ͺ�',width:60,align:'center'},
				{field:'Range',title:'������Χ',width:80,align:'center'},
				{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:80,align:'center'},
				{field:'Manufacturer',title:'���쳧',width:80,align:'center'},
				{field:'ApplianceCode',title:'�������',width:80,align:'center'},
				{field:'ManageCode',title:'������',width:80,align:'center'},
				{field:'Quantity',title:'��������',width:60,align:'center'},
				
				{field:'ProjectName',title:'������Ŀ����',width:80,align:'center'},
				{field:'Staff',title:'��/У��Ա',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(rowData.StaffChecked){
							return value;
						}else{
							return "<span style='color: #FF0000' title='��δ�˶�'>"+value+"</span>";
						}
					}
				},
				{field:'WorkDate',title:'��/У����',width:80,align:'center'},				
				{field:'ExcelId',title:'ԭʼ��¼Excel',width:80,align:'center', 
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.ExcelDoc == ""){
							return "";
						}else{
							if(rowData.ExcelPdf == ""){
								return "<span style='color: #FF0000'>δ���</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>�����</span></a>"
							}
						}
					}},
				{field:'CertificateId',title:'֤���ļ�',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.CertificateDoc==""){
							return "";
						}else{
							if(rowData.CertificatePdf == ""){
								return "<span style='color: #FF0000'>δ���</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='������ظ�֤��' ><span style='color: #0033FF'>�����</span></a>"
							}
						}
					}
				},
				{field:'VerifierName',title:'������',width:80,align:'center',
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
				{field:'AuthorizerName',title:'��׼��',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
							return "";
						}else{
							if(rowData.AuthorizeResult == ""){		//��δ����
								return "<span title='��δǩ��'>"+value+"</span>";
							}
							else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //��׼ͨ��
								if(rowData.IsAuthBgRuning==true){
									return "<span style='color: #009933' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����ͨ��(��̨ǩ��ִ����...)\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{
									return "<span style='color: #0033FF' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����ͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
							}else{	//��׼δͨ��
								return "<span style='color: #FF0000' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����δͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
							}
						}
					}
				},
				
				{field:'TestFee',title:'����',width:60,align:'center'},
				{field:'RepairFee',title:'�����',width:60,align:'center'},
				{field:'MaterialFee',title:'���Ϸ�',width:60,align:'center'},
				{field:'CarFee',title:'��ͨ��',width:60,align:'center'},
				{field:'DebugFee',title:'���Է�',width:60,align:'center'},
				{field:'OtherFee',title:'��������',width:60,align:'center'},
				{field:'TotalFee',title:'�ܼƷ���',width:60,align:'center'}
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
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#OriginalRecord').datagrid('reload');
			}
		},'-',{
			text:'ע��',
			iconCls:'icon-cancel',
			handler:function(){
				var rows = $("#OriginalRecord").datagrid("getSelections");
				if(rows.length == 0){
					$.messager.alert('��ʾ',"��ѡ��Ҫע����ԭʼ��¼��",'info');
					return false;
				}
				if(rows.length > 1){
					$.messager.alert('��ʾ',"һ�����ֻ��ע��һ��ԭʼ��¼��",'info');
					return false;
				}
				
				var result = confirm("�ò������ɳ�������ȷ��Ҫע����ԭʼ��¼��");
				if(result == false){
					return false;
				}
				$.ajax({
						type: "post",
						url: "/jlyw/OriginalRecordServlet.do?method=2",
						data: {"OriginalRecordId":rows[0].OriginalRecordId},
						dataType: "json",	//�������������ݵ�Ԥ������
						beforeSend: function(XMLHttpRequest){
						},
						success: function(data, textStatus){
							if(data.IsOK){
								$("#OriginalRecord").datagrid("unselectAll");
								$("#OriginalRecord").datagrid("reload");
								
							}else{
								$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
							}
						},
						complete: function(XMLHttpRequest, textStatus){
							//HideLoading();
						},
						error: function(){
							//���������
						}
				});
			}
		},'-',{
			text:'����Excel�����ļ�',
			iconCls:'icon-download',
			handler:function(){
				doUpdateExcelXml();
			}
		},'-',{
			text:'�༭ԭʼ��¼',
			iconCls:'icon-edit',
			handler:function(){
				doLoadOrEditOriginalRecord(1);
			}
		},'-',{
			text:'����ԭʼ��¼',
			iconCls:'icon-download',
			handler:function(){
				doLoadOrEditOriginalRecord(0);
			}
		},'-',{
			text:'�ѻ��ļ��ϴ�',
			iconCls:'icon-upload',
			handler:function(){
				var row = $("#OriginalRecord").datagrid("getSelected");
				if(row == null){
					$.messager.alert('��ʾ',"��ѡ��һ��ԭʼ��¼��",'info');
					return false;
				}
				$('#file_upload_window').window('open');
			}
		},'-',{
			text:'����',
			iconCls:'icon-undo',
			handler:function(){
				javascript:history.go(-1);
			}
		},'-',{
			text:'��ת�������˺Ͳ�ֵ����ҳ��',
			iconCls:'icon-redo',
			handler:function(){
			
				$('#CheckAndFeeForm').submit();	
				
			}
		}]
	});
	$('#Appliance').datagrid({
		title:'ѡ���ܼ�����',
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
				{field:'StandardNameName',title:'��׼����',width:80,sortable:true,align:'center'},
				{field:'TargetApplianceName',title:'�ܼ���������',width:80,align:'center'},
				{field:'Model',title:'�ͺŹ��',width:65,align:'center'},
				{field:'Range',title:'������Χ',width:80,align:'center'},
				{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:65,align:'center'},
				{field:'Fee',title:'�춨��',width:60,align:'center'},
				{field:'SRFee',title:'С�޷���',width:60,align:'center'},
				{field:'MRFee',title:'���޷���',width:60,align:'center'},
				{field:'LRFee',title:'���޷���',width:60,align:'center'},
				{field:'TargetApplianceRemark',title:'��ע',width:80,align:'center'}
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
			//��ֵ�����ֶ�
			$("#AddOriginalForm input[name='Model']:first").val(rowData.Model);
			$("#AddOriginalForm input[name='Range']:first").val(rowData.Range);
			$("#AddOriginalForm input[name='Accuracy']:first").val(rowData.Accuracy);
			$("#AddOriginalForm input[name='TargetApplianceId']:first").val(rowData.TargetApplianceId);
			setAddOriginalFormTestFee();	//�춨��
			setAddOriginalFormRepairFee();	//�����
			
			//ģ���ļ�ˢ��
			$('#template_file_grid_new').datagrid('options').queryParams={'FilesetName':rowData.StandardNameFilesetName};
			$('#template_file_grid_new').datagrid('reload');
			
			//��������ˢ��
			$("#AddOriginalForm-ApplianceName").combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=5&StandardNameId='+rowData.StandardNameId);
			
			//���쳧ˢ��
			$("#AddOriginalForm-Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);	
			
			//����Աˢ��
			$("#AddOriginalForm-VerifyUser").combobox('reload','/jlyw/TaskAssignServlet.do?method=7&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);
		},
		toolbar:"#function-toolbar"
	});
   
   
   $('#template_file_grid').datagrid({
		title:'ѡ��ԭʼ��¼ģ��',
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
				{field:'filename',title:'�ļ���',width:150,sortable:true,align:'center'},
				{field:'length',title:'��С',width:50,align:'center'},
				{field:'uploadDate',title:'�ϴ�����',width:80,align:'center'},
				{field:'uploadername',title:'�ϴ���',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}]
	});
   $('#template_file_grid_new').datagrid({
		title:'ѡ��ԭʼ��¼ģ��',
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
				{field:'filename',title:'�ļ���',width:100,sortable:true,align:'center'},
				{field:'length',title:'��С',width:50,align:'center'},
				{field:'uploadDate',title:'�ϴ�����',width:80,align:'center'},
				{field:'uploadername',title:'�ϴ���',width:60,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid_new').datagrid('reload');
			}	
		}]
	});
	
	$('#template_doc_file_grid').datagrid({
		title:'֤��ģ��',
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
				{field:'filename',title:'�ļ���',width:150,sortable:true,align:'center'},
				{field:'length',title:'��С',width:50,align:'center'},
				{field:'uploadDate',title:'�ϴ�����',width:80,align:'center'},
				{field:'uploadername',title:'�ϴ���',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		toolbar:[{
			text:'ˢ��',
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
			//Ĭ��ѡ�е�һ��
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
		title:'��ֵ����',
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
				{field:'FeeAlloteeName',title:'����',width:70,sortable:true,align:'center'},
				{field:'TestFee',title:'�춨��',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'RepairFee',title:'�����',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'MaterialFee',title:'���Ϸ�',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'CarFee',title:'��ͨ��',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'DebugFee',title:'���Է�',width:50,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:0,
						min:0.0
					}
				}},
				{field:'OtherFee',title:'������',width:50,align:'center',editor:{
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
						FeeAlloteeName:$("#fee_info_temp_form input[name='Staff']:first").val(),//ѡ��fee_info_temp_form�µ�һ����ΪStaff��input��ֵ
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
			text:'ɾ��',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("��ȷ��Ҫɾ����ѡ�Ĳ�ֵ�����¼��");
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
		title:'��ֵ����',
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
				{field:'FeeAlloteeName',title:'����',width:70,sortable:true,align:'center'},
				{field:'TestFee',title:'�춨�Ѷ��',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'RepairFee',title:'����Ѷ��',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'MaterialFee',title:'���ϷѶ��',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'CarFee',title:'��ͨ�Ѷ��',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'DebugFee',title:'���ԷѶ��',width:60,align:'center',editor:{
					type:'numberbox',
					options:{
						precision:2,
						min:0.00,
						max:1.00
					}
				}},
				{field:'OtherFee',title:'�����Ѷ��',width:60,align:'center',editor:{
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
			text:'ɾ��',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("��ȷ��Ҫɾ����ѡ�Ĳ�ֵ�����¼��");
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
	
	//���߱�׼��
	$("#function-toolbar-StandardName").combobox({
		valueField:'standardname',
		textField:'standardname',
		//required:true,
		onSelect:function(record){
			doSearchTargetAppliance();
			$("#standardNameIdstandardNameId").val(record.id);
			$("#function-toolbar-Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType=0&ApplianceSpeciesId='+record.id);	//�ͺŹ��
			$("#function-toolbar-Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType=0&ApplianceSpeciesId='+record.id);	//������Χ
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
				$.messager.alert('��ʾ��',"���߱�׼������Ϊ�գ�",'info');
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
				$.messager.alert('��ʾ��',"���߱�׼������Ϊ�գ�",'info');
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
				$.messager.alert('��ʾ��',"���߱�׼������Ϊ�գ�",'info');
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
		title:'��ʷ֤����Ϣ',
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
				{field:'CertificateCode',title:'֤����',width:60,align:'center'},
				{field:'CustomerName',title:'ί�е�λ',width:60,align:'center'},				
				{field:'ApplianceStandardName',title:'���߱�׼��',width:80,sortable:true,align:'center'},
				{field:'ApplianceName',title:'��������',width:60,align:'center'},
				{field:'Model',title:'�ͺŹ��',width:60,align:'center'},
				{field:'Range',title:'������Χ',width:60,align:'center'},
				{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:60,align:'center'},
				
				{field:'Staff',title:'��У��Ա',width:60,align:'center'},
				{field:'WorkDate',title:'��У����',width:60,align:'center'},
				{field:'Verifier',title:'������',width:60,align:'center'},
				{field:'WorkType',title:'��������',width:60,align:'center'},
				{field:'WorkLocation',title:'�����ص�',width:60,align:'center'},
				{field:'Manufacturer',title:'���쵥λ',width:60,align:'center'},
				{field:'ApplianceCode',title:'�������',width:60,align:'center'},
				{field:'ManageCode',title:'������',width:60,align:'center'},
				{field:'Temp',title:'�����¶�',width:60,align:'center'},
				{field:'Humidity',title:'���ʪ��',width:60,align:'center'},
				{field:'Pressure',title:'����ѹ',width:60,align:'center'},
				{field:'OtherCond',title:'����',width:60,align:'center'},
				{field:'StdOrStdAppUsageStatus',title:'��׼(����)״̬���',width:60,align:'center'},
				{field:'AbnormalDesc',title:'�쳣���˵��',width:60,align:'center'},
				{field:'Conclusion',title:'����',width:60,align:'center'},
				{field:'Quantity',title:'��������',width:60,align:'center'},
				{field:'RepairLevel',title:'������',width:60,align:'center'},
				{field:'MaterialDetail',title:'�����ϸ',width:60,align:'center'},
				{field:'TestFee',title:'�춨��',width:60,align:'center'},
				{field:'RepairFee',title:'�����',width:60,align:'center'},
				{field:'CarFee',title:'��ͨ��',width:60,align:'center'},
				{field:'DebugFee',title:'���Է�',width:60,align:'center'},
				{field:'MaterialFee',title:'�����',width:60,align:'center'},
				{field:'OtherFee',title:'������',width:60,align:'center'},
				{field:'FirstIsUnqualified',title:'�׼��Ƿ�ϸ�',width:60,align:'center'},
				{field:'Remark',title:'��ע',width:60,align:'center'},
				
				{field:'Mandatory',title:'�Ƿ�ǿ��',width:60,align:'center'},
				{field:'MandatoryCode',title:'ǿ��Ψһ���',width:60,align:'center'},
				{field:'MandatoryPurpose',title:'ǿ������',width:60,align:'center'},
				{field:'MandatoryItemType',title:'���',width:60,align:'center'},
				{field:'MandatoryType',title:'�ֱ�',width:60,align:'center'},
				{field:'MandatoryApplyPlace',title:'ʹ�ð�װ�ص�',width:60,align:'center'}
				
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
	
	//��У����Ĭ�ϵ���
	var nowDate = new Date();
	$("#AddOriginalForm-WorkDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	
	//$('#loading').hide();

});
function doLoadCommissionSheet(fromByCode){	//����ί�е�
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
			$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
			
			if(fromByCode){	//��ComSheetInspectByCode.jspҳ�����
				$('#AddOriginalForm-TaskId').combobox('loadData',[]);
			}
			
			if($('#Code').val()=='' || $('#Pwd').val() == ''){
				$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
				return false;
			}
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//��ѡ
				}
				
				//����ԭʼ��¼��Ϣ
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#OriginalRecord').datagrid('reload');
				
				//�����ܼ�������Ϣ
				$('#Appliance').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#Appliance').datagrid('reload');
				
				//hidden�ֶ�
				$('#TheCommissionId').val($('#CommissionId').val());
				
				
				if(fromByCode){	//��ComSheetInspectByCode.jspҳ�����
					//���ؼ�����Ŀ��Ϣ
					$('#AddOriginalForm-TaskId').combobox('reload', '/jlyw/TaskAssignServlet.do?method=5&CommissionId='+$('#CommissionId').val());
				}
				
				//��������ԭʼ��¼�Ĳ���ֵ
				$("#AddOriginalForm").form('load',result.CommissionObj);
				if(result.CommissionObj.ReportType == 1 || result.CommissionObj.ReportType == '1'){
					$("#AddOriginalForm-WorkType option[value='�춨']").attr("selected", true);
					//$("#AddOriginalForm-Conclusion").combobox("setValue", "�ϸ�");
				}else if(result.CommissionObj.ReportType == 2 || result.CommissionObj.ReportType == '2'){
					$("#AddOriginalForm-WorkType option[value='У׼']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 3 || result.CommissionObj.ReportType == '3'){
					$("#AddOriginalForm-WorkType option[value='���']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 4 || result.CommissionObj.ReportType == '4'){
					$("#AddOriginalForm-WorkType option[value='����']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkType option[value='']").attr("selected", true);
				}
				
			
				if(result.CommissionObj.CommissionType == 2 || result.CommissionObj.CommissionType == '2'){
					$("#AddOriginalForm-WorkLocation option[value='��������ʹ���ֳ�']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkLocation option[value='����ʵ����']").attr("selected", true);
				}
				
				if(result.CommissionObj.Mandatory == 1 || result.CommissionObj.Mandatory == '1'){
					$("#AddOriginalForm-Mandatory option[value='��']").attr("selected", true);
				}else{
					$("#AddOriginalForm-Mandatory option[value='��']").attr("selected", true);
				}
				
				$("#AddOriginalForm input[name='Quantity']:first").val("1");
				
				//$("#AddOriginalForm-FirstIsUnqualified").combobox("select", "�ϸ�");
									
			}else{
				$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}


function doLoadOrEditOriginalRecord(type)  //���ػ�༭ԭʼ��¼:typeΪ0ʱ����ԭʼ��¼��1ʱ��weboffice�༭ԭʼ��¼
{
	if(type == 1 || type =="1"){	//�༭ԭʼ��¼
		$('#downloadfile-submit-form-DownloadType').val("1");
	}else{
		$('#downloadfile-submit-form-DownloadType').val("0");
	}
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		if(type==1 || type=="1"){
			$("#AddOriginalForm-OriginalRecordId").val('');
			$("#AddOriginalForm-WorkStaffId").val('');
			$("#AddOriginalForm input[name='Quantity']:first").val('1');	//������������
			
			$("#AddOriginalForm-StandardAppliancesString").val('');//���޹�����׼���ߡ��淶�ȵ�����£�ѡ��ı�׼����
			$("#AddOriginalForm-StandardsString").val('');//���޹�����׼���ߡ��淶�ȵ�����£�ѡ��ļ�����׼
			$("#AddOriginalForm-SpecificationsString").val('');//���޹�����׼���ߡ��淶�ȵ�����£�ѡ��ļ����淶
			
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
			$.messager.alert('��ʾ��','����ѡ��һ��ԭʼ��¼��','info');
			return false;
		}
		
	}
	$('#weboffice-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	$('#weboffice-submit-form-StaffId').val(row.StaffId);
	$('#downloadfile-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	if(row.ExcelId != "" && row.ExcelDoc != ""){		//����ԭʼ��¼
		if(type == 1 || type =="1"){	//�༭ԭʼ��¼
			$('#weboffice-submit-form-TemplateFileId').val('');
			$('#weboffice-submit-form-FileName').val(row.ExcelFileName);
			$('#weboffice-submit-form-Version').val(row.ExcelVersion);
			doOpenEditXlsWebOfficeWindow();
		}else{	//����ԭʼ��¼
			$('#downloadfile-submit-form-TemplateFileId').val("");
			$('#downloadfile-submit-form').submit();
		}
	}else{	//û��ԭʼ��¼
		if(row.CertificateDoc == ""){	//ѡ��ԭʼ��¼ģ��
			doOpenSelecteTemplateWindow(row.TemplateFilesetName);
		}else{	//ֱ�ӱ༭֤��
			$('#makecertificate-submit-form-XlsTemplateFileId').val('');
			$('#makecertificate-submit-form-OriginalRecordId').val(row.OriginalRecordId);
			$('#makecertificate-submit-form-FileName').val(row.CertificateFileName);
			$('#makecertificate-submit-form-Version').val(row.CertificateVersion);
			$('#makecertificate-submit-form-StaffId').val(row.StaffId);
			doOpenEditDocWebOfficeWindow();
		}
		
	}
}
function doOpenSelecteTemplateWindow(filesetname)	//��ѡ��ԭʼ��¼ģ���ļ��Ĵ���
{
	$("#select_xls_template_window").window('open');
	$('#template_file_grid').datagrid('options').queryParams={'FilesetName':filesetname};
	$('#template_file_grid').datagrid('reload');
}
function doSelecteXlsTemplate()	//ѡ��ԭʼ��¼ģ���ļ�
{
	var row = $('#template_file_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��',"��ѡ��һ��ģ�壡",'info');
		return false;
	}
	doCloseSelectedXlsTemplateWindow();
	if($("#downloadfile-submit-form-DownloadType").val() == "1" || $("#downloadfile-submit-form-DownloadType").val() == 1){	//�༭ԭʼ��¼
		$('#weboffice-submit-form-TemplateFileId').val(row._id);
		$('#weboffice-submit-form-FileName').val(row.filename);
		$('#weboffice-submit-form-Version').val("-1");
		doOpenEditXlsWebOfficeWindow();
	}else{	//����ԭʼ��¼
		$('#downloadfile-submit-form-TemplateFileId').val(row._id);
		$('#downloadfile-submit-form').submit();
	}
}
function doCloseSelectedXlsTemplateWindow()	//�ر�ѡ��ģ���ļ��Ĵ���
{
	$('#select_xls_template_window').window('close');
}
function doOpenEditXlsWebOfficeWindow()	//�򿪱༭Weboffice����
{
	var win = window.open("","DoEditXlsWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//���ڲ�����
		$('#weboffice-submit-form').submit();
		return true;
	}else{
		$.messager.alert('��ʾ��',"����ͬʱ�򿪶��ԭʼ��¼�༭���ڣ������л����ȹرյ�ǰ�򿪵�ԭʼ��¼�༭���ڣ�",'info', function(){win.focus();});
		return false;
	}
}
function doUploadExcel(type)	//�ѻ��ļ��ϴ�, type��0���ݴ�;1���ύ
{
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ��ԭʼ��¼��','info');
		return false;
	}
	if(row.CertificatePdf != ""){
		var result = confirm("��ԭʼ��¼�Ѿ�����֤�飬��ȷ��Ҫ�����ϴ���");
		if(result == false){
			return false;
		}
	}
	if(type == 1 || type == '1'){	//�ύ
		//���� scriptData �Ĳ���  
		$('#file_upload').uploadifySettings('scriptData',{'method':'4','OriginalRecordId':row.OriginalRecordId,'Version':row.ExcelVersion});
	}else{	//�ݴ�
		//���� scriptData �Ĳ���  
		$('#file_upload').uploadifySettings('scriptData',{'method':'5','OriginalRecordId':row.OriginalRecordId,'Version':row.ExcelVersion});
	}
	doUploadByUploadify('filesetname', 'file_upload', true);	//�ϴ��ļ�:�˴���filesetnameʵ����û���õ�
}

/***
*�༭֤����صĺ�����doMakeCertificateDoc
*doOpenSelecteDocTemplateWindow
*doSelecteDocTemplateWithoutWebOffice
*doCloseSelectedDocTemplateWindow
***/
function doOpenEditDocWebOfficeWindow()	//�򿪱༭֤���Weboffice����
{
	var win = window.open("","DoEditDocWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//���ڲ�����
		$('#makecertificate-submit-form').submit();
		return true;
	}else{
		$.messager.alert('��ʾ��',"����ͬʱ�򿪶��֤��༭���ڣ������л����ȹرյ�ǰ�򿪵�֤��༭���ڣ�",'info', function(){win.focus();});
		return false;
	}
}
function doOpenSelecteDocTemplateWindow()	//��ѡ��֤��ģ���ļ��Ĵ���
{
	$("#select_doc_template_window").window('open');
}
function doMakeCertificateDoc()	//ֱ�ӱ༭֤��
{
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ��ԭʼ��¼��','info');
		return false;
	}
	if(row.ExcelDoc != ""){
		$.messager.alert('��ʾ��','�ü�¼���ϴ�ԭʼ��¼Excel�ļ�������ֱ�ӱ���֤�飡','info');
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
	
	doOpenEditDocWebOfficeWindow();  //�򿪱༭֤��Ĵ���
}
function doSelecteDocTemplate()	//ѡ��֤��ģ���ļ�
{
	var row = $('#template_doc_file_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��',"��ѡ��һ��ģ���ļ���",'info');
		return false;
	}
	//�༭֤��
	$('#makecertificate-submit-form-TemplateFileId').val(row._id);
	$('#makecertificate-submit-form-FileName').val(row.filename);
	$('#makecertificate-submit-form-Version').val("-1");

	var tempResult = doOpenEditDocWebOfficeWindow();
	if(tempResult){		//�ɹ��򿪱༭֤��Ĵ���
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
				ShowWaitingDlg("��������֤�飬���Ժ�...");
		},
		success:function(data){
			CloseWaitingDlg();
			var result = eval("("+data+")");
			if(result.IsOK){
				$('#OriginalRecord').datagrid('reload');
				$.messager.alert('��ʾ��','֤��������ɣ�','info');
				doCloseSelectedDocTemplateWindow();
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});*/	
}
function doCloseSelectedDocTemplateWindow()	//�ر�ѡ��֤��ģ���ļ��Ĵ���
{
	$('#select_doc_template_window').window('close');
}


/**
* openXlsWin:����ԭʼ��¼ʱ���ȷ����ť,�������Ϊtrue���ص��Լ���ʱ�򴫵ݸò���ֵ���������������
* bEditXls:Ϊtrueʱ��ʾ�༭ԭʼ��¼��Ϊfalseʱ��ʾֱ�ӱ���֤��
**/
function doAddOriginalRecord(openXlsWin, bEditXls)
{
	
	if($('#AddOriginalForm-FirstIsUnqualified').val()=="�ϸ�"){//�׼�ϸ�
		if($('#AddOriginalForm-UnqualifiedReason').val()!=""||$('#AddOriginalForm-RepairLevel').val()!=""||$("#AddOriginalForm input[name='MaterialDetail']:first").val()!=""){
			$.messager.alert('��ʾ��',"�׼�ϸ�ʱ�����ϸ�ԭ��������������ϸ�����������κ���Ϣ��",'info');
			return false;
		}
		
		if(!($("#AddOriginalForm input[name='MaterialFee']:first").val()==null||$("#AddOriginalForm input[name='MaterialFee']:first").val().length==0||getInt($("#AddOriginalForm input[name='MaterialFee']:first").val())==0)){
			$.messager.alert('��ʾ��',"�׼�ϸ�ʱ�������ӦΪ0���߲��",'info');
			return false;
		}
		
	}else{//�׼첻�ϸ�
		if($('#AddOriginalForm-UnqualifiedReason').val()==""||$('#AddOriginalForm-UnqualifiedReason').val()==null){
			$.messager.alert('��ʾ��',"�׼첻�ϸ�ʱ�����ϸ�ԭ����",'info');
			return false;
		}
	}
	
	if($("#AddOriginalForm-OriginalRecordId").val() != ""){	//�����ԭʼ��¼���򿪱༭���ڼ���	
		if(bEditXls){
			var row = $('#template_file_grid_new').datagrid('getSelected');
			if(row == null){	//δѡ��ԭʼ��¼ģ���ļ�
				return true;
			}
			$('#weboffice-submit-form-OriginalRecordId').val($("#AddOriginalForm-OriginalRecordId").val());
			$('#weboffice-submit-form-StaffId').val($("#AddOriginalForm-WorkStaffId").val());
			$('#weboffice-submit-form-TemplateFileId').val(row._id);
			$('#weboffice-submit-form-FileName').val(row.filename);
			$('#weboffice-submit-form-Version').val("-1");
			
			$('#weboffice-submit-form-VerifierName').val($('#AddOriginalForm-VerifyUser').combobox('getValue'));
			var tempResult = doOpenEditXlsWebOfficeWindow();
			if(tempResult){		//�ɹ��򿪱༭ԭʼ��¼�Ĵ���
				$("#add_original_record_window").window("close");
			}
			return true;
		}else{	//ֱ�ӱ���֤��
			var row = $('#template_file_grid_new').datagrid('getSelected');
			if(row == null){	//δѡ��ԭʼ��¼ģ���ļ�
				$('#makecertificate-submit-form-XlsTemplateFileId').val('');
			}else{
				$('#makecertificate-submit-form-XlsTemplateFileId').val(row._id);
			}
			$('#makecertificate-submit-form-OriginalRecordId').val($("#AddOriginalForm-OriginalRecordId").val());
			$('#makecertificate-submit-form-StaffId').val($("#AddOriginalForm-WorkStaffId").val());
			$('#makecertificate-submit-form-Version').val("-1");
			$('#makecertificate-submit-form-FileName').val("");
			var tempResult = doOpenEditDocWebOfficeWindow();
			if(tempResult){		//�ɹ��򿪱༭֤��Ĵ���
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
				$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
				return false;
			}
			return $("#AddOriginalForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$('#OriginalRecord').datagrid('reload');
				
				//��¼��ӵ�ԭʼ��¼ID
				$("#AddOriginalForm-OriginalRecordId").val(result.OriginalRecordId);
				$("#AddOriginalForm-WorkStaffId").val(result.WorkStaffId);
				
				doAddOriginalRecord(true, bEditXls);	//�ٴε��ã������߱༭��
			}else{
				$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}
function doSearchTargetAppliance(){	//��������ѯ�ܼ�������Ϣ
	$('#Appliance').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val(),'StandardName':encodeURI($('#function-toolbar-StandardName').combobox('getValue')),'Model':encodeURI($('#function-toolbar-Model').combobox('getValue')),'Range':encodeURI($('#function-toolbar-Range').combobox('getValue')),'Accuracy':encodeURI($('#function-toolbar-Accuracy').combobox('getValue'))};
	$('#Appliance').datagrid('reload');
}
//����ԭʼ��¼Excel�������ļ�
function doUpdateExcelXml(){
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ��ԭʼ��¼��','info');
		return false;
	}
	
	var result = confirm("��ȷ��Ҫ����ԭʼ��¼Excel�������ļ��滻Ϊ��ǰʹ�õİ汾��");
	if(result == false){
		return false;
	}
	$.ajax({
		type: "post",
		url: "/jlyw/OriginalRecordServlet.do?method=11",
		data: {"OriginalRecordId":row.OriginalRecordId},
		dataType: "json",	//�������������ݵ�Ԥ������
		beforeSend: function(XMLHttpRequest){
		},
		success: function(data, textStatus){
			if(data.IsOK){
				$.messager.alert('��ʾ��','���³ɹ���','info');
			}else{
				$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
			}
		},
		complete: function(XMLHttpRequest, textStatus){
			//HideLoading();
		},
		error: function(){
			//���������
		}
	});
}


/******************         ��ʷ֤�鵼����غ���            ***********************/
function doOpenHistoryCertificateWindow(){		//����ʷ֤����Ϣ�Ĵ���
	var row = $('#Appliance').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ�����߱�׼����','info');
		return false;
	}
	$('#load_from_history_certificate-toolbar-Model').val('');
	$('#load_from_history_certificate-toolbar-Range').val('');
	$('#load_from_history_certificate-toolbar-Accuracy').val('');
	
	$('#load_from_history_certificate').window('open');
	//������ʷ֤����Ϣ
	$('#history_certificate_grid').datagrid('options').queryParams={'StandardNameId':encodeURI(row.StandardNameId),'Model':encodeURI($('#load_from_history_certificate-toolbar-Model').val()),'Range':encodeURI($('#load_from_history_certificate-toolbar-Range').val()),'Accuracy':encodeURI($('#load_from_history_certificate-toolbar-Accuracy').val()),'WorkStaff':encodeURI($("#load_from_history_certificate-toolbar-WorkStaff").combobox("getValue"))};
	$('#history_certificate_grid').datagrid('reload');
	
}
function doSearchHistoryCertificate(){
	var row = $('#Appliance').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ�����߱�׼����','info');
		return false;
	}
	$('#history_certificate_grid').datagrid('options').queryParams={'StandardNameId':encodeURI(row.StandardNameId),'Model':encodeURI($('#load_from_history_certificate-toolbar-Model').val()),'Range':encodeURI($('#load_from_history_certificate-toolbar-Range').val()),'Accuracy':encodeURI($('#load_from_history_certificate-toolbar-Accuracy').val()),'WorkStaff':encodeURI($("#load_from_history_certificate-toolbar-WorkStaff").combobox("getValue"))};
	$('#history_certificate_grid').datagrid('reload');
}
function doLoadHistoryCertificate(){	//����ʷ֤�鵼������
	var row = $('#history_certificate_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ����ʷ֤����Ϣ��','info');
		return false;
	}
	//��������ԭʼ��¼�Ĳ���ֵ
	$("#AddOriginalForm-ApplianceName").combobox('setValue', row.ApplianceName);	//��������
	$("#AddOriginalForm-WorkType option[value='"+row.WorkType+"']").attr("selected", true);	//��������
	$("#AddOriginalForm-WorkLocation option[value='"+row.WorkLocation+"']").attr("selected", true);	//�����ص�
	
	$("#AddOriginalForm-Manufacturer").combobox('setValue', row.Manufacturer);	//���쵥λ
	$("#AddOriginalForm input[name='ApplianceCode']:first").val(row.ApplianceCode);
	$("#AddOriginalForm input[name='ApplianceManageCode']:first").val(row.ManageCode);
	$("#AddOriginalForm input[name='Temp']:first").val(row.Temp);	//�����¶�
	$("#AddOriginalForm input[name='Humidity']:first").val(row.Humidity);
	$("#AddOriginalForm input[name='Pressure']:first").val(row.Pressure);
	$("#AddOriginalForm input[name='OtherCond']:first").val(row.OtherCond);	//����
	$("#AddOriginalForm-StdOrStdAppUsageStatus").combobox('setValue', row.StdOrStdAppUsageStatus);
	$("#AddOriginalForm input[name='AbnormalDesc']:first").val(row.AbnormalDesc);
	$("#AddOriginalForm-Conclusion").combobox('setValue', row.Conclusion);//����
	$("#AddOriginalForm-VerifyUser").combobox('setValue', row.Verifier);	//����Ա
	$("#AddOriginalForm input[name='Quantity']:first").val(row.Quantity);	//��������
	$("#AddOriginalForm-RepairLevel option[value='"+row.RepairLevel+"']").attr("selected", true);	// ������
	
	$("#AddOriginalForm input[name='MaterialDetail']:first").val(row.MaterialDetail);
	$("#AddOriginalForm input[name='CarFee']:first").val(row.CarFee);	//��ͨ��
	$("#AddOriginalForm input[name='DebugFee']:first").val(row.DebugFee);
	$("#AddOriginalForm input[name='MaterialFee']:first").val(row.MaterialFee);
	$("#AddOriginalForm input[name='OtherFee']:first").val(row.OtherFee);//������
	setAddOriginalFormTestFee();	//�춨��
	setAddOriginalFormRepairFee();	//�����
	
	$("#AddOriginalForm-Mandatory option[value='"+row.Mandatory+"']").attr("selected", true);	//�Ƿ�ǿ��
	$("#AddOriginalForm input[name='MandatoryCode']:first").val(row.MandatoryCode);	//ǿ��Ψһ�Ժ�
	$("#AddOriginalForm-MandatoryPurpose option[value='"+row.MandatoryPurpose+"']").attr("selected", true);	//ǿ������/��;
	$("#AddOriginalForm input[name='MandatoryItemType']:first").val(row.MandatoryItemType);	//ǿ����Ŀ��Ӧ���
	$("#AddOriginalForm input[name='MandatoryType']:first").val(row.MandatoryType);	//ǿ����Ŀ��Ӧ�ֱ�
	$("#AddOriginalForm input[name='MandatoryApplyPlace']:first").val(row.MandatoryApplyPlace);	//ʹ�ð�װ�ص�
	
	$("#AddOriginalForm-FirstIsUnqualified option[value='"+row.FirstIsUnqualified+"']").attr("selected", true);	// �׼��Ƿ�ϸ�
	$("#AddOriginalForm input[name='UnqualifiedReason']:first").val(row.UnqualifiedReason);	//�ܼ첻�ϸ�ԭ��
	$("#AddOriginalForm input[name='Remark']:first").val(row.Remark);
	
	$("#function-toolbar-StandardName").combobox('setValue',row.ApplianceStandardName);
	$("#function-toolbar-Model").combobox('setValue',row.Model);	//�ͺŹ��
	$("#function-toolbar-Range").combobox('setValue',row.Range);
	$("#function-toolbar-Accuracy").combobox('setValue',row.Accuracy);
	doSearchTargetAppliance();
	$('#load_from_history_certificate').window('close');
				
}
/*******       �������������ķ���          *********/
//����AddOriginalForm�ļ춨��
function setAddOriginalFormTestFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='TestFee']:first").val('');//�춨��
			return false;
		}else{
			$("#AddOriginalForm input[name='TestFee']:first").val(row.Fee);//�춨��
		}
	}catch(ex){}
}
//����AddOriginalForm�������
function setAddOriginalFormRepairFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//�����
			return false;
		}
		var level = $("#AddOriginalForm-RepairLevel").val();
		if(level == "С"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.SRFee);//�����
		}else if(level == "��"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.MRFee);//�����
		}else if(level == "��"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.LRFee);//�����
		}else{
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//�����
		}
	}catch(ex){}
}
//����ķ�����2012-11-30֮��ӵ�
function selectTechnicalDocsAndStandards(){//��ѡ���̱�׼�Ĵ���
	$('#selectTechnicalDocsAndStandards_window').window('open');
}
function DeleteStdAppRecord(){//ɾ����׼����
	var row = $('#table_StandardAppliance').datagrid('getSelected');
	if(row == null){
		$.messager.alert("��ʾ","��ѡ���׼���ߣ�","info");
	}else{
		var index = $('#table_StandardAppliance').datagrid('getRowIndex', row);
		$('#table_StandardAppliance').datagrid('deleteRow', index);
	}	
}
function DeleteStdRecord(){//ɾ��������׼
	var row = $('#table_Standard').datagrid('getSelected');
	if(row == null){
		$.messager.alert("��ʾ","��ѡ�������׼��","info");
	}else{
		var index = $('#table_Standard').datagrid('getRowIndex', row);
		$('#table_Standard').datagrid('deleteRow', index);
	}	
}
function DeleteSpecificationRecord(){//ɾ�������淶
	var row = $('#table_Specification').datagrid('getSelected');
	if(row == null){
		$.messager.alert("��ʾ","��ѡ�����淶��","info");
	}else{
		var index = $('#table_Specification').datagrid('getRowIndex', row);
		$('#table_Specification').datagrid('deleteRow', index);
	}	
}
function submitDocsAndStandards(){//�ύѡ��ļ����淶
	var rows1 = $('#table_StandardAppliance').datagrid('getRows');
	var rows2 = $('#table_Standard').datagrid('getRows');
	var rows3 = $('#table_Specification').datagrid('getRows');
	if((rows1==null)&&(rows2==null)&&(rows3==null)){
		$.messager.alert("��ʾ","��ѡ�����淶��������׼�ͱ�׼�����е�һ����߶��","info");
	}else{
		$('#AddOriginalForm-StandardAppliancesString').val(JSON.stringify(rows1));
		$('#AddOriginalForm-StandardsString').val(JSON.stringify(rows2));
		$('#AddOriginalForm-SpecificationsString').val(JSON.stringify(rows3));
		$('#selectTechnicalDocsAndStandards_window').window('close');
	}
}

