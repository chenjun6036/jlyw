<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>协议管理</title>
<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../../JScript/json2.js"></script>
	<link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
	<script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>		
		$(function(){
		$('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'105'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
			//	'fileDesc'  : '全部格式:*.*', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                //'fileExt'   : '*.*',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('提示',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					closewindow();
				}
		 });
		 $('#Attachment').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
			//	'fileDesc'  : '全部格式:*.*', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                //'fileExt'   : '*.*',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('提示',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){					
				}
		 });
			
		$('#uploaded_file_table1').datagrid({
			title:'导入协议',			
			iconCls:'icon-tip',
			idField:'_id',
			fit:true,
			sortName: 'uploadDate',
			remoteSort: false,
			sortOrder:'dec',
			singleSelect:true,			
			rownumbers:true,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:"filename",title:"文件名",width:130,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='点击下载该文件' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				//{field:"_id",title:"文件Id",width:160,align:"left"},
//				{field:"filetype",title:"文件类型",width:60,align:"left"},
				{field:"length",title:"大小",width:60,align:"left"},
				{field:"uploadDate",title:"上传时间",width:120,align:"left"},
				{field:"uploadername",title:"上传人",width:60,align:"left"}
			]],
			toolbar:[{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
				
					var rows = $('#uploaded_file_table1').datagrid('getSelections');
					if(rows == null){
						$.messager.alert('提示',"请选择要删除的文件！",'info');
						return false;
					}
					for(var i=0; i<rows.length;i++){
						var rowIndex = $('#uploaded_file_table1').datagrid('getRowIndex', rows[i]._id);	//防止有时候datagrid刷新以后获取的row还是之前的旧数据（界面上没有勾选，但row不为null）
						if(rowIndex < 0){
							$.messager.alert('提示',"请选择要删除的文件！",'info');
							return false;
						}
						var result = confirm("您确定要删除 "+rows[i].filename+" 吗？");
						if(result == false){
							return false;
						}
						//请求删除文件
						$.ajax({
								type: "post",
								url: "/jlyw/FileServlet.do?method=0",
								data: {"FileId":rows[i]._id,"FileType":rows[i].filetype},
								async:false,
								dataType: "json",	//服务器返回数据的预期类型
								beforeSend: function(XMLHttpRequest){
								},
								success: function(data, textStatus){
									if(data.IsOK){
										$.messager.alert('提示','删除成功！','info');
										$('#uploaded_file_table1').datagrid('reload');
									}else{
										$.messager.alert('删除文件失败！',data.msg,'error');
									}
								},
								complete: function(XMLHttpRequest, textStatus){
									$('#uploaded_file_table1').datagrid('reload');
								},
								error: function(){
									//请求出错处理
								}
						});
					}
					
				}
			},'-',{
				text:'刷新',
				iconCls:'icon-reload',
				handler:function(){
					$('#uploaded_file_table1').datagrid('reload');
				}	
			},'-',{
				text:'上传文件',
				iconCls:'icon-save',
				handler:function(){
					upload();
				}	
			},'-',{
				text:'导入所选文件',
				iconCls:'icon-save',
				handler:function(){
					
					var rows = $('#uploaded_file_table1').datagrid('getSelections');
					if(rows.length==0){
						$.messager.alert('提示','请选择需要导入的协议！','info');
						return false;
					}
					ShowWaitingDlg("正在导入，请稍后......");
					for(var i=0; i<rows.length;i++){
						$.ajax({
							type: "POST",
							url: "/jlyw/DealServlet.do?method=1",
							data: {"FileId":rows[i]._id,"FileType":rows[i].filetype},
							cache: false,
							async: false,
							dataType: "json",
							success: function(data) {
								var items = data.rows;
								var index = $('#table_item').datagrid('getRows').length;
								for(var i=0;i<items.length;i++)
								{
									$('#table_item').datagrid('insertRow',{
										index:index,
										row:{
											StandardName:items[i].StandardName,
											StandardNameId: items[i].StandardNameId,
				
											AppFactoryCode: items[i].AppFactoryCode,											
											AppManageCode: items[i].AppManageCode,
											Manufacturer: items[i].Manufacturer,
											
											Model:items[i].Model,
											Accuracy:items[i].Accuracy,
											Range:items[i].Range,
											DealPrice:items[i].DealPrice,
											Remark:items[i].Remark
										}
									});	
									index++;
								}
								var row = $('#uploaded_file_table1').datagrid('getSelected');
								$.ajax({
										type: "post",
										url: "/jlyw/FileServlet.do?method=0",
										data: {"FileId":row._id,"FileType":row.filetype},
										dataType: "json",	//服务器返回数据的预期类型
										beforeSend: function(XMLHttpRequest){
										},
										success: function(data, textStatus){
											if(data.IsOK){
												//$.messager.alert('提示','删除成功！','info');
												$('#uploaded_file_table1').datagrid('reload');
											}else{
												$.messager.alert('删除文件失败！',data.msg,'error');
											}
										},
										complete: function(XMLHttpRequest, textStatus){
											//HideLoading();
										},
										error: function(){
											//请求出错处理
										}
								});
								CloseWaitingDlg();	
							}
						});
					}
				}	
			}]
		});
			var lastIndex;
			$("#CustomerName").combobox({
				valueField:'name',
				textField:'name',
				onSelect:function(record){
					$("#Contactor").val(record.contactor);
					$("#ContactorTel").val(record.contactorTel);
				},
				require:true,
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
							var newValue = $('#CustomerName').combobox('getText');
							$('#CustomerName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					}, 700);
					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
				}
			});
			
			$('#Signer').combobox({
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
			
			$('#Search_PopularName').combobox({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=0',
				valueField:'name',
				textField:'name',
				onSelect:function(record){
					try{
						$("#Search_Appli").combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=9&PopularName='+encodeURI(record.name));
					}catch(ex){}
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
					$(this).combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=8&AppliancePopularName='+newValue);
					}
			});
			

			$('#table_appli').datagrid({
				title:'受检器具信息',
				width:800,
				height:350,
				singleSelect:true,
				fit: true,
                nowrap: true,
                striped: true,
				rownumbers:false,
				loadMsg:'数据加载中......',
				url:'/jlyw/QuotationServlet.do?method=0',
				remoteSort: false,
				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'TargetApplianceName',title:'受检器具',width:100,sortable:true,align:'center'}
				]],
				columns:[[
					{field:'StandardNameName',title:'器具标准名称',width:120,align:'center'},
					{field:'Model',title:'型号规格',width:120,align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:120,align:'center'},
					{field:'Range',title:'测量范围',width:120,align:'center'},
					{field:'Fee',title:'标准费用',width:80,align:'center'}
				]],
				pagination:true,
				toolbar:"#table_appli-search-toolbar"
			});
			
			var lastIndex;
			$('#table_item').datagrid({
				title:'协议条目',
				width:800,
				height:350,
             	singleSelect:false, 
                nowrap: false,
                striped: true,
				remoteSort: false,
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
					{field:'DealPrice',title:'协议检测费',editor:'number',width:80,align:'center'},
					{field:'Remark',title:'备注',width:80,editor:'text',align:'center'}
				]],
				rownumbers:false,
				pagination:false,
				toolbar:[{
					text:'删除条目',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("确定要移除这些条目吗？");
						if(result == false){
							return false;
						}
						var row = $('#table_item').datagrid('getSelected');
						if(!row){
							$.messager.alert('提示','请选择需要移除的条目！','info');
							return false;
						}
						else{
							var rows = $('#table_item').datagrid('getSelections');
							var length = rows.length;
							for(var i=length-1; i>=0; i--){
								var index = $('#table_item').datagrid('getRowIndex', rows[i]);
								$('#table_item').datagrid('deleteRow', index);
							}
						}
					}
				},'-',{
					text:'生成协议',
					iconCls:'icon-add',
					handler:function(){
						var result = confirm("确定要生成协议吗？");
						if(result == false){
							return false;
						}
						var rows = $('#table_item').datagrid('getRows');
						if(rows.length == 0){
							$.messager.alert('提示','该协议条目为空，不能生成协议！','info');
							return false;
						}
						
						$('#add_Quo').window('open');
						
					}
				},'-',{
					text:'完成编辑',
					iconCls:'icon-save',
					handler:function(){
						$('#table_item').datagrid('acceptChanges');
					}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex){
			
//					if (lastIndex != rowIndex){
						$('#table_item').datagrid('endEdit', lastIndex);
						$('#table_item').datagrid('beginEdit', rowIndex);
		//					}
					lastIndex = rowIndex;
				}
			});
		});
		
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
					textField:'Name',
					onSelect:function(){},
					onChange:function(newValue, oldValue){
						
						//$(this).combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=0&ApplianceStandardName='+newValue);
						}
				});
				//
				$('#Range').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=3&standardNameName='+encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name',
					onSelect:function(){},
					onChange:function(newValue, oldValue){
						
						//$(this).combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=0&ApplianceStandardName='+newValue);
						}
				});
				//
				$('#Accuracy').combobox({
					url:'/jlyw/TargetApplianceServlet.do?method=12&Type=2&standardNameName='+encodeURI(row.StandardNameName),
					valueField:'Name',
					textField:'Name',
					onSelect:function(){},
					onChange:function(newValue, oldValue){
						
						//$(this).combobox('reload','/jlyw/AppliancePopularNameServlet.do?method=0&ApplianceStandardName='+newValue);
						}
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
		function add(){
			if($('#DealPrice').val()==''){
				$.messager.alert('提示','请填写协议检测费！','info');
			}
			else{																
				var table_appli_row = $('#table_appli').datagrid('getSelected');
				var index = $('#table_item').datagrid('getRows').length;
				$('#table_item').datagrid('insertRow',{
						index:index,
						row:{
							StandardName: table_appli_row.StandardNameName,
							StandardNameId: table_appli_row.StandardNameid,
							Model:$('#Model').combobox('getText'),
							Accuracy:$('#Accuracy').combobox('getText'),
							Range:$('#Range').combobox('getText'),
							AppFactoryCode:$('#AppFactoryCode').val(),
							AppManageCode:$('#AppManageCode').val(),
							Manufacturer:$('#Manufacturer').val(),
							DealPrice:$('#DealPrice').val(),
							Remark:$('#Remark').val()
						}
				});	
	
			}
		}
		
		//查询受检器具
		function query(){
			$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
			//$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getValue'))};
			$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getText'))};
			$('#table_appli').datagrid('reload');

		}
		//取消将协议条目加入datagrid中
		function cancel(){
			$('#add').dialog('close');
		}
		
		//新增协议
		function add_Quotation(){
		
			$('#form4').form('submit',{
				url:'/jlyw/DealServlet.do?method=2',
				onSubmit:function(){
				
					var rows = $('#table_item').datagrid('getRows');	
					if(rows.length==0){
						$.messager.alert('提示','请先选择协议条目！','info');
						return false;
					}	
					$("#Item").val(JSON.stringify(rows));
				
					return $('#form4').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					alert(result.msg);
					if(result.IsOK){
						$('#add_Quo').dialog('close');
						var attachment = result.Attachment;
						var num = $('#Attachment').uploadifySettings('queueSize');
						if (num > 0) { //有选择文件
							doUploadByUploadify(attachment,'Attachment', false);
						}
						cancel_Quotation();
						$('#table_item').datagrid('loadData',{total:0,rows:[]});	
										
					}
				}
			});
			$('#form4').form('clear');
			
		}
		
		//取消协议添加
		function cancel_Quotation(){
			$('#form4').form('clear');
			$('#add_Quo').dialog('close');
		}
		function doUploadByDefault1(){
		 	doUploadByUploadify($('#uploaded_file_table1').datagrid('options').queryParams.FilesetName,'file_upload');
		 }
		function upload(){
			$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':'20120822200401466_4321'};
			$('#upload').window('open');
			$('#ffee').show();

		}
		function closewindow(){
			
			$('#upload').window('close');
			$('#uploaded_file_table1').datagrid('reload');

		}
	</script>
</head>

<body>
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="协议管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

   <div style="+position:relative;">
  		<div style="width:800px;height:350px;">		
			<table id="table_appli" iconCls="icon-search"> </table>	
		</div>
	
		<div style="width:800px;height:350px;">		
			<table id="table_item" ></table>
		</div>
		<div style="width:800px;height:350px;">	
			<table id="uploaded_file_table1" pagination="true" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=1&FileType=105&FilesetName=20120822200401466_4321"></table>
	
		</div>
		
		<div id="table_appli-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="Add_QuotaItem()">加入协议条目</a>
					</td>
					<td align="right">常用名称:</td>
				    <td width="18%"><input id="Search_PopularName" style="width:140px" name="Search_PopularName" type="text" /></td>
				    <td align="right">受检器具标准名:</td>
				    <td width="21%"><input id="Search_Appli" style="width:152px" name="Search_Appli" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query()">查询</a></td>
				</tr>
			</table>
		</div>
	<div id="upload" class="easyui-window" title="上传" style="padding: 10px;width: 600px;" 
    iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
    	<div id="eeee">
   			 <form id="ffee" method="post">
		    	<table width="90%" height="100%" >
				<tr>
					<td height="250" valign="top" align="left" style="overflow:hidden">
						  <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> <input id="file" name="file" type="hidden" class="easyui-validatebox" required="true" /></div>
					</td>
				</tr>
				<tr>
					<td align="center" style="padding-top: 20px;"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadByDefault1()">上传文件</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">取消上传</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:closewindow()">关闭</a> 
					</td>
				</tr>
				</table>
			 </form>
    	</div>
	   
    </div>
    
    	<div id="add" class="easyui-window" title="新增条目" style="padding: 10px;width: 800;"
		iconCls="icon-add" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form3" method="post">
				<table id="table1" style="padding: 30px;width: 800;height: 500;">
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
						<td align="center" colspan="4"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="add()">新增</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">关闭</a></td>
						
						
					</tr>
				</table>
			</form>
		</div>	
		
		<div id="add_Quo" class="easyui-window" title="新增协议" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-add" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form4" method="post">
				<input id="Item" name="Item" type="hidden" />
				<table >
					<tr>
						<td align="right">协议单位:</td>
						<td align="left"><input id="CustomerName" name="CustomerName" style="width:152px;" required="true"/></td>
					</tr>
					<tr>
						<td align="right">协议单位&nbsp;<br />联&nbsp;系&nbsp;人:</td>
						<td align="left"><input id="Contactor" name="Contactor" type="text"/></td>
						<td align="right">联&nbsp;系&nbsp;人&nbsp;<br />电&nbsp;&nbsp;&nbsp;&nbsp;话:</td>
						<td align="left"><input id="ContactorTel" name="ContactorTel" type="text" maxlength="12" /></td>
						
					</tr>
                    <tr>
                    	<td align="right">协议签署人:</td>
						<td align="left"><input id="Signer" name="Signer" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    	<td align="right">协议签署日期:</td>
						<td align="left"><input id="SignDate" name="SignDate" style="width:152px;" class="easyui-datebox" required="true"/></td>
                    </tr>
                    <tr>
                    	<td align="right">协议有效期:</td>
						<td align="left"><input id="Validity" name="Validity" style="width:152px;" class="easyui-datebox" required="true"/></td>
                    </tr>
                    <tr>
                    	<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态:</td>
						<td align="left"><select id="Status" name="Status" style="width:152px;" class="easyui-combobox" required="true">
                        					<option value="0" selected="selected">正常</option>
                                            <option value="1">注销</option>
                                        </select></td>
                    </tr>
					<tr>
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注:</td>
						<td align="left" colspan="3"><input id="Remark_1" name="Remark" type="text" /></td>
					</tr>
					<tr>
						<td align="right">附&nbsp;&nbsp;&nbsp;&nbsp;件:</td>
						<td align="left" colspan="3"><input id="Attachment" name="Attachment" type="file"  style="width:420px" /></td>
					</tr>
					<tr height="50px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="add_Quotation()">提交</a></td>
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_Quotation()">取消</a></td>
					</tr>
				</table>
			</form>
		</div>
		
	</div>
	
</DIV></DIV>
</body>
</html>
