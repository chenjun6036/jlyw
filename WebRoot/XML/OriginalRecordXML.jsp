<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ԭʼ��¼XML�ļ�����</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
	<script type="text/javascript" src="../JScript/json2.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	
	$.extend($.fn.validatebox.defaults.rules, { 
		ExcelCellNameFormat: { 
			validator: function(value, param){ 
				return /^[a-z,A-Z]+[1-9][0-9]*$/.test(value);
			}, 
			message: '��������ȷ�ĵ�Ԫ���磺A10'
		} 
	}); 
	var types1 = [
		{value:'rw',text:'��д�ļ�'},
		{value:'w',text:'д���ļ�'},
		{value:'r',text:'���ļ���'}	
	];
	var types2 = [
		{value:'w',text:'д���ļ�',selected:true}
	];
	
	$(function(){
      	var products = [
		    {id:0,name:'ί�е�'},
		    {id:1,name:'ԭʼ��¼'},
			{id:2,name:'��У��Ա'},
			{id:3,name:'�����淶'},
			{id:4,name:'������׼'},
			{id:5,name:'��׼����'},
			{id:6,name:'֤��'},
			{id:7,name:'���߱�׼��'},
			{id:8,name:'������Ա'}
		];
		$("#fieldClass").combobox({
			valueField:'name',
			textField:'name',
			data:products,
			required:true,
			onSelect:function(record){
			    $('#desc').combobox('clear');
				if(record.name=="ί�е�"){
				  $('#desc').combobox('options').url='commmissionsheet.json';
				  $("#type").combobox('clear');
				  $("#type").combobox('loadData', types2);
				  $("#indexStrDivTitle").hide();
				  $("#indexStrDivContent").hide();
				}	
				else if(record.name=="ԭʼ��¼"){
				    $('#desc').combobox('options').url='originalrecord.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types1);
					$("#indexStrDivTitle").hide();
				  	$("#indexStrDivContent").hide();
				}
				else if(record.name=="��У��Ա" || record.name=="������Ա"){
				    $('#desc').combobox('options').url='Staff.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").hide();
				 	$("#indexStrDivContent").hide();
				}else if(record.name=="�����淶"){
					$('#desc').combobox('options').url='Specification.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else if(record.name=="������׼"){
					$('#desc').combobox('options').url='Standard.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else if(record.name=="��׼����"){
					$('#desc').combobox('options').url='StandardAppliance.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else if(record.name=="֤��"){
					$('#desc').combobox('options').url='Certificate.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").hide();
					$("#indexStrDivContent").hide();
				}else if(record.name=="���߱�׼��"){
					$('#desc').combobox('options').url='ApplianceStandardName.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").hide();
					$("#indexStrDivContent").hide();
				}
				$('#desc').combobox('reload');
					
			}
		});
		$("#type").combobox({
			valueField:'value',
			textField:'text',
			data:types2,
			required:true
			
		});
		$("#desc").combobox({
		    url:'commmissionsheet.json',
			valueField:'desc',
			textField:'desc',
			required:'true',
			onSelect:function(record){
				$("#attribute").val(record.attribute);
				$("#typeClass").val(record.typeClass);
							
			}
		});	
		var lastIndex;
		$('#XML').datagrid({
			width:1050,
			height:500,
			title:'XML������Ϣ(����)',
			singleSelect:false, 
			fit: false,
			nowrap: false,
			striped: true,
			url:'OriginalRecordXML.json',
			remoteSort: false,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:'fieldClass',title:'������',width:100,align:'center',sortable:true},
				{field:'attribute',title:'������',width:120,align:'center'},	
				{field:'desc',title:'��������',width:100,align:'center'},
				{field:'sheetName',title:'����������',width:100,align:'center',sortable:true,editor:{
					type:'validatebox',
					options:{
						required:true
					}}
				},
				{field:'rowIndex',title:'��Ӧ��Ԫ��',width:100,align:'center',sortable:true,editor:{
					type:'validatebox',
					options:{
						required:true,
						validType:'ExcelCellNameFormat'
					}}
					
				},	
				{field:'typeClass',title:'��������',width:180,align:'center'},	
				{field:'type',title:'��д����',width:80,align:'center'},
				{field:'indexStr',title:'˳���',width:80,align:'center',editor:'text'}
				
			]],
			rownumbers:true	,
			toolbar:[{
				text:'������ѡ��������XML',
				iconCls:'icon-ok',
				handler:function(){
					$('#xml-config').form('submit',{
						url: '/jlyw/FormatXmlServlet.do?method=1',
						onSubmit:function(){
							var res = $('#XML').datagrid('validateRow', lastIndex);
							if(res == true){
								$('#XML').datagrid('acceptChanges');
							}else{
								$.messager.alert('��ʾ��','��������Ч��ֵ��','info');
								return false;
							}
							
											
							var rows = $("#XML").datagrid("getSelections");	
							if(rows==null || rows.length==0){
								$.messager.alert('��ʾ��','�ֶ���ϢΪ��','info');
								return false;
							}
							$("#typerows").val(JSON.stringify(rows));
							return $("#xml-config").form('validate');
						},
						success:function(data){
								$.messager.alert('��ʾ��','�ύ�ɹ�','info');	
								$("#XML").datagrid("clear");	
							
						 }
					});
				}
			},'-',{
				text:'ɾ��',
				iconCls:'icon-remove',
				handler:function(){
					var result = confirm("��ȷ��Ҫ�Ƴ���Щ�ֶ���");
					if(result == false){
						return false;
					}
					var rows = $('#XML').datagrid('getSelections');
					var length = rows.length;
					for(var i=length-1; i>=0; i--){
						var index = $('#XML').datagrid('getRowIndex', rows[i]);
						$('#XML').datagrid('deleteRow', index);
					}
				}
			},'-',{
				text:'�������л�',
				iconCls:'icon-edit',
				handler:function(){
					var result = confirm("��ȷ���л��ɺ����");
					if(result == false){
						return false;
					}
					window.location.href="OriginalRecordXMLHorizon.jsp";
				}
			}],
			onClickRow:function(rowIndex){
				var res = $('#XML').datagrid('validateRow', lastIndex);
				if(res == true){
					$('#XML').datagrid('endEdit', lastIndex);
					$('#XML').datagrid('beginEdit', rowIndex);
					lastIndex = rowIndex;
				}else{
					$.messager.alert('��ʾ��','��������Ч��ֵ��','info');
				}
				
			}
		});

	});
	function add(){
		var validateResult = $("#xml-config-item").form('validate');
		if(validateResult == false){
			return false;
		}
		if($('#fieldClass').combobox('getValue').length==0||$("#desc").combobox('getValue').length==0||$("#rowIndex").val().length==0){
			$.messager.alert('����',"��Ϣ��д������!",'error');
			return false;
		}
		var fieldClass = $('#fieldClass').combobox('getValue');
		var indexStrValue="";
		if(fieldClass=="�����淶" || fieldClass=="������׼" || fieldClass=="��׼����"){
			indexStrValue = $('#indexStr').val();
		}
		var index = $('#XML').datagrid('getRows').length;	//�����һ��������¼
		$('#XML').datagrid('insertRow', {
				index: index,
				row:{
					fieldClass:$('#fieldClass').combobox('getValue'),	
					attribute:$('#attribute').val(),
					sheetName:$("#sheetName").val(),
					rowIndex:$("#rowIndex").val(),
					typeClass:$("#typeClass").val(),
					
					type:$("#type").combobox('getValue'),
					desc:$("#desc").combobox('getValue'),
					indexStr:indexStrValue
				}
			});
   }

	</script>
