<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>ί�е�λ��Ϣ¼��</title>
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
		$(function(){
		$('#industry').combobox({
		url:'/jlyw/CrmServlet.do?method=11',
		valueField:'Id',
		textField:'Name',
		editable:false,
		onSelect:function(rec)
		{
		$('#industryId').val(rec.Id);
		}
		});
	});
	
	$(function(){
		$('#rid').combobox({
		valueField:'id',
		textField:'name',
		editable:false,
		onSelect:function(rec)
		{
			$('#insideContactorId').combobox('reload','/jlyw/CustomerContactorServlet.do?method=9&RegionId='+rec.id);
		//$('#industryId').val(rec.Id);
			$('#insideContactorId').combobox({
			valueField:'ContactorId',
			textField:'Name',
			editable:false,
			onSelect:function(rec)
			{
			},
			onLoadSuccess: function () 
			{
			$('#insideContactorId').combobox('clear');
				var data=$('#insideContactorId').combobox('getData');
				$('#insideContactorId').combobox('select',data[0].ContactorId);
				
			}
			});
			

		}
		});
	});
	
	$(function()
	{
	$("#sta").combobox('setValue','0');
	$("#role").combobox('setValue','1');
	});
		function cancel(){
			$('#frm_add_customer').form('clear');
		}
		
		function savereg(){
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/CrmServlet.do?method=24',
				onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
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
		
		function check()
		{
		$('#checkCode').val($('#code').val());
		$('#frm_code').form('submit',{
				url: '/jlyw/CrmServlet.do?method=38',
				//onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			{
		   			if(result.IsOk==false)
		   			{
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				$('#code').val('');
		   				}
		   			}
		   				
		   		 }
			});
		
		}
	</script>
<script>
function checknum(id)
{
$(id).keydown(function(e){// ע��˴���Ҫ��keypress�����������ܽ��á�Ctrl+c �롡Ctrl+V,����ԭ�������в���keyPress��keyDown���֣�ʮ����Ҫ����ϸ��
if ($.browser.msie) 
{  // �ж������
   if(((event.keyCode > 47) && (event.keyCode < 58)) || (event.keyCode == 8) ) 
   {// �жϼ�ֵ 
   return true; 
   } else 
   {
   return false; 
   }
} else 
{ 
  if ( ((e.which > 47) && (e.which < 58)) || (e.which == 8) || (event.keyCode == 17) ) 
  {
  return true; 
   } else 
   { return false; 
   } 
   }}).focus(function() { 
   this.style.imeMode='disabled';   // �������뷨,��ֹ���������ַ�
   }).bind("paste",function(){return false;});
}
/* $(function()
{
//checknum('#crdamount');
//checknum('#outputExpectation');
//checknum('#serviceFeeLimitation');
$('#outputExpectation').keydown(function(e){// ע��˴���Ҫ��keypress�����������ܽ��á�Ctrl+c �롡Ctrl+V,����ԭ�������в���keyPress��keyDown���֣�ʮ����Ҫ����ϸ��
if ($.browser.msie) 
{  // �ж������
   if(((event.keyCode > 47) && (event.keyCode < 58)) || (event.keyCode == 8) ) 
   {// �жϼ�ֵ 
   return true; 
   } else 
   {
   return false; 
   }
} else 
{ alert();
  if ( ((e.which > 47) && (e.which < 58)) || (e.which == 8) || (event.keyCode == 17) ) 
  {
  return true; 
   } else 
   { return false; 
   } 
   }}).focus(function() { 
   this.style.imeMode='disabled';   // �������뷨,��ֹ���������ַ�
   }).bind("paste",function(){return false;});
}); */

$(function()
{
	$("#payType").combobox(
	{
	onSelect:function()
	{
	    if($("#payType").combobox('getValue')=='2'||$("#payType").combobox('getValue')==2)
	    {
	    	$("#accountCycle").combobox({
	    	required:true
	    	});
	    	$('#frm_add_customer').form('validate');
	    }
	    else 
	    {
	    	$("#accountCycle").combobox({
	    	required:false
	    	});
	    	$('#frm_add_customer').form('validate');
	    }
	}
	});
}
);
</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ί�е�λ��ϸ��Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<form id="frm_code" method="post">
	<input id="checkCode" name="CheckCode" type="hidden" />
	</form>

