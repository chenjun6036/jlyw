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
<%
 int m=0;
 for(int i = 0; i <= jsonArray.length()/25; i++){ 
 	JSONArray aArray = new JSONArray();
	for(int j=0;j<25&&m<jsonArray.length();j++){
		
		JSONObject obj = new JSONObject();
		obj.put("FileId",jsonArray.getJSONObject(m).getString("FileId"));
		aArray.put(obj);
		m++;
	}
	if(aArray!=null&&aArray.length()>0){
%>

<object  classid="clsid:CA8A9780-280D-11CF-A24D-444553540000" width="100px"  height="100px"  border="0">
<param  name="SRC"  value='/jlyw/FileServlet.do?method=1&FileType=102&FileIds=<%=aArray.toString()%>'/> 
</object>
<%
	}
  }
%>
</body>
</html>
