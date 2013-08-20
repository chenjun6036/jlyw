<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" pageEncoding="gbk" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>潜在客户信息管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
    <script >
    
    $(function(){
		$('#rid').combobox({
		valueField:'id',
		textField:'name',
		editable:false,
		onSelect:function(rec)
		{
			$('#insideContactorId').combobox('reload','/jlyw/CustomerContactorServlet.do?method=9&RegionId='+rec.id);
		//$('#industryId').val(rec.Id);
			$('#insideContactorId').combobox({
			valueField:'ContactorId',
			textField:'Name',
			editable:false,
			onSelect:function(rec)
			{
			},
			onLoadSuccess: function () 
			{
			$('#insideContactorId').combobox('clear');
				var data=$('#insideContactorId').combobox('getData');
				$('#insideContactorId').combobox('select',data[0].ContactorId);
				
			}
			});
			

		}
		});
	});
    $(function(){
		$('#industry1').combobox({
		url:'/jlyw/CrmServlet.do?method=11',
		valueField:'Id',
		textField:'Name',
		editable:false,
		onSelect:function(rec)
		{
		$('#industryId').val(rec.Id);
		}
		});
	});
	function cancel(){
		$('#frm_add_customer').form('clear');
		}
		
	function savereg(){
		$('#frm_add_customer').form('submit',{
			url: '/jlyw/CrmServlet.do?method=41',
			onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOK){
		   			$('#table1').datagrid('reload');
		   			cancel();
		   			}
		   		}
		});
	}
		
	function getBrief(){
		$('#brief').val(makePy($('#name').val()));
	}
		
	$(function(){
	$('#cla').combobox({
		multiple:'true'
		});
	$('#frm_add_customer').form('validate');
	});
		
	function check()
	{
	$('#checkCode').val($('#code').val());
	$('#frm_code').form('submit',{
		url: '/jlyw/CrmServlet.do?method=38',
		//onSubmit:function(){ return $('#frm_add_customer').form('validate');},
		 success:function(data){
		 var result = eval("("+data+")");
		 {
		 	if(result.IsOk==false)
		   	{
		   		$.messager.alert('提示',result.msg,'info');
		   		$('#code').val('');
		   	}
		  }
		   				
		 }
});
		
}
    </script>
	<script>
	$(function()
	{
		$('#region').combobox({
		onSelect: function(rec)
		{$('#regionId').val(rec.id);}
		});
	});
	function getBrief1(){$('#brief1').val(makePy($('#customerName').val()));}
	$(function(){
		$('#industry').combobox({
		url:'/jlyw/CrmServlet.do?method=11',
		valueField:'Id',
		textField:'Name',
		editable:false,
		onSelect:function(rec)
		{
		$('#industryId').val(rec.Id);
		}
		});
	});
	
