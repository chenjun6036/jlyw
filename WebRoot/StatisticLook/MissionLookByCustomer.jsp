<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ί�е�λ��ѯ��ֵ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../JScript/ExportToExcel.js"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
	<script>
	
		$(function(){
			$('#customerid').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
					$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
			
			$("#queryInsideContactor").combobox({
				valueField:'id',
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
			$('#cla').combobox({
				multiple:'true'
			});
			
			$('#cla').combobox('setValue',"");
			
			$('#result').datagrid({
			    width:1000,
				height:500,
				title:'��ֵ��Ϣ',
//				iconCls:'icon-save',
//                pageSize:10,
				singleSelect:true, 
				fit: false,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'Code',
				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true}
				]],
				columns:[[
					{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					{field:'Status',title:'ί�е�״̬',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
					{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
					{field:'ApplianceCode',title:'�������',width:80,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Accuracy',title:'���ȵȼ�',width:80,align:'center'},
					{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
					{field:'Quantity',title:'�ͼ�̨����',width:70,align:'center'},
					{field:'WithdrawQuantity',title:'����̨����',width:70,align:'center'},
					{field:'TotalFee',title:'�ܷ���',width:70,align:'center'},
					{field:'TestFee',title:'����',width:70,align:'center'},
					{field:'RepairFee',title:'�����',width:70,align:'center'},
					{field:'MaterialFee',title:'���Ϸ�',width:70,align:'center'},
					{field:'DebugFee',title:'���Է�',width:70,align:'center'},
					{field:'CarFee',title:'��ͨ��',width:70,align:'center'},
					{field:'OtherFee',title:'��������',width:70,align:'center'},
					{field:'ReportType',title:'������ʽ',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 1 || value == '1')
							{
								rowData['ReportType']=1;
								return "�춨";
							}
							else if(value == 2 || value == '2')
							{
								rowData['ReportType']=2;
								return "У׼";
							}
							else if(value == 3 || value == '3')
							{	rowData['ReportType']=3;
								return "����";
							}
						}},
					{field:'Allotee',title:'�ɶ���',width:80,align:'center'}
                ]],
				toolbar:[{
					text:'�鿴ί�е���ϸ',
					iconCls:'icon-search',
					handler:function(){
						var select  = $('#result').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm').submit();
					}
				},'-',{
					text:'��ӡ',
					iconCls:'icon-print',
					handler:function(){
						$('#PrintStr').val(JSON.stringify($('#result').datagrid('options').queryParams));
						alert($('#PrintStr').val());
						
						$('#formLook').submit();
					}
				}],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10||rowData.Status == "10"){	//��ע��
						return 'color:#FF0000';
					}else if(rowData.Status == 0||rowData.Status == "0"){	//���ռ�
						return 'color:#0000FF';	
					}else if(rowData.Status == 1||rowData.Status == "1"){	//�ѷ���
						return 'color:#0000FF';	
					}else if(rowData.Status == 2||rowData.Status == "2"){	//ת����
						return 'color:#CCFF00';	
					}else if(rowData.Status == 3||rowData.Status == "3"){	//���깤
						return 'color:#000000';	
					}else if(rowData.Status == 4||rowData.Status == "4"){  //�ѽ���
						return 'color:#008000';
					}else{
						return 'color:#000000';
					}
				},
				pagination:true,
				rownumbers:true,
				showFooter:true
			});
			
		});
		function query(){
			 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=4';
			 $('#result').datagrid('options').queryParams={'CustomerId':encodeURI($('#customerid').combobox('getValue')),'CommissionDateFrom':encodeURI($('#CommissionDateFrom').datebox('getValue')),'CommissionDateEnd':encodeURI($('#CommissionDateEnd').datebox('getValue')),'FinishDateFrom':encodeURI($('#FinishDateFrom').datebox('getValue')),'FinishDateEnd':encodeURI($('#FinishDateEnd').datebox('getValue')),'CheckOutDateFrom':encodeURI($('#CheckOutDateFrom').datebox('getValue')),'CheckOutDateEnd':encodeURI($('#CheckOutDateEnd').datebox('getValue')),'Status':encodeURI($('#Status').val()),'Classi':encodeURI($('#cla').combobox('getValues')),'RegionId':encodeURI($('#rid').combobox('getValue')),'ZipCode':encodeURI($('#zipcode').val()),'InsideContactor':encodeURI($('#InsideContactor').combobox('getValue'))};
			 $('#result').datagrid('reload');
		}
		
		function reset(){
			$('#query').form('clear');
			document.getElementById("Status").value="";
		}

	</script>
