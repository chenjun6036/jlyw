<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>计量标准录入</title>
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
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					//'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					//'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					//'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});
				
				/*$('#SCopy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
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
							if (num > 0) { //有选择文件
								doUploadByUploadify(file_upload,'file_upload', false);
							}
							
		   					cancel();
						}
						$.messager.alert('提示',result.msg,'info');
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
			<jsp:param name="TitleName" value="计量标准详细信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div region="center" style="width:700px" style="position:relative;">		
		<div>
		<form id="frm_add_standard" method="post">
			<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="标准录入">
				<tr height="30px">
					<td align="right">计量标准名称：</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                    <td align="right">拼音简码：</td>
					<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">英文名称：</td>
					<td align="left" colspan="3"><input id="NameEn" name="NameEn" type="text" style="width:450px" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">计量标准&nbsp;&nbsp;<br />证&nbsp;书&nbsp;号：</td>
					<td align="left"><input id="CertificateCode" name="CertificateCode" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">计量标准代码：</td>
					<td align="left"><input id="StandardCode" name="StandardCode" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				
				<tr height="30px">
					<td align="right">项目编号：</td>
					<td align="left"><input id="ProjectCode" name="ProjectCode" type="text"/></td>
					<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
							<option value='0' selected>正常</option>
							<option value='1'>注销</option>
						</select>
					</td>
				</tr>
				
				<tr height="30px">
					<td align="right">建标单位：</td>
					<td align="left"><input id="CreatedBy" name="CreatedBy" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">发证单位：</td>
					<td align="left"><input id="IssuedBy" name="IssuedBy" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=14"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">发证日期：</td>
					<td align="left"><input id="IssueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
					<td align="right">有&nbsp;效&nbsp;期：</td>
					<td align="left"><input id="ValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">测量范围：</td>
					<td align="left"><input id="Range" name="Range" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">不确定度&nbsp;&nbsp;<br />误&nbsp;&nbsp;&nbsp;&nbsp;差：</td>
					<td align="left"><input id="Uncertain" name="Uncertain" type="text" /></td>
				</tr>
				
				<tr height="30px">
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />发证机关：</td>
					<td align="left"><input id="SIssuedBy" name="SIssuedBy" style="width:152px;" required="true" class="easyui-combobox" panelHeight="auto" valueField="name" textField="name" uel="/jlyw/BaseTypeServlet.do?method=4&type=15"/></td>
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />证&nbsp;书&nbsp;号：</td>
					<td align="left"><input id="SCertificateCode" name="SCertificateCode" type="text"  class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />发证日期：</td>
					<td align="left"><input id="SIssueDate" name="SIssueDate" type="text" class="easyui-datebox" style="width:152px" required="true" /></td>
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />有效日期：</td>
					<td align="left"><input id="SValidDate" name="SValidDate" type="text" class="easyui-datebox" style="width:152px" required="true" /></td>
				</tr>
				
				<tr height="30px">
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />有效区域：</td>
					<td align="left"><input id="SRegion" name="SRegion" type="text" class="easyui-validatebox" required="true" value="常州"/></td>
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />授权证书号：</td>
					<td align="left"><input id="SAuthorizationCode" name="SAuthorizationCode" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">所内编号：</td>
					<td align="left"><input id="SLocaleCode" name="SLocaleCode" type="text" class="easyui-validatebox" required="true"/></td>
                    <td align="right">有效期预警天数<br/>(以天为单位)：</td>
					<td align="left"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-numberbox" required="true"/></td>
				</tr>
                <tr height="30px">
					<td align="right">计量标准负责人：</td>
					<td align="left"><input id="Handler" name="Handler" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" required="true"/></td>
                    <td align="right">计量标准类别：</td>
					<td align="left"><input id="Type" name="Type" url="/jlyw/ApplianceSpeciesServlet.do?method=2" class="easyui-combobox" style="width:152px;"  valueField="Name" textField="Name" required="true"/></td>
				</tr>
                <tr>
					<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
				</tr>
				<!--<tr height="30px">
					<td align="right">计量标准&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
					<td align="left" colspan="3"><input id="Copy" name="Copy" type="file" style="width:350px"/></td>
				</tr>
				
				<tr height="30px">
					<td align="right">社&nbsp;会&nbsp;证&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
					<td align="left" colspan="3"><input id="SCopy" name="SCopy" type="file" style="width:350px"/></td>
				</tr>-->
				
				<tr height="50px">	
					<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">添加</a></td>
					<td></td>
					<td align="left"><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">重置</a></td>
				</tr>
				</table>
			</form>
            <div id="p4" class="easyui-panel" style="width:700px;height:150px;"
         		title="附件上传" collapsible="false"  closable="false" scroll="no">
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
