<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>

<% 
		String retString = request.getParameter("PrintObj")==null?"[]":request.getParameter("PrintObj");
		JSONArray PrintArray = new JSONArray(retString);
	
		int length=PrintArray.length();
	
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�춨��ǩ</title>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	

<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../WebPrint/printer.js"></script>	
<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../../JScript/json2.js"></script>
<style type="text/css">

</style>
</head>

<body>
	<div style="width:1000px;">
		<p><a href="javascript:PreviewMytable();">��ӡԤ��</a>&nbsp;<a href="javascript:PrintMytable();">ֱ�Ӵ�ӡ</a></p>
	</div>
	<%
				//for(int i=0;i<PrintArray.length();i++){
	%>
	
	<div id="divJD">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<TR height="22px">
			  <TD width="16%"><DIV align="center"><font size="1">��������</font></DIV></TD>
			  <TD width="34%"><DIV align="center" id="ApplianceName1" name="ApplianceName"></DIV></TD>
			  <TD width="16%"><DIV align="center"><font size="1">�ͺŹ��</font></DIV></TD>
			  <TD width="34%"><DIV align="center" id="Model1" name="Model"></DIV></TD>
			</TR>
			<TR height="22px">
				<TD><DIV align="center"><font size="1">���߱��</font></DIV></TD>
				<TD><DIV align="center" id="ApplianceCode1" name="ApplianceCode"></DIV></TD>
				<TD><DIV align="center"><font size="1">�춨����</font></DIV></TD>
				<TD><DIV align="center" id="WorkDate1" name="WorkDate"></DIV></TD>
				
			</TR>
			<TR height="22px">
				<TD><DIV align="center"><font size="1">��&nbsp;��&nbsp;Ա</font></DIV></TD>
				<TD><DIV align="center" id="WorkStaff1" name="WorkStaff"></DIV></TD>
				<TD><DIV align="center"><font size="1">��&nbsp;Ч&nbsp;��</font></DIV></TD>
				<TD><DIV align="center" id="Validity1" name="Validity"></DIV></TD>
			</TR>
			<TR height="22px">
				<TD colspan="4"><DIV align="center" id="CustomerName1" name="CustomerName"> </DIV></TD>
			</TR>
			
		</table>
	</div>
	<div id="divJZ">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<TR height="22px">
			  <TD width="16%"><DIV align="center"><font size="1">��������</font></DIV></TD>
			  <TD width="34%"><DIV align="center" id="ApplianceName2" name="ApplianceName"></DIV></TD>
			  <TD width="16%"><DIV align="center"><font size="1">�ͺŹ��</font></DIV></TD>
			  <TD width="34%"><DIV align="center" id="Model2" name="Model"></DIV></TD>
			</TR>
			<TR height="22px">
				<TD><DIV align="center"><font size="1">���߱��</font></DIV></TD>
				<TD><DIV align="center" id="ApplianceCode2" name="ApplianceCode"> </DIV></TD>
				<TD><DIV align="center"><font size="1">У׼����</font></DIV></TD>
				<TD><DIV align="center" id="WorkDate2" name="WorkDate"> </DIV></TD>
			</TR>
			<TR height="22px">
				<TD><DIV align="center"><font size="1">У&nbsp;׼&nbsp;��</font></DIV></TD>
				<TD><DIV align="center" id="WorkStaff2" name="WorkStaff"> </DIV></TD>
				<TD><DIV align="center"><font size="1">������У��</font></DIV></TD>
				<TD><DIV align="center" id="Validity2" name="Validity"> </DIV></TD>
			</TR>
			<TR height="22px">
				<TD colspan="4"><DIV align="center" id="CustomerName2" name="CustomerName"></DIV></TD>
			</TR>
			
		</table>
	</div>
	<div id="divJC">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<TR height="22px">
			  <TD width="16%"><DIV align="center"><font size="1">��������</font></DIV></TD>
			  <TD width="34%"><DIV align="center" id="ApplianceName3" name="ApplianceName"></DIV></TD>
			  <TD width="16%"><DIV align="center"><font size="1">�ͺŹ��</font></DIV></TD>
			  <TD width="34%"><DIV align="center" id="Model3" name="Model"></DIV></TD>
			</TR>
			<TR height="22px">
				<TD><DIV align="center"><font size="1">���߱��</font></DIV></TD>
				<TD><DIV align="center" id="ApplianceCode3" name="ApplianceCode"> </DIV></TD>
				<TD><DIV align="center"><font size="1">�������</font></DIV></TD>
				<TD><DIV align="center" id="WorkDate1" name="WorkDate"> </DIV></TD>
			</TR>
			<TR height="22px">
				<TD><DIV align="center"><font size="1">��&nbsp;��&nbsp;Ա</font></DIV></TD>
				<TD><DIV align="center" id="WorkStaff3" name="WorkStaff"> </DIV></TD>
				<TD><DIV align="center"><font size="1">��&nbsp;Ч&nbsp;��</font></DIV></TD>
				<TD><DIV align="center" id="Validity3" name="Validity"> </DIV></TD>
			</TR>
			<TR height="22px">
				<TD colspan="4"><DIV align="center" id="CustomerName3" name="CustomerName"></font></DIV></TD>
			</TR>
			
		</table>
	</div>
		
