<%@ page contentType="text/html; charset=gbk" language="java" import="com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>Ԥ��ί�е�</title>
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
		<script type="text/javascript" src="../../JScript/NumberChanger.js"></script>
		
		<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>
		<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
			   <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
		</object>
		<script type="text/javascript" src="../../WebPrint/printer.js"></script>
		<script type="text/javascript" src="../../WebPrint/printCommisionSheet.js"></script>
		<script type="text/javascript" src="./CommissionSheet.js"></script>
		
		
        <style type="text/css">
<!--
.STYLE1 {color: #FF0000}
-->
        </style>

<script language="javascript">
$(function(){
	$("#CommissionDate").datebox({
		onSelect:function(nowDate){
			nowDate.setDate(nowDate.getDate() + 7);
			$('#PromiseDate').datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		}
	});
	
	var nowDate = new Date();
	$("#CommissionDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	nowDate.setDate(nowDate.getDate() + 7);
	$("#PromiseDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	$("#CommissionSheetForm").form('validate');
	
	
	
});
</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="Ԥ��ί�е�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">  
		<form id="CommissionSheetForm" method="post" >
			<input type="hidden" name="Appliances" id="Appliances" value="" />
		 <div class="easyui-panel" style="width:1005px; margin:0px; padding:0px" title="�½�ί�е�">
			<br />
			<table width="1000px" id="table1">
				<tr>
				  <td width="75" align="right">ί����ʽ��</td>
				  <td width="169"  align="left"><select name="CommissionType" style="width:152px">
					  <option value="1" selected="selected">�������</option>
					  <option value="5">����ҵ��</option>
					  <option value="6">�Լ�ҵ��</option>
					  <option value="7">�ֳ�����</option>
					  <option value="3">��������</option>
					  <option value="4">��ʽ����</option>
					</select></td>
				  <td width="68" align="right">ί�����ڣ�</td>
				  <td width="168"  align="left"><input class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" required="true" /></td>
					<td width="69"  align="right">��ŵ����</td>
				    <td width="112"  align="left"><input class="easyui-datebox" name="PromiseDate" id="PromiseDate" type="text" /></td>
				    <td width="69"  align="right"></td>
			        <td width="234"  align="left"></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">ί�е�λ��</td>
              <td width="187" align="left"><select name="CustomerName" id="CustomerName" style="width:125px"></select><input type="button" class="add" value="�½�" onclick="window.location.href='/jlyw/BaseData/wtUnit/Customer.jsp'" /></td>
                <td width="64" align="right">&nbsp;��&nbsp;&nbsp;����</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">&nbsp;��&nbsp;&nbsp;ַ��</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>
				<td width="65" align="right">�������룺</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" class="easyui-validatebox" required="true" /></td>
				<td align="right">�ֻ����룺</td>
				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">֤�鵥λ��</td>
				<td align="left"><select name="SampleFrom" id="SampleFrom" style="width:152px" ></select></td>
                <td align="right">��Ʊ��λ��</td>
				<td align="left"><select name="BillingTo"  id="BillingTo" style="width:152px" ></select></td>
			</tr>
		</table>

          <table id="table3" width="1000px">
			<tr>
				<td width="77" align="right">ί���ˣ�</td>
		    <td width="187" align="left"><input name="CustomerHandler" id="CustomerHandler" type="text" /></td>
				<td width="64" align="right">̨ͷ���ƣ�</td>
			  <td width="171" align="left"><select name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
				<!--<td width="63" align="right">�����ˣ�</td>
			  <td width="80" align="left"><input type="text" style="width:80px"/></td>
				<td width="44" align="right">���ڣ�</td>
			  <td width="80" align="left"><input type="text" id="dateTimeFrom" class="easyui-datebox" style="width:82px"/></td>
				<td width="75" align="right">�깤ȷ�ϣ�</td>
			  <td width="80" align="left"><input type="text" style="width:80px"/></td>
				<td width="47" align="right">���ڣ�</td>
			  <td width="159" align="left"><input type="text"id="dateTimeEnd" class="easyui-datebox" style="width:82px" /></td>-->
			  <td width="64" align="right">������ַ��</td>
		    <td width="168" align="left"><select name="RecipientAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true" editable="false"></select></td>
				<td width="65" align="right">ȡ����ַ��</td>
			  <td width="168" align="left"><select name="PickupAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true" editable="false"></select></td>
			</tr>
		</table>
		
		<br/>
 		 <table id="table2" width="1000">
			<tr>
				<td width="77" align="right">������Ȩ����</td>
              <td width="187" align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:152px">
              </select><span class="STYLE1">*</span>
              <input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /></td>
                <td width="64" align="right">�������ƣ�</td>
			    <td width="171"  align="left"><select name="ApplianceName" class="easyui-combobox" valueField="name" textField='name' id="ApplianceName" style="width:152px">
			      </select>		      </td>
			    <td width="64" align="right">������ţ�</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>
				<td width="65" align="right">�����ţ�</td>
			  <td width="168" align="left"><input id="AppManageCode" name="AppManageCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">�ͺŹ��</td>
				<td align="left"><select name="Model" id="Model" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">������Χ��</td>
			  <td align="left"><select name="Range" id="Range" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">���ȵȼ���</td>
				<td align="left"><select name="Accuracy" id="Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox"/>��&nbsp;&nbsp;��</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">�� �� ����</td>
				<td align="left"><select name="Manufacturer" id="Manufacturer" class="easyui-combobox" valueField="name" textField='name' style="width:152px">
				  </select>				</td>
				<td align="right">��&nbsp;&nbsp;&nbsp;����</td>
			  <td align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox" min="1" />��<span class="STYLE1">*</span></td>
				<td align="right">�Ƿ�ǿ�죺</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1" >��ǿ�Ƽ춨</option>
						<option value="0">ǿ�Ƽ춨</option>
					</select><span class="STYLE1">*</span>				</td>
                <td align="left"><input id="Trans" name="Trans" type="checkbox"/>ת&nbsp;&nbsp;��</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">��۸�����</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text" value="����������޸�����"  /></td>
				<td align="right">�������</td>
				<td align="left">
					<select id="Repair" name="Repair" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1">��������</option>
						<option value="0">��Ҫ����</option>
					</select><span class="STYLE1">*</span>				</td>
				<td align="right">������ʽ��</td>
				<td align="left">
					<select id="ReportType" name="ReportType" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1">�춨</option>
						<option value="2">У׼</option>
						<option value="3">���</option>
						<option value="4">����</option>
					</select><span class="STYLE1">*</span>				</td>
                <td align="right">ת �� ����</td>
				<td align="left"><select name="SubContractor" id="SubContractor" style="width:152px"></select></td>
			</tr>
			<tr>
				<td align="right">����Ҫ��</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text" value="��"  /></td>
				<td align="right">���λ�ã�</td>
				<td align="left"><input id="Location" name="Location" type="text" /></td>
				<td align="right">��&nbsp;��&nbsp;�ˣ�</td>
				<td align="left"><select name="Allotee" id="Allotee" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="left" colspan="2"><input name="AlloteeRule" type="radio" value="0" <%= (SystemCfgUtil.getTaskAllotRule() == 0)?"checked=\"checked\"":"" %> />��ҵ������&nbsp;<input name="AlloteeRule" type="radio" value="1" <%= (SystemCfgUtil.getTaskAllotRule() != 0)?"checked=\"checked\"":"" %> />����ֵ��</td>
			</tr>
	 	 </table>
 		
		<table id="table4" width="1000px" style="margin-top:20px">
			<tr align="center" >
			    <td align="right">Ԥ����Ŀ��</td>
				<td align="left"><input id="YLNumber" name="YLNumber"  class="easyui-numberbox" required="true" /></td>
				
				<td align="center" colspan="2">
					
			    </td>
                <td align="center" colspan="2">
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="doSubmitFormYL()" title="Ԥ��ί�е���">Ԥ��ί�е�</a>
			  </td>
                <td align="center" colspan="2">
				    <a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-print" name="print" onclick="printSubmitFormYL()" title="��ӡԤ��ί�е���">��ӡԤ��ί�е�</a>
				</td>
					
			</tr>
	  </table>
	  <br />
	  </div>
	  </form> 
<form id="formLook" method="post" action="/jlyw/SFGL/wtd/YLComSheetListPrint.jsp" target="PrintFrame" >			
		
		<input type="hidden" name="YLCommissionSheetList" id="YLCommissionSheetList" />
</form>
<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
   
	</DIV>
</DIV>
</body>
</html>
