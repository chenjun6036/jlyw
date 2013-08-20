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
		singleSelect:true, 
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
			{field:'LocaleApplianceId', title:'�ֳ������ĿID', width:80,align:'center'},
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
			text:'���',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("��ȷ��Ҫ�Ƴ���Щ������");
				if(result == false){
					return false;
				}
				$('#table5').datagrid('loadData', {'total':0, 'rows':[]});
			}
		}],
		onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		}
//		onClickRow:function(rowIndex){			
//					if (lastIndex != rowIndex){
//				$('#table5').datagrid('endEdit', lastIndex);
//				$('#table5').datagrid('beginEdit', rowIndex);
//					}
//			lastIndex = rowIndex;
//		},
		

	});
	
	
	$("#LocaleStaffId").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		editable:false,
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
	
/*	$('#LocaleCommissionDate').datebox({
		onSelect:function(nowDate){
			nowDate.setDate(nowDate.getDate() + 7);
			$('#PromiseDate').datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()))
		}

	})*/

	$("#LocaleCommissionCode").combobox({//�ֳ�ί�����
	//	url:'/jlyw/CustomerServlet.do?method=5',
		valueField:'code',
		textField:'code',
		required:true,
		onSelect:function(record){
			$("#locMissionId").val(record.Id);
			$("#CommissionSheetForm").form('load',record);
			
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
					var newValue = $('#LocaleCommissionCode').combobox('getText');
					$('#LocaleCommissionCode').combobox('reload','/jlyw/LocaleMissionServlet.do?method=15&QueryName='+encodeURI(newValue));
			}, 700);
			
		}
	});
	$('#table_LocItem').datagrid({
		//title:'��ѡ�ֳ�ҵ���е�����',
		width:780,
		height:380,
		fit:false,
		singleSelect:false, 
		nowrap: false,
		striped: true,
		sortName: 'ApplianceSpeciesName',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'Id', title:'�ֳ������ĿID', width:80, align:'center'},
			{field:'ApplianceSpeciesId',title:'������Ȩ��ID',width:80,align:'center'},
			{field:'ApplianceSpeciesName',title:'������Ȩ��',width:120,align:'center'},
			{field:'ApplianceName',title:'��������',width:180,align:'center'},
		
			{field:'Quantity',title:'̨/����',width:70,align:'center'},
			{field:'WorkStaff',title:'�ɶ���',width:80,align:'center'},
			{field:'AssistStaff',title:'�����',width:120,align:'center'},
			
			{field:'ApplianceCode',title:'�������',width:80,align:'center'},
			{field:'AppManageCode',title:'������',width:80,align:'center'},
			{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
			{field:'Range',title:'������Χ',width:80,align:'center'},
			{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:80,align:'center'},
			{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
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
			{field:'TestFee',title:'����',width:80,align:'center'}				
		]],
		pagination:false,
		rownumbers:true,
		toolbar:[{
			text:'����ȫ������',
			iconCls:'icon-add',
			handler:function(){
				var rows = $("#table_LocItem").datagrid("getRows");
				for(var i = 0; i<rows.length; i++){
					if(typeof(rows[i].ApplianceSpeciesId) == "undefined" || rows[i].ApplianceSpeciesId == ""){
						$.messager.alert('��ʾ',"�ֳ������ĿIDΪ��"+rows[i].Id+"������Ӧ��������Ȩ�������ڣ��뵽���ֳ�ҵ������������ƣ�",'info');
						return false;
					}
				}
				var index = $('#table5').datagrid('getRows').length;
				for(var i=0;i<rows.length;i++){
					var newRows = $('#table5').datagrid('getRows');
					var bExisted = false;
					for(var j=0; j<newRows.length; j++){
						if(rows[i].Id == newRows[j].LocaleApplianceId){
							bExisted = true;
							break;
						}
					}
					if(bExisted){	//�Ѿ�������ͬ�����Ŀ��ID
						continue;
					}
					if(typeof(rows[i].ApplianceSpeciesId) != "undefined" && rows[i].ApplianceSpeciesId != ""){
						$('#table5').datagrid('insertRow',{
							index:index,
							row:{
								Id:rand(1000000),	//6λ�����
								LocaleApplianceId:rows[i].Id,
								SpeciesType:rows[i].SpeciesType,	//��׼����
								ApplianceSpeciesId:rows[i].ApplianceSpeciesId,

								ApplianceSpeciesName:rows[i].ApplianceSpeciesName+((rows[i].SpeciesType=='0' || rows[i].SpeciesType==0)?"[��]":"[��]"),
								ApplianceName:rows[i].ApplianceName,
								ApplianceCode:rows[i].ApplianceCode,
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
								ReportType:rows[i].ReportType,
								OtherRequirements:'��',
								Location:'',
								Allotee:rows[i].WorkStaff						
								
							}
						});	
						index++;
					}
					
				}//end of for
				
				$("#inputLoc_window").window("close");
			}
		}],
		onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
		},onLoadSuccess:function(data){
			$(this).datagrid('selectAll');
		}
	});
	$("#CommissionSheetForm").form('validate');
	
});
function doSubmitForm()
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
function doSubmitFormNoPrint()		//����ί�е�������ӡ
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=0',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			
			$('#table5').datagrid('acceptChanges');					
			var rows = $("#table5").datagrid("getRows");	
			if(rows.length == 0){
				$.messager.alert('��ʾ',"û�м�������ߣ�",'info');
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

function searchLocMission()//��ѯ�ֳ�ί�����е�����:�½��ֳ�ί�е����õ�
{
	$("#inputLoc_window").window("open");
	$('#table_LocItem').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
	$('#table_LocItem').datagrid('options').queryParams={'Id':encodeURI($("#locMissionId").val())};
	$('#table_LocItem').datagrid('reload');

}