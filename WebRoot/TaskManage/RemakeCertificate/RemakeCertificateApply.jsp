<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���±���֤��</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	$(function(){
		$('#remake_certificate_info_table').datagrid({
			title:'���±���֤����Ϣ',
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
			url:'/jlyw/RemakeCertificateServlet.do?method=2',
			remoteSort: false,
			idField:'Id',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'���񴴽���Ϣ',colspan:4,align:'center'},
					{title:'֤�������Ϣ',colspan:3,align:'center'},
					{title:'���������',colspan:1,align:'center'}
				],[
					{field:'CertificateCode',title:'ԭ֤����',width:120,sortable:true,align:'center'},
					{field:'CreatorName',title:'����������',width:100,align:'center'},
					{field:'CreateTime',title:'����ʱ��',width:80,align:'center'},
					{field:'CreateRemark',title:'��ע',width:150,align:'center'},
					
					
					{field:'ReceiverName',title:'������',width:100,align:'center'},
					{field:'FinishTime',title:'���ʱ��',width:80,align:'center'},
					{field:'FinishRemark',title:'��ע',width:150,align:'center'},
					
					{field:'PassedTime',title:'ͨ��ʱ��',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	
		});
		$('#OriginalRecord').datagrid({
			title:'ԭʼ��¼��Ϣ',
			height:250,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
			remoteSort: false,
			idField:'OriginalRecordId',
			columns:[
				[
					{title:'������Ϣ',colspan:7,align:'center'},
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
					
					{field:'ProjectName',title:'������Ŀ����',width:80,align:'center'},
					{field:'Staff',title:'��/У��Ա',width:80,align:'center'},
					{field:'WorkDate',title:'��/У����',width:80,align:'center'},
					{field:'ExcelId',title:'ԭʼ��¼Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value==""){
								return "";
							}else{
								if(rowData.ExcelPdf == ""){
									return "<span style='color: #FF0000'>δ���</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>�����</span></a>"
								}
							}
						}},
					{field:'CertificateId',title:'֤���ļ�',width:120,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.CertificateDoc==""){
								return "";
							}else{
								if(rowData.CertificatePdf == ""){
									return "<span style='color: #FF0000'>δ���</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='������ظ�ԭʼ��¼' ><span style='color: #0033FF'>"+rowData.CertificateCode+"</span></a>"
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
			rownumbers:true	
		});
	});
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/RemakeCertificateServlet.do?method=1',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#remake_certificate_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#remake_certificate_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
				$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
				
				$("#OriginalRecordId").val('');				
				//$("#Ness").removeAttr("checked");	//ȥ��ѡ
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					if(result.CommissionObj.CommissionStatus < 3){
						$.messager.alert('��ʾ��',"��֤��������ί�е���δ�깤ȷ�ϣ����ܽ������±���֤�����룡",'info');
						return false;
					}
					if(result.CommissionObj.CommissionStatus == 10){
						$.messager.alert('��ʾ��',"��֤��������ί�е���ע�������ܽ������±���֤�����룡",'info');
						return false;
					}
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//��ѡ
					}
					
					//�������������Ϣ
					$('#remake_certificate_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#remake_certificate_info_table').datagrid('reload');
					
					//����ԭʼ��¼��Ϣ
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#OriginalRecord').datagrid('reload');
					
					$("#OriginalRecordId").val(result.OriginalRecordId);
					$("#remake-certificate-submit-form-CertificateCode").val($('#CertificateCode').val());
					
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	function doSubmitRemakeCertificateForm(){   	//�ύ�����
		$("#remake-certificate-submit-form").form('submit', {
			url:'/jlyw/RemakeCertificateServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//�ж�ѡ��ļ�����Ŀ�Ƿ���Ч
				var projectValue = $('#OriginalRecordId').val();
				if(projectValue==''){
					$.messager.alert('��ʾ��',"������һ����Ч��֤���ţ�",'info');
					return false;
				}
				return $("#remake-certificate-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//���¼������������Ϣ
					$('#remake_certificate_info_table').datagrid('reload');
					
					$('CreateRemark').val('');
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
			<jsp:param name="TitleName" value="���������±���֤�飨�깤ȷ�Ϻ󣩡�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="֤���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >֤���ţ�</td>
					<td width="22%" align="left" >
						<input id="CertificateCode" class="easyui-validatebox" name="CertificateCode" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right"></td>
					<td width="22%" align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
					<td width="25%"  align="center"></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
		<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br/>
		<table id="OriginalRecord" iconCls="icon-tip" width="1005px" height="250px"></table>
		<br />
	
		<div id="p2" class="easyui-panel" style="width:1005px;height:470px;padding:10px;"
				title="���������±���֤�顯����" collapsible="false"  closable="false">
			<table id="remake_certificate_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="remake-certificate-submit-form" method="post">
			<input type="hidden" name="OriginalRecordId" id="OriginalRecordId" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="8%" style="padding-top:15px;" align="right" >
					  ֤���ţ�					</td>
					
					<td width="21%" style="padding-top:15px;" align="left"><input id="remake-certificate-submit-form-CertificateCode"   name="CertificateCode" class="easyui-validatebox" style="width:152px;" required="true" readonly="readonly"></td>
					<td width="9%" style="padding-top:15px;" align="right" >��ע��Ϣ��</td>
					<td rowspan="3" align="left" style="padding-top:15px;"><textarea id="CreateRemark" style="width:350px;height:80px"  name="CreateRemark" class="easyui-validatebox" required="true" ></textarea></td>
			    </tr>
				<tr>
					<td width="8%" style="padding-top:15px;" align="right"></td>
					<td width="21%" style="padding-top:15px;" align="left"></td>
					<td align="right"></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitRemakeCertificateForm()">ȷ�����</a></td>
				  <td  align="left" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">����</a>--></td>
			  </tr>
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
