<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>打印所选现场检测业务单</title>
<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript" src="../WebPrint/printer.js"></script>	
<style type="text/css">

</style>
</head>

<body>
	<div style="width:1000px;">
		<p><a href="javascript:PreviewMytable();">打印预览</a>&nbsp;<a href="javascript:PrintMytable();">直接打印</a></p>
	</div>
<% 
			JSONObject ret=(JSONObject)request.getSession().getAttribute("AppItemsList");	
			String Code=ret.getString("Code");
		
		    int	length = ret.getJSONArray("rows").length();
//			String msg="";
//			if(isok="false")
//			   msg=ret.getString("msg");					         
%> 
	<DIV style="Line-HEIGHT:20px" align="center">
		<table style="background-color:#FFFFFF">
			<TR>
				<td width="56%" align="right">
					<STRONG>
						<font color="#000000" size="5">现&nbsp;场&nbsp;检&nbsp;测&nbsp;委&nbsp;托&nbsp;书</font>
					</STRONG>
				</td>
				<td width="44%" align="right">
					<font color="#000000" size="2">No:<%=ret.getString("Code") %></font>
				</td>
			</TR>
		</table>
	</DIV>
	
	<div id="div1">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<TR height="20px">
			  <TD width="20%"><DIV align="center"><font size="2">委托单位</font></DIV></TD>
			  <TD width="40%"><DIV align="center"><font size="2"><%=ret.getString("CustomerName") %></font></DIV></TD>
			  <TD width="20%"><DIV align="center"><font size="2">联系部门</font></DIV></TD>
			  <TD width="20%"><DIV align="center"><font size="2"><%=ret.getString("Department") %></font></DIV></TD>
			</TR>
			<TR height="20px">
				<TD><DIV align="center"><font size="2">地&nbsp;&nbsp;&nbsp;&nbsp;址</font></DIV></TD>
				<TD><DIV align="center"><font size="2"><%=ret.getString("Address") %></font></DIV></TD>
				<TD><DIV align="center"><font size="2">联&nbsp;系&nbsp;人</font></DIV></TD>
				<TD><DIV align="center"><font size="2"><%=ret.getString("Contactor") %></font></DIV></TD>
			</TR>
			<TR height="20px">
				<TD><DIV align="center"><font size="2">联系电话</font></DIV></TD>
				<TD><DIV align="center"><font size="2"><%=ret.getString("ContactorTel") %></font></DIV></TD>
				<TD><DIV align="center"><font size="2">邮政编码</font></DIV></TD>
				<TD><DIV align="center"><font size="2"><%=ret.getString("ZipCode") %></font></DIV></TD>
			</TR>
			<TR height="20px">
				<TD ><DIV align="center"><font size="2">现场业务检测时间</font></DIV></TD>
				<TD ><DIV align="center"><font size="2"><%=ret.getString("ExactTime") %></font></DIV></TD>
				<TD ><DIV align="center"><font size="2">检测负责人</font></DIV></TD>
				<TD ><DIV align="center"><font size="2"><%=ret.getString("SiteManager") %></font></DIV></TD>
			</TR>
			<TR>
				<TD colspan="4" style="padding:0px; border:0">
					<table border="1"  cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
						<TBODY border="1">
							<TR height="20px" >
							  <TD width="32%" style="border:1px solid black"><DIV align="center"><font size="2">受检器具(可另附页)</font></DIV></TD>
							  <TD width="8%"><DIV align="center"><font size="2">数&nbsp;&nbsp;量</font></DIV></TD>
							  <TD width="10%"><DIV align="center"><font size="2">性&nbsp;&nbsp;质</font></DIV></TD>
							  <TD width="10%"><DIV align="center"><font size="2">检测费</font></DIV></TD>
							  <TD width="10%"><DIV align="center"><font size="2">修理费</font></DIV></TD>
							  <TD width="10%"><DIV align="center"><font size="2">材料费</font></DIV></TD>
							  <TD width="10%"><DIV align="center"><font size="2">检测人</font></DIV></TD>
							  <TD width="10%"><DIV align="center"><font size="2">备&nbsp;&nbsp;注</font></DIV></TD>
							</TR>

						
							<%
								for(int i=0; i<10;i++){
							%>
							<TR height="40px">
								<TD width="40%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("applianceInfo") %></font></TD>
								<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>
								<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CertType") %></font></TD>
								<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TestCost") %></font></TD>
								<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("RepairCost") %></font></TD>
								<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("MaterialCost") %></font></TD>
								<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("WorkStaff") %></font></TD>
								<TD width="10%" align="center"><font size="2">  </font></TD>
							</TR>
							<%
							}
							%>
							<TR height="20px">
								<TD align="center"><font size="2">其他费用：</font></TD>
								<TD align="center">…</TD>
								<TD align="center">…</TD>
								<TD align="center" colspan="4"></TD>
								<TD align="center">…</TD>
							</TR>
							<TR height="20px">
								<TD align="center"><font size="2">合&nbsp;&nbsp;&nbsp;&nbsp;计：</font></TD>
								<TD align="center">…</TD>
								<TD align="center">…</TD>
								<TD align="center" colspan="4"></TD>
								<TD align="center">…</TD>
							</TR>
						</TBODY>
				</table>
				</TD>
			</TR>
			<TR>
				<TD colspan="4" style="padding-left:10px;border-top:none">
					<table style="width:100%">
						<tr height="20px">
							<td colspan="3"><font size="3">约定事项：</font></td>
						</tr>
						<tr height="20px">
							<td colspan="3"><font size="2">				&nbsp;&nbsp;1、检测单位负责对上述计量器具提供检修服务，并在现场检测后5个工作日内完成证书/报告；当需要调修服务时，委托单位应在备注中说明；</font></td>
						</tr>
						<tr height="20px">
							<td colspan="3"><font size="2">&nbsp;&nbsp;2、检定依据相应器具的国家计量检定规程；校准、检测、测试依据校准规范或产品标准等；委托单位也可在备注栏注明依据的技术文件；若现场检测条件不符合依据文件，应委托单位要求继续工作的，应在原始记录和证书/报告中记录实际环境条件；</font></td>
						</tr>
						<tr height="20px">
							<td colspan="3"><font size="2">&nbsp;&nbsp;3、委托单位应足额支付检修费用；</font></td>
						</tr>
						<tr height="20px">
							<td colspan="3"><font size="2">&nbsp;&nbsp;4、取样地址：□ 常州市武进区鸣新中路16号 □ 常州市劳动西路323号 □ _____________   </font></td>
						</tr>
						<tr height="20px">
							<td colspan="3"><font size="2">&nbsp;&nbsp;5、其他事项：<br/><br/><br/></font></td>
						</tr>
						<tr height="20px">
							<td align="left"><font size="3">检测单位代表签字：</font></td>
							<td align="center"><font size="3">委托单位代表签字：</font></td>
							<td align="right" style="padding-right:5px"><font size="3">&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</font></td>
						</tr>
					</table>
				</TD>
			</TR>
			<TR>
				<TD style="padding-left:10px" colspan="4">
					<table width="100%">
						<tr height="20px">
							<td width="40%" align="left"><font size="3">委托单位对现场检测服务质量的意见：</font></td>
							<td width="20%" align="right"><font size="3">□&nbsp;&nbsp;满意</font></td>
							<td width="18%" align="right"><font size="3">□&nbsp;&nbsp;基本满意</font></td>
							<td width="18%" align="right"><font size="3">□&nbsp;&nbsp;不满意</font></td>
						</tr>
						<tr height="20px">
							<td><font size="3">其他建议：<br/><br/><br/></font></td>
						</tr>
						<tr height="20px">
							<td align="right" colspan="2"><font size="3">委托单位代表签字：</font></td>
							<td align="right" colspan="2"><font size="3">&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</font></td>
						</tr>
					</table>
				</TD>
			</TR>
		</table>
	</div>
	
