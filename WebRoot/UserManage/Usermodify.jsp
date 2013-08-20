<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	
	<script type="text/javascript">
		
		$(function(){
			
		});
		function savereg(){
			$('#ff').form('submit',{
				//url:'form2_proc.php',
				success:function(data){
					$.messager.alert('Info', data, 'info');
					//return $(this).form('validate');
				},onSubmit:function(){
					return $(this).form('validate');
				}
			});
		}
		function cancel()
      {
        form.name.value="";
		//form.password.value=="";    
		form.user.focus();
      }
	</script>
</head>

<body >
   <div align="center" style="padding:20px;" >
    <h2>请修改用户信息</h2>
    <form id="ff" action="/ProcessServlet" method="post">

	<table>
		<tr >
			<td bgcolor="#CAD7F7">姓  &nbsp; &nbsp;名:</td>
			<td ><input style="width:155px;" name="name" class="easyui-validatebox" required="true" /></td>
		</tr>
	
		<tr >
			<td bgcolor="#CAD7F7">所属部门:</td>
			<td ><select id="cc" class="easyui-combobox" name="department" style="width:155px;" required="true">
					<option value="dpt1">财务部</option>
					<option value="dpt2">收发室</option>
					<option value="dpt3">业务管理科</option>
				</select>
		     </td>
		</tr>
	</table>
	</form>
	</div>
	<div id="dlg-buttons" align="center">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="savereg()">确定</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancel()">重置</a>
	</div>
</body>
</html>
