<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ҵ���ѯ</title>
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
			$('#Customer').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
				textField:'name',
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
			
			$("#Receiver").combobox({
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
				}
			});
			
			$("#FinishUser").combobox({
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
				}
			});
			
			$("#CheckOutUser").combobox({
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
				}
			});
				
			$('#table6').datagrid({
			    width:1000,
				height:500,
				title:'ҵ���ѯ���',
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
					{field:'LocaleCommissionCode',title:'�ֳ��������',width:80,align:'center'},
					{field:'LocaleStaff',title:'�ֳ�������',width:80,align:'center'},
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
					{field:'Allotee',title:'�ɶ���',width:80,align:'center'},
					{field:'CustomerAddress',title:'ί�е�λ��ַ',width:180,align:'center'},
					{field:'CustomerTel',title:'ί�е�λ�绰',width:80,align:'center'},
					{field:'CustomerZipCode',title:'�ʱ�',width:50,align:'center'},
					{field:'CustomerContactor',title:'��ϵ��',width:50,align:'center'},
					{field:'CustomerContactorTel',title:'��ϵ�˵绰',width:80,align:'center'}
					
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
					text:'�ϴ�ί�е�����',
					iconCls:'icon-save',
					handler:function(){
						var select  = $('#table6').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','warning');
							return;
						}
						$('#SearchForm_Code2').val(select.Code);
						$('#SearchForm_Pwd2').val(select.Pwd);
						$('#SearchForm2').submit();
					}
				},'-',{
					text:'��ӡ',
					iconCls:'icon-print',
					handler:function(){
						$('#PrintStr').val(JSON.stringify($('#table6').datagrid('options').queryParams));
						$('#formLook').submit();
					}
				},'-',{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
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
			
			$("#ApplianceSpeciesName").combobox({
				valueField:'Name',
				textField:'Name',
				onSelect:function(record){
					$("#SpeciesType").val(record.SpeciesType);		//���߷�������
					$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//���߷���ID�������Ǳ�׼����ID��	
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
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/StatisticServlet.do?method=4';
			$('#table6').datagrid('options').queryParams={'Code':encodeURI($('#Code').val()),'CustomerId':encodeURI($('#Customer').combobox('getValue')),'Receiver':encodeURI($('#Receiver').combobox('getValue')),'CommissionDateFrom':encodeURI($('#CommissionDateFrom').datebox('getValue')),'CommissionDateEnd':encodeURI($('#CommissionDateEnd').datebox('getValue')),'Status':encodeURI($('#Status').val()),'CommissionType':encodeURI($('#CommissionType').val()),'ReportType':encodeURI($('#ReportType').val()),'SpeciesType':encodeURI($('#SpeciesType').val()),'ApplianceSpeciesId':encodeURI($('#ApplianceSpeciesId').val()),'FinishUser':encodeURI($('#FinishUser').combobox('getValue')),'FinishDateFrom':encodeURI($('#FinishDateFrom').datebox('getValue')),'FinishDateEnd':encodeURI($('#FinishDateEnd').datebox('getValue')),'CheckOutUser':encodeURI($('#CheckOutUser').combobox('getValue')),'CheckOutDateFrom':encodeURI($('#CheckOutDateFrom').datebox('getValue')),'CheckOutDateEnd':encodeURI($('#CheckOutDateEnd').datebox('getValue')),'HeadName':encodeURI($('#HeadName').combobox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
			document.getElementById("ReportType").value="";
			document.getElementById("CommissionType").value="";
			document.getElementById("Status").value="";
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
			<jsp:param name="TitleName" value="ҵ���ѯ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
        <form id="SearchForm2" method="post" action="/jlyw/SFGL/wtd/XGCommissionSheet.jsp" target="_self">
        <input id="SearchForm_Code2" type="hidden" name="Code"/>
        <input id="SearchForm_Pwd2" type="hidden" name="Pwd"/>
        </form>

        <form id="frm_export" method="post" action="/jlyw/QueryServlet.do?method=10">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:360px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
                <form id="query">
			<table width="950px" id="table1">
				<tr height="30px">
					<td width="10%" align="right">ί�е��ţ�</td>
					<td width="22%" align="left">
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;"/>
					</td>
					<td width="20%" align="right">ί�е�λ��</td>
					<td width="22%" align="left">
						<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;"/>
					</td>
				</tr >
                <tr>
                	<td align="right">��&nbsp;��&nbsp;����</td>
                  	<td align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:152px"/>
                                  <input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /></td>
                    <td align="right">������ʽ��</td>
                	<td align="left">
					<select id="ReportType" name="ReportType" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1">�춨</option>
						<option value="2">У׼</option>
						<option value="3">���</option>
						<option value="4">����</option>
					</select></td>
                </tr>
                <tr height="30px">
                	<td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
				  	<td align="left">
						<select name="Status" id="Status" style="width:152px;">
                            <option value="" selected="selected">ȫ��</option>
                            <option value="0" >���ռ�</option>
                            <option value="1" >�ѷ���</option>
                            <option value="2" >ת����</option>
                            <option value="<3">δ�깤</option>
                            <option value="3" >���깤</option>
                            <option value="<4" >δ����</option>
                            <option value="4" >�ѽ���</option>
                            <option value="9" >�ѽ���</option>
                            <option value="10" >��ע��</option>
                            <option value="-1">Ԥ����</option>
                        </select>
					</td>
                    <td align="right">ί�����</td>
                    <td align="left">
						<select name="CommissionType" id="CommissionType" style="width:152px" required="true">
                        	<option value="" selected="selected">ȫ��</option>
                            <option value="1">�������</option>
                            <option value="2">�ֳ����</option>
                            <option value="3">��������</option>
                            <option value="4">��ʽ����</option>
                            <option value="5">����ҵ��</option>
                            <option value="6">�Լ�ҵ��</option>
                            <option value="7">�ֳ�����</option>
                        </select>
					</td>
				</tr>
                <tr>
                    <td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                  	<td align="left"><select name="Receiver" id="Receiver" style="width:152px"/></td>
                </tr>
                <tr>
                	<td align="right">ί��ʱ�䣺</td>
				  	<td align="left">
						<input name="CommissionDateFrom" id="CommissionDateFrom" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="CommissionDateEnd" id="CommissionDateEnd" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr>
                <tr>
                	<td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                  	<td align="left"><select name="FinishUser" id="FinishUser" style="width:152px"/></td>
                </tr>
                <tr height="30px">
                	<td align="right">�깤ʱ�䣺</td>
				  	<td align="left">
						<input name="FinishDateFrom" id="FinishDateFrom" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="FinishDateEnd" id="FinishDateEnd" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
                <tr>
                	<td align="right">��&nbsp;��&nbsp;�ˣ�</td>
                  	<td align="left"><select name="CheckOutUser" id="CheckOutUser" style="width:152px"/></td>
                </tr>
                <tr height="30px">
                	<td align="right">����ʱ�䣺</td>
				  	<td align="left">
						<input name="CheckOutDateFrom" id="CheckOutDateFrom" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="CheckOutDateEnd" id="CheckOutDateEnd" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
                <tr height="30px">
                	<td align="right">̨ͷ��λ��</td>
				  	<td align="left" colspan="3">
						<select name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" url="/jlyw/AddressServlet.do?method=1"></select>
					</td>
                </tr> 
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
                    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				</tr>
				
		</table>
        </form>
		</div>
        <br />
      <div style="width:1000px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>
	  <!--<div id="p2" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="������" collapsible="false"  closable="false">
			<form id="allot" method="post">
			<table width="850px" >
				
				<tr >
				     <td width="33%"  align="right" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search1()">�鿴ԭʼ��¼</a>
	                     
					</td>
					<td  width="33%" align="center" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search2()">����֤��</a>
	                     
					</td>
					<td  width="33%" align="left" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">����</a>
					</td>
					
				</tr>
		  </table>
		  </form>
		</div>-->

	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=14" target="PrintFrame" accept-charset="utf-8" >
    
    	<input id="PrintStr"  name="PrintStr" style="width:150px;" type="hidden"/>
				
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>
