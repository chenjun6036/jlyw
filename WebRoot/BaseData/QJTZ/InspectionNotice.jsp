<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>打印强检通知书</title>

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
				document.getElementById("Address").innerHTML = "翠竹大道";
				document.getElementById("Name1").innerHTML = "天宁区红梅没请眼镜店";
				document.getElementById("Department").innerHTML = "质量（计量）科  （收）";
				document.getElementById("Notice").innerHTML = "强制检定计量器具检定通知书";
				document.getElementById("Name2").innerHTML = "天宁区红梅没请眼镜店";
				document.getElementById("date1").innerHTML = "本月21日";
				document.getElementById("date2").innerHTML = "下月20日";
				document.getElementById("label3").innerHTML = "常州市计量所业务管理科";
				
				document.getElementById("Tel").innerHTML = "0519-86905563";
			
				document.getElementById("fax").innerHTML = "0519-86905563";
				
				document.getElementById("address2").innerHTML = "常州市劳动西路323号";
				
				document.getElementById("ZipCode2").innerHTML = "213001";

			})
		</script>
		
		<script language="javascript" type="text/javascript">   
        	var LODOP; //声明为全局变量 
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
				LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单一");
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
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label name="Department" id="Department" style="font-size:20px"  value="质量（计量）科  （收）"></label>
     </div>
	 <div>
			&nbsp;&nbsp;<label name="Notice" id="Notice"  style="font-size:30px"  ></label>
     </div>
	 <div>
			<label name="Name2" id="Name2" ></label>
     </div>
	 <div>
			&nbsp;&nbsp;&nbsp;&nbsp;贵单位下列强制检定计量器具将到期，请与<label name="date1" id="date1"></label>至<label name="label2" id="label2"></label><label name="date2" id="date2"></label>间安排检定&nbsp;&nbsp;&nbsp;&nbsp;<label name="label3" id="label3" ></label>
     </div>
	 <div>
			联系电话：<label name="Tel" id="Tel"></label>&nbsp;传真：<label name="fax" id="fax"></label>&nbsp;地址：<label name="address2" id="address2" ></label>&nbsp;邮政编码：</label><label name="ZipCode2" id="ZipCode2"></label>
     </div>
	 <div>
			附：1.《中华人民共和国计量法实施细则》第四十六条：属于强制检定范围的计量器具未按照规定申请检定以<br />及经检定不合格继续使用的，责令其停止使用，可并处一千元一下的罚款。<br />
&nbsp;&nbsp;&nbsp;&nbsp;2.《江苏省计量检定收费规定》第七条：激励狼器具使用单位非因不可抗力原因，不按法定计量检定机构安<br />排的周检计划检定的，鉴定机构在检定收费标准的基础上加收20%的鉴定费。
     </div>
	   <div>
			 <table cellpadding="0" cellspacing="0" border="1" width="750">
				 <%
				   int rows = 3;  // 多少行
				   int cols = 4;  // 多少列
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

<div><a href="javascript:prn1_preview()">打印预览</a></div>
</body>
</html>