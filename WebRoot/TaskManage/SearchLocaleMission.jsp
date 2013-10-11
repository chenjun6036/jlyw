<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,com.jlyw.hibernate.SysUser" errorPage="" %>
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
		<script type="text/javascript" src="../JScript/json2.js"></script>
		<script type="text/javascript" src="../JScript/RandGenerator.js"></script>
		<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script>
	$(function(){
		$("#ApplianceSpeciesName").combobox({
			valueField:'Name',
			textField:'Name',
			onSelect:function(record){
				
				$("#SpeciesType").val(record.SpeciesType);		//器具分类类型
				$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//器具分类ID（或者是标准名称ID）
				$("#StandName").val(record.StandName);		//器具标准名称
				$("#ApplianceName").combobox('clear');
				$("#Model").combobox('clear');
				$("#Range").combobox('clear');
				$("#Accuracy").combobox('clear');
				$("#Manufacturer").combobox('clear');
				$("#WorkStaff").combobox('clear');
				if(record.SpeciesType == 0 || record.SpeciesType == '0' || record.SpeciesType == 2 || record.SpeciesType == '2'){	//标准名称Or常用名称
					if(record.PopName == null || record.PopName.length == 0){
						$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);
					}else{
						$("#ApplianceName").combobox('setValue',record.PopName);
					}
					$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//型号规格
					$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//测量范围
					$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//精度等级
					$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//制造厂商
				}else if(record.SpeciesType == 1 || record.SpeciesType == '1'){	//分类名称
					$("#ApplianceName").combobox('loadData',[]);
					$("#Model").combobox('loadData',[]);	//型号规格
					$("#Range").combobox('loadData',[]);	//测量范围
					$("#Accuracy").combobox('loadData',[]);	//精度等级
					$("#Manufacturer").combobox('loadData',[]);	//制造厂商
				}
				$("#WorkStaff").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//检验人员
				
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
				$("#SpeciesType").val('');		//器具分类类型
				$("#ApplianceSpeciesId").val('');	//器具分类ID（或者是标准名称ID）
				$("#StandName").val('');		//器具标准名称
				try{
					window.clearTimeout(this.reloadObj);
				}catch(ex){}
				this.reloadObj = window.setTimeout(function(){   
						var newValue = $('#ApplianceSpeciesName').combobox('getText');
						$('#ApplianceSpeciesName').combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
				}, 700);
				//$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
			}
		});
		 
		 $('#username').combobox({
		//	url:'/jlyw/CustomerServlet.do?method=6',
			onSelect:function(){
				if($('#AssistStaff').val()==null||$('#AssistStaff').val().length==0)
					$('#AssistStaff').val($('#username').combobox('getText'));
				else{
					var temp=$('#AssistStaff').val();
					$('#AssistStaff').val(temp+";"+$('#username').combobox('getText'));
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
			}
		}); 
	
		$('#Licence').combobox({//车牌号的模糊查询
		//	url:'/jlyw/CustomerServlet.do?method=6',
			valueField:'Licence',
			
			textField:'Licence',
			onSelect:function(record){
				
				$('#Drivername').combobox('setValue',record.Driver);
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
				$('#Drivername').combobox('setValue','');
				try{
					window.clearTimeout(this.reloadObj);
				}catch(ex){}
				this.reloadObj = window.setTimeout(function(){   
						var newValue = $('#Licence').combobox('getText');
						$('#Licence').combobox('reload','/jlyw/VehicleServlet.do?method=6&queryname='+newValue);
				}, 700);
			}
		});   
		$("#Drivername").combobox({
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
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){   
							var newValue = $('#Drivername').combobox('getText');
							$('#Drivername').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
					}, 700);
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
			
			$("#CustomerName").combobox({//委托单位
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'name',
				textField:'name',
				onSelect:function(record){
					$("#QuotationNumber").combobox('reload','/jlyw/QuotationServlet.do?method=11&QueryName='+encodeURI(record.name));
					
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
							var newValue = $('#CustomerName').combobox('getText');
							$('#CustomerName').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					}, 700);
					
				}
			});
			$("#QuotationNumber").combobox({//委托单位
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
							var newValue = $('#QuotationNumber').combobox('getText');
							$('#QuotationNumber').combobox('reload','/jlyw/QuotationServlet.do?method=13&QueryName='+encodeURI(newValue));
					}, 700);
					
				}
			});
					
			$("#SiteManagerNameAdd").combobox({
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'id',
				textField:'name',
				onSelect:function(record){
					$("#SiteManagerIdAdd").val(record.id);
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
			
			$("#CheckDatecheck").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
           
		    $("#Namecheck").combobox({  //核定中的委托单位
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'name',
				required:true,
				textField:'name',
				onSelect:function(record){
					$("#Telcheck").val(record.tel);
					$("#CustomerIdcheck").val(record.id);
					$("#contactortelcheck").val(record.contactorTel);
					$("#Addresscheck").val(record.address);
					$("#zcdcheck").val(record.zipCode);
					$("#concheck").val(record.contactor);
					$("#contactortelcheck").val(record.contactorTel);
					$("#RegionIdcheck").val(record.regionId);
					$("#ridcheck").combobox('setValue',record.regionName);
					
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
							var newValue = $('#Namecheck').combobox('getText');
							$('#Namecheck').combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
					}, 700);
					
				}
			});
			$("#SiteManagerNamecheck").combobox({
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'id',
				textField:'name',
				onSelect:function(record){
					$("#SiteManagerIdcheck").val(record.id);
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
					$("#SiteManagerIdcheck").val('');
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){   
							var newValue = $('#SiteManagerNamecheck').combobox('getText');
							$('#SiteManagerNamecheck').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
					}, 700);
				}
			});
			$("#ridcheck").combobox({
				url:'/jlyw/RegionServlet.do?method=2',
				valueField:'id',
				textField:'name',
				onSelect:function(record){
					$("#RegionIdcheck").val(record.id);
				},
				onChange:function(newValue, oldValue){
					//$("#RegionIdcheck").val('');
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
				title:'现场任务信息',
				width:1000,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				//url:'/jlyw/LocaleMissionServlet.do?method=8',
				//sortName: 'CreateDate',
				remoteSort: false,
				//sortOrder:'dec',
				idField:'Id',
				pageSize:20,
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CreateDate',title:'创建信息',width:100,align:'center',sortable:true},
					//{field:'CreatorName',title:'创建人',width:60,align:'center',sortable:true},
					
					{field:'Code',title:'委托书号',width:80,align:'center',sortable:true},
					{field:'Status',title:'处理状态',width:60,align:'center',sortable:true,
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
					{field:'Name',title:'单位名称',width:160,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<a style='text-decoration:underline' onclick=\"editMission('" + rowData.Id + "');\"><span style='color: #0033FF'>"+value+"</span></a>";
							
						}
					},
					{field:'Address',title:'单位地址',width:120,align:'center'},
					{field:'VehicleLisences',title:'乘车信息',width:120,align:'center'},
					{field:'Department',title:'业务部门',width:80,align:'center'},
					{field:'MissionDesc',title:'器具信息',width:120,align:'center'},
					{field:'Staffs',title:'人员',width:180,align:'center'},
					
					{field:'SiteManagerName',title:'检测负责人',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #FF00CC'>"+value+"</span>";
							
						}
					},
										
					{field:'TentativeDate',title:'暂定日期',width:80,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #33FF00'>"+value+"</span>";
							
						}
					},										
					{field:'CheckDate',title:'核定日期',width:80,align:'center',sortable:true,
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #6633FF'>"+value+"</span>";
							
						}
					},
					{field:'ExactTime',title:'确定日期',width:80,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #330033'>"+value+"</span>";
							
						}
					
					},
					
					{field:'Contactor',title:'联系人',width:80,align:'center'},
					{field:'Tel',title:'单位电话',width:100,align:'center'},
					{field:'ContactorTel',title:'联系人电话',width:100,align:'center'},
					{field:'HeadNameName',title:'台头名称',width:120,align:'center'},
					{field:'Region',title:'所在地区',width:60,align:'center',sortable:true},
					{field:'ZipCode',title:'邮编',width:60,align:'center'},
					
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
					}else if(rowData.Status == 2){	//已完工，不能修改了
						return 'color:#000000';	
					}else if(rowData.Status == 5){	//负责人已核定
						return 'color:#339900';	
					}
					else if(rowData.Status==4&&rowData.Tag == 1){	//未核定；；；已到暂定日期可是未安排
						return 'color:#0000FF';
					}else if(rowData.Status==4&&rowData.Tag == 2){	//未核定；；；未到暂定日期
						return 'color:#6600CC';
					}else{
						return 'color:#0000FF';
					}
				},
				toolbar:[{
					text:'新增',
					iconCls:'icon-add',
					handler:function(){
						
						$('#add').append($("#table-Appliance"));
						$("#table-Appliance").show();	
						var nowDate = new Date();
						$("#TentativeDateAdd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
						$('#add').window('open');					
						
					}
				},'-',{
					text:'修改',
					iconCls:'icon-edit',
					handler:function(){
						var rowSelected = $('#table2').datagrid('getSelected');
						if(rowSelected != null){
							
						   var titlecode ="现场委托书号为“"+rowSelected.Code+"”的现场任务修改";
							$('#edit').window({title:titlecode});
							$('#edit').append($("#table-Appliance"));
							$("#table-Appliance").show();
							$('#edit').window('open');
							$('#frm_update_localmission').form('load', rowSelected);
							
							$("#HeadName").combobox('select',rowSelected.HeadNameId);
							//$('#rid').combobox('setValue', rowSelected.RegionIdAdd);
							$('#table5').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
							$('#table5').datagrid('options').queryParams={'Id':rowSelected.Id};
							
							$('#table5').datagrid('reload');						
							
						}else{
							$.messager.alert('提示','请选择一行数据','info');
						}
					
					}
				},'-',{
					text:'核定',
					iconCls:'icon-ok',
					handler:function(){
						var rowSelected = $('#table2').datagrid('getSelected');
						if(rowSelected != null){
							if(rowSelected.Status==2){
									$.messager.alert('提示','该任务状态“已完工”，不能进行核定！','info');
									return false;
							}
							if(rowSelected.Status!=4&&rowSelected.Status!=5){
									$.messager.alert('提示','该任务状态不是“未核定”或者“已核定”，不能进行核定！','info');
									return false;
							}
						   var titlecode ="现场委托书号为“"+rowSelected.Code+"”的现场任务核定";
							$('#check').window({title:titlecode});
							$('#check').append($("#table-Appliance"));
							$("#table-Appliance").show();
							$('#check').window('open');
							$('#frm_check_localmission').form('load', rowSelected);
							$("#HeadNamecheck").combobox('select',rowSelected.HeadNameId);
							$('#CheckDatecheck').datebox('setValue',rowSelected.TentativeDate)
							//$('#rid').combobox('setValue', rowSelected.RegionIdAdd);
							$('#table5').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
							$('#table5').datagrid('options').queryParams={'Id':rowSelected.Id};
							
							$('#table5').datagrid('reload');						
							
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
								if(selected.status==1||selected.status==2){
									$.messager.alert('提示','该任务状态是“已分配”或者“已完成”，不能删除！','info');
									return false;
								}
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
								
								$('#MissionId').val(selected.Id);
								$('#Feedback').val(selected.Feedback);
								$('#undo').window('open');
								
							}else{
								$.messager.alert('提示','请选择一行数据！','info');
							}
						}
				},'-',{
						text:'其他交通方式',
						iconCls:'icon-back',
						handler:function(){
						
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								if(selected.Status==2||selected.Status==3){
									$.messager.alert('提示','该任务“已完工”或者“已注销”，不能进行此操作！','info');
									return false;
								}
								if(selected.Status==1&&selected.VehicleLisences!=null){
									$.messager.alert('提示','该任务进行了“车辆分配”，请先删除车辆分配，再进行此操作！','info');
									return false;
								}
								$('#CustomerLC_window').window('open');
							}else{
								$.messager.alert('提示','请选择一行数据！','info');
							}
							
							
							
						}
				},'-',{
					text:'打印现场检测任务书',
					iconCls:'icon-print',
					handler:function(){
							var row = $('#table2').datagrid('getSelected');
							
							if(row)
							{
								    //if(row.SiteManagerName!=null&&row.SiteManagerName.length>0&&row.ExactTime!=null&&row.ExactTime.length>0){								
										$('#localeMissionId').val(row.Id);
										$('#MissionPrint-form').submit();
									//}else{
									//	$.messager.alert('提示','现场检测负责人为空或者确定时间为空','warning');
									//}												
							}else{
								//$('#MissionPrint-blank-form').submit()
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				},'-',{
					text:'生成现场委托单',
					iconCls:'icon-add',
					handler:function(){
							generateSheet();
						}
				},'-',{
					text:'打印空现场检测任务书',
					iconCls:'icon-print',
					handler:function(){
							var row = $('#table2').datagrid('getSelected');
							
							if(row)
							{
								    //if(row.SiteManagerName!=null&&row.SiteManagerName.length>0&&row.ExactTime!=null&&row.ExactTime.length>0){								
										$('#localeMissionId1').val(row.Id);
										$('#MissionPrint-blank-form').submit()
									//}else{
									//	$.messager.alert('提示','现场检测负责人为空或者确定时间为空','warning');
									//}												
							}else{
								//$('#MissionPrint-blank-form').submit()
								$.messager.alert('提示','请选择一行数据','warning');
							}
						}
				}]
			});
			var lastIndex;
			var products3 = [
				{id:1,name:'检定'},
				{id:2,name:'校准'},
				{id:3,name:'检测'},
				{id:4,name:'检验'}
			];
			$('#table5').datagrid({
				title:'本次检验的器具',
				width:930,
				height:250,
				//fit:true,
				singleSelect:true, 
				nowrap: false,
				striped: true,
				//sortName: 'ApplianceSpeciesName',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'ApplianceSpeciesName',title:'器具授权名',width:120,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:180,editor:'text',align:'center'},
				
					{field:'Quantity',title:'台/件数',width:70,editor:'numberbox',align:'center'},
					{field:'WorkStaff',title:'派定人',width:80,align:'center',editor:'text'},
					{field:'AssistStaff',title:'替代人',width:120,editor:'text',align:'center'},
					
					{field:'ApplianceCode',title:'出厂编号',editor:'text',width:80,align:'center'},
					{field:'AppManageCode',title:'管理编号',editor:'text',width:80,align:'center'},
					{field:'Model',title:'型号规格',width:80,editor:'text',align:'center'},
					{field:'Range',title:'测量范围',width:80,editor:'text',align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,editor:'text',align:'center'},
					{field:'Manufacturer',title:'制造厂商',width:80,editor:'text',align:'center'},
					{field:'ReportType',title:'报告形式',width:80,editor:'text',align:'center',editor:{
						type:'combobox',
						options:{
							valueField:'id',
							textField:'name',
							data:products3,
							required:true
						}
					},
					formatter:function(value,rowData,rowIndex){
						if(value == 1 || value == '1')
						{
							rowData['ReportType']=1;
							return "检定";
						}
						if(value == 2 || value == '2')
						{
							rowData['ReportType']=2;
							return "校准";
						}
						if(value == 3 || value == '3')
						{
							rowData['ReportType']=3;
							return "检测";
						}
						if(value == 4 || value == '4')
						{
							rowData['ReportType']=4;
							return "检验";
						}
					}},
					{field:'TestFee',title:'检测费合计',width:80,align:'center'},
					{field:'RepairFee',title:'修理费',width:80,align:'center'},
					{field:'MaterialFee',title:'材料费',width:80,align:'center'}				
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("您确定要移除这些器具吗？");
						if(result == false){
							return false;
						}
						var rows = $('#table5').datagrid('getSelections');
						var length = rows.length;
						for(var i=length-1; i>=0; i--){
							var index = $('#table5').datagrid('getRowIndex', rows[i]);
							$('#table5').datagrid('deleteRow', index);
						}
					}
				}
				//,'-',{
