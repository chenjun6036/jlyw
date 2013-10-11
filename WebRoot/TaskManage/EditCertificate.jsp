<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>委托单检验(修改证书)</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>

	<script>
	$(function(){
	$('#file_upload').uploadify({
		'script'    : '/jlyw/FileUploadServlet.do',
		'scriptData':{'method':'4','FileType':'101'},	//method必须放在这里，不然会与其他的参数连着，导致出错
		'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
		'queueSizeLimit': 1,//一次只能传一个文件
		'buttonImg' : '../uploadify/selectfiles.png',
		'fileDesc'  : '支持格式:xls', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
		'fileExt'   : '*.xls;',   //允许的格式
		onComplete: function (event,ID,fileObj,response,data) {  
			var retData = eval("("+response+")");
			if(retData.IsOK == false){
				$.messager.alert('提示',retData.msg,'error');
			}else{
				$.messager.alert('提示',"文件上传成功！",'info');
				$('#OriginalRecord').datagrid('reload');
				$('#file_upload_window').window('close');
			}
		},
		onAllComplete: function(event,data){
			CloseWaitingDlg();
		}
	 });
	var OriginalRecordLastSelectedId = null;	//最后一次所选择的记录Id
	$('#OriginalRecord').datagrid({
		title:'原始记录信息',
		height:350,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/OriginalRecordServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: true,
		idField:'OriginalRecordId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'CertificateCode',title:'证书编号',width:120,rowspan:2,align:'center'},
				{title:'器具信息',colspan:8,align:'center'},
				{title:'检验信息',colspan:7,align:'center'},
				{title:'费用信息',colspan:7,align:'center'}
			],[
				{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
				{field:'Model',title:'规格型号',width:60,align:'center'},
				{field:'Range',title:'测量范围',width:80,align:'center'},
				{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
				{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
				{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
				{field:'ManageCode',title:'管理编号',width:80,align:'center'},
				{field:'Quantity',title:'器具数量',width:60,align:'center'},
				
				{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
				{field:'Staff',title:'检/校人员',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(rowData.StaffChecked){
							return value;
						}else{
							return "<span style='color: #FF0000' title='尚未核定'>"+value+"</span>";
						}
					}
				},
				{field:'WorkDate',title:'检/校日期',width:80,align:'center'},				
				{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.ExcelDoc == ""){
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
						if(value=="" || rowData.CertificateDoc==""){
							return "";
						}else{
							if(rowData.CertificatePdf == ""){
								return "<span style='color: #FF0000'>未完成</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该证书' ><span style='color: #0033FF'>已完成</span></a>"
							}
						}
					}
				},
				{field:'VerifierName',title:'核验人',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
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
						if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
							return "";
						}else{
							if(rowData.AuthorizeResult == ""){		//尚未审批
								return "<span title='尚未签字'>"+value+"</span>";
							}
							else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //批准通过
								if(rowData.IsAuthBgRuning==true){
									return "<span style='color: #009933' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过(后台签字执行中...)\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{
									return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
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
		rownumbers:true	,
		onSelect:function(rowIndex, rowData){
			if(rowData.OriginalRecordId == OriginalRecordLastSelectedId){
				$(this).datagrid("unselectAll");
				OriginalRecordLastSelectedId = null;
			}else{
				OriginalRecordLastSelectedId = rowData.OriginalRecordId;
			}
		},
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#OriginalRecord').datagrid('reload');
			}
		},'-',{
			text:'修改证书',
			iconCls:'icon-edit',
			handler:function(){
				doEditCertificate();
			}
		},'-',{
			text:'返回',
			iconCls:'icon-undo',
			handler:function(){
				javascript:history.go(-1);
			}
		},'-',{
			text:'查看委托单附件等详细信息',
			iconCls:'icon-search',
			handler:function(){
				$('#DetailForm_Code').val($('#Code').val());
				$('#DetailForm').submit();
			}
		}]
	});
	$('#Appliance').datagrid({
		title:'选择受检器具',
		width:600,
		height:300,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/TargetApplianceServlet.do?method=5',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		//idField:'userid',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'StandardNameName',title:'标准名称',width:80,sortable:true,align:'center'},
				{field:'Model',title:'型号规格',width:65,align:'center'},
				{field:'Range',title:'测量范围',width:80,align:'center'},
				{field:'Accuracy',title:'准确度等级',width:65,align:'center'},
				{field:'Fee',title:'检定费',width:60,align:'center'},
				{field:'SRFee',title:'小修费用',width:60,align:'center'},
				{field:'MRFee',title:'中修费用',width:60,align:'center'},
				{field:'LRFee',title:'大修费用',width:60,align:'center'},
				{field:'TargetApplianceRemark',title:'备注',width:80,align:'center'}
			]
		],
		pagination:true,
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		onSelect:function(rowIndex, rowData){
			//赋值隐藏字段
			$("#AddOriginalForm input[name='Model']:first").val(rowData.Model);
			$("#AddOriginalForm input[name='Range']:first").val(rowData.Range);
			$("#AddOriginalForm input[name='Accuracy']:first").val(rowData.Accuracy);
			$("#AddOriginalForm input[name='TargetApplianceId']:first").val(rowData.TargetApplianceId);
			setAddOriginalFormTestFee();	//检定费
			setAddOriginalFormRepairFee();	//修理费
			
			//模板文件刷新
			$('#template_file_grid_new').datagrid('options').queryParams={'FilesetName':rowData.StandardNameFilesetName};
			$('#template_file_grid_new').datagrid('reload');
			
			//常用名称刷新
			$("#AddOriginalForm-ApplianceName").combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=5&StandardNameId='+rowData.StandardNameId);
			
			//制造厂刷新
			$("#AddOriginalForm-Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);	
			
			//核验员刷新
			$("#AddOriginalForm-VerifyUser").combobox('reload','/jlyw/TaskAssignServlet.do?method=7&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);
		},
		toolbar:"#function-toolbar"
	});
   
   
   $('#template_file_grid').datagrid({
		title:'选择原始记录模板',
		singleSelect:true, 
		fit: true,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/FileDownloadServlet.do?method=5',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		idField:'_id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'filename',title:'文件名',width:150,sortable:true,align:'center'},
				{field:'length',title:'大小',width:50,align:'center'},
				{field:'uploadDate',title:'上传日期',width:80,align:'center'},
				{field:'uploadername',title:'上传人',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}]
	});
   $('#template_file_grid_new').datagrid({
		title:'选择原始记录模板',
		width:200,
		height:300,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/FileDownloadServlet.do?method=5',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		idField:'_id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'filename',title:'文件名',width:100,sortable:true,align:'center'},
				{field:'length',title:'大小',width:50,align:'center'},
				{field:'uploadDate',title:'上传日期',width:80,align:'center'},
				{field:'uploadername',title:'上传人',width:60,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid_new').datagrid('reload');
			}	
		}]
	});
	
	$('#template_doc_file_grid').datagrid({
		title:'证书模板',
		singleSelect:true, 
		fit: true,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/FileDownloadServlet.do?method=6',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		idField:'_id',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'filename',title:'文件名',width:150,sortable:true,align:'center'},
				{field:'length',title:'大小',width:50,align:'center'},
				{field:'uploadDate',title:'上传日期',width:80,align:'center'},
				{field:'uploadername',title:'上传人',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		toolbar:[{
			text:'刷新',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}],
		pagination:true
	});
	
	
	var lastIndex;	
	var lastIndexBySheet;
	
	
	//检校日期默认当天
	var nowDate = new Date();
	$("#AddOriginalForm-WorkDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	
	
});
function doLoadCommissionSheet(fromByCode){	//查找委托单
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
			$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});

			
			if(fromByCode){	//从ComSheetInspectByCode.jsp页面调用
				$('#AddOriginalForm-TaskId').combobox('loadData',[]);
			}
			
			if($('#Code').val()=='' || $('#Pwd').val() == ''){
				$.messager.alert('提示！',"委托单无效！",'info');
				return false;
			}
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//勾选
				}
				
				//加载原始记录信息
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#OriginalRecord').datagrid('reload');
				
				//加载受检器具信息
				$('#Appliance').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#Appliance').datagrid('reload');
				
				//hidden字段
				$('#TheCommissionId').val($('#CommissionId').val());
				
				
				if(fromByCode){	//从ComSheetInspectByCode.jsp页面调用
					//加载检验项目信息
					$('#AddOriginalForm-TaskId').combobox('reload', '/jlyw/TaskAssignServlet.do?method=5&CommissionId='+$('#CommissionId').val());
				}
				
				//加载新增原始记录的参数值
				$("#AddOriginalForm").form('load',result.CommissionObj);
				if(result.CommissionObj.ReportType == 1 || result.CommissionObj.ReportType == '1'){
					$("#AddOriginalForm-WorkType option[value='检定']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 2 || result.CommissionObj.ReportType == '2'){
					$("#AddOriginalForm-WorkType option[value='校准']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 3 || result.CommissionObj.ReportType == '3'){
					$("#AddOriginalForm-WorkType option[value='检测']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 4 || result.CommissionObj.ReportType == '4'){
					$("#AddOriginalForm-WorkType option[value='检验']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkType option[value='']").attr("selected", true);
				}
				
				if(result.CommissionObj.CommissionType == 2 || result.CommissionObj.CommissionType == '2'){
					$("#AddOriginalForm-WorkLocation option[value='被测仪器使用现场']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkLocation option[value='本所实验室']").attr("selected", true);
				}
				
				if(result.CommissionObj.Mandatory == 1 || result.CommissionObj.Mandatory == '1'){
					$("#AddOriginalForm-Mandatory option[value='否']").attr("selected", true);
				}else{
					$("#AddOriginalForm-Mandatory option[value='是']").attr("selected", true);
				}
				
				$("#AddOriginalForm input[name='Quantity']:first").val("1");
				
									
			}else{
				$.messager.alert('查询失败！',result.msg,'error');
			}
		}
	});  
}


