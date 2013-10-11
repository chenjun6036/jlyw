<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>人员基本信息管理</title>
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
				'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//			'folder'    : '../../UploadFile',
				'queueSizeLimit': 1,//一次只能传一个文件
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '支持格式:jpg/jpeg/png', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
				'fileExt'   : '*.jpg;*.jpeg;*.png;',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
					var retData = eval("("+response+")");
					if(retData.IsOK == false)
						$.messager.alert('提示',retData.msg,'error');
				},
				onAllComplete: function(event,data){
				}
			});
				
			$('#Photograph').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//			'folder'    : '../../UploadFile',
				'queueSizeLimit': 1,//一次只能传一个文件
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '支持格式:jpg/jpeg/png', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
				'fileExt'   : '*.jpg;*.jpeg;*.png;',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
					var retData = eval("("+response+")");
					if(retData.IsOK == false)
						$.messager.alert('提示',retData.msg,'error');
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
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
					{
						var signature = result.signature_filesetname;
						var photo = result.photo_filesetname;
						var num = $('#Signature').uploadifySettings('queueSize');
						if (num > 0) { //没有选择文件
							doUploadByUploadify(signature,'Signature', false);
						}
						var num1 = $('#Photograph').uploadifySettings('queueSize');
						if (num1 > 0) { //没有选择文件
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
			<jsp:param name="TitleName" value="人员基本信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

	<div region="center" align="left" style="+position:relative;">
		<div>
		<form id="frm_add_employee" method="post">
			<table style="width:800px; height:400px; padding-top:10px; padding-left:10px;" class="easyui-panel" title="人员基本信息录入">
				<tr> 
					<td align="right">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td> 
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                    <td align="right">拼音简码：</td>
					<td align="left"><input name="Brief" id="Brief" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">性&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td align="left">
						<select id="Gender" name="Gender" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" editable="false">
							<option value="0">男</option>
							<option value="1">女</option>
						</select>
					</td>
				</tr> 
				<tr>
					<td align="right">登&nbsp;录&nbsp;名：</td>
					<td align="left"><input id="userName" name="userName" class="easyui-validatebox" required="true" type="text" /></td>
					<td align="right">工&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
					<td align="left"><input id="JobNum" name="JobNum" class="easyui-validatebox" required="true" type="text" /></td>
					<td align="right">所在单位：</td>
					<td align="left"><select name="WorkLocation" id="WorkLocation" style="width:150px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"/></td>
				</tr>
				<tr> 
					<td align="right">出&nbsp;生&nbsp;地：</td>
					<td align="left"><input id="Birthplace" name="Birthplace" type="text" class="easyui-validatebox" required="true" /></td>
					<td align="right">出&nbsp;&nbsp;&nbsp;&nbsp;生&nbsp;&nbsp;<br />年&nbsp;月&nbsp;日：</td>
					<td align="left"><input id="Birthday" name="Birthday" type="text" class="easyui-datebox" style="width:150px" required="true"  /></td>
					<td align="right">身份证号：</td>
					<td align="left"><input id="IDNum" name="IDNum" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">政治面貌：</td>
					<td align="left"><input id="PoliticsStatus" name="PoliticsStatus" style="width:150px" class="easyui-combobox" panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=16"/></td>
					<td align="right">民&nbsp;&nbsp;&nbsp;&nbsp;族：</td>
                    <td align="left"><input id="Nation" name="Nation" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=27"/></td>
                    <td align="right">人员性质：</td>
                    <td align="left">
                        <select id="Type" name="Type" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=17">
                        </select>
                    </td>
                 </tr>
                 <tr>
                    <td align="right">参加工作&nbsp;&nbsp;<br/>日&nbsp;&nbsp;&nbsp;&nbsp;期：</td>
                    <td align="left"><input id="WorkSince" name="WorkSince" type="text" class="easyui-datebox" style="width:150px"  required="true"  /></td>
                    <td align="right">进所工作&nbsp;&nbsp;<br/>时&nbsp;&nbsp;&nbsp;&nbsp;间：</td>
                    <td align="left"><input id="WorkHereSince" name="WorkHereSince" type="text" class="easyui-datebox" style="width:150px" required="true"  /></td>
                    <td align="right">职&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
                    <td align="left"><input id="JobTitle" name="JobTitle" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=18"/></td>
                 </tr>
                 <tr>
                    <td align="right">学&nbsp;&nbsp;&nbsp;&nbsp;历：</td>
                    <td align="left"><input id="Education" name="Education" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=19"/></td>
                    <td align="right">取得学历&nbsp;&nbsp;<br/>时&nbsp;&nbsp;&nbsp;&nbsp;间：</td>
                    <td align="left"><input id="EducationDate" name="EducationDate" type="text" class="easyui-datebox" style="width:150px" class="easyui-validatebox"/></td>
                    <td align="right">学&nbsp;&nbsp;&nbsp;&nbsp;历&nbsp;&nbsp;<br/>毕业院校：</td>
                    <td align="left"><input id="EducationFrom" name="EducationFrom" type="text" class="easyui-validatebox" required="true"/></td>
                  </tr>
                  <tr>
                    <td align="right">学&nbsp;&nbsp;&nbsp;&nbsp;位：</td>
                    <td align="left"><input id="Degree" name="Degree" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=20"/></td>
                    <td align="right">取得学位&nbsp;&nbsp;<br/>时&nbsp;&nbsp;&nbsp;&nbsp;间：</td>
                    <td align="left"><input id="DegreeDate" name="DegreeDate" type="text" class="easyui-datebox" style="width:150px"/></td>
                    <td align="right">学&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;&nbsp;<br/>毕业院校：</td>
                    <td align="left"><input id="DegreeFrom" name="DegreeFrom" type="text" class="easyui-validatebox" required="true"/></td>
                  </tr>
                  <tr>
                    <td align="right">所学专业：</td>
                    <td align="left"><input id="Specialty" name="Specialty" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=21"/></td>
                    <td align="right">行政职务：</td>
                    <td align="left"><input id="AdministrationPost" name="AdministrationPost" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=22"/></td>
                    <td align="right">党内职务：</td>
                    <td align="left"><input id="PartyPost" name="PartyPost" class="easyui-combobox" style="width:150px;" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=23"/></td>
                </tr>
				<tr>
					<td align="right">入党时间：</td>
					<td align="left"><input id="PartyDate" name="PartyDate" type="text" class="easyui-datebox" style="width:150px"/></td>
					<td align="right">家庭住址：</td>
					<td align="left"><input id="HomeAdd" name="HomeAdd" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">工作地点：</td>
					<td align="left"><input id="WorkAdd" name="WorkAdd" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr>
					<td align="right">办公电话：</td>
					<td align="left"><input id="Tel" name="Tel" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">手机号码1：</td>
					<td align="left"><input id="Cellphone1" name="Cellphone1" type="text" class="easyui-validatebox" required="true"/></td>
					<td align="right">手机号码2：</td>
					<td align="left"><input id="Cellphone2" name="Cellphone2" type="text" /></td>
				</tr>
				<tr>
					<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
					<td align="left"><input id="Email" name="Email" type="text" class="easyui-validatebox" required="true"/></td>
                    <td align="right">所属部门：</td>
					<td align="left"><select id="DepartmentId" name="DepartmentId" class="easyui-combobox" style="width:150px" valueField="Id" textField="Name" panelHeight="150px" mode="remote" url="/jlyw/DepartmentServlet.do?method=6" required="true" editable="false"/></td>
					<td align="right">所属项目组：</td>
					<td align="left"><select id="ProjectTeamId" name="ProjectTeamId" class="easyui-combobox" style="width:150px" valueField="Id" textField="Name" panelHeight="150px" mode="remote" url="" required="true" editable="false"/></td>
				</tr>
				<tr>
					<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="2"></textarea></td>
                    <td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
					<td align="left">
						<select id="Status" name="Status" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" editable="false">
							<option value="0">正常</option>
							<option value="1">注销</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">签名图片：</td>
					<td align="left" colspan="5"><input id="Signature" name="Signature" type="file" style="width:420px" /></td>
				</tr>
				<tr>
					<td align="right">照&nbsp;&nbsp;&nbsp;&nbsp;片：</td>
					<td align="left" colspan="5"><input id="Photograph" name="Photograph" type="file" style="width:420px" /></td>
				</tr>
				<tr height="50px">
					<td align="center" colspan="3">
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">添加</a>
					</td>
					<td align="center" colspan="3">
						<a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">重置</a>
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
