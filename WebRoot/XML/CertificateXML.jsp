<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>证书XML文件编制</title>
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
	
	

	$(function(){
      	var products = [
		    {attributename:'DataPage',desc:'证书数据页',indexStr:false},
		    {attributename:'CertificateCode',desc:'证书编号',indexStr:false},
			{attributename:'CertificateBarCode',desc:'证书编号条形码',indexStr:false},	
			{attributename:'Customer',desc:'委托单位',indexStr:false},
			{attributename:'CustomerAddress',desc:'委托单位地址',indexStr:false},
		    {attributename:'ApplianceName',desc:'器具名称',indexStr:false},
			{attributename:'Model',desc:'型号规格',indexStr:false},	
			{attributename:'FactoryCode',desc:'出厂编号',indexStr:false},
		    {attributename:'Manufacturer',desc:'制造单位',indexStr:false},
			
			{attributename:'DwName',desc:'台头名称（中文）',indexStr:false},
			{attributename:'DwNameEn',desc:'台头名称（英文）',indexStr:false},
			{attributename:'DwAddrName',desc:'台头单位地址（中文）',indexStr:false},
			{attributename:'DwAddrNameEn',desc:'台头单位地址（英文）',indexStr:false},
			{attributename:'DwZipCode',desc:'台头单位邮编',indexStr:false},
			{attributename:'DwWebSite',desc:'台头单位网址',indexStr:false},
			{attributename:'DwTel',desc:'台头单位电话',indexStr:false},
			{attributename:'DwComplainTel',desc:'台头单位投诉电话',indexStr:false},
			{attributename:'DwFax',desc:'台头单位传真',indexStr:false},
			
			{attributename:'SecurityCodeP1',desc:'安全码（第一部分）',indexStr:false},
			{attributename:'SecurityCodeP2',desc:'安全码（第二部分）',indexStr:false},
			
			{attributename:'TechnicalDocs-All',desc:'检定依据(证书首页)',indexStr:false},
			{attributename:'TechnicalDocs-Code',desc:'检定依据(技术规范)-编号',indexStr:true},
			{attributename:'TechnicalDocs-Name',desc:'检定依据(技术规范)-名称',indexStr:true},
			
			{attributename:'Standard-Name',desc:'计量标准-名称',indexStr:true},
			{attributename:'Standard-CertificateCode',desc:'计量标准-证书编号',indexStr:true},
			{attributename:'Standard-Range',desc:'计量标准-测量范围',indexStr:true},
			{attributename:'Standard-Uncertain',desc:'计量标准-不确定度',indexStr:true},
			{attributename:'Standard-ValidDate',desc:'计量标准-有效期',indexStr:true},
			
			{attributename:'StandardAppliance-Name',desc:'标准器具-名称',indexStr:true},
			{attributename:'StandardAppliance-Range',desc:'标准器具-测量范围',indexStr:true},
			{attributename:'StandardAppliance-Model',desc:'标准器具-型号规格',indexStr:true},
			{attributename:'StandardAppliance-Uncertain',desc:'标准器具-不确定度',indexStr:true},
			{attributename:'StandardAppliance-SeriaNumber',desc:'标准器具-证书编号',indexStr:true},
			{attributename:'StandardAppliance-LocaleCode',desc:'标准器具-所内编号',indexStr:true},
			{attributename:'StandardAppliance-ValidDate',desc:'标准器具-有效期',indexStr:true},
			
			{attributename:'Conclusion',desc:'检定结论',indexStr:false},
			
			{attributename:'AuthorizerPrinter',desc:'批准人（打印）',indexStr:false},
		    {attributename:'Authorizer',desc:'批准人',indexStr:false},
			{attributename:'AuthorizerPosition',desc:'批准人职务',indexStr:false},				
			{attributename:'CheckerPrinter',desc:'核验员（打印）',indexStr:false},			
			{attributename:'Checker',desc:'核验员',indexStr:false},	
			{attributename:'VerifierPrinter',desc:'检定员（打印）',indexStr:false},
			{attributename:'Verifier',desc:'检定员',indexStr:false},
		    {attributename:'VerifyYear',desc:'检定日期年',indexStr:false},
			{attributename:'VerifyMonth',desc:'检定日期月',indexStr:false},	
			{attributename:'VerifyDay',desc:'检定日期日',indexStr:false},
		    {attributename:'ValidYear',desc:'有效日期年',indexStr:false},
			{attributename:'ValidMonth',desc:'有效日期月',indexStr:false},
			{attributename:'ValidDay',desc:'有效日期日',indexStr:false},
			{attributename:'SampleRecvYear',desc:'样品接收日期年',indexStr:false},
			{attributename:'SampleRecvMonth',desc:'样品接收日期月',indexStr:false},
			{attributename:'SampleRecvDay',desc:'样品接收日期日',indexStr:false},
			
			{attributename:'WorkLocation',desc:'工作地点',indexStr:false},	
			{attributename:'Temp',desc:'检定温度',indexStr:false},
		    {attributename:'Humidity',desc:'检定相对湿度',indexStr:false},
			{attributename:'Pressure',desc:'检定大气压',indexStr:false}
		];
		
		$("#desc").combobox({
		    data:products,
			valueField:'desc',
			textField:'desc',
			required:'true',
			onSelect:function(record){
				$("#attribute").val(record.attributename);
				if(record.indexStr){
					$("#indexStrDivTitle").show();
					$("#indexStrDivContent").show();
				}else{
					$("#indexStr").val('');
					$("#indexStrDivTitle").hide();
					$("#indexStrDivContent").hide();
				}		
			}
		});
		var lastIndex;	
		$('#XML').datagrid({
			width:950,
			height:500,
			title:'XML配置信息',
			singleSelect:false, 
			fit: false,
			nowrap: false,
			striped: true,

			remoteSort: false,
			url:'CertificateXML.json',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:'desc',title:'属性描述',width:120,align:'center', sortable:true},
				{field:'attribute',title:'属性名',width:120,align:'center'},	
				{field:'tagName',title:'标签名',width:100,align:'center', sortable:true, editor:{
					type:'validatebox',
					options:{
						required:true
					}}
				},
				{field:'indexStr',title:'顺序号',width:80,align:'center', editor:'text'}
				
			]],
			rownumbers:true	,
			toolbar:[{
				text:'根据所选的行生成XML',
				iconCls:'icon-ok',
				handler:function(){
						$('#xml-config').form('submit',{
						url: '/jlyw/FormatXmlServlet.do?method=2',
						onSubmit:function(){
											
							var rows = $("#XML").datagrid("getSelections");	
							if(rows==null||rows.length==0){
								$.messager.alert('提示！','字段信息为空','info');
								return false;
							}
							$("#typerows").val(JSON.stringify(rows));
							//return $("#xml-config").form('validate');
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
		$("#xml-config").form('validate');
	   
		var index = $('#XML').datagrid('getRows').length;	//在最后一行新增记录			
		if($("#desc").combobox('getValue').length==0||$("#tagName").val().length==0){
			$.messager.alert('警告',"信息填写不完整!",'error');
			return false;
		}
		
		$('#XML').datagrid('insertRow', {
				index: index,
				row:{
					desc:$("#desc").combobox('getValue'),
					attribute:$('#attribute').val(),
					tagName:$("#tagName").val(),
					indexStr:$("#indexStr").val()
					
				}
			});
   }

	</script>
</head>


<body>
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="证书XML文件编制" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


<div  style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:950px;heght:200px;padding:10px;"
				title="增加配置字段" collapsible="false"  closable="false">
	 <form method="post" id="xml-config">
		
		<table width="900">
		
			<tr>			
			 <td  width="15%"  align="right">属性描述：</td>
			 <td align="left" width="17%" ><input name="desc" id="desc" readonly="readonly" style="width:152px" panelHeight="auto"/></td>
			 <td width="12%" align="right">标签名称：</td>
			 <td width="22%" align="left"><input name="tagName" class="easyui-validatebox" id="tagName" type="text" required="true"  ></td> 	
			 <td width="14%" align="right" id="indexStrDivTitle">顺序号：</td>
			 <td width="20%" align="left" id="indexStrDivContent"><input name="indexStr" id="indexStr" type="text" style="width:102px" /></td>
			</tr>
			<tr>
			   
			    <td  align="left"><input name="attribute" id="attribute" type="hidden" style="padding-top:10px"/><input name="typerows" id="typerows" type="hidden" style="padding-top:10px"/></td>
			   
			   
				<td  colspan="5" align="center" style="padding-top:10px"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="add()">添加字段</a>
			    </td>
				
			</tr>
		</table><br/>
		</form>
		</div>
	 <div style="width:900px;height:502px;">
	     <table id="XML" iconCls="icon-tip" width="1000px" height="500px" ></table>
	 </div>

</div>


</DIV></DIV>
</body>
</html>
