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
			$('#CertificateCopy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '支持格式:*', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					'fileExt'   : '*.*;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
			});
			$('#Confirmer').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(record){
					$('#ConfirmerId').val(record.id);
				},
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
					$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=6&QueryName='+newValue);
					}
			});
		 	var lastIndex;
			
			$('#testlog').datagrid({
				title:'量值溯源管理',
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
							if(value==0)
								return "正常";
							else
								return "注销";
						}
					},
					{field:'Remark',title:'备注',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#testlog').datagrid('getSelected');
						if(select){
							$('#edit').window('open');
							$('#frm_edit_testlog').show();
															
							$('#StandardName1').val(select.StandardApplianceName);
							$('#StandardNameId1').val(select.StandardApplianceId);
							$('#Model').val(select.StandardApplianceModel);
							$('#Range').val(select.StandardApplianceRange);
							$('#Uncertain').val(select.StandardApplianceUncertain);
							$('#TestDate').datebox('setValue',select.TestDate);
							$('#ValidDate').datebox('setValue',select.ValidDate);
							$('#Tester').val(select.Tester);
							$('#CertificateId').val(select.CertificateId);
							$('#ConfirmMeasure').val(select.ConfirmMeasure);
							$('#ConfirmerId').val(select.ConfirmerId);
							$('#Confirmer').combobox('setValue',select.ConfirmerName);
							$('#ConfirmDate').datebox('setValue',select.ConfirmDate);
							$('#DurationCheck').val(select.DurationCheck);
							$('#Maintenance').val(select.Maintenance);
							$('#Remark').val(select.Remark);
							$('#Status').val(select.Status);

							
							$('#Id').val(select.Id);
					}else{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'注销',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#testlog').datagrid('getSelected');
							if(select){
								$.messager.confirm('提示','确认注销吗？',function(r){
									if(r){
										$.ajax({
											type:'POST',
											url:'/jlyw/TestLogServlet.do?method=4',
											data:"id="+select.Id,
											dataType:"json",
											success:function(data){
												$('#testlog').datagrid('reload');
												$.messager.alert('提示',data.msg,'warning');
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
				}]
			});	
	});
	
	function save(){
		$('#frm_edit_testlog').form('submit',{
			url:'/jlyw/TestLogServlet.do?method=3',
			onSubmit:function(){return $('#frm_edit_testlog').form('validate');},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK)
				{
					var Copy = result.CertificateCopy_filesetname;
					
					var num = $('#CertificateCopy').uploadifySettings('queueSize');
					
					if (num > 0) { //有选择文件
						doUploadByUploadify(Copy,'CertificateCopy',false);
					}		
					//closed();
				}
				$.messager.alert('提示！',result.msg,'info');
				$('#testlog').datagrid('reload');
				
			}
		});
	}
				
	function closed(){
		$('#CertificateCopy').uploadifyClearQueue();
		$('#edit').dialog('close');
	}
	
	function query()
	{
		$('#testlog').datagrid('options').url='/jlyw/TestLogServlet.do?method=2';
		$('#testlog').datagrid('options').queryParams={'queryname':encodeURI($('#StandardName').combobox('getValue'))};
		$('#testlog').datagrid('reload');
	}
</script>
</head>

<body>
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
			<table id="table1" style="width:900px">
				<tr>
					<td align="right">标准器具名称：</td>
				    <td width="168" align="left"><select id="StandardName" name="StandardName" class="easyui-combobox" valueField="name" textField="name" style="width:152px" required="true" panelHeight="auto"/><input id="StandardNameId" name="StandardNameId" type="hidden" /></td>
					<td width="49" align="right"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">Search</a>
					</td>
				</tr>
			</table>
		</div>
		
		<table id="testlog" style="height:500px; width:900px"></table>

		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 900;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
		<form id="frm_edit_testlog" method="post">
			<input id="Id" name="Id" type="hidden"/>
			<table style="width:700px; padding-top:10px; padding-bottom:15px" class="easyui-panel" title="量值溯源记录信息录入">
				<tr height="50px">
					<td align="right" style="width：20%">标准器具：</td>
					<td align="left"  style="width：30%" >
						<input id="StandardName1" name="StandardName" readonly="readonly" required="true" /><input id="StandardNameId1" name="StandardNameId" type="hidden" />
					</td>
					<td align="right" style="width：20%">型号规格：</td>
					<td align="left"  style="width：30%">
						<input id="Model" name="Model" readonly="readonly"/>
					</td>
					
				</tr>
				<tr height="50px">
					<td align="right" style="width：20%">测量范围：</td>
					<td align="left"  style="width：30%">
						<input id="Range" name="Range" readonly="readonly"/>
					</td>
					<td align="right" style="width：20%">不确定度：</td>
					<td align="left"  style="width：30%" >
						<input id="Uncertain" name="Uncertain" readonly="readonly"/>
					</td>
					
				</tr>
				
				<tr height="50px">
					
					<td align="right">检定日期：</td>
					<td align="left" >
						<input id="TestDate" name="TestDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
					<td align="right">有效日期：</td>
					<td align="left" >
						<input id="ValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
				</tr>
				
				<tr height="50px">
					<td align="right">溯源结果&nbsp;&nbsp;<br />确&nbsp;认&nbsp;人：</td>
					<td align="left"><input id="Confirmer" name="Confirmer" type="text" class="easyui-validatebox" style="width:152px" valueField="name" textField="name" /><input id="ConfirmerId" name="ConfirmerId" type="hidden" /></td>
					<td align="right">溯源结果&nbsp;&nbsp;<br />确认日期：</td>
					<td align="left"><input id="ConfirmDate" name="ConfirmDate" type="text" class="easyui-datebox" style="width:152px" /></td>
				</tr>
				<tr  height="50px">
					<td align="right">鉴定单位：</td>
					<td align="left" >
						<input id="Tester" name="Tester" type="text" class="easyui-validatebox" style="width:250px"  required="true" />
					</td>
					<td align="right">鉴定证书&nbsp;&nbsp;<br />编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
					<td align="left" ><input id="CertificateId" name="CertificateId" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
					<td align="left"  colspan="3">
					<select id="Status" name="Status" >
						<option value="0" selected="selected">正常</option>
						<option value="1">注销</option>
					</select> </td>
				</tr>
				<tr height="30px">
					<td align="right">溯源结果&nbsp;&nbsp;<br />确认意见：</td>
					<td align="left"  colspan="3"><textarea id="ConfirmMeasure" name="ConfirmMeasure" cols="55" rows="2" ></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">期间核查：</td>
					<td align="left"  colspan="3"><textarea id="DurationCheck" name="DurationCheck" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">维护保养：</td>
					<td align="left"  colspan="3"><textarea id="Maintenance" name="Maintenance" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td align="left"  colspan="3"><textarea id="Remark" name="Remark" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">检定证书&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
					<td align="left" colspan="3"><input id="CertificateCopy" name="CertificateCopy" type="file" style="width:350px"/></td>
				</tr>
				
				<tr height="50px">	
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">修改</a></td>
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="closed()">关闭</a></td>
				</tr>
			</table>
			</form>
		
		</div>
	</div>

</DIV>
</DIV>
</body>
</html>
