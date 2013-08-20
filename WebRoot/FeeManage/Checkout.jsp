<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>结账管理</title>
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
				var lastIndex;	
				$('#AcountFee').val(0.00);	
				$('#fee').datagrid({
					width:1005,
					height:300,
					title:'原始记录收费信息',
					nowrap: false,
					striped: true,
					rownumbers:true,
					singleSelect:true, 
					fit: false,
					loadMsg:'数据装载中......',
					url:'/jlyw/OriginalRecordServlet.do?method=0',
					//sortName: 'CommissionSheetId',
				  //sortOrder: 'desc',
					remoteSort: false,
					//idField:'CommissionSheetId',
					frozenColumns:[[
						{field:'ck',checkbox:true}
						
					]],
					columns:[[
					//{field:'CommissionSheetId',title:'委托单编号',width:80,align:'center'},
					{field:'OriginalRecordId',title:'原始记录编号',width:100,align:'center'},
					{field:'ApplianceStandardName',title:'受检器具标准名称',width:100,align:'center'},
					{field:'TotalFee',title:'总检验费用',width:100,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}}
					]],
					toolbar:[{
						text:'查看收费明细',
						iconCls:'icon-add',
						handler:function(){
							changecolumn();
						}
					}],
					onLoadSuccess:function(data){
						//TotalFee总费用	
						var rows = $(this).datagrid('getRows');
						var tempTotalFee=0.0,temp1=0.0,temp2=0.0,temp3=0.0,temp4=0.0,temp5=0.0,temp6=0.0;
						for(var i=rows.length-1; i>=0; i--){
	                        temp1=getFloat(temp1)+getFloat(rows[i].TestFee);
							temp2=getFloat(temp2)+getFloat(rows[i].RepairFee);	
							temp3=getFloat(temp3)+getFloat(rows[i].MaterialFee);	
							temp4=getFloat(temp4)+getFloat(rows[i].CarFee);	
							temp5=getFloat(temp5)+getFloat(rows[i].DebugFee);	
							temp6=getFloat(temp6)+getFloat(rows[i].OtherFee);		
							tempTotalFee=getFloat(tempTotalFee)+getFloat(rows[i].TotalFee);		
						}
					   $('#FeeTest').val(temp1);
					   $('#FeeRepair').val(temp2);
					   $('#FeeMaterial').val(temp3);
					   $('#FeeCar').val(temp4);
					   $('#FeeDebug').val(temp5);
					   $('#FeeOther').val(temp6);
					   $('#TotalFee').val(tempTotalFee);
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
							else if(1 == value){
								return "同意折扣申请";
							}
							else return "还未办理";
						}	
					},
					{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onLoadSuccess:function(data){
						//TotalFee总费用	
						var rows = $(this).datagrid('getRows');
						var discountFee=0;
						
						for(var i=rows.length-1; i>=0; i--){
						    if(1 == rows[i].ExecutorResult)
	                       	    discountFee=getFloat(rows[i].NewPrice);
						}
					    if(discountFee==0||discountFee=="0")
					        $('#DiscountFee').val("无有效的折扣申请");
					    else
					        $('#DiscountFee').val(discountFee);
					}
		   });
	    });

		function changecolumn(){
			$('#fee').datagrid({
				columns:[[
					//{field:'CommissionSheetId',title:'委托单编号',width:80,align:'center'},
					{field:'OriginalRecordId',title:'原始记录编号',width:100,align:'center'},
					{field:'ApplianceStandardName',title:'受检器具标准名称',width:100,align:'center'},
					{field:'TestFee',title:'检验费',width:100,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'RepairFee',title:'维修费',width:100,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'MaterialFee',title:'材料费',width:100,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'CarFee',title:'交通费',width:100,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'DebugFee',title:'调试费',width:100,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OtherFee',title:'其他费',width:100,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TotalFee',title:'总检验费用',width:100,align:'left',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}}
				]]
			});
		}
		function acount(){
			if($('#CommissionSheetId').val()==''){
				$.messager.alert('提示！',"委托单无效！",'info');
				return false;
			}
			 $('#FeeTotal').form('submit',{
				url: '/jlyw/CustomerAccountServlet.do?method=3',
				onSubmit:function(){ return $('#FeeTotal').form('validate');},
		   		success:function(data){
			   	   var result = eval("("+data+")");
		   		   //$("#Customer_Balance").val(result.balance);
		   		   //$('#CustomerAccount').datagrid('reload');
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
				$('#fee').datagrid('options').queryParams={'CommissionId':''};
				$('#fee').datagrid('loadData',{total:0,rows:[]});
				
				$('#discount_info_table').datagrid('options').queryParams={'CommissionId':''};
				$('#discount_info_table').datagrid('loadData',{total:0,rows:[]});
				
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
				    if(result.CommissionObj.Status != 3){
				    	$.messager.alert('提示！','当前委托单状态不是“完工确认”，不能进行结账操作！','warning');
				    	$('#btn_checkout').linkbutton({
				    		disabled:true
				    	});
				    }else{
				    	$('#btn_checkout').linkbutton({
				    		disabled:false
				    	});
				    }
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//勾选
					}
					
					//加载该委托单原始记录信息
					$('#fee').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#fee').datagrid('reload');
					
					//加载折扣信息
					$('#discount_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
					$('#discount_info_table').datagrid('reload');
					
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
			<jsp:param name="TitleName" value="结账管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
<div style="+position:relative;" >
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
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
				title="委托单信息" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
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
		
      <div style="width:1005px;height:300px;">
      	
	     <table id="fee" iconCls="icon-tip" ></table>
	  </div>
	  
	  <div id="p2" class="easyui-panel" style="width:1005px;padding:10px;"
				title="操作区" collapsible="false"  closable="false">
			<table id="discount_info_table" iconCls="icon-tip" ></table>
			<form id="FeeTotal" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
			<table width="950px" >
		
				<tr >
				    
					<td width="6%" align="right" style="padding-top:10px;">
						 检测费：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="FeeTest" name="FeeTest" type="text" readonly="readonly"/>元
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						维修费：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="FeeRepair" name="FeeRepair" type="text" readonly="readonly"/>元
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						 材料费：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="FeeMaterial" name="FeeMaterial" type="text" readonly="readonly"/>元
					</td>
				</tr>
				<tr>
					<td width="6%" align="right" style="padding-top:10px;">
						 交通费：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="FeeCar" name="FeeCar" type="text" readonly="readonly"/>元
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						 调试费：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="FeeDebug" name="FeeDebug" type="text" readonly="readonly"/>元
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						 其他费：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="FeeOther" name="FeeOther" type="text" readonly="readonly"/>元
					</td>
				</tr>
				<tr>
				    <td width="6%" align="right" style="padding-top:10px;">
						 总费用：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="TotalFee" name="TotalFee" type="text" readonly="readonly"/>元
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						折扣后费用：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="DiscountFee" class="easyui-validatebox" name="DiscountFee" type="text" readonly="readonly" />
					</td>
					<td width="6%" align="right" style="padding-top:10px;">
						 账户抵扣费用：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="AcountFee" class="easyui-numberbox" name="AcountFee" type="text" />元
					</td>
					
				</tr>
		  </table>
			<table width="950px" >
		
				<tr >
				    
					<td width="6%" align="right" style="padding-top:10px;">
						 发票号：
					</td>
					<td width="25%" align="left" style="padding-top:10px;">
						 
	                     <input id="InvoiceCode" class="easyui-validatebox" name="InvoiceCode" type="text" />
					</td>
					<td  align="center" style="padding-top:10px;padding-left:80px;">
						 
	                     <a id="btn_checkout" class="easyui-linkbutton" iconCls="icon-sum" href="javascript:acount()">结账</a>
					</td>
					
				</tr>
		  </table>
		  </form>
		</div>

</div>


</DIV></DIV>
</body>
</html>
