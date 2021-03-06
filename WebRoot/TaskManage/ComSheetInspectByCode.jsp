<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,com.jlyw.hibernate.*,com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
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
	<script type="text/javascript" src="ComSheetInspectByCode.js"></script>
	<script>
	$(function(){		
		$("#AddOriginalForm-TaskId").combobox({
			valueField:'id',
			textField:'text',
			required:true,
			editable:false,
			onLoadSuccess:function(){
				var rows = $(this).combobox('getData');
				if(rows != null && rows.length == 1){
					$(this).combobox('select',rows[0].id);
				}else{
					$(this).combobox('clear');
				}
			}
		});
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
	<form id="DetailForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="DetailForm_Code" type="hidden" name="Code"/>
        </form>
	<div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
						<input id="LOGIN_USERLG" type="hidden" value="<%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getId() %>" />
					</td>

					<td width="10%" align="right">密  码：</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet(true)">查询</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
	
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
			<input type="hidden" name="VerifierName" id="makecertificate-submit-form-VerifierName" value="" />
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
	
	<div id="select_verifyAndAuthorize_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="选择原始记录、证书的核验人和批准人" iconCls="icon-save" style="width:550px;height:200px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="VerifyAndAuthorizeForm" method="post">
					<input type="hidden" name="OriginalRecordId" id="VerifyAndAuthorizeForm-OriginalRecordId"  />
					<input type="hidden" name="Version" id="VerifyAndAuthorizeForm-Version"  />
					<br/>核验人：<select name="Verifier" id="VerifyAndAuthorizeForm-Verifier" style="width:152px" valueField="id" textField="name" required="true" class="easyui-combobox"></select>&nbsp;&nbsp;
					批准人：<select name="Authorizer" id="VerifyAndAuthorizeForm-Authorizer" style="width:152px"></select>
				</form>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doSelecteVerifyAndAuthorize()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseVerifyAndAuthorizeWindow()">取消</a>
			</div>
		</div>
	</div>
	
	<div id="fee_assign_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="产值分配" iconCls="icon-save" style="width:550px;height:450px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="fee_info_temp_form">
				<table width="100%" height="50px" border="0" style="margin-bottom:10px">
					<tr>
						<td width="16%">证书编号:</td>
						<td width="20%"><input name="CertificateCode" style="width:100px" readonly="readonly" value="" /></td>
						<td width="16%">检校人:</td>
						<td width="16%"><input name="Staff" style="width:100px" readonly="readonly" value="" /></td>
						<td width="15%">&nbsp;</td>
						<td width="17%">&nbsp;</td>
					</tr>
					<tr>
					  <td>检定费:</td>
					  <td><input name="TestFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>修理费:</td>
					  <td><input name="RepairFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>材料费:</td>
					  <td><input name="MaterialFee" style="width:100px" readonly="readonly" value="" /></td>
				  </tr>
				  <tr>
					  <td>交通费:</td>
					  <td><input name="CarFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>调试费:</td>
					  <td><input name="DebugFee" style="width:100px" readonly="readonly" value="" /></td>
					  <td>其它费:</td>
					  <td><input name="OtherFee" style="width:100px" readonly="readonly" value="" /></td>
				  </tr>
				</table>
				</form>
				<form id="FeeAssignForm" method="post">
					<input type="hidden" name="OriginalRecordId" id="FeeAssignForm-OriginalRecordId"  />
					<input type="hidden" name="CertificateId" id="FeeAssignForm-CertificateId" />
					<input type="hidden" name="FeeAssignInfo" id="FeeAssignForm-FeeAssignInfo" />
				</form>
				<select name="FeeAllotee" id="FeeAssignForm-FeeAllotee" style="width:152px"></select> &nbsp;&nbsp;
				分配额度:<input type="text" id="FeeAssignForm-FeeAssignLimit" class="easyui-numberbox" style="width:80px" precision="2" min="0.00" max="1.00" value="0.00" />&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="doAddAnAllotee()" >添加人员</a>
				<br/>
				<table id="fee_assign_table" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFeeAssign()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeFeeAssignWindow()">取消</a>
			</div>
		</div>
	</div>
	
	<div id="fee_assign_by_sheet_window" class="easyui-window" closed="true" modal="true" title="委托单产值分配：分配该委托单中由我检验的、尚未分配过的证书产值" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:610px;height:420px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<form id="FeeAssignFormBySheet" method="post">
					<input type="hidden" name="CommissionSheetId" id="FeeAssignFormBySheet-CommissionSheetId" value=""  />
					<input type="hidden" name="FeeAssignInfo" id="FeeAssignFormBySheet-FeeAssignInfo" />
				</form>
				<select name="FeeAllotee" id="FeeAssignFormBySheet-FeeAllotee" style="width:152px"></select> &nbsp;&nbsp;
				分配额度:<input type="text" id="FeeAssignFormBySheet-FeeAssignLimit" class="easyui-numberbox" style="width:80px" precision="2" min="0.00" max="1.00" value="0.00" />&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="doAddAnAlloteeBySheet()" >添加人员</a>
				<br/>
				<table id="fee_assign_table_by_sheet" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFeeAssignBySheet()" >确定</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closeFeeAssignWindowBySheet()">取消</a>
			</div>
		</div>
	</div>

<div id="add_original_record_window" class="easyui-window" collapsible="false" minimizable="false" maximizable="false" closed="true" modal="true" title="编辑原始记录" iconCls="icon-save" style="width:1000px;height:670px;overflow:hidden;padding-left:5px;background:#fff;border:1px solid #ccc;">
	<form id="AddOriginalForm" method="post">
		<input type="hidden" name="OriginalRecordId" id="AddOriginalForm-OriginalRecordId" value="" />
		<input type="hidden" name="WorkStaffId" id="AddOriginalForm-WorkStaffId" value="" /><!--用于添加成功后暂存ID，每次打开该对话框时清除该ID-->
		<input type="hidden" id="TheCommissionId" name="CommissionId" value="" />
		
		<input type="hidden" name="Model" value="" />
		<input type="hidden" name="Range" value="" />
		<input type="hidden" name="Accuracy" value="" />
		<input type="hidden" name="TargetApplianceId" value="" />
		
		<input type="hidden" name="StandardAppliancesString" id="AddOriginalForm-StandardAppliancesString" value="" />
		<input type="hidden" name="StandardsString" id="AddOriginalForm-StandardsString" value="" />
		<input type="hidden" name="SpecificationsString" id="AddOriginalForm-SpecificationsString" value="" />
		
		<table width="970px" border="0" height="320px">
			<tr>
				<td width="770px" valign="top">
					<table id="Appliance" iconCls="icon-tip" width="600px" height="300px"></table>
				</td>
				<td width="200px" valign="top">
					<table id="template_file_grid_new" iconCls="icon-tip"></table>
				</td>
			</tr>
		</table>
		<table width="970px" border="0">
			<tr>
				<td width="100" align="right">常用名称：</td>
				<td width="192" align="left">
						<select name="ApplianceName" id="AddOriginalForm-ApplianceName" style="width:152px" class="easyui-combobox" valueField="Name" textField='Name'></select></td>
				<td width="100" align="right">工作性质：</td>
				<td width="192" align="left">
					<select name="WorkType" id="AddOriginalForm-WorkType" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="检定">检定</option>
						<option value="校准">校准</option>
						<option value="检测">检测</option>
						<option value="检验">检验</option>
					</select>					</td>
				<td width="90" align="right">工作地点：</td>
				<td width="192" align="left">
					<select name="WorkLocation" id="AddOriginalForm-WorkLocation" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="本实验室">本实验室</option>
						<option value="被测仪器使用现场">被测仪器使用现场</option>
					</select>				    </td>
			</tr>
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
					<input type="text" style="width:152px" name="Temp" class="easyui-numberbox" precision="1" />℃				    </td>
				<td align="right">相对湿度：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Humidity"  class="easyui-numberbox" precision="1" />%					</td> 
				<td align="right">大 气 压：</td>
				<td align="left">
					<input type="text" style="width:152px" name="Pressure" value="/" />kPa				    </td>
			</tr>
			<tr>
				<td align="right">其 &nbsp;&nbsp; 它：</td>
				<td align="left">
					<input type="text" style="width:152px" name="OtherCond" value="" /></td>
				<td align="right">标准(器具)<br/>状态检查：</td>
				<td align="left">
					<select style="width:152px" name="StdOrStdAppUsageStatus" id="AddOriginalForm-StdOrStdAppUsageStatus" class="easyui-combobox" panelHeight="auto" >
						<option value="正常" selected="selected">正常</option>
						<option value="异常">异常</option>
					</select>
					</td> 
				<td align="right">异常情况<br/>说明：</td>
				<td align="left">
					<input type="text" style="width:152px" name="AbnormalDesc" value="/" /></td>
			</tr>
			<tr>
				<td align="right">结 &nbsp;&nbsp; 论：</td>
				<td align="left">
					<select style="width:152px" name="Conclusion" id="AddOriginalForm-Conclusion" class="easyui-combobox" panelHeight="auto" required="true" >
						<option value="合格" selected="selected">合格</option>
						<option value="不合格">不合格</option>
						
					</select>				    </td>
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
					<select type="text" style="width:152px" name="FirstIsUnqualified" id="AddOriginalForm-FirstIsUnqualified"  required="true"> 
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
					<select name="Mandatory" id="AddOriginalForm-Mandatory" style="width:152px" class="easyui-validatebox" required="true">
						<option value="" selected="selected">请选择...</option>
						<option value="否">否</option>
						<option value="是">是</option>
					</select>				    </td>
				<td align="right">强管唯一性号：</td>
				<td align="left"><input type="text" style="width:152px" name="MandatoryCode"  /></td>
				<td align="right">强管类型：</td>
				<td align="left">
					<select name="MandatoryPurpose" id="AddOriginalForm-MandatoryPurpose" style="width:152px" class="easyui-validatebox">
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
				<td align="right" >检定员：</td>
				<td align="left" ><select name="TaskId" id="AddOriginalForm-TaskId" style="width:152px"></select></td>
				<td align="left" colspan="4"><a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doOpenHistoryCertificateWindow()" >从历史证书导入</a><a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, true)" >编辑原始记录</a><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="selectTechnicalDocsAndStandards()" >选择规程标准</a><a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, false)" >直接编制证书</a><!--<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#add_original_record_window').window('close')">取消</a>--></td>
				
			</tr>
			<!--<tr>
				<td align="right">&nbsp;</td>
				
				<td align="right" colspan="5" style="padding-right:10px"><a class="easyui-linkbutton" iconCls="icon-download" href="javascript:void(0)" onclick="doOpenHistoryCertificateWindow()" >从历史证书导入</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, true)" >编辑原始记录</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="doAddOriginalRecord(false, false)" >直接编制证书</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#add_original_record_window').window('close')">取消</a></td>
			</tr>-->
		</table>		
	</form>