$(function(){
	$("#customerName1").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
				$("#address").val(record.address);
				$('#customerId1').val(record.id);
				
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
					$('#customerName1').combobox('reload','/jlyw/CrmServlet.do?method=13&CustomerName='+newValue);
			}, 400);
		}
	});
	});

	

	
	///////////////////////////////////////////////////////////////////
	/////////////////////////////////**********************************

	function query()
	{
		$('#table1').datagrid('options').url='/jlyw/CrmServlet.do?method=14';
		$('#table1').datagrid('options').queryParams={'CustomerName':encodeURI($('#customerName1').combobox('getValue')),'CustomerId':/* encodeURI */($('#customerId1').val())};
		$('#table1').datagrid('reload');
	}

		function cancel1(){
			$('#frm_add_conInfo').form('clear');
		}
		
		function Add(){
			$('#frm_add_conInfo').form('submit',{
				url: '/jlyw/CrmServlet.do?method=7',
				onSubmit:function(){ return $('#frm_add_conInfo').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				if(result.IsOK)
		   					cancel();
		   		 }
			});
		}

		$(function(){
		$('#table1').datagrid({
				title:'潜在客户信息',
				width:750,
				height:450,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=15',
				//sortName:'Id',
				sortOrder:'asc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CustomerName',title:'单位名称',width:150,align:'center'},
					{field:'Address',title:'单位地址',width:150,align:'center'},
					{field:'Region',title:'地区',width:50,align:'center'},
					{field:'NameEn',title:'英文名称',width:100,align:'center'},
					{field:'From',title:'来源',width:50,align:'center',
					formatter:function(value,rowData,rowIndex)
					{
						if(value=='1'||value==1)
						{
							rowData['From']=1;
							return '电话';
						}
						else if(value=='2'||value==2)
						{
							rowData['From']=2;
							return '传真';
						}
						else if(value=='3'||value==3)
						{
							rowData['From']=3;
							return '来访';
						}
						else if(value=='4'||value==4)
						{
							rowData['From']=4;
							return '其它';
						}
					}},
					{field:'Intension',title:'合作意向',width:100,align:'center',
					formatter:function(value,rowData,rowIndex)
					{
						if(value=='1'||value==1)
						{
							rowData['Intension']=1;
							return '明确合作意向';
						}
						else if(value=='2'||value==2)
						{
							rowData['Intension']=2;
							return '初步联系、有意向';
						}
						else if(value=='3'||value==3)
						{
							rowData['Intension']=3;
							return '观望、意向不明确';
						}
						else if(value=='4'||value==4)
						{
							rowData['Intension']=4;
							return '明显拒绝';
						}
					}},
					{field:'Industry',title:'行业',width:80,align:'center'},
					{field:'IndustryId',title:'ID',width:80,align:'center',hidden:true},
					{field:'RegionId',title:'Id',width:80,align:'center',hidden:true},
					{field:'Brief1',title:'',width:80,align:'center',hidden:true},
					{field:'Id',title:'ID',width:80,align:'center',hidden:true}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table1').datagrid('getSelected');
						if(select){
						$('#editPotential').window('open');
						$('#form2').show();
						$('#form2').form('load',select);
						$('#form2').form('validate');
					}else{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'升级为正式客户',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table1').datagrid('getSelected');
							if(selected){
							
									$('#AddPotentialToFormer').window('open');
									$('#frm_add_customer').show();
									$('#frm_add_customer').form('load',selected);
									$('#name').val(selected.CustomerName);
									$('#brief').val(selected.Brief1);
									$('#del_id').val(selected.Id);
						}
						else{
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'导出',
						iconCls:'icon-save',
						handler:function(){
						ShowWaitingDlg("正在导出，请稍后......");
							$('#par').val(encodeURI($('#customerName1').combobox('getValue')));
							$('#frm_export').form('submit',{
								success:function(data){
									var result = eval("("+ data +")");
									if(result.IsOK)
									{
										$('#filePath').val(result.Path);
										$('#frm_down').submit();
										CloseWaitingDlg();
									}
									else
									{
										$.messager.alert('提示','导出失败，请重试！','warning');
										CloseWaitingDlg();
									}
								}
							});
					}
				}],
				onClickRow:function(rowIndex, rowData){
			
				}
			});
			});
			
		function Sub(){
		closed();
			$('#form2').form('submit',{
				url: '/jlyw/CrmServlet.do?method=16',
				onSubmit:function(){return $('#form2').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#table1').datagrid('reload');		
		   		}
			});
		}
		function closed()
		{
			$('#editPotential').dialog('close');
			$('#del').dialog('close');
			//$('#table1').datagrid('reload');
		}
		function del(){
			closed();
			$('#ff1').form('submit',{
				url:'/jlyw/CrmServlet.do?method=17',
				onSubmit:function(){
				//if($('#')){}
				},
				success:function(data){
					var result = eval("(" + data + ")");
			   		$.messager.alert('提示',result.msg,'info');
			   		if(result.IsOK)
			   		 closed();
			   		$('#table1').datagrid('reload');	
				}
			});
		}
		function cancel()
		{
		$('#query1').form('clear');
		}
		
	$(function()
{
	$("#payType").combobox(
	{
	onSelect:function()
	{
	    if($("#payType").combobox('getValue')=='2'||$("#payType").combobox('getValue')==2)
	    {
	    	$("#accountCycle").combobox({
	    	required:true
	    	});
	    	$('#frm_add_customer').form('validate');
	    }
	    else 
	    {
	    	$("#accountCycle").combobox({
	    	required:false
	    	});
	    	$('#frm_add_customer').form('validate');
	    }
	}
	});
}
);
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="潜在客户信息管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=3">
<input id="par" name="Par" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<form method="post" id="query1">
	<table style="width:750px;padding:20px;" class="easyui-panel" title="查询潜在客户信息">
		<tr>
			<td width="20%" align="right">单&nbsp;位&nbsp;名&nbsp;称：</td>
			<td align="left" >
			<input name="CustomerName1" id="customerName1" style="width:310px" class="easyui-combobox" ></input>
			<input id="customerId1" name="CustomerId1" type="hidden"/>
			</td>
			<td align="center">单位地址：
			</td>
			<td><input id="address" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
			<td></td>
			<td width="25%" align="right"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-search" onclick="query()">查询</a></td>
			<td width="25%" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-reload"  onclick="cancel1()">重置</a></td>
			<td ></td>
		</tr>
	</table>
	</form>
