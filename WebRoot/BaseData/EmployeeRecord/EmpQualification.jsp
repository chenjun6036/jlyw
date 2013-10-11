<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>检验人员资质信息管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
			var nodekeep="";
			var Qual="";
			var selected="";
			$(function(){
				
				$('#queryname').combobox({
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
						$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
					}
				});
				
				$('#table').treegrid({
					title:'受检器具分类与标准名称',
					height:480,
					width:700,
					rownumbers: false,
					animate:false,
					idField:'treeId',
					treeField:'text',
					singleSelect:false,
					frozenColumns:[[
						{title:'名称',field:'text',width:300,editor:'text',
							formatter:function(name){
								return '<span style="color:black">'+name+'</span>';
							}
						}
					]],
					columns:[[
						{title:'检定',field:'JianDing',width:60,editor:'text',align:'center',
							formatter:function(value,row){
								if(value==1)
									return "√";
								return "";
							}
						},
						{title:'校准',field:'JiaoZhun',width:60,align:'center',editor:'text',
							formatter:function(value,row){
								if(value==1)
									return "√";
								return "";
							}
						},
						{title:'检验',field:'JianYan',width:60,align:'center',editor:'text',
							formatter:function(value,row){
								if(value==1)
									return "√";
								return "";
							}
						},
						{title:'检验排外',field:'JianYanPaiWai',width:60,align:'center',editor:'text',
							formatter:function(value,row){
								if(value==1)
									return "√";
								return "";
							}
						},
						{title:'核验',field:'HeYan',width:60,align:'center',editor:'text',
							formatter:function(value,row){
								if(value==1)
									return "√";
								return "";
							}
						},
						{title:'授权签字',field:'ShouQuan',width:60,align:'center',editor:'text',
							formatter:function(value,row){
								if(value==1)
									return "√";
								return "";
							}
						}
					 ]],
					//url:'/jlyw/ApplianceStandardNameServlet.do?method=2',
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
										appendnode(data, node);
										reloadQual();
								}
							});
						}
					},
					onDblClickRow: function(node){
						if($(this).treegrid('isLeaf',node.treeId))  //是叶子节点
						{
							//ClickNode(node.attributes.url, node.attributes.title);
						}
						else
						{
							$(this).treegrid('toggle', node.treeId);
						}
						$(this).treegrid('unselect', node.treeId);
					}
				});
				
				$.ajax({
					type: "POST",
					url: "/jlyw/ApplianceStandardNameServlet.do?method=2",
					cache: false,
					async: false,
					dataType: "json",
					success: function(data) {
						$('#table').treegrid('loadData',data);
					}
				});
				
				$('#user').datagrid({
					title:'人员查询',
					width:250,
					height:480,
					singleSelect:true, 
					nowrap: false,
					striped: true,
					url:'',
					remoteSort: false,
					rownumbers:false,
					frozenColumns:[[
						{field:'ck',checkbox:true}
					]],
					columns:[[
						{field:'JobNum',title:'工号',width:80,align:'center'},
						{field:'Name',title:'姓名',width:80,align:'center'}
					]],
					onClickRow:function(rowIndex,rowData){
						$('#table').treegrid('unselectAll');
						$.ajax({
							type:"POST",
							url:"/jlyw/QualificationServlet.do?method=2&EmpId=" + rowData.Id,
							cache: false,
							async: false,
							dataType: "json",
							success: function(data) {
								resetQual();
								Qual = data;
								reloadQual();
							}
						});
						$('#EmpId').val(rowData.Id);
					}
					
				});
				
			});
	
			 //增加子节点
			function appendnode(datas,cnode) {
				 var node = cnode;
				 $('#table').treegrid('append', {
					parent: node.treeId,
					data: datas
				});
			   nodekeep += "," + node.id+",";
			} 
			
			function query(){
				$('#user').datagrid('options').url='/jlyw/UserServlet.do?method=0';
				$('#user').datagrid('options').queryParams={'queryname':encodeURI($('#queryname').combobox('getValue'))};
				$('#user').datagrid('reload');
			}
			
			function resetQual(){
				$('#table').treegrid('selectAll');
				var allRows = $('#table').treegrid("getSelections");
				$('#table').treegrid('unselectAll');
				for(var i=0; i<allRows.length;i++)
				{
					var vRow = allRows[i];
					vRow.JianDing = 0;
					vRow.JiaoZhun = 0;
					vRow.JianYan = 0;
					vRow.JianYanPaiWai = 0;
					vRow.HeYan = 0;
					vRow.ShouQuan = 0;
					$('#table').treegrid('refresh',vRow.treeId);
				}
			}
			
			function reloadQual(){
				/*$('#table').treegrid('selectAll');
				var allRows = $('#table').treegrid("getSelections");
				$('#table').treegrid('unselectAll');*/
				for(var j = 0; j<Qual.length; j++)
				{
					$('#table').treegrid('select',Qual[j].treeId);
					var vRow = $('#table').treegrid('getSelected');
					if(vRow==null)
						continue;
					if(Qual[j].Type==11)
						vRow.JianDing = 1;
					else if(Qual[j].Type==12)
						vRow.JiaoZhun = 1;
					else if(Qual[j].Type==13)
						vRow.JianYan = 1;
					else if(Qual[j].Type==16)
						vRow.JianYanPaiWai = 1;
					else if(Qual[j].Type==14)	
						vRow.HeYan = 1;
					else if(Qual[j].Type==15)
						vRow.ShouQuan = 1;
					$('#table').treegrid('refresh',vRow.treeId);
					$('#table').treegrid('unselect',Qual[j].treeId);
				}
				
				var selects = selected.split("|");
				for(var i = 0; i < selects.length; i++)
				{
					$('#table').treegrid('select',selects[i]);
				}
			}
			
			function AddQualOrLogoutQual(type){
				var user = $('#user').datagrid('getSelected');
				var selects = $('#table').treegrid('getSelections');
				if(user==null||selects.length==0)
					return;
				var AuthItemStr = "";
				for(var i = 0; i < selects.length; i++)
					AuthItemStr = AuthItemStr + selects[i].treeId + "|";
					
				selected = AuthItemStr;
				$.ajax({
					type:'POST',
					url:'/jlyw/QualificationServlet.do?method=1',
					data:'EmpId=' + user.Id + '&Type=' + type + '&AuthItemStr=' + AuthItemStr,
					dataType:'html',
					success:function(data){
						var result = eval("("+data+")");
						
						if(result.IsOK)
						{
							$.ajax({
								type:"POST",
								url:"/jlyw/QualificationServlet.do?method=2&EmpId=" + user.Id,
								cache: false,
								async: false,
								dataType: "json",
								success: function(data) {
									resetQual();
									Qual = data;
									reloadQual();
								}
							});
						}
						else{
							$.messager.alert('提示',result.msg,'info');
						}
					}
				});
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
			
			function myExport1(){
				if($('#EmpId').val()==null)
					return;
				ShowWaitingDlg("正在导出，请稍后......");
				$('#frm_export1').form('submit',{
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
<form id="frm_export" method="post" action="/jlyw/QualificationServlet.do?method=3">
</form>
<form id="frm_export1" method="post" action="/jlyw/QualificationServlet.do?method=3">
<input id="EmpId" name="EmpId" type="hidden"/>
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="检验人员资质信息管理" />
		</jsp:include>
	</DIV>
<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px" region="center">
    	<div>
			 <table id="table1">
                <tr>
                     <td width="40" align="left">员工：</td>
                     <td width="193" align="left"><input id="queryname" name="queryname" class="easyui-combobox"  url="" style="width:150px;" valueField="name" textField="name" panelHeight="150px" /></td>
                     <td width="100"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询</a></td>
                     <td width="200"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="myExport()">导出所有资质信息</a></td>
                     <td width="300"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="myExport1()">导出个人资质信息</a></td>
                </tr>
              </table>
		</div>
        <div>
            <table>
                <tr>
                    <td>
                        <table id="user" style="height:480px; width:250px"></table>
                    </td>
                    <td>
                        <table id="table" style="height:480px; width:700px;"></table>
                    </td>
                </tr>
            </table>        
        </div>
        <div style="overflow:hidden;height:50px">
            <table width="900px" id="table2">
                <tr height="50px">
                    <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit2" href="javascript:void(0)" onClick="AddQualOrLogoutQual(11)">检定</a></td>
                    <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit2" href="javascript:void(0)" onClick="AddQualOrLogoutQual(12)">校准</a></td>
                    <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit2" href="javascript:void(0)" onClick="AddQualOrLogoutQual(13)">检验</a></td>
                    <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit2" href="javascript:void(0)" onClick="AddQualOrLogoutQual(16)">检验排外</a></td>
                    <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit2" href="javascript:void(0)" onClick="AddQualOrLogoutQual(14)">核验</a></td>
                    <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit2" href="javascript:void(0)" onClick="AddQualOrLogoutQual(15)">授权签字</a></td>
                </tr >
            </table>
        </div>
     </div>
</DIV>
</DIV>
</body>
</html>
