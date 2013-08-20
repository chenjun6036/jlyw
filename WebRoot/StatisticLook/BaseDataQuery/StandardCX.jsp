<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>计量标准信息查询</title>
        <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
        <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
        <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
        <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
        <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
        <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
        <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
        <script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
        <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
        <script type="text/javascript" src="../../JScript/upload.js"></script>
        <script type="text/javascript" src="../../JScript/letter.js"></script>
 		<script type="text/javascript" src="../../JScript/json2.js"></script>

		<script>
				
		$(function(){
			$('#table2').datagrid({
				title:'计量标准信息查询',
				width:900,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/StandardServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'SLocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'CertificateCode',title:'计量标准证书号',width:100,align:'center'},
					{field:'Name',title:'计量标准名称',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'英文名称',width:100,align:'center'},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},				
					{field:'StandardCode',title:'计量标准代码',width:80,align:'center'},
					{field:'ProjectCode',title:'项目编号',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value == 0 || value == '0')
						{
							rowData['Status']=0;
						    return "正常";
						}
						else if(value == 1 || value == '1')
						{
							rowData['Status']=1;
							return '<span style="color:red">注销</span>';
						}
					}},
					{field:'CreatedBy',title:'建标单位',width:120,align:'center'},
					{field:'IssuedBy',title:'发证单位',width:120,align:'center'},
					{field:'IssueDate',title:'发证日期',width:100,align:'center'},
					{field:'ValidDate',title:'有效期',width:100,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Uncertain',title:'不确定度误差',width:120,align:'center'},
					{field:'SIssuedBy',title:'社会证发证机关',width:120,align:'center'},
					{field:'SCertificateCode',title:'社会证证书号',width:120,align:'center'},
					{field:'SIssueDate',title:'社会证发证日期',width:120,align:'center'},
					{field:'SValidDate',title:'社会证有效日期',width:120,align:'center'},
					{field:'SRegion',title:'社会证有效区域',width:120,align:'center'},
					{field:'SAuthorizationCode',title:'社会证授权证书号',width:120,align:'center'},
					{field:'WarnSlot',title:'有效期预警天数',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null)
							return "";
						return value+"天";
					}},
					{field:'Handler',title:'项目负责人',width:80,align:'center'},
					{field:'ProjectType',title:'项目类型',width:120,align:'center'},
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
				},'-',{
						text:'已超期标准',
						iconCls:'icon-tip',
						handler:function(){
								$('#table2').datagrid('options').queryParams={'OverValid':encodeURI(1)};
								$('#table2').datagrid('reload');
						}
				},'-',{
						text:'预警标准',
						iconCls:'icon-tip',
						handler:function(){
								$('#table2').datagrid('options').queryParams={'Warn':encodeURI(1)};
								$('#table2').datagrid('reload');
						}
				}],
				onSelect:function(rowIndex,rowData){
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.filesetname};
					$('#uploaded_file_table').datagrid('reload');
				}
			});
			
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
			$('#uploaded_file_table').datagrid('reload');
			
			$('#queryHandler').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].name){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
			
			$('#queryStatus').combobox('setValue',"");
			$('#queryType').combobox('setValue',"");
			
		});
		
		
		function query()
		{
			$('#table2').datagrid('options').url='/jlyw/StandardServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryName':encodeURI($('#queryName').val()),'queryIssuedBy':encodeURI($('#queryIssuedBy').val()),'queryStatus':encodeURI($('#queryStatus').combobox('getValue')),'queryType':encodeURI($('#queryType').combobox('getValue')),'queryStart':encodeURI($('#dateTimeFrom').combobox('getValue')),'queryEnd':encodeURI($('#dateTimeEnd').datebox('getValue')),'queryHandler':encodeURI($('#queryHandler').combobox('getValue')),'queryDept':encodeURI($('#queryDept').val())};
			$('#table2').datagrid('reload');
			
			$('#queryStatus').combobox('setValue',"");
			$('#queryType').combobox('setValue',"");
		}
		
		function myExport(){
			ShowWaitingDlg("正在导出，请稍后......");
			$('#paramsStr').val(JSON.stringify($('#table2').datagrid('options').queryParams));
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
<form id="frm_export" method="post" action="/jlyw/StandardServlet.do?method=6">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="计量标准详细信息查询管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<br />
			<table id="table1" style="width:900px">
				<tr>
				  <td align="right">计量标准名称：</td>
				  <td width="168" align="left"><input id="queryName" name="queryName" type="text"></input></td>
                  <td align="right">发证单位：</td>
				  <td width="168" align="left"><input id="queryIssuedBy" name="queryIssuedBy" type="text"></input></td>
                  <td align="right">状态：</td>
				  <td width="168" align="left">
                  				<select id="queryStatus" name="queryStatus" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value='0' selected>正常</option>
                                    <option value='1'>注销</option>
                                </select>
                   </td>
				</tr>
                <tr>
					<td align="right">计量标准类别：</td>
				  <td width="168" align="left"><input id="queryType" name="queryType" url="/jlyw/ApplianceSpeciesServlet.do?method=2" class="easyui-combobox" valueField="Name" textField="Name" style="width:152px;" editable="false"></input></td>
                  <td align="right">计量标准负责人：</td>
				  <td width="168" align="left"><input id="queryHandler" name="queryHandler" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                  <td align="right">部门：</td>
				  <td width="168" align="left"><input id="queryDept" name="queryDept" type="text"/></td>
                                
				</tr>
                <tr>
					<td align="right">有效期：</td>
					<td width="168" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
                    <td align="center">～</td>
					<td width="168" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
					<td colspan="2" align="right"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:900px"></table>
        
		<div id="p4" class="easyui-panel" style="width:900px;height:250px;"
         title="附件上传" collapsible="false"  closable="false" scroll="no">
            <table width="100%" height="100%" >
               <tr>
                   <td>
                   		<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=106"></table>
                   </td>
               </tr>
           </table>
        </div>
	</div>
</DIV>
</DIV>
</body>
</html>
