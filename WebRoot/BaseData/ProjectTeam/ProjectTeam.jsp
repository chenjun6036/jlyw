<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>项目组信息录入</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
					
			function save(){
				$('#frm_add_projectteam').form('submit',{
					url:'/jlyw/ProjectTeamServlet.do?method=1',
					onSubmit:function(){return $('#frm_add_projectteam').form('validate');},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('提示',result.msg,'info');
						if(result.IsOK)
							cancel();
					}
				});
			}
						
			function cancel(){
				/*$('#Name').val("");
				$('#Brief').val("");
				$('#Status').combobox('setValue',0);*/
				$('#frm_add_projectteam').form('clear');
			}
			
			function getBrief(){
				$('#Brief').val(makePy($('#Name').val()));
			}
			
			$(function(){
				$('#DepartmentId').combobox({
					onSelect:function(record){
						$('#DepartmentCode').val(record.DeptCode);
					}
				});
				$('#frm_add_projectteam').form('validate');
			});
			
			
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="项目组信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

		 <div style="+position:relative;"> 
		 <form id="frm_add_projectteam" method="post">
			<table style="width:800px; padding-top:10px; padding-bottom:15px" class="easyui-panel" title="项目组信息录入">
				<tr height="30px">
					<td align="right">项目组名称：</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
					<td align="right">拼音简码：</td>
					<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
                	<td align="right">所属部门：</td>
					<td align="left">
						<select id="DepartmentId" name="DepartmentId" class="easyui-combobox" url="/jlyw/DepartmentServlet.do?method=6" valueField="Id" textField="Name" mode="remote" style="width:152px" required="true" panelHeight="150px"></select>
					</td>
                    <td align="right">部门代码：</td>
					<td align="left">
						<input id="DepartmentCode" name="DepartmentCode" class="easyui-validatebox" readonly="readonly"></select>
					</td>
                </tr>
                <tr>
                	<td align="right">项目组代码：</td>
					<td align="left">
						<input id="ProjectTeamCode" name="ProjectTeamCode" class="easyui-validatebox" required="true"></select>
					</td>
					<td align="right">状&nbsp;&nbsp;态：</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
							<option value="0">正常</option>
							<option value="1">注销</option>
						</select>
					</td>
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
