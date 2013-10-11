<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>������Ϣ��ѯ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../JScript/upload.js"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
	<script>
		$(function(){
			$('#overdue').datagrid({
				title:'������Ϣ',
				singleSelect:true, 
				fit: true,
				nowrap: false,
				striped: true,
				//border:false,
				url:'/jlyw/TaskAssignServlet.do?method=11',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: true,	//Ĭ�ϰ�������������
	//			idField:'id',
				columns:[
					[
						{field:'Allotte',title:'�ɶ���',width:80,align:'center'},
						{field:'Days',title:'��������',width:60,align:'center'},
						{field:'Code',title:'ί�е���',width:80,align:'center'},
						{field:'Customer',title:'ί�е�λ',width:160,align:'center'},
						{field:'CommissionDate',title:'ί������',width:100,align:'center'},
						{field:'PromiseDate',title:'��ŵ����',width:100,align:'center'},
						{field:'ApplianceSpeciesName',title:'������Ȩ��',width:100,align:'center'},
						{field:'ApplianceName',title:'��������',width:100,align:'center'},
						{field:'Quantity',title:'����',width:100,align:'center'},
						{field:'WithQuantity',title:'��������',width:100,align:'center'},
						{field:'Status',title:'״̬',width:100,align:'center'},
						{field:'LocaleSiteManager',title:'�ֳ�������',width:100,align:'center'},
						{field:'OtherRequirements',title:'����Ҫ��',width:100,align:'center'},
						{field:'Remark',title:'��ע',width:100,align:'center'}	
					]
				],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				}]
			});
			
		});
		
		function myExport(){
			
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#overdue').datagrid('options').queryParams));
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
		
		function query(){
			$('#overdue').datagrid('options').url='/jlyw/TaskAssignServlet.do?method=11';
			$('#overdue').datagrid('options').queryParams={'StartDate':encodeURI($('#StartDate').datebox('getValue')),'EndDate':encodeURI($('#EndDate').datebox('getValue'))};
			$('#overdue').datagrid('reload');
		}
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="������Ϣ��ѯ" />
		</jsp:include>
	</DIV>
	<form id="frm_export" method="post" action="/jlyw/TaskAssignServlet.do?method=12">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
	<div id="p" class="easyui-panel" style="width:900px;height:100px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
                <form id="query">
			<table width="880px" id="table1">
				<tr height="30px">
					<td width="10%" align="right">��ŵ�ڣ�</td>
					<td width="22%" align="left">
						<input id="StartDate" class="easyui-datebox" name="StartDate" style="width:150px;"/>
					</td>
					<td width="20%" align="center">--------------------</td>
					<td width="22%" align="left">
						<input id="EndDate" class="easyui-datebox" name="EndDate" style="width:150px;"/>
					</td>
					<td width="22%" align="left">
					<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a>
					</td>
				</tr >
		</table>
        </form>
		</div>
	<br/>
      <div style="width:900px;height:500px;">
	     <table id="overdue" iconCls="icon-tip"></table>
	  </div>
</div>
</DIV>
</DIV>
</body>
</html>
