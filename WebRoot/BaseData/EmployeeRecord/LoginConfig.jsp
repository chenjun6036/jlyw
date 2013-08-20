<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�û���¼����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	 	$(function(){			
			$('#queryname').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
		});
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/UserServlet.do?method=0';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').combobox('getValue'))};
			$('#table2').datagrid('reload');
		}
				
	 
		$(function(){
			$('#table2').datagrid({
				title:'��Ա��ѯ',
				width:900,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/UserServlet.do?method=0',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'JobNum',title:'����',width:80,align:'center'},
					{field:'Name',title:'����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NeedDongle',title:'�Ƿ���Ҫ�����',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.NeedDongle==1)
							return '��';
						else
							return '��';
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'���ĵ�¼����',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){	
							$.messager.confirm('��ʾ','ȷ��ִ����',function(r){
								if(r){						
									$.ajax({
										type:'POST',
										url:'/jlyw/UserServlet.do?method=11',
										data:'id='+select.Id,
										dataType:"html",
										success:function(data){
											$('#table2').datagrid('reload');
										}
									});
								}
								});
							}
							else
							{
								$.messager.alert('��ʾ','��ѡ��һ��Ա��','warning');
							}
						}
				},'-',{
						text:'���������û���¼����',
						iconCls:'icon-edit',
						handler:function(){
							$.messager.confirm('��ʾ','ȷ��ִ����',function(r){
								if(r){						
									$.ajax({
										type:'POST',
										url:'/jlyw/UserServlet.do?method=12',
										dataType:"html",
										success:function(data){
											$('#table2').datagrid('reload');
										}
									});
								}
								});
						}
				}
				]
			});
			
		});
		
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�û���¼����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:900px">
				<tr>
                	<td></td>
					<td align="right">Ա����</td>
				  	<td align="left"><input id="queryname" name="queryname" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    <td align="left"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">��ѯ</a></td>
                    </tr>
                </table>
		</div>
		
        <div>
			<table id="table2" style="height:500px; width:900px"></table>
        </div>
	</div>
    </DIV>
    </DIV>
</body>
</html>