<%
	//}
				
%>
	
	
	<script language="javascript" type="text/javascript">
		var LODOP; //����Ϊȫ�ֱ���
		var length='<%=length%>';
		var printArray=<%=PrintArray%>;
		
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		
		function Mytable(Printobj){
			LODOP.PRINT_INIT("��ӡ�ؼ�������ʾ_Lodop����_�Զ���ֽ��JD");
			LODOP.SET_PRINT_PAGESIZE(1,800,290,"LodopCustomPage");
			
			if(Printobj.WorkType=="�춨"){
				$("#Model1").html("<font size='1'>"+ Printobj.Model +"</font>");
				$("#ApplianceName1").html("<font size='1'>"+ Printobj.ApplianceName +"</font>");
				$("#ApplianceCode1").html("<font size='1'>"+ Printobj.ApplianceCode +"</font>");
				$("#WorkDate1").html("<font size='1'>"+ Printobj.WorkDate +"</font>");
				$("#WorkStaff1").html("<font size='1'>"+ Printobj.WorkStaff +"</font>");
				$("#Validity1").html("<font size='1'>"+ Printobj.Validity +"</font>");
				$("#CustomerName1").html("<font size='1'>"+ Printobj.CustomerName +"</font>");	
				
				LODOP.ADD_PRINT_TABLE(14,0,300,"240mm",document.getElementById("divJD").innerHTML);		
			}else if(Printobj.WorkType=="У׼"){
				$("#Model2").html("<font size='1'>"+ Printobj.Model +"</font>");
				$("#ApplianceName2").html("<font size='1'>"+ Printobj.ApplianceName +"</font>");
				$("#ApplianceCode2").html("<font size='1'>"+ Printobj.ApplianceCode +"</font>");
				$("#WorkDate2").html("<font size='1'>"+ Printobj.WorkDate +"</font>");
				$("#WorkStaff2").html("<font size='1'>"+ Printobj.WorkStaff +"</font>");
				$("#Validity2").html("<font size='1'>"+ Printobj.Validity +"</font>");
				$("#CustomerName2").html("<font size='1'>"+ Printobj.CustomerName +"</font>");	
				
				LODOP.ADD_PRINT_TABLE(14,0,300,"240mm",document.getElementById("divJZ").innerHTML);		
			}else if(Printobj.WorkType=="���"){
				$("#Model3").html("<font size='1'>"+ Printobj.Model +"</font>");
				$("#ApplianceName3").html("<font size='1'>"+ Printobj.ApplianceName +"</font>");
				$("#ApplianceCode3").html("<font size='1'>"+ Printobj.ApplianceCode +"</font>");
				$("#WorkDate3").html("<font size='1'>"+ Printobj.WorkDate +"</font>");
				$("#WorkStaff3").html("<font size='1'>"+ Printobj.WorkStaff +"</font>");
				$("#Validity3").html("<font size='1'>"+ Printobj.Validity +"</font>");
				$("#CustomerName3").html("<font size='1'>"+ Printobj.CustomerName +"</font>");	
				
				LODOP.ADD_PRINT_TABLE(14,0,300,"240mm",document.getElementById("divJC").innerHTML);		
			}
		
			
			LODOP.ADD_PRINT_TEXT(1,130,50,9,"�ϸ�֤");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",8);	
		
		};
		function PreviewMytable(){
			for(var i=0;i<length;i++){
				Mytable(printArray[i]);
				
				//LODOP.SET_PRINTER_INDEXA("TSC TTP-244 Plus");	
				if(!defaultprinter(1)){//��0������ί�е���ӡ�� ��1�������ǩ��ӡ��  Ĭ��ί�е���ӡ��
					LODOP.PREVIEW();
					return false;	
				}	
				LODOP.PRINT();	
			}
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
