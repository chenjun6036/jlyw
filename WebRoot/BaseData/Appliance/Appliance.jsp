<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>受检器具信息管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
		$(function(){
			$('#Fee').numberbox();
			$('#SRFee').numberbox();
			$('#MRFee').numberbox();
			$('#LRFee').numberbox();
			$('#PromiseDuration').numberbox();
			$('#TestCycle').numberbox();
			$('#frm_appliance').form('validate');
		});
		
		$(function(){
			$('#StandardNameId').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
					}
			});
		});
		
		function cancel(){
			/*$('#StandardNameId').combobox('setValue',"");
			$('#Name').val("");
			$('#NameEn').val("");
			$('#Brief').val("");
			$('#Code').val("");
			$('#Fee').val("");
			$('#SRFee').val("");
			$('#MRFee').val("");
			$('#LRFee').val("");
			$('#PromiseDuration').val("");
			$('#TestCycle').val("");
			$('#Remark').val("");*/
			$('#frm_appliance').form('clear');
		}
		
		function save(){
			$('#frm_appliance').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=1',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					if(result.IsOK)
						cancel();
				}
			});
		}
		
		function getBrief(){
			$('#Brief').val(makePy($('#Name').val()));
		}
		
	</script>
</head>

<body> 

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="受检器具信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	
	<!--<div region="west" style="width:200px; height:1000px; border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="受检器具分类">
		<ul id="tt"></ul>
 	</div>-->

	
	
  	<div  region="center" style="border:0px;">
    	<form id="frm_appliance" method="post">
		<table id="table1" class="easyui-panel" title="受检器具信息录入" style="width:800px; height:400px; padding-top:10px">
			<tr>
				<td align="right">器具名称：</td>
				<td align="left" colspan="3"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            </tr>
            <tr>
            	<td align="right">拼音简码：</td>
				<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				<td align="right">英文名称：</td>
				<td align="left"><input id="NameEn" name="NameEn" type="text" class="easyui-validatebox" required="true"/></td>
			</tr>
			
			<tr>
				<td align="right">器&nbsp;&nbsp;&nbsp;&nbsp;具&nbsp;&nbsp;<br/>标准名称：</td>
				<td align="left"><select id="StandardNameId" name="StandardNameId" class="easyui-combobox" valueField="id" textField="name" style="width:152px" required="true" panelHeight="auto"/></td>
				<td align="right">器具编码：</td>
				<td align="left"><input id="Code" name="Code" type="text" class="easyui-validatebox" required="true"/></td>
			</tr>
			<tr>
				<td align="right">标准费用：</td>
				<td align="left"><input id="Fee" name="Fee" class="easyui-numberbox" />元</td>
				<td align="right">小修费用：</td>
				<td align="left"><input id="SRFee" name="SRFee" class="easyui-numberbox" />元</td>
			</tr>
			<tr>
				<td align="right">中修费用：</td>
				<td align="left"><input id="MRFee" name="MRFee" class="easyui-numberbox" />元</td>
				<td align="right">大修费用：</td>
				<td align="left"><input id="LRFee" name="LRFee" class="easyui-numberbox" />元</td>
			</tr>
			<tr>
				<td align="right">承诺检出期：</td>
				<td align="left"><input id="PromiseDuration" name="PromiseDuration" class="easyui-numberbox" />天</td>
				<td align="right">检定周期：</td>
				<td align="left"><input id="TestCycle" name="TestCycle" class="easyui-numberbox" />月</td>
			</tr>
			<tr>
				<td align="right">认证：</td>
				<td align="left" colspan="3">
                    <input id="cer1" name="cer1" type="checkbox">法定机构授权</input>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="cer2" name="cer2" type="checkbox">实验室认可</input>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="cer3" name="cer3" type="checkbox">计量认证</input>
				</td>
			</tr>
            <tr height="30px">
				<td align="right">状态：</td>
				<td align="left" colspan="3">
					<select id="Status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
						<option value="0">正常</option>
						<option value="1">注销</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="60" rows="3"></textarea></td>
			</tr>
			<tr height="50px">
					<td align="center" colspan="2">
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">添加</a>
					</td>
					<td align="center" colspan="2">
						<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">重置</a>
					</td>
				</tr>
		</table>
        </form>
	</div>
	
	<!--<div region="south" style="border-bottom:0px; border-left:0px; border-right:0px">12342
	</div>
-->
</DIV>
</DIV>
</body>
</html>
