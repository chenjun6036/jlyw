<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>协议查询</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
     <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
	 <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
     <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
     <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
     <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
     <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
     <script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
     <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../../JScript/json2.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
	<script type="text/javascript" src="../../JScript/NumberChanger.js"></script>
	<script>
		$(function (){
			$('#file_upload').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					//'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../../uploadify/selectfiles.png',
					//'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					//'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
						$('#uploaded_file_table').datagrid('reload');
					},
					onAllComplete: function(event,data){
					}
				});
				
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
			$('#uploaded_file_table').datagrid('reload');
			
			$('#Search_Appli').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				valueField:'standardname',
				textField:'standardname',
				onSelect:function(){},
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
					$(this).combobox('reload','/jlyw/ApplianceStandardNameServlet.do?method=0&ApplianceStandardName='+newValue);
					}
			});
			$('#queryCustomer').combobox({
				valueField:'name',
				textField:'name',
				onChange:function(newValue,oldValue){
					var allData = $(this).combobox('getData');
					if(allData !=null && allData.length >0){
						for(var i=0;i<allData.length; i++){
							if(newValue == allData[i].name){
								return false;
							}
						}
					}
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){
						var newValue = $('#queryCustomer').combobox('getText');
						$('#queryCustomer').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					},700);
				}
			});
			$('#CustomerName').combobox({
				valueField:'name',
				textField:'name',
				onChange:function(newValue,oldValue){
					var allData = $(this).combobox('getData');
					if(allData !=null && allData.length >0){
						for(var i=0;i<allData.length; i++){
							if(newValue == allData[i].name){
								return false;
							}
						}
					}
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){
						var newValue = $('#CustomerName').combobox('getText');
						$('#CustomerName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					},700);
				}
			});
			
			$('#table2').datagrid({
				title:'协议信息查询',
				height:400,
				width:900,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/DealServlet.do?method=3',
				idField:'DealCode',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'DealCode',title:'编号',width:80,align:'center'},
					{field:'CustomerName',title:'协议单位名称',width:160,align:'center'},
					{field:'Contactor',title:'协议单位联系人',width:100,align:'center'},
					{field:'ContactorTel',title:'联系人电话',width:80,align:'center'},
					{field:'Signer',title:'协议签署人',width:80,align:'center'},
					{field:'SignDate',title:'协议签署时间',width:80,align:'center'},
					{field:'Validity',title:'协议有效期',width:80,align:'center'},
					{field:'Creator',title:'编辑人',width:80,align:'center'},
					{field:'CreateDate',title:'编辑时间',width:80,align:'center'},
					{field:'Status',title:'状态',width:60,align:'center',sortable:true,
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return '<span style="color:red;">注销</span>'
;
							}
						}
					},		
					{field:'Remark',title:'备注',width:120,align:'center'},
					{field:'Detail',title:'条目详情',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							$('#quotationId').val(rowData.num);
							return "<a href='javascript:void(0)' class='easyui-linkbutton' iconCls='icon-search' onclick=\"item_Manage('" + rowData.DealCode + "');\" >查看</a>";  
						}
						
					}
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//zhengchang
						return 'color:#000000'
					}else if(rowData.Status == 1){	//zhuxiao
						return 'color:#FF0000';	
					}else{
						return 'color:#000000';
					}
				},
				toolbar:[{
					text:'协议信息修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#quotation_edit_form').form('load',select);
							
							$('#quotation_edit').window('open');	
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'协议条目修改',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
							$('#dealCode').val(select.DealCode);
							$('#table4').datagrid('options').url='/jlyw/DealServlet.do?method=4';
							$('#table4').datagrid('options').queryParams={'DealCode':encodeURI($('#dealCode').val())};
							$('#table4').datagrid('reload');
							$('#item_edit').window('open');	
						}else{
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'注销',
					iconCls:'icon-remove',
					handler:function(){
							var rows = $('#table2').datagrid('getSelections');
							if(rows.length!=0)
							{
								$.messager.confirm('警告','确认注销吗？',function(r){
								if(r){
									for(var i=rows.length-1; i>=0; i--){
										$.ajax({
											type:'POST',
											url:'/jlyw/DealServlet.do?method=5',
											data:'DealCode='+rows[i].DealCode,
											dataType:"json",
											success:function(data, textStatus){
												alert(data.msg);
											}
										});
									}
									$('#table2').datagrid('reload');
								}
								});
							}else{
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				}],
				onClickRow:function(rowIndex,rowData){
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.Attachment};
					$('#uploaded_file_table').datagrid('reload');
				}
			});
			
		});
		
		//添加条目
		$(function(){
			$('#table_appli').datagrid({
		//		title:'受检器具信息',
				width:750,
				height:450,
				singleSelect:true,
				fit: false,
				nowrap: true,
				striped: true,
				rownumbers:false,
//				url:'/jlyw/QuotationServlet.do?method=0',
				remoteSort: false,
				frozenColumns:[[
					{field:'ck',checkbox:true},
					{field:'TargetApplianceId',title:'受检器具编号',width:100,sortable:true,align:'center'}
				]],
				columns:[[
					{field:'StandardNameName',title:'器具标准名称',width:100,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Fee',title:'标准费用',width:80,align:'center'}
				]],
				pagination:true,
				toolbar:"#table_appli-search-toolbar"
			});
		});
		
		//协议条目
		$(function(){
			var lastIndex;
			$('#table4').datagrid({
				width:780,
				height:430,
             	singleSelect:true, 
                nowrap: false,
                striped: true,
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'StandardNameId',title:'器具标准名称ID',width:120,align:'center'},
					{field:'StandardName',title:'器具标准名称',width:120,align:'center',sortable:true},
					{field:'Model',title:'型号规格',width:120,align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:120,align:'center'},
					{field:'Range',title:'测量范围',width:120,align:'center'},
					{field:'AppFactoryCode',title:'出厂编号',width:80,align:'center'},
					{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
					{field:'DealPrice',title:'协议检测费',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,editor:'text',align:'center'}
				]],
				rownumbers:true,
				pagination:false,
				toolbar:[{
					text:'删除条目',
					iconCls:'icon-remove',
					handler:function(){
						var select = $('#table4').datagrid('getSelected');
						if(!select){
							$.messager.alert('提示','请选择需要移除的条目！','info');
							return false;
						}
						else{
							var result = confirm("确定要移除这些条目吗？");
							if(result == false){
								return false;
							}
							$.ajax({
									type:'POST',
									url:'/jlyw/DealServlet.do?method=8',
							 		data:'Id='+select.Id,
									dataType:"json",
									success:function(data, textStatus){
										alert(data.msg);
									}
							});
							$('#table4').datagrid('reload');
						}
					}
				},'-',{
					text:'修改条目',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table4').datagrid('getSelected');
						if(select){
							$('#quo_edit').window('open');
							$('#form1').show();
							
							//
							$('#Model1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=1&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
							});
							//
							$('#Range1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
							});
							//
							$('#Accuracy1').combobox({
								url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName=' + encodeURI(select.StandardName),
								valueField:'Name',
								textField:'Name'
							});
							
							$('#form1').form('load',select);
					}else{
						$.messager.alert('警告','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'添加条目',
					iconCls:'icon-add',
					handler:function(){	
						$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
						$('#table_appli').datagrid('reload');
						$('#item_add').window('open');
						$('#form3').show();
					}
				},'-',
				{
					text:'确认修改',
					iconCls:'icon-ok',
					handler:function(){
						var result = confirm("确定要修改吗？");
						if(result == false){
							return false;
						}
						var rows = $('#table4').datagrid('getRows');
						if(rows.length == 0){
							$.messager.alert('提示','协议条目为空！','info');
							return false;
						}
						
						$('#form2').form('submit',{
							url:'/jlyw/DealServlet.do?method=6',
							onSubmit:function(){
								$("#Item").val(JSON.stringify(rows));
								
								return $('#form2').form('validate');
							},
							success:function(data){
								var result = eval("("+data+")");
								alert(result.msg);
								if(result.IsOK){
									cancel();
									$('#table4').datagrid('reload');
								}
							}
						});						
					}	
				}]
			});
		})	
		

		//根据协议编号和询价单位查询协议
		function query(){
			$('#table2').datagrid('options').url='/jlyw/DealServlet.do?method=3';
			$('#table2').datagrid('options').queryParams={'DealCode':encodeURI($('#queryDealCode').val()),'Customer':encodeURI($('#queryCustomer').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getText')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getText')),'Status':encodeURI($('#queryStatus').val())};
			$('#table2').datagrid('reload');
			$('#queryDealCode').val('');
		}
		
		//修改协议条目
		function edit(){
			if($('#DealPrice1').val()==''){
				$.messager.alert('提示','请填写协议价格！','info');
			}
			else{
				var row = $('#table4').datagrid('getSelected');
				var index= $('#table4').datagrid('getRowIndex',row);
										 			
				$('#table4').datagrid('updateRow',{
					index:index,
					row:{
						Model:$('#Model1').combobox('getText'),
						Accuracy:$('#Accuracy1').combobox('getText'),
						Range:$('#Range1').combobox('getText'),
						AppFactoryCode:$('#AppFactoryCode1').val(),
						AppManageCode:$('#AppManageCode1').val(),
						Manufacturer:$('#Manufacturer1').val(),
						StandardNameId: row.StandardNameId,
						StandardName:row.StandardName,
						DealPrice:$('#DealPrice1').val(),
						Remark:$('#Remark1').val()
					}
				});							
				$('#quo_edit').window('close');			
				
			}
		}
		
		//取消协议修改
		function cancel(){
			$('#form1').form('clear');
			$('#quo_edit').window('close');
		}
		
		function item_Manage(number){
			$('#dealCode').val(number);
			$('#table4').datagrid('options').url='/jlyw/DealServlet.do?method=4';
			$('#table4').datagrid('options').queryParams={'DealCode':encodeURI($('#dealCode').val())};
			$('#table4').datagrid('reload');
			$('#item_edit').window('open');	
		}
		
		//查询受检器具
		function query_appli(){
			$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
			$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getText'))};
			$('#table_appli').datagrid('reload');
		}
		
		//新增协议条目，对数据等信息进行填写
		function Add_QuotaItem(){
			
			var row=$('#table_appli').datagrid('getSelected');
			$('#AppFactoryCode').val('');
			$('#AppManageCode').val('');
			$('#Manufacturer').val('');
			if(row){
				//
				$('#Model').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=1&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Range').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				//
				$('#Accuracy').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName=' + encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name'
				});
				$('#Accuracy').combobox('setValue',row.Accuracy);
				$('#Range').combobox('setValue',row.Range);
				$('#Model').combobox('setValue',row.Model);
				var rows=$('#table_appli').datagrid('getSelections');
				for(var i=0; i<rows.length; i++){
					$('#StandardName').val(rows[i].StandardNameName);
				}
				$('#add').window('open');
			}
			else{
				$.messager.alert('提示','请选择一组数据!','info');
			}
		}
		
		//将协议条目加入datagrid
		function edit_item(){	
			if($('#DealPrice').val()==''){
				$.messager.alert('提示','请填写协议检测费！','info');
			}
			else{																
				var table_appli_row = $('#table_appli').datagrid('getSelected');
				var table4_rows = $('#table4').datagrid('getRows');
				var index = $('#table4').datagrid('getRows').length;
														
				$('#table4').datagrid('insertRow',{
					index:index,
					row:{
						StandardName: table_appli_row.StandardNameName,
						StandardNameId: table_appli_row.StandardNameid,
						Model:$('#Model').combobox('getText'),
						Accuracy:$('#Accuracy').combobox('getText'),
						Range:$('#Range').combobox('getText'),
						DealPrice:$('#DealPrice').val(),
						AppFactoryCode:$('#AppFactoryCode').val(),
						AppManageCode:$('#AppManageCode').val(),
						Manufacturer:$('#Manufacturer').val(),
						Remark:$('#Remark').val()
					}
				});							
							
				$('#add').dialog('close');
				$('#item_add').dialog('close');
				$('#table_appli').datagrid('clearSelections');
			}
		}
		
		//取消将协议条目加入datagrid中
		function cancel_item(){
			$('#form4').form('clear');
			$('#add').window('close');
		}
		
		//编辑协议单位信息
		function edit_Quotation(){	
		
			$('#quotation_edit_form').form('submit',{
				url:'/jlyw/DealServlet.do?method=7',
				onSubmit:function(){
					
					return $('#quotation_edit_form').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					
					if(result.IsOK){
						$.messager.alert('提示！','修改成功！','info');
						$('#quotation_edit').window('close');
						$('#table2').datagrid('reload');
					}else{
						$.messager.alert('错误！',result.msg,'error');
					}
				}
			});
			
		}
		
		function cancel_Quotation(){
			$('#quotation_edit').window('close');
		}
	</script>
