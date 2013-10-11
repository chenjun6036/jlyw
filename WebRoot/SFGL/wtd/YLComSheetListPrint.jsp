<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>

<% 
		String retString = request.getParameter("YLCommissionSheetList")==null?"[]":request.getParameter("YLCommissionSheetList");
		JSONArray retArray = new JSONArray(retString);
	
		
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>预留委托单打印</title>
<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../../WebPrint/printer.js"></script>	
<style type="text/css">

</style>
</head>

<body>

	<div style="width:1000px">
		<p><a href="javascript:PreviewMytable();">打印预览</a>&nbsp;<a href="javascript:PrintMytable();">直接打印</a></p>
	</div>
	
	<div style="line-height:20px" align="center">
		<table style="background-color:#FFFFFF">
			<TR>
				<td align="center">
					<STRONG>
						<font color="#000000" size="5">常州市计量测试技术研究所-改费单</font>
					</STRONG>
				</td>
			</TR>
		</table>
	</div>
	
	<div id="div1">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<thead>
				<tr height="25px">
					<td width="7%" align="center"><font size="3">序号</font></td>
					<td width="50%" align="center"><font size="3">委托单位</font></td>
			
					<td width="25%" align="center"><font size="3">委托单号</font></td>
					<td width="18%" align="center"><font size="3">密码</font></td>
					
				</tr>
			</thead>
			<tbody>
				<%
				for(int i=0;i<retArray.length();i++){
			%>
				<tr height="25px">
					<td align="center"><font size="3"><%= i+1 %></font></td>
					<td align="center"><font size="3"></font></td>
					<td align="center"><font size="3"><%=retArray.getJSONObject(i).getString("Code") %></font></td>
					<td align="center"><font size="3"><%=retArray.getJSONObject(i).getString("Pwd") %></font></td>
					
				</tr>
			<%
				}
				
			%>
			</tbody>
			
		</table>
	</div>
	
	<script language="javascript" type="text/javascript">
		var LODOP;
		LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));	
		
		var nowDate = new Date();
        var date1 = nowDate.getFullYear()+'/'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'/'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
		function Mytable(){
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			
			LODOP.ADD_PRINT_TABLE(80,"1%","98%",900,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_TEXT(20,700,135,20,"第#页/共&页");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);
			LODOP.ADD_PRINT_IMAGE(10,25,55,58,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp'/>");
			LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_TEXT(18,150,500,20,"常州市计量测试技术研究所-预留委托单");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_SHAPE(1,1095,23,610,1,0,2,"#000000");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
			LODOP.ADD_PRINT_TEXT(1085,630,400,30,"http://www.czjl.net");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			LODOP.SET_PRINT_STYLEA(000,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Italic",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			
			LODOP.ADD_PRINT_TEXT(60,"78%",300,30,"打印日期:"+date1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		};
		
		function PreviewMytable(){
			Mytable();
			LODOP.PREVIEW();	
		};	
		document.onreadystatechange = function(){   
			if(document.readyState=="complete")   
			{   
				 PreviewMytable();
			} 
		}
	</script>
</body>
</html>
