<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

    <title></title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript">
		function savereg(){
			$('#ff').form('submit',{
				url:'/jlyw/UserServlet.do?method=7',
				onSubmit:function(){
					if($('#pwd').val()!=$('#copy_pwd').val())
					{
						$.messager.alert('��ʾ','�������벻һ�£������䣡','info');
						$('#pwd').val("");
						$('#copy_pwd').val("");
						return false;
					}
					else
						return $(this).form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
		   				alert(result.msg);
		   				if(result.IsOK)
		   					cancel();
				}
			});
		}
		
		function cancel()
		{
			$('#old_pwd').val("");
			$('#pwd').val("");
			$('#copy_pwd').val("");
		}
	</script>
</head>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�޸�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
    <div align="center" style="padding:20px;" bgcolor="#CAD7F7">
 
	
    <form id="ff" method="post">

	<table>
	    <tr>
			<td bgcolor="#CAD7F7">ԭʼ���룺</td>
			<td ><input style="width:155px;" id="old_pwd" type="password" name="old_pwd" class="easyui-validatebox" required="true" ></td>
		</tr>
		<tr>
			<td bgcolor="#CAD7F7">��  ��&nbsp;�룺</td>
			<td ><input style="width:155px;" id="pwd" type="password" name="pwd" class="easyui-validatebox" required="true" ></td>
		</tr>
		<tr>
			<td bgcolor="#CAD7F7">ȷ�����룺</td>
			<td><input style="width:155px;" id="copy_pwd" type="password" name="copy_pwd"class="easyui-validatebox" required="true"></td>
		</tr>

	</table>
	</form>
	</div>

	<div id="dlg-buttons" align="center">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="savereg()">ȷ��</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancel()">����</a>
	</div>


</DIV>
</DIV>
</body>
</html>