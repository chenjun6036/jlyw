<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>标准器具录入</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
        <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
			$(function(){
				
				$('#KeeperId').combobox({
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
				
				$('#Name').combobox({
				//	url:'/jlyw/CustomerServlet.do?method=6',
					onSelect:function(record){
						$('#Model').combobox('reload','/jlyw/StandardApplianceServlet.do?method=7&StdAppId='+record.name);
						$('#Range').combobox('reload','/jlyw/StandardApplianceServlet.do?method=8&StdAppId='+record.name);						
						$('#Uncertain').combobox('reload','/jlyw/StandardApplianceServlet.do?method=9&StdAppId='+record.name);
					},
					onChange:function(newValue, oldValue){
						getBrief();
						var allData = $(this).combobox('getData');
						if(allData != null && allData.length > 0){
							for(var i=0; i<allData.length; i++)
							{
								if(newValue==allData[i].name){
									return false;
								}
							}
						}
						$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=6&QueryName='+newValue);
					}
				});
				
				$('#frm_add_bzappliance').form('validate');
				
			});
			
			function save(){
				$('#frm_add_bzappliance').form('submit',{
					url:'/jlyw/StandardApplianceServlet.do?method=1',
					onSubmit:function(){return $('#frm_add_bzappliance').form('validate');},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('提示',result.msg,'info');
						if(result.IsOK)
							cancel();
					}
				});
			}
						
			function cancel(){
				/*$('#Name').combobox('setValue',"");
				$('#Brief').val("");
				$('#Model').val("");
				$('#Range').val("");
				$('#Uncertain').val("");
				$('#TestCycle').val("");
				$('#SerialNumber').val("");
				$('#Manufacturer').val("");
				$('#ReleaseDate').datebox('setValue',"");
				$('#Num').val("");
				$('#Price').val("");
				$('#Status').combobox('setValue',0);
				$('#KeeperId').val("");
				$('#LocaleCode').val("");
				$('#PermanentCode').val("");
				$('#ProjectCode').val("");
				$('#Budget').val("");*/
				$('#frm_add_bzappliance').form('clear');
			}
			
			function getBrief(){
				$('#Brief').val(makePy($('#Name').combobox('getValue')));
			}	
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="标准器具详细信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

		 <div style="+position:relative;"> 
		 <form id="frm_add_bzappliance" method="post">
			<table style="width:800px; padding-top:10px; padding-bottom:15px" class="easyui-panel" title="标准器具详细信息录入">
				<tr height="30px">
					<td align="right">器具名称：</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-combobox" required="true" valueField="name" textField="name" panelHeight="150px" style="width:152px"/></td>
					<td align="right">拼音简码：</td>
					<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">规格型号：</td>
				 	<td align="left"><input id="Model" name="Model" type="text" class="easyui-combobox" valueField="model" textField="model" panelHeight="150px" style="width:152px"/></td>
					<td align="right">测量范围：</td>
					<td align="left"><input id="Range" name="Range" type="text" class="easyui-combobox" valueField="range" textField="range" panelHeight="150px" style="width:152px"/></td>
				</tr>
				<tr height="30px">
					<td align="right">不确定度：</td>
					<td align="left"><input id="Uncertain" name="Uncertain" type="text" class="easyui-combobox" valueField="uncertain" textField="uncertain" panelHeight="150px" style="width:152px"/></td>
					<td align="right">检定周期：</td>
					<td align="left"><input id="TestCycle" name="TestCycle" type="text" class="easyui-numberbox" required="true" value="12"/></td>
				</tr>
				<tr height="30px">
					<td align="right">生产厂商：</td>
					<td align="left"><input id="Manufacturer" name="Manufacturer" type="text" class="easyui-validatebox" required="true"/></td>
                    <td align="right">出厂编号：</td>
					<td align="left"><input id="ReleaseNumber" name="ReleaseNumber" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">出厂日期：</td>
					<td align="left"><input id="ReleaseDate" name="ReleaseDate" type="text" class="easyui-datebox" style="width:152px"/></td>
					<td align="right">器具数量：</td>
					<td align="left"><input id="Num" name="Num" type="text" class="easyui-numberbox" required="true" value="1"/></td>
				</tr>
				<tr height="30px">
					<td align="right">器具价格：</td>
					<td align="left"><input id="Price" name="Price" type="text" class="easyui-numberbox" required="true"/></td>
					<td align="right">器具状态：</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
							<option value="0">正常</option>
							<option value="1">注销</option>
						</select>
					</td>
				</tr>
				<tr height="30px">
					<td align="right">保&nbsp;管&nbsp;人：</td>
					<td align="left"><input id="KeeperId" name="KeeperId" type="text" class="easyui-combobox" required="true" valueField="name" textField="name" panelHeight="150px" style="width:152px"/></td>
					<td align="right">所内编号：</td>
					<td align="left"><input id="LocaleCode" name="LocaleCode" type="text" class="easyui-numberbox" max="999999" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">固定资产编号：</td>
					<td align="left"><input id="PermanentCode" name="PermanentCode" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">项目计划编号：</td>
					<td align="left"><input id="ProjectCode" name="ProjectCode" type="text" class="easyui-validatebox" max="999999" required="true"/></td>
				</tr>
                <tr>
                    <td align="right">受检月份：</td>
					<td align="left"><input id="InspectMonth" name="InspectMonth" type="text" class="easyui-numberbox" required="true"/></td>
                    <td align="right">有效期预警天数<br/>(以天为单位)：</td>
                    <td align="left"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-numberbox" required="true"/></td>
                </tr>
                <tr height="30px">
					<td align="right">预算资金：</td>
					<td align="left" colspan="3"><input id="Budget" name="Budget" type="text" class="easyui-numberbox" required="true"/></td>
				</tr>
                <tr>
                    <td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
                    <td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
                </tr>
				<tr height="50px">
					<td align="center" colspan="2">
						<a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="save()">添加</a>
					</td>
					<td align="center" colspan="2">
						<a href="#" class="easyui-linkbutton" icon="icon-reload" name="refresh" href="javascript:void(0)" onclick="cancel()">重置</a>
					</td>
				</tr>
			</table>
			</form>
		</div>
</DIV>
</DIV>
</body>
</html>
