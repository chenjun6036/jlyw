<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>业务查询</title>
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
			JSONObject ret=(JSONObject)request.getSession().getAttribute("MissionLookList");	
		  
//			String msg="";
//			if(isok="false")
//			   msg=ret.getString("msg");					         
%> 

<p><a href="javascript:PreviewMytable();">打印预览</a>&nbsp;<a href="javascript:PrintMytable();">直接打印</a></p>
<div id="selectprinterdiv"></div>
<DIV style="LINE-HEIGHT: 20px"  align=center>

</DIV>



<div id="div2">
<TABLE border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
<thead >
  <TR height="30px">
    <TD width="4%" >
      <DIV align=center><font size="2">序号</font></DIV></TD>
    <TD width="7%" >
      <DIV align=center><font size="2">委托单号</font></DIV></TD>
	   <TD width="15%" >
      <DIV align=center><font size="2">委托单位</font></DIV></TD>
	  <TD width="6%" >
      <DIV align=center><font size="2">委托日期</font></DIV></TD>
    <TD width="11%" >
      <DIV align=center><font size="2">器具名称</font></DIV></TD>
    <TD width="4%" >
      <DIV align=center><font size="2">数量</font></DIV></TD>
    <TD width="4%" >
      <DIV align=center><font size="2">状态</font></DIV></TD>
	<TD width="6%" >
      <DIV align=center><font size="2">出厂编号</font></DIV></TD>
    <TD width="6%" >
      <DIV align=center><font size="2">管理编号</font></DIV></TD>
    <TD width="7%" >
      <DIV align=center><font size="2">型号规格</font></DIV></TD>
	  <TD width="7%" >
      <DIV align=center><font size="2">测量范围</font></DIV></TD>
    <TD width="6%" >
      <DIV align=center><font size="2">准确度等级</font></DIV></TD>
   
    <TD width="5%" >
      <DIV align=center><font size="2">报告形式</font></DIV></TD>
    <TD width="5%" >
      <DIV align=center><font size="2">派定人</font></DIV></TD>
    <TD width="6%" >
      <DIV align=center><font size="2">存放位置</font></DIV></TD>
  </TR>
</thead>      
  <TBODY> 
 <% 
	for(int i=0;i<ret.getJSONArray("rows").length();i++){
						         
 %>        
  <TR height="35px">
    <TD align="center"><font size="2"><%= i+1 %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Code") %></font></TD>
	<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CustomerName") %></font></TD>
	<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CommissionDate") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("ApplianceName") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>
	<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Status") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("ApplianceCode") %></font></TD>	
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("AppManageCode") %></font></TD>

    
	<TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Model") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Range") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Accuracy") %></font></TD>

    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("ReportType") %></font></TD>
    <TD align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Allotee") %></font></TD>
    <TD align="center"> <font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Location") %></font></TD>
    
</TR>
 <%
  }
   request.getSession().removeAttribute("MissionLookList");	
 %>

</TBODY>
  
</TABLE>
</div>



</div>
<script language="javascript" type="text/javascript"> 
	var LODOP;
	LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	
//	if(msg.length==0)
//	    console("错误："+msg);
	var nowDate = new Date();
    var printdate = nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
	
	
	function Mytable(){
	
		LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	
		LODOP.ADD_PRINT_TABLE(105,"1%","98%",430,document.getElementById("div2").innerHTML);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
		
				
		LODOP.ADD_PRINT_TEXT(33,660,135,20,"第#页/共&页");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_IMAGE(25,25,117,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp'  />");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
		LODOP.ADD_PRINT_TEXT(33,450,196,20,"常州市计量测试技术研究所");	
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		LODOP.ADD_PRINT_SHAPE(1,53,23,"95%",1,0,2,"#000000");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		
		LODOP.ADD_PRINT_TEXT(70,475,140,30,"业务统计");
		LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
		LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
		LODOP.SET_PRINT_STYLEA(0,"Bold",1);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
		LODOP.ADD_PRINT_TEXT("97%",630,130,20,"打印日期："+printdate);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
		LODOP.ADD_PRINT_TEXT("97%",25,170,20,"常州市计量测试技术研究所");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
		
		LODOP.ADD_PRINT_SHAPE(1,"96%",23,"95%",1,0,2,"#000000");
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
	};
	function PreviewMytable(){
		Mytable();
		
		LODOP.PREVIEW();	
	};	
	function PrintMytable(){
		Mytable();
		
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
