<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ת����Ϣ����</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	
	<script>
$(function(){
		   $('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'104'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:rar/zip/jpg/jpeg/png/bmp/gif/pdf.', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
                'fileExt'   : '*.rar;*.zip;*.jpg;*.jpeg;*.gif;*.png;*.bmp;*.pdf;',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('��ʾ',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table').datagrid('reload');
					CloseWaitingDlg();
				}
			  });
		  
		$("#SubContractor").combobox({
			valueField:'name',
			textField:'name',
			required:true,
			onSelect:function(record){
			},
			onChange:function(newValue, oldValue){
				var allData = $(this).combobox('getData');
				if(allData != null && allData.length > 0){
					for(var i=0; i<allData.length; i++)
					{
						if(newValue==allData[i].name){
							return false;
						}
					}
				}
				$(this).combobox('reload','/jlyw/SubContractServlet.do?method=0&QueryName='+newValue);
			}
		});
 		$("#Handler").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
			},
			onChange:function(newValue, oldValue){
				var allData = $(this).combobox('getData');
				if(allData != null && allData.length > 0){
					for(var i=0; i<allData.length; i++)
					{
						if(newValue==allData[i].name){
							return false;
						}
					}
				}
				$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}
		});
		$("#Receiver").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
			},
			onChange:function(newValue, oldValue){
				var allData = $(this).combobox('getData');
				if(allData != null && allData.length > 0){
					for(var i=0; i<allData.length; i++)
					{
						if(newValue==allData[i].name){
							return false;
						}
					}
				}
				$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}
		});
		$('#subcontract_info_table').datagrid({
			title:'ת��ҵ����Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
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
			rownumbers:true	,
			onSelect:function(rowIndex, rowData){
				//���¸�������ļ���Ϣ
				$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.Attachment};
				$('#uploaded_file_table').datagrid('reload');
				//load ��
				$('#subcontract-submit-form').form('load', rowData);
				$("#SubContractor").combobox('setValue', rowData.SubContractorName);
			},
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$(this).datagrid('selectRow', 0);
				}
			}
		});
		doLoadCommissionSheet();
});
function doLoadCommissionSheet(){	//����ί�е�
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':''};
			$('#subcontract_info_table').datagrid('loadData',{total:0,rows:[]});
			
			//��ո����б�
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
			$('#uploaded_file_table').datagrid('reload');
			
			
			$("#CommissionSheetId").val('');				
			//$("#Ness").removeAttr("checked");	//ȥ��ѡ
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//��ѡ
				}
				
				//����ת��ҵ����Ϣ
				$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#subcontract_info_table').datagrid('reload');
				
				$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
				
			}else{
				$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}
