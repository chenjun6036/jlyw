<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�����Ϣ�����ѯ</title>
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
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					//'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../../uploadify/selectfiles.png',
					//'fileDesc'  : '֧�ָ�ʽ:rar/zip/7z', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					//'fileExt'   : '*.rar;*.zip;*.7z;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});
				
				/*$('#methodConfirm').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '֧�ָ�ʽ:rar/zip/7z', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});*/
			});
	
		$(function(){
		    var lastIndex;
			$('#table2').datagrid({
				title:'�����淶��Ϣ��ѯ',
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
					{field:'SpecificationCode',title:'�����淶���',width:100,align:'center'},
					{field:'NameCn',title:'�����淶����(����)',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'�����淶����(Ӣ��)',width:120,align:'center'},
					{field:'Brief',title:'ƴ������',width:100,align:'center'},
					{field:'Type',title:'���',width:80,align:'center'},
					{field:'InCharge',title:'�Ƿ��ܿ�',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['InCharge']=0;
							    return "��";
							}
							else if(value == 1 || value == '1')
							{
								rowData['InCharge']=1;
								return "��";
							}
						}},
					{field:'Status',title:'״̬',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return '<span style="color:red">ע��</span>';
							}
							else if(value == 2 || value == '2')
							{
								rowData['Status']=2;
								return "��δʵʩ";
							}
						}},
					{field:'LocaleCode',title:'���ڱ��',width:80,align:'center'},
					{field:'IssueDate',title:'��������',width:80,align:'center'},
					{field:'EffectiveDate',title:'ʵʩ����',width:80,ealign:'center'},
					{field:'RepealDate',title:'��ֹ����',width:80,align:'center'},
					{field:'OldSpecification',title:'����μ����淶',width:120,align:'center'},
					{field:'WarnSlot',title:'��Ч��Ԥ������',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null)
							return "";
						return value+"��";
					}},
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
						text:'Ԥ�������淶',
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
					$.messager.alert('��ʾ',result.msg,'info');
					$('#table2').datagrid('reload');
				}
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#nameCn').val()));
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
		
		function replace(){
			$.messager.confirm('��ʾ','ȷ���滻��',function(r){
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
							$.messager.alert('��ʾ',result.msg,'info');
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
			<jsp:param name="TitleName" value="�����淶��Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:900px">
				<tr>
                <td colspan="2"></td>
					<td width="128" align="right">��̱�ţ�</td>
			      <td width="177" align="left"><input id="querycode" name="querycode" type="text" /></td>
					<td width="128" align="right">������ƣ�</td>
				  <td width="168" align="left"><input id="queryname" name="queryname" type="text" /></td>
				</tr>
                <tr>
					<td width="128" align="right">�淶���</td>
			      <td width="177" align="left">
                  				<input id="queryType" name="queryType" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=13"/>
                    </td>
                    <td width="128" align="right">״̬��</td>
			      <td width="177" align="left">
                  				<select id="queryStatus" name="queryStatus" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value='0'>����</option>
                                    <option value='1'>ע��</option>
                                    <option value='2'>��δʵʩ</option>
                                </select>
                    </td>
					<td width="128" align="right">�Ƿ��ܿأ�</td>
				  <td width="168" align="left"><select id="queryInCharge" name="queryInCharge" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value='0'>��</option>
                                    <option value='1'>��</option>
                                </select></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">��ѯ</a></td>
				</tr>
			</table>
		</div>
		

		<table id="table2" style="height:500px; width:900px"></table>
		<div id="p4" class="easyui-panel" style="width:900px;height:250px;"
         title="�����ϴ�" collapsible="false"  closable="false" scroll="no">
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
                   <td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByDefault()">�ϴ��ļ�</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">ȡ���ϴ�</a> </td>
               </tr>
           </table>
        </div>
        
		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 500;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1"  method="post">
				<div>
					<table id="table3" style="padding: 10px;width: 500;height: 500;">
					<input id="Id" name="Id" type="hidden"/>
						<tr height="30px">
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
                            <td align="left"><input id="specode" name="SpecificationCode" type="text"  class="easyui-validatebox" required="true"/></td>
                            <td align="right">�������ƣ�</td>
                            <td align="left"><input id="nameCn" name="NameCn" type="text"  class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">ƴ�����룺</td>
                            <td align="left"><input id="brief" name="Brief" type="text"  class="easyui-validatebox" required="true"/></td>
                            <td align="right">Ӣ�����ƣ�</td>
                            <td align="left"><input id="nameEn" name="NameEn" type="text"/></td>
                        </tr>
                        <tr height="30px">	
                            <td align="right">������</td>
                            <td align="left" >
                                <input id="type" name="Type" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=13"/>
                            </td>
                            <td align="right">�Ƿ��ܿأ�</td>
                            <td align="left" ><select id="incharge" name="InCharge" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
                                    <option value='0'>��</option>
                                    <option value='1'>��</option>
                                </select>
                            </td>
                        </tr>
                        <tr  height="30px">
                            <td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
                            <td align="left">
                                <select id="status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
                                    <option value='0'>����</option>
                                    <option value='1'>ע��</option>
                                </select>
                            </td>
                            <td align="right">���ڱ�ţ�</td>
                            <td align="left" ><input id="localecode" name="LocaleCode" type="text" /></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">�������ڣ�</td>
                            <td align="left"><input id="issueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
                            <td align="right">ʵʩ���ڣ�</td>
                            <td align="left"><input id="effectiveDate" name="EffectiveDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">��ֹ���ڣ�</td>
                            <td align="left"><input id="repealDate" name="RepealDate" type="text" class="easyui-datebox" style="width:152px"/></td>
                            <td align="right">����ι�̣�</td>
                            <td align="left" ><input id="oldSpecification" name="OldSpecification" type="text"/></td>
                        </tr>
                        <tr>
                            <td align="right">��&nbsp;Ч&nbsp;��&nbsp;&nbsp;<br />Ԥ��������</td>
                            <td align="left" colspan="3"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
                            <td align="left" colspan="3"><textarea id="remark" name="Remark" cols="56" rows="4"></textarea></td>
                        </tr>
                        <!--<tr height="30px">
                            <td align="right">�����淶&nbsp;&nbsp;<br />ɨ&nbsp;��&nbsp;����</td>
                            <td align="left" colspan="3"><input id="copy" name="Copy" type="file" style="width:400px"/></td>
                        </tr>
                        <tr height="30px">
                            <td align="right">����ȷ�ϱ�<br />ɨ&nbsp;��&nbsp;����</td>
                            <td align="left" colspan="3"><input id="methodConfirm" name="MethodConfirm" type="file" style="width:400px"/></td>
                        </tr>-->
                        <tr height="50px">	
                            <td align="right"><a class="easyui-linkbutton" icon="icon-ok" name="Edit" href="javascript:void(0)" onclick="edit()">�޸�</a></td>
                            <td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-edit" name="replace" href="javascript:void(0)" onclick="replace()">�滻</a></td>
                            <td align="left"><a class="easyui-linkbutton" icon="icon-cancel" name="Cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
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
