<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
     <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
     <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
     <script type="text/javascript"	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
     <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
     <script type="text/javascript" src="../../JScript/letter.js"></script>
     <script type="text/javascript" src="../../JScript/json2.js"></script>
     <script type="text/javascript" src="../../JScript/upload.js"></script>
	 <script>
		$(function(){
			$('#table2').datagrid({
				title:'受检器具信息查询',
				width:1000,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/TargetApplianceServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'Name',title:'受检器具名称',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},
					{field:'NameEn',title:'器具英文名称',width:100,align:'center'},
					{field:'Code',title:'器具编码',width:80,align:'right'},
					{field:'Fee',title:'标准费用(元)',width:100,align:'center'},
					{field:'SRFee',title:'小修费用(元)',width:100,align:'center'},
					{field:'MRFee',title:'中修费用(元)',width:100,align:'center'},
					{field:'LRFee',title:'大修费用(元)',width:100,align:'center'},
					{field:'PromiseDuration',title:'承诺检出期(天)',width:120,align:'center'},
					{field:'TestCycle',title:'检定周期(月)',width:100,align:'center'},
					{field:'Certification',title:'认证',width:150,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value=="0"||value==0)
						{
							rowData['Certification']="0";
							return "";
						}
						else if(value=="1"||value==1)
						{
							rowData['Certification']="1";
							return "计量认证";
						}
						else if(value=="2"||value==2)
						{
							rowData['Certification']="2";
							return "实验室认可";
						}
						else if(value=="3"||value==3)
						{
							rowData['Certification']="3";
							return "实验室认可、计量认证";
						}
						else if(value=="4"||value==4)
						{
							rowData['Certification']="4";
							return "法定机构授权";
						}
						else if(value=="5"||value==5)
						{
							rowData['Certification']="5";
							return "法定机构授权、计量认证";
						}
						else if(value=="6"||value==6)
						{
							rowData['Certification']="6";
							return "法定机构授权、实验室认可";
						}
						else if(value=="7"||value==7)
						{
							rowData['Certification']="7";
							return "法定机构授权、实验室认可、计量认证";
						}
					}},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">注销</span>';
							}
					}},
					{field:'Remark',title:'备注',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#from1').show();
						
						$('#Name').val(select.Name);
						$('#Brief').val(select.Brief);
						$('#StandardNameId').combobox('loadData',[{'id':select.StandardNameId, 'name':select.StandardName}]);
						$('#StandardNameId').combobox('setValue',select.StandardNameId);
						$('#NameEn').val(select.NameEn);
						$('#Code').val(select.Code);
						$('#Fee').val(select.Fee);
						$('#SRFee').val(select.SRFee);
						$('#MRFee').val(select.MRFee);
						$('#LRFee').val(select.LRFee);
						$('#PromiseDuration').val(select.PromiseDuration);
						$('#TestCycle').val(select.TestCycle);
						var value = select.Certification;
						var cer1 = document.getElementById('cer1');
						var cer2 = document.getElementById('cer2');
						var cer3 = document.getElementById('cer3');
						if(value=="0"||value==0)
						{
							cer1.checked=false;
							cer2.checked=false;
							cer3.checked=false;
						}
						else if(value=="1"||value==1)
						{
							cer1.checked=false;
							cer2.checked=false;
							cer3.checked=true;
						}
						else if(value=="2"||value==2)
						{
							cer1.checked=false;
							cer2.checked=true;
							cer3.checked=false;
						}
						else if(value=="3"||value==3)
						{
							cer1.checked=false;
							cer2.checked=true;
							cer3.checked=true;
						}
						else if(value=="4"||value==4)
						{
							cer1.checked=true;
							cer2.checked=false;
							cer3.checked=false;
						}
						else if(value=="5"||value==5)
						{
							cer1.checked=true;
							cer2.checked=false;
							cer3.checked=true;
						}
						else if(value=="6"||value==6)
						{
							cer1.checked=true;
							cer2.checked=true;
							cer3.checked=false;
						}
						else if(value=="7"||value==7)
						{
							cer1.checked=true;
							cer2.checked=true;
							cer3.checked=true;
						}
						$('#Status').combobox('setValue',select.Status);
						$('#Remark').val(select.Remark);
						$('#id').val(select.Id);
						$('#form1').form('validate');
						id = select.Id; 
					}else{
						$.messager.alert('warning','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'注销',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select)
							{
								$.messager.confirm('提示','确认注销吗？',function(r){
								if(r){
										$.ajax({
											type:'POST',
											url:'/jlyw/TargetApplianceServlet.do?method=4',
											data:'id='+select.Id,
											dataType:"json",
											success:function(data, textStatus){
												$('#table2').datagrid('reload');
											}
										});
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
						text:'导出',
						iconCls:'icon-save',
						handler:function(){
							myExport();
						}
				}],
				onClickRow:function(rowIndex, rowData){
					var clickname=rowData.Name;
					clickname="受检器具 '"+clickname+"' 的型号规格、不确定度、测量范围管理";
					$('#p2').panel({title:clickname});
					$('#model_info_table').datagrid('clearSelections');
					$('#accuracy_info_table').datagrid('clearSelections');
					$('#range_info_table').datagrid('clearSelections');
					
					$('#model_info_table').datagrid('options').url='/jlyw/TargetApplianceServlet.do?method=7';
					$('#model_info_table').datagrid('options').queryParams={'TargetAppId':encodeURI(rowData.Id),'Type':encodeURI('1')};
					$('#model_info_table').datagrid('reload');
					
					
					$('#accuracy_info_table').datagrid('options').url='/jlyw/TargetApplianceServlet.do?method=7';
					$('#accuracy_info_table').datagrid('options').queryParams={'TargetAppId':encodeURI(rowData.Id),'Type':encodeURI('2')};
					$('#accuracy_info_table').datagrid('reload');
					
				
					$('#range_info_table').datagrid('options').url='/jlyw/TargetApplianceServlet.do?method=7';
					$('#range_info_table').datagrid('options').queryParams={'TargetAppId':encodeURI(rowData.Id),'Type':encodeURI('3')};
					$('#range_info_table').datagrid('reload');
				}
			});
		    
			$('#model_info_table').datagrid({
	 			title:'型号规格信息',
	 			width:320,
	 			height:300,
				singleSelect:false, 
				fit: false,
				nowrap: false,
				striped: true,
				url:'',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Model',title:'型号规格',width:120,align:'center'},
					{field:'ModelEn',title:'型号规格英文',width:120,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
							$('#add_model_div').window('open');
							$('#frm_add_model').show();
							
							$('#Model_TargetApp_id').val($('#model_info_table').datagrid('options').queryParams.TargetAppId);
					}
				},'-',{
						text:'修改',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#model_info_table').datagrid('getSelected');
							if(select)
							{
								$('#edit_model_div').window('open');
								
								$('#frm_edit_model').show();

								$('#Model_id').val(select.Id);
								$('#edit_model').val(select.Model);
								$('#edit_modelEn').val(select.ModelEn);
							}
							else
							{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var rows = $('#model_info_table').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('提示','确认删除吗？',function(r){
								if(r){
									var idStr="";
									for(var i =0; i < rows.length; i++){
										idStr = idStr + rows[i].Id + "|";
									}
									$.ajax({
										type:'POST',
										url:'/jlyw/TargetApplianceServlet.do?method=10',
										data:'idStr='+idStr + '&Type=1',
										dataType:"json",
										success:function(data, textStatus){
											$('#model_info_table').datagrid('reload');
										}
									});
									$('#model_info_table').datagrid('clearSelections');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
							
						}
				}]
	 		});
			$('#accuracy_info_table').datagrid({
	 			title:'不确定度信息',
	 			width:320,
	 			height:300,
				singleSelect:false, 
				fit: false,
				nowrap: false,
				striped: true,
				url:'',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Accuracy',title:'不确定度',width:120,align:'center'},
					{field:'AccuracyEn',title:'不确定度英文',width:120,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
							$('#add_accuracy_div').window('open');
							$('#frm_add_accuracy').show();
							
							$('#Accuracy_TargetApp_id').val($('#accuracy_info_table').datagrid('options').queryParams.TargetAppId);
					}
				},'-',{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#accuracy_info_table').datagrid('getSelected');
						if(select){
							$('#edit_accuracy_div').window('open');
							$('#frm_edit_accuracy').show();
							
							$('#Accuracy_id').val(select.Id);
							$('#edit_accuracy').val(select.Accuracy);
							$('#edit_accuracyEn').val(select.AccuracyEn);
						}
						else
						{
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var rows = $('#accuracy_info_table').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('提示','确认删除吗？',function(r){
								if(r){
									var idStr="";
									for(var i =0; i < rows.length; i++){
										idStr = idStr + rows[i].Id + "|";
									}
									$.ajax({
										type:'POST',
										url:'/jlyw/TargetApplianceServlet.do?method=10',
										data:'idStr='+ idStr + '&Type=2',
										dataType:"json",
										success:function(data, textStatus){
											$('#accuracy_info_table').datagrid('reload');
										}
									});
									$('#accuracy_info_table').datagrid('clearSelections');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
							
						}
				}]
	 		});
			$('#range_info_table').datagrid({
	 			title:'测量范围信息',
	 			width:320,
	 			height:300,
				singleSelect:false, 
				fit: false,
				nowrap: false,
				striped: true,
				url:'',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Range',title:'测量范围',width:120,align:'center'},
					{field:'RangeEn',title:'测量范围英文',width:120,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
							$('#add_range_div').window('open');
							$('#frm_add_range').show();
							
							$('#Range_TargetApp_id').val($('#range_info_table').datagrid('options').queryParams.TargetAppId);
					}
				},'-',{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#range_info_table').datagrid('getSelected');
						if(select){
							$('#edit_range_div').window('open');
							$('#frm_edit_range').show();
							
							$('#Range_id').val(select.Id);
							$('#edit_range').val(select.Range);
							$('#edit_rangeEn').val(select.RangeEn);
						}
						else
						{
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var rows = $('#range_info_table').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('提示','确认删除吗？',function(r){
								if(r){
									var idStr="";
									for(var i =0; i < rows.length; i++){
										idStr = idStr + rows[i].Id + "|";
									}
									$.ajax({
										type:'POST',
										url:'/jlyw/TargetApplianceServlet.do?method=10',
										data:'idStr='+ idStr + '&Type=3',
										dataType:"json",
										success:function(data, textStatus){
											$('#range_info_table').datagrid('reload');
										}
									});
									$('#range_info_table').datagrid('clearSelections');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				}]
	 		});
			
			$('#querystatus').combobox('setValue',"");
		});
		
		function cancel(){
			$('#edit').dialog('close');
			$('#add_model_div').dialog('close');
			$('#edit_model_div').dialog('close');
			$('#add_accuracy_div').dialog('close');
			$('#edit_accuracy_div').dialog('close');
			$('#add_range_div').dialog('close');
			$('#edit_range_div').dialog('close');
		}
		
		$(function(){
			$('#StandardNameId').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
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
					$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
					}
			});
			
			$('#queryStandardName').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
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
					$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
					}
			});
		});
		
		function edit(){
			$('#form1').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=3',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
					{
						$('#table2').datagrid('reload');
						cancel();
					}
				}
			});
			
		}
		
		function query(){
			$('#table2').datagrid('unselectAll');
			$('#table2').datagrid('options').url='/jlyw/TargetApplianceServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryStandardName':encodeURI($('#queryStandardName').combobox('getValue')),'appname':encodeURI($('#nameCn').val()),'queryTestCycle':encodeURI($('#queryTestCycle').val()),'queryStatus':encodeURI($('#querystatus').combobox('getValue')),'queryPromiseDuration':encodeURI($('#queryPromiseDuration').val()),'queryCertification':encodeURI((document.getElementById('querycerother').checked?"1":"0")+(document.getElementById('querycer1').checked?"1":"0")+(document.getElementById('querycer2').checked?"1":"0")+(document.getElementById('querycer3').checked?"1":"0"))};
			$('#table2').datagrid('reload');
			$('#querystatus').combobox('setValue',"");
		}
		
		function doSubmitModel(){
			$('#frm_add_model').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=8',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
					$('#model_info_table').datagrid('reload');
				}
			});
		}
		
		function doEditModel(){
			$('#frm_edit_model').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=9',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
					$('#model_info_table').datagrid('reload');
				}
			});
		}
		
		function doSubmitAccuracy(){
			$('#frm_add_accuracy').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=8',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
					$('#accuracy_info_table').datagrid('reload');
				}
			});
		}
		
		function doEditAccuracy(){
			$('#frm_edit_accuracy').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=9',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
					$('#accuracy_info_table').datagrid('reload');
				}
			});
		}
		
		function doSubmitRange(){
			$('#frm_add_range').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=8',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
					$('#range_info_table').datagrid('reload');
				}
			});
		}
		
		function doEditRange(){
			$('#frm_edit_range').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=9',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
					$('#range_info_table').datagrid('reload');
				}
			});
		}
		
		function getBrief(){
			$('#Brief').val(makePy($('#Name').val()));
		}
		
		function myExport(){
			ShowWaitingDlg("正在导出，请稍后......");
			$('#paramsStr').val(JSON.stringify($('#table2').datagrid('options').queryParams));
			$('#frm_export').form('submit',{
				success:function(data){
					var result = eval("("+ data +")");
					if(result.IsOK)
					{
						$('#filePath').val(result.Path);
						$('#frm_down').submit();
						CloseWaitingDlg();
					}
					else
					{
						$.messager.alert('提示','导出失败，请重试！','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
		function uncheckOthers(obj){
			if(obj.value=="4")
			{
				document.getElementById('querycer1').checked=false;
				document.getElementById('querycer2').checked=false;
				document.getElementById('querycer3').checked=false;
			}
			else
			{
				document.getElementById('querycerother').checked=false;
			}
		}
		
		</script>
</head>

<body>
<form id="frm_export" method="post" action="/jlyw/TargetApplianceServlet.do?method=11">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="受检器具查询管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

	<div style="width:1000px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:1000px">
				<tr>
               		<td width="120" align="right">标准名称：</td>
				  <td width="152"  align="left"><input id="queryStandardName" name="queryStandardName" class="easyui-combobox" valueField="id" textField="name" style="width:152px" panelHeight="auto"/></td>	
					<td width="120" align="right">器具名称：</td>
				  <td width="152"  align="left"><input id="nameCn" name="nameCn" class="easyui-validatebox"/></td>
                  <td width="120" align="right">认证：</td>
				  <td align="left" colspan="2">
						<input id="querycer1" name="querycer1" value="1" type="checkbox" onclick="uncheckOthers(this)">法定机构授权</input>
						<input id="querycer2" name="querycer2" value="2" type="checkbox" onclick="uncheckOthers(this)">实验室认可</input>
						<input id="querycer3" name="querycer3" value="3" type="checkbox" onclick="uncheckOthers(this)">计量认证</input>
                        <input id="querycerother" name="querycerother" value="4" type="checkbox" onclick="uncheckOthers(this)">其他</input>
				  </td>
				</tr>
                <tr>
               		<td width="120" align="right">检定周期：</td>
				  <td width="152"  align="left"><input id="queryTestCycle" name="queryTestCycle" class="easyui-numberbox"/></td>	
				  <td width="120" align="right">承诺期：</td>
				  <td width="152"  align="left"><input id="queryPromiseDuration" name="queryPromiseDuration" class="easyui-numberbox" /></td>
                  <td width="120" align="right">状态：</td>
				  <td width="250"  align="left">
                  	<select id="querystatus" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
						<option value="0">正常</option>
						<option value="1">注销</option>
					</select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:1000px"></table>

		<div id="edit" class="easyui-window" title="修改" style="padding:10px;width:550px;height:500;" overflow="hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
			<input id="id" name="id" type="hidden"/>
				<div>
					<table id="table3">
						<tr height="30px">
                            <td align="right">器具名称：</td>
                            <td align="left" colspan="3"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">拼音简码：</td>
                            <td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">英文名称：</td>
                            <td align="left"><input id="NameEn" name="NameEn" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
						
						<tr height="30px">
							<td align="right">器&nbsp;&nbsp;&nbsp;&nbsp;具&nbsp;&nbsp;<br/>标准名称：</td>
							<td align="left"><select id="StandardNameId" name="StandardNameId" class="easyui-combobox" valueField="id" textField="name" style="width:152px" required="true" panelHeight="auto"/></td>
							<td align="right">器具编码：</td>
							<td align="left"><input id="Code" name="Code" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr height="30px">
							<td align="right">标准费用：</td>
							<td align="left"><input id="Fee" name="Fee" class="easyui-numberbox" />元</td>
							<td align="right">小修费用：</td>
							<td align="left"><input id="SRFee" name="SRFee" class="easyui-numberbox" />元</td>
						</tr>
						<tr height="30px">
							<td align="right">中修费用：</td>
							<td align="left"><input id="MRFee" name="MRFee" class="easyui-numberbox" />元</td>
							<td align="right">大修费用：</td>
							<td align="left"><input id="LRFee" name="LRFee" class="easyui-numberbox" />元</td>
						</tr>
						<tr height="30px">
							<td width="120" align="right">承诺检出期：</td>
							<td align="left"><input id="PromiseDuration" name="PromiseDuration" class="easyui-numberbox" />天</td>
							<td align="right">检定周期：</td>
							<td align="left"><input id="TestCycle" name="TestCycle" class="easyui-numberbox" />月</td>
						</tr>
						<tr height="30px">
							<td align="right">认证：</td>
							<td align="left" colspan="3">
								<input id="cer1" name="cer1" type="checkbox">法定机构授权</input>&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="cer2" name="cer2" type="checkbox">实验室认可</input>&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="cer3" name="cer3" type="checkbox">计量认证</input>
							</td>
						</tr>
                        <tr height="30px">
							<td align="right">状态：</td>
							<td align="left" colspan="3">
								<select id="Status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
									<option value="0">正常</option>
									<option value="1">注销</option>
								</select>
							</td>
						</tr>
						<tr height="30px">
							<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
							<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="52" rows="4"></textarea></td>
						</tr>
						<tr height="50px">	
							<td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">修改</a></td>
							<td></td>
							<td align="left"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
        <br />
		<div id="p2" class="easyui-panel" style="width:1000px;height:350px;padding:10px;"
				title="型号规格、不确定度、测量范围管理" collapsible="false"  closable="false">
			<table width="100%" height="100%">
			<tr><td>
			<table id="model_info_table" iconCls="icon-tip" ></table>
			 </td><td>
            <table id="accuracy_info_table" iconCls="icon-tip" ></table>
             </td><td>
            <table id="range_info_table" iconCls="icon-tip" ></table>
			</td></tr>
			</table>
		</div>
        
        <div id="add_model_div" class="easyui-window" title="新增型号规格" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_model" method="post">
			<input id="Model_TargetApp_id" name="TargetAppId" type="hidden"/>
            <input id="model_type" name="Type" type="hidden" value="1"/>
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >型号规格：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="model" name="Model" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" >型号规格英文：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="modelEn" name="ModelEn" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitModel()">提交</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		    </table>
			</form>
		</div>
        <div id="edit_model_div" class="easyui-window" title="修改型号规格" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_model" method="post">
			<input id="Model_id" name="id" type="hidden"/>
            <input id="edit_model_type" name="Type" type="hidden" value="1"/>
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >型号规格：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit_model" name="Model" style="width:500px"  class="easyui-validatebox"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" >型号规格英文：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit_modelEn" name="ModelEn" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doEditModel()">提交</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		    </table>
			</form>
		</div>
        
        <div id="add_accuracy_div" class="easyui-window" title="新增不确定度" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_accuracy" method="post">
			<input id="Accuracy_TargetApp_id" name="TargetAppId" type="hidden"/>
            <input id="accuracy_type" name="Type" type="hidden" value="2"/>
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >准确度等级、最大允许误差、不确定度：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="accuracy" name="Accuracy" style="width:500px"  class="easyui-validatebox"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" >准确度等级、最大允许误差、不确定度英文：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="accuracyEn" name="AccuracyEn" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAccuracy()">提交</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		    </table>
			</form>
		</div>
        <div id="edit_accuracy_div" class="easyui-window" title="修改不确定度" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_accuracy" method="post">
			<input id="Accuracy_id" name="id" type="hidden"/>
            <input id="edit_accuracy_type" name="Type" type="hidden" value="2"/>
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >准确度等级、最大允许误差、不确定度：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit_accuracy" name="Accuracy" style="width:500px"  class="easyui-validatebox"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" >准确度等级、最大允许误差、不确定度英文：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit_accuracyEn" name="AccuracyEn" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doEditAccuracy()">提交</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		    </table>
			</form>
		</div>
        
        <div id="add_range_div" class="easyui-window" title="新增测量范围" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_range" method="post">
			<input id="Range_TargetApp_id" name="TargetAppId" type="hidden"/>
            <input id="range_type" name="Type" type="hidden" value="3"/>
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >测量范围：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="range" name="Range" style="width:500px"  class="easyui-validatebox"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" >测量范围英文：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="rangeEn" name="RangeEn" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitRange()">提交</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		    </table>
			</form>
		</div>
        <div id="edit_range_div" class="easyui-window" title="修改测量范围" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_range" method="post">
			<input id="Range_id" name="id" type="hidden"/>
            <input id="edit_range_type" name="Type" type="hidden" value="3"/>
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >测量范围：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit_range" name="Range" style="width:500px"  class="easyui-validatebox"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" >测量范围格英文：</td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit_rangeEn" name="RangeEn" style="width:500px" class="easyui-validatebox"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doEditRange()">提交</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		    </table>
			</form>
		</div>
        
	</div>
    </DIV>
    </DIV>
</body>
</html>
