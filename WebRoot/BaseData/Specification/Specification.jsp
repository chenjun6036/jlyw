<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>规程信息管理</title>
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
				
				/*$('#methodConfirm').uploadify({
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
						if (num > 0) { //没有选择文件
							doUploadByUploadify(file_upload,'file_upload', false);
						}
						cancel();
					}
					$.messager.alert('提示',result.msg,'info');
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
			<jsp:param name="TitleName" value="技术规范信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div region="center" style="width:700px" style="position:relative;">
<div>
<form id="frm_add_specification" method="post">
	<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="技术规范录入">
		<tr height="30px">
			<td align="right">编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td align="left"><input id="specode" name="SpecificationCode" type="text"  class="easyui-validatebox" required="true"/></td>
			<td align="right">中文名称：</td>
			<td align="left"><input id="nameCn" name="NameCn" type="text"  class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
		</tr>
		<tr height="30px">
        	<td align="right">拼音简码：</td>
			<td align="left"><input id="brief" name="Brief" type="text"  class="easyui-validatebox" required="true"/></td>
			<td align="right">英文名称：</td>
			<td align="left"><input id="nameEn" name="NameEn" type="text"/></td>
		</tr>
		<tr height="30px">	
			<td align="right">规范类别：</td>
			<td align="left" >
				<select id="type" name="Type" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=13">
				</select>
			</td>
			<td align="right">是否受控：</td>
			<td align="left" ><select id="incharge" name="InCharge" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
					<option value='0'>是</option>
					<option value='1'>否</option>
				</select>
			</td>
		</tr>
		<tr  height="30px">
			<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
			<td align="left">
				<select id="status" name="Status" class="easyui-combobox" style="width:152px" panelHeight="auto" required="true">
					<option value='0'>正常</option>
					<option value='1'>注销</option>
				</select>
			</td>
			<td align="right">所内编号：</td>
			<td align="left" ><input id="localecode" name="LocaleCode" type="text" /></td>
		</tr>
		<tr height="30px">
			<td align="right">发布日期：</td>
			<td align="left"><input id="issueDate" name="IssueDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
			<td align="right">实施日期：</td>
			<td align="left"><input id="effectiveDate" name="EffectiveDate" type="text" class="easyui-datebox" style="width:152px" required="true"/></td>
		</tr>
		<tr height="30px">
			<td align="right">废止日期：</td>
			<td align="left"><input id="repealDate" name="RepealDate" type="text" class="easyui-datebox" style="width:152px"/></td>
			<td align="right">替代何规程：</td>
			<td align="left" ><input id="oldSpecification" name="OldSpecification" type="text"/></td>
		</tr>
        <tr>
            <td align="right">有&nbsp;效&nbsp;期&nbsp;&nbsp;<br />预警天数：</td>
			<td align="left" colspan="3"><input id="WarnSlot" name="WarnSlot" type="text" class="easyui-validatebox" required="true" value="0"/></td>
        </tr>
        <tr>
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
		</tr>
		<!--<tr height="30px">
			<td align="right">技术规范&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
			<td align="left" colspan="3"><input id="copy" name="Copy" type="file" style="width:400px"/></td>
		</tr>
		<tr height="30px">
			<td align="right">方法确认表<br />扫&nbsp;描&nbsp;件：</td>
			<td align="left" colspan="3"><input id="methodConfirm" name="MethodConfirm" type="file" style="width:400px"/></td>
		</tr>-->
		<tr height="50px">	
			<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">添加</a></td>
			<td></td>
			<td align="left"><a class="easyui-linkbutton" icon="icon-reload" name="reset" href="javascript:void(0)" onclick="cancel()">重置</a></td>
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
