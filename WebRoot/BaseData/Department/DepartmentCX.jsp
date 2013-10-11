<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>������Ϣ��ѯ</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../../JScript/letter.js"></script>   
 	<script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
		    var lastIndex;
			
			$('#table2').datagrid({
				title:'������Ϣ��ѯ',
				width:800,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/DepartmentServlet.do?method=2',
				sortName: 'DeptCode',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'��������',width:80,align:'center'},
					{field:'Brief',title:'ƴ������',width:80,align:'center'},
					{field:'DeptCode',title:'���Ŵ���',width:80,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">'+'ע��'+'</span>';
							}
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'����',
					iconCls:'icon-add',
					handler:function(){
						$('#add').window('open');
						$('#frm_add_department').show();
					}
				},'-',{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#edit').window('open');
							$('#form1').show();
												
							$('#Name').val(select.Name);
							$('#Brief').val(select.Brief);
							$('#DeptCode').val(select.DeptCode);
							$('#Status').combobox('setValue',select.Status);
							$('#Id').val(select.Id);
							$('#form1').form('validate');
						}else{
							$.messager.alert('warning','��ѡ��һ������','warning');
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
										url:'/jlyw/DepartmentServlet.do?method=4',
										data:'id='+select.Id,
										dataType:"json",
										success:function(data){
											$('#table2').datagrid('reload');
										}
									});
								}
							});
							}
							else
							{
								$.messager.alert('��ʾ','��ѡ��һ����Ŀ��','warning');
							}
						}
				},'-',{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
							myExport();
						}
				}],
				onClickRow:function(rowIndex,rowData){
					$('#projects').datagrid('options').url='/jlyw/ProjectTeamServlet.do?method=5';
					$('#projects').datagrid('options').queryParams={'DepartmentId':rowData.Id};
					$('#projects').datagrid('reload');
					
					$('#employees').datagrid('options').url='/jlyw/UserServlet.do?method=0';
					$('#employees').datagrid('options').queryParams={'queryDepartment':rowData.Id};
					$('#employees').datagrid('reload');
				}
			});
			
			$('#projects').datagrid({
				title:'������Ŀ���ѯ',
				width:750,
				height:200,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				/*frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],*/
				columns:[[
					{field:'Name',title:'��Ŀ������',width:80,align:'center'},
					{field:'Brief',title:'ƴ������',width:80,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">'+'ע��'+'</span>';
							}
					}}
				]],
				pagination:true,
				rownumbers:true
			});
			
			$('#employees').datagrid({
				title:'����Ա��',
				width:750,
				height:200,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				columns:[[
					{field:'JobNum',title:'����',width:80,align:'center'},
					{field:'Name',title:'����',width:80,align:'center'},
					{field:'Gender',title:'�Ա�',width:60,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == true)
							{
								rowData['Gender']=0;
							    return "��";
							}
							else if(value == false)
							{
								rowData['Gender']=1;
								return "Ů";
							}
					}},
					{field:'WorkLocation',title:'��������',width:160,align:'center'},
					{field:'Birthplace',title:'������',width:80,align:'center'},
					{field:'Birthday',title:'����������',width:100,align:'center'},
					{field:'IDNum',title:'���֤��',width:100,align:'center'},
					{field:'PoliticsStatus',title:'������ò',width:80,align:'center'},
					{field:'Nation',title:'����',width:80,align:'center'},
					{field:'WorkSince',title:'�μӹ�������',width:120,align:'center'},
					{field:'WorkHereSince',title:'��������ʱ��',width:120,ealign:'center'},
					{field:'Education',title:'ѧ��',width:80,align:'center'},
					{field:'EducationDate',title:'ȡ��ѧ��ʱ��',width:120,align:'center'},
					{field:'EducationFrom',title:'ѧ����ҵԺУ',width:120,align:'center'},
					{field:'Degree',title:'ѧλ',width:80,align:'center'},
					{field:'DegreeDate',title:'ȡ��ѧλʱ��',width:120,align:'center'},
					{field:'DegreeFrom',title:'ѧλ��ҵԺУ',width:120,align:'center'},
					{field:'JobTitle',title:'ְ��',width:80,align:'center'},
					{field:'Specialty',title:'��ѧרҵ',width:80,align:'center'},
					{field:'AdministrationPost',title:'����ְ��',width:80,align:'center'},
					{field:'PartyPost',title:'����ְ��',width:80,align:'center'},
					{field:'PartyDate',title:'�뵳ʱ��',width:80,align:'center'},
					{field:'HomeAdd',title:'��ͥסַ',width:80,align:'center'},
					{field:'WorkAdd',title:'�����ص�',width:120,align:'center'},
					{field:'Tel',title:'�칫�绰',width:80,align:'center'},
					{field:'Cellphone1',title:'�ֻ�1',width:80,align:'center'},
					{field:'Cellphone2',title:'�ֻ�2',width:80,align:'center'},
					{field:'Email',title:'����',width:120,align:'center'},
					{field:'ProjectTeamId',title:'������Ŀ��',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						rowData['ProjectTeamId']=value;
						var datas = $('#projects').datagrid('getRows');
						for(var i = 0; i < datas.length; i++)
						{
							if(datas[i].Id==value)
								return datas[i].Name;
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
								return "ע��";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Status']=2;
								return "����";
							}
						}},
					{field:'Type',title:'��Ա����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Type']=0;
							    return "�ڱ�";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Type']=1;
								return "���´���";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Type']=2;
								return "������ǲ";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Type']=3;
								return "���ݷ�Ƹ";
							}
						}},
					{field:'CancelDate',title:'ע��ʱ��',width:80,align:'center'},
					{field:'Signature',title:'ǩ��ͼƬ',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
						}
					},
					{field:'Photograph',title:'��Ƭ',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
						}
					},
					{field:'Remark',title:'��ע',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true
			});
		});
		
		function add(){
			$('#frm_add_department').form('submit',{
				url:'/jlyw/DepartmentServlet.do?method=1',
				onSubmit:function(){return $('#frm_add_department').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
						closed();
					$('#table2').datagrid('reload');
				}
			});
		}
		
		function edit(){
			$('#form1').form('submit',{
				url:'/jlyw/DepartmentServlet.do?method=3',
				onSubmit:function(){return $('#form1').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
						closed();
					$('#table2').datagrid('reload');
				}
			});
		}
		
		function closed(){
			$('#edit').dialog('close');
			$('#add').dialog('close');
		}
		
		
		function query(){
			$('#table2').datagrid('unselectAll');
			$('#table2').datagrid('options').url='/jlyw/DepartmentServlet.do?method=2';
			$('#table2').datagrid('options').queryParams={'DepartmentName':encodeURI($('#name').val())};
			$('#table2').datagrid('reload');
		}
		
		function getBrief(){
			$('#Brief').val(makePy($('#Name').val()));
		}
		
		function getAddBrief(){
			$('#addBrief').val(makePy($('#addName').val()));
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
<form id="frm_export" method="post" action="/jlyw/DepartmentServlet.do?method=7">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="������ϸ��Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

	<div region="center">
		<div>
			<br />
			<table id="table1">
				<tr>
					<td width="516" align="right">�������ƣ�</td>
				  <td width="168" align="left"><input id="name" name="name" type="text"></input></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">��ѯ
					</a></td>
				</tr>
			</table>
		</div>
		<table id="table2" style="height:450px; width:800px"></table>
        <br/>
		<div id="p2" class="easyui-panel" style="width:800px;height:260px;padding:10px;"
				title="������Ŀ��" collapsible="false"  closable="false">
			<table id="projects" iconCls="icon-tip" width="850px" height="200px"></table>
			<br />
		</div>
		<br/>
		<div id="p3" class="easyui-panel" style="width:800px;height:260px;padding:10px;"
				title="����Ա��" collapsible="false"  closable="false">
			<table id="employees" iconCls="icon-tip"></table>
			<br />
		</div>
		
		<div id="add" class="easyui-window" title="����" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_department" method="post">
				<table>
					<tr height="30px">
						<td align="right">�������ƣ�</td>
						<td align="left"><input id="addName" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getAddBrief()"/></td>
						<td align="right">ƴ�����룺</td>
						<td align="left"><input id="addBrief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr height="30px">
	                	<td align="right">���Ŵ��룺</td>
						<td align="left"><input id="addDeptCode" name="DeptCode" type="text" class="easyui-validatebox" required="true"/></td>
						<td align="right">״&nbsp;&nbsp;̬��</td>
						<td align="left" colspan="3">
							<select id="addStatus" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
								<option value="0">����</option>
								<option value="1">ע��</option>
							</select>
						</td>
					</tr>
					<tr height="50px">
						<td align="center" colspan="2">
							<a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="add()">����</a>
						</td>
						<td align="center" colspan="2">
							<a href="#" class="easyui-linkbutton" icon="icon-cancel" name="refresh" href="javascript:void(0)" onclick="closed()">ȡ��</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden" />
						<tr height="30px">
                            <td align="right">�������ƣ�</td>
                            <td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                            <td align="right">ƴ�����룺</td>
                            <td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr height="30px">
                            <td align="right">���Ŵ��룺</td>
                            <td align="left"><input id="DeptCode" name="DeptCode" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">״&nbsp;&nbsp;̬��</td>
                            <td align="left" colspan="3">
                                <select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
                                    <option value="0">����</option>
                                    <option value="1">ע��</option>
                                </select>
                            </td>
                        </tr>
                        <tr height="50px">
                            <td align="center" colspan="2">
                                <a href="#" class="easyui-linkbutton" icon="icon-edit" name="edit" onclick="edit()">ȷ���ύ</a>
                            </td>
                            <td align="center" colspan="2">
                                <a href="#" class="easyui-linkbutton" icon="icon-cancel" name="refresh" href="javascript:void(0)" onclick="closed()">ȡ��</a>
                            </td>
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