//					text:'完成编辑',
//					iconCls:'icon-save',
//					handler:function(){
//						$('#table5').datagrid('acceptChanges');
//						
//					}
//				}
				//,'-',{
//					text:'添加器具',
//					iconCls:'icon-add',
//					handler:function(){
//						AddRecord();
//					}
//				}
				],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onClickRow:function(rowIndex, rowDataParam){
					var rows = $(this).datagrid('getSelections');			
					var rowData = null;	//查找界面上第一个勾选的项
					var minIndex = null;
					for(var i = 0; i< rows.length;i++){
						var rowIndexTemp = $(this).datagrid('getRowIndex', rows[i]);
						if(rowData == null){
							rowData=rows[i];
							minIndex = rowIndexTemp;
						}else{
							if(minIndex >= rowIndexTemp){
								rowData=rows[i];
								minIndex = rowIndexTemp;
							}
																				
						}
					}
					if(rowData == null){
						return false;
					}

					var appSpeComboData = {   //器具授权名称
						SpeciesType:rowData.SpeciesType,
						ApplianceSpeciesId:rowData.ApplianceSpeciesId,
						Name:rowData.ApplianceSpeciesName,
						PopName:rowData.ApplianceName
					};	
					$("#ApplianceSpeciesName").combobox('loadData',[appSpeComboData]);
					$("#ApplianceSpeciesName").combobox('setValue', rowData.ApplianceSpeciesName);
					$("#SpeciesType").val(rowData.SpeciesType);	//隐藏的ID
					$("#ApplianceSpeciesId").val(rowData.ApplianceSpeciesId);	//隐藏的ID
					$("#ApplianceName").combobox('setValue',rowData.ApplianceName);	//器具名称
					
					$("#StandName").val(rowData.ApplianceSpeciesName);		//器具标准名称
										
					$("#RepairFee").val(rowData.RepairFee);	//
					$("#MaterialFee").val(rowData.MaterialFee);	//
					$("#TestFee").val(rowData.TestFee);	//
					
					$("#ApplianceCode").val(rowData.ApplianceCode);	//出厂编号
					$('#AppManageCode').val(rowData.AppManageCode);	//管理编号
					$('#Model').combobox('setValue', rowData.Model);	//型号规格
					$('#Range').combobox('setValue', rowData.Range);	//测量范围
					$('#Accuracy').combobox('setValue', rowData.Accuracy);	//精度等级			
					$('#Manufacturer').combobox('setValue', rowData.Manufacturer);	//制造厂
					$('#Quantity').val(rowData.Quantity);	//器具数量
							
					$("#ReportType").val(rowData.ReportType);	//报告形式
					$("#WorkStaff").combobox('setValue', rowData.WorkStaff);	//派定人
					$('AssistStaff').val(rowData.AssistStaff)	;		
					if(rowData.SpeciesType == 0 || rowData.SpeciesType == '0' || rowData.SpeciesType == 2 || rowData.SpeciesType == '2'){	//标准名称Or常用名称
						if(rowData.ApplianceName == null || rowData.ApplianceName.length == 0){
							$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);
						}else{
							$("#ApplianceName").combobox('setValue',rowData.ApplianceName);
						}
						$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//型号规格
						$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//测量范围
						$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//精度等级
						$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//制造厂商
					}else if(rowData.SpeciesType == 1 || rowData.SpeciesType == '1'){	//分类名称
						$("#ApplianceName").combobox('loadData',[]);
						$("#Model").combobox('loadData',[]);	//型号规格
						$("#Range").combobox('loadData',[]);	//测量范围
						$("#Accuracy").combobox('loadData',[]);	//精度等级
						$("#Manufacturer").combobox('loadData',[]);	//制造厂商
					}
					$("#WorkStaff").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//检验人员
					
				
				},
				onSelect:function(rowIndex,rowData){
					var row = $("#table5").datagrid("getSelected");
					
					//$('#form-Appliance').form('load',rowData);
					
				}
		
			});
			$('#table_QuoItem').datagrid({
				//width:800,
				//height:300,
				//title:"报价单条目",
				fit:true,
             	singleSelect:false, 
                nowrap: false,
                striped: true,
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'编号',width:40,align:'center'},
					{field:'StandardNameId',title:'受检器具标准名称Id',width:120,align:'center'},
					{field:'StandardName',title:'受检器具标准名称',width:120,align:'center',sortable:true},
					{field:'CertificateName',title:'受检器具证书名称',width:120,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Quantity',title:'台件数',width:80,align:'center',editor:'text'},
					{field:'AppFactoryCode',title:'出厂编号',width:80,align:'center'},
					{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
					{field:'CertType',title:'证书类型',width:80,align:'center',sortable:true,
						formatter:function(value,rowData,rowIndex){
							if(value=='1'){
								rowData['CertType']="1";
								return "检定";
							}
							else if(value == '4'){
								rowData['CertType']="4";
								return "检验";
							}
							else if(value == '2'){
								rowData['CertType']="2";
								return "校准";
							}
							else{
								rowData['CertType']="3";
								return "检测";
							}
						}
					},
					{field:'SiteTest',title:'现场检测',width:80,align:'center',sortable:true,
						formatter:function(value,rowData,rowIndex){
							if(value=='0'){
								rowData['SiteTest'] ="0";
								return "是";
							}
							else{
								rowData['SiteTest'] ="1";
								return "否";
							}
						}
					},
					{field:'MinCost',title:'检测费最少',width:80,align:'center',
						formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
						
					}},
					{field:'MaxCost',title:'检测费最多',width:80,align:'center',
						formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
						
					}},
					{field:'Remark',title:'备注',width:80,align:'center'}
				]],
				rownumbers:false,
				pagination:false,
				toolbar:"#quo-search-toolbar"
			});
			$('#table6').datagrid({//历史记录
				fit:true,
				//title:'委托单位历史任务器具记录',
				singleSelect:false, 
				nowrap: false,
				striped: true,
				//sortName: 'ApplianceSpeciesName',
				remoteSort: false,
				//idField:'Id',
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					
					{field:'ApplianceSpeciesName',title:'器具授权名',width:120,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:180,align:'center'},
				
					{field:'Quantity',title:'台/件数',width:70,align:'center'},
					{field:'WorkStaff',title:'派定人',width:80,align:'center'},
					{field:'AssistStaff',title:'替代人',width:120,align:'center'},
					
					{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
					{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Accuracy',title:'不确定度/准确度等级/最大允差',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂商',width:80,align:'center'},
					{field:'ReportType',title:'报告形式',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 1 || value == '1')
							{
								rowData['ReportType']=1;
								return "检定";
							}
							if(value == 2 || value == '2')
							{
								rowData['ReportType']=2;
								return "校准";
							}
							if(value == 3 || value == '3')
							{
								rowData['ReportType']=3;
								return "检测";
							}
							if(value == 4 || value == '4')
							{
								rowData['ReportType']=4;
								return "检验";
							}
					}},
					{field:'TestFee',title:'检测费',width:80,align:'center'}				
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#table6-search-toolbar"
			});
			
			
			$('#table5').datagrid('loadData',{total:0,rows:[]});
			$('#table6').datagrid('loadData',{total:0,rows:[]});		
		    $("#table-Appliance").hide();
		
		
			$("#add").window({			
				onBeforeClose:function(){
					$('#table5').datagrid('loadData',{total:0,rows:[]});
					$('#form-Appliance').form('clear');
					$('#frm_add_customer').form('clear');
				}
			});
			$("#inputQuo_window").window({			
				onBeforeClose:function(){
					$('#table_QuoItem').datagrid('loadData',{total:0,rows:[]});
					$('#frm_inputQuo').form('clear');
				}
			});
			$("#inputHistory_window").window({			
				onBeforeClose:function(){
					$('#table6').datagrid('loadData',{total:0,rows:[]});
					$('#inputHistory-form').form('clear');
				}
			});
			$("#edit").window({			
				onBeforeClose:function(){
					$('#table5').datagrid('loadData',{'total':0,'rows':[]});
					$('#form-Appliance').form('clear');
				}
			});
			$("#check").window({			
				onBeforeClose:function(){
					$('#table5').datagrid('loadData',{total:0,rows:[]});
					$('#form-Appliance').form('clear');
				}
			});
			
			query();

		});
		
		function UpdateRecord()	//更新一条检验的器具
		{
			var rows = $('#table5').datagrid('getSelections');
			if(rows.length == 0){
				$.messager.alert("提示","请在‘本次检验的器具’中选择一个需要更新的器具！","info");
				return false;
			}
			if(rows.length > 1){
				$.messager.alert("提示","只能在‘本次检验的器具’中选择一个需要更新的器具！","info");
				return false;
			}
			
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("提示","请选择一个有效的‘器具授权名’！","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("提示","请输入‘器具数量’！","info");
				return false;
			}
		    var testFee="";
			var totalFee="";
			if($("#TestFee").val()==null||$("#TestFee").val().length==0){
				if($('#SpeciesType').val()==0||$('#SpeciesType').val()=='0'){
					$.ajax({   //根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
						type:'POST',
						url:'/jlyw/TargetApplianceServlet.do?method=13',
						data:{'Application':encodeURI($('#StandName').val()),'Model':encodeURI($('#Model').combobox('getText')),'Range':encodeURI($('#Range').combobox('getText')),'Accuracy':encodeURI($('#Accuracy').combobox('getText'))},
						dataType:"json",
						async: false,
						success:function(data, textStatus){
							if(data.IsOK){//费用取得成功	
								//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
								if(data.MinFee==data.MaxFee){						
									testFee=data.MinFee;
									//console.info("TestFee:"+testFee);
								}
							}else{//费用取得失败
								$.messager.alert('错误！',data.msg,'error');
							}
						}
					});
				}
				
				if(testFee.length > 0){
					rows[0].TestFee=getInt(testFee)*getInt($('#Quantity').val());
				}else{
					rows[0].TestFee="";
				}
				
		    }else{
				rows[0].TestFee=getInt($('#TestFee').val());
			}
			var index = $("#table5").datagrid("getRowIndex", rows[0]);
			rows[0].SpeciesType=$('#SpeciesType').val();
			rows[0].ApplianceSpeciesId=$('#ApplianceSpeciesId').val();
			//rows[0].ApplianceSpeciesName=$('#ApplianceSpeciesName').combobox('getValue');
			rows[0].ApplianceSpeciesName=$("#StandName").val(),
			rows[0].ApplianceName=$('#ApplianceName').combobox('getValue');
			rows[0].ApplianceCode=$('#ApplianceCode').val();
			rows[0].AppManageCode=$('#AppManageCode').val();
			rows[0].Model=$('#Model').combobox('getValue');
			rows[0].Range=$('#Range').combobox('getValue');
			rows[0].Accuracy=$('#Accuracy').combobox('getValue');
			rows[0].Manufacturer=$('#Manufacturer').combobox('getValue');
			rows[0].Quantity=$('#Quantity').val();
			rows[0].ReportType=$('#ReportType').val();	
			rows[0].WorkStaff=$('#WorkStaff').combobox('getValue');
			rows[0].AssistStaff = $('#AssistStaff').val();
			
			rows[0].RepairFee=$('#RepairFee').val();
			rows[0].MaterialFee=$('#MaterialFee').val();
			
			//console.info(rows[0]);
			
			$('#table5').datagrid('updateRow', {
				index: index,
				row:rows[0]
			});
			//console.info("12")
			$('#SpeciesType').val("");
			$('#ApplianceSpeciesId').val("");
			$('#ApplianceSpeciesName').combobox('setValue',"");
			$('#ApplianceName').val("");
			$('#ApplianceCode').val("");
			$('#AppManageCode').val("");
			$('#Quantity').val("");
			$('#ReportType').val("");
			$('#RepairFee').val("");
			$('#MaterialFee').val("");
			$('#form-Appliance').form('clear');
		}
		function cancel(){
			$('#edit').window('close');
		}
		function editMission(id){
			var data=$('#table2').datagrid('selectRecord',id);
			var rowdata=$('#table2').datagrid('getSelected');
			
			var titlecode ="现场委托书号为“"+rowdata.Code+"”的现场任务修改";
			$('#edit').window({title:titlecode});
			$('#edit').append($("#table-Appliance"));
			$("#table-Appliance").show();
			$('#edit').window('open');			
		
			//var data=$('#table2').datagrid('selectRecord',id);
			//var rowdata=$('#table2').datagrid('getSelected');
			$('#frm_update_localmission').form('load', rowdata);
			$("#HeadName").combobox('select',rowdata.HeadNameId);
			$('#table5').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=11';
			$('#table5').datagrid('options').queryParams={'Id':rowdata.Id};
			
			$('#table5').datagrid('reload');
				
		}
		
		function query()
		{
			var History_BeginDate=$('#History_BeginDate').datebox('getValue');
			var History_EndDate=$('#History_EndDate').datebox('getValue');
			
			$('#table2').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=8';
			$('#table2').datagrid('options').queryParams={'QueryName':encodeURI($("#QueryName").combobox('getText')),'Department':encodeURI($("#Department").val()),'MissionStatus':encodeURI($("#MissionStatus").val()),'Code':encodeURI($('#QueryCode').val()),'History_BeginDate':encodeURI(History_BeginDate),'History_EndDate':encodeURI(History_EndDate)};
			
			$('#table2').datagrid('reload');
		}
		
		function savereg(){//修改任务信息
		    var rowdata=$("#table2").datagrid("getSelected");
			if(rowdata.Status==2){
					$.messager.alert('提示','该任务状态“已完工”，不能进行修改！','info');
					return false;
			}
			$('#frm_update_localmission').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=3',
				onSubmit:function(){
					$('#table5').datagrid('acceptChanges');					
					var rows = $("#table5").datagrid("getRows");	
					if(rows.length == 0){
						$.messager.alert('提示',"请选择检验的器具！",'info');
						return false;
					}
					for(var i=0; i<rows.length; i++){
						if(rows[i].Quantity == null || rows[i].Quantity == 0){
							$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（大于0的整数）！",'info');
							return false;
						}
					}
					$("#Appliances1").val(JSON.stringify(rows));
					
					return $('#frm_update_localmission').form('validate');
				},
		   		success:function(data){
		   			//alert(data);
		   			var result = eval("("+data+")");
		   			if(result.IsOK){
					     cancel();
		   				
						$('#table2').datagrid('reload');
						 $.messager.alert('提示！','修改成功','info');
		   			}else{
						$.messager.alert('修改失败！',result.msg,'error');
		   			}
					
		   		 }
			});
			
		}
		function saveundo(){//反馈
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
		
		function savereg1(){//添加新任务
			$('#table5').datagrid('acceptChanges');					
			var rows = $("#table5").datagrid("getRows");	
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=1',
				onSubmit:function(){
					//alert('onSubmit');
					if($('#RegionIdAdd').val() == ''){
						$.messager.alert('提示','请选择一个有效的地区！','info');
						return false;
					}
					
				
					if(rows.length == 0){
						$.messager.alert('提示',"请选择检验的器具！",'info');
						return false;
					}
					for(var i=0; i<rows.length; i++){
						if(rows[i].Quantity == null || rows[i].Quantity == 0){
							$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（大于0的整数）！",'info');
							return false;
						}
					}
					$("#Appliances").val(JSON.stringify(rows));
					
					return $('#frm_add_customer').form('validate');
				},
		   		success:function(data){
		   		    var result = eval("("+data+")");
		   		    if(result.IsOK){
		   		   		
						 $('#table2').datagrid('reload');
						$('#add').window('close');
						 $.messager.alert('提示！','添加成功','info');
					}
					else{
						 $.messager.alert('添加失败！',result.msg,'error');
					}
		   		 }
			});
		}
		function cancelcheck(){
			$('#check').window('close');
		}
		
		function saveregcheck(){//核定新任务
			$('#table5').datagrid('acceptChanges');	
			 var rowdata=$("#table2").datagrid("getSelected");
			if(rowdata.Status==2){
					$.messager.alert('提示','该任务状态“已完工”，不能进行修改！','info');
					return false;
			}		
			
			if($("#CheckDatecheck").datebox('getValue')==null||$("#CheckDatecheck").datebox('getValue').length==0){
				$.messager.alert('提示','核定任务请选择核定日期！','info');
				return false;
			}
			if($("#SiteManagerNamecheck").combobox('getText')==null||$("#SiteManagerNamecheck").combobox('getText').length==0){
				$.messager.alert('提示','核定任务请选择现场负责人！','info');
				return false;
			}
			var rows = $("#table5").datagrid("getRows");	
			$('#frm_check_localmission').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=16',
				onSubmit:function(){
					//alert('onSubmit');
					if($('#RegionIdcheck').val() == ''){
						$.messager.alert('提示','请选择一个有效的地区！','info');
						return false;
					}
					
				
					if(rows.length == 0){
						$.messager.alert('提示',"请选择检验的器具！",'info');
						return false;
					}
					for(var i=0; i<rows.length; i++){
						if(rows[i].Quantity == null || rows[i].Quantity == 0){
							$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（大于0的整数）！",'info');
							return false;
						}
					}
					$("#Appliancescheck").val(JSON.stringify(rows));
					
					return $('#frm_check_localmission').form('validate');
				},
		   		success:function(data){
		   		    var result = eval("("+data+")");
		   		    if(result.IsOK){
		   		   		
						 $('#table2').datagrid('reload');
						 $('#check').window('close');
						 $.messager.alert('提示！','核定成功','info');
					}
					else{
						 $.messager.alert('核定失败！',result.msg,'error');
					}
		   		 }
			});
		}
		function AddRecord(){//添加器具
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("提示","请选择一个有效的‘器具授权名’！","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("提示","请输入'器具数量'！","info");
				return false;
			}
			
			if($("#ReportType").val()==''||$("#ReportType").val().length==0){
				$.messager.alert("提示","请选择报告形式！","info");
				return false;
			}
			
		    var testFee="";
			//alert("121");
			var index = $('#table5').datagrid('getRows').length;	//在最后一行新增记录
			 var testfeeTotal;
			
			//var index1 = 1;	//在最后一行新增记录
			if($("#TestFee").val()==null||$("#TestFee").val().length==0){
				if($('#SpeciesType').val()==0||$('#SpeciesType').val()=='0'){
					$.ajax({   //根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
						type:'POST',
						url:'/jlyw/TargetApplianceServlet.do?method=13',
						data:{'Application':encodeURI($('#StandName').val()),'Model':encodeURI($('#Model').combobox('getText')),'Range':encodeURI($('#Range').combobox('getText')),'Accuracy':encodeURI($('#Accuracy').combobox('getText'))},
						dataType:"json",
						async: false,
						success:function(data, textStatus){
							if(data.IsOK){//费用取得成功	
								//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
								if(data.MinFee==data.MaxFee){						
									testFee=data.MinFee;
									//console.info("TestFee:"+testFee);
								}
							}else{//费用取得失败
								$.messager.alert('错误！',data.msg,'error');
							}
						}
					});
				}
				
				if(testFee.length > 0){
					testfeeTotal = getInt(testFee)*getInt($('#Quantity').val());
				}else{
					testfeeTotal = "";
				}
				
			}else{
				testfeeTotal=getInt($('#TestFee').val());
			}
			
			
			$('#table5').datagrid('insertRow', {
				index: index,
				row:{		
					
					SpeciesType:$('#SpeciesType').val(),
					ApplianceSpeciesId:$('#ApplianceSpeciesId').val(),
					//ApplianceSpeciesName:$('#ApplianceSpeciesName').combobox('getValue'),
					ApplianceSpeciesName:$("#StandName").val(),
					ApplianceName:$('#ApplianceName').combobox('getValue'),	
					ApplianceCode:$('#ApplianceCode').val(),
					AppManageCode:$('#AppManageCode').val(),
					Model:$('#Model').combobox('getValue'),
					Range:$('#Range').combobox('getValue'),
					Accuracy:$('#Accuracy').combobox('getValue'),
					Manufacturer:$('#Manufacturer').combobox('getValue'),
					ReportType:$('#ReportType').val(),
					Quantity:$('#Quantity').val(),		
								
					WorkStaff:$('#WorkStaff').combobox('getText'),
					AssistStaff:$('#AssistStaff').val(),
					TestFee:testfeeTotal,
					RepairFee:$('#RepairFee').val(),
					MaterialFee:$('#MaterialFee').val()
				}
			});
			
			$('#SpeciesType').val(""),
			$('#ApplianceSpeciesId').val(""),
			$('#ApplianceSpeciesName').combobox('setValue',""),
			$('#ApplianceName').val("");			
			$('#Quantity').val("");
			$('#WorkStaff').combobox('setValue',"");	
			$('#WorkStaffId').val("");
			$('#AssistStaff').val("");
			$('#RepairFee').val("");
			$('#MaterialFee').val("");
			$('#TestFee').val("");
		}
		function inputQuo(){//
			$('#inputQuo_window').window('open');//打开报价单查询窗口	
			if($('#NameAdd').combobox('getText').length>0){
				$("#CustomerName").combobox('setValue',$('#NameAdd').combobox('getText'));
				$("#QuotationNumber").combobox('reload','/jlyw/QuotationServlet.do?method=11&QueryName='+encodeURI($('#NameAdd').combobox('getText')));
			}
			
				
		}
		function inputQuoItem(){//导入所选条目
			var rows = $("#table_QuoItem").datagrid("getSelections");				
			$("#QuoItemRows").val(JSON.stringify(rows));	
			
			$('#frm_inputQuo').form('submit',{
				url:'/jlyw/QuotationServlet.do?method=12',
				onSubmit:function(){
					var selects = $('#table_QuoItem').datagrid('getSelections');
					if(selects.length==0){
						$.messager.alert('提示','请先选择报价单条目！','info');
						return false;
					}
								
					$("#QuoItemRows").val(JSON.stringify(selects));	
					
					return $('#frm_inputQuo').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");									
					if(result.IsOK){
						var items = result.rows;//导入操作
						var index = $('#table5').datagrid('getRows').length;
						var testfeeTotal;
						for(var i=0;i<items.length;i++)
						{
							/****重新计算检测费汇总***/
							var testFee="";
							//var index1 = 1;	//在最后一行新增记录
							if(items[i].SpeciesType==0||items[i].SpeciesType=='0'){
								$.ajax({   //根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
									type:'POST',
									url:'/jlyw/TargetApplianceServlet.do?method=13',
									data:{'Application':encodeURI(items[i].ApplianceSpeciesName),'Model':encodeURI(items[i].Model),'Range':encodeURI(items[i].Range),'Accuracy':encodeURI(items[i].Accuracy)},
									dataType:"json",
									async: false,
									success:function(data, textStatus){
										if(data.IsOK){//费用取得成功	
											//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
											if(data.MinFee==data.MaxFee){						
												testFee=data.MinFee;
												//console.info("TestFee:"+testFee);
											}
										}else{//费用取得失败
											$.messager.alert('错误！',data.msg,'error');
										}
									}
								});
							}
							
							if(testFee.length > 0&&items[i].Quantity!=null&&items[i].Quantity!=""){
								testfeeTotal = getInt(testFee)*getInt(items[i].Quantity);
							}else{
								testfeeTotal = "";
							}
							/****重新计算检测费汇总结束***/	

							$('#table5').datagrid('insertRow',{
								index:index,
								row:{
									
									SpeciesType:items[i].SpeciesType,
									ApplianceSpeciesId:items[i].ApplianceSpeciesId,
									ApplianceSpeciesName:items[i].ApplianceSpeciesName,
									ApplianceName:items[i].ApplianceName,	
															
									ApplianceCode: items[i].ApplianceCode,											
									AppManageCode: items[i].AppManageCode,
									Manufacturer: items[i].Manufacturer,
									
									Model:items[i].Model,
									Accuracy:items[i].Accuracy,
									Range:items[i].Range,
									Quantity:items[i].Quantity,
									ReportType:items[i].ReportType,
									WorkStaff:"",
									AssistStaff:"",
									TestFee: testfeeTotal,
									RepairFee:"",
									MaterialFee:""
									
								}
							});	
							index++;
						}
						
						$("#frm_add_customer").form('load',result);
						 $('#inputQuo_window').window('close');
					}else{
						alert(result.msg);
					}
				}
			});
										
		}
		function searchQuoItem()//查询报价单条目
		{
			if(!$("#frm_inputQuo").form('validate'))
				return false ;
			$('#QuoNumber').val($('#QuotationNumber').combobox('getText'));
			$('#table_QuoItem').datagrid('options').url='/jlyw/QuotationServlet.do?method=5';
			$('#table_QuoItem').datagrid('options').queryParams={'quotationId':encodeURI($('#QuoNumber').val())};
			$('#table_QuoItem').datagrid('reload');
	
		}
		function doLoadHistoryAppItems()
		{
			if($('#NameAdd').combobox('getText').length==0){
				$.messager.alert('提示','请输入委托单位的名称！','info');
				return false;
			}
			//console.info($('#NameAdd').combobox('getText'));
			$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=1';
			$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#NameAdd').combobox('getText')),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#table6_History_BeginDate').datebox('getValue'),'EndDate':$('#table6_History_EndDate').datebox('getValue')};
			$('#table6').datagrid('reload');
		}
		
		function AddRecordFromHistory(){
			var items = $('#table6').datagrid('getSelections');
			var index = $('#table5').datagrid('getRows').length;	//在最后一行新增记录
			var testfeeTotal;
			for(var i=0; i<items.length; i++){
				if(items[i].ApplianceSpeciesNameStatus != 0){
					$.messager.alert('提示',"授权名称 '"+items[i].ApplianceSpeciesName+"' 已过时，不可添加！",'info');
					continue;
				}
				/****重新计算检测费汇总***/
				var testFee="";
				//var index1 = 1;	//在最后一行新增记录
				if(items[i].SpeciesType==0||items[i].SpeciesType=='0'){
					$.ajax({   //根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
						type:'POST',
						url:'/jlyw/TargetApplianceServlet.do?method=13',
						data:{'Application':encodeURI(items[i].ApplianceSpeciesName),'Model':encodeURI(items[i].Model),'Range':encodeURI(items[i].Range),'Accuracy':encodeURI(items[i].Accuracy)},
						dataType:"json",
						async: false,
						success:function(data, textStatus){
							if(data.IsOK){//费用取得成功	
								//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
								if(data.MinFee==data.MaxFee){						
									testFee=data.MinFee;
									//console.info("TestFee:"+testFee);
								}
							}else{//费用取得失败
								$.messager.alert('错误！',data.msg,'error');
							}
						}
					});
				}
				
				var testfeeTotal;
				if(testFee.length > 0&&items[i].Quantity!=null&&items[i].Quantity!=""){
					testfeeTotal = getInt(testFee)*getInt(items[i].Quantity);
					
				}else{
					testfeeTotal = "";
					
				}
			
				/****重新计算检测费汇总结束***/			
				$('#table5').datagrid('insertRow', {
					index: index,
					row:{
						
						SpeciesType:items[i].SpeciesType,
						ApplianceSpeciesId:items[i].ApplianceSpeciesId,
						ApplianceSpeciesName:items[i].ApplianceSpeciesName,
						ApplianceName:items[i].ApplianceName,	
												
						ApplianceCode: items[i].ApplianceCode,											
						AppManageCode: items[i].AppManageCode,
						Manufacturer: items[i].Manufacturer,
						
						Model:items[i].Model,
						Accuracy:items[i].Accuracy,
						Range:items[i].Range,
						Quantity:items[i].Quantity,
						ReportType:items[i].ReportType,
						WorkStaff:items[i].WorkStaff,
						AssistStaff:items[i].AssistStaff,
						TestFee:testfeeTotal,
						RepairFee:"",
						MaterialFee:""
					}
				});
				index++;
			}
			$('#table6').datagrid('clearSelections');
			$('#inputHistory_window').window('close');
		}
	function inputHistoryOpen(){
		if($('#NameAdd').combobox('getText').length==0){
				$.messager.alert('提示','请输入委托单位的名称！','info');
				return false;
		}
		$('#inputHistory_window').window('open');
	}
	function CustomerLC(){//委托方来车
		var selected = $('#table2').datagrid('getSelected');
		if(selected != null){
			if(selected.Status==2||selected.Status==3){
				$.messager.alert('提示','该任务“已注销”或者“已完工”，不能进行此操作！','info');
				return false;
			}
			
			if(!$("#CustomerLC_form").form('validate'))
				return false ;
				
			var result = confirm("您确定要进行此操作吗？");
			if(result == false){
				return false;
			}
			$.ajax({
				type:'POST',
				url:'/jlyw/LocaleMissionServlet.do?method=10',
				data:{id:selected.Id,ExactTime:$('#ExactTime').datebox('getValue'),Remark:encodeURI($('#RemarkLC').val()),QTway:encodeURI($('#QTway').combobox('getValue'))},
				dataType:"json",
				success:function(data, textStatus){
					if(data.IsOK){
						$('#table2').datagrid('reload');
						$('#CustomerLC_window').window('close');
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
	function openBDwindow(){
		if($("#SiteManagerNameAdd").combobox('getText')==null||$("#SiteManagerNameAdd").combobox('getText').length==0){
			$.messager.alert('提示',"请填写现场负责人！",'error');
			return;
		}
		$('#MissionBD_window').window('open');//打开报价单查询窗口	
		var nowDate = new Date();
		var dateString=nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
		
		$('#ExactTimeBD').datebox('setValue',dateString);
	}
	
	function generateSheet(){
		
		var row = $('#table2').datagrid('getSelected');
							
		if(row)
		{
			<%
				SysUser loginuser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
			%>
			if(!('<%=loginuser.getName()%>'==row.SiteManagerName||'<%=loginuser.getName()%>'=='系统管理员')){
				$.messager.alert('提示','非现场负责人和系统管理员不能生成现场委托单!','warning');
				return;
			}
				
		 	if(row.SiteManagerName!=null&&row.SiteManagerName.length>0&&row.ExactTime!=null&&row.ExactTime.length>0){
								
					$('#TZ_LocaleCommissionCode').val(row.Code);
					$('#TZForm').submit();
			}else{
				$.messager.alert('提示','现场检测负责人为空或者确定时间为空','warning');
			}
		
							
		}else{
			$.messager.alert('提示','请选择一行数据','warning');
		}
	}
	
	function saveBD(){//补登业务
		$('#table5').datagrid('acceptChanges');					
		var rows = $("#table5").datagrid("getRows");	
		
		$('#frm_add_customer').form('submit',{
			url: '/jlyw/LocaleMissionServlet.do?method=17&'+decodeURIComponent($("#MissionBD_form").serialize(),true),
			onSubmit:function(){
				//alert('onSubmit');
				if($('#RegionIdAdd').val() == ''){
					$.messager.alert('提示','请选择一个有效的地区！','info');
					return false;
				}
							
				if(rows.length == 0){
					$.messager.alert('提示',"请选择检验的器具！",'info');
					return false;
				}
				for(var i=0; i<rows.length; i++){
					if(rows[i].Quantity == null || rows[i].Quantity == 0){
						$.messager.alert('提示',"请输入 '"+rows[i].ApplianceSpeciesName+"' 的台/件数（大于0的整数）！",'info');
						return false;
					}
				}
				$("#Appliances").val(JSON.stringify(rows));
				
				return ($('#frm_add_customer').form('validate') && $("#MissionBD_form").form('validate'));
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#MissionBD_form").form('clear');
					 $('#table2').datagrid('reload');
					$('#MissionBD_window').window('close');
					
					 $.messager.alert('提示！','添加成功','info');
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
<form id="TZForm" method="post" action="/jlyw/SFGL/wtd/LocaleCommissionSheet.jsp" target="_blank">
	 <input id="TZ_LocaleCommissionCode"  type="hidden" name="LocaleCommissionCode"/>
</form>	
		<div style="margin-bottom:10px;padding-top:10px;padding-bottom:10px;width:1000px" class="easyui-panel" title="查询条件" collapsible="false"  closable="false">
			<form id="ff">
			<table id="table1" style="width:980px">
				<tr>
					<td  align="left">
					<label>委托单位：</label><select name="QueryName" id="QueryName" style="width:152px"></select>&nbsp;
					<label>业务部门：</label><input name="Department" id="Department" style="width:152px"/>&nbsp;
					<label>状&nbsp;&nbsp;&nbsp;&nbsp;态：</label><select name="MissionStatus" id="MissionStatus" style="width:152px"><option value="" >全部</option><option value="4">未核定</option><option value="5">已核定</option><option value="1">已分配</option><option value="2">已完成</option><option value="0" selected="selected">未完成</option></select>
                    </td>
					<td align="left"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
				</tr>
                <tr>
                	<td align="left">
                    <label>任务书号：</label><input class="easyui-validatebox" id="QueryCode" type="text" style="width:152px" />&nbsp;
					<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:152px" />&nbsp;
					<label>结束时间：</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:152px" />
                    </td>
                </tr>
			</table>
			</form>
		</div>
		
		<table id="table2" style="height:500px; width:1000px;padding-top:10px"></table>
		
	<form id="MissionPrint-form" name="MissionPrint" method="post" action="/jlyw/LocaleMissionServlet.do?method=13" target="MissionPrintFrame">
		<input id="localeMissionId" name="localeMissionId" type="hidden" />				
	</form>
	<form id="MissionPrint-blank-form" name="MissionPrint-blank-form" method="post" action="/jlyw/LocaleMissionServlet.do?method=18" target="MissionPrintFrame">	
		<input id="localeMissionId1" name="localeMissionId" type="hidden" />		
	</form>
			
	<iframe id="MissionPrintFrame" name="MissionPrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>	

<div id="edit" class="easyui-window" modal="true" title="修改现场业务信息" style="padding: 10px;width: 1000;position:relative" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_update_localmission" method="post">
				<input type="hidden" id="Id" name="Id" value="" />
				<input id="Appliances1" name="Appliances" type="hidden" />
				<div>
				 <table style="width:980px;" >
					<tr >
						<td align="right" style="width：10%">单位名称：</td>
						<td align="left"  style="width：20%"><select name="Name" id="Name" class="easyui-validatebox" require="true" style="width:178px" readonly="readonly"></select><input type="hidden" name="CustomerId" id="CustomerId" /></td>
						<td align="right" style="width：10%">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
						<td align="left"  style="width：20%"><input id="zcd" name="ZipCode" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
						<td align="right" style="width：10%">单位地址：</td>
						<td align="left" style="width：20%"><input id="Address" name="Address" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
					</tr>
					<tr >
						<td align="right">单位电话：</td>
						<td align="left"><input id="Tel" name="Tel" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">联 系 人：</td>
						<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">联系人号码：</td>
						<td align="left"><input id="contactortel" name="ContactorTel" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
					</tr>
			
					<tr >
						<td align="right">所在地区：</td>
						<td align="left">
							<select id="rid" name="Region" class="easyui-combobox" style="width:178px" required="true"></select><input type="hidden" name="RegionId" id="RegionId" /></td>
						<td align="right">暂定日期：</td>
						<td align="left"><input type="text" id="TentativeDate" name="TentativeDate" class="easyui-datebox" style="width:178px" required="true" /></td>
						<td align="right">现场检测负责人：</td>
						<td align="left"><select name="SiteManagerName" id="SiteManagerName" style="width:178px" ></select><input type="hidden" name="SiteManagerId" id="SiteManagerId" value="" /></td>
					</tr>
					<tr>
						<td align="right">业务部门：</td>
						<td align="left"><input id="Department" name="Department" class="easyui-validatebox" style="width:175px"  required="true"/></td>
						<td align="right">核定日期：</td>
						<td align="left"><input type="text" id="CheckDate" name="CheckDate" class="easyui-datebox" style="width:178px"  /></td>
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td align="left"  ><input s id="Remark" name="Remark" style="width:200px" ></input></td>
					</tr>
					<tr>
						<td  align="right">台头名称：</td>
			  			<td  align="left" colspan="5"><select name="HeadName" id="HeadName" style="width:178px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" ></select></td>
					</tr>
					<tr >	
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">确认修改</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="generateSheet()">生成现场委托单</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel()">关闭</a></td>
					</tr>
				</table>
				</div>
			</form>
			
</div>

<div id="check" class="easyui-window" modal="true" title="核定现场业务信息" style="padding: 10px;width: 1000;position:relative" iconCls="icon-ok" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_check_localmission" method="post">
				<input type="hidden" id="Idcheck" name="Id" value="" />
				<input id="Appliancescheck" name="Appliances" type="hidden" />
				<div>
				 <table style="width:980px;" >
					<tr >
						<td align="right" style="width：10%">单位名称：</td>
						<td align="left"  style="width：20%"><select name="Name" id="Namecheck" class="easyui-validatebox" require="true" style="width:178px" readonly="readonly"></select><input  name="CustomerId" id="CustomerIdcheck"  type="hidden"/></td>
						<td align="right" style="width：10%">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
						<td align="left"  style="width：20%"><input id="zcdcheck" name="ZipCode" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
						<td align="right" style="width：10%">单位地址：</td>
						<td align="left" style="width：20%"><input id="Addresscheck" name="Address" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
					</tr>
					<tr >
						<td align="right">单位电话：</td>
						<td align="left"><input id="Telcheck" name="Tel" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">联 系 人：</td>
						<td align="left"><input id="concheck" name="Contactor" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">联系人号码：</td>
						<td align="left"><input id="contactortelcheck" name="ContactorTel" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
					</tr>
			
					<tr >
						<td align="right">所在地区：</td>
						<td align="left">
							<select id="ridcheck" name="Region" class="easyui-combobox" style="width:178px" required="true"></select><input  name="RegionId" id="RegionIdcheck" type="hidden"/></td>
						<td align="right">暂定日期：</td>
						<td align="left"><input type="text" id="TentativeDatecheck" name="TentativeDate" class="easyui-datebox" style="width:178px" required="true" /></td>
						<td align="right">现场检测负责人：</td>
						<td align="left"><select name="SiteManagerName" id="SiteManagerNamecheck" style="width:178px" ></select><input  name="SiteManagerId" id="SiteManagerIdcheck" value="" type="hidden"/></td>
					</tr>
					<tr>
						<td align="right">业务部门：</td>
						<td align="left"><input id="Departmentcheck" name="Department" class="easyui-validatebox" style="width:175px"  required="true"/></td>
						<td align="right">核定日期：</td>
						<td align="left"><input type="text" id="CheckDatecheck" name="CheckDate" class="easyui-datebox" style="width:178px"  /></td>
						<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td align="left"  ><input  id="Remarkcheck" name="Remark" style="width:200px" ></input></td>
					</tr>
					<tr>
						<td  align="right">台头名称：</td>
			  			<td  align="left" colspan="5"><select name="HeadName" id="HeadNamecheck" style="width:178px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
					</tr>
					<tr >	
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="saveregcheck()">确认核定</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancelcheck()">关闭</a></td>
					</tr>
				</table>
				</div>
			</form>
			
</div>
		
<div id="add" class="easyui-window" modal="true" title="录入现场业务信息" style="padding: 10px;width: 1000px; position:relative" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">	
        <form id="frm_add_customer" method="post" >
		<input id="Appliances" name="Appliances" type="hidden" />
	<table style="width:980px;" >
		<tr >
			<td align="right" style="width：10%">单位名称：</td>
			<td align="left"  style="width：20%"><select name="Name" id="NameAdd" style="width:178px"></select><input type="hidden" name="CustomerId" id="CustomerIdAdd" /></td>
			<td align="right" style="width：10%">邮&nbsp;&nbsp;&nbsp;&nbsp;编：</td>
			<td align="left"  style="width：20%"><input id="zcdAdd" name="ZipCode" type="text" style="width:175px"  /></td>
			<td align="right" style="width：10%">单位地址：</td>
			<td align="left" style="width：20%"><input id="AddressAdd" name="Address" type="text" style="width:175px" /></td>
		</tr>
		<tr >			
		    <td align="right">单位电话：</td>
		    <td align="left"><input id="TelAdd" name="Tel" type="text" style="width:175px" /></td>
			<td align="right">联 系 人：</td>
			<td align="left"><input id="conAdd" name="Contactor" type="text" style="width:175px" /></td>
		    <td align="right">联系人号码：</td>
		    <td align="left"><input id="contactortelAdd" name="ContactorTel" type="text" style="width:175px"/></td>
		</tr>

		<tr >
			<td align="right">所在地区：</td>
			<td align="left">
				<select id="ridAdd" name="Region" class="easyui-combobox" style="width:178px" required="true"></select><input type="hidden" name="RegionId" id="RegionIdAdd" /></td>
			<td align="right">暂定日期：</td>
			<td align="left"><input type="text" id="TentativeDateAdd" name="TentativeDate" class="easyui-datebox" style="width:175px" required="true" /></td>
			<td align="right">现场检测负责人：</td>
			<td align="left" colspan="3"><select name="SiteManagerName" id="SiteManagerNameAdd" style="width:178px"></select><input type="hidden" name="SiteManagerId" id="SiteManagerIdAdd" value="" /></td>
		</tr>
	
		<tr >
			<td align="right">业务部门：</td>
			<td  align="left"><input id="DepartmentAdd" name="Department" class="easyui-validatebox" style="width:175px"  required="true"/></td>
			<td align="right">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
			<td align="left"  colspan="3"><input  id="RemarkAdd" name="Remark"  style="width:330px;"  ></input></td>
			
		</tr>
		<tr>
			<td  align="right">台头名称：</td>
			<td  align="left" colspan="5"><select name="HeadName" id="HeadNameAdd" style="width:178px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
		</tr>
		<tr height="20px">	
			
			<td colspan="2"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg1()">添加此业务</a>&nbsp;&nbsp;<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="openBDwindow()">补登</a></td>
			<td ><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="inputQuo()">从报价单导入</a></td>
			<td ><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="inputHistoryOpen()">从历史记录导入</a></td>
			<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel1()">关闭</a></td>
			
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
						
						<td align="center"><a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#undo').window('close');">关闭</a></td>
					</tr>
				</table>
				</div>
			</form>
		</div>
<div id="inputQuo_window" class="easyui-window" modal="true" title="从报价单导入到现场业务中" style="height:400px; width:800px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false">

		<table id="table_QuoItem" ></table>
				
	<!--</form>-->
</div>


<div id="quo-search-toolbar" style="padding:2px 0">
	<form id="frm_inputQuo" method="post">
		 <input name="QuoNumber" id="QuoNumber" type="hidden">
 		 <input name="QuoItemRows" id="QuoItemRows" type="hidden">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="inputQuoItem()">导入所选条目</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>委托单位：</label><input name="CustomerName" class="easyui-combobox" id="CustomerName" style="width:175px" valueField="name" textField='name'>&nbsp;<label>报价单号：</label><input name="QuotationNumber" class="easyui-combobox" id="QuotationNumber" valueField="name" textField='name' required="true"></input>&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询报价单条目" id="btnHistorySearch" onclick="searchQuoItem()">查询报价单条目</a>
					</td>
				</tr>
			</table>
	</form>
</div>

<div id="table-Appliance">	
	<table id="table5" height="240px" width="930px">
	</table>
	<form id="form-Appliance">
	<div style="width: 930px;padding-top:10px; padding-bottom:10px" class="easyui-panel"  modal="true" id="appliance_panel" iconCls="icon-edit">
	 	 <table id="table2" >
			<tr>
				<td width="10%" align="right">器具授权&nbsp;&nbsp;</br>名&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
                <td width="15%" align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:142px"></select><input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /><input type="hidden" id="StandName" name="StandName" value="" /><span style="color:red;">*</span></td>
                <td width="10%" align="right">器具名称：</td>
			    <td width="15%"  align="left"><select name="ApplianceName" class="easyui-combobox" valueField="name" textField='name' id="ApplianceName" style="width:152px"> </select> </td>
				
			    <td width="64" align="right">出厂编号：</td>
			    <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>
				<td width="65" align="right">管理编号：</td>
				<td width="168" align="left"><input id="AppManageCode" name="AppManageCode" type="text" /></td>
						    
			</tr>
			<tr>
				<td align="right">型号规格：</td>
				<td align="left"><select name="Model" id="Model" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">测量范围：</td>
			 	 <td align="left"><select name="Range" id="Range" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">精度等级：</td>
				<td align="left"><select name="Accuracy" id="Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">制 造 厂：</td>
				<td align="left"><select name="Manufacturer" id="Manufacturer" class="easyui-combobox" valueField="name" textField='name' style="width:152px"> </select>	</td>
			</tr>
			<tr>
				<td align="right">报告形式：</td>
				<td align="left">
					<select id="ReportType" name="ReportType" style="width:140px">
						
						<option value="1">检定</option>
						<option value="2">校准</option>
						<option value="3">检测</option>
						<option value="4">检验</option>
					</select><span style="color:red;">*</span>			</td>
				<td width="10%" align="right">数&nbsp;&nbsp;&nbsp;量：</td>
				<td width="15%" align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox" style="width:120px"/>件<span style="color:red;">*</span></td>	
				<td width="10%" align="right">派定人：</td>
                <td width="20%" align="left"><select name="WorkStaff" id="WorkStaff" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
                <td width="10%" align="right">替代人：</td>
			    <td width="20%"  align="left"><input id="AssistStaff" name="AssistStaff" type="text" /></td>
				<!--<td width="10%" align="center" colspan="2"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="AddRecord()">添加器具</a></td>-->
			</tr>
			<tr>
				<!-- <td align="right">维修费：</td>
				<td align="left">
					<input id="RepairFee" name="RepairFee" class="easyui-numberbox" min="0" />
				</td>
				<td  align="right">材料费：</td>
				<td  align="left">
					<input id="MaterialFee" name="MaterialFee" type="text" class="easyui-numberbox" min="0" />
				</td>
				<td  align="right">检测费合计：</td>
				<td  align="left">
					<input id="TestFee" name="TestFee" type="text" class="easyui-numberbox" min="0" />
				</td> -->		
				            
			    <td  colspan="2" align="right">添加<input id="username" name="username" class="easyui-combobox"  url="" style="width:100px;" valueField="name" textField="name" panelHeight="150px" /></td>
				<!--<td width="10%" align="center" colspan="2"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="AddRecord()">添加器具</a></td>-->
			</tr>
			<tr>
				<td align="center" colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"  title="添加器具" onclick="AddRecord()">添加器具</a></td>
				<td align="center" colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"  title="添加器具" onclick="UpdateRecord()">更新器具</a></td>
				
			</tr>
		</table>
	</div>	
	</form>
</div>	
<div id="inputHistory_window" class="easyui-window" modal="true" title="从历史记录导入到现场业务中" style="height:300px; width:850px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false" >
	    <table id="table6">
		</table>		
</div >
<div id="CustomerLC_window" class="easyui-window" modal="true" title="其他交通方式" style="height:230px; width:400px; padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="CustomerLC_form" >
<table>
<tr>
	  <td>
	  <div style="height:30px">
	  <label>&nbsp;&nbsp;准确时间：</label><input class="easyui-datebox" id="ExactTime" name="ExactTime" type="text" style="width:100px" required="true"/>&nbsp;&nbsp;
	  <label>交通方式：</label><select class="easyui-combobox" id="QTway" name="QTway" type="text" style="width:100px" required="true" panelHeight="auto"><option value="来车" selected="selected">来车</option><option value="步行" selected="selected">步行</option><option value="摩托车">摩托车</option><option value="电动车" >电动车</option><option value="自驾车" >自驾车</option><option value="公交" >公交</option></select>&nbsp;
	  </div>
	  </td>
 </tr>
 <tr>
	 <td valign="top">
	  <label style="vertical-align:top">&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;注：</label><textarea name="Remark" class="easyui-validatebox" required="true" id="RemarkLC" cols="30" rows="4"></textarea>
	  
	  </td>
</tr>
 <tr>
	   <td align="center" height="45px" valign="bottom">
	  <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-save"  href="javascript:void(0)" onclick="CustomerLC()">确认</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#CustomerLC_window').window('close');" >关闭</a>
	  </div>
	  </td>	
</tr>
</table>
</form>	
</div >

<div id="MissionBD_window" class="easyui-window" modal="true" title="现场业务补登" style="padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="MissionBD_form"  method="post">
<table>
<tr>
	  <td>

	  <label>确定日期：</label><input class="easyui-datebox" id="ExactTimeBD" name="ExactTime" type="text" style="width:140px" required="true"/>  
	  </td>
	  <td>
	  <label>现场委托书号：</label><input class="easyui-validatebox" id="LocaleMissionCode" name="LocaleMissionCode" type="text" style="width:140px" required="true"/>  
	  </td>
 </tr>
 <tr>
	  <td >
		  <label>交通方式：</label><select class="easyui-combobox" id="QTway1" name="QTway" type="text" style="width:140px" required="true" panelHeight="auto">					            <option value="所内派车" selected="selected">所内派车</option>
		  	<option value="来车">来车</option>
			<option value="步行" >步行</option>
			<option value="摩托车">摩托车</option>
			<option value="电动车" >电动车</option>
			<option value="自驾车" >自驾车</option>
			<option value="公交" >公交</option>
		  </select>
	  <td>
	  <label>&nbsp;&nbsp;&nbsp;&nbsp;车&nbsp;牌&nbsp;号：</label><select id="Licence" name="Licence" style="width:140px" ></select>
	  </td>
 </tr>
 <tr>
	  <td>  
	  <label>乘车司机：</label><select id="Drivername" name="Drivername" style="width:140px" ></select>	
	  </td>
	  <td>
	
	  </td>
</tr>
	
 <tr>
	   <td align="center" height="45px" valign="bottom" colspan="2">
	   <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-save"  href="javascript:void(0)" onclick="saveBD()">确认补登</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#MissionBD_window').window('close');" >关闭</a>
	   </div>
	  </td>	
</tr>
</table>
</form>
</div >

<div id="table6-search-toolbar">
		 	<form id="inputHistory-form">
			<table cellpadding="0" cellspacing="0" style="width:99%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="AddRecordFromHistory()">提交选中的历史记录</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>器具名称：</label><input type="text" id="History_ApplianceName" value="" style="width:100px" />&nbsp;<label>开始时间：</label><input class="easyui-datebox" id="table6_History_BeginDate" type="text" style="width:100px" />&nbsp;<label>截止时间：</label><input class="easyui-datebox" id="table6_History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询历史记录" id="btnHistorySearch" onclick="doLoadHistoryAppItems()">查询历史记录</a>
					</td>
				</tr>
			</table>
			</form>
</div>
</DIV></DIV>
</body>
</html>
