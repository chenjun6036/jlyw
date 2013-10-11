<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>��׼������Ϣ��ѯ</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
		<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
        <script type="text/javascript"	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
            <script type="text/javascript" src="../../JScript/letter.js"></script>
            <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
			<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
            <script type="text/javascript" src="../../JScript/upload.js"></script>
            <script type="text/javascript" src="../../JScript/json2.js"></script>
	<script>
		$(function(){				
				$('#queryKeeper').combobox({
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
						$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
					}
				});
			
			$('#table2').datagrid({
				title:'��׼������Ϣ��ѯ',
				width:900,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/StandardApplianceServlet.do?method=2',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'LocaleCode',title:'���ڱ��',width:80,align:'center'},
					{field:'Name',title:'��������',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'Brief',title:'ƴ������',width:80,align:'center'},
					{field:'Model',title:'����ͺ�',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Uncertain',title:'��ȷ����',width:80,align:'center'},
					{field:'TestCycle',title:'�춨����',width:80,align:'center'},
					{field:'SeriaNumber',title:'֤����',width:80,align:'center'},
					{field:'Manufacturer',title:'��������',width:120,align:'center'},
					{field:'ReleaseDate',title:'��������',width:80,align:'center'},
					{field:'ReleaseNumber',title:'�������',width:80,align:'center'},
					{field:'Num',title:'��������',width:80,align:'center'},
					{field:'Price',title:'���߼۸�',width:80,align:'center'},
					{field:'Status',title:'����״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">ע��</span>';
							}
					}},
					{field:'KeeperId',title:'������',width:70,align:'center'},
					{field:'PermanentCode',title:'�̶��ʲ����',width:90,align:'center'},
					{field:'ProjectCode',title:'��Ŀ�ƻ����',width:90,align:'center'},
					{field:'InspectMonth',title:'�ܼ��·�',width:90,align:'center'},
					{field:'WarnSlot',title:'��Ч��Ԥ������',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null)
							return "";
						return value+"��";
					}},
					{field:'ValidDate',title:'��Ч��',width:120,align:'center'},
					{field:'Budget',title:'Ԥ���ʽ�',width:80,align:'center'},
					{field:'Remark',title:'��ע',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
							myExport();
						}
				},'-',{
						text:'Ԥ����׼����',
						iconCls:'icon-tip',
						handler:function(){
							$('#table2').datagrid('options').queryParams={'Warn':encodeURI(1)};
							$('#table2').datagrid('reload');
						}
				}],
				onSelect:function(rowIndex,rowData){
					$('#testlog').datagrid('options').url='/jlyw/TestLogServlet.do?method=2';
					$('#testlog').datagrid('options').queryParams={'StdAppId':encodeURI(rowData.Id)};
					$('#testlog').datagrid('reload');
				}
			});
			
			$('#testlog').datagrid({
				title:'��ֵ��Դ��¼',
				width:850,
				height:300,
				singleSelect:false, 
				nowrap: false,
				striped: true,
				url:'',
				sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CertificateId',title:'�춨֤����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status!=0)
							return '<span style="color:red">' + value + '</span>';
						else
							return value;	
					}},
					{field:'TestDate',title:'�춨����',width:120,align:'center'},				
					{field:'ValidDate',title:'��Ч����',width:120,align:'center'},
					{field:'Tester',title:'�춨��λ',width:150,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value==0)
								return "����";
							else
								return '<span style="color:red">ע��</span>';
						}
					},
					{field:'ConfirmMeasure',title:'��Դ���ȷ���������ʩ',width:200,align:'center'},	
					{field:'Confirmer',title:'��Դ���ȷ����',width:80,align:'center'},
					{field:'ConfirmDate',title:'��Դ���ȷ������',width:120,align:'center'},
					{field:'Copy',title:'�춨֤��ɨ���',width:120,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value==""||value==null)
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
						}
					},
					{field:'DurationCheck',title:'�ڼ�˲�',width:200,align:'center'},
					{field:'Maintenance',title:'ά������',width:200,align:'center'},
					{field:'Remark',title:'��ע',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
						ExportTestLog();
					}
				}]
			});
			
			$('#queryKeeper').combobox('setValue',"");
			$('#queryStatus').combobox('setValue',"");	
			$('#queryTestLogStatus').combobox('setValue',"");
		
		});		
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/StandardApplianceServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').val()),'queryModel':encodeURIComponent($('#queryModel').val())
			,'querySeriaNumber':encodeURI($('#querySeriaNumber').val()),'queryKeeper':encodeURI($('#queryKeeper').combobox('getValue')),'queryLocaleCode':encodeURI($('#queryLocaleCode').val()),'queryPermanentCode':encodeURI($('#queryPermanentCode').val()),'queryInspectMonth':encodeURI($('#queryInspectMonth').val()),'queryStatus':encodeURI($('#queryStatus').combobox('getValue')),'queryDept':encodeURI($('#queryDept').val())};
			$('#table2').datagrid('reload');
			
			$('#queryKeeper').combobox('setValue',"");
			$('#queryStatus').combobox('setValue',"");
		}
		
		function queryTestlog(){
			if($('#table2').datagrid('getSelected')==null)
				return;
			$('#testlog').datagrid('unselectAll');
			$('#testlog').datagrid('options').url='/jlyw/TestLogServlet.do?method=2';
			$('#testlog').datagrid('options').queryParams={'StdAppId':encodeURI($('#table2').datagrid('getSelected').Id),'queryValidDateFrom':encodeURI($('#queryValidDateFrom').datebox('getValue')),'queryValidDateEnd':encodeURI($('#queryValidDateEnd').datebox('getValue')),'queryTestDateFrom':encodeURI($('#queryTestDateFrom').datebox('getValue')),'queryTestDateEnd':encodeURI($('#queryTestDateEnd').datebox('getValue')),'queryTester':encodeURI($('#queryTester').val()),'queryCertificateId':encodeURI($('#queryCertificateId').val()),'queryStatus':encodeURI($('#queryTestLogStatus').combobox('getValue'))};
			$('#testlog').datagrid('reload');
			$('#queryTestLogStatus').combobox('setValue',"");
		}
			
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
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
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
		function ExportTestLog(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr_testlog').val(JSON.stringify($('#testlog').datagrid('options').queryParams));
			$('#frm_export_testlog').form('submit',{
				success:function(data){
					var result = eval("("+ data +")");
					if(result.IsOK)
					{
						$('#filePath_testlog').val(result.Path);
						$('#frm_down_testlog').submit();
						CloseWaitingDlg();
					}
					else
					{
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
		</script>

</head>

<body>
<form id="frm_export" method="post" action="/jlyw/StandardApplianceServlet.do?method=12">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form id="frm_export_testlog" method="post" action="/jlyw/TestLogServlet.do?method=5">
<input id="paramsStr_testlog" name="paramsStr" type="hidden" />
</form>
<form id="frm_down_testlog" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath_testlog" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��׼������ϸ��Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

	<div region="center">
		<div>
			<br />
			<table id="table1"  style="width:900px">
				<tr>
					<td align="right">�������ƣ�</td>
				  	<td align="left"><input id="queryname" name="queryname" type="text"></input></td>
                 	<td align="right">�ͺŹ�񡢲�����Χ����ȷ���ȣ�</td>
				  	<td align="left"><input id="queryModel" name="queryModel" type="text"></input></td>
                    <td align="right">���ţ�</td>
				    <td align="left"><input id="queryDept" name="queryDept" type="text"/></td>
				</tr>
                <tr>
					<td align="right">�����ˣ�</td>
				  	<td align="left"><input id="queryKeeper" name="queryKeeper" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    <td align="right">������ţ�</td>
				  	<td align="left"><input id="querySeriaNumber" name="querySeriaNumber" type="text"></input></td>
                 	<td align="right">���ڱ�ţ�</td>
				  	<td align="left"><input id="queryLocaleCode" name="queryLocaleCode" type="text"></input></td>
                    <td></td>
				</tr>
                <tr>
                    <td align="right">�̶��ʲ���ţ�</td>
				  	<td align="left"><input id="queryPermanentCode" name="queryPermanentCode" type="text"></input></td>
					<td align="right">�ܼ��·ݣ�</td>
				  	<td align="left"><input id="queryInspectMonth" name="queryInspectMonth"  class="easyui-numberbox" type="text"/></td>
                 	<td align="right">״̬��</td>
				  	<td align="left">
                    			<select id="queryStatus" name="queryStatus" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value="0">����</option>
                                    <option value="1">ע��</option>
                                </select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">��ѯ</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:900px"></table>
        
        <br />
        <div id="p2" class="easyui-panel" style="width:900px;height:450px;padding:10px;"
				title="��ֵ��Դ��¼����" collapsible="false"  closable="false">
                <table  style="width:800px">
				<tr>
					<td align="right">��Ч�ڣ�</td>
					<td width="168" align="left">
						<input name="date1" id="queryValidDateFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
                    <td align="center">��</td>
					<td width="168" align="left">
						<input name="date2" id="queryValidDateEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
				</tr>
                <tr>
                   <td align="right">�춨���ڣ�</td>
					<td width="168" align="left">
						<input name="date3" id="queryTestDateFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
                    <td align="center">��</td>
					<td width="168" align="left">
						<input name="date4" id="queryTestDateEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
				</tr>
                <tr>
                    <td align="right">�춨��λ��</td>
				  	<td align="left"><input id="queryTester" name="queryTester" style="width:152px;" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=12"></input></td>
					<td align="right">֤���ţ�</td>
				  	<td align="left"><input id="queryCertificateId" name="queryCertificateId"  class="easyui-validatebox" type="text"/></td>
                    <td align="right">״̬��</td>
				  	<td align="left"><select id="queryTestLogStatus" name="queryTestLogStatus" class="easyui-combobox" style="width:152px" panelHeight="auto">
                                    <option value="0">����</option>
                                    <option value="1">ע��</option>
                                </select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="queryTestlog()">��ѯ</a></td>
				</tr>
			</table>
        <table id="testlog" style="height:500px; width:900px"></table>
        </div>
        
	</div>
</DIV>
</DIV>
</body>
</html>
