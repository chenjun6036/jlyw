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
			$('#queryname').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'name',
				textField:'name',
				onSelect:function(){},
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
								var newValue = $('#queryname').combobox('getText');
								$('#queryname').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);

					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
		
			$('#cla').combobox({
				multiple:'true'
			});
			
			$('#queryClassi').combobox({
				multiple:'true'
			});
			
			$('#InsideContactor').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
			$('#queryInsideContactor').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
			
			$('#table2').datagrid({
				title:'单位信息',
				width:900,
				height:500,
                nowrap: false,
                striped: true,
                singleSelect:true, 
				url:'/jlyw/CrmServlet.do?method=25',
				sortName:'Id',
				sortOrder:'desc',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'单位名称',width:200,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'单位名称英文',width:100,align:'center'},
					{field:'Brief',title:'拼音简码',width:80,align:'center'},
					{field:'RegionId',title:'地区',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						rowData['RegionId']=value;
						var datas = $('#rid').combobox('getData');
						for(var i = 0; i < datas.length; i++)
						{
							if(datas[i].id==value)
								return datas[i].name;
						}
					}},
					{field:'CustomerTypeName',title:'单位类型',width:80,align:'center'},
					{field:'CustomerType',hidden:true},
					{field:'Code',title:'单位代码',width:70,align:'center'},
					{field:'Address',title:'单位地址',width:80,align:'center'},
					{field:'AddressEn',title:'单位地址英文',width:100,align:'center'},
					{field:'Tel',title:'单位电话',width:60,align:'center'},
					{field:'Fax',title:'单位传真',width:80,align:'center'},
					{field:'ZipCode',title:'邮编',width:60,align:'center'},
					{field:'Status',title:'单位状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return '<span style="color:red">注销</span>';
							}
							else 
							{
								rowData['Status']=-1;
							    return "未知";
							}
							
						}},
					{field:'CancelDate',title:'注销时间',width:100,align:'center'},
					{field:'CancelReason',title:'注销原因',width:100,align:'center'},
					{field:'Balance',title:'单位余额',width:80,align:'center'},
					{field:'AccountBank',title:'开户银行',width:90,align:'center'},
					{field:'Account',title:'账号',width:120,align:'center'},
					{field:'InsideContactor',title:'内部联系人',width:90,align:'center'},
					{field:'Classification',title:'企业分类',width:125,align:'center'},
					{field:'FieldDemands',title:'下厂要求',width:80,align:'center'},
					{field:'CertificateDemands',title:'证书要求',width:80,align:'center'},
					{field:'SpecialDemands',title:'特殊要求',width:80,align:'center'},
					{field:'CreditAmount',title:'信用额度',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,align:'center'},
					{field:'Contactor',title:'外部联系人',width:80,align:'center'},
					{field:'ContactorTel1',title:'手机号码1',width:80,align:'center'},
					{field:'ContactorTel2',title:'手机号码2',width:80,align:'center'},
					
					////////////////////////////////////////////////////////////////
					{field:'PayVia',title:'支付方式',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['PayVia']=1;
							return '现金';
						}
						else if(value==2||value=='2')
						{
							rowData['PayVia']=2;
							return '支票';
						}
						else if(value==3||value=='3')
						{
							rowData['PayVia']=3;
							return '付汇';
						}
						else if(value==4||value=='4')
						{
							rowData['PayVia']=4;
							return 'POS机';
						}
						else 
						{
							rowData['PayVia']=5;
							return '其它';
						}
					}
					},
					{field:'PayType',title:'付款类型',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['PayType']=1;
							return '检后结账';
						}
						else if(value==2||value=='2')
						{
							rowData['PayType']=2;
							return '周期结账';
						}
						else if(value==3||value=='3')
						{
							rowData['预付款']=3;
							return '付汇';
						}
						else if(value==4||value=='4')
						{
							rowData['其它']=4;
							return 'POS机';
						}
						
					}},
					{field:'AccountCycle',title:'结账周期（月）',width:100,align:'center'},
					{field:'CustomerValueLevel',title:'客户价值等级',width:100,align:'center'},
					{field:'CustomerLevel',title:'企事业等级',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['CustomerLevel']=1;
							return 'VIP客户';
						}
						else if(value==2||value=='2')
						{
							rowData['CustomerLevel']=2;
							return '重点客户';
						}
						else if(value==3||value=='3')
						{
							rowData['CustomerLevel']=3;
							return '重要客户';
						}
						else if(value==4||value=='4')
						{
							rowData['CustomerLevel']=4;
							return '一般客户';
						}
						else if(value==5||value=='5')
						{
							rowData['CustomerLevel']=5;
							return '特殊客户';
						}
					}},
					{field:'Trendency',title:'变动趋势',width:100,align:'center',
					formatter:function(value,rowData,index)
					{
						if(value==1||value=='1')
						{
							rowData['Trendency']=1;
							return '升级可能';
						}
						else if(value==2||value=='2')
						{
							rowData['Trendency']=2;
							return '降级可能';
						}
						else if(value==3||value=='3')
						{
							rowData['Trendency']=3;
							return '维持现状';
						}
					
					}},
					{field:'OutputExpectation',title:'产值期望值',width:100,align:'center'},
					{field:'ServiceFeeLimitation',title:'服务费用限值',width:100,align:'center'},
					{field:'Industry',hidden:true},
					{field:'IndustryName',title:'所在行业',width:100,align:'center'},
					////////////////////////////////////////////////////////////////
					{field:'Modificator',title:'修改者',width:100,align:'center'},
					{field:'ModifyDate',title:'最近修改日期',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#frm_edit_customer').show();
						$('#edit_id').val(select.Id);
						$('#frm_edit_customer').form('load',select);
						$('#cla').combobox('setValue',select.Classification);
						$('#customerLevel').combobox('setValue',select.CustomerLevel);
						//if($('#payVia').combobox('getValue').trim()=="")
						
						$('#frm_edit_customer').form('validate');
					}else{
						$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
						text:'注销',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected){
								$('#del').window('open');
								$('#ff1').show();
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
								myExport();
						}
				}],
				onClickRow:function(rowIndex, rowData){
					$('#contactors').datagrid('options').url='/jlyw/CustomerContactorServlet.do?method=2';
					$('#contactors').datagrid('options').queryParams={'CustomerId':rowData.Id};
					$('#CustomerId').val(rowData.Id);
					$('#contactors').datagrid('reload');
				}
			});
			
			$('#contactors').datagrid({
				title:'外部联系人信息',
				width:900,
				height:300,
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
					{field:'Name',title:'姓名',width:200,align:'center'},
					{field:'Cellphone1',title:'联系电话1',width:100,align:'center'},
					{field:'CurJob',title:'职业',width:100,align:'center'},
					{field:'Cellphone2',title:'联系电话2',width:100,align:'center'},
					{field:'Birthday',title:'生日',width:100,align:'center'},
					{field:'Email',title:'电子邮箱',width:80,align:'center'},
					{field:'LastUse',title:'最后使用时间',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'新增',
						iconCls:'icon-add',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select==null){ 
								$.messager.alert('提示','请选择一个委托单位','info');
								return;
							}
							$('#edit_con').window('open');
							$('#form2').show();
							$('#form2').form('clear');
							var customer = $('#table2').datagrid('getSelected');
							$('#CustomerId').val(customer.Id);
							$('#edit_con').panel({title:"新增"});
						}
				},'-',{
						text:'修改',
						iconCls:'icon-edit',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select){
							$('#edit_con').window('open');
							$('#form2').show();
							
							$('#form2').form('load',select);
							$('#edit_con').panel({title:"修改"});
							$('#form2').form('validate');
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
						text:'注销',
						iconCls:'icon-remove',
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
							$('#curJob2').val("");
							$('#formerJob').val(select.CurJob);
							//$('#edit_con').panel({title:"修改"});
							$('#frm_log_off_con').form('validate');
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
							}
						/* 
						text:'注销',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#contactors').datagrid('getSelected');
							if(select){
								$.messager.confirm('提示','确认删除吗？',function(r){
									if(r){
										$.ajax({
										type:'POST',
										url:'/jlyw/CustomerContactorServlet.do?method=3',
										data:"id="+select.Id,
										dataType:"html",
										success:function(data){
											$('#contactors').datagrid('reload');
										}
									});
									}
								});	
							}
							else
							{
								$.messager.alert('提示','请选择一行数据','warning');
							} */
					}
				}]
			});
			
			$('#queryname').combobox('setValue',"");
			$('#queryInsideContactor').combobox('setValue',"");
		});
		
		function closed(){
			$('#edit').dialog('close');
			$('#del').dialog('close');
			$('#edit_con').dialog('close');
			$('#log_off_con').dialog('close');
			
		}
		
		function SubmitContactor(){
			$('#form2').form('submit',{
				url: '/jlyw/CustomerContactorServlet.do?method=1',
				onSubmit:function(){return $('#form2').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
		   			$('#contactors').datagrid('reload');		
		   		}
			});
		}
		
		function query()
		{
			$('#table2').datagrid('options').url='/jlyw/CrmServlet.do?method=25';
			$('#table2').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').combobox('getText')),'queryZipCode':encodeURI($('#queryZipCode').val()),'queryAddress':encodeURI($('#queryAddress').val()),'queryTel':encodeURI($('#queryTel').val()),'queryInsideContactor':encodeURI($('#queryInsideContactor').combobox('getText')),'queryClassi':encodeURI($('#queryClassi').combobox('getValues')),'queryCredit':encodeURI($('#queryCredit').val()),'queryContactor':encodeURI($('#queryContactor').val()),'queryContactorTel':encodeURI($('#queryContactorTel').val())};
			$('#table2').datagrid('reload');
			
			//$('#queryname').combobox('setValue',"");
			$('#queryInsideContactor').combobox('setValue',"");
		}
		
		function del(){
			$('#ff1').form('submit',{
				url:'/jlyw/CrmServlet.do?method=27',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
			   		$.messager.alert('提示',result.msg,'info');
			   		if(result.IsOK)
			   		 closed();
			   		$('#table2').datagrid('reload');	
				}
			});
		}
		
		function getBrief(){
			$('#brief').val(makePy($('#name').val()));
		}
		
		function myExport(){
			ShowWaitingDlg("正在导出，请稍后......");
			$('#paramsStr').val(JSON.stringify($('#table2').datagrid('options').queryParams));
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
		
		function savereg(){
		$('#frm_edit_customer').form('submit',{
			url: '/jlyw/CrmServlet.do?method=26',
			onSubmit:function(){ return $('#frm_edit_customer').form('validate');},
		   	success:function(data){
		   		var result = eval("("+data+")");
		   		$.messager.alert('提示',result.msg,'info');
		   		if(result.IsOk){
		   			$("#edit").dialog('close');
		   			$('#table2').datagrid('reload');
		   			cancel();
		   			}
		   		}
		});
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
	    	$('#frm_edit_customer').form('validate');
	    }
	    else 
	    {
	    	$("#accountCycle").combobox({
	    	required:false
	    	});
	    	$('#frm_edit_customer').form('validate');
	    }
	}
	});
}
);
function Resets()
{
$('#query').form('clear');
}

