<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>���߱�׼����Ϣ����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
		var nodekeep="";
		$(function(){
			$('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'103'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:xls/xml.', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
                'fileExt'   : '*.xls;*.xml',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('��ʾ',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table').datagrid('reload');
					CloseWaitingDlg();
				}
			 });
			  
			$('#tt').tree({
//				url:'/jlyw/ApplianceStandardNameServlet.do?method=2',
				dnd:true,
				onBeforeExpand:function(node){
					if(nodekeep.indexOf("," + node.id+",")==-1)
                    {
						$.ajax({
							type: "POST",
							url: "/jlyw/ApplianceStandardNameServlet.do?method=2&parentid=" + node.id,
							cache: false,
							async: false,
							dataType: "json",
							success: function(data) {
								append(data, node);
							}
						});
					}
				},
				onClick: function(node){
					if($(this).tree('isLeaf',node.target))  //��Ҷ�ӽڵ�
					{
						//ClickNode(node.attributes.url, node.attributes.title);
						editStandard(node);
					}
					else
					{
						$(this).tree('toggle', node.target);
					}
				},
				onContextMenu: function(e, node){
					e.preventDefault();
					$('#tt').tree('select', node.target);
					if(node.attributes.type==0){          //���ͣ��������߱�׼���ƹ���0--���߷��࣬1--���߱�׼��
						$('#mm').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
					}
					else if(node.attributes.type==1){
						$('#mm1').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
					}
				},
				onDrop:function (target, source, point){
					if(!$(this).tree('isLeaf',source.target)){
						return false;
					}
					var targetNode = $('#tt').tree('getNode', target);
					$('#SpeciesId1').val(targetNode.text);
					$('#SpeciesId1_1').val(targetNode.id);
					editStandard(source);
				}
			});
			
			$('#popularName').datagrid({
	 			title:'��������',
	 			width:390,
	 			height:300,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
				url:'',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'StandardName',title:'��׼����',width:100,align:'center'},
					{field:'Name',title:'��������',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1||rowData.Status=='1')
							return '<span style="color:red">' + value + '</span>';
						else
							return value;
					}},
					{field:'Brief',title:'ƴ������',width:120,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">ע��</span>';
							}
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'����',
					iconCls:'icon-add',
					handler:function(){
							var node = $('#tt').tree('getSelected');
							if(node==null||node.attributes.type==0)
							{
								$.messager.alert('��ʾ','����ѡ��һ����׼����','warning');
								return;
							}
							$('#add_popularname').window('open');
							$('#frm_add_popularname').show();
					}
				},'-',{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#popularName').datagrid('getSelected');
						if(select){
							$('#edit_popularname').window('open');
							$('#frm_edit_popularname').show();
							
							$('#PopularName').val(select.Name);
							$('#PopularBrief').val(select.Brief);
							$('#PopularStatus').combobox('setValue',select.Status);
							$('#Id').val(select.Id);
						}
						else
						{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#popularName').datagrid('getSelected');
							if(select)
							{
								$.messager.confirm('��ʾ','ȷ��ע����',function(r){
								if(r){
										$.ajax({
											type:'POST',
											url:'/jlyw/AppliancePopularNameServlet.do?method=4',
											data:'id='+select.Id,
											dataType:"html",
											success:function(data, textStatus){
												$('#popularName').datagrid('reload');
											}
										});
									
								}
								});
							}else{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
						text:'�������г�������',
						iconCls:'icon-save',
						handler:function(){
							ExportPopularName();
						}
				}]
	 		});
			$('#manufacturer').datagrid({
	 			title:'��������',
	 			width:390,
	 			height:300,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
				url:'',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'StandardName',title:'��׼����',width:100,align:'center'},
					{field:'Name',title:'�ܼ��������쳧',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1||rowData.Status=='1')
							return '<span style="color:red">' + value + '</span>';
						else
							return value;
					}},
					{field:'Brief',title:'ƴ������',width:120,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">ע��</span>';
							}
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'����',
					iconCls:'icon-add',
					handler:function(){
							var node = $('#tt').tree('getSelected');
							if(node==null||node.attributes.type==0)
							{
								$.messager.alert('��ʾ','����ѡ��һ����׼����','warning');
								return;
							}
							$('#add_manufacturer').window('open');
							$('#frm_add_manufacturer').show();
					}
				},'-',{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#manufacturer').datagrid('getSelected');
						if(select){
							$('#edit_manufacturer').window('open');
							$('#frm_edit_manufacturer').show();
							
							$('#ManufacturerName').val(select.Name);
							$('#ManufacturerBrief').val(select.Brief);
							$('#ManufacturerStatus').combobox('setValue',select.Status);
							$('#ManufacturerId').val(select.Id);
						}
						else
						{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#manufacturer').datagrid('getSelected');
							if(select)
							{
								$.messager.confirm('��ʾ','ȷ��ע����',function(r){
								if(r){
										$.ajax({
											type:'POST',
											url:'/jlyw/ApplianceManufacturerServlet.do?method=4',
											data:'id='+select.Id,
											dataType:"html",
											success:function(data, textStatus){
												$('#manufacturer').datagrid('reload');
											}
										});
									
								}
								});
							}else{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
						text:'����������������',
						iconCls:'icon-save',
						handler:function(){
							ExportManufacturer();
						}
				}]
	 		});
			
			$.ajax({
				type: "POST",
                url: "/jlyw/ApplianceStandardNameServlet.do?method=2",
                cache: false,
                async: false,
                dataType: "json",
                success: function(data) {
                     	$('#tt').tree('loadData',data);
                }
			});
			
		})
		
		function appendStandard(){   //�Ҽ��˵�������һ����������߱�׼����
			$('#form2').hide();
			$('#form1').show();
			var node = $('#tt').tree('getSelected');
			$('#SpeciesId').val(node.text);
			$('#SpeciesId_1').val(node.id);
			
			$('#popularName').datagrid('options').url='';
			$('#popularName').datagrid('reload');
			
			$('#manufacturer').datagrid('options').url='';
			$('#manufacturer').datagrid('reload');
			
			//����ģ���ļ���Ϣ
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
			$('#uploaded_file_table').datagrid('reload');
			
		}
		
		function editStandard(node){      //�Ҽ��˵����Ե��нڵ���Ϣ�����޸�
			$('#form2').show();
			$('#form1').hide();
			$('#id').val(node.id);
			$('#Name1').val(node.attributes.Name);
			$('#NameEn1').val(node.attributes.NameEn);
			$('#Brief1').val(node.attributes.Brief);
			$('#SpeciesId1').val($('#tt').tree('getParent',node.target).text);
	    	$('#SpeciesId1_1').val(node.attributes.ParentId);
	    	$('#HoldMany1').combobox('setValue',node.attributes.HoldMany);
	    	$('#Status1').combobox('setValue',node.attributes.Status);
			
			var clickname=node.attributes.Name;
			clickname="��׼���� '"+clickname+"' �������ơ��������̹���";
			$('#p2').panel({title:clickname});
			
			$('#newStandardNameId').val(node.id);
			$('#StandardNameId').val(node.id);
			$('#popularName').datagrid('options').url='/jlyw/AppliancePopularNameServlet.do?method=2';
			$('#popularName').datagrid('options').queryParams={'StandardNameId':node.id};
			$('#popularName').datagrid('reload');
			
			$('#newManufacturerStandardNameId').val(node.id);
			$('#ManufacturerStandardNameId').val(node.id);
			$('#manufacturer').datagrid('options').url='/jlyw/ApplianceManufacturerServlet.do?method=2';
			$('#manufacturer').datagrid('options').queryParams={'StandardNameId':node.id};
			$('#manufacturer').datagrid('reload');
			
			//����ģ���ļ���Ϣ
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			$('#uploaded_file_table').datagrid('reload');
		}
		
		//�����ӽڵ�
		function append(datas,cnode) {
		     var node = cnode;
		     $('#tt').tree('append', {
		        parent: node.target,
		        data: datas
		    });
		   nodekeep += "," + node.id + ",";
		}
		
		
		function cancel(){
			$('#Name').val('');
			$('#NameEn').val('');
			$('#SpeciesId').val('');
			$('#SpeciesId_1').val('');
			$('#HoldMany').val('');
			$('#Status').val('');
			$('#Name1').val('');
			$('#NameEn1').val('');
			$('#Brief1').val('');
			$('#SpeciesId1').val('');
			$('#HoldMany1').val('');
			$('#Status1').val('');
		}
		
		function reload(){
			var node = $('#tt').tree('getSelected');
			if (node){
				$('#tt').tree('options').url="/jlyw/ApplianceStandardNameServlet.do?method=2&parentid=" + node.id;
				$('#tt').tree('reload', node.target);
			} else {
				$('#tt').tree('options').url="/jlyw/ApplianceStandardNameServlet.do?method=2";
				$('#tt').tree('reload');
			}
		}
		
		//������׼����
		function add(){
			$('#form1').form('submit',{
				url:'/jlyw/ApplianceStandardNameServlet.do?method=1',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
					{
						cancel();
						reload();
					}
				}
			});
		}
		
		//�޸ı�׼����
		function edit(){
			$('#form2').form('submit',{
				url:'/jlyw/ApplianceStandardNameServlet.do?method=3',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
					{
						var node = $('#tt').tree('getSelected');
						var parent = $('#tt').tree('getParent',node.target);
						$('#tt').tree('options').url="/jlyw/ApplianceStandardNameServlet.do?method=2&parentid=" + parent.id;
						$('#tt').tree('reload', parent.target);
						cancel();
					}
				}
			});
		}
		
			//������������
			function addPopularName(){
				$('#frm_add_popularname').form('submit',{
					url:'/jlyw/AppliancePopularNameServlet.do?method=1',
					onSubmit:function(){},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('��ʾ',result.msg,'info');
						if(result.IsOK)
						{
							$('#popularName').datagrid('reload');
							closed();
						}
					}
				});
			}
			
			//�޸ĳ�������
			function editPopularName(){
				$('#frm_edit_popularname').form('submit',{
					url:'/jlyw/AppliancePopularNameServlet.do?method=3',
					onSubmit:function(){},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('��ʾ',result.msg,'info');
						if(result.IsOK)
						{
							$('#popularName').datagrid('reload');
							closed();
						}
					}
				});
			}
			
			//������������
			function addManufacturer(){
				$('#frm_add_manufacturer').form('submit',{
					url:'/jlyw/ApplianceManufacturerServlet.do?method=1',
					onSubmit:function(){},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('��ʾ',result.msg,'info');
						if(result.IsOK)
						{
							$('#manufacturer').datagrid('reload');
							closed();
						}
					}
				});
			}
			
			//�޸���������
			function editManufacturer(){
				$('#frm_edit_manufacturer').form('submit',{
					url:'/jlyw/ApplianceManufacturerServlet.do?method=3',
					onSubmit:function(){},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('��ʾ',result.msg,'info');
						if(result.IsOK)
						{
							$('#manufacturer').datagrid('reload');
							closed();
						}
					}
				});
			}
		
		function closed(){
			$('#add_popularname').dialog('close');
			$('#edit_popularname').dialog('close');
			$('#add_manufacturer').dialog('close');
			$('#edit_manufacturer').dialog('close');
		}
		
		function getBrief()
		{
			$('#Brief').val(makePy($('#Name').val()));
		}
		
		function getBrief1()
		{
			$('#Brief1').val(makePy($('#Name1').val()));
		}
		
		function getnewPopularNameBrief()
		{
			$('#newPopularBrief').val(makePy($('#newPopularName').val()));
		}
		
		function getPopularNameBrief()
		{
			$('#PopularBrief').val(makePy($('#PopularName').val()));
		}
		
		function getnewManufacturerBrief()
		{
			$('#newManufacturerBrief').val(makePy($('#newManufacturerName').val()));
		}
		
		function getManufacturerBrief()
		{
			$('#ManufacturerBrief').val(makePy($('#ManufacturerName').val()));
		}
		
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#frm_export').form('submit',{
				url:'/jlyw/ApplianceStandardNameServlet.do?method=8',
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
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
		function ExportManufacturer(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#frm_export').form('submit',{
				url:'/jlyw/ApplianceManufacturerServlet.do?method=5',
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
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
		function ExportPopularName(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#frm_export').form('submit',{
				url:'/jlyw/AppliancePopularNameServlet.do?method=6',
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
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
	</script>
</head>
<body>
<form id="frm_export" method="post">
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="���߱�׼���ƹ���" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV" style="left:0px; margin-left:0px">
<DIV class="easyui-layout" fit="true" nohead="true"   >
	
	<div region="west" style="width:185px;height:600px;border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="�ܼ����߱�׼����">
		<ul id="tt"></ul>
 	</div>

	
	
  	<div region="center"  id="add" name="add"  iconCls="icon-add" closed="false" maximizable="false" minimizable="false" collapsible="false" style="+position:relative;height:600px">
		<form id="form1"  method="post">
			<table id="table1" class="easyui-panel" title="������߱�׼����" style="width:800px; height:200px; padding-top:10px">
				<tr>
					<td align="right">��׼���ƣ�</td>
					<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
					<td align="right">ƴ�����룺</td>
					<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr>
                	<td align="right">Ӣ�����ƣ�</td>
					<td align="left"><input id="NameEn" name="NameEn" type="text" class="easyui-validatebox" required="true" /></td>
					<td align="right">�������ࣺ</td>
					<td align="left"><input id="SpeciesId" name="Species" type="text" class="easyui-validatebox" required="true" />
									<input id="SpeciesId_1" name="SpeciesId" type="text" class="easyui-validatebox" required="true" style="display:none" />
					</td>
				</tr>
				<tr>
                	<td align="right">һ֤�����</td>
					<td align="left">
						<select id="HoldMany" name="HoldMany" class="easyui-combobox" style="width:152px" panelHeight="auto">
							<option value="0">������</option>
							<option value="1">����</option>
						</select>
					</td>
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left">
                        <select id="Status" name="Status" type="text" class="easyui-combobox" required="true" style="width:152px" panelHeight="auto">
                            <option value="0">����</option>
                            <option value="1">ע��</option>
                        </select>
                    </td>
				</tr>
				<tr>
                		<td></td>
						<td align="center">
							<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="add()">ȷ��¼��</a>
						</td>
						<td align="left">
							<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a>
						</td>
                        <td align="left">
							<a class="easyui-linkbutton" icon="icon-save" name="Add" href="javascript:void(0)" onclick="myExport()">�������б�׼����</a>
						</td>
                        
					</tr>
			</table>
		</form>
		<form id="form2" style="display:none" method="post">
		<input id="id" name="id" type="hidden"/>
			<table id="table2" class="easyui-panel" title="�޸����߱�׼����" style="width:800px; height:200px; padding-top:10px">
				<tr>
					<td align="right">��׼���ƣ�</td>
					<td align="left"><input id="Name1" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief1()"/></td>
					<td align="right">ƴ�����룺</td>
					<td align="left"><input id="Brief1" name="Brief" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr>
                	<td align="right">Ӣ�����ƣ�</td>
					<td align="left"><input id="NameEn1" name="NameEn" type="text" class="easyui-validatebox" required="true" /></td>
					<td align="right">�������ࣺ</td>
					<td align="left"><input id="SpeciesId1" name="Species" type="text" class="easyui-validatebox" required="true" />
									<input id="SpeciesId1_1" name="SpeciesId" type="text" class="easyui-validatebox" required="true" style="display:none" />
					</td>
				</tr>
				<tr>
                	<td align="right">һ֤�����</td>
					<td align="left">
						<select id="HoldMany1" name="HoldMany" class="easyui-combobox" style="width:152px" panelHeight="auto">
							<option value="0">������</option>
							<option value="1">����</option>
						</select>
					</td>
					<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
					<td align="left">
                        <select id="Status1" name="Status" type="text" class="easyui-combobox" required="true" style="width:152px" panelHeight="auto">
                            <option value="0">����</option>
                            <option value="1">ע��</option>
                        </select>
                    </td>
				</tr>
				<tr height="50px">
                	     <td></td>
						<td align="center">
							<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="edit()">ȷ���޸�</a>
						</td>
						<td align="left">
							<a class="easyui-linkbutton" icon="icon-reload" name="Refresh" href="javascript:void(0)" onclick="cancel()">����</a>
						</td>
                        <td align="left">
							<a class="easyui-linkbutton" icon="icon-save" name="Add" href="javascript:void(0)" onclick="myExport()">�������б�׼����</a>
						</td>

					</tr>
			</table>
		</form>
        <br/>
        <div id="p2" class="easyui-panel" style="width:800px;height:350px;padding:10px;"
				title="�������ơ��������̹���" collapsible="false"  closable="false">
			<table width="100%" height="100%">
			<tr><td>
			<table id="popularName" class="easyui-datagrid"></table>
			 </td><td>
            <table id="manufacturer" class="easyui-datagrid"></table>
             </td></tr>
			</table>
		</div>
            
		<br/>
		<div id="p4" class="easyui-panel" style="width:800px;height:250px;"
				title="ģ���ļ���Excel��XML�ļ����ϴ�" collapsible="false"  closable="false" scroll="no">
			<table width="100%" height="100%" >
				<tr>
					<td width="57%" rowspan="2">
						<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=103"></table>
					</td>
				 	<td width="43%" height="175" valign="top" align="left" style="overflow:hidden">
					  <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
					</td>
				</tr>
				<tr>
				  	<td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByDefault()">�ϴ��ļ�</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">ȡ���ϴ�</a> </td>
				</tr>
			</table>
	   </div>
       
       <div id="add_popularname" class="easyui-window" title="������������" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_popularname" method="post">
				<div>
                <input id="newStandardNameId" name="StandardNameId" type="hidden"/>
					<table>
						<tr>
							<td align="right">�������ƣ�</td>
							<td align="left"><input id="newPopularName" name="Name" class="easyui-validatebox" required="true" onchange="getnewPopularNameBrief()"/></td>
							<td align="right">ƴ�����룺</td>
							<td align="left"><input id="newPopularBrief" name="Brief" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
							<td align="left">
								<select id="newPopularStatus" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">����</option>
									<option value="1">ע��</option>
								</select>
							</td>
                        </tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="addPopularName()">����</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
       
       <div id="edit_popularname" class="easyui-window" title="�޸ĳ�������" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_popularname" method="post">
				<div>
					<table>
					<input id="Id" name="Id" type="hidden"/>
                    <input id="StandardNameId" name="StandardNameId" type="hidden"/>
						<tr>
							<td align="right">�������ƣ�</td>
							<td align="left"><input id="PopularName" name="Name" class="easyui-validatebox" required="true" onchange="getPopularNameBrief()"/></td>
							<td align="right">ƴ�����룺</td>
							<td align="left"><input id="PopularBrief" name="Brief" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
							<td align="left">
								<select id="PopularStatus" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">����</option>
									<option value="1">ע��</option>
								</select>
							</td>
                        </tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="editPopularName()">�޸�</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
       
       <div id="add_manufacturer" class="easyui-window" title="������������" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_manufacturer" method="post">
				<div>
                <input id="newManufacturerStandardNameId" name="StandardNameId" type="hidden"/>
					<table>
						<tr>
							<td align="right">�������̣�</td>
							<td align="left"><input id="newManufacturerName" name="Name" class="easyui-validatebox" required="true" onchange="getnewManufacturerBrief()"/></td>
							<td align="right">ƴ�����룺</td>
							<td align="left"><input id="newManufacturerBrief" name="Brief" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
							<td align="left">
								<select id="newManufacturerStatus" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">����</option>
									<option value="1">ע��</option>
								</select>
							</td>
                        </tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="addManufacturer()">����</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
       
       <div id="edit_manufacturer" class="easyui-window" title="�޸���������" style="padding: 10px;width: 500px;height: 180px;overflow:hidden"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_manufacturer" method="post">
				<div>
					<table>
					<input id="ManufacturerId" name="Id" type="hidden"/>
                    <input id="ManufacturerStandardNameId" name="StandardNameId" type="hidden"/>
						<tr>
							<td align="right">�������̣�</td>
							<td align="left"><input id="ManufacturerName" name="Name" class="easyui-validatebox" required="true" onchange="getManufacturerBrief()"/></td>
							<td align="right">ƴ�����룺</td>
							<td align="left"><input id="ManufacturerBrief" name="Brief" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
							<td align="left">
								<select id="ManufacturerStatus" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto"  editable="false">
									<option value="0">����</option>
									<option value="1">ע��</option>
								</select>
							</td>
                        </tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="editManufacturer()">�޸�</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
       
       
	</div>
	
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="appendStandard()" iconCls="icon-add">��ӱ�׼��</div>
	</div>
	
	<div id="mm1" class="easyui-menu" style="width:120px;">
		<div onclick="editStandard()" iconCls="icon-edit">�޸ı�׼��</div>
	</div>
    
</DIV></DIV></DIV>

</body>

</html>
