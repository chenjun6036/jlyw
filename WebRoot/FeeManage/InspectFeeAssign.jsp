<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���÷���</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script>
$(function(){		
	$("#ProjectSelect").combobox({
		valueField:'appStdProId',
		textField:'appStdProName',
		editable:false,
		panelHeight:'auto', 
		onSelect:function(record){
			$("#AlloteeSelect").combobox('clear');
			$("#AlloteeSelect").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType=0&ApplianceSpeciesId='+record.appStdNameId);	//������Ա
//				$("#AlloteeSelect").combobox('loadData', record.AlloteeList);
		}
	});
		
	$('#allot_info_table').datagrid({
		title:'������Ŀ��Ϣ',
//			iconCls:'icon-save',
//			width:900,
		height:200,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
		url:'/jlyw/TaskAssignServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		//idField:'userid',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'AppStdNameProTeamName',title:'������Ŀ����',width:120,sortable:true},
			{field:'AlloteeName',title:'������Ա',width:80},
			{field:'AssignerName',title:'������',width:80},
			{field:'AssignTime',title:'����ʱ��',width:80},
			{field:'TestFee',title:'����',width:70},
			{field:'RepairFee',title:'�����',width:70},
			{field:'MaterialFee',title:'���Ϸ�',width:70},
			{field:'CarFee',title:'��ͨ��',width:70},
			{field:'DebugFee',title:'���Է�',width:70},
			{field:'OtherFee',title:'������',width:70},
			{field:'TotalFee',title:'�ܷ���',width:70},
			{field:'LastFeeAssignerName',title:'�ϴη��÷�����',width:80},
			{field:'LastFeeAssignTime',title:'�ϴη��÷���ʱ��',width:80}
		]],
		pagination:false,
		rownumbers:true	,
		toolbar:[{
			text:'ע��',
			iconCls:'icon-remove',
			handler:function(){
				var row = $("#allot_info_table").datagrid('getSelected');
				if(row){
					var result = confirm("�ò��������棬��ȷ��Ҫע����������");
					if(result == false){
						return false;
					}
					$.ajax({
							type: "post",
							url: "/jlyw/TaskAssignServlet.do?method=9",
							data: {"TaskId":row.TaskId},
							dataType: "json",	//�������������ݵ�Ԥ������
							beforeSend: function(XMLHttpRequest){
								//ShowLoading();
							},
							success: function(data, textStatus){
								if(data.IsOK){
									$('#allot_info_table').datagrid('reload');
									//����ԭʼ��¼��Ϣ
									$('#OriginalRecord').datagrid('reload');
										
									//����ת��ҵ����Ϣ
									$('#subcontract_info_table').datagrid('reload');
								}else{
									$.messager.alert('ע��ʧ�ܣ�',data.msg,'error');
								}
							},
							complete: function(XMLHttpRequest, textStatus){
								//HideLoading();
							},
							error: function(){
								//���������
							}
					});
				}else{
					$.messager.alert('��ʾ��',"����ѡ��һ����¼",'info');
				}
			}
		},'-',{
			text:'��Ӽ���Ա',
			iconCls:'icon-add',
			handler:function(){
				$("#task_assign_window").window('open');
			}
		}],
		onSelect:function(rowIndex, rowData){
			//load ��
			$('#FeeSubmitForm').form('load', rowData);
		}	
	});
	$('#OriginalRecord').datagrid({
		title:'ԭʼ��¼��Ϣ',
		height:250,
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
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{title:'������Ϣ',colspan:7,align:'center'},
				{title:'������Ϣ',colspan:7,align:'center'}
			],[
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
		rownumbers:true	,
		toolbar:[{
			text:'ˢ��',
			iconCls:'icon-reload',
			handler:function(){
				$('#OriginalRecord').datagrid('reload');
			}
		}]
	});
	$('#subcontract_info_table').datagrid({
		title:'ת��ҵ����Ϣ',
//		iconCls:'icon-save',
//		width:900,
		height:200,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//		collapsible:true,
		url:'/jlyw/SubContractServlet.do?method=3',
//		sortName: 'userid',
// 		sortOrder: 'desc',
		remoteSort: false,
		idField:'SubContractId',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[
			[
				{title:'ת������Ϣ',colspan:3,align:'center'},
				{title:'ת��ҵ����Ϣ',colspan:8,align:'center'}
			],[
				{field:'SubContractorName',title:'ת����',width:80,sortable:true,align:'center'},
				{field:'SubContractorContactor',title:'��ϵ��',width:60,align:'center'},
				{field:'SubContractorContactorTel',title:'��ϵ�绰',width:80,align:'center'},
				{field:'SubContractDate',title:'ת��ʱ��',width:80,align:'center'},
				{field:'Handler',title:'ת����',width:80,align:'center'},
				{field:'TotalFee',title:'ת������',width:80,align:'center'},
				{field:'ReceiveDate',title:'����ʱ��',width:80,align:'center'},
				{field:'Receiver',title:'������',width:80,align:'center'},
				{field:'Remark',title:'��ע��Ϣ',width:100,align:'center'},
				{field:'LastEditor',title:'���༭��',width:80,align:'center'},
				{field:'LastEditTime',title:'���༭ʱ��',width:80,align:'center'}
			]
		],
		pagination:false,
		rownumbers:true	
	});
});
function doLoadCommissionSheet(){	//����ί�е�
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#allot_info_table').datagrid('options').queryParams={'CommissionId':''};
			$('#allot_info_table').datagrid('loadData',{total:0,rows:[]});
			
			$("#CommissionSheetId").val();
			$("#ProjectSelect").combobox('clear');
			$("#ProjectSelect").combobox('loadData',[]);
			$("#AlloteeSelect").combobox('clear');
			$("#AlloteeSelect").combobox('loadData',[]);
			
			//$("#Ness").removeAttr("checked");	//ȥ��ѡ
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				if(result.CommissionObj.CommissionStatus >= 3){
					$.messager.alert('��ʾ��',"��ί�е����ܽ��з��÷���",'info');
					return false;
				}
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//��ѡ
				}
				
				//�������������Ϣ
				$('#allot_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#allot_info_table').datagrid('reload');
				
				//����ԭʼ��¼��Ϣ
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#OriginalRecord').datagrid('reload');
					
				//����ת��ҵ����Ϣ
				$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#subcontract_info_table').datagrid('reload');
				
				
				
				$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
				//���ؼ�����Ŀ��������Ա��Ϣ
				$("#ProjectSelect").combobox('reload','/jlyw/TaskAssignServlet.do?method=2&CommissionId='+$('#CommissionId').val());
				
			}else{
				$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}
