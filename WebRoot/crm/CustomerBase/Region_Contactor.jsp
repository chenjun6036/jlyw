<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>委托单位信息查询</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>

$(function(){
	$("#insideContactor1").combobox({
		valueField:'id',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#jobNum1").val(record.jobNum);
				//$("#insideContactorId").val(record.id);
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
					var newValue = $("#insideContactor1").combobox('getText');
					$("#insideContactor1").combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	} );
	
	
	$(function(){
	$("#name").combobox({
		valueField:'id',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#jobNum").val(record.jobNum);
				$("#conId").val(record.id);
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
					var newValue = $("#name").combobox('getText');
					$("#name").combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	} );
	
	
		$(function(){
	$("#name0").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				//$("#jobNum").val(record.jobNum);
				//$("#insideContactorId").val(record.id);
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
					var newValue = $("#name0").combobox('getText');
					$("#name0").combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 500);
		}
	});
	} );
	
	
			$(function(){
			$('#region_contactors').datagrid({
				title:'联系人信息',
				width:800,
				height:500,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'',
				//sortName:'Id',
				//sortOrder:'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'CustomerName',title:'联系单位',width:70,align:'center'},
					//{field:'CustomerId',title:'联系单位Id',width:70,align:'center'},
					{field:'RegionName',title:'地区',width:100,align:'center'},
					{field:'RegionId',title:'地区ID',width:100,align:'center'},
					//{field:'Address',title:'地址',width:100,align:'center'},
					{field:'Name',title:'联系人姓名',width:100,align:'center'},
					{field:'ContactorId',title:'联系人Id',width:100,align:'center'},
					{field:'JobNum',title:'联系人工号',width:100,align:'center'},
					{field:'Id',title:'Id',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center'},
				
					{field:'LastEditTime',title:'最后使用时间',width:160,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'新增',
						iconCls:'icon-add',
						handler:function(){
							
							$('#add_region_contactors').window('open');
							$('#frm_add').show();
							$('#frm_add').form('clear');
							$('#add_region_contactors').panel({title:"新增"});
						}
				},'-',{
						text:'修改',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#region_contactors').datagrid('getSelected');
							if(select){
							$('#edit_region_contactors').window('open');
							$('#frm_edit').show();
							$('#frm_edit').form('clear');
							$('#frm_edit').form('load',select);
							$('#conId').val(select.ContactorId);
							$('#edit_region_contactors').panel({title:"修改"});
							$('#frm_edit').form('validate');
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-'/* ,{
						text:'注销',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select)
							{
							$('#log_off_con').window('open');
							$('#frm_log_off_con').show();
							$('#frm_log_off_con').form('clear');
							$('#frm_log_off_con').form('load',select);
							var select2 = $('#table2').datagrid('getSelected');
							$('#formerDep').val(select2.Name);
							//$('#edit_con').panel({title:"修改"});
							$('#frm_log_off_con').form('validate');
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
							}
					}
				} */]
			});
			
			//$('#queryname').combobox('setValue',"");
			//$('#queryInsideContactor').combobox('setValue',"");
		});

		function query()
		{
			$('#region_contactors').datagrid('options').url='/jlyw/CustomerContactorServlet.do?method=6';
			$('#region_contactors').datagrid('options').queryParams={'RegionId':encodeURI($('#regionId0').combobox('getValue')),'Name':encodeURI($('#name0').combobox('getValue')),'Status':encodeURI($('#status0').combobox('getValue'))};
			$('#region_contactors').datagrid('reload');
			//$('#queryname').combobox('setValue',"");
			//$('#queryInsideContactor').combobox('setValue',"");
		}
		
			function Add(){
		$('#frm_add').form('submit',{
			url: '/jlyw/CustomerContactorServlet.do?method=7',
			onSubmit:function(){ return $('#frm_add').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOk){
		   			$('#region_contactors').datagrid('reload');
		   			//cancel();
		   			}
		   		}
		});
	}
	
	function SubmitEdit(){
		$('#frm_edit').form('submit',{
			url: '/jlyw/CustomerContactorServlet.do?method=8',
			onSubmit:function(){ return $('#frm_edit').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOk){
		   			$('#region_contactors').datagrid('reload');
		   			//cancel();
		   			}
		   		}
		});
	}
	
	function LogOffContactor(){
			$('#frm_log_off_con').form('submit',{
				url: '/jlyw/CustomerContactorServlet.do?method=3',
				onSubmit:function(){return $('#frm_log_off_con').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				//closed();
		   			$('#contactors').datagrid('reload');		
		   		}
			});
		}
