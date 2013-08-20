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
			
			$("#QuotationId").combobox('reload','/jlyw/QuotationServlet.do?method=11&QueryName='+encodeURI(record.name));	//报价单查询
			
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
	
	$("#ApplianceSpeciesName").combobox({
		valueField:'Name',
		textField:'Name',
		onSelect:function(record){
			
			$("#SpeciesType").val(record.SpeciesType);		//器具分类类型
			$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//器具分类ID（或者是标准名称ID）
/*			$("#ApplianceName").combobox('loadData',record.ApplianceName);	//器具名称
			if(record.ApplianceName.length == 1){
				$("#ApplianceName").combobox('select',record.ApplianceName[0].name);
			}else{
				$("#ApplianceName").combobox('clear');
			}
			$("#Model").combobox('loadData',record.Model);	//型号规格
			$("#Range").combobox('loadData',record.Range);	//测量范围
			$("#Accuracy").combobox('loadData',record.Accuracy);	//精度等级
			$("#Manufacturer").combobox('loadData',record.Manufacturer);	//制造厂商
			$("#Allotee").combobox('loadData',record.Allotee);	//检验人员*/
			$("#ApplianceName").combobox('clear');
			$("#Model").combobox('clear');
			$("#Range").combobox('clear');
			$("#Accuracy").combobox('clear');
			$("#Manufacturer").combobox('clear');
			$("#Allotee").combobox('clear');
			if(record.SpeciesType == 0 || record.SpeciesType == '0' || record.SpeciesType == 2 || record.SpeciesType == '2'){	//标准名称Or常用名称
				if(record.PopName == null || record.PopName.length == 0){
					$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);
				}else{
					$("#ApplianceName").combobox('setValue',record.PopName);
				}
				$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//型号规格
				$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//测量范围
				$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//精度等级
				$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//制造厂商
			}else if(record.SpeciesType == 1 || record.SpeciesType == '1'){	//分类名称
				$("#ApplianceName").combobox('loadData',[]);
				$("#Model").combobox('loadData',[]);	//型号规格
				$("#Range").combobox('loadData',[]);	//测量范围
				$("#Accuracy").combobox('loadData',[]);	//精度等级
				$("#Manufacturer").combobox('loadData',[]);	//制造厂商
			}
			$("#Allotee").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//检验人员
			
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
			$("#SpeciesType").val('');		//器具分类类型
			$("#ApplianceSpeciesId").val('');	//器具分类ID（或者是标准名称ID）
			
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
			{field:'CommissionNumber',title:'委托单号',width:80,align:'center'},
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
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var result = confirm("您确定要移除这些器具吗？");
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
			text:'注销委托单',
			iconCls:'icon-cancel',
			handler:function(){
				var rows = $("#table5").datagrid("getSelections");
				if(rows.length == 0){
					$.messager.alert('提示',"请选择要注销的委托单！",'info');
					return false;
				}
				if(rows.length > 1){
					$.messager.alert('提示',"一次最多只能注销一张委托单！",'info');
					return false;
				}
				if(rows[0].CommissionNumber == null || rows[0].CommissionNumber.length == 0){
					$.messager.alert('提示',"所选器具未生成委托单，不能注销！",'info');
					return false;
				}
				var result = confirm("您确定要注销单号为 '"+rows[0].CommissionNumber+"' 的委托单吗？");
				if(result == false){
					return false;
				}
				$.ajax({
						type: "post",
						url: "/jlyw/CommissionSheetServlet.do?method=2",
						data: {"Code":rows[0].CommissionNumber,"Pwd":rows[0].PrintObj.Pwd},
						dataType: "json",	//服务器返回数据的预期类型
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
			text:'选择未保存的记录',
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
			text:'清空',
			iconCls:'icon-undo',
			handler:function(){
				var result = confirm("确定要清空数据吗？");
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
			
			var rowData = null;	//查找界面上第一个勾选的项
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
			
			var appSpeComboData = {   //器具授权名称
				SpeciesType:rowData.SpeciesType,
				ApplianceSpeciesId:rowData.ApplianceSpeciesId,
				Name:rowData.ApplianceSpeciesName,
				PopName:rowData.ApplianceName
			};	
			$("#ApplianceSpeciesName").combobox('loadData',[appSpeComboData]);
			$("#ApplianceSpeciesName").combobox('setValue', rowData.ApplianceSpeciesName);
			$("#SpeciesType").val(rowData.SpeciesType);	//隐藏的ID
			$("#ApplianceSpeciesId").val(rowData.ApplianceSpeciesId);	//隐藏的ID
			$("#ApplianceName").combobox('setValue',rowData.ApplianceName);	//器具名称
			
			$("#ApplianceCode").val(rowData.ApplianceCode);	//出厂编号
			$('#AppManageCode').val(rowData.AppManageCode);	//管理编号
			$('#Model').combobox('setValue', rowData.Model);	//型号规格
			$('#Range').combobox('setValue', rowData.Range);	//测量范围
			$('#Accuracy').combobox('setValue', rowData.Accuracy);	//精度等级			
			$('#Manufacturer').combobox('setValue', rowData.Manufacturer);	//制造厂
			$('#Quantity').val(rowData.Quantity);	//器具数量
			$('#Mandatory').val(rowData.MandatoryInspection);	//是否强检
			
			if(rowData.Urgent == 0 || rowData.Urgent == '0'){	//是否加急
				$("#Ness").attr("checked",true);	//勾选
			}else{
				$("#Ness").attr("checked",false);	//去勾选
			}
			
			if(rowData.Trans == 0 || rowData.Trans == '0'){		//是否转包
				$("#Trans").attr("checked",true);	//勾选
			}else{
				$("#Trans").attr("checked",false);	//去勾选
			}
			$("#Appearance").val(rowData.Appearance);	//外观附件
			$("#Repair").val(rowData.Repair);	//需修理否
			$("#ReportType").val(rowData.ReportType);	//报告形式
			$("#SubContractor").combobox('setValue', rowData.SubContractor);	//转包方
			$("#OtherRequirements").val(rowData.OtherRequirements);	//其他要求
			$("#Location").val(rowData.Location);	//存放位置
			$("#Allotee").combobox('setValue', rowData.Allotee);	//派定人
						
			if(rowData.SpeciesType == 0 || rowData.SpeciesType == '0' || rowData.SpeciesType == 2 || rowData.SpeciesType == '2'){	//标准名称Or常用名称
				if(rowData.ApplianceName == null || rowData.ApplianceName.length == 0){
					$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);
				}else{
					$("#ApplianceName").combobox('setValue',rowData.ApplianceName);
				}
				$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//型号规格
				$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//测量范围
				$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//精度等级
				$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//制造厂商
			}else if(rowData.SpeciesType == 1 || rowData.SpeciesType == '1'){	//分类名称
				$("#ApplianceName").combobox('loadData',[]);
				$("#Model").combobox('loadData',[]);	//型号规格
				$("#Range").combobox('loadData',[]);	//测量范围
				$("#Accuracy").combobox('loadData',[]);	//精度等级
				$("#Manufacturer").combobox('loadData',[]);	//制造厂商
			}
			$("#Allotee").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//检验人员
			
		}

	});
	

	$('#table6').datagrid({
		width:1000,
		height:500,
		title:'委托单位历史检验记录',
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
			{field:'CustomerName',title:'委托单位',width:120,align:'center'},
			{field:'CommissionCode',title:'委托单号',width:80,align:'center'},
			{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
			{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
			{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
			{field:'Model',title:'型号规格',width:80,align:'center'},
			{field:'Range',title:'测量范围',width:80,align:'center'},
			{field:'Accuracy',title:'精度等级',width:80,align:'center'},
			{field:'Manufacturer',title:'制造厂商',width:80,align:'center'},
			{field:'Quantity',title:'台/件数',width:70,align:'center'},
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
			{field:'Location',title:'存放位置',width:80,align:'center'},
			{field:'Allotee',title:'派定人',width:80,align:'center'},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center'}
		]],
		pagination:true,
		rownumbers:true,
		toolbar:"#table6-search-toolbar"
	});
	
	
	//报价单
	$('#table_QuoItem').datagrid({
		//width:800,
		//height:300,
		//title:"报价单条目",
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
			{field:'Id',title:'编号',width:40,align:'center'},
			{field:'StandardNameId',title:'受检器具标准名称Id',width:120,align:'center'},
			{field:'StandardName',title:'受检器具标准名称',width:120,align:'center',sortable:true},
			{field:'CertificateName',title:'受检器具证书名称',width:120,align:'center'},
			{field:'Model',title:'型号规格',width:80,align:'center'},
			{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,align:'center'},
			{field:'Range',title:'测量范围',width:80,align:'center'},
			{field:'Quantity',title:'台件数',width:80,align:'center',editor:'text'},
			{field:'AppFactoryCode',title:'出厂编号',width:80,align:'center'},
			{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
			{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
			{field:'CertType',title:'证书类型',width:80,align:'center',sortable:true,
				formatter:function(value,rowData,rowIndex){
					if(value=='1'){
						rowData['CertType']="1";
						return "检定";
					}
					else if(value == '4'){
						rowData['CertType']="4";
						return "检验";
					}
					else if(value == '2'){
						rowData['CertType']="2";
						return "校准";
					}
					else{
						rowData['CertType']="3";
						return "检测";
					}
				}
			},
			{field:'SiteTest',title:'现场检测',width:80,align:'center',sortable:true,
				formatter:function(value,rowData,rowIndex){
					if(value=='0'){
						rowData['SiteTest'] ="0";
						return "是";
					}
					else{
						rowData['SiteTest'] ="1";
						return "否";
					}
				}
			},
			{field:'MinCost',title:'检测费最少',width:80,align:'center',
				formatter:function(val,rec){
				
					return '<span style="color:red;">'+val+'</span>';
				
			}},
			{field:'MaxCost',title:'检测费最多',width:80,align:'center',
				formatter:function(val,rec){
				
					return '<span style="color:red;">'+val+'</span>';
				
			}},
			{field:'Remark',title:'备注',width:80,align:'center'}
		]],
		rownumbers:false,
		pagination:false,
		toolbar:[{
			text:'导入所选器具',
			iconCls:'icon-add',
			handler:function(){
				var rows = $("#table_QuoItem").datagrid("getSelections");				
				if(rows.length == 0){
					$.messager.alert('提示','请先选择要导入的报价单条目！','info');
					return false;
				}
				var index = $('#table5').datagrid('getRows').length;
				for(var i=0;i<rows.length;i++){	
					if(typeof(rows[i].StandardNameId) != "undefined" && rows[i].StandardNameId != ""){
						$('#table5').datagrid('insertRow',{
							index:index,
							row:{
								Id:rand(1000000),	//6位随机数
								SpeciesType:0,	//标准名称
								ApplianceSpeciesId:rows[i].StandardNameId,
								ApplianceSpeciesName:rows[i].StandardName+"[标]",
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
								Appearance:'外观正常，无附件。',
								Repair:1,
								ReportType:rows[i].CertType,
								OtherRequirements:'无',
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
	$("#QuotationId").combobox({ //报价单号
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
function doSubmitFormNoPrint(t)		//保存委托单，不打印
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
function doLoadHistoryCommission()
{
	if($('#CustomerName').combobox('getValue').length==0){
		$.messager.alert('提示','请输入委托单位的名称！','info');
		return false;
	}
	$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=1';
	$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#History_BeginDate').datebox('getValue'),'EndDate':$('#History_EndDate').datebox('getValue')};
	$('#table6').datagrid('reload');
}
function UpdateRecord()	//更新一条检验的器具
{
	var rows = $('#table5').datagrid('getSelections');
	if(rows.length == 0){
		$.messager.alert("提示","请在‘本次检验的器具’中选择一个需要更新的器具！","info");
		return false;
	}
	if(rows.length > 1){
		$.messager.alert("提示","只能在‘本次检验的器具’中选择一个需要更新的器具！","info");
		return false;
	}
	if(rows[0].CommissionNumber != null && rows[0].CommissionNumber.length > 0){
		var result = confirm("器具 '"+rows[0].ApplianceSpeciesName+"' 已经生成过委托单了，您确定要更新吗？");
		if(result == false){
			return false;
		}
	}
	if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
		$.messager.alert("提示","请选择一个有效的‘器具授权名’！","info");
		return false;
	}
	if($("#Quantity").val()==''){
		$.messager.alert("提示","请输入‘器具数量’！","info");
		return false;
	}
	if($('#Mandatory').val()==''){
		$.messager.alert("提示","请选择‘是否强检’！","info");
		return false;
	}
	if($('#Repair').val()==''){
		$.messager.alert("提示","请选择‘需修理否’！","info");
		return false;
	}
	if($('#ReportType').val()==''){
		$.messager.alert("提示","请选择‘报告形式’！","info");
		return false;
	}
	if($('#Quantity').val()<0){
		$.messager.alert("提示","器具数量不能小于零！","info");
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
		$.messager.alert("提示","请选择一个有效的‘器具授权名’！","info");
		return false;
	}
	if($("#Quantity").val()==''){
		$.messager.alert("提示","请输入‘器具数量’！","info");
		return false;
	}
	if($('#Mandatory').val()==''){
		$.messager.alert("提示","请选择‘是否强检’！","info");
		return false;
	}
	if($('#Repair').val()==''){
		$.messager.alert("提示","请选择‘需修理否’！","info");
		return false;
	}
	if($('#ReportType').val()==''){
		$.messager.alert("提示","请选择‘报告形式’！","info");
		return false;
	}
	if($('#Quantity').val()<0){
		$.messager.alert("提示","器具数量不能小于零！","info");
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

	var index = $('#table5').datagrid('getRows').length;	//在最后一行新增记录
	$('#table5').datagrid('insertRow', {
		index: index,
		row:{
			Id:rand(1000000),	//6位随机数
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
	var index = $('#table5').datagrid('getRows').length;	//在最后一行新增记录
	
	for(var i=0; i<rows.length; i++){
		if(rows[i].ApplianceSpeciesNameStatus != 0){
			$.messager.alert('提示',"授权名称 '"+rows[i].ApplianceSpeciesName+"' 已过时，不可添加！",'info');
			continue;
		}
		$('#table5').datagrid('insertRow', {
			index: index,
			row:{
				Id:rand(1000000),	//6位随机数
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

//验证报价单是否有效
function doCheckQuotationValid(){
	$.ajax({
			type: "post",
			url: "/jlyw/QuotationServlet.do?method=6",
			data: {"QuotationId":$('#QuotationId').val()},
			dataType: "json",	//服务器返回数据的预期类型
			beforeSend: function(XMLHttpRequest){
				if($('#QuotationId').val().length == 0){
					$.messager.alert('提醒!','请先输入报价单号！','info');
					return false;
				}
			},
			success: function(data, textStatus){
				if(data.IsOK){
					if(data.IsValid){
						$.messager.alert('验证结果','该报价单号有效！','info');
					}else{
						$.messager.alert('验证结果','该报价单号无效！','warning');
					}
				}else{
					$.messager.alert('验证失败！',data.msg,'error');
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

function searchQuoItem()//查询报价单条目
{
	$("#inputQuo_window").window("open");
	$('#table_QuoItem').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
	$('#table_QuoItem').datagrid('options').queryParams={'quotationId':encodeURI($('#QuotationId').combobox('getValue'))};
	$('#table_QuoItem').datagrid('reload');

}
function searchLocMission()//查询现场委托书中的器具:新建现场委托单里用到
{
	$("#inputLoc_window").window("open");
	$('#table_LocItem').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
	$('#table_LocItem').datagrid('options').queryParams={'Id':encodeURI($("#locMissionId").val())};
	$('#table_LocItem').datagrid('reload');

}

function doPrintLabel(){  //打开打印标签窗口
	var rows = $("#table5").datagrid("getSelections");	
	if(rows.length == 0){
		$.messager.alert('提示',"请选择要打印的委托单！",'info');
		return false;
	}
	if(rows.length>1){
		$.messager.alert('提示',"只能选择打印一份委托单的器具标签！",'info');
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


function doPrintLabelSubmit(){  //打印标签
	if(!$("#LabelPrint_form").form('validate'))
		return false ;
	var rows = $("#table5").datagrid("getSelections");	
	
	if(getInt($('#RangeFrom').val())<1||getInt($('#RangeFrom').val())>getInt($('#RangeEnd').val())||getInt($('#RangeEnd').val())>rows[0].PrintObj.Quantity){
		$.messager.alert('提示',"打印器具范围不正确！",'info');
		return false;
	}
	if(getInt($('#RangeFrom').val())==getInt($('#RangeEnd').val())&&getInt($('#AttachmentNum').val())>1){
		
	}
				
	var result = confirm("您确定要进行此操作吗？");
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

function doSubmitFormYL()		//预留委托单
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=16',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("提示","请选择一个有效的‘器具授权名’！","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("提示","请输入‘器具数量’！","info");
				return false;
			}
			if($('#Mandatory').val()==''){
				$.messager.alert("提示","请选择‘是否强检’！","info");
				return false;
			}
			if($('#Repair').val()==''){
				$.messager.alert("提示","请选择‘需修理否’！","info");
				return false;
			}
			if($('#ReportType').val()==''){
				$.messager.alert("提示","请选择‘报告形式’！","info");
				return false;
			}
			if($('#Quantity').val()<0){
				$.messager.alert("提示","器具数量不能小于零！","info");
				return false;
			}
		
			return $("#CommissionSheetForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#YLCommissionSheetList").val(JSON.stringify(result.CommissionSheetList));
				var result = confirm("预留成功，是否开始打印？");
				if(result == false){
					return false;
				}
				printSubmitFormYL();
				
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});
}
function printSubmitFormYL(){
	if($('#YLCommissionSheetList').val() == ''||$('#YLCommissionSheetList').val().length==0){
		$.messager.alert('提示','还未预留委托单！','error');
	}
	else{			
		$('#formLook').submit();
	}
}
