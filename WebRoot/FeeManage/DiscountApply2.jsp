<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>折扣申请</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
<link href="../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />

<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src="../Inc/JScript/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="../JScript/json2.js"></script>
<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
<script type="text/javascript" src="../JScript/StatusInfo.js"></script>
<script>
$(function(){
	$("#CustomerName").combobox({
		valueField:'name',
		textField:'name',
		onSelect:function(record){
			$("#CustomerTel").val(record.tel);
			$("#CustomerAddress").val(record.address);
			$("#CustomerZipCode").val(record.zipCode);
			$("#ContactPerson").val(record.contactor);
			$("#ContactorTel").val(record.contactorTel);
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
			$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
		}
	});
	
	

	var lastIndex;
	
	$('#table4').datagrid({
		width:1000,
		height:350,
		
		title:'费用清单信息',
		singleSelect:true, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=22&DetailListId=""',
		sortName: 'DetailListLastEditTime',
		remoteSort: false,
		sortOrder:'dec',
		idField:'DetailListId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DetailListCode',title:'费用清单号',width:100,align:'center',sortable:true},
			{field:'Customer',title:'委托单位',width:180,align:'center',sortable:true},
			{field:'DetailListLastEditTime',title:'清单最后编辑时间',width:160,align:'center',sortable:true},
			
			{field:'TestFee',title:'检验费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TotalFee',title:'总检验费用',width:100,align:'left',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DiscountString',title:'折扣信息',width:180,align:'center'}
			
		]],
		pagination:true,
		rownumbers:true,
		rowStyler:function(rowIndex, rowData){
			if(rowData.DiscountString.length>0){	//已生成过清单
				return 'color:#FF0000'
			}else{
				return 'color:#000000';
			}
		},
		onDblClickRow:function(rowIndex, rowData)
		{
			var DetailListId=rowData.DetailListId;
			var DetailListCode=rowData.DetailListCode;
			clickname="对应清单号'"+DetailListCode+"' 的委托单信息";
			$('#table6').datagrid({title:clickname});
			$('#table6').datagrid('options').url='/jlyw/DetailListComServlet.do?method=3';
			$('#table6').datagrid('options').queryParams={'DetailListId':DetailListId};
			$('#table6').datagrid('reload');
		},
		onSelect:function(rowIndex, rowData)
		{
			var DetailListId=rowData.DetailListId;
			var DetailListCode=rowData.DetailListCode;
			clickname="对应清单号'"+DetailListCode+"' 的委托单信息";
			$('#table6').datagrid({title:clickname});
			$('#table6').datagrid('options').url='/jlyw/DetailListComServlet.do?method=3';
			$('#table6').datagrid('options').queryParams={'DetailListId':DetailListId};
			$('#table6').datagrid('reload');
			
			$('#OldTestFee').val(rowData.TestFee);
			$('#OldRepairFee').val(rowData.RepairFee);
			$('#OldMaterialFee').val(rowData.MaterialFee);
			$('#OldCarFee').val(rowData.CarFee);
			$('#OldDebugFee').val(rowData.DebugFee);
			$('#OldOtherFee').val(rowData.OtherFee);
			$('#OldTotalFee').val(rowData.TotalFee);
			
			$('#NewTestFee').val(rowData.TestFee);
			$('#NewRepairFee').val(rowData.RepairFee);
			$('#NewMaterialFee').val(rowData.MaterialFee);
			$('#NewCarFee').val(rowData.CarFee);
			$('#NewDebugFee').val(rowData.DebugFee);
			$('#NewOtherFee').val(rowData.OtherFee);
			$('#NewTotalFee').val(rowData.TotalFee);
			
			$('#discountTestFee').val(1.00);
			$('#discountRepairFee').val(1.00);
			$('#discountMaterialFee').val(1.00);
			$('#discountCarFee').val(1.00);
			$('#discountDebugFee').val(1.00);
			$('#discountOtherFee').val(1.00);
			$('#discountTotalFee').val(1.00);
			
			$('#DetailListId').val(rowData.DetailListId)
			  var feeArray = new Array('TestFee','RepairFee','MaterialFee','CarFee','DebugFee','OtherFee','TotalFee');   
			  for(var i=0;i<7;i++){ 
				  if(!(getFloat($("#Old"+feeArray[i]).val())>0)){
				  		
						$("#discount"+feeArray[i]).attr('readonly','readonly');
						$("#New"+feeArray[i]).attr('readonly','readonly'); 
				  }
			  }
			return $('#discount-submit-form').form('validate');
		}
	});
	
	$('#table6').datagrid({
		width:1000,
		height:350,
		
		title:'费用清单对应的委托单信息',
		singleSelect:false, 
		nowrap: false,
		striped: true,
//				collapsible:true,
		url:'/jlyw/DetailListComServlet.do?method=3',
		remoteSort: false,
		idField:'id',
		
		columns:[[
			{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
			{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
			{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
			{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
			{field:'Status',title:'委托单状态',width:80,align:'center',
				formatter:function(value,rowData,rowIndex){
					return getCommissionSheetStatusInfo(value);
				}
			},
			{field:'TestFee',title:'检验费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'RepairFee',title:'维修费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'MaterialFee',title:'材料费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'CarFee',title:'交通费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'DebugFee',title:'调试费',width:100,align:'right',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'OtherFee',title:'其他费',width:100,align:'right',formatter:function(val,rec){
					
						return '<span style="color:red;">'+val+'</span>';
				}},
			{field:'TotalFee',title:'总检验费用',width:100,align:'left',formatter:function(val,rec){
						return '<span style="color:red;">'+val+'</span>';
				}}
			
			
		]],
		pagination:true,
		rownumbers:true
	});
		
	var nowDate = new Date();
	$("#CommissionDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	$('#').val("");
	$('#Code').val("");
	$('#CommissionSheetId').val("");
});



function AddRecordFromHistory(){
	
}
function doLoadHistoryDeList()
{
	
	$('#table4').datagrid('options').url='/jlyw/DetailListComServlet.do?method=22';
	$('#table4').datagrid('options').queryParams={'FeeCode':$('#FeeCode').val(),'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val()};
	$('#table4').datagrid('reload');
}

function doDenerate(){
	
}
function multiplication(fee)  
{  	 
		$('#discount-submit-form').form('validate')
	  var oldFee = getFloat($("#Old"+fee).val());  
	  var discount = getFloat($("#discount"+fee).val());  
	  var newFee = oldFee*discount;  
	  $("#New"+fee).val(newFee); 
	 
	  var oldtotalFee = getFloat($("#OldTotalFee").val());    
	  $("#NewTotalFee").val(getFloat(oldtotalFee-oldFee)+ getFloat($("#New"+fee).val())); 
	  $("#discountTotalFee").val(getFloat($("#NewTotalFee").val()/$("#OldTotalFee").val())); 
	  
	  return $('#discount-submit-form').form('validate');
}  
function division(fee){
	$('#discount-submit-form').form('validate')
	var oldFee = getFloat($("#Old"+fee).val()); 
	if(oldFee>0) 
		$("#discount"+fee).setAttribute("readOnly","true"); 
    var newFee = getFloat($("#New"+fee).val()); 
	var discount = getFloat(newFee/oldFee); 
	$("#discount"+fee).val(discount); 
	
	var oldtotalFee = getFloat($("#OldTotalFee").val());
	$("#NewTotalFee").val(getFloat(oldtotalFee-oldFee)+ getFloat($("#New"+fee).val())); 
	$("#discountTotalFee").val(getFloat($("#NewTotalFee").val()/$("#OldTotalFee").val())); 
	
	return $('#discount-submit-form').form('validate');
	  
}
function multiplication1()  
{  
		$('#discount-submit-form').form('validate')
	  var oldFee = getFloat($("#OldTotalFee").val());  
	  var discount = getFloat($("#discountTotalFee").val());  
	  var newFee = oldFee*discount;  
	  $("#NewTotalFee").val(newFee); 
	 
	  var feeArray = new Array('TestFee','RepairFee','MaterialFee','CarFee','DebugFee','OtherFee');   
	  for(var i=0;i<6;i++){ 
	         	
		  $("#discount"+feeArray[i]).val(discount); 
		  $("#New"+feeArray[i]).val(getFloat($("#Old"+feeArray[i]).val())*getFloat($("#discount"+feeArray[i]).val())); 
	  }
	  
	  return $('#discount-submit-form').form('validate');
}  
function division1(){
	$('#discount-submit-form').form('validate')
	var oldFee = getFloat($("#OldTotalFee").val());  
    var newFee = getFloat($("#NewTotalFee").val()); 
	var discount = getFloat(newFee/oldFee); 
	$("#discountTotalFee").val(discount); 
	
	var feeArray = new Array('TestFee','RepairFee','MaterialFee','CarFee','DebugFee','OtherFee');   
	for(var i=0;i<6;i++){ 
	         	
		$("#discount"+feeArray[i]).val(discount); 
		$("#New"+feeArray[i]).val(getFloat($("#Old"+feeArray[i]).val())*getFloat($("#discount"+feeArray[i]).val())); 
	}
	
	return $('#discount-submit-form').form('validate');
	  
}
function doSubmitdiscount(){   	//提交折扣申请表单
		$("#discount-submit-form").form('submit', {
			url:'/jlyw/DiscountServlet.do?method=2',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//判断选择的检验项目是否有效
				var projectValue = $('#DetailListId').val();
				if(projectValue==''){
					$.messager.alert('提示！',"请选择需要折扣的费用清单！",'info');
					return false;
				}
				return $("#discount-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//重新加载任务分配信息
					//$('#discount_info_table').datagrid('reload');
					
					//清空申请表单的折扣信息
					//$('#NewPrice').val('');
					
					$.messager.alert('提示','折扣申请成功!','info');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
			}
		});  
   }  
</script>
</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="折扣申请" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
		<div id="p" class="easyui-panel" style="width:1000px;height:100px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="12%" align="right" >费用清单编号：</td>
					<td width="15%" align="left" >
						<input id="FeeCode" class="easyui-validatebox" name="FeeCode" style="width:150px;" />
					</td>
					
					<td width="10%" align="right">委托单位：</td>
					<td width="20%" align="left">
						<select name="CustomerName" id="CustomerName" style="width:180px"></select>
					</td>
					<td width="10%" align="right" >委托单编号：</td>
					<td width="15%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;"></input>
					</td>

					
					<td width="10%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryDeList()">查询</a></td>
					
				</tr >
		</table>
		</form>
		</div> 
	<table id="table4" style="height:350px; width:1000px"><!--清单信息-->
	</table>
	<table id="table6" style="height:350px; width:1000px"><!--委托单信息-->
	</table>

	
	<form id="Generate" method="post">
	<input type="hidden" name="DetailList" id="DetailList" value="" />
  	</form>
	<div id="p2" class="easyui-panel" style="width:1000px;padding:10px;"
				title="折扣申请" collapsible="false"  closable="false" >  
			<table id="discount_info_table" iconCls="icon-tip" ></table>
			<br />
			<form id="discount-submit-form" method="post" >  
			<input id="DetailListId" name="DetailListId"  style="width:152px;" type="hidden" required="true"/>
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="12%"   align="right" >检验费原价：</td>
				    <td width="22%"  align="left"><input id="OldTestFee" name="OldTestFee" class="easyui-numberbox" precision="2" groupSeparator="," prefix="￥" style="width:152px;" required="true" readonly="readonly"/>元</td>
					<td width="14%"   align="right" >
					  检验费折扣额：					</td>
					<td width="22%"   align="left">
				  	<input id="discountTestFee"   name="discountTestFee" class="easyui-numberbox" precision="2" min="0" max="1" style="width:152px;" required="true" onchange="javascript:multiplication('TestFee');"/></td>
					<td width="10%"   align="right" >检验费现价：					</td>
					
					<td width="22%"   align="left"><input id="NewTestFee"   name="NewTestFee" class="easyui-numberbox" precision="0" groupSeparator="," prefix="￥" style="width:152px;" required="true" onchange="javascript:division('TestFee');"/>元</td>
							
				</tr>
				<tr>
					<td width="12%"   align="right" >维修费原价：</td>
				    <td width="22%"  align="left"><input id="OldRepairFee"   name="OldRepairFee" class="easyui-numberbox" style="width:152px;" precision="2" required="true" readonly="readonly"/>元</td>
					<td width="14%"   align="right" >
					  维修费折扣额：					</td>
					<td width="22%"   align="left">
				  	<input id="discountRepairFee"   name="discountRepairFee" class="easyui-numberbox" precision="2" min="0" max="1" style="width:152px;" required="true" onchange="javascript:multiplication('RepairFee');"/></td>
					<td width="10%"   align="right" >维修费现价：					</td>
					
					<td width="22%"   align="left"><input id="NewRepairFee"   name="NewRepairFee" class="easyui-numberbox" style="width:152px;" precision="0" required="true" onchange="javascript:division('TestFee');"/>元</td>
							
				</tr>
				<tr>
					<td width="12%"   align="right" >材料费原价：</td>
				    <td width="22%"  align="left"><input id="OldMaterialFee"   name="OldMaterialFee" class="easyui-numberbox" precision="2" style="width:152px;" required="true" readonly="readonly"/>元</td>
					<td width="14%"   align="right" >
					  材料费折扣额：					</td>
					<td width="22%"   align="left">
				  	<input id="discountMaterialFee"   name="discountMaterialFee" class="easyui-numberbox" style="width:152px;" precision="2" min="0" max="1" required="true" onchange="javascript:multiplication('MaterialFee');"/></td>
					<td width="10%"   align="right" >材料费现价：					</td>
					
					<td width="22%"   align="left"><input id="NewMaterialFee"   name="NewMaterialFee" class="easyui-numberbox" style="width:152px;" precision="0" required="true" onchange="javascript:division('TestFee');"/>元</td>
							
				</tr>
				
				<td width="12%"   align="right" >交通费原价：</td>
				    <td width="22%"  align="left"><input id="OldCarFee"   name="OldCarFee" class="easyui-numberbox" style="width:152px;" precision="2" required="true" readonly="readonly"/>元</td>
					<td width="14%"   align="right" >
					  交通费折扣额：					</td>
					<td width="22%"   align="left">
				  	<input id="discountCarFee"   name="discountCarFee" class="easyui-numberbox" style="width:152px;" required="true" precision="2" min="0" max="1" onchange="javascript:multiplication('CarFee');"/></td>
					<td width="10%"   align="right" >交通费现价：					</td>
					
					<td width="22%"   align="left"><input id="NewCarFee"   name="NewCarFee" class="easyui-numberbox" style="width:152px;" precision="0" required="true" onchange="javascript:division('TestFee');"/>元</td>
							
				</tr>
				<tr>
					<td width="12%"   align="right" >调试费原价：</td>
				    <td width="22%"  align="left"><input id="OldDebugFee"   name="OldDebugFee" class="easyui-numberbox" style="width:152px;" precision="2" required="true" readonly="readonly"/>元</td>
					<td width="14%"   align="right" >
					  调试费折扣额：					</td>
					<td width="22%"   align="left">
				  	<input id="discountDebugFee"   name="discountDebugFee" class="easyui-numberbox" style="width:152px;" required="true" precision="2" min="0" max="1" onchange="javascript:multiplication('DebugFee');"/></td>
					<td width="10%"   align="right" >调试费现价：					</td>
					
					<td width="22%"   align="left"><input id="NewDebugFee"   name="NewDebugFee" class="easyui-numberbox" style="width:152px;" precision="0" required="true" onchange="javascript:division('TestFee');"/>元</td>
							
				</tr>
				<tr>
					<td width="12%"  align="right" >其他费原价：</td>
				    <td width="22%"  align="left"><input id="OldOtherFee"   name="OldOtherFee" class="easyui-numberbox" style="width:152px;" precision="2" required="true" readonly="readonly"/>元</td>
					<td width="14%"  align="right" >
					  其他费折扣额：					</td>
					<td width="22%"  align="left">
				  	<input id="discountOtherFee"   name="discountOtherFee" class="easyui-numberbox" style="width:152px;" required="true" precision="2" min="0" max="1" onchange="javascript:multiplication('OtherFee');"/></td>
					<td width="10%"  align="right" >其他费现价：					</td>
					
					<td width="22%"  align="left"><input id="NewOtherFee"   name="NewOtherFee" class="easyui-numberbox" style="width:152px;" precision="0" required="true" onchange="javascript:division('TestFee');"/>元</td>
							
				</tr>
				<tr>
					<td width="12%"  align="right" >总费用原价：</td>
				    <td width="22%"  align="left"><input id="OldTotalFee"   name="OldTotalFee" class="easyui-numberbox" style="width:152px;" precision="2" required="true" readonly="readonly"/>元</td>
					<td width="14%"  align="right" >
					  总费用折扣额：					</td>
					<td width="22%"  align="left">
				  	<input id="discountTotalFee"   name="discountTotalFee" class="easyui-numberbox" style="width:152px;" required="true" precision="2" min="0" max="1" onchange="javascript:multiplication1();"/></td>
					<td width="10%" s align="right" >总费用现价：					</td>
					
					<td width="22%"  align="left"><input id="NewTotalFee"   name="NewTotalFee" class="easyui-numberbox" style="width:152px;" precision="0" required="true" onchange="javascript:division1();"/>元</td>
							
				</tr>
				<tr>
					<td  style="padding-top:15px;" align="right" >委托方办理人：</td>
				    <td  style="padding-top:10px;" align="left"><input id="Contector"   name="Contector" class="easyui-validatebox" style="width:152px;"  /></td>
					<td  style="padding-top:15px;" align="right" >办理人电话：					</td>
					
					<td  style="padding-top:15px;" align="left"><input id="ContectorTel"   name="ContectorTel" class="easyui-numberbox" style="width:152px;"  /></td>		
					<td  style="padding-top:15px;" align="right" >
					  申请原因：					</td>
					<td style="padding-top:15px;" align="left">
				  	<input id="Reasons" class="easyui-combobox" required="true" editable="false" name="Reasons" url="/jlyw/ReasonServlet.do?method=0&type=13" style="width:154px;" valueField="name" textField="name" panelHeight="auto" />					</td>	
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitdiscount()">提交申请</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		  </table>
		  </form>
		</div>
	
	
	</DIV>
</DIV>
</body>
</html>
