<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ӡ���۵�</title>
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
			JSONObject ret=(JSONObject)request.getSession().getAttribute("QuotationList");	
			String Number=ret.getString("Number");
			String CustomerName=ret.getString("CustomerName");
			String OfferDate=ret.getString("OfferDate");
			String Remark=ret.getString("Remark");
		    int	length = ret.getJSONArray("rows").length();
//			String msg="";
//			if(isok="false")
//			   msg=ret.getString("msg");					         
%> 

<p><a href="javascript:PreviewMytable();">��ӡԤ��</a>&nbsp;<a href="javascript:PrintMytable();">ֱ�Ӵ�ӡ</a></p>
<div id="selectprinterdiv"></div>
<DIV style="LINE-HEIGHT: 20px;"  align=center>
<table style="background-color:#FFFFFF">
<TR>
  <td width="56%" align="right">
	<STRONG>
	<font color="#000000" size="5">���۵�</font>
	</STRONG>
  <td>
  <td width="44%" align="right">
	<font color="#000000" size="2" >No��<%=ret.getString("Number") %></font>
  <td>
</TR>
</table>	
</DIV>

<div id="div1" style="background-color: #FFFFFF; color:#FFFFFF" > 
    
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%" >
  <TBODY >
  <TR>
	 <TD width="60%" align="left"><font color="#000000" size="2">�ͻ����ƣ�<%=ret.getString("CustomerName") %></font></TD>  
     <TD width="20%" align="left"><font color="#000000" size="2">��ϵ�ˣ�<%=ret.getString("Attn") %></font></TD>
    <TD width="18%" align="left"><font color="#000000" size="2">��ϵ�绰��<%=ret.getString("Tel") %></font></TD>
 </TR>

 <TR>
	 <TD width="60%" align="left"><font color="#000000" size="2">�ͻ���ַ��<%=ret.getString("Address") %></font></TD>  
     <TD width="20%" align="left"><font color="#000000" size="2">Email��<%=ret.getString("Email") %></font></TD>
    <TD width="18%" align="left"><font color="#000000" size="2">���棺<%=ret.getString("Fax") %></font></TD>
 </TR>
</TBODY>
</TABLE>
</div>

<div id="div2">
<TABLE border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
<thead >
  <TR height="35px">
    <TD width="3%">
      <DIV align=center><font size="2">���</font></DIV></TD>
    
    <TD width="13%">
      <DIV align=center><font size="2">��������</font></DIV></TD>
    <TD width="9%">
      <DIV align=center><font size="2">�ͺŹ��</font></DIV></TD>
	  <TD width="9%">
      <DIV align=center><font size="2">������Χ</font></DIV></TD>
    <TD width="10%">
      <DIV align=center><font size="2">��ȷ����/׼ȷ�ȵȼ�/����ʲ�</font></DIV></TD>
	
	  
	<TD width="10%">
      <DIV align=center><font size="2">�������</font></DIV></TD>
    <TD width="10%">
      <DIV align=center><font size="2">������</font></DIV></TD>
	<TD width="10%">
      <DIV align=center><font size="2">���쳧</font></DIV></TD> 
	 
    <TD width="4%" >
      <DIV align=center><font size="2">̨����</font></DIV></TD>
    <TD width="8%">
      <DIV align=center><font size="2">����(Ԫ/̨��)</font></DIV></TD>
	  <TD width="8%">
	   <DIV align=center><font size="2">�ܼ���</font></DIV></TD>
	  <TD width="5%">
      <DIV align=center><font size="2">��ע</font></DIV></TD>
    
  </TR>
</thead>      
  <TBODY> 
  
 <% 
	for(int i=0;i<14;i++){
%>						         
       
  <TR height="29px">
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Id") %></font></TD>
   <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("StandardName")%></font></TD>
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Model") %></font></TD>
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Range") %></font></TD>
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Accuracy") %></font></TD>

	
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("AppFactoryCode") %></font></TD>
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("AppManageCode") %></font></TD>
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Manufacturer") %></font></TD>
	
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>

    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Cost") %></font></TD>
	 <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TotalCost") %></font></TD>
	
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Remark") %></font></TD>
    
    
</TR>

<%
 }
 %> 
</TBODY>
</TABLE>
</div>

<div id="div4">
<% 
     if(ret.getJSONArray("rows").length()>14){
	
%>	
<TABLE border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#333333">
<thead >
  <TR height="35px">
    <TD width="3%">
      <DIV align=center><font size="2">���</font></DIV></TD>
    
    <TD width="13%">
      <DIV align=center><font size="2">��������</font></DIV></TD>
    <TD width="9%">
      <DIV align=center><font size="2">�ͺŹ��</font></DIV></TD>
	  <TD width="9%">
      <DIV align=center><font size="2">������Χ</font></DIV></TD>
    <TD width="10%">
      <DIV align=center><font size="2">��ȷ����/׼ȷ�ȵȼ�/����ʲ�</font></DIV></TD>
	
	  
	<TD width="10%">
      <DIV align=center><font size="2">�������</font></DIV></TD>
    <TD width="10%">
      <DIV align=center><font size="2">������</font></DIV></TD>
	<TD width="10%">
      <DIV align=center><font size="2">���쳧</font></DIV></TD> 
	 
    <TD width="4%" >
      <DIV align=center><font size="2">̨����</font></DIV></TD>
    <TD width="8%">
      <DIV align=center><font size="2">����(Ԫ/̨��)</font></DIV></TD>
	  <TD width="8%">
	   <DIV align=center><font size="2">�ܼ���</font></DIV></TD>
	  <TD width="5%">
      <DIV align=center><font size="2">��ע</font></DIV></TD>
    
  </TR>
</thead>      
  <TBODY> 
  <% 
    
	for(int i=14;i<ret.getJSONArray("rows").length();i++){
%>						         
       
  <TR height="29px">
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Id") %></font></TD>
   <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("StandardName")%></font></TD>
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Model") %></font></TD>
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Range") %></font></TD>
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Accuracy") %></font></TD>

	
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("AppFactoryCode") %></font></TD>
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("AppManageCode") %></font></TD>
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Manufacturer") %></font></TD>
	
    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>

    <TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Cost") %></font></TD>
	 <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TotalCost") %></font></TD>
	
	<TD align="center" ><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Remark") %></font></TD>
    
    
</TR>
<%
 }}
 %> 

