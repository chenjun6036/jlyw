<%@ page contentType="text/html; charset=gbk" language="java" import="com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>Ԥ��ί�е��޸�</title>
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
		<link href="../../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />

		<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		
		<script type="text/javascript" src="../../JScript/json2.js"></script>
		<script type="text/javascript" src="../../JScript/RandGenerator.js"></script>
		<script type="text/javascript" src="../../JScript/NumberChanger.js"></script>
		
		<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>
		<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
			   <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
		</object>
		<script type="text/javascript" src="../../WebPrint/printer.js"></script>
		<script type="text/javascript" src="../../WebPrint/printCommisionSheet.js"></script>
		
        <style type="text/css">
<!--
.STYLE1 {color: #FF0000}
-->
        </style>

<script language="javascript">
$(function(){
	var JSONobjPrint=null;
	$("#CustomerName").combobox({
		valueField:'name',
		textField:'name',
		required:true,
		onSelect:function(record){
			$("#CustomerTel").val(record.tel);
			$("#CustomerAddress").val(record.address);
			$("#CustomerZipCode").val(record.zipCode);
			$("#ContactPerson").val(record.contactor);
			$("#ContactorTel").val(record.contactorTel);
			$("#CustomerHandler").val('');
			$("#SampleFrom").combobox('setValue', '');
			$("#BillingTo").combobox('setValue','');
			
			$("#QuotationId").combobox('reload','/jlyw/QuotationServlet.do?method=11&QueryName='+encodeURI(record.name));	//���۵���ѯ
			
			try{
				//�ֳ�ί�е���ѯ���������½��ֳ�ί�е������õ�
				$("#LocaleCommissionCode").combobox('reload','/jlyw/LocaleMissionServlet.do?method=14&QueryName='+encodeURI(record.name));
			}catch(ex){}
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
			}, 500);
//			$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
		}
	});
	
	$("#SampleFrom").combobox({
		valueField:'name',
		textField:'name',
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
					var newValue = $('#SampleFrom').combobox('getText');
					$('#SampleFrom').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}, 500);
			//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
		}
	});
	$("#BillingTo").combobox({
		valueField:'name',
		textField:'name',
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
					var newValue = $('#BillingTo').combobox('getText');
					$('#BillingTo').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
			}, 500);
			//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
		}
	});
	
	$("#ApplianceSpeciesName").combobox({
		valueField:'Name',
		textField:'Name',
		onSelect:function(record){
			
			$("#SpeciesType").val(record.SpeciesType);		//���߷�������
			$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//���߷���ID�������Ǳ�׼����ID��
/*			$("#ApplianceName").combobox('loadData',record.ApplianceName);	//��������
			if(record.ApplianceName.length == 1){
				$("#ApplianceName").combobox('select',record.ApplianceName[0].name);
			}else{
				$("#ApplianceName").combobox('clear');
			}
			$("#Model").combobox('loadData',record.Model);	//�ͺŹ��
			$("#Range").combobox('loadData',record.Range);	//������Χ
			$("#Accuracy").combobox('loadData',record.Accuracy);	//���ȵȼ�
			$("#Manufacturer").combobox('loadData',record.Manufacturer);	//���쳧��
			$("#Allotee").combobox('loadData',record.Allotee);	//������Ա*/
			$("#ApplianceName").combobox('clear');
			$("#Model").combobox('clear');
			$("#Range").combobox('clear');
			$("#Accuracy").combobox('clear');
			$("#Manufacturer").combobox('clear');
			$("#Allotee").combobox('clear');
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
			$("#Allotee").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+record.SpeciesType+'&ApplianceSpeciesId='+record.ApplianceSpeciesId+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//������Ա
			
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
	
	$("#SubContractor").combobox({
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
			$(this).combobox('reload','/jlyw/SubContractServlet.do?method=0&QueryName='+newValue);
		}
	});
	$("#CommissionDate").datebox({
		onSelect:function(nowDate){
			nowDate.setDate(nowDate.getDate() + 7);
			$('#PromiseDate').datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		}
	});
	
	var nowDate = new Date();
	$("#CommissionDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	nowDate.setDate(nowDate.getDate() + 7);
	$("#PromiseDate").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
	
	$("#CommissionSheetForm").form('validate');
});
function doLoadCommissionSheet(){	//����ί�е�
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3&type=1',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			
			//$("#CommissionSheetId").val('');				
			//$("#Ness").removeAttr("checked");	//ȥ��ѡ
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//��ѡ
				}
				if(result.CommissionObj.Trans == 0){
					$("#Trans").attr("checked",true);	//��ѡ
				}
				$("#comSheetCode").val($("#Code").val());
				if($("#SpeciesType").val() == 0 || $("#SpeciesType").val() == '0' || $("#SpeciesType").val() == 2 || $("#SpeciesType").val() == '2'){	//��׼����Or��������
					
					$("#Model").combobox('reload','/jlyw/ApplianceServlet.do?method=2&SpeciesType='+$("#SpeciesType").val()+'&ApplianceSpeciesId='+$("#ApplianceSpeciesId").val());	//�ͺŹ��
					$("#Range").combobox('reload','/jlyw/ApplianceServlet.do?method=3&SpeciesType='+$("#SpeciesType").val()+'&ApplianceSpeciesId='+$("#ApplianceSpeciesId").val());	//������Χ
					$("#Accuracy").combobox('reload','/jlyw/ApplianceServlet.do?method=4&SpeciesType='+$("#SpeciesType").val()+'&ApplianceSpeciesId='+$("#ApplianceSpeciesId").val());	//���ȵȼ�
					$("#Manufacturer").combobox('reload','/jlyw/ApplianceServlet.do?method=5&SpeciesType='+$("#SpeciesType").val()+'&ApplianceSpeciesId='+$("#ApplianceSpeciesId").val());	//���쳧��
				}else if($("#SpeciesType").val() == 1 || $("#SpeciesType").val() == '1'){	//��������
					$("#ApplianceName").combobox('loadData',[]);
					$("#Model").combobox('loadData',[]);	//�ͺŹ��
					$("#Range").combobox('loadData',[]);	//������Χ
					$("#Accuracy").combobox('loadData',[]);	//���ȵȼ�
					$("#Manufacturer").combobox('loadData',[]);	//���쳧��
				}
				$("#Allotee").combobox('reload','/jlyw/TaskAssignServlet.do?method=6&SpeciesType='+$("#SpeciesType").val()+'&ApplianceSpeciesId='+$("#ApplianceSpeciesId").val()+'&AlloteeRule='+ $("input[name='AlloteeRule']:checked").val());	//������Ա
			}else{
				$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
			}
		}
	});  
} 
function updateYLcomSheet()	//Ԥ��ί�е��޸�
{
	$("#CommissionSheetForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=17',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			if($("#SpeciesType").val()=='' || $("#ApplianceSpeciesId").val()==''){
				$.messager.alert("��ʾ","��ѡ��һ����Ч�ġ�������Ȩ������","info");
				return false;
			}
			if($("#Quantity").val()==''){
				$.messager.alert("��ʾ","�����롮������������","info");
				return false;
			}
			if($('#Mandatory').val()==''){
				$.messager.alert("��ʾ","��ѡ���Ƿ�ǿ�졯��","info");
				return false;
			}
			if($('#Repair').val()==''){
				$.messager.alert("��ʾ","��ѡ��������񡯣�","info");
				return false;
			}
			if($('#ReportType').val()==''){
				$.messager.alert("��ʾ","��ѡ�񡮱�����ʽ����","info");
				return false;
			}
			if($('#Quantity').val()<0){
				$.messager.alert("��ʾ","������������С���㣡","info");
				return false;
			}
		
			return $("#CommissionSheetForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				
				JSONobjPrint=result.PrintObj
				var result1 = confirm("Ԥ��ί�е��޸ĳɹ����Ƿ��ӡί�е���");
				if(result1 == false){
					return false;
				}			
				Preview1(result.PrintObj);
				
			}else{
				$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
			}
		}
	});
}	
function doPrintCommissionSheet(){
	if(JSONobjPrint==null){
		$.messager.alert('��ʾ','��δ�޸�Ԥ��ί�е������ܴ�ӡ','error');
	}else{
		
		Preview1(JSONobjPrint);

	}
}
	
