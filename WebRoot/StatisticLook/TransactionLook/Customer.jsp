<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>按委托单位查询业务量</title>
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
		$('#customerid').combobox({
		//	url:'/jlyw/CustomerServlet.do?method=6',
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
				$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}
		});
		});
	
		$(function(){
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:500,
				title:'业务量信息',
//				iconCls:'icon-save',
//                pageSize:10,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/StatisticServlet.do?method=0',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'code',
				columns:[[
					{field:'Code',title:'委托单号',sortable:true,width:150,align:'center'},
				    {field:'CustomerName',title:'委托单位名称',width:150,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
					{field:'Quantity',title:'器具数量',width:80,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex)
					{
						return getCommissionSheetStatusInfo(value);
					}}
                ]],
				pagination:true,
				rownumbers:true,
				onLoadSuccess:function(data){
					if($('#customerid').combobox('getValue')=="")
						return;
					var label = document.getElementById('statistics');
					var customer;
					var customers = $('#customerid').combobox('getData');
					for(var i = 0; i < customers.length; i++)
					{
						if(customers[i].id == $('#customerid').combobox('getValue'))
							customer = customers[i].name;
					}
					label.innerHTML = $('#dateTimeFrom').datebox('getValue') + "至" + $('#dateTimeEnd').datebox('getValue') + "全所共承接" + customer +"业务" + data.total + "单。其中，已完工" + data.doneTotal + "单";
				}
				
			});
		});
		
		function query(){
			 $('#result').datagrid('loadData', {'total':0, 'rows':[]});
			 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=0';
			 $('#result').datagrid('options').queryParams={'CustomerId':encodeURI($('#customerid').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue'))};
			 $('#result').datagrid('reload');
		}
		
		function reset(){
			$('#customerid').combobox('setValue',"");
			$('#dataTimeFrom').val("");
			$('#dataTimeEnd').val("");
		}

		function myExport(){
			//获取委托单位信息
    		var CustomerId = $('#customerid').combobox('getValue');
    		var customer;
			var customers = $('#customerid').combobox('getData');
			for(var i = 0; i < customers.length; i++)
			{
				if(customers[i].id == CustomerId)
					customer = customers[i].name;
			}
			
			if($('#result').datagrid('options').queryParams.StartTime==null||CustomerId=="")
				return;
				
			var title = $('#dateTimeFrom').datebox('getValue') + "至" + $('#dateTimeEnd').datebox('getValue') + customer + "业务量统计";
			var url = "/jlyw/StatisticServlet.do?method=0";
			var params = "StartTime="+$('#result').datagrid('options').queryParams.StartTime + "&EndTime=" + $('#result').datagrid('options').queryParams.EndTime + "&CustomerId=" + CustomerId;
			var total = $('#result').datagrid('getData').total;
			
			var columns = $('#result').datagrid('options').columns;
			
			ExportToExcel("业务量统计",title,columns[0],url,params,total);
			
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
			<jsp:param name="TitleName" value="按委托单位查询业务量" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:125px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
			<table width="850px" id="table1">
				<tr >
					<td width="14%" align="right" >委托单位：</td>
					<td width="22%" align="left" >
						<input id="customerid" class="easyui-combobox" name="customer" url="" style="width:150px;" valueField="id" textField="name" panelHeight="auto" />
					</td>
					<td width="10%" align="right">起始时间：</td>
					<td width="22%" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" />
					</td>
					<td width="10%" align="right">结束时间：</td>
					<td width="22%" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" />
					</td>
					
				</tr >
				<tr height="40px">
				    <td width="10%" colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				    <td width="21%" colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
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
