	
	
	String.prototype.Trim = function() { 
		return this.replace(/(^\s*)|(\s*$)/g, ""); 
	} //ȥ���ո�,�س� 
	String.prototype.LTrim = function() { 
		return this.replace(/(^\s*)/g, ""); //ɾ���ַ�����ߵĿո�س� 
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
				alert("D:/printer.txtд��ɹ���");
			else 
				alert(strResult);
			
						
		}
		else alert("D:/printer.txt�ļ������ڣ�");
		
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
			alert("D:/printer.txt�ļ�����Ϊ�գ�");
			return "";
		}
		return printers[i].Trim();
	}
	
	function defaultprinter(i){//-1Ĭ�ϴ�ӡ��   0ί�е���ӡ��   1��ǩ��ӡ��  nullί�е���ӡ��
		
		var printername=getprinter(0);
		if(i!=null){
			if(i < 0){//ϵͳĬ�ϴ�ӡ��
				if(LODOP.SET_PRINTER_INDEXA(-1)){//SET_PRINTER_INDEX(oIndexOrName);����Ż�����ָ����ӡ����ѡ�����ֹ�ֹ���ѡ��
//SET_PRINTER_INDEXA(IndexorName);����Ż�����ָ����ӡ����ѡ���������ֹ���ѡ��

				    
					return true;
				}else{
					
					return false;
				}
			}else{
				printername=getprinter(i);	
				
			}
		}
	
		if(printername.length==0){
			alert("D:/printer.txt�����ڻ����ļ�����Ϊ��,��");
			return false;
		}else{
			if(LODOP.SET_PRINTER_INDEXA(printername)){//SET_PRINTER_INDEX(oIndexOrName);����Ż�����ָ����ӡ����ѡ�����ֹ�ֹ���ѡ��
//SET_PRINTER_INDEXA(IndexorName);����Ż�����ָ����ӡ����ѡ���������ֹ���ѡ��

				//alert("���óɹ���");
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
//				alert("D:/labelprinter.txtд��ɹ���");
//			else 
//				alert(strResult);
//		}
//		else alert("D:/labelprinter.txt�ļ������ڣ�");
		
		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
			var selectprinter=document.getElementById("selectLabelprinter");   
			var strResult=LODOP.WRITE_FILE_TEXT(1,"D:/printer.txt",selectprinter.options[selectprinter.selectedIndex].text);
			if (strResult=="ok")
				alert("D:/labelprinter.txtд��ɹ���");
			else 
				alert(strResult);
		}
		else alert("D:/labelprinter.txt�ļ������ڣ�");
	};
	
	function defaultLabelprinter(){
//		var defaultp="";
//		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
//			defaultp=LODOP.GET_FILE_TEXT("D:/labelprinter.txt");
//		}
//		else{
//		    alert("D:/labelprinter.txt�ļ������ڣ�");
//			return false;
//		}
//		if(defaultp.length>0){
//			var length1=defaultp.length;		
//			defaultp=defaultp.substring(0,length1-2);
//			if(LODOP.SET_PRINTER_INDEXA(defaultp)){//SET_PRINTER_INDEX(oIndexOrName);����Ż�����ָ����ӡ����ѡ�����ֹ�ֹ���ѡ��
////SET_PRINTER_INDEXA(IndexorName);����Ż�����ָ����ӡ����ѡ���������ֹ���ѡ��
//
//				//alert("���óɹ���");
//				return true;
//			}
//		}
//		else{
//			alert("D:/labelprinter.txt�ļ�����Ϊ�գ�");
//			return false;
//		}
		
		var defaultp="";
		if (LODOP.IS_FILE_EXIST("D:/labelprinter.txt")){
			defaultp=LODOP.GET_FILE_TEXT("D:/labelprinter.txt");
		}
		else{
		    alert("D:/labelprinter.txt�ļ������ڣ�");
			return false;
		}
		if(defaultp.length>0){
			var length1=defaultp.length;		
			defaultp=defaultp.substring(0,length1-2);
			if(LODOP.SET_PRINTER_INDEXA(defaultp)){//SET_PRINTER_INDEX(oIndexOrName);����Ż�����ָ����ӡ����ѡ�����ֹ�ֹ���ѡ��
//SET_PRINTER_INDEXA(IndexorName);����Ż�����ָ����ӡ����ѡ���������ֹ���ѡ��

				//alert("���óɹ���");
				return true;
			}
		}
		else{
			alert("D:/labelprinter.txt�ļ�����Ϊ�գ�");
			return false;
		}
	};	