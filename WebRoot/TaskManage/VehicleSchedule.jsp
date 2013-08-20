<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>车辆调度</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
	<script type="text/javascript" src="../JScript/json2.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

	<script>
	    var ar = new Array();
		var start="";
		var end="";
        ar[0] = "星期一";
	    ar[1] = "星期二";
		ar[2] = "星期三";
		ar[3] = "星期四";
		ar[4] = "星期五";
		ar[5] = "星期六";
		ar[6] = "星期日";
		
		$(function(){
			$('#task-table').datagrid({
				//title:'我的出车任务列表',
				singleSelect:true, 
			    //width:1000,
				//height:500,
				fit: true,
                nowrap: false,
                striped: true,
				//url:'/jlyw/VehicleMissionServlet.do?method=0',
				sortName: 'BeginDate',
				remoteSort: false,
				sortOrder:'dec',
				idField:'WithdrawId',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
					
				]],
				columns:[[
					{title:'客户信息',colspan:6,align:'center'},
					{title:'任务信息',colspan:5,align:'center'},
			        {title:'车辆信息',colspan:9,align:'center'}				
					
				],[
					{field:'CreateDate',title:'创建时间',width:100,align:'center',sortable:true},
					{field:'CreatorName',title:'创建人',width:60,align:'center',sortable:true},
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
				rownumbers:true
			
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
		$("#MissionRegion").combobox({
			url:'/jlyw/RegionServlet.do?method=2',
			valueField:'name',
			textField:'name',
			onSelect:function(record){
				
			},
			onChange:function(newValue, oldValue){
				//$("#RegionId").val('');
			}
		});
		 
		$('#username').combobox({
		//	url:'/jlyw/CustomerServlet.do?method=6',
			onSelect:function(){
				if($('#People').val()==null||$('#People').val().length==0)
					$('#People').val($('#username').combobox('getText'));
				else{
					var temp=$('#People').val();
					$('#People').val(temp+$('#username').combobox('getText')+";");
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
	    $("#DriverName").combobox({
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
				$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+encodeURI(newValue));
			}
		});
			$('#test').datagrid({
				title:'车辆信息',
//				iconCls:'icon-save',
//				width:900,
//				height:300,
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				//url:'vehicle_data.json',
				//sortName: 'userid',
			  //sortOrder: 'desc',
				remoteSort: false,

				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'vehicleid',title:'车辆编号',width:80,sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<a style='text-decoration:underline' onclick=\"lookVehicleMission('" + value + "');\"><span style='color: #0033FF'>"+value+"</span></a>";
							
						}
					},
					{field:'limit',title:'限载',width:30,align:'center'}
				]],
				columns:[[
			        {title:ar[0],colspan:2},
					{title:ar[1],colspan:2},
					{title:ar[2],colspan:2},
					{title:ar[3],colspan:2},
					{title:ar[4],colspan:2},
					{title:ar[5],colspan:2},
					{title:ar[6],colspan:2}
				],[
					{field:'onea',title:'上午',width:65},
					{field:'onep',title:'下午',width:65},
					{field:'twoa',title:'上午',width:65},
					{field:'twop',title:'下午',width:65},
					{field:'threea',title:'上午',width:65},
					{field:'threep',title:'下午',width:65},
					{field:'foura',title:'上午',width:65},
					{field:'fourp',title:'下午',width:65},
					{field:'fivea',title:'上午',width:65},
					{field:'fivep',title:'下午',width:65},
					{field:'sixa',title:'上午',width:65},
					{field:'sixp',title:'下午',width:65},
					{field:'sevena',title:'上午',width:65},
					{field:'sevenp',title:'下午',width:65}
				]],
				pagination:true,
				rownumbers:true

			});
			var lastIndex;
			$('#locale').datagrid({
				title:'现场检测业务',
				//width:900,
				//height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				fit:true,
				pageSize:50,
				url:'/jlyw/LocaleMissionServlet.do?method=6',
				sortName: 'CheckDate',
				sortOrder:'dec',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'CreateDate',title:'创建时间',width:80,align:'center',sortable:true},
					//{field:'CreatorName',title:'创建人',width:60,align:'center',sortable:true},

					{field:'Code',title:'委托书号',width:80,align:'center',sortable:true},
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
					{field:'Name',title:'单位名称',width:180,align:'center',sortable:true},
					{field:'Address',title:'单位地址',width:120,align:'center'},
					
					{field:'Department',title:'业务部门',width:80,align:'center'},
					{field:'MissionDesc',title:'器具信息',width:120,align:'center'},
					{field:'Staffs',title:'人员',width:180,align:'center'},
					{field:'VehicleLisences',title:'乘车信息',width:120,align:'center'},
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
					
					{field:'Region',title:'地区',width:80,align:'center',sortable:true},
					{field:'ZipCode',title:'邮编',width:60,align:'center'},				
					{field:'Remark',title:'备注',width:180,align:'center'}
									
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 3){	//已注销
						return 'color:#000000'
					}else if(rowData.Status == 1){	//已分配
						return 'color:#FF0033';	
					}else if(rowData.Status == 2){	//已完工(包括已结账、已结束)
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
				toolbar:"#table6-search-toolbar"
				/*toolbar:[{
					text:'查看所选',
					iconCls:'icon-ok',
					handler:function(){
						var rows = $('#locale').datagrid('getSelections');
						var length = rows.length;
						for(var i=length-1; i>=0; i--){
							//var index = $('#table6').datagrid('getRowIndex', rows[i]);
							//$('#table6').datagrid('deleteRow', index);
							$('#result').datagrid('appendRow',rows[i]);
							$('#locale').datagrid('clearSelections');
						}
					}
				}]*/
			});
			
			$("#DriverName").combobox({
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
							
							$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
						}
					});
			var nowDate = new Date();
			$("#dateTime").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
			$("#ExactTime").datebox({
			
				onSelect:function(record){
					
					$('#BeginDate').datetimebox('setValue',$("#ExactTime").datebox('getText')+" 08:30:00");
				}
			});
			
			
			Search();
			
		});
		

		function ok(){
			 $('#allot').form('submit',{
				//url: 'userAdd.action',
				onSubmit:function(){ return $('#allot').form('validate');},
		   		success:function(){
			   		 cancel();
		   		 }
			});
		}
		
		function doLoadLocaleMission(){//安排调度
			var result_select = $('#locale').datagrid('getSelections');
			var vehicle_select = $('#test').datagrid('getSelected');
			
			if(result_select.length>0&&vehicle_select!=null){
				$('#frm_time_arrange').form('clear');
				
				$('#DriverName').combobox('setValue',vehicle_select.driverName);
				$('#AssemblingPlace').val('本所');
				var nowDate = new Date();
				var dateString=nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate());
				
				$('#ExactTime').datebox('setValue',result_select[0].CheckDate);
				$('#BeginDate').datetimebox('setValue',result_select[0].CheckDate+" 08:30:00");
				for(var i=0;i<result_select.length;i++){
					var temp=$('#People').val();
					
					if(temp.indexOf(result_select[i].Staffs)<0 )  
					{  
						$('#People').val(temp+result_select[i].Staffs);
					}
				}
					
				$('#edit').window('open');
				$('#frm_time_arrange').show();
	
			}else{
				$.messager.alert('warning','请在车辆与任务表中都至少选择一行数据','warning');
			}
		}
		
		function SearchLocaleMission()
		{
			var History_BeginDate=$('#History_BeginDate').datebox('getValue');
			var History_EndDate=$('#History_EndDate').datebox('getValue');
			
			$('#locale').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=6';
			$('#locale').datagrid('options').queryParams={'QueryName':encodeURI($("#QueryName").combobox('getText')),'Department':encodeURI($("#Department").val()),'History_BeginDate':encodeURI(History_BeginDate),'History_EndDate':encodeURI(History_EndDate)};
			
			$('#locale').datagrid('reload');
		}
		
		function cancel(){
			$('#edit').window(close);
		}
		
		function lookVehicleMission(vehicleid){
			
			$('#vehiclemission').window('open');
			 $('#task-table').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=0';
			
			 $('#task-table').datagrid('options').queryParams={'Licence':encodeURI(vehicleid),'StartTime':encodeURI(start),'EndTime':encodeURI(end)};
			 $('#task-table').datagrid('reload');
		}
		
		function Search(){
			
			var datetime=$('#dateTime').datebox('getValue');
			if(datetime == ""){
				$.messager.alert("提示","请先选择日期","info");	
				return false;
			}
			var Lisence=$('#License').val();
			//var birthDay = new Date(datetime);
			var birthDay = new  Date(datetime.replace(/-/g,"/ "));
			//birthDay.setDate(birthDay.getDate()-1);
			var day = birthDay.getDay();
			var temp=new Date();
			if(day=='0'){  //周日
			   ar[6]=birthDay.toLocaleDateString();
			   end=birthDay.getFullYear()+'-'+(birthDay.getMonth()<9?('0'+(birthDay.getMonth()+1)):(birthDay.getMonth()+1))+'-'+(birthDay.getDate()<10?('0'+birthDay.getDate()):birthDay.getDate());
			   temp=birthDay;
			   for(var i=5;i>=0;i--){ 
			   	   temp.setDate(temp.getDate()-1);
				   if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   
				   ar[i]=temp.toLocaleDateString();
			       //alert(ar[i]);
			   }
			}
			else if(day=="1"){
			   ar[0]=birthDay.toLocaleDateString();
			  // alert(ar[0]);
			   temp=birthDay;
			   for(var i=1;i<=6;i++){ 
			   	   temp.setDate(temp.getDate()+1);
				   if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   if(i==6){
				  	end=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());
				  }
				   ar[i]=temp.toLocaleDateString();
			       //alert(ar[i]);
			   }
			}
			else if(day=="2"){
			   temp=birthDay;
			   temp.setDate(temp.getDate()-1);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
				  if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   if(i==6){
				  	end=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());
				  }
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="3"){
			   temp=birthDay;
			   temp.setDate(temp.getDate()-2);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
				  if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   if(i==6){
				  	end=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());
				  }
				  	
			      temp.setDate(temp.getDate()+1);
				 // alert(ar[i].getFullYear());
			  }
			}
			else if(day=="4"){
			    temp=birthDay;
			   temp.setDate(temp.getDate()-3);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
				  if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   if(i==6){
				  	end=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());
				  }
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="5"){
			    temp=birthDay;
			   temp.setDate(temp.getDate()-4);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
				  if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   if(i==6){
				  	end=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());
				  }
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="6"){
			   temp=birthDay;
			   temp.setDate(temp.getDate()-5);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
				  if(i==0){
				  	start=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());					
				  }
				   if(i==6){
				  	end=temp.getFullYear()+'-'+(temp.getMonth()<9?('0'+(temp.getMonth()+1)):(temp.getMonth()+1))+'-'+(temp.getDate()<10?('0'+temp.getDate()):temp.getDate());
				  }
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			//alert(birthDay.toLocaleDateString());
			$('#test').datagrid({
				title:'车辆信息',
//				iconCls:'icon-save',
//				width:900,
//				height:300,
				singleSelect:true, 
				fit: true,
                nowrap: true,
                striped: true,
//				collapsible:true,
				url:'/jlyw/LocaleMissionServlet.do?method=5',
				queryParams:{
					'dateTime':encodeURI(datetime),
					'Lisence':encodeURI(Lisence)
				},
				//sortName: 'userid',
			  //sortOrder: 'desc',
				remoteSort: false,

				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'vehicleid',title:'车辆编号',width:60,sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<a style='text-decoration:underline' onclick=\"lookVehicleMission('" + rowData.vehicleid + "');\"><span style='color: #0033FF'>"+value+"</span></a>";
							
						}
					},
					{field:'limit',title:'限载',width:30,align:'center'}
				]],
				columns:[[
			        {title:ar[0],colspan:2},
					{title:ar[1],colspan:2},
					{title:ar[2],colspan:2},
					{title:ar[3],colspan:2},
					{title:ar[4],colspan:2},
					{title:ar[5],colspan:2},
					{title:ar[6],colspan:2}
				],[
					{field:'onea',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}
                     },
					{field:'onep',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'twoa',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'twop',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'threea',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'threep',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'foura',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'fourp',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'fivea',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'fivep',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sixa',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sixp',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sevena',title:'上午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sevenp',title:'下午',width:65,formatter:function(val,rec){
							if (val.indexOf("出车")>=0){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}}
				]],
				pagination:true,
				rownumbers:true

			});
		}
		
		function savereg(){
			
			
			var AssemblingPlace=$('#AssemblingPlace').val();
			var Kilometers=$('#Kilometers').val();
			var People=$('#People').val();
			var Description=$('#Description').val();
			var DriverName=$('#DriverName').combobox('getValue');
			var testrows=$("#test").datagrid("getSelections");
			var resultrows=$("#locale").datagrid("getSelections");
			$("#vehiclearrange").val(JSON.stringify(testrows));
			$("#missionarrange").val(JSON.stringify(resultrows));
			$('#frm_time_arrange').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=7',
				onSubmit:function(){
					return $('#frm_time_arrange').form('validate');
				},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			if(result.IsOK){
		   				$("#test").datagrid("reload");
						$("#locale").datagrid("reload");
						$.messager.alert("提示","调度成功","info");
						$("#locale").datagrid("clearSelections");
						$('#frm_time_arrange').form('clear');
		   			}else{
		   				$.messager.alert("错误",result.msg,"error");
		   			}
		   			$('#edit').window('close');
		   		 }
			});
		}
		
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="车辆调度" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

