<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>转包业务查询</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
	<script>
		$(function(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
			$('#SubContractor').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'name',
				textField:'name',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/SubContractServlet.do?method=0&QueryName='+newValue);
				}
			});
				
			$('#table6').datagrid({
			    width:900,
				height:500,
				title:'转包业务查询结果',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'委托单号',width:100,align:'center'},
					{field:'SubContractorName',title:'转包方单位名称',width:180,align:'center'},
					{field:'SubContractorContactor',title:'转包方联系人',width:80,align:'center'},
					{field:'SubContractorContactorTel',title:'联系人电话',width:80,align:'center'},
					{field:'SubContractDate',title:'转包时间',width:80,align:'center'},
					{field:'Handler',title:'转包人',width:80,align:'center'},
					{field:'TotalFee',title:'转包费用',width:80,align:'center'},
					{field:'ReceiveDate',title:'接收时间',width:80,align:'center'},
					{field:'Receiver',title:'接收人',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,align:'center'},
					{field:'Attachment',title:'附件',width:80,align:'center'}
					
                ]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				toolbar:[{
					text:'查看委托单明细',
					iconCls:'icon-search',
					handler:function(){
						var select  = $('#table6').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('提示','请选择一行数据！','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm').submit();
					}
				}]
			});
			
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/SubContractServlet.do?method=4';
			$('#table6').datagrid('options').queryParams={'SubContractor':encodeURI($('#SubContractor').combobox('getValue')),'DateFrom':encodeURI($('#dateTimeFrom').datebox('getValue')),'DateEnd':encodeURI($('#dateTimeEnd').datebox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
		}
		
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="转包业务查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:125px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="850px" id="table1">
				<tr height="30px">
					<td width="14%" align="right" >转包方：</td>
					<td width="22%" align="left" >
						<input id="SubContractor" class="easyui-combobox" name="SubContractor" url="" style="width:150px;" valueField="name" textField="name" panelHeight="auto" />
					</td>
					<td width="10%" align="right">起始时间：</td>
					<td width="22%" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" />
					</td>
					<td width="10%" align="right">结束时间：</td>
					<td width="22%" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" />
					</td>
				</tr>
                <tr height="40px">
				    <td colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
                    <td colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				</tr>
		</table>
        </form>
		</div>
        <br />
      <div style="width:900px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>
</div>
</DIV>
</DIV>
</body>
</html>
