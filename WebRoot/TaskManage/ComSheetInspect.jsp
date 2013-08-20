<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<!-- <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
 <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
 <META HTTP-EQUIV="Expires" CONTENT="0">-->
<title>委托单检验</title>
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
		doLoadCommissionSheet(false);	//加载委托单
		$('#loading').hide();	//等待框隐藏
	});
	</script>

</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		服务器处理中，请稍后...
		<br />
		<span id="loading-msg">^_^</span>
	</div>
</div>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单检验" />
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
	
	<div id="select_xls_template_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="选择原始记录模板" iconCls="icon-save" style="width:550px;height:550px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="template_file_grid" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteXlsTemplate()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectedXlsTemplateWindow()">取消</a>
			</div>
		</div>
	</div>
	
	<div id="select_doc_template_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="选择证书模板" iconCls="icon-save" style="width:550px;height:550px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="template_doc_file_grid" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteDocTemplate()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectedDocTemplateWindow()">取消</a>
			</div>
		</div>
	</div>
	
	
<div id="add_original_record_window" class="easyui-window" closed="true" collapsible="false" minimizable="false" maximizable="false" modal="true" title="编辑原始记录" iconCls="icon-save" style="width:830px;height:660px;overflow:hidden;padding-left:5px;background:#fff;border:1px solid #ccc;">
	<form id="AddOriginalForm" method="post">
		<input type="hidden" name="OriginalRecordId" id="AddOriginalForm-OriginalRecordId" value="" />
		<input type="hidden" name="WorkStaffId" id="AddOriginalForm-WorkStaffId" value="" /><!--用于添加成功后暂存ID，每次打开该对话框时清除该ID-->
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
				<td width="74" align="right">常用名称：</td>
				<td width="185" align="left">
						<select name="ApplianceName" id="AddOriginalForm-ApplianceName" style="width:152px" class="easyui-combobox" valueField="Name" textField='Name'></select></td>
				<td width="69" align="right">工作性质：</td>
				<td width="163" align="left">
					<select name="WorkType" id="AddOriginalForm-WorkType" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="检定">检定</option>
						<option value="校准">校准</option>
						<option value="检测">检测</option>
						<option value="检验">检验</option>
					</select>					</td>
				<td width="65" align="right">工作地点：</td>
				<td width="168" align="left">
					<select name="WorkLocation" id="AddOriginalForm-WorkLocation" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="本所实验室">本所实验室</option>
						<option value="被测仪器使用现场">被测仪器使用现场</option>
					</select>				    </td>
			</tr>
			<tr>
				<td align="right">制造单位：</td>
				<td align="left">
						<select name="Manufacturer" id="AddOriginalForm-Manufacturer" style="width:152px"  class="easyui-combobox" valueField="name" textField='name'></select>				    </td>
				<td align="right">出厂编号：</td>
				<td align="left">
					<input type="text" style="width:152px" name="ApplianceCode" />					</td>
				<td align="right">管理编号：</td>
				<td align="left">
					<input type="text" style="width:152px" name="ApplianceManageCode" />				    </td>
			</tr>
			<tr>
				<td align="right">环境温度：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Temp" class="easyui-validatebox"/>℃				    </td>
				<td align="right">相对湿度：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Humidity"  class="easyui-validatebox"/>%					</td> 
				<td align="right">大 气 压：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Pressure" value="/" />kPa				    </td>
			</tr>
			<tr>
				<td align="right">其 &nbsp;&nbsp; 它：</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherCond" value="" /></td>
				<td align="right">标准(器具)<br/>
				  状态检查：</td>
				<td align="left">
					<select style="width:152px" name="StdOrStdAppUsageStatus" id="AddOriginalForm-StdOrStdAppUsageStatus"  class="easyui-combobox" panelHeight="auto" >
						<option value="正常" selected="selected">正常</option>
						<option value="异常">异常</option>
					</select>			  </td> 
				<td align="right">异常情况<br/>说明：</td>
				<td align="left">
					<input type="text" style="width:152px" name="AbnormalDesc" value="/" /></td>
			</tr>
			<tr>
				<td align="right">结 &nbsp;&nbsp; 论：</td>
				<td align="left">
					<select style="width:152px" name="Conclusion" id="AddOriginalForm-Conclusion"  class="easyui-combobox" panelHeight="auto" required="true" >
						<option value="合格" selected="selected">合格</option>
						
						<option value="不合格">不合格</option>
						
					</select></td>
				<td align="right">检校日期：</td>
				<td align="left">
					<input type="text" style="width:152px" name="WorkDate" id="AddOriginalForm-WorkDate" class="easyui-datebox" required="true" />					</td>
				<td align="right">核 验 员：</td>
				<td align="left">
					<select style="width:152px" name="VerifyUser" id="AddOriginalForm-VerifyUser" valueField="name" textField="name" class="easyui-combobox" editable="false" ></select>				    </td>
			</tr>
			<tr>
				<td align="right">器具数量：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Quantity" class="easyui-numberbox" min="1" value="1" required="true" />件				    </td>
				<td align="right">首检是否合格：</td>
				<td align="left">
					<select type="text" style="width:152px" name="FirstIsUnqualified" id="AddOriginalForm-FirstIsUnqualified" > 
						<option value="合格" selected="selected">合格</option>
						<option value="不合格">不合格</option>					
					 </select>
				</td>			
				<td align="right">首检不合格原因：</td>
				<td align="left"><input type="text" style="width:152px" id="AddOriginalForm-UnqualifiedReason" name="UnqualifiedReason"  value="" /></td>
			</tr>
			<tr>
				<td align="right">备注：</td>
				<td align="left"><input type="text" style="width:152px" name="Remark" /></td>
				<td align="right">修理级别：</td>
				<td align="left">
					<select name="RepairLevel" id="AddOriginalForm-RepairLevel" onchange="setAddOriginalFormRepairFee()" style="width:152px">
						<option value="" selected="selected">请选择...</option>
						<option value="小">小</option>
						<option value="中">中</option>
						<option value="大">大</option>
					</select>					</td>
				<td align="right">修 理 费：</td>
				<td align="left"><input type="text" style="width:152px; background-color:#CCCCCC" name="RepairFee" readonly="readonly" /></td>
			</tr>
			<tr>
				<td align="right">交 通 费：</td>
				<td align="left">
					<input type="text" style="width:152px" name="CarFee" class="easyui-numberbox" min="0" />元				    </td>
				<td align="right">调 试 费：</td>
				<td align="left">
					<input type="text" style="width:152px" name="DebugFee" class="easyui-numberbox" min="0" />元					</td>
				<td align="right">检 定 费：</td>
				<td align="left"><input type="text" style="width:152px;background-color:#CCCCCC" name="TestFee" readonly="readonly" /></td>
			</tr>
			<tr>
				<td align="right">其 他 费：</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherFee" class="easyui-numberbox" min="0" />元				    </td>
				<td align="right">配 件 费：</td>
				<td align="left"><input type="text" style="width:152px" name="MaterialFee" class="easyui-numberbox" min="0" />元</td>
				<td align="right">配件明细：</td>
				<td align="left"><input type="text" style="width:152px" name="MaterialDetail" /></td>
			</tr>
			<tr>
				<td align="right">是否强管：</td>
				<td align="left">
					<select name="Mandatory" id="AddOriginalForm-Mandatory" style="width:154px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="否">否</option>
						<option value="是">是</option>
					</select>				    </td>
				<td align="right">强管唯一性号：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryCode"  /></td>
				<td align="right">强管类型：</td>
				<td align="left">
					<select name="MandatoryPurpose" id="AddOriginalForm-MandatoryPurpose" style="width:154px" class="easyui-validatebox">
						<option value="" selected="selected">请选择...</option>
						<option value="贸易结算">贸易结算</option>
						<option value="医疗卫生">医疗卫生</option>
						<option value="安全防护">安全防护</option>
						<option value="环境监测">环境监测</option>
						<option value="社会公用计量标准器具">社会公用计量标准器具</option>
						<option value="部门、企事业单位最高计量标准器具">部门、企事业单位最高计量标准器具</option>
					</select></td>
			</tr>
			<tr>
				<td align="right">强检目录对应项别：</td>
				<td align="left">
					<input type="text" style="width:152px" name="MandatoryItemType" value="" /></td>
				<td align="right">强检目录对应种别：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryType" value="" /></td>
				<td align="right">使用/安装地点：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryApplyPlace" /></td>
			</tr>
			
			<tr>
				
				<td align="right" colspan="6" style="padding-right:10px"><a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doOpenHistoryCertificateWindow()" >从历史证书导入</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, true)" >编辑原始记录</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="selectTechnicalDocsAndStandards()" >选择规程标准</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, false)" >直接编制证书</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#add_original_record_window').window('close')">取消</a></td>
			</tr>
		</table>		
	</form>
