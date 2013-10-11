<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>重新编制证书（完工确认后）记录</title>
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
				title:'重新编制证书列表',
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
			        {title:'委托单信息',colspan:6,align:'center'},
					{title:'证书编制任务创建信息',colspan:4,align:'center'},
					{title:'证书编制信息',colspan:3,align:'center'},
					{title:'完成情况审核',colspan:1,align:'center'}
				],[
					{field:'CommissionCode',title:'委托单号',width:100,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:100,align:'center'},
					{field:'CustomerName',title:'委托单位',width:150,align:'center'},
					{field:'CustomerContactor',title:'联系人',width:60,align:'center'},
					{field:'CustomerContactorTel',title:'联系电话',width:80,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					
					{field:'CertificateCode',title:'原证书编号',width:120,sortable:true,align:'center'},
					{field:'CreatorName',title:'创建人姓名',width:100,align:'center'},
					{field:'CreateTime',title:'创建时间',width:80,align:'center'},
					{field:'CreateRemark',title:'备注',width:150,align:'center'},
					
					
					{field:'ReceiverName',title:'编制人',width:100,align:'center'},
					{field:'FinishTime',title:'完成时间',width:80,align:'center'},
					{field:'FinishRemark',title:'备注',width:150,align:'center'},
					
					{field:'PassedTime',title:'通过时间',width:80,align:'center'}
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'导出',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				}]
			});
			
		});
		function myExport(){
			
			ShowWaitingDlg("正在导出，请稍后......");
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
						$.messager.alert('提示','导出失败，请重试！','warning');
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
			<jsp:param name="TitleName" value="重新编制证书（完工确认后）记录" />
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
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="950px" id="table1">
                <tr>
                	<td align="right">起始时间：</td>
				  	<td align="left">
						<input name="StartTime" id="StartTime" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="EndTime" id="EndTime" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr>
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
                    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
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
