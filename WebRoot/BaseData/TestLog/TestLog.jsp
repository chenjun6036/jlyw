<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>新建量值溯源</title>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
<link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="../../uploadify/swfobject.js"></script>
<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
<script type="text/javascript" src="../../JScript/upload.js"></script>
<script>
	$(function(){
			$('#CertificateCopy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					'fileDesc'  : '支持格式:*', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					'fileExt'   : '*.*;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
			});
			$('#Confirmer').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(record){
					$('#ConfirmerId').val(record.id);
				},
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
			$('#StandardName').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				onSelect:function(record){
					$('#StandardNameId').val(record.id);
					$('#Model').val(record.model);
					$('#Range').val(record.range);
					$('#Uncertain').val(record.uncertain);
				},
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
					$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=10&QueryName='+newValue);
					}
			});
	});
	function save(){
		$('#frm_add_testlog').form('submit',{
			url:'/jlyw/TestLogServlet.do?method=1',
			onSubmit:function(){return $('#frm_add_testlog').form('validate');},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK)
				{
					var Copy = result.CertificateCopy_filesetname;
					var num = $('#CertificateCopy').uploadifySettings('queueSize');
					
					if (num > 0) { //有选择文件
						doUploadByUploadify(Copy,'CertificateCopy',false);
					}		
					cancel();
				}
				$.messager.alert('提示！',result.msg,'info');
				
			}
		});
	}
				
	function cancel(){
		$('#StandardName').combobox('setValue',"");
		$('#CertificateId').val("");
		$('#TestDate').datebox('setValue',"");
		$('#ValidDate').datebox('setValue',"");
		$('#Confirmer').combobox('setValue',"");
		$('#ConfirmDate').datebox('setValue',"");
		
		$('#Tester').val("");
		$('#ConfirmMeasure').val("");
		$('#DurationCheck').val("");
		$('#Maintenance').val("");
		$('#Remark').val("");
	}
</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="量值溯源记录信息录入" />
		</jsp:include>
	</DIV>
	<DIV  class="JlywCenterLayoutDIV">

		 <div style="padding-left:30px;+position:relative;"> 
		 <form id="frm_add_testlog" method="post">
			<table style="width:700px; padding-top:10px; padding-bottom:15px" class="easyui-panel" title="量值溯源记录信息录入">
				<tr height="50px">
					<td align="right" style="width：20%">标准器具：</td>
					<td align="left"  style="width：30%">
						<select id="StandardName" name="StandardName" class="easyui-combobox" valueField="name" textField="name" style="width:152px" required="true" panelHeight="auto"/><input id="StandardNameId" name="StandardNameId" type="hidden" />
					</td>
					<td align="right" style="width：20%">型号规格：</td>
					<td align="left"  style="width：30%">
						<input id="Model" name="Model" readonly="readonly"/>
					</td>
					
				</tr>
				<tr height="50px">
					<td align="right" style="width：20%">测量范围：</td>
					<td align="left"  style="width：30%">
						<input id="Range" name="Range" readonly="readonly"/>
					</td>
					<td align="right" style="width：20%">不确定度：</td>
					<td align="left"  style="width：30%" >
						<input id="Uncertain" name="Uncertain" readonly="readonly"/>
					</td>
					
				</tr>
				
				<tr height="50px">
					
					<td align="right">检定日期：</td>
					<td align="left" >
						<input id="TestDate" name="TestDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
					<td align="right">有效日期：</td>
					<td align="left" >
						<input id="ValidDate" name="ValidDate" type="text" class="easyui-datebox" style="width:152px" required="true"/>
					</td>
				</tr>
				
				<tr height="50px">
					<td align="right">溯源结果&nbsp;&nbsp;<br />确&nbsp;认&nbsp;人：</td>
					<td align="left"><input id="Confirmer" name="Confirmer" type="text" class="easyui-validatebox" style="width:152px" valueField="name" textField="name" /><input id="ConfirmerId" name="ConfirmerId" type="hidden" /></td>
					<td align="right">溯源结果&nbsp;&nbsp;<br />确认日期：</td>
					<td align="left"><input id="ConfirmDate" name="ConfirmDate" type="text" class="easyui-datebox" style="width:152px" /></td>
				</tr>
				<tr  height="50px">
					<td align="right">鉴定单位：</td>
					<td align="left" >
						<input id="Tester" name="Tester" type="text" class="easyui-validatebox" style="width:250px"  required="true" />
					</td>
					<td align="right">鉴定证书&nbsp;&nbsp;<br />编&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
					<td align="left" ><input id="CertificateId" name="CertificateId" type="text" class="easyui-validatebox" required="true"/></td>
				</tr>
				<tr height="30px">
					<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
					<td align="left"  colspan="3">
					<select id="Status" name="Status" >
						<option value="0" selected="selected">正常</option>
						<option value="1">注销</option>
					</select> </td>
				</tr>
				<tr height="30px">
					<td align="right">溯源结果&nbsp;&nbsp;<br />确认意见：</td>
					<td align="left"  colspan="3"><textarea id="ConfirmMeasure" name="ConfirmMeasure" cols="55" rows="2" ></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">期间核查：</td>
					<td align="left"  colspan="3"><textarea id="DurationCheck" name="DurationCheck" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">维护保养：</td>
					<td align="left"  colspan="3"><textarea id="Maintenance" name="Maintenance" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td align="left"  colspan="3"><textarea id="Remark" name="Remark" cols="55" rows="2"></textarea> </td>
				</tr>
				<tr height="30px">
					<td align="right">检定证书&nbsp;&nbsp;<br />扫&nbsp;描&nbsp;件：</td>
					<td align="left" colspan="3"><input id="CertificateCopy" name="CertificateCopy" type="file" style="width:350px"/></td>
				</tr>
				
				<tr height="50px">	
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="save()">添加</a></td>
					<td></td>
					<td><a href="#" class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">重置</a></td>
				</tr>
			</table>
			</form>
		</div>
</DIV>
</DIV>
</body>
</html>
