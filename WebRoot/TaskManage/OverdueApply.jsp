<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��������</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	$(function(){
		$('#overdue_info_table').datagrid({
			title:'������Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/OverdueServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'������Ϣ',colspan:4,align:'center'},
					{title:'������Ϣ',colspan:4,align:'center'}
				],[
					{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
					{field:'DelayDays',title:'��������',width:60,align:'center'},
					{field:'Reason',title:'����ԭ��',width:80,align:'center'},
					{field:'RequesterTime',title:'����ʱ��',width:80,align:'center'},
					
					{field:'ExecutorName',title:'������',width:80,align:'center'},
					{field:'ExecuteTime',title:'����ʱ��',width:80,align:'center'},
					{field:'ExecutorResult',title:'������',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">����</span>';
							}
							if(1 == value){
								return "ͬ������";
							}
							return "";
						}	
					},
					{field:'ExecuteMsg',title:'��ע��Ϣ',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	
		});
	});
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#overdue_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$("#CommissionSheetId").val('');				
				//$("#Ness").removeAttr("checked");	//ȥ��ѡ
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					if(result.CommissionObj.CommissionStatus != 0 && result.CommissionObj.CommissionStatus !=1 && result.CommissionObj.CommissionStatus !=2){
						$.messager.alert('��ʾ��',"��ί�е������������ڣ�",'info');
						return false;
					}
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//��ѡ
					}
					
					//�������������Ϣ
					$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#overdue_info_table').datagrid('reload');
					
					$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	function doSubmitOverdue(){   	//�ύ���������
		$("#overdue-submit-form").form('submit', {
			url:'/jlyw/OverdueServlet.do?method=2',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//�ж�ѡ��ļ�����Ŀ�Ƿ���Ч
				var projectValue = $('#CommissionSheetId').val();
				if(projectValue==''){
					$.messager.alert('��ʾ��',"��ѡ����Ҫ�������ڵ�ί�е���",'info');
					return false;
				}
				return $("#overdue-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//���¼������������Ϣ
					$('#overdue_info_table').datagrid('reload');
					
					//�������������ġ�������������Ϣ
					$('#DelayDays').val('');
					
				}else{
					$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
				}
			}
		});  
   }  
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��������" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
    <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	<br/>
	
		<div id="p2" class="easyui-panel" style="width:1005px;height:400px;padding:10px;"
				title="��������" collapsible="false"  closable="false">
			<table id="overdue_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="overdue-submit-form" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="8%" style="padding-top:15px;" align="right" >����������					</td>
					
					<td width="21%" style="padding-top:15px;" align="left"><input id="DelayDays"   name="DelayDays" class="easyui-numberbox" style="width:152px;" required="true" />��</td>
					<td width="9%" style="padding-top:15px;" align="right" >
					  ����ԭ��					</td>
					<td width="22%" style="padding-top:15px;" align="left">
				  <input id="Reason" class="easyui-combobox" name="Reason" url="/jlyw/ReasonServlet.do?method=0&type=12" style="width:172px;" valueField="name" textField="name" panelHeight="auto" />					</td>
					<td width="9%" style="padding-top:15px;" align="right" ></td>
				  <td width="31%" style="padding-top:10px;" align="left"></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitOverdue()">�ύ����</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">����</a>--></td>
			  </tr>
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
