<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ӡǿ��֪ͨ��</title>

		<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../Inc/JScript/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
			<script language="javascript" src="../Inc/LodopFuncs.js"></script>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
       <script>
			$(function(){
				document.getElementById("ZipCode").innerHTML = "213001";
				document.getElementById("Address").innerHTML = "������";
				document.getElementById("Name1").innerHTML = "��������÷û���۾���";
				document.getElementById("Department").innerHTML = "��������������  ���գ�";
				document.getElementById("Notice").innerHTML = "ǿ�Ƽ춨�������߼춨֪ͨ��";
				document.getElementById("Name2").innerHTML = "��������÷û���۾���";
				document.getElementById("date1").innerHTML = "����21��";
				document.getElementById("date2").innerHTML = "����20��";
				document.getElementById("label3").innerHTML = "�����м�����ҵ������";
				
				document.getElementById("Tel").innerHTML = "0519-86905563";
			
				document.getElementById("fax").innerHTML = "0519-86905563";
				
				document.getElementById("address2").innerHTML = "�������Ͷ���·323��";
				
				document.getElementById("ZipCode2").innerHTML = "213001";

			})
		</script>
		
		<script language="javascript" type="text/javascript">   
        	var LODOP; //����Ϊȫ�ֱ��� 
			function prn1_preview() {	
				CreateOneFormPage();	
				LODOP.PREVIEW();	
			};
			function prn1_print() {		
				CreateOneFormPage();
				LODOP.PRINT();	
			};
			function prn1_printA() {		
				CreateOneFormPage();
				LODOP.PRINTA(); 	
			};	
			function CreateOneFormPage(){
				LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
				LODOP.PRINT_INIT("��ӡ�ؼ�������ʾ_Lodop����_��һ");
				LODOP.SET_PRINT_STYLE("FontSize",18);
				LODOP.SET_PRINT_STYLE("Bold",1);
				LODOP.ADD_PRINT_HTM(28,10,950,600,document.getElementById("content").innerHTML);
			};	                    
			
</script> 
    
</head>
<body>
<form id="content">
	 <div >
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label name="ZipCode" id="ZipCode"  style="font-size:20px"></label>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label name="Address" id="Address"  style="font-size:20px" ></label>
	 </div>
	 <div>
			<label name="Name1" id="Name1"  style="font-size:20px"  ></label>
     </div>
	 <div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label name="Department" id="Department" style="font-size:20px"  value="��������������  ���գ�"></label>
     </div>
	 <div>
			&nbsp;&nbsp;<label name="Notice" id="Notice"  style="font-size:30px"  ></label>
     </div>
	 <div>
			<label name="Name2" id="Name2" ></label>
     </div>
	 <div>
			&nbsp;&nbsp;&nbsp;&nbsp;��λ����ǿ�Ƽ춨�������߽����ڣ�����<label name="date1" id="date1"></label>��<label name="label2" id="label2"></label><label name="date2" id="date2"></label>�䰲�ż춨&nbsp;&nbsp;&nbsp;&nbsp;<label name="label3" id="label3" ></label>
     </div>
	 <div>
			��ϵ�绰��<label name="Tel" id="Tel"></label>&nbsp;���棺<label name="fax" id="fax"></label>&nbsp;��ַ��<label name="address2" id="address2" ></label>&nbsp;�������룺</label><label name="ZipCode2" id="ZipCode2"></label>
     </div>
	 <div>
			����1.���л����񹲺͹�������ʵʩϸ�򡷵���ʮ����������ǿ�Ƽ춨��Χ�ļ�������δ���չ涨����춨��<br />�����춨���ϸ����ʹ�õģ�������ֹͣʹ�ã��ɲ���һǧԪһ�µķ��<br />
&nbsp;&nbsp;&nbsp;&nbsp;2.������ʡ�����춨�շѹ涨��������������������ʹ�õ�λ���򲻿ɿ���ԭ�򣬲������������춨������<br />�ŵ��ܼ�ƻ��춨�ģ����������ڼ춨�շѱ�׼�Ļ����ϼ���20%�ļ����ѡ�
     </div>
	   <div>
			 <table cellpadding="0" cellspacing="0" border="1" width="750">
				 <%
				   int rows = 3;  // ������
				   int cols = 4;  // ������
				   for(int i = 0; i < rows; i++ ){
				 %>
				  <tr align="center" height="30">
				 <%
					for(int j = 0; j < cols; j++ ){
				 %>
				   <td>[<%=i+1 %>] | [<%=j+1 %>]</td>
				 <%
					}
				   }
				 %> 
 			</table>
      </div>
</form>

<div><a href="javascript:prn1_preview()">��ӡԤ��</a></div>
</body>
</html>