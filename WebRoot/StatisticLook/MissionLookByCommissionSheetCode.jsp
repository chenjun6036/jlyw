<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>业务查询</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
	<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>
	 <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
   </object>
   <script type="text/javascript" src="../WebPrint/printer.js"></script>
	<script type="text/javascript" src="../WebPrint/printCommisionSheet.js"></script>
  
	<script>
		$(function(){
			$('#OriginalRecord').datagrid({
				title:'记录/证书信息',
				width:980,
				height:300,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
	//			collapsible:true,
				url:'/jlyw/OriginalRecordServlet.do?method=13',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: false,
				idField:'OriginalRecordId',
				frozenColumns:[[
					{field:'CertificateCode',title:'证书编号',width:80,align:'center'},
					{field:'ApplianceStandardName',title:'器具标准名称',width:80,align:'center'}
				]],
				columns:[
					[
						{title:'器具信息',colspan:7,align:'center'},
						{title:'检验信息',colspan:7,align:'center'},
						{title:'费用信息',colspan:7,align:'center'}
					],[
						{field:'Model',title:'规格型号',width:60,align:'center'},
						{field:'Range',title:'测量范围',width:80,align:'center'},
						{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
						{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
						{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
						{field:'ManageCode',title:'管理编号',width:80,align:'center'},
						{field:'Quantity',title:'数量',width:80,align:'center'},
						
						{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
						{field:'Staff',title:'检/校人员',width:80,align:'center'},
						{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
						{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
							formatter:function(value, rowData, rowIndex){
								if(value=="" || value==null || rowData.ExcelDoc == ""){
									return "";
								}else{
									if(rowData.ExcelPdf == ""){
										return "<span style='color: #FF0000'>未完成</span>";
									}else{
										return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
									}
								}
							}},
						{field:'CertificateId',title:'证书文件',width:80,align:'center',
							formatter:function(value, rowData, rowIndex){
								if(value=="" || rowData.CertificateDoc==""||value==null){
									return "";
								}else{
									if(rowData.CertificatePdf == ""){
										return "<span style='color: #FF0000'>未完成</span>";
									}else{
										return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
									}
								}
							}
						},
						{field:'VerifierName',title:'核验人',width:80,align:'center',
							formatter:function(value, rowData, rowIndex){
								if(value==null||value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
									return "";
								}else{
									if(rowData.VerifyResult == ""){		//尚未审批
										return "<span title='尚未核验'>"+value+"</span>";
									}
									else if(rowData.VerifyResult == "1" || rowData.VerifyResult == 1 ){ //核验通过
										return "<span style='color: #0033FF' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
									}else{	//核验未通过
										return "<span style='color: #FF0000' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：未通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
									}
								}						
							}
						},
						{field:'AuthorizerName',title:'批准人',width:80,align:'center',
							formatter:function(value, rowData, rowIndex){
								if(value==null||value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
									return "";
								}else{
									if(rowData.AuthorizeResult == ""){		//尚未审批
										return "<span title='尚未签字'>"+value+"</span>";
									}
									else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //批准通过
										return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
									}else{	//批准未通过
										return "<span style='color: #FF0000' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：未通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
									}
								}
							}
						},
						
						{field:'TestFee',title:'检测费',width:60,align:'center'},
						{field:'RepairFee',title:'修理费',width:60,align:'center'},
						{field:'MaterialFee',title:'材料费',width:60,align:'center'},
						{field:'CarFee',title:'交通费',width:60,align:'center'},
						{field:'DebugFee',title:'调试费',width:60,align:'center'},
						{field:'OtherFee',title:'其他费用',width:60,align:'center'},
						{field:'TotalFee',title:'总计费用',width:60,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true,
				showFooter:true
			});
			
			$('#subcontract_info_table').datagrid({
				title:'转包业务信息',
	//			iconCls:'icon-save',
				width:980,
				height:300,
				//closed:true,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
	//			collapsible:true,
				url:'/jlyw/SubContractServlet.do?method=3',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: false,
				idField:'SubContractId',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[
					[
						{title:'转包方信息',colspan:3,align:'center'},
						{title:'转包业务信息',colspan:7,align:'center'}
					],[
						{field:'SubContractorName',title:'转包方',width:80,sortable:true,align:'center'},
						{field:'SubContractorContactor',title:'联系人',width:60,align:'center'},
						{field:'SubContractorContactorTel',title:'联系电话',width:80,align:'center'},
						{field:'SubContractDate',title:'转包时间',width:80,align:'center'},
						{field:'Handler',title:'转包人',width:80,align:'center'},
						{field:'ReceiveDate',title:'接收时间',width:80,align:'center'},
						{field:'Receiver',title:'接收人',width:80,align:'center'},
						{field:'Remark',title:'备注信息',width:100,align:'center'},
						{field:'LastEditor',title:'最后编辑人',width:80,align:'center'},
						{field:'LastEditTime',title:'最后编辑时间',width:80,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true	,
				onSelect:function(rowIndex, rowData){
					//更新附件表格文件信息
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.Attachment};
					$('#uploaded_file_table').datagrid('reload');
				}
			});
			$('#withdraw_info_table').datagrid({
				title:'退样信息',
	//			iconCls:'icon-save',
				width:980,
				height:300,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
	//			collapsible:true,
				url:'/jlyw/WithdrawServlet.do?method=0',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: false,
				//idField:'userid',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[
					[
						{title:'申请信息',colspan:5,align:'center'},
						{title:'办理信息',colspan:6,align:'center'}
					],[
						{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
						{field:'WithdrawNumber',title:'退样数量',width:60,align:'center'},
						{field:'Reason',title:'退样原因',width:80,align:'center'},
						{field:'WithdrawDesc',title:'退样样品描述',width:80,align:'center'},
						{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
						
						
						{field:'ExecutorName',title:'办理人',width:80,align:'center'},
						{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
						{field:'ExecutorResult',title:'办理结果',width:80,align:'center',
							formatter:function(value,rowData,rowIndex){
								if(0 == value) {
									return '<span style="color:red;">驳回</span>';
								}
								if(1 == value){
									return "同意退样";
								}
								return "";
							}	
						},
						{field:'WithdrawDate',title:'退样日期',width:80,align:'center'},
						{field:'Location',title:'样品存放位置',width:80,align:'center'},
						{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true	
			});
			
			$('#overdue_info_table').datagrid({
				title:'超期信息',
	//			iconCls:'icon-save',
				width:980,
				height:300,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
	//			collapsible:true,
				url:'/jlyw/OverdueServlet.do?method=0',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: false,
				//idField:'userid',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[
					[
						{title:'申请信息',colspan:4,align:'center'},
						{title:'办理信息',colspan:4,align:'center'}
					],[
						{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
						{field:'DelayDays',title:'延期天数',width:60,align:'center'},
						{field:'Reason',title:'超期原因',width:80,align:'center'},
						{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
						
						{field:'ExecutorName',title:'办理人',width:80,align:'center'},
						{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
						{field:'ExecutorResult',title:'办理结果',width:80,align:'center',
							formatter:function(value,rowData,rowIndex){
								if(0 == value) {
									return '<span style="color:red;">驳回</span>';
								}
								if(1 == value){
									return "同意超期";
								}
								return "";
							}	
						},
						{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true
			});
			
			$('#discount_info_table').datagrid({
				title:'折扣申请信息',
	//			iconCls:'icon-save',
				width:980,
				height:300,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
	//			collapsible:true,
				url:'/jlyw/DiscountServlet.do?method=5',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: false,
				//idField:'userid',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{title:'折扣申请信息',colspan:6,align:'center'},
					{title:'折扣办理信息',colspan:4,align:'center'},
			        {title:'委托单信息',colspan:18,align:'center'}					
				],[
					{field:'CustomerName',title:'申请单位',width:160,sortable:true,align:'center'},
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'Reason',title:'申请原因',width:150,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:150,align:'center'},
					{field:'Contector',title:'委托方经办人',width:90,align:'center'},
					{field:'ContectorTel',title:'经办人电话',width:90,align:'center'},
					{field:'ExecutorName',title:'办理人',width:80,sortable:true,align:'center'},
					{field:'ExecutorResult',title:'办理结果',width:80,sortable:true,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==1)
							return "通过";
						else if(value==0)
							return "驳回";
						else
							return "";
					}},
					{field:'ExecuteTime',title:'办理时间',width:80,sortable:true,align:'center'},
					{field:'ExecuteMsg',title:'办理备注信息',width:80,sortable:true,align:'center'},
					{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					
					{field:'Status',title:'委托单状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'OldTestFee',title:'检验费原价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TestFee',title:'检验费现价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldRepairFee',title:'维修费原价',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
					}},
					{field:'RepairFee',title:'维修费现价',width:80,align:'right',formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
					}},
					{field:'OldMaterialFee',title:'材料费原价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'MaterialFee',title:'材料费现价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldCarFee',title:'交通费原价',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'CarFee',title:'交通费现价',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldDebugFee',title:'调试费原价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'DebugFee',title:'调试费现价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldOtherFee',title:'其他费原价',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OtherFee',title:'其他费现价',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldTotalFee',title:'总费用原价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TotalFee',title:'总费用现价',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}}
					
					
				]],
				pagination:false,
				rownumbers:true
			});
			
			$('#uploaded_file_table').datagrid({
				title:'转包业务已上传的文件',			
				iconCls:'icon-tip',
				idField:'_id',
				width:980,
				height:200,
				//closed:true,
				//fit:true,
				singleSelect:true,
				rownumbers:true,
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:"filename",title:"文件名",width:130,align:"left", 
						formatter : function(value,rowData,rowIndex){
							return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='点击下载该文件' ><span style='color: #0033FF'>"+value+"</span></a>"
						}
					},
					{field:"length",title:"大小",width:60,align:"left"},
					{field:"uploadDate",title:"上传时间",width:120,align:"left"},
					{field:"uploadername",title:"上传人",width:60,align:"left"}
				]]
			});
			
			$('#attachment_info_table').datagrid({
				title:'委托单已上传的附件',			
				iconCls:'icon-tip',
				idField:'_id',
				width:980,
				height:300,
				//closed:true,
				//fit:true,
				singleSelect:true,
				rownumbers:true,
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:"filename",title:"文件名",width:130,align:"left", 
						formatter : function(value,rowData,rowIndex){
							return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='点击下载该文件' ><span style='color: #0033FF'>"+value+"</span></a>"
						}
					},
					{field:"length",title:"大小",width:60,align:"left"},
					{field:"uploadDate",title:"上传时间",width:120,align:"left"},
					{field:"uploadername",title:"上传人",width:60,align:"left"}
				]]
			});
			
			var StatusList = [
				{id:0,name:'已收件'},
				{id:1,name:'已分配'},
				{id:2,name:'转包中'},
				{id:3,name:'已完工'},
				{id:4,name:'已结账'},
				{id:9,name:'已结束'},
				{id:10,name:'已注销'},
				{id:-1,name:'预留中'}
			];
			
			$('#CommissionStatus').combobox({
				data:StatusList,
				valueField:'id',
				textField:'name'
			});
			
			$('#fee_assign_table').datagrid({
				title:'费用分配信息',
				singleSelect:false, 
				height: 300,
	//			width:800,
				nowrap: false,
				striped: true,
				url:'/jlyw/CertificateFeeAssignServlet.do?method=2',
				remoteSort: false,
				showFooter:true,
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[
					[
						{field:'CertificateCode',title:'证书编号',width:70,sortable:true,align:'center'},
						{field:'FeeAlloteeName',title:'姓名',width:70,align:'center'},
						{field:'TotalFee',title:'总费用',width:50,align:'center'},
						{field:'TestFee',title:'检定费',width:50,align:'center'},
						{field:'RepairFee',title:'修理费',width:50,align:'center'},
						{field:'MaterialFee',title:'材料费',width:50,align:'center'},
						{field:'CarFee',title:'交通费',width:50,align:'center'},
						{field:'DebugFee',title:'调试费',width:50,align:'center'},
						{field:'OtherFee',title:'其它费',width:50,align:'center'},
						{field:'Remark',title:'备注',width:380,align:'center'}
					]
				],
				rownumbers:true	,
				pagination:false
			});
			
			doLoadCommissionSheet();
			
		});	
		

		var printObj;
		function doLoadCommissionSheet(){	//查找委托单
			$("#SearchForm").form('submit', {
				url:'/jlyw/CommissionSheetServlet.do?method=10',
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					$("#CommissionSheetForm").form('clear');
					
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
					$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
					
					$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':''};
					$('#fee_assign_table').datagrid('reload');
					
					$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':''};
					$('#subcontract_info_table').datagrid('loadData',{total:0,rows:[]});
					
					$('#discount_info_table').datagrid('options').queryParams={'CommissionId':''};
					$('#discount_info_table').datagrid('loadData',{total:0,rows:[]});
					
					$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':''};
					$('#overdue_info_table').datagrid('loadData',{total:0,rows:[]});
					
					$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':''};
					$('#withdraw_info_table').datagrid('loadData',{total:0,rows:[]});				
					
					//清空附件列表
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
					$('#uploaded_file_table').datagrid('reload');
					
					$('#attachment_info_table').datagrid('options').queryParams={'FilesetName':''};
					$('#attachment_info_table').datagrid('reload');

					return $("#SearchForm").form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK){
						$("#CommissionSheetForm").form('load',result.CommissionObj);
						printObj = result.PrintObj;
						if(result.CommissionObj.Ness == 0){
							$("#Ness").attr("checked",true);	//勾选
						}
						
						//加载原始记录信息
						$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#OriginalRecord').datagrid('reload');
						
						//加载费用分配信息
						$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':$('#CommissionId').val()};
						$('#fee_assign_table').datagrid('reload');
						
						//加载转包信息
						$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#subcontract_info_table').datagrid('reload');
						
						//加载折扣信息
						$('#discount_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#discount_info_table').datagrid('reload');
						
						//加载任务分配信息
						$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#overdue_info_table').datagrid('reload');
						
						//加载退样信息
						$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#withdraw_info_table').datagrid('reload');
						
						//加载附件信息
						$('#attachment_info_table').datagrid('options').queryParams={'FilesetName':result.CommissionObj.Attachment};
						$('#attachment_info_table').datagrid('reload');	
											
					}else{
						$.messager.alert('查询失败！',result.msg,'error');
					}
				}
			});  
		}
		
		function printCom(){
			Preview1(printObj);
		}
		
		function goback(){
			history.go(-1);
		}
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="业务查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1050px;height:80px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post">
            <table width="850px" id="table1">        
				<tr >
					<td>委托单编号：</td>
					<td>
						<input id="Code" type="text" class="easyui-validatebox" name="Code" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>"  />
                        <input style="display:none"/>
					</td>
				    <td align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>
                    <td align="center"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="goback()">返回</a></td>
				</tr>
				
			</table>
        </form>
		</div>
        <br />
        <div id="Details" class="easyui-tabs" style="width:1050px;height:400px;">
        	<div style="padding:20px;" title="委托单信息">
                <form id="CommissionSheetForm" method="post">
                <input type="hidden" id="CommissionId" name="CommissionId" value="" />
                <input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
                <input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
                <table width="1000px" id="table1">
                    <tr>
                      <td width="77" align="right">委托形式：</td>
                      <td width="187"  align="left">
                            <select name="CommissionType" style="width:152px" class="easyui-combobox">
                                <option value="1">送样检测</option>
                                <option value="5">其它业务</option>
                                <option value="6">自检业务</option>
                                <option value="7">现场带回</option>
                                <option value="3">公正计量</option>
                                <option value="4">形式评价</option>
                                <option value="2">现场检测</option>
                            </select>
                      </td>
                      <td width="77" align="right">委托日期：</td>
                      <td width="187"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
                      <td width="100"  align="right">委托单状态：</td>
                      <td width="187" align="left"><input style="width:151px;" class="easyui-combobox" name="CommissionStatus" id="CommissionStatus" editable="false"/></td>
                      <td width="120" align="right">报告形式：</td>
					  <td width="187" align="left"><select id="ReportType" name="ReportType" style="width:152px">
						<option value="1">检定</option>
						<option value="2">校准</option>
						<option value="3">检测</option>
						<option value="4">检验</option>
					</select></td>
                    </tr>
                <tr>
                  <td align="right">委托单位：</td>
                  <td align="left"><input type="text" name="CustomerName" id="CustomerName" readonly="readonly"/></td>
                  <td align="right">电&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
                  <td align="left"><input name="CustomerTel" id="CustomerTel" type="text" readonly="readonly"/></td>
                  <td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
                  <td align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" readonly="readonly"/></td>
                  <td align="right">邮政编码：</td>
                  <td align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">联&nbsp;系&nbsp;人：</td>
                    <td align="left"><input name="ContactPerson" id="ContactPerson" type="text" readonly="readonly"/></td>
                    <td align="right">手机号码：</td>
                    <td align="left"><input name="ContactorTel" id="ContactorTel" type="text" readonly="readonly"/></td>
                    <td align="right">证书单位：</td>
                    <td align="left"><input name="SampleFrom" id="SampleFrom" type="text" readonly="readonly"/></td>
                    <td align="right">开票单位：</td>
                    <td align="left"><input name="BillingTo" id="BillingTo" type="text" readonly="readonly"/></td>
                </tr>
   				<tr>
                    <td align="right">企业分类：</td>
                    <td align="left"><input id="CustomerClassification" name="CustomerClassification" type="text" readonly="readonly"/></td>
                    <td align="right">接&nbsp;收&nbsp;人：</td>
                    <td align="left"><input id="Receiver" name="Receiver" type="text" readonly="readonly"/></td>
                    <td align="right">完&nbsp;工&nbsp;人：</td>
                    <td align="left"><input id="FinishStaff" name="FinishStaff" type="text" readonly="readonly"/></td>
                    <td align="right">完工日期：</td>
                    <td align="left"><input id="FinishDate" name="FinishDate" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">结&nbsp;账&nbsp;人：</td>
                    <td align="left"><input id="CheckOutStaff" name="CheckOutStaff" type="text" readonly="readonly"/></td>
                    <td align="right">结账时间：</td>
                    <td align="left"><input id="CheckOutTime" name="CheckOutTime" type="text" readonly="readonly"/></td>
                    <td align="right">现&nbsp;&nbsp;&nbsp;&nbsp;场&nbsp;&nbsp;<br/>委托书号：</td>
                    <td align="left"><input id="LocaleCommissionCode" name="LocaleCommissionCode" type="text" readonly="readonly"/></td>
                    <td align="right">现&nbsp;&nbsp;&nbsp;&nbsp;场&nbsp;&nbsp;<br/>检测时间：</td>
                    <td align="left"><input id="LocaleCommissionDate" name="LocaleCommissionDate" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">器具名称：</td>
                    <td align="left"><input id="ApplianceName" name="ApplianceName" type="text" readonly="readonly"/></td>
                    <td align="right">型号规格：</td>
                  	<td align="left"><input id="Model" name="Model" type="text" readonly="readonly"/></td>
                    <td align="right">出厂编号：</td>
                  	<td align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" readonly="readonly"/></td>
                    <td align="right">管理编号：</td>
                  	<td align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">制 造 厂：</td>
                    <td align="left"><input id="Manufacturer" name="Manufacturer" type="text" readonly="readonly"/></td>
                    <td align="right">数&nbsp;&nbsp;&nbsp;&nbsp;量：</td>
    
                    <td align="left"><input id="Quantity" name="Quantity" type="text" readonly="readonly"/>件</td>
                    <td align="right">是否强检：</td>
                    <td align="left">
                        <select id="Mandatory" name="Mandatory" style="width:152px" class="easyui-combobox">
                            <option selected="selected" value="1" >非强制检定</option>
                            <option value="0">强制检定</option>
                        </select>
    
                    </td>
                    <td align="left"><input id="Ness" name="Ness" type="checkbox" disabled="disabled"/>加&nbsp;&nbsp;急</td>
                    <td align="left">&nbsp;</td>
                </tr>
                <tr>
                    <td align="right">外观附件：</td>
                    <td align="left"><input id="Appearance" name="Appearance" type="text" readonly="readonly"/></td>
                    <td align="right">其他要求：</td>
                    <td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text" readonly="readonly"/></td>
                    <td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
                    <td align="left" colspan="3"><input id="Remark" name="Remark" type="text" readonly="readonly" style="width:350px"/></td>
                </tr>
                <tr height="50">
                    <td width="300" align="right"><a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onClick="printCom()">打印委托单</a></td>
                </tr>
             </table>
            </form>
            </div>
            <div style="padding:20px;" title="记录/证书信息">
            	<table id="OriginalRecord" iconCls="icon-tip"></table>
            </div>
            <div style="padding:20px;" title="费用分配信息">
                <table id="fee_assign_table" iconCls="icon-tip"></table>
             </div>
             <div style="padding:20px;" title="转包业务信息">
                <table id="subcontract_info_table" iconCls="icon-tip"></table>
                <br />
                <table id="uploaded_file_table" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104" iconCls="icon-tip"></table>
             </div>
             <div style="padding:20px;" title="退样信息">
                <table id="withdraw_info_table" iconCls="icon-tip"></table>
             </div>             
             <div style="padding:20px;" title="超期信息">
                <table id="overdue_info_table" iconCls="icon-tip"></table>
             </div>            
             <div style="padding:20px;" title="折扣信息">
                <table id="discount_info_table" iconCls="icon-tip"></table>
             </div>
             <div style="padding:20px;" title="委托单附件">
                <table id="attachment_info_table" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104" iconCls="icon-tip"></table>
             </div>
        </div>
	  <!--<div id="p2" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="操作区" collapsible="false"  closable="false">
			<form id="allot" method="post">
			<table width="850px" >
				
				<tr >
				     <td width="33%"  align="right" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search1()">查看原始记录</a>
	                     
					</td>
					<td  width="33%" align="center" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search2()">产看证书</a>
	                     
					</td>
					<td  width="33%" align="left" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">返回</a>
					</td>
					
				</tr>
		  </table>
		  </form>
		</div>-->

</div>
</DIV>
</DIV>
</body>
</html>
