<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>������׼¼��</title>
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
				
		   		$('#file_upload').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					//'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../../uploadify/selectfiles.png',
					//'fileDesc'  : '֧�ָ�ʽ:rar/zip/7z', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					//'fileExt'   : '*.rar;*.zip;*.7z;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});
				
				/*$('#SCopy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '֧�ָ�ʽ:rar/zip/7z', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});*/
				
				$('#Handler').combobox({
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
				
				$('#frm_add_standard').form('validate');
				
			});
			
			function cancel(){
				/*$('#Name').val("");
				$('#NameEn').val("");
				$('#CertificateCode').val("");
				$('#StandardCode').val("");
				$('#ProjectCode').val("");
				$('#Status').val("");
				$('#CreatedBy').val("");
				$('#IssuedBy').val("");
				$('#IssueDate').val("");
				$('#ValidDate').val("");
				$('#Range').val("");
				$('#Uncertain').val("");
				$('#SIssuedBy').val("");
				$('#SIssueDate').val("");
				$('#SValidDate').val("");
				$('#SCertificateCode').val("");
				$('#SRegion').val("");
				$('#SAuthorizationCode').val("");
				$('#SLocaleCode').val("");
				$('#Remark').val("");*/
				$('#frm_add_standard').form('clear');
			}
			
			function savereg()
			{
				$('#frm_add_standard').form('submit',{
					url:'/jlyw/StandardServlet.do?method=1',
					onSubmit:function(){
						return $('#frm_add_standard').form('validate');
					},
		   			success:function(data){
		   				var result = eval("("+data+")");
		   				
		   				if(result.IsOK)
						{
							var file_upload = result.file_upload_filesetname;
							var num = $('#file_upload').uploadifySettings('queueSize');
							if (num > 0) { //��ѡ���ļ�
								doUploadByUploadify(file_upload,'file_upload', false);
							}
							
		   					cancel();
						}
						$.messager.alert('��ʾ',result.msg,'info');
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
			<jsp:param name="TitleName" value="������׼��ϸ��Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div region="center" style="width:700px" style="position:relative;">		
		<div>
		<form id="frm_add_standard" method="post">
			<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="��׼¼��">
				<tr height="30px">
					<td align="right">������׼���ƣ�</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                    <td align="right">ƴ�����룺</td>
					<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">Ӣ�����ƣ�</td>
					<td align="left" colspan="3"><input id="NameEn" name="NameEn" type="text" style="width:450px" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">������׼&nbsp;&nbsp;<br />֤&nbsp;��&nbsp;�ţ�</td>
					<td align="left"><input id="CertificateCode" name="CertificateCode" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">������׼���룺</td>
					<td align="left"><input id="StandardCode" name="StandardCode" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				
				<tr height="30px">
					<td align="right">��Ŀ��ţ�</td>
					<td align="left"><input id="ProjectCode" name="ProjectCode" type="text"/></td>
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
							<option value='0' selected>����</option>
							<option value='1'>ע��</option>
						</select>
					</td>
				</tr>
				
				<tr height="30px">
					<td align="right">���굥λ��</td>
					<td align="left"><input id="CreatedBy" name="CreatedBy" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">��֤��λ��</td>
					<td align="left"><input id="IssuedBy" name="IssuedBy" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=14"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">��֤���ڣ�</td>
					<td align="left"><input id="IssueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
					<td align="right">��&nbsp;Ч&nbsp;�ڣ�</td>
					<td align="left"><input id="ValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">������Χ��</td>
					<td align="left"><input id="Range" name="Range" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">��ȷ����&nbsp;&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;�</td>
					<td align="left"><input id="Uncertain" name="Uncertain" type="text" /></td>
				</tr>
				
				<tr height="30px">
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />��֤���أ�</td>
					<td align="left"><input id="SIssuedBy" name="SIssuedBy" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=15"/></td>
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />֤&nbsp;��&nbsp;�ţ�</td>
					<td align="left"><input id="SCertificateCode" name="SCertificateCode" type="text"  class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />��֤���ڣ�</td>
					<td align="left"><input id="SIssueDate" name="SIssueDate" type="text" class="easyui-datebox" style="width:152px" required="true" /></td>
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />��Ч���ڣ�</td>
					<td align="left"><input id="SValidDate" name="SValidDate" type="text" class="easyui-datebox" style="width:152px" required="true" /></td>
				</tr>
				
				<tr height="30px">
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />��Ч����</td>
					<td align="left"><input id="SRegion" name="SRegion" type="text" class="easyui-validatebox" required="true" value="����"/></td>
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />��Ȩ֤��ţ�</td>
					<td align="left"><input id="SAuthorizationCode" name="SAuthorizationCode" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">���ڱ�ţ�</td>
					<td align="left"><input id="SLocaleCode" name="SLocaleCode" type="text" class="easyui-validatebox" required="true"/></td>
                    <td align="right">��Ч��Ԥ������<br/>(����Ϊ��λ)��</td>
					<td align="left"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-numberbox" required="true"/></td>
				</tr>
                <tr height="30px">
					<td align="right">������׼�����ˣ�</td>
					<td align="left"><input id="Handler" name="Handler" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" required="true"/></td>
                    <td align="right">������׼���</td>
					<td align="left"><input id="Type" name="Type" url="/jlyw/ApplianceSpeciesServlet.do?method=2" class="easyui-combobox" style="width:152px;"  valueField="Name" textField="Name" required="true"/></td>
				</tr>
                <tr>
					<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
					<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
				</tr>
				<!--<tr height="30px">
					<td align="right">������׼&nbsp;&nbsp;<br />ɨ&nbsp;��&nbsp;����</td>
					<td align="left" colspan="3"><input id="Copy" name="Copy" type="file" style="width:350px"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">��&nbsp;��&nbsp;֤&nbsp;&nbsp;<br />ɨ&nbsp;��&nbsp;����</td>
					<td align="left" colspan="3"><input id="SCopy" name="SCopy" type="file" style="width:350px"/></td>
				</tr>-->
				
				<tr height="50px">	
					<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">���</a></td>
					<td></td>
					<td align="left"><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
				</tr>
				</table>
			</form>
            <div id="p4" class="easyui-panel" style="width:700px;height:150px;"
         		title="�����ϴ�" collapsible="false"  closable="false" scroll="no">
            <table width="100%" height="100%" >
               <tr>
                   <td width="43%" height="100" valign="top" align="left" style="overflow:hidden">
                        <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
                   </td>
               </tr>
           </table>
        </div>
			
		</div>
	</div>
    </DIV>
    </DIV>
</body>
</html>
