<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>������������Ϣ����</title>
		<link rel="stylesheet" type="text/css"
			href="../../Inc/Style/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="../../Inc/Style/themes/icon2.css" />

		<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/jquery.easyui.min.js"></script>
		<script>
			function cancel(){
			$('#StandardNameId').val("");
			$('#StandardName').val("");
			$('#Item').val("");
			}
		</script>
</head>

<body class="easyui-layout">
	<div region="center" align="left" >
		<div>
			<h2 align="center" style="width:700px">������������Ϣ����</h2>
		</div>
		<div style="+position:relative;">
			<table style="width:700px; height:200px; padding-top:10px; padding-left:10px;" class="easyui-panel" title="������������Ϣ¼��">
				<tr>
					<td align="right">���߱�׼&nbsp;&nbsp;<br />���Ʊ�ţ�</td>
					<td align="left"><input id="StandardNameId" name="StandardNameId" type="text" /></td>
					<td align="right">���߱�׼&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�ƣ�</td>
					<td align="left"><input id="StandardName" name="StandardName" type="text" /></td>
				</tr>
				<tr>
					<td align="right">�������ݣ�</td>
					<td align="left" colspan="3"><textarea id="Item" name="Item" cols="69" rows="4"></textarea></td>
				</tr>
				<tr height="50px">
					<td align="center" colspan="2">
						<a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)">ȷ��¼��</a>
					</td>
					<td align="center" colspan="2">
						<a href="#" class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
