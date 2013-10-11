<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>���±���֤�飨�깤ȷ�Ϻ󣩼�¼</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
	<script>
		$(function(){			
			$('#rctable').datagrid({
				title:'���±���֤���б�',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				url:'/jlyw/RemakeCertificateServlet.do?method=8',
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
			$('#paramsStr').val(JSON.stringify($('#rctable').datagrid('options').queryParams));
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
		function reset(){
			$('#query').form('clear');
		}
		function query()
		{
			$('#rctable').datagrid('options').queryParams={'StartTime':encodeURI($('#StartTime').datebox('getValue')),'EndTime':encodeURI($('#EndTime').datebox('getValue'))};
			$('#rctable').datagrid('reload');
		}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="���±���֤�飨�깤ȷ�Ϻ󣩼�¼" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="frm_export" method="post" action="/jlyw/RemakeCertificateServlet.do?method=9">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
	<div id="p" class="easyui-panel" style="width:1000px;height:110px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
                <form id="query">
			<table width="950px" id="table1">
                <tr>
                	<td align="right">��ʼʱ�䣺</td>
				  	<td align="left">
						<input name="StartTime" id="StartTime" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="EndTime" id="EndTime" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr>
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
                    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				</tr>
				
		</table>
        </form>
		</div>
		<br/>
	<div style="width:1000px;height:500px;">
		<table id="rctable" iconCls="icon-search"></table>
	</div>

</DIV></DIV>
</body>
</html>
