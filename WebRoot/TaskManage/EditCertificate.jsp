<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ί�е�����(�޸�֤��)</title>
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
		'scriptData':{'method':'4','FileType':'101'},	//method������������Ȼ���������Ĳ������ţ����³���
		'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
		'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
		'buttonImg' : '../uploadify/selectfiles.png',
		'fileDesc'  : '֧�ָ�ʽ:xls', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
		'fileExt'   : '*.xls;',   //����ĸ�ʽ
		onComplete: function (event,ID,fileObj,response,data) {  
			var retData = eval("("+response+")");
			if(retData.IsOK == false){
				$.messager.alert('��ʾ',retData.msg,'error');
			}else{
				$.messager.alert('��ʾ',"�ļ��ϴ��ɹ���",'info');
				$('#OriginalRecord').datagrid('reload');
				$('#file_upload_window').window('close');
			}
		},
		onAllComplete: function(event,data){
			CloseWaitingDlg();
		}
	 });
	var OriginalRecordLastSelectedId = null;	//���һ����ѡ��ļ�¼Id
	$('#OriginalRecord').datagrid({
		title:'ԭʼ��¼��Ϣ',
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
				{field:'CertificateCode',title:'֤����',width:120,rowspan:2,align:'center'},
				{title:'������Ϣ',colspan:8,align:'center'},
				{title:'������Ϣ',colspan:7,align:'center'},
				{title:'������Ϣ',colspan:7,align:'center'}
			],[
				{field:'ApplianceStandardName',title:'���߱�׼����',width:80,sortable:true,align:'center'},
				{field:'Model',title:'����ͺ�',width:60,align:'center'},
				{field:'Range',title:'������Χ',width:80,align:'center'},
				{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:80,align:'center'},
				{field:'Manufacturer',title:'���쳧',width:80,align:'center'},
				{field:'ApplianceCode',title:'�������',width:80,align:'center'},
				{field:'ManageCode',title:'������',width:80,align:'center'},
				{field:'Quantity',title:'��������',width:60,align:'center'},
				
				{field:'ProjectName',title:'������Ŀ����',width:80,align:'center'},
				{field:'Staff',title:'��/У��Ա',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(rowData.StaffChecked){
							return value;
						}else{
							return "<span style='color: #FF0000' title='��δ�˶�'>"+value+"</span>";
						}
					}
				},
				{field:'WorkDate',title:'��/У����',width:80,align:'center'},				
				{field:'ExcelId',title:'ԭʼ��¼Excel',width:80,align:'center', 
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.ExcelDoc == ""){
							return "";
						}else{
							if(rowData.ExcelPdf == ""){
								return "<span style='color: #FF0000'>δ���</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>�����</span></a>"
							}
						}
					}},
				{field:'CertificateId',title:'֤���ļ�',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.CertificateDoc==""){
							return "";
						}else{
							if(rowData.CertificatePdf == ""){
								return "<span style='color: #FF0000'>δ���</span>";
							}else{
								return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='������ظ�֤��' ><span style='color: #0033FF'>�����</span></a>"
							}
						}
					}
				},
				{field:'VerifierName',title:'������',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
							return "";
						}else{
							if(rowData.VerifyResult == ""){		//��δ����
								return "<span title='��δ����'>"+value+"</span>";
							}
							else if(rowData.VerifyResult == "1" || rowData.VerifyResult == 1 ){ //����ͨ��
								return "<span style='color: #0033FF' title='����ʱ�䣺"+rowData.VerifyTime+"\r\n��������ͨ��\r\n��ע��"+rowData.VerifyRemark+"'>"+value+"</span>";
							}else{	//����δͨ��
								return "<span style='color: #FF0000' title='����ʱ�䣺"+rowData.VerifyTime+"\r\n��������δͨ��\r\n��ע��"+rowData.VerifyRemark+"'>"+value+"</span>";
							}
						}						
					}
				},
				{field:'AuthorizerName',title:'��׼��',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
						if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
							return "";
						}else{
							if(rowData.AuthorizeResult == ""){		//��δ����
								return "<span title='��δǩ��'>"+value+"</span>";
							}
							else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //��׼ͨ��
								if(rowData.IsAuthBgRuning==true){
									return "<span style='color: #009933' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����ͨ��(��̨ǩ��ִ����...)\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{
									return "<span style='color: #0033FF' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����ͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
							}else{	//��׼δͨ��
								return "<span style='color: #FF0000' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����δͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
							}
						}
					}
				},
				
				{field:'TestFee',title:'����',width:60,align:'center'},
				{field:'RepairFee',title:'�����',width:60,align:'center'},
				{field:'MaterialFee',title:'���Ϸ�',width:60,align:'center'},
				{field:'CarFee',title:'��ͨ��',width:60,align:'center'},
				{field:'DebugFee',title:'���Է�',width:60,align:'center'},
				{field:'OtherFee',title:'��������',width:60,align:'center'},
				{field:'TotalFee',title:'�ܼƷ���',width:60,align:'center'}
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
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#OriginalRecord').datagrid('reload');
			}
		},'-',{
			text:'�޸�֤��',
			iconCls:'icon-edit',
			handler:function(){
				doEditCertificate();
			}
		},'-',{
			text:'����',
			iconCls:'icon-undo',
			handler:function(){
				javascript:history.go(-1);
			}
		},'-',{
			text:'�鿴ί�е���������ϸ��Ϣ',
			iconCls:'icon-search',
			handler:function(){
				$('#DetailForm_Code').val($('#Code').val());
				$('#DetailForm').submit();
			}
		}]
	});
	$('#Appliance').datagrid({
		title:'ѡ���ܼ�����',
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
				{field:'StandardNameName',title:'��׼����',width:80,sortable:true,align:'center'},
				{field:'Model',title:'�ͺŹ��',width:65,align:'center'},
				{field:'Range',title:'������Χ',width:80,align:'center'},
				{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:65,align:'center'},
				{field:'Fee',title:'�춨��',width:60,align:'center'},
				{field:'SRFee',title:'С�޷���',width:60,align:'center'},
				{field:'MRFee',title:'���޷���',width:60,align:'center'},
				{field:'LRFee',title:'���޷���',width:60,align:'center'},
				{field:'TargetApplianceRemark',title:'��ע',width:80,align:'center'}
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
			//��ֵ�����ֶ�
			$("#AddOriginalForm input[name='Model']:first").val(rowData.Model);
			$("#AddOriginalForm input[name='Range']:first").val(rowData.Range);
			$("#AddOriginalForm input[name='Accuracy']:first").val(rowData.Accuracy);
			$("#AddOriginalForm input[name='TargetApplianceId']:first").val(rowData.TargetApplianceId);
			setAddOriginalFormTestFee();	//�춨��
			setAddOriginalFormRepairFee();	//�����
			
			//ģ���ļ�ˢ��
			$('#template_file_grid_new').datagrid('options').queryParams={'FilesetName':rowData.StandardNameFilesetName};
			$('#template_file_grid_new').datagrid('reload');
			
			//��������ˢ��
			$("#AddOriginalForm-ApplianceName").combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=5&StandardNameId='+rowData.StandardNameId);
			
			//���쳧ˢ��
			$("#AddOriginalForm-Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);	
			
			//����Աˢ��
			$("#AddOriginalForm-VerifyUser").combobox('reload','/jlyw/TaskAssignServlet.do?method=7&SpeciesType=0&ApplianceSpeciesId='+rowData.StandardNameId);
		},
		toolbar:"#function-toolbar"
	});
   
   
   $('#template_file_grid').datagrid({
		title:'ѡ��ԭʼ��¼ģ��',
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
				{field:'filename',title:'�ļ���',width:150,sortable:true,align:'center'},
				{field:'length',title:'��С',width:50,align:'center'},
				{field:'uploadDate',title:'�ϴ�����',width:80,align:'center'},
				{field:'uploadername',title:'�ϴ���',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}]
	});
   $('#template_file_grid_new').datagrid({
		title:'ѡ��ԭʼ��¼ģ��',
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
				{field:'filename',title:'�ļ���',width:100,sortable:true,align:'center'},
				{field:'length',title:'��С',width:50,align:'center'},
				{field:'uploadDate',title:'�ϴ�����',width:80,align:'center'},
				{field:'uploadername',title:'�ϴ���',width:60,align:'center'}
			]
		],
		rownumbers:true	,
		onLoadSuccess:function(data){
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		toolbar:[{
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid_new').datagrid('reload');
			}	
		}]
	});
	
	$('#template_doc_file_grid').datagrid({
		title:'֤��ģ��',
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
				{field:'filename',title:'�ļ���',width:150,sortable:true,align:'center'},
				{field:'length',title:'��С',width:50,align:'center'},
				{field:'uploadDate',title:'�ϴ�����',width:80,align:'center'},
				{field:'uploadername',title:'�ϴ���',width:80,align:'center'}
			]
		],
		rownumbers:true	,
		toolbar:[{
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#template_file_grid').datagrid('reload');
			}	
		}],
		pagination:true
	});
	
	
	var lastIndex;	
	var lastIndexBySheet;
	
	
	//��У����Ĭ�ϵ���
	var nowDate = new Date();
	$("#AddOriginalForm-WorkDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	
	
});
function doLoadCommissionSheet(fromByCode){	//����ί�е�
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
			$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});

			
			if(fromByCode){	//��ComSheetInspectByCode.jspҳ�����
				$('#AddOriginalForm-TaskId').combobox('loadData',[]);
			}
			
			if($('#Code').val()=='' || $('#Pwd').val() == ''){
				$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
				return false;
			}
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//��ѡ
				}
				
				//����ԭʼ��¼��Ϣ
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#OriginalRecord').datagrid('reload');
				
				//�����ܼ�������Ϣ
				$('#Appliance').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#Appliance').datagrid('reload');
				
				//hidden�ֶ�
				$('#TheCommissionId').val($('#CommissionId').val());
				
				
				if(fromByCode){	//��ComSheetInspectByCode.jspҳ�����
					//���ؼ�����Ŀ��Ϣ
					$('#AddOriginalForm-TaskId').combobox('reload', '/jlyw/TaskAssignServlet.do?method=5&CommissionId='+$('#CommissionId').val());
				}
				
				//��������ԭʼ��¼�Ĳ���ֵ
				$("#AddOriginalForm").form('load',result.CommissionObj);
				if(result.CommissionObj.ReportType == 1 || result.CommissionObj.ReportType == '1'){
					$("#AddOriginalForm-WorkType option[value='�춨']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 2 || result.CommissionObj.ReportType == '2'){
					$("#AddOriginalForm-WorkType option[value='У׼']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 3 || result.CommissionObj.ReportType == '3'){
					$("#AddOriginalForm-WorkType option[value='���']").attr("selected", true);
				}else if(result.CommissionObj.ReportType == 4 || result.CommissionObj.ReportType == '4'){
					$("#AddOriginalForm-WorkType option[value='����']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkType option[value='']").attr("selected", true);
				}
				
				if(result.CommissionObj.CommissionType == 2 || result.CommissionObj.CommissionType == '2'){
					$("#AddOriginalForm-WorkLocation option[value='��������ʹ���ֳ�']").attr("selected", true);
				}else{
					$("#AddOriginalForm-WorkLocation option[value='����ʵ����']").attr("selected", true);
				}
				
				if(result.CommissionObj.Mandatory == 1 || result.CommissionObj.Mandatory == '1'){
					$("#AddOriginalForm-Mandatory option[value='��']").attr("selected", true);
				}else{
					$("#AddOriginalForm-Mandatory option[value='��']").attr("selected", true);
				}
				
				$("#AddOriginalForm input[name='Quantity']:first").val("1");
				
									
			}else{
				$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}


function doLoadOrEditOriginalRecord(type)  //���ػ�༭ԭʼ��¼:typeΪ0ʱ����ԭʼ��¼��1ʱ��weboffice�༭ԭʼ��¼
{
	if(type == 1 || type =="1"){	//�༭ԭʼ��¼
		$('#downloadfile-submit-form-DownloadType').val("1");
	}else{
		$('#downloadfile-submit-form-DownloadType').val("0");
	}
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		if(type==1 || type=="1"){
			$("#AddOriginalForm-OriginalRecordId").val('');
			$("#AddOriginalForm-WorkStaffId").val('');
			$("#AddOriginalForm input[name='Quantity']:first").val('1');	//������������
			$("#add_original_record_window").window("open");
//			$("#AddOriginalForm").form('validate');
			return false;
		}else{
			$.messager.alert('��ʾ��','����ѡ��һ��ԭʼ��¼��','info');
			return false;
		}
		
	}
	$('#weboffice-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	$('#weboffice-submit-form-StaffId').val(row.StaffId);
	$('#downloadfile-submit-form-OriginalRecordId').val(row.OriginalRecordId);
	if(row.ExcelId != "" && row.ExcelDoc != ""){		//����ԭʼ��¼
		if(type == 1 || type =="1"){	//�༭ԭʼ��¼
			$('#weboffice-submit-form-TemplateFileId').val('');
			$('#weboffice-submit-form-FileName').val(row.ExcelFileName);
			$('#weboffice-submit-form-Version').val(row.ExcelVersion);
			doOpenEditXlsWebOfficeWindow();
		}else{	//����ԭʼ��¼
			$('#downloadfile-submit-form-TemplateFileId').val("");
			$('#downloadfile-submit-form').submit();
		}
	}else{	//û��ԭʼ��¼
		if(row.CertificateDoc == ""){	//ѡ��ԭʼ��¼ģ��
			doOpenSelecteTemplateWindow(row.TemplateFilesetName);
		}else{	//ֱ�ӱ༭֤��
			$('#makecertificate-submit-form-XlsTemplateFileId').val('');
			$('#makecertificate-submit-form-OriginalRecordId').val(row.OriginalRecordId);
			$('#makecertificate-submit-form-FileName').val(row.CertificateFileName);
			$('#makecertificate-submit-form-Version').val(row.CertificateVersion);
			$('#makecertificate-submit-form-StaffId').val(row.StaffId);
			doOpenEditDocWebOfficeWindow();
		}
		
	}
}
function doOpenSelecteTemplateWindow(filesetname)	//��ѡ��ԭʼ��¼ģ���ļ��Ĵ���
{
	$("#select_xls_template_window").window('open');
	$('#template_file_grid').datagrid('options').queryParams={'FilesetName':filesetname};
	$('#template_file_grid').datagrid('reload');
}
function doSelecteXlsTemplate()	//ѡ��ԭʼ��¼ģ���ļ�
{
	var row = $('#template_file_grid').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��',"��ѡ��һ��ģ�壡",'info');
		return false;
	}
	doCloseSelectedXlsTemplateWindow();
	if($("#downloadfile-submit-form-DownloadType").val() == "1" || $("#downloadfile-submit-form-DownloadType").val() == 1){	//�༭ԭʼ��¼
		$('#weboffice-submit-form-TemplateFileId').val(row._id);
		$('#weboffice-submit-form-FileName').val(row.filename);
		$('#weboffice-submit-form-Version').val("-1");
		doOpenEditXlsWebOfficeWindow();
	}else{	//����ԭʼ��¼
		$('#downloadfile-submit-form-TemplateFileId').val(row._id);
		$('#downloadfile-submit-form').submit();
	}
}
function doCloseSelectedXlsTemplateWindow()	//�ر�ѡ��ģ���ļ��Ĵ���
{
	$('#select_xls_template_window').window('close');
}
function doOpenEditXlsWebOfficeWindow()	//�򿪱༭Weboffice����
{
	var win = window.open("","DoEditXlsWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//���ڲ�����
		$('#weboffice-submit-form').submit();
		return true;
	}else{
		$.messager.alert('��ʾ��',"����ͬʱ�򿪶��ԭʼ��¼�༭���ڣ������л����ȹرյ�ǰ�򿪵�ԭʼ��¼�༭���ڣ�",'info', function(){win.focus();});
		return false;
	}
}


