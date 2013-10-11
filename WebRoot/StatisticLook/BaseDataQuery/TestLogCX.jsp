<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>量值溯源管理</title>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
<link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="../../uploadify/swfobject.js"></script>
<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
<script type="text/javascript" src="../../JScript/upload.js"></script>
<script>
	$(function(){
			
			$('#StandardName').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				onSelect:function(record){
					//$('#StandardNameId').val(record.id);
				},
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
					$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=10&QueryName='+newValue);
					}
			});
					
			$('#testlog').datagrid({
				title:'量值溯源导出',
				width:900,
				height:500,
				singleSelect:false, 
				nowrap: false,
				striped: true,
				url:'/jlyw/TestLogServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'StandardApplianceName',title:'标准器具名称',width:100,align:'center',sortable:true},
					{field:'StandardApplianceModel',title:'标准器具型号规格',width:100,align:'center'},
					{field:'StandardApplianceRange',title:'标准器具测量范围',width:100,align:'center'},
					{field:'StandardApplianceUncertain',title:'标准器具不确定度',width:100,align:'center'},
					{field:'TestDate',title:'检定日期',width:120,align:'center'},				
					{field:'ValidDate',title:'有效日期',width:120,align:'center'},
					{field:'Tester',title:'检定单位',width:150,align:'center'},
					{field:'CertificateId',title:'检定证书编号',width:80,align:'center',sortable:true},
					{field:'ConfirmMeasure',title:'溯源结果确认意见及措施',width:200,align:'center'},
		
					{field:'ConfirmerName',title:'溯源结果确认人',width:80,align:'center'},
					{field:'ConfirmDate',title:'溯源结果确认日期',width:120,align:'center'},
					
					{field:'Copy',title:'检定证书扫描件',width:120,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='点击下载该文件' >"+value.filename+"</a>";
						}
					},
					{field:'DurationCheck',title:'期间核查',width:200,align:'center'},
					{field:'Maintenance',title:'维护保养',width:200,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">注销</span>';
							}
						}
					},
					{field:'Remark',title:'备注',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'导出',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				}]
			});	
			$('#queryTestLogStatus').combobox('setValue',"")
		});
	
		function queryTestlog(){
			$('#testlog').datagrid('options').url='/jlyw/TestLogServlet.do?method=2';
			$('#testlog').datagrid('options').queryParams={'queryValidDateFrom':encodeURI($('#queryValidDateFrom').datebox('getValue')),'queryValidDateEnd':encodeURI($('#queryValidDateEnd').datebox('getValue')),'queryTestDateFrom':encodeURI($('#queryTestDateFrom').datebox('getValue')),'queryTestDateEnd':encodeURI($('#queryTestDateEnd').datebox('getValue')),'queryTester':encodeURI($('#queryTester').val()),'queryCertificateId':encodeURI($('#queryCertificateId').val()),'queryStatus':encodeURI($('#queryTestLogStatus').combobox('getValue'))};
			$('#testlog').datagrid('reload');
			$('#queryTestLogStatus').combobox('setValue',"");
		}
		
		function myExport(){
			ShowWaitingDlg("正在导出，请稍后......");
			$('#paramsStr').val(JSON.stringify($('#testlog').datagrid('options').queryParams));
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
<form id="frm_export" method="post" action="/jlyw/TestLogServlet.do?method=5">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="量值溯源管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px;padding-left:30px"  region="center">
		<div>
			<br />
			<br />
			<table  style="width:800px">
            	<tr>
                	<td align="right">标准器具名称：</td>
				    <td width="168" align="left"><select id="StdAppId" name="StdAppId" class="easyui-combobox" valueField="id" textField="name" style="width:152px" required="true" panelHeight="auto"/></td>
                </tr>
				<tr>
					<td align="right">有效期：</td>
					<td width="168" align="left">
						<input name="date1" id="queryValidDateFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
                    <td align="center">～</td>
					<td width="168" align="left">
						<input name="date2" id="queryValidDateEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
				</tr>
                <tr>
                   <td align="right">检定日期：</td>
					<td width="168" align="left">
						<input name="date3" id="queryTestDateFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
                    <td align="center">～</td>
					<td width="168" align="left">
						<input name="date4" id="queryTestDateEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
				</tr>
                <tr>
                    <td align="right">检定单位：</td>
				  	<td align="left"><input id="queryTester" name="queryTester" style="width:152px;" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=12"></input></td>
					<td align="right">证书编号：</td>
				  	<td align="left"><input id="queryCertificateId" name="queryCertificateId"  class="easyui-validatebox" type="text"/></td>
                     <td align="right">状态：</td>
				  	<td align="left"><select id="queryTestLogStatus" name="queryTestLogStatus" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value="0">正常</option>
                                    <option value="1">注销</option>
                                </select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="queryTestlog()">查询</a></td>
				</tr>
			</table>
		</div>
		
		<table id="testlog" style="height:500px; width:900px"></table>
	</div>

</DIV>
</DIV>
</body>
</html>
