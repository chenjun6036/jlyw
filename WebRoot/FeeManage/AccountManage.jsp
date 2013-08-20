<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>账户管理</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		$("#CustomerName").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
				$("#Name11").val(record.name);
				
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
				$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+encodeURI(newValue));
			}
		});
		    var lastIndex;		
			$('#CustomerAccount').datagrid({
			    width:900,
				height:500,
				title:'账户信息',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/CustomerAccountServlet.do?method=0',
				sortName: 'Id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
				
				columns:[[
					{field:'CustomerName',title:'单位名称',width:250,align:'center'},	
					{field:'HandleType',title:'操作类型',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['HandleType']=0;
							    return "充值";
							}
							else
							{
								rowData['HandleType']=1;
								return "消费";
							}
							
						}},
					{field:'Amount',title:'金额',width:100,align:'center'},
					{field:'HandleTime',title:'操作时间',width:180,align:'center'},	
					{field:'HandlerName',title:'经办人姓名',width:80,align:'center'},	
					{field:'Remark',title:'备注',width:120,align:'center'}			
                ]],
				pagination:true,
				rownumbers:true,
				toolbar:"#blance-toolbar"
			});

		});
		function query()
		{
			$('#CustomerAccount').datagrid('options').url='/jlyw/CustomerAccountServlet.do?method=0';
			$('#CustomerAccount').datagrid('options').queryParams={'queryname':encodeURI($('#CustomerName').combobox('getValue'))};
			$('#CustomerAccount').datagrid('clearSelections');
			$('#CustomerAccount').datagrid('reload');
			
			$("#Name22").val($('#CustomerName').combobox('getValue'));
			$('#Customer').form('submit',{
				url: '/jlyw/CustomerAccountServlet.do?method=2',
				onSubmit:function(){ return $('#Customer').form('validate');},
		   	    success:function(data){
		   	    	var result = eval("("+data+")");
		   			$("#Customer_Balance").val(result.balance);
		   		}
			});
		}
		function ok()
		{
		    $("#Name11").val($('#CustomerName').combobox('getValue'));
			$('#Account').form('submit',{
				url: '/jlyw/CustomerAccountServlet.do?method=1',
				onSubmit:function(){ return $('#Account').form('validate');},
		   	    success:function(data){
		   		   var result = eval("("+data+")");
		   		   $("#Customer_Balance").val(result.balance);
		   		   $('#CustomerAccount').datagrid('reload');
		   		     $.messager.alert('提示',result.msg,'info');
		   		}
			});
			
		}
		function cancel(){
		   $('#Remark').val(" ");
		   $('#Amount').val(" ");
		}
	</script>
</head>

<body>
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="账户管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

<div  style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:80px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="Customer" method="post">
		<input name="Name22" id="Name22" type="hidden"  >
			<table width="850px" id="table1">
				<tr >
	
					<td width="10%" align="right">委托单位名称：</td>
					<td width="32%" align="left">
						<input id="CustomerName"  name="CustomerName"  style="width:200px;"  panelHeight="auto" >
					</td>
					
					<td width="40%"  align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				</tr>
				
			</table>
		</form>
		</div>

		
      <div style="width:900px;height:500px;">
	     <table id="CustomerAccount" iconCls="icon-tip" width="900px" height="500px" ></table>
	  </div>
	  <br />
	  <div id="p2" class="easyui-panel" style="width:900px;padding:10px;"
				title="操作区" collapsible="false"  closable="false">
			<form id="Account" method="post">
			 <input name="Name11" id="Name11" type="hidden"  >
			 <input name="Type" type="hidden" value="0">
			<table  width="855" style="margin-left:20px">
		
			  <tr >
			  		<td width="7%" style="padding-top:15px;" align="right" >备注：</td>
					<td width="21%"  align="left" style="padding-top:15px;">
					   <textarea id="Remark" style="width:350px;height:80px"  name="Remark" class="easyui-validatebox"  ></textarea>
					</td>	
					<td width="8%" style="padding-top:15px;" align="right" >充值金额:</td>
					<td width="21%" style="padding-top:15px;" align="left">
					   <input id="Amount" name="Amount" type="Amount" class="easyui-numberbox" precision="2" required="true" >&nbsp;元
					</td>
						
			  </tr>
			  
			  <tr >
				  <td  colspan="2"  align="center"  style="padding-top:15px;"> 
				     <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="ok()">充值</a>
				  </td>
				  <td  colspan="2"  align="left" style="padding-top:15px;">
				     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="cancel()">重置</a>
				  </td>
			  </tr>
				
		  </table>
		  </form>
		</div>

</div>


</DIV></DIV>
<div id="blance-toolbar" >
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="text-align:left;">
					<label>该单位账户余额：</label><input type="text" id="Customer_Balance" name="Customer_Balance" style="width:120px" readonly="readonly" />&nbsp;
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
