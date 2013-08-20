<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>规程信息管理查询</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
		
	<script>	
		$(function(){
			$('#table2').datagrid({
				title:'技术规范信息查询',
				width:900,
				height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'/jlyw/SpecificationServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'SpecificationCode',title:'技术规范编号',width:100,align:'center'},
					{field:'NameCn',title:'技术规范名称(中文)',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'技术规范名称(英文)',width:120,align:'center'},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},
					{field:'Type',title:'类别',width:80,align:'center'},
					{field:'InCharge',title:'是否受控',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['InCharge']=0;
							    return "是";
							}
							else if(value == 1 || value == '1')
							{
								rowData['InCharge']=1;
								return "否";
							}
						}},
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
					{field:'LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'IssueDate',title:'发布日期',width:80,align:'center'},
					{field:'EffectiveDate',title:'实施日期',width:80,ealign:'center'},
					{field:'RepealDate',title:'废止日期',width:80,align:'center'},
					{field:'OldSpecification',title:'替代何技术规范',width:120,align:'center'},
					{field:'WarnSlot',title:'有效期预警天数',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null)
							return "";
						return value+"天";
					}},
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
						text:'预警技术规范',
						iconCls:'icon-tip',
						handler:function(){
							$('#table2').datagrid('options').queryParams={'Warn':encodeURI(1)};
							$('#table2').datagrid('reload');
						}
				}],
				onClickRow:function(rowIndex,rowData){
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.filesetname};
					$('#uploaded_file_table').datagrid('reload');
				}
			});
			
			$('#queryStatus').combobox('setValue',"");
			$('#queryInCharge').combobox('setValue',"");
			$('#queryType').combobox('setValue',"");
		});
		
		
		function query()
		{
			$('#table2').datagrid('options').url='/jlyw/SpecificationServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryName':encodeURI($('#queryname').val()),'queryCode':encodeURI($('#querycode').val()),'queryStatus':encodeURI($('#queryStatus').combobox('getValue')),'queryInCharge':encodeURI($('#queryInCharge').combobox('getValue')),'queryType':encodeURI($('#queryType').combobox('getValue'))};
			$('#table2').datagrid('reload');
			$('#queryStatus').combobox('setValue',"");
			$('#queryInCharge').combobox('setValue',"");
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
<form id="frm_export" method="post" action="/jlyw/SpecificationServlet.do?method=6">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="技术规范信息查询管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:900px">
				<tr>
                <td colspan="2"></td>
					<td width="128" align="right">规程编号：</td>
			      <td width="177" align="left"><input id="querycode" name="querycode" type="text" /></td>
					<td width="128" align="right">规程名称：</td>
				  <td width="168" align="left"><input id="queryname" name="queryname" type="text" /></td>
				</tr>
                <tr>
					<td width="128" align="right">规范类别：</td>
			      <td width="177" align="left">
                  				<input id="queryType" name="queryType" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=13"/>
                    </td>
                    <td width="128" align="right">状态：</td>
			      <td width="177" align="left">
                  				<select id="queryStatus" name="queryStatus" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value='0'>正常</option>
                                    <option value='1'>注销</option>
                                    <option value='2'>暂未实施</option>
                                </select>
                    </td>
					<td width="128" align="right">是否受控：</td>
				  <td width="168" align="left"><select id="queryInCharge" name="queryInCharge" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value='0'>是</option>
                                    <option value='1'>否</option>
                                </select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a></td>
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
