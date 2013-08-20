<%@ page contentType="text/html; charset=gb2312" language="java" import="com.jlyw.util.*" errorPage="" %>
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
	
<script type="text/javascript">
	
	
	function save()
	{
		$('#ff').form('submit',{
			url:'/jlyw/SysConfigServlet.do?method=0',
			onSubmit:function(){},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$.messager.alert('提示！','配置成功！','info');
				}else{
					$.messager.alert('错误！',result.msg,'error');
				}
			}
		});
		
	}	
</script>
</head>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="系统运行参数配置" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
    <div align="center" style="padding:20px;" bgcolor="#CAD7F7">

	
    <form id="ff" method="post">

	<table cellspacing="20" bgcolor="#CAD7F7">
	    <tr>
			<td>任务分配规则：</td>
			<td align="left"><select style="width:155px" id="AllotRule" name="AllotRule">
					<option value="0" <%= (SystemCfgUtil.getTaskAllotRule() == 0)?"selected=\"selected\"":"" %> >按业务量</option>
					<option value="1" <%= (SystemCfgUtil.getTaskAllotRule() != 0)?"selected=\"selected\"":"" %> >按产值</option>
				</select>
			</td>
		</tr>
		<tr>
			<td >二次登陆间隔：</td>
			<td align="left"><input style="width:155px"  type="text" id="SecondLoginPeriod" name="SecondLoginPeriod" class="easyui-numberbox" value="<%=SystemCfgUtil.getSecondLoginPeriod() %>" />分钟</td>
		</tr>
        <tr>
			<td >软件狗PID：</td>
			<td align="left"><input style="width:155px"  type="text" id="DonglePID" name="DonglePID" value="<%=SystemCfgUtil.getDonglePID() %>" /></td>
		</tr>
        <tr>
			<td >软件狗PIN：</td>
			<td align="left"><input style="width:155px"  type="text" id="DonglePIN" name="DonglePIN" value="<%=SystemCfgUtil.getDonglePIN() %>" /></td>
		</tr>
        <tr>
			<td >常用特殊字符：</td>
			<td align="left"><textarea id="SpecialLetters" name="SpecialLetters" cols="30" rows="4"><%= SystemCfgUtil.getSpecialLetters() %></textarea></td>
		</tr>
		
	</table>
	</form>
	</div>

	<div id="dlg-buttons" align="center">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="save()">确认修改</a>
	</div>

</DIV>
</DIV>
</body>
</html>