</head>


<body>
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ԭʼ��¼XML�ļ�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">



<div  style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1050px;heght:200px;padding:10px;"
				title="EXCEL�ļ�����" collapsible="false"  closable="false">
	 <form method="post" id="xml-config">
	 	<input name="typerows" id="typerows" type="hidden" style="padding-top:10px"/>
		<table width="1000">
			<tr>
			 
                <td width="15%" align="right">֤�鹤�������ƣ�</td>
			  <td width="17%"  align="left"><input name="excel2" id="excel2" type="text" required="true" class="easyui-validatebox" value="֤��" /></td>
				<td width="15%" align="right">֤������</td>
			  <td width="18%" align="left"><input name="begin" id="begin" class="easyui-validatebox" type="text" style="width:50px" validType="ExcelCellNameFormat" required="true" />:<input class="easyui-validatebox" name="end" id="end" type="text" style="width:50px" required="true" validType="ExcelCellNameFormat" />��A1:M20</td>
			  <td width="15%"  align="right"></td>
              <td width="17%" align="left"></td>
			</tr>
			<tr>
			 
                <td  align="right">������Ϣ���������ƣ�</td>
			  <td align="left"><input name="excel3" id="excel3" type="text" required="true" class="easyui-validatebox"  value="ԭʼ��¼"  /></td>
				<td align="right">������Ϣ����</td>
			  <td align="left"><input name="excel3Begin" id="excel3Begin" class="easyui-validatebox" type="text" style="width:50px" validType="ExcelCellNameFormat" required="true" />:<input class="easyui-validatebox" name="excel3End" id="excel3End" type="text" style="width:50px" required="true" validType="ExcelCellNameFormat" />��A1:M20</td>
			  <td align="right"></td>
              <td align="left"></td>
			</tr>
			<tr>
			 
                <td align="right">�ܼ����߹����</td>
				<td align="left" >
				<input name="model" id="model" type="checkbox" value="�ͺŹ��">�ͺŹ��</input>
				</td>
				<td align="left">
				<input name="range" id="range" type="checkbox" value="������Χ">������Χ</input>
				</td>
				<td align="left" colspan="3">
				<input name="accuracy" id="accuracy" type="checkbox" value="׼ȷ��/��ȷ���ȵȼ�">׼ȷ��/��ȷ���ȵȼ�</input>
				</td>
			 
			</tr>
		</table>
		</form>
			<hr />
		<form method="post" id="xml-config-item">
		<table width="1000">
		
			
			<tr>
			    <td width="15%" align="right">���ݿ�������</td>
			    <td width="17%" align="left">
					<select name="fieldClass" id="fieldClass" style="width:152px" panelHeight="auto">
