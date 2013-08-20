<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>原始记录XML文件编制</title>
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
			message: '请输入正确的单元格，如：A10'
		} 
	}); 
	var types1 = [
		{value:'rw',text:'读写文件'},
		{value:'w',text:'写入文件'},
		{value:'r',text:'从文件读'}	
	];
	var types2 = [
		{value:'w',text:'写入文件',selected:true}
	];
	
	$(function(){
      	var products = [
		    {id:0,name:'委托单'},
		    {id:1,name:'原始记录'},
			{id:2,name:'检校人员'},
			{id:3,name:'技术规范'},
			{id:4,name:'计量标准'},
			{id:5,name:'标准器具'},
			{id:6,name:'证书'},
			{id:7,name:'器具标准名'},
			{id:8,name:'核验人员'}
		];
		$("#fieldClass").combobox({
			valueField:'name',
			textField:'name',
			data:products,
			required:true,
			onSelect:function(record){
			    $('#desc').combobox('clear');
				if(record.name=="委托单"){
				  $('#desc').combobox('options').url='commmissionsheet.json';
				  $("#type").combobox('clear');
				  $("#type").combobox('loadData', types2);
				  $("#indexStrDivTitle").hide();
				  $("#indexStrDivContent").hide();
				}	
				else if(record.name=="原始记录"){
				    $('#desc').combobox('options').url='originalrecord.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types1);
					$("#indexStrDivTitle").hide();
				  	$("#indexStrDivContent").hide();
				}
				else if(record.name=="检校人员" || record.name=="核验人员"){
				    $('#desc').combobox('options').url='Staff.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").hide();
				 	$("#indexStrDivContent").hide();
				}else if(record.name=="技术规范"){
					$('#desc').combobox('options').url='Specification.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else if(record.name=="计量标准"){
					$('#desc').combobox('options').url='Standard.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else if(record.name=="标准器具"){
					$('#desc').combobox('options').url='StandardAppliance.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else if(record.name=="证书"){
					$('#desc').combobox('options').url='Certificate.json';
					$("#type").combobox('clear');
					$("#type").combobox('loadData', types2);
					$("#indexStrDivTitle").hide();
					$("#indexStrDivContent").hide();
				}else if(record.name=="器具标准名"){
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
			title:'XML配置信息(竖版)',
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
				{field:'fieldClass',title:'类名称',width:100,align:'center',sortable:true},
				{field:'attribute',title:'属性名',width:120,align:'center'},	
				{field:'desc',title:'属性描述',width:100,align:'center'},
				{field:'sheetName',title:'工作表名称',width:100,align:'center',sortable:true,editor:{
					type:'validatebox',
					options:{
						required:true
					}}
				},
				{field:'rowIndex',title:'对应单元格',width:100,align:'center',sortable:true,editor:{
					type:'validatebox',
					options:{
						required:true,
						validType:'ExcelCellNameFormat'
					}}
					
				},	
				{field:'typeClass',title:'属性类型',width:180,align:'center'},	
				{field:'type',title:'读写类型',width:80,align:'center'},
				{field:'indexStr',title:'顺序号',width:80,align:'center',editor:'text'}
				
			]],
			rownumbers:true	,
			toolbar:[{
				text:'根据所选的行生成XML',
				iconCls:'icon-ok',
				handler:function(){
					$('#xml-config').form('submit',{
						url: '/jlyw/FormatXmlServlet.do?method=1',
						onSubmit:function(){
							var res = $('#XML').datagrid('validateRow', lastIndex);
							if(res == true){
								$('#XML').datagrid('acceptChanges');
							}else{
								$.messager.alert('提示！','请输入有效的值！','info');
								return false;
							}
							
											
							var rows = $("#XML").datagrid("getSelections");	
							if(rows==null || rows.length==0){
								$.messager.alert('提示！','字段信息为空','info');
								return false;
							}
							$("#typerows").val(JSON.stringify(rows));
							return $("#xml-config").form('validate');
						},
						success:function(data){
								$.messager.alert('提示！','提交成功','info');	
								$("#XML").datagrid("clear");	
							
						 }
					});
				}
			},'-',{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					var result = confirm("您确定要移除这些字段吗？");
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
				text:'横竖版切换',
				iconCls:'icon-edit',
				handler:function(){
					var result = confirm("您确定切换成横版吗？");
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
					$.messager.alert('提示！','请输入有效的值！','info');
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
			$.messager.alert('警告',"信息填写不完整!",'error');
			return false;
		}
		var fieldClass = $('#fieldClass').combobox('getValue');
		var indexStrValue="";
		if(fieldClass=="技术规范" || fieldClass=="计量标准" || fieldClass=="标准器具"){
			indexStrValue = $('#indexStr').val();
		}
		var index = $('#XML').datagrid('getRows').length;	//在最后一行新增记录
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
			<jsp:param name="TitleName" value="原始记录XML文件编制" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">



<div  style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1050px;heght:200px;padding:10px;"
				title="EXCEL文件设置" collapsible="false"  closable="false">
	 <form method="post" id="xml-config">
	 	<input name="typerows" id="typerows" type="hidden" style="padding-top:10px"/>
		<table width="1000">
			<tr>
			 
                <td width="15%" align="right">证书工作表名称：</td>
			  <td width="17%"  align="left"><input name="excel2" id="excel2" type="text" required="true" class="easyui-validatebox" value="证书" /></td>
				<td width="15%" align="right">证书区域：</td>
			  <td width="18%" align="left"><input name="begin" id="begin" class="easyui-validatebox" type="text" style="width:50px" validType="ExcelCellNameFormat" required="true" />:<input class="easyui-validatebox" name="end" id="end" type="text" style="width:50px" required="true" validType="ExcelCellNameFormat" />如A1:M20</td>
			  <td width="15%"  align="right"></td>
              <td width="17%" align="left"></td>
			</tr>
			<tr>
			 
                <td  align="right">附加信息工作表名称：</td>
			  <td align="left"><input name="excel3" id="excel3" type="text" required="true" class="easyui-validatebox"  value="原始记录"  /></td>
				<td align="right">附加信息区域：</td>
			  <td align="left"><input name="excel3Begin" id="excel3Begin" class="easyui-validatebox" type="text" style="width:50px" validType="ExcelCellNameFormat" required="true" />:<input class="easyui-validatebox" name="excel3End" id="excel3End" type="text" style="width:50px" required="true" validType="ExcelCellNameFormat" />如A1:M20</td>
			  <td align="right"></td>
              <td align="left"></td>
			</tr>
			<tr>
			 
                <td align="right">受检器具关联项：</td>
				<td align="left" >
				<input name="model" id="model" type="checkbox" value="型号规格">型号规格</input>
				</td>
				<td align="left">
				<input name="range" id="range" type="checkbox" value="测量范围">测量范围</input>
				</td>
				<td align="left" colspan="3">
				<input name="accuracy" id="accuracy" type="checkbox" value="准确度/不确定度等级">准确度/不确定度等级</input>
				</td>
			 
			</tr>
		</table>
		</form>
			<hr />
		<form method="post" id="xml-config-item">
		<table width="1000">
		
			
			<tr>
			    <td width="15%" align="right">数据库类名：</td>
			    <td width="17%" align="left">
					<select name="fieldClass" id="fieldClass" style="width:152px" panelHeight="auto">
<!--			            <OPTION VALUE="CommissionSheet" selected="selected">委托单</OPTION>
						<OPTION VALUE="OriginalRecord">原始记录</OPTION>         -->  
				   </select></td>
				
				<td width="15%" align="right">属性描述：</td>
				<td width="17%" align="left"><input name="desc" id="desc" readonly="readonly" style="width:152px" panelHeight="auto"/></td>
				
				<td width="15%" align="right"></td>
				<td width="18%" align="left"></td>
			</tr>
			
			<tr>
			    <td align="right">读/写类型：</td>
			    <td align="left">
				 <select name="type" id="type" type="text" class="easyui-combobox" style="width:152px"  panelHeight="auto">
				        <!--<OPTION VALUE="rw" selected="selected">读写</OPTION>
						<OPTION VALUE="w">写</OPTION>
						<OPTION VALUE="r">读</OPTION>-->
			      </select></td>
				
				<td align="right">属性类型：</td>
				<td align="left"><input name="typeClass" id="typeClass" type="text" readonly="readonly" style="width:152px"  panelHeight="auto" /></td>
				
				<td align="right"></td>
				<td align="left"></td>
			</tr>
			
			<tr>			
			  <td align="right">Excel工作表名称：</td>
              <td align="left"><input name="sheetName" id="sheetName" type="text" style="width:152px" required="true" class="easyui-validatebox" />
			       </td>
			 <td align="right">Excel对应单元格：</td>
			 <td align="left" ><input name="rowIndex" class="easyui-validatebox" id="rowIndex" type="text" required="true" style="width:102px" validType="ExcelCellNameFormat" />
如A10</td>
				
			 <td align="right" id="indexStrDivTitle">顺序号：</td>
			  <td align="left" id="indexStrDivContent"><input name="indexStr" id="indexStr" type="text" style="width:102px" />
			     
			 </td>
			</tr>
			
			<tr>
			   
			    <td  align="left"><input name="attribute" id="attribute" type="hidden" style="padding-top:10px"/></td>
			   
				<td  colspan="2"   align="center" style="padding-top:10px"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="add()">添加字段</a>
			    </td>
				<td></td>
				<td></td>
				<td></td>
				<!--<td  colspan="2"   align="center" style="padding-top:10px"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="configxml()">生成XML</a>
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
