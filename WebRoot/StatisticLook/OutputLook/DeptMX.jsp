<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<% 
		String person = request.getParameter("person");//判断是否只查询个人所在科室产值
		String departmentid = request.getParameter("departmentid");
		String departmentname = request.getParameter("departmentname");
		String dateTimeFrom = request.getParameter("dateTimeFrom");
		String dateTimeEnd = request.getParameter("dateTimeEnd");
		String CommissionType = request.getParameter("CommissionType");
		String headnameid = request.getParameter("HeadNameid");
		String headname = request.getParameter("HeadName");
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查询科室明细</title>
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
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		    $('#departmentid').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(record){
					$('#departmentid1').val(record.Id);
				}
				
			});
			
			if('<%=person%>'=='1'){
				$('#departmentid').combobox('disable');
			}
			
			if('<%=dateTimeFrom%>'!='null'&&'<%=dateTimeEnd%>'!='null'){
				
				$("#dateTimeFrom").datebox('setValue','<%=dateTimeFrom%>');
				$("#dateTimeEnd").datebox('setValue','<%=dateTimeEnd%>');			
				for (var i = 0; i < document.getElementById("CommissionType").options.length; i++) {       
					if (document.getElementById("CommissionType").options[i].value == '<%=CommissionType%>') {       
						document.getElementById("CommissionType").options[i].selected = true;           
						break;       
					}       
				} 
				$('#departmentid').combobox('loadData',[{Id:'<%=departmentid%>',Name:'<%=departmentname%>'}]);
				$('#departmentid').combobox('setValue','<%=departmentid%>');
				if('<%=headnameid%>'!='null'){
				 	$('#HeadName').combobox('loadData',[{id:'<%=headnameid%>',headname:'<%=headname%>'}]);
					$('#HeadName').combobox('setValue','<%=headnameid%>');		
				} 
			 
			}   
			
		});
		
		$(function(){
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:400,
				title:'部门产值明细',
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				showFooter:true,
			  	frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'委托单号',width:100,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:100,align:'center'},
				    {field:'CustomerName',title:'委托单位名称',width:180,align:'center'},
					
					{field:'ApplianceName',title:'器具名称',width:120,editor:'text',align:'center'},
					{field:'Model',title:'型号规格',width:80,editor:'text',align:'center'},
					{field:'Range',title:'测量范围',width:80,editor:'text',align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,editor:'text',align:'center'},
					{field:'Manufacturer',title:'制造厂商',width:80,editor:'text',align:'center'},
					{field:'Quantity',title:'台/件数',width:70,align:'center'},
					//{field:'FinishQuantity',title:'完工器具数量',width:70,align:'center'},
					{field:'WithdrawQuantity',title:'退样数量',width:70,align:'center'},
					{field:'CertificateCode',title:'证书号',width:100,align:'center'},
					{field:'TotalFee',title:'产值',width:60,align:'center'},
					{field:'TestFee',title:'检测费',width:60,align:'center'},
					{field:'RepairFee',title:'修理费',width:60,align:'center'},
					{field:'MaterialFee',title:'材料费',width:60,align:'center'},
					{field:'CarFee',title:'交通费',width:60,align:'center'},
					{field:'DebugFee',title:'调试费',width:60,align:'center'},
					{field:'OtherFee',title:'其他费用',width:60,align:'center'},
					{field:'CheckOutStaff',title:'结账人',width:60,align:'center'},
					{field:'CheckOutTime',title:'结账日期',width:80,align:'center'},
					{field:'DetailListCode',title:'结账清单号',width:80,align:'center'},
								
					{field:'FeeAlloteeName',title:'产值人',width:80,align:'center'}
                ]],
				toolbar:[{
					text:'查看委托单明细',
					iconCls:'icon-search',
					handler:function(){
						var select = $('#result').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('提示','请选择一行数据！','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm1').submit();
					}
				}],
				pagination:true,
				rownumbers:true,
				onLoadSuccess:function(data){
					
					
					$('#JDFee').val(data.JDFee);
					$('#JZFee').val(data.JZFee);
					$('#JYFee').val(data.JYFee);
					$('#JCFee').val(data.JCFee);
					
				}
			});
			
			if('<%=dateTimeFrom%>'!='null'&&'<%=dateTimeEnd%>'!='null'){
				 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=7';
				 $('#result').datagrid('options').queryParams={'DepartmentId':encodeURI('<%=departmentid%>'),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'CommissionType':$('#CommissionType').val(),'HeadName':encodeURI($('#HeadName').combobox('getValue'))};
				 $('#result').datagrid('reload');
				 
				 $('#departmentid1').val(encodeURI('<%=departmentid%>'));

			}
				
		});
		
		function query(){
	   		 if(!$("#searchForm").form('validate'))
				return false ;
			 $('#result').datagrid('loadData', {'total':0, 'rows':[]});
			 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=7';
			 $('#result').datagrid('options').queryParams={'DepartmentId':encodeURI($('#departmentid').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'CommissionType':$('#CommissionType').val(),'HeadName':encodeURI($('#HeadName').combobox('getValue'))};
			 $('#result').datagrid('reload');
		}
		

		
		function reset()
		{
			if('<%=person%>'!='1'){
				$('#departmentid').combobox('setValue',"");
			}
			
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			document.getElementById("CommissionType").value="";
		}
		function myPrint(){
			 if(!$("#searchForm").form('validate'))
				return false ;
			if('<%=dateTimeFrom%>'!='null'&&'<%=dateTimeEnd%>'!='null'){
				
			}
			
			$('#StartTime').val($('#dateTimeFrom').datebox('getValue'));
			$('#EndTime').val($('#dateTimeEnd').datebox('getValue'));
			$('#HeadName1').val($('#HeadName').combobox("getValue"));
			
			$('#CommissionType1').val($('#CommissionType').val());
			
			$('#formLook').submit();
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
<form id="SearchForm1" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
</form>
<form id="frm_export" method="post" action="/jlyw/StatisticServlet.do?method=12">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="查询科室明细" />
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
				    <td width="10%" colspan="2"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				    <td width="21%" colspan="2"  align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				</tr>
				
		</table>
		</form>
		</div>
		<br/>
      <div style="width:900px;height:400px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="400px" ></table>
	  </div>
	 <br/>
	  <div id="p1" class="easyui-panel" style="width:900px;padding:10px;"
				title="统计结果" collapsible="false"  closable="false" align="center">
                <label id="statistics"></label>
            <table width="870px" >
				<tr height="30px" >
					<td  width="24%" align="center">
					检定费：<input id="JDFee" type="text" name="JDFee" value="" style="width:75px;" readonly="readonly" />元
					</td>
					<td  width="24%" align="center">
					校准费：<input id="JZFee" type="text" name="JZFee" value="" style="width:75px;" readonly="readonly"/>元
					</td>
					<td  width="24%" align="center">
					检测费：<input id="JCFee" type="text" name="JCFee" value="" style="width:75px;" readonly="readonly"/>元
					</td>
					<td  width="24%" align="center">
					检验费：<input id="JYFee" type="text" name="JYFee" value="" style="width:75px;" readonly="readonly"/>元
					</td>					
				</tr>
				<tr height="50px">
					<td   align="center" colspan="2">
					<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onClick="myExport()">导出</a>
					</td>	
					<td   align="center" colspan="2">
					<a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onClick="myPrint()">打印</a>
					</td>					
				</tr>
		  </table>
		</div>
		
	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=8" target="PrintFrame" accept-charset="utf-8" >
			
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
