<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�깤ί�е���ӡ</title>
<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>	
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object> 
<script type="text/javascript" src="../../WebPrint/printer.js"></script>	
<style type="text/css">

</style>
</head>

<body>
	<div style="width:1000px">
		<p><a href="javascript:PreviewMytable();">��ӡԤ��</a>&nbsp;<a href="javascript:PrintMytable();">ֱ�Ӵ�ӡ</a></p>
	</div>
	
	<div style="line-height:20px" align="center">
		<table style="background-color:#FFFFFF">
			<TR>
				<td align="center">
					<STRONG>
						<font color="#000000" size="5">�깤ί�е�һ����</font>
					</STRONG>
				</td>
			</TR>
		</table>
	</div>
	
	<div id="div1" style="background-color: #FFFFFF; color:#FFFFFF" >
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<TR>
				<TD width="40%" align="left" style="padding-left:20px"><font size="2" color="#000000">ͳ��ʱ�䣺2012-7-9 8:20:12</font></TD>
				<TD align="center"><font size="2" color="#000000">��</font></TD>
				<TD align="left"><font size="2" color="#000000">2012-7-9 8:30:22</font></TD>
			</TR>
			<TR>
				<TD align="left" style="padding-left:20px"><font size="2" color="#000000">�깤�˹��ţ�000890</font></TD>
				<TD width="17%" align="right"><font size="2" color="#000000">�깤��������</font></TD>
				<TD width="43%" align="left"><font size="3" color="#000000">����</font></TD>
			</TR>
		</table>
	</div>
	
	<div id="div2">
		<table border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse;table-layout:fixed;" bordercolor="#333333">
			<THEAD>
				<TR height="20px">
					<TD width="6%" align="center"><font size="2">���</font></TD>
					<TD width="18%" align="center"><font size="2">ί�е���</font></TD>
					<TD width="9%" align="center"><font size="2">��ע</font></TD>
					<TD width="6%" align="center"><font size="2">���</font></TD>
					<TD width="18%" align="center"><font size="2">ί�е���</font></TD>
					<TD width="9%" align="center"><font size="2">��ע</font></TD>
					<TD width="6%" align="center"><font size="2">���</font></TD>
					<TD width="18%" align="center"><font size="2">ί�е���</font></TD>
					<TD width="10%" align="center"><font size="2">��ע</font></TD>
				</TR>
			</THEAD>
			<TBODY>
		<%
			int count = 620;//ί�е��ܸ���
			int size = 50;//ÿ����ʾ������
			int sizetotal =size*3;//ÿһҳ��ʾ������
		
			int tmp = count%sizetotal;
			int p;//���ί�е�ҳ��
			if(tmp == 0)
				p = count/sizetotal;
			else
				p = count/sizetotal+1;
				
			int tmp1 = count%size;
			int column;//���ί�е�����
			if(tmp1==0)
				column = count/size;
			else
				column = count/size+1;
			
			for(int i=0;i<p;i++){ //i--��ǰҳ
				for(int j=0;j<size;j++){//j--��ǰ��
				%>
					<TR height="18px">
				<%
					for(int m=0;m<3;m++){//m--��ǰ��
					if((i*sizetotal+m*size+j)<count){
				%>
						<TD align="center"><font size="2"><%=(i*sizetotal+m*size+j+1)%></font></TD>
						<TD align="center"><font size="2">uuuu</font></TD>
						<TD align="center"><font size="2">uuuuu</font></TD>
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
			}
		%>
			</TBODY>
		</table>
	</div>
	
	<script language="javascript" type="text/javascript">
		var LODOP;
		LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
		//LODOP = getLODOP(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));	
		
		function Mytable(){
			LODOP.PRINT_INIT("��ӡ�ؼ�������ʾ_Lodop����_��ҳ��ӡ�ۺϱ���");
			LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			
			LODOP.ADD_PRINT_TABLE(150,"2%","96%",900,document.getElementById("div2").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_HTM(85,"5%","90%",80,document.getElementById("div1").innerHTML);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",1);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			
			LODOP.ADD_PRINT_TEXT(20,700,135,20,"��#ҳ/��&ҳ");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",2);
			LODOP.SET_PRINT_STYLEA(0,"Horient",1);
			LODOP.ADD_PRINT_IMAGE(10,25,115,25,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' />");
			LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_TEXT(18,140,196,20,"�����м������Լ����о���");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
			LODOP.ADD_PRINT_SHAPE(1,38,23,750,1,0,2,"#000000");
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
			LODOP.ADD_PRINT_TEXT(55,300,400,30,"�깤ί�е�һ����");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#000000");
			LODOP.SET_PRINT_STYLEA(0,"Bold",1);
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		};
		
		function PreviewMytable(){
			Mytable();
			LODOP.PREVIEW();	
		};	
	</script>
</body>
</html>