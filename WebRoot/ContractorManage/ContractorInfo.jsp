<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ת������Ϣ¼��</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script>
		 $(function(){
		   		$('#Copy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../uploadify/selectfiles.png',
					'fileDesc'  : '֧�ָ�ʽ:rar/zip/7z', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});
			});
	

		function save(){
			 $('#frm_add_contractor').form('submit',{
				url: '/jlyw/SubContractorServlet.do?method=1',
				onSubmit:function(){ return $('#frm_add_contractor').form('validate');},
		   		success:function(data){
			   		 var result = eval("(" + data + ")");
			   		 alert(result.msg);
			   		 if(result.IsOK)
					 {
						 	var Copy = result.Copy_filesetname;
							var num = $('#Copy').uploadifySettings('queueSize');
							if (num > 0) { //��ѡ���ļ�
								doUploadByUploadify(Copy,'Copy', false);
							}
			   		 	cancel();
					 }
		   		 }
			});
		}
		
		function cancel(){
			/*$('#name').val("");
			$('#code').val("");
			$('#zipcode').val("");
			$('#address').val("");
			$('#region').combobox('setValue',0);
			$('#contactor').val("");
			$('#tel').val("");
			$('#ContactorTel').val("");
			$('#remark').val("");
			$('#status').combobox('setValue',0);*/
			$('#frm_add_contractor').form('clear');
		}
	</script>
</head>

<body>
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ת������Ϣ¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
   <div style="+position:relative;">
   <form id="frm_add_contractor" method="post">
    <!-- <div id="p" class="easyui-panel" style="width:900px;height:500px;padding:10px;"
				title="ת������Ϣ" collapsible="false"  closable="false">-->
			<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="ת������Ϣ">
				<tr height="50px">
					<td width="10%" align="right">��λ���ƣ�</td>
					<td width="22%" align="left">
						<input id="name" type="text" name="name" class="easyui-validatebox" required="true"/>
					</td>
					<td width="10%" align="right">��λ���룺</td>
				    <td width="21%" align="left"><input name="code" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="50px">
					<td width="10%" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
				    <td width="22%" align="left"><select name="region" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="150px" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" /></td>
					<td width="10%" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
				    <td width="22%" align="left"><input name="zipcode" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="50px">
					<td width="10%"  align="right">��λ��ַ��</td>
					<td width="22%"  align="left"><input name="address" type="text" style="width:280px;" class="easyui-validatebox" required="true"  /></td>
					<td width="10%"  align="right">��λ�绰��</td>
					<td width="22%" align="left"><input name="tel" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="50px">
					<td width="10%" align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
				    <td width="22%" align="left"><input name="contactor" type="text" class="easyui-validatebox" required="true" /></td>
					<td width="10%" align="right">��ϵ�绰��</td>
				    <td width="22%" align="left"><input name="contactorTel" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="50px">
					<td width="10%" align="right">��λ״̬��</td>
				    <td width="22%" colspan="3" align="left">
					    <select id="status" name="status" style="width:150px;" class="easyui-combobox" panelHeight="auto">                           
						     <option value="0">����</option>
							 <option value="1">ע��</option>
							 
						</select>
					</td>
				</tr>
				<tr height="140px">
				    <td width="10%" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
				    <td width="22%" colspan="3" align="left"><textarea id="remark" style="width:280px;height:100px"  name="remark" ></textarea></td>
				</tr>
                <tr height="50px">
					<td width="10%" align="right">ת��������<br />ɨ&nbsp;��&nbsp;����</td>
					<td width="22%" align="left" colspan="3"><input id="Copy" type="file" name="Copy" style="width:350px"/></td>
				</tr>
				<tr height="50px">
					<td width="20%"  align="center" colspan="2">
						 <a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onClick="save()">���</a>
	                     
					</td>
					<td width="20%"  align="center" colspan="2">
	                     <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">����</a>
					</td>
				</tr>
		</table>
		
		<!--</div>-->
		</form>
	</div>
	

</div>
</div>
</body>
</html>
