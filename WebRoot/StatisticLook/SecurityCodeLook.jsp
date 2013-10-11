<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>安全码查询</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
	<script>
	
	
		function searchSecurityCode(){
			
			
			$('#query').form('submit',{
				url: '/jlyw/QueryServlet.do?method=9',
				onSubmit:function(){ return $('#query').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK){
					 	$('#securityCode').val(result.securityCode);
				 	}else{	
						$.messager.alert('提示',result.msg,'info');
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
			<jsp:param name="TitleName" value="证书安全码查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:200px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
         <form id="query" method="post">
			<table width="950px" id="table1">
				<tr height="60px">
					<td width="10%" align="right">证书号：</td>
					<td width="22%" align="left">
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;"  required="true"/>
					</td>
					<td width="60%" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="searchSecurityCode()">查询</a></td>
				</tr>
				<tr height="60px">	
					<td width="10%" align="right">安全码：</td>
					<td width="22%" align="left" colspan="2">
						<input id="securityCode"  name="securityCode" style="width:350px;"/>
					</td>					
				</tr >           				
			</table>
        </form>
		</div>
        <br />
	 
</div>
</DIV>
</DIV>
</body>
</html>
