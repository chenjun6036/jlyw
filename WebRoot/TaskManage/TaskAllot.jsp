<%@ page contentType="text/html; charset=gb2312" language="java" import="com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�������</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	$(function(){		
		$("#ProjectSelect").combobox({
			valueField:'appStdProId',
			textField:'appStdProName',
			editable:false,
			panelHeight:'auto', 
			onSelect:function(record){
				$("#AlloteeSelect").combobox('clear');
				$("#AlloteeSelect").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+record.speciesType+'&ApplianceSpeciesId='+record.appStdNameId+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//������Ա
//				$("#AlloteeSelect").combobox('loadData', record.AlloteeList);
			}
		});
			
		$('#allot_info_table').datagrid({
			title:'������Ŀ��Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/TaskAssignServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:'AppStdNameProTeamName',title:'������Ŀ����',width:200,sortable:true},
				{field:'AlloteeName',title:'������Ա',width:150}
			]],
			pagination:false,
			rownumbers:true	,
			toolbar:[{
				text:'ע��',
				iconCls:'icon-remove',
				handler:function(){
					var row = $("#allot_info_table").datagrid('getSelected');
					if(row){
						var result = confirm("��ȷ��Ҫע����������");
						if(result == false){
							return false;
						}
						$.ajax({
								type: "post",
								url: "/jlyw/TaskAssignServlet.do?method=1",
								data: {"TaskId":row.TaskId},
								dataType: "json",	//�������������ݵ�Ԥ������
								beforeSend: function(XMLHttpRequest){
									//ShowLoading();
								},
								success: function(data, textStatus){
									if(data.IsOK){
										$('#allot_info_table').datagrid('reload');
									}else{
										$.messager.alert('ע��ʧ�ܣ�',data.msg,'error');
									}
								},
								complete: function(XMLHttpRequest, textStatus){
									//HideLoading();
								},
								error: function(){
									//���������
								}
						});
					}else{
						$.messager.alert('��ʾ��',"����ѡ��һ����¼",'info');
					}
				}
			}]	
		});
	});
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#allot_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#allot_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$("#CommissionSheetId").val();
				$("#ProjectSelect").combobox('clear');
				$("#ProjectSelect").combobox('loadData',[]);
				$("#AlloteeSelect").combobox('clear');
				$("#AlloteeSelect").combobox('loadData',[]);
				
				//$("#Ness").removeAttr("checked");	//ȥ��ѡ
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					if(result.CommissionObj.CommissionStatus != 0 && result.CommissionObj.CommissionStatus !=1){
						$.messager.alert('��ʾ��',"��ί�е��������·��������Ա",'info');
						return false;
					}
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//��ѡ
					}
					
					//�������������Ϣ
					$('#allot_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#allot_info_table').datagrid('reload');
					
					$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
					//���ؼ�����Ŀ��������Ա��Ϣ
					$("#ProjectSelect").combobox('reload','/jlyw/TaskAssignServlet.do?method=2&CommissionId='+$('#CommissionId').val());
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	function doTaskAllotee(){   	//�ύ��������
		$("#TaskAlloteeForm").form('submit', {
			url:'/jlyw/TaskAssignServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//�ж�ѡ��ļ�����Ŀ�Ƿ���Ч
				var projectValue = $('#ProjectSelect').combobox('getValue');
				/*if(projectValue==''){
					$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ļ�����Ŀ",'info');
					return false;
				}*/
				//�ж�ѡ�����Ա�Ƿ���Ч
				var alloteeValue = $('#AlloteeSelect').combobox('getValue');
				if(alloteeValue == ''){
					$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ļ�����Ա",'info');
					return false;
				}
				var alloteeChecked = false;
				var alloteeAllData = $('#AlloteeSelect').combobox('getData');
				if(alloteeAllData != null && alloteeAllData.length > 0){
					for(var i=0; i<alloteeAllData.length; i++)
					{
						if(alloteeValue==alloteeAllData[i].id){
							alloteeChecked = true;
							break;
						}
					}
				}
				if(!alloteeChecked){
					$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ļ�����Ա",'info');
					return false;
				}
				
				
				//�жϼ�����Ŀ�Ƿ����
				var allTask = $('#allot_info_table').datagrid('getRows');
				for(var i=0; i<allTask.length; i++){
					if(projectValue == allTask[i].AppStdNameProTeamId && alloteeValue == allTask[i].AlloteeId){
						$.messager.alert('��ʾ��',"һ��������Ŀ�����ظ������ͬһ���ˣ�����Ҫ���·��䣬����ע���ѷ���ļ�����Ŀ��",'info');
						return false;
					}
				}
				return $("#TaskAlloteeForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//���¼������������Ϣ
					$('#allot_info_table').datagrid('reload');
					
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
			<jsp:param name="TitleName" value="�������" />
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
		
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;+position:relative;"
				title="��������" collapsible="false"  closable="false">
			<table id="allot_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<br />
			<form id="TaskAlloteeForm" method="post">
			<input type="hidden" id="CommissionSheetId" name="CommissionSheetId" value="" />
			<table width="950px" >
				
							<tr >
								<td  align="right">ѡ�������Ŀ��</td>
								 <td   align="left">
										<select id="ProjectSelect" name="ProjectSelect" style="width:152px"></select>
								  </td>
								 <td  align="right">������Ա��</td>
								 <td  align="left">
										<select id="AlloteeSelect" class="easyui-combobox" name="AlloteeSelect" style="width:152px;" valueField="id" textField="name" panelHeight="150" editable="false" ></select>&nbsp;&nbsp;<input name="AlloteeRule" type="radio" value="0" <%= (SystemCfgUtil.getTaskAllotRule() == 0)?"checked=\"checked\"":"" %> />��ҵ������&nbsp;<input name="AlloteeRule" type="radio" value="1" <%= (SystemCfgUtil.getTaskAllotRule() != 0)?"checked=\"checked\"":"" %> />����ֵ��
								  </td>
							</tr>
							<tr >
								<td colspan="2" align="center" style="padding-top:15px;">
									 <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doTaskAllotee()">ȷ������</a>
									 
								</td>
								<td colspan="2" align="center" style="padding-top:15px;">
									 
									 <!--<a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">����</a>-->
								</td>
								
							</tr>
						
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
