<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>器具标准名称与项目组关系管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
	<script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		var nodekeep="";
		$(function(){

			$('#tt').tree({
				//url:'/jlyw/ApplianceStandardNameServlet.do?method=2',
				onBeforeExpand:function(node){
					if(nodekeep.indexOf(node.id)==-1)
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
				onBeforeCollapse:function(node){
					
				},
				onClick:function(node){
					if(node.attributes.type=="0"||node.attributes.type==0)
						return;
					$('#projectTeam_table').datagrid('options').url='/jlyw/ApplianceStandardNameServlet.do?method=5';
					$('#projectTeam_table').datagrid('options').queryParams={'ApplianceStandardNameId':node.id};
					$('#projectTeam_table').datagrid('reload');
				}
			});
			
			$('#projectTeam_table').datagrid({
				title:'项目组信息',
				singleSelect:true,			
				url:'',
				idField:'Id',
				fit:true,
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'StandardName',title:'标准名称',width:100,align:'center'},
					{field:'Name',title:'项目组名称',width:100,align:'center'},
					{field:'ProjectName',title:'检验项目名称',width:100,align:'center'}
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#projectTeam_table').datagrid('getSelected');
							if(select){
								$.messager.confirm('提示','确认删除吗？',function(r){
								if(r){	
								$.ajax({
									type:'POST',
									url:'/jlyw/ApplianceStandardNameServlet.do?method=7',
									data:'id='+select.Id,
									dataType:"html",
									success:function(data){
										$('#projectTeam_table').datagrid('reload');
									}
								});
								}
								});
							}
							else
							{
								$.messager.alert('提示','请选择一个项目组','warning');
							}
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
				
		function cancel(){
			$('#frm_StdName_ProTeam').form('clear');
		}
		
		//增加子节点
		function append(datas,cnode) {
		   var node = cnode;
		   $('#tt').tree('append', {
		       parent: node.target,
		       data: datas
		   });
		   nodekeep += "," + node.id;
		}
		
		function addProjectTeam(){
			var node = $('#tt').tree('getSelected');
			if(node)
			{
				$('#StdName_ProTeam_StdNameId').val(node.id);
				$('#frm_StdName_ProTeam').form('submit',{
					url:'/jlyw/ApplianceStandardNameServlet.do?method=6',
					onSubmit:function(){return $(this).form('validate');},
					success:function(data){
						var result = eval("("+data+")");
						$.messager.alert('提示',result.msg,'info');
						if(result.IsOK)
						{
							cancel();
						}
						$('#projectTeam_table').datagrid('reload');
					}
				});
			}
		}
		
	</script>
</head>
<body>

<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="器具标准名称与项目组关系管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV" style="left:0px; margin-left:0px">
<DIV class="easyui-layout" fit="true" nohead="true" border="true">
	
	<div region="west" style="width:200px; border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="器具标准名称">
		<ul id="tt"></ul>
 	</div>

	
	
  	<div region="center" id="add" name="add" style="border:0px;" iconCls="icon-add" closed="false" maximizable="false" minimizable="false" collapsible="false">
		<div id="p4" class="easyui-panel" style="width:800px;height:250px;"
				title="关联项目组" collapsible="false"  closable="false" scroll="no">
			<table width="100%" height="100%" >
				<tr>
					<td width="57%" rowspan="2">
						<table id="projectTeam_table" class="easyui-datagrid"></table>
					</td>
				 	<td width="43%" height="175" valign="top" align="left" style="overflow:hidden" align="center">
                    <div class="easyui-panel" title="添加项目组">
                	<form id="frm_StdName_ProTeam" method="post">
                    <input id="StdName_ProTeam_StdNameId" name="StdName_ProTeam_StdNameId" type="hidden"/>
					 <table align="center">
                      <tr>
                      	<td align="center">项目组:</td>
						<td align="center">
                          <select id="ProjectTeamId" name="ProjectTeamId" type="text" class="easyui-combobox" url="/jlyw/ProjectTeamServlet.do?method=6" valueField="Id" textField="Name" required="true" style="width:152px" panelHeight="150px"/>
                    	</td>
                      </tr>
                      <tr>
                      	<td align="center">检验项目名称：</td>
                        <td align="center"><input id="ProjectName" name="ProjectName" type="text" style="width:200px" ></input></td>
                      </tr>
                      <tr  style="height:50px">
						<td align="center"><a href="javascript:void(0)" onclick="addProjectTeam()" class="easyui-linkbutton" iconCls="icon-add">新增</a></td>
                        <td align="center"> <a href="javascript:void(0)" onclick="cancel()" class="easyui-linkbutton" iconCls="icon-cancel">取消</a></td>
					  </tr>
                      </table>
                    </form>
				</div> 
					</td>
				</tr>
			</table>
	   </div>
	</div>
    
</DIV></DIV></DIV>

</body>

</html>