<!--			            <OPTION VALUE="CommissionSheet" selected="selected">ί�е�</OPTION>
						<OPTION VALUE="OriginalRecord">ԭʼ��¼</OPTION>         -->  
				   </select></td>
				
				<td width="15%" align="right">����������</td>
				<td width="17%" align="left"><input name="desc" id="desc" readonly="readonly" style="width:152px" panelHeight="auto"/></td>
				
				<td width="15%" align="right"></td>
				<td width="18%" align="left"></td>
			</tr>
			
			<tr>
			    <td align="right">��/д���ͣ�</td>
			    <td align="left">
				 <select name="type" id="type" type="text" class="easyui-combobox" style="width:152px"  panelHeight="auto">
				        <!--<OPTION VALUE="rw" selected="selected">��д</OPTION>
						<OPTION VALUE="w">д</OPTION>
						<OPTION VALUE="r">��</OPTION>-->
			      </select></td>
				
				<td align="right">�������ͣ�</td>
				<td align="left"><input name="typeClass" id="typeClass" type="text" readonly="readonly" style="width:152px"  panelHeight="auto" /></td>
				
				<td align="right"></td>
				<td align="left"></td>
			</tr>
			
			<tr>			
			  <td align="right">Excel���������ƣ�</td>
              <td align="left"><input name="sheetName" id="sheetName" type="text" style="width:152px" required="true" class="easyui-validatebox" />
			       </td>
			 <td align="right">Excel��Ӧ��Ԫ��</td>
			 <td align="left" ><input name="rowIndex" class="easyui-validatebox" id="rowIndex" type="text" required="true" style="width:102px" validType="ExcelCellNameFormat" />
��A10</td>
				
			 <td align="right" id="indexStrDivTitle">˳��ţ�</td>
			  <td align="left" id="indexStrDivContent"><input name="indexStr" id="indexStr" type="text" style="width:102px" />
			     
			 </td>
			</tr>
			
			<tr>
			   
			    <td  align="left"><input name="attribute" id="attribute" type="hidden" style="padding-top:10px"/></td>
			   
				<td  colspan="2"   align="center" style="padding-top:10px"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="add()">����ֶ�</a>
			    </td>
				<td></td>
				<td></td>
				<td></td>
				<!--<td  colspan="2"   align="center" style="padding-top:10px"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="configxml()">����XML</a>
			    </td>-->
				 
			</tr>
		</table><br/>
		</form>
		</div>
	 <div style="width:900px;height:502px;">
	     <table id="XML" iconCls="icon-tip" width="1050px" height="500px" ></table>
	 </div>
	 
</div>

</DIV></DIV>
</body>
</html>
