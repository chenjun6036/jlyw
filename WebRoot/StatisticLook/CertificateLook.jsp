<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>证书查询</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	    <script type="text/javascript" src="../JScript/upload.js"></script>
    <script type="text/javascript" src="../JScript/json2.js"></script>
	<script>
		$(function(){
			nowDate = new Date();
			$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
			$('#Customer').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
				textField:'name',
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
					$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
			
			$("#Receiver").combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(record){
				},
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
			$("#FinishUser").combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(record){
				},
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
			
			$("#CheckOutUser").combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(record){
				},
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
				}
			});
				
			$('#table6').datagrid({
			    width:1000,
				height:500,
				title:'证书查询结果',
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
					{field:'Code',title:'证书号',width:100,align:'center'},
					{field:'CommissionSheetCode',title:'委托单号',width:180,align:'center'},
					{field:'CustomerName',title:'委托单位',width:80,align:'center'},
					{field:'CustomerAddress',title:'委托单位地址',width:80,align:'center'},
					{field:'CustomerZipCode',title:'委托单位邮编',width:80,align:'center'},
					{field:'CustomerTel',title:'委托单位电话',width:80,align:'center',sortable:true},
					{field:'CustomerContactor',title:'委托单位联系人',width:80,align:'center'},
					{field:'ApplianceName',title:'器具名称',width:80,align:'center'},
					{field:'AppManageCode',title:'管理编号',width:80,align:'center'},
					{field:'ApplianceCode',title:'出厂编号',width:80,align:'center'},
					{field:'Model',title:'型号规格',width:80,align:'center'},
					{field:'Quantity',title:'台/件数',width:70,align:'center'},
					{field:'Validity',title:'证书有效期',width:70,align:'center'},
					{field:'TotalFee',title:'总费用',width:70,align:'center'},
					{field:'TestFee',title:'检测费',width:70,align:'center'},
					{field:'RepairFee',title:'修理费',width:70,align:'center'},
					{field:'MaterialFee',title:'材料费',width:70,align:'center'},
					{field:'DebugFee',title:'调试费',width:70,align:'center'},
					{field:'CarFee',title:'交通费',width:70,align:'center'},
					{field:'OtherFee',title:'其他费用',width:70,align:'center'}
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
						$('#SearchForm_Code').val(select.CommissionSheetCode);
						$('#SearchForm').submit();
					}
				},'-',{
					text:'导出',
					iconCls:'icon-print',
					handler:function(){
						myExport();
					}
				}]
			});
			
			$("#ApplianceSpeciesName").combobox({
				valueField:'Name',
				textField:'Name',
				onSelect:function(record){
					$("#SpeciesType").val(record.SpeciesType);		//器具分类类型
					$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//器具分类ID（或者是标准名称ID）	
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
					$("#SpeciesType").val('');		//器具分类类型
					$("#ApplianceSpeciesId").val('');	//器具分类ID（或者是标准名称ID）
					
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
		});
		function query(){
			$('#table6').datagrid('options').url='/jlyw/StatisticServlet.do?method=15';
			$('#table6').datagrid('options').queryParams={'Code':encodeURI($('#Code').val()),'CustomerId':encodeURI($('#Customer').combobox('getValue')),'Receiver':encodeURI($('#Receiver').combobox('getValue')),'CommissionDateFrom':encodeURI($('#CommissionDateFrom').datebox('getValue')),'CommissionDateEnd':encodeURI($('#CommissionDateEnd').datebox('getValue')),'Status':encodeURI($('#Status').val()),'CommissionType':encodeURI($('#CommissionType').val()),'ReportType':encodeURI($('#ReportType').val()),'SpeciesType':encodeURI($('#SpeciesType').val()),'ApplianceSpeciesId':encodeURI($('#ApplianceSpeciesId').val()),'FinishUser':encodeURI($('#FinishUser').combobox('getValue')),'FinishDateFrom':encodeURI($('#FinishDateFrom').datebox('getValue')),'FinishDateEnd':encodeURI($('#FinishDateEnd').datebox('getValue')),'CheckOutUser':encodeURI($('#CheckOutUser').combobox('getValue')),'CheckOutDateFrom':encodeURI($('#CheckOutDateFrom').datebox('getValue')),'CheckOutDateEnd':encodeURI($('#CheckOutDateEnd').datebox('getValue'))};
			$('#table6').datagrid('reload');
		}
		function reset(){
			$('#query').form('clear');
			document.getElementById("ReportType").value="";
			document.getElementById("CommissionType").value="";
			document.getElementById("Status").value="";
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
			<jsp:param name="TitleName" value="证书查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<form id="SearchForm" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
        </form>
		<form id="frm_export" method="post" action="/jlyw/StatisticServlet.do?method=16">
		<input id="paramsStr" name="paramsStr" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden" />
		</form>
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:330px;padding:10px;"
				title="查询条件" collapsible="false"  closable="false">
                <form id="query">
			<table width="950px" id="table1">
				<tr height="30px">
					<td width="10%" align="right">委托单号：</td>
					<td width="22%" align="left">
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;"/>
					</td>
					<td width="20%" align="right">委托单位：</td>
					<td width="22%" align="left">
						<input id="Customer" class="easyui-combobox" name="Customer" style="width:150px;"/>
					</td>
				</tr >
                <tr>
                	<td align="right">器&nbsp;具&nbsp;名：</td>
                  	<td align="left"><select name="ApplianceSpeciesName" id="ApplianceSpeciesName" style="width:152px"/>
                                  <input type="hidden" id="SpeciesType" name="SpeciesType" value="" /><input type="hidden" id="ApplianceSpeciesId" name="ApplianceSpeciesId" value="" /></td>
                    <td align="right">报告形式：</td>
                	<td align="left">
					<select id="ReportType" name="ReportType" style="width:152px">
						<option value="" selected="selected">请选择...</option>
						<option value="1">检定</option>
						<option value="2">校准</option>
						<option value="3">检测</option>
						<option value="4">检验</option>
					</select></td>
                </tr>
                <tr height="30px">
                	<td align="right">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				  	<td align="left">
						<select name="Status" id="Status" style="width:152px;">
                            <option value="" selected="selected">全部</option>
                            <option value="0" >已收件</option>
                            <option value="1" >已分配</option>
                            <option value="2" >转包中</option>
                            <option value="3" >已完工</option>
                            <option value="4" >已结账</option>
                            <option value="10" >已注销</option>
                            <option value="-1">预留中</option>
                        </select>
					</td>
                    <td align="right">委托类别：</td>
                    <td align="left">
						<select name="CommissionType" id="CommissionType" style="width:152px" required="true">
                        	<option value="" selected="selected">全部</option>
                            <option value="1">送样检测</option>
                            <option value="2">现场检测</option>
                            <option value="3">公正计量</option>
                            <option value="4">形式评价</option>
                            <option value="5">其它业务</option>
                            <option value="6">自检业务</option>
                            <option value="7">现场带回</option>
                        </select>
					</td>
				</tr>
                <tr>
                    <td align="right">接&nbsp;收&nbsp;人：</td>
                  	<td align="left"><select name="Receiver" id="Receiver" style="width:152px"/></td>
                </tr>
                <tr>
                	<td align="right">委托时间：</td>
				  	<td align="left">
						<input name="CommissionDateFrom" id="CommissionDateFrom" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="CommissionDateEnd" id="CommissionDateEnd" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr>
                <tr>
                	<td align="right">完&nbsp;工&nbsp;人：</td>
                  	<td align="left"><select name="FinishUser" id="FinishUser" style="width:152px"/></td>
                </tr>
                <tr height="30px">
                	<td align="right">完工时间：</td>
				  	<td align="left">
						<input name="FinishDateFrom" id="FinishDateFrom" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="FinishDateEnd" id="FinishDateEnd" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
                <tr>
                	<td align="right">结&nbsp;账&nbsp;人：</td>
                  	<td align="left"><select name="CheckOutUser" id="CheckOutUser" style="width:152px"/></td>
                </tr>
                <tr height="30px">
                	<td align="right">结账时间：</td>
				  	<td align="left">
						<input name="CheckOutDateFrom" id="CheckOutDateFrom" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="left">----------------------------</td>
					<td align="left">
						<input name="CheckOutDateEnd" id="CheckOutDateEnd" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">查询</a></td>
                    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				</tr>
				
		</table>
        </form>
		</div>
        <br />
      <div style="width:1000px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
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
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search2()">查看证书</a>
	                     
					</td>
					<td  width="33%" align="left" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">返回</a>
					</td>
					
				</tr>
		  </table>
		  </form>
		</div>-->
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>
