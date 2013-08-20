<%@ page contentType="text/html; charset=gb2312" language="java" import="org.json.me.*" errorPage="" %>
<%
	String cerArray = request.getParameter("CertificationId");
	if(cerArray==null || cerArray.length() == 0){
		cerArray = "[]";
	}
	JSONArray jsonArray = new JSONArray(cerArray);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>打印证书</title>
</head>

<body>
<% for(int i = 0; i < jsonArray.length(); i++){ 
	if(i%9 == 0){
%>
<br />
<%
	}
%>
<object  classid="clsid:CA8A9780-280D-11CF-A24D-444553540000" width="100px"  height="100px"  border="0">
<param  name="SRC"  value="/jlyw/FileServlet.do?method=1&FileType=102&FileId=<%=jsonArray.getJSONObject(i).getString("FileId")%>"/> 
</object>
<%
  }
%>
</body>
</html>
