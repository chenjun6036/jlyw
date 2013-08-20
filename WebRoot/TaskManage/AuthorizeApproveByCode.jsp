<%@ page contentType="text/html; charset=gb2312" language="java" import="com.jlyw.hibernate.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ԭʼ��¼��֤���ǩ</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/waitingDlg.js"></script>
	<script>
	$(function(){
				 
		$('#OriginalRecord').datagrid({
			title:'ԭʼ��¼��Ϣ',
			height:350,
			singleSelect:false, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			idField:'OriginalRecordId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'������Ϣ',colspan:8,align:'center'},
					{title:'������Ϣ',colspan:8,align:'center'},
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
					{field:'Staff',title:'��/У��Ա',width:80,align:'center'},
					{field:'WorkDate',title:'��/У����',width:80,align:'center'},
					{field:'CertificateCode',title:'֤����',width:120,align:'center'},
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
			toolbar:[{
				text:'ˢ��',
				iconCls:'icon-reload',
				handler:function(){
					$('#OriginalRecord').datagrid('reload');
				}
			}]
		});
		
		doLoadCommissionSheet();	//����ί�е�
	});
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
				$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});					
				
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
					
								
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	
   	 function doSubmitAuthorizeByBatch(ExecutorResult){   	//�ύǩ�ֽ��
			$('#ExecutorResult').val(ExecutorResult);
			var selectedRows = $('#OriginalRecord').datagrid('getSelections');
			if(selectedRows.length == 0){
				$.messager.alert('��ʾ��',"��ѡ��һ��ǩ������",'info');
				return false;
			}
			
			var oRecordIds = "";
			for(var i=0; i<selectedRows.length; i++){
				oRecordIds = oRecordIds + selectedRows[i].OriginalRecordId +";";
			}
			$('#OriginalRecordIds').val(oRecordIds);
			
			
			$("#Authorize-submit-form").form('submit', {
				url:'/jlyw/OriginalRecordServlet.do?method=12',	//������Ȩǩ��
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					
					var vResult = $("#Authorize-submit-form").form('validate');
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
												
						//���¼�����Ϣ
						$('#OriginalRecord').datagrid('reload');
						
						//�������������ġ���ע����Ϣ
						$('#ExecuteMsg').val('');
						
						
					}else{
						$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
					}
				}
			});  
	   }
	   
	   function doSubmitAuthorizeByExecuteNow(ExecutorResult){   	//�ύǩ�ֽ��(����ִ��)
			$('#ExecutorResult').val(ExecutorResult);
			var selectedRows = $('#OriginalRecord').datagrid('getSelections');
			if(selectedRows.length == 0){
				$.messager.alert('��ʾ��',"��ѡ��һ��ǩ������",'info');
				return false;
			}
			if(selectedRows.length > 1){
				$.messager.alert('��ʾ��',"��Ȩǩ��(����ִ��)һ��ֻ��ѡ��һ��ǩ������",'info');
				return false;
			}
			
			var oRecordIds = "";
			for(var i=0; i<selectedRows.length; i++){
				oRecordIds = oRecordIds + selectedRows[i].OriginalRecordId +";";
			}
			$('#OriginalRecordIds').val(oRecordIds);
			
			$("#Authorize-submit-form").form('submit', {
				url:'/jlyw/OriginalRecordServlet.do?method=8',	//������Ȩǩ��
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					
					var vResult = $("#Authorize-submit-form").form('validate');
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
						$.messager.alert('��ʾ','�����ɹ���','info');
												
						//���¼�����Ϣ
						$('#OriginalRecord').datagrid('reload');
						
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
			<jsp:param name="TitleName" value="ԭʼ��¼��֤���ǩ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
	
		<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br/>
	
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ԭʼ��¼" collapsible="false"  closable="false">
			<table id="OriginalRecord" iconCls="icon-tip" width="1000px" height="350px"></table>
			<br />
			<form id="Authorize-submit-form" method="post">
			<input type="hidden" name="OriginalRecordIds" id="OriginalRecordIds"  />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<input type="hidden" name="FromAuthByCode" value="true" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="16%" align="right">��ע��Ϣ��</td>
					<td colspan="4" rowspan="2" align="left"><textarea id="ExecuteMsg" style="width:350px;height:80px"  name="ExecuteMsg" class="easyui-validatebox" ></textarea></td>
				</tr>
				<tr >
					<td></td>
				</tr>
				<tr style="padding-top:15px" >
				  <td height="39"  align="right"  style="padding-right:10px;"></td>
				  <td width="19%"  align="center" ><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAuthorizeByBatch(1)">��Ȩǩ��(��̨)</a></td>
			      <td width="21%"  align="center" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitAuthorizeByExecuteNow(1)" title="����ִ����Ȩǩ�֣���ǩ��д��֤���ļ�����������֤��PDF">��Ȩǩ��(����ִ��)</a></td>
			      <td width="15%"  align="center" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitAuthorizeByBatch(0)">����</a></td>
			      <td width="29%"  align="left" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="javascript:history.go(-1)">����</a></td>
			  </tr>
		  </table>
		  </form>
		</div>		

</DIV></DIV>
</body>
</html>
