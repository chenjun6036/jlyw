<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>完工委托单打印</title>
<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../../WebPrint/printer.js"></script>	
<style type="text/css">

</style>
</head>

<body>
<% 
			JSONObject ret=(JSONObject)request.getSession().getAttribute("WGCSList");	
						         
%> 
	<div style="width:1000px">
		<p><a href="javascript:PreviewMytable();">打印预览</a>&nbsp;<a href="javascript:PrintMytable();">直接打印</a></p>
	</div>
	
	<div style="line-height:20px" align="center">
		<table style="background-color:#FFFFFF">
			<TR>
				<td align="center">
					<STRONG>
						<font color="#000000" size="5">完工委托单一览表</font>
					</STRONG>
				</td>
			</TR>
		</table>
	</div>
	
	<div id="div1" style="background-color: #FFFFFF; color:#FFFFFF" >
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<TR>
				<TD width="40%" align="left" style="padding-left:20px"><font size="2" color="#000000">统计时间：<%=ret.getString("DateFrom") %></font></TD>
				<TD align="center"><font size="2" color="#000000">至</font></TD>
				<TD align="left"><font size="2" color="#000000"><%=ret.getString("DateEnd") %></font></TD>
			</TR>
			<TR>
				<TD align="left" style="padding-left:20px"><font size="2" color="#000000">完工人工号：<%=ret.getString("UserId") %></font></TD>
				<TD width="17%" align="right"><font size="2" color="#000000">完工人姓名：</font></TD>
				<TD width="43%" align="left"><font size="3" color="#000000"><%=ret.getString("UserName") %></font></TD>
			</TR>
		</table>
	</div>
	
	<div id="div2">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#333333">
			<THEAD>
				<TR height="20px">
					<TD width="6%" align="center"><font size="2">序号</font></TD>
					<TD width="18%" align="center"><font size="2">委托单号</font></TD>
					<TD width="9%" align="center"><font size="2">备注</font></TD>
					<TD width="6%" align="center"><font size="2">序号</font></TD>
					<TD width="18%" align="center"><font size="2">委托单号</font></TD>
					<TD width="9%" align="center"><font size="2">备注</font></TD>
					<TD width="6%" align="center"><font size="2">序号</font></TD>
					<TD width="18%" align="center"><font size="2">委托单号</font></TD>
					<TD width="10%" align="center"><font size="2">备注</font></TD>
				</TR>
			</THEAD>
			<TBODY>
		<%
			int count = ret.getJSONArray("rows").length();//委托单总个数
			int size = 48;//每列显示的行数
			int sizetotal =size*3;//每一页显示的行数
		
			int tmp = count%sizetotal;
			int p;//获得委托单页数
			if(tmp == 0)
				p = count/sizetotal;
			else
				p = count/sizetotal+1;
				
			int tmp1 = count%size;
			int column;//获得委托单列数
			if(tmp1==0)
				column = count/size;
			else
				column = count/size+1;
			
			for(int i=0;i<p;i++){ //i--当前页
				for(int j=0;j<size;j++){//j--当前行
				%>
					<TR height="18px">
				<%
					for(int m=0;m<3;m++){//m--当前列
					if((i*sizetotal+m*size+j)<count){
				%>
						<TD align="center"><font size="2"><%=(i*sizetotal+m*size+j+1)%></font></TD>
						<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i*sizetotal+m*size+j).getString("Code") %></font></TD>
						<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i*sizetotal+m*size+j).getString("Remark") %></font></TD>
				<%}else{%>
						<TD align="center"><font size="2"></font></TD>
						<TD align="center"><font size="2"></font></TD>
						<TD align="center"><font size="2"></font></TD>
				<%}
				}
			%>
								
					</TR>
			<%
				}
			} request.getSession().removeAttribute("WGCSList");	
		%>
			</TBODY>
		</table>
	</div>
	
	<script language="javascript" type="text/javascript">
		var LODOP;
		LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		//LODOP = getLODOP(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));	
		var nowDate = new Date();
    	var printdate = nowDate.getFullYear()+'/'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'/'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
		function Mytable(){
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			
			LODOP.ADD_PRINT_TABLE(140,"2%","96%",900,document.getElementById("div2").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_HTM(85,"5%","90%",80,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_TEXT(20,700,135,20,"第#页/共&页");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);
			LODOP.ADD_PRINT_IMAGE(10,25,115,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' width='115px' height='25px' />");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(18,140,196,20,"常州市计量测试技术研究所");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_SHAPE(1,38,23,750,1,0,2,"#000000");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
			LODOP.ADD_PRINT_TEXT(55,300,400,30,"完工委托单一览表");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_TEXT(1050,630,130,20,"打印日期："+printdate);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",0);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
			LODOP.ADD_PRINT_TEXT(1050,25,150,20,"接收人：");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",0);	
			LODOP.ADD_PRINT_TEXT(1050,300,196,20,"收发人");	
			LODOP.SET_PRINT_STYLEA(0,"ItemType",0);	
			
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