function doLoadOrEditOriginalRecord(type)  //下载或编辑原始记录:type为0时下载原始记录，1时在weboffice编辑原始记录
{
	if(type == 1 || type =="1"){	//编辑原始记录
		$('#downloadfile-submit-form-DownloadType').val("1");
	}else{
		$('#downloadfile-submit-form-DownloadType').val("0");
	}
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		if(type==1 || type=="1"){
			$("#AddOriginalForm-OriginalRecordId").val('');
			$("#AddOriginalForm-WorkStaffId").val('');
			$("#AddOriginalForm input[name='Quantity']:first").val('1');	//重置器具数量
			$("#add_original_record_window").window("open");
//			$("#AddOriginalForm").form('validate');
			return false;
		}else{
			$.messager.alert('提示！','请先选择一条原始记录！','info');
			return false;
		}
		
	}
	$('#weboffice-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	$('#weboffice-submit-form-StaffId').val(row.StaffId);
	$('#downloadfile-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	if(row.ExcelId != "" && row.ExcelDoc != ""){		//已有原始记录
		if(type == 1 || type =="1"){	//编辑原始记录
			$('#weboffice-submit-form-TemplateFileId').val('');
			$('#weboffice-submit-form-FileName').val(row.ExcelFileName);
			$('#weboffice-submit-form-Version').val(row.ExcelVersion);
			doOpenEditXlsWebOfficeWindow();
		}else{	//下载原始记录
			$('#downloadfile-submit-form-TemplateFileId').val("");
			$('#downloadfile-submit-form').submit();
		}
	}else{	//没有原始记录
		if(row.CertificateDoc == ""){	//选择原始记录模板
			doOpenSelecteTemplateWindow(row.TemplateFilesetName);
		}else{	//直接编辑证书
			$('#makecertificate-submit-form-XlsTemplateFileId').val('');
			$('#makecertificate-submit-form-OriginalRecordId').val(row.OriginalRecordId);
			$('#makecertificate-submit-form-FileName').val(row.CertificateFileName);
			$('#makecertificate-submit-form-Version').val(row.CertificateVersion);
			$('#makecertificate-submit-form-StaffId').val(row.StaffId);
			doOpenEditDocWebOfficeWindow();
		}
		
	}
}
function doOpenSelecteTemplateWindow(filesetname)	//打开选择原始记录模板文件的窗口
{
	$("#select_xls_template_window").window('open');
	$('#template_file_grid').datagrid('options').queryParams={'FilesetName':filesetname};
	$('#template_file_grid').datagrid('reload');
}
function doSelecteXlsTemplate()	//选择原始记录模板文件
{
	var row = $('#template_file_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！',"请选择一个模板！",'info');
		return false;
	}
	doCloseSelectedXlsTemplateWindow();
	if($("#downloadfile-submit-form-DownloadType").val() == "1" || $("#downloadfile-submit-form-DownloadType").val() == 1){	//编辑原始记录
		$('#weboffice-submit-form-TemplateFileId').val(row._id);
		$('#weboffice-submit-form-FileName').val(row.filename);
		$('#weboffice-submit-form-Version').val("-1");
		doOpenEditXlsWebOfficeWindow();
	}else{	//下载原始记录
		$('#downloadfile-submit-form-TemplateFileId').val(row._id);
		$('#downloadfile-submit-form').submit();
	}
}
function doCloseSelectedXlsTemplateWindow()	//关闭选择模板文件的窗口
{
	$('#select_xls_template_window').window('close');
}
function doOpenEditXlsWebOfficeWindow()	//打开编辑Weboffice窗口
{
	var win = window.open("","DoEditXlsWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//窗口不存在
		$('#weboffice-submit-form').submit();
		return true;
	}else{
		$.messager.alert('提示！',"不能同时打开多个原始记录编辑窗口，如需切换请先关闭当前打开的原始记录编辑窗口！",'info', function(){win.focus();});
		return false;
	}
}


