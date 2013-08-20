var obj = new Array();
var oSheet;
function ExportToExcel(sheetTitle,title,columns,url,params,total){
	try{
		var oXL = new ActiveXObject("Excel.Application");
 	}catch(e){
    	alert("�޷�����Excel!\n\n������Ѿ���װ��Excel���밴���·�������IE�İ�ȫ����\n\n���������\n\n"+"���� �� Internetѡ�� �� ��ȫ �� �Զ��弶�� �� ��û�б��Ϊ��ȫ��ActiveX���г�ʼ���ͽű����� �� ���ã�ȷ����");
  		return false;
    }
    //����AX����excel 
    var oWB = oXL.Workbooks.Add(); 
    //��ȡworkbook���� 
    oSheet = oWB.ActiveSheet;
    //���ǰsheet
	oSheet.name = sheetTitle;
	
    var size = 1000;//ÿ����Servlet������ȡ���ݵ���󳤶�
	var index=1;
	
	if(title!="")
	{
		var colLen = columns.length;
		oSheet.Range(oSheet.cells(index,1),oSheet.cells(index,colLen)).select();
		oXL.Selection.HorizontalAlignment = 3;
		oXL.Selection.VerticalAlignment = 2;
     	oXL.Selection.Font.Size = 15;
		oXL.Selection.MergeCells = true;
		oSheet.Cells(index,1).value = title;
		index = index + 1;
	}
	
	var i = 1;
	for(var key in columns)
	{
		oSheet.Cells(index,i).value = columns[key].title;
		var temp={};
		temp.colIndex = i;
		temp.key = columns[key].field;
		obj.push(temp);
		i=i+1;
	}	
	
	oSheet.Columns("A:AZ").ColumnWidth = 15;
	
	
	oXL.Visible = true;
    //����excel�ɼ�����
	for(var i = 0; i <= total/size; i++)
	{
     	$.ajax({
			type:'POST',
			url:url,
			data: params + "&page=" + (i+1) + "&rows=" + size,
			dataType:"json",
			async:false,
			cache: false,
			success:function(data){
				var datas = data.rows;
				var params = this.data;
				var pageStr = params.split("&");
				var pageString = pageStr[pageStr.length-2];
				var page = parseInt(pageString.substr(5, pageString.length));
				for(var j = 0; j < datas.length; j++)
				{		
					index = (page-1) * size + j + (title==""?2:3);
					for(var k = 0;k < obj.length; k++)
					{
						var value = datas[j][obj[k].key];
						if(value!=""&&value!=null)
							oSheet.Cells(index, obj[k].colIndex).value = "'"+value;
					}
				}
				delete datas;
				CollectGarbage();
			}
		});
    }
	

}