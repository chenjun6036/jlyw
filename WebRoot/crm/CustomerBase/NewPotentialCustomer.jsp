<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>潜在客户信息录入</title>
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
		function cancel(){
			$('#frm_add_customer').form('clear');
		}
		
		function savereg(){
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/CrmServlet.do?method=12',
				onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOk)
		   				{
		   					cancel();
		   				}
		   		 }
			});
		}
		
		 function getBrief(){
			$('#brief').val(makePy($('#name').val()));
		} 
		
		
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="潜在客户信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	
	</div>


<form id="frm_add_customer" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="新增潜在客户">
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：</td>
			<td align="left"  style="width：30%">
			<input id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>		
			<td align="right">单位地址：</td>
			<td align="left">
			<input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/>
			</td>
			<td align="left"  style="width：30%">
			<input type="hidden" id="brief" name="Brief" type="text" class="easyui-validatebox" />
			</td>
		</tr>
				<tr height="30px">
			
			<td align="right">合作意向：</td>
			<td align="left">
			<select id="cooperationIntension" name="CooperationIntension" class="easyui-combobox" style="width:152px"  panelHeight="auto"  required="true" editable="false" >
				<option value="1">明确合作意向</option>
				<option value="2">初步联系、有意向 </option>
				<option value="3">观望、意向不明确</option>
				<option value="4">明显拒绝</option>
				</select>
				</td>
				<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
			<td align="left">
				<select id="rid" name="RegionId" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
			</td>
		</tr>
		<tr height="30px" >
		<td colspan="4">---------------------------------------------------------------------------------------------------------</td>
		</tr>
		<tr height="30px">
			<td align="right">英文名称：</td>
			<td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:485px" class="easyui-validatebox" /></td>
		</tr>

		<tr height="30px">
			<td align="right">所在行业：</td>
			<td align="left">
				<input id="industry" name="Industry" class="easyui-combobox" style="width:152px" panelHeight="auto" mode="remote">
				</input><input id="industryId" name="IndustryId" type="hidden"/>
			</td>
			<td align="right">来&nbsp;&nbsp;&nbsp;&nbsp;源：</td>
			<td align="left">
				<select id="from" name="From" class="easyui-combobox" style="width:152px"  panelHeight="auto" editable="false" >
				<option value="1">电话</option>
				<option value="2">传真</option>
				<option value="3">来访</option>
				<option value="4">其它</option>
				</select>
			</td>
		</tr>
		<tr height="30px">
		<td align="right">现行服务机构：</td>
		<td><input id="facilitatingAgency" name="FacilitatingAgency"/></td>
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