/*******       调整修理级别后更改费用          *********/
//设置AddOriginalForm的检定费
function setAddOriginalFormTestFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='TestFee']:first").val('');//检定费
			return false;
		}else{
			$("#AddOriginalForm input[name='TestFee']:first").val(row.Fee);//检定费
		}
	}catch(ex){}
}
//设置AddOriginalForm的修理费
function setAddOriginalFormRepairFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//修理费
			return false;
		}
		var level = $("#AddOriginalForm-RepairLevel").val();
		if(level == "小"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.SRFee);//修理费
		}else if(level == "中"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.MRFee);//修理费
		}else if(level == "大"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.LRFee);//修理费
		}else{
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//修理费
		}
	}catch(ex){}
}
function doOpenEditDocWebOfficeWindow()	//打开编辑证书的Weboffice窗口
{
	var win = window.open("","DoEditDocWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//窗口不存在
		$('#makecertificate-submit-form').submit();
		return true;
	}else{
		$.messager.alert('提示！',"不能同时打开多个证书编辑窗口，如需切换请先关闭当前打开的证书编辑窗口！",'info', function(){win.focus();});
		return false;
	}
}
function doEditCertificate(){
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('提示！','请先选择一条原始记录！','info');
		return false;
	}
	if(row.CertificateDoc == ""||row.CertificateDoc==null){
		$.messager.alert('提示！','该原始记录还未生成证书，不能修改证书！','info');
		return false;
	}
	
	$('#makecertificate-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	$('#makecertificate-submit-form-FlagEditCertificate').val(1);
	if(row.CertificateId != "" && row.CertificateDoc != ""){
		$('#makecertificate-submit-form-Version').val(row.CertificateVersion);
	}else{
		$('#makecertificate-submit-form-Version').val("-1");
	}
	$('#makecertificate-submit-form-FileName').val(row.CertificateFileName);
	$('#makecertificate-submit-form-StaffId').val(row.StaffId);
	$('#makecertificate-submit-form-XlsTemplateFileId').val('');
	
	doOpenEditDocWebOfficeWindow();  //打开编辑证书的窗口
}
	$(function(){
	
	
		doLoadCommissionSheet(false);	//加载委托单
		$('#loading').hide();	//等待框隐藏
	});
	</script>
