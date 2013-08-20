<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>查看委托单信息</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
   <!-- <script type="text/javascript" src="../JScript/upload.js"></script>-->
	<script>
	$(function(){
		//$('#subcontract').hide();
		$('#OriginalRecord').datagrid({
			title:'原始记录信息',
			height:250,
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
			/*frozenColumns:[[
				{field:'ck',checkbox:true}
			]],*/
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
			width:980,
			height:200,
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
			onSelect:function(rowIndex, rowData){
				//更新附件表格文件信息
				$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.Attachment};
				$('#uploaded_file_table').datagrid('reload');
			},
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#subcontract').panel('open');
					$('#subcontract_info_table').datagrid('selectRow', 0);
				}
			}
		});
		$('#withdraw_info_table').datagrid({
			title:'退样信息',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/WithdrawServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'申请信息',colspan:5,align:'center'},
					{title:'办理信息',colspan:6,align:'center'}
				],[
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'WithdrawNumber',title:'退样数量',width:60,align:'center'},
					{field:'Reason',title:'退样原因',width:80,align:'center'},
					{field:'WithdrawDesc',title:'退样样品描述',width:80,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
					
					
					{field:'ExecutorName',title:'办理人',width:80,align:'center'},
					{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
					{field:'ExecutorResult',title:'办理结果',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">驳回</span>';
							}
							if(1 == value){
								return "同意退样";
							}
							return "";
						}	
					},
					{field:'WithdrawDate',title:'退样日期',width:80,align:'center'},
					{field:'Location',title:'样品存放位置',width:80,align:'center'},
					{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#withdraw').panel('open');
				}
			}
		});
		
		$('#overdue_info_table').datagrid({
			title:'超期信息',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/OverdueServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'申请信息',colspan:4,align:'center'},
					{title:'办理信息',colspan:4,align:'center'}
				],[
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'DelayDays',title:'延期天数',width:60,align:'center'},
					{field:'Reason',title:'超期原因',width:80,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:80,align:'center'},
					
					{field:'ExecutorName',title:'办理人',width:80,align:'center'},
					{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
					{field:'ExecutorResult',title:'办理结果',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">驳回</span>';
							}
							if(1 == value){
								return "同意超期";
							}
							return "";
						}	
					},
					{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true		,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#overdue').panel('open');
				}
			}
		});
		
		$('#discount_info_table').datagrid({
			title:'折扣申请信息',
//			iconCls:'icon-save',
			width:950,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/DiscountServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			//idField:'userid',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'申请信息',colspan:8,align:'center'},
					{title:'办理信息',colspan:4,align:'center'}
				],[
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'DiscountAmount',title:'折扣额度',width:60,align:'center'},
					{field:'OldPrice',title:'原价',width:80,align:'center'},
					{field:'NewPrice',title:'现价',width:80,align:'center'},
					{field:'Reason',title:'申请原因',width:120,align:'center'},
					{field:'RequesterTime',title:'申请时间',width:150,align:'center'},
					{field:'Contector',title:'委托方经办人',width:90,align:'center'},
					{field:'ContectorTel',title:'经办人电话',width:90,align:'center'},
					
					{field:'ExecutorName',title:'办理人',width:80,align:'center'},
					{field:'ExecuteTime',title:'办理时间',width:80,align:'center'},
					{field:'ExecutorResult',title:'办理结果',width:90,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">驳回</span>';
							}
							if(1 == value){
								return "同意折扣申请";
							}
							return "";
						}	
					},
					{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$('#discount').panel('open');
				}
			}
		});
		
		$('#uploaded_file_table').datagrid({
			title:'转包业务已上传的文件',			
			iconCls:'icon-tip',
			idField:'_id',
			width:980,
			height:200,
			//closed:true,
			//fit:true,
			singleSelect:true,
			rownumbers:true,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[[
				{field:"filename",title:"文件名",width:130,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='点击下载该文件' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				{field:"length",title:"大小",width:60,align:"left"},
				{field:"uploadDate",title:"上传时间",width:120,align:"left"},
				{field:"uploadername",title:"上传人",width:60,align:"left"}
			]]
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
				
				$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#subcontract_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$('#discount_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#discount_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#overdue_info_table').datagrid('loadData',{total:0,rows:[]});
				
				$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#withdraw_info_table').datagrid('loadData',{total:0,rows:[]});				
				
				//清空附件列表
				$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
				$('#uploaded_file_table').datagrid('reload');
				
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
					
					//加载转包信息
					$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#subcontract_info_table').datagrid('reload');
					
					//加载折扣信息
					$('#discount_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#discount_info_table').datagrid('reload');
					
					//加载任务分配信息
					$('#overdue_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#overdue_info_table').datagrid('reload');
					
					//加载任务分配信息
					$('#withdraw_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#withdraw_info_table').datagrid('reload');					
										
				}else{
					$.messager.alert('查询失败！',result.msg,'error');
				}
			}
		});  
	}
	function back(){
		history.go(-1);
	}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="查看委托单信息" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">

		<form id="SearchForm" method="post" >
		<input id="Code" type="hidden" name="Code" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" style="width:150px;" />
		<input id="Pwd" type="hidden" name="Pwd" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" style="width:150px;" />
		</form>
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:10px;"
				title="委托单信息" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<table width="1000px" id="table1">
				<tr>

				  <td width="77" align="right">委托形式：</td>
				  <td width="185"  align="left">
						<select name="CommissionType" style="width:152px" class="easyui-combobox">
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
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" readonly="readonly"/></td>
                <td width="64" align="right">电&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text" readonly="readonly"/></td>
				<td width="64" align="right">地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" readonly="readonly"/></td>

				<td width="65" align="right">邮政编码：</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" readonly="readonly"/></td>
			</tr>
			<tr>
				<td align="right">联&nbsp;系&nbsp;人：</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" readonly="readonly"/></td>
				<td align="right">手机号码：</td>

				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" readonly="readonly"/></td>
				<td align="right">证书单位：</td>
				<td align="left"><input name="SampleFrom" id="SampleFrom" type="text" readonly="readonly"/></td>
                <td align="right">开票单位：</td>
				<td align="left"><input name="BillingTo" id="BillingTo" type="text" readonly="readonly"/></td>
			</tr>
		</table><br/>
 		 <table id="table2" width="1000">

			<tr>
				<td width="77" align="right">器具名称：</td>
                <td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" readonly="readonly"/></td>
                <td width="64" align="right">型号规格：</td>
			  <td width="171"  align="left"><input id="Model" name="Model" type="text" readonly="readonly"/></td>
				<td width="64" align="right">出厂编号：</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" readonly="readonly"/></td>

				<td width="65" align="right">管理编号：</td>
			  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" readonly="readonly"/></td>
			</tr>
			<tr>
				<td align="right">制 造 厂：</td>
				<td align="left"><input id="Manufacturer" name="Manufacturer" type="text" readonly="readonly"/></td>
				<td align="right">数&nbsp;&nbsp;&nbsp;&nbsp;量：</td>

				<td align="left"><input id="Quantity" name="Quantity" type="text" readonly="readonly"/>件</td>
				<td align="right">是否强检：</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px" class="easyui-combobox" disabled="disabled">
						<option selected="selected" value="1" >非强制检定</option>
						<option value="0">强制检定</option>
					</select>

				</td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox" disabled="disabled"/>加&nbsp;&nbsp;急</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">外观附件：</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text" readonly="readonly"/></td>

				<td align="right">其他要求：</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text" readonly="readonly"/></td>
				<td align="right"></td>
				<td align="left"></td>
                <td align="left"></td>
				<td align="left">&nbsp;</td>
			</tr>
	 	 </table><br/>

		</form>
		</div><br/>
	
			<table id="OriginalRecord" iconCls="icon-tip" width="1005px" height="250px"></table>
            <br/>
             <div id="subcontract" class="easyui-panel" style="width:1005px;padding:10px;" title="转包业务信息" closed="true">
                <table id="subcontract_info_table" iconCls="icon-tip"></table>
                <br />
                <table id="uploaded_file_table" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104" iconCls="icon-tip"></table>
             </div>
            <br/>
              <div id="withdraw" class="easyui-panel" style="width:1005px;padding:10px;" title="退样信息" closed="true">
                <table id="withdraw_info_table" iconCls="icon-tip"></table>
             </div>
            <br/>             
              <div id="overdue" class="easyui-panel" style="width:1005px;padding:10px;" title="超期信息" closed="true">
                <table id="overdue_info_table" iconCls="icon-tip"></table>
             </div>
            <br/>             
              <div id="discount" class="easyui-panel" style="width:1005px;padding:10px;" title="折扣信息" closed="true">
                <table id="discount_info_table" iconCls="icon-tip"></table>
             </div>
            <table width="1005px" id="table1">
				<tr height="50px">
					<td width="25%" align="right"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="back()">返回</a></td>
				</tr >
		</table>
		  <form id="downloadfile-submit-form" method="post" target="_blank" action="/jlyw/FileDownloadServlet.do?method=3">
			<input type="hidden" name="TemplateFileId" id="downloadfile-submit-form-TemplateFileId" value="" />
			<input type="hidden" name="OriginalRecordId" id="downloadfile-submit-form-OriginalRecordId" value="" />
			<input type="hidden" name="DownloadType" id="downloadfile-submit-form-DownloadType" value="0" />
		  </form>

</DIV>
</DIV>
</body>
</html>
