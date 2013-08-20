<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>收发室业务统计</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
            <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		
		$(function(){
			nowDate = new Date();
			$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
			$('#Employee').combobox({
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
			var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:350,
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
				columns:[[
					{field:'EmpName',title:'员工',width:80,align:'center'},
					{field:'CommissionTypeName',title:'委托形式',width:80,align:'center'},
					{field:'ChuDanShu',title:'出单数',width:80,align:'center'},
					{field:'ChuDanTaiJianShu',title:'出单台件数',width:80,align:'center'},
				    {field:'WanGongDanShu',title:'完工单数',width:80,align:'center'},
					{field:'WanGongTaiJianShu',title:'完工台件数',width:80,align:'center'},
					{field:'JieZhangDanShu',title:'结账单数',width:160,align:'center'},
					{field:'JieZhangTaiJianShu',title:'结账台件数',width:80,align:'center'},
					{field:'ZhuXiaoDanShu',title:'注销单数',width:80,align:'center'},
					{field:'ZhuXiaoTaiJianShu',title:'注销台件数',width:80,align:'center'},
					{field:'TuiYangDanShu',title:'退样单数',width:80,align:'center'},
					{field:'TuiYangTaiJianShu',title:'退样台件数',width:80,align:'center'}
                ]],
				toolbar:[{
					text:'导出',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				},'-',{
					text:'打印',
					iconCls:'icon-print',
					handler:function(){
						var emp = $('#Employee').combobox('getValue');
						if(emp == null||emp == ""){
							$.messager.alert('提示','请选择一个员工！','warning');
							return;
						}
						$('#Employee1').val(encodeURI($('#Employee').combobox('getValue')));
						$('#DateFrom1').val(encodeURI($('#DateFrom').datebox('getValue')));
						$('#DateEnd1').val(encodeURI($('#DateEnd').datebox('getValue')));
						
						$('#formLook').submit();
					}
				}],
				pagination:false,
				rownumbers:true
			});
			
		});
		
		function query(){
			var emp = $('#Employee').combobox('getValue');
			if(emp == null||emp == ""){
				$.messager.alert('提示','请选择一个员工！','warning');
				return;
			}
			$('#result').datagrid('options').url='/jlyw/QueryServlet.do?method=1';
			$('#result').datagrid('options').queryParams={'Employee':encodeURI(emp),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue'))};
			$('#result').datagrid('reload');
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
<form id="frm_export" method="post" action="/jlyw/QueryServlet.do?method=4">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="收发室业务统计" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:150px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
            <form id="query" method="post">
			<table width="880px" id="table1">
				<tr height="30px">
					<td align="right">员&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工：</td>
					<td align="left" colspan="5">
						<input id="Employee" class="easyui-combobox" name="Employee" url="" style="width:150px;" valueField="id" textField="name" panelHeight="150px" />
					</td>
				</tr >
                <tr height="30px">
                	<td align="right">起始时间：</td>
				  	<td align="left" colspan="2">
						<input name="DateFrom" id="DateFrom" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="right" colspan="2">结束时间：</td>
					<td align="left">
						<input name="DateEnd" id="DateEnd" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr>     
                <tr height="30px">
                    <td colspan="5"></td>
                    <td align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">统计</a></td>
                </tr>				
				</table>
                </form>
		</div>
        <br />
      <div style="width:900px;height:350px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="350px" ></table>
	  </div>
      <!--<div id="p1" class="easyui-panel" style="width:900px;height:220px;padding:10px;"
				title="统计结果" collapsible="false"  closable="false" align="center">
                <form id="statistic">
                <table width="850px" height="180px">
                	<tr>
                    	<td align="right">出单数：</td>
                        <td align="left"><input id="ChuDanShu" name="ChuDanShu" type="text" readonly="readonly"/></td>
                        <td align="right">出单台件数：</td>
                        <td align="left"><input id="ChuDanTaiJianShu" name="ChuDanTaiJianShu" type="text" readonly="readonly"/></td>
                    </tr>
                    <tr>
                    	<td align="right">完工单数：</td>
                        <td align="left"><input id="WanGongDanShu" name="WanGongDanShu" type="text" readonly="readonly"/></td>
                        <td align="right">完工台件数：</td>
                        <td align="left"><input id="WanGongTaiJianShu" name="WanGongTaiJianShu" type="text" readonly="readonly"/></td>
                    </tr>
                    <tr>
                    	<td align="right">结账单数：</td>
                        <td align="left"><input id="JieZhangDanShu" name="JieZhangDanShu" type="text" readonly="readonly"/></td>
                        <td align="right">结账台件数：</td>
                        <td align="left"><input id="JieZhangTaiJianShu" name="JieZhangTaiJianShu" type="text" readonly="readonly"/></td>
                    </tr>
                    <tr>
                    	<td align="right">注销单数：</td>
                        <td align="left" colspan="3"><input id="ZhuXiaoDanShu" name="ZhuXiaoDanShu" type="text" readonly="readonly"/></td>
                    </tr>
                </table>
                </form>
		</div>-->
		
	<form id="formLook" method="post" action="/jlyw/QueryServlet.do?method=8" target="PrintFrame" accept-charset="utf-8" >
		<input id="Employee1"  name="Employee"  style="width:150px;" type="hidden"/>
			
		<input name="DateFrom" id="DateFrom1" type="hidden" style="width:152px;" />
				
		<input name="DateEnd" id="DateEnd1" type="hidden" style="width:152px;"  />
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	
</div>
</DIV>
</DIV>
</body>
</html>