<div id="div4">
<% 
     if(ret.getJSONArray("rows").length()>10){
	
%>	
		<table border="1"  cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#000000">
			<TBODY border="1">
				<TR height="20px" >
				  <TD width="30%" style="border:1px solid black"><DIV align="center"><font size="2">受检器具(可另附页)</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">数&nbsp;&nbsp;量</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">性&nbsp;&nbsp;质</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">检测费</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">修理费</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">材料费</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">检测人</font></DIV></TD>
				  <TD width="10%"><DIV align="center"><font size="2">备&nbsp;&nbsp;注</font></DIV></TD>
				</TR>

			
				<%
					for(int i=10;i<ret.getJSONArray("rows").length();i++){
				%>
				<TR height="40px">
					<TD width="40%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("applianceInfo") %></font></TD>
					<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("Quantity") %></font></TD>
					<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("CertType") %></font></TD>
					<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("TestCost") %></font></TD>
					<TD width="10%" align="center"><font size="2">  </font></TD>
					<TD width="10%" align="center"><font size="2">   </font></TD>
					<TD width="10%" align="center"><font size="2"><%=ret.getJSONArray("rows").getJSONObject(i).getString("WorkStaff") %></font></TD>
					<TD width="10%" align="center"><font size="2">  </font></TD>
				</TR>
				<%
				}}
				%>
				<TR height="20px">
					<TD align="center"><font size="2">其他费用：</font></TD>
					<TD align="center">…</TD>
					<TD align="center">…</TD>
					<TD align="center" colspan="4"></TD>
					<TD align="center">…</TD>
				</TR>
				<TR height="20px">
					<TD align="center"><font size="2">合&nbsp;&nbsp;&nbsp;&nbsp;计：</font></TD>
					<TD align="center">…</TD>
					<TD align="center">…</TD>
					<TD align="center" colspan="4"></TD>
					<TD align="center">…</TD>
				</TR>
			</TBODY>
	</table>
