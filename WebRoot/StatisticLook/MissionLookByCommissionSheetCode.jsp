<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ҵ���ѯ</title>
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
				title:'��¼/֤����Ϣ',
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
					{field:'CertificateCode',title:'֤����',width:80,align:'center'},
					{field:'ApplianceStandardName',title:'���߱�׼����',width:80,align:'center'}
				]],
				columns:[
					[
						{title:'������Ϣ',colspan:7,align:'center'},
						{title:'������Ϣ',colspan:7,align:'center'},
						{title:'������Ϣ',colspan:7,align:'center'}
					],[
						{field:'Model',title:'����ͺ�',width:60,align:'center'},
						{field:'Range',title:'������Χ',width:80,align:'center'},
						{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:80,align:'center'},
						{field:'Manufacturer',title:'���쳧',width:80,align:'center'},
						{field:'ApplianceCode',title:'�������',width:80,align:'center'},
						{field:'ManageCode',title:'������',width:80,align:'center'},
						{field:'Quantity',title:'����',width:80,align:'center'},
						
						{field:'ProjectName',title:'������Ŀ����',width:80,align:'center'},
						{field:'Staff',title:'��/У��Ա',width:80,align:'center'},
						{field:'WorkDate',title:'��/У����',width:80,align:'center'},
						{field:'ExcelId',title:'ԭʼ��¼Excel',width:80,align:'center', 
							formatter:function(value, rowData, rowIndex){
								if(value=="" || value==null || rowData.ExcelDoc == ""){
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
								if(value=="" || rowData.CertificateDoc==""||value==null){
									return "";
								}else{
									if(rowData.CertificatePdf == ""){
										return "<span style='color: #FF0000'>δ���</span>";
									}else{
										return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>�����</span></a>"
									}
								}
							}
						},
						{field:'VerifierName',title:'������',width:80,align:'center',
							formatter:function(value, rowData, rowIndex){
								if(value==null||value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
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
								if(value==null||value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
									return "";
								}else{
									if(rowData.AuthorizeResult == ""){		//��δ����
										return "<span title='��δǩ��'>"+value+"</span>";
									}
									else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //��׼ͨ��
										return "<span style='color: #0033FF' title='��׼ʱ�䣺"+rowData.AuthorizeTime+"\r\n��׼�����ͨ��\r\n��ע��"+rowData.AuthorizeRemark+"'>"+value+"</span>";
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
				rownumbers:true,
				showFooter:true
			});
			
			$('#subcontract_info_table').datagrid({
				title:'ת��ҵ����Ϣ',
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
						{title:'ת������Ϣ',colspan:3,align:'center'},
						{title:'ת��ҵ����Ϣ',colspan:7,align:'center'}
					],[
						{field:'SubContractorName',title:'ת����',width:80,sortable:true,align:'center'},
						{field:'SubContractorContactor',title:'��ϵ��',width:60,align:'center'},
						{field:'SubContractorContactorTel',title:'��ϵ�绰',width:80,align:'center'},
						{field:'SubContractDate',title:'ת��ʱ��',width:80,align:'center'},
						{field:'Handler',title:'ת����',width:80,align:'center'},
						{field:'ReceiveDate',title:'����ʱ��',width:80,align:'center'},
						{field:'Receiver',title:'������',width:80,align:'center'},
						{field:'Remark',title:'��ע��Ϣ',width:100,align:'center'},
						{field:'LastEditor',title:'���༭��',width:80,align:'center'},
						{field:'LastEditTime',title:'���༭ʱ��',width:80,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true	,
				onSelect:function(rowIndex, rowData){
					//���¸�������ļ���Ϣ
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.Attachment};
					$('#uploaded_file_table').datagrid('reload');
				}
			});
			$('#withdraw_info_table').datagrid({
				title:'������Ϣ',
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
						{title:'������Ϣ',colspan:5,align:'center'},
						{title:'������Ϣ',colspan:6,align:'center'}
					],[
						{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
						{field:'WithdrawNumber',title:'��������',width:60,align:'center'},
						{field:'Reason',title:'����ԭ��',width:80,align:'center'},
						{field:'WithdrawDesc',title:'������Ʒ����',width:80,align:'center'},
						{field:'RequesterTime',title:'����ʱ��',width:80,align:'center'},
						
						
						{field:'ExecutorName',title:'������',width:80,align:'center'},
						{field:'ExecuteTime',title:'����ʱ��',width:80,align:'center'},
						{field:'ExecutorResult',title:'������',width:80,align:'center',
							formatter:function(value,rowData,rowIndex){
								if(0 == value) {
									return '<span style="color:red;">����</span>';
								}
								if(1 == value){
									return "ͬ������";
								}
								return "";
							}	
						},
						{field:'WithdrawDate',title:'��������',width:80,align:'center'},
						{field:'Location',title:'��Ʒ���λ��',width:80,align:'center'},
						{field:'ExecuteMsg',title:'��ע��Ϣ',width:80,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true	
			});
			
			$('#overdue_info_table').datagrid({
				title:'������Ϣ',
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
						{title:'������Ϣ',colspan:4,align:'center'},
						{title:'������Ϣ',colspan:4,align:'center'}
					],[
						{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
						{field:'DelayDays',title:'��������',width:60,align:'center'},
						{field:'Reason',title:'����ԭ��',width:80,align:'center'},
						{field:'RequesterTime',title:'����ʱ��',width:80,align:'center'},
						
						{field:'ExecutorName',title:'������',width:80,align:'center'},
						{field:'ExecuteTime',title:'����ʱ��',width:80,align:'center'},
						{field:'ExecutorResult',title:'������',width:80,align:'center',
							formatter:function(value,rowData,rowIndex){
								if(0 == value) {
									return '<span style="color:red;">����</span>';
								}
								if(1 == value){
									return "ͬ�ⳬ��";
								}
								return "";
							}	
						},
						{field:'ExecuteMsg',title:'��ע��Ϣ',width:80,align:'center'}
					]
				],
				pagination:false,
				rownumbers:true
			});
			
			$('#discount_info_table').datagrid({
				title:'�ۿ�������Ϣ',
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
					{title:'�ۿ�������Ϣ',colspan:6,align:'center'},
					{title:'�ۿ۰�����Ϣ',colspan:4,align:'center'},
			        {title:'ί�е���Ϣ',colspan:18,align:'center'}					
				],[
					{field:'CustomerName',title:'���뵥λ',width:160,sortable:true,align:'center'},
					{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
					{field:'Reason',title:'����ԭ��',width:150,align:'center'},
					{field:'RequesterTime',title:'����ʱ��',width:150,align:'center'},
					{field:'Contector',title:'ί�з�������',width:90,align:'center'},
					{field:'ContectorTel',title:'�����˵绰',width:90,align:'center'},
					{field:'ExecutorName',title:'������',width:80,sortable:true,align:'center'},
					{field:'ExecutorResult',title:'������',width:80,sortable:true,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==1)
							return "ͨ��";
						else if(value==0)
							return "����";
						else
							return "";
					}},
					{field:'ExecuteTime',title:'����ʱ��',width:80,sortable:true,align:'center'},
					{field:'ExecuteMsg',title:'����ע��Ϣ',width:80,sortable:true,align:'center'},
					{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
					{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					
					{field:'Status',title:'ί�е�״̬',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'OldTestFee',title:'�����ԭ��',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TestFee',title:'������ּ�',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldRepairFee',title:'ά�޷�ԭ��',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
					}},
					{field:'RepairFee',title:'ά�޷��ּ�',width:80,align:'right',formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
					}},
					{field:'OldMaterialFee',title:'���Ϸ�ԭ��',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'MaterialFee',title:'���Ϸ��ּ�',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldCarFee',title:'��ͨ��ԭ��',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'CarFee',title:'��ͨ���ּ�',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldDebugFee',title:'���Է�ԭ��',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'DebugFee',title:'���Է��ּ�',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldOtherFee',title:'������ԭ��',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OtherFee',title:'�������ּ�',width:80,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldTotalFee',title:'�ܷ���ԭ��',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TotalFee',title:'�ܷ����ּ�',width:80,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}}
					
					
				]],
				pagination:false,
				rownumbers:true
			});
			
			$('#uploaded_file_table').datagrid({
				title:'ת��ҵ�����ϴ����ļ�',			
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
					{field:"filename",title:"�ļ���",width:130,align:"left", 
						formatter : function(value,rowData,rowIndex){
							return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='������ظ��ļ�' ><span style='color: #0033FF'>"+value+"</span></a>"
						}
					},
					{field:"length",title:"��С",width:60,align:"left"},
					{field:"uploadDate",title:"�ϴ�ʱ��",width:120,align:"left"},
					{field:"uploadername",title:"�ϴ���",width:60,align:"left"}
				]]
			});
			
			$('#attachment_info_table').datagrid({
				title:'ί�е����ϴ��ĸ���',			
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
					{field:"filename",title:"�ļ���",width:130,align:"left", 
						formatter : function(value,rowData,rowIndex){
							return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='������ظ��ļ�' ><span style='color: #0033FF'>"+value+"</span></a>"
						}
					},
					{field:"length",title:"��С",width:60,align:"left"},
					{field:"uploadDate",title:"�ϴ�ʱ��",width:120,align:"left"},
					{field:"uploadername",title:"�ϴ���",width:60,align:"left"}
				]]
			});
			
			var StatusList = [
				{id:0,name:'���ռ�'},
				{id:1,name:'�ѷ���'},
				{id:2,name:'ת����'},
				{id:3,name:'���깤'},
				{id:4,name:'�ѽ���'},
				{id:9,name:'�ѽ���'},
				{id:10,name:'��ע��'},
				{id:-1,name:'Ԥ����'}
			];
			
			$('#CommissionStatus').combobox({
				data:StatusList,
				valueField:'id',
				textField:'name'
			});
			
			$('#fee_assign_table').datagrid({
				title:'���÷�����Ϣ',
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
						{field:'CertificateCode',title:'֤����',width:70,sortable:true,align:'center'},
						{field:'FeeAlloteeName',title:'����',width:70,align:'center'},
						{field:'TotalFee',title:'�ܷ���',width:50,align:'center'},
						{field:'TestFee',title:'�춨��',width:50,align:'center'},
						{field:'RepairFee',title:'�����',width:50,align:'center'},
						{field:'MaterialFee',title:'���Ϸ�',width:50,align:'center'},
						{field:'CarFee',title:'��ͨ��',width:50,align:'center'},
						{field:'DebugFee',title:'���Է�',width:50,align:'center'},
						{field:'OtherFee',title:'������',width:50,align:'center'},
						{field:'Remark',title:'��ע',width:380,align:'center'}
					]
				],
				rownumbers:true	,
				pagination:false
			});
			
			doLoadCommissionSheet();
			
		});	
		

		var printObj;
		function doLoadCommissionSheet(){	//����ί�е�
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
					
					//��ո����б�
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
							$("#Ness").attr("checked",true);	//��ѡ
						}
						
						//����ԭʼ��¼��Ϣ
						$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#OriginalRecord').datagrid('reload');
						
						//���ط��÷�����Ϣ
						$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':$('#CommissionId').val()};
						$('#fee_assign_table').datagrid('reload');
						
						//����ת����Ϣ
						$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#subcontract_info_table').datagrid('reload');
						
						//�����ۿ���Ϣ
						$('#discount_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#discount_info_table').datagrid('reload');
						
						//�������������Ϣ
						$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#overdue_info_table').datagrid('reload');
						
						//����������Ϣ
						$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
						$('#withdraw_info_table').datagrid('reload');
						
						//���ظ�����Ϣ
						$('#attachment_info_table').datagrid('options').queryParams={'FilesetName':result.CommissionObj.Attachment};
						$('#attachment_info_table').datagrid('reload');	
											
					}else{
						$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
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
			<jsp:param name="TitleName" value="ҵ���ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1050px;height:80px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
		<form id="SearchForm" method="post">
            <table width="850px" id="table1">        
				<tr >
					<td>ί�е���ţ�</td>
					<td>
						<input id="Code" type="text" class="easyui-validatebox" name="Code" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>"  />
                        <input style="display:none"/>
					</td>
				    <td align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
                    <td align="center"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="goback()">����</a></td>
				</tr>
				
			</table>
        </form>
		</div>
        <br />
        <div id="Details" class="easyui-tabs" style="width:1050px;height:400px;">
        	<div style="padding:20px;" title="ί�е���Ϣ">
                <form id="CommissionSheetForm" method="post">
                <input type="hidden" id="CommissionId" name="CommissionId" value="" />
                <input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
                <input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
                <table width="1000px" id="table1">
                    <tr>
                      <td width="77" align="right">ί����ʽ��</td>
                      <td width="187"  align="left">
                            <select name="CommissionType" style="width:152px" class="easyui-combobox">
                                <option value="1">�������</option>
                                <option value="5">����ҵ��</option>
                                <option value="6">�Լ�ҵ��</option>
                                <option value="7">�ֳ�����</option>
                                <option value="3">��������</option>
                                <option value="4">��ʽ����</option>
                                <option value="2">�ֳ����</option>
                            </select>
                      </td>
                      <td width="77" align="right">ί�����ڣ�</td>
                      <td width="187"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
                      <td width="100"  align="right">ί�е�״̬��</td>
                      <td width="187" align="left"><input style="width:151px;" class="easyui-combobox" name="CommissionStatus" id="CommissionStatus" editable="false"/></td>
                      <td width="120" align="right">������ʽ��</td>
					  <td width="187" align="left"><select id="ReportType" name="ReportType" style="width:152px">
						<option value="1">�춨</option>
						<option value="2">У׼</option>
						<option value="3">���</option>
						<option value="4">����</option>
					</select></td>
                    </tr>
                <tr>
                  <td align="right">ί�е�λ��</td>
                  <td align="left"><input type="text" name="CustomerName" id="CustomerName" readonly="readonly"/></td>
                  <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
                  <td align="left"><input name="CustomerTel" id="CustomerTel" type="text" readonly="readonly"/></td>
                  <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ַ��</td>
                  <td align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" readonly="readonly"/></td>
                  <td align="right">�������룺</td>
                  <td align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
                    <td align="left"><input name="ContactPerson" id="ContactPerson" type="text" readonly="readonly"/></td>
                    <td align="right">�ֻ����룺</td>
                    <td align="left"><input name="ContactorTel" id="ContactorTel" type="text" readonly="readonly"/></td>
                    <td align="right">֤�鵥λ��</td>
                    <td align="left"><input name="SampleFrom" id="SampleFrom" type="text" readonly="readonly"/></td>
                    <td align="right">��Ʊ��λ��</td>
                    <td align="left"><input name="BillingTo" id="BillingTo" type="text" readonly="readonly"/></td>
                </tr>
   				<tr>
                    <td align="right">��ҵ���ࣺ</td>
                    <td align="left"><input id="CustomerClassification" name="CustomerClassification" type="text" readonly="readonly"/></td>
                    <td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                    <td align="left"><input id="Receiver" name="Receiver" type="text" readonly="readonly"/></td>
                    <td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                    <td align="left"><input id="FinishStaff" name="FinishStaff" type="text" readonly="readonly"/></td>
                    <td align="right">�깤���ڣ�</td>
                    <td align="left"><input id="FinishDate" name="FinishDate" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                    <td align="left"><input id="CheckOutStaff" name="CheckOutStaff" type="text" readonly="readonly"/></td>
                    <td align="right">����ʱ�䣺</td>
                    <td align="left"><input id="CheckOutTime" name="CheckOutTime" type="text" readonly="readonly"/></td>
                    <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br/>ί����ţ�</td>
                    <td align="left"><input id="LocaleCommissionCode" name="LocaleCommissionCode" type="text" readonly="readonly"/></td>
                    <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br/>���ʱ�䣺</td>
                    <td align="left"><input id="LocaleCommissionDate" name="LocaleCommissionDate" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">�������ƣ�</td>
                    <td align="left"><input id="ApplianceName" name="ApplianceName" type="text" readonly="readonly"/></td>
                    <td align="right">�ͺŹ��</td>
                  	<td align="left"><input id="Model" name="Model" type="text" readonly="readonly"/></td>
                    <td align="right">������ţ�</td>
                  	<td align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" readonly="readonly"/></td>
                    <td align="right">�����ţ�</td>
                  	<td align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td align="right">�� �� ����</td>
                    <td align="left"><input id="Manufacturer" name="Manufacturer" type="text" readonly="readonly"/></td>
                    <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
    
                    <td align="left"><input id="Quantity" name="Quantity" type="text" readonly="readonly"/>��</td>
                    <td align="right">�Ƿ�ǿ�죺</td>
                    <td align="left">
                        <select id="Mandatory" name="Mandatory" style="width:152px" class="easyui-combobox">
                            <option selected="selected" value="1" >��ǿ�Ƽ춨</option>
                            <option value="0">ǿ�Ƽ춨</option>
                        </select>
    
                    </td>
                    <td align="left"><input id="Ness" name="Ness" type="checkbox" disabled="disabled"/>��&nbsp;&nbsp;��</td>
                    <td align="left">&nbsp;</td>
                </tr>
                <tr>
                    <td align="right">��۸�����</td>
                    <td align="left"><input id="Appearance" name="Appearance" type="text" readonly="readonly"/></td>
                    <td align="right">����Ҫ��</td>
                    <td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text" readonly="readonly"/></td>
                    <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
                    <td align="left" colspan="3"><input id="Remark" name="Remark" type="text" readonly="readonly" style="width:350px"/></td>
                </tr>
                <tr height="50">
                    <td width="300" align="right"><a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onClick="printCom()">��ӡί�е�</a></td>
                </tr>
             </table>
            </form>
            </div>
            <div style="padding:20px;" title="��¼/֤����Ϣ">
            	<table id="OriginalRecord" iconCls="icon-tip"></table>
            </div>
            <div style="padding:20px;" title="���÷�����Ϣ">
                <table id="fee_assign_table" iconCls="icon-tip"></table>
             </div>
             <div style="padding:20px;" title="ת��ҵ����Ϣ">
                <table id="subcontract_info_table" iconCls="icon-tip"></table>
                <br />
                <table id="uploaded_file_table" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104" iconCls="icon-tip"></table>
             </div>
             <div style="padding:20px;" title="������Ϣ">
                <table id="withdraw_info_table" iconCls="icon-tip"></table>
             </div>             
             <div style="padding:20px;" title="������Ϣ">
                <table id="overdue_info_table" iconCls="icon-tip"></table>
             </div>            
             <div style="padding:20px;" title="�ۿ���Ϣ">
                <table id="discount_info_table" iconCls="icon-tip"></table>
             </div>
             <div style="padding:20px;" title="ί�е�����">
                <table id="attachment_info_table" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104" iconCls="icon-tip"></table>
             </div>
        </div>
	  <!--<div id="p2" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="������" collapsible="false"  closable="false">
			<form id="allot" method="post">
			<table width="850px" >
				
				<tr >
				     <td width="33%"  align="right" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search1()">�鿴ԭʼ��¼</a>
	                     
					</td>
					<td  width="33%" align="center" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search2()">����֤��</a>
	                     
					</td>
					<td  width="33%" align="left" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">����</a>
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
