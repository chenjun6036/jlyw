<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�깤ȷ��ί�е��б�</title>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
<link href="../../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />

<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript"
	src="../../Inc/JScript/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="../../JScript/json2.js"></script>
<script type="text/javascript" src="../../JScript/NumberChanger.js"></script>
<script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
<script type="text/javascript" src="./CXCommissionSheet.js"></script>
<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object> 
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�깤ȷ��ί�е��б�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV"> 
	 <div id="p" class="easyui-panel" style="width:1000px;height:150px;padding:10px;"
				title="ѡ�����" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr>
					<td  align="right" >ί�е���ţ�</td>
					<td  align="left" >
				  <input id="SearchForm-Code" class="easyui-validatebox" name="Code" style="width:152px;" value="" />				  </td>

					<td  align="right">ί�е�λ��</td>
					<td  align="left">
				  <select name="CustomerName" id="CustomerName" style="width:152px"></select>				  </td>
					
					<td   align="right">ί�е�״̬��</td>
					
				    <td   align="left"><select name="CommissionStatus" id="CommissionStatus" style="width:100px">
							<option value="" >ȫ��</option>
							<option value="1" selected="selected">δ�깤</option>
							<option value="2">���깤</option>
							<option value="4">�ѽ���</option>
							<option value="9">�ѽ���</option>

					</select>	</td>
				    <td   align="center"></td>
				    <td  align="center"></td>
				</tr>
				<tr>
					<td align="right">��ʼʱ�䣺</td>
					<td align="left"><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:152px" /></td>
					<td align="right">��ֹʱ�䣺</td>
					<td align="left"><input class="easyui-datebox" id="History_EndDate" type="text" style="width:152px" /></td>
					<td align="right">�ֳ����ţ�</td>
					<td align="left"><input id="localeCode" type="text" style="width:152px" /></td>
					<td align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadHistoryCommission()">��ѯ</a></td>
					<td align="center"><a class="easyui-linkbutton" iconCls="icon-reload" href="javascript:void(0)" onClick="queryreset()">����</a></td>
				</tr>
		</table>
		</form>
		<form id="Confirm" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
			<table width="850px" style="margin-top:20px" >
				<tr >
				     <td width="12%" align="right" ><!--<label>�������ƣ�</label>--><input type="hidden" id="History_ApplianceName" value="" style="width:120px" />�깤���λ��:</td>
					 <td width="25%" align="left" ><input name="FinishLocation" id="FinishLocation" type="text" value="" class="easyui-validatebox" required="true" /><input name="FinishComCode" id="FinishComCode" type="hidden" value="" />
					 </td>
					
					<td  align="left" style="padding-top:10px;">
	                     <a id="btn_confirm" class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doConfirm()" >�깤ȷ��</a>  <a id="btn_updateLocation" class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="updateLocation()" >�깤���λ���޸�</a> 
					</td>
					
				</tr>
		  </table>
		  </form>
		</div> 
	<table id="table6" style="height:300px; width:1000px">
	</table>
	<form method="post" action="/jlyw/TaskManage/CompleteConfirm.jsp" id="doConfirmForm" target="_blank">
		<input type="hidden" name="Code" id="Code" value="" />
		<input type="hidden" name="Pwd" id="Pwd" value="" />
	</form>
	<div id="table6-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="PrintCertificate()" title="��ӡί�е�֤��">��ӡ֤��</a>&nbsp;<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-search" name="print" onclick="doLook()" title="�鿴ί�е���ϸ">�鿴ί�е���ϸ</a>&nbsp;<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="PrintOriginalRecordExcel()" title="��ӡί�е���ԭʼ��¼">��ӡԭʼ��¼</a>&nbsp;<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-print" name="print" onclick="PrintLabel()" title="��ӡ�ϸ�֤��ǩ">��ӡ�ϸ�֤��ǩ</a>&nbsp;<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-undo" name="SelectLeft" onclick="SelectLeft()" title="��ѡ">��ѡ</a>&nbsp;<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" icon="icon-search" name="CertificateList" onclick="CertificateList()" title="ѡ��֤���ӡ">ѡ��֤���ӡ</a>
				</td>
				<td style="text-align:right;padding-right:2px">
				</td>
			</tr>
		</table>
	</div>
	
	 <div id="p2" class="easyui-panel" style="width:1000px; height:200px; padding:10px;"
				title="��ӡԤ����" collapsible="false"  closable="false">
			<iframe id="PdfPrintFrame" name="PdfPrintFrame" src="" frameborder="0" width="100%" height="100%"></iframe>
			<form action="" method="post" target="PdfPrintFrame" id="postData_form">
		 		<input id="CertificationId" name="CertificationId" type="hidden" value=""/>
			</form>
	 </div>
	
	<div id="select_certificate_print_window" class="easyui-window" closed="true" modal="true" title="ѡ��Ҫ��ӡ��֤���ԭʼ��¼" iconCls="icon-print" style="width:780px;height:400px;padding:5px;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
				<table id="OriginalRecordTable" iconCls="icon-tip"></table>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<span id="oneprint-execute-now"><a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onclick="PrintCertificateByOneCommissionSheet()" >��ӡ֤��</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onclick="PrintOriginalRecordExcelByOneCommissionSheet()" >��ӡԭʼ��¼</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onclick="PrintLabelByOneCommissionSheet()" >��ӡ�ϸ�֤��ǩ</a></span>&nbsp;&nbsp;
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="doCloseSelectCertificatePrintWindow()">ȡ��</a>
			</div>
		</div>
	</div>
	
<form id="formLook" method="post" action="/jlyw/WebPrint/JDLabelPrint.jsp" target="PrintFrame">			
		
		<input type="hidden" name="PrintObj" id="PrintObj" value="" />
</form>
<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	
		
	</DIV>
</DIV>
</body>
</html>
