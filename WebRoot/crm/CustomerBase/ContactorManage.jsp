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
		
	
		
		
		$('#customerName').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'name',
				textField:'name',
				onSelect:function(rec){
				//$('#customerId').val(rec.id);
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
								var newValue = $('#customerName').combobox('getText');
								$('#customerName').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);
				}
			});
		
			$('#customerName1').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
				textField:'name',
				onSelect:function(rec){
				$('#customerId').val(rec.id);
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
								var newValue = $('#customerName1').combobox('getText');
								$('#customerName1').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);
				}
			});

			$('#customerName2').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
				textField:'name',
				onSelect:function(rec){
				$('#customerId').val(rec.id);
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
								var newValue = $('#customerName2').combobox('getText');
								$('#customerName2').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);
				}
			});
			
			$('#contactors').datagrid({
				title:'联系人信息',
				width:900,
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
					{field:'Name',title:'姓名',width:70,align:'center'},
					//{field:'Email',title:'电子邮箱',width:80,align:'center'},
					{field:'Cellphone1',title:'联系电话1',width:100,align:'center'},
					{field:'CurJob',title:'职业',width:100,align:'center'},
					{field:'Cellphone2',title:'联系电话2',width:100,align:'center'},
					{field:'Birthday',title:'生日',width:100,align:'center'},
					{field:'Email',title:'电子邮箱',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center'},
					{field:'FormerDep',title:'原单位',width:80,align:'center'},
					{field:'FormerJob',title:'原职位',width:80,align:'center'},
					{field:'CurDep',title:'现单位',width:80,align:'center'},
					{field:'CustomerId',title:'现职位',width:80,align:'center',hidden:true},
					{field:'Count',title:'使用次数',width:80,align:'center'},
					{field:'LastUse',title:'最后使用时间',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'新增',
						iconCls:'icon-add',
						handler:function(){
							
							$('#add_con').window('open');
							$('#frm_add_con').show();
							$('#frm_add_con').form('clear');
							
							$('#edit_con').panel({title:"新增"});
						}
				},'-',{
						text:'修改',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select){
							$('#edit_con').window('open');
							$('#frm_edit_con').show();
							$('#frm_edit_con').form('clear');
							$('#frm_edit_con').form('load',select);
							$('#frm_edit_con').panel({title:"修改"});
							$('#frm_edit_con').form('validate');
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
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
				}]
			});
			
			$('#queryname').combobox('setValue',"");
			$('#queryInsideContactor').combobox('setValue',"");
		});

		function query()
		{
			$('#contactors').datagrid('options').url='/jlyw/CustomerContactorServlet.do?method=4';
			$('#contactors').datagrid('options').queryParams={'CustomerName':encodeURI($('#customerName').combobox('getValue')),'Name':encodeURI($('#name').val()),'Status':encodeURI($('#status').combobox('getValue'))};
			$('#contactors').datagrid('reload');
			//$('#queryname').combobox('setValue',"");
			//$('#queryInsideContactor').combobox('setValue',"");
		}
		
			function AddContactor(){
		$('#frm_add_con').form('submit',{
			url: '/jlyw/CustomerContactorServlet.do?method=5',
			onSubmit:function(){ return $('#frm_add_con').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOk){
		   			$('#contactors').datagrid('reload');
		   			//cancel();
		   			}
		   		}
		});
	}
	
	function SubmitEdit(){
		$('#frm_edit_con').form('submit',{
			url: '/jlyw/CustomerContactorServlet.do?method=1',
			onSubmit:function(){ return $('#frm_edit_con').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOK){
		   			$('#contactors').datagrid('reload');
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
					<td align="right" >单位：</td>
				  	<td align="left"><input style="width:200px" class="easyui-combobox"  id="customerName" name="CustomerName" ></input></td>
                    <td align="right">姓名：</td>
				  	<td align="left"><input class="easyui-validate" id="name" name="Name"></input></td>
                    <td align="right">状态：</td>
				  	<td align="left"><select id="status" name="Status" class="easyui-combobox">
				  	<option value="">==显示全部==</option>
				  	<option value="0">正常</option>
				  	<option value="1">已注销</option>
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
        <table id="contactors" style="height:500px; width:900px"></table>
        </div>
    <div id="add_con" class="easyui-window" title="新增" style="padding: 10px;width: 500px;height: 200px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_con" method="post">
				<div>
					<table  iconCls="icon-edit" >
						<tr>
							<td align="right">姓&nbsp;&nbsp;&nbsp;&nbsp;名：<input id="Id" name="Id" type="hidden"/>
                    								<input id="customerId" name="CustomerId" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">联系单位：</td>
							<td align="left"><input id="customerName1" name="CustomerName1" class="easyui-combobox" required="true"/></td>
                        </tr>
                        <tr>
                        	<td align="right">联系电话1：</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
							
							<td align="right">联系电话2：</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td align="right">职&nbsp;&nbsp;&nbsp;&nbsp;业：
						</td>
						<td><input id="curJob" name="CurJob" /></td>
						<td align="right">生&nbsp;&nbsp;&nbsp;&nbsp;日：</td>
							<td align="left"><input id="birthday" name="Birthday" class="easyui-datebox" editable="false"/></td>
						</tr>
						 <tr>
							
							<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
							<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
							<td align="left"><input id="remark" name="Remark" class="easyui-validatebox"/></td>
						</tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="AddContactor()">添加</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div id="edit_con" class="easyui-window" title="新增" style="padding: 10px;width: 500px;height: 300px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_con" method="post">
				<div>
					<table  iconCls="icon-edit" >
						<tr>
							<td align="right">姓&nbsp;&nbsp;&nbsp;&nbsp;名：<input id="Id" name="Id" type="hidden"/>
                    								<input id="customerId" name="CustomerId" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">联系单位：</td>
							<td align="left"><input id="customerName2" name="CustomerName2" class="easyui-combobox" required="true"/></td>
                        </tr>
                        <tr>
                        	<td align="right">联系电话1：</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
							
							<td align="right">联系电话2：</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						
						<td align="right">生&nbsp;&nbsp;&nbsp;&nbsp;日：</td>
							<td align="left"><input id="birthday" name="Birthday" class="easyui-datebox" editable="false"/></td>
							<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td align="right">原单位：</td>
							<td align="left"><input id="formerDep" name="FormerDep" class="easyui-validatebox"/></td>
							
							<td align="right">现单位：</td>
							<td align="left"><input id="curDep" name="CurDep" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
						 <td align="right">(原)职位：</td>
							<td align="left"><input id="formerJob" name="FormerJob" class="easyui-validatebox"/></td>
							<td align="right">现职位：</td>
							<td align="left"><input id="curJob2" name="CurJob" class="easyui-validatebox"/></td>
							
						</tr>
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
							<td align="left"><input id="remark" name="Remark" class="easyui-validatebox"/></td>
						<tr>
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
		
		 <div id="log_off_con" class="easyui-window" title="注销" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			
			<form id="frm_log_off_con" method="post">
			<table id="table4" iconCls="icon-edit" >
						<tr>
							<td align="right">姓名：<input type="hidden" id="Id" name="Id" />
                    								<input id="CustomerId1" name="CustomerId1" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">原单位：</td>
							<td align="left"><input id="formerDep" name="FormerDep" class="easyui-validatebox" /></td>
                        </tr>
                        <tr>
                        	<td align="right">原职位：</td>
							<td align="left"><input id="formerJob" name="FormerJob" class="easyui-validatebox"/></td>
							
							<td align="right">现单位：</td>
							<td align="left"><input id="curDep" name="CurDep" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
							<td align="right">现职位：</td>
							<td align="left"><input id="curJob2" name="CurJob" class="easyui-validatebox"/></td>
						</tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="LogOffContactor()">确定</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
						</table>
			</form>
		</div>
    
    </DIV>
    </DIV>
</body>
</html>