<div id="p5" class="easyui-panel" style="width:1000px;height:80px;padding:10px;"
				title="查询" collapsible="false"  closable="false">
				
				    <label id="label_dd" for="dd">选择日期:</label>
					
						<input id="dateTime" name="dateTime" class="easyui-datebox" style="width:152px" required="true" editable="true" />
					&nbsp;
                    <label>车辆牌号：</label><input id="License" name="License" type="text" style="width:100px" />
					
                    &nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="Search()">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  onclick="doLoadLocaleMission()">安排调度</a>
		</div>
<div style="position:relative;width:900px;height:810px;">
         <div style="width:1000px;height:300px;">
		  <table width="100%" height="100%" id="test" iconCls="icon-edit" singleSelect="true"  >
		  </table>
		 </div>
		 
		  <div style="width:1000px;height:500px;">		   
           <table width="100%" height="90%" id="locale" iconCls="icon-edit" singleSelect="true"  >
            </table>
		  </div>
		
</div>
<div id="table6-search-toolbar" style="padding:2px;">
		
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="text-align:right;padding-right:2px">
					<label>委托单位：</label><select name="QueryName" id="QueryName" style="width:152px"></select>&nbsp;
					<label>业务部门：</label><input name="Department" id="Department" style="width:80px"/>&nbsp;
					
					<label>开始时间：</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:80px" />&nbsp;
					<label>结束时间：</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:80px" />&nbsp;
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="查询现场任务" id="SearchLocaleMission" onclick="SearchLocaleMission()">查询现场任务</a>
					</td>
				</tr>
			</table>
			
