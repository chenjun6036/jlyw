<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ί�е��ۿ�</title>
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
				title:'���깤ȷ�ϵ�ί�е�',
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
					{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					{field:'Status',title:'ί�е�״̬',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
					{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
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
					//{field:'Appearance',title:'��۸���',width:80,align:'center'},
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
					//{field:'OtherRequirements',title:'����Ҫ��',width:80,align:'center'},
					{field:'Location',title:'���λ��',width:80,align:'center'},
					{field:'Allotee',title:'�ɶ���',width:80,align:'center'}
                ]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'�ύѡ�е�ί�е�',
					iconCls:'icon-save',
					handler:function(){
						AddRecordFromHistory();
					}
				}],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10){	//��ע��
						return 'color:#FF0000'
					}
				}
			});
			var lastIndex;
			$('#table5').datagrid({
				title:'ѡ�е�ί�е���Ϣ',
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
					{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'ί�е�λ',width:120,align:'center',sortable:true},
					{field:'ApplianceName',title:'��������',width:80,align:'center'},
					{field:'Quantity',title:'̨/����',width:70,align:'center'},
					{field:'OldTotalFee',title:'�ܷ���',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TotalFee',title:'�ܷ����ۿ�',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldTestFee',title:'����',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TestFee',title:'�����ۿ�',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldRepairFee',title:'ά�޷�',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'RepairFee',title:'ά�޷��ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldMaterialFee',title:'���Ϸ�',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'MaterialFee',title:'���Ϸ��ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldCarFee',title:'��ͨ��',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'CarFee',title:'��ͨ���ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldDebugFee',title:'���Է�',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'DebugFee',title:'���Է��ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldOtherFee',title:'������',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OtherFee',title:'�������ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					}
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'ɾ��',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("��ȷ��Ҫ�Ƴ���Щί�е���Ϣ��");
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
					text:'�ۿ�����',
					iconCls:'icon-save',
					handler:function(){
						$('#table5').datagrid('acceptChanges');
						try{
							var row = $('#table5').datagrid('getSelected');
							var index=$("#table5").datagrid("getRowIndex",row);
							doComputeTotalFee(row);	//������е��ܷ���
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
							doComputeTotalFee(row);	//������е��ܷ���
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
										//compute($(this),e.target);  //��Ҫ���еĴ������
								   }
								});
							}
				      }  
				}
			});
				
			var lastIndex_FeeCount;
			$('#FeeCount').datagrid({
				//title:'ѡ�е�ί�е���Ϣ',
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
					{field:'OldTotalFee',title:'�ܷ���',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TotalFee',title:'�ܷ����ۿ�',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldTestFee',title:'����',width:60,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'TestFee',title:'�����ۿ�',width:60,align:'left',editor:'numberbox',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldRepairFee',title:'ά�޷�',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'RepairFee',title:'ά�޷��ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldMaterialFee',title:'���Ϸ�',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'MaterialFee',title:'���Ϸ��ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldCarFee',title:'��ͨ��',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'CarFee',title:'��ͨ���ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldDebugFee',title:'���Է�',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'DebugFee',title:'���Է��ۿ�',width:60,align:'left',editor:'numberbox',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OldOtherFee',title:'������',width:60,align:'left',
						formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}
					},
					{field:'OtherFee',title:'�������ۿ�',width:60,align:'left',editor:'numberbox',
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
		
//��ȡһ�е�ֵ
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
//����ί�е��ַ���;row:ָ���У�fieldName����������discount���ۿ���
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
//�����ܷ��ã�ί�е����߻��ܵ��ܷ��ã�
function doComputeTotalFee(row){
	row.TotalFee = getInt(row.TestFee)+getInt(row.RepairFee)+getInt(row.MaterialFee)+getInt(row.CarFee)+getInt(row.DebugFee)+getInt(row.OtherFee);
}
//�����ܷ��ã�ԭ�ۣ�ί�е����߻��ܵ��ܷ��ã�
function doComputeTotalFeeOld(row){
	row.OldTotalFee = getInt(row.OldTestFee)+getInt(row.OldRepairFee)+getInt(row.OldMaterialFee)+getInt(row.OldCarFee)+getInt(row.OldDebugFee)+getInt(row.OldOtherFee);
}
//������ܵķַ���
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

//����ί�е��ĸ�����ò����������
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
	doComputeTotalFee(CountFeeRow);	//�ַ�����ӡ����ּ�
	doComputeTotalFeeOld(CountFeeRow);	//�ַ�����ӡ���ԭ��
	
	$('#FeeCount').datagrid('loadData',{total:0,rows:[]});
	$('#FeeCount').datagrid('insertRow', {
		index: 0,
		row:CountFeeRow
	});
}
/**
*  ���༭�����
* @param fieldName:������
* @param preValueObj:�޸�֮ǰ��ֵ�Ķ���
* @param newValueObj:�޸�֮���ֵ�Ķ���
*/
function commissionSheetChildFeeChange(fieldName, preValueObj, newValueObj)//ί�е��ַ��øı�ʱ
{
	var newValue = getFloat($(newValueObj).val());
	if(newValue < 0){
		$.messager.alert('����','�������ֵ����С��0��','warning');
		return false;
	}
	$('#table5').datagrid('acceptChanges');
	var row = $("#table5").datagrid("getSelected");
	var index=$("#table5").datagrid("getRowIndex",row);
	
	if(newValue<= 1 && newValue>= 0){
		doComputeCommissionSheetChildFee(row, fieldName, newValue);	//�����ۿ��ʺ�ķ���
	}	
  
	doComputeTotalFee(row);	//������е��ܷ���
	$('#table5').datagrid('updateRow',{
		index: index,
		row:row
	});
	doCountFeeAndUpdate();
	return true;
 
}
/**
*  ���༭�����
* @param preValueObj:�޸�֮ǰ��ֵ�Ķ���
* @param newValueObj:�޸�֮���ֵ�Ķ���
*/
function commissionSheetTotalFeeChange(preValueObj, newValueObj)//ί�е��ܷ��øı�ʱ
{
	var newValue = getFloat($(newValueObj).val());
	if(newValue < 0){
		$.messager.alert('����','�������ֵ����С��0��','warning');
		return false;
	}
	$('#table5').datagrid('acceptChanges');
	var row = $("#table5").datagrid("getSelected");
	var index=$("#table5").datagrid("getRowIndex",row);	
	
	var discount = 0.0;	//�ۿ���
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
	doComputeCommissionSheetChildFee(row, "TestFee", discount);	//�����ۿ��ʺ�ķ���
	doComputeCommissionSheetChildFee(row, "RepairFee", discount);	//�����ۿ��ʺ�ķ���
	doComputeCommissionSheetChildFee(row, "MaterialFee", discount);	//�����ۿ��ʺ�ķ���
	doComputeCommissionSheetChildFee(row, "CarFee", discount);	//�����ۿ��ʺ�ķ���
	doComputeCommissionSheetChildFee(row, "DebugFee", discount);	//�����ۿ��ʺ�ķ���
	doComputeCommissionSheetChildFee(row, "OtherFee", discount);	//�����ۿ��ʺ�ķ���	
  
	doComputeTotalFee(row);	//������е��ܷ���
	$('#table5').datagrid('updateRow',{
		index: index,
		row:row
	});
	doCountFeeAndUpdate();
	return true;
}
/**
*  ���༭�����
* @param fieldName:������
* @param preValueObj:�޸�֮ǰ��ֵ�Ķ���
* @param newValueObj:�޸�֮���ֵ�Ķ���
*/
function feeCountChange(fieldName,preValueObj, newValueObj)//ί�е����ܵķ��øı�ʱ
{
	var newValue = getFloat($(newValueObj).val());
	if(newValue < 0){
		$.messager.alert('����','�������ֵ����С��0��','warning');
		return false;
	}
//	alert("feeCountChange   "+newValue+"   "+fieldName);
	$('#FeeCount').datagrid('acceptChanges');
	var row = $("#FeeCount").datagrid("getSelected");
	var index=$("#FeeCount").datagrid("getRowIndex",row);
	
	
	var discount = 0.0;	//�ۿ���
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
	
	//��������ί�е���
	var rows = $("#table5").datagrid("getRows");
	for(var i = 0; i<rows.length; i++){
		var tempRow = rows[i];
		var tempIndex=$("#table5").datagrid("getRowIndex",tempRow);
		if(fieldName == "TestFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "TestFee", discount);	//�����ۿ��ʺ�ķ���
		}
		if(fieldName == "RepairFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "RepairFee", discount);	//�����ۿ��ʺ�ķ���
		}
		if(fieldName == "MaterialFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "MaterialFee", discount);	//�����ۿ��ʺ�ķ���
		}
		if(fieldName == "CarFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "CarFee", discount);	//�����ۿ��ʺ�ķ���
		}
		if(fieldName == "DebugFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "DebugFee", discount);	//�����ۿ��ʺ�ķ���
		}
		if(fieldName == "OtherFee" || fieldName=="TotalFee"){
			doComputeCommissionSheetChildFee(tempRow, "OtherFee", discount);	//�����ۿ��ʺ�ķ���
		}
		doComputeTotalFee(tempRow);	//������е��ܷ���
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
function AddRecordFromHistory(){	//���һ��ί�е�
	var rows = $('#table6').datagrid('getSelections');
	
	var row=$('#table5').datagrid('getRows');
	var index=row.length;
	var bStopRunning = false;
	for(var i=0; i<rows.length; i++){
		var j;		
		for(j=0; j<index; j++){
			if(rows[i].Code==row[j].Code){
				$.messager.alert('��ʾ','ѡ�������е�ί�е���','warning');				
				bStopRunning = true;
				break;
			}
			if(rows[i].CustomerName!=row[j].CustomerName){
				$.messager.alert('��ʾ','��ѡί�е�������ͬһ��ί�е�λ��','warning');				
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
	doCountFeeAndUpdate();	//���·��û��ܱ�
	$('#table6').datagrid('clearSelections');
	
	
}
function doSubmitdiscount(){
	$("#discount-submit-form").form('submit', {
			url:'/jlyw/DiscountServlet.do?method=2',
			onSubmit: function(){			
				var rows=$('#table5').datagrid('getRows');
				if(rows.length==0){
					$.messager.alert('��ʾ','�ۿ������¼Ϊ��!','info');
				}
			    $('#comSheets').val(JSON.stringify(rows));
				return $("#discount-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//���¼������������Ϣ
					//$('#discount_info_table').datagrid('reload');
					
					//�����������ۿ���Ϣ
					//$('#NewPrice').val('');
					$('#apply-window').window('close');
					$.messager.alert('��ʾ','�ۿ�����ɹ�!','info');
				}else{
					$.messager.alert('��ʾ',result.msg,'error');
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
			<jsp:param name="TitleName" value="ί�е��ۿ�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;padding:10px;height:100px"
				title="��ѯ����" collapsible="false"  closable="false">
			<table width="950px" id="table1">
				<tr>
					<td  width="99%" align="left">
						ί�е��ţ�<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;"/>&nbsp;
						ί�е�λ��<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;"/>&nbsp;
						��ʼʱ�䣺<input name="DateFrom" id="DateFrom" type="text" style="width:90px;" class="easyui-datebox" />&nbsp;
						����ʱ�䣺<input name="DateEnd" id="DateEnd" type="text" style="width:90px;" class="easyui-datebox" />&nbsp;&nbsp;&nbsp;&nbsp;
						
						<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a>
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
		
		<div id="apply-window" class="easyui-window" modal="true" title="�ۿ�����" style="padding: 10px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="discount-submit-form" method="post" >  
				<input id="comSheets" name="comSheets"  style="width:152px;" type="hidden" required="true"/>
       		
			 <table>
				 <tr>
						<td   align="right" >ί�з������ˣ�</td>
						<td   align="left"><input id="Contector"   name="Contector" class="easyui-validatebox" style="width:152px;"  /></td>
				</tr>
				<tr>
						<td   align="right" >�����˵绰��					</td>
						
						<td   align="left"><input id="ContectorTel"   name="ContectorTel" style="width:152px;"  /></td>	
				 </tr>
				 <tr>	
						<td   align="right" >
						  ����ԭ��					</td>
						<td  align="left">
						<input id="Reasons" class="easyui-combobox" required="true"  name="Reasons" url="/jlyw/ReasonServlet.do?method=0&type=13" style="width:154px;" valueField="name" textField="name" panelHeight="auto" />	</td>	
				 </tr>
				 <tr>	
						<td colspan="2"  align="center"  > <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitdiscount()">�ύ����</a></td>
				 </tr>
				 
		  </table>
		   </form>
		</div>
</div>
</DIV>
</DIV>
</body>
</html>
