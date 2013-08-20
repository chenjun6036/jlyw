<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>反馈信息管理</title>
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
				$('#careContactor2').combobox(
				{
				valueField:'name',
				textField:'name',
				url:'/jlyw/CrmServlet.do?method=5&CustomerId='+record.id,
				onLoadSuccess:function()
				{
					var data=$('#careContactor2').combobox('getData');
					$('#careContactor2').combobox('setValue',data[0].name);
				}
				
				}
);
				
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
	$("#careDutyMan").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#jobNum1").val(record.jobNum);
				$('#careDutyManId').val(record.id);
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
					var newValue = $('#careDutyMan').combobox('getText');
					$('#careDutyMan').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	});
	
$(function(){
	$("#representative").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#jobNum2").val(record.jobNum);
				$('#representativeId').val(record.id);
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
					var newValue = $('#representative').combobox('getText');
					$('#representative').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	});
	/////////////////////////////////**********************************
	$(function(){
	$("#careContactor2").combobox({
		valueField:'name',
		textField:'name',
		//required:true,
		onSelect:function(record){
				$("#careContactor").val($("#careContactor").val()+record.name+';');
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
					var newValue = $('#careContactor2').combobox('getText');
					$('#careContactor2').combobox('reload','/jlyw/CrmServlet.do?method=5&QueryName='+newValue);
			}, 500);
		}
	});
	});
	/////////////////////////////////////////////////////////////////////////
		function checknameorcode(){
		
		}
		function cancel(){
			$('#frm_add_care').form('clear');
		}
		
		function Add(){
			$('#frm_add_care').form('submit',{
				url: '/jlyw/CrmServlet.do?method=6',
				onSubmit:function(){ return $('#frm_add_care').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
		}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="关怀信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	</div>


<form id="frm_add_care" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="录入信息">
		<tr height=>
			<td align="right">关&nbsp;怀&nbsp;单&nbsp;位:</td>
			<td align="left" >
			<select name="CustomerName" id="customerName" style="width:200px" class="easyui-validatebox"></select>
			<input id="customerId" name="CustomerId" type="hidden"/>
			</td>
			<td align="right">单&nbsp;位&nbsp;地&nbsp;址:
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<!-- <tr >
			<td align="right" style="width：20%">关怀优先级：</td>
			<td align="left"  style="width：30%">
			<select id="priority" name="Priority" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>高</option>
					<option value='1'>中</option>
					<option value='2'>低</option>
					
			</select>
            <td align="right">关&nbsp;怀&nbsp;方&nbsp;式:</td>
			<td align="left" >
			<select id="way" name="Way" type="text" class="easyui-combobox" required="true" panelHeight="auto">
					<option value='0'>电话</option>
					<option value='1'>短信</option>
					<option value='2'>礼品</option>
					<option value='3'>宴请</option>
					<option value='4'>其它</option>
			</select>
			</td>
		</tr> -->
		
		<tr >
			
			<td align="right">关&nbsp;怀&nbsp;方&nbsp;式:</td>
			<td align="left" >
			<select id="way" name="Way" type="text" class="easyui-combobox" required="true" panelHeight="auto">
					<option value='0'>电话</option>
					<option value='1'>短信</option>
					<option value='2'>礼品</option>
					<option value='3'>宴请</option>
					<option value='4'>其它</option>
			</select>
			</td>
			<td align="right">关怀联系人：<br />&nbsp;
			</td>
			<td>
			<input id="careContactor" name="CareContactor" class="easyui-validatebox" required="true" style="width:145px;"/><br />
			<select id="careContactor2" name="CareContactor2" class="easyui-combobox" style="width:75px;" required="false">
			<option></option>
			</select>
			</td>
		</tr>
		<tr >
		<td align="right">关&nbsp;怀&nbsp;时&nbsp;间：</td>
			<td  align="left">
			<input class="easyui-datebox" name="Time" id="time" type="text"  required="true"/></td>
			<td align="right">服务费用：</td>
			<td>
				<input class="easyui-numberbox" name="Fee" id="time"/>
			</td>
		</tr>
		<tr>
		<td align="right">关怀负责人：
		</td>
		<td>
		<input id="careDutyMan" name="CareDutyMan" class="easyui-combobox"/>
		<input type="hidden" id="careDutyManId" name="CareDutyManId" />
		</td>
		<td align="right">负责人工号：
		</td>
		<td>
		<input id="jobNum1" name="JobNum1" class="easyui-validatebox"/>
		</td>
		</tr>
		<tr>
		<td align="right">客&nbsp;户&nbsp;代&nbsp;表:</td>
		<td>
		<input id="representative" name="Representative" class="easyui-combobox"/>
		<input type="hidden" id="representativeId" Name="RepresentativeId"/>
		</td>
		<td align="right">代&nbsp;表&nbsp;工&nbsp;号:
		</td>
		<td>
		<input id="jobNum2" name="JobNum2" class="easyui-validatebox"/>
		</td>
		</tr>
		<tr height="30px">
			<td align="right">备注：</td>
			<td align="left"  colspan="3"><textarea id="remark" name="Remark" cols="55" rows="4" requird="true"></textarea> </td>
		</tr>
	
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="Add()">添加</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">重置</a></td>
		</tr>
	</table>
	</form>
	<br />
	<br />


</DIV>
</DIV>
</body>
</html>
