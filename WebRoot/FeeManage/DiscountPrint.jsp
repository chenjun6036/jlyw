<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ۿ۵���ӡ</title>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../WebPrint/printer.js"></script>	


</head>

<body>
<% 
		JSONObject ret=(JSONObject)request.getSession().getAttribute("DiscountList");	
		String CustomerName = ret.getString("CustomerName");	
		String date = ret.getString("date");	
		
		
%> 
	<div style="width:1000px">
		<p><a href="javascript:PreviewMytable();">��ӡԤ��</a>&nbsp;<a href="javascript:PrintMytable();">ֱ�Ӵ�ӡ</a></p>
	</div>
	
	<div style="line-height:20px" align="center">
		<table style="background-color:#FFFFFF">
			<TR>
				<td align="center">
					<STRONG>
						<font color="#000000" size="5">�����м������Լ����о���-�ۿ۵�</font>
					</STRONG>
				</td>
			</TR>
		</table>
	</div>
	
	<div id="div1">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<thead>
				<tr height="40px">
					<td width="9%" align="center"><font size="2">ί�е���</font></td>
					<!--<td width="10%" align="center"><font size="2">��λ����</font></td>-->
					<td width="15%" align="center"><font size="2">��������</font></td>
					<td width="3%" align="center"><font size="2">����</font></td>
					<td width="5%" align="center"><font size="2">�ܼ�</font></td>
					<td width="5%" align="center"><font size="2">�ܼ�<br />�ۿ�</font></td>
					<td width="5%" align="center"><font size="2">����</font></td>
					<td width="5%" align="center"><font size="2">����<br />
				    ��&nbsp;&nbsp;��</font></td>
					<td width="5%" align="center"><font size="2">�����</font></td>
					<td width="5%" align="center"><font size="2">�����<br />
				    ��&nbsp;&nbsp;��</font></td>
					<td width="5%" align="center"><font size="2">���Ϸ�</font></td>
					<td width="5%" align="center"><font size="2">���Ϸ�<br />
				    ��&nbsp;&nbsp;��</font></td>
					<td width="5%" align="center"><font size="2">��ͨ��</font></td>
					<td width="5%" align="center"><font size="2">��ͨ��<br />
				    ��&nbsp;&nbsp;��</font></td>
					<td width="5%" align="center"><font size="2">���Է�</font></td>
					<td width="5%" align="center"><font size="2">���Է�<br />
				    ��&nbsp;&nbsp;��</font></td>
					<td width="5%" align="center"><font size="2">������</font></td>
					<td width="5%" align="center"><font size="2">������<br />
				    ��&nbsp;&nbsp;��</font></td>
				</tr>
			</thead>
			<tbody>
			<%
				for(int i=0;i<ret.getJSONArray("rows").length();i++){
			%>
				<tr height="25px">
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Code") %></font></td>
					<!--<td align="center"><font size="1"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CustomerName") %></font></td>-->
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("ApplianceName") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldTotalFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TotalFee") %></font></td>
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldTestFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TestFee") %></font></td>
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldRepairFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("RepairFee") %></font></td>
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldMaterialFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("MaterialFee") %></font></td>
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldCarFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CarFee") %></font></td>
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldDebugFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("DebugFee") %></font></td>
					
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OldOtherFee") %></font></td>
					<td align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OtherFee") %></font></td>
				</tr>
			<%
				}
				 request.getSession().removeAttribute("DiscountList");	
			%>
			</tbody>
			
			<tfoot>
				<tr height="25px">
					<td colspan="2" align="center"><font size="2">�ϼ�</font></td>
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
	var LODOP; //����Ϊȫ�ֱ���
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
	var customerName='<%=CustomerName%>';
	var date1='<%=date%>';
		function Mytable(){
			//console.info("begin");				
			LODOP.PRINT_INIT("��ӡ�ؼ�������ʾ_Lodop����_��ҳ��ӡ�ۺϱ��");
			LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");	
			//console.info("begin11");		
					
			LODOP.ADD_PRINT_TABLE(80,"1%","98%",550,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_TEXT(20,700,135,20,"��#ҳ/��&ҳ");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);
			LODOP.ADD_PRINT_IMAGE(20,25,115,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp'  />");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
			LODOP.ADD_PRINT_TEXT(18,400,500,20,"�����м������Լ����о���-�ۿ۵�");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_SHAPE(1,730,23,860,1,0,2,"#000000");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
			LODOP.ADD_PRINT_TEXT(720,900,400,30,"Http://www.czjl.net");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
			LODOP.SET_PRINT_STYLEA(000,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Italic",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			
			LODOP.ADD_PRINT_TEXT(60,"3%",400,30,"ί�е�λ:"+customerName);
			 LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(60,"78%",300,30,"�ۿ���׼ʱ��:"+date1);
			 LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			//console.info("end");
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
