<%@ page contentType="text/html; charset=gbk" language="java" import="com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>委托单管理</title>
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
		<link href="../../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />

		<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="../../JScript/tool.js"></script>
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
			<jsp:param name="TitleName" value="录入委托单" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">  
		<form id="CommissionSheetForm" method="post" >
			<input type="hidden" name="Appliances" id="Appliances" value="" />
			<input type="hidden" name="ComSheetType" id="ComSheetType" value="" /><!-- 现场向委托单导入的标识符，新建委托单界面应为空 -->
		 <div class="easyui-panel" style="width:1005px; margin:0px; padding:0px" title="新建委托单">
			<br />
			<table width="1000px" id="table1">
				<tr>
				  <td width="75" align="right">委托形式：</td>
				  <td width="169"  align="left"><select name="CommissionType" style="width:152px">
					  <option value="1" selected="selected">送样检测</option>
					  <option value="2">现场检测</option>
					  <option value="5">其它业务</option>
					  <option value="6">自检业务</option>
					  <option value="7">现场带回</option>
					  <option value="3">公正计量</option>
					  <option value="4">型式评价</option>
					</select></td>
				  <td width="68" align="right">委托日期：</td>
				  <td width="168"  align="left"><input class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" required="true" /></td>
					<td width="69"  align="right">承诺日期</td>
				    <td width="112"  align="left"><input class="easyui-datebox" name="PromiseDate" id="PromiseDate" type="text" /></td>
				    <td width="69"  align="right">报价单号：</td>
			      <td width="234"  align="left"><input type="text" name="QuotationId" id="QuotationId" /><input type="button" class="preview-button" name="check" value="导入" onclick="searchQuoItem()" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">委托单位：</td>
              <td width="187" align="left"><select name="CustomerName" id="CustomerName" style="width:125px"></select><input type="button" class="add" value="新建" onclick="window.location.href='/jlyw/BaseData/wtUnit/Customer.jsp'" /></td>
                <td width="64" align="right">&nbsp;电&nbsp;&nbsp;话：</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">&nbsp;地&nbsp;&nbsp;址：</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>
				<td width="65" align="right">邮政编码：</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">联&nbsp;系&nbsp;人：</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" class="easyui-validatebox" required="true" /></td>
				<td align="right">手机号码：</td>
				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">证书单位：</td>
				<td align="left"><select name="SampleFrom" id="SampleFrom" style="width:152px" ></select></td>
                <td align="right">开票单位：</td>
				<td align="left"><select name="BillingTo"  id="BillingTo" style="width:152px" ></select></td>
			</tr>
		</table>

          <table id="table3" width="1000px">
			<tr>
				<td width="77" align="right">委托人：</td>
		    <td width="187" align="left"><input name="CustomerHandler" id="CustomerHandler" type="text" /></td>
				<td width="64" align="right">台头名称：</td>
			  <td width="171" align="left"><select name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
				<!--<td width="63" align="right">检修人：</td>
			  <td width="80" align="left"><input type="text" style="width:80px"/></td>
				<td width="44" align="right">日期：</td>
			  <td width="80" align="left"><input type="text" id="dateTimeFrom" class="easyui-datebox" style="width:82px"/></td>
				<td width="75" align="right">完工确认：</td>
			  <td width="80" align="left"><input type="text" style="width:80px"/></td>
				<td width="47" align="right">日期：</td>
			  <td width="159" align="left"><input type="text"id="dateTimeEnd" class="easyui-datebox" style="width:82px" /></td>-->
			  <td width="64" align="right">送样地址：</td>
		    <td width="168" align="left"><select name="RecipientAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true" editable="false"></select></td>
				<td width="65" align="right">取样地址：</td>
			  <td width="168" align="left"><select name="PickupAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true" editable="false"></select></td>
			</tr>
		</table>
		
		<br/>
 		 <table id="table2" width="1000">
			<tr>
				<td width="77" align="right">器具授权名：</td>
              <td width="187" align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:152px">
              </select><span class="STYLE1">*</span>
              <input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /></td>
                <td width="64" align="right">器具名称：</td>
			    <td width="171"  align="left"><select name="ApplianceName" class="easyui-combobox" valueField="name" textField='name' id="ApplianceName" style="width:152px">
			      </select>		      </td>
			    <td width="64" align="right">出厂编号：</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>
				<td width="65" align="right">管理编号：</td>
			  <td width="168" align="left"><input id="AppManageCode" name="AppManageCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">型号规格：</td>
				<td align="left"><select name="Model" id="Model" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">测量范围：</td>
			  <td align="left"><select name="Range" id="Range" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">准确度等&nbsp;&nbsp;</br>级：</td>
				<td align="left"><select name="Accuracy" id="Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox"/>加&nbsp;&nbsp;急</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">制 造 厂：</td>
				<td align="left"><select name="Manufacturer" id="Manufacturer" class="easyui-combobox" valueField="name" textField='name' style="width:152px">
				  </select>				</td>
				<td align="right">数&nbsp;&nbsp;&nbsp;量：</td>
			  <td align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox"/>件<span class="STYLE1">*</span></td>
				<td align="right">是否强检：</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option value="" selected="selected">请选择...</option>
						<option value="1" >非强制检定</option>
						<option value="0">强制检定</option>
					</select><span class="STYLE1">*</span>				</td>
                <td align="left"><input id="Trans" name="Trans" type="checkbox"/>转&nbsp;&nbsp;包</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">外观附件：</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text" value="外观正常，无附件。"  /></td>
				<td align="right">需修理否：</td>
				<td align="left">
					<select id="Repair" name="Repair" style="width:152px">
						<option value="" selected="selected">请选择...</option>
						<option value="1">无需修理</option>
						<option value="0">需要修理</option>
					</select><span class="STYLE1">*</span>				</td>
				<td align="right">报告形式：</td>
				<td align="left">
					<select id="ReportType" name="ReportType" style="width:152px">
						<option value="" selected="selected">请选择...</option>
						<option value="1">检定</option>
						<option value="2">校准</option>
						<option value="3">检测</option>
						<option value="4">检验</option>
					</select><span class="STYLE1">*</span>				</td>
                <td align="right">转 包 方：</td>
				<td align="left"><select name="SubContractor" id="SubContractor" style="width:152px"></select></td>
			</tr>
			<tr>
				<td align="right">其他要求：</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text" value="无"  /></td>
				<td align="right">存放位置：</td>
				<td align="left"><input id="Location" name="Location" type="text" /></td>
				<td align="right">派&nbsp;定&nbsp;人：</td>
				<td align="left"><select name="Allotee" id="Allotee" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="left" colspan="2"><input name="AlloteeRule" type="radio" value="0" <%= (SystemCfgUtil.getTaskAllotRule() == 0)?"checked=\"checked\"":"" %> />按业务量排&nbsp;<input name="AlloteeRule" type="radio" value="1" <%= (SystemCfgUtil.getTaskAllotRule() != 0)?"checked=\"checked\"":"" %> />按产值排</td>
			</tr>
	 	 </table>
 		
		<table id="table4" width="1000px" style="margin-top:20px">
			<tr align="center" >
			    <td align="center" colspan="2">
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="AddRecord()" id="AddRecordHref">添加器具</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-edit2" name="Add" onclick="UpdateRecord()">更新器具</a>
			    </td>
				<td align="center" colspan="2">
					<a href="javascript:void(0)" id="doSubmitFormHref" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="doSubmitForm('#doSubmitFormHref')" title="保存选中的器具并打印生成的委托单">保存并打印委托单</a>
			  </td>
                <td align="center" colspan="2">
					<a href="javascript:void(0)" id="doSubmitFormNoPrintHref" class="easyui-linkbutton" icon="icon-reload" name="Add" onclick="doSubmitFormNoPrint('#doSubmitFormNoPrintHref')" title="保存选中的器具并生成的委托单，不打印！">保存委托单</a>
			  </td>
                <td align="center" colspan="2">
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintCommissionSheet()" title="打印勾选的委托单">打印委托单</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintLabel()" >打印标签</a>
			  </td>
			</tr>
	  </table>
	  <br />
	  </div>
	  </form> 
	  
	  	  <table id="table5" style="height:300px; width:1000px" >
		  </table>
	  <br/>
	  
	  
		<table id="table6" style="height:300px; width:1000px">
		</table>
		<div id="table6-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="AddRecordFromHistory()">提交选中的历史记录</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>器具名称：</label><input type="text" id="History_ApplianceName" value="" style="width:120px" />&nbsp;<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;<label>截止时间：</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询所选委托单位的历史送检记录" id="btnHistorySearch" onclick="doLoadHistoryCommission()">查询历史记录</a>
					</td>
				</tr>
			</table>
		</div>
		
		
