<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��Ա������Ϣ����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
        <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
        <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
        <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
        <script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
        <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
        <script type="text/javascript" src="../../JScript/upload.js"></script>
        <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
		$(function(){
			$('#Signature').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//			'folder'    : '../../UploadFile',
				'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:jpg/jpeg/png', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
				'fileExt'   : '*.jpg;*.jpeg;*.png;',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
					var retData = eval("("+response+")");
					if(retData.IsOK == false)
						$.messager.alert('��ʾ',retData.msg,'error');
				},
				onAllComplete: function(event,data){
				}
			});
				
			$('#Photograph').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//			'folder'    : '../../UploadFile',
				'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:jpg/jpeg/png', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
				'fileExt'   : '*.jpg;*.jpeg;*.png;',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
					var retData = eval("("+response+")");
					if(retData.IsOK == false)
						$.messager.alert('��ʾ',retData.msg,'error');
				},
				onAllComplete: function(event,data){
				}
			});
			
			$('#DepartmentId').combobox({
				onSelect:function(record){
					$('#ProjectTeamId').combobox('setValue',"");
					$('#ProjectTeamId').combobox('reload','/jlyw/ProjectTeamServlet.do?method=8&DepartmentId='+record.Id);
				}
			});
			$('#frm_add_employee').form('validate');
		});
				
		function save(){
			$('#frm_add_employee').form('submit',{
				url:'/jlyw/UserServlet.do?method=1',
				onSubmit:function(){return $('#frm_add_employee').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
					{
						var signature = result.signature_filesetname;
						var photo = result.photo_filesetname;
						var num = $('#Signature').uploadifySettings('queueSize');
						if (num > 0) { //û��ѡ���ļ�
							doUploadByUploadify(signature,'Signature', false);
						}
						var num1 = $('#Photograph').uploadifySettings('queueSize');
						if (num1 > 0) { //û��ѡ���ļ�
							doUploadByUploadify(photo,'Photograph', false);
						}
		   				cancel();
					}
				}
			});
		}
		
		function cancel(){
			$('#frm_add_employee').form('clear');
			$('#Birthday').datebox('setValue',"");
			$('#WorkSince').datebox('setValue',"");
			$('#WorkHereSince').datebox('setValue',"");
			$('#EducationDate').datebox('setValue',"");
			$('#DegreeDate').datebox('setValue',"");
			$('#PartyDate').datebox('setValue',"");
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
			<jsp:param name="TitleName" value="��Ա������Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

	<div region="center" align="left" style="+position:relative;">
		<div>
		<form id="frm_add_employee" method="post">
			<table style="width:800px; height:400px; padding-top:10px; padding-left:10px;" class="easyui-panel" title="��Ա������Ϣ¼��">
				<tr> 
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td> 
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                    <td align="right">ƴ�����룺</td>
					<td align="left"><input name="Brief" id="Brief" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��</td>
					<td align="left">
						<select id="Gender" name="Gender" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" editable="false">
							<option value="0">��</option>
							<option value="1">Ů</option>
						</select>
					</td>
				</tr> 
				<tr>
					<td align="right">��&nbsp;¼&nbsp;����</td>
					<td align="left"><input id="userName" name="userName" class="easyui-validatebox" required="true" type="text" /></td>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
					<td align="left"><input id="JobNum" name="JobNum" class="easyui-validatebox" required="true" type="text" /></td>
					<td align="right">���ڵ�λ��</td>
					<td align="left"><select name="WorkLocation" id="WorkLocation" style="width:150px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"/></td>
				</tr>
				<tr> 
					<td align="right">��&nbsp;��&nbsp;�أ�</td>
					<td align="left"><input id="Birthplace" name="Birthplace" type="text" class="easyui-validatebox" required="true" /></td>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br />��&nbsp;��&nbsp;�գ�</td>
					<td align="left"><input id="Birthday" name="Birthday" type="text" class="easyui-datebox" style="width:150px" required="true"  /></td>
					<td align="right">���֤�ţ�</td>
					<td align="left"><input id="IDNum" name="IDNum" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">������ò��</td>
					<td align="left"><input id="PoliticsStatus" name="PoliticsStatus" style="width:150px" class="easyui-combobox" panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=16"/></td>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�壺</td>
                    <td align="left"><input id="Nation" name="Nation" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=27"/></td>
                    <td align="right">��Ա���ʣ�</td>
                    <td align="left">
                        <select id="Type" name="Type" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=17">
                        </select>
                    </td>
                 </tr>
                 <tr>
                    <td align="right">�μӹ���&nbsp;&nbsp;<br/>��&nbsp;&nbsp;&nbsp;&nbsp;�ڣ�</td>
                    <td align="left"><input id="WorkSince" name="WorkSince" type="text" class="easyui-datebox" style="width:150px"  required="true"  /></td>
                    <td align="right">��������&nbsp;&nbsp;<br/>ʱ&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                    <td align="left"><input id="WorkHereSince" name="WorkHereSince" type="text" class="easyui-datebox" style="width:150px" required="true"  /></td>
                    <td align="right">ְ&nbsp;&nbsp;&nbsp;&nbsp;�ƣ�</td>
                    <td align="left"><input id="JobTitle" name="JobTitle" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=18"/></td>
                 </tr>
                 <tr>
                    <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;����</td>
                    <td align="left"><input id="Education" name="Education" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=19"/></td>
                    <td align="right">ȡ��ѧ��&nbsp;&nbsp;<br/>ʱ&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                    <td align="left"><input id="EducationDate" name="EducationDate" type="text" class="easyui-datebox" style="width:150px" class="easyui-validatebox"/></td>
                    <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br/>��ҵԺУ��</td>
                    <td align="left"><input id="EducationFrom" name="EducationFrom" type="text" class="easyui-validatebox" required="true"/></td>
                  </tr>
                  <tr>
                    <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;λ��</td>
                    <td align="left"><input id="Degree" name="Degree" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=20"/></td>
                    <td align="right">ȡ��ѧλ&nbsp;&nbsp;<br/>ʱ&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                    <td align="left"><input id="DegreeDate" name="DegreeDate" type="text" class="easyui-datebox" style="width:150px"/></td>
                    <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;λ&nbsp;&nbsp;<br/>��ҵԺУ��</td>
                    <td align="left"><input id="DegreeFrom" name="DegreeFrom" type="text" class="easyui-validatebox" required="true"/></td>
                  </tr>
                  <tr>
                    <td align="right">��ѧרҵ��</td>
                    <td align="left"><input id="Specialty" name="Specialty" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=21"/></td>
                    <td align="right">����ְ��</td>
                    <td align="left"><input id="AdministrationPost" name="AdministrationPost" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=22"/></td>
                    <td align="right">����ְ��</td>
                    <td align="left"><input id="PartyPost" name="PartyPost" class="easyui-combobox" style="width:150px;" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=23"/></td>
                </tr>
				<tr>
					<td align="right">�뵳ʱ�䣺</td>
					<td align="left"><input id="PartyDate" name="PartyDate" type="text" class="easyui-datebox" style="width:150px"/></td>
					<td align="right">��ͥסַ��</td>
					<td align="left"><input id="HomeAdd" name="HomeAdd" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">�����ص㣺</td>
					<td align="left"><input id="WorkAdd" name="WorkAdd" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">�칫�绰��</td>
					<td align="left"><input id="Tel" name="Tel" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">�ֻ�����1��</td>
					<td align="left"><input id="Cellphone1" name="Cellphone1" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">�ֻ�����2��</td>
					<td align="left"><input id="Cellphone2" name="Cellphone2" type="text" /></td>
				</tr>
				<tr>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
					<td align="left"><input id="Email" name="Email" type="text" class="easyui-validatebox" required="true"/></td>
                    <td align="right">�������ţ�</td>
					<td align="left"><select id="DepartmentId" name="DepartmentId" class="easyui-combobox" style="width:150px" valueField="Id" textField="Name" panelHeight="150px" mode="remote" url="/jlyw/DepartmentServlet.do?method=6" required="true" editable="false"/></td>
					<td align="right">������Ŀ�飺</td>
					<td align="left"><select id="ProjectTeamId" name="ProjectTeamId" class="easyui-combobox" style="width:150px" valueField="Id" textField="Name" panelHeight="150px" mode="remote" url="" required="true" editable="false"/></td>
				</tr>
				<tr>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
					<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="2"></textarea></td>
                    <td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" editable="false">
							<option value="0">����</option>
							<option value="1">ע��</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">ǩ��ͼƬ��</td>
					<td align="left" colspan="5"><input id="Signature" name="Signature" type="file" style="width:420px" /></td>
				</tr>
				<tr>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;Ƭ��</td>
					<td align="left" colspan="5"><input id="Photograph" name="Photograph" type="file" style="width:420px" /></td>
				</tr>
				<tr height="50px">
					<td align="center" colspan="3">
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">���</a>
					</td>
					<td align="center" colspan="3">
						<a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
    </DIV>
    </DIV>
</body>
</html>
