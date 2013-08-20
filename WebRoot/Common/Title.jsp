<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%
	String titleName = request.getParameter("TitleName");
	if(titleName==null){
		titleName = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>标题</title>
<script type="text/javascript" src="/jlyw/Inc/JScript/unback.js"></script>
<style type="text/css">
table {
	border-collapse:collapse;
	font-size:12px;
}
th {
	font-weight:normal;
	background-color:#f3f3f3
}
</style>
</head>

<body  leftMargin="0" topMargin="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR vAlign=top>
    <TD style="BACKGROUND-REPEAT: repeat-x" background="/jlyw/images/title/Title2.gif">
	    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	        <TR vAlign=top>
	          <TD height=32 style="padding-left:0px; padding-right:0px" noWrap width="100%"><P style="MARGIN-LEFT: 3px; MARGIN-RIGHT: 3px; margin-top:1px; vertical-align:bottom; FONT-SIZE: 13px;FONT-FAMILY: 宋体; line-height:18px;" ><IMG height=16 src="/jlyw/images/title/TitleFlag.png" width=16 align="absmiddle"> 当前位置：<%=titleName %></P></TD>
	        </TR>
      </TABLE>
    </TD>
    <TD width=10 style="padding-left:0px; padding-right:0px"><IMG height=21 src="/jlyw/images/title/Title3.gif" width=16></TD>
    <TD style="BACKGROUND-REPEAT: repeat-x" vAlign=bottom width="100%" background="/jlyw/images/title/Title4.gif"></TD>
  </TR>
</TABLE>
</body>
</html>
