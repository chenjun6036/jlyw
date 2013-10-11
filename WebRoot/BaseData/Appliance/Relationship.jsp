<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>标准项目的关系管理</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="./Relationship.js" charset="utf-8"></script>	

<style type="text/css">

#loading{
	position:absolute;
	left:0px;
	top:0px;
	padding:2px;
	z-index:20001;
	height:100%;
	width:100%;
	text-align:center;
	background:#ffffff;
}
#loading a {
	color:#225588;
}
#loading .loading-indicator{
	position: absolute;
	top: 50%; 
	left: 50%;
	background:white;
	color:#444;
	font:bold 13px tahoma,arial,helvetica;
	padding:10px;
	margin-left:-100px;
	margin-top:-50px;
	width:200px;
	height:100px;
}
#loading-msg {
	font: normal 10px arial,tahoma,sans-serif;
}
</style>
</head>

<body>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		标准项目的关系管理
		<br />
		<span id="loading-msg">加载样式和图片...</span>
	</div>
</div>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="标准项目的关系管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	

		<div style="padding-left:3px;">
			<div class="easyui-panel" style="width:804px;" title="对应关系操作" >
				<div style="padding-left:10px; padding-top:10px;">
				
					<table width="774">
						<tr>
						  <td><input id="radio1" type="radio" value="1" name="relation" onclick="query(this)"/>计量标准和标准器具的对应关系</td>
                          <td><input id="radio3" type="radio" value="3" name="relation" onclick="query(this)"/>受检器具和计量标准的对应关系</td>
						  <td><input id="radio2" type="radio" value="2" name="relation" onclick="query(this)"/>受检器具和标准器具的对应关系</td>
						</tr>
						<tr>
                       	  <td><input id="radio5" type="radio" value="5" name="relation" onclick="query(this)"/>受检器具和技术规范的对应关系</td>
						  <td><input id="radio4" type="radio" value="4" name="relation" onclick="query(this)"/>计量标准和技术规范的对应关系</td>
                          <td><input id="radio4" type="radio" value="6" name="relation" onclick="query(this)"/>受检器具已有对应关系</td>
						</tr>	
				  </table>
				</div>
			</div>
			<br />
			
			<div id="panel_Std_StdApp" class="easyui-panel" style="width:804px;" closed="true" >
				<div>
					<table id="table_Std_StdApp"></table>
				</div>
				
				<br />
				
				<div class="easyui-panel" style="width:802px" title="对应关系建立">
				<form id="frm_StdStdApp" method="post">
				    <table id="add_Std_StdApp" style="width:802px"> 
						<th style="padding-top:10px">计量标准</th>
						<th style="padding-top:10px">标准器具</th>
						<tr>
							<td align="center"><select id="StdStdApp_StandardId" name="StdStdApp_StandardId" class="easyui-combobox" valueField="id" textField="name" style="width:170px" required="true" panelHeight="150px"/></td>
							<td align="center"><select id="StdStdApp_StandardApplianceId" name="StdStdApp_StandardApplianceId" class="easyui-combobox" valueField="id" textField="name" style="width:252px" required="true" panelHeight="150px"/></td>
						</tr>
                        <tr height="20px"></tr>
					</table>
					</form>
				</div>
			</div>
			
			<div id="panel_StdApp_TgtApp" class="easyui-panel" style="width:804px;" closed="true" >
				<div>
					<table id="table_StdApp_TgtApp"></table>
				</div>
				
				<br />
				
				<div class="easyui-panel" style="width:802px" title="对应关系建立">
                <form id="frm_StdAppTgtApp" method="post">
					 <table id="add_StdApp_TgtApp" style="width:802px"> 
						<th style="padding-top:10px">受检器具</th>
						<th style="padding-top:10px">计量标准</th>
						<tr>
							<td align="center"><input id="StdAppTgtApp_TargetApplianceId" name="StdAppTgtApp_TargetApplianceId" class="easyui-combobox" url="" valueField="id" textField="name" style="width:200px" required="true" panelHeight="150px"/></td>
							<td align="center"><input id="StdAppTgtApp_StandardId" name="StdAppTgtApp_StandardId" class="easyui-combobox" url="" valueField="id" textField="name" style="width:200px" required="true" panelHeight="150px"/></td>
						</tr>
                        <tr height="20px"></tr>
					</table>
                    </form>
				</div>
			</div>
			
			<div id="panel_Std_TgtApp" class="easyui-panel" style="width:804px;" closed="true" >
				<div>
					<table id="table_Std_TgtApp"></table>
				</div>
				<br />
				<div class="easyui-panel" style="width:802px" title="对应关系建立">
                <form id="frm_StdTgtApp" method="post">
					<table id="add_Std_TgtApp" style="width:802px"> 
						<th style="padding-top:10px">受检器具</th>
                        <th style="padding-top:10px">计量标准</th>
						<tr>
							<td align="center"><select id="StdTgtApp_TargetApplianceId" name="StdTgtApp_TargetApplianceId" class="easyui-combobox" url="" valueField="id" textField="name" style="width:180px" required="true" panelHeight="150px"/></td>
                            <td align="center"><select id="StdTgtApp_StandardId" name="StdTgtApp_StandardId" class="easyui-combobox" valueField="id" textField="name" style="width:180px" required="true" panelHeight="150px"/></td>
						</tr>
						<tr style="height:50px">
							<td align="center"><a href="javascript:void(0)" onclick="Sub_Std_TgtApp()" class="easyui-linkbutton" iconCls="icon-add">新增</a></td>
							<td align="left" style="padding-left:30px"> <a href="javascript:void(0)" onclick="cancel_Std_TgtApp()" class="easyui-linkbutton" iconCls="icon-cancel">取消</a></td>
						</tr>
					</table>
                    </form>
				</div>
			</div>
			
			<div id="panel_Std_Spec" class="easyui-panel" style="width:804px;" closed="true" >
				<div>
					<table id="table_Std_Spec"></table>
				</div>
				<!--<div class="easyui-panel" style="width:802px" title="对应关系建立">
					<table id="add_Std_Spec" style="width:802px"> 
						<th style="padding-top:10px">标准</th>
						<th style="padding-top:10px">受检器具</th>
						<th style="padding-top:10px">技术规范</th>
						<tr>
							<td align="center"><input id="StandardId_3" name="StandardId" type="text" /></td>
							<td align="center"><input id="TargetApplianceId_3" name="TargetApplianceId" type="text" /></td>
							<td align="center"><input id="SpecificationId_3" name="SpecificationId" type="text" /></td>
						</tr>
						<tr style="height:50px">
							<td align="right"><a href="javascript:void(0)" onclick="Sub_Std_Spec()" class="easyui-linkbutton" iconCls="icon-add">新增</a></td>
							<td align="right"> <a href="javascript:void(0)" onclick="cancel_Std_Spec()" class="easyui-linkbutton" iconCls="icon-cancel">取消</a></td>
						</tr>
					</table>
				</div>-->
			</div>
			<div id="panel_TgtApp_Spec" class="easyui-panel" style="width:804px;" closed="true" >
				<div>
					<table id="table_TgtApp_Spec"></table>
				</div>
				<br />
				<div class="easyui-panel" style="width:802px" title="对应关系建立">
                <form id="frm_TgtApp_Spec" method="post">
					<table id="add_TgtApp_Spec" style="width:802px"> 
						<th style="padding-top:10px">受检器具</th>
						<th style="padding-top:10px">技术规范</th>
						<tr>
							<td align="center"><input id="TgtAppSpec_TargetApplianceId" name="TgtAppSpec_TargetApplianceId" class="easyui-combobox" url="" valueField="id" textField="name" style="width:200px" required="true" panelHeight="150px"/></td>
							<td align="left"><input id="TgtAppSpec_SpecificationId" name="TgtAppSpec_SpecificationId" class="easyui-combobox" url="" valueField="id" textField="name" style="width:310px" required="true" panelHeight="150px"/></td>
						</tr>
						<tr style="height:50px">
							<td width="400px" align="center"><a href="javascript:void(0)" onclick="Sub_TgtApp_Spec()" class="easyui-linkbutton" iconCls="icon-add">新增</a></td>
							<td width="400px" align="center" style="padding-left:30px"> <a href="javascript:void(0)" onclick="cancel_TgtApp_Spec()" class="easyui-linkbutton" iconCls="icon-cancel">取消</a></td>
						</tr>
					</table>
                    </form>
				</div>
			</div>
            <div id="panel_TgtApp" class="easyui-panel" style="width:804px;" closed="true" >
            	<div class="easyui-panel" style="width:802px" title="查询">
					<table id="add_TgtApp_Spec" style="width:802px"> 
						<tr>
							<td align="center">受检器具</td>
							<td align="left"><input id="TgtApp" name="TgtApp" class="easyui-combobox" url="" valueField="id" textField="name" style="width:200px" required="true" panelHeight="150px"/></td>
							<td width="400px" align="center"><a href="javascript:void(0)" onclick="queryTgtApp()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
						</tr>
					</table>
				</div>
                <br/>
				<div>
					<table id="table_StdApp_TgtApp1"></table>
                    <br />
                    <table id="table_Std_TgtApp1"></table>
                    <br />
                    <table id="table_TgtApp_Spec1"></table>
				</div>
			</div>
            
		</div>
		
		<div id="table_Std_StdApp-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="Delete_Std_StdApp()">删除</a>
					</td>
                    <td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" onclick="ShowUnRelatedStd()">查询未建关系的计量标准</a>
					</td>
                    
				  <td align="right"><input id="radio" type="radio" value="1" name="Radio_Std_StdApp" />计量标准</td>
				  <td width="9%"><input id="radio" type="radio" value="2" name="Radio_Std_StdApp" />标准器具</td>
				  <td width="21%"><input id="Std_StdApp" name="Std_StdApp" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="Query_Std_StdApp()">查询</a></td>
				</tr>
			</table>
		</div>
		
		<div id="table_StdApp_TgtApp-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="Delete_StdApp_TgtApp()">删除</a>
					</td>
                    <td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" onclick="ShowUnRelatedTgtApp(1)">查询未建关系的受检器具</a>
					</td>
				  <td align="right"><input id="radio" type="radio" value="1" name="Radio_StdApp_TgtApp" />标准器具</td>
				  <td width="9%"><input id="radio" type="radio" value="2" name="Radio_StdApp_TgtApp" />受检器具</td>
				  <td width="21%"><input id="StdApp_TgtApp" name="StdApp_TgtApp" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="Query_StdApp_TgtApp()">查询</a></td>
				</tr>
			</table>
		</div>
		
		<div id="table_Std_TgtApp-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="Delete_Std_TgtApp()">删除</a>
					</td>
                     <td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" onclick="ShowUnRelatedTgtApp(2)">查询未建关系的受检器具</a>
					</td>
				  <td align="right"><input id="radio" type="radio" value="1" name="Radio_Std_TgtApp" />计量标准</td>
				  <td width="9%"><input id="radio" type="radio" value="2" name="Radio_Std_TgtApp" />受检器具</td>
				  <td width="21%"><input id="Std_TgtApp" name="Std_TgtApp" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="Query_Std_TgtApp()">查询</a></td>
				</tr>
			</table>
		</div>
		
		<div id="table_Std_Spec-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					 <!--<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="Delete_Std_Spec()">删除</a>
					</td> -->
				  <td align="right"><input id="radio" type="radio" value="1" name="Radio_Std_Spec" />计量标准</td>
				  <td width="9%"><input id="radio" type="radio" value="2" name="Radio_Std_Spec" />技术规范</td>
				  <td width="21%"><input id="Std_Spec" name="Std_Spec" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="Query_Std_Spec()">查询</a></td>
				</tr>
			</table>
		</div>
		
		<div id="table_TgtApp_Spec-search-toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="Delete_TgtApp_Spec()">删除</a>
					</td>
                     <td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" onclick="ShowUnRelatedTgtApp(3)">查询未建关系的受检器具</a>
					</td>
				    <td align="right"><input id="radio" type="radio" value="1" name="Radio_TgtApp_Spec" />受检器具</td>
				    <td width="9%"><input id="radio" type="radio" value="2" name="Radio_TgtApp_Spec" />技术规范</td>
				    <td width="21%"><input id="TgtApp_Spec" name="TgtApp_Spec" type="text" /></td>
					<td width="9%"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="Query_TgtApp_Spec()">查询</a></td>
				</tr>
			</table>
		</div>
        
         <div id="div_StdApp" class="easyui-window" title="标准器具" style="padding: 10px;width: 850px;height: 450px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <table id="table_StdStdApp_StdApp" class="easyui-datagrid" title="标准器具"></table>
        </div>
        
         <div id="div_Std_StdApp" class="easyui-window" title="标准器具" style="padding: 10px;width: 850px;height: 450px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <table id="table_TgtAppStdApp_StdApp" class="easyui-datagrid" title="标准器具"></table>
        </div>
        
         <div id="div_UnRelated_Std" class="easyui-window" title="未建立关系的计量标准" style="padding: 10px;width: 850px;height: 450px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <table id="table_TgtAppStdApp_UnRelated_Std" class="easyui-datagrid" title="未建立关系的计量标准"></table>
        </div>
        
         <div id="div_UnRelated_TgtApp" class="easyui-window" title="未建立关系的受检器具" style="padding: 10px;width: 850px;height: 450px;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <table id="table_UnRelated_TgtApp" class="easyui-datagrid" title="未建立关系的受检器具"></table>
        </div>
        
</DIV>
</DIV>
</body>
</html>
