<%@ page contentType="text/html; charset=gb2312" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<title>新建用户</title>
		<link rel="stylesheet" type="text/css"
			href="../Inc/Style/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="../Inc/Style/themes/icon2.css" />

		<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../Inc/JScript/jquery.easyui.min.js"></script>

		<script type="text/javascript">
	$(function() {

	});

	function savereg() {
		$('#ff').form('submit', {
			//url:'form2_proc.php',
			success : function(data) {
				$.messager.alert('Info', data, 'info');
				//return $(this).form('validate');
			},
			onSubmit : function() {
				return $(this).form('validate');
			}
		});
	}
</script>
	</head>

	<body>
		<div align="center" style="padding: 20px;" bgcolor="#CAD7F7">
			<div id="edit" class="easyui-window" title="请输入请增用户信息"
				style="padding: 10px; width: 320; height: 250;" iconCls="icon-edit"
				closed="false" maximizable="false" minimizable="false"
				collapsible="false">

				<form id="ff" action="/ProcessServlet" method="post">

					<table>
						<tr>
							<td >
								姓 &nbsp; &nbsp;名:
							</td>
							<td>
								<select class="easyui-combobox" name="name"
									style="width: 155px;" required="true">
									<option value="name1">
										张三
									</option>
									<option>
										李四
									</option>
									<option>
										王五
									</option>
								</select>
							</td>
						</tr>
						<tr>
							<td >
								用 户 名:
							</td>
							<td>
								<input style="width: 155px;" name="username"
									class="easyui-validatebox" required="true">
							</td>
						</tr>
						<tr>
							<td >
								所属部门:
							</td>
							<td align="left">
								<input class="easyui-combobox" name="type"
									url="combobox_log_type.json" valueField="id" textField="text"
									panelHeight="auto"></input>
							</td>
						</tr>
						<tr>
							<td >
								密 &nbsp; &nbsp;码：
							</td>
							<td>
								<input style="width: 155px;" type="password" name="psd1"
									class="easyui-validatebox" required="true">
							</td>
						</tr>
						<tr>
							<td >
								确认密码：
							</td>
							<td>
								<input style="width: 155px;" type="password"
									class="easyui-validatebox" required="true">
							</td>
						</tr>

					</table>
				</form>
				<div id="dlg-buttons" align="center">
				<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
					onclick=savereg();>确定</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick=cancel();>重置</a>
			   </div>
			</div>
			
		</div>
		
		</div>
	</body>
</html>