function Reset()
{
$('#frm_edit_customer').form('clear');
}


function LogOffContactor(){
			$('#frm_log_off_con').form('submit',{
				url: '/jlyw/CustomerContactorServlet.do?method=3',
				onSubmit:function(){return $('#frm_log_off_con').form('validate');},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   				closed();
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
			<br /><form id="query">
			<table id="table1" style="width:900px">
            
				<tr>
					<td align="right">单位名称：</td>
				  	<td align="left"><input class="easyui-combobox" style="width:152px" id="queryname" name="queryName"  panelHeight="150px"></input></td>
                    <td align="right">邮政编码：</td>
				  	<td align="left"><input class="easyui-validate" id="queryZipCode" name="queryZipCode"></input></td>
                    <td align="right">单位地址：</td>
				  	<td align="left"><input class="easyui-validate" id="queryAddress" name="queryAddress"></input></td>
				</tr>
                <tr>
					<td align="right">电话或传真：</td>
				  	<td align="left"><input class="easyui-validate" id="queryTel" name="queryTel"></input></td>
                    <td align="right">内部联系人：</td>
				  	<td align="left"><input id="queryInsideContactor" name="queryInsideContactor" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    <td align="right">分类：</td>
				  	<td align="left"><input id="queryClassi" name="queryClassi" class="easyui-combobox" style="width:152px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=11"/></td>
                     <td width="100"><a href="javascript:void(0)" onclick="Resets()" class="easyui-linkbutton" iconCls="icon-reload">重置</a></td>
				</tr>
                <tr>
					<td align="right">信用度：</td>
				  	<td align="left"><input class="easyui-numberbox" id="queryCredit" name="queryCredit"></input></td>
                    <td align="right">联系人：</td>
				  	<td align="left"><input id="queryContactor" name="queryContactor" class="easyui-validatebox"/></td>
                    <td align="right">联系人电话：</td>
				  	<td align="left"><input id="queryContactorTel" name="queryContactorTel" class="easyui-validatebox"/></td>
					<td width="100"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
				</tr>
               
			</table> </form>
		</div>
		

		<table id="table2" style="height:500px; width:900px"></table>
        <br/>
        <table id="contactors" style="height:300px; width:900px"></table>
		

		<div id="edit" class="easyui-window" title="修改" style="padding: 10px;width: 900;height: 500;"
        iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_customer" method="post">
	<table >
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：<input type="hidden" id="edit_id" name="Edit_id"/></td>
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
		<td align="right">外部联系人：</td>
			<td align="left"><input id="con" name="Contactor" type="text" style="border:none;" class="easyui-validatebox" readonly="readonly" /></td>
		</tr>
		
		<tr height="30px">
			<td align="right">单位类型：</td>
			<td align="left" >
				<select id="customerType" name="CustomerType" class="easyui-combobox" style="width:145px" required="true" panelHeight="auto" valueField="id" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=29" editable="false">
					
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
						<option value='0'>正常</option>
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
		<td><select style="width:145px" id="industry" name="Industry" class="easyui-combobox" ></select>
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
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">修改</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-reload" name="Reset" href="javascript:void(0)" onclick="Reset()">重置</a></td>
		</tr>
	</table>
	</form>
		</div>
		
        <div id="edit_con" class="easyui-window" title="修改" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
                    <input id="CustomerId" name="CustomerId" type="hidden"/>
						<tr>
							<td align="right">姓名：</td>
							<td align="left" ><input id="Name" name="Name" class="easyui-validatebox" required="true"/></td>
							<td align="right">联系电话1：</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                        	<td align="right">职业：</td>
							<td align="left"><input id="curJob" name="CurJob" class="easyui-validatebox"/></td>
							
							<td align="right">联系电话2：</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" class="easyui-validatebox"/></td>
						</tr>
						 <tr>
							
							<td align="right">生日：</td>
							<td align="left"><input id="birthday" name="Birthday" class="easyui-datebox" editable="false"/></td>
							<td align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;箱：</td>
							<td align="left"><input id="Email" name="Email" class="easyui-validatebox"/></td>
						</tr>	
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="SubmitContactor()">提交</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
        
		<div id="del" class="easyui-window" title="注销" style="width:280px;height:130px;" 
    iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false">
    <form id="ff1" method="post">
    	<table>
    	<input id="del_id" name="del_id" type="hidden"/>
    	<tr>
    		<td>注销原因：</td>
    		<td><input id="reason" class="easyui-combobox" name="reason" url="/jlyw/ReasonServlet.do?method=0&type=22" style="width:170px;" valueField="name" textField="name" panelHeight="auto" /></td>
    	</tr>
    	<tr height="40px">	
			<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="del()">注销</a></td>
			<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">取消</a></td>
		</tr>
    	</table>
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
        </div>
    </DIV>
    </DIV>
</body>
</html>
