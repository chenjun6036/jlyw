<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>֤������޸�</title>
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
				title:'���깤ȷ�ϵ�ί�е�',
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
				
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10){	//��ע��
						return 'color:#FF0000'
					}
				},
				onSelect:function(rowIndex, rowData)
				{
					var Code=rowData.Code;
					$('#CommissionSheetId').val(rowData.Id);
										
					clickname="ί�е���Ϊ'"+Code+"' �ķ��÷�����Ϣ";
					$('#fee_assign_table').datagrid({title:clickname});
					$('#fee_assign_table').datagrid('loadData',{total:0,rows:[]});
					$('#fee_assign_table').datagrid('options').url='/jlyw/CertificateFeeAssignServlet.do?method=6';
					$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':rowData.Id};
					$('#fee_assign_table').datagrid('reload');
					
					$('#OldcertificateFee').val($('#fee_assign_table').datagrid('getRows'));
				}
			});
		$('#fee_assign_table').datagrid({
			title:'�����޸�',
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
					
					{field:'CertificateCode',title:'֤����',width:70,sortable:true,align:'center'},
					{field:'CustomerName',title:'��λ����',width:120,sortable:true,align:'center'},
					{field:'ApplianceName',title:'��������',width:70,sortable:true,align:'center'},
					{field:'FeeAlloteeName',title:'����',width:70,sortable:true,align:'center'},
					{field:'Quantity',title:'����',width:40,sortable:true,align:'center'},
					{field:'OldTotalFee',title:'�ܷ���',width:50,align:'center'},
					{field:'TotalFee',title:'�ܷ����޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldTestFee',title:'�춨��',width:50,align:'center'},
					{field:'TestFee',title:'�춨���޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldRepairFee',title:'�����',width:50,align:'center'},
					{field:'RepairFee',title:'������޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldMaterialFee',title:'���Ϸ�',width:50,align:'center'},
					{field:'MaterialFee',title:'���Ϸ��޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldCarFee',title:'��ͨ��',width:50,align:'center'},
					{field:'CarFee',title:'��ͨ���޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldDebugFee',title:'���Է�',width:50,align:'center'},
					{field:'DebugFee',title:'���Է��޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OldOtherFee',title:'������',width:50,align:'center'},
					{field:'OtherFee',title:'�������޸�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'Remark',title:'��ע',width:300,align:'center',editor:{
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
								//compute($(this),e.target);  //��Ҫ���еĴ������
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
	$('#fee_assign_table').datagrid('acceptChanges');
	var row = $("#fee_assign_table").datagrid("getSelected");
	var index=$("#fee_assign_table").datagrid("getRowIndex",row);
	
	if(newValue<= 1 && newValue>= 0){
		doComputeCommissionSheetChildFee(row, fieldName, newValue);	//�����ۿ��ʺ�ķ���
	}	
  
	doComputeTotalFee(row);	//������е��ܷ���
	$('#fee_assign_table').datagrid('updateRow',{
		index: index,
		row:row
	});
	
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
	$('#fee_assign_table').datagrid('acceptChanges');
	var row = $("#fee_assign_table").datagrid("getSelected");
	var index=$("#fee_assign_table").datagrid("getRowIndex",row);	
	
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
	
	
	/******************         ��ֵ������غ���             ***********************/
	/*function doDelFeeAssignRecord(){	//ɾ����ѡ�ķ��ü�¼
		var result = confirm("��ȷ��Ҫɾ����ѡ���ü�¼��");
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
	function doAddAnAllotee(){	//���һ�����÷�����
		if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==""){
			var result = confirm("ȷ��������������ԱΪ����");
			if(result == false){
				return false;
			}
		}
		var rows = $('#fee_assign_table').datagrid('getRows');
		for(var i = 0; i<rows.length; i++){
			if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==rows[i].FeeAlloteeName){
				$.messager.alert('��ʾ��','�Ѵ���:'+ $('#FeeAssignForm-FeeAllotee').combobox('getValue') + '�Ĳ�ֵ���䣬�����ظ���ӣ�' ,'info');
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
	function doFeeAssign(){	//ȷ���ύ���÷���
		$("#FeeAssignForm").form('submit', {
			url:'/jlyw/CertificateFeeAssignServlet.do?method=5',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;	
						
				$('#fee_assign_table').datagrid('acceptChanges');
				try{
					var row = $('#fee_assign_table').datagrid('getSelected');
					var index=$("#fee_assign_table").datagrid("getRowIndex",row);
					doComputeTotalFee(row);	//������е��ܷ���
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
					$.messager.alert('��ʾ��','�����޸ĳɹ���','info');
					$('#certificateFee').val(JSON.stringify($('#fee_assign_table').datagrid('getRows')));

					$('#fee_assign_table').datagrid('reload');
				}else{
					$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
				}
			}
		});
	}
	function printUpdateFee(){
		if($('#CommissionSheetId').val() == ''||$('#CommissionSheetId').val().length==0){
				$.messager.alert('��ʾ','��ѡ��ķѵ�ί�е�','error');
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
			<jsp:param name="TitleName" value="֤������޸�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
			<form id="SearchForm" method="post" >
				<table width="980px" id="table1">
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
				    <td style="text-align:left;padding-left:2px"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" href="javascript:void(0)" onClick="doFeeAssign()">������÷���</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-print" plain="true" href="javascript:void(0)" onClick="printUpdateFee()">��ӡ�ķѵ�</a><!--<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" href="javascript:void(0)" onClick="doDelFeeAssignRecord()">ɾ��</a>-->
					</td>
					<!--<td style="text-align:right;padding-right:2px">
					ѡ��Ա����<select name="FeeAllotee" id="FeeAssignForm-FeeAllotee" style="width:152px"></select>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" plain="true" onClick="doAddAnAllotee()">�������÷���</a>
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
