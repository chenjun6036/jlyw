<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<!-- <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
 <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
 <META HTTP-EQUIV="Expires" CONTENT="0">-->
<title>ί�е�����</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<link rel="stylesheet" type="text/css" href="../css/loading.css" />
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
	<script type="text/javascript" src="ComSheetInspect.js"></script>

	<script>
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
	 
	  <form id="makecertificate-submit-form" method="post" target="DoEditDocWebOffice" action="/jlyw/WebOffice/docEdit.jsp">
		<input type="hidden" name="XlsTemplateFileId" id="makecertificate-submit-form-XlsTemplateFileId" value="" />
		<input type="hidden" name="OriginalRecordId" id="makecertificate-submit-form-OriginalRecordId" value="" />
		<input type="hidden" name="FileName" id="makecertificate-submit-form-FileName" value="" />
		<input type="hidden" name="Version" id="makecertificate-submit-form-Version" value="" />
		<input type="hidden" name="StaffId" id="makecertificate-submit-form-StaffId" value="" />
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
	
	
<div id="add_original_record_window" class="easyui-window" closed="true" collapsible="false" minimizable="false" maximizable="false" modal="true" title="�༭ԭʼ��¼" iconCls="icon-save" style="width:830px;height:660px;overflow:hidden;padding-left:5px;background:#fff;border:1px solid #ccc;">
	<form id="AddOriginalForm" method="post">
		<input type="hidden" name="OriginalRecordId" id="AddOriginalForm-OriginalRecordId" value="" />
		<input type="hidden" name="WorkStaffId" id="AddOriginalForm-WorkStaffId" value="" /><!--������ӳɹ����ݴ�ID��ÿ�δ򿪸öԻ���ʱ�����ID-->
		<input type="hidden" id="TheCommissionId" name="CommissionId" value="" />
		<input type="hidden" name="TaskId" value="<%= request.getParameter("TaskId")==null?"":request.getParameter("TaskId") %>" />
		<input type="hidden" name="Model" value="" />
		<input type="hidden" name="Range" value="" />
		<input type="hidden" name="Accuracy" value="" />
		<input type="hidden" name="TargetApplianceId" value="" />
		
		<input type="hidden" name="StandardAppliancesString" id="AddOriginalForm-StandardAppliancesString" value="" />
		<input type="hidden" name="StandardsString" id="AddOriginalForm-StandardsString" value="" />
		<input type="hidden" name="SpecificationsString" id="AddOriginalForm-SpecificationsString" value="" />
		
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
					<input type="text" style="width:152px" name="Temp" class="easyui-validatebox"/>��				    </td>
				<td align="right">���ʪ�ȣ�</td>
				<td align="left">
					<input type="text" style="width:152px" name="Humidity"  class="easyui-validatebox"/>%					</td> 
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
				<td align="right">�׼��Ƿ�ϸ�</td>
				<td align="left">
					<select type="text" style="width:152px" name="FirstIsUnqualified" id="AddOriginalForm-FirstIsUnqualified" > 
						<option value="�ϸ�" selected="selected">�ϸ�</option>
						<option value="���ϸ�">���ϸ�</option>					
					 </select>
				</td>			
				<td align="right">�׼첻�ϸ�ԭ��</td>
				<td align="left"><input type="text" style="width:152px" id="AddOriginalForm-UnqualifiedReason" name="UnqualifiedReason"  value="" /></td>
			</tr>
			<tr>
				<td align="right">��ע��</td>
				<td align="left"><input type="text" style="width:152px" name="Remark" /></td>
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
					<select name="Mandatory" id="AddOriginalForm-Mandatory" style="width:154px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="��">��</option>
						<option value="��">��</option>
					</select>				    </td>
				<td align="right">ǿ��Ψһ�Ժţ�</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryCode"  /></td>
				<td align="right">ǿ�����ͣ�</td>
				<td align="left">
					<select name="MandatoryPurpose" id="AddOriginalForm-MandatoryPurpose" style="width:154px" class="easyui-validatebox">
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
				
				<td align="right" colspan="6" style="padding-right:10px"><a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doOpenHistoryCertificateWindow()" >����ʷ֤�鵼��</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, true)" >�༭ԭʼ��¼</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="selectTechnicalDocsAndStandards()" >ѡ���̱�׼</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, false)" >ֱ�ӱ���֤��</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#add_original_record_window').window('close')">ȡ��</a></td>
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

