<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���������б�</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){			
			$('#task-table').datagrid({
				title:'�����������������б�',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/WithdrawServlet.do?method=3',
				//queryParams:{'CommissionNumber':encodeURI($('#Search_CommissionNumber').val())},
//				sortName: 'id',
//				sortOrder: 'desc',
				remoteSort: false,
				idField:'WithdrawId',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {title:'ί�е���Ϣ',colspan:5,align:'center'},
					{title:'����������Ϣ',colspan:6,align:'center'}
				],[
					{field:'CommissionCode',title:'ί�е���',width:80,align:'center'},
					{field:'ApplianceName',title:'��������',width:80,align:'center'},
					{field:'CustomerName',title:'ί�е�λ',width:120,align:'center'},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					{field:'PromiseDate',title:'��ŵ�������',width:80,align:'center'},
					
					{field:'WithdrawNumber',title:'��������',width:60,align:'center'},
					{field:'Reason',title:'����ԭ��',width:100,align:'center'},
					{field:'WithdrawDesc',title:'������Ʒ����',width:100,align:'center'},
					{field:'RequesterName',title:'������',width:60,align:'center'},
					{field:'RequesterTime',title:'����ʱ��',width:80,align:'center'},
					{field:'ExecutorName',title:'������',width:60,align:'center'}
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#task-table-search-toolbar",
				onLoadSuccess:function(data){
					if(data.rows.length > 0){
						$(this).datagrid('selectRow', 0);
					}
				}
			});
			
		});
		function doTask()
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('��ʾ��',"��ѡ��һ�����������¼��",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#WithdrawId').val(selectedRow.WithdrawId);
			//window.open("","DoWithdrawTask","height=850,width=1024,top=50,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			$('#WithdrawForm').submit();
		}
		function doSearchTaskList()
		{
			$('#task-table').datagrid('options').queryParams={'CommissionNumber':encodeURI($('#Search_CommissionNumber').val()),'CustomerName':encodeURI($('#Search_CustomerName').val())};
			$('#task-table').datagrid('reload');
		}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="����������ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<table width="98%" height="98%" id="task-table" iconCls="icon-search"></table>
	</DIV>
</DIV>
	<form method="post" action="/jlyw/TaskManage/WithdrawApprove.jsp" id="WithdrawForm">
		<input type="hidden" name="Code" id="Code" value="" />
		<input type="hidden" name="Pwd" id="Pwd" value="" />
		<input type="hidden" name="WithdrawId" id="WithdrawId" value="" />
	</form>
	<div id="task-table-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">����</a>
					</td>
				<td style="text-align:right;padding-right:4px">
					<label>ί�е��ţ�</label><input type="text" id="Search_CommissionNumber" value="<%= request.getParameter("commissionsheetcode")==null?"":request.getParameter("commissionsheetcode") %>" style="width:120px" />&nbsp;<label>ί�е�λ��</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ�Ҵ���������������" id="btnHistorySearch" onclick="doSearchTaskList()">��ѯ��������</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
