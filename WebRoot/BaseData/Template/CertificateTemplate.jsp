<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>证书模板文件管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
	<script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
			$('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'103'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '支持格式:doc/xml.', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                'fileExt'   : '*.doc;*.xml',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('提示',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table').datagrid('reload');
					CloseWaitingDlg();
				}
			 });
		})
	</script>
</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="证书模板文件管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
	<div id="p4" class="easyui-panel" style="width:800px;height:450px;"
			title="证书模板文件（Word及XML文件）上传" collapsible="false"  closable="false" scroll="no">
		<table width="100%" height="100%" >
			<tr>
				<td width="57%" rowspan="2">
					<table id="uploaded_file_table" class="easyui-datagrid" pagination="true" url="/jlyw/FileDownloadServlet.do?method=1&FileType=103&FilesetName=CertificateTemplate"></table>
				</td>
				<td width="43%" height="375" valign="top" align="left" style="overflow:hidden">
				  <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
				</td>
			</tr>
			<tr>
				<td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByUploadify('CertificateTemplate','file_upload')">上传文件</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">取消上传</a> </td>
			</tr>
		</table>
   </div>
  
</DIV></DIV>
</body>

</html>
