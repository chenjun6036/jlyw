<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,com.jlyw.hibernate.SysUser" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ֳ����ҵ���ɾ��</title>
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
				
				$("#SpeciesType").val(record.SpeciesType);		//���߷�������
				$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//���߷���ID�������Ǳ�׼����ID��
				$("#StandName").val(record.StandName);		//���߱�׼����
				$("#ApplianceName").combobox('clear');
				$("#Model").combobox('clear');
				$("#Range").combobox('clear');
				$("#Accuracy").combobox('clear');
				$("#Manufacturer").combobox('clear');
				$("#WorkStaff").combobox('clear');
				if(record.SpeciesType == 0 || record.SpeciesType == '0' || record.SpeciesType == 2 || record.SpeciesType == '2'){	//��׼����Or��������
					if(record.PopName == null || record.PopName.length == 0){
						$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);
					}else{
						$("#ApplianceName").combobox('setValue',record.PopName);
					}
					$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//�ͺŹ��
					$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//������Χ
					$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//���ȵȼ�
					$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//���쳧��
				}else if(record.SpeciesType == 1 || record.SpeciesType == '1'){	//��������
					$("#ApplianceName").combobox('loadData',[]);
					$("#Model").combobox('loadData',[]);	//�ͺŹ��
					$("#Range").combobox('loadData',[]);	//������Χ
					$("#Accuracy").combobox('loadData',[]);	//���ȵȼ�
					$("#Manufacturer").combobox('loadData',[]);	//���쳧��
				}
				$("#WorkStaff").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId);	//������Ա
				
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
				$("#SpeciesType").val('');		//���߷�������
				$("#ApplianceSpeciesId").val('');	//���߷���ID�������Ǳ�׼����ID��
				$("#StandName").val('');		//���߱�׼����
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
	
		$('#Licence').combobox({//���ƺŵ�ģ����ѯ
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
			
			$("#CustomerName").combobox({//ί�е�λ
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
			$("#QuotationNumber").combobox({//ί�е�λ
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
           
		    $("#Namecheck").combobox({  //�˶��е�ί�е�λ
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
				title:'�ֳ�������Ϣ',
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
					{field:'CreateDate',title:'������Ϣ',width:100,align:'center',sortable:true},
					//{field:'CreatorName',title:'������',width:60,align:'center',sortable:true},
					
					{field:'Code',title:'ί�����',width:80,align:'center',sortable:true},
					{field:'Status',title:'����״̬',width:60,align:'center',sortable:true,
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
					{field:'Name',title:'��λ����',width:160,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<a style='text-decoration:underline' onclick=\"editMission('" + rowData.Id + "');\"><span style='color: #0033FF'>"+value+"</span></a>";
							
						}
					},
					{field:'Address',title:'��λ��ַ',width:120,align:'center'},
					{field:'VehicleLisences',title:'�˳���Ϣ',width:120,align:'center'},
					{field:'Department',title:'ҵ����',width:80,align:'center'},
					{field:'MissionDesc',title:'������Ϣ',width:120,align:'center'},
					{field:'Staffs',title:'��Ա',width:180,align:'center'},
					
					{field:'SiteManagerName',title:'��⸺����',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #FF00CC'>"+value+"</span>";
							
						}
					},
										
					{field:'TentativeDate',title:'�ݶ�����',width:80,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #33FF00'>"+value+"</span>";
							
						}
					},										
					{field:'CheckDate',title:'�˶�����',width:80,align:'center',sortable:true,
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #6633FF'>"+value+"</span>";
							
						}
					},
					{field:'ExactTime',title:'ȷ������',width:80,align:'center',sortable:true, 
						formatter:function(value, rowData, rowIndex){
							
								return "<span style='color: #330033'>"+value+"</span>";
							
						}
					
					},
					
					{field:'Contactor',title:'��ϵ��',width:80,align:'center'},
					{field:'Tel',title:'��λ�绰',width:100,align:'center'},
					{field:'ContactorTel',title:'��ϵ�˵绰',width:100,align:'center'},
					{field:'HeadNameName',title:'̨ͷ����',width:120,align:'center'},
					{field:'Region',title:'���ڵ���',width:60,align:'center',sortable:true},
					{field:'ZipCode',title:'�ʱ�',width:60,align:'center'},
					
					{field:'Remark',title:'��ע',width:180,align:'center'},
					{field:'Feedback',title:'����',width:180,align:'center'}
					
				]],
				pagination:true,
				rownumbers:true,
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 3){	//��ע��
						return 'color:#FF00FF'
					}else if(rowData.Status == 1){	//�ѷ���
						return 'color:#FF0033';	
					}else if(rowData.Status == 2){	//���깤�������޸���
						return 'color:#000000';	
					}else if(rowData.Status == 5){	//�������Ѻ˶�
						return 'color:#339900';	
					}
					else if(rowData.Status==4&&rowData.Tag == 1){	//δ�˶��������ѵ��ݶ����ڿ���δ����
						return 'color:#0000FF';
					}else if(rowData.Status==4&&rowData.Tag == 2){	//δ�˶�������δ���ݶ�����
						return 'color:#6600CC';
					}else{
						return 'color:#0000FF';
					}
				},
				toolbar:[{
					text:'����',
					iconCls:'icon-add',
					handler:function(){
						
						$('#add').append($("#table-Appliance"));
						$("#table-Appliance").show();	
						var nowDate = new Date();
						$("#TentativeDateAdd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
						$('#add').window('open');					
						
					}
				},'-',{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var rowSelected = $('#table2').datagrid('getSelected');
						if(rowSelected != null){
							
						   var titlecode ="�ֳ�ί�����Ϊ��"+rowSelected.Code+"�����ֳ������޸�";
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
							$.messager.alert('��ʾ','��ѡ��һ������','info');
						}
					
					}
				},'-',{
					text:'�˶�',
					iconCls:'icon-ok',
					handler:function(){
						var rowSelected = $('#table2').datagrid('getSelected');
						if(rowSelected != null){
							if(rowSelected.Status==2){
									$.messager.alert('��ʾ','������״̬�����깤�������ܽ��к˶���','info');
									return false;
							}
							if(rowSelected.Status!=4&&rowSelected.Status!=5){
									$.messager.alert('��ʾ','������״̬���ǡ�δ�˶������ߡ��Ѻ˶��������ܽ��к˶���','info');
									return false;
							}
						   var titlecode ="�ֳ�ί�����Ϊ��"+rowSelected.Code+"�����ֳ�����˶�";
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
							$.messager.alert('��ʾ','��ѡ��һ������','info');
						}
					
					}
				},'-',{
						text:'ɾ��',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								if(selected.status==1||selected.status==2){
									$.messager.alert('��ʾ','������״̬�ǡ��ѷ��䡱���ߡ�����ɡ�������ɾ����','info');
									return false;
								}
								var result = confirm("��ȷ��Ҫɾ����");
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
											$.messager.alert('�ύ��','ɾ���ɹ�','info');
										}else{
											$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
										}
									}
								});
							}else{
								$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','info');
							}
						}
				},'-',{
						text:'����',
						iconCls:'icon-undo',
						handler:function(){
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								
								$('#MissionId').val(selected.Id);
								$('#Feedback').val(selected.Feedback);
								$('#undo').window('open');
								
							}else{
								$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','info');
							}
						}
				},'-',{
						text:'������ͨ��ʽ',
						iconCls:'icon-back',
						handler:function(){
						
							var selected = $('#table2').datagrid('getSelected');
							if(selected != null){
								if(selected.Status==2||selected.Status==3){
									$.messager.alert('��ʾ','���������깤�����ߡ���ע���������ܽ��д˲�����','info');
									return false;
								}
								if(selected.Status==1&&selected.VehicleLisences!=null){
									$.messager.alert('��ʾ','����������ˡ��������䡱������ɾ���������䣬�ٽ��д˲�����','info');
									return false;
								}
								$('#CustomerLC_window').window('open');
							}else{
								$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','info');
							}
							
							
							
						}
				},'-',{
					text:'��ӡ�ֳ����������',
					iconCls:'icon-print',
					handler:function(){
							var row = $('#table2').datagrid('getSelected');
							
							if(row)
							{
								    //if(row.SiteManagerName!=null&&row.SiteManagerName.length>0&&row.ExactTime!=null&&row.ExactTime.length>0){								
										$('#localeMissionId').val(row.Id);
										$('#MissionPrint-form').submit();
									//}else{
									//	$.messager.alert('��ʾ','�ֳ���⸺����Ϊ�ջ���ȷ��ʱ��Ϊ��','warning');
									//}												
							}else{
								//$('#MissionPrint-blank-form').submit()
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				},'-',{
					text:'�����ֳ�ί�е�',
					iconCls:'icon-add',
					handler:function(){
							generateSheet();
						}
				},'-',{
					text:'��ӡ���ֳ����������',
					iconCls:'icon-print',
					handler:function(){
							var row = $('#table2').datagrid('getSelected');
							
							if(row)
							{
								    //if(row.SiteManagerName!=null&&row.SiteManagerName.length>0&&row.ExactTime!=null&&row.ExactTime.length>0){								
										$('#localeMissionId1').val(row.Id);
										$('#MissionPrint-blank-form').submit()
									//}else{
									//	$.messager.alert('��ʾ','�ֳ���⸺����Ϊ�ջ���ȷ��ʱ��Ϊ��','warning');
									//}												
							}else{
								//$('#MissionPrint-blank-form').submit()
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				}]
			});
			var lastIndex;
			var products3 = [
				{id:1,name:'�춨'},
				{id:2,name:'У׼'},
				{id:3,name:'���'},
				{id:4,name:'����'}
			];
			$('#table5').datagrid({
				title:'���μ��������',
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
					
					{field:'ApplianceSpeciesName',title:'������Ȩ��',width:120,align:'center'},
					{field:'ApplianceName',title:'��������',width:180,editor:'text',align:'center'},
				
					{field:'Quantity',title:'̨/����',width:70,editor:'numberbox',align:'center'},
					{field:'WorkStaff',title:'�ɶ���',width:80,align:'center',editor:'text'},
					{field:'AssistStaff',title:'�����',width:120,editor:'text',align:'center'},
					
					{field:'ApplianceCode',title:'�������',editor:'text',width:80,align:'center'},
					{field:'AppManageCode',title:'������',editor:'text',width:80,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,editor:'text',align:'center'},
					{field:'Range',title:'������Χ',width:80,editor:'text',align:'center'},
					{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:80,editor:'text',align:'center'},
					{field:'Manufacturer',title:'���쳧��',width:80,editor:'text',align:'center'},
					{field:'ReportType',title:'������ʽ',width:80,editor:'text',align:'center',editor:{
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
							return "�춨";
						}
						if(value == 2 || value == '2')
						{
							rowData['ReportType']=2;
							return "У׼";
						}
						if(value == 3 || value == '3')
						{
							rowData['ReportType']=3;
							return "���";
						}
						if(value == 4 || value == '4')
						{
							rowData['ReportType']=4;
							return "����";
						}
					}},
					{field:'TestFee',title:'���Ѻϼ�',width:80,align:'center'},
					{field:'RepairFee',title:'�����',width:80,align:'center'},
					{field:'MaterialFee',title:'���Ϸ�',width:80,align:'center'}				
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'ɾ��',
					iconCls:'icon-remove',
					handler:function(){
						var result = confirm("��ȷ��Ҫ�Ƴ���Щ������");
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
//					text:'��ɱ༭',
//					iconCls:'icon-save',
//					handler:function(){
//						$('#table5').datagrid('acceptChanges');
//						
//					}
//				}
				//,'-',{
//					text:'�������',
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
					var rowData = null;	//���ҽ����ϵ�һ����ѡ����
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

					var appSpeComboData = {   //������Ȩ����
						SpeciesType:rowData.SpeciesType,
						ApplianceSpeciesId:rowData.ApplianceSpeciesId,
						Name:rowData.ApplianceSpeciesName,
						PopName:rowData.ApplianceName
					};	
					$("#ApplianceSpeciesName").combobox('loadData',[appSpeComboData]);
					$("#ApplianceSpeciesName").combobox('setValue', rowData.ApplianceSpeciesName);
					$("#SpeciesType").val(rowData.SpeciesType);	//���ص�ID
					$("#ApplianceSpeciesId").val(rowData.ApplianceSpeciesId);	//���ص�ID
					$("#ApplianceName").combobox('setValue',rowData.ApplianceName);	//��������
					
					$("#StandName").val(rowData.ApplianceSpeciesName);		//���߱�׼����
										
					$("#RepairFee").val(rowData.RepairFee);	//
					$("#MaterialFee").val(rowData.MaterialFee);	//
					$("#TestFee").val(rowData.TestFee);	//
					
					$("#ApplianceCode").val(rowData.ApplianceCode);	//�������
					$('#AppManageCode').val(rowData.AppManageCode);	//������
					$('#Model').combobox('setValue', rowData.Model);	//�ͺŹ��
					$('#Range').combobox('setValue', rowData.Range);	//������Χ
					$('#Accuracy').combobox('setValue', rowData.Accuracy);	//���ȵȼ�			
					$('#Manufacturer').combobox('setValue', rowData.Manufacturer);	//���쳧
					$('#Quantity').val(rowData.Quantity);	//��������
							
					$("#ReportType").val(rowData.ReportType);	//������ʽ
					$("#WorkStaff").combobox('setValue', rowData.WorkStaff);	//�ɶ���
					$('AssistStaff').val(rowData.AssistStaff)	;		
					if(rowData.SpeciesType == 0 || rowData.SpeciesType == '0' || rowData.SpeciesType == 2 || rowData.SpeciesType == '2'){	//��׼����Or��������
						if(rowData.ApplianceName == null || rowData.ApplianceName.length == 0){
							$("#ApplianceName").combobox('reload','/jlyw/ApplianceServlet.do?method=1&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);
						}else{
							$("#ApplianceName").combobox('setValue',rowData.ApplianceName);
						}
						$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//�ͺŹ��
						$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//������Χ
						$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//���ȵȼ�
						$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//���쳧��
					}else if(rowData.SpeciesType == 1 || rowData.SpeciesType == '1'){	//��������
						$("#ApplianceName").combobox('loadData',[]);
						$("#Model").combobox('loadData',[]);	//�ͺŹ��
						$("#Range").combobox('loadData',[]);	//������Χ
						$("#Accuracy").combobox('loadData',[]);	//���ȵȼ�
						$("#Manufacturer").combobox('loadData',[]);	//���쳧��
					}
					$("#WorkStaff").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+rowData.SpeciesType+'&ApplianceSpeciesId='+rowData.ApplianceSpeciesId);	//������Ա
					
				
				},
				onSelect:function(rowIndex,rowData){
					var row = $("#table5").datagrid("getSelected");
					
					//$('#form-Appliance').form('load',rowData);
					
				}
		
			});
			$('#table_QuoItem').datagrid({
				//width:800,
				//height:300,
				//title:"���۵���Ŀ",
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
					//{field:'Id',title:'���',width:40,align:'center'},
					{field:'StandardNameId',title:'�ܼ����߱�׼����Id',width:120,align:'center'},
					{field:'StandardName',title:'�ܼ����߱�׼����',width:120,align:'center',sortable:true},
					{field:'CertificateName',title:'�ܼ�����֤������',width:120,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Quantity',title:'̨����',width:80,align:'center',editor:'text'},
					{field:'AppFactoryCode',title:'�������',width:80,align:'center'},
					{field:'AppManageCode',title:'������',width:80,align:'center'},
					{field:'Manufacturer',title:'���쳧',width:80,align:'center'},
					{field:'CertType',title:'֤������',width:80,align:'center',sortable:true,
						formatter:function(value,rowData,rowIndex){
							if(value=='1'){
								rowData['CertType']="1";
								return "�춨";
							}
							else if(value == '4'){
								rowData['CertType']="4";
								return "����";
							}
							else if(value == '2'){
								rowData['CertType']="2";
								return "У׼";
							}
							else{
								rowData['CertType']="3";
								return "���";
							}
						}
					},
					{field:'SiteTest',title:'�ֳ����',width:80,align:'center',sortable:true,
						formatter:function(value,rowData,rowIndex){
							if(value=='0'){
								rowData['SiteTest'] ="0";
								return "��";
							}
							else{
								rowData['SiteTest'] ="1";
								return "��";
							}
						}
					},
					{field:'MinCost',title:'��������',width:80,align:'center',
						formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
						
					}},
					{field:'MaxCost',title:'�������',width:80,align:'center',
						formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
						
					}},
					{field:'Remark',title:'��ע',width:80,align:'center'}
				]],
				rownumbers:false,
				pagination:false,
				toolbar:"#quo-search-toolbar"
			});
			$('#table6').datagrid({//��ʷ��¼
				fit:true,
				//title:'ί�е�λ��ʷ�������߼�¼',
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
					
					{field:'ApplianceSpeciesName',title:'������Ȩ��',width:120,align:'center'},
					{field:'ApplianceName',title:'��������',width:180,align:'center'},
				
					{field:'Quantity',title:'̨/����',width:70,align:'center'},
					{field:'WorkStaff',title:'�ɶ���',width:80,align:'center'},
					{field:'AssistStaff',title:'�����',width:120,align:'center'},
					
					{field:'ApplianceCode',title:'�������',width:80,align:'center'},
					{field:'AppManageCode',title:'������',width:80,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:80,align:'center'},
					{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
					{field:'ReportType',title:'������ʽ',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 1 || value == '1')
							{
								rowData['ReportType']=1;
								return "�춨";
							}
							if(value == 2 || value == '2')
							{
								rowData['ReportType']=2;
								return "У׼";
							}
							if(value == 3 || value == '3')
							{
								rowData['ReportType']=3;
								return "���";
							}
							if(value == 4 || value == '4')
							{
								rowData['ReportType']=4;
								return "����";
							}
					}},
					{field:'TestFee',title:'����',width:80,align:'center'}				
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
		
		function UpdateRecord()	//����һ�����������
		{
			var rows = $('#table5').datagrid('getSelections');
			if(rows.length == 0){
				$.messager.alert("��ʾ","���ڡ����μ�������ߡ���ѡ��һ����Ҫ���µ����ߣ�","info");
				return false;
			}
			if(rows.length > 1){
				$.messager.alert("��ʾ","ֻ���ڡ����μ�������ߡ���ѡ��һ����Ҫ���µ����ߣ�","info");
				return false;
			}
			
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("��ʾ","��ѡ��һ����Ч�ġ�������Ȩ������","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("��ʾ","�����롮������������","info");
				return false;
			}
		    var testFee="";
			var totalFee="";
			if($("#TestFee").val()==null||$("#TestFee").val().length==0){
				if($('#SpeciesType').val()==0||$('#SpeciesType').val()=='0'){
					$.ajax({   //�����ܼ����ߣ��ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ�֮��Ĳ��Ҽ��������Сֵ��
						type:'POST',
						url:'/jlyw/TargetApplianceServlet.do?method=13',
						data:{'Application':encodeURI($('#StandName').val()),'Model':encodeURI($('#Model').combobox('getText')),'Range':encodeURI($('#Range').combobox('getText')),'Accuracy':encodeURI($('#Accuracy').combobox('getText'))},
						dataType:"json",
						async: false,
						success:function(data, textStatus){
							if(data.IsOK){//����ȡ�óɹ�	
								//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
								if(data.MinFee==data.MaxFee){						
									testFee=data.MinFee;
									//console.info("TestFee:"+testFee);
								}
							}else{//����ȡ��ʧ��
								$.messager.alert('����',data.msg,'error');
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
			
			var titlecode ="�ֳ�ί�����Ϊ��"+rowdata.Code+"�����ֳ������޸�";
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
		
		function savereg(){//�޸�������Ϣ
		    var rowdata=$("#table2").datagrid("getSelected");
			if(rowdata.Status==2){
					$.messager.alert('��ʾ','������״̬�����깤�������ܽ����޸ģ�','info');
					return false;
			}
			$('#frm_update_localmission').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=3',
				onSubmit:function(){
					$('#table5').datagrid('acceptChanges');					
					var rows = $("#table5").datagrid("getRows");	
					if(rows.length == 0){
						$.messager.alert('��ʾ',"��ѡ���������ߣ�",'info');
						return false;
					}
					for(var i=0; i<rows.length; i++){
						if(rows[i].Quantity == null || rows[i].Quantity == 0){
							$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/����������0����������",'info');
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
						 $.messager.alert('��ʾ��','�޸ĳɹ�','info');
		   			}else{
						$.messager.alert('�޸�ʧ�ܣ�',result.msg,'error');
		   			}
					
		   		 }
			});
			
		}
		function saveundo(){//����
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
		   				 $.messager.alert('��ʾ��','�����ɹ�','info');
						
		   			}else{
						$.messager.alert('�޸�ʧ�ܣ�',result.msg,'error');
		   			}
					$('#table2').datagrid('reload');
		   		 }
			});
			
		}
		function cancel1(){
			$('#add').window('close');
		}
		
		function savereg1(){//���������
			$('#table5').datagrid('acceptChanges');					
			var rows = $("#table5").datagrid("getRows");	
			$('#frm_add_customer').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=1',
				onSubmit:function(){
					//alert('onSubmit');
					if($('#RegionIdAdd').val() == ''){
						$.messager.alert('��ʾ','��ѡ��һ����Ч�ĵ�����','info');
						return false;
					}
					
				
					if(rows.length == 0){
						$.messager.alert('��ʾ',"��ѡ���������ߣ�",'info');
						return false;
					}
					for(var i=0; i<rows.length; i++){
						if(rows[i].Quantity == null || rows[i].Quantity == 0){
							$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/����������0����������",'info');
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
						 $.messager.alert('��ʾ��','��ӳɹ�','info');
					}
					else{
						 $.messager.alert('���ʧ�ܣ�',result.msg,'error');
					}
		   		 }
			});
		}
		function cancelcheck(){
			$('#check').window('close');
		}
		
		function saveregcheck(){//�˶�������
			$('#table5').datagrid('acceptChanges');	
			 var rowdata=$("#table2").datagrid("getSelected");
			if(rowdata.Status==2){
					$.messager.alert('��ʾ','������״̬�����깤�������ܽ����޸ģ�','info');
					return false;
			}		
			
			if($("#CheckDatecheck").datebox('getValue')==null||$("#CheckDatecheck").datebox('getValue').length==0){
				$.messager.alert('��ʾ','�˶�������ѡ��˶����ڣ�','info');
				return false;
			}
			if($("#SiteManagerNamecheck").combobox('getText')==null||$("#SiteManagerNamecheck").combobox('getText').length==0){
				$.messager.alert('��ʾ','�˶�������ѡ���ֳ������ˣ�','info');
				return false;
			}
			var rows = $("#table5").datagrid("getRows");	
			$('#frm_check_localmission').form('submit',{
				url: '/jlyw/LocaleMissionServlet.do?method=16',
				onSubmit:function(){
					//alert('onSubmit');
					if($('#RegionIdcheck').val() == ''){
						$.messager.alert('��ʾ','��ѡ��һ����Ч�ĵ�����','info');
						return false;
					}
					
				
					if(rows.length == 0){
						$.messager.alert('��ʾ',"��ѡ���������ߣ�",'info');
						return false;
					}
					for(var i=0; i<rows.length; i++){
						if(rows[i].Quantity == null || rows[i].Quantity == 0){
							$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/����������0����������",'info');
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
						 $.messager.alert('��ʾ��','�˶��ɹ�','info');
					}
					else{
						 $.messager.alert('�˶�ʧ�ܣ�',result.msg,'error');
					}
		   		 }
			});
		}
		function AddRecord(){//�������
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("��ʾ","��ѡ��һ����Ч�ġ�������Ȩ������","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("��ʾ","������'��������'��","info");
				return false;
			}
			
			if($("#ReportType").val()==''||$("#ReportType").val().length==0){
				$.messager.alert("��ʾ","��ѡ�񱨸���ʽ��","info");
				return false;
			}
			
		    var testFee="";
			//alert("121");
			var index = $('#table5').datagrid('getRows').length;	//�����һ��������¼
			 var testfeeTotal;
			
			//var index1 = 1;	//�����һ��������¼
			if($("#TestFee").val()==null||$("#TestFee").val().length==0){
				if($('#SpeciesType').val()==0||$('#SpeciesType').val()=='0'){
					$.ajax({   //�����ܼ����ߣ��ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ�֮��Ĳ��Ҽ��������Сֵ��
						type:'POST',
						url:'/jlyw/TargetApplianceServlet.do?method=13',
						data:{'Application':encodeURI($('#StandName').val()),'Model':encodeURI($('#Model').combobox('getText')),'Range':encodeURI($('#Range').combobox('getText')),'Accuracy':encodeURI($('#Accuracy').combobox('getText'))},
						dataType:"json",
						async: false,
						success:function(data, textStatus){
							if(data.IsOK){//����ȡ�óɹ�	
								//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
								if(data.MinFee==data.MaxFee){						
									testFee=data.MinFee;
									//console.info("TestFee:"+testFee);
								}
							}else{//����ȡ��ʧ��
								$.messager.alert('����',data.msg,'error');
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
			$('#inputQuo_window').window('open');//�򿪱��۵���ѯ����	
			if($('#NameAdd').combobox('getText').length>0){
				$("#CustomerName").combobox('setValue',$('#NameAdd').combobox('getText'));
				$("#QuotationNumber").combobox('reload','/jlyw/QuotationServlet.do?method=11&QueryName='+encodeURI($('#NameAdd').combobox('getText')));
			}
			
				
		}
		function inputQuoItem(){//������ѡ��Ŀ
			var rows = $("#table_QuoItem").datagrid("getSelections");				
			$("#QuoItemRows").val(JSON.stringify(rows));	
			
			$('#frm_inputQuo').form('submit',{
				url:'/jlyw/QuotationServlet.do?method=12',
				onSubmit:function(){
					var selects = $('#table_QuoItem').datagrid('getSelections');
					if(selects.length==0){
						$.messager.alert('��ʾ','����ѡ�񱨼۵���Ŀ��','info');
						return false;
					}
								
					$("#QuoItemRows").val(JSON.stringify(selects));	
					
					return $('#frm_inputQuo').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");									
					if(result.IsOK){
						var items = result.rows;//�������
						var index = $('#table5').datagrid('getRows').length;
						var testfeeTotal;
						for(var i=0;i<items.length;i++)
						{
							/****���¼�����ѻ���***/
							var testFee="";
							//var index1 = 1;	//�����һ��������¼
							if(items[i].SpeciesType==0||items[i].SpeciesType=='0'){
								$.ajax({   //�����ܼ����ߣ��ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ�֮��Ĳ��Ҽ��������Сֵ��
									type:'POST',
									url:'/jlyw/TargetApplianceServlet.do?method=13',
									data:{'Application':encodeURI(items[i].ApplianceSpeciesName),'Model':encodeURI(items[i].Model),'Range':encodeURI(items[i].Range),'Accuracy':encodeURI(items[i].Accuracy)},
									dataType:"json",
									async: false,
									success:function(data, textStatus){
										if(data.IsOK){//����ȡ�óɹ�	
											//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
											if(data.MinFee==data.MaxFee){						
												testFee=data.MinFee;
												//console.info("TestFee:"+testFee);
											}
										}else{//����ȡ��ʧ��
											$.messager.alert('����',data.msg,'error');
										}
									}
								});
							}
							
							if(testFee.length > 0&&items[i].Quantity!=null&&items[i].Quantity!=""){
								testfeeTotal = getInt(testFee)*getInt(items[i].Quantity);
							}else{
								testfeeTotal = "";
							}
							/****���¼�����ѻ��ܽ���***/	

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
		function searchQuoItem()//��ѯ���۵���Ŀ
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
				$.messager.alert('��ʾ','������ί�е�λ�����ƣ�','info');
				return false;
			}
			//console.info($('#NameAdd').combobox('getText'));
			$('#table6').datagrid('options').url='/jlyw/CommissionSheetServlet.do?method=1';
			$('#table6').datagrid('options').queryParams={'CustomerName':encodeURI($('#NameAdd').combobox('getText')),'ApplianceName':encodeURI($('#History_ApplianceName').val()),'BeginDate':$('#table6_History_BeginDate').datebox('getValue'),'EndDate':$('#table6_History_EndDate').datebox('getValue')};
			$('#table6').datagrid('reload');
		}
		
		function AddRecordFromHistory(){
			var items = $('#table6').datagrid('getSelections');
			var index = $('#table5').datagrid('getRows').length;	//�����һ��������¼
			var testfeeTotal;
			for(var i=0; i<items.length; i++){
				if(items[i].ApplianceSpeciesNameStatus != 0){
					$.messager.alert('��ʾ',"��Ȩ���� '"+items[i].ApplianceSpeciesName+"' �ѹ�ʱ��������ӣ�",'info');
					continue;
				}
				/****���¼�����ѻ���***/
				var testFee="";
				//var index1 = 1;	//�����һ��������¼
				if(items[i].SpeciesType==0||items[i].SpeciesType=='0'){
					$.ajax({   //�����ܼ����ߣ��ͺŹ�񡢲�����Χ��׼ȷ�ȵȼ�֮��Ĳ��Ҽ��������Сֵ��
						type:'POST',
						url:'/jlyw/TargetApplianceServlet.do?method=13',
						data:{'Application':encodeURI(items[i].ApplianceSpeciesName),'Model':encodeURI(items[i].Model),'Range':encodeURI(items[i].Range),'Accuracy':encodeURI(items[i].Accuracy)},
						dataType:"json",
						async: false,
						success:function(data, textStatus){
							if(data.IsOK){//����ȡ�óɹ�	
								//console.info("data.MinFee:"+data.MinFee+"  data.MaxFee:"+data.MaxFee);
								if(data.MinFee==data.MaxFee){						
									testFee=data.MinFee;
									//console.info("TestFee:"+testFee);
								}
							}else{//����ȡ��ʧ��
								$.messager.alert('����',data.msg,'error');
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
			
				/****���¼�����ѻ��ܽ���***/			
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
				$.messager.alert('��ʾ','������ί�е�λ�����ƣ�','info');
				return false;
		}
		$('#inputHistory_window').window('open');
	}
	function CustomerLC(){//ί�з�����
		var selected = $('#table2').datagrid('getSelected');
		if(selected != null){
			if(selected.Status==2||selected.Status==3){
				$.messager.alert('��ʾ','��������ע�������ߡ����깤�������ܽ��д˲�����','info');
				return false;
			}
			
			if(!$("#CustomerLC_form").form('validate'))
				return false ;
				
			var result = confirm("��ȷ��Ҫ���д˲�����");
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
						$.messager.alert('�ύ��','�����ɹ�','info');
					}else{
						$.messager.alert('�ύʧ�ܣ�',data.msg,'error');
					}
				}
			});
			
		}else{
			$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','info');
		}
	}
	function openBDwindow(){
		if($("#SiteManagerNameAdd").combobox('getText')==null||$("#SiteManagerNameAdd").combobox('getText').length==0){
			$.messager.alert('��ʾ',"����д�ֳ������ˣ�",'error');
			return;
		}
		$('#MissionBD_window').window('open');//�򿪱��۵���ѯ����	
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
			if(!('<%=loginuser.getName()%>'==row.SiteManagerName||'<%=loginuser.getName()%>'=='ϵͳ����Ա')){
				$.messager.alert('��ʾ','���ֳ������˺�ϵͳ����Ա���������ֳ�ί�е�!','warning');
				return;
			}
				
		 	if(row.SiteManagerName!=null&&row.SiteManagerName.length>0&&row.ExactTime!=null&&row.ExactTime.length>0){
								
					$('#TZ_LocaleCommissionCode').val(row.Code);
					$('#TZForm').submit();
			}else{
				$.messager.alert('��ʾ','�ֳ���⸺����Ϊ�ջ���ȷ��ʱ��Ϊ��','warning');
			}
		
							
		}else{
			$.messager.alert('��ʾ','��ѡ��һ������','warning');
		}
	}
	
	function saveBD(){//����ҵ��
		$('#table5').datagrid('acceptChanges');					
		var rows = $("#table5").datagrid("getRows");	
		
		$('#frm_add_customer').form('submit',{
			url: '/jlyw/LocaleMissionServlet.do?method=17&'+decodeURIComponent($("#MissionBD_form").serialize(),true),
			onSubmit:function(){
				//alert('onSubmit');
				if($('#RegionIdAdd').val() == ''){
					$.messager.alert('��ʾ','��ѡ��һ����Ч�ĵ�����','info');
					return false;
				}
							
				if(rows.length == 0){
					$.messager.alert('��ʾ',"��ѡ���������ߣ�",'info');
					return false;
				}
				for(var i=0; i<rows.length; i++){
					if(rows[i].Quantity == null || rows[i].Quantity == 0){
						$.messager.alert('��ʾ',"������ '"+rows[i].ApplianceSpeciesName+"' ��̨/����������0����������",'info');
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
					
					 $.messager.alert('��ʾ��','��ӳɹ�','info');
				}
				else{
					 $.messager.alert('���ʧ�ܣ�',result.msg,'error');
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
			<jsp:param name="TitleName" value="�ֳ����ҵ���ѯ���޸�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<form id="TZForm" method="post" action="/jlyw/SFGL/wtd/LocaleCommissionSheet.jsp" target="_blank">
	 <input id="TZ_LocaleCommissionCode"  type="hidden" name="LocaleCommissionCode"/>
</form>	
		<div style="margin-bottom:10px;padding-top:10px;padding-bottom:10px;width:1000px" class="easyui-panel" title="��ѯ����" collapsible="false"  closable="false">
			<form id="ff">
			<table id="table1" style="width:980px">
				<tr>
					<td  align="left">
					<label>ί�е�λ��</label><select name="QueryName" id="QueryName" style="width:152px"></select>&nbsp;
					<label>ҵ���ţ�</label><input name="Department" id="Department" style="width:152px"/>&nbsp;
					<label>״&nbsp;&nbsp;&nbsp;&nbsp;̬��</label><select name="MissionStatus" id="MissionStatus" style="width:152px"><option value="" >ȫ��</option><option value="4">δ�˶�</option><option value="5">�Ѻ˶�</option><option value="1">�ѷ���</option><option value="2">�����</option><option value="0" selected="selected">δ���</option></select>
                    </td>
					<td align="left"><a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">��ѯ</a></td>
				</tr>
                <tr>
                	<td align="left">
                    <label>������ţ�</label><input class="easyui-validatebox" id="QueryCode" type="text" style="width:152px" />&nbsp;
					<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="History_BeginDate" type="text" style="width:152px" />&nbsp;
					<label>����ʱ�䣺</label><input class="easyui-datebox" id="History_EndDate" type="text" style="width:152px" />
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

<div id="edit" class="easyui-window" modal="true" title="�޸��ֳ�ҵ����Ϣ" style="padding: 10px;width: 1000;position:relative" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_update_localmission" method="post">
				<input type="hidden" id="Id" name="Id" value="" />
				<input id="Appliances1" name="Appliances" type="hidden" />
				<div>
				 <table style="width:980px;" >
					<tr >
						<td align="right" style="width��10%">��λ���ƣ�</td>
						<td align="left"  style="width��20%"><select name="Name" id="Name" class="easyui-validatebox" require="true" style="width:178px" readonly="readonly"></select><input type="hidden" name="CustomerId" id="CustomerId" /></td>
						<td align="right" style="width��10%">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
						<td align="left"  style="width��20%"><input id="zcd" name="ZipCode" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
						<td align="right" style="width��10%">��λ��ַ��</td>
						<td align="left" style="width��20%"><input id="Address" name="Address" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
					</tr>
					<tr >
						<td align="right">��λ�绰��</td>
						<td align="left"><input id="Tel" name="Tel" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">�� ϵ �ˣ�</td>
						<td align="left"><input id="con" name="Contactor" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">��ϵ�˺��룺</td>
						<td align="left"><input id="contactortel" name="ContactorTel" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
					</tr>
			
					<tr >
						<td align="right">���ڵ�����</td>
						<td align="left">
							<select id="rid" name="Region" class="easyui-combobox" style="width:178px" required="true"></select><input type="hidden" name="RegionId" id="RegionId" /></td>
						<td align="right">�ݶ����ڣ�</td>
						<td align="left"><input type="text" id="TentativeDate" name="TentativeDate" class="easyui-datebox" style="width:178px" required="true" /></td>
						<td align="right">�ֳ���⸺���ˣ�</td>
						<td align="left"><select name="SiteManagerName" id="SiteManagerName" style="width:178px" ></select><input type="hidden" name="SiteManagerId" id="SiteManagerId" value="" /></td>
					</tr>
					<tr>
						<td align="right">ҵ���ţ�</td>
						<td align="left"><input id="Department" name="Department" class="easyui-validatebox" style="width:175px"  required="true"/></td>
						<td align="right">�˶����ڣ�</td>
						<td align="left"><input type="text" id="CheckDate" name="CheckDate" class="easyui-datebox" style="width:178px"  /></td>
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
						<td align="left"  ><input s id="Remark" name="Remark" style="width:200px" ></input></td>
					</tr>
					<tr>
						<td  align="right">̨ͷ���ƣ�</td>
			  			<td  align="left" colspan="5"><select name="HeadName" id="HeadName" style="width:178px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" ></select></td>
					</tr>
					<tr >	
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg()">ȷ���޸�</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="generateSheet()">�����ֳ�ί�е�</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel()">�ر�</a></td>
					</tr>
				</table>
				</div>
			</form>
			
</div>

<div id="check" class="easyui-window" modal="true" title="�˶��ֳ�ҵ����Ϣ" style="padding: 10px;width: 1000;position:relative" iconCls="icon-ok" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_check_localmission" method="post">
				<input type="hidden" id="Idcheck" name="Id" value="" />
				<input id="Appliancescheck" name="Appliances" type="hidden" />
				<div>
				 <table style="width:980px;" >
					<tr >
						<td align="right" style="width��10%">��λ���ƣ�</td>
						<td align="left"  style="width��20%"><select name="Name" id="Namecheck" class="easyui-validatebox" require="true" style="width:178px" readonly="readonly"></select><input  name="CustomerId" id="CustomerIdcheck"  type="hidden"/></td>
						<td align="right" style="width��10%">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
						<td align="left"  style="width��20%"><input id="zcdcheck" name="ZipCode" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
						<td align="right" style="width��10%">��λ��ַ��</td>
						<td align="left" style="width��20%"><input id="Addresscheck" name="Address" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
					</tr>
					<tr >
						<td align="right">��λ�绰��</td>
						<td align="left"><input id="Telcheck" name="Tel" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">�� ϵ �ˣ�</td>
						<td align="left"><input id="concheck" name="Contactor" type="text" class="easyui-validatebox" require="true" style="width:175px" /></td>
						<td align="right">��ϵ�˺��룺</td>
						<td align="left"><input id="contactortelcheck" name="ContactorTel" class="easyui-validatebox" require="true" type="text" style="width:175px" /></td>
					</tr>
			
					<tr >
						<td align="right">���ڵ�����</td>
						<td align="left">
							<select id="ridcheck" name="Region" class="easyui-combobox" style="width:178px" required="true"></select><input  name="RegionId" id="RegionIdcheck" type="hidden"/></td>
						<td align="right">�ݶ����ڣ�</td>
						<td align="left"><input type="text" id="TentativeDatecheck" name="TentativeDate" class="easyui-datebox" style="width:178px" required="true" /></td>
						<td align="right">�ֳ���⸺���ˣ�</td>
						<td align="left"><select name="SiteManagerName" id="SiteManagerNamecheck" style="width:178px" ></select><input  name="SiteManagerId" id="SiteManagerIdcheck" value="" type="hidden"/></td>
					</tr>
					<tr>
						<td align="right">ҵ���ţ�</td>
						<td align="left"><input id="Departmentcheck" name="Department" class="easyui-validatebox" style="width:175px"  required="true"/></td>
						<td align="right">�˶����ڣ�</td>
						<td align="left"><input type="text" id="CheckDatecheck" name="CheckDate" class="easyui-datebox" style="width:178px"  /></td>
						<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
						<td align="left"  ><input  id="Remarkcheck" name="Remark" style="width:200px" ></input></td>
					</tr>
					<tr>
						<td  align="right">̨ͷ���ƣ�</td>
			  			<td  align="left" colspan="5"><select name="HeadName" id="HeadNamecheck" style="width:178px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
					</tr>
					<tr >	
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="saveregcheck()">ȷ�Ϻ˶�</a></td>
						<td></td>
						<td><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancelcheck()">�ر�</a></td>
					</tr>
				</table>
				</div>
			</form>
			
</div>
		
<div id="add" class="easyui-window" modal="true" title="¼���ֳ�ҵ����Ϣ" style="padding: 10px;width: 1000px; position:relative" iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false">	
        <form id="frm_add_customer" method="post" >
		<input id="Appliances" name="Appliances" type="hidden" />
	<table style="width:980px;" >
		<tr >
			<td align="right" style="width��10%">��λ���ƣ�</td>
			<td align="left"  style="width��20%"><select name="Name" id="NameAdd" style="width:178px"></select><input type="hidden" name="CustomerId" id="CustomerIdAdd" /></td>
			<td align="right" style="width��10%">��&nbsp;&nbsp;&nbsp;&nbsp;�ࣺ</td>
			<td align="left"  style="width��20%"><input id="zcdAdd" name="ZipCode" type="text" style="width:175px"  /></td>
			<td align="right" style="width��10%">��λ��ַ��</td>
			<td align="left" style="width��20%"><input id="AddressAdd" name="Address" type="text" style="width:175px" /></td>
		</tr>
		<tr >			
		    <td align="right">��λ�绰��</td>
		    <td align="left"><input id="TelAdd" name="Tel" type="text" style="width:175px" /></td>
			<td align="right">�� ϵ �ˣ�</td>
			<td align="left"><input id="conAdd" name="Contactor" type="text" style="width:175px" /></td>
		    <td align="right">��ϵ�˺��룺</td>
		    <td align="left"><input id="contactortelAdd" name="ContactorTel" type="text" style="width:175px"/></td>
		</tr>

		<tr >
			<td align="right">���ڵ�����</td>
			<td align="left">
				<select id="ridAdd" name="Region" class="easyui-combobox" style="width:178px" required="true"></select><input type="hidden" name="RegionId" id="RegionIdAdd" /></td>
			<td align="right">�ݶ����ڣ�</td>
			<td align="left"><input type="text" id="TentativeDateAdd" name="TentativeDate" class="easyui-datebox" style="width:175px" required="true" /></td>
			<td align="right">�ֳ���⸺���ˣ�</td>
			<td align="left" colspan="3"><select name="SiteManagerName" id="SiteManagerNameAdd" style="width:178px"></select><input type="hidden" name="SiteManagerId" id="SiteManagerIdAdd" value="" /></td>
		</tr>
	
		<tr >
			<td align="right">ҵ���ţ�</td>
			<td  align="left"><input id="DepartmentAdd" name="Department" class="easyui-validatebox" style="width:175px"  required="true"/></td>
			<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
			<td align="left"  colspan="3"><input  id="RemarkAdd" name="Remark"  style="width:330px;"  ></input></td>
			
		</tr>
		<tr>
			<td  align="right">̨ͷ���ƣ�</td>
			<td  align="left" colspan="5"><select name="HeadName" id="HeadNameAdd" style="width:178px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
		</tr>
		<tr height="20px">	
			
			<td colspan="2"><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="savereg1()">��Ӵ�ҵ��</a>&nbsp;&nbsp;<a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="openBDwindow()">����</a></td>
			<td ><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="inputQuo()">�ӱ��۵�����</a></td>
			<td ><a class="easyui-linkbutton" icon="icon-add" name="Add" href="javascript:void(0)" onclick="inputHistoryOpen()">����ʷ��¼����</a></td>
			<td colspan="2" align="center"><a class="easyui-linkbutton" icon="icon-cancel" name="Refresh" href="javascript:void(0)" onclick="cancel1()">�ر�</a></td>
			
		</tr>
	</table>
		
	</form>
	  
</div>
		
		
		<div id="undo" class="easyui-window" modal="true" title="����" style="padding: 10px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false">
			<form id="frm_undo_localmission" method="post">
				<input type="hidden" id="MissionId" name="Id" value="" />
				<div>
				 <table style="padding-top:20px" cellspacing="3" >
					<tr >
						<td align="center" colspan="2"><font size="+2">ҵ��ִ�����</font></td>						
					</tr>
					<tr >
						<td align="center" colspan="2" ><textarea name="Feedback" class="easyui-validatebox" required="true" id="Feedback" cols="40" rows="8"></textarea></td>						
					</tr>
					
					
					<tr height="50px">	
						
						<td align="center"><a class="easyui-linkbutton" icon="icon-save"  href="javascript:void(0)" onclick="saveundo()">ȷ��</a></td>
						
						<td align="center"><a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#undo').window('close');">�ر�</a></td>
					</tr>
				</table>
				</div>
			</form>
		</div>
<div id="inputQuo_window" class="easyui-window" modal="true" title="�ӱ��۵����뵽�ֳ�ҵ����" style="height:400px; width:800px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false">

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
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="inputQuoItem()">������ѡ��Ŀ</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>ί�е�λ��</label><input name="CustomerName" class="easyui-combobox" id="CustomerName" style="width:175px" valueField="name" textField='name'>&nbsp;<label>���۵��ţ�</label><input name="QuotationNumber" class="easyui-combobox" id="QuotationNumber" valueField="name" textField='name' required="true"></input>&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ���۵���Ŀ" id="btnHistorySearch" onclick="searchQuoItem()">��ѯ���۵���Ŀ</a>
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
				<td width="10%" align="right">������Ȩ&nbsp;&nbsp;</br>��&nbsp;&nbsp;&nbsp;&nbsp;�ƣ�</td>
                <td width="15%" align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:142px"></select><input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /><input type="hidden" id="StandName" name="StandName" value="" /><span style="color:red;">*</span></td>
                <td width="10%" align="right">�������ƣ�</td>
			    <td width="15%"  align="left"><select name="ApplianceName" class="easyui-combobox" valueField="name" textField='name' id="ApplianceName" style="width:152px"> </select> </td>
				
			    <td width="64" align="right">������ţ�</td>
			    <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>
				<td width="65" align="right">�����ţ�</td>
				<td width="168" align="left"><input id="AppManageCode" name="AppManageCode" type="text" /></td>
						    
			</tr>
			<tr>
				<td align="right">�ͺŹ��</td>
				<td align="left"><select name="Model" id="Model" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">������Χ��</td>
			 	 <td align="left"><select name="Range" id="Range" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">���ȵȼ���</td>
				<td align="left"><select name="Accuracy" id="Accuracy" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="right">�� �� ����</td>
				<td align="left"><select name="Manufacturer" id="Manufacturer" class="easyui-combobox" valueField="name" textField='name' style="width:152px"> </select>	</td>
			</tr>
			<tr>
				<td align="right">������ʽ��</td>
				<td align="left">
					<select id="ReportType" name="ReportType" style="width:140px">
						
						<option value="1">�춨</option>
						<option value="2">У׼</option>
						<option value="3">���</option>
						<option value="4">����</option>
					</select><span style="color:red;">*</span>			</td>
				<td width="10%" align="right">��&nbsp;&nbsp;&nbsp;����</td>
				<td width="15%" align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox" style="width:120px"/>��<span style="color:red;">*</span></td>	
				<td width="10%" align="right">�ɶ��ˣ�</td>
                <td width="20%" align="left"><select name="WorkStaff" id="WorkStaff" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
                <td width="10%" align="right">����ˣ�</td>
			    <td width="20%"  align="left"><input id="AssistStaff" name="AssistStaff" type="text" /></td>
				<!--<td width="10%" align="center" colspan="2"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="AddRecord()">�������</a></td>-->
			</tr>
			<tr>
				<!-- <td align="right">ά�޷ѣ�</td>
				<td align="left">
					<input id="RepairFee" name="RepairFee" class="easyui-numberbox" min="0" />
				</td>
				<td  align="right">���Ϸѣ�</td>
				<td  align="left">
					<input id="MaterialFee" name="MaterialFee" type="text" class="easyui-numberbox" min="0" />
				</td>
				<td  align="right">���Ѻϼƣ�</td>
				<td  align="left">
					<input id="TestFee" name="TestFee" type="text" class="easyui-numberbox" min="0" />
				</td> -->		
				            
			    <td  colspan="2" align="right">���<input id="username" name="username" class="easyui-combobox"  url="" style="width:100px;" valueField="name" textField="name" panelHeight="150px" /></td>
				<!--<td width="10%" align="center" colspan="2"><a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="AddRecord()">�������</a></td>-->
			</tr>
			<tr>
				<td align="center" colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"  title="�������" onclick="AddRecord()">�������</a></td>
				<td align="center" colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"  title="�������" onclick="UpdateRecord()">��������</a></td>
				
			</tr>
		</table>
	</div>	
	</form>
</div>	
<div id="inputHistory_window" class="easyui-window" modal="true" title="����ʷ��¼���뵽�ֳ�ҵ����" style="height:300px; width:850px;" iconCls="icon-undo" closed="true" maximizable="false" minimizable="false" collapsible="false" >
	    <table id="table6">
		</table>		
</div >
<div id="CustomerLC_window" class="easyui-window" modal="true" title="������ͨ��ʽ" style="height:230px; width:400px; padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="CustomerLC_form" >
<table>
<tr>
	  <td>
	  <div style="height:30px">
	  <label>&nbsp;&nbsp;׼ȷʱ�䣺</label><input class="easyui-datebox" id="ExactTime" name="ExactTime" type="text" style="width:100px" required="true"/>&nbsp;&nbsp;
	  <label>��ͨ��ʽ��</label><select class="easyui-combobox" id="QTway" name="QTway" type="text" style="width:100px" required="true" panelHeight="auto"><option value="����" selected="selected">����</option><option value="����" selected="selected">����</option><option value="Ħ�г�">Ħ�г�</option><option value="�綯��" >�綯��</option><option value="�Լݳ�" >�Լݳ�</option><option value="����" >����</option></select>&nbsp;
	  </div>
	  </td>
 </tr>
 <tr>
	 <td valign="top">
	  <label style="vertical-align:top">&nbsp;&nbsp;��&nbsp;&nbsp;&nbsp;&nbsp;ע��</label><textarea name="Remark" class="easyui-validatebox" required="true" id="RemarkLC" cols="30" rows="4"></textarea>
	  
	  </td>
</tr>
 <tr>
	   <td align="center" height="45px" valign="bottom">
	  <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-save"  href="javascript:void(0)" onclick="CustomerLC()">ȷ��</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#CustomerLC_window').window('close');" >�ر�</a>
	  </div>
	  </td>	
</tr>
</table>
</form>	
</div >

<div id="MissionBD_window" class="easyui-window" modal="true" title="�ֳ�ҵ�񲹵�" style="padding:10px" iconCls="icon-back" closed="true" maximizable="false" minimizable="false" collapsible="false" >
<form id="MissionBD_form"  method="post">
<table>
<tr>
	  <td>

	  <label>ȷ�����ڣ�</label><input class="easyui-datebox" id="ExactTimeBD" name="ExactTime" type="text" style="width:140px" required="true"/>  
	  </td>
	  <td>
	  <label>�ֳ�ί����ţ�</label><input class="easyui-validatebox" id="LocaleMissionCode" name="LocaleMissionCode" type="text" style="width:140px" required="true"/>  
	  </td>
 </tr>
 <tr>
	  <td >
		  <label>��ͨ��ʽ��</label><select class="easyui-combobox" id="QTway1" name="QTway" type="text" style="width:140px" required="true" panelHeight="auto">					            <option value="�����ɳ�" selected="selected">�����ɳ�</option>
		  	<option value="����">����</option>
			<option value="����" >����</option>
			<option value="Ħ�г�">Ħ�г�</option>
			<option value="�綯��" >�綯��</option>
			<option value="�Լݳ�" >�Լݳ�</option>
			<option value="����" >����</option>
		  </select>
	  <td>
	  <label>&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;��&nbsp;�ţ�</label><select id="Licence" name="Licence" style="width:140px" ></select>
	  </td>
 </tr>
 <tr>
	  <td>  
	  <label>�˳�˾����</label><select id="Drivername" name="Drivername" style="width:140px" ></select>	
	  </td>
	  <td>
	
	  </td>
</tr>
	
 <tr>
	   <td align="center" height="45px" valign="bottom" colspan="2">
	   <div style="height:40px">
	    <a class="easyui-linkbutton" icon="icon-save"  href="javascript:void(0)" onclick="saveBD()">ȷ�ϲ���</a>					
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#MissionBD_window').window('close');" >�ر�</a>
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
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="AddRecordFromHistory()">�ύѡ�е���ʷ��¼</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>�������ƣ�</label><input type="text" id="History_ApplianceName" value="" style="width:100px" />&nbsp;<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="table6_History_BeginDate" type="text" style="width:100px" />&nbsp;<label>��ֹʱ�䣺</label><input class="easyui-datebox" id="table6_History_EndDate" type="text" style="width:100px" />&nbsp;<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" title="��ѯ��ʷ��¼" id="btnHistorySearch" onclick="doLoadHistoryAppItems()">��ѯ��ʷ��¼</a>
					</td>
				</tr>
			</table>
			</form>
</div>
</DIV></DIV>
</body>
</html>
