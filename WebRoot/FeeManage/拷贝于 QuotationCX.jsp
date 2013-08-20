<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>报价单查询</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
     <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	 <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
     <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
     <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
     <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script>
		$(function (){
			$('#Search_Appli').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				valueField:'standardname',
				textField:'standardname',
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
			$('#Customer').combobox({
				valueField:'name',
				textField:'name',
				onChange:function(newValue,oldValue){
					var allData = $(this).combobox('getData');
					if(allData !=null && allData.length >0){
						for(var i=0;i<allData.length; i++){
							if(newValue == allData[i].name){
								return false;
							}
						}
					}
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){
						var newValue = $('#Customer').combobox('getText');
						$('#Customer').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					},700);
				}
			});
			
			$('#table2').datagrid({
				title:'报价单信息查询',
				height:500,
				width:800,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/QuotationServlet.do?method=2',
				
				idField:'num',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'num',title:'编号',width:180,align:'center'},
					{field:'CustomerName',title:'询价单位名称',width:180,align:'center'},
					{field:'Contactor',title:'询价单位联系人',width:120,align:'center'},
					{field:'ContactorTel',title:'联系人电话',width:100,align:'center'},
					{field:'CarCost',title:'交通费',width:80,align:'center'},
					{field:'OfferDate',title:'报价时间',width:80,align:'center'},
					{field:'OffererId',title:'报价人',width:80,align:'center',editor:'text'},
					{field:'Status',title:'状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return '<span style="color:red;">注销</span>'
;
							}
						}
					},
					
	//				{field:'Cost',title:'检测费',width:80,align:'center'},	
					{field:'Version',title:'版本号',width:80,align:'center'},		
					{field:'Remark',title:'备注',width:80,align:'center'},
					{field:'Detail',title:'条目详情',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							$('#quotationId').val(rowData.num);
							return "<a href='javascript:void(0)' class='easyui-linkbutton' iconCls='icon-search' onclick=\"item_Manage('" + rowData.num + "');\" >查看</a>";  
						}
						
					}
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//zhengchang
						return 'color:#000000'
					}else if(rowData.Status == 1){	//zhuxiao
						return 'color:#FF0000';	
					}else{
						return 'color:#000000';
					}
				},
				toolbar:[{
					text:'询价单位信息修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#number').val(select.num);
							$('#ContactorTel').val(select.ContactorTel);
							$('#Contactor').val(select.Contactor);
							$('#Customername').val(select.CustomerName);
							
							$('#quotation_edit').window('open');	
						}else{
							$.messager.alert('warning','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'报价单条目修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#quotationId').val(select.num);
							$('#table4').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
							$('#table4').datagrid('options').queryParams={'quotationId':encodeURI($('#quotationId').val())};
							$('#table4').datagrid('reload');
							$('#item_edit').window('open');	
						}else{
							$.messager.alert('warning','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'注销',
					iconCls:'icon-remove',
					handler:function(){
							var rows = $('#table2').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('警告','确认注销吗？',function(r){
								if(r){
									for(var i=rows.length-1; i>=0; i--){
										$.ajax({
											type:'POST',
											url:'/jlyw/QuotationServlet.do?method=4',
											data:'id='+rows[i].num,
											dataType:"json",
											success:function(data, textStatus){
												alert(data.msg);
											}
										});
									}
									$('#table2').datagrid('reload');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
					text:'打印所选报价单',
					iconCls:'icon-print',
					handler:function(){
							var row = $('#table2').datagrid('getSelected');
							
							if(row)
							{
								$('#quotationId1').val(row.num);
								$('#printquotation').submit();
								
				
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
					text:'导出所选报价单',
					iconCls:'icon-save',
					handler:function(){
					  var row = $('#table2').datagrid('getSelected');
							
					   if(row){
							ShowWaitingDlg("正在导出，请稍后......");
							$('#paramsStr').val(row.num);
							$('#contactorcel').val(row.ContactorTel);
							$('#contactor').val(row.Contactor);
							$('#customername').val(row.CustomerName);
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
					  }else{
							$.messager.alert('提示','请选择一行数据','warning');
					  }
					  
				}	
			}]
			});
			
		});
		
		//添加条目
		$(function(){
			$('#table_appli').datagrid({
		//		title:'受检器具信息',
				width:750,
				height:450,
				singleSelect:true,
				fit: false,
				nowrap: true,
				striped: true,
				rownumbers:false,
				loadMsg:'数据加载中......',
//				url:'/jlyw/QuotationServlet.do?method=0',
				remoteSort: false,
				frozenColumns:[[
					{field:'ck',checkbox:true},
					{field:'TargetApplianceId',title:'受检器具编号',width:100,sortable:true,align:'center'}
				]],
				columns:[[
					{field:'StandardNameName',title:'器具标准名称',width:100,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Accuracy',title:'精度',width:80,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Fee',title:'标准费用',width:80,align:'center'}
				]],
				pagination:true,
				toolbar:"#table_appli-search-toolbar"
			});
		});
		
		//报价单条目
		$(function(){
			var lastIndex;
			$('#table4').datagrid({
				width:780,
				height:430,
             	singleSelect:true, 
                nowrap: false,
                striped: true,
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Id',title:'编号',width:40,align:'center'},
					
					{field:'StandardName',title:'受检器具标准名称',width:120,align:'center'},
					{field:'CertificateName',title:'受检器具证书名称',width:120,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Accuracy',title:'精度',width:80,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Quantity',title:'台件数',width:80,align:'center',editor:'text'},
					{field:'CertType',title:'证书类型',width:80,align:'center',
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
					{field:'SiteTest',title:'现场检测',width:80,align:'center',
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
					text:'删除条目',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("确定要移除这些条目吗？");
						if(result == false){
							return false;
						}
						var row = $('#table4').datagrid('getSelected');
						if(!row){
							$.messager.alert('提示','请选择需要移除的条目！','info');
							return false;
						}
						else{
							var rows = $('#table4').datagrid('getSelections');
							var length = rows.length;
							for(var i=length-1; i>=0; i--){
								var index = $('#table4').datagrid('getRowIndex', rows[i]);
								$('#table4').datagrid('deleteRow', index);
							}
						}
					}
				},'-',{
					text:'修改条目',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table4').datagrid('getSelected');
						if(select){
							$('#quo_edit').window('open');
							$('#form1').show();
							
							
							
							//证书常用名称
							$('#CertificateName').combobox({
								url:'/jlyw/AppliancePopularNameServlet.do?method=7&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
							});
							
							//
							$('#Model1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=1&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'

							});
							//
							$('#Range1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
	
							});
							//
							$('#Accuracy1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'

							});
							$('#Accuracy1').combobox('setValue',select.Accuracy);
							$('#Range1').combobox('setValue',select.Range);
							$('#Model1').combobox('setValue',select.Model);
																						
							$('#Application').val(select.StandardName);
							$('#Quantity').val(select.Quantity);
							$('#SiteTest').val(select.SiteTest);
							$('#CertificateName').combobox('setValue',select.CertificateName);
							$('#CertType').val(select.CertType);
							$('#Remark').val(select.Remark);
					}else{
						$.messager.alert('警告','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'添加条目',
					iconCls:'icon-add',
					handler:function(){	
						$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
						$('#table_appli').datagrid('reload');
						$('#item_add').window('open');
						$('#form3').show();
					}
				},'-',
				{
					text:'确认修改',
					iconCls:'icon-ok',
					handler:function(){
						var result = confirm("确定要修改吗？（注销的报价单将恢复正常）");
						if(result == false){
							return false;
						}
						var rows = $('#table4').datagrid('getRows');
						if(rows.length == 0){
							$.messager.alert('提示','该报价单条目为空，不能生成报价单！','info');
							return false;
						}
						
						$('#form2').form('submit',{
							url:'/jlyw/QuotationServlet.do?method=3',
							onSubmit:function(){
								var selects = $('#table2').datagrid('getSelected');
								if(selects.length==0){
									$.messager.alert('提示','请先选择报价单！','info');
									return false;
								}
								
								$("#quotationId").val(JSON.stringify(selects));
								$("#Item").val(JSON.stringify(rows));
								
								return $('#form2').form('validate');
							},
							success:function(data){
								var result = eval("("+data+")");
								alert(result.msg);
								if(result.IsOK){
									$('#item_edit').window('close');
									cancel();
									$('#table2').datagrid('reload');
								}
							}
						});
						$('#item_edit').window('close');
						
					}	
				}]
			});
		})	
		

		//根据报价单编号和询价单位查询报价单
		function query(){
			$('#table2').datagrid('options').url='/jlyw/QuotationServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'QuotationId':encodeURI($('#QuotationId').val()),'Customer':encodeURI($('#Customer').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getText')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getText')),'QuoStatus':encodeURI($('#QuoStatus').val())};
			$('#table2').datagrid('reload');
			$('#QuotationId').val('');
		}
		
		//修改报价单条目
		function edit(){
			if($('#Quantity').val()==''){
				$.messager.alert('提示','请将数据填写完整！','info');
			}
			else{
				if($('#Quantity').val()=='0'){
					$.messager.alert('提示','请输入大于0的台件数！','info');
					return false;
				}
				var row = $('#table4').datagrid('getSelected');
				var index= $('#table4').datagrid('getRowIndex',row);
				var name_obj=document.getElementById("SiteTest");
				var certype_obj=document.getElementById("CertType");
				
				$.ajax({   //根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
					type:'POST',
					url:'/jlyw/TargetApplianceServlet.do?method=13',
					data:{'Application':encodeURI($('#Application').val()),'Model':encodeURI($('#Model1').combobox('getText')),'Range':encodeURI($('#Range1').combobox('getText')),'Accuracy':encodeURI($('#Accuracy1').combobox('getText'))},
					dataType:"json",
					success:function(data, textStatus){
						if(data.IsOK){//费用取得成功		
						 			
							$('#table4').datagrid('updateRow',{
								index:index,
								row:{
									CertificateName:$('#CertificateName').combobox('getText'),
									Model:$('#Model1').combobox('getText'),
									Accuracy:$('#Accuracy1').combobox('getText'),
									Range:$('#Range1').combobox('getText'),
									Quantity:$('#Quantity').val(),
									CertType:certype_obj.options[certype_obj.selectedIndex].value,
									SiteTest:name_obj.options[name_obj.selectedIndex].value,
									MinCost: data.MinFee,
									MaxCost: data.MaxFee,
									Remark:$('#Remark').val()
								}
							});							
							$('#quo_edit').window('close');
							
						}else{//费用取得失败
							$.messager.alert('错误！',data.msg,'error');
						}
					}
				});
		
				
				
			}
		}
		
		//取消报价单修改
		function cancel(){
			$('#Quantity').val('');
			$('#Remark').val('');
			
			$('#quo_edit').window('close');
		}
		
		function item_Manage(number){
			$('#quotationId').val(number);
			$('#table4').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
			$('#table4').datagrid('options').queryParams={'quotationId':encodeURI($('#quotationId').val())};
			$('#table4').datagrid('reload');
			$('#item_edit').window('open');	
		}
		
		//查询受检器具
		function query_appli(){
			$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
			$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getText'))};
			$('#table_appli').datagrid('reload');
		}
		
		//新增报价单条目，对数据等信息进行填写
		function Add_QuotaItem(){
			
			var row=$('#table_appli').datagrid('getSelected');
			if(row){
				
									
				//证书常用名称
				$('#CertificateName1').combobox({
					url:'/jlyw/AppliancePopularNameServlet.do?method=7&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Model').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=1&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Range').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Accuracy').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				$('#Accuracy').combobox('setValue',row.Accuracy);
				$('#Range').combobox('setValue',row.Range);
				$('#Model').combobox('setValue',row.Model);
				var rows=$('#table_appli').datagrid('getSelections');
				for(var i=0; i<rows.length; i++){
					$('#Application1').val(rows[i].StandardNameName);
				}
				$('#add').window('open');
			}
			else{
				$.messager.alert('提示','请选择一组数据!','info');
			}
		}
		
		//将报价单条目加入datagrid
		function edit_item(){	
			if($('#Quantity1').val()==''){
				$.messager.alert('提示','请将数据填写完整！','info');
			}
			else{
				if($('#Quantity1').val()=='0'){
					$.messager.alert('提示','请输入大于0的台件数！','info');
					return false;
				}																
				var table_appli_row = $('#table_appli').datagrid('getSelected');
				var table4_rows = $('#table4').datagrid('getRows');
				var index = $('#table4').datagrid('getRows').length;
				var name_obj=document.getElementById("SiteTest1");
				var certype_obj=document.getElementById("CertType1");
				
				for(var j=0;j<index;j++){
					if(table4_rows[j].StandardName==table_appli_row.StandardNameName && table4_rows[j].Model==$('#Model').combobox('getText') && table4_rows[j].Range==$('#Range').combobox('getText') && table4_rows[j].Accuracy==$('#Accuracy').combobox('getText'))
					{
						$.messager.alert('提示','选择了已有受检器具！','warning');
						return false;
					}
				}
								
				$.ajax({   //根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
					type:'POST',
					url:'/jlyw/TargetApplianceServlet.do?method=13',
					data:{'Application':encodeURI($('#Application1').val()),'Model':encodeURI($('#Model').combobox('getText')),'Range':encodeURI($('#Range').combobox('getText')),'Accuracy':encodeURI($('#Accuracy').combobox('getText'))},
					dataType:"json",
					success:function(data, textStatus){
						if(data.IsOK){//费用取得成功							
							$('#table4').datagrid('insertRow',{
								index:index,
								row:{
									CertificateName:$('#CertificateName1').combobox('getText'),
									StandardName: table_appli_row.StandardNameName,
									TargetApplianceId: table_appli_row.TargetApplianceId,
									Model:$('#Model').combobox('getText'),
									Accuracy:$('#Accuracy').combobox('getText'),
									Range:$('#Range').combobox('getText'),
									Quantity:$('#Quantity1').val(),
									CertType:certype_obj.options[certype_obj.selectedIndex].value,
									SiteTest:name_obj.options[name_obj.selectedIndex].value,
									MinCost: data.MinFee,
									MaxCost: data.MaxFee,
									Remark:$('#Remark1').val()
								}
							});							
							
							$('#add').dialog('close');
							$('#item_add').dialog('close');
							$('#table_appli').datagrid('clearSelections');
							$('#table2').datagrid('reload');
						}else{//费用取得失败
							$.messager.alert('错误！',data.msg,'error');
						}
					}
				});
			
			
				
			}
		}
		
		//取消将报价单条目加入datagrid中
		function cancel_item(){
			$('#Quantity1').val('');
			$('#CertificateName1').val('');
			$('#CertType1').val('');
			$('#Remark1').val('');
			$('#add').window('close');
		}
		
		//编辑询价单位信息
		function edit_Quotation(){	
		
			$('#quotation_edit_form').form('submit',{
				url:'/jlyw/QuotationServlet.do?method=10',
				onSubmit:function(){
					
					return $('#quotation_edit_form').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					
					if(result.IsOK){
						$.messager.alert('提示！','修改成功！','info');
						$('#quotation_edit').window('close');
						$('#table2').datagrid('reload');
					}else{
						$.messager.alert('错误！',result.msg,'error');
					}
				}
			});
			
		}
		
		function cancel_Quotation(){
			$('#quotation_edit').window('close');
		}
	</script>
</head>

<body >
<form id="frm_export" method="post" action="/jlyw/QuotationServlet.do?method=9">
<input id="paramsStr" name="paramsStr" type="hidden" />
<input id="customername" name="customername" type="hidden" />
<input id="contactortel" name="contactortel" type="hidden" />
<input id="contactor" name="contactor" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="报价单查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<div  align="center" style="width:800px;padding:10px" class="easyui-panel" title="统计条件" collapsible="false"  closable="false">
			<table id="table1" style="width:780px;">
				<tr>
						
					<td align="left">
					报价单编号：<input id="QuotationId" name="QuotationId" type="text" style="width:120px;"/>&nbsp;
				    询价单位：<input id="Customer" name="Customer" type="text" style="width:120px;"/>&nbsp;
					<select name="QuoStatus" id="QuoStatus" style="width:100px"><option value="" selected="selected">全部</option><option value="0">正常</option><option value="1" >注销</option></select>&nbsp;
					</td>
					</tr>
				<tr>
					<td align="left">
					&nbsp;&nbsp;起始时间：<input name="date1" id="dateTimeFrom" type="text" style="width:123px;"  class="easyui-datebox" >&nbsp;					
					结束时间：<input name="date2" id="dateTimeEnd" type="text" style="width:122px;"  class="easyui-datebox" >&nbsp;
					
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
					</td>
					
				</tr>
			</table>
	   </div>
	   
	   <div style="+position:relative;">
			<table id="table2" iconCls="icon-search" > </table>	
		</div>
		
		<div id="quo_edit" class="easyui-window" title="修改报价单条目" style="padding:10px;width:280px;height:320px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" >
			<form id="form1" method="post">
				<table id="table3">
					<tr height="25px">
						<td align="right">受检器具:</td>
						<td align="left"><input id="Application" name="Application" type="text" readonly="readonly"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">型号规格:</td>
						<td align="left"><input id="Model1" name="Model" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">测量范围:</td>
						<td align="left"><input id="Range1" name="Range" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">准确度等级:</td>
						<td align="left"><input id="Accuracy1" name="Accuracy" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px">
						<td align="right">台/件&nbsp;数:</td>
						<td align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox" value="1" required="true" /></td>
					</tr>
					<tr height="25px">
						<td align="right">现场检验:</td>
						<td align="left">
							<select id="SiteTest" name="SiteTest" style="width:152px">   
								<option value="1" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">受检器具&nbsp;<br />证书名称:</td>
						<td align="left"><input id="CertificateName" name="CertificateName" type="text" style="width:152px"//></td>
					</tr>
					<tr height="25px">
						<td align="right">证书类型:</td>
						<td align="left">
							<select id="CertType" name="CertType" style="width:152px" />
								<option value="1" selected="selected">检定</option>
								<option value="4">检验</option>
								<option value="2">校准</option>
								<option value="3">检测</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
						<td align="left"><input id="Remark" name="Remark" type="text" /></td>
					</tr>
					<tr height="30px">	
						<td align="right" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">确定</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a></td>
					
					</tr>
				</table>
				
			</form>
		</div>
		
		<div id="item_edit" class="easyui-window" title="报价单条目信息查看" style="width: 800px;height: 500px;"
		iconCls="icon-search" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" name="form2" method="post">
				<input id="quotationId" name="quotationId" type="hidden" />
				<input id="Item" name="Item" type="hidden" /> 
				<table id="table4" iconCls="icon-search" style="height:430px; width:780px"></table>	
			</form>
		</div>
		
		<div id="item_add" class="easyui-window" title="添加条目" style="width:800px;height:500px" iconCls="icon-search" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			
				<table id="table_appli" iconCls="icon-search"> </table>	
			
		</div>
		
		
		<div id="table_appli-search-toolbar" style="padding:2px 0">
			<form id="form3" name="form3" method="post">
				<table cellpadding="0" cellspacing="0" style="width:100%">
					<tr>
						<td style="padding-left:2px">
							<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="Add_QuotaItem()">加入报价单条目</a>
						</td>
						<td align="right">受检器具标准名:</td>
						<td width="21%"><input id="Search_Appli" name="Search_Appli" type="text" style="width:152px" /></td>
						<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query_appli()">查询</a></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="add" class="easyui-window" title="添加报价单条目" style="width:280px;height:320px;padding:10px"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true" >
			<form id="form4" method="post">
				<table id="table5">
					<tr height="25px">
						<td align="right">受检器具:</td>
						<td align="left"><input id="Application1" name="Application" type="text" readonly="readonly"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">型号规格:</td>
						<td align="left"><input id="Model" name="Model" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">测量范围:</td>
						<td align="left"><input id="Range" name="Range" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px" >
						<td align="right">准确度等级:</td>
						<td align="left"><input id="Accuracy" name="Accuracy" class="easyui-combobox" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px">
						<td align="right">台/件&nbsp;数:</td>
						<td align="left"><input id="Quantity1" name="Quantity" type="text" class="easyui-numberbox" value="1" required="true" /></td>
					</tr>
					<tr height="25px">
						<td align="right">现场检验:</td>
						<td align="left">
							<select id="SiteTest1" name="SiteTest" style="width:152px">   
								<option value="1" selected="selected">否</option>
								<option value="0">是</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">受检器具&nbsp;<br />证书名称:</td>
						<td align="left"><input id="CertificateName1" name="CertificateName" type="text" style="width:152px"/></td>
					</tr>
					<tr height="25px">
						<td align="right">证书类型:</td>
						<td align="left">
							<select id="CertType1" name="CertType" style="width:152px" />
								<option value="1" selected="selected">检定</option>
								<option value="4">检验</option>
								<option value="2">校准</option>
								<option value="3">检测</option>
							</select>
						</td>
					</tr>
					<tr height="25px">
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
						<td align="left"><input id="Remark1" name="Remark" type="text" /></td>
					</tr>
					<tr height="30px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit_item()">确定</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_item()">取消</a></td>
						
					</tr>
				</table>
				
			</form>
		</div>
		
		<div id="quotation_edit" class="easyui-window" title="报价单基本信息条目" style="width:280px;padding:10px"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true" >
			<form id="quotation_edit_form" method="post">
			<input id="number" name="number" type="hidden" readonly="readonly"/>
				<table >
					<tr height="35px">
						<td align="right">询价单位:</td>
						<td align="left"><input id="Customername" name="CustomerName" class="easyui-validatebox" required="true" type="text" readonly="readonly"/></td>
					</tr>
					<tr height="35px" >
						<td align="right">单位联系人:</td>
						<td align="left"><input id="Contactor" name="Contactor" class="easyui-validatebox" required="true"   type="text" style="width:152px"/></td>
					</tr>
					<tr height="35px" >
						<td align="right">联系人电话:</td>
						<td align="left"><input id="ContactorTel" name="ContactorTel" class="easyui-numberbox" required="true"  type="text" style="width:152px"/></td>
					</tr>
					
					<tr height="40px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit_Quotation()">确定</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_Quotation()">关闭</a></td>
						
					</tr>
				</table>
				
			</form>
		</div>
		
		<form id="printquotation" name="printquotation" method="post" action="/jlyw/QuotationServlet.do?method=7" target="QuotPrintFrame">
				<input id="quotationId1" name="quotationId" type="hidden" />
				
		</form>
		
		
	
		<iframe id="QuotPrintFrame" name="QuotPrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	</div></DIV>
</body>

</html>
