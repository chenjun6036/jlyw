<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�����Ϣ����</title>
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
				$('#WarnSlot').numberbox();
				
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
				
				/*$('#methodConfirm').uploadify({
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
				
				$('#frm_add_specification').form('validate');
				
			});
	
		function cancel(){
			/*$('#specode').val("");
			$('#nameCn').val("");
			$('#nameEn').val("");
			$('#type').combobox('setValue','0');
			$('#incharge').combobox('setValue','0');
			$('#status').combobox('setValue','0');
			$('#localecode').val("");
			$('#issueDate').datebox('setValue',"");
			$('#effectiveDate').datebox('setValue',"");
			$('#repealDate').datebox('setValue',"");
			$('#oldSpecification').val("");
			$('#copypath').val("");
			$('#methodConfirmpath').val("");
			$('#remark').val("");*/
			$('#frm_add_specification').form('clear');
		}
		
		function save(){
			$('#frm_add_specification').form('submit',{
				url:'/jlyw/SpecificationServlet.do?method=1',
				onSubmit:function(){return $('#frm_add_specification').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
		   			if(result.IsOK)
					{
						var file_upload = result.file_upload_filesetname;
						var num = $('#file_upload').uploadifySettings('queueSize');
						if (num > 0) { //û��ѡ���ļ�
							doUploadByUploadify(file_upload,'file_upload', false);
						}
						cancel();
					}
					$.messager.alert('��ʾ',result.msg,'info');
		   		}
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#nameCn').val()));
		}
		
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�����淶��Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div region="center" style="width:700px" style="position:relative;">
<div>
<form id="frm_add_specification" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="�����淶¼��">
		<tr height="30px">
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
			<td align="left"><input id="specode" name="SpecificationCode" type="text"  class="easyui-validatebox" required="true"/></td>
			<td align="right">�������ƣ�</td>
			<td align="left"><input id="nameCn" name="NameCn" type="text"  class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
		</tr>
		<tr height="30px">
        	<td align="right">ƴ�����룺</td>
			<td align="left"><input id="brief" name="Brief" type="text"  class="easyui-validatebox" required="true"/></td>
			<td align="right">Ӣ�����ƣ�</td>
			<td align="left"><input id="nameEn" name="NameEn" type="text"/></td>
		</tr>
		<tr height="30px">	
			<td align="right">�淶���</td>
			<td align="left" >
				<select id="type" name="Type" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=13">
				</select>
			</td>
			<td align="right">�Ƿ��ܿأ�</td>
			<td align="left" ><select id="incharge" name="InCharge" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
					<option value='0'>��</option>
					<option value='1'>��</option>
				</select>
			</td>
		</tr>
		<tr  height="30px">
			<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
			<td align="left">
				<select id="status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
					<option value='0'>����</option>
					<option value='1'>ע��</option>
				</select>
			</td>
			<td align="right">���ڱ�ţ�</td>
			<td align="left" ><input id="localecode" name="LocaleCode" type="text" /></td>
		</tr>
		<tr height="30px">
			<td align="right">�������ڣ�</td>
			<td align="left"><input id="issueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
			<td align="right">ʵʩ���ڣ�</td>
			<td align="left"><input id="effectiveDate" name="EffectiveDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
		</tr>
		<tr height="30px">
			<td align="right">��ֹ���ڣ�</td>
			<td align="left"><input id="repealDate" name="RepealDate" type="text" class="easyui-datebox" style="width:152px"/></td>
			<td align="right">����ι�̣�</td>
			<td align="left" ><input id="oldSpecification" name="OldSpecification" type="text"/></td>
		</tr>
        <tr>
            <td align="right">��&nbsp;Ч&nbsp;��&nbsp;&nbsp;<br />Ԥ��������</td>
			<td align="left" colspan="3"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-validatebox" required="true" value="0"/></td>
        </tr>
        <tr>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
			<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
		</tr>
		<!--<tr height="30px">
			<td align="right">�����淶&nbsp;&nbsp;<br />ɨ&nbsp;��&nbsp;����</td>
			<td align="left" colspan="3"><input id="copy" name="Copy" type="file" style="width:400px"/></td>
		</tr>
		<tr height="30px">
			<td align="right">����ȷ�ϱ�<br />ɨ&nbsp;��&nbsp;����</td>
			<td align="left" colspan="3"><input id="methodConfirm" name="MethodConfirm" type="file" style="width:400px"/></td>
		</tr>-->
		<tr height="50px">	
			<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">���</a></td>
			<td></td>
			<td align="left"><a class="easyui-linkbutton" icon="icon-reload" name="reset" href="javascript:void(0)" onclick="cancel()">����</a></td>
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