</head>

<body >
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="协议查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
    <div>
		<div  align="center" style="width:900px;padding:10px" class="easyui-panel" title="查询条件" collapsible="false"  closable="false">
			<table id="table1" style="width:750px;">
				<tr>
						
					<td align="left">
					协议编号：<input id="queryDealCode" name="queryDealCode" type="text" style="width:120px;"/>&nbsp;
				    询价单位：<input id="queryCustomer" name="queryCustomer" type="text" style="width:120px;"/>&nbsp;&nbsp;&nbsp;
					<select name="queryStatus" id="queryStatus" style="width:100px"><option value="" selected="selected">全部</option><option value="0">正常</option><option value="1" >注销</option></select>&nbsp;
					</td>
					</tr>
				<tr>
					<td align="left">
					&nbsp;&nbsp;起始时间：<input name="date1" id="dateTimeFrom" type="text" style="width:124px;"  class="easyui-datebox" >&nbsp;					
					结束时间：<input name="date2" id="dateTimeEnd" type="text" style="width:120px;"  class="easyui-datebox" >&nbsp;&nbsp;&nbsp;
					
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a>
					</td>
					
				</tr>
			</table>
	   </div>
	   
	   
			<table id="table2" iconCls="icon-search" > </table>	
		
        <div id="p4" class="easyui-panel" style="width:900px;height:250px;"
         title="附件上传" collapsible="false"  closable="false" scroll="no">
            <table width="100%" height="100%" >
               <tr>
                   <td width="57%" rowspan="2">
                   		<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=106"></table>
                   </td>
                   <td width="43%" height="175" valign="top" align="left" style="overflow:hidden">
                        <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
                   </td>
               </tr>
               <tr>
                   <td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByDefault()">上传文件</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">取消上传</a> </td>
               </tr>
           </table>
        </div>
		
		
		<div id="quo_edit" class="easyui-window" title="修改协议条目" style="padding:10px;width:480px;height:320px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false"  modal="true">
			<form id="form1" method="post">
				<table id="table3">
					<tr height="35px" style="padding-left:10px">
						<td align="right">受检器具&nbsp;<br />标准名称:</td>
						<td align="left"><input id="StandardName1" name="StandardName" type="text" /></td>
						<td align="right">型号规格:</td>
						<td align="left"><input id="Model1" name="Model" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
					</tr>
					
					<tr height="35px" style="padding-left:10px">
						<td align="right">测量范围:</td>
						<td align="left"><input id="Range1" name="Range" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
						<td align="right">准确度等级:</td>
						<td align="left"><input id="Accuracy1" name="Accuracy" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
					</tr>
					
					<tr height="35px" style="padding-left:10px">
						<td align="right">出厂编号:</td>
						<td align="left"><input id="AppFactoryCode1" name="AppFactoryCode" type="text"/></td>
                        <td align="right">管理编号:</td> 
						<td align="left"><input id="AppManageCode1" name="AppManageCode" type="text" /></td>
					</tr>
					
					
					<tr height="35px" style="padding-left:10px">
						
						<td align="right">制&nbsp;造&nbsp;厂:</td>
						<td align="left"><input id="Manufacturer1" name="Manufacturer" type="text" /></td>
                        <td align="right">协议检测费:</td>
						<td align="left"><input id="DealPrice1" name="DealPrice" type="text" /></td>
					</tr>
					<tr height="35px" style="padding-left:10px">
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
						<td align="left"><input id="Remark1" name="Remark" type="text" /></td>
					</tr>
					<tr height="50px">	
						<td align="center" colspan="2" style="padding-left:10px"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">确定</a></td>
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a></td>
					
					</tr>
				</table>
				
			</form>
		</div>
		
		<div id="item_edit" class="easyui-window" title="协议条目信息查看" style="width: 800px;height: 500px;"
		iconCls="icon-search" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form2" name="form2" method="post">
				<input id="dealCode" name="dealCode" type="hidden" />
				<input id="Item" name="Item" type="hidden" /> 
				<table id="table4" iconCls="icon-search" style="height:430px; width:780px"></table>	
			</form>
		</div>
		
		<div id="item_add" class="easyui-window" title="添加条目" style="width:800px;height:500px" iconCls="icon-search" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
				<table id="table_appli" iconCls="icon-search"> </table>	
		</div>
		
		
		<div id="table_appli-search-toolbar" style="padding:2px 0">
			<form id="form3" name="form3" method="post">
				<table cellpadding="0" cellspacing="0" style="width:100%">
					<tr>
						<td style="padding-left:2px">
							<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="Add_QuotaItem()">加入协议条目</a>
						</td>
						<td align="right">受检器具标准名:</td>
						<td width="21%"><input id="Search_Appli" name="Search_Appli" type="text" style="width:152px" /></td>
						<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query_appli()">查询</a></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="add" class="easyui-window" title="添加协议条目" style="width:480px;height:320px;padding:10px"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true" >
			<form id="form4" method="post">
				<table id="table5">
					<tr height="35px" style="padding-left:10px">
						<td align="right">受检器具&nbsp;<br />标准名称:</td>
						<td align="left"><input id="StandardName" name="StandardName" type="text" /></td>
						<td align="right">型号规格:</td>
						<td align="left"><input id="Model" name="Model" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
					</tr>
					
					<tr height="35px" style="padding-left:10px">
						<td align="right">测量范围:</td>
						<td align="left"><input id="Range" name="Range" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
						<td align="right">准确度等级:</td>
						<td align="left"><input id="Accuracy" name="Accuracy" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
					</tr>
					
					<tr height="35px" style="padding-left:10px">
						<td align="right">出厂编号:</td>
						<td align="left"><input id="AppFactoryCode" name="AppFactoryCode" type="text"/></td>
                        <td align="right">管理编号:</td>
						<td align="left"><input id="AppManageCode" name="AppManageCode" type="text" /></td>
					</tr>
					
					
					<tr height="35px" style="padding-left:10px">
						
						<td align="right">制&nbsp;造&nbsp;厂:</td>
						<td align="left"><input id="Manufacturer" name="Manufacturer" type="text" /></td>
                        <td align="right">协议检测费:</td>
						<td align="left"><input id="DealPrice" name="DealPrice" type="text" /></td>
					</tr>
					<tr height="35px" style="padding-left:10px">
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
						<td align="left"><input id="Remark" name="Remark" type="text" /></td>
					</tr>
					<tr height="50px">	
						<td align="center" colspan="2"  style="padding-left:10px"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit_item()">确定</a></td>
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_item()">取消</a></td>
						
					</tr>
				</table>
				
			</form>
		</div>

		<div id="quotation_edit" class="easyui-window" title="协议基本信息修改" style="width:800;padding:10px"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true" >
			<form id="quotation_edit_form" name="quotation_edit_form" method="post">
			<input id="DealCode" name="DealCode" type="hidden" readonly="readonly"/>
				<table >
					<tr>
						<td align="right">协议单位:</td>
						<td align="left"><input id="CustomerName" name="CustomerName" style="width:152px;" /></td>
					</tr>
					<tr>
						<td align="right">协议单位&nbsp;<br />联&nbsp;系&nbsp;人:</td>
						<td align="left"><input id="Contactor" name="Contactor" type="text" /></td>
						<td align="right">联&nbsp;系&nbsp;人&nbsp;<br />电&nbsp;&nbsp;&nbsp;&nbsp;话:</td>
						<td align="left"><input id="ContactorTel" name="ContactorTel" type="text" maxlength="12" /></td>
						
					</tr>
                    <tr>
                    	<td align="right">协议签署人:</td>
						<td align="left"><input id="Signer" name="Signer" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    	<td align="right">协议签署日期:</td>
						<td align="left"><input id="SignDate" name="SignDate" style="width:152px;" class="easyui-datebox"/></td>
                    </tr>
                    <tr>
                    	<td align="right">协议有效期:</td>
						<td align="left"><input id="Validity" name="Validity" style="width:152px;" class="easyui-datebox"/></td>
                    </tr>
                    <tr>
                    	<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态:</td>
						<td align="left"><select id="Status" name="Status" style="width:152px;" class="easyui-combobox" panelHeight="auto">
                        					<option value="0" selected="selected">正常</option>
                                            <option value="1">注销</option>
                                        </select></td>
                    </tr>
					<tr>
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
						<td align="left" colspan="3"><input id="Remark_1" name="Remark" type="text" /></td>
					</tr>
					<tr height="50px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit_Quotation()">提交</a></td>
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_Quotation()">取消</a></td>
					</tr>
				</table>
				
			</form>
		</div>
     </div>
</DIV>
</DIV>
</body>

</html>
