<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��δ��ɵġ����±���֤�飨�깤ȷ�Ϻ󣩡�����</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){			
			$('#task-table').datagrid({
				title:'���±���֤�������б�',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/RemakeCertificateServlet.do?method=4',
				queryParams:{'CertificateCode':encodeURI($('#Search_CertificateCode').val())},
//				sortName: 'id',
//				sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
			        {title:'ί�е���Ϣ',colspan:6,align:'center'},
					{title:'֤��������񴴽���Ϣ',colspan:4,align:'center'},
					{title:'֤�������Ϣ',colspan:3,align:'center'},
					{title:'���������',colspan:1,align:'center'}
				],[
					{field:'CommissionCode',title:'ί�е���',width:100,align:'center'},
					{field:'ApplianceName',title:'��������',width:100,align:'center'},
					{field:'CustomerName',title:'ί�е�λ',width:150,align:'center'},
					{field:'CustomerContactor',title:'��ϵ��',width:60,align:'center'},
					{field:'CustomerContactorTel',title:'��ϵ�绰',width:80,align:'center'},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					
					{field:'CertificateCode',title:'ԭ֤����',width:120,sortable:true,align:'center'},
					{field:'CreatorName',title:'����������',width:100,align:'center'},
					{field:'CreateTime',title:'����ʱ��',width:80,align:'center'},
					{field:'CreateRemark',title:'��ע',width:150,align:'center'},
					
					
					{field:'ReceiverName',title:'������',width:100,align:'center'},
					{field:'FinishTime',title:'���ʱ��',width:80,align:'center'},
					{field:'FinishRemark',title:'��ע',width:150,align:'center'},
					
					{field:'PassedTime',title:'ͨ��ʱ��',width:80,align:'center'}
					
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
				$.messager.alert('��ʾ��',"��ѡ��һ������",'info');
				return false;
			}
			$('#Code').val(selectedRow.CommissionCode);
			$('#Pwd').val(selectedRow.CommissionPwd);
			$('#TaskId').val(selectedRow.TaskId);
			$('#TaskForm').submit();
		}
		function doOpenFinishWindow()
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('��ʾ��',"��ѡ��һ������",'info');
				return false;
			}
			$('#FinishForm-RemakeCertificateId').val(selectedRow.Id);
			$('#FinishForm-FinishRemark').val('');
			$("#finish_window").window('open');
		}
		function doFinishTask()	//ȷ�����
		{
			$("#FinishForm").form('submit', {
				url:'/jlyw/RemakeCertificateServlet.do?method=6',
				onSubmit: function(){
					return $("#FinishForm").form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK){
						$('#task-table').datagrid('reload');
						$("#finish_window").window('close');
					}else{
						$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
					}
				}
			});  
		}
		function doSearchTaskList()
		{
			$('#task-table').datagrid('options').queryParams={'CertificateCode':encodeURI($('#Search_CertificateCode').val()),'CustomerName':encodeURI($('#Search_CustomerName').val())};
			$('#task-table').datagrid('reload');
		}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��δ��ɵġ����±���֤�飨�깤ȷ�Ϻ󣩡�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	
		<table id="task-table" iconCls="icon-search"></table>
		<form method="post" action="/jlyw/TaskManage/ComSheetInspect.jsp" id="TaskForm">
		  <input type="hidden" name="Code" id="Code" value="" />
		  <input type="hidden" name="Pwd" id="Pwd" value="" />
		  <input type="hidden" name="TaskId" id="TaskId" value="" />
		</form>

<div id="task-table-search-toolbar" style="padding:2px 0">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doTask()">����֤��</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="doOpenFinishWindow()">ȷ�����</a>
			</td>
			<td style="text-align:right;padding-right:4px">
				<label>֤���ţ�</label><input type="text" id="Search_CertificateCode" value="<%= request.getParameter("CertificateCode")==null?"":request.getParameter("CertificateCode") %>" style="width:120px" />&nbsp;<label>ί�е�λ��</label><input type="text" id="Search_CustomerName" value="" style="width:120px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��δ��ɵļ�������" id="btnHistorySearch" onclick="doSearchTaskList()">��ѯ����</a>
			</td>
		</tr>
	</table>
</div>

<div id="finish_window" class="easyui-window" closed="true" modal="true" title="�����¼��" iconCls="icon-save" style="width:500px;height:250px;padding:5px;">
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
			<form method="post" id="FinishForm">
				<input type="hidden" name="Id" id="FinishForm-RemakeCertificateId" value="" />
				<table width="400" style="margin-left:20px" >
					<tr>
						<td width="20%" align="right">��ע��Ϣ��</td>
						<td width="80%" rowspan="2" align="left"><textarea id="FinishForm-FinishRemark" style="width:300px;height:80px"  name="FinishRemark" class="easyui-validatebox" required="true" ></textarea></td>
					</tr>
					<tr>
						<td></td>
					</tr>
			  </table>
			</form>
		</div>
		<div region="south" border="false" style="text-align:center;height:50px;line-height:30px;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doFinishTask()" >ȷ�����</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onclick="$('#finish_window').window('close');">ȡ��</a>
		</div>
	</div>
</div>

</DIV></DIV>
</body>
</html>
