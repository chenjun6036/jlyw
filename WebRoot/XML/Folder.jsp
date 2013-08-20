<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>共享文件夹管理</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
    
	<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script>
		var nodeExp=false;
		var nodekeep="";
		$(function(){
			$('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'105'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../uploadify/selectfiles.png',
			//	'fileDesc'  : '全部格式:*.*', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                //'fileExt'   : '*.*',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('提示',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table1').datagrid('reload');
					CloseWaitingDlg();
					closewindow();
				}
			 });
			
			$('#uploaded_file_table1').datagrid({
			title:'已上传的文件',			
			iconCls:'icon-tip',
			idField:'_id',
			fit:true,
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
				{field:"length",title:"大小",width:60,align:"left"},
				{field:"uploadDate",title:"上传时间",width:120,align:"left"},
				{field:"uploadername",title:"上传人",width:60,align:"left"}
			]],
			toolbar:[{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					var row = $('#uploaded_file_table1').datagrid('getSelected');
					if(row == null){
						$.messager.alert('提示',"请选择要删除的文件！",'info');
						return false;
					}
					var rowIndex = $('#uploaded_file_table1').datagrid('getRowIndex', row._id);	//防止有时候datagrid刷新以后获取的row还是之前的旧数据（界面上没有勾选，但row不为null）
					if(rowIndex < 0){
						$.messager.alert('提示',"请选择要删除的文件！",'info');
						return false;
					}
					var result = confirm("您确定要删除 "+row.filename+" 吗？");
					if(result == false){
						return false;
					}
					//请求删除文件
					$.ajax({
							type: "post",
							url: "/jlyw/FileServlet.do?method=0",
							data: {"FileId":row._id,"FileType":row.filetype},
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
								//HideLoading();
							},
							error: function(){
								//请求出错处理
							}
					});
					
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
			}]
		});
		 
			$('#tt').tree({
				url:'/jlyw/SharingFolderServlet.do?method=2',
				animate : false,
				onBeforeExpand:function(node){
					$.ajax({
						type: "POST",
                		url: "/jlyw/SharingFolderServlet.do?method=2&parentid=" + node.id,
                 		cache: false,
                 		async: false,
                 		dataType: "json",
                 		success: function(data) {
              
                     		if(nodekeep.indexOf("," + node.id+",")==-1)
                    		{
                     			append(data, node);
                     			nodeExp = true;
                   			}
				
							$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
							$('#uploaded_file_table1').datagrid('reload');
						
                		}
					});
				},
				onBeforeCollapse:function(node){
					$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
					$('#uploaded_file_table1').datagrid('reload');
				},
				onContextMenu: function(e, node){
					e.preventDefault();
					$('#tt').tree('select', node.target);				
						$('#mm').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
				},
				onClick: function(node){
					$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
					$('#SpeciesId').val(node.id);
					$('#SpeciesId1').val(node.id);
					if($(this).tree('isLeaf',node.target))  //是叶子节点
					{
						//ClickNode(node.attributes.url, node.attributes.title);
					}
					else
					{
						$(this).tree('toggle', node.target);
					}
				}
				
			})
		})
		
		function appendStandard(){   //右键菜单，在下一级中添加文件夹名称
			$('#table1').panel('open', true);
			$('#table2').panel('close', true);
			var node = $('#tt').tree('getSelected');
			$('#SpeciesId').val(node.id);
			$('#Name').val('');
			$('#Name').focus();
			
			//加载模板文件信息
			//$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			//$('#uploaded_file_table1').datagrid('reload');
			
		}
		
		function editStandard(){      //右键菜单，对点中节点信息进行修改
			$('#table2').panel('open', true);
			$('#table1').panel('close', true);
			var node = $('#tt').tree('getSelected');
			if(node.attributes.ParentId==0||node.attributes.ParentId.length=='0'){
			   $.messager.alert('提示！',"根文件夹不能修改",'info');
				return false;
			}

			$('#Name1').val(node.text);
			$('#SpeciesId1').val(node.id);
			
			//加载模板文件信息
			//$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			//$('#uploaded_file_table1').datagrid('reload');
		}
		
		//增加子节点
		function append(datas,cnode) {
		     var node = cnode;
		     $('#tt').tree('append', {
		        parent: node.target,
		        data: datas
		    });
		   nodekeep+="," + node.id+",";
		}
		
		
		function cancel(){
			$('#Name').val('');
			$('#SpeciesId').val('');
			$('#SpeciesId1').val('');
		}
		
		function reload(){
			var node = $('#tt').tree('getSelected');
			$('#tt').tree('options').url="/jlyw/SharingFolderServlet.do?method=2&parentid=" + node.id;
			if (node){
				$('#tt').tree('reload', node.target);
			} else {
				$('#tt').tree('reload');
			}
		}
		
		function add(){
			$('#form1').form('submit',{
				url:'/jlyw/SharingFolderServlet.do?method=1',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示！',result.msg,'info');
					if(result.IsOK)
					{
						cancel();
				        reload();
		
					}
				}
			});
