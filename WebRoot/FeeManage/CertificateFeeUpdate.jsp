<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>证书费用修改</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script type="text/javascript" src="../JScript/StatusInfo.js"></script>
	<script>
	$(function(){
		nowDate = new Date();
			//$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		/*$("#FeeAssignForm-FeeAllotee").combobox({
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
		});*/
		$('#Customer').combobox({
		//	url:'/jlyw/CustomerServlet.do?method=6',
			valueField:'id',
			textField:'name',
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
				$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}
		});
		var lastIndex;
		$('#table6').datagrid({
			    width:1005,
				height:300,
				title:'已完工确认的委托单',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'Status',title:'委托单状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
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
					//{field:'Appearance',title:'外观附件',width:80,align:'center'},
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
					//{field:'OtherRequirements',title:'其他要求',width:80,align:'center'},
					{field:'Location',title:'存放位置',width:80,align:'center'},
					{field:'Allotee',title:'派定人',width:80,align:'center'}
                ]],
				pagination:true,
				rownumbers:true,
				
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10){	//已注销
						return 'color:#FF0000'
					}
				},
				onSelect:function(rowIndex, rowData)
				{
					var Code=rowData.Code;
					$('#CommissionSheetId').val(rowData.Id);
										
					clickname="委托单号为'"+Code+"' 的费用分配信息";
					$('#fee_assign_table').datagrid({title:clickname});
					$('#fee_assign_table').datagrid('loadData',{total:0,rows:[]});
					$('#fee_assign_table').datagrid('options').url='/jlyw/CertificateFeeAssignServlet.do?method=6';
					$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':rowData.Id};
					$('#fee_assign_table').datagrid('reload');
					
					$('#OldcertificateFee').val($('#fee_assign_table').datagrid('getRows'));
				}
			});
		$('#fee_assign_table').datagrid({
			title:'费用修改',
			singleSelect:true, 
			height: 300,
//			width:800,
			nowrap: false,
			striped: true,
			showFooter:true,
			url:'/jlyw/CertificateFeeAssignServlet.do?method=6',
			remoteSort: false,
			idField:'CertificateFeeAssignId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					
					{field:'CertificateCode',title:'证书编号',width:70,sortable:true,align:'center'},
					{field:'CustomerName',title:'单位名称',width:120,sortable:true,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:70,sortable:true,align:'center'},
					{field:'FeeAlloteeName',title:'姓名',width:70,sortable:true,align:'center'},
					{field:'Quantity',title:'数量',width:40,sortable:true,align:'center'},
					{field:'OldTotalFee',title:'总费用',width:50,align:'center'},
					{field:'TotalFee',title:'总费用修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldTestFee',title:'检定费',width:50,align:'center'},
					{field:'TestFee',title:'检定费修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldRepairFee',title:'修理费',width:50,align:'center'},
					{field:'RepairFee',title:'修理费修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldMaterialFee',title:'材料费',width:50,align:'center'},
					{field:'MaterialFee',title:'材料费修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldCarFee',title:'交通费',width:50,align:'center'},
					{field:'CarFee',title:'交通费修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldDebugFee',title:'调试费',width:50,align:'center'},
					{field:'DebugFee',title:'调试费修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldOtherFee',title:'其它费',width:50,align:'center'},
					{field:'OtherFee',title:'其它费修改',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'Remark',title:'备注',width:300,align:'center',editor:{
						type:'text'
					}}
				]
			],
			onClickRow:function(rowIndex){
				$('#fee_assign_table').datagrid('endEdit', lastIndex);
				$('#fee_assign_table').datagrid('beginEdit', rowIndex);
				lastIndex = rowIndex;
				
			  var ed = $('#fee_assign_table').datagrid('getEditors', rowIndex);  
			  for (var i = 0; i < ed.length; i++){  
					var e = ed[i]; 
					var fieldName = e.field;
					if(fieldName == "TotalFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetTotalFeeChange(e.target, $(this));
						   }
						});
					}else if(fieldName == "TestFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetChildFeeChange("TestFee", e.target, $(this));
						   }
						});
					}else if(fieldName == "RepairFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetChildFeeChange("RepairFee", e.target, $(this));
						   }
						});
					}else if(fieldName == "MaterialFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetChildFeeChange("MaterialFee", e.target, $(this));
						   }
						});
					}else if(fieldName == "CarFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetChildFeeChange("CarFee", e.target, $(this));
						   }
						});
					}else if(fieldName == "DebugFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetChildFeeChange("DebugFee", e.target, $(this));
						   }
						});
					}else if(fieldName == "OtherFee"){
						$(e.target).bind("keydown" ,function(event){
						   if (event.keyCode=="13"){
								commissionSheetChildFeeChange("OtherFee", e.target, $(this));
								//compute($(this),e.target);  //需要进行的处理程序
						   }
						});
					}
			  }  
			},
			rownumbers:true	,
			pagination:false,
			toolbar:"#fee_assign_table-toolbar"
		});
		
		query();
	});
	
			
