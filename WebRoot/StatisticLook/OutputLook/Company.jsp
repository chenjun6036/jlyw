<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查询全所产值</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:500,
				title:'产值信息',
//				iconCls:'icon-save',
//              pageSize:10,
				singleSelect:true, 
				fit: false,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				showFooter:true,
				//idField:'id',
				frozenColumns:[[
					{field:'ck',checkbox:true},
					{field:'DeptCode',title:'部门代码',width:80,align:'center'},
					{field:'DeptName',title:'部门名称',width:80,align:'center'},
					{field:'UserJobNum',title:'员工工号',width:80,align:'center'},
					{field:'UserName',title:'员工姓名',width:80,align:'center'}
				]],
				columns:[[
					{field:'TotalFee',title:'产值',width:80,align:'center'},
					{field:'Quantity',title:'检验台件数',width:80,align:'center'},
					{field:'Withdraw',title:'退样台件数',width:80,align:'center'},
					{field:'Certificate',title:'证书数量',width:80,align:'center'},
				    {field:'TestFee',title:'检定费',width:80,align:'center'},
					{field:'RepairFee',title:'修理费',width:80,align:'center'},
					{field:'MaterialFee',title:'材料费',width:80,align:'center'},
					{field:'CarFee',title:'交通费',width:80,align:'center'},
					{field:'DebugFee',title:'调试费',width:80,align:'center'},
					{field:'OtherFee',title:'其他费',width:80,align:'center'}
                ]],
				toolbar:[{
					text:'打印',
					iconCls:'icon-print',
					handler:function(){
						$('#StartTime').val($('#dateTimeFrom').datebox('getValue'));
						$('#EndTime').val($('#dateTimeEnd').datebox('getValue'));
						$('#HeadName1').val($('#HeadName').combobox("getValue"));
						$('#CommissionType1').val($('#CommissionType').val());
						$('#formLook').submit();
					}
				},'-',{
					text:'导出',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				},'-',{
					text:'查看个人产值明细',
					iconCls:'icon-search',
					handler:function(){
						if(!$("#searchForm").form('validate'))
							return false ;
					    var select = $('#result').datagrid('getSelected');
						if(select==null||select.UserId==null){
							$.messager.alert('提示','请选择一个员工！','warning');
							return;
						}
						$('#UserMX_userid').val(select.UserId);
						$('#UserMX_username').val(select.UserName);
						$('#UserMX_dateTimeFrom').val($('#dateTimeFrom').datebox('getValue'));
						$('#UserMX_dateTimeEnd').val($('#dateTimeEnd').datebox('getValue'));
						$('#UserMX_CommissionType').val($('#CommissionType').val());
						$('#UserMX_HeadNameId').val($('#HeadName').combobox('getValue'));
						$('#UserMX_HeadName').val($('#HeadName').combobox('getText'));
						$('#UserMXForm').submit();
					}
				},'-',{
					text:'查看部门产值明细',
					iconCls:'icon-search',
					handler:function(){
						if(!$("#searchForm").form('validate'))
							return false ;
					    var select = $('#result').datagrid('getSelected');
						if(select==null||select.DeptId==null){
							$.messager.alert('提示','请选择一个部门！','warning');
							return;
						}
						$('#DeptMX_departmentid').val(select.DeptId);
						$('#DeptMX_departmentname').val(select.DeptName);
						$('#DeptMX_dateTimeFrom').val($('#dateTimeFrom').datebox('getValue'));
						$('#DeptMX_dateTimeEnd').val($('#dateTimeEnd').datebox('getValue'));
						$('#DeptMX_CommissionType').val($('#CommissionType').val());
						$('#DeptMX_HeadNameId').val($('#HeadName').combobox('getValue'));
						$('#DeptMX_HeadName').val($('#HeadName').combobox('getText'));
						$('#DeptMXForm').submit();
					}
				},'-',{
					text:'查看全所产值明细',
					iconCls:'icon-search',
					handler:function(){
						if(!$("#searchForm").form('validate'))
							return false ;
					   
						$('#MX_dateTimeFrom').val($('#dateTimeFrom').datebox('getValue'));
						$('#MX_dateTimeEnd').val($('#dateTimeEnd').datebox('getValue'));
						$('#MX_CommissionType').val($('#CommissionType').val());
						$('#MX_HeadNameId').val($('#HeadName').combobox('getValue'));
						$('#MX_HeadName').val($('#HeadName').combobox('getText'));
						$('#MXForm').submit();
					}
				}],
				pagination:false,
				rownumbers:true
			});
			
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
		});
		
		function query(){
			$('#result').datagrid('loadData', {'total':0, 'rows':[]});
			$('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=1';
			$('#result').datagrid('options').queryParams={'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'CommissionType':encodeURI($('#CommissionType').val()),'HeadName':encodeURI($('#HeadName').combobox('getValue'))};
			$('#result').datagrid('reload');
		}
		
		function reset(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			document.getElementById("CommissionType").value="";
		}
		
		function myExport1(){
			if($('#result').datagrid('options').queryParams.StartTime==null)
				return;
			
			var title = $('#result').datagrid('options').queryParams.StartTime + "至" + $('#result').datagrid('options').queryParams.EndTime + "全所产值统计";
			var url = "/jlyw/StatisticServlet.do?method=1";
			var params = 'StartTime='+$('#result').datagrid('options').queryParams.StartTime + "&EndTime=" + $('#result').datagrid('options').queryParams.EndTime;
			var total = $('#result').datagrid('getData').total;
			
			var columns = $('#result').datagrid('options').columns;
			
			ExportToExcel("产值统计",title,columns[0],url,params,total);
			
		}
		
		function myExport(){
			ShowWaitingDlg("正在导出，请稍后......");
			$('#paramsStr').val(JSON.stringify($('#result').datagrid('options').queryParams));
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
		
	</script>
</head>
<form id="DeptMXForm" method="post" action="/jlyw/StatisticLook/OutputLook/DeptMX.jsp" target="_blank">
	 <input id="DeptMX_departmentid" type="hidden" name="departmentid"/>
     <input id="DeptMX_departmentname" type="hidden" name="departmentname"/>
     <input id="DeptMX_dateTimeFrom" type="hidden" name="dateTimeFrom"/>
	 <input id="DeptMX_dateTimeEnd" type="hidden" name="dateTimeEnd"/>
	 <input id="DeptMX_CommissionType" type="hidden" name="CommissionType"/>
	 <input id="DeptMX_HeadNameId" type="hidden" name="HeadNameId"/>
	 <input id="DeptMX_HeadName" type="hidden" name="HeadName"/>
</form>
<form id="UserMXForm" method="post" action="/jlyw/StatisticLook/OutputLook/Person.jsp" target="_blank">
	 <input id="UserMX_userid" type="hidden" name="employeeid"/>
     <input id="UserMX_username" type="hidden" name="employeename"/>
     <input id="UserMX_dateTimeFrom" type="hidden" name="dateTimeFrom"/>
	 <input id="UserMX_dateTimeEnd" type="hidden" name="dateTimeEnd"/>
	 <input id="UserMX_CommissionType" type="hidden" name="CommissionType"/>
	 <input id="UserMX_HeadNameId" type="hidden" name="HeadNameId"/>
	 <input id="UserMX_HeadName" type="hidden" name="HeadName"/>
</form>
<form id="MXForm" method="post" action="/jlyw/StatisticLook/OutputLook/CompanyMX.jsp" target="_blank">
	 <input id="MX_dateTimeFrom" type="hidden" name="dateTimeFrom"/>
	 <input id="MX_dateTimeEnd" type="hidden" name="dateTimeEnd"/>
	 <input id="MX_CommissionType" type="hidden" name="CommissionType"/>
	 <input id="MX_HeadNameId" type="hidden" name="HeadNameId"/>
	 <input id="MX_HeadName" type="hidden" name="HeadName"/>
</form>
<form id="frm_export" method="post" action="/jlyw/StatisticServlet.do?method=11">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="查询全所产值" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:135px;padding:10px;"
				title="统计条件" collapsible="false"  closable="false">
			<table width="850px" id="table1">
            <form id="searchForm">
				<tr >		
					<td width="10%" align="right">起始时间：</td>
					<td width="22%" align="left">
						<input name="dateTimeFrom"id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" required="true">
					</td>
					<td width="10%" align="right">结束时间：</td>
					<td width="22%" align="left">
						<input name="dateTimeEnd" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox"  required="true">
					</td>
				</tr >
				<tr height="30px">
					<td width="10%" align="right" >委托形式：</td>
					<td width="22%" align="left" >
						<select name="CommissionType" id="CommissionType" style="width:152px"><option value="" selected="selected">全部</option><option value="1">送样检测</option><option value="2" >现场检测</option><option value="3" >公正计量</option><option value="4" >形式评价</option><option value="5" >其它业务</option><option value="6" >自检业务</option><option value="7" >现场带回</option></select>
					</td>
					<td width="10%" align="right">台头单位：</td>
					<td width="22%" align="left" colspan="3">
						<input name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" url="/jlyw/AddressServlet.do?method=1"/>
					</td>
				</tr>
				<tr height="40px">
				    <td width="10%" colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				    <td width="21%" colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				</tr>
				</form>
		</table>
		</div>
	  <br />
      <div style="width:900px;height:500px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>
	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=9" target="PrintFrame" accept-charset="utf-8" >
			
		
		<input name="StartTime" id="StartTime" type="hidden"/>
		<input name="EndTime" id="EndTime" type="hidden" />
		<input name="CommissionType" id="CommissionType1" type="hidden" />
		<input name="HeadName" id="HeadName1" type="hidden" />
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>


		
</div>
</DIV>
</DIV>
</body>
</html>
