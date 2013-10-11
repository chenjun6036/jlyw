<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ҵ���ѯ</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
      <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
	$(function(){
	$("#Customer").combobox({
		valueField:'id',
		textField:'name',
		required:true,
		onSelect:function(record){},
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
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $('#Customer').combobox('getText');
					$('#Customer').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}, 500);
		}
	});
	});
		$(function(){
			$('#table6').datagrid({
			    width:750,
				height:500,
				title:'ҵ���ѯ���',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/CrmServlet.do?method=37',
				remoteSort: false,
				//frozenColumns:[[{field:'ck',checkbox:true}]],
				columns:[[
				{field:'Id',title:'ID',width:80,align:'center' ,hidden:true },
				{field:'Name',title:'ί�е�λ',width:200,align:'center'},
					{field:'Total',title:'�ܲ�ֵ',width:80,align:'center'},
					{field:'Avg',title:'ƽ��ÿ�η���',width:100,align:'center',sortable:true},
					{field:'Avg1',title:'ƽ��ÿ������',width:100,align:'center',sortable:true},
					{field:'Counter',title:'ҵ�����',width:70,align:'center'},
					{field:'Percentage',title:'ռ�ܲ�ֵ�İٷֱ�',width:180,align:'center'},
					{field:'ValueLevel',title:'��ֵ�ȼ�',width:70,align:'center'},
					{field:'Level',title:'�ͻ�����',width:70,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 1 || value == '1')
							{
								rowData['Level']=1;
							    return "VIP�ͻ�";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Level']=2;
							    return "�ص�ͻ�";
							}
							else if(value == 3 || value == '3')
							{
								rowData['Level']=3;
							    return "��Ҫ�ͻ�";
							}
							else if(value == 4 || value == '4')
							{
								rowData['Level']=4;
							    return "һ��ͻ�";
							}
							
						}}
					
                ]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				
				toolbar:[{
					text:'���ÿͻ�����',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table6').datagrid('getSelected');
						if(select){
						$('#editLevel').window('open');
						$('#form2').show();
						//$('#Id').val(select.Id);
						$('#form2').form('clear');
						$('#form2').form('load',select);
						$('#form2').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				}
				,'-',{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
						//$('#PrintStr').val(JSON.stringify($('#table6').datagrid('options').queryParams));
						//$('#formLook').submit();
						myExport();
					}
				}]
				
			});
			
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/CrmServlet.do?method=37';
			$('#table6').datagrid('options').queryParams={'Id':$('#Customer').combobox('getValue')};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
			
		}
		
		function set()
		{
			//$('#editLevel').dialog('close');
			$('#form2').form('submit',{
				url: '/jlyw/CrmServlet.do?method=40',
				onSubmit:function(){ return $('#form2').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				if(result.IsOk)
		   					$('#table6').datagrid('reload');
		   					closed();
		   		 }
			});
		}
		function closed()
		{
			$('#editLevel').dialog('close');
		}
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#par').val(JSON.stringify($('#table6').datagrid('options').queryParams));
			$('#frm_export').form('submit',{
				success:function(data){
					var result = eval("("+ data +")");
					if(result.IsOk)
					{
					//alert();
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
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ȼ�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=1">
		<input id="par" name="Par" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden"/>
		</form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:750px;height:120px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
                <form id="query">
			<table width="700px" id="table1">
				<tr height="30px">
					<td width="20%" align="right">ί�е�λ��</td>
					<td width="22%" align="left">
						<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;"/>
					</td>
				</tr >

                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
                    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				</tr>
				
		</table>
        </form>
		</div>
        <br />
      <div style="width:750px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="7500px" height="500px" ></table>
	  </div>


	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=14" target="PrintFrame" accept-charset="utf-8" >
    
    	<input id="PrintStr"  name="PrintStr" style="width:150px;" type="hidden"/>
				
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
<div id="editLevel" class="easyui-window" title="���ÿͻ�����" style="padding: 10px;width: 350px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					
						<tr>
							<td align="right">ί�е�λ��</td>
							<td align="left" colspan="3"><input style="width:200px;" id="customerName" name="Name" class="easyui-validatebox"  />
							<input id="Id" name="Id" type="hidden"/>                    
                    </td>
                        </tr>
                        <tr></br></tr>
                        <tr>
                         <td>�ͻ����ࣺ
                    </td>
                    <td>
                    <select id="Level" name="Level" class="easyui-combobox" required="true" panelHeight="auto">
					<option value="1">VIP�ͻ�</option>
					<option value="2">�ص�ͻ�</option>
					<option value="3">��Ҫ�ͻ�</option>
					<option value="4">һ��ͻ�</option>
					</select></td>
                       </tr>
						
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="set()">����</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
</DIV>
</DIV>
</body>
</html>
