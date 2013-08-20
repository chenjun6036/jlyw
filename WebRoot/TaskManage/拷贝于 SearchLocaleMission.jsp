<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>现场检测业务查删改</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
		<script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
			$("#ProjectTeamIdAdd").combobox({
				valueField:'Name',
				textField:'Name',
				onSelect:function(record){
					if($('#MissionDescAdd').val()==null||$('#MissionDescAdd').val().length==0)
							$('#MissionDescAdd').val(record.Project);
					else{						
						var temp=$('#MissionDescAdd').val();
						$('#MissionDescAdd').val(temp+'\r\n'+record.Project);
					}
					document.getElementById("MissionDescAdd").focus(); 
				},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].Name){
								return false;
							}
						}
					}											
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){   
							var newValue = $('#ProjectTeamIdAdd').combobox('getText');
							$('#ProjectTeamIdAdd').combobox('reload','/jlyw/ApplianceServlet.do?method=6&QueryName='+encodeURI(newValue));
					}, 700);
					//$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
				}
			});
		
				$("#NameAdd").combobox({
				//	url:'/jlyw/CustomerServlet.do?method=5',
					valueField:'name',
					required:true,
					textField:'name',
					onSelect:function(record){
						$("#TelAdd").val(record.tel);
						$("#CustomerIdAdd").val(record.id);
						$("#contactortelAdd").val(record.contactorTel);
						$("#AddressAdd").val(record.address);
						$("#zcdAdd").val(record.zipCode);
						$("#conAdd").val(record.contactor);
						$("#contactortelAdd").val(record.contactorTel);
						$("#RegionIdAdd").val(record.regionId);
						$("#ridAdd").combobox('setValue',record.regionName);
						
					},
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
								var newValue = $('#NameAdd').combobox('getText');
								$('#NameAdd').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
						}, 700);
						
					}
				});
				
				$('#usernameAdd').combobox({
				//	url:'/jlyw/CustomerServlet.do?method=6',
					onSelect:function(){
						if($('#StaffsAdd').val()==null||$('#StaffsAdd').val().length==0)
							$('#StaffsAdd').val($('#usernameAdd').combobox('getText'));
						else{
							var temp=$('#StaffsAdd').val();
							$('#StaffsAdd').val(temp+";"+$('#usernameAdd').combobox('getText'));
						}
						//('#usernameAdd').combobox('clear');
					},
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
								var newValue = $('#usernameAdd').combobox('getText');
								$('#usernameAdd').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
						}, 700);
						//$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
					}
				});
				
				
						
				$("#SiteManagerNameAdd").combobox({
				//	url:'/jlyw/CustomerServlet.do?method=5',
					valueField:'id',
					textField:'name',
					onSelect:function(record){
						$("#SiteManagerId").val(record.id);
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
						$("#SiteManagerId").val('');
						try{
							window.clearTimeout(this.reloadObj);
						}catch(ex){}
						this.reloadObj = window.setTimeout(function(){   
								var newValue = $('#SiteManagerNameAdd').combobox('getText');
								$('#SiteManagerNameAdd').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
						}, 700);
					}
				});
				$("#ridAdd").combobox({
					url:'/jlyw/RegionServlet.do?method=2',
					valueField:'id',
					textField:'name',
					onSelect:function(record){
						$("#RegionIdAdd").val(record.id);
					},
					onChange:function(newValue, oldValue){
						//$("#RegionIdAdd").val('');
					}
				});
				
				var nowDate = new Date();
				$("#TentativeDateAdd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));

			$("#ProjectTeamId").combobox({
					valueField:'Name',
					textField:'Name',
					onSelect:function(record){
						if($('#MissionDesc').val()==null||$('#MissionDesc').val().length==0)
								$('#MissionDesc').val(record.Project);
						else{						
							var temp=$('#MissionDesc').val();
							$('#MissionDesc').val(temp+'\r\n'+record.Project);
						}
						document.getElementById("MissionDesc").focus(); 
					},
					onChange:function(newValue, oldValue){
						var allData = $(this).combobox('getData');
						if(allData != null && allData.length > 0){
							for(var i=0; i<allData.length; i++)
							{
								if(newValue==allData[i].Name){
									return false;
								}
							}
						}											
						try{
							window.clearTimeout(this.reloadObj);
						}catch(ex){}
						this.reloadObj = window.setTimeout(function(){   
								var newValue = $('#ProjectTeamId').combobox('getText');
								$('#ProjectTeamId').combobox('reload','/jlyw/ApplianceServlet.do?method=6&QueryName='+encodeURI(newValue));
						}, 700);
						//$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
					}
				});
			$("#Name").combobox({
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'name',
				required:true,
				textField:'name',
				onSelect:function(record){
					$("#Tel").val(record.tel);
					$("#CustomerId").val(record.id);
					$("#contactortel").val(record.contactorTel);
					$("#Address").val(record.address);
					$("#zcd").val(record.zipCode);
					$("#con").val(record.contactor);
					$("#contactortel").val(record.contactorTel);
					$("#RegionIdAdd").val(record.regionId);
					$("#rid").combobox('setValue',record.regionName);
					
				},
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
							var newValue = $('#Name').combobox('getText');
							$('#Name').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					}, 700);
					
				}
			});
			$('#username').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){
					if($('#Staffs').val()==null||$('#Staffs').val().length==0)
						$('#Staffs').val($('#username').combobox('getText'));
					else{
						var temp=$('#Staffs').val();
						$('#Staffs').val(temp+";"+$('#username').combobox('getText'));
					}
					//('#username').combobox('clear');
				},
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
							var newValue = $('#username').combobox('getText');
							$('#username').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
					}, 700);
					//$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
					
			$("#QueryName").combobox({
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'name',
				textField:'name',
				onSelect:function(record){
				},
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
							var newValue = $('#QueryName').combobox('getText');
							$('#QueryName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					}, 700);
				}
			});
			$("#SiteManagerName").combobox({
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'id',
				textField:'name',
				onSelect:function(record){
					$("#SiteManagerId").val(record.id);
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
				
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			$("#rid").combobox({
				url:'/jlyw/RegionServlet.do?method=2',
				valueField:'id',
				textField:'name',
				onSelect:function(record){
					$("#RegionIdAdd").val(record.id);
				},
				onChange:function(newValue, oldValue){
					//$("#RegionIdAdd").val('');
				}
			});
			$('#table2').datagrid({
				title:'单位信息',
				width:1000,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/LocaleMissionServlet.do?method=8',
				sortName: 'CreateDate',
				remoteSort: false,
				sortOrder:'dec',
				idField:'Id',
				pageSize:50,
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CreateDate',title:'创建时间',width:80,align:'center',sortable:true},
					{field:'CreatorName',title:'创建人',width:60,align:'center',sortable:true},
					{field:'Name',title:'单位名称',width:160,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<a style='text-decoration:underline' onclick=\"editMission('" + rowData.Id + "');\"><span style='color: #0033FF'>"+value+"</span></a>";
							
						}
					},
					{field:'Region',title:'所在地区',width:80,align:'center',sortable:true},
					
					{field:'MissionDescEnter',title:'检测项目及台件数',width:200,align:'center'},
					{field:'Status',title:'处理状态',width:90,align:'center',sortable:true,
						formatter:function(value, rowData, rowIndex){
							if(value == 0 || value =="0"){
								return '未分配';
							}
							if(value == 1 || value =="1"){
								return '已分配';
							}
							if(value == 2 || value =="2"){
								return "已完成";
							}
							if(value ==3 || value =="3"){
								return "已注销";
							}
							if(value == 4 || value =="4"){
								return "未核定";
							}
							if(value == 5 || value =="5"){
								return "已核定";
							}
							return "已注销";
						}
					},
					
					{field:'SiteManagerName',title:'检测负责人',width:80,align:'center'},
					{field:'Department',title:'业务部门',width:80,align:'center'},
					{field:'Staffs',title:'人员',width:180,align:'center'},
					{field:'TentativeDate',title:'暂定日期',width:80,align:'center',sortable:true},					
					
					{field:'CheckDate',title:'核定日期',width:80,align:'center',sortable:true},
					{field:'ExactTime',title:'确定日期',width:80,align:'center',sortable:true},
					{field:'VehicleLisences',title:'乘车信息',width:120,align:'center'},
				
					{field:'Address',title:'单位地址',width:120,align:'center'},
					{field:'ZipCode',title:'邮编',width:60,align:'center'},
					{field:'Contactor',title:'联系人',width:80,align:'center'},
					{field:'Tel',title:'单位电话',width:100,align:'center'},
					{field:'ContactorTel',title:'联系人电话',width:100,align:'center'},
					{field:'Remark',title:'备注',width:180,align:'center'},
					{field:'Feedback',title:'反馈',width:180,align:'center'}
					
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 3){	//已注销
						return 'color:#FF00FF'
					}else if(rowData.Status == 1){	//已分配
						return 'color:#FF0033';	
					}else if(rowData.Status == 2){	//已完工
						return 'color:#000000';	
					}else if(rowData.Status == 5){	//负责人已核定
						return 'color:#339900';	
					}
					else if(rowData.Status==4&&rowData.Tag == 1){	//未核定；；；已到暂定日期可是未安排
						return 'color:#FFFF33';
					}else if(rowData.Status==4&&rowData.Tag == 2){	//未核定；；；未到暂定日期
						return 'color:#0000FF';
					}else{
						return 'color:#000000';
					}
				},
				toolbar:[{
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
						$('#add').window('open');
					
					}
				},'-',{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var rowSelected = $('#table2').datagrid('getSelected');
						if(rowSelected != null){
							$('#edit').window('open');
							$('#frm_update_localmission').form('load', rowSelected);
							//$('#rid').combobox('setValue', rowSelected.RegionIdAdd);
							
						}else{
							$.messager.alert('提示','请选择一行数据','info');
						}
					
					}
				},'-',{
						text:'删除',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								var result = confirm("您确定要删除吗？");
								if(result == false){
									return false;
								}
								$.ajax({
									type:'POST',
									url:'/jlyw/LocaleMissionServlet.do?method=4',
									data:"id="+selected.Id,
									dataType:"json",
									success:function(data, textStatus){
										if(data.IsOK){
											$('#table2').datagrid('reload');
											$.messager.alert('提交！','删除成功','info');
										}else{
											$.messager.alert('提交失败！',data.msg,'error');
										}
									}
								});
							}else{
								$.messager.alert('提示','请选择一行数据！','info');
							}
						}
				},'-',{
						text:'反馈',
						iconCls:'icon-undo',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								if(selected.Status!=1&&selected.Status!=2){
									$.messager.alert('提示','该任务状态不是“已分配”或者“已完工”，不能进行反馈！','info');
									return false;
								}
								 
								$('#MissionId').val(selected.Id);
								$('#Feedback').val(selected.Feedback);
								$('#undo').window('open');
								
							}else{
								$.messager.alert('提示','请选择一行数据！','info');
							}
						}
				},'-',{
						text:'委托方来车',
						iconCls:'icon-back',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								if(selected.Status==1&&selected.Status==2){
									$.messager.alert('提示','该任务“已注销”或者“已完工”，不能进行此操作！','info');
									return false;
								}
								var result = confirm("您确定要进行此操作吗？");
								if(result == false){
									return false;
								}
								$.ajax({
									type:'POST',
									url:'/jlyw/LocaleMissionServlet.do?method=10',
									data:"id="+selected.Id,
									dataType:"json",
									success:function(data, textStatus){
										if(data.IsOK){
											$('#table2').datagrid('reload');
											$.messager.alert('提交！','操作成功','info');
										}else{
											$.messager.alert('提交失败！',data.msg,'error');
										}
									}
								});
								
							}else{
								$.messager.alert('提示','请选择一行数据！','info');
							}
						}
				}]
			});
		
		});
		
		function cancel(){
			$('#edit').window('close');
		}
		function editMission(id){
			$('#edit').window('open');			
			//var index=$('#table2').datagrid('getRowIndex',id);		
			var data=$('#table2').datagrid('selectRecord',id);
			var rowdata=$('#table2').datagrid('getSelected');
			$('#frm_update_localmission').form('load', rowdata);
				
		}
		
		function edit(){
			$('#frm_update_localmission').form('submit',{
				url: '/jlyw/CustomerServlet.do?method=3',
				onSubmit:function(){ 
					if($('#RegionIdAdd').val() == ''){
						$.messager.alert('提示','请选择一个有效的地区！','info');
						return false;
					}
					if($("#SiteManagerId").val()==''){
						$.messager.alert('提示','请选择一个有效的现场检测负责人！','info');
						return false;
					}
					return $('#frm_update_localmission').form('validate');
				},
		   		success:function(data){
		   		    var result = eval("("+data+")");
		   		    if(!result.IsOK){
		   		   		 $.messager.alert('修改失败！',result.msg,'error');
							
					}
					else{
						$.messager.alert('提示！','修改成功','error');	
					}
		   		}
			});
		}
		
		function query()
		{
			var History_BeginDate=$('#History_BeginDate').datebox('getValue');
			var History_EndDate=$('#History_EndDate').datebox('getValue');
			
			$('#table2').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=8';
			$('#table2').datagrid('options').queryParams={'QueryName':encodeURI($("#QueryName").combobox('getText')),'Department':encodeURI($("#Department").val()),'MissionStatus':encodeURI($("#MissionStatus").val()),'History_BeginDate':encodeURI(History_BeginDate),'History_EndDate':encodeURI(History_EndDate)};
			
			$('#table2').datagrid('reload');
		}
		
		function savereg(){
			$('#frm_update_localmission').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=3',
				onSubmit:function(){
					return $('#frm_update_localmission').form('validate');
				},
		   		success:function(data){
		   			//alert(data);
		   			var result = eval("("+data+")");
		   			if(result.IsOK){
					     cancel();
		   				 $.messager.alert('提示！','修改成功','info');
						$('#table2').datagrid('reload');
		   			}else{
						$.messager.alert('修改失败！',result.msg,'error');
		   			}
					
		   		 }
			});
			
		}
		function saveundo(){
			$('#frm_undo_localmission').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=9',
				onSubmit:function(){
					return $('#frm_undo_localmission').form('validate');
				},
		   		success:function(data){
		   			//alert(data);
		   			var result = eval("("+data+")");
		   			if(result.IsOK){
					 	
						  $('#undo').window('close');
		   				 $.messager.alert('提示！','反馈成功','info');
						
		   			}else{
						$.messager.alert('修改失败！',result.msg,'error');
		   			}
					$('#table2').datagrid('reload');
		   		 }
			});
			
		}
		function cancel1(){
			$('#add').window('close');
		}
		
		function savereg1(){
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=1',
				onSubmit:function(){
					//alert('onSubmit');
					if($('#RegionIdAdd').val() == ''){
						$.messager.alert('提示','请选择一个有效的地区！','info');
						return false;
					}
					
					return $('#frm_add_customer').form('validate');
				},
		   		success:function(data){
		   		    var result = eval("("+data+")");
		   		    if(result.IsOK){
		   		   		 $.messager.alert('提示！','添加成功','info');
						 $('#table2').datagrid('reload');
						$('#add').window('close');
					}
					else{
						 $.messager.alert('添加失败！',result.msg,'error');
					}
		   		 }
			});
		}
		
		</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="现场检测业务查询与修改" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	
		<div style="margin-bottom:10px;padding-top:10px;width:1000px" class="easyui-panel" title="查询条件" collapsible="false"  closable="false">
			<form id="ff">
			<table id="table1" style="width:980px">
				<tr>
					<td  align="right">
					<label>委托单位：</label><select name="QueryName" id="QueryName" style="width:152px"></select>&nbsp;
					<label>业务部门：</label><input name="Department" id="Department" style="width:132px"/>&nbsp;
					<select name="MissionStatus" id="MissionStatus" style="width:100px"><option value="" selected="selected">全部</option><option value="4">未核定</option><option value="5">已核定</option><option value="1">已分配</option><option value="2">已完工</option></select>&nbsp;
					<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;
					<label>结束时间：</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;</td>
				    
					<td  ><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
				</tr>
			</table>
			</form>
		</div>
		

		<table id="table2" style="height:500px; width:1000px;padding-top:10px"></table>
		
		<div id="edit" class="easyui-window" modal="true" title="修改现场业务信息" style="padding: 10px;width: 900;height: 500;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_update_localmission" method="post">
				<input type="hidden" id="Id" name="Id" value="" />
				<div>
				 <table style="width:700px; height:400px; padding-top:20px" cellspacing="5" >
					<tr height="30px">
						<td align="right" style="width：20%">单位名称：</td>
						<td align="left"  style="width：30%"><select name="Name" id="Name" class="easyui-validatebox" require="true" style="width:175px" readonly="readonly"></select><input type="hidden" name="CustomerId" id="CustomerId" /></td>
						<td align="right" style="width：20%">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
						<td align="left"  style="width：30%"><input id="zcd" name="ZipCode" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
					</tr>
					<tr height="30px">
						<td align="right">单位地址：</td>
						<td align="left"><input id="Address" name="Address" type="text" class="easyui-validatebox" require="true" style="width:172px" /></td>
						<td align="right">单位电话：</td>
						<td align="left"><input id="Tel" name="Tel" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
					</tr>
					<tr height="30px">
						<td align="right">联 系 人：</td>
						<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">联系人号码：</td>
						<td align="left"><input id="contactortel" name="ContactorTel" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
					</tr>
					<tr  height="30px">
						<td align="right">所在地区：</td>
						<td align="left">
							<select id="rid" name="Region" class="easyui-combobox" style="width:177px" required="true"></select><input type="hidden" name="RegionId" id="RegionId" /></td>
						<td align="right">暂定日期：</td>
						<td align="left"><input type="text" id="TentativeDate" name="TentativeDate" class="easyui-datebox" style="width:177px" required="true" /></td>
					</tr>
					<tr height="30px">
						<td align="right">现场检测负责人：</td>
						<td align="left"><select name="SiteManagerName" id="SiteManagerName" style="width:177px" ></select><input type="hidden" name="SiteManagerId" id="SiteManagerId" value="" /></td>
						<td align="right">核定日期：</td>
						<td align="left"><input type="text" id="CheckDate" name="CheckDate" class="easyui-datebox" style="width:177px"  /></td>
					</tr>
					<tr height="30px">
						<td align="right">参与人员：</td>
						<td colspan="3" align="left"><input id="Staffs" name="Staffs" type="text" style="width:300px" class="easyui-validatebox" maxlength="95"/>&nbsp;选择<input id="username" name="username" class="easyui-combobox"  url="" style="width:177px;" valueField="name" textField="name" panelHeight="150px" /></td>
					</tr>
					<tr height="30px">
						<td align="right">检测项目及台件数：</td>
						<td align="left"  colspan="3"><textarea style="height:60px;font-size:12px" id="MissionDesc" name="MissionDesc" cols="47" rows="2" class="easyui-validatebox" required="true" maxlength="500"></textarea>&nbsp;添加<select id="ProjectTeamId" name="ProjectTeamId" type="text" class="easyui-combobox"  style="width:177px" panelHeight="150px"/></td>
					</tr>
					<tr height="30px">
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td align="left"  colspan="3"><textarea style="height:60px;font-size:12px" id="Remark" name="Remark" cols="47" rows="2" maxlength="500"></textarea></td>
					</tr>
					<tr height="30px">
						<td align="right">业务部门：</td>
						<td colspan="3" align="left"><input id="Department" name="Department" class="easyui-validatebox" style="width:177px"  required="true"/></td>
					</tr>
					
					<tr height="50px">	
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">确认修改</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel()">关闭</a></td>
					</tr>
				</table>
				</div>
			</form>
		</div>
		
