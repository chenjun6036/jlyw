<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>

<script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>	
<script type="text/javascript" src="../WebPrint/printer.js"></script>	

<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="/jlyw/WebPrint/install_lodop.exe"></embed>
</object> 
<script language="javascript" type="text/javascript"> 
   var LODOP; //声明为全局变量
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
	
	var printerdefault=LODOP.GET_PRINTER_NAME(-1);//系统默认打印机

/////////////设置委托单打印机		
	var printerdiv='选择委托单打印机：<select style="width:300px"  type="text" id="selectprinter" name="selectprinter">';
	var count=LODOP.GET_PRINTER_COUNT();
	var defaultpr="";
	//if (LODOP.IS_FILE_EXIST("D:/printer.txt")){
//		defaultpr=LODOP.GET_FILE_TEXT("D:/printer.txt");
//		var length1=defaultpr.length;
//		
//		defaultpr=defaultpr.substring(0,length1-2);
//		
//	}
	defaultpr=getprinter(0);
	for(var j=0;j<=(count-1);j++){
		
		var printername=LODOP.GET_PRINTER_NAME(j);
		
		printerdiv=printerdiv+'<option value="'+printername+'">'+printername+'</option>';

			
	}
	
	//printerdiv=printerdiv+'</select><input type="button" value="保存设置" onClick="saveprinter()"></input>';	
	printerdiv=printerdiv+'</select>';	
	
/////////////设置标签打印机	
	var Labelprinterdiv='选择 标签 打印机：<select style="width:300px"  type="text" id="selectLabelprinter" name="selectLabelprinter">';
	var count1=LODOP.GET_PRINTER_COUNT();
	var defaultpr1="";
	//if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
//		defaultpr1=LODOP.GET_FILE_TEXT("D:/labelprinter.txt");
//		var length2=defaultpr1.length;
//		
//		defaultpr1=defaultpr.substring(0,length2-2);
//		
//	}
	defaultpr1=getprinter(1);
	for(var j=0;j<=(count-1);j++){
		
		var printername=LODOP.GET_PRINTER_NAME(j);
		
		Labelprinterdiv=Labelprinterdiv+'<option value="'+printername+'">'+printername+'</option>';

			
	}
	
	Labelprinterdiv=Labelprinterdiv+'</select><input type="button" value="保存设置" onClick="saveprinter()"></input>';
	
	
	//document.getElementById("selectprinterdiv").innerHTML=printerdiv;
	$(function(){
		 $('#selectprinterdiv').append(printerdiv); 
		 $("#selectprinter option[value='"+defaultpr+"']").attr("selected",true);
		 
		 $('#selectLabelprinterdiv').append(Labelprinterdiv); 
		 $("#selectLabelprinter option[value='"+defaultpr1+"']").attr("selected",true);
		 $("#ZSlabel").html(printerdefault+" (证书打印)"); 
    })
</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="设置委托单打印机" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	     <br />
		    <br />
			   <br />
			      <br />
				     <br />
	
		<div style="padding-left:20%" id="selectprinterdiv" ></div>
		<div style="padding-left:20%" id="selectLabelprinterdiv" ></div>
	
		<div style="padding-left:20%;padding-top:10px"  id="ZSprinterdiv" >系统默认的打印机：<label id="ZSlabel"></label></div>
		
	</DIV>
</DIV>
</body>
</html>
