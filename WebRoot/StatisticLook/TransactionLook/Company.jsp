<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查询全所业务量</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
	<script>
		$(function(){
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:350,
				title:'业务量信息',
//				iconCls:'icon-save',
//              pageSize:10,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				columns:[[
				    {field:'Code',title:'委托单号',sortable:true,width:150,align:'center'},
				    {field:'CustomerName',title:'委托单位',width:150,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
					{field:'Quantity',title:'器具数量',width:80,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex)
						{
							return getCommissionSheetStatusInfo(value);
						}
					}
                ]],
				pagination:true,
				rownumbers:true,
				onLoadSuccess:function(data){
					if($('#dateTimeFrom').datebox('getValue')=="")
						return false;
					var label = document.getElementById('statistics');
					var done = data.doneTotal;
					//var datas = $('#result').datagrid('getData').rows;
					label.innerHTML = $('#dateTimeFrom').datebox('getValue') + "至" + $('#dateTimeEnd').datebox('getValue') + "全所共接业务" + data.total + "单。其中，已完工" + done + "单";
				}

			});
		});
		function query(){
			 $('#result').datagrid('loadData', {'total':0, 'rows':[]});
			 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=0';
			 $('#result').datagrid('options').queryParams={'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue'))};
			 $('#result').datagrid('reload');
		}

		function myExport(){
			if($('#result').datagrid('options').queryParams.StartTime==null)
				return;
			
			var title = $('#result').datagrid('options').queryParams.StartTime + "至" + $('#result').datagrid('options').queryParams.EndTime + "全所业务量统计";
			var url = "/jlyw/StatisticServlet.do?method=0";
			var params = 'StartTime='+$('#result').datagrid('options').queryParams.StartTime + "&EndTime=" + $('#result').datagrid('options').queryParams.EndTime;
			var total = $('#result').datagrid('getData').total;
			
			var columns = $('#result').datagrid('options').columns;
			
			ExportToExcel("业务量统计",title,columns[0],url,params,total);
			
		}		
		
		function reset(){
			$('#dataTimeFrom').val("");
			$('#dataTimeEnd').val("");
		}
		
		$(function(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		})
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="查询全所业务量" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:125px;padding:10px;"
				title="统计条件" collapsible="false"  closable="false">
			<table width="850px" id="table1">
				<tr >		
					<td width="10%" align="right">起始时间：</td>
					<td width="22%" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
					<td width="10%" align="right">结束时间：</td>
					<td width="22%" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>	
				</tr >
				<tr height="40px">
				    <td width="10%" colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				    <td width="21%" colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				</tr>
				
		</table>
		</div>
	  <br />	
      <div style="width:900px;height:500px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>
	  <br />
	  <div id="p1" class="easyui-panel" style="width:900px;height:80px;padding:10px;"
				title="统计结果" collapsible="false"  closable="false" align="center">
                <label id="statistics"></label>
           <!-- <table width="100px" align="right">
				<tr >
					<td  width="33%" align="center">
					<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onClick="myExport()">导出</a>
					</td>					
				</tr>
		  </table>-->
		</div>
</div>
</DIV>
</DIV>
</body>
</html>
