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
				
				$('#newKeeperId').combobox({
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
				
				$('#newName').combobox({
				//	url:'/jlyw/CustomerServlet.do?method=6',
					onSelect:function(record){
						$('#newModel').combobox('reload','/jlyw/StandardApplianceServlet.do?method=7&StdAppId='+record.name);
						$('#newRange').combobox('reload','/jlyw/StandardApplianceServlet.do?method=8&StdAppId='+record.name);						
						$('#newUncertain').combobox('reload','/jlyw/StandardApplianceServlet.do?method=9&StdAppId='+record.name);
					},
					onChange:function(newValue, oldValue){
						getnewBrief();						
						var allData = $(this).combobox('getData');
						if(allData != null && allData.length > 0){
							for(var i=0; i<allData.length; i++)
							{
								if(newValue==allData[i].name){
									return false;
								}
							}
						}
						$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=6&QueryName='+newValue);
					}
				});
				
				$('#KeeperId').combobox({
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
						$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
					}
				});
				
				$('#Name').combobox({
				//	url:'/jlyw/CustomerServlet.do?method=6',
					onSelect:function(record){
						$('#Model').combobox('reload','/jlyw/StandardApplianceServlet.do?method=7&StdAppId='+record.name);
						$('#Range').combobox('reload','/jlyw/StandardApplianceServlet.do?method=8&StdAppId='+record.name);						
						$('#Uncertain').combobox('reload','/jlyw/StandardApplianceServlet.do?method=9&StdAppId='+record.name);
					},
					onChange:function(newValue, oldValue){
						getBrief();						
						var allData = $(this).combobox('getData');
						if(allData != null && allData.length > 0){
							for(var i=0; i<allData.length; i++)
							{
								if(newValue==allData[i].name){
									return false;
								}
							}
						}
						$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=6&QueryName='+newValue);
					}
				});
				
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
					onSelect:function(record){},
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
				
				$('#addCertificateCopy').uploadify({
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
				$('#addConfirmer').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
					onSelect:function(record){},
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
					{field:'SeriaNumber',title:'֤���',width:80,align:'center'},
					{field:'ReleaseNumber',title:'�������',width:80,align:'center'},
					{field:'Manufacturer',title:'��������',width:120,align:'center'},
					{field:'ReleaseDate',title:'��������',width:80,align:'center'},
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
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#edit').window('open');
							$('#form1').show();
												
							$('#Name').combobox('setValue',select.Name);
							$('#Brief').val(select.Brief);
							$('#Model').combobox('setValue',select.Model);
							$('#Range').combobox('setValue',select.Range);
							$('#Uncertain').combobox('setValue',select.Uncertain);
							$('#TestCycle').val(select.TestCycle);
							$('#ReleaseNumber').val(select.ReleaseNumber);
							$('#Manufacturer').val(select.Manufacturer);
							$('#ReleaseDate').datebox('setValue',select.ReleaseDate);
							$('#Num').val(select.Num);
							$('#Price').val(select.Price);
							$('#Status').combobox('setValue',select.Status);
							$('#KeeperId').combobox('setValue',select.KeeperId);
							$('#LocaleCode').val(select.LocaleCode);
							$('#PermanentCode').val(select.PermanentCode);
							$('#ProjectCode').val(select.ProjectCode);
							$('#Budget').val(select.Budget);
							$('#InspectMonth').val(select.InspectMonth);
							$('#WarnSlot').val(select.WarnSlot);
							$('#Remark').val(select.Remark);
							$('#Id').val(select.Id);
							$('#form1').form('validate');
						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){
							$.messager.confirm('��ʾ','ȷ��ע����',function(r){
							if(r){	
								$.ajax({
									type:'POST',
									url:'/jlyw/StandardApplianceServlet.do?method=4',
									data:'id='+select.Id,
									dataType:"html",
									success:function(data){
											$('#table2').datagrid('reload');
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
				},'-',{
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
					
					$('#addStandardApplianceName').val(rowData.Name);
					$('#addStandardApplianceId').val(rowData.Id);
					$('#addStandardApplianceModel').val(rowData.Model);
					$('#addStandardApplianceRange').val(rowData.Range);
					$('#addStandardApplianceUncertain').val(rowData.Uncertain);
					
					$('#StandardApplianceName').val(rowData.Name);
					$('#StandardApplianceId').val(rowData.Id);
					$('#StandardApplianceModel').val(rowData.Model);
					$('#StandardApplianceRange').val(rowData.Range);
					$('#StandardApplianceUncertain').val(rowData.Uncertain);
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
							if(value=="")
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
					iconCls:'icon-add',
					handler:function(){
						if($('#table2').datagrid('getSelected')==null)
						{
							$.messager.alert('��ʾ','��ѡ��һ����׼����!','warning');
							return;
						}
						$('#add_testlog').window('open');
						$('#frm_add_testlog').show();
					}
				},'-',{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#testlog').datagrid('getSelected');
						if(select){
							$('#edit_testlog').window('open');
							$('#frm_edit_testlog').show();
															
							$('#TestDate').datebox('setValue',select.TestDate);
							$('#ValidDate').datebox('setValue',select.ValidDate);
							$('#Tester').val(select.Tester);
							$('#CertificateId').val(select.CertificateId);
							$('#ConfirmMeasure').val(select.ConfirmMeasure);
							$('#Confirmer').combobox('setValue',select.Confirmer);
							$('#ConfirmDate').datebox('setValue',select.ConfirmDate);
							$('#DurationCheck').val(select.DurationCheck);
							$('#Maintenance').val(select.Maintenance);
							$('#Remark').val(select.Remark);
							$('#TestLogStatus').combobox('setValue',select.Status);

							
							$('#TestLogId').val(select.Id);
							$('#frm_add_testlog').form('validate');
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
				},'-',{
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
		
		function replace(){
			$.messager.confirm('��ʾ','ȷ���滻��',function(r){
				if(r){
					$('#form1').form('submit',{
						url:'/jlyw/StandardApplianceServlet.do?method=11',
						onSubmit:function(){return $('#form1').form('validate');},
						success:function(data){
							var result = eval("("+data+")");
							$.messager.alert('��ʾ',result.msg,'info');
							if(result.IsOK)
							{
								closed();
								$('#table2').datagrid('reload');
							}
						}
					});
				}
			});
		}
		
		function edit(){
			$('#form1').form('submit',{
				url:'/jlyw/StandardApplianceServlet.do?method=3',
				onSubmit:function(){return $('#form1').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
					{
						closed();
						$('#table2').datagrid('reload');
					}
				}
			});
		}
		
		function doAddTestlog(){
			$('#frm_add_testlog').form('submit',{
				url:'/jlyw/TestLogServlet.do?method=1',
				onSubmit:function(){return $('#frm_add_testlog').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK)
					{
						var Copy = result.CertificateCopy_filesetname;
						var num = $('#CertificateCopy').uploadifySettings('queueSize');
						
						if (num > 0) { //��ѡ���ļ�
							doUploadByUploadify(Copy,'CertificateCopy',false);
						}		
						closed();
					}
					$.messager.alert('��ʾ',result.msg,'info');
					$('#testlog').datagrid('reload');
				}
			});
		}
		
		function doEditTestlog(){
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
					closed();
				}
				$.messager.alert('��ʾ��',result.msg,'info');
				$('#testlog').datagrid('reload');				
			}
		});
	}
		
		function closed(){
			$('#add').dialog('close');
			$('#edit').dialog('close');
			$('#add_testlog').dialog('close');
			$('#edit_testlog').dialog('close');
		}
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/StandardApplianceServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').val()),'queryModel':encodeURIComponent($('#queryModel').val())
			,'queryReleaseNumber':encodeURI($('#queryReleaseNumber').val()),'queryKeeper':encodeURI($('#queryKeeper').combobox('getValue')),'queryLocaleCode':encodeURI($('#queryLocaleCode').val()),'queryPermanentCode':encodeURI($('#queryPermanentCode').val()),'queryInspectMonth':encodeURI($('#queryInspectMonth').val()),'queryStatus':encodeURI($('#queryStatus').combobox('getValue')),'queryDept':encodeURI($('#queryDept').val())};
			$('#table2').datagrid('reload');
			
			$('#queryKeeper').combobox('setValue',"");
			$('#queryStatus').combobox('setValue',"");
		}
		
		function queryTestlog(){
			if($('#table2').datagrid('getSelected')==null)
				return;
			$('#testlog').datagrid('options').url='/jlyw/TestLogServlet.do?method=2';
			$('#testlog').datagrid('options').queryParams={'StdAppId':encodeURI($('#addStandardApplianceId').val()),'queryValidDateFrom':encodeURI($('#queryValidDateFrom').datebox('getValue')),'queryValidDateEnd':encodeURI($('#queryValidDateEnd').datebox('getValue')),'queryTestDateFrom':encodeURI($('#queryTestDateFrom').datebox('getValue')),'queryTestDateEnd':encodeURI($('#queryTestDateEnd').datebox('getValue')),'queryTester':encodeURI($('#queryTester').val()),'queryCertificateId':encodeURI($('#queryCertificateId').val()),'queryStatus':encodeURI($('#queryTestLogStatus').combobox('getValue'))};
			$('#testlog').datagrid('reload');
			$('#queryTestLogStatus').combobox('setValue',"");
		}
			
		function getBrief(){
			$('#Brief').val(makePy($('#Name').combobox('getValue')));
		}	
		
		function getnewBrief(){
			$('#newBrief').val(makePy($('#newName').combobox('getValue')));
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
				  	<td align="left"><input id="queryReleaseNumber" name="queryReleaseNumber" type="text"></input></td>
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
				  	<td align="left"><input id="queryTester" name="queryTester" style="width:152px;" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=12"></input></td>
					<td align="right">֤���ţ�</td>
				  	<td align="left"><input id="queryCertificateId" name="queryCertificateId"  class="easyui-validatebox" type="text"/></td>
                    <td align="right">״̬��</td>
				  	<td align="left"><select id="queryTestLogStatus" name="queryTestLogStatus" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
                                    <option value="0">����</option>
                                    <option value="1">ע��</option>
                                </select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="queryTestlog()">��ѯ</a></td>
				</tr>
			</table>
        <table id="testlog" style="height:500px; width:900px"></table>
        </div>
        
		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 900;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden" />
						<tr height="30px">
                            <td align="right">�������ƣ�</td>
                            <td align="left"><input id="Name" name="Name" type="text" class="easyui-combobox" required="true" valueField="name" textField="name" panelHeight="150px" style="width:152px"/></td>
                            <td align="right">ƴ�����룺</td>
                            <td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">����ͺţ�</td>
                            <td align="left"><input id="Model" name="Model" type="text" class="easyui-combobox" valueField="model" textField="model" panelHeight="150px" style="width:152px"/></td>
                            <td align="right">������Χ��</td>
                            <td align="left"><input id="Range" name="Range" type="text" class="easyui-combobox" valueField="range" textField="range" panelHeight="150px" style="width:152px"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��ȷ���ȣ�</td>
                            <td align="left"><input id="Uncertain" name="Uncertain" type="text" class="easyui-combobox" valueField="uncertain" textField="uncertain" panelHeight="150px" style="width:152px"/></td>
                            <td align="right">�춨���ڣ�</td>
                            <td align="left"><input id="TestCycle" name="TestCycle" type="text" class="easyui-validatebox" required="true" value="12"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�������̣�</td>
                            <td align="left"><input id="Manufacturer" name="Manufacturer" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">������ţ�</td>
                            <td align="left"><input id="ReleaseNumber" name="ReleaseNumber" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�������ڣ�</td>
                            <td align="left"><input id="ReleaseDate" name="ReleaseDate" type="text" class="easyui-datebox" style="width:152px"/></td>
                            <td align="right">����������</td>
                            <td align="left"><input id="Num" name="Num" type="text" class="easyui-numberbox" required="true" value="1"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">���߼۸�</td>
                            <td align="left"><input id="Price" name="Price" type="text" class="easyui-numberbox" required="true"/></td>
                            <td align="right">����״̬��</td>
                            <td align="left">
                                <select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
                                    <option value="0">����</option>
                                    <option value="1">ע��</option>
                                </select>
                            </td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                            <td align="left"><input id="KeeperId" name="KeeperId" type="text" class="easyui-combobox" required="true" valueField="name" textField="name" panelHeight="150px" style="width:152px"/></td>
                            <td align="right">���ڱ�ţ�</td>
                            <td align="left"><input id="LocaleCode" name="LocaleCode" type="text" class="easyui-numberbox" max="999999" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�̶��ʲ�&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
                            <td align="left"><input id="PermanentCode" name="PermanentCode" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">��Ŀ�ƻ�&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
                            <td align="left"><input id="ProjectCode" name="ProjectCode" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�ܼ��·ݣ�</td>
                            <td align="left"><input id="InspectMonth" name="InspectMonth" type="text" class="easyui-numberbox" required="true"/></td>
                            <td align="right">��&nbsp;Ч&nbsp;��&nbsp;&nbsp;<br />Ԥ��������</td>
                            <td align="left"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-numberbox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">Ԥ���ʽ�</td>
                            <td align="left"><input id="Budget" name="Budget" type="text" class="easyui-numberbox" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
                            <td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
                        </tr>
						<tr height="50px">	
							<td align="right"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">�޸�</a></td>
							<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="replace()">���</a></td>
							<td align="left"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
        
         <div id="add_testlog" class="easyui-window" title="����" style="padding: 10px;width: 900;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
		<form id="frm_add_testlog" method="post">
			<table>
				<tr height="50px">
					<td align="right" style="width��20%">��׼���ߣ�</td>
					<td align="left"  style="width��30%" >
						<input id="addStandardApplianceName" name="StandardApplianceName" readonly="readonly" required="true" /><input id="addStandardApplianceId" name="StandardApplianceId" type="hidden"/>
					</td>
					<td align="right" style="width��20%">�ͺŹ��</td>
					<td align="left"  style="width��30%">
						<input id="addStandardApplianceModel" name="StandardApplianceModel" readonly="readonly"/>
					</td>
					
				</tr>
				<tr height="50px">
					<td align="right" style="width��20%">������Χ��</td>
					<td align="left"  style="width��30%">
						<input id="addStandardApplianceRange" name="StandardApplianceRange" readonly="readonly"/>
					</td>
					<td align="right" style="width��20%">��ȷ���ȣ�</td>
					<td align="left"  style="width��30%" >
						<input id="addStandardApplianceUncertain" name="StandardApplianceUncertain" readonly="readonly"/>
					</td>
					
				</tr>
				
				<tr height="50px">
					
					<td align="right">�춨���ڣ�</td>
					<td align="left" >
						<input id="addTestDate" name="TestDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
					<td align="right">��Ч���ڣ�</td>
					<td align="left" >
						<input id="addValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
				</tr>
				
				<tr height="50px">
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ&nbsp;��&nbsp;�ˣ�</td>
					<td align="left"><input id="addConfirmer" name="Confirmer" class="easyui-combobox" style="width:152px" valueField="name" textField="name" /></td>
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ�����ڣ�</td>
					<td align="left"><input id="addConfirmDate" name="ConfirmDate" type="text" class="easyui-datebox" style="width:152px" /></td>
				</tr>
				<tr  height="50px">
					<td align="right">�춨��λ��</td>
					<td align="left" >
						<input id="addTester" name="Tester" type="text" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=12"/>
					</td>
					<td align="right">�춨֤��&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
					<td align="left" ><input id="addCertificateId" name="CertificateId" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left"  colspan="3">
					<select id="addTestLogStatus" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto">
						<option value="0">����</option>
						<option value="1">ע��</option>
					</select> </td>
				</tr>
				<tr height="30px">
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ�������</td>
					<td align="left"  colspan="3"><textarea id="addConfirmMeasure" name="ConfirmMeasure" cols="55" rows="2" ></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">�ڼ�˲飺</td>
					<td align="left"  colspan="3"><textarea id="addDurationCheck" name="DurationCheck" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">ά��������</td>
					<td align="left"  colspan="3"><textarea id="addMaintenance" name="Maintenance" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
					<td align="left"  colspan="3"><textarea id="addRemark" name="Remark" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">�춨֤��&nbsp;&nbsp;<br />ɨ&nbsp;��&nbsp;����</td>
					<td align="left" colspan="3"><input id="addCertificateCopy" name="CertificateCopy" type="file" style="width:350px"/></td>
				</tr>
				
				<tr height="50px">	
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="doAddTestlog()">����</a></td>
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
				</tr>
			</table>
			</form>
		
		</div>
        
        <div id="edit_testlog" class="easyui-window" title="�޸�" style="padding: 10px;width: 900;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
		<form id="frm_edit_testlog" method="post">
			<input id="TestLogId" name="Id" type="hidden"/>
			<table>
				<tr height="50px">
					<td align="right" style="width��20%">��׼���ߣ�</td>
					<td align="left"  style="width��30%" >
						<input id="StandardApplianceName" name="StandardApplianceName" readonly="readonly" required="true" /><input id="StandardApplianceId" name="StandardApplianceId" type="hidden" />
					</td>
					<td align="right" style="width��20%">�ͺŹ��</td>
					<td align="left"  style="width��30%">
						<input id="StandardApplianceModel" name="StandardApplianceModel" readonly="readonly"/>
					</td>
					
				</tr>
				<tr height="50px">
					<td align="right" style="width��20%">������Χ��</td>
					<td align="left"  style="width��30%">
						<input id="StandardApplianceRange" name="StandardApplianceRange" readonly="readonly"/>
					</td>
					<td align="right" style="width��20%">��ȷ���ȣ�</td>
					<td align="left"  style="width��30%" >
						<input id="StandardApplianceUncertain" name="StandardApplianceUncertain" readonly="readonly"/>
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
					<td align="left"><input id="Confirmer" name="Confirmer" class="easyui-combobox" style="width:152px" valueField="name" textField="name" /></td>
					<td align="right">��Դ���&nbsp;&nbsp;<br />ȷ�����ڣ�</td>
					<td align="left"><input id="ConfirmDate" name="ConfirmDate" type="text" class="easyui-datebox" style="width:152px" /></td>
				</tr>
				<tr  height="50px">
					<td align="right">�춨��λ��</td>
					<td align="left" >
						<input id="Tester" name="Tester" type="text" class="easyui-validatebox" required="true" />
					</td>
					<td align="right">�춨֤��&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
					<td align="left" ><input id="CertificateId" name="CertificateId" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left"  colspan="3">
					<select id="TestLogStatus" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto">
						<option value="0">����</option>
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
					<td><a href="#" class="easyui-linkbutton" icon="icon-edit" name="Edit" href="javascript:void(0)" onclick="doEditTestlog()">�޸�</a></td>
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
				</tr>
			</table>
			</form>
		
		</div>
        
	</div>
</DIV>
</DIV>
</body>
</html>