</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		服务器处理中，请稍后...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单检验" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="DetailForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="DetailForm_Code" type="hidden" name="Code"/>
        </form>
		<form id="SearchForm" method="post" >
		<input id="Code" type="hidden" name="Code" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" style="width:150px;" />
		<input id="Pwd" type="hidden" name="Pwd" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" style="width:150px;" />
		</form>
     
	 <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	 <br/>
	 <table id="OriginalRecord" iconCls="icon-tip" width="1005px" height="350px"></table>
		<br />
	  <form id="weboffice-submit-form" method="post" target="DoEditXlsWebOffice" action="/jlyw/WebOffice/xlsEdit.jsp">
		<input type="hidden" name="TemplateFileId" id="weboffice-submit-form-TemplateFileId" value="" />
		<input type="hidden" name="OriginalRecordId" id="weboffice-submit-form-OriginalRecordId" value="" />
		<input type="hidden" name="FileName" id="weboffice-submit-form-FileName" value="" />
		<input type="hidden" name="Version" id="weboffice-submit-form-Version" value="" />
		<input type="hidden" name="StaffId" id="weboffice-submit-form-StaffId" value="" />
		<input type="hidden" name="VerifierName" id="weboffice-submit-form-VerifierName" value="" />
	  </form>
	  <form id="downloadfile-submit-form" method="post" target="_blank" action="/jlyw/FileDownloadServlet.do?method=3">
		<input type="hidden" name="TemplateFileId" id="downloadfile-submit-form-TemplateFileId" value="" />
		<input type="hidden" name="OriginalRecordId" id="downloadfile-submit-form-OriginalRecordId" value="" />
		<input type="hidden" name="DownloadType" id="downloadfile-submit-form-DownloadType" value="0" />
	  </form>
	 
	  <form id="makecertificate-submit-form" method="post" target="DoEditDocWebOffice" action="/jlyw/WebOffice/docEditCertificate.jsp">
		<input type="hidden" name="XlsTemplateFileId" id="makecertificate-submit-form-XlsTemplateFileId" value="" />
		<input type="hidden" name="OriginalRecordId" id="makecertificate-submit-form-OriginalRecordId" value="" />
		<input type="hidden" name="FileName" id="makecertificate-submit-form-FileName" value="" />
		<input type="hidden" name="Version" id="makecertificate-submit-form-Version" value="" />
		<input type="hidden" name="StaffId" id="makecertificate-submit-form-StaffId" value="" />
		<input type="hidden" name="FlagEditCertificate" id="makecertificate-submit-form-FlagEditCertificate" value="" />
	  </form>