//获取一列的值
function getFieldValue(row, fieldName){
	if(fieldName=="TestFee"){
		return getInt(row.TestFee);
	}else if(fieldName == "RepairFee"){
		return getInt(row.RepairFee);
	}else if(fieldName == "MaterialFee"){
		return getInt(row.MaterialFee);
	}else if(fieldName == "CarFee"){
		return getInt(row.CarFee);
	}else if(fieldName == "DebugFee"){
		return getInt(row.DebugFee);
	}else if(fieldName == "OtherFee"){
		return getInt(row.OtherFee);
	}else if(fieldName=="TotalFee"){
		return getInt(row.TotalFee);
	}else if(fieldName=="OldTestFee"){
		return getInt(row.OldTestFee);
	}else if(fieldName == "OldRepairFee"){
		return getInt(row.OldRepairFee);
	}else if(fieldName == "OldMaterialFee"){
		return getInt(row.OldMaterialFee);
	}else if(fieldName == "OldCarFee"){
		return getInt(row.OldCarFee);
	}else if(fieldName == "OldDebugFee"){
		return getInt(row.OldDebugFee);
	}else if(fieldName == "OldOtherFee"){
		return getInt(row.OldOtherFee);
	}else if(fieldName == "OldTotalFee"){
		return getInt(row.OldTotalFee);
	}
	return 0;
}
//计算委托单分费用;row:指定行；fieldName：属性名；discount：折扣率
function doComputeCommissionSheetChildFee(row, fieldName, discount){	
	if(fieldName=="TestFee"){
		row.TestFee = getIntByRound(getFloat(row.OldTestFee)* getFloat(discount));
	}else if(fieldName == "RepairFee"){
		row.RepairFee = getIntByRound(getFloat(row.OldRepairFee)* getFloat(discount));
	}else if(fieldName == "MaterialFee"){
		row.MaterialFee = getIntByRound(getFloat(row.OldMaterialFee)* getFloat(discount));
	}else if(fieldName == "CarFee"){
		row.CarFee = getIntByRound(getFloat(row.OldCarFee)* getFloat(discount));
	}else if(fieldName == "DebugFee"){
		row.DebugFee = getIntByRound(getFloat(row.OldDebugFee)* getFloat(discount));
	}else if(fieldName == "OtherFee"){
		row.OtherFee = getIntByRound(getFloat(row.OldOtherFee)* getFloat(discount));
	}
}
//计算总费用（委托单或者汇总的总费用）
function doComputeTotalFee(row){
	row.TotalFee = getInt(row.TestFee)+getInt(row.RepairFee)+getInt(row.MaterialFee)+getInt(row.CarFee)+getInt(row.DebugFee)+getInt(row.OtherFee);
}
//计算总费用（原价，委托单或者汇总的总费用）
function doComputeTotalFeeOld(row){
	row.OldTotalFee = getInt(row.OldTestFee)+getInt(row.OldRepairFee)+getInt(row.OldMaterialFee)+getInt(row.OldCarFee)+getInt(row.OldDebugFee)+getInt(row.OldOtherFee);
}
//计算汇总的分费用
function doComputeChildFeeTotal(fieldName){
	var rows = $("#fee_assign_table").datagrid("getRows");
	var tempFee = 0;
	for(var i = 0; i<rows.length; i++){
		if(fieldName=="TestFee"){
			tempFee += getInt(rows[i].TestFee);
		}else if(fieldName == "RepairFee"){
			tempFee += getInt(rows[i].RepairFee);
		}else if(fieldName == "MaterialFee"){
			tempFee += getInt(rows[i].MaterialFee);
		}else if(fieldName == "CarFee"){
			tempFee += getInt(rows[i].CarFee);
		}else if(fieldName == "DebugFee"){
			tempFee += getInt(rows[i].DebugFee);
		}else if(fieldName == "OtherFee"){
			tempFee += getInt(rows[i].OtherFee);
		}else if(fieldName=="OldTestFee"){
			tempFee += getInt(rows[i].OldTestFee);
		}else if(fieldName == "OldRepairFee"){
			tempFee += getInt(rows[i].OldRepairFee);
		}else if(fieldName == "OldMaterialFee"){
			tempFee += getInt(rows[i].OldMaterialFee);
		}else if(fieldName == "OldCarFee"){
			tempFee += getInt(rows[i].OldCarFee);
		}else if(fieldName == "OldDebugFee"){
			tempFee += getInt(rows[i].OldDebugFee);
		}else if(fieldName == "OldOtherFee"){
			tempFee += getInt(rows[i].OldOtherFee);
		}
	}
	return tempFee;	
}


