<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>业务查询</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
        <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
			nowDate = new Date();
			$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
				
			$('#table6').datagrid({
			    width:1000,
				height:500,
				title:'业务查询结果',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/CrmServlet.do?method=30',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Id',title:'ID',width:50,align:'center',sortable:true,hidden:true},
					{field:'CustomerName',title:'委托单位',width:220,align:'center',sortable:true},
					{field:'CommissionDateStart',title:'委托单日期(起)',width:100,align:'center'},
					{field:'CommissionDateEnd',title:'委托单日期(止)',width:100,align:'center'},
					//{field:'Quantity',title:'台/件数',width:70,align:'center'},
					//{field:'WithdrawQuantity',title:'退样台件数',width:70,align:'center'},
					{field:'TotalFee',title:'总费用',width:70,align:'center'}/* ,
					{field:'TestFee',title:'检测费',width:70,align:'center'},
					{field:'RepairFee',title:'修理费',width:70,align:'center'},
					{field:'MaterialFee',title:'材料费',width:70,align:'center'},
					{field:'DebugFee',title:'调试费',width:70,align:'center'},
					{field:'CarFee',title:'交通费',width:70,align:'center'},
					{field:'OtherFee',title:'其他费用',width:70,align:'center'} */
                ]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				toolbar:['-',{
						text:'导出',
						iconCls:'icon-save',
						handler:function(){
							ShowWaitingDlg("正在导出，请稍后......");
							$('#par').val(JSON.stringify($('#table6').datagrid('options').queryParams));
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
				},'-',{
					text:'打印',
					iconCls:'icon-print',
					handler:function(){
						$('#PrintStr').val(JSON.stringify($('#table6').datagrid('options').queryParams));
						$('#formLook').submit();
					}
				}],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10||rowData.Status == "10"){	//已注销
						return 'color:#FF0000';
					}else if(rowData.Status == 0||rowData.Status == "0"){	//已收件
						return 'color:#0000FF';	
					}else if(rowData.Status == 1||rowData.Status == "1"){	//已分配
						return 'color:#0000FF';	
					}else if(rowData.Status == 2||rowData.Status == "2"){	//转包中
						return 'color:#CCFF00';	
					}else if(rowData.Status == 3||rowData.Status == "3"){	//已完工
						return 'color:#000000';	
					}else if(rowData.Status == 4||rowData.Status == "4"){  //已结账
						return 'color:#008000';
					}else{
						return 'color:#000000';
					}
				}
			});
			});
			
		
		function query(){
			$('#table6').datagrid('options').url='/jlyw/CrmServlet.do?method=30';
			$('#table6').datagrid('options').queryParams={'LeastOutput':encodeURI($('#leastOutput').val())}//,/* 'SpeciesType':encodeURI($('#SpeciesType').val()), *//* 'ApplianceSpeciesId':encodeURI($('#ApplianceSpeciesId').val()), */'CheckOutDateFrom':encodeURI($('#CheckOutDateFrom').datebox('getValue')),'CheckOutDateEnd':encodeURI($('#CheckOutDateEnd').datebox('getValue'))};
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
			<jsp:param name="TitleName" value="按产值下限查询" />
		</jsp:include>
	</DIV>
<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=7">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
        <form id="SearchForm2" method="post" action="/jlyw/SFGL/wtd/XGCommissionSheet.jsp" target="_self">
        <input id="SearchForm_Code2" type="hidden" name="Code"/>
        <input id="SearchForm_Pwd2" type="hidden" name="Pwd"/>
        </form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:700px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="680px" id="table1">
			

				<tr>
				<td align="right">产值下限：</td>
				<td><input id="leastOutput" min="0" class="easyui-numberbox"/></td>
				</tr>
               <!--  <tr height="30px">
                	<td align="right">结账时间：</td>
				  	<td align="left">
						<input name="CheckOutDateFrom" id="CheckOutDateFrom" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="CheckOutDateEnd" id="CheckOutDateEnd" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr>  -->
                <tr height="40px">
				    <td colspan="1" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
                    <td colspan="1" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()" >重置</a></td>
				</tr>
				
		</table>
        </form>
		</div>
        <br />
      <div style="width:700px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="700px" height="500px" ></table>
	  </div>

	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=14" target="PrintFrame" accept-charset="utf-8" >
    
    	<input id="PrintStr"  name="PrintStr" style="width:150px;" type="hidden"/>
				
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>
