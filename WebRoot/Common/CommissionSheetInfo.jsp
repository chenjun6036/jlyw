<%@ page language="java" pageEncoding="gb2312"%>

<div id="CommissionSheetInfoDiv" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
			title="ί�е���Ϣ" collapsible="false"  closable="false">
	<form id="CommissionSheetForm" method="post">
		<input type="hidden" id="CommissionId" name="CommissionId" value="" />
		<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
		<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
		<table width="1000px" id="table1">
			<tr>

			  <td width="77" align="right">ί����ʽ��</td>
			  <td width="187"  align="left">
					<select name="CommissionType" style="width:152px">
						<option value="1">�������</option>
						<option value="5">����ҵ��</option>
						<option value="6">�Լ�ҵ��</option>
						<option value="7">�ֳ�����</option>
						<option value="3">��������</option>
						<option value="4">��ʽ����</option>
						<option value="2">�ֳ����</option>
					</select>
			  </td>
			  <td width="64" align="right">ί�����ڣ�</td>
			  <td width="171"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
				  <td width="64" align="right">ί�е��ţ�</td>
			  <td width="168" align="left"><input name="CommissionCode" id="CommissionCode" type="text" /></td>
	
			  <td width="65" align="right">ί�е�״̬��</td>
			  <td width="168" align="left">
			  		<select name="CommissionStatus" style="width:152px">
						<option value="0">���ռ�</option>
						<option value="1">�ѷ���</option>
						<option value="2">ת����</option>
						<option value="3">���깤</option>
						<option value="4">�ѽ���</option>
						<option value="9">��ȡ��</option>
						<option value="10">��ע��</option>
                        <option value="-1">Ԥ����</option>
					</select>
			  </td>
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
            <td align="right">������ʽ��</td>
			<td align="left"><select id="ReportType" name="ReportType" style="width:152px">
						<option value="1">�춨</option>
						<option value="2">У׼</option>
						<option value="3">���</option>
						<option value="4">����</option>
					</select></td>
			<td align="left"></td>
			<td align="left">&nbsp;</td>
		</tr>
	 </table><br/>

	</form>
</div>
