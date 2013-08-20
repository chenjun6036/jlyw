<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ͻ�ȡ��</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	 <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		$('#OriginalRecord').datagrid({
			title:'ԭʼ��¼��Ϣ',
			width:1005,
			height:350,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
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
		
		$('#subcontract_info_table').datagrid({
			title:'ת��ҵ����Ϣ',
//			iconCls:'icon-save',
			width:1005,
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
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					
					$('#subcontract_info_table').datagrid('selectRow', 0);
				}
			}
		});
				
		});
		
		function doConfirm(){
			if($('#CommissionSheetId').val()==''){
				$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
				return false;
			}
	
		    var result = confirm("��ȷ��Ҫ���пͻ�ȡ����?");
			if(result == false){
				return false;
			}
			 $('#Confirm').form('submit',{
				url: '/jlyw/CommissionSheetServlet.do?method=7',
				onSubmit:function(){ return $('#Confirm').form('validate');},
		   		success:function(data){
			   	   var result = eval("("+data+")");
		   		   
		   		   alert(result.msg);
		   		 }
			});
		}
		function doLoadCommissionSheet(){	//����ί�е�
			
			$("#SearchForm").form('submit', {
				url:'/jlyw/CommissionSheetServlet.do?method=3',
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					$("#CommissionSheetForm").form('clear');
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
					$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
					
					if($('#Code').val()=='' || $('#Pwd').val() == ''){
						$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
						return false;
					}
			
					$("#CommissionSheetId").val('');				
					$("#Ness").removeAttr("checked");	//ȥ��ѡ
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
					
					//����ת����Ϣ
					$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#subcontract_info_table').datagrid('reload');
						
					$("#CommissionSheetId").val(result.CommissionObj.CommissionId);    //ί�е�ID					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
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
			<jsp:param name="TitleName" value="�ͻ�ȡ��" />
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
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a>&nbsp;&nbsp;<a id="btn_confirm" class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doConfirm()" >ȡ��ȷ��</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
    	<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br/>
      <table id="OriginalRecord" iconCls="icon-tip"  ></table>
     
      <table id="subcontract_info_table" iconCls="icon-tip"></table>
      
		<form id="Confirm" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
	    </form>


</DIV></DIV>
</body>
</html>