</DIV></DIV>
	
	<div id="select_xls_template_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="选择原始记录模板" iconCls="icon-save" style="width:550px;height:550px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="template_file_grid" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteXlsTemplate()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectedXlsTemplateWindow()">取消</a>
			</div>
		</div>
	</div>
	
	<div id="select_doc_template_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="选择证书模板" iconCls="icon-save" style="width:550px;height:550px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="template_doc_file_grid" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteDocTemplate()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectedDocTemplateWindow()">取消</a>
			</div>
		</div>
	</div>
	
	<div id="select_verifyAndAuthorize_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="选择原始记录、证书的核验人和批准人" iconCls="icon-save" style="width:550px;height:200px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="VerifyAndAuthorizeForm" method="post">
					<input type="hidden" name="OriginalRecordId" id="VerifyAndAuthorizeForm-OriginalRecordId"  />
					<input type="hidden" name="Version" id="VerifyAndAuthorizeForm-Version"  />
					<br/>核验人：<select name="Verifier" id="VerifyAndAuthorizeForm-Verifier" style="width:152px" valueField="id" textField="name" required="true" class="easyui-combobox"  editable="false"></select>&nbsp;&nbsp;
					批准人：<select name="Authorizer" id="VerifyAndAuthorizeForm-Authorizer" style="width:152px"></select>
				</form>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteVerifyAndAuthorize()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseVerifyAndAuthorizeWindow()">取消</a>
			</div>
		</div>
	</div>
		
	<div id="fee_assign_window" class="easyui-window" closed="true" modal="true" title="产值分配" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:550px;height:450px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="fee_info_temp_form">
				<table width="100%" height="50px" border="0" style="margin-bottom:10px">
					<tr>
						<td width="16%">证书编号:</td>
						<td width="20%"><input name="CertificateCode" style="width:100px" readonly="readonly" value="" /></td>
						<td width="16%">检校人:</td>
						<td width="16%"><input name="Staff" style="width:100px" readonly="readonly" value="" /></td>
						<td width="15%">&nbsp;</td>
						<td width="17%">&nbsp;</td>
					</tr>
					<tr>
					  <td>检定费:</td>
					  <td><input name="TestFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>修理费:</td>
					  <td><input name="RepairFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>材料费:</td>
					  <td><input name="MaterialFee" style="width:100px" readonly="readonly" value="" /></td>
				  </tr>
				  <tr>
					  <td>交通费:</td>
					  <td><input name="CarFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>调试费:</td>
					  <td><input name="DebugFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>其它费:</td>
					  <td><input name="OtherFee" style="width:100px" readonly="readonly" value="" /></td>
				  </tr>
				</table>
				</form>
				<form id="FeeAssignForm" method="post">
					<input type="hidden" name="OriginalRecordId" id="FeeAssignForm-OriginalRecordId"  />
					<input type="hidden" name="CertificateId" id="FeeAssignForm-CertificateId" />
					<input type="hidden" name="FeeAssignInfo" id="FeeAssignForm-FeeAssignInfo" />
				</form>
				<select name="FeeAllotee" id="FeeAssignForm-FeeAllotee" style="width:152px"></select> &nbsp;&nbsp;
				分配额度(0.00~1.00):<input type="text" id="FeeAssignForm-FeeAssignLimit" class="easyui-numberbox" style="width:80px" precision="2" min="0.00" max="1.00" value="0.00" />&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="doAddAnAllotee()" >添加人员</a>
				<br/>
				<table id="fee_assign_table" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFeeAssign()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeFeeAssignWindow()">取消</a>
			</div>
		</div>
	</div>
	
	<div id="fee_assign_by_sheet_window" class="easyui-window" closed="true" modal="true" title="委托单产值分配：分配该委托单中由我检验的、尚未分配过的证书产值" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:610px;height:420px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:5px;background:#fff;border:1px solid #ccc;">
				<form id="FeeAssignFormBySheet" method="post">
					<input type="hidden" name="CommissionSheetId" id="FeeAssignFormBySheet-CommissionSheetId" value=""  />
					<input type="hidden" name="FeeAssignInfo" id="FeeAssignFormBySheet-FeeAssignInfo" />
				</form>
				<select name="FeeAllotee" id="FeeAssignFormBySheet-FeeAllotee" style="width:152px"></select> &nbsp;&nbsp;
				分配额度(0.00~1.00):<input type="text" id="FeeAssignFormBySheet-FeeAssignLimit" class="easyui-numberbox" style="width:80px" precision="2" min="0.00" max="1.00" value="0.00" />&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="doAddAnAlloteeBySheet()" >添加人员</a>
				<br/>
				<table id="fee_assign_table_by_sheet" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFeeAssignBySheet()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeFeeAssignWindowBySheet()">取消</a>
			</div>
		</div>
	</div>

	
