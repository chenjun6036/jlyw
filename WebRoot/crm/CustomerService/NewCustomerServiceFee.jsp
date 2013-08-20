<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�½��������</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
			$('#customerName').combobox({
				valueField:'name',
				textField:'name',
				onSelect:function(rec)
				{
					$('#address').val(rec.address);
					$('#customerId').val(rec.id)
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
						}, 400);

					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
			});
			
		$(function(){
		$('#paidMan').combobox({
		valueField:'name',
		textField:'name',
				onSelect:function(rec)
				{
					$('#jobNum').val(rec.jobNum);
					$('#paidManId').val(rec.id);
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
					$('#paidMan').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});});
			function sub()
			{
				$('#frm_add_fee').form('submit',{
				url: '/jlyw/CrmServlet.do?method=4',
				onSubmit:function(){ return $('#frm_add_fee').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
			}
			function cancel(){
			$('#frm_add_fee').form('clear');
			}
		</script>

</head>

<body>
<form id="frm_export" method="post" action="/jlyw/CustomerServlet.do?method=7">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�½��������" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div border="true" style="width:900px;overflow:hidden;position:relative;">
        <br/>
        <form id="frm_add_fee" method="post">
	<table style="width:900px; height:200px; padding-top:10px; padding-left:20px" class="easyui-panel" title="�������������Ŀ">
		<tr>
		<td align="right">��&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;�ƣ�
		<input type="hidden" id="customerId" name="CustomerId"/>
		</td>
		<td align="left">
		<input id="customerName" name="CustomerName" class="easyui-combobox" required="true" style="width:250px"/>
		</td>
		<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ַ��
		</td>
		<td><input id="address" name="Address" class="eaasyui-validatebox"/>
		</td>
		</tr>
		
		<tr><td align="right">Ʊ&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;�ţ�
		</td>
		<td align="left">
		<input id="billNum" name="BillNum" class="easyui-validatebox" required="true" style="width:150px"/>
		</td>
		<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�</td>
			<td align="left" ><input id="money" name="Money" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr >
			<td align="right" style="width��20%">��&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;Ŀ��</td>
			<td align="left"  style="width��30%">
			<input id="paidSubject" name="PaidSubject"/ class="easyui-validatebox">
			</td>
            <td align="right" style="width��20%">���״̬��</td>
			<td align="left"  style="width��30%">
			<select id="status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value="1">�������</option>
					<option value="2">��ͨ��</option>
					<option vaule="3">δͨ��</option>
				</select>
			</td>
			
		</tr>
		
		<tr>
			            <td align="right">�������֧���ˣ�</td>
			<td align="left" ><input id="paidMan" name="PaidMan" type="text" class="easyui-combobox" required="true"/>
			<input type="hidden" id="paidManId" name="PaidManId"/>
			</td>
			<td align="right">֧���˹��ţ�
		</td>
		<td><input id="jobNum" name="JobNum" class="eaasyui-validatebox"/>
		</td>
			
		</tr>
		<tr>
			<td align="right" style="width��20%">֧&nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;ʽ��</td>
			<td align="left"  style="width��30%">
			<select id="paidVia" name="PaidVia" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
					<option value='0'>�ֽ�</option>
					<option value='1'>���ÿ�</option>
					<option value='2'>����</option>
				</select>
			</td>			<td align="right" style="width��20%">֧�����ڣ�</td>
			<td align="left" style="width��30%"><input class="easyui-datebox" name="PaidDate" id="paidDate" type="text" required="true" /></td>
		</tr>
		
		
		<tr>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
			<td align="left"  colspan="3"><textarea id="remark" name="Remark" cols="55" rows="4"></textarea> </td>
		</tr>
	
		<tr>	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="sub()">���</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
		</tr>
	</table>
	</form>
        </div>
    </DIV>
    </DIV>

</DIV>
</DIV>
</body>
</html>
