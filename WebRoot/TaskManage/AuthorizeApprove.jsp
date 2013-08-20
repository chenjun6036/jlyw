<%@ page contentType="text/html; charset=gb2312" language="java" import="com.jlyw.hibernate.*" errorPage="" %>
<%
	String oRecordId = request.getParameter("OriginalRecordId")==null?"-1":request.getParameter("OriginalRecordId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>原始记录、证书明细</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/waitingDlg.js"></script>
	<script>
	$(function(){
				 
		$('#OriginalRecord').datagrid({
			title:'原始记录信息',
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
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'器具信息',colspan:8,align:'center'},
					{title:'检验信息',colspan:8,align:'center'},
					{title:'费用信息',colspan:7,align:'center'}
				],[
					{field:'ApplianceStandardName',title:'器具标准名称',width:80,sortable:true,align:'center'},
					{field:'Model',title:'规格型号',width:60,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Accuracy',title:'准确度等级',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂',width:80,align:'center'},
					{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
					{field:'ManageCode',title:'管理编号',width:80,align:'center'},
					{field:'Quantity',title:'器具数量',width:60,align:'center'},
					
					{field:'ProjectName',title:'检验项目名称',width:80,align:'center'},
					{field:'Staff',title:'检/校人员',width:80,align:'center'},
					{field:'WorkDate',title:'检/校日期',width:80,align:'center'},
					{field:'CertificateCode',title:'证书编号',width:120,align:'center'},
					{field:'ExcelId',title:'原始记录Excel',width:80,align:'center', 
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.ExcelDoc == ""){
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
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该证书' ><span style='color: #0033FF'>已完成</span></a>"
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
									if(rowData.IsAuthBgRuning==true){
										return "<span style='color: #009933' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过(后台签字执行中...)\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
									}else{
										return "<span style='color: #0033FF' title='批准时间："+rowData.AuthorizeTime+"\r\n批准结果：通过\r\n备注："+rowData.AuthorizeRemark+"'>"+value+"</span>";
									}
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
			rownumbers:true	,
			toolbar:[{
				text:'刷新',
				iconCls:'icon-reload',
				handler:function(){
					$('#OriginalRecord').datagrid('reload');
				}
			},'-',{
				text:'返回上一页',
				iconCls:'icon-undo',
				handler:function(){
					history.go(-1);
				}
			}],
			onLoadSuccess:function(data){
				var oRecordId = <%=oRecordId %>;
				var rowIndex = $(this).datagrid('getRowIndex', oRecordId);
				if(rowIndex != -1){
					$(this).datagrid('selectRow', rowIndex);
				}
			}
		});
		
		doLoadCommissionSheet();	//加载委托单
	});
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
			<jsp:param name="TitleName" value="查看原始记录、证书明细" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

		<form id="SearchForm" method="post" >
		<input id="Code" type="hidden" name="Code" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" style="width:150px;" />
		<input id="Pwd" type="hidden" name="Pwd" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" style="width:150px;" />
		</form>
     <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	 <br/>
	
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;"
				title="原始记录" collapsible="false"  closable="false">
			<table id="OriginalRecord" iconCls="icon-tip" width="1000px" height="350px"></table>
			<br />
		</div>		

</DIV></DIV>
</body>
</html>