</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="Ԥ��ί�е��޸�" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">  
	  <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true"  value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>"  />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true"  value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>"  />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
                   
				</tr >
		</table>
		</form>

		</div>
	
		<form id="CommissionSheetForm" method="post" >
			<input type="hidden" name="Appliances" id="Appliances" value="" />
			<input type="hidden" name="comSheetCode" id="comSheetCode" value="" />
		 <div class="easyui-panel" style="width:1005px; margin:0px; padding:0px" title="�½�ί�е�">
			<br />
			<table width="1000px" id="table1">
				<tr>
				  <td width="75" align="right">ί����ʽ��</td>
				  <td width="169"  align="left"><select name="CommissionType" style="width:152px">
					  <option value="1" selected="selected">�������</option>
					  <option value="5">����ҵ��</option>
					  <option value="6">�Լ�ҵ��</option>
					  <option value="7">�ֳ�����</option>
					  <option value="3">��������</option>
					  <option value="4">��ʽ����</option>
					</select></td>
				  <td width="68" align="right">ί�����ڣ�</td>
				  <td width="168"  align="left"><input class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" required="true" /></td>
					<td width="69"  align="right">��ŵ����</td>
				    <td width="112"  align="left"><input class="easyui-datebox" name="PromiseDate" id="PromiseDate" type="text" /></td>
				    <td width="69"  align="right"></td>
			        <td width="234"  align="left"></td>
				</tr>
		</table>
		<table width="1000">
			<tr>
			  <td width="77"  align="right">ί�е�λ��</td>
              <td width="187" align="left"><select name="CustomerName" id="CustomerName" style="width:125px"></select><input type="button" class="add" value="�½�" onclick="window.location.href='/jlyw/BaseData/wtUnit/Customer.jsp'" /></td>
                <td width="64" align="right">&nbsp;��&nbsp;&nbsp;����</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">&nbsp;��&nbsp;&nbsp;ַ��</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>
				<td width="65" align="right">�������룺</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">��&nbsp;ϵ&nbsp;�ˣ�</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" class="easyui-validatebox" required="true" /></td>
				<td align="right">�ֻ����룺</td>
				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">֤�鵥λ��</td>
				<td align="left"><select name="SampleFrom" id="SampleFrom" style="width:152px" ></select></td>
                <td align="right">��Ʊ��λ��</td>
				<td align="left"><select name="BillingTo"  id="BillingTo" style="width:152px" ></select></td>
			</tr>
		</table>

          <table id="table3" width="1000px">
			<tr>
				<td width="77" align="right">ί���ˣ�</td>
		    <td width="187" align="left"><input name="CustomerHandler" id="CustomerHandler" type="text" /></td>
				<td width="64" align="right">̨ͷ���ƣ�</td>
			  <td width="171" align="left"><select name="HeadName" id="HeadName" style="width:152px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"></select></td>
				<!--<td width="63" align="right">�����ˣ�</td>
			  <td width="80" align="left"><input type="text" style="width:80px"/></td>
				<td width="44" align="right">���ڣ�</td>
			  <td width="80" align="left"><input type="text" id="dateTimeFrom" class="easyui-datebox" style="width:82px"/></td>
				<td width="75" align="right">�깤ȷ�ϣ�</td>
			  <td width="80" align="left"><input type="text" style="width:80px"/></td>
				<td width="47" align="right">���ڣ�</td>
			  <td width="159" align="left"><input type="text"id="dateTimeEnd" class="easyui-datebox" style="width:82px" /></td>-->
			  <td width="64" align="right">������ַ��</td>
		    <td width="168" align="left"><select name="RecipientAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true" editable="false"></select></td>
				<td width="65" align="right">ȡ����ַ��</td>
			  <td width="168" align="left"><select name="PickupAddress" style="width:152px" class="easyui-combobox" valueField="id" textField="address" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=0" required="true" editable="false"></select></td>
			</tr>
		</table>
		
		<br/>
 		 <table id="table2" width="1000">
			<tr>
				<td width="77" align="right">������Ȩ����</td>
              <td width="187" align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:152px">
              </select><span class="STYLE1">*</span>
              <input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /></td>
                <td width="64" align="right">�������ƣ�</td>
			    <td width="171"  align="left"><select name="ApplianceName" class="easyui-combobox" valueField="name" textField='name' id="ApplianceName" style="width:152px">
			      </select>		      </td>
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
                <td align="left"><input id="Ness" name="Ness" type="checkbox"/>��&nbsp;&nbsp;��</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">�� �� ����</td>
				<td align="left"><select name="Manufacturer" id="Manufacturer" class="easyui-combobox" valueField="name" textField='name' style="width:152px">
				  </select>				</td>
				<td align="right">��&nbsp;&nbsp;&nbsp;����</td>
			  <td align="left"><input id="Quantity" name="Quantity" type="text" class="easyui-numberbox"/>��<span class="STYLE1">*</span></td>
				<td align="right">�Ƿ�ǿ�죺</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1" >��ǿ�Ƽ춨</option>
						<option value="0">ǿ�Ƽ춨</option>
					</select><span class="STYLE1">*</span>				</td>
                <td align="left"><input id="Trans" name="Trans" type="checkbox"/>ת&nbsp;&nbsp;��</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">��۸�����</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text" value="����������޸�����"  /></td>
				<td align="right">�������</td>
				<td align="left">
					<select id="Repair" name="Repair" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1">��������</option>
						<option value="0">��Ҫ����</option>
					</select><span class="STYLE1">*</span>				</td>
				<td align="right">������ʽ��</td>
				<td align="left">
					<select id="ReportType" name="ReportType" style="width:152px">
						<option value="" selected="selected">��ѡ��...</option>
						<option value="1">�춨</option>
						<option value="2">У׼</option>
						<option value="3">���</option>
						<option value="4">����</option>
					</select><span class="STYLE1">*</span>				</td>
                <td align="right">ת �� ����</td>
				<td align="left"><select name="SubContractor" id="SubContractor" style="width:152px"></select></td>
			</tr>
			<tr>
				<td align="right">����Ҫ��</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text" value="��"  /></td>
				<td align="right">���λ�ã�</td>
				<td align="left"><input id="Location" name="Location" type="text" /></td>
				<td align="right">��&nbsp;��&nbsp;�ˣ�</td>
				<td align="left"><select name="Allotee" id="Allotee" class="easyui-combobox" valueField="name" textField='name' style="width:152px"></select></td>
				<td align="left" colspan="2"><input name="AlloteeRule" type="radio" value="0" <%= (SystemCfgUtil.getTaskAllotRule() == 0)?"checked=\"checked\"":"" %> />��ҵ������&nbsp;<input name="AlloteeRule" type="radio" value="1" <%= (SystemCfgUtil.getTaskAllotRule() != 0)?"checked=\"checked\"":"" %> />����ֵ��</td>
			</tr>
	 	 </table>
 		
		<table id="table4" width="1000px" style="margin-top:20px">
			<tr align="center" >
			    <td align="right"></td>
				<td align="left"></td>
				
				<td align="center" colspan="2">
					
			    </td>
                <td align="center" colspan="2">
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-add" name="Add" onclick="updateYLcomSheet()" title="�޸�Ԥ��ί�е���">�޸�Ԥ��ί�е�</a>
			  </td>
                <td align="center" colspan="2">
				    <a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintCommissionSheet()" title="��ӡ�޸ĺ��Ԥ��ί�е���">��ӡԤ��ί�е�</a>
				</td>
					
			</tr>
	  </table>
	  <br />
	  </div>
	  </form> 
<form id="formLook" method="post" action="/jlyw/SFGL/wtd/YLComSheetListPrint.jsp" target="PrintFrame" >			
		
		<input type="hidden"  name="YLCommissionSheetList" id="YLCommissionSheetList" />
</form>
<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
   
	</DIV>
</DIV>
</body>
</html>
