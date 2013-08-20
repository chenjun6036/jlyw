<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>委托单查询</title>
   <link rel="stylesheet" type="text/css"
			href="../../Inc/Style/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="../../Inc/Style/themes/icon2.css" />
		<link href="../../BtnStyle/css/theme.css" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />

		<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="../../WebPrint/LodopFuncs.js"></script>
		<script type="text/javascript" src="../../WebPrint/printer.js"></script>
		<script type="text/javascript" src="../../WebPrint/printCommisionSheet.js"></script>
		<script>
		$(function(){
/*			$('#OriginalRecord').datagrid({
			title:'原始记录信息',
			height:300,
			width:1005,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			idField:'OriginalRecordId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'器具信息',colspan:7,align:'center'},
					{title:'检验信息',colspan:7,align:'center'},
					{title:'费用信息',colspan:7,align:'center'},
					{title:'工作环境',colspan:5,align:'center'},
					{title:'其他信息',colspan:5,align:'center'}
				],[
					{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
					{field:'Model',title:'规格型号',width:60,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
					{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
					{field:'ManageCode',title:'管理编号',width:80,align:'center'},
					
					{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
					{field:'Staff',title:'检/校人员',width:80,align:'center'},
					{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
					{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value==""){
								return "";
							}else{
								if(rowData.ExcelPdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.ExcelPdf+"&FileType=101' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
								}
							}
						}},
					{field:'CertificateId',title:'证书文件',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.CertificateDoc==""){
								return "";
							}else{
								if(rowData.CertificatePdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>已完成</span></a>"
								}
							}
						}
					},
					{field:'VerifierName',title:'核验人',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.VerifyResult == ""){		//尚未审批
									return "<span title='尚未核验'>"+value+"</span>";
								}
								else if(rowData.VerifyResult == "1" || rowData.VerifyResult == 1 ){ //核验通过
									return "<span style='color: #0033FF' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
								}else{	//核验未通过
									return "<span style='color: #FF0000' title='核验时间："+rowData.VerifyTime+"\r\n核验结果：未通过\r\n备注："+rowData.VerifyRemark+"'>"+value+"</span>";
								}
							}
						}
					},
					{field:'AuthorizerName',title:'批准人',width:80,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.VerifyAndAuthorizeExcelId != rowData.ExcelId || rowData.VerifyAndAuthorizeCertificateId != rowData.CertificateId){
								return "";
							}else{
								if(rowData.AuthorizeResult == ""){		//尚未审批
									return "<span title='尚未签字'>"+value+"</span>";
								}
								else if(rowData.AuthorizeResult == "1" || rowData.AuthorizeResult == 1 ){ //批准通过
									return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}else{	//批准未通过
									return "<span style='color: #FF0000' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：未通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
								}
							}
						}
					},
					
					{field:'TestFee',title:'检测费',width:60,align:'center'},
					{field:'RepairFee',title:'修理费',width:60,align:'center'},
					{field:'MaterialFee',title:'材料费',width:60,align:'center'},
					{field:'CarFee',title:'交通费',width:60,align:'center'},
					{field:'DebugFee',title:'调试费',width:60,align:'center'},
					{field:'OtherFee',title:'其他费用',width:60,align:'center'},
					{field:'TotalFee',title:'总计费用',width:60,align:'center'},
					
					{field:'WorkType',title:'工作性质',width:60,align:'center'}, 
					{field:'WorkLocation',title:'工作地点',width:60,align:'center'},
					{field:'Temp',title:'温度',width:60,align:'center'},
					{field:'Humidity',title:'相对湿度',width:60,align:'center'},
					{field:'Pressure',title:'大气压',width:60,align:'center'},
					
					{field:'Validity',title:'有效期',width:80,align:'center'},
					{field:'TechnicalDocs',title:'依据技术文档',width:100,align:'center'},
					{field:'Standards',title:'计量标准',width:100,align:'center'},
					{field:'StandardAppliances',title:'主要标准器具',width:100,align:'center'},
					{field:'Status',title:'状态',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else 
							{
								rowData['Status']=1;
								return "无效";
							}
							
						}} 

					
				]
			],
			pagination:false,
			rownumbers:true,
			toolbar:"#table-search-toolbar"	
		});*/
		})
		function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=6',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				//$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
				////$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
				
				if($('#Code').val()=='' || $('#Pwd').val() == ''){
					$.messager.alert('提示！',"委托单无效！",'info');
					return false;
				}
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载原始记录信息
					//$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					//$('#OriginalRecord').datagrid('reload');
					
					//hidden字段
					//$('#TheCommissionId').val($('#CommissionId').val());
					$('#CommissionPrintObj').val(data);
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	function doPrintCommissionSheet(){
		if($('#Code').val()=='' || $('#Pwd').val() == ''){
			$.messager.alert('提示！',"委托单无效！",'info');
			return false;
		}
		var result = eval("("+$('#CommissionPrintObj').val()+")");
		Preview1(result.PrintObj);
		
	}
	</script>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object> 	
