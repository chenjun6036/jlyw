<%@ page language="java" pageEncoding="gb2312"%>

<div id="CommissionSheetInfoDiv" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
			title="委托单信息" collapsible="false"  closable="false">
	<form id="CommissionSheetForm" method="post">
		<input type="hidden" id="CommissionId" name="CommissionId" value="" />
		<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
		<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
		<table width="1000px" id="table1">
			<tr>

			  <td width="77" align="right">委托形式：</td>
			  <td width="187"  align="left">
					<select name="CommissionType" style="width:152px">
						<option value="1">送样检测</option>
						<option value="5">其它业务</option>
						<option value="6">自检业务</option>
						<option value="7">现场带回</option>
						<option value="3">公正计量</option>
						<option value="4">形式评价</option>
						<option value="2">现场检测</option>
					</select>
			  </td>
			  <td width="64" align="right">委托日期：</td>
			  <td width="171"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
				  <td width="64" align="right">委托单号：</td>
			  <td width="168" align="left"><input name="CommissionCode" id="CommissionCode" type="text" /></td>
	
			  <td width="65" align="right">委托单状态：</td>
			  <td width="168" align="left">
			  		<select name="CommissionStatus" style="width:152px">
						<option value="0">已收件</option>
						<option value="1">已分配</option>
						<option value="2">转包中</option>
						<option value="3">已完工</option>
						<option value="4">已结帐</option>
						<option value="9">已取样</option>
						<option value="10">已注销</option>
                        <option value="-1">预留中</option>
					</select>
			  </td>
			</tr>
	</table>
	<table width="1000">
		<tr>

		  <td width="77"  align="right">委托单位：</td>
		  <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" style="width:152px;" /></td>
			<td width="64" align="right">电&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
		  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text" /></td>
			<td width="64" align="right">地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
		  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" /></td>

			<td width="65" align="right">邮政编码：</td>
		  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" /></td>
		</tr>
		<tr>
			<td align="right">联&nbsp;系&nbsp;人：</td>
			<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" /></td>
			<td align="right">手机号码：</td>

			<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
			<td align="right">证书单位：</td>
			<td align="left"><input name="SampleFrom" id="SampleFrom" type="text" /></td>
			<td align="right">开票单位：</td>
			<td align="left"><input name="BillingTo" id="BillingTo" type="text" /></td>
		</tr>
	</table><br/>
	 <table id="table2" width="1000">

		<tr>
			<td width="77" align="right">器具名称：</td>
			<td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" /></td>
			<td width="64" align="right">型号规格：</td>
		  <td width="171"  align="left"><input id="Model" name="Model" type="text" /></td>
			<td width="64" align="right">出厂编号：</td>
		  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>

			<td width="65" align="right">管理编号：</td>
		  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" /></td>
		</tr>
		<tr>
			<td align="right">制 造 厂：</td>
			<td align="left"><input id="Manufacturer" name="Manufacturer" type="text"  /></td>
			<td align="right">数&nbsp;&nbsp;&nbsp;&nbsp;量：</td>

			<td align="left"><input id="Quantity" name="Quantity" type="text"/>件</td>
			<td align="right">是否强检：</td>
			<td align="left">
				<select id="Mandatory" name="Mandatory" style="width:152px">
					<option selected="selected" value="1" >非强制检定</option>
					<option value="0">强制检定</option>
				</select>

			</td>
			<td align="left"><input id="Ness" name="Ness" type="checkbox" />加&nbsp;&nbsp;急</td>
			<td align="left">&nbsp;</td>
		</tr>
		<tr>
			<td align="right">外观附件：</td>
			<td align="left"><input id="Appearance" name="Appearance" type="text"  /></td>

			<td align="right">其他要求：</td>
			<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text"  /></td>
            <td align="right">报告形式：</td>
			<td align="left"><select id="ReportType" name="ReportType" style="width:152px">
						<option value="1">检定</option>
						<option value="2">校准</option>
						<option value="3">检测</option>
						<option value="4">检验</option>
					</select></td>
			<td align="left"></td>
			<td align="left">&nbsp;</td>
		</tr>
	 </table><br/>

	</form>
</div>