//			var node = $('#tt').tree('getSelected');
//			$('#tt').tree('append',{
//				parent: (node?node.target:null),
//				data:[{
//					text:$("#Name").val(),	
//					
//				}]
//			});
		}
		
		function edit(){
			$('#form2').form('submit',{
				url:'/jlyw/SharingFolderServlet.do?method=3',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示！',result.msg,'info');
					if(result.IsOK)
					{
						cancel();
						//reload();
						 var node = $('#tt').tree('getSelected');
						//
					     node.text= $("#Name1").val();
						
     					 $("#tt").tree("update",node);

					}
				}
			});
			
		}
		function deleteStandard(){
		    
			var result = confirm("您确定要删除该文件夹（包括该文件夹下的所有文件及子文件夹）吗？");
			if(result == false){
				return false;
			}
			var node = $('#tt').tree('getSelected');
			if(node.attributes.ParentId==0||node.attributes.ParentId.length=='0'){
			   $.messager.alert('提示！',"根文件夹不能删除",'info');
				return false;
			}
			//$('#tt').tree('remove',node.target); 
			$.ajax({
				type: "POST",
                url: "/jlyw/SharingFolderServlet.do?method=4&id=" + node.id,
                cache: false,
                async: false,
                dataType: "json",
                success: function(data) {
                var result = eval("("+data+")");
					$.messager.alert('提示！',result.msg,'info');
					if(result.IsOK)
					{
						cancel();
						
					}
                }
			})
			$('#tt').tree('remove',node.target); 
			
		}
		function doUploadByDefault1(){
			
		 	doUploadByUploadify($('#uploaded_file_table1').datagrid('options').queryParams.FilesetName,'file_upload');
		 }
		function upload(){
			var node = $('#tt').tree('getSelected');
			
			$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			$('#upload').window('open');
			$('#ffee').show();

		}
		function closewindow(){
			
			$('#upload').window('close');
			

		}
	</script>
</head>
<body>

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="共享文件夹管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV" style="left:0px; margin-left:0px">
<DIV class="easyui-layout" fit="true" nohead="true" border="true">
	
	<div region="west" style="width:200px; border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="共享文件夹名称">
		<ul id="tt"></ul>
 	</div>

	
	
  	<div region="center" id="add" name="add" noheader="true" border="false" style="position:relative;">
		<form id="form1" method="post">
			<table id="table1" class="easyui-panel" title="添加文件夹名称" style="width:800px; height:100px; padding-top:10px;">
				<tr>
					<td align="right">文件夹名称：</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" /><input id="SpeciesId" name="SpeciesId" type="hidden" class="easyui-validatebox" required="true" /></td>
					<td align="center" >
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="add()">确认添加</a>
					</td>
					<td align="left" >
						<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">重置</a>
					</td>
				</tr>
			</table>
		</form>
		
		<form id="form2" method="post">
		<input id="id" name="id" type="hidden"/>
			<table id="table2" class="easyui-panel" title="修改文件夹名称" style="width:800px; height:100px; padding-top:10px;" closed="true">
				<tr>
					<td align="right">文件夹名称：</td>
					<td align="left"><input id="Name1" name="Name" type="text" class="easyui-validatebox" required="true" /><input id="SpeciesId1" name="SpeciesId" type="hidden" class="easyui-validatebox" required="true" /></td>
					<td align="center" >
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="edit()">确认修改</a>
					</td>
					<td align="left" >
						<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">重置</a>
					</td>
				</tr>
				
			</table>
		</form>
		<br/>
		<table width="800px" height="400px" >
			<tr>
				<td width="57%">
					<table id="uploaded_file_table1" pagination="true" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=1&FileType=105"></table>
				</td>

			</tr>
		</table>
	</div>
	

	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="appendStandard()" iconCls="icon-add">添加文件夹</div>
		<div onclick="upload()" iconCls="icon-add">上传文件</div>
		<div onclick="editStandard()" iconCls="icon-edit">修改文件夹信息</div>
		<div onclick="deleteStandard()" iconCls="icon-edit">删除文件夹</div>
	</div>
	
	<div id="upload" class="easyui-window" title="上传" style="padding: 10px;width: 600px;" 
    iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
    	<div id="eeee">
		
   			 <form id="ffee" method="post">
		    	<table width="95%" height="100%" >
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
    
</DIV></DIV></DIV>
 

</body>

</html>