</head>

<body>
 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


<div  >
     <div id="p" class="easyui-panel" style="width:1005px;height:100px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right">密  码：</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-print" name="print" onclick="doPrintCommissionSheet()" title="打印委托单">打印委托单</a></td>
					
				</tr >
		</table>
		</form>
		</div>
		<br/>
		<br/>
 <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
				title="委托单信息" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<input type="hidden" id="CommissionPrintObj" name="CommissionPrintObj" value="" />
			<table width="1000px" id="table1">
				<tr>

				  <td width="77" align="right">委托形式：</td>
				  <td width="185"  align="left">
						<select name="CommissionType" style="width:152px">
							<option value="1">送样检测</option>
							<option value="5">其它业务</option>
							<option value="6">自检业务</option>
							<option value="7">现场带回</option>
							<option value="3">公正计量</option>
							<option value="4">形式评价</option>
							<option value="2">现场检测</option>
						</select>
				  </td>
				  <td width="68" align="right">委托日期：</td>
				  <td width="650"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>

			  <td width="77"  align="right">委托单位：</td>
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" style="width:152px;" /></td>
                <td width="64" align="right">电&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text"></td>
				<td width="64" align="right">地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text"></td>

				<td width="65" align="right">邮政编码：</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text"></td>
			</tr>
			<tr>
				<td align="right">联&nbsp;系&nbsp;人：</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" /></td>
				<td align="right">手机号码：</td>

				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">证书单位：</td>
				<td align="left"><input name="SampleFrom" id="SampleFrom" type="text" /></td>
                <td align="right">开票单位：</td>
				<td align="left"><input name="BillingTo" id="BillingTo" type="text" /></td>
			</tr>
		</table><br/>
 		 <table id="table2" width="1000">

			<tr>
				<td width="77" align="right">器具名称：</td>
                <td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" /></td>
                <td width="64" align="right">型号规格：</td>
			  <td width="171"  align="left"><input id="Model" name="Model" type="text" /></td>
				<td width="64" align="right">出厂编号：</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>

				<td width="65" align="right">管理编号：</td>
			  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">制 造 厂：</td>
				<td align="left"><input id="Manufacturer" name="Manufacturer" type="text"  /></td>
				<td align="right">数&nbsp;&nbsp;&nbsp;&nbsp;量：</td>

				<td align="left"><input id="Quantity" name="Quantity" type="text"/>件</td>
				<td align="right">是否强检：</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option selected="selected" value="1" >非强制检定</option>
						<option value="0">强制检定</option>
					</select>

				</td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox" />加&nbsp;&nbsp;急</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">外观附件：</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text"  /></td>

				<td align="right">其他要求：</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text"  /></td>
				<td align="right"></td>
				<td align="left"></td>
                <td align="left"></td>
				<td align="left">&nbsp;</td>
			</tr>
	 	 </table><br/>

		</form>
		</div>
	  
	
</div>




</DIV></DIV>



</body>
</html>
