<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>打印报价单</title>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/jlyw/WebPrint/install_lodop.exe"></embed>
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
//			String msg="";
//			if(isok="false")
//			   msg=ret.getString("msg");					         
%> 

<p><a href="javascript:PreviewMytable();">打印预览</a>&nbsp;<a href="javascript:PrintMytable();">直接打印</a></p>
<div id="selectprinterdiv"></div>
<DIV style="LINE-HEIGHT: 20px"  align=center>
<table>
<TR>
  <td width="56%" align="right">
	<STRONG>
	<font color="#000000" size="5">报价单</font>
	</STRONG>
  <td>
  <td width="44%" align="right">
	<font color="#000000" size="2">No：<%=ret.getString("Number") %></font>
  <td>
</TR>
</table>	
</DIV>

<div id="div1"> 

     
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
  <TR>
     <TD width="44%" align="left"><font color="#000000" size="2">接收方：<%=ret.getString("CustomerName") %></font></TD>
    <TD width="44%" align="right"><font color="#000000" size="2">接收方地址：<%=ret.getString("Address") %></font></TD>
 </TR>
 </TBODY>
</TABLE>
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
 <TR>
	 <TD width="30%" align="left"><font color="#000000" size="2">联系人：<%=ret.getString("Attn") %></font></TD>  
     <TD width="30%" align="center"><font color="#000000" size="2">传真：<%=ret.getString("Fax") %></font></TD>
    <TD width="30%" align="right"><font color="#000000" size="2">电话：<%=ret.getString("Tel") %></font></TD>
 </TR>
</TBODY>
</TABLE>
</div>

<div id="div2">
<TABLE border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#333333">
<thead >
  <TR height="35px">
    <TD width="5%">
      <DIV align=center><font size="2">序号</font></DIV></TD>
    
    <TD width="15%">
      <DIV align=center><font size="2">器具名称</font></DIV></TD>
    <TD width="12%">
      <DIV align=center><font size="2">型号规格</font></DIV></TD>
    <TD width="14%">
      <DIV align=center><font size="2">不确定度/准确度等级/最大允差</font></DIV></TD>
	<TD width="12%">
      <DIV align=center><font size="2">测量范围</font></DIV></TD>
    <TD width="5%" >
      <DIV align=center><font size="2">台件数</font></DIV></TD>
    <TD width="10%">
      <DIV align=center><font size="2">检测费(元/台件)</font></DIV></TD>
	  <TD width="12%">
	   <DIV align=center><font size="2">总检测费</font></DIV></TD>
	  <TD width="7%">
      <DIV align=center><font size="2">备注</font></DIV></TD>
    
  </TR>
</thead>      
  <TBODY> 
 <% 
	for(int i=0;i<ret.getJSONArray("rows").length();i++){
%>						         
       
  <TR height="25px">
    <TD align="center" style="word-wrap:break-word "><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Id") %></font></TD>
   <TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("StandardName")%></font></TD>
    <TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Model") %></font></TD>
    <TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Accuracy") %></font></TD>
	<TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Range") %></font></TD>
    <TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>

    <TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Cost") %></font></TD>
	 <TD align="center" style="overflow: hidden;text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TotalCost") %></font></TD>
	
	<TD align="center" style="overflow: hidden; text-overflow:clip;white-space:nowrap;"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Remark") %></font></TD>
    
    
</TR>
<%
 }
 %> 

</TBODY>
  
</TABLE>
</div>

<div id="div3">
  <div style="padding-left:20px"><font size="2">注：1、本报价不含调试修理及配件费用。</font></div>
  <div style="padding-left:25px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;2、请详细提供检测器具型号规格，精度等级。</font></div>
  <div style="padding-left:25px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;3、计量器具经过鉴定、测试校准后，企业在本所取证书时，需结清各项检修费用，方可取证书。</font></div>
  <div style="padding-left:25px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;4、现场检定不另加费用；本所派车现场检测，收交通费<%=ret.getString("CarCost") %>元/次。</font></div>
  <div style="padding-left:25px"><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;5、本所地址：常州市劳动西路323号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话：0519-86662583&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;传真：86692565&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Email：</font></div>
  
 <%
 
   request.getSession().removeAttribute("QuotationList");	
 %>					
</div>

</div>
<script language="javascript" type="text/javascript"> 
	var LODOP; //声明为全局变量
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
	
	var code='<%=Number%>';
	var OfferDate='<%=OfferDate%>';
//	if(msg.length==0)
//	    console("错误："+msg);
	var nowDate = new Date();
    var printdate = nowDate.getFullYear()+'/'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'/'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
		
	function Mytable(){
	
		LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
		LODOP.SET_PRINT_PAGESIZE(0,0,0,"A4");
	
		LODOP.ADD_PRINT_TABLE(165,"1%","98%",700,document.getElementById("div2").innerHTML);
		//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		
		LODOP.ADD_PRINT_HTM(110,"5%","90%",60,document.getElementById("div1").innerHTML);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
	    LODOP.ADD_PRINT_HTM("83%","5%","90%",100,document.getElementById("div3").innerHTML);			
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);	
				
		LODOP.ADD_PRINT_TEXT(33,660,135,20,"第#页/共&页");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_IMAGE(25,25,115,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' />");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_TEXT(33,300,196,20,"常州市计量测试技术研究所");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_SHAPE(1,53,23,700,1,0,2,"#000000");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_BARCODE(60,"75%",120,30,"EAN128A",code);
		LODOP.SET_PRINT_STYLEA(0,"Color","#000000");
		LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
	
		LODOP.ADD_PRINT_TEXT(90,"75%",133,30,"No:"+code);
		LODOP.SET_PRINT_STYLEA(0,"FontSize",9);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT(70,"42%",140,30,"报 价 单");
		LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT(1020,25,180,20,"开户行：常州工行广化支行");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_TEXT(1020,300,180,20,"账号：1105020909000068197");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.ADD_PRINT_TEXT(1020,570,196,20,"现金 □   支票 □   汇款 □");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_TEXT(1050,630,130,20,"打印日期："+printdate);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_TEXT(1050,360,180,20,"报价日期："+OfferDate);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
	
		LODOP.ADD_PRINT_TEXT(1050,25,196,20,"常州市计量测试技术研究所");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_SHAPE(1,1045,23,700,1,0,2,"#000000");
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
		 //  alert("已发出实际打印命令！"); 
		}else 
		   alert("放弃打印！"); 
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