/**
*  表格编辑框监听
* @param fieldName:属性名
* @param preValueObj:修改之前的值的对象
* @param newValueObj:修改之后的值的对象
*/
function commissionSheetChildFeeChange(fieldName, preValueObj, newValueObj)//委托单分费用改变时
{
	var newValue = getFloat($(newValueObj).val());
	if(newValue < 0){
		$.messager.alert('警告','您输入的值不能小于0！','warning');
		return false;
	}
	$('#fee_assign_table').datagrid('acceptChanges');
	var row = $("#fee_assign_table").datagrid("getSelected");
	var index=$("#fee_assign_table").datagrid("getRowIndex",row);
	
	if(newValue<= 1 && newValue>= 0){
		doComputeCommissionSheetChildFee(row, fieldName, newValue);	//计算折扣率后的费用
	}	
  
	doComputeTotalFee(row);	//计算该行的总费用
	$('#fee_assign_table').datagrid('updateRow',{
		index: index,
		row:row
	});
	
	return true;
 
}
/**
*  表格编辑框监听
* @param preValueObj:修改之前的值的对象
* @param newValueObj:修改之后的值的对象
*/
function commissionSheetTotalFeeChange(preValueObj, newValueObj)//委托单总费用改变时
{
	var newValue = getFloat($(newValueObj).val());
	if(newValue < 0){
		$.messager.alert('警告','您输入的值不能小于0！','warning');
		return false;
	}
	$('#fee_assign_table').datagrid('acceptChanges');
	var row = $("#fee_assign_table").datagrid("getSelected");
	var index=$("#fee_assign_table").datagrid("getRowIndex",row);	
	
	var discount = 0.0;	//折扣率
	if(newValue<= 1 && newValue>= 0){
		discount = newValue;
	}else{
		var oldTotalFee = getFieldValue(row, "OldTotalFee");
		if(oldTotalFee == 0){
			discount = 0.0;
		}else{
			discount = (newValue*1.0)/oldTotalFee;
		}
	}
	doComputeCommissionSheetChildFee(row, "TestFee", discount);	//计算折扣率后的费用
	doComputeCommissionSheetChildFee(row, "RepairFee", discount);	//计算折扣率后的费用
	doComputeCommissionSheetChildFee(row, "MaterialFee", discount);	//计算折扣率后的费用
	doComputeCommissionSheetChildFee(row, "CarFee", discount);	//计算折扣率后的费用
	doComputeCommissionSheetChildFee(row, "DebugFee", discount);	//计算折扣率后的费用
	doComputeCommissionSheetChildFee(row, "OtherFee", discount);	//计算折扣率后的费用	
  
	doComputeTotalFee(row);	//计算该行的总费用
	$('#fee_assign_table').datagrid('updateRow',{
		index: index,
		row:row
	});
	
	return true;
}
	function query(){
		$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=4';
		$('#table6').datagrid('options').queryParams={'Code':encodeURI($('#Code').val()),'CustomerId':encodeURI($('#Customer').combobox('getValue')),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue')),'Status':"3"};
		$('#table6').datagrid('reload');
	}
	
	
	/******************         产值分配相关函数             ***********************/
	/*function doDelFeeAssignRecord(){	//删除所选的费用记录
		var result = confirm("您确定要删除所选费用记录吗？");
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
	function doAddAnAllotee(){	//添加一个费用分配人
		if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==""){
			var result = confirm("确定费用所分配人员为空吗？");
			if(result == false){
				return false;
			}
		}
		var rows = $('#fee_assign_table').datagrid('getRows');
		for(var i = 0; i<rows.length; i++){
			if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==rows[i].FeeAlloteeName){
				$.messager.alert('提示！','已存在:'+ $('#FeeAssignForm-FeeAllotee').combobox('getValue') + '的产值分配，不能重复添加！' ,'info');
				return false;
			}
		}
		$('#fee_assign_table').datagrid('insertRow', {
			index:rows.length,
			row:{
				FeeAlloteeName:$('#FeeAssignForm-FeeAllotee').combobox('getValue'),
				TestFee:getInt(0),
				RepairFee:getInt(0),
				MaterialFee:getInt(0),
				CarFee:getInt(0),
				DebugFee:getInt(0),
				OtherFee:getInt(0),
				Remark:''
			}
		});
	}*/
	function doFeeAssign(){	//确认提交费用分配
		$("#FeeAssignForm").form('submit', {
			url:'/jlyw/CertificateFeeAssignServlet.do?method=5',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;	
						
				$('#fee_assign_table').datagrid('acceptChanges');
				try{
					var row = $('#fee_assign_table').datagrid('getSelected');
					var index=$("#fee_assign_table").datagrid("getRowIndex",row);
					doComputeTotalFee(row);	//计算该行的总费用
					$('#fee_assign_table').datagrid('updateRow',{
						index: index,
						row:row
					});
				}catch(ex){}
				
				var rows = $("#fee_assign_table").datagrid("getRows");						
				$('#FeeAssignForm-FeeAssignInfo').val(JSON.stringify(rows));
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$.messager.alert('提示！','费用修改成功！','info');
					$('#certificateFee').val(JSON.stringify($('#fee_assign_table').datagrid('getRows')));

					$('#fee_assign_table').datagrid('reload');
				}else{
					$.messager.alert('提交失败！',result.msg,'error');
				}
			}
		});
	}
	function printUpdateFee(){
		if($('#CommissionSheetId').val() == ''||$('#CommissionSheetId').val().length==0){
				$.messager.alert('提示','请选择改费的委托单','error');
		}
		else{			
			$('#formLook').submit();
		}
	}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="证书费用修改" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
			<form id="SearchForm" method="post" >
				<table width="980px" id="table1">
				<tr>
					<td  width="99%" align="left">
						委托单号：<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;"/>&nbsp;
						委托单位：<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;"/>&nbsp;
						起始时间：<input name="DateFrom" id="DateFrom" type="text" style="width:90px;" class="easyui-datebox" />&nbsp;
						结束时间：<input name="DateEnd" id="DateEnd" type="text" style="width:90px;" class="easyui-datebox" />&nbsp;&nbsp;&nbsp;&nbsp;
						
						<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a>
					</td>
				</tr>				
				</table>
			</form>

		</div>
		 <div style="width:1005px;height:300px;">
	    	 <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	    </div>
		<table id="fee_assign_table" iconCls="icon-tip" width="1005px"></table>
		<br />
		<form id="FeeAssignForm" method="post">
			
			<input type="hidden" name="FeeAssignInfo" id="FeeAssignForm-FeeAssignInfo" />
		</form>
		
<div id="fee_assign_table-toolbar" style="padding:2px;">
		<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
				    <td style="text-align:left;padding-left:2px"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" href="javascript:void(0)" onClick="doFeeAssign()">保存费用分配</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-print" plain="true" href="javascript:void(0)" onClick="printUpdateFee()">打印改费单</a><!--<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" href="javascript:void(0)" onClick="doDelFeeAssignRecord()">删除</a>-->
					</td>
					<!--<td style="text-align:right;padding-right:2px">
					选择员工：<select name="FeeAllotee" id="FeeAssignForm-FeeAllotee" style="width:152px"></select>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" plain="true" onClick="doAddAnAllotee()">新增费用分配</a>
					</td>-->
				</tr>
		</table>			
</div>
<form id="formLook" method="post" action="/jlyw/FeeManage/FeeChangePrint.jsp" target="FeePrintFrame" >			
		<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" />
		
		<input type="hidden" name="certificateFee" id="certificateFee" />
</form>
<iframe id="FeePrintFrame" name="FeePrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
</DIV></DIV>
</body>
</html>
