<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>部门产值打印</title>
<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/jlyw/WebPrint/install_lodop.exe"></embed>
</object> 
<script type="text/javascript" src="../../WebPrint/printer.js"></script>	
<style type="text/css">

</style>
</head>

<body>
<% 
			JSONObject ret=(JSONObject)request.getSession().getAttribute("DeptOutputList");	
			         
%>
	<div style="width:1000px">
		<p><a href="javascript:PreviewMytable();">打印预览</a>&nbsp;<a href="javascript:PrintMytable();">直接打印</a></p>
	</div>
	
	<div style="line-height:20px" align="center">
		<table style="background-color:#FFFFFF">
			<TR>
				<td align="center">
					<STRONG>
							<font color="#000000" size="5">全所产值明细表</font>
					</STRONG>
				</td>
			</TR>
		</table>
	</div>
	
	<div id="div1" style="background-color: #FFFFFF; color:#FFFFFF" >
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<TR>
				<TD width="20%" align="left" style="padding-left:20px"><font size="2" color="#000000">统计时间：<%=ret.getString("DateFrom") %></font></TD>
				<TD width="3%" align="center"><font size="2" color="#000000">至</font></TD>
				<TD width="20%" align="left"><font size="2" color="#000000"><%=ret.getString("DateEnd") %></font></TD>
				<TD width="50%" align="right"><font size="2" color="#000000">打印日期：<%=ret.getString("PrintDate") %></font></TD>
			</TR>
			<TR>
				<TD width="23%" align="left" style="padding-left:20px" colspan="2"><font size="2" color="#000000">部门：<%=ret.getString("DeptCode") %>&nbsp;&nbsp;&nbsp;&nbsp;<%=ret.getString("DeptName") %></font></TD>
				
				<TD width="20%" align="left"></TD>
				<TD width="50%"></TD>
			</TR>
			
		</table>
	</div>
	
	<div id="div2">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<thead>
				<tr height="40px">
					
					<td width="18%" align="center"><font size="2">员工姓名</font></td>
					<td width="16%" align="center"><font size="2">工号</font></td>
					<td width="18%" align="center"><font size="2">产值</font></td>
					
					<td width="16%" align="center"><font size="2">台件数</font></td>					
					<td width="16%" align="center"><font size="2">退样数</font></td>
					<td width="16%" align="center"><font size="2">证书数</font></td>
					
				</tr>
			</thead>
			<tbody>
				<%
				
				for(int i=0;i<ret.getJSONArray("rows").length();i++){
			%>
				<tr height="25px">
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("UserName") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("UserJobNum") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TotalFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Withdraw") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Certificate") %></font></td>
					
				</tr>
			<%
				}
				request.getSession().removeAttribute("DeptOutputList");	
			%>
			</tbody>
		</table>
	</div>
	
	<script language="javascript" type="text/javascript">
		var LODOP;
		LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		//LODOP = getLODOP(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));	
		
		function Mytable(){
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			
			LODOP.ADD_PRINT_TABLE(140,"1%","98%",870,document.getElementById("div2").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_HTM(80,"5%","90%",50,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_TEXT(20,700,135,20,"第#页/共&页");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);
			LODOP.ADD_PRINT_IMAGE(10,25,55,58,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' />");
			LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(18,300,196,20,"常州市计量测试技术研究所");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_SHAPE(1,38,83,750,1,0,2,"#000000");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
			LODOP.ADD_PRINT_TEXT(55,310,400,30,"部门产值表");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
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
