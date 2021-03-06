<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>

<% 
		String retString = request.getParameter("certificateFee")==null?"[]":request.getParameter("certificateFee");
		JSONArray retArray = new JSONArray(retString);
		String CustomerName = retArray.getJSONObject(0).getString("CustomerName");	
		
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>改费单打印</title>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../WebPrint/printer.js"></script>	
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
				<tr height="40px">
					<td width="10%" align="center"><font size="2">证书号</font></td>
				<!--	<td width="13%" align="center"><font size="2">单位名称</font></td>-->
					<td width="15%" align="center"><font size="2">器具名称</font></td>
					<td width="6%" align="center"><font size="2">姓名</font></td>
					<td width="4%" align="center"><font size="2">数量</font></td>
					
					<td width="4%" align="center"><font size="2">总计</font></td>
					<td width="5%" align="center"><font size="2">合计<br />修改</font></td>
					<td width="4%" align="center"><font size="2">检测费</font></td>
					<td width="5%" align="center"><font size="2">检测费<br />
				    修&nbsp;&nbsp;改</font></td>
					<td width="4%" align="center"><font size="2">修理费</font></td>
					<td width="5%" align="center"><font size="2">修理费<br />
				    修&nbsp;&nbsp;改</font></td>
					<td width="4%" align="center"><font size="2">材料费</font></td>
					<td width="5%" align="center"><font size="2">材料费<br />
				    修&nbsp;&nbsp;改</font></td>
					<td width="4%" align="center"><font size="2">交通费</font></td>
					<td width="5%" align="center"><font size="2">交通费<br />
				    修&nbsp;&nbsp;改</font></td>
					<td width="4%" align="center"><font size="2">调试费</font></td>
					<td width="5%" align="center"><font size="2">调试费<br />
				    修&nbsp;&nbsp;改</font></td>
					<td width="4%" align="center"><font size="2">其他费</font></td>
					<td width="5%" align="center"><font size="2">其他费<br />
			      修&nbsp;&nbsp;改</font></td>
				</tr>
			</thead>
			<tbody>
				<%
				for(int i=0;i<retArray.length();i++){
			%>
				<tr height="25px">
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("CertificateCode") %></font></td>
					<!--<td align="center"><font size="1"><%=retArray.getJSONObject(i).getString("CustomerName") %></font></td>-->
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("ApplianceName") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("FeeAlloteeName") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("Quantity") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldTotalFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("TotalFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldTestFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("TestFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldRepairFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("RepairFee") %></font></td>
					
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldMaterialFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("MaterialFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldCarFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("CarFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldDebugFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("DebugFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OldOtherFee") %></font></td>
					<td align="center"><font size="2"><%=retArray.getJSONObject(i).getString("OtherFee") %></font></td>
				</tr>
			<%
				}
				
			%>
			</tbody>
			
			<tfoot>
				<tr height="25px">
					<td colspan="3" align="center"><font size="2">合计</font></td>
					<td align="center" tdata="subSum"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
					<td align="center" tdata="subSum" format="#,##0.0"><font size="2" color="#000000">###</font></td>
				</tr>
			</tfoot>
		</table>
	</div>
	
	<script language="javascript" type="text/javascript">
		var LODOP;
		LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));	
		var customerName='<%=CustomerName%>';
		
		var nowDate = new Date();
        var date1 = nowDate.getFullYear()+'/'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'/'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
		function Mytable(){
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
			LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
			
			LODOP.ADD_PRINT_TABLE(80,"1%","98%",550,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_TEXT(20,700,135,20,"第#页/共&页");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);
			LODOP.ADD_PRINT_IMAGE(20,25,115,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp'/>");
			LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_TEXT(18,400,500,20,"常州市计量测试技术研究所-改费单");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_SHAPE(1,730,23,850,1,0,2,"#000000");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
			LODOP.ADD_PRINT_TEXT(720,900,400,30,"Http://www.czjl.net");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			LODOP.SET_PRINT_STYLEA(000,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Italic",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_TEXT(60,"3%",400,30,"委托单位:"+customerName);
		    LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(60,"78%",300,30,"费用修改日期:"+date1);
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
