<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�鿴ί�е���Ϣ</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
   <!-- <script type="text/javascript" src="../JScript/upload.js"></script>-->
	<script>
	$(function(){
		//$('#subcontract').hide();
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
			/*frozenColumns:[[
				{field:'ck',checkbox:true}
			]],*/
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
			width:980,
			height:200,
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
			},
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#subcontract').panel('open');
					$('#subcontract_info_table').datagrid('selectRow', 0);
				}
			}
		});
		$('#withdraw_info_table').datagrid({
			title:'������Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
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
			rownumbers:true	,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#withdraw').panel('open');
				}
			}
		});
		
		$('#overdue_info_table').datagrid({
			title:'������Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
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
			rownumbers:true		,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#overdue').panel('open');
				}
			}
		});
		
		$('#discount_info_table').datagrid({
			title:'�ۿ�������Ϣ',
//			iconCls:'icon-save',
			width:950,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/DiscountServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'������Ϣ',colspan:8,align:'center'},
					{title:'������Ϣ',colspan:4,align:'center'}
				],[
					{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
					{field:'DiscountAmount',title:'�ۿ۶��',width:60,align:'center'},
					{field:'OldPrice',title:'ԭ��',width:80,align:'center'},
					{field:'NewPrice',title:'�ּ�',width:80,align:'center'},
					{field:'Reason',title:'����ԭ��',width:120,align:'center'},
					{field:'RequesterTime',title:'����ʱ��',width:150,align:'center'},
					{field:'Contector',title:'ί�з�������',width:90,align:'center'},
					{field:'ContectorTel',title:'�����˵绰',width:90,align:'center'},
					
					{field:'ExecutorName',title:'������',width:80,align:'center'},
					{field:'ExecuteTime',title:'����ʱ��',width:80,align:'center'},
					{field:'ExecutorResult',title:'������',width:90,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">����</span>';
							}
							if(1 == value){
								return "ͬ���ۿ�����";
							}
							return "";
						}	
					},
					{field:'ExecuteMsg',title:'��ע��Ϣ',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#discount').panel('open');
				}
			}
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
					
					//����ת����Ϣ
					$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#subcontract_info_table').datagrid('reload');
					
					//�����ۿ���Ϣ
					$('#discount_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#discount_info_table').datagrid('reload');
					
					//�������������Ϣ
					$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#overdue_info_table').datagrid('reload');
					
					//�������������Ϣ
					$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#withdraw_info_table').datagrid('reload');					
										
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	function back(){
		history.go(-1);
	}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�鿴ί�е���Ϣ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

		<form id="SearchForm" method="post" >
		<input id="Code" type="hidden" name="Code" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" style="width:150px;" />
		<input id="Pwd" type="hidden" name="Pwd" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" style="width:150px;" />
		</form>
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:10px;"
				title="ί�е���Ϣ" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<table width="1000px" id="table1">
				<tr>

				  <td width="77" align="right">ί����ʽ��</td>
				  <td width="185"  align="left">
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
				  <td width="68" align="right">ί�����ڣ�</td>
				  <td width="650"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">ί�е�λ��</td>
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" readonly="readonly"/></td>
                <td width="64" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text" readonly="readonly"/></td>
				<td width="64" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ַ��</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" readonly="readonly"/></td>

				<td width="65" align="right">�������룺</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" readonly="readonly"/></td>
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
		</table><br/>
 		 <table id="table2" width="1000">

			<tr>
				<td width="77" align="right">�������ƣ�</td>
                <td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" readonly="readonly"/></td>
                <td width="64" align="right">�ͺŹ��</td>
			  <td width="171"  align="left"><input id="Model" name="Model" type="text" readonly="readonly"/></td>
				<td width="64" align="right">������ţ�</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" readonly="readonly"/></td>

				<td width="65" align="right">�����ţ�</td>
			  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" readonly="readonly"/></td>
			</tr>
			<tr>
				<td align="right">�� �� ����</td>
				<td align="left"><input id="Manufacturer" name="Manufacturer" type="text" readonly="readonly"/></td>
				<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>

				<td align="left"><input id="Quantity" name="Quantity" type="text" readonly="readonly"/>��</td>
				<td align="right">�Ƿ�ǿ�죺</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px" class="easyui-combobox" disabled="disabled">
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
				<td align="right"></td>
				<td align="left"></td>
                <td align="left"></td>
				<td align="left">&nbsp;</td>
			</tr>
	 	 </table><br/>

		</form>
		</div><br/>
	
			<table id="OriginalRecord" iconCls="icon-tip" width="1005px" height="250px"></table>
            <br/>
             <div id="subcontract" class="easyui-panel" style="width:1005px;padding:10px;" title="ת��ҵ����Ϣ" closed="true">
                <table id="subcontract_info_table" iconCls="icon-tip"></table>
                <br />
                <table id="uploaded_file_table" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104" iconCls="icon-tip"></table>
             </div>
            <br/>
              <div id="withdraw" class="easyui-panel" style="width:1005px;padding:10px;" title="������Ϣ" closed="true">
                <table id="withdraw_info_table" iconCls="icon-tip"></table>
             </div>
            <br/>             
              <div id="overdue" class="easyui-panel" style="width:1005px;padding:10px;" title="������Ϣ" closed="true">
                <table id="overdue_info_table" iconCls="icon-tip"></table>
             </div>
            <br/>             
              <div id="discount" class="easyui-panel" style="width:1005px;padding:10px;" title="�ۿ���Ϣ" closed="true">
                <table id="discount_info_table" iconCls="icon-tip"></table>
             </div>
            <table width="1005px" id="table1">
				<tr height="50px">
					<td width="25%" align="right"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="back()">����</a></td>
				</tr >
		</table>
		  <form id="downloadfile-submit-form" method="post" target="_blank" action="/jlyw/FileDownloadServlet.do?method=3">
			<input type="hidden" name="TemplateFileId" id="downloadfile-submit-form-TemplateFileId" value="" />
			<input type="hidden" name="OriginalRecordId" id="downloadfile-submit-form-OriginalRecordId" value="" />
			<input type="hidden" name="DownloadType" id="downloadfile-submit-form-DownloadType" value="0" />
		  </form>

</DIV>
</DIV>
</body>
</html>
