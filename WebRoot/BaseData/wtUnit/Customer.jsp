<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>委托方信息管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
		function cancel(){
			/*$('#name').val("");
			$('#nameEn').val("");
			$('#customerType').combobox('setValue',"0");
			$('#brief').val("");
			$('#code').val("");
			$('#rid').val("");
			$('#zcd').val("");
			$('#addr').val("");
			$('#addren').val("");
			$('#tel').val("");
			$('#fax').val("");
			$('#con').val("");
			$('#contactortel1').val("");
			$('#contactortel2').val("");
			$('#cla').combobox('setValue',"3C");
			$('#sta').combobox('setValue',"0");
			$('#acc').val("");
			$('#accBank').val("");
			$('#crdamount').val("");
			$('#fldema').val("");
			$('#spcdema').val("");
			$('#cerdema').val("");
			$('#rmk').val("");
			$('#InsideContactor').val("");*/
			$('#frm_add_customer').form('clear');
		}
		
		function savereg(){
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/CustomerServlet.do?method=1',
				onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#name').val()));
		}
		
		$(function(){
			$('#cla').combobox({
				multiple:'true'
			});
			
			$('#InsideContactor').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
			$('#frm_add_customer').form('validate');
		});
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单位详细信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

<form id="frm_add_customer" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="新增委托单位">
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：</td>
			<td align="left"  style="width：30%"><input id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            <td align="right" style="width：20%">拼音简码：</td>
			<td align="left"  style="width：30%"><input id="brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr height="30px">
			<td align="right">英文名称：</td>
			<td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:465px" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr height="30px">
			<td align="right">单位类型：</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>单位</option>
					<option value='1'>个人</option>
					<option value='2'>网上注册单位</option>
				</select>
			</td>
			<td align="right">单位代码：</td>
			<td align="left" ><input id="code" name="Code" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr  height="30px">
			<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
			<td align="left">
				<select id="rid" name="RegionId" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
			</td>
			<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left" ><input id="zcd" name="ZipCode" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr height="30px">
			<td align="right">单位地址：</td>
			<td align="left"><input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/></td>
			<td align="right">地址英文：</td>
			<td align="left"><input id="addren" name="AddressEn" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr height="30px">
			<td align="right">联系电话：</td>
			<td align="left"><input id="tel" name="Tel" type="text" class="easyui-validatebox" required="true"/></td>
			<td align="right">单位传真：</td>
			<td align="left"><input id="fax" name="Fax" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr>
			<td align="right" rowspan="2">联&nbsp;系&nbsp;人：</td>
			<td align="left" rowspan="2"><input id="con" name="Contactor" type="text" class="easyui-validatebox" required="true"/></td>
			<td align="right">联系人号码1：</td>
			<td align="left"><input id="contactortel1" name="ContactorTel1" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr>
			<td align="right">联系人号码2：</td>
			<td align="left"><input id="contactortel2" name="ContactorTel2" type="text" /></td>
		</tr>
		<tr height="30px">
			<td align="right">企业分类：</td>
			<td align="left">
				<input id="cla" name="Classification" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
			</td>
			<td align="right">单位状态：</td>
			<td align="left">
				<select id="sta" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
						<option value='0' selected>正常</option>
						<option value='1'>注销</option>
				 </select></td>
		</tr>
		<tr height="30px">
        	<td align="right">开户银行：</td>
			<td align="left"><input id="accBank" name="AccountBank" type="text" class="easyui-validatebox" required="true"/></td>
			<td align="right">账&nbsp;&nbsp;号：</td>
			<td align="left"><input id="acc" name="Account" type="text" class="easyui-validatebox" required="true"/></td>
        </tr>
        <tr>
			<td align="right">信用额度：</td>
			<td align="left"><input id="crdamount" name="CreditAmount" type="text" class="easyui-numberbox" required="true"/></td>
            <td align="right">内部联系人：</td>
			<td align="left"><input id="InsideContactor" name="InsideContactor" class="easyui-combobox" style="width:150px" valueField="name" textField="name" panelHeight="150px"/></td>
		</tr>
		<tr height="30px">
			<td align="right">下厂要求：</td>
			<td align="left"  colspan="3"><textarea id="fldema" name="FieldDemands" cols="55" rows="2" ></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">特殊要求：</td>
			<td align="left"  colspan="3"><textarea id="spcdema" name="SpecialDemands" cols="55" rows="2"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">证书要求：</td>
			<td align="left"  colspan="3"><textarea id="cerdema" name="CertificateDemands" cols="55" rows="2"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left" colspan="3"><textarea id="rmk" name="Remark" cols="55" rows="2"></textarea> </td>
		</tr>
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">添加</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">重置</a></td>
		</tr>
	</table>
	</form>
	<br />
	<br />


</DIV>
</DIV>
</body>
</html>
