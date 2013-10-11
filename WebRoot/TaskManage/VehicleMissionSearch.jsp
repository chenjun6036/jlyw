<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�����������ѯ</title>
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
				title:'�ҵĳ��������б�',
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
					{title:'�ͻ���Ϣ',colspan:7,align:'center'},
					{title:'������Ϣ',colspan:6,align:'center'},
			        {title:'������Ϣ',colspan:9,align:'center'}				
					
				],[
					{field:'CreateDate',title:'����ʱ��',width:80,align:'center',sortable:true},
					{field:'Code',title:'ί�����',width:80,align:'center',sortable:true},
					{field:'CreatorName',title:'������',width:60,align:'center',sortable:true},
					{field:'Customer',title:'�ͻ�����',width:180,align:'center'},
					{field:'Address',title:'�ͻ���ַ',width:180,align:'center'},
					{field:'Contactor',title:'��ϵ��',width:100,align:'center'},
					{field:'Tel',title:'��ϵ�绰',width:120,align:'center'},
					{field:'ContactorTel',title:'��ϵ�˵绰',width:120,align:'center'},
					
					{field:'MissionDesc',title:'�ֳ������Ŀ��̨����',width:150,align:'center'},
					{field:'SiteManagerId',title:'�ֳ���⸺����',width:100,align:'center'},
					{field:'Department',title:'ҵ����',width:80,align:'center'},
					{field:'Staffs',title:'Ա��',width:160,align:'center'},
					//{field:'Description',title:'��������',width:100,align:'center'},
					{field:'ExactTime',title:'ȷ������',width:80,align:'center',sortable:true},
					
					{field:'DriverId',title:'����˾��',width:60,align:'center'},					
					{field:'People',title:'������Ա��',width:160,align:'center'},
					{field:'AssemblingPlace',title:'���ϵص�',width:100,align:'center'},
					{field:'BeginDate',title:'����ʱ��',width:160,align:'center',
						formatter:function(value,rowData,rowIndex){							
							return '<span style="color:red;">'+value+'</span>';	
						}
					},
					{field:'Kilometers',title:'��ʻ������',width:100,align:'center'},		   
					{field:'Licence',title:'���ƺ�',width:80,align:'center'},
					{field:'Limit',title:'��������',width:80,align:'center'},
					{field:'Model',title:'�������',width:120,align:'center'},
					{field:'Brand',title:'����Ʒ��',width:80,align:'center'}
					
					
					
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
						text:'ɾ����ѡ��������',
						iconCls:'icon-remove',
						handler:function(){
							var selected = $('#task-table').datagrid('getSelected');
							if(selected != null){
								var result = confirm("��ȷ��Ҫɾ����");
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
											$.messager.alert('��ʾ��',data.msg,'error');
										}
									}
								});
							}else{
								$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','info');
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

			$("#LocaleCode").combobox({//�ֳ�ί�����
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
			<jsp:param name="TitleName" value="����-�����ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

    <div id="p" class="easyui-panel" style="width:1000px;padding:10px;"
				title="ͳ������" collapsible="false"  closable="false">
			<table width="950px" id="table1">
				<tr >		
					<td align="left">
					<label>ҵ���ţ�</label><input name="Department" id="Department" style="width:80px"/>&nbsp;
					<label>���ƺţ�</label><input name="Licence" id="Licence" style="width:80px"/>&nbsp;
					<select name="MissionStatus" id="MissionStatus" style="width:100px"><option value="0">�ҵ�����</option><option value="1" selected="selected">��������</option></select>&nbsp;
					��ʼʱ��<input name="date1" id="dateTimeFrom" type="text" style="width:120px;"  class="easyui-datebox" >&nbsp;					
					����ʱ�䣺<input name="date2" id="dateTimeEnd" type="text" style="width:120px;"  class="easyui-datebox" >&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a>
					</td>
				</tr >
				<tr >		
					<td align="left">
					<label>ί�е�λ��</label><input name="CustomerName" id="CustomerName" style="width:80px"/>&nbsp;
					<label>�ֳ�ί����ţ�</label><input name="LocaleCode" id="LocaleCode" style="width:80px"/>
					</td>
				</tr >
				
		</table>
		</div>
	
		<table  id="task-table" iconCls="icon-search" >
		</table>

</DIV></DIV>	
</body>
</html>
