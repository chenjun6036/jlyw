<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>延期申请</title>
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
			title:'延期信息',
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
					{title:'申请信息',colspan:4,align:'center'},
					{title:'办理信息',colspan:4,align:'center'}
				],[
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'DelayDays',title:'延期天数',width:60,align:'center'},
					{field:'Reason',title:'延期原因',width:80,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
					
					{field:'ExecutorName',title:'办理人',width:80,align:'center'},
					{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
					{field:'ExecutorResult',title:'办理结果',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">驳回</span>';
							}
							if(1 == value){
								return "同意延期";
							}
							return "";
						}	
					},
					{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	
		});
	});
	function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#overdue_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$("#CommissionSheetId").val('');				
				//$("#Ness").removeAttr("checked");	//去勾选
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					if(result.CommissionObj.CommissionStatus != 0 && result.CommissionObj.CommissionStatus !=1 && result.CommissionObj.CommissionStatus !=2){
						$.messager.alert('提示！',"该委托单不能申请延期！",'info');
						return false;
					}
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载任务分配信息
					$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#overdue_info_table').datagrid('reload');
					
					$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	function doSubmitOverdue(){   	//提交延期申请表单
		$("#overdue-submit-form").form('submit', {
			url:'/jlyw/OverdueServlet.do?method=2',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//判断选择的检验项目是否有效
				var projectValue = $('#CommissionSheetId').val();
				if(projectValue==''){
					$.messager.alert('提示！',"请选择需要申请延期的委托单！",'info');
					return false;
				}
				return $("#overdue-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//重新加载任务分配信息
					$('#overdue_info_table').datagrid('reload');
					
					//清空延期申请表单的“延期天数”信息
					$('#DelayDays').val('');
					
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
			<jsp:param name="TitleName" value="延期申请" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right">密  码：</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
    <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	<br/>
	
		<div id="p2" class="easyui-panel" style="width:1005px;height:400px;padding:10px;"
				title="延期申请" collapsible="false"  closable="false">
			<table id="overdue_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="overdue-submit-form" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="8%" style="padding-top:15px;" align="right" >延期天数：					</td>
					
					<td width="21%" style="padding-top:15px;" align="left"><input id="DelayDays"   name="DelayDays" class="easyui-numberbox" style="width:152px;" required="true" />天</td>
					<td width="9%" style="padding-top:15px;" align="right" >
					  延期原因：					</td>
					<td width="22%" style="padding-top:15px;" align="left">
				  <input id="Reason" class="easyui-combobox" name="Reason" url="/jlyw/ReasonServlet.do?method=0&type=12" style="width:172px;" valueField="name" textField="name" panelHeight="auto" />					</td>
					<td width="9%" style="padding-top:15px;" align="right" ></td>
				  <td width="31%" style="padding-top:10px;" align="left"></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitOverdue()">提交申请</a></td>
				  <td  align="left" colspan="3" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
