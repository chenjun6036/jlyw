<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>注销委托单</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
	function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				
				$("#CommissionSheetId").val('');				
				//$("#Ness").removeAttr("checked");	//去勾选
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					$("#delCommissionId").val(result.CommissionObj.CommissionId);
					
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	} 
	
	function del(){
		if($('#CommissionId').val()==''){
			$.messager.alert('提示','请先查询委托单！','info');
			return false;
		}
		$('#frm_cancel').form('submit',{
			url:'/jlyw/CommissionSheetServlet.do?method=12',
			onSubmit:function(){
				var result = confirm("此操作不可逆，确定注销吗？");
				if(result == false){
					return false;
				}
				return $('#frm_cancel').form('validate');
			},
			success:function(data){
				var result = eval("(" + data + ")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOK)
		   		 	$('#del').dialog('close');	
			}
		});
	}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="注销委托单" />
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
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true"  />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>					
				</tr >
		</table>
		</form>

		</div>
		<br />
    <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	<br/>
    <div id="p2" class="easyui-panel" style="width:1005px;height:40px;padding:20px; "
				title="注销操作" collapsible="false"  closable="false" scroll="no">
                <form id="frm_cancel" method="post">
                <input type="hidden" id="delCommissionId" name="CommissionId" value="" />
                    <table width="900px">
                    <tr>
                        <td align="center">注销原因：</td>
                        <td align="left"><input id="reason" class="easyui-combobox" name="Reason" url="/jlyw/ReasonServlet.do?method=0&type=14" style="width:152px;" valueField="name" textField="name" panelHeight="auto" required="true" /></td>
                        <td align="left"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="del()">注销</a></td>	
                    </tr>
                    </table>
            </form>
	   </div>
</DIV></DIV>
</body>
</html>
