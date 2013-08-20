// JavaScript Document
// ¼��ί�е�ҳ�棨CommissionSheet.jsp �ű���
	


$(function(){
	
	$("#CustomerName").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
			$("#CustomerTel").val(record.tel);
			$("#CustomerAddress").val(record.address);
			$("#CustomerZipCode").val(record.zipCode);
			$("#ContactPerson").val(record.contactor);
			$("#ContactorTel").val(record.contactorTel);
			$("#CustomerHandler").val('');
			$("#SampleFrom").combobox('setValue', '');
			$("#BillingTo").combobox('setValue','');
			
			$("#QuotationId").combobox('reload','/jlyw/QuotationServlet.do?method=11&QueryName='+encodeURI(record.name));	//���۵���ѯ
			
			try{
				//�ֳ�ί�е���ѯ���������½��ֳ�ί�е������õ�
				$("#LocaleCommissionCode").combobox('reload','/jlyw/LocaleMissionServlet.do?method=14&QueryName='+encodeURI(record.name));
			}catch(ex){}
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
					var newValue = $('#CustomerName').combobox('getText');
					$('#CustomerName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}, 500);
//			$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
		}
	});
	
	$("#SampleFrom").combobox({
		valueField:'name',
		textField:'name',
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
					var newValue = $('#SampleFrom').combobox('getText');
					$('#SampleFrom').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}, 500);
			//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
		}
	});
	$("#BillingTo").combobox({
		valueField:'name',
		textField:'name',
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
					var newValue = $('#BillingTo').combobox('getText');
					$('#BillingTo').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}, 500);
			//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
		}
	});
	
	$("#ApplianceSpeciesName").combobox({
		valueField:'Name',
		textField:'Name',
		onSelect:function(record){
			
			$("#SpeciesType").val(record.SpeciesType);		//���߷�������
			$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//���߷���ID�������Ǳ�׼����ID��
/*			$("#ApplianceName").combobox('loadData',record.ApplianceName);	//��������
			if(record.ApplianceName.length == 1){
				$("#ApplianceName").combobox('select',record.ApplianceName[0].name);
			}else{
				$("#ApplianceName").combobox('clear');
			}
			$("#Model").combobox('loadData',record.Model);	//�ͺŹ��
			$("#Range").combobox('loadData',record.Range);	//������Χ
			$("#Accuracy").combobox('loadData',record.Accuracy);	//���ȵȼ�
			$("#Manufacturer").combobox('loadData',record.Manufacturer);	//���쳧��
			$("#Allotee").combobox('loadData',record.Allotee);	//������Ա*/
			$("#ApplianceName").combobox('clear');
			$("#Model").combobox('clear');
			$("#Range").combobox('clear');
			$("#Accuracy").combobox('clear');
			$("#Manufacturer").combobox('clear');
			$("#Allotee").combobox('clear');
			if(record.SpeciesType == 0 || record.SpeciesType == '0' || record.SpeciesType == 2 || record.SpeciesType == '2'){	//��׼����Or��������
				if(record.PopName == null || record.PopName.length == 0){
					$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);
				}else{
					$("#ApplianceName").combobox('setValue',record.PopName);
				}
				$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//�ͺŹ��
				$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//������Χ
				$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//���ȵȼ�
				$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//���쳧��
			}else if(record.SpeciesType == 1 || record.SpeciesType == '1'){	//��������
				$("#ApplianceName").combobox('loadData',[]);
				$("#Model").combobox('loadData',[]);	//�ͺŹ��
				$("#Range").combobox('loadData',[]);	//������Χ
				$("#Accuracy").combobox('loadData',[]);	//���ȵȼ�
				$("#Manufacturer").combobox('loadData',[]);	//���쳧��
			}
			$("#Allotee").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//������Ա
			
		},
		onChange:function(newValue, oldValue){
			var allData = $(this).combobox('getData');
			if(allData != null && allData.length > 0){
				for(var i=0; i<allData.length; i++)
				{
					if(newValue==allData[i].Name){
						return false;
					}
				}
			}
			$("#SpeciesType").val('');		//���߷�������
			$("#ApplianceSpeciesId").val('');	//���߷���ID�������Ǳ�׼����ID��
			
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $('#ApplianceSpeciesName').combobox('getText');
					$('#ApplianceSpeciesName').combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
			}, 700);
			//$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
		}
	});
	
	$("#SubContractor").combobox({
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
			$(this).combobox('reload','/jlyw/SubContractServlet.do?method=0&QueryName='+newValue);
		}
	});
