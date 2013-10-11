<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*,com.jlyw.manager.*" %>
<% 
		int proId = (int)(session.getAttribute("LOGIN_USER")==null?0:((SysUser)session.getAttribute("LOGIN_USER")).getProjectTeamId());
		Department dept = (new ProjectTeamManager()).findById(proId).getDepartment();
		int deptId = dept.getId();
		String deptName = dept.getName();
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ѯ���ڿ���ҵ����</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
	<script>		
		$(function(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
			var row = {'Id':<%=deptId%>,'Name':'<%=deptName%>'};
			$('#departmentid').combobox('loadData',[row]);
			$('#departmentid').combobox('select',<%=deptId%>);
			
		})
		
		$(function(){
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:500,
				title:'ҵ������Ϣ',
//				iconCls:'icon-save',
//              pageSize:10,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				//url:'/jlyw/StatisticServlet.do?method=0',
			//	sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
			//	idField:'id',
				frozenColumns:[[
						{field:'ck',checkbox:true}
					]],
				columns:[[
					{field:'Code',title:'ί�е���',sortable:true,width:150,align:'center'},
				    {field:'CustomerName',title:'ί�е�λ����',width:150,align:'center'},
					{field:'ApplianceName',title:'��������',width:80,align:'center'},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					{field:'CStatus',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex)
					{
						return getCommissionSheetStatusInfo(value);
					}},
					{field:'TotalFee',title:'�ܷ���',width:70,align:'center'},
					{field:'TestFee',title:'����',width:70,align:'center'},
					{field:'RepairFee',title:'�����',width:70,align:'center'},
					{field:'MaterialFee',title:'���Ϸ�',width:70,align:'center'},
					{field:'DebugFee',title:'���Է�',width:70,align:'center'},
					{field:'CarFee',title:'��ͨ��',width:70,align:'center'},
					{field:'OtherFee',title:'��������',width:70,align:'center'}
                ]],
				pagination:true,
				rownumbers:true,
				onLoadSuccess:function(data){
					if($('#departmentid').combobox('getValue')=="")
						return;
					var label = document.getElementById('statistics');
					var department = $('#departmentid').combobox('getText');
					label.innerHTML = $('#dateTimeFrom').datebox('getValue') + "��" + $('#dateTimeEnd').datebox('getValue') + department +"����ҵ��" + data.total + "�������У����깤" + data.doneTotal + "��";
				},
				toolbar:[{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				}],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10||rowData.Status == "10"){	//��ע��
						return 'color:#FF0000';
					}else if(rowData.Status == 0||rowData.Status == "0"){	//���ռ�
						return 'color:#0000FF';	
					}else if(rowData.Status == 1||rowData.Status == "1"){	//�ѷ���
						return 'color:#0000FF';	
					}else if(rowData.Status == 2||rowData.Status == "2"){	//ת����
						return 'color:#CCFF00';	
					}else if(rowData.Status == 3||rowData.Status == "3"){	//���깤
						return 'color:#000000';	
					}else if(rowData.Status == 4||rowData.Status == "4"){  //�ѽ���
						return 'color:#008000';
					}else{
						return 'color:#000000';
					}
				}
			});
			
			$("#departmentid").combobox('disable');
			
		});
		
		function query(){
			if(!$("#searchForm").form('validate'))
				return false ;
			$('#result').datagrid('loadData', {'total':0, 'rows':[]});
			$('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=0';
			$('#result').datagrid('options').queryParams={'DepartmentId':encodeURI($('#departmentid').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'Status':encodeURI($('#Status').val()),'HeadName':encodeURI($('#HeadName').combobox('getValue'))};
			$('#result').datagrid('reload');
		}
		
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#result').datagrid('options').queryParams));
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
		
		function reset(){
			$('#deparmentid').combobox('setValue',"");
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ѯ����ҵ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
    <form id="frm_export" method="post" action="/jlyw/QueryServlet.do?method=11">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:135px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
                <form id="searchForm">
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >��  �ң�</td>
					<td width="22%" align="left" >
						<input id="departmentid" class="easyui-combobox" name="department" url="/jlyw/DepartmentServlet.do?method=6" style="width:150px;" valueField="Id" textField="Name" panelHeight="auto" required="true" />
					</td>
					<td width="10%" align="right">��ʼʱ�䣺</td>
					<td width="22%" align="left">
						<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" />
					</td>
					<td width="10%" align="right">����ʱ�䣺</td>
					<td width="22%" align="left">
						<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" />
					</td>
				</tr >
				<tr height="30px">
					<td width="10%" align="right">̨ͷ��λ��</td>
					<td width="22%" align="left" colspan="5">
						<input name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="headname" textField="headname" panelHeight="auto" url="/jlyw/AddressServlet.do?method=1"/>
					</td>
				</tr>
				<tr height="40px">
				    <td width="10%" colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
				    <td width="21%" colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				</tr>
				
		</table>
        </form>
		</div>
		<br />
      <div style="width:900px;height:500px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>
	  <br />
	  <div id="p1" class="easyui-panel" style="width:900px;height:80px;padding:10px;"
				title="ͳ�ƽ��" collapsible="false"  closable="false" align="center">
                <label id="statistics"></label>
            <!--<table width="100px" align="right">
				<tr >
					<td  width="33%" align="center">
					<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onClick="myExport()">����</a>
					</td>					
				</tr>
		  </table>-->
		</div>
</div>
</DIV>
</DIV>
</body>
</html>
