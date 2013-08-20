<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�������÷���</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
    <script type="text/javascript" src="../JScript/upload.js"></script>
<script>
$(function(){		
	
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
			try{
				window.clearTimeout(this.reloadObj);
			}catch(ex){}
			this.reloadObj = window.setTimeout(function(){   
					var newValue = $('#DriverName').combobox('getText');
					$('#DriverName').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 700);
			
		}
	});	
	$("#StaffName").combobox({
	//	url:'/jlyw/CustomerServlet.do?method=5',
		valueField:'name',
		textField:'name',
		onSelect:function(record){		
			$("#StaffId").val(record.id);
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
					var newValue = $('#StaffName').combobox('getText');
					$('#StaffName').combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}, 700);
			
		}
	});	
	$('#allot_info_table').datagrid({
		title:'���÷�����Ϣ',
//			iconCls:'icon-save',
		width:985,
		height:200,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//			collapsible:true,
//		url:'/jlyw/TaskAssignServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
		remoteSort: false,
		//idField:'userid',
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns:[[
			{field:'DrivingVehicleId',title:'������¼���',width:120,sortable:true},
			{field:'StaffName',title:'Ա������',width:120},
			{field:'StaffFee',title:'�е�����',width:120}
			//{field:'Remark',title:'��ע',width:120}
		]],
		pagination:false,
		rownumbers:true	,
		toolbar:"#allot-toolbar",
		
		onSelect:function(rowIndex, rowData){
			
		},
		onLoadSuccess:function(data){
			computeFee();
		}		
	});
	
	$('#driving_table').datagrid({
		title:'������¼��Ϣ',
//		iconCls:'icon-save',
		width:600,
		height:350,
		singleSelect:true, 
		fit: false,
		nowrap: false,
		striped: true,
//		collapsible:true,
		url:'/jlyw/VehicleMissionServlet.do?method=2',
	    sortName: 'BeginDate',
 		sortOrder: 'desc',
		remoteSort: false,
		idField:'Id',
		frozenColumns:[[
	         {field:'ck',checkbox:true}
		]],
		columns:[
			[
				{field:'Id',title:'������¼���',width:80,sortable:true,align:'center'},
				{field:'VehicleLicence',title:'���ƺ�',width:80,sortable:true,align:'center'},
				{field:'DriverName',title:'˾��',width:80,align:'center'},
				{field:'BeginDate',title:'����ʱ��',width:150,align:'center'},
				{field:'People',title:'������Ա��',width:200,align:'center'},
				{field:'AssemblingPlace',title:'���ϵص�',width:120,align:'center'},
				{field:'Kilometers',title:'��ʻ������',width:80,align:'center'},
				{field:'TotalFee',title:'��������',width:80,align:'center'},
				{field:'Status',title:'�Ƿ��ѽ���',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value == 0 || value =="0"){
								return 'δ����';
							}
							if(value == 1 || value =="1"){
								return '�ѽ���';
							}
							
							return "δ����";
						}
				},
				{field:'SettlementName',title:'������',width:100,align:'center'},
				{field:'SettlementTime',title:'����ʱ��',width:150,align:'center'}
				
			]
		],
		rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 0){	//δ����
						return 'color:#000000'
					}else if(rowData.Status == 1){	//�ѽ���
						return 'color:#FF0033';	
					}
		},
		pagination:true,
		rownumbers:true	,
		toolbar:[{
			text:'����',
			iconCls:'icon-money',
			handler:function(){
				var result = confirm("��ȷ��Ҫ������");
				if(result == false){
					return false;
				}
				//var rows = $('#driving_table').datagrid('getRows');
				var recordIds="";
//				for(var i=0; i<rows.length; i++){
//					recordIds = recordIds + rows[i].Id + "|";				
//				}
				recordIds=JSON.stringify($('#driving_table').datagrid('options').queryParams);
				$.ajax({
					type: "post",
					url: "/jlyw/VehicleMissionServlet.do?method=7",
					data: {"recordIds":recordIds},
					dataType: "json",	//�������������ݵ�Ԥ������
					beforeSend: function(XMLHttpRequest){
						//ShowLoading();
					},
					success: function(data, textStatus){
						if(data.IsOK){
							$('#driving_table').datagrid('reload');
							$.messager.alert('��ʾ','����ɹ���','info');
							
						}else{
							$.messager.alert('��ʾ',data.msg,'error');
						}
					},
					complete: function(XMLHttpRequest, textStatus){
						//HideLoading();
					},
					error: function(){
						//���������
					}
				});
			}
		},'-',{
			text:'����ָ�δ����',
			iconCls:'icon-money',
			handler:function(){
				var result = confirm("��ȷ��Ҫ�ָ�δ������");
				if(result == false){
					return false;
				}
				var rowSelected = $('#driving_table').datagrid('getSelected');		
				if(rowSelected == null){
					$.messager.alert('��ʾ','��ѡ��һ������','info');
					return false;
				}
				var recordIds=rowSelected.Id;
//				for(var i=0; i<rows.length; i++){
//					recordIds = recordIds + rows[i].Id + "|";				
//				}
				//recordIds=JSON.stringify($('#driving_table').datagrid('options').queryParams);
				$.ajax({
					type: "post",
					url: "/jlyw/VehicleMissionServlet.do?method=10",
					data: {"recordIds":recordIds},
					dataType: "json",	//�������������ݵ�Ԥ������
					beforeSend: function(XMLHttpRequest){
						//ShowLoading();
					},
					success: function(data, textStatus){
						if(data.IsOK){
							$('#driving_table').datagrid('reload');
							$.messager.alert('��ʾ','�ָ��ɹ���','info');
							
						}else{
							$.messager.alert('��ʾ',data.msg,'error');
						}
					},
					complete: function(XMLHttpRequest, textStatus){
						//HideLoading();
					},
					error: function(){
						//���������
					}
				});
			}
		},'-',{
			text:'����������¼',
			iconCls:'icon-download',
			handler:function(){
				drivingExport();				
			}
		},'-',{
			text:'����������Ϣ',
			iconCls:'icon-download',
			handler:function(){
				drivingFeeExport();				
			}
		},'-',{
			text:'�鿴��ѡ��¼��Ӧ������',
			iconCls:'icon-search',
			handler:function(){
				var rowSelected = $('#driving_table').datagrid('getSelected');		
				if(rowSelected != null){
				   var titlecode ="������¼��š�"+rowSelected.Id+"����Ӧ���ֳ�������Ϣ";
					$('#table2').datagrid({title:titlecode});
					
					$('#table2').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=11';
					$('#table2').datagrid('options').queryParams={'DrivingVehicleId':rowSelected.Id};
					
					$('#table2').datagrid('reload');						
					
				}else{
					$.messager.alert('��ʾ','��ѡ��һ������','info');
				}	
			  }
		}],
		onSelect:function(rowIndex, rowData){
			$('#DrivingVehicleId').val(rowData.Id);
			$('#DrivingVehicleId1').val(rowData.Id);
			$('#Kilometers').val(rowData.Kilometers);
			$('#Status').val(rowData.Status);
			$('#allot_info_table').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=4';
			$('#allot_info_table').datagrid('options').queryParams={'DrivingVehicleId':encodeURI(rowData.Id)};
			$('#allot_info_table').datagrid('reload');
		}
	});
	
	$('#table2').datagrid({
		title:'�ֳ�������Ϣ',
		width:400,
		height:350,
		singleSelect:true, 
		nowrap: false,
		striped: true,
	//	url:'/jlyw/LocaleMissionServlet.do?method=8',
		sortName: 'CreateDate',
		remoteSort: false,
		sortOrder:'dec',
		idField:'Id',
		pageSize:10,
		
		columns:[[
			{field:'CreateDate',title:'����ʱ��',width:80,align:'center',sortable:true},
			{field:'CreatorName',title:'������',width:60,align:'center',sortable:true},
			{field:'Code',title:'ί�����',width:80,align:'center',sortable:true},
			{field:'Name',title:'��λ����',width:160,align:'center',sortable:true},
			{field:'Region',title:'���ڵ���',width:80,align:'center',sortable:true},
			
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
			{field:'MissionDesc',title:'������Ϣ',width:120,align:'center'},
			{field:'Department',title:'ҵ����',width:80,align:'center'},
			{field:'SiteManagerName',title:'��⸺����',width:80,align:'center'},
			{field:'Staffs',title:'��Ա',width:180,align:'center'},
			{field:'TentativeDate',title:'�ݶ�����',width:80,align:'center',sortable:true},					
			
			{field:'CheckDate',title:'�˶�����',width:80,align:'center',sortable:true},
			{field:'ExactTime',title:'ȷ������',width:80,align:'center',sortable:true},
			{field:'VehicleLisences',title:'�˳���Ϣ',width:120,align:'center'},
		
			{field:'Address',title:'��λ��ַ',width:120,align:'center'},
			{field:'ZipCode',title:'�ʱ�',width:60,align:'center'},
			{field:'Contactor',title:'��ϵ��',width:80,align:'center'},
			{field:'Tel',title:'��λ�绰',width:100,align:'center'},
			{field:'ContactorTel',title:'��ϵ�˵绰',width:100,align:'center'},
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
			}else if(rowData.Status == 2){	//���깤
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
		}
	});
			
});
function computeFee(){	//�����ܷ���
		
		var rows = $('#allot_info_table').datagrid('getRows');
		
		
		var TotalFee=0.0;
		for(var i=0; i<rows.length; i++){
			TotalFee=TotalFee+getFloat(rows[i].StaffFee);
			
		}
		
		$('#TotalFee').val(TotalFee);
				
	
}
function doLoadDrivingVehicle(){	//���ҳ�����¼
				
		//���س�����¼
		$("#SearchForm").form('validate');
		var History_BeginDate=$('#History_BeginDate').datebox('getText');
		var History_EndDate=$('#History_EndDate').datebox('getText');
		$('#driving_table').datagrid('options').url='/jlyw/VehicleMissionServlet.do?method=2';
		$('#driving_table').datagrid('options').queryParams={'DriverName':encodeURI($("#DriverName").combobox('getText')),'History_BeginDate':encodeURI(History_BeginDate),'History_EndDate':encodeURI(History_EndDate),'License':encodeURI($("#License").val()),'MissionStatus':encodeURI($("#MissionStatus").val())};
		$('#driving_table').datagrid('reload');
				
	
}
function addFee(){   	//����һ����÷���
	
	$("#FeeSubmitForm1").form('submit', {
		url:'/jlyw/VehicleMissionServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($('#Status').val()=='1'||$('#Status').val()==1){
				$.messager.alert('��ʾ��',"�˳�����¼�ѽ��㣬�����ڽ��д˲�����",'info');
				return false;
			}	
			if($('#DrivingVehicleId1').val()==''||$('#DrivingVehicleId1').val().length==0){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ĳ�����¼",'info');
				return false;
			}
			var StaffName = $('#StaffName').combobox('getText');
			
			if(StaffName==''){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч��Ա��",'info');
				return false;
			}
			var rows = $('#allot_info_table').datagrid('getRows');//��ֹ����ظ�Ա��
			for(var i=0; i<rows.length; i++){
				if(rows[i].StaffName==StaffName){
					$.messager.alert('��ʾ��',"��Ա���Ѿ������˷��ã������ظ����䣡",'info');
					return false;
				}	
			}		
			var StaffFee = $('#StaffFee').val();
			if(StaffFee == ''){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ķ������",'info');
				return false;
			}
			
			return $("#FeeSubmitForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				
				$('#allot_info_table').datagrid('reload');
				$.messager.alert('��ʾ��','�ύ�ɹ���','info');
				
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}
function removeFee(){
	if($('#Status').val()=='1'||$('#Status').val()==1){
				$.messager.alert('��ʾ��',"�˳�����¼�ѽ��㣬�����ڽ��д˲�����",'info');
				return false;
	}	
	var row = $("#allot_info_table").datagrid('getSelected');
	if(row){
		var result = confirm("�ò��������棬��ȷ��Ҫע����");
		if(result == false){
			return false;
		}
		$.ajax({
				type: "post",
				url: "/jlyw/VehicleMissionServlet.do?method=5",
				data: {"Id":row.Id},
				dataType: "json",	//�������������ݵ�Ԥ������
				beforeSend: function(XMLHttpRequest){
					//ShowLoading();
				},
				success: function(data, textStatus){
					if(data.IsOK){
						$('#allot_info_table').datagrid('reload');
						$.messager.alert('��ʾ��','ɾ���ɹ���','info');
						computeFee();
					}else{
						$.messager.alert('ע��ʧ�ܣ�',data.msg,'error');
					}
				},
				complete: function(XMLHttpRequest, textStatus){
					//HideLoading();
				},
				error: function(){
					//���������
				}
		});
	}else{
		$.messager.alert('��ʾ��',"����ѡ��һ����¼",'info');
	}

} 
function doFeeSubmit(){   	//�ύ���÷���
	$("#FeeSubmitForm").form('submit', {
		url:'/jlyw/VehicleMissionServlet.do?method=6',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($('#Status').val()=='1'||$('#Status').val()==1){
				$.messager.alert('��ʾ��',"�˳�����¼�ѽ��㣬�����ڽ��д˲�����",'info');
				return false;
			}	
			if($('#DrivingVehicleId').val()==''||$('#DrivingVehicleId').val().length==0){
				$.messager.alert('��ʾ��',"��ѡ��һ����Ч�ĳ�����¼",'info');
				return false;
			}
			
			return $("#FeeSubmitForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//���¼������������Ϣ
				$('#driving_table').datagrid('reload');
				$.messager.alert('��ʾ��','���³ɹ���','info');;
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});  
}  

function drivingExport(){
	ShowWaitingDlg("���ڵ��������Ժ�......");
	$('#paramsStr').val(JSON.stringify($('#driving_table').datagrid('options').queryParams));
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
				$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
				CloseWaitingDlg();
			}
		}
	});
}
function drivingFeeExport(){
	ShowWaitingDlg("���ڵ��������Ժ�......");
	$('#paramsStr1').val(JSON.stringify($('#driving_table').datagrid('options').queryParams));
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
				$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
				CloseWaitingDlg();
			}
		}
	});
}
</script>
</head>

