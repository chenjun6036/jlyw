<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>超期信息查询</title>
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
				title:'超期信息',
				singleSelect:true, 
				fit: true,
				nowrap: false,
				striped: true,
				//border:false,
				url:'/jlyw/TaskAssignServlet.do?method=11',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: true,	//默认按服务器的排序
	//			idField:'id',
				columns:[
					[
						{field:'Allotte',title:'派定人',width:80,align:'center'},
						{field:'Days',title:'超期天数',width:60,align:'center'},
						{field:'Code',title:'委托单号',width:80,align:'center'},
						{field:'Customer',title:'委托单位',width:160,align:'center'},
						{field:'CommissionDate',title:'委托日期',width:100,align:'center'},
						{field:'PromiseDate',title:'承诺日期',width:100,align:'center'},
						{field:'ApplianceSpeciesName',title:'器具授权名',width:100,align:'center'},
						{field:'ApplianceName',title:'器具名称',width:100,align:'center'},
						{field:'Quantity',title:'数量',width:100,align:'center'},
						{field:'WithQuantity',title:'退样数量',width:100,align:'center'},
						{field:'Status',title:'状态',width:100,align:'center'},
						{field:'LocaleSiteManager',title:'现场负责人',width:100,align:'center'},
						{field:'OtherRequirements',title:'其他要求',width:100,align:'center'},
						{field:'Remark',title:'备注',width:100,align:'center'}	
					]
				],
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
						$.messager.alert('提示','导出失败，请重试！','warning');
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
			<jsp:param name="TitleName" value="超期信息查询" />
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
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="880px" id="table1">
				<tr height="30px">
					<td width="10%" align="right">承诺期：</td>
					<td width="22%" align="left">
						<input id="StartDate" class="easyui-datebox" name="StartDate" style="width:150px;"/>
					</td>
					<td width="20%" align="center">--------------------</td>
					<td width="22%" align="left">
						<input id="EndDate" class="easyui-datebox" name="EndDate" style="width:150px;"/>
					</td>
					<td width="22%" align="left">
					<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a>
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
