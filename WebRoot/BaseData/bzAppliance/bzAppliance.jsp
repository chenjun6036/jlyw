<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>��׼����¼��</title>
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
						$.messager.alert('��ʾ',result.msg,'info');
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
			<jsp:param name="TitleName" value="��׼������ϸ��Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

		 <div style="+position:relative;"> 
		 <form id="frm_add_bzappliance" method="post">
			<table style="width:800px; padding-top:10px; padding-bottom:15px" class="easyui-panel" title="��׼������ϸ��Ϣ¼��">
				<tr height="30px">
					<td align="right">�������ƣ�</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-combobox" required="true" valueField="name" textField="name" panelHeight="150px" style="width:152px"/></td>
					<td align="right">ƴ�����룺</td>
					<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">����ͺţ�</td>
				 	<td align="left"><input id="Model" name="Model" type="text" class="easyui-combobox" valueField="model" textField="model" panelHeight="150px" style="width:152px"/></td>
					<td align="right">������Χ��</td>
					<td align="left"><input id="Range" name="Range" type="text" class="easyui-combobox" valueField="range" textField="range" panelHeight="150px" style="width:152px"/></td>
				</tr>
				<tr height="30px">
					<td align="right">��ȷ���ȣ�</td>
					<td align="left"><input id="Uncertain" name="Uncertain" type="text" class="easyui-combobox" valueField="uncertain" textField="uncertain" panelHeight="150px" style="width:152px"/></td>
					<td align="right">�춨���ڣ�</td>
					<td align="left"><input id="TestCycle" name="TestCycle" type="text" class="easyui-numberbox" required="true" value="12"/></td>
				</tr>
				<tr height="30px">
					<td align="right">�������̣�</td>
					<td align="left"><input id="Manufacturer" name="Manufacturer" type="text" class="easyui-validatebox" required="true"/></td>
                    <td align="right">������ţ�</td>
					<td align="left"><input id="ReleaseNumber" name="ReleaseNumber" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">�������ڣ�</td>
					<td align="left"><input id="ReleaseDate" name="ReleaseDate" type="text" class="easyui-datebox" style="width:152px"/></td>
					<td align="right">����������</td>
					<td align="left"><input id="Num" name="Num" type="text" class="easyui-numberbox" required="true" value="1"/></td>
				</tr>
				<tr height="30px">
					<td align="right">���߼۸�</td>
					<td align="left"><input id="Price" name="Price" type="text" class="easyui-numberbox" required="true"/></td>
					<td align="right">����״̬��</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
							<option value="0">����</option>
							<option value="1">ע��</option>
						</select>
					</td>
				</tr>
				<tr height="30px">
					<td align="right">��&nbsp;��&nbsp;�ˣ�</td>
					<td align="left"><input id="KeeperId" name="KeeperId" type="text" class="easyui-combobox" required="true" valueField="name" textField="name" panelHeight="150px" style="width:152px"/></td>
					<td align="right">���ڱ�ţ�</td>
					<td align="left"><input id="LocaleCode" name="LocaleCode" type="text" class="easyui-numberbox" max="999999" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">�̶��ʲ���ţ�</td>
					<td align="left"><input id="PermanentCode" name="PermanentCode" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">��Ŀ�ƻ���ţ�</td>
					<td align="left"><input id="ProjectCode" name="ProjectCode" type="text" class="easyui-validatebox" max="999999" required="true"/></td>
				</tr>
                <tr>
                    <td align="right">�ܼ��·ݣ�</td>
					<td align="left"><input id="InspectMonth" name="InspectMonth" type="text" class="easyui-numberbox" required="true"/></td>
                    <td align="right">��Ч��Ԥ������<br/>(����Ϊ��λ)��</td>
                    <td align="left"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-numberbox" required="true"/></td>
                </tr>
                <tr height="30px">
					<td align="right">Ԥ���ʽ�</td>
					<td align="left" colspan="3"><input id="Budget" name="Budget" type="text" class="easyui-numberbox" required="true"/></td>
				</tr>
                <tr>
                    <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
                    <td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
                </tr>
				<tr height="50px">
					<td align="center" colspan="2">
						<a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="save()">���</a>
					</td>
					<td align="center" colspan="2">
						<a href="#" class="easyui-linkbutton" icon="icon-reload" name="refresh" href="javascript:void(0)" onclick="cancel()">����</a>
					</td>
				</tr>
			</table>
			</form>
		</div>
</DIV>
</DIV>
</body>
</html>
