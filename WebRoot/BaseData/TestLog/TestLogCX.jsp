<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ֵ��Դ����</title>
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
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '֧�ָ�ʽ:*', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					'fileExt'   : '*.*;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
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
				title:'��ֵ��Դ����',
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
					{field:'StandardApplianceName',title:'��׼��������',width:100,align:'center',sortable:true},
					{field:'StandardApplianceModel',title:'��׼�����ͺŹ��',width:100,align:'center'},
					{field:'StandardApplianceRange',title:'��׼���߲�����Χ',width:100,align:'center'},
					{field:'StandardApplianceUncertain',title:'��׼���߲�ȷ����',width:100,align:'center'},
					{field:'TestDate',title:'�춨����',width:120,align:'center'},				
					{field:'ValidDate',title:'��Ч����',width:120,align:'center'},
					{field:'Tester',title:'�춨��λ',width:150,align:'center'},
					{field:'CertificateId',title:'�춨֤����',width:80,align:'center',sortable:true},
					{field:'ConfirmMeasure',title:'��Դ���ȷ���������ʩ',width:200,align:'center'},
		
					{field:'ConfirmerName',title:'��Դ���ȷ����',width:80,align:'center'},
					{field:'ConfirmDate',title:'��Դ���ȷ������',width:120,align:'center'},
					
					{field:'Copy',title:'�춨֤��ɨ���',width:120,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
						}
					},
					{field:'DurationCheck',title:'�ڼ�˲�',width:200,align:'center'},
					{field:'Maintenance',title:'ά������',width:200,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value==0)
								return "����";
							else
								return "ע��";
						}
					},
					{field:'Remark',title:'��ע',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'�޸�',
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
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#testlog').datagrid('getSelected');
							if(select){
								$.messager.confirm('��ʾ','ȷ��ע����',function(r){
									if(r){
										$.ajax({
											type:'POST',
											url:'/jlyw/TestLogServlet.do?method=4',
											data:"id="+select.Id,
											dataType:"json",
											success:function(data){
												$('#testlog').datagrid('reload');
												$.messager.alert('��ʾ',data.msg,'warning');
											}
										});
									}
								});	
							}
							else
							{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
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
					
					if (num > 0) { //��ѡ���ļ�
						doUploadByUploadify(Copy,'CertificateCopy',false);
					}		
					//closed();
				}
				$.messager.alert('��ʾ��',result.msg,'info');
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
			<jsp:param name="TitleName" value="��ֵ��Դ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px;padding-left:30px"  region="center">
		<div>
			<br />
			<br />
			<table id="table1" style="width:900px">
				<tr>
					<td align="right">��׼�������ƣ�</td>
				    <td width="168" align="left"><select id="StandardName" name="StandardName" class="easyui-combobox" valueField="name" textField="name" style="width:152px" required="true" panelHeight="auto"/><input id="StandardNameId" name="StandardNameId" type="hidden" /></td>
					<td width="49" align="right"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">Search</a>
					</td>
				</tr>
			</table>
		</div>
		
		<table id="testlog" style="height:500px; width:900px"></table>

		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 900;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
		<form id="frm_edit_testlog" method="post">
			<input id="Id" name="Id" type="hidden"/>
			<table style="width:700px; padding-top:10px; padding-bottom:15px" class="easyui-panel" title="��ֵ��Դ��¼��Ϣ¼��">
				<tr height="50px">
					<td align="right" style="width��20%">��׼���ߣ�</td>
					<td align="left"  style="width��30%" >
						<input id="StandardName1" name="StandardName" readonly="readonly" required="true" /><input id="StandardNameId1" name="StandardNameId" type="hidden" />
					</td>
					<td align="right" style="width��20%">�ͺŹ��</td>
					<td align="left"  style="width��30%">
						<input id="Model" name="Model" readonly="readonly"/>
					</td>
					
				</tr>
				<tr height="50px">
					<td align="right" style="width��20%">������Χ��</td>
					<td align="left"  style="width��30%">
						<input id="Range" name="Range" readonly="readonly"/>
					</td>
					<td align="right" style="width��20%">��ȷ���ȣ�</td>
					<td align="left"  style="width��30%" >
						<input id="Uncertain" name="Uncertain" readonly="readonly"/>
					</td>
					
				</tr>
				
				<tr height="50px">
					
					<td align="right">�춨���ڣ�</td>
					<td align="left" >
						<input id="TestDate" name="TestDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
					<td align="right">��Ч���ڣ�</td>
					<td align="left" >
						<input id="ValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
				</tr>
				
				<tr height="50px">
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ&nbsp;��&nbsp;�ˣ�</td>
					<td align="left"><input id="Confirmer" name="Confirmer" type="text" class="easyui-validatebox" style="width:152px" valueField="name" textField="name" /><input id="ConfirmerId" name="ConfirmerId" type="hidden" /></td>
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ�����ڣ�</td>
					<td align="left"><input id="ConfirmDate" name="ConfirmDate" type="text" class="easyui-datebox" style="width:152px" /></td>
				</tr>
				<tr  height="50px">
					<td align="right">������λ��</td>
					<td align="left" >
						<input id="Tester" name="Tester" type="text" class="easyui-validatebox" style="width:250px"  required="true" />
					</td>
					<td align="right">����֤��&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
					<td align="left" ><input id="CertificateId" name="CertificateId" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left"  colspan="3">
					<select id="Status" name="Status" >
						<option value="0" selected="selected">����</option>
						<option value="1">ע��</option>
					</select> </td>
				</tr>
				<tr height="30px">
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ�������</td>
					<td align="left"  colspan="3"><textarea id="ConfirmMeasure" name="ConfirmMeasure" cols="55" rows="2" ></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">�ڼ�˲飺</td>
					<td align="left"  colspan="3"><textarea id="DurationCheck" name="DurationCheck" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">ά��������</td>
					<td align="left"  colspan="3"><textarea id="Maintenance" name="Maintenance" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
					<td align="left"  colspan="3"><textarea id="Remark" name="Remark" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">�춨֤��&nbsp;&nbsp;<br />ɨ&nbsp;��&nbsp;����</td>
					<td align="left" colspan="3"><input id="CertificateCopy" name="CertificateCopy" type="file" style="width:350px"/></td>
				</tr>
				
				<tr height="50px">	
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">�޸�</a></td>
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="closed()">�ر�</a></td>
				</tr>
			</table>
			</form>
		
		</div>
	</div>

</DIV>
</DIV>
</body>
</html>