<div id="add_original_record_window" class="easyui-window" closed="true" collapsible="false" minimizable="false" maximizable="false" modal="true" title="编辑原始记录" iconCls="icon-save" style="width:830px;height:650px;overflow:hidden;padding-left:5px;background:#fff;border:1px solid #ccc;">
	<form id="AddOriginalForm" method="post">
		<input type="hidden" name="OriginalRecordId" id="AddOriginalForm-OriginalRecordId" value="" />
		<input type="hidden" name="WorkStaffId" id="AddOriginalForm-WorkStaffId" value="" /><!--用于添加成功后暂存ID，每次打开该对话框时清除该ID-->
		<input type="hidden" id="TheCommissionId" name="CommissionId" value="" />
		<input type="hidden" name="TaskId" value="<%= request.getParameter("TaskId")==null?"":request.getParameter("TaskId") %>" />
		<input type="hidden" name="Model" value="" />
		<input type="hidden" name="Range" value="" />
		<input type="hidden" name="Accuracy" value="" />
		<input type="hidden" name="TargetApplianceId" value="" />
		<table width="800px" border="0" height="320px">
			<tr>
				<td width="600px" valign="top">
					<table id="Appliance" iconCls="icon-tip" width="600px" height="300px"></table>
				</td>
				<td width="200px" valign="top">
					<table id="template_file_grid_new" iconCls="icon-tip"></table>
				</td>
			</tr>
		</table>
		<table width="800px" border="0">
			<tr>
				<td width="74" align="right">常用名称：</td>
				<td width="185" align="left">
						<select name="ApplianceName" id="AddOriginalForm-ApplianceName" style="width:152px" class="easyui-combobox" valueField="Name" textField='Name'></select></td>
				<td width="69" align="right">工作性质：</td>
				<td width="163" align="left">
					<select name="WorkType" id="AddOriginalForm-WorkType" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="检定">检定</option>
						<option value="校准">校准</option>
						<option value="检测">检测</option>
						<option value="检验">检验</option>
					</select>					</td>
				<td width="65" align="right">工作地点：</td>
				<td width="168" align="left">
					<select name="WorkLocation" id="AddOriginalForm-WorkLocation" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="本所实验室">本所实验室</option>
						<option value="被测仪器使用现场">被测仪器使用现场</option>
					</select>				    </td>
			</tr>
			<tr>
				<td align="right">制造单位：</td>
				<td align="left">
						<select name="Manufacturer" id="AddOriginalForm-Manufacturer" style="width:152px"  class="easyui-combobox" valueField="name" textField='name'></select>				    </td>
				<td align="right">出厂编号：</td>
				<td align="left">
					<input type="text" style="width:152px" name="ApplianceCode" />					</td>
				<td align="right">管理编号：</td>
				<td align="left">
					<input type="text" style="width:152px" name="ApplianceManageCode" />				    </td>
			</tr>
			<tr>
				<td align="right">环境温度：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Temp" class="easyui-numberbox" precision="1" />℃				    </td>
				<td align="right">相对湿度：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Humidity"  class="easyui-numberbox" precision="1" />%					</td> 
				<td align="right">大 气 压：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Pressure" value="/" />kPa				    </td>
			</tr>
			<tr>
				<td align="right">其 &nbsp;&nbsp; 它：</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherCond" value="" /></td>
				<td align="right">标准(器具)<br/>
				  状态检查：</td>
				<td align="left">
					<select style="width:152px" name="StdOrStdAppUsageStatus" id="AddOriginalForm-StdOrStdAppUsageStatus"  class="easyui-combobox" panelHeight="auto" >
						<option value="正常" selected="selected">正常</option>
						<option value="异常">异常</option>
					</select>			  </td> 
				<td align="right">异常情况<br/>说明：</td>
				<td align="left">
					<input type="text" style="width:152px" name="AbnormalDesc" value="/" /></td>
			</tr>
			<tr>
				<td align="right">结 &nbsp;&nbsp; 论：</td>
				<td align="left">
					<select style="width:152px" name="Conclusion" id="AddOriginalForm-Conclusion"  class="easyui-combobox" panelHeight="auto" required="true" >
						<option value="合格" selected="selected">合格</option>
						<option value="不合格">不合格</option>
					</select></td>
				<td align="right">检校日期：</td>
				<td align="left">
					<input type="text" style="width:152px" name="WorkDate" id="AddOriginalForm-WorkDate" class="easyui-datebox" required="true" />					</td>
				<td align="right">核 验 员：</td>
				<td align="left">
					<select style="width:152px" name="VerifyUser" id="AddOriginalForm-VerifyUser" valueField="name" textField="name" class="easyui-combobox" editable="false" ></select>				    </td>
			</tr>
			<tr>
				<td align="right">器具数量：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Quantity" class="easyui-numberbox" min="1" value="1" required="true" />件				    </td>
				<td align="right">修理级别：</td>
				<td align="left">
					<select name="RepairLevel" id="AddOriginalForm-RepairLevel" onchange="setAddOriginalFormRepairFee()" style="width:152px">
						<option value="" selected="selected">请选择...</option>
						<option value="小">小</option>
						<option value="中">中</option>
						<option value="大">大</option>
					</select>					</td>
				<td align="right">修 理 费：</td>
				<td align="left"><input type="text" style="width:152px; background-color:#CCCCCC" name="RepairFee" readonly="readonly" /></td>
			</tr>
			<tr>
				<td align="right">交 通 费：</td>
				<td align="left">
					<input type="text" style="width:152px" name="CarFee" class="easyui-numberbox" min="0" />元				    </td>
				<td align="right">调 试 费：</td>
				<td align="left">
					<input type="text" style="width:152px" name="DebugFee" class="easyui-numberbox" min="0" />元					</td>
				<td align="right">检 定 费：</td>
				<td align="left"><input type="text" style="width:152px;background-color:#CCCCCC" name="TestFee" readonly="readonly" /></td>
			</tr>
			<tr>
				<td align="right">其 他 费：</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherFee" class="easyui-numberbox" min="0" />元				    </td>
				<td align="right">配 件 费：</td>
				<td align="left"><input type="text" style="width:152px" name="MaterialFee" class="easyui-numberbox" min="0" />元</td>
				<td align="right">配件明细：</td>
				<td align="left"><input type="text" style="width:152px" name="MaterialDetail" /></td>
			</tr>
			<tr>
				<td align="right">是否强管：</td>
				<td align="left">
					<select name="Mandatory" id="AddOriginalForm-Mandatory" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="否">否</option>
						<option value="是">是</option>
					</select>				    </td>
				<td align="right">强管唯一性号：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryCode"  /></td>
				<td align="right">强管类型：</td>
				<td align="left">
					<select name="MandatoryPurpose" id="AddOriginalForm-MandatoryPurpose" style="width:152px" class="easyui-validatebox">
						<option value="" selected="selected">请选择...</option>
						<option value="贸易结算">贸易结算</option>
						<option value="医疗卫生">医疗卫生</option>
						<option value="安全防护">安全防护</option>
						<option value="环境监测">环境监测</option>
						<option value="社会公用计量标准器具">社会公用计量标准器具</option>
						<option value="部门、企事业单位最高计量标准器具">部门、企事业单位最高计量标准器具</option>
					</select></td>
			</tr>
			<tr>
				<td align="right">强检目录对应项别：</td>
				<td align="left">
					<input type="text" style="width:152px" name="MandatoryItemType" value="" /></td>
				<td align="right">强检目录对应种别：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryType" value="" /></td>
				<td align="right">使用/安装地点：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryApplyPlace" /></td>
			</tr>
			<tr>
				<td align="right">&nbsp;</td>
				
				<td align="right" colspan="5" style="padding-right:10px"><a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doOpenHistoryCertificateWindow()" >从历史证书导入</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, true)" >编辑原始记录</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, false)" >直接编制证书</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#add_original_record_window').window('close')">取消</a></td>
			</tr>
		</table>		
	</form>
