<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�����嵥</title>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../WebPrint/printer.js"></script>	
<style type="text/css">


</style>


</head>

<body>
<div style="width:1000px;height:800px">
<% 
			JSONObject ret=(JSONObject)request.getSession().getAttribute("FeeList");	
			String name=ret.getString("DetailListSysUser");	
			String code=ret.getString("DetailListCode");	
			String CustomerName=ret.getString("CustomerName");
//			String msg="";
//			if(isok="false")
//			   msg=ret.getString("msg");					         
%> 

<p><a href="javascript:PreviewMytable();">��ӡԤ��</a>&nbsp;<a href="javascript:PrintMytable();">ֱ�Ӵ�ӡ</a></p>
<div id="selectprinterdiv"></div>
<DIV style="LINE-HEIGHT: 20px"  align=center>
<table>
<TR>
  <td width="56%" align="right">
	<STRONG>
	<font color="#000000" size="5">�����嵥</font>
	</STRONG>
  <td>
  <td width="44%" align="right">
	<font color="#000000" size="2">No��<%=ret.getString("DetailListCode") %></font>
  <td>
</TR>
</table>	
</DIV>

<div id="div1"> 

     
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
  <TR>
     <TD width="74%"><font color="#000000" size="2">��λ���ƣ�<%=ret.getString("CustomerName") %></font></TD>
    
    <TD width="24%"><font color="#000000" size="2">�������ڣ�<%=ret.getString("DetailListDate") %></font>
	</TD>
 </TR>
</TBODY>
</TABLE>
</div>

<div id="div2">
<TABLE border=0 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
<thead >
  <TR>
    <TD width="5%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">���</font></DIV></TD>
    <TD width="9%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">ί�е���</font></DIV></TD>
    <TD width="15%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">��������</font></DIV></TD>
    <TD width="5%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">����</font></DIV></TD>
    <TD width="5%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">����</font></DIV></TD>
    <TD width="7%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">����</font></DIV></TD>
    <TD width="6%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">�����</font></DIV></TD>
	  <TD width="6%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">���Ϸ�</font></DIV></TD>
    <TD width="6%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">��ͨ��</font></DIV></TD>
    <TD width="6%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">���Է�</font></DIV></TD>
    <TD width="6%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">������</font></DIV></TD>
    <TD width="7%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">ʵ�տ�</font></DIV></TD>
    <TD width="9%" style="border-left:0;border-right:0; border-bottom:solid 1px; border-top:solid 1px">
      <DIV align=center><font size="2">���λ��</font></DIV></TD>
  </TR>
</thead>      
  <TBODY> 
 <% 
	for(int i=0;i<ret.getJSONArray("rows").length();i++){
						         
 %>        
  <TR>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Id") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CommissionSheetCode") %></font></TD>
    <TD style="overflow:hidden;" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("ApplianceName").length()>8?ret.getJSONArray("rows").getJSONObject(i).getString("ApplianceName").substring(0, 8):ret.getJSONArray("rows").getJSONObject(i).getString("ApplianceName") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TQuantity") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TestFee") %></font></TD>

    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("RepairFee") %></font></TD>
	<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("MaterialFee") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CarFee") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("DebugFee") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("OtherFee") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TotalFee") %></font></TD>
    <TD align="center"> <font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("FinishLocation") %></font></TD>
    
</TR>
 <%
  }
   request.getSession().removeAttribute("FeeList");	
 %>
<tr>
	<td colspan="13" height="2px" style="height:2px"><div><hr color="black" size="1"/></div></td>
</tr>
</TBODY>
  <tfoot >
  <tr >
    <TD align="center" ></TD>
    <TD align="center"  >
		
	</TD>
    <TD align="center"  >
		
	</TD> 
	<TD align="center" tdata="subSum"  align="center" >
  		<font color="#000000" size="2">###</font>
	</TD>
	<TD align="center" tdata="subSum"  align="center" >
		<font color="#000000" size="2">###</font>
  	</TD>    
	<TD align="center" tdata="subSum" format="#,##0.0" align="center" >
		<font color="#000000" size="2">###</font>
	</TD>
	<TD align="center" tdata="subSum" format="#,##0.0" align="center" >
		<font color="#000000" size="2">###</font>
	</TD>
	<TD align="center" tdata="subSum" format="#,##0.0" align="center"  >
  		<font color="#000000" size="2">###</font>
	</TD>
	<TD align="center" tdata="subSum" format="#,##0.0" align="center"  >
		<font color="#000000" size="2">###</font>
  	</TD>    
	<TD align="center" tdata="subSum" format="#,##0.0" align="center"  >
		<font color="#000000" size="2">###</font>
	</TD>
	<TD align="center" tdata="subSum" format="#,##0.0" align="center"  >
		<font color="#000000" size="2">###</font>
	</TD> 
	<TD align="center" tdata="subSum" format="#,##0.0" align="center"  >
		<font color="#000000" size="2">###</font>
	</TD>
	<TD align="center"  align="center"  >
		
	</TD>   
 </tr>
  </tfoot>
