<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	 <title>ת������Ϣ��ɾ��</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<script>
		$(function(){
		    var lastIndex;		
			$('#contractor').datagrid({
				title:'ת������Ϣ',
//				iconCls:'icon-save',
				width:900,
				height:500,
				singleSelect:true, 
				//fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'/jlyw/SubContractorServlet.do?method=2',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'userid',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'��λ����',width:120,align:'center'},
					{field:'Brief',title:'��λ����',width:120,align:'center'},
					{field:'Code',title:'��λ����',width:80,align:'center'},
					{field:'Region',title:'����',width:80,align:'center'},
					{field:'ZipCode',title:'�ʱ�',width:80,align:'center'},
					{field:'Address',title:'��λ��ַ',width:180,align:'center'},
					{field:'Tel',title:'��λ�绰',width:80,align:'center'},
					{field:'Contactor',title:'��ϵ��',width:80,align:'center'},
					{field:'ContactorTel',title:'��ϵ�绰',width:80,align:'center'},
					{field:'Status',title:'��λ״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value=="0"||value==0)
						{
							rowData['Status']=="0";
							return "����";
						}
						else if(value=="1"||value==1)
						{
							rowData['Status']=="1";
							return "ע��";
						}
					}},
					{field:'CancelDate',title:'ע��ʱ��',width:100,align:'center'},
					{field:'CancelReason',title:'ע��ԭ��',width:100,align:'center'},
					{field:'ModifyDate',title:'����޸�����',width:100,align:'center'},
					{field:'Modificator',title:'�޸���',width:100,align:'center'},
					{field:'Copy',title:'����ɨ���',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
					}},
					{field:'Remark',title:'��ע',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					  text:'�޸�',
					  iconCls:'icon-edit',
					  handler:function(){
						var select = $('#contractor').datagrid('getSelected');
						if(select){
							$('#edit').window('open');
							$('#ff').show();
						
							$('#name').val(select.Name);
							$('#code').val(select.Code);
							$('#zipcode').val(select.ZipCode);
							$('#address').val(select.Address);
							$('#region').combobox('setValue',select.Region);
							$('#contactor').val(select.Contactor);
							$('#tel').val(select.Tel);
							$('#contactorTel').val(select.ContactorTel);
							$('#remark').val(select.Remark);
							$('#status').combobox('setValue',select.Status);
							$('#edit_id').val(select.Id);
						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
					text:'ע��',
					iconCls:'icon-remove',
					handler:function(){
						var select = $('#contractor').datagrid('getSelected');
						if (select){
								$('#del').window('open');
								$('#ff2').show();
								$('#del_id').val(select.Id);
								//$.ajax({
								//	type:'POST',
								//	url:'/jlyw/SubContractorServlet.do?method=4',
								//	data:'id='+select.Id,
								//	dataType:"json",
								//	success:function(data){
								//		$('#contractor').datagrid('reload');
								//	}
								//});

						}else{
							$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				}]
			});
		});
		
		$(function(){
		   		$('#Copy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
					'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
					'buttonImg' : '../uploadify/selectfiles.png',
					'fileDesc'  : '֧�ָ�ʽ:rar/zip/7z', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //����ĸ�ʽ
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('��ʾ',retData.msg,'error');
					},
					onAllComplete: function(event,data){
					}
				});
		});
		
		function query(){
			$('#contractor').datagrid('options').url='/jlyw/SubContractorServlet.do?method=2';
			$('#contractor').datagrid('options').queryParams={'name':encodeURI($('#queryname').val())};
			$('#contractor').datagrid('reload');
		}
		
		function edit(){
			$('#ff').form('submit',{
				url:'/jlyw/SubContractorServlet.do?method=3',
				onSubmit:function(){return $('#ff').form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
			   		 $.messager.alert('��ʾ',result.msg,'info');
			   		 if(result.IsOK)
					 {
						var Copy = result.Copy_filesetname;
						var num = $('#Copy').uploadifySettings('queueSize');
						if (num > 0) { //��ѡ���ļ�
							doUploadByUploadify(Copy,'Copy', false);
						}
			   		 	closed();
					 }
			   		 $('#contractor').datagrid('reload');
				}
			});
		}
		
		function closed(){
			$('#edit').dialog('close');
			$('#del').dialog('close');
		}
		
		function del(){
			$('#ff1').form('submit',{
				url:'/jlyw/SubContractorServlet.do?method=4',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
			   		 $.messager.alert('��ʾ',result.msg,'info');
			   		 if(result.IsOK)
			   		 	closed();
			   		 $('#contractor').datagrid('reload');	
				}
			});
		}
	</script>

