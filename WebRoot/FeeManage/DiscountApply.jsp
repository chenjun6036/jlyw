<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>委托单折扣</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script>
		$(function(){
			nowDate = new Date();
			//$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
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
				
			$('#table6').datagrid({
			    width:1000,
				height:300,
				title:'已完工确认的委托单',
//				iconCls:'icon-save',
				singleSelect:false, 
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
				toolbar:[{
					text:'提交选中的委托单',
					iconCls:'icon-save',
					handler:function(){
						AddRecordFromHistory();
					}
				}],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10){	//已注销
						return 'color:#FF0000'
					}
				}
			});
			var lastIndex;
			$('#table5').datagrid({
				title:'选中的委托单信息',
				width:1000,
				height:300,
				fit:true,
				showFooter:true,
				singleSelect:true, 
				nowrap: false,
				striped: true,
				sortName: 'ApplianceName',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'委托单位',width:120,align:'center',sortable:true},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
					{field:'Quantity',title:'台/件数',width:70,align:'center'},
					{field:'OldTotalFee',title:'总费用',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TotalFee',title:'总费用折扣',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldTestFee',title:'检测费',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TestFee',title:'检测费折扣',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldRepairFee',title:'维修费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'RepairFee',title:'维修费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldMaterialFee',title:'材料费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'MaterialFee',title:'材料费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldCarFee',title:'交通费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'CarFee',title:'交通费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldDebugFee',title:'调试费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'DebugFee',title:'调试费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldOtherFee',title:'其他费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OtherFee',title:'其他费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					}
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("您确定要移除这些委托单信息吗？");
						if(result == false){
							return false;
						}
						var rows = $('#table5').datagrid('getSelections');
						var length = rows.length;
						for(var i=length-1; i>=0; i--){
							var index = $('#table5').datagrid('getRowIndex', rows[i]);
							$('#table5').datagrid('deleteRow', index);
						}
						doCountFeeAndUpdate();
						
					}
				},'-',{
					text:'折扣申请',
					iconCls:'icon-save',
					handler:function(){
						$('#table5').datagrid('acceptChanges');
						try{
							var row = $('#table5').datagrid('getSelected');
							var index=$("#table5").datagrid("getRowIndex",row);
							doComputeTotalFee(row);	//计算该行的总费用
							$('#table5').datagrid('updateRow',{
								index: index,
								row:row
							});
							doCountFeeAndUpdate();
						}catch(ex){}
						
						//$('#FeeCount').datagrid('rejectChanges');
						$('#apply-window').window('open');
					}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
						$('#table5').datagrid('acceptChanges');
						var rows = $('#table5').datagrid('getRows');
						try{
							var row = rows[lastIndex];
							var index=$("#table5").datagrid("getRowIndex",row);
							doComputeTotalFee(row);	//计算该行的总费用
							$('#table5').datagrid('updateRow',{
								index: index,
								row:row
							});
							doCountFeeAndUpdate();
						}catch(ex){}
						
						$('#table5').datagrid('beginEdit', rowIndex);
						lastIndex = rowIndex;
					
					  var ed = $('#table5').datagrid('getEditors', rowIndex);  
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
				}
			});
				
			var lastIndex_FeeCount;
			$('#FeeCount').datagrid({
				//title:'选中的委托单信息',
				width:1000,
				height:300,
				fit:true,
				showFooter:true,
				singleSelect:true, 
				nowrap: false,
				striped: true,
				
				remoteSort: false,
			
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'OldTotalFee',title:'总费用',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TotalFee',title:'总费用折扣',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldTestFee',title:'检测费',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TestFee',title:'检测费折扣',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldRepairFee',title:'维修费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'RepairFee',title:'维修费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldMaterialFee',title:'材料费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'MaterialFee',title:'材料费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldCarFee',title:'交通费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'CarFee',title:'交通费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldDebugFee',title:'调试费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'DebugFee',title:'调试费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldOtherFee',title:'其他费',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OtherFee',title:'其他费折扣',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					}			
		
				]],
				pagination:false,
				rownumbers:true,
				
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
//					if (lastIndex != rowIndex){
						$('#FeeCount').datagrid('endEdit', lastIndex_FeeCount);
						$('#FeeCount').datagrid('beginEdit', rowIndex);
		//					}
						lastIndex_FeeCount = rowIndex;
					
					  var ed = $('#FeeCount').datagrid('getEditors', rowIndex);  
					  for (var i = 0; i < ed.length; i++){  
						var e = ed[i];
						
						var fieldName = e.field;
						if(fieldName == "TotalFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("TotalFee", e.target, $(this));
							   }
							});
						}else if(fieldName == "TestFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("TestFee", e.target, $(this));
							   }
							});
						}else if(fieldName == "RepairFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("RepairFee", e.target, $(this));
							   }
							});
						}else if(fieldName == "MaterialFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("MaterialFee", e.target, $(this));
							   }
							});
						}else if(fieldName == "CarFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("CarFee", e.target, $(this));
							   }
							});
						}else if(fieldName == "DebugFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("DebugFee", e.target, $(this));
							   }
							});
						}else if(fieldName == "OtherFee"){
							$(e.target).bind("keydown" ,function(event){
							   if (event.keyCode=="13"){
									feeCountChange("OtherFee", e.target, $(this));
							   }
							});
						}
				      }  
				}
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
	var rows = $("#table5").datagrid("getRows");
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