<body>
<form id="frm_export" method="post" action="/jlyw/VehicleMissionServlet.do?method=8">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_export1" method="post" action="/jlyw/VehicleMissionServlet.do?method=9">
<input id="paramsStr1" name="paramsStr" type="hidden" />
</form>

<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�������÷���" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

      <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="������¼��ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="950px" id="table1">
				<tr >
					<td align="center" ><label>�����ƺţ�</label><input id="License" name="License" type="text" style="width:100px" />	&nbsp;			
					<label>˾����</label><input id="DriverName" name="DriverName" type="text" style="width:100px" />&nbsp;
					<select name="MissionStatus" id="MissionStatus" style="width:100px"><option value="" selected="selected">ȫ��</option><option value="0" >δ����</option> <option value="1" >�ѽ���</option></select>&nbsp;
					<label>��ʼʱ�䣺</label><input class="easyui-datebox" id="History_BeginDate" name="History_BeginDate" type="text" style="width:100px" />&nbsp;
					<lable>����ʱ�䣺</label><input class="easyui-datebox" id="History_EndDate" name="History_EndDate" type="text" style="width:100px" />            </td>
					
					<td width="15%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadDrivingVehicle()">��ѯ</a></td>
					
				</tr >
		</table>
		</form>
		</div>
		<table width="1005px" >
    	 <tr>
			  <td>
				 <table id="driving_table" iconCls="icon-tip" width="600" height="350px"></table>
			  </td>
			  <td>
				<table id="table2" style="height:350px; width:400px"><!--�ֳ�������Ϣ-->
				</table>
			  </td>
		  </tr>
		</table>
	
		
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;+position:relative;"
				title="���÷���" collapsible="false"  closable="false">
			<form id="FeeSubmitForm" method="post">
			
			<table id="allot_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br/>
			
			<input type="hidden" id="DrivingVehicleId" name="DrivingVehicleId" value="" />
			<input type="hidden" id="Status" name="Status" value="" />
			<table width="950">
				
				<tr height="40px">
					<td width="200" align="center" >��ʻ�������<input id="Kilometers" name="Kilometers" type="text" class="easyui-numberbox" precision="2" style="width:120px;" required="true"  /></td>
					
					<td width="200" align="center" >�ܷ��ã�<input id="TotalFee" name="TotalFee" type="text" style="width:110px" class="easyui-numberbox" precision="2" required="true" />Ԫ</td>
					<td align="left" ><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doFeeSubmit()">ȷ��</a>&nbsp;&nbsp;</td>
					
				</tr>
			 </table>
		  </form>
		</div><br/>
<div id="allot-toolbar" style="padding:2px;">
		<form id="FeeSubmitForm1" method="post">
		<input type="hidden" id="DrivingVehicleId1" name="DrivingVehicleId" value="" />
		<table cellpadding="0" cellspacing="0" style="width:100%">
		
				<tr>
				    <td style="text-align:left;padding-left:2px"><a class="easyui-linkbutton" iconCls="icon-remove" plain="true" href="javascript:void(0)" onClick="removeFee()">ɾ��</a></td>
					<td style="text-align:right;padding-right:2px">
					ѡ��Ա����<input id="StaffName" name="StaffName" type="text" style="width:122px;"  /><input id="StaffId" name="StaffId" type="hidden" style="width:120px;"   />&nbsp;&nbsp;
					���ã�<input id="StaffFee" name="StaffFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">Ԫ&nbsp;&nbsp;
					<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" plain="true" onClick="addFee()">���</a>
					</td>
				</tr>
		</table>
		</form>
			
</div>

		

</DIV></DIV>

</body>
</html>