</head>
<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
</form>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ѯί�е�λ��ֵ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:250px;padding:10px;"
				title="ͳ������" collapsible="false"  closable="false">
			<table width="850px" id="table1">
            <form id="query">
				<tr >
					<td align="right" >ί�е�λ���ƣ�</td>
					<td align="left" >
						<input id="customerid" class="easyui-combobox" name="customer" url="" style="width:152px;" valueField="id" textField="name" panelHeight="auto" />
					</td>
                    <td align="right" >ί�е�λ�ʱࣺ</td>
					<td align="left" >
						<input id="zipcode" class="easyui-validatebox" name="ZipCode"/>
					</td>
                </tr>
                <tr>
                	<td align="right" >ί�е�λ������</td>
					<td align="left" >
						<select id="rid" name="RegionId" class="easyui-combobox" style="width:152px" valueField="id" textField="name" panelHeight="auto" mode="remote" url="/jlyw/RegionServlet.do?method=2" editable="false" ></select>
					</td>
                    <td align="right" >ί�е�λ���ͣ�</td>
					<td align="left" >
						<select id="cla" name="Classification" class="easyui-combobox" style="width:152px" panelHeight="auto">
                                <option value='3C'>3C</option>
                                <option value='QS'>QS</option>
                                <option value='ISO9000'>ISO9000</option>
                                <option value='��ҵ��Ʒ���֤'>��ҵ��Ʒ���֤</option>
                        </select>
					</td>
                </tr>
                <tr>
					<td align="right">ί��ʱ�䣺</td>
					<td align="left">
						<input name="CommissionDateFrom" id="CommissionDateFrom" style="width:152px;"  class="easyui-datebox" />
					</td>
					<td align="center">��</td>
					<td align="left">
						<input name="CommissionDateEnd" id="CommissionDateEnd" style="width:152px;"  class="easyui-datebox" />
					</td>
				</tr >
                <tr>
					<td align="right">�깤ʱ�䣺</td>
					<td align="left">
						<input name="FinishDateFrom" id="FinishDateFrom" style="width:152px;"  class="easyui-datebox" />
					</td>
					<td align="center">��</td>
					<td align="left">
						<input name="FinishDateEnd" id="FinishDateEnd" style="width:152px;"  class="easyui-datebox" />
					</td>
				</tr >
                <tr>
					<td align="right">����ʱ�䣺</td>
					<td align="left">
						<input name="CheckOutDateFrom" id="CheckOutDateFrom" style="width:152px;"  class="easyui-datebox" />
					</td>
					<td align="center">��</td>
					<td align="left">
						<input name="CheckOutDateEnd" id="CheckOutDateEnd" style="width:152px;"  class="easyui-datebox" />
					</td>
				</tr >
                <tr>
					<td align="right">״̬��</td>
					<td align="left">
						<select name="Status" id="Status" style="width:152px;">
                            <option value="" selected="selected">ȫ��</option>
                            <option value="0" >���ռ�</option>
                            <option value="1" >�ѷ���</option>
                            <option value="2" >ת����</option>
                            <option value="3" >���깤</option>
                            <option value="4" >�ѽ���</option>
                            <option value="10" >��ע��</option>
                            <option value="-1">Ԥ����</option>
                        </select>
					</td>
                    <td align="left">
                    	<td align="right">�ڲ���ϵ�ˣ�</td>
						<input name="InsideContactor" id="InsideContactor" style="width:152px;"  class="easyui-combobox" />
					</td>
				</tr >
				<tr height="50px">
				    <td colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
				    <td colspan="3" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				</tr>
				</form>
		</table>
		</div>
		<br />
      <div style="width:1000px;height:500px;">
	     <table id="result" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>		
	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=14" target="PrintFrame" accept-charset="utf-8" >
		
		<input id="PrintStr"  name="PrintStr" style="width:150px;" type="hidden"/>

	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>