/*******       �������������ķ���          *********/
//����AddOriginalForm�ļ춨��
function setAddOriginalFormTestFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='TestFee']:first").val('');//�춨��
			return false;
		}else{
			$("#AddOriginalForm input[name='TestFee']:first").val(row.Fee);//�춨��
		}
	}catch(ex){}
}
//����AddOriginalForm�������
function setAddOriginalFormRepairFee(){
	try{
		var row = $('#Appliance').datagrid('getSelected');
		if(row == null){
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//�����
			return false;
		}
		var level = $("#AddOriginalForm-RepairLevel").val();
		if(level == "С"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.SRFee);//�����
		}else if(level == "��"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.MRFee);//�����
		}else if(level == "��"){
			$("#AddOriginalForm input[name='RepairFee']:first").val(row.LRFee);//�����
		}else{
			$("#AddOriginalForm input[name='RepairFee']:first").val('');//�����
		}
	}catch(ex){}
}
function doOpenEditDocWebOfficeWindow()	//�򿪱༭֤���Weboffice����
{
	var win = window.open("","DoEditDocWebOffice","fullscreen=yes,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
	if(win.location.href=="about:blank"){	//���ڲ�����
		$('#makecertificate-submit-form').submit();
		return true;
	}else{
		$.messager.alert('��ʾ��',"����ͬʱ�򿪶��֤��༭���ڣ������л����ȹرյ�ǰ�򿪵�֤��༭���ڣ�",'info', function(){win.focus();});
		return false;
	}
}
function doEditCertificate(){
	var row = $('#OriginalRecord').datagrid('getSelected');
	if(row == null){
		$.messager.alert('��ʾ��','����ѡ��һ��ԭʼ��¼��','info');
		return false;
	}
	if(row.CertificateDoc == ""||row.CertificateDoc==null){
		$.messager.alert('��ʾ��','��ԭʼ��¼��δ����֤�飬�����޸�֤�飡','info');
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
	
	doOpenEditDocWebOfficeWindow();  //�򿪱༭֤��Ĵ���
}
	$(function(){
	
	
		doLoadCommissionSheet(false);	//����ί�е�
		$('#loading').hide();	//�ȴ�������
	});
	</script>
</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		�����������У����Ժ�...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ί�е�����" />
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
	
	<div id="select_xls_template_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="ѡ��ԭʼ��¼ģ��" iconCls="icon-save" style="width:550px;height:550px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="template_file_grid" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteXlsTemplate()" >ȷ��</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectedXlsTemplateWindow()">ȡ��</a>
			</div>
		</div>
	</div>
	
	<div id="select_doc_template_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="ѡ��֤��ģ��" iconCls="icon-save" style="width:550px;height:550px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="template_doc_file_grid" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteDocTemplate()" >ȷ��</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectedDocTemplateWindow()">ȡ��</a>
			</div>
		</div>
	</div>
	
	<div id="select_verifyAndAuthorize_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="ѡ��ԭʼ��¼��֤��ĺ����˺���׼��" iconCls="icon-save" style="width:550px;height:200px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="VerifyAndAuthorizeForm" method="post">
					<input type="hidden" name="OriginalRecordId" id="VerifyAndAuthorizeForm-OriginalRecordId"  />
					<input type="hidden" name="Version" id="VerifyAndAuthorizeForm-Version"  />
					<br/>�����ˣ�<select name="Verifier" id="VerifyAndAuthorizeForm-Verifier" style="width:152px" valueField="id" textField="name" required="true" class="easyui-combobox"  editable="false"></select>&nbsp;&nbsp;
					��׼�ˣ�<select name="Authorizer" id="VerifyAndAuthorizeForm-Authorizer" style="width:152px"></select>
				</form>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteVerifyAndAuthorize()" >ȷ��</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseVerifyAndAuthorizeWindow()">ȡ��</a>
			</div>
		</div>
	</div>
		
	<div id="fee_assign_window" class="easyui-window" closed="true" modal="true" title="��ֵ����" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:550px;height:450px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="fee_info_temp_form">
				<table width="100%" height="50px" border="0" style="margin-bottom:10px">
					<tr>
						<td width="16%">֤����:</td>
						<td width="20%"><input name="CertificateCode" style="width:100px" readonly="readonly" value="" /></td>
						<td width="16%">��У��:</td>
						<td width="16%"><input name="Staff" style="width:100px" readonly="readonly" value="" /></td>
						<td width="15%">&nbsp;</td>
						<td width="17%">&nbsp;</td>
					</tr>
					<tr>
					  <td>�춨��:</td>
					  <td><input name="TestFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>�����:</td>
					  <td><input name="RepairFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>���Ϸ�:</td>
					  <td><input name="MaterialFee" style="width:100px" readonly="readonly" value="" /></td>
				  </tr>
				  <tr>
					  <td>��ͨ��:</td>
					  <td><input name="CarFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>���Է�:</td>
					  <td><input name="DebugFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>������:</td>
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
				������(0.00~1.00):<input type="text" id="FeeAssignForm-FeeAssignLimit" class="easyui-numberbox" style="width:80px" precision="2" min="0.00" max="1.00" value="0.00" />&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="doAddAnAllotee()" >�����Ա</a>
				<br/>
				<table id="fee_assign_table" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFeeAssign()" >ȷ��</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeFeeAssignWindow()">ȡ��</a>
			</div>
		</div>
	</div>
	
	<div id="fee_assign_by_sheet_window" class="easyui-window" closed="true" modal="true" title="ί�е���ֵ���䣺�����ί�е������Ҽ���ġ���δ�������֤���ֵ" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:610px;height:420px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:5px;background:#fff;border:1px solid #ccc;">
				<form id="FeeAssignFormBySheet" method="post">
					<input type="hidden" name="CommissionSheetId" id="FeeAssignFormBySheet-CommissionSheetId" value=""  />
					<input type="hidden" name="FeeAssignInfo" id="FeeAssignFormBySheet-FeeAssignInfo" />
				</form>
				<select name="FeeAllotee" id="FeeAssignFormBySheet-FeeAllotee" style="width:152px"></select> &nbsp;&nbsp;
				������(0.00~1.00):<input type="text" id="FeeAssignFormBySheet-FeeAssignLimit" class="easyui-numberbox" style="width:80px" precision="2" min="0.00" max="1.00" value="0.00" />&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="doAddAnAlloteeBySheet()" >�����Ա</a>
				<br/>
				<table id="fee_assign_table_by_sheet" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFeeAssignBySheet()" >ȷ��</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeFeeAssignWindowBySheet()">ȡ��</a>
			</div>
		</div>
	</div>

	
<div id="add_original_record_window" class="easyui-window" closed="true" collapsible="false" minimizable="false" maximizable="false" modal="true" title="�༭ԭʼ��¼" iconCls="icon-save" style="width:830px;height:650px;overflow:hidden;padding-left:5px;background:#fff;border:1px solid #ccc;">
	<form id="AddOriginalForm" method="post">
		<input type="hidden" name="OriginalRecordId" id="AddOriginalForm-OriginalRecordId" value="" />
		<input type="hidden" name="WorkStaffId" id="AddOriginalForm-WorkStaffId" value="" /><!--������ӳɹ����ݴ�ID��ÿ�δ򿪸öԻ���ʱ�����ID-->
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
				<td width="74" align="right">�������ƣ�</td>
				<td width="185" align="left">
						<select name="ApplianceName" id="AddOriginalForm-ApplianceName" style="width:152px" class="easyui-combobox" valueField="Name" textField='Name'></select></td>
				<td width="69" align="right">�������ʣ�</td>
				<td width="163" align="left">
					<select name="WorkType" id="AddOriginalForm-WorkType" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="�춨">�춨</option>
						<option value="У׼">У׼</option>
						<option value="���">���</option>
						<option value="����">����</option>
					</select>					</td>
				<td width="65" align="right">�����ص㣺</td>
				<td width="168" align="left">
					<select name="WorkLocation" id="AddOriginalForm-WorkLocation" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="����ʵ����">����ʵ����</option>
						<option value="��������ʹ���ֳ�">��������ʹ���ֳ�</option>
					</select>				    </td>
			</tr>
			<tr>
				<td align="right">���쵥λ��</td>
				<td align="left">
						<select name="Manufacturer" id="AddOriginalForm-Manufacturer" style="width:152px"  class="easyui-combobox" valueField="name" textField='name'></select>				    </td>
				<td align="right">������ţ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="ApplianceCode" />					</td>
				<td align="right">�����ţ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="ApplianceManageCode" />				    </td>
			</tr>
			<tr>
				<td align="right">�����¶ȣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="Temp" class="easyui-numberbox" precision="1" />��				    </td>
				<td align="right">���ʪ�ȣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="Humidity"  class="easyui-numberbox" precision="1" />%					</td> 
				<td align="right">�� �� ѹ��</td>
				<td align="left">
					<input type="text" style="width:152px" name="Pressure" value="/" />kPa				    </td>
			</tr>
			<tr>
				<td align="right">�� &nbsp;&nbsp; ����</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherCond" value="" /></td>
				<td align="right">��׼(����)<br/>
				  ״̬��飺</td>
				<td align="left">
					<select style="width:152px" name="StdOrStdAppUsageStatus" id="AddOriginalForm-StdOrStdAppUsageStatus"  class="easyui-combobox" panelHeight="auto" >
						<option value="����" selected="selected">����</option>
						<option value="�쳣">�쳣</option>
					</select>			  </td> 
				<td align="right">�쳣���<br/>˵����</td>
				<td align="left">
					<input type="text" style="width:152px" name="AbnormalDesc" value="/" /></td>
			</tr>
			<tr>
				<td align="right">�� &nbsp;&nbsp; �ۣ�</td>
				<td align="left">
					<select style="width:152px" name="Conclusion" id="AddOriginalForm-Conclusion"  class="easyui-combobox" panelHeight="auto" required="true" >
						<option value="�ϸ�" selected="selected">�ϸ�</option>
						<option value="���ϸ�">���ϸ�</option>
					</select></td>
				<td align="right">��У���ڣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="WorkDate" id="AddOriginalForm-WorkDate" class="easyui-datebox" required="true" />					</td>
				<td align="right">�� �� Ա��</td>
				<td align="left">
					<select style="width:152px" name="VerifyUser" id="AddOriginalForm-VerifyUser" valueField="name" textField="name" class="easyui-combobox" editable="false" ></select>				    </td>
			</tr>
			<tr>
				<td align="right">����������</td>
				<td align="left">
					<input type="text" style="width:152px" name="Quantity" class="easyui-numberbox" min="1" value="1" required="true" />��				    </td>
				<td align="right">������</td>
				<td align="left">
					<select name="RepairLevel" id="AddOriginalForm-RepairLevel" onchange="setAddOriginalFormRepairFee()" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="С">С</option>
						<option value="��">��</option>
						<option value="��">��</option>
					</select>					</td>
				<td align="right">�� �� �ѣ�</td>
				<td align="left"><input type="text" style="width:152px; background-color:#CCCCCC" name="RepairFee" readonly="readonly" /></td>
			</tr>
			<tr>
				<td align="right">�� ͨ �ѣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="CarFee" class="easyui-numberbox" min="0" />Ԫ				    </td>
				<td align="right">�� �� �ѣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="DebugFee" class="easyui-numberbox" min="0" />Ԫ					</td>
				<td align="right">�� �� �ѣ�</td>
				<td align="left"><input type="text" style="width:152px;background-color:#CCCCCC" name="TestFee" readonly="readonly" /></td>
			</tr>
			<tr>
				<td align="right">�� �� �ѣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherFee" class="easyui-numberbox" min="0" />Ԫ				    </td>
				<td align="right">�� �� �ѣ�</td>
				<td align="left"><input type="text" style="width:152px" name="MaterialFee" class="easyui-numberbox" min="0" />Ԫ</td>
				<td align="right">�����ϸ��</td>
				<td align="left"><input type="text" style="width:152px" name="MaterialDetail" /></td>
			</tr>
			<tr>
				<td align="right">�Ƿ�ǿ�ܣ�</td>
				<td align="left">
					<select name="Mandatory" id="AddOriginalForm-Mandatory" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="��">��</option>
						<option value="��">��</option>
					</select>				    </td>
				<td align="right">ǿ��Ψһ�Ժţ�</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryCode"  /></td>
				<td align="right">ǿ�����ͣ�</td>
				<td align="left">
					<select name="MandatoryPurpose" id="AddOriginalForm-MandatoryPurpose" style="width:152px" class="easyui-validatebox">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="ó�׽���">ó�׽���</option>
						<option value="ҽ������">ҽ������</option>
						<option value="��ȫ����">��ȫ����</option>
						<option value="�������">�������</option>
						<option value="��ṫ�ü�����׼����">��ṫ�ü�����׼����</option>
						<option value="���š�����ҵ��λ��߼�����׼����">���š�����ҵ��λ��߼�����׼����</option>
					</select></td>
			</tr>
			<tr>
				<td align="right">ǿ��Ŀ¼��Ӧ���</td>
				<td align="left">
					<input type="text" style="width:152px" name="MandatoryItemType" value="" /></td>
				<td align="right">ǿ��Ŀ¼��Ӧ�ֱ�</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryType" value="" /></td>
				<td align="right">ʹ��/��װ�ص㣺</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryApplyPlace" /></td>
			</tr>
			<tr>
				<td align="right">&nbsp;</td>
				
				<td align="right" colspan="5" style="padding-right:10px"><a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doOpenHistoryCertificateWindow()" >����ʷ֤�鵼��</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, true)" >�༭ԭʼ��¼</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, false)" >ֱ�ӱ���֤��</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#add_original_record_window').window('close')">ȡ��</a></td>
			</tr>
		</table>		
	</form>
</div>

<div id="function-toolbar" style="padding:2px 0">
  <table cellpadding="0" cellspacing="0" style="width:100%">
    <tr>
      <td style="text-align:left;padding-left:2px"><label>��׼��:</label>
          <select name="select" id="function-toolbar-StandardName" style="width:60px">
          </select>
        <label>�ͺ�:</label>
        <select name="text"  id="function-toolbar-Model" class="easyui-combobox" valueField="name" textField='name' style="width:60px" ></select>
        <label>��Χ:</label>
        <input name="text"  id="function-toolbar-Range" class="easyui-combobox" valueField="name" textField='name' style="width:60px" />
        <label>�ȼ�:</label>
        <input name="text"  id="function-toolbar-Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:60px" />
		<input id="standardNameIdstandardNameId" name="standardNameIdstandardNameId"  type="hidden" />
        <a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-search" plain="true" title="��ѯ�ܼ�����" id="btnHistorySearch" onclick="doSearchTargetAppliance()">��ѯ</a> </td>
    </tr>
  </table>
</div>

<div id="file_upload_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="�ѻ��ļ��ϴ�" iconCls="icon-save" style="width:500px;height:200px;padding:5px;">
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
		  <td height="39" align="left"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadExcel(0)">�ѻ��ļ��ݴ�</a> &nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadExcel(1)">�ѻ��ļ��ύ</a>&nbsp; &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#file_upload').uploadifyClearQueue();$('#file_upload_window').window('close');">�رնԻ���</a></td>
	  </tr>
 </table>
</div>

<div id="load_from_history_certificate" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="����ʷ֤���е���" iconCls="icon-save" style="width:650px;height:550px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="background:#fff;border:1px solid #ccc;">
			<table id="history_certificate_grid" width="620px" height="450px" iconCls="icon-tip"></table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doLoadHistoryCertificate()" >ȷ��</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#load_from_history_certificate').window('close');">ȡ��</a>
		</div>
	</div>
</div>



</body>
</html>