<form id="frm_add_customer" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="����ί�е�λ">
		<tr height="30px">
			<td align="right" style="width��20%">��λ���ƣ�</td>
			<td align="left"  style="width��30%"><input style="width:140px" id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            <td align="right" style="width��20%">ƴ�����룺</td>
			<td align="left"  style="width��30%"><input style="width:140px" id="brief" name="Brief" type="text" class="easyui-validatebox"/></td>
		</tr>
		<tr>
		<td align="right">���ʽ��</td>
		<td><select style="width:145px" id="payVia" name="PayVia" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">�ֽ�</option>
		<option value="2">֧Ʊ</option>
		<option value="3">����</option>
		<option value="4">POS��</option>
		<option value="5">����</option>
		</select></td>
		<td align="right">�������ͣ�</td>
		<td><select style="width:145px" id="payType" name="PayType" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">������</option>
		<option value="2">���ڽ���</option>
		<option value="3">Ԥ����</option>
		<option value="4">����</option>
		</select>
		</td>
		</tr>
		<tr><td align="right">�������ڣ�</td>
		<td><select style="width:145px" id="accountCycle" name="AccountCycle" class="easyui-combobox" editable="false" required="false">
		<option value="1">һ����</option>
		<option value="2">������</option>
		<option value="3">������</option>
		<option value="4">�ĸ���</option>
		<option value="5">�����</option>
		<option value="6">������</option>
		<option value="7">�߸���</option>
		<option value="8">�˸���</option>
		<option value="9">�Ÿ���</option>
		<option value="10">ʮ����</option>
		<option value="11">ʮһ����</option>
		<option value="12">ʮ������</option>
		</select></td>
		<td align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
			<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr height="30px">
			<td align="right">��λ���ͣ�</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:145px" required="true" panelHeight="auto" valueField="id" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=29" editable="false">
					
				</select>
			</td>
			<td align="right">��λ��ַ��</td>
			<td align="left"><input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr  height="30px">
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
			<td align="left">
				<select id="rid" name="RegionId" class="easyui-combobox" style="width:145px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
			</td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
			<td align="left" ><input id="zcd" name="ZipCode" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr ><td align="left" boderwidth="2px;" colspan="5" >-------------------------------------------------------------------------------------------------------------</td>
		</tr>
		<tr  height="30px">
			<td align="right">�ڲ���ϵ�ˣ�</td>
			<td align="left">
				<select id="insideContactorId" name="InsideContactorId" panelHeight="auto" class="easyui-combobox" style="width:145px"  editable="false" ></select>
			</td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ɫ��</td>
			<td align="left" ><select style="width:145px" id="role" name="Role" type="text" class="easyui-combobox"  panelHeight="auto">
			<option value="1" selected="selected">A</option>
			<option value="2">B</option>
			</select></td>
		</tr>
		
		<tr height="30px">
			
			<td align="right">��λ���룺</td>
			<td align="left" ><input id="code" name="Code" type="text" class="easyui-validatebox" onchange="check()"/></td>
			<td align="right">��ַӢ�ģ�</td>
			<td align="left"><input id="addren" name="AddressEn" type="text" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">Ӣ�����ƣ�</td>
			<td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:500px" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">��ϵ�绰��</td>
			<td align="left"><input id="tel" name="Tel" type="text" class="easyui-validatebox"/></td>
			<td align="right">��λ���棺</td>
			<td align="left"><input id="fax" name="Fax" type="text" class="easyui-validatebox" /></td>
		</tr>
		<tr>
			
			<td align="right">��ϵ�˺���1��</td>
			<td align="left"><input id="contactortel1" name="ContactorTel1" type="text" class="easyui-validatebox"/></td>
			<td align="right">��ϵ�˺���2��</td>
			<td align="left"><input id="contactortel2" name="ContactorTel2" type="text" /></td>
		</tr>
		<tr>
			
		</tr>
		<tr height="30px">
			<td align="right">��ҵ���ࣺ</td>
			<td align="left">
				<input id="cla" name="Classification" class="easyui-combobox" style="width:145px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
			</td>
			<td align="right">������ҵ��</td>
		<td><select style="width:145px" id="industry" name="Industry" class="easyui-combobox" ></select>
		<input type="hidden" id="industryId" name="IndustryId"/></td>
			<!-- <td align="right">��λ״̬��</td> -->
			<!-- <td align="left">
				<select id="sta" name="Status" class="easyui-combobox" style="width:145px" panelHeight="auto" editable="false">
						<option value='0' >����</option>
						<option value='1'>ע��</option>
				 </select></td> -->
		</tr>
		<tr height="30px">
        	<td align="right">�������У�</td>
			<td align="left"><input id="accBank" name="AccountBank" type="text" class="easyui-validatebox" /></td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
			<td align="left"><input id="acc" name="Account" type="text" class="easyui-validatebox" /></td>
        </tr>
        <tr>
			<td align="right">���ö�ȣ�</td>
			<td align="left"><input id="crdamount" name="CreditAmount" type="text" class="easyui-numberbox" min="0"/></td>
			<td align="right">���������ֵ��</td>
		<td><input class="easyui-numberbox" id="serviceFeeLimitation" name="ServiceFeeLimitation" min="0"/></td>
   		</tr>
		
		<tr>
		
		<td align="right">�ͻ���ֵ�ȼ���</td>
		<td><select style="width:145px" id="customerValueLevel" name="CustomerValueLevel" class="easyui-combobox"  editable="false">
		<option value="1">1��</option>
		<option value="2">2��</option>
		<option value="3">3��</option>
		<option value="4">4��</option>
		<option value="5">5��</option>
		<option value="6">6��</option>
		<option value="7">7��</option>
		<option value="8">8��</option>
		<option value="9">9��</option>
		<option value="10">10��</option>
		
		</select>
		
		</td>
		<td align="right">�ͻ����ࣺ</td>
		<td>
		<select style="width:145px" class="easyui-combobox" id="customerLevel" name="CustomerLevel"  panelHeight="auto" ediable="false">
		<option value="1">VIP�ͻ�</option>
		<option value="2">�ص�ͻ�</option>
		<option value="3">��Ҫ�ͻ�</option>
		<option value="4">һ��ͻ�</option>
		<option value="5">����ͻ�</option>
		</select>
		
		</td >
		</tr>
		<tr>
		
		<td align="right">�䶯���ƣ�</td>
		<td><select style="width:145px" id="trendency" name="Trendency" class="easyui-combobox"  editable="false">
		<option value="1">��������</option>
		<option value="2">��������</option>
		<option value="3">ά����״</option>
		</select></td>
		<td align="right">��ֵ����ֵ��</td>
		<td><input class="easyui-numberbox" min="0" id="outputExpectation" name="OutputExpectation" /></td>
		
		</tr>
		<tr>
		
		</tr>
		<tr>
		<td align="right">�ҳ϶ȣ�</td>
		<td><input name="Loyalty" class="easyui-numberbox" min="0" max="100"></input></td>
		<td align="right">����ȣ�</td>
		<td><input name="Satisfaction" class="easyui-numberbox" min="0" max="100"></input></td>
		</tr>
		<tr height="30px">
			<td align="right">�³�Ҫ��</td>
			<td align="left"  colspan="4"><textarea id="fldema" name="FieldDemands" cols="70" rows="2" ></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">����Ҫ��</td>
			<td align="left"  colspan="4"><textarea id="spcdema" name="SpecialDemands" cols="70" rows="2"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">֤��Ҫ��</td>
			<td align="left"  colspan="4"><textarea id="cerdema" name="CertificateDemands" cols="70" rows="2"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
			<td align="left" colspan="4"><textarea id="rmk" name="Remark" cols="70" rows="2"></textarea> </td>
		</tr>
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">���</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
		</tr>
	</table>
	</form>
	<br />
	<br />


</DIV>
</DIV>
</body>
</html>
