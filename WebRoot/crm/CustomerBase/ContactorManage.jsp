<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>ί�е�λ��Ϣ��ѯ</title>
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
				title:'��ϵ����Ϣ',
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
					{field:'Name',title:'����',width:70,align:'center'},
					//{field:'Email',title:'��������',width:80,align:'center'},
					{field:'Cellphone1',title:'��ϵ�绰1',width:100,align:'center'},
					{field:'CurJob',title:'ְҵ',width:100,align:'center'},
					{field:'Cellphone2',title:'��ϵ�绰2',width:100,align:'center'},
					{field:'Birthday',title:'����',width:100,align:'center'},
					{field:'Email',title:'��������',width:80,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center'},
					{field:'FormerDep',title:'ԭ��λ',width:80,align:'center'},
					{field:'FormerJob',title:'ԭְλ',width:80,align:'center'},
					{field:'CurDep',title:'�ֵ�λ',width:80,align:'center'},
					{field:'CustomerId',title:'��ְλ',width:80,align:'center',hidden:true},
					{field:'Count',title:'ʹ�ô���',width:80,align:'center'},
					{field:'LastUse',title:'���ʹ��ʱ��',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'����',
						iconCls:'icon-add',
						handler:function(){
							
							$('#add_con').window('open');
							$('#frm_add_con').show();
							$('#frm_add_con').form('clear');
							
							$('#edit_con').panel({title:"����"});
						}
				},'-',{
						text:'�޸�',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select){
							$('#edit_con').window('open');
							$('#frm_edit_con').show();
							$('#frm_edit_con').form('clear');
							$('#frm_edit_con').form('load',select);
							$('#frm_edit_con').panel({title:"�޸�"});
							$('#frm_edit_con').form('validate');
						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
						text:'ע��',
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
							//$('#edit_con').panel({title:"�޸�"});
							$('#frm_log_off_con').form('validate');
						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
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
		   		$.messager.alert('��ʾ',result.msg,'info');
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
		   		$.messager.alert('��ʾ',result.msg,'info');
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
		   			$.messager.alert('��ʾ',result.msg,'info');
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
			<jsp:param name="TitleName" value="ί�е�λ��Ϣ��ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div border="true" style="width:900px;overflow:hidden;position:relative;">
		<div>
			<br />
			<br />
			<form id="query">
			<table id="table1" class="easyui-panel" title="��ѯ" style="width:900px;">
            
				<tr>
					<td align="right" >��λ��</td>
				  	<td align="left"><input style="width:200px" class="easyui-combobox"  id="customerName" name="CustomerName" ></input></td>
                    <td align="right">������</td>
				  	<td align="left"><input class="easyui-validate" id="name" name="Name"></input></td>
                    <td align="right">״̬��</td>
				  	<td align="left"><select id="status" name="Status" class="easyui-combobox">
				  	<option value="">==��ʾȫ��==</option>
				  	<option value="0">����</option>
				  	<option value="1">��ע��</option>
				  	</select></td>
				</tr>
                <tr>
                <td></td><td></td>
                <td  align="right"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">��ѯ</a></td>
					<td ><a href="javascript:void(0)" onclick="$('#query').form('clear');" class="easyui-linkbutton" iconCls="icon-reload">����</a></td>
				</tr>            
			</table> </form>
		</div>
        <br/>
        <table id="contactors" style="height:500px; width:900px"></table>
        </div>
    <div id="add_con" class="easyui-window" title="����" style="padding: 10px;width: 500px;height: 200px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_con" method="post">
				<div>
					<table  iconCls="icon-edit" >
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����<input id="Id" name="Id" type="hidden"/>
                    								<input id="customerId" name="CustomerId" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">��ϵ��λ��</td>
							<td align="left"><input id="customerName1" name="CustomerName1" class="easyui-combobox" required="true"/></td>
                        </tr>
                        <tr>
                        	<td align="right">��ϵ�绰1��</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
							
							<td align="right">��ϵ�绰2��</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td align="right">ְ&nbsp;&nbsp;&nbsp;&nbsp;ҵ��
						</td>
						<td><input id="curJob" name="CurJob" /></td>
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�գ�</td>
							<td align="left"><input id="birthday" name="Birthday" class="easyui-datebox" editable="false"/></td>
						</tr>
						 <tr>
							
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
							<td align="left"><input id="remark" name="Remark" class="easyui-validatebox"/></td>
						</tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="AddContactor()">���</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div id="edit_con" class="easyui-window" title="����" style="padding: 10px;width: 500px;height: 300px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_con" method="post">
				<div>
					<table  iconCls="icon-edit" >
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����<input id="Id" name="Id" type="hidden"/>
                    								<input id="customerId" name="CustomerId" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">��ϵ��λ��</td>
							<td align="left"><input id="customerName2" name="CustomerName2" class="easyui-combobox" required="true"/></td>
                        </tr>
                        <tr>
                        	<td align="right">��ϵ�绰1��</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
							
							<td align="right">��ϵ�绰2��</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�գ�</td>
							<td align="left"><input id="birthday" name="Birthday" class="easyui-datebox" editable="false"/></td>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
						</tr>
						<tr>
						<td align="right">ԭ��λ��</td>
							<td align="left"><input id="formerDep" name="FormerDep" class="easyui-validatebox"/></td>
							
							<td align="right">�ֵ�λ��</td>
							<td align="left"><input id="curDep" name="CurDep" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
						 <td align="right">(ԭ)ְλ��</td>
							<td align="left"><input id="formerJob" name="FormerJob" class="easyui-validatebox"/></td>
							<td align="right">��ְλ��</td>
							<td align="left"><input id="curJob2" name="CurJob" class="easyui-validatebox"/></td>
							
						</tr>
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
							<td align="left"><input id="remark" name="Remark" class="easyui-validatebox"/></td>
						<tr>
						</tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="SubmitEdit()">�ύ</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		 <div id="log_off_con" class="easyui-window" title="ע��" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			
			<form id="frm_log_off_con" method="post">
			<table id="table4" iconCls="icon-edit" >
						<tr>
							<td align="right">������<input type="hidden" id="Id" name="Id" />
                    								<input id="CustomerId1" name="CustomerId1" type="hidden"/></td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">ԭ��λ��</td>
							<td align="left"><input id="formerDep" name="FormerDep" class="easyui-validatebox" /></td>
                        </tr>
                        <tr>
                        	<td align="right">ԭְλ��</td>
							<td align="left"><input id="formerJob" name="FormerJob" class="easyui-validatebox"/></td>
							
							<td align="right">�ֵ�λ��</td>
							<td align="left"><input id="curDep" name="CurDep" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
							<td align="right">��ְλ��</td>
							<td align="left"><input id="curJob2" name="CurJob" class="easyui-validatebox"/></td>
						</tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="LogOffContactor()">ȷ��</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
						</table>
			</form>
		</div>
    
    </DIV>
    </DIV>
</body>
</html>