</TABLE>
</div>

<div id="div3">
  <DIV style="LINE-HEIGHT: 20px" align=center>
  	<table border=0 cellspacing=0 cellpadding=1 width="100%" style="border-collapse:collapse" bordercolor="#333333">
	  <tr>
		<td width="8%" align="left"><font size="2">���ƣ�</font></td>
		<td width="5%" align="center"><font size="2"><%=ret.getString("total") %>��</font></td>
		<td width="15%" align="right"><font size="2">����������</font></td>
		<td width="8%" align="left"><font size="2"><%=ret.getString("ApplianceNumber") %></font></td>
		<td width="15%" align="right"><font size="2">�춨�Ѻϼƣ���</font></td>
		<td width="7%" align="left"><font size="2"><%=ret.getString("JDfee") %></font></td>
		<td width="17%" align="right"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;�����Ѻϼƣ���</font></td>
		<td width="8%" align="left"><font size="2"><%=ret.getString("QTfee") %></font></td>
		<td width="12%" align="right"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;�ϼ�:��</font></td>
		<td width="6%" align="left"><font size="2"><%=ret.getString("DetailListTotalFee") %></font></td>
	  </tr>
	</table>
    <table border=0 cellSpacing=0 cellPadding=1 width="100%">
	 	<tr>
			<td width="45%"  align="right"><font size="2">�շѱ�׼��</font></td>
			<td width="18%" align="right" ><font size="2">�ռ۷�[2005]175��&nbsp;&nbsp;</font></td>
			<td width="18%" align="right"><font size="2">��Ʊ���룺</font></td>
			<td width="18%" align="left"><font size="2"><%=ret.getString("DetailListInvoiceCode") %></font></td>
		</tr>
		<tr>
			<td colspan="2"  align="right"><font size="2">���۷�[2007]162��&nbsp;&nbsp;</font></td>
			<td colspan="2"  align="right"></td>
		</tr>
    </table>
  </DIV>
</div>

</div>
<script language="javascript" type="text/javascript"> 
	var LODOP; //����Ϊȫ�ֱ���
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
	var name2='<%=name%>';
	var code='<%=code%>';
	
//	if(msg.length==0)
//	    console("����"+msg);
	var nowDate = new Date();
    var printdate = nowDate.getFullYear()+'/'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'/'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
	
	var printerdiv='ѡ��Ĭ�ϴ�ӡ����<select style="width:155px"  type="text" id="selectprinter" name="selectprinter">';
	var count=LODOP.GET_PRINTER_COUNT();
	for(var j=0;j<=(count-1);j++){
		var printername=LODOP.GET_PRINTER_NAME(j);
		printerdiv=printerdiv+'<option value="'+printername+'">'+printername+'</option>';
	}
	printerdiv=printerdiv+'</select><input type="button" value="��������" onClick="saveprinter()"></input>';	
	
	
	document.getElementById("selectprinterdiv").innerHTML=printerdiv;
	
	function Mytable(){
	
		LODOP.PRINT_INIT("��ӡ�ؼ�������ʾ_Lodop����_��ҳ��ӡ�ۺϱ��");
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
	
		LODOP.ADD_PRINT_TABLE(145,"1%","98%",340,document.getElementById("div2").innerHTML);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
		
		LODOP.ADD_PRINT_HTM(110,"5%","90%",30,document.getElementById("div1").innerHTML);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
			
	    LODOP.ADD_PRINT_HTM(0,"5%","90%",100,document.getElementById("div3").innerHTML);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",0);
		//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);	
				
		LODOP.ADD_PRINT_TEXT(33,660,135,20,"��#ҳ/��&ҳ");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_IMAGE(25,25,117,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp'  />");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
		LODOP.ADD_PRINT_TEXT(33,300,196,20,"�����м������Լ����о���");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_SHAPE(1,53,23,"95%",1,0,2,"#000000");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_BARCODE(60,"75%",120,30,"EAN128A",code);
		LODOP.SET_PRINT_STYLEA(0,"Color","#000000");
		LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
	
		LODOP.ADD_PRINT_TEXT(90,"76%",113,30,"No:"+code);
		LODOP.SET_PRINT_STYLEA(0,"FontSize",9);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT(75,"42%",140,30,"�����嵥");
		LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT(1050,630,130,20,"��ӡ���ڣ�"+printdate);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_TEXT(1050,25,150,20,"�տ��ˣ�"+name2);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_TEXT(1050,300,196,20,"�����м������Լ����о���");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_SHAPE(1,1045,23,"95%",1,0,2,"#000000");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
	};
	function PreviewMytable(){
		Mytable();
		defaultprinter();	
		LODOP.PREVIEW();	
	};	
	function PrintMytable(){
		Mytable();
		if(!defaultprinter()){
			LODOP.PREVIEW();
			return false;	
		}	
		if (LODOP.PRINTA()) {
		 //  alert("�ѷ���ʵ�ʴ�ӡ���"); 
		}else 
		   alert("������ӡ��"); 
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
