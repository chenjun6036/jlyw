<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�����ļ��й���</title>
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
				'scriptData':{'method':'1','FileType':'105'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../uploadify/selectfiles.png',
			//	'fileDesc'  : 'ȫ����ʽ:*.*', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
                //'fileExt'   : '*.*',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('��ʾ',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table1').datagrid('reload');
					CloseWaitingDlg();
					closewindow();
				}
			 });
			
			$('#uploaded_file_table1').datagrid({
			title:'���ϴ����ļ�',			
			iconCls:'icon-tip',
			idField:'_id',
			fit:true,
			singleSelect:true,			
			rownumbers:true,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:"filename",title:"�ļ���",width:130,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='������ظ��ļ�' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				{field:"length",title:"��С",width:60,align:"left"},
				{field:"uploadDate",title:"�ϴ�ʱ��",width:120,align:"left"},
				{field:"uploadername",title:"�ϴ���",width:60,align:"left"}
			]],
			toolbar:[{
				text:'ɾ��',
				iconCls:'icon-remove',
				handler:function(){
					var row = $('#uploaded_file_table1').datagrid('getSelected');
					if(row == null){
						$.messager.alert('��ʾ',"��ѡ��Ҫɾ�����ļ���",'info');
						return false;
					}
					var rowIndex = $('#uploaded_file_table1').datagrid('getRowIndex', row._id);	//��ֹ��ʱ��datagridˢ���Ժ��ȡ��row����֮ǰ�ľ����ݣ�������û�й�ѡ����row��Ϊnull��
					if(rowIndex < 0){
						$.messager.alert('��ʾ',"��ѡ��Ҫɾ�����ļ���",'info');
						return false;
					}
					var result = confirm("��ȷ��Ҫɾ�� "+row.filename+" ��");
					if(result == false){
						return false;
					}
					//����ɾ���ļ�
					$.ajax({
							type: "post",
							url: "/jlyw/FileServlet.do?method=0",
							data: {"FileId":row._id,"FileType":row.filetype},
							dataType: "json",	//�������������ݵ�Ԥ������
							beforeSend: function(XMLHttpRequest){
							},
							success: function(data, textStatus){
								if(data.IsOK){
									$.messager.alert('��ʾ','ɾ���ɹ���','info');
									$('#uploaded_file_table1').datagrid('reload');
								}else{
									$.messager.alert('ɾ���ļ�ʧ�ܣ�',data.msg,'error');
								}
							},
							complete: function(XMLHttpRequest, textStatus){
								//HideLoading();
							},
							error: function(){
								//���������
							}
					});
					
				}
			},'-',{
				text:'ˢ��',
				iconCls:'icon-reload',
				handler:function(){
					$('#uploaded_file_table1').datagrid('reload');
				}	
			},'-',{
				text:'�ϴ��ļ�',
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
					if($(this).tree('isLeaf',node.target))  //��Ҷ�ӽڵ�
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
		
		function appendStandard(){   //�Ҽ��˵�������һ��������ļ�������
			$('#table1').panel('open', true);
			$('#table2').panel('close', true);
			var node = $('#tt').tree('getSelected');
			$('#SpeciesId').val(node.id);
			$('#Name').val('');
			$('#Name').focus();
			
			//����ģ���ļ���Ϣ
			//$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			//$('#uploaded_file_table1').datagrid('reload');
			
		}
		
		function editStandard(){      //�Ҽ��˵����Ե��нڵ���Ϣ�����޸�
			$('#table2').panel('open', true);
			$('#table1').panel('close', true);
			var node = $('#tt').tree('getSelected');
			if(node.attributes.ParentId==0||node.attributes.ParentId.length=='0'){
			   $.messager.alert('��ʾ��',"���ļ��в����޸�",'info');
				return false;
			}

			$('#Name1').val(node.text);
			$('#SpeciesId1').val(node.id);
			
			//����ģ���ļ���Ϣ
			//$('#uploaded_file_table1').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			//$('#uploaded_file_table1').datagrid('reload');
		}
		
		//�����ӽڵ�
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
					$.messager.alert('��ʾ��',result.msg,'info');
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
					$.messager.alert('��ʾ��',result.msg,'info');
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
		    
			var result = confirm("��ȷ��Ҫɾ�����ļ��У��������ļ����µ������ļ������ļ��У���");
			if(result == false){
				return false;
			}
			var node = $('#tt').tree('getSelected');
			if(node.attributes.ParentId==0||node.attributes.ParentId.length=='0'){
			   $.messager.alert('��ʾ��',"���ļ��в���ɾ��",'info');
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
					$.messager.alert('��ʾ��',result.msg,'info');
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
			<jsp:param name="TitleName" value="�����ļ��й���" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV" style="left:0px; margin-left:0px">
<DIV class="easyui-layout" fit="true" nohead="true" border="true">
	
	<div region="west" style="width:200px; border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="�����ļ�������">
		<ul id="tt"></ul>
 	</div>

	
	
  	<div region="center" id="add" name="add" noheader="true" border="false" style="position:relative;">
		<form id="form1" method="post">
			<table id="table1" class="easyui-panel" title="����ļ�������" style="width:800px; height:100px; padding-top:10px;">
				<tr>
					<td align="right">�ļ������ƣ�</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" /><input id="SpeciesId" name="SpeciesId" type="hidden" class="easyui-validatebox" required="true" /></td>
					<td align="center" >
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="add()">ȷ�����</a>
					</td>
					<td align="left" >
						<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a>
					</td>
				</tr>
			</table>
		</form>
		
		<form id="form2" method="post">
		<input id="id" name="id" type="hidden"/>
			<table id="table2" class="easyui-panel" title="�޸��ļ�������" style="width:800px; height:100px; padding-top:10px;" closed="true">
				<tr>
					<td align="right">�ļ������ƣ�</td>
					<td align="left"><input id="Name1" name="Name" type="text" class="easyui-validatebox" required="true" /><input id="SpeciesId1" name="SpeciesId" type="hidden" class="easyui-validatebox" required="true" /></td>
					<td align="center" >
						<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="edit()">ȷ���޸�</a>
					</td>
					<td align="left" >
						<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a>
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
		<div onclick="appendStandard()" iconCls="icon-add">����ļ���</div>
		<div onclick="upload()" iconCls="icon-add">�ϴ��ļ�</div>
		<div onclick="editStandard()" iconCls="icon-edit">�޸��ļ�����Ϣ</div>
		<div onclick="deleteStandard()" iconCls="icon-edit">ɾ���ļ���</div>
	</div>
	
	<div id="upload" class="easyui-window" title="�ϴ�" style="padding: 10px;width: 600px;" 
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
					<td align="center" style="padding-top: 20px;"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadByDefault1()">�ϴ��ļ�</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">ȡ���ϴ�</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:closewindow()">�ر�</a> 
					</td>
				</tr>
				</table>
			 </form>
    	</div>
	   
    </div>	
    
</DIV></DIV></DIV>
 

</body>

</html>