</head>

<body >
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ת������Ϣ��ɾ��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<table width="900px">
		<tr>
			<td align="right">
				<div>
					<label for="queryname">ת������λ��:</label>
					<input id="queryname" type="text" name="queryname"></input>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onClick="query()">��ѯ</a>
				</div>
			</td>
		</tr>
	</table>
    <table id="contractor" iconCls="icon-edit"></table>
 <div id="edit" class="easyui-window" title="�޸�" style="width:700px;height:400px;" 
    iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
    	<div id="ee">
   		<form id="ff" method="post">
			 <table id="table1">
			 <input id="edit_id" name="edit_id" type="hidden"/>
				<tr height="30px">
					<td width="10%" align="right">��λ���ƣ�</td>
					<td width="22%" align="left">
						<input id="name" type="text" name="name" class="easyui-validatebox" required="true"/>
					</td>
					<td width="10%" align="right">��λ���룺</td>
				    <td width="21%" align="left"><input id="code" name="code" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="30px">
					<td width="10%" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td>
				    <td width="22%" align="left"><select id="region" name="region" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="150px" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" /></td>
					<td width="10%" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
				    <td width="22%" align="left"><input id="zipcode" name="zipcode" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="30px">
					<td width="10%"  align="right">��λ��ַ��</td>
					<td width="22%"  align="left"><input id="address" name="address" type="text" style="width:280px;" class="easyui-validatebox" required="true"  /></td>
					<td width="10%"  align="right">��λ�绰��</td>
					<td width="22%" align="left"><input id="tel" name="tel" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="30px">
					<td width="10%" align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
				    <td width="22%" align="left"><input id="contactor" name="contactor" type="text" class="easyui-validatebox" required="true" /></td>
					<td width="10%" align="right">��ϵ�绰��</td>
				    <td width="22%" align="left"><input id="contactorTel" name="contactorTel" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="30px">
					<td width="10%" align="right">��λ״̬��</td>
				    <td width="22%" colspan="3" align="left">
					    <select id="status" name="status" style="width:150px;" class="easyui-combobox" panelHeight="auto">                           
						     <option value="0">����</option>
							 <option value="1">ע��</option>
						</select>
					</td>
				</tr>
				<tr height="30px">
				    <td width="10%" align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
				    <td width="22%" colspan="3" align="left"><textarea id="remark" style="width:280px;height:100px"  name="remark" ></textarea></td>
				</tr>
                </form>
                <tr height="30px">
					<td width="10%" align="right">ת��������<br />ɨ&nbsp;��&nbsp;����</td>
					<td width="22%" align="left" colspan="3"><input id="Copy" type="file" name="Copy" style="width:350px"/></td>
				</tr>
				<tr height="50px">
					<td align="center" colspan="2">
						 <a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onClick="edit()">�޸�</a> 
					</td>
					<td align="center" colspan="2">
	                     <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="closed()">ȡ��</a>
					</td>
				</tr>
		</table>
	    
    	</div>
	    
    </div>	
  	  
    <div id="del" class="easyui-window" title="ע��" style="width:280px;height:130px;" 
    iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
    <form id="ff1" method="post">
    	<table>
    	<input id="del_id" name="del_id" type="hidden"/>
    	<tr>
    		<td>ע��ԭ��</td>
    		<td><input id="reason" class="easyui-combobox" name="reason" url="/jlyw/ReasonServlet.do?method=0&type=21" style="width:170px;" valueField="name" textField="name" panelHeight="auto" /></td>
    	</tr>
    	<tr height="40px">	
			<td><a href="#" class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)"  onclick="del()">ע��</a></td>
			<td><a href="#" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  onclick="closed()">ȡ��</a></td>
		</tr>
    	</table>
    </form>
    
    </div>


</div></div>
</body>
</html>
