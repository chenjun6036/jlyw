<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>延期审批列表</title>
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
				title:'待审批的延期申请列表',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/OverdueServlet.do?method=3',
				//queryParams:{'CommissionNumber':encodeURI($('#Search_CommissionNumber').val())},
//				sortName: 'id',
//				sortOrder: 'desc',
				remoteSort: false,
				idField:'OverdueId',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {title:'委托单信息',colspan:5,align:'center'},
					{title:'延期申请信息',colspan:5,align:'center'}
				],[
					{field:'CommissionCode',title:'委托单号',width:80,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:100,align:'center'},
					{field:'CustomerName',title:'委托单位',width:120,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'PromiseDate',title:'承诺检出日期',width:80,align:'center'},
					
					{field:'DelayDays',title:'延期天数',width:60,align:'center'},
					{field:'Reason',title:'延期原因',width:120,align:'center'},
					{field:'RequesterName',title:'申请人',width:60,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
					{field:'ExecutorName',title:'办理人',width:60,align:'center'}
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#task-table-search-toolbar",
				onLoadSuccess:function(data){
					if(data.rows.length == 1){
						$(this).datagrid('selectRow', 0);
					}
				}
			});
			
		});
		function doTask()
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('提示！',"请选择一条延期申请记录！",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#OverdueId').val(selectedRow.OverdueId);
			//window.open("","DoOverdueTask","height=780,width=1024,top=50,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			$('#OverdueForm').submit();
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
			<jsp:param name="TitleName" value="延期审批查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

		<table width="98%" height="98%" id="task-table" iconCls="icon-search" ></table>
	<form method="post" action="/jlyw/TaskManage/OverdueApprove.jsp" id="OverdueForm">
		<input type="hidden" name="Code" id="Code" value="" />
		<input type="hidden" name="Pwd" id="Pwd" value="" />
		<input type="hidden" name="OverdueId" id="OverdueId" value="" />
	</form>
	<div id="task-table-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="doTask()">审批</a>
					</td>
				<td style="text-align:right;padding-right:4px">
					<label>委托单号：</label><input type="text" id="Search_CommissionNumber" value="<%= request.getParameter("commissionsheetcode")==null?"":request.getParameter("commissionsheetcode") %>" style="width:120px" />&nbsp;<label>委托单位：</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询我待审批的延期申请" id="btnHistorySearch" onclick="doSearchTaskList()">查询延期申请</a>
				</td>
			</tr>
		</table>
	</div>
	
</DIV></DIV>
</body>
</html>
