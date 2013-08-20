<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	 <title>转包方业务信息查删改</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />

    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script>
		$(function(){
		    var lastIndex;
			
			
			$('#test').datagrid({
				title:'转包方业务信息',
//				iconCls:'icon-save',
				width:700,
				height:350,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'datagrid_data.json',
				sortName: 'userid',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'userid',
				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'name',title:'单位名称',width:120,sortable:true,align:'center'}
				]],
				columns:[[
					{field:'code',title:'单位代码',width:80,align:'center'},
					{field:'address1',title:'地区',width:80,align:'center'},
					{field:'ZipCode',title:'邮编',width:80,align:'center'},
					{field:'address2',title:'单位地址',width:180,align:'center'},
					{field:'Tel',title:'单位电话',width:80,align:'center'},
					{field:'Contactor',title:'联系人',width:80,align:'center'},
					{field:'Tel2',title:'联系电话',width:80,align:'center'},
					{field:'Status',title:'单位状态',width:80,align:'center'},
					{field:'Reason',title:'备注',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'append',
					iconCls:'icon-add',
					handler:function(){
						$('#test').datagrid('endEdit', lastIndex);
						$('#test').datagrid('appendRow',{
							 name:'',
							code:'',
							ZipCode:'',
							address1:'',
							Contactor:'',
							Tel:'',
							Tel2:'',
							Status:'',
							Reason:''
						});
						lastIndex = $('#test').datagrid('getRows').length-1;
						$('#test').datagrid('selectRow', lastIndex);
						$('#test').datagrid('beginEdit', lastIndex);
					}
				},'-',{
					  text:'modify',
					  iconCls:'icon-edit',
					  handler:function(){
						var select = $('#test').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#ff').show();
						
						$('#name').val(select.name);
						$('#code').val(select.code);
						$('#ZipCode').val(select.ZipCode);
						$('#address1').val(select.address1);
						$('#Contactor').val(select.Contactor);
						$('#Tel').val(select.Tel);
						$('#Tel2').val(select.Tel2);
						$('#ZipCode').val(select.ZipCode);
						$('#Reason').val(select.Reason);
						name = select.name;
					}else{
						$.messager.alert('warning','请选择一行数据','warning');
			}
					}
				},'-',{
					text:'delete',
					iconCls:'icon-remove',
					handler:function(){
						var row = $('#test').datagrid('getSelected');
						if (row){
							var index = $('#test').datagrid('getRowIndex', row);
							$('#test').datagrid('deleteRow', index);
						}
					}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
					if (lastIndex != rowIndex){
						$('#test').datagrid('endEdit', lastIndex);
						$('#test').datagrid('beginEdit', rowIndex);
					}
					lastIndex = rowIndex;
				}
			});
		});
	</script>

</head>

