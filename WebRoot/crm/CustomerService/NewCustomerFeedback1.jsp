<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>������Ϣ����</title>
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
		valueField:'id',
		textField:'name',
		required:true,
		onSelect:function(record){
			
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
					$('#customerName').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}, 500);
//			$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
		}
	});
	});
		function checknameorcode(){
		
		}
		function cancel(){
			$('#frm_add_feedback').form('clear');
		}
		
		function AddFeedback(){
			$('#frm_add_feedback').form('submit',{
				url: '/jlyw/CrmServlet.do?method=0',
				onSubmit:function(){ return $('#frm_add_feedback').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#name').val()));
		}
		
		$(function(){
			$('#cla').combobox({
				multiple:'true'
			});
			
			$('#InsideContactor').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			$('#frm_add_customer').form('validate');
		});
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ͻ�������Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div>
	
	</div>


<form id="frm_add_feedback" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="����������Ϣ��Ŀ">
		<tr height="30px">
			<td align="right">Ͷ����������λ��</td>
			<td align="left" colspan="3">
			<select name="CustomerName" id="customerName" style="width:325px"></select>
			</td>
		</tr>
		<tr height="30px">
			<!-- <td align="right" style="width��20%">Ͷ&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;��</td>
			<td align="left"  style="width��30%"><select id="complainAbout" name="ComplainAbout" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>����</option>
					<option value='1'>����Ա</option>
					<option value='2'>֤��</option>
					<option value='3'>�շ�</option>
					<option value='4'>����</option>
				</select>
				</td> -->
            <td align="right">Ͷ&nbsp;&nbsp;��&nbsp;&nbsp;�ˣ�</td>
			<td align="left" ><input id="complainer" name="Complainer" type="text" class="easyui-validatebox" required="true"/></td>
			
		
		</tr>
		
		<!-- <tr height="30px">
			<td align="right">��&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;��</td>
			<td align="left" >
				<select id="handleLevel" name="HandleLevel" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>��</option>
					<option value='1'>��</option>
					<option value='2'>��</option>
				</select>
			</td>
			<td align="right" style="width��20%">״&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
			<td align="left"  style="width��30%">
			<select id="status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>δ��ʼ</option>
					<option value='1'>������</option>
					<option value='2'>�ѽ���</option>
				</select>
			</td>
		</tr> -->
		<!-- <tr  height="30px">
			<td align="right" style="width��20%">��&nbsp;��&nbsp;��&nbsp;ʼ&nbsp;ʱ&nbsp;�䣺</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="PlanStartTime" id="planStartTime" type="text"  /></td>
			<td align="right" style="width��20%">�ƻ�����ʱ�䣺</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="PlanEndTime" id="planEndTime" type="text"  /></td>
		</tr> -->
		<tr  height="30px">
		<td align="right">�ͻ�Ҫ�����ʱ�䣺</td>
			<td width="168"  align="left"><input class="easyui-datebox" name="CustomerRequiredTime" id="customerRequiredTime" type="text"  /></td>
		</tr>
		
		
		<tr height="30px">
			<td align="right">Ͷ�����ݣ�</td>
			<td align="left"  colspan="3"><textarea id="feedback" name="Feedback" cols="55" rows="4" requird="true"></textarea> </td>
		</tr>
	
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="AddFeedback()">���</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
		</tr>
	</table>
	</form>
	<br />
	<br />


</DIV>
</DIV>
</body>
</html>
