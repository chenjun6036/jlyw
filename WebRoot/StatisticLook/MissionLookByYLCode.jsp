<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>未使用预留委托单号查询</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../WebPrint/LodopFuncs.js"></script>
		<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
			   <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
		</object>
		<script type="text/javascript" src="../WebPrint/printer.js"></script>
	<script>
		$(function(){
				
			$('#table6').datagrid({
			    width:1000,
				height:500,
				title:'未使用预留委托单号查询',
//				iconCls:'icon-save',
				//singleSelect:true, 
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
					{field:'Pwd',title:'密码',width:100,align:'center'},
					{field:'CommissionDate',title:'预留日期',width:100,align:'center'}
                ]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'打印',
					iconCls:'icon-print',
					handler:function(){
						$("#YLCommissionSheetList").val(JSON.stringify($('#table6').datagrid('getSelections')));
						$('#formLook').submit();
					}
				}]
			});
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=18';
			$('#table6').datagrid('options').queryParams={'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue'))};
			$('#table6').datagrid('reload');
		}
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="未使用预留委托单号查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:150px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
			<table width="950px" id="table1">
                <tr height="30px">
                	<td align="right">起始时间：</td>
				  	<td align="left">
						<input name="DateFrom" id="DateFrom" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="right">结束时间：</td>
					<td align="left">
						<input name="DateEnd" id="DateEnd" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
				<tr height="40px">
                	<td align="right"></td>
				    <td align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				</tr>
				
		</table>
		</div>
        <br />
      <div style="width:1000px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>


<form id="formLook" method="post" action="/jlyw/SFGL/wtd/YLComSheetListPrint.jsp" target="PrintFrame" >			
		
		<input type="hidden" name="YLCommissionSheetList" id="YLCommissionSheetList" />
</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>
