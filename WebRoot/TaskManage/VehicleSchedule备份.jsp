<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��������</title>
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
		
        ar[0] = "����һ";
	    ar[1] = "���ڶ�";
		ar[2] = "������";
		ar[3] = "������";
		ar[4] = "������";
		ar[5] = "������";
		ar[6] = "������";
		
		$(function(){
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
					$('#People').val(temp+";"+$('#username').combobox('getText'));
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
				$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
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
				title:'������Ϣ',
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
					{field:'vehicleid',title:'�������',width:80,sortable:true}
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
					{field:'onea',title:'����',width:80},
					{field:'onep',title:'����',width:80},
					{field:'twoa',title:'����',width:80},
					{field:'twop',title:'����',width:80},
					{field:'threea',title:'����',width:80},
					{field:'threep',title:'����',width:80},
					{field:'foura',title:'����',width:80},
					{field:'fourp',title:'����',width:80},
					{field:'fivea',title:'����',width:80},
					{field:'fivep',title:'����',width:80},
					{field:'sixa',title:'����',width:80},
					{field:'sixp',title:'����',width:80},
					{field:'sevena',title:'����',width:80},
					{field:'sevenp',title:'����',width:80}
				]],
				pagination:true,
				rownumbers:true

			});
			var lastIndex;
			$('#locale').datagrid({
				title:'�ֳ����ҵ��',
				//width:900,
				//height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				fit:true,
				url:'/jlyw/LocaleMissionServlet.do?method=6',
				sortName: 'TentativeDate',
				sortOrder:'dec',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'���',width:40,align:'center'},
					{field:'Name',title:'��λ����',width:180,align:'center',sortable:true},
					{field:'RegionId',title:'����',width:80,align:'center',sortable:true},
					
					{field:'MissionDescEnter',title:'�����Ŀ��̨����',width:200,align:'center'},
					{field:'Status',title:'����״̬',width:90,align:'center',sortable:true,
						formatter:function(value, rowData, rowIndex){
							if(value == 0 || value =="0"){
								return 'δ����';
							}
							if(value == 1 || value =="1"){
								return '�ѷ���';
							}
							if(value == 2 || value =="2"){
								return "�����";
							}
							if(value ==3 || value =="3"){
								return "��ע��";
							}
							if(value == 4 || value =="4"){
								return "δ�˶�";
							}
							if(value == 5 || value =="5"){
								return "�Ѻ˶�";
							}
							return "��ע��";
						}
					},
					{field:'Address',title:'��λ��ַ',width:120,align:'center'},
					{field:'Department',title:'ҵ����',width:80,align:'center'},
					{field:'Staffs',title:'��Ա',width:180,align:'center'},
					{field:'TentativeDate',title:'�ݶ�����',width:80,align:'center',sortable:true},
					{field:'ZipCode',title:'�ʱ�',width:60,align:'center'},
					{field:'ExactTime',title:'ȷ������',width:80,align:'center',sortable:true},
					{field:'CheckDate',title:'�˶�����',width:80,align:'center',sortable:true},
					{field:'SiteManagerName',title:'��⸺����',width:80,align:'center'},
					{field:'Contactor',title:'��ϵ��',width:80,align:'center'},
					{field:'Tel',title:'��λ�绰',width:100,align:'center'},
					{field:'ContactorTel',title:'��ϵ�˵绰',width:100,align:'center'},
					{field:'Remark',title:'��ע',width:180,align:'center'}
									
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 3){	//��ע��
						return 'color:#000000'
					}else if(rowData.Status == 1){	//�ѷ���
						return 'color:#FF0033';	
					}else if(rowData.Status == 2){	//���깤(�����ѽ��ˡ��ѽ���)
						return 'color:#000000';	
					}else if(rowData.Status == 5){	//�������Ѻ˶�
						return 'color:#339900';	
					}
					else if(rowData.Status==4&&rowData.Tag == 1){	//δ�˶��������ѵ��ݶ����ڿ���δ����
						return 'color:#FFFF33';
					}else if(rowData.Status==4&&rowData.Tag == 2){	//δ�˶�������δ���ݶ�����
						return 'color:#0000FF';
					}else{
						return 'color:#000000';
					}
				},
				toolbar:"#table6-search-toolbar"
				/*toolbar:[{
					text:'�鿴��ѡ',
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
			$('#result').datagrid({
				title:'��ѡ���',
				//width:900,
				//height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				fit:true,
				//url:'localemission.json',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					//{field:'Id',title:'���',width:40,align:'center'},
					{field:'Name',title:'��λ����',width:180,align:'center'},
					{field:'RegionId',title:'����',width:80,align:'center'},
					
					{field:'MissionDesc',title:'�����Ŀ��̨����',width:200,align:'center'},
					{field:'Address',title:'��λ��ַ',width:120,align:'center'},
					{field:'Department',title:'ҵ����',width:80,align:'center'},
					{field:'Staffs',title:'��Ա',width:180,align:'center'},
					{field:'TentativeDate',title:'�ݶ�����',width:80,align:'center'},
					{field:'ZipCode',title:'�ʱ�',width:60,align:'center'},
					{field:'ExactTime',title:'ȷ������',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(rowData.Status == 0 || rowData.Status =="0"){
								return "";
							}
							return value;
						}
					},
					{field:'SiteManagerId',title:'��⸺����',width:80,align:'center'},
					{field:'Contactor',title:'��ϵ��',width:80,align:'center'},
					{field:'Tel',title:'��λ�绰',width:100,align:'center'},
					{field:'ContactorTel',title:'��ϵ�˵绰',width:100,align:'center'},
					{field:'Status',title:'����״̬',width:90,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value == 0 || value =="0"){
								return '<span style="color:red;">'+'δ����'+'</span>';
							}
							if(value == 1 || value =="1"){
								return "�ѷ���";
							}
							if(value == 2 || value =="2"){
								return "�����";
							}
							return "��ע��";
						}
					}
					
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'ȷ������',
					iconCls:'icon-ok',
					handler:function(){
						var result_select = $('#result').datagrid('getSelected');
						var vehicle_select = $('#test').datagrid('getSelected');
						
						if(result_select&&vehicle_select){
						$('#DriverName').combobox('setValue',vehicle_select.driverName);
						$('#edit').window('open');
						$('#frm_time_arrange').show();
						
						//$('#Id').val(select.Id);
						//$('#Name').val(select.Name);
						//$('#RegionId').val(select.RegionId);
						//$('#ZipCode').val(select.ZipCode);
						//$('#Address').val(select.Address);
						//$('#MissionDesc').val(select.MissionDesc);
						//$('#Tel').val(select.Tel);
						//$('#Department').val(select.Department);
						//$('#Staffs').val(select.Staffs);
						//$('#TentativeDate').val(select.TentativeDate);
						//$('#Contactortel').val(select.ContactorTel);
						//$('#ExactTime').val(select.ExactTime);
						//$('#Status').val(select.Status);
						//$('#SiteManager').val(select.SiteManagerId);
				        //$('#Contactor').val(select.Contactor);
						//id = select.id; 
					}else{
						$.messager.alert('warning','���ڳ�����������ж�����ѡ��һ������','warning');
						}
					}
				}]
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
			
			$("#dateTime").datebox({
				formatter: function(date){ return date.getFullYear()+'-'+(date.getMonth()<9?('0'+(date.getMonth()+1)):(date.getMonth()+1))+'-'+(date.getDate()<10?('0'+date.getDate()):date.getDate()); }
			});
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
		
		function doLoadLocaleMission(){
			var rows = $('#locale').datagrid('getSelections');
			
			var length = rows.length;
			for(var i=length-1; i>=0; i--){
				var index = $('#result').datagrid('getRowIndex', rows[i].Id);
				if(index == null || index < 0){
					$('#result').datagrid('appendRow',rows[i]);
				}
					
			}
			$('#locale').datagrid('clearSelections');
			
		}
		
		function SearchLocaleMission()
		{
			var History_BeginDate=$('#History_BeginDate').datebox('getValue');
			var History_EndDate=$('#History_EndDate').datebox('getValue');
			$('#locale').datagrid('options').url='/jlyw/LocaleMissionServlet.do?method=6';
			$('#locale').datagrid('options').queryParams={'MissionRegion':encodeURI($("#MissionRegion").combobox('getText')),'History_BeginDate':encodeURI(History_BeginDate),'History_EndDate':encodeURI(History_EndDate)};
			$('#locale').datagrid('reload');
		}
		
		function cancel(){
			$('#edit').window(close);
		}
		
		function Search(){
			
			var datetime=$('#dateTime').datebox('getValue');
			if(datetime == ""){
				$.messager.alert("��ʾ","����ѡ������","info");	
				return false;
			}
			var Lisence=$('#License').val();
			//var birthDay = new Date(datetime);
			var birthDay = new  Date(datetime.replace(/-/g,"/ "));
			//birthDay.setDate(birthDay.getDate()-1);
			var day = birthDay.getDay();
			var temp=new Date();
			if(day=='0'){  
			   ar[6]=birthDay.toLocaleDateString();
			   //alert(ar[6]);
			   temp=birthDay;
			   for(var i=5;i>=0;i--){ 
			   	   temp.setDate(temp.getDate()-1);
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
				   ar[i]=temp.toLocaleDateString();
			       //alert(ar[i]);
			   }
			}
			else if(day=="2"){
			   temp=birthDay;
			   temp.setDate(temp.getDate()-1);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="3"){
			   temp=birthDay;
			   temp.setDate(temp.getDate()-2);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="4"){
			    temp=birthDay;
			   temp.setDate(temp.getDate()-3);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="5"){
			    temp=birthDay;
			   temp.setDate(temp.getDate()-4);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			else if(day=="6"){
			   temp=birthDay;
			   temp.setDate(temp.getDate()-5);
			   for(i=0;i<=6;i++){
			      ar[i]=temp.toLocaleDateString();
			      temp.setDate(temp.getDate()+1);
				  //alert(ar[i]);
			  }
			}
			//alert(birthDay.toLocaleDateString());
			$('#test').datagrid({
				title:'������Ϣ',
//				iconCls:'icon-save',
//				width:900,
//				height:300,
				singleSelect:true, 
				fit: true,
                nowrap: false,
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
					{field:'vehicleid',title:'�������',width:80,sortable:true}
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
					{field:'onea',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}
                     },
					{field:'onep',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'twoa',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'twop',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'threea',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'threep',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'foura',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'fourp',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'fivea',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'fivep',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sixa',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sixp',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sevena',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
								return '<span style="color:red;">'+val+'</span>';
							} else {
								return val;
							}
						}},
					{field:'sevenp',title:'����',width:80,formatter:function(val,rec){
							if (val =="����"){
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
			var BeginDate=$('#BeginDate').datetimebox('getValue');
			var EndDate=$('#EndDate').datetimebox('getValue');
			var DepartureTime=$('#DepartureTime').datetimebox('getValue');
			var AssemblingPlace=$('#AssemblingPlace').val();
			var Kilometers=$('#Kilometers').val();
			var People=$('#People').val();
			var Description=$('#Description').val();
			var DriverName=$('#DriverName').combobox('getValue');
			var testrows=$("#test").datagrid("getSelections");
			var resultrows=$("#result").datagrid("getSelections");
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
						$.messager.alert("��ʾ","���ȳɹ�","info");
		   			}else{
		   				$.messager.alert("����",result.msg,"error");
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
			<jsp:param name="TitleName" value="��������" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

<div id="p5" class="easyui-panel" style="width:900px;height:80px;padding:10px;"
				title="��ѯ" collapsible="false"  closable="false">
				
				    <label id="label_dd" for="dd">ѡ������:</label>
					
						<input id="dateTime" name="dateTime" class="easyui-datebox" style="width:152px" required="true" editable=false />
					&nbsp;
                    <label>�����ƺţ�</label><input id="License" name="License" type="text" style="width:100px" />
					
                    &nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="Search()">��ѯ</a>
		</div>
<div style="position:relative;width:900px;height:910px;">
         <div style="width:900px;height:350px;">
		  <table width="100%" height="100%" id="test" iconCls="icon-edit" singleSelect="true"  >
		  </table>
		 </div>
		 <div style="width:900px;height:200px;">
		  <table width="100%" height="100%" id="result" iconCls="icon-edit" singleSelect="true"  >
		  </table>
		 </div>
		  <div style="width:900px;height:350px;">		   
           <table width="100%" height="90%" id="locale" iconCls="icon-edit" singleSelect="true"  >
            </table>
		  </div>
		
</div>
<div id="table6-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="doLoadLocaleMission()">���ŵ���</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>���������</label><input type="text" id="MissionRegion" value="" style="width:120px" />&nbsp;<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:100px" />&nbsp;����ʱ�䣺</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ�ֳ�����" id="SearchLocaleMission" onclick="SearchLocaleMission()">��ѯ�ֳ�����</a>
					</td>
				</tr>
			</table>
</div>
<div id="edit" class="easyui-window" title="ʱ�䰲��" style="padding: 10px;width: 900;height: 500;" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
	<form id="frm_time_arrange" method="post">
		<input type="hidden" name="vehiclearrange" id="vehiclearrange" value="" /><input type="hidden" name="missionarrange" id="missionarrange" value="" />
		<div>
		 <table style="width:600px; height:400px; padding-top:10px" class="easyui-panel" id="vehicleschedule" name="vehicleschedule" title="��������ʱ�䰲��">
			<tr height="30px">
				<td  align="right" style="width��20%">������ʼʱ�䣺</td>
				<td align="left"  style="width��25%"><input class="easyui-datetimebox" id="BeginDate" name="BeginDate" type="text" required="true" editable="false" style="width: 152px"/></td>
				<td align="right" style="width��20%">���Ƚ���ʱ�䣺</td>
				<td align="left"  style="width��25%"><input class="easyui-datetimebox" id="EndDate" name="EndDate" type="text" required="true" editable="false"style="width: 152px"/></td>
				
			</tr>
			<tr height="30px">
				<td  align="right" style="width��20%">����ʱ�䣺</td>
				<td align="left"  style="width��25%"><input class="easyui-datetimebox" id="DepartureTime" name="DepartureTime" type="text" required="true" editable="false" style="width: 152px"/></td>
				<td align="right" style="width��20%">˾����</td>
				<td align="left"  style="width��25%"><select name="DriverName" id="DriverName" value="" style="width:152px"></select></td>
			</tr>
			<tr height="30px">
				
				<td align="right" style="width��20%">����Ա����</td>
				<td align="left" colspan="3" ><input id="People" name="People" type="text" style="width:256px"/>&nbsp;���<input id="username" name="username" class="easyui-combobox"  url="" style="width:152px;" valueField="name" textField="name" panelHeight="150px" /></td>
			</tr>
			 <tr height="30px">
				<td align="right" style="width��20%">���ϵص㣺</td>
				<td align="left"  style="width��25%"><input id="AssemblingPlace" name="AssemblingPlace" type="text" /></td>
				<td align="right" style="width��20%">��ʻ��̣�</td>
				<td align="left"  style="width��25%"><input id="Kilometers" class="easyui-numberbox" name="Kilometers" type="text" />����</td>
			</tr>
			<tr height="30px">
				<td align="right" style="width��20%">����������</td>
				<td align="left"  colspan="3"><textarea style="height:60px;" id="Description" name="Description" cols="55" rows="2"></textarea> </td>
			</tr>
			<tr height="50px">	
				<td></td>
				<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">ȷ��</a></td>
				<td></td>
				<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel()">�ر�</a></td>
			</tr>
		</table>
		</div>
	</form>
</div>
		
</DIV></DIV>
</body>
</html>
