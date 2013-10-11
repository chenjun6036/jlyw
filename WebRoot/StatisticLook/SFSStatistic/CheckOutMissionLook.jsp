<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,com.jlyw.hibernate.SysUser" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>完工业务查询</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
		$(function(){
			
			nowDate = new Date();
			$("#DateFrom").datetimebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate())+' '+'0:0:0');
			$("#DateEnd").datetimebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate())+' '+'23:59:59');
			
			$('#Employee').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
				}
			});
		});
	
		$(function(){
			$('#details').datagrid({
			    width:1000,
				height:500,
				title:'结账清单列表',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: false,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				//sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				showFooter:true,
				//idField:'id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'清单号',width:100,align:'center',sortable:true},
					{field:'TotalFee',title:'总计费用',width:180,align:'center'},
					{field:'PaidFee',title:'已付费用',width:80,align:'center'},
					{field:'Cash',title:'现金',width:80,align:'center'},
					{field:'Cheque',title:'支票',width:80,align:'center'},
					{field:'Account',title:'账户预付款',width:80,align:'center'},
					{field:'CheckOutStaff',title:'结账人',width:80,align:'center'},
					{field:'CheckOutDate',title:'结账时间',width:80,align:'center'},
					{field:'InvoiceCode',title:'支票号',width:70,align:'center'}
                ]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'打印',
					iconCls:'icon-print',
					handler:function(){
						var emp = $('#Employee').combobox('getValue');
						if(!emp.match('^[0-9]*$')){
							$.messager.alert('提示','请确认员工输入是否有误！','warning');
							return;
						}
						$('#Employee1').val(encodeURI($('#Employee').combobox('getValue')));
						$('#DateFrom1').val(encodeURI($('#DateFrom').datebox('getValue')));
						$('#DateEnd1').val(encodeURI($('#DateEnd').datebox('getValue')));
						
						$('#formLook').submit();
					}
				},'-',{
					text:'导出',
					iconCls:'icon-save',
					handler:function(){
						myExport1();
					}
				}],
				onClickRow:function(rowIndex, rowData){
					$('#commissionSheets').datagrid('options').url='/jlyw/DetailListComServlet.do?method=3';
					$('#commissionSheets').datagrid('options').queryParams={'DetailListCode':encodeURI(rowData.Code)};
					$('#commissionSheets').datagrid('reload');
				}
			});
			
			$('#commissionSheets').datagrid({
			    width:1000,
				height:300,
				title:'委托单列表',
//				iconCls:'icon-save',
				singleSelect:true, 
				fit: false,
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
					{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'委托单位',width:180,align:'center'},
					{field:'Status',title:'委托单状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
					{field:'Quantity',title:'台/件数',width:70,align:'center'},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'TotalFee',title:'总费用',width:70,align:'center'},
					{field:'TestFee',title:'检测费',width:70,align:'center'},
					{field:'RepairFee',title:'修理费',width:70,align:'center'},
					{field:'MaterialFee',title:'材料费',width:70,align:'center'},
					{field:'DebugFee',title:'调试费',width:70,align:'center'},
					{field:'CarFee',title:'交通费',width:70,align:'center'},
					{field:'OtherFee',title:'其他费用',width:70,align:'center'}
                ]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'查看委托单明细',
					iconCls:'icon-search',
					handler:function(){
						var select  = $('#commissionSheets').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('提示','请选择一行数据！','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm').submit();
					}
				}]
			});
			
			var userName ='<%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getName()%>';
			var userId = <%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getId()%>;
			var row = {'id':userId,'name':userName};
			$('#Employee').combobox('loadData', [row]);
			$('#Employee').combobox('select',row.id);

		});
		function query(){
			var emp = $('#Employee').combobox('getValue');
			if(!emp.match('^[0-9]*$')){
				$.messager.alert('提示','请确认员工输入是否有误！','warning');
				return;
			}
			$('#details').datagrid('options').url='/jlyw/QueryServlet.do?method=3';
			$('#details').datagrid('options').queryParams={'EmpId':encodeURI(emp),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue'))};
			$('#details').datagrid('reload');			
		}
		
		function myExport1(){
			ShowWaitingDlg("正在导出，请稍后......");
			$('#paramsStr').val(JSON.stringify($('#details').datagrid('options').queryParams));
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
						$.messager.alert('提示','导出失败，请重试！','warning');
						CloseWaitingDlg();
					}
				}
			});
		}
		
		</script>
</head>
<form id="frm_export" method="post" action="/jlyw/QueryServlet.do?method=5">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="业务查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_self">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:150px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
			<table width="950px" id="table1">
				<tr height="30px">
					<td align="right">员&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工：</td>
					<td align="left" colspan="3">
						<input id="Employee" class="easyui-combobox" name="Employee" url="" style="width:150px;" valueField="id" textField="name" panelHeight="150px" />
				</tr >
                <tr height="30px">
                	<td align="right">起始时间：</td>
				  	<td align="left">
						<input name="DateFrom" id="DateFrom" type="text" style="width:152px;" class="easyui-datetimebox" />
					</td>
                    <td align="right">结束时间：</td>
					<td align="left">
						<input name="DateEnd" id="DateEnd" type="text" style="width:152px;" class="easyui-datetimebox" />
					</td>
                </tr> 
				<tr height="40px">
                	<td colspan="3"></td>
				    <td align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				</tr>
				
		</table>
		</div>
        <br />
      <div>
      	 <table id="details" iconCls="icon-tip" style="height:500px; width:1000px"></table>
         <br/>
	     <table id="commissionSheets" iconCls="icon-tip" style="height:300px; width:1000px"></table>
	  </div>
	  <!--<div id="p2" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="操作区" collapsible="false"  closable="false">
			<form id="allot" method="post">
			<table width="850px" >
				
				<tr >
				     <td width="33%"  align="right" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search1()">查看原始记录</a>
	                     
					</td>
					<td  width="33%" align="center" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search2()">产看证书</a>
	                     
					</td>
					<td  width="33%" align="left" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">返回</a>
					</td>
					
				</tr>
		  </table>
		  </form>
		</div>-->
		
	<form id="formLook" method="post" action="/jlyw/QueryServlet.do?method=7" target="PrintFrame" accept-charset="utf-8" >
		<input id="Employee1"  name="EmpId"  style="width:150px;" type="hidden"/>
			
		<input name="DateFrom" id="DateFrom1" type="hidden" style="width:152px;" />
				
		<input name="DateEnd" id="DateEnd1" type="hidden" style="width:152px;"  />
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>