</div>

<div id="function-toolbar" style="padding:2px 0">
  <table cellpadding="0" cellspacing="0" style="width:100%">
    <tr>
      <td style="text-align:left;padding-left:2px"><label>标准名:</label>
          <select name="select" id="function-toolbar-StandardName" style="width:60px">
          </select>
        <label>型号:</label>
        <select name="text"  id="function-toolbar-Model" class="easyui-combobox" valueField="name" textField='name' style="width:60px" ></select>
        <label>范围:</label>
        <input name="text"  id="function-toolbar-Range" class="easyui-combobox" valueField="name" textField='name' style="width:60px" />
        <label>等级:</label>
        <input name="text"  id="function-toolbar-Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:60px" />
		<input id="standardNameIdstandardNameId" name="standardNameIdstandardNameId"  type="hidden" />
        <a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-search" plain="true" title="查询受检器具" id="btnHistorySearch" onclick="doSearchTargetAppliance()">查询</a> </td>
    </tr>
  </table>
</div>

<div id="file_upload_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="脱机文件上传" iconCls="icon-save" style="width:500px;height:200px;padding:5px;">
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
		  <td height="39" align="left"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadExcel(0)">脱机文件暂存</a> &nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadExcel(1)">脱机文件提交</a>&nbsp; &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#file_upload').uploadifyClearQueue();$('#file_upload_window').window('close');">关闭对话框</a></td>
	  </tr>
 </table>
