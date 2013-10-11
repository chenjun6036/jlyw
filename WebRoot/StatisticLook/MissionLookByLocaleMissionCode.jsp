<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>现场委托单查询</title>
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
			$("#LocaleCommissionCode").combobox({//现场委托书号
			//	url:'/jlyw/CustomerServlet.do?method=5',
				valueField:'code',
				textField:'code',
				required:true,
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
							var newValue = $('#LocaleCommissionCode').combobox('getText');
							$('#LocaleCommissionCode').combobox('reload','/jlyw/LocaleMissionServlet.do?method=15&QueryName='+encodeURI(newValue));
					}, 700);
					
				}
			});
				
			$('#table6').datagrid({
			    width:900,
				height:500,
				title:'现场委托单结果',
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
					{field:'Code',title:'委托单号',width:100,align:'center',sortable:true},
					{field:'CustomerName',title:'委托单位',width:180,align:'center',sortable:true},
					{field:'CommissionDate',title:'委托日期',width:80,align:'center'},
					{field:'Status',title:'委托单状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							return getCommissionSheetStatusInfo(value);
						}
					},
					{field:'sdsd',title:'是否待完工',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(rowData.Status == 10){	//已注销
								return ""
							}else if(rowData.Status >= 3){	//已完工(包括已结账、已结束)
								return ""
							}else if(rowData.IsSubContract==true ||(rowData.FinishQuantity!=null&&rowData.EffectQuantity!=null&&rowData.FinishQuantity == rowData.EffectQuantity)){	//可以完工的器具
								return "待完工"
							}else{
								return "";
							}
						}
					},
					{field:'ApplianceSpeciesName',title:'器具授权名',width:80,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center',sortable:true},
					{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
					{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Accuracy',title:'精度等级',width:80,align:'center'},
					{field:'Manufacturer',title:'制造厂商',width:80,align:'center'},
					{field:'Quantity',title:'台/件数',width:70,align:'center'},
					{field:'WithdrawQuantity',title:'退样台件数',width:70,align:'center'},
					{field:'TotalFee',title:'总费用',width:70,align:'center'},
					{field:'TestFee',title:'检测费',width:70,align:'center'},
					{field:'RepairFee',title:'修理费',width:70,align:'center'},
					{field:'MaterialFee',title:'材料费',width:70,align:'center'},
					{field:'DebugFee',title:'调试费',width:70,align:'center'},
					{field:'CarFee',title:'交通费',width:70,align:'center'},
					{field:'OtherFee',title:'其他费用',width:70,align:'center'},
					{field:'MandatoryInspection',title:'强制检验',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['MandatoryInspection']=0;
								return "强制检定";
							}
							else if(value == 1 || value == '1')
							{
								rowData['MandatoryInspection']=1;
								return "非强制检定";
							}
							else
							{
								return "";
							}
						}},
					{field:'Urgent',title:'是否加急',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Urgent']=0;
								return "是";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Urgent']=1;
								return "否";
							}
							else
							{
								return "";
							}
						}},
					{field:'Trans',title:'是否转包',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Trans']=0;
								return "是";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Trans']=1;
								return "否";
							}
							else
							{
								return "";
							}
						}},
					{field:'SubContractor',title:'转包方',width:80,align:'center'},
					{field:'Appearance',title:'外观附件',width:80,align:'center'},
					{field:'Repair',title:'需修理否',width:60,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Repair']=0;
								return "需要修理";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Repair']=1;
								return "无需修理";
							}
							else
							{
								return "";
							}
						}},
					{field:'ReportType',title:'报告形式',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 1 || value == '1')
							{
								rowData['ReportType']=1;
								return "检定";
							}
							else if(value == 2 || value == '2')
							{
								rowData['ReportType']=2;
								return "校准";
							}
							else if(value == 3 || value == '3')
							{	rowData['ReportType']=3;
								return "检验";
							}
							else
							{
								return "";
							}
						}},
					{field:'OtherRequirements',title:'其他要求',width:80,align:'center'},
					{field:'Location',title:'存放位置',width:80,align:'center'},
					{field:'FinishLocation',title:'完工存放位置',width:80,align:'center'},
					{field:'Allotee',title:'派定人',width:80,align:'center'}	
                ]],
				pagination:true,
				rownumbers:true,
				showFooter:true,
				toolbar:[{
					text:'查看委托单明细',
					iconCls:'icon-search',
					handler:function(){
						var select  = $('#table6').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('提示','请选择一行数据！','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm').submit();
					}
				},'-',{
					text:'导出',
					iconCls:'icon-save',
					handler:function() {
						myExport();
					}
				}],
				rowStyler:function(rowIndex, rowData){
					if(rowData.Status == 10||rowData.Status == "10"){	//已注销
						return 'color:#FF0000';
					}else if(rowData.Status == 0||rowData.Status == "0"){	//已收件
						return 'color:#0000FF';	
					}else if(rowData.Status == 1||rowData.Status == "1"){	//已分配
						return 'color:#0000FF';	
					}else if(rowData.Status == 2||rowData.Status == "2"){	//转包中
						return 'color:#CCFF00';	
					}else if(rowData.Status == 3||rowData.Status == "3"){	//已完工
						return 'color:#000000';	
					}else if(rowData.Status == 4||rowData.Status == "4"){  //已结账
						return 'color:#008000';
					}else{
						return 'color:#000000';
					}
				}
			});
			
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/StatisticServlet.do?method=4';
			$('#table6').datagrid('options').queryParams={'LocaleMissionCode':encodeURI($('#LocaleCommissionCode').combobox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
		}
		function myExport(){
			
			ShowWaitingDlg("正在导出，请稍后......");
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
						$.messager.alert('提示','导出失败，请重试！','warning');
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
			<jsp:param name="TitleName" value="现场委托单查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
        <form id="frm_export" method="post" action="/jlyw/QueryServlet.do?method=10">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:80px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="850px" id="table1">
				<tr height="40px">
					<td width="14%" align="right" >现场任务书号：</td>
					<td width="22%" align="left" >
						<input id="LocaleCommissionCode" class="easyui-combobox" name="LocaleCommissionCode" url="" style="width:150px;" panelHeight="150px" />
					</td>
				    <td align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
				</tr>
		</table>
        </form>
		</div>
        <br />
      <div style="width:900px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>
</div>
</DIV>
</DIV>
</body>
</html>