</div>
 <%
 
   request.getSession().removeAttribute("AppItemsList");	
 %>	
	<div id ="div2" style="background-color:#FFFFFF;height:60px">
		<table style="width:100%">
			<TR valign="top">
				<TD align="left"><font size="2">检测单位：常州市计量测试技术研究所</font></TD>
				<TD align="left"><font size="2">开户行：常州市工行广化支行</font></TD>
				<TD align="left"><font size="2">账号：1105020909000068197</font></TD>
			</TR>
			<TR valign="top">
				<TD align="left"><font size="2">地&nbsp;&nbsp;&nbsp;&nbsp;址：常州市武进区鸣新中路16号</font></TD>
				<TD align="left"><font size="2">查询电话：4008580800</font></TD>
				<TD align="left"><font size="2">业务联系电话：0519-81002519</font></TD>
			</TR>
			<TR valign="top">
				<TD align="left"><font size="2">邮&nbsp;&nbsp;&nbsp;&nbsp;编：213164</font></TD>
				<TD align="left"><font size="2">投诉电话：0519-81002513</font></TD>
				<TD align="left"><font size="2">传真电话：0519-81002098</font></TD>
			</TR>
		</table>
		<!--<div style="padding-left:10px"><font size="2">检测单位：常州市计量测试技术研究所&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开户行：常州市工行广化支行&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账号：1105020909000068197</font>
		</div>
		<div style="padding-left:10px"><font size="2">地&nbsp;&nbsp;&nbsp;&nbsp;址：常州市劳动西路323号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;查询电话：0519-86646491&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业务联系电话：0519-86662583</font>
		</div>
		<div style="padding-left:10px"><font size="2">邮&nbsp;&nbsp;&nbsp;&nbsp;编：213001&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;投诉电话：0519-86995616&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;传真电话：0519-86692565</font>
		</div>-->
	</div>
	
	<script language="javascript" type="text/javascript">
		var LODOP; //声明为全局变量
		var code='<%=Code%>';

		var length='<%=length%>';
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		
		function Mytable(){
			LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_分页打印综合表格");
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
		
			LODOP.ADD_PRINT_TABLE(100,"1%","98%",900,document.getElementById("div1").innerHTML);
			//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
				
			LODOP.ADD_PRINT_HTM("91%","2%","98%",80,document.getElementById("div2").innerHTML);			
			LODOP.SET_PRINT_STYLEA(0,"ItemType",0);
			//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);	
			
			if(length>10){
				LODOP.NewPageA();
				LODOP.ADD_PRINT_TABLE(100,"1%","98%",1050,document.getElementById("div4").innerHTML);
				//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);	
				LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
				
			}
					
			LODOP.ADD_PRINT_TEXT(1090,705,135,20,"第  页/共  页");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);	

			
			LODOP.ADD_PRINT_BARCODE(45,600,120,30,"EAN128A",code);
			LODOP.SET_PRINT_STYLEA(0,"Color","#000000");
			LODOP.SET_PRINT_STYLEA(0,"ShowBarText",0);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
		
			LODOP.ADD_PRINT_TEXT(75,600,133,30,"No:"+ code);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",9);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
			
			LODOP.ADD_PRINT_TEXT(55,250,400,30,"现 场 检 测 委 托 书");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			//LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
			
			LODOP.ADD_PRINT_IMAGE(25,25,55,58,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' />");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
					
		
		};
		function PreviewMytable(){
			Mytable();
			//defaultprinter();	
			LODOP.PREVIEW();	
		};	
		document.onreadystatechange = function(){   
				if(document.readyState=="complete")   
				{   
					//if(Remark.length>0){
		//				document.getElementById("Remarkdiv").innerHTML="<font size="2">&nbsp;&nbsp;&nbsp;&nbsp;6、备注：" +Remark+ "</font>";
		//			}			
					 PreviewMytable();
				} 
		}
	
	</script>
</body>
</html>
