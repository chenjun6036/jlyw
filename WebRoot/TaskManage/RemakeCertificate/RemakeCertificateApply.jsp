<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>重新编制证书</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	$(function(){
		$('#remake_certificate_info_table').datagrid({
			title:'重新编制证书信息',
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
			url:'/jlyw/RemakeCertificateServlet.do?method=2',
			remoteSort: false,
			idField:'Id',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'任务创建信息',colspan:4,align:'center'},
					{title:'证书编制信息',colspan:3,align:'center'},
					{title:'完成情况审核',colspan:1,align:'center'}
				],[
					{field:'CertificateCode',title:'原证书编号',width:120,sortable:true,align:'center'},
					{field:'CreatorName',title:'创建人姓名',width:100,align:'center'},
					{field:'CreateTime',title:'创建时间',width:80,align:'center'},
					{field:'CreateRemark',title:'备注',width:150,align:'center'},
					
					
					{field:'ReceiverName',title:'编制人',width:100,align:'center'},
					{field:'FinishTime',title:'完成时间',width:80,align:'center'},
					{field:'FinishRemark',title:'备注',width:150,align:'center'},
					
					{field:'PassedTime',title:'通过时间',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	
		});
		$('#OriginalRecord').datagrid({
			title:'原始记录信息',
			height:250,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
			url:'/jlyw/OriginalRecordServlet.do?method=0',
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
					{field:'CertificateId',title:'证书文件',width:120,align:'center',
						formatter:function(value, rowData, rowIndex){
							if(value=="" || rowData.CertificateDoc==""){
								return "";
							}else{
								if(rowData.CertificatePdf == ""){
									return "<span style='color: #FF0000'>未完成</span>";
								}else{
									return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData.CertificatePdf+"&FileType=102' target='_blank' title='点击下载该原始记录' ><span style='color: #0033FF'>"+rowData.CertificateCode+"</span></a>"
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
	});
	function doLoadCommissionSheet(){	//查找委托单
		$("#SearchForm").form('submit', {
			url:'/jlyw/RemakeCertificateServlet.do?method=1',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#remake_certificate_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#remake_certificate_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':''};
				$('#OriginalRecord').datagrid('loadData',{total:0,rows:[]});
				
				$("#OriginalRecordId").val('');				
				//$("#Ness").removeAttr("checked");	//去勾选
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					if(result.CommissionObj.CommissionStatus < 3){
						$.messager.alert('提示！',"该证书所属的委托单尚未完工确认，不能进行重新编制证书申请！",'info');
						return false;
					}
					if(result.CommissionObj.CommissionStatus == 10){
						$.messager.alert('提示！',"该证书所属的委托单已注销，不能进行重新编制证书申请！",'info');
						return false;
					}
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载任务分配信息
					$('#remake_certificate_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#remake_certificate_info_table').datagrid('reload');
					
					//加载原始记录信息
					$('#OriginalRecord').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#OriginalRecord').datagrid('reload');
					
					$("#OriginalRecordId").val(result.OriginalRecordId);
					$("#remake-certificate-submit-form-CertificateCode").val($('#CertificateCode').val());
					
					
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	function doSubmitRemakeCertificateForm(){   	//提交申请表单
		$("#remake-certificate-submit-form").form('submit', {
			url:'/jlyw/RemakeCertificateServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//判断选择的检验项目是否有效
				var projectValue = $('#OriginalRecordId').val();
				if(projectValue==''){
					$.messager.alert('提示！',"请输入一个有效的证书编号！",'info');
					return false;
				}
				return $("#remake-certificate-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//重新加载任务分配信息
					$('#remake_certificate_info_table').datagrid('reload');
					
					$('CreateRemark').val('');
				}else{
					$.messager.alert('提交失败！',result.msg,'error');
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
			<jsp:param name="TitleName" value="新增‘重新编制证书（完工确认后）’任务" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="证书查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >证书编号：</td>
					<td width="22%" align="left" >
						<input id="CertificateCode" class="easyui-validatebox" name="CertificateCode" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right"></td>
					<td width="22%" align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>
					<td width="25%"  align="center"></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
		<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br/>
		<table id="OriginalRecord" iconCls="icon-tip" width="1005px" height="250px"></table>
		<br />
	
		<div id="p2" class="easyui-panel" style="width:1005px;height:470px;padding:10px;"
				title="新增‘重新编制证书’任务" collapsible="false"  closable="false">
			<table id="remake_certificate_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="remake-certificate-submit-form" method="post">
			<input type="hidden" name="OriginalRecordId" id="OriginalRecordId" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="8%" style="padding-top:15px;" align="right" >
					  证书编号：					</td>
					
					<td width="21%" style="padding-top:15px;" align="left"><input id="remake-certificate-submit-form-CertificateCode"   name="CertificateCode" class="easyui-validatebox" style="width:152px;" required="true" readonly="readonly"></td>
					<td width="9%" style="padding-top:15px;" align="right" >备注信息：</td>
					<td rowspan="3" align="left" style="padding-top:15px;"><textarea id="CreateRemark" style="width:350px;height:80px"  name="CreateRemark" class="easyui-validatebox" required="true" ></textarea></td>
			    </tr>
				<tr>
					<td width="8%" style="padding-top:15px;" align="right"></td>
					<td width="21%" style="padding-top:15px;" align="left"></td>
					<td align="right"></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" colspan="3"  align="right"  style="padding-top:15px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitRemakeCertificateForm()">确认添加</a></td>
				  <td  align="left" style="padding-top:15px;"><!-- <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">重置</a>--></td>
			  </tr>
		  </table>
		  </form>
		</div>

</DIV></DIV>
</body>
</html>
