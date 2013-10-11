<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>委托单位信息录入</title>
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
		   				$.messager.alert('提示',result.msg,'info');
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
$(id).keydown(function(e){// 注意此处不要用keypress方法，否则不能禁用　Ctrl+c 与　Ctrl+V,具体原因请自行查找keyPress与keyDown区分，十分重要，请细查
if ($.browser.msie) 
{  // 判断浏览器
   if(((event.keyCode > 47) && (event.keyCode < 58)) || (event.keyCode == 8) ) 
   {// 判断键值 
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
   this.style.imeMode='disabled';   // 禁用输入法,禁止输入中文字符
   }).bind("paste",function(){return false;});
}
/* $(function()
{
//checknum('#crdamount');
//checknum('#outputExpectation');
//checknum('#serviceFeeLimitation');
$('#outputExpectation').keydown(function(e){// 注意此处不要用keypress方法，否则不能禁用　Ctrl+c 与　Ctrl+V,具体原因请自行查找keyPress与keyDown区分，十分重要，请细查
if ($.browser.msie) 
{  // 判断浏览器
   if(((event.keyCode > 47) && (event.keyCode < 58)) || (event.keyCode == 8) ) 
   {// 判断键值 
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
   this.style.imeMode='disabled';   // 禁用输入法,禁止输入中文字符
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
			<jsp:param name="TitleName" value="委托单位详细信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<form id="frm_code" method="post">
	<input id="checkCode" name="CheckCode" type="hidden" />
	</form>

<form id="frm_add_customer" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="新增委托单位">
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：</td>
			<td align="left"  style="width：30%"><input style="width:140px" id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            <td align="right" style="width：20%">拼音简码：</td>
			<td align="left"  style="width：30%"><input style="width:140px" id="brief" name="Brief" type="text" class="easyui-validatebox"/></td>
		</tr>
		<tr>
		<td align="right">付款方式：</td>
		<td><select style="width:145px" id="payVia" name="PayVia" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">现金</option>
		<option value="2">支票</option>
		<option value="3">付汇</option>
		<option value="4">POS机</option>
		<option value="5">其它</option>
		</select></td>
		<td align="right">付款类型：</td>
		<td><select style="width:145px" id="payType" name="PayType" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">检后结账</option>
		<option value="2">周期结账</option>
		<option value="3">预付款</option>
		<option value="4">其它</option>
		</select>
		</td>
		</tr>
		<tr><td align="right">结账周期：</td>
		<td><select style="width:145px" id="accountCycle" name="AccountCycle" class="easyui-combobox" editable="false" required="false">
		<option value="1">一个月</option>
		<option value="2">两个月</option>
		<option value="3">三个月</option>
		<option value="4">四个月</option>
		<option value="5">五个月</option>
		<option value="6">六个月</option>
		<option value="7">七个月</option>
		<option value="8">八个月</option>
		<option value="9">九个月</option>
		<option value="10">十个月</option>
		<option value="11">十一个月</option>
		<option value="12">十二个月</option>
		</select></td>
		<td align="right">联&nbsp;系&nbsp;人：</td>
			<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr height="30px">
			<td align="right">单位类型：</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:145px" required="true" panelHeight="auto" valueField="id" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=29" editable="false">
					
				</select>
			</td>
			<td align="right">单位地址：</td>
			<td align="left"><input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr  height="30px">
			<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
			<td align="left">
				<select id="rid" name="RegionId" class="easyui-combobox" style="width:145px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
			</td>
			<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left" ><input id="zcd" name="ZipCode" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr ><td align="left" boderwidth="2px;" colspan="5" >-------------------------------------------------------------------------------------------------------------</td>
		</tr>
		<tr  height="30px">
			<td align="right">内部联系人：</td>
			<td align="left">
				<select id="insideContactorId" name="InsideContactorId" panelHeight="auto" class="easyui-combobox" style="width:145px"  editable="false" ></select>
			</td>
			<td align="right">角&nbsp;&nbsp;&nbsp;&nbsp;色：</td>
			<td align="left" ><select style="width:145px" id="role" name="Role" type="text" class="easyui-combobox"  panelHeight="auto">
			<option value="1" selected="selected">A</option>
			<option value="2">B</option>
			</select></td>
		</tr>
		
		<tr height="30px">
			
			<td align="right">单位代码：</td>
			<td align="left" ><input id="code" name="Code" type="text" class="easyui-validatebox" onchange="check()"/></td>
			<td align="right">地址英文：</td>
			<td align="left"><input id="addren" name="AddressEn" type="text" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">英文名称：</td>
			<td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:500px" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">联系电话：</td>
			<td align="left"><input id="tel" name="Tel" type="text" class="easyui-validatebox"/></td>
			<td align="right">单位传真：</td>
			<td align="left"><input id="fax" name="Fax" type="text" class="easyui-validatebox" /></td>
		</tr>
		<tr>
			
			<td align="right">联系人号码1：</td>
			<td align="left"><input id="contactortel1" name="ContactorTel1" type="text" class="easyui-validatebox"/></td>
			<td align="right">联系人号码2：</td>
			<td align="left"><input id="contactortel2" name="ContactorTel2" type="text" /></td>
		</tr>
		<tr>
			
		</tr>
		<tr height="30px">
			<td align="right">企业分类：</td>
			<td align="left">
				<input id="cla" name="Classification" class="easyui-combobox" style="width:145px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
			</td>
			<td align="right">所在行业：</td>
		<td><select style="width:145px" id="industry" name="Industry" class="easyui-combobox" ></select>
		<input type="hidden" id="industryId" name="IndustryId"/></td>
			<!-- <td align="right">单位状态：</td> -->
			<!-- <td align="left">
				<select id="sta" name="Status" class="easyui-combobox" style="width:145px" panelHeight="auto" editable="false">
						<option value='0' >正常</option>
						<option value='1'>注销</option>
				 </select></td> -->
		</tr>
		<tr height="30px">
        	<td align="right">开户银行：</td>
			<td align="left"><input id="accBank" name="AccountBank" type="text" class="easyui-validatebox" /></td>
			<td align="right">账&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td align="left"><input id="acc" name="Account" type="text" class="easyui-validatebox" /></td>
        </tr>
        <tr>
			<td align="right">信用额度：</td>
			<td align="left"><input id="crdamount" name="CreditAmount" type="text" class="easyui-numberbox" min="0"/></td>
			<td align="right">服务费用限值：</td>
		<td><input class="easyui-numberbox" id="serviceFeeLimitation" name="ServiceFeeLimitation" min="0"/></td>
   		</tr>
		
		<tr>
		
		<td align="right">客户价值等级：</td>
		<td><select style="width:145px" id="customerValueLevel" name="CustomerValueLevel" class="easyui-combobox"  editable="false">
		<option value="1">1级</option>
		<option value="2">2级</option>
		<option value="3">3级</option>
		<option value="4">4级</option>
		<option value="5">5级</option>
		<option value="6">6级</option>
		<option value="7">7级</option>
		<option value="8">8级</option>
		<option value="9">9级</option>
		<option value="10">10级</option>
		
		</select>
		
		</td>
		<td align="right">客户分类：</td>
		<td>
		<select style="width:145px" class="easyui-combobox" id="customerLevel" name="CustomerLevel"  panelHeight="auto" ediable="false">
		<option value="1">VIP客户</option>
		<option value="2">重点客户</option>
		<option value="3">重要客户</option>
		<option value="4">一般客户</option>
		<option value="5">特殊客户</option>
		</select>
		
		</td >
		</tr>
		<tr>
		
		<td align="right">变动趋势：</td>
		<td><select style="width:145px" id="trendency" name="Trendency" class="easyui-combobox"  editable="false">
		<option value="1">升级可能</option>
		<option value="2">降级可能</option>
		<option value="3">维持现状</option>
		</select></td>
		<td align="right">产值期望值：</td>
		<td><input class="easyui-numberbox" min="0" id="outputExpectation" name="OutputExpectation" /></td>
		
		</tr>
		<tr>
		
		</tr>
		<tr>
		<td align="right">忠诚度：</td>
		<td><input name="Loyalty" class="easyui-numberbox" min="0" max="100"></input></td>
		<td align="right">满意度：</td>
		<td><input name="Satisfaction" class="easyui-numberbox" min="0" max="100"></input></td>
		</tr>
		<tr height="30px">
			<td align="right">下厂要求：</td>
			<td align="left"  colspan="4"><textarea id="fldema" name="FieldDemands" cols="70" rows="2" ></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">特殊要求：</td>
			<td align="left"  colspan="4"><textarea id="spcdema" name="SpecialDemands" cols="70" rows="2"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">证书要求：</td>
			<td align="left"  colspan="4"><textarea id="cerdema" name="CertificateDemands" cols="70" rows="2"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left" colspan="4"><textarea id="rmk" name="Remark" cols="70" rows="2"></textarea> </td>
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
