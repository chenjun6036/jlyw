<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>转包方信息录入</title>
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
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../uploadify/selectfiles.png',
					'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
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
							if (num > 0) { //有选择文件
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
			<jsp:param name="TitleName" value="转包方信息录入" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
   <div style="+position:relative;">
   <form id="frm_add_contractor" method="post">
    <!-- <div id="p" class="easyui-panel" style="width:900px;height:500px;padding:10px;"
				title="转包方信息" collapsible="false"  closable="false">-->
			<table style="width:700px; height:400px; padding-top:10px; padding-left:20px" class="easyui-panel" title="转包方信息">
				<tr height="50px">
					<td width="10%" align="right">单位名称：</td>
					<td width="22%" align="left">
						<input id="name" type="text" name="name" class="easyui-validatebox" required="true"/>
					</td>
					<td width="10%" align="right">单位代码：</td>
				    <td width="21%" align="left"><input name="code" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="50px">
					<td width="10%" align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
				    <td width="22%" align="left"><select name="region" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="150px" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" /></td>
					<td width="10%" align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
				    <td width="22%" align="left"><input name="zipcode" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="50px">
					<td width="10%"  align="right">单位地址：</td>
					<td width="22%"  align="left"><input name="address" type="text" style="width:280px;" class="easyui-validatebox" required="true"  /></td>
					<td width="10%"  align="right">单位电话：</td>
					<td width="22%" align="left"><input name="tel" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="50px">
					<td width="10%" align="right">联&nbsp;系&nbsp;人：</td>
				    <td width="22%" align="left"><input name="contactor" type="text" class="easyui-validatebox" required="true" /></td>
					<td width="10%" align="right">联系电话：</td>
				    <td width="22%" align="left"><input name="contactorTel" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="50px">
					<td width="10%" align="right">单位状态：</td>
				    <td width="22%" colspan="3" align="left">
					    <select id="status" name="status" style="width:150px;" class="easyui-combobox" panelHeight="auto">                           
						     <option value="0">正常</option>
							 <option value="1">注销</option>
							 
						</select>
					</td>
				</tr>
				<tr height="140px">
				    <td width="10%" align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				    <td width="22%" colspan="3" align="left"><textarea id="remark" style="width:280px;height:100px"  name="remark" ></textarea></td>
				</tr>
                <tr height="50px">
					<td width="10%" align="right">转包方资质<br />扫&nbsp;描&nbsp;件：</td>
					<td width="22%" align="left" colspan="3"><input id="Copy" type="file" name="Copy" style="width:350px"/></td>
				</tr>
				<tr height="50px">
					<td width="20%"  align="center" colspan="2">
						 <a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onClick="save()">添加</a>
	                     
					</td>
					<td width="20%"  align="center" colspan="2">
	                     <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>
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
