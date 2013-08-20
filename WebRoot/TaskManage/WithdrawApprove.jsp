<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>退样审批</title>
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
			title:'退样信息',
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
					{title:'申请信息',colspan:5,align:'center'},
					{title:'办理信息',colspan:6,align:'center'}
				],[
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'WithdrawNumber',title:'退样数量',width:60,align:'center'},
					{field:'Reason',title:'退样原因',width:80,align:'center'},
					{field:'WithdrawDesc',title:'退样样品描述',width:80,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
					
					
					{field:'ExecutorName',title:'办理人',width:80,align:'center'},
					{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
					{field:'ExecutorResult',title:'办理结果',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">驳回</span>';
							}
							if(1 == value){
								return "同意退样";
							}
							return "";
						}	
					},
					{field:'WithdrawDate',title:'退样日期',width:80,align:'center'},
					{field:'Location',title:'样品存放位置',width:80,align:'center'},
					{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onSelect:function(rowIndex, rowData){
				$('#WithdrawId').val(rowData.WithdrawId)
			}
		});
		
		doLoadCommissionSheet();	//加载委托单
	});
	function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#withdraw_info_table').datagrid('loadData',{total:0,rows:[]});
				
				if($('#Code').val()=='' || $('#Pwd').val() == ''){
					$.messager.alert('提示！',"委托单无效！",'info');
					return false;
				}
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载任务分配信息
					$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#withdraw_info_table').datagrid('reload');
					
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	function doSubmitWithdraw(ExecutorResult){   	//提交退样审批表单
		$('#ExecutorResult').val(ExecutorResult);
		$("#withdraw-submit-form").form('submit', {
			url:'/jlyw/WithdrawServlet.do?method=1',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//判断退样申请是否存在
				var WithdrawIdValue = $('#WithdrawId').val();
				if(WithdrawIdValue==''){
					$.messager.alert('提示！',"退样申请不存在！",'info');
					return false;
				}
				return $("#withdraw-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//重新加载任务分配信息
					$('#withdraw_info_table').datagrid('reload');
					
					//清空退样申请表单的“备注”信息
					$('#ExecuteMsg').val('');
					
				}else{
					$.messager.alert('提交失败！',result.msg,'error');
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
			<jsp:param name="TitleName" value="退样审批" />
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
				title="退样申请" collapsible="false"  closable="false">
			<table id="withdraw_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="withdraw-submit-form" method="post">
			<input type="hidden" name="WithdrawId" id="WithdrawId" value="<%= request.getParameter("WithdrawId")==null?"":request.getParameter("WithdrawId") %>" />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="8%" style="padding-top:15px;" align="right" >
					  退样日期：					</td>
					<td width="21%" style="padding-top:15px;" align="left"><input style="width:151px;" class="easyui-datebox" name="WithdrawDate" id="WithdrawDate" type="text" editable="false" /></td>
					<td width="9%" align="right">备注信息：</td>
					<td colspan="2" rowspan="2" align="left"><textarea id="ExecuteMsg" style="width:350px;height:80px"  name="ExecuteMsg" class="easyui-validatebox" required="true" ></textarea></td>
				</tr>
				<tr >
					<td align="right">样品存放位置：</td>
					<td align="left"><input id="Location" name="Location" style="width:150px;" type="text"></td>
					<td></td>
				</tr>
				<tr style="padding-top:15px" >
				  <td height="39" colspan="3"  align="right"  style="padding-right:10px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitWithdraw(1)">同意申请</a></td>
				  <td width="12%"  align="center" ><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitWithdraw(0)">驳回</a></td>
			      <td width="50%"  align="left" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="javascript:history.go(-1)">返回</a></td>
			  </tr>
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
