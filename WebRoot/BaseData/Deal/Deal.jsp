<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>Э�����</title>
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
				'scriptData':{'method':'1','FileType':'105'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
			//	'fileDesc'  : 'ȫ����ʽ:*.*', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
                //'fileExt'   : '*.*',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('��ʾ',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					closewindow();
				}
		 });
		 $('#Attachment').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
			//	'fileDesc'  : 'ȫ����ʽ:*.*', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
                //'fileExt'   : '*.*',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('��ʾ',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){					
				}
		 });
			
		$('#uploaded_file_table1').datagrid({
			title:'����Э��',			
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
				{field:"filename",title:"�ļ���",width:130,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='������ظ��ļ�' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				//{field:"_id",title:"�ļ�Id",width:160,align:"left"},
//				{field:"filetype",title:"�ļ�����",width:60,align:"left"},
				{field:"length",title:"��С",width:60,align:"left"},
				{field:"uploadDate",title:"�ϴ�ʱ��",width:120,align:"left"},
				{field:"uploadername",title:"�ϴ���",width:60,align:"left"}
			]],
			toolbar:[{
				text:'ɾ��',
				iconCls:'icon-remove',
				handler:function(){
				
					var rows = $('#uploaded_file_table1').datagrid('getSelections');
					if(rows == null){
						$.messager.alert('��ʾ',"��ѡ��Ҫɾ�����ļ���",'info');
						return false;
					}
					for(var i=0; i<rows.length;i++){
						var rowIndex = $('#uploaded_file_table1').datagrid('getRowIndex', rows[i]._id);	//��ֹ��ʱ��datagridˢ���Ժ��ȡ��row����֮ǰ�ľ����ݣ�������û�й�ѡ����row��Ϊnull��
						if(rowIndex < 0){
							$.messager.alert('��ʾ',"��ѡ��Ҫɾ�����ļ���",'info');
							return false;
						}
						var result = confirm("��ȷ��Ҫɾ�� "+rows[i].filename+" ��");
						if(result == false){
							return false;
						}
						//����ɾ���ļ�
						$.ajax({
								type: "post",
								url: "/jlyw/FileServlet.do?method=0",
								data: {"FileId":rows[i]._id,"FileType":rows[i].filetype},
								async:false,
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
									$('#uploaded_file_table1').datagrid('reload');
								},
								error: function(){
									//���������
								}
						});
					}
					
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
			},'-',{
				text:'������ѡ�ļ�',
				iconCls:'icon-save',
				handler:function(){
					
					var rows = $('#uploaded_file_table1').datagrid('getSelections');
					if(rows.length==0){
						$.messager.alert('��ʾ','��ѡ����Ҫ�����Э�飡','info');
						return false;
					}
					ShowWaitingDlg("���ڵ��룬���Ժ�......");
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
										dataType: "json",	//�������������ݵ�Ԥ������
										beforeSend: function(XMLHttpRequest){
										},
										success: function(data, textStatus){
											if(data.IsOK){
												//$.messager.alert('��ʾ','ɾ���ɹ���','info');
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
				title:'�ܼ�������Ϣ',
				width:800,
				height:350,
				singleSelect:true,
				fit: true,
                nowrap: true,
                striped: true,
				rownumbers:false,
				loadMsg:'���ݼ�����......',
				url:'/jlyw/QuotationServlet.do?method=0',
				remoteSort: false,
				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'TargetApplianceName',title:'�ܼ�����',width:100,sortable:true,align:'center'}
				]],
				columns:[[
					{field:'StandardNameName',title:'���߱�׼����',width:120,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:120,align:'center'},
					{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:120,align:'center'},
					{field:'Range',title:'������Χ',width:120,align:'center'},
					{field:'Fee',title:'��׼����',width:80,align:'center'}
				]],
				pagination:true,
				toolbar:"#table_appli-search-toolbar"
			});
			
			var lastIndex;
			$('#table_item').datagrid({
				title:'Э����Ŀ',
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
					{field:'StandardNameId',title:'���߱�׼����ID',width:120,align:'center'},
					{field:'StandardName',title:'���߱�׼����',width:120,align:'center',sortable:true},
					{field:'Model',title:'�ͺŹ��',width:120,align:'center'},
					{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:120,align:'center'},
					{field:'Range',title:'������Χ',width:120,align:'center'},
					{field:'AppFactoryCode',title:'�������',width:80,align:'center'},
					{field:'AppManageCode',title:'������',width:80,align:'center'},
					{field:'Manufacturer',title:'���쳧',width:80,align:'center'},
					{field:'DealPrice',title:'Э�����',editor:'number',width:80,align:'center'},
					{field:'Remark',title:'��ע',width:80,editor:'text',align:'center'}
				]],
				rownumbers:false,
				pagination:false,
				toolbar:[{
					text:'ɾ����Ŀ',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("ȷ��Ҫ�Ƴ���Щ��Ŀ��");
						if(result == false){
							return false;
						}
						var row = $('#table_item').datagrid('getSelected');
						if(!row){
							$.messager.alert('��ʾ','��ѡ����Ҫ�Ƴ�����Ŀ��','info');
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
					text:'����Э��',
					iconCls:'icon-add',
					handler:function(){
						var result = confirm("ȷ��Ҫ����Э����");
						if(result == false){
							return false;
						}
						var rows = $('#table_item').datagrid('getRows');
						if(rows.length == 0){
							$.messager.alert('��ʾ','��Э����ĿΪ�գ���������Э�飡','info');
							return false;
						}
						
						$('#add_Quo').window('open');
						
					}
				},'-',{
					text:'��ɱ༭',
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
		
		//����Э����Ŀ�������ݵ���Ϣ������д
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
				$.messager.alert('��ʾ','��ѡ��һ������!','info');
			}
		}
		
		//��Э����Ŀ����datagrid
		function add(){
			if($('#DealPrice').val()==''){
				$.messager.alert('��ʾ','����дЭ����ѣ�','info');
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
		
		//��ѯ�ܼ�����
		function query(){
			$('#table_appli').datagrid('options').url='/jlyw/QuotationServlet.do?method=0';
			//$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getValue'))};
			$('#table_appli').datagrid('options').queryParams={'appname':encodeURI($('#Search_Appli').combobox('getText'))};
			$('#table_appli').datagrid('reload');

		}
		//ȡ����Э����Ŀ����datagrid��
		function cancel(){
			$('#add').dialog('close');
		}
		
		//����Э��
		function add_Quotation(){
		
			$('#form4').form('submit',{
				url:'/jlyw/DealServlet.do?method=2',
				onSubmit:function(){
				
					var rows = $('#table_item').datagrid('getRows');	
					if(rows.length==0){
						$.messager.alert('��ʾ','����ѡ��Э����Ŀ��','info');
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
						if (num > 0) { //��ѡ���ļ�
							doUploadByUploadify(attachment,'Attachment', false);
						}
						cancel_Quotation();
						$('#table_item').datagrid('loadData',{total:0,rows:[]});	
										
					}
				}
			});
			$('#form4').form('clear');
			
		}
		
		//ȡ��Э�����
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
			<jsp:param name="TitleName" value="Э�����" />
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
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="Add_QuotaItem()">����Э����Ŀ</a>
					</td>
					<td align="right">��������:</td>
				    <td width="18%"><input id="Search_PopularName" style="width:140px" name="Search_PopularName" type="text" /></td>
				    <td align="right">�ܼ����߱�׼��:</td>
				    <td width="21%"><input id="Search_Appli" style="width:152px" name="Search_Appli" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="query()">��ѯ</a></td>
				</tr>
			</table>
		</div>
	<div id="upload" class="easyui-window" title="�ϴ�" style="padding: 10px;width: 600px;" 
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
					<td align="center" style="padding-top: 20px;"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doUploadByDefault1()">�ϴ��ļ�</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">ȡ���ϴ�</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:closewindow()">�ر�</a> 
					</td>
				</tr>
				</table>
			 </form>
    	</div>
	   
    </div>
    
    	<div id="add" class="easyui-window" title="������Ŀ" style="padding: 10px;width: 800;"
		iconCls="icon-add" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form3" method="post">
				<table id="table1" style="padding: 30px;width: 800;height: 500;">
					<tr height="35px" style="padding-left:10px">
						<td align="right">�ܼ�����&nbsp;<br />��׼����:</td>
						<td align="left"><input id="StandardName" name="StandardName" type="text" /></td>
						<td align="right">�ͺŹ��:</td>
						<td align="left"><input id="Model" name="Model" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
					</tr>
					
					<tr height="35px" style="padding-left:10px">
						<td align="right">������Χ:</td>
						<td align="left"><input id="Range" name="Range" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
						<td align="right">׼ȷ�ȵȼ�:</td>
						<td align="left"><input id="Accuracy" name="Accuracy" class="easyui-combobox" type="text" style="width:152px" panelHeight
="auto"/></td>
					</tr>
					
					<tr height="35px" style="padding-left:10px">
						<td align="right">�������:</td>
						<td align="left"><input id="AppFactoryCode" name="AppFactoryCode" type="text"/></td>
                        <td align="right">������:</td>
						<td align="left"><input id="AppManageCode" name="AppManageCode" type="text" /></td>
					</tr>
					
					
					<tr height="35px" style="padding-left:10px">
						
						<td align="right">��&nbsp;��&nbsp;��:</td>
						<td align="left"><input id="Manufacturer" name="Manufacturer" type="text" /></td>
                        <td align="right">Э�����:</td>
						<td align="left"><input id="DealPrice" name="DealPrice" type="text" /></td>
					</tr>
					<tr height="35px" style="padding-left:10px">
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע:</td>
						<td align="left"><input id="Remark" name="Remark" type="text" /></td>
					</tr>
					
					<tr height="50px">	
						<td align="center" colspan="4"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="add()">����</a>&nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">�ر�</a></td>
						
						
					</tr>
				</table>
			</form>
		</div>	
		
		<div id="add_Quo" class="easyui-window" title="����Э��" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-add" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form4" method="post">
				<input id="Item" name="Item" type="hidden" />
				<table >
					<tr>
						<td align="right">Э�鵥λ:</td>
						<td align="left"><input id="CustomerName" name="CustomerName" style="width:152px;" required="true"/></td>
					</tr>
					<tr>
						<td align="right">Э�鵥λ&nbsp;<br />��&nbsp;ϵ&nbsp;��:</td>
						<td align="left"><input id="Contactor" name="Contactor" type="text"/></td>
						<td align="right">��&nbsp;ϵ&nbsp;��&nbsp;<br />��&nbsp;&nbsp;&nbsp;&nbsp;��:</td>
						<td align="left"><input id="ContactorTel" name="ContactorTel" type="text" maxlength="12" /></td>
						
					</tr>
                    <tr>
                    	<td align="right">Э��ǩ����:</td>
						<td align="left"><input id="Signer" name="Signer" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    	<td align="right">Э��ǩ������:</td>
						<td align="left"><input id="SignDate" name="SignDate" style="width:152px;" class="easyui-datebox" required="true"/></td>
                    </tr>
                    <tr>
                    	<td align="right">Э����Ч��:</td>
						<td align="left"><input id="Validity" name="Validity" style="width:152px;" class="easyui-datebox" required="true"/></td>
                    </tr>
                    <tr>
                    	<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬:</td>
						<td align="left"><select id="Status" name="Status" style="width:152px;" class="easyui-combobox" required="true">
                        					<option value="0" selected="selected">����</option>
                                            <option value="1">ע��</option>
                                        </select></td>
                    </tr>
					<tr>
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע:</td>
						<td align="left" colspan="3"><input id="Remark_1" name="Remark" type="text" /></td>
					</tr>
					<tr>
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��:</td>
						<td align="left" colspan="3"><input id="Attachment" name="Attachment" type="file"  style="width:420px" /></td>
					</tr>
					<tr height="50px">	
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="add_Quotation()">�ύ</a></td>
						<td align="center" colspan="2"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel_Quotation()">ȡ��</a></td>
					</tr>
				</table>
			</form>
		</div>
		
	</div>
	
</DIV></DIV>
</body>
</html>