</TBODY>
  
</TABLE>
</div>

<div id="div3" style="background-color:#FFFFFF">
  <div style="padding-left:20px"><font size="2">ע��1�������۲������ԡ�����������á�</font></div>
  <div style="padding-left:35px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;2������ȷ��ϸ���ṩ�ܼ������ͺŹ��׼ȷ�ȵȼ��ȸ�����Ϣ����Щ��Ϣ�������Ƶ��ύ���ͻ���֤���ϡ�</font></div>
  <div style="padding-left:35px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;3���������߾����춨��У׼�󣬿ͻ��ڱ�����ȡ��Ʒ��֤��ʱ������������޷��á�</font></div>
  <div style="padding-left:35px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;4���ֳ���ⲻ��ӷ��ã������ɳ��ֳ���⣬�ս�ͨ��<%=ret.getString("CarCost") %>Ԫ/�Ρ�</font></div>
  <div style="padding-left:35px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;5��������ַ���������Ͷ���·323��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�绰��0519-86662583&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���棺0519-86692565&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Email��cjs@czjl.net</font></div>
   <div style="padding-left:47px" id="Remarkdiv"><font size=2><%=ret.getString("Remark") %></font></div>
  
 <%
 
   request.getSession().removeAttribute("QuotationList");	
 %>					
</div>

</div>
<script language="javascript" type="text/javascript"> 
	var LODOP; //����Ϊȫ�ֱ���
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
	
	var code='<%=Number%>';
	var Remark='<%=Remark%>';
	
	var OfferDate='<%=OfferDate%>';
	var length='<%=length%>';
//	if(msg.length==0)
//	    console("����"+msg);
	var nowDate = new Date();
    var printdate = nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
		
	function Mytable(){
	
		LODOP.PRINT_INIT("��ӡ�ؼ�������ʾ_Lodop����_��ҳ��ӡ�ۺϱ��");
		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	
		//LODOP.ADD_PRINT_TABLE(150,"1%","98%",400,document.getElementById("div2").innerHTML);
		LODOP.ADD_PRINT_TABLE(150,"1%","98%",500,document.getElementById("div2").innerHTML);
		//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		
		
		
		LODOP.ADD_PRINT_HTM(95,"5%","92%",60,document.getElementById("div1").innerHTML);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
	   
		LODOP.ADD_PRINT_HTM("80%","5%","92%",120,document.getElementById("div3").innerHTML);			
		//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",0);
		if(length>14){
			LODOP.NewPageA();
			LODOP.ADD_PRINT_TABLE(150,"1%","98%",550,document.getElementById("div4").innerHTML);
			//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_HTM(95,"5%","92%",60,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",4);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_BARCODE(45,920,120,30,"EAN128A",code);
			LODOP.SET_PRINT_STYLEA(0,"Color","#000000");
			LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",4);
		
			LODOP.ADD_PRINT_TEXT(75,920,133,30,"No:"+code);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",9);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",4);
			
			LODOP.ADD_PRINT_TEXT(55,460,140,30,"�� �� ��");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",4);
		}
	  //  LODOP.ADD_PRINT_HTM("80%","5%","92%",120,document.getElementById("div3").innerHTML);				
//		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
//		//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);	
				
		LODOP.ADD_PRINT_TEXT(18,700,135,20,"��#ҳ/��&ҳ");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_IMAGE(10,25,115,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' />");
		LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_TEXT(18,450,196,20,"�����м������Լ����о���");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_SHAPE(1,38,23,1040,1,0,2,"#000000");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_BARCODE(45,920,120,30,"EAN128A",code);
		LODOP.SET_PRINT_STYLEA(0,"Color","#000000");
		LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
	
		LODOP.ADD_PRINT_TEXT(75,920,133,30,"No:"+code);
		LODOP.SET_PRINT_STYLEA(0,"FontSize",9);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT(55,460,140,30,"�� �� ��");
		LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT("97%",250,180,20,"�����У����ݹ��й㻯֧��");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		//LODOP.ADD_PRINT_TEXT("93%",450,180,20,"");
		//LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.ADD_PRINT_TEXT("97%",500,196,20,"�˺ţ�1105020909000068197");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_TEXT("97%",630,130,20,"��ӡ���ڣ�"+printdate);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_TEXT("97%",750,180,20,"�������ڣ�"+OfferDate);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		//LODOP.SET_PRINT_STYLEA(0,"Horient",1);		
		LODOP.ADD_PRINT_TEXT("97%",25,196,20,"�����м������Լ����о���");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_SHAPE(1,"96%",23,1040,1,0,2,"#000000");
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
			//if(Remark.length>0){
//				document.getElementById("Remarkdiv").innerHTML="<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;6����ע��" +Remark+ "</font>";
//			}			
       		 PreviewMytable();
        } 
	}
	window.onload = function(){} 
		
</script>

</body>
</html>