//汇总委托单的各项费用并更新至表格
function doCountFeeAndUpdate(){
	$('#FeeCount').datagrid('acceptChanges');
	var CountFeeRow = {};
	CountFeeRow.TestFee = doComputeChildFeeTotal("TestFee");
	CountFeeRow.RepairFee = doComputeChildFeeTotal("RepairFee");
	CountFeeRow.MaterialFee = doComputeChildFeeTotal("MaterialFee");
	CountFeeRow.CarFee = doComputeChildFeeTotal("CarFee");
	CountFeeRow.DebugFee = doComputeChildFeeTotal("DebugFee");
	CountFeeRow.OtherFee = doComputeChildFeeTotal("OtherFee");
	
	CountFeeRow.OldTestFee = doComputeChildFeeTotal("OldTestFee");
	CountFeeRow.OldRepairFee = doComputeChildFeeTotal("OldRepairFee");
	CountFeeRow.OldMaterialFee = doComputeChildFeeTotal("OldMaterialFee");
	CountFeeRow.OldCarFee = doComputeChildFeeTotal("OldCarFee");
	CountFeeRow.OldDebugFee = doComputeChildFeeTotal("OldDebugFee");
	CountFeeRow.OldOtherFee = doComputeChildFeeTotal("OldOtherFee");
	doComputeTotalFee(CountFeeRow);	//分费用相加——现价
	doComputeTotalFeeOld(CountFeeRow);	//分费用相加——原价
	
	$('#FeeCount').datagrid('loadData',{total:0,rows:[]});
	$('#FeeCount').datagrid('insertRow', {
		index: 0,
		row:CountFeeRow
	});
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
	$('#table5').datagrid('acceptChanges');
	var row = $("#table5").datagrid("getSelected");
	var index=$("#table5").datagrid("getRowIndex",row);
	
	if(newValue<= 1 && newValue>= 0){
		doComputeCommissionSheetChildFee(row, fieldName, newValue);	//计算折扣率后的费用
	}	
  
	doComputeTotalFee(row);	//计算该行的总费用
	$('#table5').datagrid('updateRow',{
		index: index,
		row:row
	});
	doCountFeeAndUpdate();
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
	$('#table5').datagrid('acceptChanges');
	var row = $("#table5").datagrid("getSelected");
	var index=$("#table5").datagrid("getRowIndex",row);	
	
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
	$('#table5').datagrid('updateRow',{
		index: index,
		row:row
	});
	doCountFeeAndUpdate();
	return true;
}
/**
*  表格编辑框监听
* @param fieldName:属性名
* @param preValueObj:修改之前的值的对象
* @param newValueObj:修改之后的值的对象
*/
function feeCountChange(fieldName,preValueObj, newValueObj)//委托单汇总的费用改变时
{
	var newValue = getFloat($(newValueObj).val());
	if(newValue < 0){
		$.messager.alert('警告','您输入的值不能小于0！','warning');
		return false;
	}
//	alert("feeCountChange   "+newValue+"   "+fieldName);
	$('#FeeCount').datagrid('acceptChanges');
	var row = $("#FeeCount").datagrid("getSelected");
	var index=$("#FeeCount").datagrid("getRowIndex",row);
	
	
	var discount = 0.0;	//折扣率
	if(newValue<= 1 && newValue>= 0){
		discount = newValue;
	}else{
		var oldFee = getFieldValue(row, "Old"+fieldName);
		if(oldFee == 0){
			discount = 0.0;
		}else{
			discount = (newValue*1.0)/oldFee;
		}
	}
	
	//更新所有委托单的
	var rows = $("#table5").datagrid("getRows");
	for(var i = 0; i<rows.length; i++){
		var tempRow = rows[i];
		var tempIndex=$("#table5").datagrid("getRowIndex",tempRow);
		if(fieldName == "TestFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "TestFee", discount);	//计算折扣率后的费用
		}
		if(fieldName == "RepairFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "RepairFee", discount);	//计算折扣率后的费用
		}
		if(fieldName == "MaterialFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "MaterialFee", discount);	//计算折扣率后的费用
		}
		if(fieldName == "CarFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "CarFee", discount);	//计算折扣率后的费用
		}
		if(fieldName == "DebugFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "DebugFee", discount);	//计算折扣率后的费用
		}
		if(fieldName == "OtherFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "OtherFee", discount);	//计算折扣率后的费用
		}
		doComputeTotalFee(tempRow);	//计算该行的总费用
		$('#table5').datagrid('updateRow',{
			index: tempIndex,
			row:tempRow
		});
		
	}
	
	doCountFeeAndUpdate();
	return true;
}
function query(){
	$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=4';
	$('#table6').datagrid('options').queryParams={'Code':encodeURI($('#Code').val()),'CustomerId':encodeURI($('#Customer').combobox('getValue')),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue')),'Status':"3"};
	$('#table6').datagrid('reload');
}
function AddRecordFromHistory(){	//添加一张委托单
	var rows = $('#table6').datagrid('getSelections');
	
	var row=$('#table5').datagrid('getRows');
	var index=row.length;
	var bStopRunning = false;
	for(var i=0; i<rows.length; i++){
		var j;		
		for(j=0; j<index; j++){
			if(rows[i].Code==row[j].Code){
				$.messager.alert('提示','选择了已有的委托单！','warning');				
				bStopRunning = true;
				break;
			}
			if(rows[i].CustomerName!=row[j].CustomerName){
				$.messager.alert('提示','所选委托单不属于同一个委托单位！','warning');				
				bStopRunning = true;
				break;
			}
		}
		if(bStopRunning){
			break;
		}else{
			$('#table5').datagrid('insertRow', {
				index: index,
				row:rows[i]
				
			});
			index++;
		}		
	}
	doCountFeeAndUpdate();	//更新费用汇总表
	$('#table6').datagrid('clearSelections');
	
	
}
function doSubmitdiscount(){
	$("#discount-submit-form").form('submit', {
			url:'/jlyw/DiscountServlet.do?method=2',
			onSubmit: function(){			
				var rows=$('#table5').datagrid('getRows');
				if(rows.length==0){
					$.messager.alert('提示','折扣申请记录为空!','info');
				}
			    $('#comSheets').val(JSON.stringify(rows));
				return $("#discount-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//重新加载任务分配信息
					//$('#discount_info_table').datagrid('reload');
					
					//清空申请表单的折扣信息
					//$('#NewPrice').val('');
					$('#apply-window').window('close');
					$.messager.alert('提示','折扣申请成功!','info');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
			}
		});  
}
</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单折扣" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;padding:10px;height:100px"
				title="查询条件" collapsible="false"  closable="false">
			<table width="950px" id="table1">
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
		</div>

      <div style="width:1000px;height:300px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>
	  <div style="width:1000px;height:300px;">
	 	<table id="table5" style="height:300px; width:1000px" >
		</table>
	  </div>
	   <div id="price" class="easyui-panel" style="width:1000px;height:100px"
				 collapsible="false"  closable="false">
		<table width="980px" height="50px" id="FeeCount" >
					
		</table>
		</div>
		
		<div id="apply-window" class="easyui-window" modal="true" title="折扣申请" style="padding: 10px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="discount-submit-form" method="post" >  
				<input id="comSheets" name="comSheets"  style="width:152px;" type="hidden" required="true"/>
       		
			 <table>
				 <tr>
						<td   align="right" >委托方办理人：</td>
						<td   align="left"><input id="Contector"   name="Contector" class="easyui-validatebox" style="width:152px;"  /></td>
				</tr>
				<tr>
						<td   align="right" >办理人电话：					</td>
						
						<td   align="left"><input id="ContectorTel"   name="ContectorTel" style="width:152px;"  /></td>	
				 </tr>
				 <tr>	
						<td   align="right" >
						  申请原因：					</td>
						<td  align="left">
						<input id="Reasons" class="easyui-combobox" required="true"  name="Reasons" url="/jlyw/ReasonServlet.do?method=0&type=13" style="width:154px;" valueField="name" textField="name" panelHeight="auto" />	</td>	
				 </tr>
				 <tr>	
						<td colspan="2"  align="center"  > <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitdiscount()">提交申请</a></td>
				 </tr>
				 
		  </table>
		   </form>
		</div>
</div>
</DIV>
</DIV>
</body>
</html>
