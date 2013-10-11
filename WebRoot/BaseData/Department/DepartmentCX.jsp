<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>部门信息查询</title>
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
				title:'部门信息查询',
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
					{field:'Name',title:'部门名称',width:80,align:'center'},
					{field:'Brief',title:'拼音简码',width:80,align:'center'},
					{field:'DeptCode',title:'部门代码',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">'+'注销'+'</span>';
							}
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
						$('#add').window('open');
						$('#frm_add_department').show();
					}
				},'-',{
					text:'修改',
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
							$.messager.alert('warning','请选择一个部门','warning');
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
								$.messager.alert('提示','请选择一个项目组','warning');
							}
						}
				},'-',{
						text:'导出',
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
				title:'下属项目组查询',
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
					{field:'Name',title:'项目组名称',width:80,align:'center'},
					{field:'Brief',title:'拼音简码',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">'+'注销'+'</span>';
							}
					}}
				]],
				pagination:true,
				rownumbers:true
			});
			
			$('#employees').datagrid({
				title:'部门员工',
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
					{field:'JobNum',title:'工号',width:80,align:'center'},
					{field:'Name',title:'姓名',width:80,align:'center'},
					{field:'Gender',title:'性别',width:60,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == true)
							{
								rowData['Gender']=0;
							    return "男";
							}
							else if(value == false)
							{
								rowData['Gender']=1;
								return "女";
							}
					}},
					{field:'WorkLocation',title:'所在中心',width:160,align:'center'},
					{field:'Birthplace',title:'出生地',width:80,align:'center'},
					{field:'Birthday',title:'出生年月日',width:100,align:'center'},
					{field:'IDNum',title:'身份证号',width:100,align:'center'},
					{field:'PoliticsStatus',title:'政治面貌',width:80,align:'center'},
					{field:'Nation',title:'民族',width:80,align:'center'},
					{field:'WorkSince',title:'参加工作日期',width:120,align:'center'},
					{field:'WorkHereSince',title:'进所工作时间',width:120,ealign:'center'},
					{field:'Education',title:'学历',width:80,align:'center'},
					{field:'EducationDate',title:'取得学历时间',width:120,align:'center'},
					{field:'EducationFrom',title:'学历毕业院校',width:120,align:'center'},
					{field:'Degree',title:'学位',width:80,align:'center'},
					{field:'DegreeDate',title:'取得学位时间',width:120,align:'center'},
					{field:'DegreeFrom',title:'学位毕业院校',width:120,align:'center'},
					{field:'JobTitle',title:'职称',width:80,align:'center'},
					{field:'Specialty',title:'所学专业',width:80,align:'center'},
					{field:'AdministrationPost',title:'行政职务',width:80,align:'center'},
					{field:'PartyPost',title:'党内职务',width:80,align:'center'},
					{field:'PartyDate',title:'入党时间',width:80,align:'center'},
					{field:'HomeAdd',title:'家庭住址',width:80,align:'center'},
					{field:'WorkAdd',title:'工作地点',width:120,align:'center'},
					{field:'Tel',title:'办公电话',width:80,align:'center'},
					{field:'Cellphone1',title:'手机1',width:80,align:'center'},
					{field:'Cellphone2',title:'手机2',width:80,align:'center'},
					{field:'Email',title:'邮箱',width:120,align:'center'},
					{field:'ProjectTeamId',title:'所属项目组',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						rowData['ProjectTeamId']=value;
						var datas = $('#projects').datagrid('getRows');
						for(var i = 0; i < datas.length; i++)
						{
							if(datas[i].Id==value)
								return datas[i].Name;
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
								return "注销";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Status']=2;
								return "退休";
							}
						}},
					{field:'Type',title:'人员性质',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Type']=0;
							    return "在编";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Type']=1;
								return "人事代理";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Type']=2;
								return "劳务派遣";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Type']=3;
								return "退休返聘";
							}
						}},
					{field:'CancelDate',title:'注销时间',width:80,align:'center'},
					{field:'Signature',title:'签名图片',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='点击下载该文件' >"+value.filename+"</a>";
						}
					},
					{field:'Photograph',title:'照片',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='点击下载该文件' >"+value.filename+"</a>";
						}
					},
					{field:'Remark',title:'备注',width:80,align:'center'}
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
					$.messager.alert('提示',result.msg,'info');
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
					$.messager.alert('提示',result.msg,'info');
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
<form id="frm_export" method="post" action="/jlyw/DepartmentServlet.do?method=7">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="部门详细信息查询管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

	<div region="center">
		<div>
			<br />
			<table id="table1">
				<tr>
					<td width="516" align="right">部门名称：</td>
				  <td width="168" align="left"><input id="name" name="name" type="text"></input></td>
					<td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询
					</a></td>
				</tr>
			</table>
		</div>
		<table id="table2" style="height:450px; width:800px"></table>
        <br/>
		<div id="p2" class="easyui-panel" style="width:800px;height:260px;padding:10px;"
				title="下属项目组" collapsible="false"  closable="false">
			<table id="projects" iconCls="icon-tip" width="850px" height="200px"></table>
			<br />
		</div>
		<br/>
		<div id="p3" class="easyui-panel" style="width:800px;height:260px;padding:10px;"
				title="部门员工" collapsible="false"  closable="false">
			<table id="employees" iconCls="icon-tip"></table>
			<br />
		</div>
		
		<div id="add" class="easyui-window" title="新增" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_department" method="post">
				<table>
					<tr height="30px">
						<td align="right">部门名称：</td>
						<td align="left"><input id="addName" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getAddBrief()"/></td>
						<td align="right">拼音简码：</td>
						<td align="left"><input id="addBrief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr height="30px">
	                	<td align="right">部门代码：</td>
						<td align="left"><input id="addDeptCode" name="DeptCode" type="text" class="easyui-validatebox" required="true"/></td>
						<td align="right">状&nbsp;&nbsp;态：</td>
						<td align="left" colspan="3">
							<select id="addStatus" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
								<option value="0">正常</option>
								<option value="1">注销</option>
							</select>
						</td>
					</tr>
					<tr height="50px">
						<td align="center" colspan="2">
							<a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="add()">新增</a>
						</td>
						<td align="center" colspan="2">
							<a href="#" class="easyui-linkbutton" icon="icon-cancel" name="refresh" href="javascript:void(0)" onclick="closed()">取消</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden" />
						<tr height="30px">
                            <td align="right">部门名称：</td>
                            <td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                            <td align="right">拼音简码：</td>
                            <td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr height="30px">
                            <td align="right">部门代码：</td>
                            <td align="left"><input id="DeptCode" name="DeptCode" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">状&nbsp;&nbsp;态：</td>
                            <td align="left" colspan="3">
                                <select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
                                    <option value="0">正常</option>
                                    <option value="1">注销</option>
                                </select>
                            </td>
                        </tr>
                        <tr height="50px">
                            <td align="center" colspan="2">
                                <a href="#" class="easyui-linkbutton" icon="icon-edit" name="edit" onclick="edit()">确认提交</a>
                            </td>
                            <td align="center" colspan="2">
                                <a href="#" class="easyui-linkbutton" icon="icon-cancel" name="refresh" href="javascript:void(0)" onclick="closed()">取消</a>
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