<div id="add" class="easyui-window" modal="true" title="录入现场业务信息" style="padding: 10px;width: 900;height: 500;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">	
        <form id="frm_add_customer" method="post" >
	<table style="width:700px; height:400px; padding-top:10px" cellspacing="5" >
		<tr height="30px">
			<td align="right" style="width：20%">单位名称：</td>
			<td align="left"  style="width：30%"><select name="Name" id="NameAdd" style="width:175px"></select><input type="hidden" name="CustomerId" id="CustomerIdAdd" /></td>
			<td align="right" style="width：20%">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left"  style="width：30%"><input id="zcdAdd" name="ZipCode" type="text" style="width:175px"  readonly="readonly"/></td>
		</tr>
		<tr height="30px">
			<td align="right">单位地址：</td>
			<td align="left"><input id="AddressAdd" name="Address" type="text" style="width:172px" readonly="readonly"/></td>
		    <td align="right">单位电话：</td>
		    <td align="left"><input id="TelAdd" name="Tel" type="text" style="width:175px" readonly="readonly"/></td>
		</tr>
		<tr height="30px">
			<td align="right">联 系 人：</td>
			<td align="left"><input id="conAdd" name="Contactor" type="text" style="width:175px" readonly="readonly"/></td>
		    <td align="right">联系人号码：</td>
		    <td align="left"><input id="contactortelAdd" name="ContactorTel" type="text" style="width:175px" readonly="readonly"/></td>
		</tr>
		<tr  height="30px">
			<td align="right">所在地区：</td>
			<td align="left">
				<select id="ridAdd" name="Region" class="easyui-combobox" style="width:177px" required="true"></select><input type="hidden" name="RegionId" id="RegionIdAdd" /></td>
			<td align="right">暂定日期：</td>
			<td align="left"><input type="text" id="TentativeDateAdd" name="TentativeDate" class="easyui-datebox" style="width:177px" required="true" /></td>
		</tr>
		<tr height="30px">
			<td align="right">现场检测负责人：</td>
			<td align="left" colspan="3"><select name="SiteManagerName" id="SiteManagerNameAdd" style="width:177px"></select><input type="hidden" name="SiteManagerId" id="SiteManagerId" value="" /></td>
			
		</tr>
		<tr height="30px">
			<td align="right">参与人员：</td>
			<td colspan="3" align="left"><input id="StaffsAdd" name="Staffs" type="text" style="width:300px" class="easyui-validatebox" maxlength="100" />&nbsp;选择<input id="usernameAdd" name="username" class="easyui-combobox"  url="" style="width:177px;" valueField="name" textField="name" panelHeight="150px" /></td>
		</tr>
		<tr height="30px">
			<td align="right">检测项目及台件数：</td>
			<td align="left"  colspan="3"><textarea style="height:60px;font-size:12px" id="MissionDescAdd" name="MissionDesc" cols="47" rows="2" class="easyui-validatebox" required="true" maxlength="500"></textarea>&nbsp;添加<select id="ProjectTeamIdAdd" name="ProjectTeamId" type="text" class="easyui-combobox"  style="width:177px" panelHeight="150px"/></td>
		</tr>
		<tr height="30px">
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left"  colspan="3"><textarea style="height:60px;font-size:12px" id="RemarkAdd" name="Remark" cols="47" rows="2"  maxlength="500"></textarea></td>
		</tr>
		<tr height="30px">
			<td align="right">业务部门：</td>
			<td colspan="3" align="left"><input id="DepartmentAdd" name="Department" class="easyui-validatebox" style="width:177px"  required="true"/></td>
		</tr>
		
		<tr height="50px">	
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg1()">添加</a></td>
			<td></td>
			<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel1()">关闭</a></td>
			
		</tr>
	</table>
	
	
	
	
	
	</form>
</div>
		
		
		<div id="undo" class="easyui-window" modal="true" title="反馈" style="padding: 10px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_undo_localmission" method="post">
				<input type="hidden" id="MissionId" name="Id" value="" />
				<div>
				 <table style="padding-top:20px" cellspacing="3" >
					<tr >
						<td align="center" colspan="2"><font size="+2">业务执行情况</font></td>						
					</tr>
					<tr >
						<td align="center" colspan="2" ><textarea name="Feedback" class="easyui-validatebox" required="true" id="Feedback" cols="40" rows="8"></textarea></td>						
					</tr>
					
					
					<tr height="50px">	
						
						<td align="center"><a class="easyui-linkbutton" icon="icon-save"  href="javascript:void(0)" onclick="saveundo()">确认</a></td>
						
						<td align="center"><a class="easyui-linkbutton" icon="icon-cancel"  href="javascript:$('#undo').window('close');" >关闭</a></td>
					</tr>
				</table>
				</div>
			</form>
		</div>

</DIV></DIV>
</body>
</html>
