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
				$('#WarnSlot').numberbox();
				
		   		$('#file_upload').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					//'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					//'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					//'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});
				
				/*$('#methodConfirm').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});*/
			});
	
		$(function(){
		    var lastIndex;
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
							else if(value == 2 || value == '2')
							{
								rowData['Status']=2;
								return "尚未实施";
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
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#form1').show();
						
						$('#specode').val(select.SpecificationCode);
						$('#nameCn').val(select.NameCn);
						$('#nameEn').val(select.NameEn);
						$('#brief').val(select.Brief);
						$('#type').combobox('setValue',select.Type);
						$('#incharge').combobox('setValue',select.InCharge);
						$('#status').combobox('setValue',select.Status);
						$('#localecode').val(select.LocaleCode);
						$('#issueDate').datebox('setValue',select.IssueDate);
						$('#effectiveDate').datebox('setValue',select.EffectiveDate);
						$('#repealDate').datebox('setValue',select.RepealDate);
						$('#oldSpecification').val(select.OldSpecification);
						$('#WarnSlot').val(select.WarnSlot);
						$('#remark').val(select.Remark);
						
						$('#Id').val(select.Id);
						$('#form1').form('validate');
					}else{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'注销',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){
								$.messager.confirm('提示','确认注销吗？',function(r){
									if(r){
										$.ajax({
											type:'POST',
											url:'/jlyw/SpecificationServlet.do?method=4',
											data:'id='+select.Id,
											dataType:"json",
											success:function(data){
												$('#table2').datagrid('reload');
											}
										});
									}
								});	
							}
							else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
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
		
		function closed(){
			$('#edit').dialog('close');
		}
		
		function query()
		{
			$('#table2').datagrid('unselectAll');
			$('#table2').datagrid('options').url='/jlyw/SpecificationServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryName':encodeURI($('#queryname').val()),'queryCode':encodeURI($('#querycode').val()),'queryStatus':encodeURI($('#queryStatus').combobox('getValue')),'queryInCharge':encodeURI($('#queryInCharge').combobox('getValue')),'queryType':encodeURI($('#queryType').combobox('getValue'))};
			$('#table2').datagrid('reload');
			$('#queryStatus').combobox('setValue',"");
			$('#queryInCharge').combobox('setValue',"");
			$('#queryType').combobox('setValue',"");
		}
		
		function edit()
		{
			$('#form1').form('submit',{
				url:'/jlyw/SpecificationServlet.do?method=3',
				onSubmit:function(){return $('#form1').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
		   			if(result.IsOK)
					{
		   				closed();
					}
					$.messager.alert('提示',result.msg,'info');
					$('#table2').datagrid('reload');
				}
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#nameCn').val()));
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
		
		function replace(){
			$.messager.confirm('提示','确定替换吗？',function(r){
				if(r){
					$('#form1').form('submit',{
						url:'/jlyw/SpecificationServlet.do?method=8',
						onSubmit:function(){return $('#form1').form('validate');},
						success:function(data){
							var result = eval("("+data+")");
							if(result.IsOK)
							{
								closed();
							}
							$.messager.alert('提示',result.msg,'info');
							$('#table2').datagrid('reload');
						}
					});
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
                   <td width="57%" rowspan="2">
                   		<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=106"></table>
                   </td>
                   <td width="43%" height="175" valign="top" align="left" style="overflow:hidden">
                        <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
                   </td>
               </tr>
               <tr>
                   <td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByDefault()">上传文件</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">取消上传</a> </td>
               </tr>
           </table>
        </div>
        
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 500;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1"  method="post">
				<div>
					<table id="table3" style="padding: 10px;width: 500;height: 500;">
					<input id="Id" name="Id" type="hidden"/>
						<tr height="30px">
                            <td align="right">编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
                            <td align="left"><input id="specode" name="SpecificationCode" type="text"  class="easyui-validatebox" required="true"/></td>
                            <td align="right">中文名称：</td>
                            <td align="left"><input id="nameCn" name="NameCn" type="text"  class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">拼音简码：</td>
                            <td align="left"><input id="brief" name="Brief" type="text"  class="easyui-validatebox" required="true"/></td>
                            <td align="right">英文名称：</td>
                            <td align="left"><input id="nameEn" name="NameEn" type="text"/></td>
                        </tr>
                        <tr height="30px">	
                            <td align="right">规程类别：</td>
                            <td align="left" >
                                <input id="type" name="Type" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=13"/>
                            </td>
                            <td align="right">是否受控：</td>
                            <td align="left" ><select id="incharge" name="InCharge" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
                                    <option value='0'>是</option>
                                    <option value='1'>否</option>
                                </select>
                            </td>
                        </tr>
                        <tr  height="30px">
                            <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
                            <td align="left">
                                <select id="status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
                                    <option value='0'>正常</option>
                                    <option value='1'>注销</option>
                                </select>
                            </td>
                            <td align="right">所内编号：</td>
                            <td align="left" ><input id="localecode" name="LocaleCode" type="text" /></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">发布日期：</td>
                            <td align="left"><input id="issueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
                            <td align="right">实施日期：</td>
                            <td align="left"><input id="effectiveDate" name="EffectiveDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">废止日期：</td>
                            <td align="left"><input id="repealDate" name="RepealDate" type="text" class="easyui-datebox" style="width:152px"/></td>
                            <td align="right">替代何规程：</td>
                            <td align="left" ><input id="oldSpecification" name="OldSpecification" type="text"/></td>
                        </tr>
                        <tr>
                            <td align="right">有&nbsp;效&nbsp;期&nbsp;&nbsp;<br />预警天数：</td>
                            <td align="left" colspan="3"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
                            <td align="left" colspan="3"><textarea id="remark" name="Remark" cols="56" rows="4"></textarea></td>
                        </tr>
                        <!--<tr height="30px">
                            <td align="right">技术规范&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
                            <td align="left" colspan="3"><input id="copy" name="Copy" type="file" style="width:400px"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">方法确认表<br />扫&nbsp;描&nbsp;件：</td>
                            <td align="left" colspan="3"><input id="methodConfirm" name="MethodConfirm" type="file" style="width:400px"/></td>
                        </tr>-->
                        <tr height="50px">	
                            <td align="right"><a class="easyui-linkbutton" icon="icon-ok" name="Edit" href="javascript:void(0)" onclick="edit()">修改</a></td>
                            <td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-edit" name="replace" href="javascript:void(0)" onclick="replace()">替换</a></td>
                            <td align="left"><a class="easyui-linkbutton" icon="icon-cancel" name="Cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
                        </tr>
					</table>
				</div>
			</form>
		</div>
	</div>
    </DIV>
    </DIV>
</body>
</html>
