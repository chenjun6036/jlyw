<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*,com.jlyw.manager.*" %>
<% 
		int proId = (int)(session.getAttribute("LOGIN_USER")==null?0:((SysUser)session.getAttribute("LOGIN_USER")).getProjectTeamId());
		Department dept = (new ProjectTeamManager()).findById(proId).getDepartment();
		int deptId = dept.getId();
		String deptName = dept.getName();
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查询科室产值</title>
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
			
			var row = {'Id':<%=deptId%>,'Name':'<%=deptName%>'};
			$('#departmentid').combobox('loadData',[row]);
			$('#departmentid').combobox('select',<%=deptId%>);
						
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:500,
				title:'产值信息',
//				iconCls:'icon-save',
//                pageSize:10,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'id',
				showFooter:true,
				frozenColumns:[[
					{field:'ck',checkbox:true},
					{field:'UserJobNum',title:'员工工号',width:80,align:'center'}
					
				]],
				columns:[[
					{field:'UserName',title:'员工姓名',width:80,align:'center'},
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
						$('#departmentid1').val($('#departmentid').datebox('getValue'));
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
					text:'查看所选用户的详细产值信息',
					iconCls:'icon-search',
					handler:function(){
						 if(!$("#searchForm").form('validate'))
							return false ;
					    var select = $('#result').datagrid('getSelected')
						if(select){
							$('#Submit_employeeid').val(select.UserId);
							$('#Submit_employeename').val(select.UserName);
							$('#Submit_dateTimeFrom').val($('#dateTimeFrom').datebox('getValue'));
							$('#Submit_dateTimeEnd').val($('#dateTimeEnd').datebox('getValue'));
							$('#Submit_CommissionType').val($('#CommissionType').val());
							$('#Submit_HeadNameId').val($('#HeadName').combobox('getValue'));
							$('#Submit_HeadName').val($('#HeadName').combobox('getText'));
							$('#SubmitForm').submit();
						}
					}
				},'-',{
					text:'查看部门产值明细',
					iconCls:'icon-search',
					handler:function(){
						if(!$("#searchForm").form('validate'))
							return false ;
					   	
						$('#MX_departmentid').val($('#departmentid').combobox('getValue'));
						$('#MX_departmentname').val($('#departmentid').combobox('getText'));
						$('#MX_dateTimeFrom').val($('#dateTimeFrom').datebox('getValue'));
						$('#MX_dateTimeEnd').val($('#dateTimeEnd').datebox('getValue'));
						$('#MX_CommissionType').val($('#CommissionType').val());
						$('#MX_HeadNameId').val($('#HeadName').combobox('getValue'));
						$('#MX_HeadName').val($('#HeadName').combobox('getText'));
						$('#MXForm').submit();
					}
				}],
				pagination:false,
				rownumbers:true,
				onDblClickRow:function(rowIndex, rowData){
					
				},
				onLoadSuccess:function(data){
					
				}
			});
			
			$("#departmentid").combobox('disable');
		});
		function query(){
			 if(!$("#searchForm").form('validate'))
				return false ;
			$('#result').datagrid('loadData', {'total':0, 'rows':[]});
			$('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=3';
			$('#result').datagrid('options').queryParams={'DepartmentId':encodeURI($('#departmentid').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'CommissionType':$('#CommissionType').val(),'HeadName':encodeURI($('#HeadName').combobox('getValue'))};
			$('#result').datagrid('reload');
		}
		
		function reset(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			document.getElementById("CommissionType").value="";
		}
		
		$(function(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		})
		
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

<body>
<form id="SubmitForm" method="post" action="/jlyw/StatisticLook/OutputLook/Person.jsp" target="_blank">
     <input id="Submit_employeeid" type="hidden" name="employeeid"/>
	 <input id="Submit_employeename" type="hidden" name="employeename"/>
	 <input id="Submit_dateTimeFrom" type="hidden" name="dateTimeFrom"/>
	 <input id="Submit_dateTimeEnd" type="hidden" name="dateTimeEnd"/>
	 <input id="Submit_CommissionType" type="hidden" name="CommissionType"/>
	 <input id="Submit_HeadNameId" type="hidden" name="HeadNameId"/>
	 <input id="Submit_HeadName" type="hidden" name="HeadName"/>
</form>
<form id="MXForm" method="post" action="/jlyw/StatisticLook/OutputLook/DeptMX.jsp" target="_blank">
	 <input id="person" type="hidden" name="person" value="1"/>
     <input id="MX_departmentid" type="hidden" name="departmentid"/>
	 <input id="MX_departmentname" type="hidden" name="departmentname"/>
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
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="查询科室产值" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:135px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
			<form id="searchForm">
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >科&nbsp;&nbsp;室：</td>
					<td width="22%" align="left" >
						<input id="departmentid" class="easyui-combobox" name="department" url="/jlyw/DepartmentServlet.do?method=6" style="width:150px;" valueField="Id" textField="Name" panelHeight="auto"  required="true"/>
					</td>
					<td width="10%" align="right">起始时间：</td>
					<td width="22%" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" required="true"/>
					</td>
					<td width="10%" align="right">结束时间：</td>
					<td width="22%" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox"  required="true"/>
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
				    <td colspan="3"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				    <td colspan="3"  align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				</tr>
				
		</table>
		 </form>
		</div>
		<br/>
      <div style="width:900px;height:500px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>
	  <br/>
	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=13" target="PrintFrame" accept-charset="utf-8" >
		<input id="departmentid1" name="DepartmentId"  type="hidden"/>	
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
