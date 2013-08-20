<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>ǿ��֪ͨ��</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"  src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
   <script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>	
        <script type="text/javascript" src="../../JScript/letter.js"></script>
 		<script type="text/javascript" src="../../JScript/json2.js"></script>
        <script type="text/javascript" src="../../JScript/upload.js"></script>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
	<script>
		$(function(){
		$('#Customer').combobox({
		//	url:'/jlyw/CustomerServlet.do?method=5',
			valueField:'name',
			textField:'name',
			onSelect:function(){},
			onChange:function(newValue, oldValue){
				var allData = $(this).combobox('getData');
				if(allData != null && allData.length > 0){
					for(var i=0; i<allData.length; i++)
					{
						if(newValue==allData[i].name){
							return false;
						}
					}
				}
				$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}
		});
		});
		$(function(){
	
			$('#table6').datagrid({
			    width:900,
				height:400,
				title:'ǿ��֪ͨ��',
//				iconCls:'icon-save',
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				//url:'/jlyw/QJTZServlet.do?method=1',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CustomerName',title:'ί�е�λ',width:180,align:'center'},
					{field:'name2',title:'��������',width:80,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'QGid',title:'ǿ��Ψһ�Ժ�',width:120,align:'center'},
					{field:'time1',title:'�ϴ�ǿ���ʱ��',width:100,align:'center'},
					{field:'cycle',title:'ǿ������',width:80,align:'center'},
					{field:'time2',title:'����Ӧǿ��ʱ��',width:100,align:'center'},
					{field:'Remark',title:'��ע',width:80,editor:'text',align:'center'}
                ]],
				pagination:true,
				rownumbers:true
			});

		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/QJTZServlet.do?method=1';
			$('#table6').datagrid('options').queryParams={'Customer':encodeURI($('#Customer').combobox('getValue')),'StartTime':encodeURI($('#StartTime').datebox('getValue')),'EndTime':encodeURI($('#EndTime').datebox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function clear(){
			$('#Customer').combobox('setValue',"");
			$('#StartTime').datebox('setValue',"");
			$('#EndTime').datebox('setValue',"");
		}
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
			LODOP.ADD_PRINT_HTM(28,10,950,1000,document.getElementById("content").innerHTML);
		};
		
		function printQJTZ(customerObjectRows){
			rows=customerObjectRows;
			document.getElementById("ZipCode").innerHTML = rows[0].Zipcode;
			document.getElementById("Address").innerHTML = rows[0].Address;
			document.getElementById("Name1").innerHTML = rows[0].CustomerName;
			document.getElementById("Notice").innerHTML = "ǿ�Ƽ춨�������߼춨֪ͨ��";
			document.getElementById("Name2").innerHTML = rows[0].CustomerName;
			document.getElementById("ownAddress").innerHTML = $('#SendAddress').combobox('getText');
			var date = new Date();
			var year="",month="",day="";
			year=date.getFullYear();
			month=date.getMonth()+1;
			day=date.getDate();
			document.getElementById("date1").innerHTML = year+"��"+month+"��"+day+"��";
			document.getElementById("date3").innerHTML = year+"��"+month+"��"+day+"��";
			month = month+1;
			if(month==13){
				month=1;
				year = year + 1;
			}
			document.getElementById("date2").innerHTML = year+"��"+month+"��"+day+"��";
			
			var index = $('#SendAddress').combobox('getValue');
			var datas = $('#SendAddress').combobox('getData');
			for(var i = 0; i < datas.length; i++)
			{
				if(datas[i].id == index)
				{
					document.getElementById("label3").innerHTML = datas[i].headname;
					document.getElementById("Tel").innerHTML = datas[i].Tel==null?"":"��ϵ�绰��" + datas[i].Tel;
					document.getElementById("fax").innerHTML = datas[i].Fax==null?"":"���棺" + datas[i].Fax;				
					document.getElementById("address2").innerHTML = datas[i].address==null?"":"��ַ��" + datas[i].address;				
					document.getElementById("ZipCode2").innerHTML = datas[i].Zipcode==null?"":"�������룺" + datas[i].Zipcode;
					break;
				}
			}
			var appliances = document.getElementById("Appliances");
			var html = "<table cellpadding='0' cellspacing='0' border='1' width='750'>";
			html = html + "<tr><td>��������</td><td>ǿ��Ψһ�Ժ�</td><td>�ͺŹ��</td><td>�ϴ�ǿ��ʱ��</td><td>��ע</td></tr>";
			for(var i = 0; i < rows.length; i++)
			{
				
				html = html + "<tr>";
				html = html + "<td>" + rows[i].name2 + "</td>" + "<td>" + rows[i].QGid + "</td>" + "<td>" + rows[i].Model + "</td>" + "<td>" + rows[i].time1 + "</td>" + "<td>" + rows[i].Remark + "</td>";
				html = html + "</tr>";
			}
			html = html + "</table>";
			appliances.innerHTML = html;
			prn1_preview();
		}
		
		function goback(){
			history.go(-1);
		}
		
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#table6').datagrid('options').queryParams));
			$('#frm_export').form('submit',{
				success:function(data){
					var result = eval("("+ data +")");
					if(result.IsOK)
					{
						$('#filePath').val(result.Path);
						$('#frm_down').submit();
						CloseWaitingDlg();
					}
					else
					{
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		function submitPrint(){//��ӡ
			if($('#table6').datagrid('getRows').length==0)
				return;
			if($('#SendAddress').combobox('getValue')==""){
				$.messager.alert('��ʾ','��ѡ���ͼ��ַ','warning');
				return;
			}
				
			$('#SearchForm').form('submit',{
				url: '/jlyw/QJTZServlet.do?method=3',
				onSubmit:function(){
					return $('#SearchForm').form('validate');
				},
				success:function(data){
					//alert(data);
					var result = eval("("+data+")");
					if(result.IsOK){
						alert("��ʼ��ӡ:"+result.RowsList.length);
						
						for(var i=0;i<result.RowsList.length;i++){
							//Preview1(result.RowsList[i].PrintObj);
							printQJTZ(result.RowsList[i].rows);
						}
						
					}else{
						$.messager.alert('��ӡʧ�ܣ�',result.msg,'error');
					}
					
				 }
			});
		}
		</script>
</head>

<body>
<form id="frm_export" method="post" action="/jlyw/QJTZServlet.do?method=2">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">  
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ѯ��ӡǿ��֪ͨ��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div style="position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:125px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
			<form id="SearchForm" method="post">
			<table width="850px" id="table1">
				<tr >
					<td align="right" >ί�е�λ��</td>
					<td align="left" >
						<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;" >
					</td>
					<td align="right">��ֹ���ڣ�</td>
					<td align="left">
						<input id="StartTime" class="easyui-datebox" name="StartTime" style="width:150px;"> --
						<input id="EndTime" class="easyui-datebox" name="EndTime" style="width:150px;">
					</td>
					
				</tr >
				<tr height="40px">
					
				    <td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
					
				    <td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="clear()">����</a></td>
					
				</tr>
				
		</table>
		</form>
		</div>
		
      <div style="width:900px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>
	  <br />
	  <div id="p2" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="������" collapsible="false"  closable="false">
			<form id="allot" method="post">
			<table width="850px" >
					<tr>
						<td width="33%" align="right">�ͼ��ַ��</td>
						<td width="33%" align="left" ><select name="SendAddress" id="SendAddress" style="width:250px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=2" required="true" editable="false"/></td>
					</tr>
					<tr >
				     <td align="center" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onclick="submitPrint()">��ӡǿ��֪ͨ��</a>
					</td>
                    <td align="right" style="padding-top:15px;">
						<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onClick="myExport()">����</a>
					</td>	
				</tr>
		  </table>
		  <!--  <div><a href="javascript:prn1_preview()">��ӡԤ��</a></div>-->
		  </form>
		</div>
</div>
<div id="qjtz" class="easyui-window"  style="padding: 10px;width: 900;height: 1000;" 
     closed="true" maximizable="false" minimizable="false" collapsible="false">
     <form id="content">
	 <div >
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label name="ZipCode" id="ZipCode"  style="font-size:20px"></label>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label name="Address" id="Address"  style="font-size:20px" ></label>
	 </div>
	   <br />
	 <div>
			<label name="Name1" id="Name1"  style="font-size:20px"  ></label>���գ�
     </div>
   
     <br />
	 <div>
			&nbsp;&nbsp;<label name="Notice" id="Notice"  style="font-size:30px"  ></label>
     </div>
     <br />
	 <div>
			<label name="Name2" id="Name2" ></label>
     </div>
	 <div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��λ����ǿ�Ƽ춨�������߽����ڣ�����<label name="date1" id="date1"></label>��<label name="label2" id="label2"></label><label name="date2" id="date2"></label>�䰲�ż춨��
     </div>
     <div id="Appliances">
      </div>
      <br />
	 <div>
	 		�ͼ��ַ��<label name="label3" id="label3" ></label>
	 		<br />

			<label name="Tel" id="Tel"></label>&nbsp;<label name="fax" id="fax"></label>&nbsp;<label name="address2" id="address2" ></label>&nbsp;</label><label name="ZipCode2" id="ZipCode2"></label>
     </div>
     <br />
	 <div>
			����1.���л����񹲺͹�������ʵʩϸ�򡷵���ʮ����������ǿ�Ƽ춨��Χ�ļ�������δ���չ涨����춨��<br />�����춨���ϸ����ʹ�õģ�������ֹͣʹ�ã��ɲ���һǧԪһ�µķ��<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.������ʡ�����춨�շѹ涨������������������ʹ�õ�λ���򲻿ɿ���ԭ�򣬲������������춨������<br />�ŵ��ܼ�ƻ��춨�ģ����������ڼ춨�շѱ�׼�Ļ����ϼ���20%�ļ����ѡ�
     </div>
     <br />
     <br />

     <div>
     <table cellpadding='0' cellspacing='0' width='750'>
					<tr>
						<td width="33%" align="right">����ʡ�������������ල��</td>
					</tr>
					<tr >
				    	<td width="33%"  align="right"><label name = "ownAddress" id = "ownAddress"></td>
					</tr>
					<tr>
						<td width="33%"  align="right"><label name="date3" id="date3"></label></td>
					</tr>
		  </table>
     </div>
		</form>
</div>
</DIV>
</DIV>
</body>
</html>
