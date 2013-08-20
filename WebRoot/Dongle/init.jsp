<%@ page contentType="text/html; charset=gb2312" language="java" import="com.jlyw.util.*"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<title>��ʼ��ET299</title>
	<link rel="stylesheet" href="test.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
</head>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ʼ�������" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<!---------------------------------------------------------------------------------------------------------------------> 
	<object classid="clsid:e6bd6993-164f-4277-ae97-5eb4bab56443" id="ET299" style="left:0px; top:0px; width:20px; height:20px"></object>
	<table width="100%" height="50%"><tr><td align="center">
	<form id="form1" name="form1">
		
	<table width="400" height="206" border="0" cellpadding="0" cellspacing="1"  bgcolor="#666666">
	<tr bgcolor="#FFFFFF"> 
		<td width="197" height="40" align="right">ET299 PID��</td>
		<td width="200" height="40" align="center"><input type="text" name="PID" id="PID" size="25"></td>
	</tr>
	<tr bgcolor="#efefef"> 
		<td width="197" height="40" align="right">SO PIN��</td>
		<td height="40" align="center"><input type="text" name="PIN" id="PIN" size="25"></td>
	</tr>
	<tr bgcolor="#FFFFFF"> 
		<td width="197" height="40" align="right">�û�����</td>
		<td height="40" align="center"><input type="text" name="UserName" id="UserName" size="25"></td>
	</tr>
	<tr bgcolor="#efefef"> 
		<td width="197" height="40" align="right">��Կ��</td>
		<td height="40" align="center"><input type="text" name="UserKey" id="UserKey" size="25"></td>
	</tr>
	<tr align="center" bgcolor="efefef"> 
		<td height="40" colspan="2"><input type="button" value="��ʼ��" onClick="Init()"></td>
	</tr>
	</table>
		
	</form>	  
	</td></tr></table>
    </DIV>
    </DIV>
<!---------------------------------------------------------------------------------------------------------------------> 
</body>
<script language="javascript">
	function Init()
	{
		form1.PID.value = <%=SystemCfgUtil.getDonglePID()%>	;	
		form1.PIN.value = <%=SystemCfgUtil.getDonglePIN()%>	;
		if((form1.UserName.value == "") || (form1.UserKey.value == ""))
		{
			alert("�û�������Կ������Ϊ��");
			form1.UserName.focus();
			return;
		}			
		try //�ҵ���
		{
			var pid = form1.PID.value;
			var token_count = 0;
			token_count = ET299.FindToken(pid);
		} 
		catch(err)
		{
			alert("û���ҵ�ET299: " + (err.number & 0x0FFFF).toString(16));
			return;
		}		
		try //����
		{
			ET299.OpenToken(pid, 1);
		}
		catch(err)
		{
			alert("���豸ʧ��: " + (err.number & 0x0FFFF).toString(16));
			return;
		}
		try //��֤�û�
		{
			var pin = form1.PIN.value;
			ET299.VerifyPIN(1, pin)
		}
		catch(err)
		{
			alert("Verify SO PIN Faild: " + (err.number & 0x0FFFF).toString(16));
			return;
		}
		try //��������д���û���
		{
			var username = form1.UserName.value;
			ET299.Write(0, 0, username.length, username);	
		}
		catch(err)
		{
			alert("Write Faild: " + (err.number & 0x0FFFF).toString(16));
			return;
		}
		try //��������д��Կ
		{
			var userkey = form1.UserKey.value;
			var md5key = ET299.Soft_MD5HMAC(0, "1234", userkey);
			ET299.SetKey(1, 1, md5key);
		}
		catch(err)
		{
			alert("SetKeyEx Faild: " + (err.number & 0x0FFFF).toString(16));
			return;
		}
		alert("ET299��ʼ���ɹ������ס�û�������Կ");
	}
</script>
</html>
