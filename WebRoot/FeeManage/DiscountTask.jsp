<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>折扣审批列表</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/StatusInfo.js"></script>
	<script>
	$(function(){	
		$("#CustomerName").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
				$("#CustomerTel").val(record.tel);
				$("#CustomerAddress").val(record.address);
				$("#CustomerZipCode").val(record.zipCode);
				$("#ContactPerson").val(record.contactor);
				$("#ContactorTel").val(record.contactorTel);
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
				$(this).combobox('reload','/jlyw/CustomerServlet.do?method=5&CustomerName='+newValue);
			}
		});		
			$('#task-table').datagrid({
				title:'待审批的折扣申请列表',
				singleSelect:true, 
				fit: true,
                nowrap: false,
                striped: true,
				//url:'/jlyw/DiscountServlet.do?method=3&DetailListId=""',
				sortName: 'RequesterTime',
				sortOrder: 'desc',
				remoteSort: false,
				idField:'DiscountId',
				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'Id',title:'折扣编号',width:60,align:'center'}
				]],
				columns:[[
					{field:'CustomerName',title:'申请单位',width:160,sortable:true,align:'center'},
					{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
					{field:'Reason',title:'申请原因',width:150,align:'center'},
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
						}
				
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#task-table-search-toolbar",
				onClickRow:function(rowIndex, rowData)
				{
					var Id=rowData.Id;
					
					clickname="此次折扣编号为'"+Id+"' 的委托单信息";
					$('#table6').datagrid({title:clickname});
					$('#table6').datagrid('options').url='/jlyw/DiscountServlet.do?method=6';
					$('#table6').datagrid('options').queryParams={'DiscountId':Id};
					$('#table6').datagrid('reload');
				},
				onLoadSuccess:function(data){
					if(data.rows.length > 0){
						$(this).datagrid('selectRow', 0);
					}
				}
			});
			
			$('#table6').datagrid({
				width:1000,
				height:350,
				
				title:'折扣对应的委托单信息',
				singleSelect:false, 
				nowrap: false,
				striped: true,
		//				collapsible:true,
				url:'/jlyw/DiscountServlet.do?method=6',
				remoteSort: false,
				idField:'Id',
				showFooter:true,
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'Status',title:'委托单状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'OldTotalFee',title:'总费用原价',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TotalFee',title:'总费用折扣',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldTestFee',title:'检验费原价',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'TestFee',title:'检验费折扣',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldRepairFee',title:'维修费原价',width:60,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
					}},
					{field:'RepairFee',title:'维修费折扣',width:60,align:'right',formatter:function(val,rec){
						
							return '<span style="color:red;">'+val+'</span>';
					}},
					{field:'OldMaterialFee',title:'材料费原价',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'MaterialFee',title:'材料费折扣',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldCarFee',title:'交通费原价',width:60,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'CarFee',title:'交通费折扣',width:60,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldDebugFee',title:'调试费原价',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'DebugFee',title:'调试费折扣',width:60,align:'right',formatter:function(val,rec){
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OldOtherFee',title:'其他费原价',width:60,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}},
					{field:'OtherFee',title:'其他费折扣',width:60,align:'right',formatter:function(val,rec){
							
								return '<span style="color:red;">'+val+'</span>';
						}}
					
					
					
				]],
				pagination:false,
				rownumbers:true
			});
			$('#overdue_info_table').datagrid({
				title:'折扣信息',
	//			iconCls:'icon-save',
	//			width:900,
				height:200,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
	//			collapsible:true,
				//url:'/jlyw/DiscountServlet.do?method=0',
	//			sortName: 'userid',
	// 			sortOrder: 'desc',
				remoteSort: false,
				idField:'Id',
					frozenColumns:[[
						{field:'ck',checkbox:true}
						
					]],
					columns:[[
						{title:'折扣办理信息',colspan:5,align:'center'},
						{title:'折扣申请信息',colspan:6,align:'center'}
						
					],[
						{field:'Id',title:'折扣编号',width:40,align:'center'},
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
						
						{field:'ExecuteMsg',title:'备注信息',width:80,align:'center'},
						{field:'CustomerName',title:'申请单位',width:120,sortable:true,align:'center'},
						{field:'RequesterName',title:'申请人',width:80,sortable:true,align:'center'},
						{field:'Reason',title:'申请原因',width:150,align:'center'},
						{field:'RequesterTime',title:'申请时间',width:150,align:'center'},
						{field:'Contector',title:'委托方经办人',width:90,align:'center'},
						{field:'ContectorTel',title:'经办人电话',width:90,align:'center'}
					]],
				pagination:false,
				rownumbers:true	
				
			});
			doSearchTaskList();
		});
		function doTask()
		{
			var selectedRow = $('#task-table').datagrid('getSelected');
			if(selectedRow == null){
				$.messager.alert('提示！',"请选择一条折扣申请记录！",'info');
				return false;
			}
			
			$('#DiscountId').val(selectedRow.Id);
			//$('#DiscountForm').submit();
			
			$('#approve-window').window('open');
			doLoadDiscount();
		}
		function doSearchTaskList()
		{
			$('#task-table').datagrid('options').url='/jlyw/DiscountServlet.do?method=3';
			$('#task-table').datagrid('options').queryParams={'CustomerName':encodeURI($('#CustomerName').combobox('getValue')),'Code':$('#Code').val(),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue')),'Status':$('#Status').val()};
			$('#task-table').datagrid('reload');
		}
		
		function doLoadDiscount(){	//查找委托单
			
			$('#overdue_info_table').datagrid('options').url='/jlyw/DiscountServlet.do?method=0';
		
			$('#overdue_info_table').datagrid('options').queryParams={'DiscountId':$('#DiscountId').val()};
			$('#overdue_info_table').datagrid('reload');
		}
		function doSubmitDiscount(ExecutorResult){   	//提交折扣审批表单
			$('#ExecutorResult').val(ExecutorResult);
			$("#overdue-submit-form").form('submit', {
				url:'/jlyw/DiscountServlet.do?method=1',
				onSubmit: function(){
						// do some check
						// return false to prevent submit;
					//判断选择的检验项目是否有效
					var DiscountIdValue = $('#DiscountId').val();
					if(DiscountIdValue==''){
						$.messager.alert('提示！',"折扣申请不存在！",'info');
						return false;
					}
					return $("#overdue-submit-form").form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					if(result.IsOK){
						//重新加载任务分配信息
						//$('#overdue_info_table').datagrid('options').queryParams={'DiscountId':$('#DiscountId').val()};
						$('#overdue_info_table').datagrid('reload');
						
						//清空退样申请表单的“备注”信息
						$('#ExecuteMsg').val('');
						$('#task-table').datagrid('reload');
						
						$.messager.alert('提示！','审批成功','info');
						$('#approve-window').window('close');
						
					}else{
						$.messager.alert('提交失败！',result.msg,'error');
					}
				}
			});  
	   }  
	   function printDiscount(){
			var row = $('#task-table').datagrid('getSelected');
			if(row.ExecuteTime.length >0&&row.ExecutorResult==1){
				$('#discountId').val(row.Id)
				//console.info(row.Id);
				$('#formLook').submit();
			}
			else{
				
				$.messager.alert('提示','折扣单还未审批或审批未通过！','error');
			}
	   }
	</script>
</head>

<body>

 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="折扣审批管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div id="p" class="easyui-panel" style="width:1000px;height:120px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="950px" id="table1">
				<tr >				
					<td align="left" width="30%">
					委托单位：<select name="CustomerName" id="CustomerName" style="width:180px"></select>
					</td>
					<td width="30%" align="left">
					委托单编号：
					<input id="Code" class="easyui-validatebox" name="Code" style="width:120px;"></input>
					</td>
					<td width="30%" align="left">
					折扣申请状态：<select name="Status" id="Status" style="width:100px"><option value="">全部申请</option><<option value="0" selected="selected">未审核申请</option><option value="1">已审核申请</option></select>
					</td>
					
				</tr >
				<tr >				
					<td  align="left" width="30%">
					起始时间：<input name="DateFrom" id="DateFrom" type="text" style="width:180px" class="easyui-datebox" />
					</td>
					<td width="30%" align="left">
					&nbsp;&nbsp;结束时间：&nbsp;<input name="DateEnd" id="DateEnd" type="text" style="width:125px;" class="easyui-datebox" />
					</td>
					<td width="30%" align="left">
				
                    <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doSearchTaskList()">查询折扣申请</a>
					</td>
					
				</tr >
		</table>
		</form>
	</div> 
	<div  style="width:1000px;height:300px;">
		<table width="98%" height="98%" id="task-table" iconCls="icon-search" >
		  </table>
	</div>
	<table id="table6" style="height:350px; width:1000px"><!--委托单信息-->
	</table>
	<!--<form method="post" action="/jlyw/FeeManage/DiscountApprove.jsp" id="DiscountForm">
		<input type="hidden" name="DiscountId" id="DiscountId" value="" />
	</form>-->
	<div id="task-table-search-toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="doTask()">审批</a>
						<a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" plain="true" onClick="printDiscount()">打印折扣单</a>
					</td>
					
			</tr>
		</table>
	</div>
	<div id="approve-window" class="easyui-window" modal="true" title="折扣审批" style="padding: 10px;" iconCls="icon-ok" closed="true" maximizable="false" minimizable="false" collapsible="false">
		<table id="overdue_info_table" iconCls="icon-tip" width="800px" height="150px"></table>
			<br />
			<form id="overdue-submit-form" method="post">
			<input type="hidden" name="DiscountId" id="DiscountId" />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<table width="700" style="margin-left:20px;" >
				<tr>
					<td width="50%" align="right">备注信息：</td>
					<td align="left"><textarea id="ExecuteMsg" style="width:350px;height:80px"  name="ExecuteMsg" class="easyui-validatebox"></textarea></td>
				</tr>
				
				<tr style="padding-top:15px" >
				  <td height="52%" align="center" > <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitDiscount(1)">同意申请</a></td>
				  <td width="10%"  align="center" ><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitDiscount(0)">驳回</a></td>
			      <td width="30%"  align="left" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="$('#approve-window').window('close');">关闭</a></td>
			  </tr>
			   
			</table>
		 </form>
	<form id="formLook" method="post" action="/jlyw/DiscountServlet.do?method=7" target="FeePrintFrame" >			
		<input type="hidden" name="discountId" id="discountId" />
	</form>
	<iframe id="FeePrintFrame" name="FeePrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
	
	</div>

</DIV></DIV>
</body>
</html>
