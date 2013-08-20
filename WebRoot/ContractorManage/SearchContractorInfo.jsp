<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	 <title>转包方信息查删改</title>
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
				title:'转包方信息',
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
					{field:'Name',title:'单位名称',width:120,align:'center'},
					{field:'Brief',title:'单位名称',width:120,align:'center'},
					{field:'Code',title:'单位代码',width:80,align:'center'},
					{field:'Region',title:'地区',width:80,align:'center'},
					{field:'ZipCode',title:'邮编',width:80,align:'center'},
					{field:'Address',title:'单位地址',width:180,align:'center'},
					{field:'Tel',title:'单位电话',width:80,align:'center'},
					{field:'Contactor',title:'联系人',width:80,align:'center'},
					{field:'ContactorTel',title:'联系电话',width:80,align:'center'},
					{field:'Status',title:'单位状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value=="0"||value==0)
						{
							rowData['Status']=="0";
							return "正常";
						}
						else if(value=="1"||value==1)
						{
							rowData['Status']=="1";
							return "注销";
						}
					}},
					{field:'CancelDate',title:'注销时间',width:100,align:'center'},
					{field:'CancelReason',title:'注销原因',width:100,align:'center'},
					{field:'ModifyDate',title:'最近修改日期',width:100,align:'center'},
					{field:'Modificator',title:'修改者',width:100,align:'center'},
					{field:'Copy',title:'资质扫描件',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='点击下载该文件' >"+value.filename+"</a>";
					}},
					{field:'Remark',title:'备注',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					  text:'修改',
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
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				},'-',{
					text:'注销',
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
							$.messager.alert('提示','请选择一行数据','warning');
						}
					}
				}]
			});
		});
		
		$(function(){
		   		$('#Copy').uploadify({
					'script'    : '/jlyw/FileUploadServlet.do',
					'scriptData':{'method':'1','FileType':'106'},	//method必须放在这里，不然会与其他的参数连着，导致出错
					'method'    :'GET',	//需要传参数必须改为GET，默认POST
		//				'folder'    : '../../UploadFile',
					'queueSizeLimit': 1,//一次只能传一个文件
					'buttonImg' : '../uploadify/selectfiles.png',
					'fileDesc'  : '支持格式:rar/zip/7z', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
					'fileExt'   : '*.rar;*.zip;*.7z;',   //允许的格式
					onComplete: function (event,ID,fileObj,response,data) {  
						var retData = eval("("+response+")");
						if(retData.IsOK == false)
							$.messager.alert('提示',retData.msg,'error');
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
			   		 $.messager.alert('提示',result.msg,'info');
			   		 if(result.IsOK)
					 {
						var Copy = result.Copy_filesetname;
						var num = $('#Copy').uploadifySettings('queueSize');
						if (num > 0) { //有选择文件
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
			   		 $.messager.alert('提示',result.msg,'info');
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
			<jsp:param name="TitleName" value="转包方信息查删改" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<table width="900px">
		<tr>
			<td align="right">
				<div>
					<label for="queryname">转包方单位名:</label>
					<input id="queryname" type="text" name="queryname"></input>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onClick="query()">查询</a>
				</div>
			</td>
		</tr>
	</table>
    <table id="contractor" iconCls="icon-edit"></table>
 <div id="edit" class="easyui-window" title="修改" style="width:700px;height:400px;" 
    iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
    	<div id="ee">
   		<form id="ff" method="post">
			 <table id="table1">
			 <input id="edit_id" name="edit_id" type="hidden"/>
				<tr height="30px">
					<td width="10%" align="right">单位名称：</td>
					<td width="22%" align="left">
						<input id="name" type="text" name="name" class="easyui-validatebox" required="true"/>
					</td>
					<td width="10%" align="right">单位代码：</td>
				    <td width="21%" align="left"><input id="code" name="code" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="30px">
					<td width="10%" align="right">地&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
				    <td width="22%" align="left"><select id="region" name="region" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="150px" mode="remote" url="/jlyw/RegionServlet.do?method=2" required="true" editable="false" /></td>
					<td width="10%" align="right">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
				    <td width="22%" align="left"><input id="zipcode" name="zipcode" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="30px">
					<td width="10%"  align="right">单位地址：</td>
					<td width="22%"  align="left"><input id="address" name="address" type="text" style="width:280px;" class="easyui-validatebox" required="true"  /></td>
					<td width="10%"  align="right">单位电话：</td>
					<td width="22%" align="left"><input id="tel" name="tel" type="text" class="easyui-validatebox" required="true"  /></td>
				</tr>
				<tr height="30px">
					<td width="10%" align="right">联&nbsp;系&nbsp;人：</td>
				    <td width="22%" align="left"><input id="contactor" name="contactor" type="text" class="easyui-validatebox" required="true" /></td>
					<td width="10%" align="right">联系电话：</td>
				    <td width="22%" align="left"><input id="contactorTel" name="contactorTel" type="text" class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr height="30px">
					<td width="10%" align="right">单位状态：</td>
				    <td width="22%" colspan="3" align="left">
					    <select id="status" name="status" style="width:150px;" class="easyui-combobox" panelHeight="auto">                           
						     <option value="0">正常</option>
							 <option value="1">注销</option>
						</select>
					</td>
				</tr>
				<tr height="30px">
				    <td width="10%" align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
				    <td width="22%" colspan="3" align="left"><textarea id="remark" style="width:280px;height:100px"  name="remark" ></textarea></td>
				</tr>
                </form>
                <tr height="30px">
					<td width="10%" align="right">转包方资质<br />扫&nbsp;描&nbsp;件：</td>
					<td width="22%" align="left" colspan="3"><input id="Copy" type="file" name="Copy" style="width:350px"/></td>
				</tr>
				<tr height="50px">
					<td align="center" colspan="2">
						 <a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onClick="edit()">修改</a> 
					</td>
					<td align="center" colspan="2">
	                     <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="closed()">取消</a>
					</td>
				</tr>
		</table>
	    
    	</div>
	    
    </div>	
  	  
    <div id="del" class="easyui-window" title="注销" style="width:280px;height:130px;" 
    iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
    <form id="ff1" method="post">
    	<table>
    	<input id="del_id" name="del_id" type="hidden"/>
    	<tr>
    		<td>注销原因：</td>
    		<td><input id="reason" class="easyui-combobox" name="reason" url="/jlyw/ReasonServlet.do?method=0&type=21" style="width:170px;" valueField="name" textField="name" panelHeight="auto" /></td>
    	</tr>
    	<tr height="40px">	
			<td><a href="#" class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)"  onclick="del()">注销</a></td>
			<td><a href="#" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)"  onclick="closed()">取消</a></td>
		</tr>
    	</table>
    </form>
    
    </div>


</div></div>
</body>
</html>
