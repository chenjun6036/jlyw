<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��Ʒ������Ϣ¼��</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script>
		$(function(){
		   
           $("#dateTimeFrom").datebox({
				formatter: function(date){ return date.getFullYear()+'-'+(date.getMonth()<9?('0'+(date.getMonth()+1)):(date.getMonth()+1))+'-'+(date.getDate()<10?('0'+date.getDate()):date.getDate()); }
			});
		});
		function ok(){
			 $('#allot').form('submit',{
				//url: 'userAdd.action',
				onSubmit:function(){ return $('#allot').form('validate');},
		   		success:function(){
			   		 close1();
		   		 }
			});
		}
	</script>
</head>

<body>
	<br />
    <div  align="center" style="width:900px;height:40px;" >
	     <h2>��Ʒ������Ϣ¼��</h2>
   </div>
   <br />
   <div >
     <div id="p" class="easyui-panel" style="width:900px;height:160px;padding:10px;"
				title="ί�е���Ϣ" collapsible="false"  closable="false">
			<table width="850px" id="table1">
				<tr>
					<td width="10%" align="right">ί�е���ţ�</td>
					<td width="22%" align="left">
						<input id="id" class="easyui-combobox" name="type" url="commissionid.json" style="width:150px;" valueField="id" textField="text" panelHeight="auto" >
					</td>
					<td width="10%" align="right">ί��&nbsp;&nbsp;ʱ�䣺</td>
				    <td width="21%" colspan="3" align="left"><input name="time" type="text" readonly></td>
				</tr>
				<tr>
					<td width="10%" align="right">ί��&nbsp;&nbsp;��λ��</td>
				    <td width="22%" align="left"><input name="Name" type="text" style="width:200px;" readonly></td>
					<td width="10%" align="right">��λ&nbsp;&nbsp;��ַ��</td>
				    <td width="22%" align="left"><input name="address" type="text" readonly></td>
					<td width="8%" align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
				    <td width="22%" align="left"><input name="contactperson" type="text" readonly></td>
				</tr>
				<tr>
					<td width="10%"  align="right">����&nbsp;&nbsp;Ҫ��</td>
					<td width="22%" align="left"><input name="demand" type="text" readonly></td>
					<td width="10%" align="right">ָ������Ա��</td>
					<td width="21%" colspan="3" align="left"><input name="check" type="text" readonly></td>
				</tr>
		</table>
		</div>
		<div id="p1" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="�ͼ�����" collapsible="false"  closable="false">
			<table width="850px" id="table2">
				<tr>
					<td width="10%" align="right">����&nbsp;&nbsp;���ƣ�</td>
					<td width="22%" align="left">
						<input name="name2" type="text" readonly>
					</td>
					<td width="10%" align="right">�ͺ�&nbsp;&nbsp;���</td>
				    <td width="22%" colspan="3" align="left"><input name="standards" type="text" readonly></td>
				</tr>
				<tr>
					<td style="padding-top:5px" width="10%" align="right" valign="middle">�Ƿ�&nbsp;&nbsp;�Ӽ���</td>
				    <td  width="22%" align="left" valign="middle"><INPUT type = "radio" name = selection1 value = "A"></INPUT> �� <INPUT type = "radio" name = selection1 value = "B"></INPUT>��
</td>
					<td style="padding-top:5px"  width="10%" align="right" valign="middle">�Ƿ�&nbsp;&nbsp;����</td>
				    <td  width="22%" align="left" valign="middle"><INPUT type = "radio" name = selection value = "A"></INPUT> �� <INPUT type = "radio" name = selection value = "B"></INPUT>��</td>
					<td width="8%" align="right">������ʽ��</td>
				    <td width="22%" align="left"><input name="Reportform" type="text" readonly> </td>
				</tr>
				
		</table>
		</div>
      <div >
        <div id="p" class="easyui-panel" style="width:900px;height:150px;padding:10px;"
				title="��Ʒ������Ϣ" collapsible="false"  closable="false">
			<table width="850px" id="table3">
				<tr >
					<td width="10%" align="right">�� �� �</td>
					<td width="22%" align="left">
						<input id="item" type="text" name="item" class="easyui-combobox" style="width:150px;" >
					</td>
					<td width="10%" align="right">������</td>
				    <td width="21%" align="left"> 
					     <select id="level" name="level" style="width:50px;"  panelHeight="auto" >                           
						     <option>С</option>
							 <option>��</option>
							 <option>��</option>
						</select>
					</td>
				</tr>
				<tr >
					<td width="10%"  align="right">������ƣ�</td>
					<td width="22%"  align="left"><input name="assemblyname" type="text" class="easyui-validatebox" required="true" ></td>
					<td width="10%"  align="right">������ã�</td>
					<td width="22%" align="left"><input name="assemblyfee" type="text" class="easyui-numberbox" required="true" >&nbsp;Ԫ</td>
				</tr>
				<tr >
					<td width="10%" align="right">������ã�</td>
				    <td width="22%" align="left"><input name="fee" type="text" class="easyui-numberbox" required="true">&nbsp;Ԫ</td>
					<td width="10%" align="right">����ʱ�䣺</td>
				    <td width="22%" align="left"><input id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" ></td>
				</tr>
	
		  </table>
		</div>
		<br/>
		<br/>
		
		<div id="p2" class="easyui-panel" style="width:900px;height:90px;"
				title="����" collapsible="false"  closable="false">
			
			<table width="850px" >
				
				<tr>
					<td width="30%"  align="right" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onClick="ok()">ȷ�����</a>
	                     
					</td>
					<td width="30%"  align="right" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">����</a>
					</td>
					<td  align="right" style="padding-top:15px;">
						 
	                     
					</td>
					
					
				</tr>
		  </table>
		  
		</div>
	</div>
</body>
</html>
