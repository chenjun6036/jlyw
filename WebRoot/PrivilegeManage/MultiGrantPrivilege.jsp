<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>һ��Ȩ�޷���������ɫ</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />

    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		$("#rolename").combobox({
				valueField:'name',
				textField:'name',
				required:true,
				onSelect:function(record){
				},
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
					$(this).combobox('reload','/jlyw/RoleServlet.do?method=8&QueryName='+encodeURI(newValue));
				}
			});
		$("#privilegename").combobox({
			valueField:'name',
			textField:'name',
			required:true,
			onSelect:function(record){
			},
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
				$(this).combobox('reload','/jlyw/PrivilegeServlet.do?method=8&QueryName='+encodeURI(newValue));
			}
		});	     
			$('#privilege').datagrid({
				title:'Ȩ����Ϣ',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				collapsible:false,
				pagination:true,
				rownumbers:true,
				url:'/jlyw/PrivilegeServlet.do?method=2',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'ID',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'Ȩ������',width:150},
					{field:'ResourcesName',title:'��Ӧ��Դ����',width:150},
					{field:'MappingUrl',title:'��Ӧ��ԴURL',width:150},
					{field:'Parameters',title:'��ԴURL�Ĳ���',width:150},
					{field:'Description',title:'Ȩ������',width:150}
				]],
				onDblClickRow:function(rowIndex, rowData)
				{
					var clickname=rowData.Name;
					clickname="Ȩ�� '"+clickname+"' �Ľ�ɫ";
					$('#privilegerole').datagrid({title:clickname,url:""});
					$('#privilegerole').datagrid('loadData',{"total":0,"rows":[]});
					$('#privilegerole').datagrid('options').url='/jlyw/PrivilegeServlet.do?method=9'; //��ѯ��Ȩ�޶�Ӧ�Ľ�ɫ
					$('#privilegerole').datagrid('options').queryParams={'privilegeId':encodeURI(rowData.Id)};
					$('#privilegerole').datagrid('reload');
				},
				onLoadSuccess:function(data){
					//$('#privilege').datagrid('selectRow',0);
					//$('#privilegerole').datagrid('reload',{'privilegeId':encodeURI($('#privilege').datagrid('getRows')[0].Id)});
				}
			});

			$('#privilegerole').datagrid({
				title:'Ȩ�޵Ķ�Ӧ��ɫ',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/PrivilegeServlet.do?method=9',
				
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'Name',title:'��ɫ����',width:150},
					{field:'Description',title:'��ɫ����',width:150}
				
				]],
				toolbar:[{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
						var select = $('#privilege').datagrid('getSelected');
						var rows = $('#privilegerole').datagrid('getRows');
						var length = rows.length;
						if(length==0)
						{
							$.messager.alert('����',"������ѡ��һ���û�",'warning');
							return;
						}
						for(var i=length-1; i>=0; i--){
							$.ajax({
								type:'POST',
							    url:'/jlyw/PrivilegeServlet.do?method=5',         
								data:"privileges=" + select.Id + "&roleId=" + rows[i].Id,
								dataType:"json",
								async:false,
								success:function(data){
									//var result = eval("("+data+")");
		   							//	alert(result.msg);
		   							$('#privilegerole').datagrid('options').queryParams={'privilegeId':encodeURI(select.Id)};
		   							$('#privilegerole').datagrid('reload');
								}
							});
						}
						alert("��Ȩ�ɹ�");
						$('#privilegerole').datagrid('clearSelections');
						
					}
				},'-',{
					text:'ע��',
					iconCls:'icon-cancel',
					handler:function(){
						var rows = $('#privilegerole').datagrid('getSelections');
						var length = rows.length;
						if(length==0)
						{
							$.messager.alert('����',"������ѡ��һ����ɫ",'warning');
							return;
						}
						for(var i=length-1; i>=0; i--){
							if(rows[i].RolePrivilegeId < 0){
								var index = $('#privilegerole').datagrid('getRowIndex', rows[i]);
								$('#privilegerole').datagrid('deleteRow', index);
							}else{
								$.ajax({
									type:'POST',
									url:'/jlyw/PrivilegeServlet.do?method=7',
									data:{"RolePrivilegeId":rows[i].RolePrivilegeId},
									dataType:"json",
									async:false,
									success:function(data){
										
									}
								});
								var index = $('#privilegerole').datagrid('getRowIndex', rows[i]);
								$('#privilegerole').datagrid('deleteRow', index);
							}
						}
						alert("ȡ����Ȩ�ɹ�");
						$('#privilegerole').datagrid('clearSelections');
						$('#privilegerole').datagrid('reload');
					}
				}
				]
			});
			$('#roles').datagrid({
				title:'��ɫ��Ϣ',
//				iconCls:'icon-save',
				//width:800,
				//height:350,
				pagination:true,
				//rownumbers:true,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/RoleServlet.do?method=2',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				
				columns:[[
					
					{field:'Name',title:'��ɫ����',width:150},
					//{field:'ParentId',title:'����ɫ���',width:80},
					{field:'Description',title:'��ɫ����',width:150}
					
				]],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//����
						return 'color:#000000';
					}else 
						return 'color:#FF0000';	
					
				},
				toolbar:"#table-search-toolbar"
				
			});
		});
		function query()
		{
			$('#privilege').datagrid('options').url='/jlyw/PrivilegeServlet.do?method=2';
			$('#privilege').datagrid('options').queryParams={'queryname':encodeURI($('#privilegename').combobox('getValue'))};
			$('#privilege').datagrid('clearSelections');
			$('#privilege').datagrid('reload');
		}
		function addup()
		{
			var rows = $('#roles').datagrid('getSelections');
						
			var row = $('#privilegerole').datagrid('getRows');
			var index = row.length;
			
			for(var i=0; i<rows.length; i++){
				var j;
				for(j=0; j<index; j++)
				{
					if(rows[i].Id==row[j].Id)
						break;
				}
				if(index!=j)
					continue;
				$('#privilegerole').datagrid('insertRow', {
					index: index,
					row:{
						RolePrivilegeId:-1,
						Id:rows[i].Id,
						Name:rows[i].Name,
						Description:rows[i].Description,
						Privilege:rows[i].Privilege
					}
				});
				index++;
			}
			$('#roles').datagrid('clearSelections');
		}
		function query1()
		{
			$('#roles').datagrid('options').url='/jlyw/RoleServlet.do?method=2';
			$('#roles').datagrid('options').queryParams={'queryname':encodeURI($('#rolename').combobox('getValue'))};
			$('#roles').datagrid('clearSelections');
			$('#roles').datagrid('reload');
		}
	</script>

