<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>新建内部联系人</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
$(function(){
	$("#customerName").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				//$('#careContactor2').combobox('reload','/jlyw/CrmServlet.do?method=5&CustomerId='+record.id);
				//var data=$('#careContactor2').combobox('getData');
				$('#customerId').val(record.id);
				/* $('#careContactor2').combobox(
				{
				valueField:'name',
				textField:'name',
				url:'/jlyw/CrmServlet.do?method=5&CustomerId='+record.id,
				onLoadSuccess:function()
				{
					var data=$('#careContactor2').combobox('getData');
					$('#careContactor2').combobox('setValue',data[0].name);
				}
				}); */
				
		},
		onChange:function(newValue, oldValue){
			var allData = $(this).combobox('getData');
			if(allData != null && allData.length > 0){
				for(var i=0; i<allData.length; i++)
				{
					if(newValue==allData[i].name){
						return false;
					}
				}
			}
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $('#customerName').combobox('getText');
					$('#customerName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}, 500);
//			$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
		}
	});
	});
	
$(function(){
	$("#insideContactor_0").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#jobNum_0").val(record.jobNum);
				$("#insideContactorId_0").val(record.id);
		},
		onChange:function(newValue, oldValue){
			var allData = $(this).combobox('getData');
			if(allData != null && allData.length > 0){
				for(var i=0; i<allData.length; i++)
				{
					if(newValue==allData[i].name){
						return false;
					}
				}
			}
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $("#insideContactor_0").combobox('getText');
					$("#insideContactor_0").combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	} );
	/////////////////////////////////**********************************

	/////////////////////////////////////////////////////////////////////////
		function checknameorcode(){
		
		}
		function cancel(){
			$('#frm_add_conInfo').form('clear');
		}
		
		function Add(){
			$('#frm_add_conInfo').form('submit',{
				url: '/jlyw/CrmServlet.do?method=7',
				onSubmit:function(){ return $('#frm_add_conInfo').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOk)
		   					cancel();
		   		 }
			});
		}
		$(function(){$("#role").combobox('setValue',1);});
		
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="内部联系信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	</div>

</br>
</br>
<form id="frm_add_conInfo" method="post">
	<table style="width:700px; height:200px; padding-top:10px; padding-left:20px" class="easyui-panel" title="录入联系信息">
		<tr >
			<td align="center">联系单位：</td>
			<td align="left" >
			<select name="CustomerName" id="customerName" style="width:250px"></select>
			<input id="customerId" name="CustomerId" type="hidden"/>
			</td>
			<td align="center">单位地址：
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>

		<tr >
		<td align="center">内部联系人：
		</td>
		<td>
		<input id="insideContactor_0" name="InsideContactor_0" class="easyui-combobox" type="text"/>
		<input type="hidden" id="insideContactorId_0" name="InsideContactorId_0" />
		</td>
		
		<td align="center">联系人工号：
		</td>
		<td>
		<input id="jobNum_0" name="JobNum_0" />
		</td>
		</tr>
	
		<tr>
			<td align="right">角色：</td>
			<td align="left"><select id="role" name="Role" class="easyui-combobox" panelHeight="auto" editable="false" required="true">
			<option value="1" >A</option>
			<option value="2">B</option>
			</select></td>
			
		</tr>
		
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="Add()">添加</a></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">重置</a></td>
		<td></td>
		</tr>
	</table>
	</form>
	<br />
	<br />
</DIV>
</DIV>
</body>
</html>