</script>

</head>

<body>
<form id="frm_export" method="post" action="/jlyw/CustomerServlet.do?method=7">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单位信息查询管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div border="true" style="width:900px;overflow:hidden;position:relative;">
		<div>
			<br />
			<br />
			<form id="query">
			<table id="table1" class="easyui-panel" title="查询" style="width:900px;">
            
				<tr>
					<td align="right" >地区：</td>
				  	<td align="left"><input  class="easyui-combobox"  id="regionId0" name="RegionId0" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></input></td>
                    <td align="right">姓名：</td>
				  	<td align="left"><input class="easyui-validate" id="name0" name="Name0"></input></td>
                    <td align="right">状态：</td>
				  	<td align="left"><select id="status0" name="Status0" class="easyui-combobox">
				  	<option value="1">已分配</option>
				  	<option value="2">未分配</option>
				  	<option value="3">已过期</option>
				  	</select></td>
				</tr>
                <tr>
                <td></td><td></td>
                <td  align="right"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
					<td ><a href="javascript:void(0)" onclick="$('#query').form('clear');" class="easyui-linkbutton" iconCls="icon-reload">重置</a></td>
				</tr>            
			</table> </form>
		</div>
        <br/>
        <table id="region_contactors" style="height:500px; width:900px"></table>
        </div>
    <div id="add_region_contactors" class="easyui-window" title="新增" style="padding: 10px;width: 500px;height: 200px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add" method="post">
				<div>
					<table  iconCls="icon-edit" >
					<tr>
					<td align="right" >地区：</td>
				  	<td align="left"><input  class="easyui-combobox"  id="rigionId1" name="RegionId1" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false"></input></td>
					</tr>
						<tr>
						
						<td align="right">姓名：</td>
							<td align="left"><input id="insideContactor1" name="InsideContactor1" class="easyui-combobox" required="true"/></td>
							<td align="right">工&nbsp;&nbsp;&nbsp;&nbsp;号：
													
							<td align="left" ><input id="jobNum1" name="JobNum1" class="easyui-validatebox" required="true"/></td>
							
                        </tr>
                      	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="Add()">添加</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div id="edit_region_contactors" class="easyui-window" title="新增" style="padding: 10px;width: 450px;height: 200px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit" method="post">
				<div>
					<table  iconCls="icon-edit" >
					<tr>
						<td align="right" ><input type="hidden" name="Id"></input>地区：</td>
				  		<td align="left"><input  class="easyui-combobox"  id="rigionId" name="RegionId" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false"></input></td>
						<td align="right">姓名：</td>
						<td align="left"><input id="name" name="Name" class="easyui-combobox" required="true"/>
						<input id="conId" name="ConId" type="hidden"/>
						</td>
					</tr>
                    <tr>
                        	<td align="right">工&nbsp;&nbsp;&nbsp;&nbsp;号：
                        	<td align="left" ><input id="jobNum" name="JobNum" class="easyui-validatebox" required="true"/></td>
                        	<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：
                        	<td align="left" >
                        	<select id="status" name="Status" class="easyui-combobox" required="true">
                        	<option value="1">已分配</option>
				  			<option value="2">未分配</option>
				  			<option value="3">已过期</option>
                        	</select></td>
					</tr>

						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="SubmitEdit()">提交</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
    
    </DIV>
    </DIV>
</body>
</html>