</div>

<div id="load_from_history_certificate" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="从历史证书中导入" iconCls="icon-save" style="width:650px;height:550px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="background:#fff;border:1px solid #ccc;">
			<table id="history_certificate_grid" width="620px" height="450px" iconCls="icon-tip"></table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doLoadHistoryCertificate()" >确定</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#load_from_history_certificate').window('close');">取消</a>
		</div>
	</div>
</div>

<div id="load_from_history_certificate-toolbar" style="padding:2px 0">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="text-align:left;padding-left:2px">
			<label>型号规格:</label><input id="load_from_history_certificate-toolbar-Model" type="text" style="width:60px" /><label>测量范围:</label><input id="load_from_history_certificate-toolbar-Range" type="text" style="width:60px" /><label>准确度等级:</label><input id="load_from_history_certificate-toolbar-Accuracy" type="text" style="width:60px" /><label>检校人员:</label><select type="text" id="load_from_history_certificate-toolbar-WorkStaff" style="width:90px" ></select><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询历史证书" id="btnHistorySearch" onclick="doSearchHistoryCertificate()">查询</a>
			
			</td>
		</tr>
	</table>
</div>

<div id="table_Standard_toolbar" style="">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px;text-align:left">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteStdRecord()">删除所选计量标准</a>
			</td>
			<td style="text-align:right;padding-right:2px">
			
			<label>计量标准：</label><select id="StandardId" name="StandardId" class="easyui-combobox" url="/jlyw/StandardServlet.do?method=5" valueField="id" textField="name_num" style="width:250px"  panelHeight="150px"/>
			
			</td>
		</tr>
	</table>
</div>
<div id="table_Specification_toolbar" style="">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px;text-align:left">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteSpecificationRecord()">删除所选技术规范</a>
			</td>
			<td style="text-align:right;padding-right:2px">
			<label>技术规范：</label><select id="SpecificationId" name="SpecificationId" class="easyui-combobox" url="/jlyw/SpecificationServlet.do?method=5"  valueField="id" textField="name_num"  style="width:250px"  panelHeight="150px" />			
			</td>
		</tr>
	</table>
</div>
<div id="table_StandardAppliance_toolbar" style="">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px;text-align:left">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteStdAppRecord()">删除所选标准器具</a>
			</td>
			<td style="text-align:right;padding-right:2px">
			<label>标准器具：</label><select id="StandardApplianceId" name="StandardApplianceId" class="easyui-combobox" url="/jlyw/StandardApplianceServlet.do?method=5" valueField="id" textField="name_num" style="width:362px" panelHeight="150px"/>			
			</td>
		</tr>
	</table>
</div>

<div id="selectTechnicalDocsAndStandards_window" class="easyui-window" closed="true" modal="true" title="选择检定规程、计量标准和标准器具" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:650px;height:600px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="background:#fff;border:1px solid #ccc;">				
			<table id="table_Specification" fit="true" iconCls="icon-tip"></table>
			<table id="table_Standard" fit="true" iconCls="icon-tip"></table>
			<table id="table_StandardAppliance" fit="true" iconCls="icon-tip"></table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="submitDocsAndStandards()" >提交并编辑证书</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#selectTechnicalDocsAndStandards_window').window('close');">取消</a>
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
