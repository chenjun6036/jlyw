<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ܼ�������Ϣ����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"	src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
		$(function(){
			$('#Fee').numberbox();
			$('#SRFee').numberbox();
			$('#MRFee').numberbox();
			$('#LRFee').numberbox();
			$('#PromiseDuration').numberbox();
			$('#TestCycle').numberbox();
			$('#frm_appliance').form('validate');
		});
		
		$(function(){
			$('#StandardNameId').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
					}
			});
		});
		
		function cancel(){
			/*$('#StandardNameId').combobox('setValue',"");
			$('#Name').val("");
			$('#NameEn').val("");
			$('#Brief').val("");
			$('#Code').val("");
			$('#Fee').val("");
			$('#SRFee').val("");
			$('#MRFee').val("");
			$('#LRFee').val("");
			$('#PromiseDuration').val("");
			$('#TestCycle').val("");
			$('#Remark').val("");*/
			$('#frm_appliance').form('clear');
		}
		
		function save(){
			$('#frm_appliance').form('submit',{
				url:'/jlyw/TargetApplianceServlet.do?method=1',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
						cancel();
				}
			});
		}
		
		function getBrief(){
			$('#Brief').val(makePy($('#Name').val()));
		}
		
	</script>
</head>

<body> 

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ܼ�������Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	
	<!--<div region="west" style="width:200px; height:1000px; border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="�ܼ����߷���">
		<ul id="tt"></ul>
 	</div>-->

	
	
  	<div  region="center" style="border:0px;">
    	<form id="frm_appliance" method="post">
		<table id="table1" class="easyui-panel" title="�ܼ�������Ϣ¼��" style="width:800px; height:400px; padding-top:10px">
			<tr>
				<td align="right">�������ƣ�</td>
				<td align="left" colspan="3"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            </tr>
            <tr>
            	<td align="right">ƴ�����룺</td>
				<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				<td align="right">Ӣ�����ƣ�</td>
				<td align="left"><input id="NameEn" name="NameEn" type="text" class="easyui-validatebox" required="true"/></td>
			</tr>
			
			<tr>
				<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br/>��׼���ƣ�</td>
				<td align="left"><select id="StandardNameId" name="StandardNameId" class="easyui-combobox" valueField="id" textField="name" style="width:152px" required="true" panelHeight="auto"/></td>
				<td align="right">���߱��룺</td>
				<td align="left"><input id="Code" name="Code" type="text" class="easyui-validatebox" required="true"/></td>
			</tr>
			<tr>
				<td align="right">��׼���ã�</td>
				<td align="left"><input id="Fee" name="Fee" class="easyui-numberbox" />Ԫ</td>
				<td align="right">С�޷��ã�</td>
				<td align="left"><input id="SRFee" name="SRFee" class="easyui-numberbox" />Ԫ</td>
			</tr>
			<tr>
				<td align="right">���޷��ã�</td>
				<td align="left"><input id="MRFee" name="MRFee" class="easyui-numberbox" />Ԫ</td>
				<td align="right">���޷��ã�</td>
				<td align="left"><input id="LRFee" name="LRFee" class="easyui-numberbox" />Ԫ</td>
			</tr>
			<tr>
				<td align="right">��ŵ����ڣ�</td>
				<td align="left"><input id="PromiseDuration" name="PromiseDuration" class="easyui-numberbox" />��</td>
				<td align="right">�춨���ڣ�</td>
				<td align="left"><input id="TestCycle" name="TestCycle" class="easyui-numberbox" />��</td>
			</tr>
			<tr>
				<td align="right">��֤��</td>
				<td align="left" colspan="3">
                    <input id="cer1" name="cer1" type="checkbox">����������Ȩ</input>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="cer2" name="cer2" type="checkbox">ʵ�����Ͽ�</input>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="cer3" name="cer3" type="checkbox">������֤</input>
				</td>
			</tr>
            <tr height="30px">
				<td align="right">״̬��</td>
				<td align="left" colspan="3">
					<select id="Status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" editable="false">
						<option value="0">����</option>
						<option value="1">ע��</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
				<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="60" rows="3"></textarea></td>
			</tr>
			<tr height="50px">
					<td align="center" colspan="2">
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">���</a>
					</td>
					<td align="center" colspan="2">
						<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a>
					</td>
				</tr>
		</table>
        </form>
	</div>
	
	<!--<div region="south" style="border-bottom:0px; border-left:0px; border-right:0px">12342
	</div>
-->
</DIV>
</DIV>
</body>
</html>
