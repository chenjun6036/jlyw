<%@ page contentType="text/html; charset=gbk" language="java" import="com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�ֳ�ί�е�����</title>
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
		<script type="text/javascript" src="../../JScript/RandGenerator.js"></script>
		
		<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>
		<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
			   <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
		</object>
		<script type="text/javascript" src="../../WebPrint/printer.js"></script>
		<script type="text/javascript" src="../../WebPrint/printCommisionSheet.js"></script>
		<script type="text/javascript" src="./LocaleCommissionSheet.js"></script>
<script>
$(function(){
	var LocaleCommissionCodeTemp = '<%=request.getParameter("LocaleCommissionCode")==null?"":request.getParameter("LocaleCommissionCode") %>';
	if(LocaleCommissionCodeTemp.length > 0){	//�����ֳ�ҵ����Ϣ
		$.ajax({
				type: "post",
				url: "/jlyw/LocaleMissionServlet.do?method=15&QueryName="+encodeURI(LocaleCommissionCodeTemp),
				dataType: "json",	//�������������ݵ�Ԥ������
				beforeSend: function(XMLHttpRequest){
				},
				success: function(data, textStatus){
					if(data.length > 0){
						$("#LocaleCommissionCode").combobox('loadData',data);
						$("#LocaleCommissionCode").combobox('setValue',data[0].code);
						$("#locMissionId").val(data[0].Id);
						$("#CommissionSheetForm").form('load',data[0]);
						searchLocMission();
					}
				},
				complete: function(XMLHttpRequest, textStatus){
					//HideLoading();
				},
				error: function(){
					//���������
				}
		});
	}
});
</script>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/jlyw/WebPrint/install_lodop.exe"></embed>
</object> 
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="¼���ֳ�ί�е�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">  
		<form id="CommissionSheetForm" method="post" >
			<input type="hidden" name="Appliances" id="Appliances" value="" />
			<input type="hidden" name="ComSheetType" id="ComSheetType" value="2" /><!-- �ֳ���ί�е�����ı�ʶ�����½�ί�е�����ӦΪ�� -->
		 <div class="easyui-panel" style="width:1005px; margin:0px; padding:0px" title="�½��ֳ�ί�е�">
			<br />
			
			<table width="1000px" id="table1">
				<tr>
				  <td width="75" align="right">ί����ʽ��</td>
				  <td width="169"  align="left">
						<select name="CommissionType" style="width:152px">
							<option value="2" selected="selected">�ֳ����</option>
						</select>				  </td>
				  <td width="68" align="right">��ŵ���ڣ�</td>
				  <td width="168"  align="left"><input class="easyui-datebox" name="PromiseDate" id="PromiseDate" type="text" /></td>
					<td width="69"  align="right"></td>
				    <td width="112"  align="left"></td>
				    <td width="69"  align="right"></td>
			      <td width="234"  align="left"></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">ί�е�λ��</td>
              <td width="187" align="left"><select name="CustomerName" id="CustomerName" style="width:125px" onclick="this.select()"></select><input type="button" class="add" value="�½�" onclick="window.location.href='/jlyw/BaseData/wtUnit/Customer.jsp'" /></td>
                <td width="64" align="right">&nbsp;��&nbsp;&nbsp;����</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">&nbsp;��&nbsp;&nbsp;ַ��</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>
				<td width="65" align="right">�������룺</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">�� ϵ �ˣ�</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" class="easyui-validatebox" required="true" /></td>
				<td align="right">�ֻ����룺</td>
				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">֤�鵥λ��</td>
				<td align="left"><select name="SampleFrom" id="SampleFrom" style="width:152px" ></select></td>
                <td align="right">��Ʊ��λ��</td>
				<td align="left"><select name="BillingTo"  id="BillingTo" style="width:152px" ></select></td>
			</tr>
			<tr>
				<td align="right">ί �� �ˣ�</td>
		    	<td align="left"><input name="CustomerHandler" type="text" id="CustomerHandler" style="width:152px"  /></td>
				<td align="right">̨ͷ���ƣ�</td>
			  	<td align="left"><select name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
			  	<td align="right">������ַ��</td>
		    	<td align="left"><select name="RecipientAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" editable="false"></select></td>
				<td align="right">ȡ����ַ��</td>
			  	<td align="left"><select name="PickupAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true"  editable="false"></select></td>
			</tr>
			<tr>
				<td align="right">�ֳ�ί��</br>��&nbsp;&nbsp;�ţ�</td>
		   		<td align="left"><select name="LocaleCommissionCode" id="LocaleCommissionCode" style="width:120px"></select><input type="button" class="preview-button" name="check" value="����" onclick="searchLocMission()" /></td>
		   		<td align="right">�ֳ����</br>ʱ�䣺</td>
			  	<td align="left"><input name="LocaleCommissionDate" id="LocaleCommissionDate" type="text" style="width:152px" class="easyui-validatebox" required="true" readonly="readonly" /></td>
			  	<td align="right">��⸺��</br>�ˣ�</td>
		    	<td align="left"><select name="LocaleStaffId" id="LocaleStaffId" style="width:152px"></select></td>
				<td align="right"></td>
			  	<td align="left"></td>
			</tr>
		</table>

		<table id="table4" width="1000px" style="margin-top:20px">
			<tr align="left" >
			    <td align="left" colspan="2">
					&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload" name="Add" onclick="doSubmitFormNoPrint()" title="�������е����߲����ɵ�ί�е���">����ί�е�</a>
					
			    </td>
                <td align="center" colspan="2">
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-print" onclick="doPrintCommissionSheet()" >��ӡ��ѡί�е�</a>
			  </td>
			</tr>
	  </table>
	  
	  <br />
	  </div>
	  </form>
	  
	  <table id="table5" style="height:300px; width:1000px" >
	  </table>
	  <br/>
	  

<div id="inputLoc_window" class="easyui-window" modal="true" title="���ֳ������뵽ί�е�" style="height:400px; width:800px;" iconCls="icon-save" closed="true" maximizable="false" minimizable="false" collapsible="false">
		<table id="table_LocItem" ></table>
		<input id="locMissionId" type="hidden"/>
</div> 

	</DIV>
</DIV>
</body>
</html>