<table id="table1"></table>
	<br />
	 <div id="editPotential" class="easyui-window" title="修改" style="padding: 10px;width: 600px;height: 300px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
						<tr>
							<td align="right" style="width:100px;">单位名称：</td>
							<td align="left" ><input id="customerName" name="CustomerName" class="easyui-validatebox"  required="true" onchange="getBrief1()"/>
							<input id="Id" name="Id" type="hidden"/>
                    		</td>
                    		<td align="right" style="width:100px;">拼音简码：</td>
							<td align="left"><input id="brief1" name="Brief1" class="easyui-validatebox"  required="true"/>
							</td>
                    	</tr>
                    	
                        <tr>
                        	<td align="right">单位名称英文：</td>
							<td align="left"><input id="nameEn" name="NameEn" class="easyui-validatebox" required="true" />
                    		</td>
							<td align="right">地区：</td>
							<td align="left">
							<select id="region" name="Region" class="easyui-combobox" required="true" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2"  editable="false">
							</select><input id="regionId" name="RegionId" type="hidden"/>
							</td>
						</tr>
		
					<tr>
                        <td align="right">单位地址：</td>
							<td align="left"><input id="address" name="Address" class="easyui-validatebox"  required="true"/>
                   		</td>
					</tr>
						
					<tr>
                        <td align="right">潜在客户来源：</td>
							<td align="left">
							<select id="from" name="From" class="easyui-combobox"  required="true" panelHeight="auto">
							<option value="1">电话</option>
							<option value="2">传真</option>
							<option value="3">来访</option>
							<option value="4">其它</option>
							</select>
          					</td>
							<td align="right">合作意向：</td>
							<td align="left">
							<select id="intension" name="Intension" class="easyui-combobox" required="true" panelHeight="auto">
							<option value="1">明确合作意向</option>
							<option value="2">初步联系、有意向 </option>
							<option value="3">观望、意向不明确</option>
							<option value="4">明显拒绝</option>
							</select>
							</td>
						</tr>
						<tr>
 
							<td align="right">行业：</td>
							<td align="left">
							<select id="industry" name="Industry" class="easyui-combobox" required="true" panelHeight="auto" mode="remote" editable="false">
							</select>
							<input id="industryId" Name="IndustryId" type="hidden"/>
							</td>
						</tr>
						
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="Sub()">提交</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	<br />
	<div id="AddPotentialToFormer" class="easyui-window" title="确认" style="width:700px;height:750px;" iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false">
        
        <form id="frm_add_customer" method="post">
	<table >
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：<input type="hidden" id="del_id" name="Del_id"/></td>
			<td align="left"  style="width：30%"><input id="name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
            <td align="right" style="width：20%">拼音简码：</td>
			<td align="left"  style="width：30%"><input id="brief" name="Brief" type="text" class="easyui-validatebox"/>
			</td>
		</tr>
		<tr>
		<td align="right">付款方式：</td>
		<td><select style="width:145px" id="payVia" name="PayVia" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">现金</option>
		<option value="2">支票</option>
		<option value="3">付汇</option>
		<option value="4">POS机</option>
		<option value="5">其它</option>
		</select></td>
		<td align="right">付款类型：</td>
		<td><select style="width:145px" id="payType" name="PayType" class="easyui-combobox" required="true" panelHeight="auto" editable="false">
		<option value="1">检后结账</option>
		<option value="2">周期结账</option>
		<option value="3">预付款</option>
		<option value="4">其它</option>
		</select>
		</td>
		</tr>
		<tr><td align="right">结账周期：</td>
		<td><select style="width:145px" id="accountCycle" name="AccountCycle" class="easyui-combobox" editable="false" required="false">
		<option value="1">一个月</option>
		<option value="2">两个月</option>
		<option value="3">三个月</option>
		<option value="4">四个月</option>
		<option value="5">五个月</option>
		<option value="6">六个月</option>
		<option value="7">七个月</option>
		<option value="8">八个月</option>
		<option value="9">九个月</option>
		<option value="10">十个月</option>
		<option value="11">十一个月</option>
		<option value="12">十二个月</option>
		</select></td>
		<td align="right">联&nbsp;系&nbsp;人：</td>
			<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr height="30px">
			<td align="right">单位类型：</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:145px" required="true" panelHeight="auto" editable="false">
					<option value='0'>国有企业</option>
					<option value='1'>外资企业</option>
					<option value='2'>中外合资企业</option>
					<option value='3'>民营企业</option>
				</select>
			</td>
			<td align="right">单位地址：</td>
			<td align="left"><input id="addr" name="Address" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		
		<tr  height="30px">
			<td align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
			<td align="left">
				<select id="rid" name="RegionId" class="easyui-combobox" style="width:145px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" ></select>
			</td>
			<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left" ><input id="zcd" name="ZipCode" type="text" class="easyui-validatebox" required="true"/></td>
		</tr>
		<tr  height="30px">
			<td align="right">内部联系人：</td>
			<td align="left">
				<select id="insideContactorId" name="InsideContactorId" panelHeight="auto" class="easyui-combobox" style="width:145px" required="true" editable="false" ></select>
			</td>
			<td align="right">角&nbsp;&nbsp;&nbsp;&nbsp;色：</td>
			<td align="left" ><select style="width:145px" id="role" name="Role" type="text" class="easyui-combobox" required="true" panelHeight="auto">
			<option value="1" selected="selected">A</option>
			<option value="2">B</option>
			</select></td>
		</tr>
		<tr height="3px;"><td align="left" colspan="5" >-------------------------------------------------------------------------------------------------------------</td>
		</tr>
		<tr height="30px">
			
			<td align="right">单位代码：</td>
			<td align="left" ><input id="code" name="Code" type="text" class="easyui-validatebox" onchange="check()"/></td>
			<td align="right">地址英文：</td>
			<td align="left"><input id="addren" name="AddressEn" type="text" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">英文名称：</td>
			<td align="left" colspan="3"><input id="nameEn" name="NameEn" type="text" style="width:486px" class="easyui-validatebox"/></td>
		</tr>
		<tr height="30px">
			<td align="right">联系电话：</td>
			<td align="left"><input id="tel" name="Tel" type="text" class="easyui-validatebox"/></td>
			<td align="right">单位传真：</td>
			<td align="left"><input id="fax" name="Fax" type="text" class="easyui-validatebox" /></td>
		</tr>
		<tr>
			
			<td align="right">联系人号码1：</td>
			<td align="left"><input id="contactortel1" name="ContactorTel1" type="text" class="easyui-validatebox"/></td>
			<td align="right">联系人号码2：</td>
			<td align="left"><input id="contactortel2" name="ContactorTel2" type="text" /></td>
		</tr>
		<tr>
			
		</tr>
		<tr height="30px">
			<td align="right">企业分类：</td>
			<td align="left">
				<input id="cla" name="Classification" class="easyui-combobox" style="width:145px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/>
			</td>
			<td align="right">单位状态：</td>
			<td align="left">
				<select id="sta" name="Status" class="easyui-combobox" style="width:145px" panelHeight="auto" editable="false">
						<option value='0' selected>正常</option>
						<option value='1'>注销</option>
				 </select></td>
		</tr>
		<tr height="30px">
        	<td align="right">开户银行：</td>
			<td align="left"><input id="accBank" name="AccountBank" type="text" class="easyui-validatebox" /></td>
			<td align="right">账&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
			<td align="left"><input id="acc" name="Account" type="text" class="easyui-validatebox" /></td>
        </tr>
        <tr>
			<td align="right">信用额度：</td>
			<td align="left"><input id="crdamount" name="CreditAmount" type="text" class="easyui-numberbox" /></td>
			<td align="right">服务费用限值：</td>
		<td><input class="easyui-numberbox" id="serviceFeeLimitation" name="ServiceFeeLimitation" /></td>
   		</tr>
		
		<tr>
		
		<td align="right">客户价值等级：</td>
		<td><select style="width:145px" id="customerValueLevel" name="CustomerValueLevel" class="easyui-combobox"  editable="false">
		<option value="1">1级</option>
		<option value="2">2级</option>
		<option value="3">3级</option>
		<option value="4">4级</option>
		<option value="5">5级</option>
		<option value="6">6级</option>
		<option value="7">7级</option>
		<option value="8">8级</option>
		<option value="9">9级</option>
		<option value="10">10级</option>
		
		</select>
		
		</td>
		<td align="right">企业等级：</td>
		<td>
		<select style="width:145px" class="easyui-combobox" id="customerLevel" name="CustomerLevel"  panelHeight="auto" ediable="false">
		<option value="1">VIP客户</option>
		<option value="2">重点客户</option>
		<option value="3">重要客户</option>
		<option value="4">一般客户</option>
		<option value="5">特殊客户</option>
		</select>
		
		</td >
		</tr>
		<tr>
		
		<td align="right">变动趋势：</td>
		<td><select style="width:145px" id="trendency" name="Trendency" class="easyui-combobox"  editable="false">
		<option value="1">升级可能</option>
		<option value="2">降级可能</option>
		<option value="3">维持现状</option>
		</select></td>
		<td align="right">所在行业：</td>
		<td><select style="width:145px" id="industry1" name="Industry" class="easyui-combobox" ></select>
		<input type="hidden" id="industryId" name="IndustryId"/></td>
		</tr>
		<tr>
		<td align="right">产值期望值：</td>
		<td><input class="easyui-numberbox" id="outputExpectation" name="OutputExpectation" /></td>
		</tr>
		<tr>
		<td align="right">忠诚度：</td>
		<td><input name="Loyalty" class="easyui-numberbox"></input></td>
		<td align="right">满意度：</td>
		<td><input name="Satisfaction" class="easyui-numberbox"></input></td>
		</tr>
		<tr height="30px">
			<td align="right">下厂要求：</td>
			<td align="left"  colspan="4"><textarea id="fldema" name="FieldDemands" cols="56" rows="1" ></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">特殊要求：</td>
			<td align="left"  colspan="4"><textarea id="spcdema" name="SpecialDemands" cols="56" rows="1"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">证书要求：</td>
			<td align="left"  colspan="4"><textarea id="cerdema" name="CertificateDemands" cols="56" rows="1"></textarea> </td>
		</tr>
		<tr height="30px">
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left" colspan="4"><textarea id="rmk" name="Remark" cols="56" rows="1"></textarea> </td>
		</tr>
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">添加</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="cancel()">重置</a></td>
		</tr>
	</table>
	</form>
        </div>
</DIV>
</DIV>
</body>
</html>
