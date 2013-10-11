<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���������б�</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../../JScript/waitingDlg.js"></script>
	<script>
		$(function(){			
			$('#task-table').datagrid({
				title:'�������ԭʼ��¼��֤��',
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/OriginalRecordServlet.do?method=5&type=1',//type=1�������±���֤��ĺ���
				queryParams:{'CommissionNumber':encodeURI($('#Search_CommissionNumber').val())},
//				sortName: 'id',
//				sortOrder: 'desc',
				remoteSort: true,
				idField:'OriginalRecordId',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {field:'CertificateCode',title:'֤����',width:120,rowspan:2,align:'center'},
					{title:'ί�е���Ϣ',colspan:3,align:'center'},					
					{title:'������Ϣ',colspan:7,align:'center'},
					{title:'������Ϣ',colspan:5,align:'center'}
				],[
					{field:'CommissionCode',title:'ί�е���',width:100,align:'center'},
					{field:'CustomerName',title:'ί�е�λ',width:120,align:'center'},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					
					{field:'ProjectName',title:'������Ŀ����',width:80,align:'center'},
					{field:'ExcelId',title:'ԭʼ��¼Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.ExcelDoc==""){
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
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>�����</span></a>"
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
					{field:'Staff',title:'��/У��Ա',width:80,align:'center'},
					{field:'WorkDate',title:'��/У����',width:80,align:'center'},
					
					{field:'ApplianceStandardName',title:'���߱�׼����',width:80,sortable:true,align:'center'},
					{field:'Model',title:'����ͺ�',width:60,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Accuracy',title:'׼ȷ�ȵȼ�',width:80,align:'center'},
					{field:'Quantity',title:'��������',width:60,align:'center'},
					{field:'CreateRemark',title:'���±���֤�鱸ע',width:150,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#task-table-search-toolbar",
				onLoadSuccess:function(data){
					if(data.rows.length ==1){
						$(this).datagrid('selectRow', 0);
					}
				}
			});
			
		});
		
		function doSearchTaskList()
		{
			$('#task-table').datagrid('options').queryParams={'CommissionNumber':encodeURI($('#Search_CommissionNumber').val()),'CustomerName':encodeURI($('#Search_CustomerName').val())};
			$('#task-table').datagrid('reload');
		}
		
	   
	   
	   function doTask()	//�鿴��ϸ
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#OriginalRecordId').val(selectedRow.OriginalRecordId);
			$('#VetifyForm').submit();
		}
		
		//�򿪺���Ի���
		function doOpenVerifyWindow(){
			var selectedRows = $('#task-table').datagrid('getSelections');
			if(selectedRows.length == 0){
				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
				return false;
			}
			
			var oRecordIds = "";
			for(var i=0; i<selectedRows.length; i++){
				oRecordIds = oRecordIds + selectedRows[i].OriginalRecordId +";";
			}
			$('#OriginalRecordIds').val(oRecordIds);
			$("#verify_window").window('open');	
		}
		function doCloseVerifyWindow(){
			$("#verify_window").window('close');	
		}
		function doSubmitVerify(ExecutorResult){   	//�ύ������
			$('#ExecutorResult').val(ExecutorResult);
			if($('#OriginalRecordIds').val()==""){
				$.messager.alert('��ʾ��',"��ѡ��һ����������",'info');
				return false;
			}
			$("#verify-submit-form").form('submit', {
				url:'/jlyw/OriginalRecordServlet.do?method=6',	//������Ȩǩ��
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					
					var vResult = $("#verify-submit-form").form('validate');
					if(vResult == false){
						return false;
					}else{
						ShowWaitingDlg("����ִ�У����Ժ�...");
						return true;
					}
				},
				success:function(data){
					CloseWaitingDlg();
						
					var result = eval("("+data+")");
					if(result.IsOK){
						$.messager.alert('��ʾ',result.msg,'info');
						
						doCloseVerifyWindow();
						
						$('#task-table').datagrid('unselectAll');	//ȡ������ѡ��
						
						//���¼�����Ϣ
						$('#task-table').datagrid('reload');
						
						//�������������ġ���ע����Ϣ
						$('#ExecuteMsg').val('');
						
						
					}else{
						$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
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
			<jsp:param name="TitleName" value="ԭʼ��¼��֤����������ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<table id="task-table" iconCls="icon-search"></table>

	<form method="post" action="/jlyw/TaskManage/AuthorizeApprove.jsp" id="VetifyForm">
		<input type="hidden" name="Code" id="Code" value="" />
		<input type="hidden" name="Pwd" id="Pwd" value="" />
		<input type="hidden" name="OriginalRecordId" id="OriginalRecordId" value="" />
	</form>
	<div id="task-table-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">�鿴��ϸ</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" title="��ѡ�е�ԭʼ��¼��֤��¼�������" iconCls="icon-add" plain="true" onclick="doOpenVerifyWindow()">�ύ������</a>
					</td>
				<td style="text-align:right;padding-right:4px">
					<label>ί�е��ţ�</label><input type="text" id="Search_CommissionNumber" value="<%= request.getParameter("commissionsheetcode")==null?"":request.getParameter("commissionsheetcode") %>" style="width:120px" />&nbsp;<label>ί�е�λ��</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��Ҫ���Һ����ԭʼ��¼��֤��" id="btnHistorySearch" onclick="doSearchTaskList()">��ѯ��������</a>
				</td>
			</tr>
		</table>
	</div>


<div id="verify_window" class="easyui-window" closed="true" modal="true" title="ԭʼ��¼��֤�����" iconCls="icon-save" style="width:600px;height:250px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form id="verify-submit-form" method="post">
			<input type="hidden" name="OriginalRecordIds" id="OriginalRecordIds"  />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<table width="500" style="margin-left:20px" >
				<tr>
					<td width="22%" align="right">��ע��Ϣ��</td>
					<td width="78%" colspan="3" rowspan="2" align="left"><textarea id="ExecuteMsg" style="width:300px;height:80px"  name="ExecuteMsg" class="easyui-validatebox" ></textarea></td>
				</tr>
				<tr >
					<td></td>
				</tr>
		  </table>
		  </form>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitVerify(1)">����ͨ��</a>&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitVerify(0)">����</a>&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onclick="doCloseVerifyWindow()">ȡ��</a>&nbsp;&nbsp;&nbsp;
		</div>
	</div>
</div>
	
	</DIV>
</DIV>
</body>
</html>
