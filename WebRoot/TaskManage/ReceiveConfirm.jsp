<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>客户取样</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	 <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
		$('#OriginalRecord').datagrid({
			title:'原始记录信息',
			width:1005,
			height:350,
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
			
			columns:[
				[
					{title:'器具信息',colspan:7,align:'center'},
					{title:'检验信息',colspan:7,align:'center'},
					{title:'费用信息',colspan:7,align:'center'}
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
					{field:'TotalFee',title:'总计费用',width:60,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true
		});
		
		$('#subcontract_info_table').datagrid({
			title:'转包业务信息',
//			iconCls:'icon-save',
			width:1005,
			height:300,
			//closed:true,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/SubContractServlet.do?method=3',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			idField:'SubContractId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'转包方信息',colspan:3,align:'center'},
					{title:'转包业务信息',colspan:7,align:'center'}
				],[
					{field:'SubContractorName',title:'转包方',width:80,sortable:true,align:'center'},
					{field:'SubContractorContactor',title:'联系人',width:60,align:'center'},
					{field:'SubContractorContactorTel',title:'联系电话',width:80,align:'center'},
					{field:'SubContractDate',title:'转包时间',width:80,align:'center'},
					{field:'Handler',title:'转包人',width:80,align:'center'},
					{field:'ReceiveDate',title:'接收时间',width:80,align:'center'},
					{field:'Receiver',title:'接收人',width:80,align:'center'},
					{field:'Remark',title:'备注信息',width:100,align:'center'},
					{field:'LastEditor',title:'最后编辑人',width:80,align:'center'},
					{field:'LastEditTime',title:'最后编辑时间',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					
					$('#subcontract_info_table').datagrid('selectRow', 0);
				}
			}
		});
				
		});
		
		function doConfirm(){
			if($('#CommissionSheetId').val()==''){
				$.messager.alert('提示！',"委托单无效！",'info');
				return false;
			}
	
		    var result = confirm("您确定要进行客户取样吗?");
			if(result == false){
				return false;
			}
			 $('#Confirm').form('submit',{
				url: '/jlyw/CommissionSheetServlet.do?method=7',
				onSubmit:function(){ return $('#Confirm').form('validate');},
		   		success:function(data){
			   	   var result = eval("("+data+")");
		   		   
		   		   alert(result.msg);
		   		 }
			});
		}
		function doLoadCommissionSheet(){	//查找委托单
			
			$("#SearchForm").form('submit', {
				url:'/jlyw/CommissionSheetServlet.do?method=3',
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					$("#CommissionSheetForm").form('clear');
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
					$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
					
					if($('#Code').val()=='' || $('#Pwd').val() == ''){
						$.messager.alert('提示！',"委托单无效！",'info');
						return false;
					}
			
					$("#CommissionSheetId").val('');				
					$("#Ness").removeAttr("checked");	//去勾选
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
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#OriginalRecord').datagrid('reload');
					
					//加载转包信息
					$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#subcontract_info_table').datagrid('reload');
						
					$("#CommissionSheetId").val(result.CommissionObj.CommissionId);    //委托单ID					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
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
			<jsp:param name="TitleName" value="客户取样" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

      <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
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
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a>&nbsp;&nbsp;<a id="btn_confirm" class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="doConfirm()" >取样确认</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
    	<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br/>
      <table id="OriginalRecord" iconCls="icon-tip"  ></table>
     
      <table id="subcontract_info_table" iconCls="icon-tip"></table>
      
		<form id="Confirm" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
	    </form>


</DIV></DIV>
</body>
</html>
