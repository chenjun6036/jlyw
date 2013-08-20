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
						$('#uploaded_file_table').datagrid('reload');
					},
					onAllComplete: function(event,data){
					}
				});
				
				/*$('#SCopy').uploadify({
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
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#edit').window('open');
							$('#form1').show();
															
							$('#Name').val(select.Name);
							$('#NameEn').val(select.NameEn);
							$('#Brief').val(select.Brief);
							$('#CertificateCode').val(select.CertificateCode);
							$('#StandardCode').val(select.StandardCode);
							$('#ProjectCode').val(select.ProjectCode);
							$('#Status').combobox('setValue',select.Status);
							$('#CreatedBy').val(select.CreatedBy);
							$('#IssuedBy').val(select.IssuedBy);
							$('#IssueDate').datebox('setValue',select.IssueDate);
							$('#ValidDate').datebox('setValue',select.ValidDate);
							$('#Range').val(select.Range);
							$('#Uncertain').val(select.Uncertain);
							$('#SIssuedBy').val(select.SIssuedBy);
							$('#SIssueDate').datebox('setValue',select.SIssueDate);
							$('#SValidDate').datebox('setValue',select.SValidDate);
							$('#SCertificateCode').val(select.SCertificateCode);
							$('#SRegion').val(select.SRegion);
							$('#SAuthorizationCode').val(select.SAuthorizationCode);
							$('#SLocaleCode').val(select.SLocaleCode);
							$('#Remark').val(select.Remark);
							$('#WarnSlot').val(select.WarnSlot);
							$('#Handler').combobox('setValue',select.Handler);
							$('#Type').combobox('setValue',select.ProjectType);
							
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
											url:'/jlyw/StandardServlet.do?method=4',
											data:"id="+select.Id,
											dataType:"json",
											success:function(){
												$('#table2').datagrid('reload');
											}
										});
									}
								});	
							}
							else
							{
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
			
			$('#Handler').combobox({
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
		
		function edit(){
			$('#form1').form('submit',{
				url: '/jlyw/StandardServlet.do?method=3',
				onSubmit:function(){ return $('#form1').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
					{
						closed();
					}
					$('#table2').datagrid('reload');
		   		 }
			});
		}
		
		function closed(){
			$('#edit').dialog('close');
		}
		
		function query()
		{
			$('#table2').datagrid('options').url='/jlyw/StandardServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryName':encodeURI($('#queryName').val()),'queryIssuedBy':encodeURI($('#queryIssuedBy').val()),'queryStatus':encodeURI($('#queryStatus').combobox('getValue')),'queryType':encodeURI($('#queryType').combobox('getValue')),'queryStart':encodeURI($('#dateTimeFrom').combobox('getValue')),'queryEnd':encodeURI($('#dateTimeEnd').datebox('getValue')),'queryHandler':encodeURI($('#queryHandler').combobox('getValue')),'queryDept':encodeURI($('#queryDept').val())};
			$('#table2').datagrid('reload');
			
			$('#queryStatus').combobox('setValue',"");
			$('#queryType').combobox('setValue',"");
		}
		
		function getBrief(){
			$('#Brief').val(makePy($('#Name').val()));
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
			$.messager.confirm('提示','确认替换吗？',function(r){
				if(r){
					$('#form1').form('submit',{
						url: '/jlyw/StandardServlet.do?method=8',
						onSubmit:function(){ return $('#form1').form('validate');},
						success:function(data){
							var result = eval("("+data+")");
							$.messager.alert('提示',result.msg,'info');
							if(result.IsOK)
							{
								closed();
							}
							$('#table2').datagrid('reload');
						 }
					});
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
         
        
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 900;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3">
					<input id="Id" name="Id" type="hidden"/>
						<tr height="30px">
                            <td align="right">计量标准名称：</td>
                            <td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                            <td align="right">拼音简码：</td>
                            <td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">英文名称：</td>
                            <td align="left" colspan="3"><input id="NameEn" name="NameEn" type="text" style="width:450px" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">计量标准&nbsp;&nbsp;<br />证&nbsp;书&nbsp;号：</td>
                            <td align="left"><input id="CertificateCode" name="CertificateCode" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">计量标准代码：</td>
                            <td align="left"><input id="StandardCode" name="StandardCode" type="text" class="easyui-validatebox" required="true" /></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">项目编号：</td>
                            <td align="left"><input id="ProjectCode" name="ProjectCode" type="text"/></td>
                            <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
                            <td align="left">
                                <select id="Status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true" editable="false">
                                    <option value='0' selected>正常</option>
                                    <option value='1'>注销</option>
                                </select>
                            </td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">建标单位：</td>
                            <td align="left"><input id="CreatedBy" name="CreatedBy" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">发证单位：</td>
                            <td align="left"><input id="IssuedBy" name="IssuedBy" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=14"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">发证日期：</td>
                            <td align="left"><input id="IssueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
                            <td align="right">有&nbsp;效&nbsp;期：</td>
                            <td align="left"><input id="ValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">测量范围：</td>
                            <td align="left"><input id="Range" name="Range" type="text" class="easyui-validatebox"/></td>
                            <td align="right">不确定度&nbsp;&nbsp;<br />误&nbsp;&nbsp;&nbsp;&nbsp;差：</td>
                            <td align="left"><input id="Uncertain" name="Uncertain" type="text" /></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />发证机关：</td>
                            <td align="left"><input id="SIssuedBy" name="SIssuedBy" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=15"/></td>
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />证&nbsp;书&nbsp;号：</td>
                            <td align="left"><input id="SCertificateCode" name="SCertificateCode" type="text"  class="easyui-validatebox" required="true"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />发证日期：</td>
                            <td align="left"><input id="SIssueDate" name="SIssueDate" type="text" class="easyui-datebox" style="width:152px" required="true" /></td>
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />有效日期：</td>
                            <td align="left"><input id="SValidDate" name="SValidDate" type="text" class="easyui-datebox" style="width:152px" required="true" /></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />有效区域：</td>
                            <td align="left"><input id="SRegion" name="SRegion" type="text" class="easyui-validatebox" required="true" value="常州"/></td>
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />授权证书号：</td>
                            <td align="left"><input id="SAuthorizationCode" name="SAuthorizationCode" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">所内编号：</td>
                            <td align="left"><input id="SLocaleCode" name="SLocaleCode" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">有效期预警天数<br/>(以天为单位)：</td>
							<td align="left"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                         <tr height="30px">
                            <td align="right">标准负责人：</td>
                            <td align="left"><input id="Handler" name="Handler" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" required="true"/></td>
                            <td align="right">标准类别：</td>
                            <td align="left"><input id="Type" name="Type" url="/jlyw/ApplianceSpeciesServlet.do?method=2" style="width:152px;" class="easyui-combobox" valueField="Name" textField="Name" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
                            <td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
                        </tr>
                       <!-- <tr height="30px">
                            <td align="right">计量标准&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
                            <td align="left" colspan="3"><input id="Copy" name="Copy" type="file" style="width:350px"/></td>
                        </tr>
                        
                        <tr height="30px">
                            <td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
                            <td align="left" colspan="3"><input id="SCopy" name="SCopy" type="file" style="width:350px"/></td>
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