function doTaskAllotee(){   	//�ύ��������
	$("#TaskAlloteeForm").form('submit', {
		url:'/jlyw/TaskAssignServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			//�ж�ѡ��ļ�����Ŀ�Ƿ���Ч
			var projectValue = $('#ProjectSelect').combobox('getValue');
			if(projectValue==''){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ļ�����Ŀ",'info');
				return false;
			}
			//�ж�ѡ�����Ա�Ƿ���Ч
			var alloteeValue = $('#AlloteeSelect').combobox('getValue');
			if(alloteeValue == ''){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ļ�����Ա",'info');
				return false;
			}
			var alloteeChecked = false;
			var alloteeAllData = $('#AlloteeSelect').combobox('getData');
			if(alloteeAllData != null && alloteeAllData.length > 0){
				for(var i=0; i<alloteeAllData.length; i++)
				{
					if(alloteeValue==alloteeAllData[i].id){
						alloteeChecked = true;
						break;
					}
				}
			}
			if(!alloteeChecked){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ļ�����Ա",'info');
				return false;
			}
			
			
			//�жϼ�����Ŀ�Ƿ����
			var allTask = $('#allot_info_table').datagrid('getRows');
			for(var i=0; i<allTask.length; i++){
				if(projectValue == allTask[i].AppStdNameProTeamId && alloteeValue == allTask[i].AlloteeId){
					$.messager.alert('��ʾ��',"һ��������Ŀ�����ظ������ͬһ���ˣ�����Ҫ���·��䣬����ע���ѷ���ļ�����Ŀ��",'info');
					return false;
				}
			}
			return $("#TaskAlloteeForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$('#task_assign_window').window('close');
				//���¼������������Ϣ
				$('#allot_info_table').datagrid('reload');
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});  
} 
function doFeeSubmit(){   	//�ύ���÷���
	$("#FeeSubmitForm").form('submit', {
		url:'/jlyw/TaskAssignServlet.do?method=10',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			//�ж�ѡ��ļ�����Ŀ�Ƿ���Ч
			var taskIdValue = $('#TaskId').val();
			if(taskIdValue==''){
				$.messager.alert('��ʾ��',"��ѡ��һ��������Ŀ",'info');
				return false;
			}
			return $("#FeeSubmitForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//���¼������������Ϣ
				$('#allot_info_table').datagrid('reload');
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
			<jsp:param name="TitleName" value="���÷���" />
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
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
				title="ί�е���Ϣ" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<table width="1000px" id="table1">
				<tr>
				  <td width="77" align="right">ί����ʽ��</td>
				  <td width="188"  align="left">
						<select name="CommissionType" style="width:152px">
							<option value="1">�������</option>
							<option value="5">����ҵ��</option>
							<option value="6">�Լ�ҵ��</option>
							<option value="7">�ֳ�����</option>
							<option value="3">��������</option>
							<option value="4">��ʽ����</option>
							<option value="2">�ֳ����</option>
						</select>
				  </td>
				  <td width="65" align="right">ί�����ڣ�</td>
				  <td width="650"  align="left"><input style="width:151px;" name="CommissionDate" id="CommissionDate" type="text" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">ί�е�λ��</td>
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" style="width:152px;" /></td>
                <td width="64" align="right">&nbsp;��&nbsp;&nbsp;����</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">&nbsp;��&nbsp;&nbsp;ַ��</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>
				<td width="65" align="right">�������룺</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" /></td>
				<td align="right">�ֻ����룺</td>
				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">֤�鵥λ��</td>
				<td align="left"><input name="SampleFrom" id="SampleFrom" type="text" /></td>
                <td align="right">��Ʊ��λ��</td>
				<td align="left"><input name="BillingTo" id="BillingTo" type="text" /></td>
			</tr>
		</table><br/>
 		 <table id="table2" width="1000">
			<tr>
				<td width="77" align="right">�������ƣ�</td>
                <td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" /></td>
                <td width="64" align="right">�ͺŹ��</td>
			  <td width="171"  align="left"><input id="Model" name="Model" type="text" /></td>
				<td width="64" align="right">������ţ�</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>
				<td width="65" align="right">�����ţ�</td>
			  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">�� �� ����</td>
				<td align="left"><input id="Manufacturer" name="Manufacturer" type="text"  /></td>
				<td align="right">��&nbsp;&nbsp;&nbsp;����</td>
				<td align="left"><input id="Quantity" name="Quantity" type="text"/>��</td>
				<td align="right">�Ƿ�ǿ�죺</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option selected="selected" value="1" >��ǿ�Ƽ춨</option>
						<option value="0">ǿ�Ƽ춨</option>
					</select>
				</td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox" />��&nbsp;&nbsp;��</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">��۸�����</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text"  /></td>
				<td align="right">����Ҫ��</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text"  /></td>
				<td align="right"></td>
				<td align="left"></td>
                <td align="left"></td>
				<td align="left">&nbsp;</td>
			</tr>
	 	 </table><br/>
		</form>
		</div><br/>
		
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;+position:relative;"
				title="�����÷���" collapsible="false"  closable="false">
			<table id="allot_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<br />
			<form id="FeeSubmitForm" method="post">
			<input type="hidden" id="TaskId" name="TaskId" value="" />
			<table width="950">
				<tr>
				    <td width="60" align="right" >������Ŀ:</td>
					<td width="150" align="left"><input type="text" id="AppStdNameProTeamName" name="AppStdNameProTeamName" style="width:120px;" readonly="readonly" value="" /></td>
					<td width="60" align="right" >������Ա:</td>
					<td width="168"  align="left"><input id="AlloteeName" name="AlloteeName" type="text" style="width:120px;" readonly="readonly" value="" /></td>
					<td width="80" align="right" >����:</td>
					<td width="150" align="left"><input id="TestFee" name="TestFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
					<td width="60" align="right" >�����:</td>
					<td width="150"  align="left"><input id="RepairFee" name="RepairFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
				</tr>
				<tr>
					<td align="right" >���Ϸ�:</td>
					<td align="left"><input id="MaterialFee" name="MaterialFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
					<td align="right" >��ͨ��:</td>
					<td align="left"><input id="CarFee" name="CarFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
					<td align="right" >���Է�:</td>
					<td align="left"><input id="DebugFee" name="DebugFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
					<td align="right" >������:</td>
					<td align="left"><input id="OtherFee" name="OtherFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
				</tr>
				<tr>
					<td colspan="4" align="right" style="padding-top:15px;"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doFeeSubmit()">¼�������Ϣ</a>&nbsp;&nbsp;</td>
					<td colspan="4" align="left">  </td>
				</tr>
			 </table>
		  </form>
		</div><br/>
		<table id="OriginalRecord" iconCls="icon-tip" width="1000px" height="250px"></table><br/>
		<table id="subcontract_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>

</DIV></DIV>
<div id="task_assign_window" class="easyui-window" closed="true" modal="true" title="��Ӽ�����Ա" iconCls="icon-save" style="width:550px;height:200px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form id="TaskAlloteeForm" method="post">
				<input type="hidden" id="CommissionSheetId" name="CommissionSheetId" value="" />
				<br/>������Ŀ��<select id="ProjectSelect" name="ProjectSelect" style="width:152px"></select>&nbsp;&nbsp;
				������Ա��<select id="AlloteeSelect" class="easyui-combobox" name="AlloteeSelect" style="width:152px;" valueField="id" textField="name" panelHeight="150" ></select>
			</form>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doTaskAllotee()" >ȷ��</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#task_assign_window').window('close');">ȡ��</a>
		</div>
	</div>
</div>
</body>
</html>
