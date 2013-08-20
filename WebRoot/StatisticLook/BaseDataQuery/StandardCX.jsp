<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>������׼��Ϣ��ѯ</title>
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
				title:'������׼��Ϣ��ѯ',
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
					{field:'SLocaleCode',title:'���ڱ��',width:80,align:'center'},
					{field:'CertificateCode',title:'������׼֤���',width:100,align:'center'},
					{field:'Name',title:'������׼����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'Ӣ������',width:100,align:'center'},
					{field:'Brief',title:'ƴ������',width:100,align:'center'},				
					{field:'StandardCode',title:'������׼����',width:80,align:'center'},
					{field:'ProjectCode',title:'��Ŀ���',width:80,align:'center'},
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
					}},
					{field:'CreatedBy',title:'���굥λ',width:120,align:'center'},
					{field:'IssuedBy',title:'��֤��λ',width:120,align:'center'},
					{field:'IssueDate',title:'��֤����',width:100,align:'center'},
					{field:'ValidDate',title:'��Ч��',width:100,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Uncertain',title:'��ȷ�������',width:120,align:'center'},
					{field:'SIssuedBy',title:'���֤��֤����',width:120,align:'center'},
					{field:'SCertificateCode',title:'���֤֤���',width:120,align:'center'},
					{field:'SIssueDate',title:'���֤��֤����',width:120,align:'center'},
					{field:'SValidDate',title:'���֤��Ч����',width:120,align:'center'},
					{field:'SRegion',title:'���֤��Ч����',width:120,align:'center'},
					{field:'SAuthorizationCode',title:'���֤��Ȩ֤���',width:120,align:'center'},
					{field:'WarnSlot',title:'��Ч��Ԥ������',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null)
							return "";
						return value+"��";
					}},
					{field:'Handler',title:'��Ŀ������',width:80,align:'center'},
					{field:'ProjectType',title:'��Ŀ����',width:120,align:'center'},
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
						text:'�ѳ��ڱ�׼',
						iconCls:'icon-tip',
						handler:function(){
								$('#table2').datagrid('options').queryParams={'OverValid':encodeURI(1)};
								$('#table2').datagrid('reload');
						}
				},'-',{
						text:'Ԥ����׼',
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
			<jsp:param name="TitleName" value="������׼��ϸ��Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<br />
			<table id="table1" style="width:900px">
				<tr>
				  <td align="right">������׼���ƣ�</td>
				  <td width="168" align="left"><input id="queryName" name="queryName" type="text"></input></td>
                  <td align="right">��֤��λ��</td>
				  <td width="168" align="left"><input id="queryIssuedBy" name="queryIssuedBy" type="text"></input></td>
                  <td align="right">״̬��</td>
				  <td width="168" align="left">
                  				<select id="queryStatus" name="queryStatus" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
                                    <option value='0' selected>����</option>
                                    <option value='1'>ע��</option>
                                </select>
                   </td>
				</tr>
                <tr>
					<td align="right">������׼���</td>
				  <td width="168" align="left"><input id="queryType" name="queryType" url="/jlyw/ApplianceSpeciesServlet.do?method=2" class="easyui-combobox" valueField="Name" textField="Name" style="width:152px;" editable="false"></input></td>
                  <td align="right">������׼�����ˣ�</td>
				  <td width="168" align="left"><input id="queryHandler" name="queryHandler" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                  <td align="right">���ţ�</td>
				  <td width="168" align="left"><input id="queryDept" name="queryDept" type="text"/></td>
                                
				</tr>
                <tr>
					<td align="right">��Ч�ڣ�</td>
					<td width="168" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
                    <td align="center">��</td>
					<td width="168" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" >
					</td>
					<td colspan="2" align="right"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">��ѯ</a></td>
				</tr>
			</table>
		</div>
		
		<table id="table2" style="height:500px; width:900px"></table>
        
		<div id="p4" class="easyui-panel" style="width:900px;height:250px;"
         title="�����ϴ�" collapsible="false"  closable="false" scroll="no">
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
