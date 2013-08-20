<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��Ʒ������Ϣ��ѯ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script>
		$(function(){
		    var lastIndex;		
			$('#RepairRecord').datagrid({
			    width:900,
				height:500,
				title:'��Ʒ������Ϣ',
//				iconCls:'icon-save',
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'repairrecord.json',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'id',
				columns:[[
					{field:'id',title:'������',width:80,sortable:true,align:'center'},
					{field:'commissionid',title:'ί�е����',width:80,align:'center'},
					{field:'consumername',title:'ί�е�λ',width:120,align:'center'},
					{field:'appliancename',title:'��������',width:100,align:'center'},
					{field:'item',title:'������',width:80,align:'center'},
					{field:'assemblyname',title:'�������',width:100,align:'center'},
					{field:'assemblyfee',title:'�������',width:80,align:'center'},
					{field:'fee',title:'�������',width:80,align:'center'},
					{field:'date',title:'����ʱ��',width:100,align:'center'}
                ]],
				pagination:true,
				rownumbers:true
				
			});

		});
		function ok(){
			 $('#allot').form('submit',{
				//url: 'userAdd.action',
				onSubmit:function(){ return $('#allot').form('validate');},
		   		success:function(){
			   		 close1();
		   		 }
			});
		}
	</script>
</head>

<body>
	<br />
<div  align="center" style="width:900px;height:40px;" >
	     <h2>��Ʒ������Ϣ��ѯ</h2>
</div>
   <br />
<div >
     <div id="p" class="easyui-panel" style="width:900px;height:90px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
			<table width="850px" id="table1">
				<tr>
					<td width="10%" align="right">ί�е���ţ�</td>
					<td width="22%" align="left">
						<input id="id" class="easyui-combobox" name="type" url="commissionid.json" style="width:150px;" valueField="id" textField="text" panelHeight="auto" >
					</td>
					<td width="10%" align="right"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="ok()">��ѯ</a></td>
				    <td width="21%" align="left"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="goback()">����</a></td>
				</tr>
				
		</table>
		</div>
		<br />
		
      <div style="width:900px;height:500px;">
	     <table id="RepairRecord" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>

</div>
</body>
</html>
