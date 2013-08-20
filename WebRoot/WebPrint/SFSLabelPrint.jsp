<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>打印委托单</title>
<script language="javascript" src="LodopFuncs.js"></script>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
</head>

<body>

<h2 align="center"><b><font color="#009999">打印委托单</font></b></h2>
<a href="JavaScript:Preview1()">打印预览</a>        
<a href="javascript:Design1()">打印设计</a>
<a href="javascript:Preview2()">打印预览</a>        
<a href="JavaScript:Setup2()">打印维护</a>

<a href="JavaScript:RealPrint()">选择打印</a>
<script language="javascript" type="text/javascript">
        var LODOP; //声明为全局变量   
	function PreviewSFS() {		
		CreateFullBill();
	  	LODOP.PREVIEW();		
	};
	
	function RealPrintSFS() {		
		CreateFullBill();
		if (LODOP.PRINTA()) 
		   alert("已发出实际打印命令！"); 
		else 
		   alert("放弃打印！"); 
	};	

function CreateFullBill() {
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  

	LODOP.PRINT_INITA(11,11,302,110,"打印控件功能演示_Lodop功能_自定义纸张SFS");
	LODOP.SET_PRINT_PAGESIZE(1,800,285,"LodopCustomPage");
	LODOP.ADD_PRINT_BARCODE(22,14,150,42,"128A","123456789012");
	LODOP.ADD_PRINT_IMAGE(-7,7,65,14,"<img border='0' src='/jlyw/WebPrint/CZJL_Black_2.bmp' />");
	LODOP.SET_PRINT_STYLEA(0,"Stretch",1);	
	LODOP.ADD_PRINT_TEXT(-7,72,166,14,"常州市计量测试技术研究所");
	LODOP.SET_PRINT_STYLEA(0,"FontSize",10);
	LODOP.ADD_PRINT_TEXT(10,217,46,20,"待检");
	LODOP.ADD_PRINT_TEXT(10,190,25,20,"□");
	LODOP.ADD_PRINT_TEXT(10,190,25,20,"√");
	LODOP.ADD_PRINT_TEXT(24,190,25,20,"□");
	LODOP.ADD_PRINT_TEXT(25,217,46,20,"已检");
	LODOP.ADD_PRINT_TEXT(40,190,25,20,"□\n");
	LODOP.ADD_PRINT_TEXT(40,217,46,20,"退样");
	LODOP.ADD_PRINT_TEXT(55,188,45,20,"工号：");
	LODOP.ADD_PRINT_TEXT(55,219,65,20,"000049");
	LODOP.ADD_PRINT_TEXT(70,2,72,20,"器具名称：");
	LODOP.ADD_PRINT_TEXT(70,59,199,20,"XXXXX计量器具一般压力表大舍大得");
	LODOP.ADD_PRINT_TEXT(86,59,199,20,"XXXXX计量器具一般压力表大舍大得\n\n");
	LODOP.ADD_PRINT_TEXT(86,2,72,20,"证书名称");

};	

</script> 
</body>
</html>