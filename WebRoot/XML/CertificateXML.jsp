<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>֤��XML�ļ�����</title>
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
		    {attributename:'DataPage',desc:'֤������ҳ',indexStr:false},
		    {attributename:'CertificateCode',desc:'֤����',indexStr:false},
			{attributename:'CertificateBarCode',desc:'֤����������',indexStr:false},	
			{attributename:'Customer',desc:'ί�е�λ',indexStr:false},
			{attributename:'CustomerAddress',desc:'ί�е�λ��ַ',indexStr:false},
		    {attributename:'ApplianceName',desc:'��������',indexStr:false},
			{attributename:'Model',desc:'�ͺŹ��',indexStr:false},	
			{attributename:'FactoryCode',desc:'�������',indexStr:false},
		    {attributename:'Manufacturer',desc:'���쵥λ',indexStr:false},
			
			{attributename:'DwName',desc:'̨ͷ���ƣ����ģ�',indexStr:false},
			{attributename:'DwNameEn',desc:'̨ͷ���ƣ�Ӣ�ģ�',indexStr:false},
			{attributename:'DwAddrName',desc:'̨ͷ��λ��ַ�����ģ�',indexStr:false},
			{attributename:'DwAddrNameEn',desc:'̨ͷ��λ��ַ��Ӣ�ģ�',indexStr:false},
			{attributename:'DwZipCode',desc:'̨ͷ��λ�ʱ�',indexStr:false},
			{attributename:'DwWebSite',desc:'̨ͷ��λ��ַ',indexStr:false},
			{attributename:'DwTel',desc:'̨ͷ��λ�绰',indexStr:false},
			{attributename:'DwComplainTel',desc:'̨ͷ��λͶ�ߵ绰',indexStr:false},
			{attributename:'DwFax',desc:'̨ͷ��λ����',indexStr:false},
			
			{attributename:'SecurityCodeP1',desc:'��ȫ�루��һ���֣�',indexStr:false},
			{attributename:'SecurityCodeP2',desc:'��ȫ�루�ڶ����֣�',indexStr:false},
			
			{attributename:'TechnicalDocs-All',desc:'�춨����(֤����ҳ)',indexStr:false},
			{attributename:'TechnicalDocs-Code',desc:'�춨����(�����淶)-���',indexStr:true},
			{attributename:'TechnicalDocs-Name',desc:'�춨����(�����淶)-����',indexStr:true},
			
			{attributename:'Standard-Name',desc:'������׼-����',indexStr:true},
			{attributename:'Standard-CertificateCode',desc:'������׼-֤����',indexStr:true},
			{attributename:'Standard-Range',desc:'������׼-������Χ',indexStr:true},
			{attributename:'Standard-Uncertain',desc:'������׼-��ȷ����',indexStr:true},
			{attributename:'Standard-ValidDate',desc:'������׼-��Ч��',indexStr:true},
			
			{attributename:'StandardAppliance-Name',desc:'��׼����-����',indexStr:true},
			{attributename:'StandardAppliance-Range',desc:'��׼����-������Χ',indexStr:true},
			{attributename:'StandardAppliance-Model',desc:'��׼����-�ͺŹ��',indexStr:true},
			{attributename:'StandardAppliance-Uncertain',desc:'��׼����-��ȷ����',indexStr:true},
			{attributename:'StandardAppliance-SeriaNumber',desc:'��׼����-֤����',indexStr:true},
			{attributename:'StandardAppliance-LocaleCode',desc:'��׼����-���ڱ��',indexStr:true},
			{attributename:'StandardAppliance-ValidDate',desc:'��׼����-��Ч��',indexStr:true},
			
			{attributename:'Conclusion',desc:'�춨����',indexStr:false},
			
			{attributename:'AuthorizerPrinter',desc:'��׼�ˣ���ӡ��',indexStr:false},
		    {attributename:'Authorizer',desc:'��׼��',indexStr:false},
			{attributename:'AuthorizerPosition',desc:'��׼��ְ��',indexStr:false},				
			{attributename:'CheckerPrinter',desc:'����Ա����ӡ��',indexStr:false},			
			{attributename:'Checker',desc:'����Ա',indexStr:false},	
			{attributename:'VerifierPrinter',desc:'�춨Ա����ӡ��',indexStr:false},
			{attributename:'Verifier',desc:'�춨Ա',indexStr:false},
		    {attributename:'VerifyYear',desc:'�춨������',indexStr:false},
			{attributename:'VerifyMonth',desc:'�춨������',indexStr:false},	
			{attributename:'VerifyDay',desc:'�춨������',indexStr:false},
		    {attributename:'ValidYear',desc:'��Ч������',indexStr:false},
			{attributename:'ValidMonth',desc:'��Ч������',indexStr:false},
			{attributename:'ValidDay',desc:'��Ч������',indexStr:false},
			{attributename:'SampleRecvYear',desc:'��Ʒ����������',indexStr:false},
			{attributename:'SampleRecvMonth',desc:'��Ʒ����������',indexStr:false},
			{attributename:'SampleRecvDay',desc:'��Ʒ����������',indexStr:false},
			
			{attributename:'WorkLocation',desc:'�����ص�',indexStr:false},	
			{attributename:'Temp',desc:'�춨�¶�',indexStr:false},
		    {attributename:'Humidity',desc:'�춨���ʪ��',indexStr:false},
			{attributename:'Pressure',desc:'�춨����ѹ',indexStr:false}
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
			title:'XML������Ϣ',
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
				{field:'desc',title:'��������',width:120,align:'center', sortable:true},
				{field:'attribute',title:'������',width:120,align:'center'},	
				{field:'tagName',title:'��ǩ��',width:100,align:'center', sortable:true, editor:{
					type:'validatebox',
					options:{
						required:true
					}}
				},
				{field:'indexStr',title:'˳���',width:80,align:'center', editor:'text'}
				
			]],
			rownumbers:true	,
			toolbar:[{
				text:'������ѡ��������XML',
				iconCls:'icon-ok',
				handler:function(){
						$('#xml-config').form('submit',{
						url: '/jlyw/FormatXmlServlet.do?method=2',
						onSubmit:function(){
											
							var rows = $("#XML").datagrid("getSelections");	
							if(rows==null||rows.length==0){
								$.messager.alert('��ʾ��','�ֶ���ϢΪ��','info');
								return false;
							}
							$("#typerows").val(JSON.stringify(rows));
							//return $("#xml-config").form('validate');
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
		$("#xml-config").form('validate');
	   
		var index = $('#XML').datagrid('getRows').length;	//�����һ��������¼			
		if($("#desc").combobox('getValue').length==0||$("#tagName").val().length==0){
			$.messager.alert('����',"��Ϣ��д������!",'error');
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
			<jsp:param name="TitleName" value="֤��XML�ļ�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


<div  style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:950px;heght:200px;padding:10px;"
				title="���������ֶ�" collapsible="false"  closable="false">
	 <form method="post" id="xml-config">
		
		<table width="900">
		
			<tr>			
			 <td  width="15%"  align="right">����������</td>
			 <td align="left" width="17%" ><input name="desc" id="desc" readonly="readonly" style="width:152px" panelHeight="auto"/></td>
			 <td width="12%" align="right">��ǩ���ƣ�</td>
			 <td width="22%" align="left"><input name="tagName" class="easyui-validatebox" id="tagName" type="text" required="true"  ></td> 	
			 <td width="14%" align="right" id="indexStrDivTitle">˳��ţ�</td>
			 <td width="20%" align="left" id="indexStrDivContent"><input name="indexStr" id="indexStr" type="text" style="width:102px" /></td>
			</tr>
			<tr>
			   
			    <td  align="left"><input name="attribute" id="attribute" type="hidden" style="padding-top:10px"/><input name="typerows" id="typerows" type="hidden" style="padding-top:10px"/></td>
			   
			   
				<td  colspan="5" align="center" style="padding-top:10px"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="add()">����ֶ�</a>
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
