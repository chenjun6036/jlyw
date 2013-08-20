<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>工位挡回</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
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
/*					if(result.CommissionObj.Status==0||result.CommissionObj.Status==1||result.CommissionObj.Status==2||result.CommissionObj.Status==3)
					{
						$.messager.alert('提示','该委托单所处状态不可执行工位挡回操作！','warning');
						return;
					}*/
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}					
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	
	function TurnBack(){
		if($('#CommissionId').val()==''){
			$.messager.alert('提示','请先查询委托单！','info');
			return false;
		}
		var result = confirm("确认挡回该委托单吗？");
		if(result == false){
			return false;
		}
		if($('#type').val()=="0"){
			$.messager.alert('提示','请选择挡回类型！','info');
			return false;
		}
		$.ajax({
			url:$('#type').val()=="1"?'/jlyw/CommissionSheetServlet.do?method=13':'/jlyw/CommissionSheetServlet.do?method=14',
			type:'post',
			data:'CommissionId='+$('#CommissionId').val(),
			dataType:'html',
			success:function(data){
				var result = eval("("+data+")");
				$.messager.alert('提示',result.msg,'info');
			}
		});
	}
	
	function OpenCancelWindow(){
		$('#del').window('open');
		$('#ff1').show();
		$('#delCommissionId').val($("#CommissionId").val());
	}
	
	function del(){
		if($('#CommissionId').val()==''){
			$.messager.alert('提示','请先查询委托单！','info');
			return false;
		}
		$('#ff1').form('submit',{
			url:'/jlyw/CommissionSheetServlet.do?method=12',
			onSubmit:function(){
				var result = confirm("此操作不可逆，确定注销吗？");
				if(result == false){
					return false;
				}
				return $('#ff1').form('validate');
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
			<jsp:param name="TitleName" value="工位挡回" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="990px" id="table1">
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
    <div id="p2" class="easyui-panel" style="width:1005px;height:40px;padding:20px; "
				title="挡回操作" collapsible="false"  closable="false" scroll="no">
                    <table width="900px">
                    <tr>
                        <td align="center">挡回类型：</td>
                        <td align="left">
                        	<select id="type" name="type" style="width:152px">
                            	<option value="0" selected="selected">请选择....</option>
                                <option value="1" >已结账--->已完工</option>
                                <option value="2" >已注销--->已收件</option>
                            </select>
                        </td>
                        <td align="left"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="TurnBack()">工位挡回</a></td>
                        <td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-remove" href="javascript:void(0)" onClick="OpenCancelWindow()">注销</a></td>	
                    </tr>
                    </table>
	   </div>
        <div id="del" class="easyui-window" title="注销" style="width:280px;height:150px;" 
        iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <form id="ff1" method="post">
            <table style="width:250px;height:100px;" >
            <input id="delCommissionId" name="CommissionId" type="hidden"/>
            <tr>
                <td align="center">注销原因：</td>
                <td align="left"><input id="reason" class="easyui-combobox" name="Reason" url="/jlyw/ReasonServlet.do?method=0&type=14" style="width:152px;" valueField="name" textField="name" panelHeight="auto" required="true" /></td>
            </tr>
            <tr height="30px">	
                <td width="125" align="right"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="del()">注销</a></td>
                <td width="125" align="center"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="javascript:$('#del').dialog('close');">取消</a></td>
            </tr>
            </table>
            </form>
        </div>
</DIV></DIV>
</body>
</html>
