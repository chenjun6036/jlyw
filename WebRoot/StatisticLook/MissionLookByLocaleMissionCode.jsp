<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ֳ�ί�е���ѯ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../JScript/upload.js"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
	<script>
		$(function(){
			$("#LocaleCommissionCode").combobox({//�ֳ�ί�����
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'code',
				textField:'code',
				required:true,
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
							var newValue = $('#LocaleCommissionCode').combobox('getText');
							$('#LocaleCommissionCode').combobox('reload','/jlyw/LocaleMissionServlet.do?method=15&QueryName='+encodeURI(newValue));
					}, 700);
					
				}
			});
				
			$('#table6').datagrid({
			    width:900,
				height:500,
				title:'�ֳ�ί�е����',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'ί�е���',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'ί�е�λ',width:180,align:'center',sortable:true},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					{field:'Status',title:'ί�е�״̬',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'sdsd',title:'�Ƿ���깤',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(rowData.Status == 10){	//��ע��
								return ""
							}else if(rowData.Status >= 3){	//���깤(�����ѽ��ˡ��ѽ���)
								return ""
							}else if(rowData.IsSubContract==true ||(rowData.FinishQuantity!=null&&rowData.EffectQuantity!=null&&rowData.FinishQuantity == rowData.EffectQuantity)){	//�����깤������
								return "���깤"
							}else{
								return "";
							}
						}
					},
					{field:'ApplianceSpeciesName',title:'������Ȩ��',width:80,align:'center'},
					{field:'ApplianceName',title:'��������',width:80,align:'center',sortable:true},
					{field:'ApplianceCode',title:'�������',width:80,align:'center'},
					{field:'AppManageCode',title:'������',width:80,align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,align:'center'},
					{field:'Range',title:'������Χ',width:80,align:'center'},
					{field:'Accuracy',title:'���ȵȼ�',width:80,align:'center'},
					{field:'Manufacturer',title:'���쳧��',width:80,align:'center'},
					{field:'Quantity',title:'̨/����',width:70,align:'center'},
					{field:'WithdrawQuantity',title:'����̨����',width:70,align:'center'},
					{field:'TotalFee',title:'�ܷ���',width:70,align:'center'},
					{field:'TestFee',title:'����',width:70,align:'center'},
					{field:'RepairFee',title:'�����',width:70,align:'center'},
					{field:'MaterialFee',title:'���Ϸ�',width:70,align:'center'},
					{field:'DebugFee',title:'���Է�',width:70,align:'center'},
					{field:'CarFee',title:'��ͨ��',width:70,align:'center'},
					{field:'OtherFee',title:'��������',width:70,align:'center'},
					{field:'MandatoryInspection',title:'ǿ�Ƽ���',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['MandatoryInspection']=0;
								return "ǿ�Ƽ춨";
							}
							else if(value == 1 || value == '1')
							{
								rowData['MandatoryInspection']=1;
								return "��ǿ�Ƽ춨";
							}
							else
							{
								return "";
							}
						}},
					{field:'Urgent',title:'�Ƿ�Ӽ�',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Urgent']=0;
								return "��";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Urgent']=1;
								return "��";
							}
							else
							{
								return "";
							}
						}},
					{field:'Trans',title:'�Ƿ�ת��',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Trans']=0;
								return "��";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Trans']=1;
								return "��";
							}
							else
							{
								return "";
							}
						}},
					{field:'SubContractor',title:'ת����',width:80,align:'center'},
					{field:'Appearance',title:'��۸���',width:80,align:'center'},
					{field:'Repair',title:'�������',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Repair']=0;
								return "��Ҫ����";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Repair']=1;
								return "��������";
							}
							else
							{
								return "";
							}
						}},
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
							else
							{
								return "";
							}
						}},
					{field:'OtherRequirements',title:'����Ҫ��',width:80,align:'center'},
					{field:'Location',title:'���λ��',width:80,align:'center'},
					{field:'FinishLocation',title:'�깤���λ��',width:80,align:'center'},
					{field:'Allotee',title:'�ɶ���',width:80,align:'center'}	
                ]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				toolbar:[{
					text:'�鿴ί�е���ϸ',
					iconCls:'icon-search',
					handler:function(){
						var select  = $('#table6').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm').submit();
					}
				},'-',{
					text:'����',
					iconCls:'icon-save',
					handler:function() {
						myExport();
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
				}
			});
			
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/StatisticServlet.do?method=4';
			$('#table6').datagrid('options').queryParams={'LocaleMissionCode':encodeURI($('#LocaleCommissionCode').combobox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
		}
		function myExport(){
			
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#table6').datagrid('options').queryParams));
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
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ֳ�ί�е���ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
        <form id="frm_export" method="post" action="/jlyw/QueryServlet.do?method=10">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:80px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
                <form id="query">
			<table width="850px" id="table1">
				<tr height="40px">
					<td width="14%" align="right" >�ֳ�������ţ�</td>
					<td width="22%" align="left" >
						<input id="LocaleCommissionCode" class="easyui-combobox" name="LocaleCommissionCode" url="" style="width:150px;" panelHeight="150px" />
					</td>
				    <td align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
				</tr>
		</table>
        </form>
		</div>
        <br />
      <div style="width:900px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>
</div>
</DIV>
</DIV>
</body>
</html>
