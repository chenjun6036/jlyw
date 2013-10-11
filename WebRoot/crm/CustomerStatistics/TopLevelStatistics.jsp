<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>器具大类统计</title>
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
			
			$('#Customer').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
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
					$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
			
			$('#topSpecies').combobox({
			onSelect:function(rec){
			$('#topSpecies').val(rec.Id);
			}
			});

				
			$('#table6').datagrid({
			    width:750,
				height:300,
				title:'业务查询结果',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/CrmServlet.do?method=33',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'ID',width:100,align:'center',sortable:true},
			
					{field:'Id',title:'器具分类编号',width:80,align:'center'},
					{field:'Name',title:'器具分类名',width:80,align:'center',sortable:true},
					{field:'TotalFee',title:'总费用',width:70,align:'center',sortable:true},
					{field:'TestFee',title:'检测费',width:70,align:'center'},
					{field:'RepairFee',title:'修理费',width:70,align:'center'},
					{field:'MaterialFee',title:'材料费',width:70,align:'center'},
					{field:'DebugFee',title:'调试费',width:70,align:'center'},
					{field:'CarFee',title:'交通费',width:70,align:'center'},
					{field:'OtherFee',title:'其他费用',width:70,align:'center'}
                ]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				toolbar:[{
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
				},'-'/* ,{
					text:'打印',
					iconCls:'icon-print',
					handler:function(){
						$('#PrintStr').val(JSON.stringify($('#table6').datagrid('options').queryParams));
						$('#formLook').submit();
					}
				} */]
			});
			});
			
		
		function query(){
			$('#table6').datagrid('options').url='/jlyw/CrmServlet.do?method=33';
			$('#table6').datagrid('options').queryParams={/* 'Id':$('#topSpecies').combobox('getValue') */};//{'CustomerId':encodeURI($('#Customer').combobox('getValue')),'LeastOutput':encodeURI($('#leastOutput').val()),'Classfication':encodeURI($('#classification').combobox('getValue')),'Status':encodeURI($('#Status').val()),/* 'SpeciesType':encodeURI($('#SpeciesType').val()), *//* 'ApplianceSpeciesId':encodeURI($('#ApplianceSpeciesId').val()), */'CheckOutDateFrom':encodeURI($('#CheckOutDateFrom').datebox('getValue')),'CheckOutDateEnd':encodeURI($('#CheckOutDateEnd').datebox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
			document.getElementById("Status").value="";
		}
		
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="器具大类统计" />
		</jsp:include>
	</DIV>
<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=10">
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
     <!-- <div id="p" class="easyui-panel" style="width:750px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="700px" id="table1">
				<tr >
					<td width="20%" align="right">委托单位：</td>
					<td width="22%" align="left">
						<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;"/>
					</td>
					<td align="right">器具大类：</td>
					<td>
					<select class="easyui-combobox" id="topSpecies" name="TopSpecies" required="true" mode="remote" valueField="Id" textField="Name" url="/jlyw/CrmServlet.do?method=32">
					</select>
					
					</td>
				</tr >
                <tr height="30px">
                	
					<td width="20%" align="right">企业分类：</td>
					<td width="22%" align="left">
						<input id="classification" class="easyui-combobox" name="Classification" style="width:150px;"panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
					</td>
									<td align="right">地区：</td>
				<td><select id="regionId" name="RegionId" class="easyui-combobox" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2"></select></td>
					
                   
				</tr>

                <tr height="30px">
                	<td align="right">结账时间：</td>
				  	<td align="left">
						<input name="CheckOutDateFrom" id="CheckOutDateFrom" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="CheckOutDateEnd" id="CheckOutDateEnd" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
                    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()" >重置</a></td>
				</tr>
				
		</table>
        </form>
		</div> -->
        <br />
      <div style="width:750px;height:400px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
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
