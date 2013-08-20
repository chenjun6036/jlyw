<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>增加现场业务</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
			$(function() {
				$("#ProjectTeamIdAdd").combobox({
					valueField:'Name',
					textField:'Name',
					onSelect:function(record){
						if($('#MissionDescAdd').val()==null||$('#MissionDescAdd').val().length==0)
								$('#MissionDescAdd').val(record.Project);
						else{						
							var temp=$('#MissionDescAdd').val();
							$('#MissionDescAdd').val(temp+'\r\n'+record.Project);
						}
						document.getElementById("MissionDescAdd").focus(); 
					},
					onChange:function(newValue, oldValue){
						var allData = $(this).combobox('getData');
						if(allData != null && allData.length > 0){
							for(var i=0; i<allData.length; i++)
							{
								if(newValue==allData[i].Name){
									return false;
								}
							}
						}											
						try{
							window.clearTimeout(this.reloadObj);
						}catch(ex){}
						this.reloadObj = window.setTimeout(function(){   
								var newValue = $('#ProjectTeamIdAdd').combobox('getText');
								$('#ProjectTeamIdAdd').combobox('reload','/jlyw/ApplianceServlet.do?method=6&QueryName='+encodeURI(newValue));
						}, 700);
						//$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
					}
				});
			
					$("#NameAdd").combobox({
					//	url:'/jlyw/CustomerServlet.do?method=5',
						valueField:'name',
						required:true,
						textField:'name',
						onSelect:function(record){
							$("#TelAdd").val(record.tel);
							$("#CustomerIdAdd").val(record.id);
							$("#contactortelAdd").val(record.contactorTel);
							$("#AddressAdd").val(record.address);
							$("#zcdAdd").val(record.zipCode);
							$("#conAdd").val(record.contactor);
							$("#contactortelAdd").val(record.contactorTel);
							$("#RegionIdAdd").val(record.regionId);
							$("#ridAdd").combobox('setValue',record.regionName);
							
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
							try{
								window.clearTimeout(this.reloadObj);
							}catch(ex){}
							this.reloadObj = window.setTimeout(function(){   
									var newValue = $('#NameAdd').combobox('getText');
									$('#NameAdd').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
							}, 700);
							
						}
					});
					
					$('#usernameAdd').combobox({
					//	url:'/jlyw/CustomerServlet.do?method=6',
						onSelect:function(){
							if($('#StaffsAdd').val()==null||$('#StaffsAdd').val().length==0)
								$('#StaffsAdd').val($('#usernameAdd').combobox('getText'));
							else{
							    var temp=$('#StaffsAdd').val();
								$('#StaffsAdd').val(temp+";"+$('#usernameAdd').combobox('getText'));
							}
							//('#usernameAdd').combobox('clear');
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
							try{
								window.clearTimeout(this.reloadObj);
							}catch(ex){}
							this.reloadObj = window.setTimeout(function(){   
									var newValue = $('#usernameAdd').combobox('getText');
									$('#usernameAdd').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
							}, 700);
							//$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
						}
					});
					
					
							
					$("#SiteManagerNameAdd").combobox({
					//	url:'/jlyw/CustomerServlet.do?method=5',
						valueField:'id',
						textField:'name',
						onSelect:function(record){
							$("#SiteManagerId").val(record.id);
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
							$("#SiteManagerId").val('');
							try{
								window.clearTimeout(this.reloadObj);
							}catch(ex){}
							this.reloadObj = window.setTimeout(function(){   
									var newValue = $('#SiteManagerNameAdd').combobox('getText');
									$('#SiteManagerNameAdd').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
							}, 700);
						}
					});
					$("#ridAdd").combobox({
						url:'/jlyw/RegionServlet.do?method=2',
						valueField:'id',
						textField:'name',
						onSelect:function(record){
							$("#RegionIdAdd").val(record.id);
						},
						onChange:function(newValue, oldValue){
							//$("#RegionId").val('');
						}
					});
					
					var nowDate = new Date();
					$("#TentativeDateAdd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			});

		function cancel(){
			$('#frm_add_customer').form('clear');
		}
		
		function savereg(){
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=1',
				onSubmit:function(){
					//alert('onSubmit');
					if($('#RegionIdAdd').val() == ''){
						$.messager.alert('提示','请选择一个有效的地区！','info');
						return false;
					}
					
					return $('#frm_add_customer').form('validate');
				},
		   		success:function(data){
		   		    var result = eval("("+data+")");
		   		    if(result.IsOK){
		   		   		 $.messager.alert('提示！','添加成功','info');
					}
					else{
						 $.messager.alert('添加失败！',result.msg,'error');
					}
		   		 }
			});
		}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="增加现场业务" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div id="add" class="easyui-window" modal="true" title="录入现场业务信息" style="padding: 10px;width: 900;height: 500;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">	
        <form id="frm_add_customer" method="post" >
	<table style="width:700px; height:400px; padding-top:10px" cellspacing="5" >
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：</td>
			<td align="left"  style="width：30%"><select name="Name" id="NameAdd" style="width:175px"></select><input type="hidden" name="CustomerId" id="CustomerId" /></td>
			<td align="right" style="width：20%">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left"  style="width：30%"><input id="zcdAdd" name="ZipCode" type="text" style="width:175px"  readonly="readonly"/></td>
		</tr>
		<tr height="30px">
			<td align="right">单位地址：</td>
			<td align="left"><input id="AddressAdd" name="Address" type="text" style="width:172px" readonly="readonly"/></td>
		    <td align="right">单位电话：</td>
		    <td align="left"><input id="TelAdd" name="Tel" type="text" style="width:175px" readonly="readonly"/></td>
		</tr>
		<tr height="30px">
			<td align="right">联 系 人：</td>
			<td align="left"><input id="conAdd" name="Contactor" type="text" style="width:175px" readonly="readonly"/></td>
		    <td align="right">联系人号码：</td>
		    <td align="left"><input id="contactortelAdd" name="ContactorTel" type="text" style="width:175px" readonly="readonly"/></td>
		</tr>
		<tr  height="30px">
			<td align="right">所在地区：</td>
			<td align="left">
				<select id="ridAdd" name="Region" class="easyui-combobox" style="width:177px" required="true"></select><input type="hidden" name="RegionId" id="RegionIdAdd" /></td>
			<td align="right">暂定日期：</td>
			<td align="left"><input type="text" id="TentativeDateAdd" name="TentativeDate" class="easyui-datebox" style="width:177px" required="true" /></td>
		</tr>
		<tr height="30px">
			<td align="right">现场检测负责人：</td>
			<td align="left" colspan="3"><select name="SiteManagerName" id="SiteManagerNameAdd" style="width:177px"></select><input type="hidden" name="SiteManagerId" id="SiteManagerId" value="" /></td>
			
		</tr>
		<tr height="30px">
			<td align="right">参与人员：</td>
			<td colspan="3" align="left"><input id="StaffsAdd" name="Staffs" type="text" style="width:300px" class="easyui-validatebox" maxlength="100" />&nbsp;选择<input id="usernameAdd" name="username" class="easyui-combobox"  url="" style="width:177px;" valueField="name" textField="name" panelHeight="150px" /></td>
		</tr>
		<tr height="30px">
			<td align="right">检测项目及台件数：</td>
			<td align="left"  colspan="3"><textarea style="height:60px;font-size:12px" id="MissionDescAdd" name="MissionDesc" cols="47" rows="2" class="easyui-validatebox" required="true" maxlength="500"></textarea>&nbsp;添加<select id="ProjectTeamIdAdd" name="ProjectTeamId" type="text" class="easyui-combobox"  style="width:177px" panelHeight="150px"/></td>
		</tr>
		<tr height="30px">
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left"  colspan="3"><textarea style="height:60px;font-size:12px" id="RemarkAdd" name="Remark" cols="47" rows="2"  maxlength="500"></textarea></td>
		</tr>
		<tr height="30px">
			<td align="right">业务部门：</td>
			<td colspan="3" align="left"><input id="DepartmentAdd" name="Department" class="easyui-validatebox" style="width:177px"  required="true"/></td>
		</tr>
		
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">添加</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">重置</a></td>
		</tr>
	</table>
	</form>
</div>
		
</DIV></DIV>
</body>
</html>
