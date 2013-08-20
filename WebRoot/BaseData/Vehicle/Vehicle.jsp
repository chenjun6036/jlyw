<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>������Ϣ����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />

		<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/jquery.easyui.min.js"></script>
                <script type="text/javascript"	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		<script>
			$(function(){
				$('#Limit').numberbox();
				$('#FuelFee').numberbox();
				
				$('#Driver').combobox({
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
				
				$('#frm_add_vehicle').form('validate');
				
			})
			
			function save()
			{
				$('#frm_add_vehicle').form('submit',{
					url:'/jlyw/VehicleServlet.do?method=1',
					onSubmit:function(){ return $('#frm_add_vehicle').form('validate');},
		   			success:function(data){
		   				var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 	}
				});
			}
			
			function cancel()
			{
				/*$('#Licence').val("");
				$('#Limit').val("");
				$('#Model').val("");
				$('#Brand').val("");
				$('#FuelFee').val("");
				$('#Status').combobox('setValue',"0");*/
				$('#frm_add_vehicle').form('clear');
			}
			
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="����������Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div region="center" style="width:700px" style="+position:relative;">
	
		<div>
		<form id="frm_add_vehicle" method="post">
			<table style="width:700px; height:200px; padding-top:20px; padding-left:0px" class="easyui-panel" title="����������Ϣ¼��">
				<tr>
					<td align="right">��&nbsp;��&nbsp;�ţ�</td>
					<td align="left"><input id="Licence" name="Licence" class="easyui-validatebox" required="true"/></td>
					<td align="right">����������</td>
					<td align="left"><input id="Limit" name="Limit" class="easyui-numberbox" required="true"/></td>
				</tr>
				<tr>
                	<td align="right">����Ʒ�ƣ�</td>
					<td align="left"><input id="Brand" name="Brand" class="easyui-validatebox" required="true"/></td>
					<td align="right">�����ͺţ�</td>
					<td align="left"><input id="Model" name="Model" class="easyui-validatebox" required="true"/></td>

				</tr>
				<tr>
					<td align="right">�ٹ����ͺģ�</td>
					<td align="left"><input id="FuelFee" name="FuelFee" class="easyui-numberbox" required="true"/></td>
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto" editable="false">
							<option value="0">����</option>
							<option value="1">ά��</option>
							<option value="2">���ϡ�ע��</option>
						</select>
					</td>
                    </tr>
                    <tr>
                    <td align="right">˾&nbsp;&nbsp;&nbsp;����</td>
					<td align="left" colspan="3"><input id="Driver" name="Driver" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
				</tr>
				<tr height="50px">	
					<td colspan="2" align="center"><a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">���</a></td>
					<td></td>
					<td align="left"><a href="#" class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a></td>
				</tr>
			</table>
			</form>
		</div>
		
	</div>
    </DIV>
    </DIV>
</body>
</html>