function doSubmitNewSubContract(){   	//�ύ¼��ת����¼����
	$("#subcontract-submit-form").form('submit', {
		url:'/jlyw/SubContractServlet.do?method=1',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			var projectValue = $('#CommissionSheetId').val();
			if(projectValue==''){
				$.messager.alert('��ʾ��',"��ѡ����Ҫת����ί�е���",'info');
				return false;
			}
			return $("#subcontract-submit-form").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//���¼������������Ϣ
				$('#subcontract_info_table').datagrid('reload');
				
				//���ת����
				$('#SubContractor').combobox('clear');
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}  
function doSubmitEditSubContract(){   	//�ύ�޸�ת����¼����
	$("#subcontract-submit-form").form('submit', {
		url:'/jlyw/SubContractServlet.do?method=2',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			var row = $("#subcontract_info_table").datagrid('getSelected');
			if(row){
				/*var result = confirm("��ȷ��Ҫע����������");
				if(result == false){
					return false;
				}*/
				$('#SubContractId').val(row.SubContractId);
			}else{
				$.messager.alert('��ʾ��',"����ѡ��һ��ת����¼",'info');
				return false;
			}
			return $("#subcontract-submit-form").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//���¼������������Ϣ
				$('#subcontract_info_table').datagrid('reload');
				
				//���ת����
				$('#SubContractor').combobox('clear');
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}  
//function uploadifyUpload(){ 
// $('#file_upload').uploadifyUpload(); 
// var str="���ڴ����ϴ����ļ�";
// 
// //waiting(str) ;
// Show();
//} 

function ok(){
	 $('#allot').form('submit',{
		//url: 'userAdd.action',
		onSubmit:function(){ return $('#allot').form('validate');},
		success:function(){
			 close1();
		 }
	});
}
	   // document.getElementById('waiting').style.visibility='hidden'; 
    
	  // function wait() { 
//	    //document.all.ly.style.display="block";
//
//		document.getElementById('waiting').style.visibility='visible'; 
//	   } 
//	    function complete() { 
//		document.getElementById('waiting').style.visibility='hidden'; 
//	   } <br />
//        var c = $(window.parent.document.body) 
//       function wait() { 
//          parent.document.getElementById('waiting2').style.visibility='visible'; 
//	   } 
//	    function complete() { 
//		//$(window.parent.document.body).getElementById('waiting2').style.visibility='hidden'; 
//		parent.document.getElementById('waiting2').style.visibility='hidden'; 
//	   } 
	</script>
</head>

<body>
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ת����Ϣ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <div >
          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Pwd") %>" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
					
				</tr >
		</table>
		</form>

	 </div>
	 <br/>	
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
				title="ί�е���Ϣ" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<table width="1000px" id="table1">
				<tr>

				  <td width="77" align="right">ί����ʽ��</td>
				  <td width="185"  align="left"><select name="CommissionType" style="width:152px">
                    		<option value="1">�������</option>
							<option value="5">����ҵ��</option>
							<option value="6">�Լ�ҵ��</option>
							<option value="7">�ֳ�����</option>
							<option value="3">��������</option>
							<option value="4">��ʽ����</option>
							<option value="2">�ֳ����</option>
                  </select></td>
				  <td width="68" align="right">ί�����ڣ�</td>
				  <td width="650"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>

			  <td width="77"  align="right">ί�е�λ��</td>
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" style="width:152px;" /></td>
                <td width="64" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text" /></td>
				<td width="64" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ַ��</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" /></td>

				<td width="65" align="right">�������룺</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" /></td>
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
				<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>

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
		</div>
		<br/>
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ת��ҵ����Ϣ" collapsible="false"  closable="false">
			<table id="subcontract_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="subcontract-submit-form" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
			<input type="hidden" name="SubContractId" id="SubContractId" value="" />
			<table width="950">
				<tr>
				    <td width="60" align="right" >ת������</td>
					<td width="150" align="left"><select name="SubContractor" id="SubContractor" style="width:152px"></select></td>
					<td width="60" align="right" >ת��ʱ�䣺</td>
					<td width="168"  align="left"><input id="SubContractDate"   name="SubContractDate" type="text" class="easyui-datebox" style="width:152px;" /></td>
					<td width="80" align="right" >ת���ˣ�</td>
					<td width="150" align="left"><select name="Handler" id="Handler" style="width:152px"></select></td>
					<td width="60" align="right" >ת�����ã�</td>
					<td width="150"  align="left"><input id="TotalFee" name="TotalFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ</td>
				</tr>
				<tr>
					<td align="right" >����ʱ�䣺</td>
					<td align="left"><input id="ReceiveDate"   name="ReceiveDate" type="text" class="easyui-datebox" style="width:152px;" /></td>
					<td align="right" >�����ˣ�</td>
					<td align="left"><select name="Receiver" id="Receiver" style="width:152px"></select></td>
					<td align="right" >��ע��Ϣ��</td>
					<td colspan="3" rowspan="2" align="left"><textarea id="Remark" style="width:350px;height:80px"  name="Remark" class="easyui-validatebox" ></textarea></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="2" align="right"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitNewSubContract()">����ת����¼</a>&nbsp;&nbsp;</td>
					<td colspan="2" align="left">&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onClick="doSubmitEditSubContract()">�޸�ת����¼</a></td>
					<td colspan="4" align="left">  </td>
				</tr>
		  </table>
		  </form>
		</div>
		<br/>
		<div id="p4" class="easyui-panel" style="width:1005px;height:200px;"
				title="�ļ��ϴ�" collapsible="false"  closable="false" scroll="no">
			<table width="100%" height="100%" >
				<tr>
					<td width="57%" rowspan="2">
						<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104"></table>
					</td>
				 	<td width="43%" height="125" valign="top" align="left" style="overflow:hidden">
						<div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
					</td>
				</tr>
				<tr>
				  	<td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:var rowSelected=$('#subcontract_info_table').datagrid('getSelected');if(rowSelected==null || rowSelected.Attachment!=$('#uploaded_file_table').datagrid('options').queryParams.FilesetName){$.messager.alert('��ʾ', '����ѡ�񸽼�������ת����¼��','info'); return false;} doUploadByDefault()">�ϴ��ļ�</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">ȡ���ϴ�</a> </td>
				</tr>
			</table>
	   </div>	 
	</div>
	 

</DIV></DIV>
</body>
</html>
