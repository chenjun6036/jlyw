<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>车辆—任务查询</title>
  <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	 $(function(){		
	 
			 var nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));	
			
			$('#task-table').datagrid({
				title:'我的出车任务列表',
				singleSelect:true, 
			    width:1000,
				height:500,
				fit: false,
                nowrap: false,
                striped: true,
				url:'/jlyw/VehicleMissionServlet.do?method=0',
				//sortName: 'CreateDate',
				remoteSort: false,
				//sortOrder:'dec',
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
					
				]],
				columns:[[
					{title:'客户信息',colspan:7,align:'center'},
					{title:'任务信息',colspan:6,align:'center'},
			        {title:'车辆信息',colspan:9,align:'center'}				
					
				],[
					{field:'CreateDate',title:'发布时间',width:80,align:'center',sortable:true},
					{field:'Code',title:'委托书号',width:80,align:'center',sortable:true},
					{field:'CreatorName',title:'发布人',width:60,align:'center',sortable:true},
					{field:'Customer',title:'客户名称',width:180,align:'center'},
					{field:'Address',title:'客户地址',width:180,align:'center'},
					{field:'Contactor',title:'联系人',width:100,align:'center'},
					{field:'Tel',title:'联系电话',width:120,align:'center'},
					{field:'ContactorTel',title:'联系人电话',width:120,align:'center'},
					
					{field:'MissionDesc',title:'现场检测项目及台件数',width:150,align:'center'},
					{field:'SiteManagerId',title:'现场检测负责人',width:100,align:'center'},
					{field:'Department',title:'业务部门',width:80,align:'center'},
					{field:'Staffs',title:'员工',width:160,align:'center'},
					//{field:'Description',title:'任务描述',width:100,align:'center'},
					{field:'ExactTime',title:'确定日期',width:80,align:'center',sortable:true},
					
					{field:'DriverId',title:'本次司机',width:60,align:'center'},					
					{field:'People',title:'坐车的员工',width:160,align:'center'},
					{field:'AssemblingPlace',title:'集合地点',width:100,align:'center'},
					{field:'BeginDate',title:'发车时间',width:160,align:'center',
						formatter:function(value,rowData,rowIndex){							
							return '<span style="color:red;">'+value+'</span>';	
						}
					},
					{field:'Kilometers',title:'行驶公里数',width:100,align:'center'},		   
					{field:'Licence',title:'车牌号',width:80,align:'center'},
					{field:'Limit',title:'限载人数',width:80,align:'center'},
					{field:'Model',title:'车辆编号',width:120,align:'center'},
					{field:'Brand',title:'车辆品牌',width:80,align:'center'}
					
					
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'删除所选车辆调度',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#task-table').datagrid('getSelected');
							if(selected != null){
								var result = confirm("您确定要删除吗？");
								if(result == false){
									return false;
								}
								$.ajax({
									type:'POST',
									url:'/jlyw/VehicleMissionServlet.do?method=1',
									data:"id="+selected.Id,
									dataType:"json",
									success:function(data, textStatus){
										if(data.IsOK){
											$('#task-table').datagrid('reload');
										}else{
											$.messager.alert('提示！',data.msg,'error');
										}
									}
								});
							}else{
								$.messager.alert('提示','请选择一行数据！','info');
							}
						}
				}]
			
			});
			$('#CustomerName').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'name',
				textField:'name',
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
					 try{
							window.clearTimeout(this.reloadObj);
						}catch(ex){}
						this.reloadObj = window.setTimeout(function(){   
								var newValue = $('#CustomerName').combobox('getText');
								$('#CustomerName').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);

					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});

			$("#LocaleCode").combobox({//现场委托书号
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'code',
				textField:'code',
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
							var newValue = $('#LocaleCode').combobox('getText');
							$('#LocaleCode').combobox('reload','/jlyw/LocaleMissionServlet.do?method=15&QueryName='+encodeURI(newValue));
					}, 700);
					
				}
			});
			
		});
		function query(){
			
			 $('#task-table').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=0';
			 
			 $('#task-table').datagrid('options').queryParams={'StartTime':encodeURI($('#dateTimeFrom').datebox('getText')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getText')),'MissionStatus':encodeURI($('#MissionStatus').val()),'Department':encodeURI($("#Department").val()),'Licence':encodeURI($('#Licence').val()),'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'LocaleCode':encodeURI($('#LocaleCode').combobox('getValue'))};
			 $('#task-table').datagrid('reload');
		}
</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="车辆-任务查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

    <div id="p" class="easyui-panel" style="width:1000px;padding:10px;"
				title="统计条件" collapsible="false"  closable="false">
			<table width="950px" id="table1">
				<tr >		
					<td align="left">
					<label>业务部门：</label><input name="Department" id="Department" style="width:80px"/>&nbsp;
					<label>车牌号：</label><input name="Licence" id="Licence" style="width:80px"/>&nbsp;
					<select name="MissionStatus" id="MissionStatus" style="width:100px"><option value="0">我的任务</option><option value="1" selected="selected">所有任务</option></select>&nbsp;
					起始时间<input name="date1" id="dateTimeFrom" type="text" style="width:120px;"  class="easyui-datebox" >&nbsp;					
					结束时间：<input name="date2" id="dateTimeEnd" type="text" style="width:120px;"  class="easyui-datebox" >&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a>
					</td>
				</tr >
				<tr >		
					<td align="left">
					<label>委托单位：</label><input name="CustomerName" id="CustomerName" style="width:80px"/>&nbsp;
					<label>现场委托书号：</label><input name="LocaleCode" id="LocaleCode" style="width:80px"/>
					</td>
				</tr >
				
		</table>
		</div>
	
		<table  id="task-table" iconCls="icon-search" >
		</table>

</DIV></DIV>	
</body>
</html>
