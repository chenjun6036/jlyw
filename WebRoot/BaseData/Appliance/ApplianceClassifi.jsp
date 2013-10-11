<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>受检器具分类管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/letter.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
	<script>
		var nodekeep="";
		$(function(){
			
		    //var lastIndex;
		    $('#table').treegrid({
				title:'受检器具分类管理',
				height:550,
				rownumbers: false,
				animate:false,
				//url:'/jlyw/ApplianceSpeciesServlet.do?method=2',
				idField:'id',//id=productId_propId_largetypeId_midlletypeId_comtypeId;
				treeField:'Name',
				frozenColumns:[[
					{title:'名称',field:'Name',width:300,editor:'text',
	                  	formatter:function(name){
					  		return '<span>'+name+'</span>';
	                 	}
	                 }
				]],
				columns:[[
					{title:'分类编号',field:'id',width:60,editor:'text',align:'center'},
				 	{field:'Status',title:'状态',width:100,align:'center',editor:'text',
						formatter:function(Status){
							if(Status==1 || Status=="1")
								return '<span style="color:red">'+'注销'+'</span>';
							else if(Status==0 || Status=="0") 
								return'<span>'+'正常'+'</span>';
						}
					},
					{title:'排序号',field:'Sequence',width:60,editor:'text',align:'center'}
				 ]],
				toolbar:[{
					text:'导出',
					iconCls:'icon-save',
					handler:function(){
						myExport();
					}
				}],
				onBeforeExpand: function(node){
					 if(nodekeep.indexOf("," + node.id+",")==-1)
	                 {
						$.ajax({
							type: "POST",
	                		url: "/jlyw/ApplianceSpeciesServlet.do?method=2&parentid=" + node.id,
	                 		cache: false,
	                 		async: false,
	                 		dataType: "json",
	                 		success: function(data) {
	                     		appendnode(data, node.id);
	                		}
						});
					}
				},
				onContextMenu: function(e, node){
						e.preventDefault();
						$('#table').treegrid('select', node.target);
						$('#mm').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
				},
				onClickRow: function(node){
					if($(this).treegrid('isLeaf', node.id))  //是叶子节点
					{
						//ClickNode(node.attributes.url, node.attributes.title);
					}
					else
					{
						$(this).treegrid('toggle', node.id);
					}
				}
		   });
		   
		   $.ajax({
				type: "POST",
                url: "/jlyw/ApplianceSpeciesServlet.do?method=2",
                cache: false,
                async: false,
                dataType: "json",
                success: function(data) {
                     $('#table').treegrid('loadData',data);
                }
			});
		   
		  });
		  
		 //增加子节点
		function appendnode(datas,cnodeId) {
		     //var nodeId = cnodeId;
		     $('#table').treegrid('append', {
		        parent: cnodeId,
		        data: datas
		    });
		   nodekeep += "," + cnodeId +",";
		}
		  
		function append(){           //右击菜单新增
			var node = $('#table').treegrid('getSelected');
			if(node==null)
				return;
			$('#add').window('open');			
						
			$('#ParentId').val(node.id);
			$('#ParentName').val(node.Name);
	//		alert(node._parentId);
		}
		
		function edit(){            //右击菜单修改
			var node = $('#table').treegrid('getSelected');
			if(node==null)
				return;
			$('#edit').window('open');
				
			$('#Name1').val(node.Name);
			$('#Brief1').val(node.Brief);
			var parent = $('#table').treegrid('getParent',node.id);
			if(parent!=null)
			{
				$('#ParentName1').val(parent.Name);		
				$('#ParentId1').val(parent.id);
			}
			$('#Sequence1').val(node.Sequence);
			$('#Status1').combobox('setValue',node.Status);
			$('#Id').val(node.id);
		}
		
		function logout(){    //右击菜单注销（未完成）
			var node=$('#table').treegrid('getSelected');
			if(node==null)
				return;
			$.ajax({
				type:'POST',
				url:'/jlyw/ApplianceSpeciesServlet.do?method=4',
				data:'id='+node.id,
				dataType:"html",
				cache: false,
	            async: false,
				success:function(data){
				var result = eval("("+data+")");
				//	alert(result.msg);
		   			if(result.IsOK)
		   			{
						node.Status = 1;
						$('#table').treegrid('refresh',node.id);
					}
				}
			});
		}
		
		function add(){
			$('#form1').form('submit',{
				url:'/jlyw/ApplianceSpeciesServlet.do?method=1',
				onSubmit:function(){return $('#form1').form('validate');},
				success:function(data){
					var result = eval("(" + data + ")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
		   			{
		   				closedAdd();
		   				reload();
		   			}
				}
			});
		}
		
		function editSubmit(){
			$('#form2').form('submit',{
				url:'/jlyw/ApplianceSpeciesServlet.do?method=3',
				onSubmit:function(){return $('#form2').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');
		   			if(result.IsOK)
					{
		   				closedEdit();
						var node = $('#table').treegrid('getSelected');				
						var parent = $('#table').treegrid('getParent',node.id);
						if(parent!=null)
						{
							replaceNodekeep(parent);
							$('#table').treegrid('options').url="/jlyw/ApplianceSpeciesServlet.do?method=2&parentid=" + parent.id;
							$('#table').treegrid('reload', parent.id);
						}
						else
						{
							$('#table').treegrid('options').url="/jlyw/ApplianceSpeciesServlet.do?method=2";
							$('#table').treegrid('reload');
						}
						/*nodekeep = nodekeep.replace(","+node.id+",","");
						nodekeep = nodekeep.replace(","+parent.id+",","");*/
					}
				}
			});
		}
		
		function replaceNodekeep(node)
		{
			nodekeep = nodekeep.replace(","+node.id+",","");
			var children = $('#table').treegrid('getChildren',node.id);
			for(var i = 0;i<children.length;i++)
			{
				nodekeep = nodekeep.replace(","+children[i].id+",","");
				replaceNodekeep(children[i]);
			}
		}
		
		function closedAdd(){
			$('#add').dialog('close');
		}
		
		function closedEdit(){
			$('#edit').dialog('close');
		}
		
		function reload(){		
			if ($('#ParentId').val()!=""){
				//$('#table').treegrid('unselectAll');
				//$('#table').treegrid('select',$('#ParentId').val());
				//var node = $('#table').treegrid('getSelected');
				//$('#table').treegrid('unselect',$('#ParentId').val());
				if($('#table').treegrid('getChildren',$('#ParentId').val()).length==0)
				{
					$.ajax({
						type: "POST",
		                url: "/jlyw/ApplianceSpeciesServlet.do?method=2&parentid=" + $('#ParentId').val(),
		                cache: false,
		                async: false,
		                dataType: "json",
		                success: function(data) {
		                     appendnode(data,$('#ParentId').val());
		                }
					});
				}
				else
				{
					$('#table').treegrid('options').url="/jlyw/ApplianceSpeciesServlet.do?method=2&parentid=" + $('#ParentId').val();
					$('#table').treegrid('reload', $('#ParentId').val());
				}
			} else {
				$('#table').treegrid('options').url="/jlyw/ApplianceSpeciesServlet.do?method=2";
				$('#table').treegrid('reload');
			}
			//nodekeep = nodekeep.replace(","+$('#ParentId').val()+",","");
		}
		
		function getAddBrief()
		{
			$('#Brief').val(makePy($('#Name').val()));
		}
		
		function getBrief()
		{
			$('#Brief1').val(makePy($('#Name1').val()));
		}
		
		function myExport(){
			ShowWaitingDlg("正在导出，请稍后......");
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
		
		</script>

</head>

<body>
<form id="frm_export" method="post" action="/jlyw/ApplianceSpeciesServlet.do?method=5">
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="受检器具分类管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div style="width:900px"  region="center">
		<table id="table" style="height:500px; width:800px"></table>
		
		<div id="add" class="easyui-window" title="添加分类" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-add" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="form1" method="post">
				<div>
					<table id="table2" iconCls="icon-add" >
						<tr height="30px">
							<td align="right">分&nbsp;类&nbsp;名：</td>
							<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getAddBrief()"/></td>
                            <td align="right">拼音简码：</td>
							<td align="left"><input id="Brief" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr height="30px">
                        	<td align="right" >父分类编号：</td>
							<td align="left" ><input id="ParentId" name="ParentId" type="text"/></td>
                        	<td align="right" >父分类名称：</td>
							<td align="left" ><input id="ParentName" name="ParentName" type="text" readonly="readonly"/></td>
						</tr>
                        <tr>
                       		<td align="right">序&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
							<td align="left">
                            	<input id="Sequence" name="Sequence" class="easyui-numberbox" required="true"/>
                            </td>
                        	<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
							<td align="left">
                            	<select id="Status" name="Status" class="easyui-combobox" style="width:151px" required="true" panelHeight="auto">
									<option value="0">正常</option>
									<option value="1">注销</option>
								</select>
                            </td>
                        </tr>
						<tr height="50px">
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="add()">新增</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closedAdd()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div id="edit" class="easyui-window" title="修改分类" style="padding: 10px;width: 500px;height: 200px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="form2" method="post">
				<div>
					<table id="table3" iconCls="icon-edit" >
					<input id="Id" name="Id" type="hidden"/>
						<tr height="30px">
							<td align="right">分&nbsp;类&nbsp;名：</td>
							<td align="left"><input id="Name1" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                            <td align="right">拼音简码：</td>
							<td align="left"><input id="Brief1" name="Brief" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr height="30px">
							<td align="right" >父分类编号：</td>
							<td align="left" ><input id="ParentId1" name="ParentId" type="text"/></td>
                            <td align="right" >父分类名称：</td>
							<td align="left" ><input id="ParentName1" name="ParentName" type="text" readonly="readonly"/></td>
						</tr>
                        <tr>
                       		<td align="right">序&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
							<td align="left">
                            	<input id="Sequence1" name="Sequence" class="easyui-numberbox" required="true"/>
                            </td>
                        	<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
							<td align="left">
                            	<select id="Status1" name="Status" class="easyui-combobox" style="width:152px" required="true" panelHeight="auto">
									<option value="0">正常</option>
									<option value="1">注销</option>
								</select>
                            </td>
                        </tr>
						<tr height="50px">	
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onclick="editSubmit()">修改</a></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closedEdit()">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div id="mm" class="easyui-menu" style="width:120px;">
			<div onclick="append()" iconCls="icon-add">新增</div>
			<div onclick="edit()" iconCls="icon-edit">修改</div>
			<div onclick="logout()" iconCls="icon-remove">注销</div>
		</div>
	</div>
</DIV>
</DIV>
</body>
</html>