</div>

<div id="function-toolbar" style="padding:2px 0">
  <table cellpadding="0" cellspacing="0" style="width:100%">
    <tr>
      <td style="text-align:left;padding-left:2px"><label>标准名:</label>
          <select name="select" id="function-toolbar-StandardName" style="width:60px">
          </select>
        <label>型号:</label>
        <select name="text"  id="function-toolbar-Model" class="easyui-combobox" valueField="name" textField='name' style="width:60px" ></select>
        <label>范围:</label>
        <input name="text"  id="function-toolbar-Range" class="easyui-combobox" valueField="name" textField='name' style="width:60px" />
        <label>等级:</label>
        <input name="text"  id="function-toolbar-Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:60px" />
		<input id="standardNameIdstandardNameId" name="standardNameIdstandardNameId"  type="hidden" />
        <a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-search" plain="true" title="查询受检器具" id="btnHistorySearch" onclick="doSearchTargetAppliance()">查询</a> </td>
    </tr>
  </table>
</div>

<div id="file_upload_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="脱机文件上传" iconCls="icon-save" style="width:500px;height:200px;padding:5px;">
	<table width="450px" style="margin:10px auto">
		<tr>
		  <td height="80" align="left">
				<table width="350px"; height="80px">
					<tr>
						<td height="80" valign="top" align="left" style="overflow:hidden">
							<div class="easyui-panel" fit="true" collapsible="false"  closable="false">
								<input id="file_upload" type="file" name="file_upload" />
							</div>								</td>
					</tr>
				</table>
		  </td>
	  </tr>
		<tr style="padding-top:25px" >
		  <td height="39" align="left"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadExcel(0)">脱机文件暂存</a> &nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadExcel(1)">脱机文件提交</a>&nbsp; &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#file_upload').uploadifyClearQueue();$('#file_upload_window').window('close');">关闭对话框</a></td>
	  </tr>
 </table>
</div>

<div id="load_from_history_certificate" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="从历史证书中导入" iconCls="icon-save" style="width:650px;height:550px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="background:#fff;border:1px solid #ccc;">
			<table id="history_certificate_grid" width="620px" height="450px" iconCls="icon-tip"></table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doLoadHistoryCertificate()" >确定</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#load_from_history_certificate').window('close');">取消</a>
		</div>
	</div>
</div>



</body>
</html>