</div>
<div id="edit" class="easyui-window" title="时间安排" style="padding: 10px;width: 900;height: 500;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
	<form id="frm_time_arrange" method="post">
		<input type="hidden" name="vehiclearrange" id="vehiclearrange" value="" /><input type="hidden" name="missionarrange" id="missionarrange" value="" />
		<div>
		 <table style="width:600px;padding-top:10px"  id="vehicleschedule" name="vehicleschedule" >
			<tr height="30px">
				<td  align="right" style="width：100px">任务确定日期：</td>
				<td align="left"  style="width：160px"><input class="easyui-datebox" id="ExactTime" name="ExactTime" type="text" required="true" editable="true" style="width: 152px"/></td>
				<td align="right" style="width：120px">发车时间：</td>
				<td align="left"  style="width：150px"><input name="BeginDate" id="BeginDate" class="easyui-datetimebox" style="width:152px" /></td>
			</tr>
			 <tr height="30px" colspan="3">
				 <td align="right" >司机：</td>
				<td align="left"  ><select name="DriverName" id="DriverName" value="" style="width:152px"></select></td>
				<td align="right" >集合地点：</td>
				<td align="left" ><input id="AssemblingPlace" name="AssemblingPlace" type="text" /></td>				
			</tr>
			<tr height="30px">			
				<td align="right" >坐车员工：</td>
				<td align="left" colspan="3"><input id="People" name="People" type="text" style="width:256px"/>&nbsp;添加<input id="username" name="username" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
			</tr>
			
			
			<tr height="50px">	
				
				<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">确定</a></td>
				
				<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel()">关闭</a></td>
			</tr>
		</table>
		</div>
	</form>
</div>

<div id="vehiclemission" class="easyui-window" title="查询日期所在周的车辆任务情况" style="width: 1000px;height: 500px;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
	
		<table  id="task-table" iconCls="icon-search" >
		</table>
		
	
</div>
		
</DIV></DIV>
</body>
</html>