<body >
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="转包方业务信息查删改" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<table width="100%" height="100%">
		<tr>
			<td width="100%" style="padding-top:15px; padding-right:12px; vertical-align:bottom; overflow:visible" align="right">
			<form id="ff" method="post">
				<div>
					
					<label for="type">部门:</label>
					<input class="easyui-combobox" name="type" url="combobox_log_type.json" valueField="id" textField="text" panelHeight="auto"></input>
					&nbsp;&nbsp;&nbsp;
					
					<label id="label_dd" for="dd">用户名:</label>
					<div id="div_username" style="display:inline">
						<input id="dd" class="easyui-combobox" name="type" url="datagrid_data2.json"  valueField="id" textField="text" panelHeight="auto" ></input>
					</div>
					&nbsp;&nbsp;&nbsp;
					<a href="javascript:$('#ff').form('submit',{
								url:'',
								onSubmit:function(){
									return $(this).form('validate');
								},
								success:function(data){
								}
							});" class="easyui-linkbutton" iconCls="icon-search">Search</a>
				</div>
			</form>
			</td>
		</tr>
		<tr height="100%">
			<td width="100%">
				<table id="test" iconCls="icon-edit" singleSelect="true" ></table>
			</td>
		</tr>
	</table>
 <div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 950;height: 650;+position:relative;" 
    iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
    	<div id="ee">
		
    <form id="ff" method="post">
     <div id="p" class="easyui-panel" style="width:900px;height:180px;padding:10px;"
				title="委托单信息" collapsible="false"  closable="false">
			<table width="850px" id="table1">
				<tr>
					<td width="14%" align="right">委托单编号：</td>
					<td width="20%" align="left">
						<input id="id" class="easyui-combobox" name="id" url="commissionid.json" style="width:150px;" valueField="id" textField="text" panelHeight="auto" >
					</td>
					<td width="14%" align="right">委托&nbsp;&nbsp;时间：</td>
				    <td width="20%" colspan="3" align="left"><input name="time" class="easyui-datebox" style="width:152px;" type="text" readonly></td>
				</tr>
				<tr>
					<td width="14%" align="right">委托&nbsp;&nbsp;单位：</td>
				    <td width="20%" align="left"><input name="Name" type="text" style="width:200px;" readonly></td>
					<td width="14%" align="right">单位&nbsp;&nbsp;地址：</td>
				    <td width="20%" align="left"><input name="address" type="text" readonly></td>
					<td width="14%" align="right">联&nbsp;系&nbsp;人：</td>
				    <td width="20%" align="left"><input name="contactperson" type="text" readonly></td>
				</tr>
				<tr>
					<td width="14%"  align="right">特殊&nbsp;&nbsp;要求：</td>
					<td width="20%" align="left"><input name="demand" type="text" readonly></td>
					<td width="14%" align="right">指定检验员：</td>
					<td width="20%" colspan="3" align="left"><input name="check" type="text" readonly></td>
				</tr>
		</table>
		</div>
		<div id="p1" class="easyui-panel" style="width:900px;height:140px;padding:10px;"
				title="送检器具" collapsible="false"  closable="false">
			<table width="850px" id="table2">
				<tr>
					<td width="12%" align="right">器具&nbsp;&nbsp;名称：</td>
					<td width="22%" align="left">
						<input name="name2" type="text" readonly>
					</td>
					<td width="12%" align="right">型号&nbsp;&nbsp;规格：</td>
				    <td width="22%" colspan="3" align="left"><input name="standards" type="text" readonly></td>
				</tr>
				<tr>
					<td style="padding-top:5px" width="12%" align="right" valign="middle">是否&nbsp;&nbsp;加急：</td>
				    <td  width="22%" align="left" valign="middle"><INPUT type = "radio" name = selection1 value = "A"></INPUT> 是 <INPUT type = "radio" name = selection1 value = "B"></INPUT>否
</td>
					<td style="padding-top:5px"  width="12%" align="right" valign="middle">是否&nbsp;&nbsp;修理：</td>
				    <td  width="22%" align="left" valign="middle"><INPUT type = "radio" name = selection value = "A"></INPUT> 是 <INPUT type = "radio" name = selection value = "B"></INPUT>否</td>
					<td width="10%" align="right">报告形式：</td>
				    <td width="22%" align="left"><input name="Reportform" type="text" readonly> </td>
				</tr>
				
		</table>
		</div>
   <div >
     <div id="p" class="easyui-panel" style="width:900px;height:150px;padding:10px;"
				title="转包方信息" collapsible="false"  closable="false">
			
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right">单位名称：</td>
					<td width="22%" align="left">
						<input id="name" type="text" name="name" class="easyui-combobox" style="width:150px;" >
					</td>
					<td width="10%" align="right">单位代码：</td>
				    <td width="21%" align="left"><input name="time" type="text" readonly></td>
				</tr>
				<tr >
					<td width="10%"  align="right">单位地址：</td>
					<td width="22%"  align="left"><input name="demand" type="text" style="width:280px;" readonly></td>
					<td width="10%"  align="right">单位电话：</td>
					<td width="22%" align="left"><input name="demand" type="text" readonly></td>
				</tr>
				<tr >
					<td width="10%" align="right">联&nbsp;系&nbsp;人：</td>
				    <td width="22%" align="left"><input name="Name" type="text" readonly></td>
					<td width="10%" align="right">联系电话：</td>
				    <td width="22%" align="left"><input name="address" type="text" readonly></td>
				</tr>
	
		</table>
		
		</div>
		<div id="p2" class="easyui-panel" style="width:900px;height:100px;"
				title="操作" collapsible="false"  closable="false">
			
			<table width="850px" >
				<tr>
					<td  colspan="2" align="center" style="padding-top:15px;">
						  <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="edit()">修改</a>
	                     
					</td>
					<td colspan="2" align="center" style="padding-top:15px;">
						 
	                    <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="close2()">取消</a>
					</td>
					
				</tr>
		  </table>
		  
		</div>
	    </form>
    	</div>
	    
    </div>	


</div>	</div>
</body>
</html>