/*		$("#Allotee").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
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
			$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
		}
	});*/
	
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
	$('#table5').datagrid({
		title:'���μ��������',
		width:1000,
		height:300,
		singleSelect:false, 
		nowrap: false,
		striped: true,
		sortName: 'ApplianceCode',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'CommissionNumber',title:'ί�е���',width:80,align:'center'},
			{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
			{field:'ApplianceName',title:'��������',width:80,editor:'text',align:'center'},
			{field:'ApplianceCode',title:'�������',editor:'text',width:80,align:'center'},
			{field:'AppManageCode',title:'������',editor:'text',width:80,align:'center'},
			{field:'Model',title:'�ͺŹ��',width:80,editor:'text',align:'center'},
			{field:'Range',title:'������Χ',width:80,editor:'text',align:'center'},
			{field:'Accuracy',title:'���ȵȼ�',width:80,editor:'text',align:'center'},
			{field:'Manufacturer',title:'���쳧��',width:80,editor:'text',align:'center'},
			{field:'Quantity',title:'̨/����',width:70,editor:'numberbox',align:'center'},
			{field:'MandatoryInspection',title:'ǿ�Ƽ���',width:80,align:'center',editor:{
					type:'combobox',
					options:{
						valueField:'id',
						textField:'name',
						data:products,
						required:true
					}
				},
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
			{field:'Urgent',title:'�Ƿ�Ӽ�',width:60,align:'center',editor:{
					type:'combobox',
					options:{
						valueField:'id',
						textField:'name',
						data:products1,
						required:true
					}
				},
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
			{field:'Trans',title:'�Ƿ�ת��',width:80,editor:'text',align:'center',editor:{
					type:'combobox',
					options:{
						valueField:'id',
						textField:'name',
						data:products1,
						required:true
					}
				},
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
			{field:'SubContractor',title:'ת����',width:80,editor:'text',align:'center'},
			{field:'Appearance',title:'��۸���',width:80,editor:'text',align:'center'},
			{field:'Repair',title:'�������',width:100,editor:'text',align:'center',editor:{
					type:'combobox',
					options:{
						valueField:'id',
						textField:'name',
						data:products2,
						required:true
					}
				},
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
			{field:'ReportType',title:'������ʽ',width:80,editor:'text',align:'center',editor:{
					type:'combobox',
					options:{
						valueField:'id',
						textField:'name',
						data:products3,
						required:true
					}
				},
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
					if(value == 3 || value == '3')
					{
						rowData['ReportType']=3;
						return "���";
					}
					if(value == 4 || value == '4')
					{
						rowData['ReportType']=4;
						return "����";
					}
				}},
			{field:'OtherRequirements',title:'����Ҫ��',width:80,editor:'text',align:'center'},
			{field:'Location',title:'���λ��',width:80,editor:'text',align:'center'},
			{field:'Allotee',title:'�ɶ���',width:80,editor:'text',align:'center'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:[{
			text:'ɾ��',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("��ȷ��Ҫ�Ƴ���Щ������");
				if(result == false){
					return false;
				}
				var rows = $('#table5').datagrid('getSelections');
				var length = rows.length;
				for(var i=length-1; i>=0; i--){
					var index = $('#table5').datagrid('getRowIndex', rows[i]);
					$('#table5').datagrid('deleteRow', index);
				}
			}
		},'-',{
			text:'ע��ί�е�',
			iconCls:'icon-cancel',
			handler:function(){
				var rows = $("#table5").datagrid("getSelections");
				if(rows.length == 0){
					$.messager.alert('��ʾ',"��ѡ��Ҫע����ί�е���",'info');
					return false;
				}
				if(rows.length > 1){
					$.messager.alert('��ʾ',"һ�����ֻ��ע��һ��ί�е���",'info');
					return false;
				}
				if(rows[0].CommissionNumber == null || rows[0].CommissionNumber.length == 0){
					$.messager.alert('��ʾ',"��ѡ����δ����ί�е�������ע����",'info');
					return false;
				}
				var result = confirm("��ȷ��Ҫע������Ϊ '"+rows[0].CommissionNumber+"' ��ί�е���");
				if(result == false){
					return false;
				}
				$.ajax({
						type: "post",
						url: "/jlyw/CommissionSheetServlet.do?method=2",
						data: {"Code":rows[0].CommissionNumber,"Pwd":rows[0].PrintObj.Pwd},
						dataType: "json",	//�������������ݵ�Ԥ������
						beforeSend: function(XMLHttpRequest){
							$('#table5').datagrid('acceptChanges');	
							//ShowLoading();
						},
						success: function(data, textStatus){
							if(data.IsOK){
								var index = $("#table5").datagrid("getRowIndex", rows[0]);
								if(index != -1){
									var newValue = rows[0];
									newValue.CommissionNumber="";
									newValue.PrintObj=null;
									$("#table5").datagrid("updateRow", {index:index, row:newValue});
								}
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
			text:'ѡ��δ����ļ�¼',
			iconCls:'icon-redo',
			handler:function(){
				var rows = $("#table5").datagrid("getRows");
				for(var i = 0; i<rows.length; i++){
					if(rows[i].CommissionNumber != null && rows[i].CommissionNumber.length > 0){
						$("#table5").datagrid("unselectRow",i);
					}else{
						$("#table5").datagrid("selectRow",i);
					}
				}	
		
			}
		},'-',{
			text:'���',
			iconCls:'icon-undo',
			handler:function(){
				var result = confirm("ȷ��Ҫ���������");
				if(result == false){
					return false;
				}
				$('#SpeciesType').val(""),
				$('#ApplianceSpeciesId').val(""),
				$('#ApplianceSpeciesName').combobox('setValue',""),
				$('#ApplianceName').val("");
				$('#ApplianceCode').val("");
				$('#AppManageCode').val("");
				$('#Quantity').val("");
				$('#Mandatory').val("");
				$('#Repair').val("");
				$('#ReportType').val("");
				var rows = $('#table5').datagrid('getRows');
				var length = rows.length;
				for(var i=length-1; i>=0; i--){
					var index = $('#table5').datagrid('getRowIndex', rows[i]);
					$('#table5').datagrid('deleteRow', index);
				}
			}
		}],
		onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		},
//		onClickRow:function(rowIndex){			
//					if (lastIndex != rowIndex){
//				$('#table5').datagrid('endEdit', lastIndex);
//				$('#table5').datagrid('beginEdit', rowIndex);
//					}
//			lastIndex = rowIndex;
//		},
		onClickRow:function(rowIndex, rowDataParam){
			var rows = $(this).datagrid('getSelections');
			
			var rowData = null;	//���ҽ����ϵ�һ����ѡ����
			var minIndex = null;
			for(var i = 0; i< rows.length;i++){
				var rowIndexTemp = $(this).datagrid('getRowIndex', rows[i]);
				if(rowData == null){
					rowData=rows[i];
					minIndex = rowIndexTemp;
				}else{
					if(minIndex >= rowIndexTemp){
						rowData=rows[i];
						minIndex = rowIndexTemp;
					}
																		
				}
			}
			if(rowData == null){
				return false;
			}
			
			var appSpeComboData = {   //������Ȩ����
				SpeciesType:rowData.SpeciesType,
				ApplianceSpeciesId:rowData.ApplianceSpeciesId,
				Name:rowData.ApplianceSpeciesName,
				PopName:rowData.ApplianceName
			};	
			$("#ApplianceSpeciesName").combobox('loadData',[appSpeComboData]);
			$("#ApplianceSpeciesName").combobox('setValue', rowData.ApplianceSpeciesName);
			$("#SpeciesType").val(rowData.SpeciesType);	//���ص�ID
			$("#ApplianceSpeciesId").val(rowData.ApplianceSpeciesId);	//���ص�ID
			$("#ApplianceName").combobox('setValue',rowData.ApplianceName);	//��������
			
			$("#ApplianceCode").val(rowData.ApplianceCode);	//�������
			$('#AppManageCode').val(rowData.AppManageCode);	//������
			$('#Model').combobox('setValue', rowData.Model);	//�ͺŹ��
			$('#Range').combobox('setValue', rowData.Range);	//������Χ
			$('#Accuracy').combobox('setValue', rowData.Accuracy);	//���ȵȼ�			
			$('#Manufacturer').combobox('setValue', rowData.Manufacturer);	//���쳧
			$('#Quantity').val(rowData.Quantity);	//��������
			$('#Mandatory').val(rowData.MandatoryInspection);	//�Ƿ�ǿ��
			
			if(rowData.Urgent == 0 || rowData.Urgent == '0'){	//�Ƿ�Ӽ�
				$("#Ness").attr("checked",true);	//��ѡ
			}else{
				$("#Ness").attr("checked",false);	//ȥ��ѡ
			}
			
			if(rowData.Trans == 0 || rowData.Trans == '0'){		//�Ƿ�ת��
				$("#Trans").attr("checked",true);	//��ѡ
			}else{
				$("#Trans").attr("checked",false);	//ȥ��ѡ
			}
			$("#Appearance").val(rowData.Appearance);	//��۸���
			$("#Repair").val(rowData.Repair);	//�������
			$("#ReportType").val(rowData.ReportType);	//������ʽ
			$("#SubContractor").combobox('setValue', rowData.SubContractor);	//ת����
			$("#OtherRequirements").val(rowData.OtherRequirements);	//����Ҫ��
			$("#Location").val(rowData.Location);	//���λ��
			$("#Allotee").combobox('setValue', rowData.Allotee);	//�ɶ���
						
			if(rowData.SpeciesType == 0 || rowData.SpeciesType == '0' || rowData.SpeciesType == 2 || rowData.SpeciesType == '2'){	//��׼����Or��������
				if(rowData.ApplianceName == null || rowData.ApplianceName.length == 0){
					$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);
				}else{
					$("#ApplianceName").combobox('setValue',rowData.ApplianceName);
				}
				$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//�ͺŹ��
				$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//������Χ
				$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//���ȵȼ�
				$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//���쳧��
			}else if(rowData.SpeciesType == 1 || rowData.SpeciesType == '1'){	//��������
				$("#ApplianceName").combobox('loadData',[]);
				$("#Model").combobox('loadData',[]);	//�ͺŹ��
				$("#Range").combobox('loadData',[]);	//������Χ
				$("#Accuracy").combobox('loadData',[]);	//���ȵȼ�
				$("#Manufacturer").combobox('loadData',[]);	//���쳧��
			}
			$("#Allotee").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//������Ա
			
		}

	});
	

	$('#table6').datagrid({
		width:1000,
		height:500,
		title:'ί�е�λ��ʷ�����¼',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/CommissionSheetServlet.do?method=1',
		remoteSort: false,
		//idField:'id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'CustomerName',title:'ί�е�λ',width:120,align:'center'},
			{field:'CommissionCode',title:'ί�е���',width:80,align:'center'},
			{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
			{field:'ApplianceName',title:'��������',width:80,align:'center'},
			{field:'ApplianceCode',title:'�������',width:80,align:'center'},
			{field:'AppManageCode',title:'������',width:80,align:'center'},
			{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
			{field:'Range',title:'������Χ',width:80,align:'center'},
			{field:'Accuracy',title:'���ȵȼ�',width:80,align:'center'},
			{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
			{field:'Quantity',title:'̨/����',width:70,align:'center'},
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
			{field:'Location',title:'���λ��',width:80,align:'center'},
			{field:'Allotee',title:'�ɶ���',width:80,align:'center'},
			{field:'CommissionDate',title:'ί������',width:80,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:"#table6-search-toolbar"
	});
	
	
	//���۵�
	$('#table_QuoItem').datagrid({
		//width:800,
		//height:300,
		//title:"���۵���Ŀ",
		fit:true,
		singleSelect:false, 
		nowrap: false,
		striped: true,
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Id',title:'���',width:40,align:'center'},
			{field:'StandardNameId',title:'�ܼ����߱�׼����Id',width:120,align:'center'},
			{field:'StandardName',title:'�ܼ����߱�׼����',width:120,align:'center',sortable:true},
			{field:'CertificateName',title:'�ܼ�����֤������',width:120,align:'center'},
			{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
			{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:80,align:'center'},
			{field:'Range',title:'������Χ',width:80,align:'center'},
			{field:'Quantity',title:'̨����',width:80,align:'center',editor:'text'},
			{field:'AppFactoryCode',title:'�������',width:80,align:'center'},
			{field:'AppManageCode',title:'������',width:80,align:'center'},
			{field:'Manufacturer',title:'���쳧',width:80,align:'center'},
			{field:'CertType',title:'֤������',width:80,align:'center',sortable:true,
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
			{field:'SiteTest',title:'�ֳ����',width:80,align:'center',sortable:true,
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
			text:'������ѡ����',
			iconCls:'icon-add',
			handler:function(){
				var rows = $("#table_QuoItem").datagrid("getSelections");				
				if(rows.length == 0){
					$.messager.alert('��ʾ','����ѡ��Ҫ����ı��۵���Ŀ��','info');
					return false;
				}
				var index = $('#table5').datagrid('getRows').length;
				for(var i=0;i<rows.length;i++){	
					if(typeof(rows[i].StandardNameId) != "undefined" && rows[i].StandardNameId != ""){
						$('#table5').datagrid('insertRow',{
							index:index,
							row:{
								Id:rand(1000000),	//6λ�����
								SpeciesType:0,	//��׼����
								ApplianceSpeciesId:rows[i].StandardNameId,
								ApplianceSpeciesName:rows[i].StandardName+"[��]",
								ApplianceName:rows[i].CertificateName,
								ApplianceCode:rows[i].AppFactoryCode,
								AppManageCode:rows[i].AppManageCode,
								Model:rows[i].Model,
								Range:rows[i].Range,
								Accuracy:rows[i].Accuracy,
								Manufacturer:rows[i].Manufacturer,
								Quantity:(typeof(rows[i].Quantity)=="undefined" || rows[i].Quantity=="")?1:rows[i].Quantity,
								MandatoryInspection:1,
								Urgent:1,
								Trans:1,
								SubContractor:'',
								Appearance:'����������޸�����',
								Repair:1,
								ReportType:rows[i].CertType,
								OtherRequirements:'��',
								Location:'',
								Allotee:''
							
								
							}
						});	
						index++;
					}
					
				}//end of for
				
				$("#inputQuo_window").window("close");
			}
		}]
	});
	$("#QuotationId").combobox({ //���۵���
		valueField:'name',
		textField:'name',
		onSelect:function(record){},
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
					var newValue = $('#QuotationId').combobox('getValue');
					$('#QuotationId').combobox('reload','/jlyw/QuotationServlet.do?method=13&QueryName='+encodeURI(newValue));
			}, 500);
			
		}
	});
	
	
		
	var nowDate = new Date();
	$("#CommissionDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
});
function doSubmitForm(t)
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=0',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			
			$('#table5').datagrid('acceptChanges');					
			var rows = $("#table5").datagrid("getSelections");	
			if(rows.length == 0){
				$.messager.alert('��ʾ',"��ѡ���������ߣ�",'info');
				return false;
			}
			for(var i=0; i<rows.length; i++){
				if(rows[i].Quantity == null || rows[i].Quantity < 0){
					$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/��������С��0����������",'info');
					return false;
				}
				if(rows[i].Quantity == 0 && $("#CommissionSheetForm select[name='CommissionType']:first").val() != '5'){	//�ǡ�����ҵ�񡯵�����������Ϊ0
					$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/����������0����������",'info');
					return false;
				}
				if(rows[i].CommissionNumber != null && rows[i].CommissionNumber.length > 0){
					var result = confirm("���� '"+rows[i].ApplianceSpeciesName+"' �Ѿ����ɹ�ί�е��ˣ���ȷ��Ҫ��������һ���µ�ί�е���");
					if(result == false){
						return false;
					}
				}
			}
			$("#Appliances").val(JSON.stringify(rows));
			NoSubmitAgain(t);
			return $("#CommissionSheetForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				var allRows = $("#table5").datagrid("getRows");
				for(var i=0;i<result.CommissionSheetList.length;i++){
					var index = $("#table5").datagrid("getRowIndex", result.CommissionSheetList[i].Id);
					if(index != null && index >= 0 && index < allRows.length){
						var vRow = allRows[index];
						vRow.CommissionNumber = result.CommissionSheetList[i].CommissionNumber;
						vRow.PrintObj = result.CommissionSheetList[i].PrintObj;
						$("#table5").datagrid("updateRow", {index:index, row:vRow});
					}
				}
				alert("��ʼ��ӡ:"+result.CommissionSheetList.length);
				for(var i=0;i<result.CommissionSheetList.length;i++){
					Preview1(result.CommissionSheetList[i].PrintObj);
				}
				
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});
}
function doSubmitFormNoPrint(t)		//����ί�е�������ӡ
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=0',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			
			$('#table5').datagrid('acceptChanges');					
			var rows = $("#table5").datagrid("getSelections");	
			if(rows.length == 0){
				$.messager.alert('��ʾ',"��ѡ���������ߣ�",'info');
				return false;
			}
			for(var i=0; i<rows.length; i++){
				if(rows[i].Quantity == null || rows[i].Quantity == 0){
					$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/����������0����������",'info');
					return false;
				}
				if(rows[i].CommissionNumber != null && rows[i].CommissionNumber.length > 0){
					var result = confirm("���� '"+rows[i].ApplianceSpeciesName+"' �Ѿ����ɹ�ί�е��ˣ���ȷ��Ҫ��������һ���µ�ί�е���");
					if(result == false){
						return false;
					}
				}
			}
			$("#Appliances").val(JSON.stringify(rows));
			NoSubmitAgain(t);
			return $("#CommissionSheetForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				var allRows = $("#table5").datagrid("getRows");
				for(var i=0;i<result.CommissionSheetList.length;i++){
					var index = $("#table5").datagrid("getRowIndex", result.CommissionSheetList[i].Id);
					if(index != null && index >= 0 && index < allRows.length){
						var vRow = allRows[index];
						vRow.CommissionNumber = result.CommissionSheetList[i].CommissionNumber;
						vRow.PrintObj = result.CommissionSheetList[i].PrintObj;
						$("#table5").datagrid("updateRow", {index:index, row:vRow});
					}
				}
				
				
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});
}
function doPrintCommissionSheet(){
	var rows = $("#table5").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	for(var i=0; i<rows.length; i++){
		if(rows[i].CommissionNumber == null || rows[i].CommissionNumber.length == 0){
			$.messager.alert('��ʾ',"���� '"+rows[i].ApplianceSpeciesName+"' ��δ����ί�е������ܴ�ӡ��",'info');
			return false;
		}
	}
	for(var i=0; i<rows.length; i++){
		Preview1(rows[i].PrintObj);
	}
}
function doLoadHistoryCommission()
{
	if($('#CustomerName').combobox('getValue').length==0){
		$.messager.alert('��ʾ','������ί�е�λ�����ƣ�','info');
		return false;
	}
	$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=1';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}
function UpdateRecord()	//����һ�����������
{
	var rows = $('#table5').datagrid('getSelections');
	if(rows.length == 0){
		$.messager.alert("��ʾ","���ڡ����μ�������ߡ���ѡ��һ����Ҫ���µ����ߣ�","info");
		return false;
	}
	if(rows.length > 1){
		$.messager.alert("��ʾ","ֻ���ڡ����μ�������ߡ���ѡ��һ����Ҫ���µ����ߣ�","info");
		return false;
	}
	if(rows[0].CommissionNumber != null && rows[0].CommissionNumber.length > 0){
		var result = confirm("���� '"+rows[0].ApplianceSpeciesName+"' �Ѿ����ɹ�ί�е��ˣ���ȷ��Ҫ������");
		if(result == false){
			return false;
		}
	}
	if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
		$.messager.alert("��ʾ","��ѡ��һ����Ч�ġ�������Ȩ������","info");
		return false;
	}
	if($("#Quantity").val()==''){
		$.messager.alert("��ʾ","�����롮������������","info");
		return false;
	}
	if($('#Mandatory').val()==''){
		$.messager.alert("��ʾ","��ѡ���Ƿ�ǿ�졯��","info");
		return false;
	}
	if($('#Repair').val()==''){
		$.messager.alert("��ʾ","��ѡ��������񡯣�","info");
		return false;
	}
	if($('#ReportType').val()==''){
		$.messager.alert("��ʾ","��ѡ�񡮱�����ʽ����","info");
		return false;
	}
	if($('#Quantity').val()<0){
		$.messager.alert("��ʾ","������������С���㣡","info");
		return false;
	}
	var urgent,trans,subContractor;	
	if($("#Trans").is(":checked")){
		 trans=0;
		 subContractor=$('#SubContractor').combobox('getValue');
	}else{
		 trans=1;
		 subContractor="";
	}
	if($("#Ness").is(":checked"))
		 urgent=0;
	else
		 urgent=1;

	var index = $("#table5").datagrid("getRowIndex", rows[0].Id);
	rows[0].SpeciesType=$('#SpeciesType').val();
	rows[0].ApplianceSpeciesId=$('#ApplianceSpeciesId').val();
	rows[0].ApplianceSpeciesName=$('#ApplianceSpeciesName').combobox('getValue');
	rows[0].ApplianceName=$('#ApplianceName').combobox('getValue');
	rows[0].ApplianceCode=$('#ApplianceCode').val();
	rows[0].AppManageCode=$('#AppManageCode').val();
	rows[0].Model=$('#Model').combobox('getValue');
	rows[0].Range=$('#Range').combobox('getValue');
	rows[0].Accuracy=$('#Accuracy').combobox('getValue');
	rows[0].Manufacturer=$('#Manufacturer').combobox('getValue');
	rows[0].Quantity=$('#Quantity').val();
	rows[0].MandatoryInspection=$('#Mandatory').val();
	rows[0].Urgent=urgent;
	rows[0].Trans=trans;
	rows[0].SubContractor=subContractor;
	rows[0].Appearance=$('#Appearance').val();
	rows[0].Repair=$('#Repair').val();
	rows[0].ReportType=$('#ReportType').val();
	rows[0].OtherRequirements=$('#OtherRequirements').val();
	rows[0].Location=$('#Location').val();
	rows[0].Allotee=$('#Allotee').combobox('getValue');
	$('#table5').datagrid('updateRow', {
		index: index,
		row:rows[0]
	});
	$('#SpeciesType').val(""),
	$('#ApplianceSpeciesId').val(""),
	$('#ApplianceSpeciesName').combobox('setValue',""),
	$('#ApplianceName').val("");
	$('#ApplianceCode').val("");
	$('#AppManageCode').val("");
	$('#Quantity').val("");
	$('#Mandatory').val("");
	$('#Repair').val("");
	$('#ReportType').val("");	
}
function AddRecord()
{
	if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
		$.messager.alert("��ʾ","��ѡ��һ����Ч�ġ�������Ȩ������","info");
		return false;
	}
	if($("#Quantity").val()==''){
		$.messager.alert("��ʾ","�����롮������������","info");
		return false;
	}
	if($('#Mandatory').val()==''){
		$.messager.alert("��ʾ","��ѡ���Ƿ�ǿ�졯��","info");
		return false;
	}
	if($('#Repair').val()==''){
		$.messager.alert("��ʾ","��ѡ��������񡯣�","info");
		return false;
	}
	if($('#ReportType').val()==''){
		$.messager.alert("��ʾ","��ѡ�񡮱�����ʽ����","info");
		return false;
	}
	if($('#Quantity').val()<0){
		$.messager.alert("��ʾ","������������С���㣡","info");
		return false;
	}
	var urgent,trans,subContractor;	
	if($("#Trans").is(":checked")){
		 trans=0;
		 subContractor=$('#SubContractor').combobox('getValue');
	}else{
		 trans=1;
		 subContractor="";
	}
	if($("#Ness").is(":checked"))
		 urgent=0;
	else
		 urgent=1;

	var index = $('#table5').datagrid('getRows').length;	//�����һ��������¼
	$('#table5').datagrid('insertRow', {
		index: index,
		row:{
			Id:rand(1000000),	//6λ�����
			SpeciesType:$('#SpeciesType').val(),
			ApplianceSpeciesId:$('#ApplianceSpeciesId').val(),
			ApplianceSpeciesName:$('#ApplianceSpeciesName').combobox('getValue'),
			ApplianceName:$('#ApplianceName').combobox('getValue'),
			ApplianceCode:$('#ApplianceCode').val(),
			AppManageCode:$('#AppManageCode').val(),
			Model:$('#Model').combobox('getValue'),
			Range:$('#Range').combobox('getValue'),
			Accuracy:$('#Accuracy').combobox('getValue'),
			Manufacturer:$('#Manufacturer').combobox('getValue'),
			Quantity:$('#Quantity').val(),
			MandatoryInspection:$('#Mandatory').val(),
			Urgent:urgent,
			Trans:trans,
			SubContractor:subContractor,
			Appearance:$('#Appearance').val(),
			Repair:$('#Repair').val(),
			ReportType:$('#ReportType').val(),
			OtherRequirements:$('#OtherRequirements').val(),
			Location:$('#Location').val(),
			Allotee:$('#Allotee').combobox('getValue')
		}
	});
	$('#SpeciesType').val(""),
	$('#ApplianceSpeciesId').val(""),
	$('#ApplianceSpeciesName').combobox('setValue',""),
	$('#ApplianceName').val("");
	$('#ApplianceCode').val("");
	$('#AppManageCode').val("");
	$('#Quantity').val("");
	$('#Mandatory').val("");
	$('#Repair').val("");
	$('#ReportType').val("");
	
	//NoSubmitAgain(t);

}
function AddRecordFromHistory(){
	var rows = $('#table6').datagrid('getSelections');
	var index = $('#table5').datagrid('getRows').length;	//�����һ��������¼
	
	for(var i=0; i<rows.length; i++){
		if(rows[i].ApplianceSpeciesNameStatus != 0){
			$.messager.alert('��ʾ',"��Ȩ���� '"+rows[i].ApplianceSpeciesName+"' �ѹ�ʱ��������ӣ�",'info');
			continue;
		}
		$('#table5').datagrid('insertRow', {
			index: index,
			row:{
				Id:rand(1000000),	//6λ�����
				SpeciesType:rows[i].SpeciesType,
				ApplianceSpeciesId:rows[i].ApplianceSpeciesId,
				ApplianceSpeciesName:rows[i].ApplianceSpeciesName,
				ApplianceName:rows[i].ApplianceName,
				ApplianceCode:rows[i].ApplianceCode,
				AppManageCode:rows[i].AppManageCode,
				Model:rows[i].Model,
				Range:rows[i].Range,
				Accuracy:rows[i].Accuracy,
				Manufacturer:rows[i].Manufacturer,
				Quantity:rows[i].Quantity,
				MandatoryInspection:rows[i].MandatoryInspection,
				Urgent:rows[i].Urgent,
				Trans:rows[i].Trans,
				SubContractor:rows[i].SubContractor,
				Appearance:rows[i].Appearance,
				Repair:rows[i].Repair,
				ReportType:rows[i].ReportType,
				OtherRequirements:rows[i].OtherRequirements,
				Location:rows[i].Location,
				Allotee:rows[i].Allotee
			}
		});
		index++;
	}
	$('#table6').datagrid('clearSelections');
}

//��֤���۵��Ƿ���Ч
function doCheckQuotationValid(){
	$.ajax({
			type: "post",
			url: "/jlyw/QuotationServlet.do?method=6",
			data: {"QuotationId":$('#QuotationId').val()},
			dataType: "json",	//�������������ݵ�Ԥ������
			beforeSend: function(XMLHttpRequest){
				if($('#QuotationId').val().length == 0){
					$.messager.alert('����!','�������뱨�۵��ţ�','info');
					return false;
				}
			},
			success: function(data, textStatus){
				if(data.IsOK){
					if(data.IsValid){
						$.messager.alert('��֤���','�ñ��۵�����Ч��','info');
					}else{
						$.messager.alert('��֤���','�ñ��۵�����Ч��','warning');
					}
				}else{
					$.messager.alert('��֤ʧ�ܣ�',data.msg,'error');
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

function searchQuoItem()//��ѯ���۵���Ŀ
{
	$("#inputQuo_window").window("open");
	$('#table_QuoItem').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
	$('#table_QuoItem').datagrid('options').queryParams={'quotationId':encodeURI($('#QuotationId').combobox('getValue'))};
	$('#table_QuoItem').datagrid('reload');

}
function searchLocMission()//��ѯ�ֳ�ί�����е�����:�½��ֳ�ί�е����õ�
{
	$("#inputLoc_window").window("open");
	$('#table_LocItem').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
	$('#table_LocItem').datagrid('options').queryParams={'Id':encodeURI($("#locMissionId").val())};
	$('#table_LocItem').datagrid('reload');

}

function doPrintLabel(){  //�򿪴�ӡ��ǩ����
	var rows = $("#table5").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('��ʾ',"��ѡ��Ҫ��ӡ��ί�е���",'info');
		return false;
	}
	if(rows.length>1){
		$.messager.alert('��ʾ',"ֻ��ѡ���ӡһ��ί�е������߱�ǩ��",'info');
		return false;	
	}
	$("#LabelPrint_form").form('clear');	
	$('#LabelPrint_window').window('open');
	if(rows.length==1){		
		$('#RangeFrom').val(1);	
		$('#RangeEnd').val(rows[0].PrintObj.Quantity);	
		$('#AttachmentNum').val(1);		
	}
}


function doPrintLabelSubmit(){  //��ӡ��ǩ
	if(!$("#LabelPrint_form").form('validate'))
		return false ;
	var rows = $("#table5").datagrid("getSelections");	
	
	if(getInt($('#RangeFrom').val())<1||getInt($('#RangeFrom').val())>getInt($('#RangeEnd').val())||getInt($('#RangeEnd').val())>rows[0].PrintObj.Quantity){
		$.messager.alert('��ʾ',"��ӡ���߷�Χ����ȷ��",'info');
		return false;
	}
	if(getInt($('#RangeFrom').val())==getInt($('#RangeEnd').val())&&getInt($('#AttachmentNum').val())>1){
		
	}
				
	var result = confirm("��ȷ��Ҫ���д˲�����");
	if(result == false){
		return false;
	}
	
	if(rows.length==1){
		for(var i=getInt($('#RangeFrom').val());i<=getInt($('#RangeEnd').val());i++){
			var WYID = rows[0].PrintObj.Code + "-" + i + "/" + rows[0].PrintObj.Quantity;
			//console.info(WYID);
			if(getInt($('#RangeFrom').val())==getInt($('#RangeEnd').val())&&getInt($('#AttachmentNum').val())>1){
				for(var j=0;j<getInt($('#AttachmentNum').val());j++){
					var tempWYID=WYID;
				   tempWYID = tempWYID + "-" + (j+1);
				   RealPrintSFS(rows[0].PrintObj,tempWYID);
				}
			}else{
				RealPrintSFS(rows[0].PrintObj,WYID);
			}
		}
	}
}

function doSubmitFormYL()		//Ԥ��ί�е�
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=16',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("��ʾ","��ѡ��һ����Ч�ġ�������Ȩ������","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("��ʾ","�����롮������������","info");
				return false;
			}
			if($('#Mandatory').val()==''){
				$.messager.alert("��ʾ","��ѡ���Ƿ�ǿ�졯��","info");
				return false;
			}
			if($('#Repair').val()==''){
				$.messager.alert("��ʾ","��ѡ��������񡯣�","info");
				return false;
			}
			if($('#ReportType').val()==''){
				$.messager.alert("��ʾ","��ѡ�񡮱�����ʽ����","info");
				return false;
			}
			if($('#Quantity').val()<0){
				$.messager.alert("��ʾ","������������С���㣡","info");
				return false;
			}
		
			return $("#CommissionSheetForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#YLCommissionSheetList").val(JSON.stringify(result.CommissionSheetList));
				var result = confirm("Ԥ���ɹ����Ƿ�ʼ��ӡ��");
				if(result == false){
					return false;
				}
				printSubmitFormYL();
				
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});
}
function printSubmitFormYL(){
	if($('#YLCommissionSheetList').val() == ''||$('#YLCommissionSheetList').val().length==0){
		$.messager.alert('��ʾ','��δԤ��ί�е���','error');
	}
	else{			
		$('#formLook').submit();
	}
}