</head>

<body  >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��һ��Ȩ�޸��������ɫ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

<div border="true" style="width:900px;overflow:hidden;+position:relative;">
    <div id="p" class="easyui-panel" style="width:900px;height:100px;"
				title="��ѯ����" collapsible="false"  closable="false">
	<table width="100%" height="100%">
		<tr height="3%">
			<td width="100%" style="overflow:visible" align="right">
				<div align="left">
					<label id="label_dd" for="dd">Ȩ����:</label>
					<div id="div_privilegename" style="display:inline">
						<input id="privilegename" class="easyui-combobox" name="privilegename" valueField="id" textField="text" panelHeight="auto" style="width:200px;"></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">Search</a>
				</div>
			</td>
		</tr>
	</table> 	
  </div>
<div style="width:900px;height:400px;">	
	<table id="privilege" iconCls="icon-edit" width="900px" height="400px" singleSelect="true" ></table>
</div>

<div   border="true" style="width:900px;height:300px;overflow:hidden;">
	<table id="privilegerole" class="easyui-datagrid" iconCls="icon-edit"></table>
	
</div>	
<div border="true" style="width:900px;height:400px;overflow:hidden;">
	<table id="roles" class="easyui-datagrid" iconCls="icon-edit"></table>
</div>
<form id="frm_privilege_role" method="post">
<input id="sub_privilege" name="privilegeId" type="hidden"/>
<input id="sub_roles" name="roles" type="hidden"/>
</form>	
</div>	


</DIV>
</DIV>
<div id="table-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addup()">�ύѡ��Ľ�ɫ</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>��ɫ���ƣ�</label><input id="rolename" class="easyui-combobox" name="rolename" style="width:200px;" panelHeight="auto" ></input>&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��ɫ" id="SearchLocaleMission" onclick="query1()">��ѯ��ɫ</a>
					</td>
				</tr>
			</table>
</div>

</body>
</html>