	
	
	String.prototype.Trim = function() { 
		return this.replace(/(^\s*)|(\s*$)/g, ""); 
	} //去除空格,回车 
	String.prototype.LTrim = function() { 
		return this.replace(/(^\s*)/g, ""); //删除字符串左边的空格回车 
	} 
	String.prototype.RTrim = function() { 
		return this.replace(/(\s*$)/g, ""); 
	} 
	function saveprinter(){

		if (LODOP.IS_FILE_EXIST("D:/printer.txt")){
			var selectprinter=document.getElementById("selectprinter");  			
			var selectvalue = selectprinter.options[selectprinter.selectedIndex].text;
			
			var selectprinter1=document.getElementById("selectLabelprinter");   
			var selectlabelvalue = selectprinter1.options[selectprinter1.selectedIndex].text;
			var strResult=LODOP.WRITE_FILE_TEXT(0,"D:/printer.txt",selectvalue.Trim() +";;"+ selectlabelvalue.Trim());
			if (strResult=="ok")
				alert("D:/printer.txt写入成功！");
			else 
				alert(strResult);
			
						
		}
		else alert("D:/printer.txt文件不存在！");
		
	};
	
	function getprinter(i){
		var defaultp="";
		if (LODOP.IS_FILE_EXIST("D:/printer.txt")){
			defaultp=LODOP.GET_FILE_TEXT("D:/printer.txt");
		}
		else{
		  
			return "";
		}
		var printers = new Array();
		if(defaultp.length>0){
			printers = defaultp.split(";;");
			
		}
		else{
			alert("D:/printer.txt文件内容为空！");
			return "";
		}
		return printers[i].Trim();
	}
	
	function defaultprinter(i){//-1默认打印机   0委托单打印机   1标签打印机  null委托单打印机
		
		var printername=getprinter(0);
		if(i!=null){
			if(i < 0){//系统默认打印机
				if(LODOP.SET_PRINTER_INDEXA(-1)){//SET_PRINTER_INDEX(oIndexOrName);按序号或名称指定打印机，选定后禁止手工重选；
//SET_PRINTER_INDEXA(IndexorName);按序号或名称指定打印机，选定后允许手工重选；

				    
					return true;
				}else{
					
					return false;
				}
			}else{
				printername=getprinter(i);	
				
			}
		}
	
		if(printername.length==0){
			alert("D:/printer.txt不存在或者文件内容为空,！");
			return false;
		}else{
			if(LODOP.SET_PRINTER_INDEXA(printername)){//SET_PRINTER_INDEX(oIndexOrName);按序号或名称指定打印机，选定后禁止手工重选；
//SET_PRINTER_INDEXA(IndexorName);按序号或名称指定打印机，选定后允许手工重选；

				//alert("设置成功！");
				return true;
			}else{
				return false;
			}
		}
	
	};	
	
	
	function saveLabelprinter(){
//		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
//			var selectprinter=document.getElementById("selectLabelprinter");   
//			var strResult=LODOP.WRITE_FILE_TEXT(0,"D:/labelprinter.txt",selectprinter.options[selectprinter.selectedIndex].text);
//			if (strResult=="ok")
//				alert("D:/labelprinter.txt写入成功！");
//			else 
//				alert(strResult);
//		}
//		else alert("D:/labelprinter.txt文件不存在！");
		
		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
			var selectprinter=document.getElementById("selectLabelprinter");   
			var strResult=LODOP.WRITE_FILE_TEXT(1,"D:/printer.txt",selectprinter.options[selectprinter.selectedIndex].text);
			if (strResult=="ok")
				alert("D:/labelprinter.txt写入成功！");
			else 
				alert(strResult);
		}
		else alert("D:/labelprinter.txt文件不存在！");
	};
	
	function defaultLabelprinter(){
//		var defaultp="";
//		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
//			defaultp=LODOP.GET_FILE_TEXT("D:/labelprinter.txt");
//		}
//		else{
//		    alert("D:/labelprinter.txt文件不存在！");
//			return false;
//		}
//		if(defaultp.length>0){
//			var length1=defaultp.length;		
//			defaultp=defaultp.substring(0,length1-2);
//			if(LODOP.SET_PRINTER_INDEXA(defaultp)){//SET_PRINTER_INDEX(oIndexOrName);按序号或名称指定打印机，选定后禁止手工重选；
////SET_PRINTER_INDEXA(IndexorName);按序号或名称指定打印机，选定后允许手工重选；
//
//				//alert("设置成功！");
//				return true;
//			}
//		}
//		else{
//			alert("D:/labelprinter.txt文件内容为空！");
//			return false;
//		}
		
		var defaultp="";
		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
			defaultp=LODOP.GET_FILE_TEXT("D:/labelprinter.txt");
		}
		else{
		    alert("D:/labelprinter.txt文件不存在！");
			return false;
		}
		if(defaultp.length>0){
			var length1=defaultp.length;		
			defaultp=defaultp.substring(0,length1-2);
			if(LODOP.SET_PRINTER_INDEXA(defaultp)){//SET_PRINTER_INDEX(oIndexOrName);按序号或名称指定打印机，选定后禁止手工重选；
//SET_PRINTER_INDEXA(IndexorName);按序号或名称指定打印机，选定后允许手工重选；

				//alert("设置成功！");
				return true;
			}
		}
		else{
			alert("D:/labelprinter.txt文件内容为空！");
			return false;
		}
	};	