<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	 <title>��ɫ��Ϣ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />

    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		    var lastIndex;
		 $("#Parent").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
			   $("#ParentId").val(record.id);
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
				$('#ParentId').val("");
			}	
		});
		
			$("#rolename").combobox({
				valueField:'name',
				textField:'name',
				required:true,
				width:200,
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
			$('#role').datagrid({
				title:'��ɫ��Ϣ',
//				iconCls:'icon-save',
				//width:700,
				//height:350,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/RoleServlet.do?method=2',
				sortName: 'Id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'��ɫ����',width:150},
					{field:'Parent',title:'����ɫ����',width:150},
					{field:'Description',title:'��ɫ����',width:150}				
				]],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//����
						return 'color:#000000';
					}else 
						return 'color:#FF0000';	
					
				},
				pagination:true,
				rownumbers:true,
				toolbar:[{
					  text:'�޸�',
					  iconCls:'icon-edit',
					  handler:function(){
						var select = $('#role').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#ff').show();
						$('#roleId').val(select.Id);
						$('#name').val(select.Name);
						$('#Description').val(select.Description);
						$('#Parent').combobox('setValue',select.Parent);
						$('#Privilege').val(select.Privilege);
						$('#ParentId').val(select.ParentId);
						
					}else{
						$.messager.alert('warning','��ѡ��һ������','warning');
			   }
					}
				},'-',{
					text:'ɾ��',
					iconCls:'icon-remove',
					handler:function(){
						var rows = $('#role').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('����','ȷ��ɾ����',function(r){
								if(r){
									for(var i=rows.length-1; i>=0; i--){
										$.ajax({
											type:'POST',
											url:'/jlyw/RoleServlet.do?method=4',
											data:'id='+rows[i].Id,
											dataType:"json",
											success:function(data, textStatus){
												
												$.messager.alert('��ʾ',data.msg,'info');
											}
										});
									}
									$('#role').datagrid('reload');
								}
								});
							}else{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
					if (lastIndex != rowIndex){
						$('#role').datagrid('endEdit', lastIndex);
						$('#role').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
				}
			});
		});
		function closed(){
			$('#edit').window('close');
			
		}
		function edit(){
			$('#ff').form('submit', {
				url:'/jlyw/RoleServlet.do?method=3',
				success : function(data) {
					var result = eval("("+data+")");
		   			alert(result.msg);
		   			if(result.IsOK)
		   				close();
		   			$('#role').datagrid('reload');
				},
				onSubmit : function() {
					return $(this).form('validate');
				}
			});
		}
		function query()
		{
			$('#role').datagrid('options').url='/jlyw/RoleServlet.do?method=2';
			$('#role').datagrid('options').queryParams={'queryname':encodeURI($('#rolename').combobox('getValue'))};
			$('#role').datagrid('clearSelections');
			$('#role').datagrid('reload');
		}
	</script>

</head>

<body >
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ɫ��Ϣ��ɾ��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	<table width="98%" height="90%" border="0">
		<tr>
			<td width="92%" style="padding-top:15px; padding-right:12px; vertical-align:bottom; overflow:visible" align="right">
			<form id="eeff" method="post">
				<div>
					
					<label id="label_dd" >��ɫ����:</label>
					<div id="rolename" style="display:inline">
						<input id="rolename" class="easyui-combobox" name="rolename" type="text"  style="width:200px;"></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">����</a>
				</div>
			</form>
			</td>
		</tr>
		<tr height="650px">
			<td >
				<table id="role" iconCls="icon-edit" ></table>
			</td>
		</tr>
	</table>
	<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 320;height: 250;" 
    iconCls="icon-edit" closed="true" modal="true" maximizable="false" minimizable="false" collapsible="false">
    	<div id="ee">
		
			<form id="ff" method="post">
				 <input type="hidden" name="roleId" id="roleId" value="" />
					  <div>
						��  &nbsp;  ɫ&nbsp; ��:<input id="name" style="width:200px"  name="Name" class="easyui-validatebox" required="true"></input>
					 </div>
					 <div>
						 ����ɫ ����:<input id="Parent"  name="Parent"  style="width:205px;"></input><input type="hidden" name="ParentId" id="ParentId" value="" />
					 </div>
					 <div >
						<table>
					 <tr>
					 	<td>
							<label style="valign:top" >Ȩ �� �� ��:</label>
						</td>
						<td>
							<textarea id="Description" style="width:200px;height:100px"  name="Description"  ></textarea>
					 	</td>
					 </tr>
					 </table>
					 </div>
			 </form>
    	</div>
	    <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="edit()">�޸�</a>
	    <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="closed()">�ر�</a>
    </div>




</DIV>
</DIV>
</body>
</html>