</div>

<div id="function-toolbar" style="padding:2px 0">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="text-align:left;padding-left:2px">
			<label>标准名:</label><select id="function-toolbar-StandardName" style="width:60px" ></select><label>型号:</label><input id="function-toolbar-Model" class="easyui-combobox" valueField="name" textField='name' style="width:60px" /><label>范围:</label><input id="function-toolbar-Range" class="easyui-combobox" valueField="name" textField='name' style="width:60px" /><label>等级:</label><input id="function-toolbar-Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:60px" /><label>检定费:</label>
        <input name="text"  id="function-toolbar-TestFee" class="easyui-numberbox" style="width:60px" />
		 <label>受检器具:</label>
          <select name="select" id="function-toolbar-TargetAppName" style="width:60px">
          </select>
			<input id="standardNameIdstandardNameId" name="standardNameIdstandardNameId"  type="hidden" />
			<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询受检器具" id="btnHistorySearch" onclick="doSearchTargetAppliance()">查询</a>
			
			</td>
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
			<label>标准器具：</label><select id="StandardApplianceId" name="StandardApplianceId" class="easyui-combobox" url="/jlyw/StandardApplianceServlet.do?method=5" valueField="id" textField="name_num" style="width:252px" panelHeight="150px"/>			
			</td>
		</tr>
	</table>
</div>

<div id="selectTechnicalDocsAndStandards_window" class="easyui-window" closed="true" modal="true" title="选择检定规程、计量标准和标准器具" collapsible="false" minimizable="false" maximizable="false" iconCls="icon-save" style="width:650px;height:650px;padding:5px;position:relative">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="background:#fff;border:1px solid #ccc;">				
			<table id="table_Specification" fit="true" iconCls="icon-tip"></table>
			<table id="table_Standard" fit="true" iconCls="icon-tip"></table>
			<table id="table_StandardAppliance" fit="true" iconCls="icon-tip"></table>
		</div>
		<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="submitDocsAndStandards()" >提交所选并编辑证书</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="$('#selectTechnicalDocsAndStandards_window').window('close');">取消</a>
		</div>
	</div>
</div>

</body>
</html>
