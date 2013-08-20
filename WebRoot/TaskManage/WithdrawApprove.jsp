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
		$('#withdraw_info_table').datagrid({
			title:'������Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/WithdrawServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'������Ϣ',colspan:5,align:'center'},
					{title:'������Ϣ',colspan:6,align:'center'}
				],[
					{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
					{field:'WithdrawNumber',title:'��������',width:60,align:'center'},
					{field:'Reason',title:'����ԭ��',width:80,align:'center'},
					{field:'WithdrawDesc',title:'������Ʒ����',width:80,align:'center'},
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
					{field:'WithdrawDate',title:'��������',width:80,align:'center'},
					{field:'Location',title:'��Ʒ���λ��',width:80,align:'center'},
					{field:'ExecuteMsg',title:'��ע��Ϣ',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onSelect:function(rowIndex, rowData){
				$('#WithdrawId').val(rowData.WithdrawId)
			}
		});
		
		doLoadCommissionSheet();	//����ί�е�
	});
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#withdraw_info_table').datagrid('loadData',{total:0,rows:[]});
				
				if($('#Code').val()=='' || $('#Pwd').val() == ''){
					$.messager.alert('��ʾ��',"ί�е���Ч��",'info');
					return false;
				}
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//��ѡ
					}
					
					//�������������Ϣ
					$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#withdraw_info_table').datagrid('reload');
					
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	function doSubmitWithdraw(ExecutorResult){   	//�ύ����������
		$('#ExecutorResult').val(ExecutorResult);
		$("#withdraw-submit-form").form('submit', {
			url:'/jlyw/WithdrawServlet.do?method=1',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//�ж����������Ƿ����
				var WithdrawIdValue = $('#WithdrawId').val();
				if(WithdrawIdValue==''){
					$.messager.alert('��ʾ��',"�������벻���ڣ�",'info');
					return false;
				}
				return $("#withdraw-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//���¼������������Ϣ
					$('#withdraw_info_table').datagrid('reload');
					
					//�������������ġ���ע����Ϣ
					$('#ExecuteMsg').val('');
					
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
	
		<form id="SearchForm" method="post" >
		<input id="Code" type="hidden" name="Code" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" style="width:150px;" />
		<input id="Pwd" type="hidden" name="Pwd" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" style="width:150px;" />
		</form>
		 <%@ include file="/Common/CommissionSheetInfo.jsp"%>
		 <br/>
	
		<div id="p2" class="easyui-panel" style="width:1005px;height:470px;padding:10px;"
				title="��������" collapsible="false"  closable="false">
			<table id="withdraw_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="withdraw-submit-form" method="post">
			<input type="hidden" name="WithdrawId" id="WithdrawId" value="<%= request.getParameter("WithdrawId")==null?"":request.getParameter("WithdrawId") %>" />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="8%" style="padding-top:15px;" align="right" >
					  �������ڣ�					</td>
					<td width="21%" style="padding-top:15px;" align="left"><input style="width:151px;" class="easyui-datebox" name="WithdrawDate" id="WithdrawDate" type="text" editable="false" /></td>
					<td width="9%" align="right">��ע��Ϣ��</td>
					<td colspan="2" rowspan="2" align="left"><textarea id="ExecuteMsg" style="width:350px;height:80px"  name="ExecuteMsg" class="easyui-validatebox" required="true" ></textarea></td>
				</tr>
				<tr >
					<td align="right">��Ʒ���λ�ã�</td>
					<td align="left"><input id="Location" name="Location" style="width:150px;" type="text"></td>
					<td></td>
				</tr>
				<tr style="padding-top:15px" >
				  <td height="39" colspan="3"  align="right"  style="padding-right:10px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitWithdraw(1)">ͬ������</a></td>
				  <td width="12%"  align="center" ><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitWithdraw(0)">����</a></td>
			      <td width="50%"  align="left" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="javascript:history.go(-1)">����</a></td>
			  </tr>
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
