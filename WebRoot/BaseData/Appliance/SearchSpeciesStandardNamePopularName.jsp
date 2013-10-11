<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>受检器具分类、标准名称、常用名称查询</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"  src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){			
			$('#table2').datagrid({
				title:'分类信息查询',
				width:900,
				height:500,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				//sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'parentStr',title:'父分类',width:150,align:'center'},
					{field:'SpecName',title:'分类名称',width:120,align:'center'},
					{field:'StdName',title:'标准名称',width:120,align:'center'},
					{field:'PopName',title:'常用名称',width:120,align:'center'}
				]],
				pagination:false,
				rownumbers:true
			});
			
			$("#queryname").combobox({
				valueField:'Name',
				textField:'Name',
				onSelect:function(record){
					$("#SpeciesType").val(record.SpeciesType);		//器具分类类型
					$("#ApplianceSpeciesId").val(record.ApplianceSpeciesId);	//器具分类ID（或者是标准名称ID）
					$("#popName").val(record.PopName?record.PopName:"");					
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
					$("#popName").val('');
					
					try{
						window.clearTimeout(this.reloadObj);
					}catch(ex){}
					this.reloadObj = window.setTimeout(function(){   
							var newValue = $('#queryname').combobox('getText');
							$('#queryname').combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
					}, 700);
					//$(this).combobox('reload','/jlyw/ApplianceServlet.do?method=0&QueryName='+encodeURI(newValue));
				}
			});
			
		});
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/ApplianceSpeciesServlet.do?method=6';
			$('#table2').datagrid('options').queryParams={'SpeciesType':encodeURI($("#SpeciesType").val()),'ApplianceSpeciesId':encodeURI($("#ApplianceSpeciesId").val()),'PopName':encodeURI($("#popName").val())};
			$('#table2').datagrid('reload');
		}
		
		</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="受检器具分类、标准名称、常用名称查询" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		
        <div>
			<br />
			<table id="table1" style="width:900px">
				<tr>
					<td align="right">名称：</td>
				   <td width="300" align="left"><input id="queryname" name="queryname" class="easyui-combobox" style="width:152px" panelHeight="auto" />&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">查询
					</a></td>
				</tr>
                <input id="SpeciesType" type="hidden"/>
                <input id="ApplianceSpeciesId" type="hidden"/>
                <input id="popName" type="hidden"/>
			</table>
		</div>
        
        
		<table id="table2" style="height:500px; width:900px"></table>
		
	</div>
</DIV>
</DIV>
</body>
</html>