<div id="inputQuo_window" class="easyui-window" modal="true" title="从报价单导入到委托单" style="height:400px; width:800px;" iconCls="icon-save" closed="true" maximizable="false" minimizable="false" collapsible="false">
		<table id="table_QuoItem" ></table>
</div> 

<div id="LabelPrint_window" class="easyui-window" modal="true" title="选择打印范围" style="width:350px; padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="LabelPrint_form" >
<table>
	<tr>
	  <td>
	  <div style="height:30px">
	  器具范围：<input class="easyui-numberbox" id="RangeFrom" name="RangeFrom" type="text" style="width:40px" required="true"/>--<input class="easyui-numberbox" id="RangeEnd" name="RangeEnd" type="text" style="width:40px" required="true"/>&nbsp;&nbsp;
	  &nbsp;&nbsp;附件数：<input class="easyui-numberbox" id="AttachmentNum" name="AttachmentNum" type="text" style="width:40px" required="true"/>
	  </div>
	  </td>
 </tr>
 <tr>
	   <td align="center" height="45px" valign="bottom">
	  <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-print"  href="javascript:void(0)" onclick="doPrintLabelSubmit()">确认打印</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#LabelPrint_window').window('close');" >关闭</a>
	  </div>
	  </td>	
</tr>
</table>
</form>	
</div >	
   
	</DIV>
</DIV>
</body>
</html>