<div id="load_from_history_certificate-toolbar" style="padding:2px 0">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="text-align:left;padding-left:2px">
			<label>�ͺŹ��:</label><input id="load_from_history_certificate-toolbar-Model" type="text" style="width:60px" /><label>������Χ:</label><input id="load_from_history_certificate-toolbar-Range" type="text" style="width:60px" /><label>׼ȷ�ȵȼ�:</label><input id="load_from_history_certificate-toolbar-Accuracy" type="text" style="width:60px" /><label>��У��Ա:</label><select type="text" id="load_from_history_certificate-toolbar-WorkStaff" style="width:90px" ></select><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��ʷ֤��" id="btnHistorySearch" onclick="doSearchHistoryCertificate()">��ѯ</a>
			
			</td>
		</tr>
	</table>
</div>

<div id="table_Standard_toolbar" style="">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px;text-align:left">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteStdRecord()">ɾ����ѡ������׼</a>
			</td>
			<td style="text-align:right;padding-right:2px">
			
			<label>������׼��</label><select id="StandardId" name="StandardId" class="easyui-combobox" url="/jlyw/StandardServlet.do?method=5" valueField="id" textField="name_num" style="width:250px"  panelHeight="150px"/>
			
			</td>
		</tr>
	</table>
</div>
<div id="table_Specification_toolbar" style="">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px;text-align:left">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteSpecificationRecord()">ɾ����ѡ�����淶</a>
			</td>
			<td style="text-align:right;padding-right:2px">
			<label>�����淶��</label><select id="SpecificationId" name="SpecificationId" class="easyui-combobox" url="/jlyw/SpecificationServlet.do?method=5"  valueField="id" textField="name_num"  style="width:250px"  panelHeight="150px" />			
			</td>
		</tr>
	</table>
</div>
<div id="table_StandardAppliance_toolbar" style="">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px;text-align:left">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteStdAppRecord()">ɾ����ѡ��׼����</a>
			</td>
			<td style="text-align:right;padding-right:2px">
			<label>��׼���ߣ�</label><select id="StandardApplianceId" name="StandardApplianceId" class="easyui-combobox" url="/jlyw/StandardApplianceServlet.do?method=5" valueField="id" textField="name_num" style="width:362px" panelHeight="150px"/>			
			</td>
		</tr>
	</table>
</div>

<div id="selectTechnicalDocsAndStandards_window" class="easyui-window" closed="true" modal="true" title="ѡ��춨��̡�������׼�ͱ�׼����" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:650px;height:600px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="background:#fff;border:1px solid #ccc;">				
			<table id="table_Specification" fit="true" iconCls="icon-tip"></table>
			<table id="table_Standard" fit="true" iconCls="icon-tip"></table>
			<table id="table_StandardAppliance" fit="true" iconCls="icon-tip"></table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="submitDocsAndStandards()" >�ύ���༭֤��</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#selectTechnicalDocsAndStandards_window').window('close');">ȡ��</a>
		</div>
	</div>
</div>
		<form method="post" action="/jlyw/TaskManage/CheckAndFee.jsp" id="CheckAndFeeForm">
		  <input type="hidden" name="Code" id="Code1" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" />
		  <input type="hidden" name="Pwd" id="Pwd1" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" />
		  <input type="hidden" name="TaskId" id="TaskId1" value="<%= request.getParameter("TaskId")==null?"":request.getParameter("TaskId") %>" />
		</form>
</body>
</html>
