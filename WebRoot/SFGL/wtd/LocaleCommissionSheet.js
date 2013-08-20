// JavaScript Document
// 录入委托单页面（CommissionSheet.jsp 脚本）
	


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
				//现场委托单查询——仅在新建现场委托单界面用到
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
	$('#table5').datagrid({
		title:'本次检验的器具',
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
			{field:'CommissionNumber',title:'委托单号',width:80,align:'center'},
			{field:'LocaleApplianceId', title:'现场检测条目ID', width:80,align:'center'},
			{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
			{field:'ApplianceName',title:'器具名称',width:80,editor:'text',align:'center'},
			{field:'ApplianceCode',title:'出厂编号',editor:'text',width:80,align:'center'},
			{field:'AppManageCode',title:'管理编号',editor:'text',width:80,align:'center'},
			{field:'Model',title:'型号规格',width:80,editor:'text',align:'center'},
			{field:'Range',title:'测量范围',width:80,editor:'text',align:'center'},
			{field:'Accuracy',title:'精度等级',width:80,editor:'text',align:'center'},
			{field:'Manufacturer',title:'制造厂商',width:80,editor:'text',align:'center'},
			{field:'Quantity',title:'台/件数',width:70,editor:'numberbox',align:'center'},
			{field:'MandatoryInspection',title:'强制检验',width:80,align:'center',editor:{
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
						return "强制检定";
					}
					else
					{
						rowData['MandatoryInspection']=1;
						return "非强制检定";
					}
					
				}},
			{field:'Urgent',title:'是否加急',width:60,align:'center',editor:{
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
						return "是";
					}
					else
					{
						rowData['Urgent']=1;
						return "否";
					}
					
				}},
			{field:'Trans',title:'是否转包',width:80,editor:'text',align:'center',editor:{
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
						return "是";
					}
					else
					{
						rowData['Trans']=1;
						return "否";
					}
					
				}},
			{field:'SubContractor',title:'转包方',width:80,editor:'text',align:'center'},
			{field:'Appearance',title:'外观附件',width:80,editor:'text',align:'center'},
			{field:'Repair',title:'需修理否',width:100,editor:'text',align:'center',editor:{
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
						return "需要修理";
					}
					else
					{
						rowData['Repair']=1;
						return "无需修理";
					}
					
				}},
			{field:'ReportType',title:'报告形式',width:80,editor:'text',align:'center',editor:{
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
						return "检定";
					}
					if(value == 2 || value == '2')
					{
						rowData['ReportType']=2;
						return "校准";
					}
					if(value == 3 || value == '3')
					{
						rowData['ReportType']=3;
						return "检测";
					}
					if(value == 4 || value == '4')
					{
						rowData['ReportType']=4;
						return "检验";
					}
				}},
			{field:'OtherRequirements',title:'其他要求',width:80,editor:'text',align:'center'},
			{field:'Location',title:'存放位置',width:80,editor:'text',align:'center'},
			{field:'Allotee',title:'派定人',width:80,editor:'text',align:'center'}
		]],
		pagination:false,
		rownumbers:true,
		toolbar:[{
			text:'清空',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("您确定要移除这些器具吗？");
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

	$("#LocaleCommissionCode").combobox({//现场委托书号
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
		//title:'所选现场业务中的器具',
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
			{field:'Id', title:'现场检测条目ID', width:80, align:'center'},
			{field:'ApplianceSpeciesId',title:'器具授权名ID',width:80,align:'center'},
			{field:'ApplianceSpeciesName',title:'器具授权名',width:120,align:'center'},
			{field:'ApplianceName',title:'器具名称',width:180,align:'center'},
		
			{field:'Quantity',title:'台/件数',width:70,align:'center'},
			{field:'WorkStaff',title:'派定人',width:80,align:'center'},
			{field:'AssistStaff',title:'替代人',width:120,align:'center'},
			
			{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
			{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
			{field:'Model',title:'型号规格',width:80,align:'center'},
			{field:'Range',title:'测量范围',width:80,align:'center'},
			{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,align:'center'},
			{field:'Manufacturer',title:'制造厂商',width:80,align:'center'},
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
					if(value == 3 || value == '3')
					{
						rowData['ReportType']=3;
						return "检测";
					}
					if(value == 4 || value == '4')
					{
						rowData['ReportType']=4;
						return "检验";
					}
			}},
			{field:'TestFee',title:'检测费',width:80,align:'center'}				
		]],
		pagination:false,
		rownumbers:true,
		toolbar:[{
			text:'导入全部器具',
			iconCls:'icon-add',
			handler:function(){
				var rows = $("#table_LocItem").datagrid("getRows");
				for(var i = 0; i<rows.length; i++){
					if(typeof(rows[i].ApplianceSpeciesId) == "undefined" || rows[i].ApplianceSpeciesId == ""){
						$.messager.alert('提示',"现场检测条目ID为‘"+rows[i].Id+"’所对应的器具授权名不存在，请到‘现场业务管理’进行完善！",'info');
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
					if(bExisted){	//已经存在相同检测条目的ID
						continue;
					}
					if(typeof(rows[i].ApplianceSpeciesId) != "undefined" && rows[i].ApplianceSpeciesId != ""){
						$('#table5').datagrid('insertRow',{
							index:index,
							row:{
								Id:rand(1000000),	//6位随机数
								LocaleApplianceId:rows[i].Id,
								SpeciesType:rows[i].SpeciesType,	//标准名称
								ApplianceSpeciesId:rows[i].ApplianceSpeciesId,

								ApplianceSpeciesName:rows[i].ApplianceSpeciesName+((rows[i].SpeciesType=='0' || rows[i].SpeciesType==0)?"[标]":"[类]"),
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
								Appearance:'外观正常，无附件。',
								Repair:1,
								ReportType:rows[i].ReportType,
								OtherRequirements:'无',
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
				$.messager.alert('提示',"请选择检验的器具！",'info');
				return false;
			}
			for(var i=0; i<rows.length; i++){
				if(rows[i].Quantity == null || rows[i].Quantity < 0){
					$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（不小于0的整数）！",'info');
					return false;
				}
				if(rows[i].Quantity == 0 && $("#CommissionSheetForm select[name='CommissionType']:first").val() != '5'){	//非‘其它业务’但是器具数量为0
					$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（大于0的整数）！",'info');
					return false;
				}
				if(rows[i].CommissionNumber != null && rows[i].CommissionNumber.length > 0){
					var result = confirm("器具 '"+rows[i].ApplianceSpeciesName+"' 已经生成过委托单了，您确定要重新生成一张新的委托单吗？");
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
				alert("开始打印:"+result.CommissionSheetList.length);
				for(var i=0;i<result.CommissionSheetList.length;i++){
					Preview1(result.CommissionSheetList[i].PrintObj);
				}
				
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});
}
function doSubmitFormNoPrint()		//保存委托单，不打印
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=0',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			
			$('#table5').datagrid('acceptChanges');					
			var rows = $("#table5").datagrid("getRows");	
			if(rows.length == 0){
				$.messager.alert('提示',"没有检验的器具！",'info');
				return false;
			}
			for(var i=0; i<rows.length; i++){
				if(rows[i].Quantity == null || rows[i].Quantity == 0){
					$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（大于0的整数）！",'info');
					return false;
				}
				if(rows[i].CommissionNumber != null && rows[i].CommissionNumber.length > 0){
					var result = confirm("器具 '"+rows[i].ApplianceSpeciesName+"' 已经生成过委托单了，您确定要重新生成一张新的委托单吗？");
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
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});
}
function doPrintCommissionSheet(){
	var rows = $("#table5").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
		return false;
	}
	for(var i=0; i<rows.length; i++){
		if(rows[i].CommissionNumber == null || rows[i].CommissionNumber.length == 0){
			$.messager.alert('提示',"器具 '"+rows[i].ApplianceSpeciesName+"' 尚未生成委托单，不能打印！",'info');
			return false;
		}
	}
	for(var i=0; i<rows.length; i++){
		Preview1(rows[i].PrintObj);
	}
}

function searchLocMission()//查询现场委托书中的器具:新建现场委托单里用到
{
	$("#inputLoc_window").window("open");
	$('#table_LocItem').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
	$('#table_LocItem').datagrid('options').queryParams={'Id':encodeURI($("#locMissionId").val())};
	$('#table_LocItem').datagrid('reload');

}