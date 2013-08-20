<%@ page contentType="text/html; charset=gbk" language="java" import="com.jlyw.hibernate.*,com.jlyw.util.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
    <title>常州计量信息管理系统</title>
    <link rel="stylesheet" id="easyuiTheme" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/unback.js"></script>
	<script type="text/javascript" src="../JScript/main.js"></script>
	<script type="text/javascript" src="../JScript/dongle.js"></script>
    <script>
		$(function(){
			<% if(session.getAttribute("LOGIN_USER")==null || (((SysUser)session.getAttribute("LOGIN_USER")).getNeedDongle()!=null&&((SysUser)session.getAttribute("LOGIN_USER")).getNeedDongle())){ %>
			checkDongle('<%=SystemCfgUtil.getDonglePID()%>','<%=SystemCfgUtil.getDonglePIN()%>','<%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getUserName()%>');
			<% } %>
			
			
			<% if(session.getAttribute("LOGIN_USER")==null || (((SysUser)session.getAttribute("LOGIN_USER")).getNeedDongle()!=null&&((SysUser)session.getAttribute("LOGIN_USER")).getNeedDongle())){ %>
			setInterval("checkDongle('<%=SystemCfgUtil.getDonglePID()%>','<%=SystemCfgUtil.getDonglePIN()%>','<%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getUserName()%>')", 30000);
			<% } %>
		});
	</script>
<style type="text/css">
TD.TopLinkBott {
	PADDING-RIGHT: 10px;
	PADDING-LEFT: 3px;
	PADDING-BOTTOM: 10px;
	VERTICAL-ALIGN:bottom;
	COLOR: #000000;
	PADDING-TOP: 3px;
	FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
TD.TopLink {
	PADDING-RIGHT: 3px;
	PADDING-LEFT: 3px;
	PADDING-BOTTOM: 1px;
	VERTICAL-ALIGN: middle;
	COLOR: #000000;
	PADDING-TOP: 3px;
	FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif
}
TD.TopLink_over {
	BORDER-RIGHT: #adc9ff 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #adc9ff 1px solid;
	PADDING-LEFT: 2px;
	PADDING-BOTTOM: 0px;
	VERTICAL-ALIGN: middle;
	BORDER-LEFT: #adc9ff 1px solid;
	COLOR: #000000;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #adc9ff 1px solid;
	FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif;
	BACKGROUND-COLOR: #5781d5
}
TD.TopLink_down {
	BORDER-RIGHT: #adc9ff 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #adc9ff 1px solid;
	PADDING-LEFT: 2px;
	PADDING-BOTTOM: 0px;
	VERTICAL-ALIGN: middle;
	BORDER-LEFT: #adc9ff 1px solid;
	COLOR: #ffffff;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #adc9ff 1px solid;
	FONT-FAMILY: Arial, Verdana, Helvetica, sans-serif;
	BACKGROUND-COLOR: #3063c9
}
TD.ToolbarSeparator {
	FONT: 10px verdana;
	MARGIN-LEFT: 3px;
	VERTICAL-ALIGN: middle;
	COLOR: #ffffff;
	MARGIN-RIGHT: 3px
}
#loading{
	position:absolute;
	left:0px;
	top:0px;
	padding:2px;
	z-index:20001;
	height:100%;
	width:100%;
	text-align:center;
	background:#FFFFFF;
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
<body class="easyui-layout" style="background-color:#D2E0F2;" bgcolor="#FFFFFF">
<object classid="clsid:e6bd6993-164f-4277-ae97-5eb4bab56443" id="ET299" style="left:0px; top:0px; width:20px; height:20px"></object>
<div id="loading">
	<div class="loading-indicator">
		<img src="/jlyw/images/loading32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:center;" />
		常州计量信息管理系统
		<br />
		<span id="loading-msg">加载样式和图片...</span>
	</div>
</div>
<bgsound id="snd" loop="0" src="" />

    <!-- 主界面框架 -->
    <!-- 系统名、版本、版权…… -->
    <div region="north" split="false" border="false" style="height:70px;overflow:hidden">
        <div class="easyui-layout" fit="true" scroll="no" border="false">
			<div region="south" split="false" border="false" style="height:5px; background-color:#D2E0F2; padding-top:0px; padding-bottom:0px" align="center">
				<div style="margin-top:0px; margin-bottom:0px; padding-top:0px; padding-bottom:0px"><img id="top-spilter-img" src="../images/datagrid_sort_asc.gif" width="13px" height="5px" onclick="doTopWin()" title="点击隐藏" border="0" style="cursor:pointer; margin-top:0px; margin-bottom:0px" /></div>
			</div>
			<div region="center" style="overflow:hidden;">
				<table id="01" height="70px" width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="799px">
							<img src="../images/222.png" width="799" height="70" alt="常州市计量测试技术研究所信息管理系统" />
						</td>
						<td style="BACKGROUND-REPEAT: repeat-x" width="100%" background="/jlyw/images/tc.gif" align="right">
							<TABLE height="100%" cellSpacing="0" cellPadding="0" border="0">
							  <TR>
								<TD vAlign="top" align="right"><TABLE cellSpacing="0" cellPadding="0" border="0">
									<TR>									  
									  <TD onMouseUp="this.className='TopLink_over'" class="TopLink" onMouseDown="this.className='TopLink_down'" onMouseOver="this.className='TopLink_over'" style="CURSOR: hand" onMouseOut="this.className='TopLink'" Align="center" valign="middle"><a href="/jlyw/docs/help.pdf" target="_blank"><FONT 
								color="#E5E5E5"><NOBR>帮助</NOBR></FONT></a></TD>
									  <TD class="ToolbarSeparator">|</TD>
									  
									  <TD onMouseUp="this.className='TopLink_over'" class="TopLink" onMouseDown="this.className='TopLink_down'" onMouseOver="this.className='TopLink_over'" style="CURSOR: hand" onMouseOut="this.className='TopLink'" Align="center" valign="middle"><a href="javascript:void(0)" onclick="doLogout()"><FONT color="#FF0000" style="font-size:15px; font-weight:bold"><NOBR>安全退出</NOBR></FONT></a></TD>
									  <TD>&nbsp;</TD>
									</TR>
								  </TABLE></TD>
							  </TR>
							  <TR>
								<TD class="TopLinkBott"  valign="bottom"><NOBR><span id="myDate"></span>&nbsp;&nbsp;&nbsp;&nbsp;欢迎您，<%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getName() %></NOBR></TD>
							  </TR>
							</TABLE>
							
						</td>
							
						
						<!--<td width="120px">
							<img src="../images/1_02.gif" width="120" height="70" alt="" />
						</td>-->
					</tr>
				</table>
			</div>
		</div>
    </div>
    <!-- 系统名、版本、版权…… -->
	
    <!-- 菜单 -->
    <div id="menu" region="west" split="true" title="功能菜单" style="width: 240px;overflow:hidden">
       <iframe id="MenuFrame" src="../menu.html" frameborder="0" width="100%" height="99.5%"></iframe>
    </div>
    <!-- 菜单 -->
	
	<!--版权信息-->
	<div region="south" split="false" border="false" style="height:50px;overflow:hidden">
		<div class="easyui-layout" fit="true" scroll="no">
			<div region="north" split="false" border="false" style="height:5px; background-color:#D2E0F2; padding-top:0px; padding-bottom:0px" align="center">
				<div style="margin-top:0px; margin-bottom:0px; padding-top:0px; padding-bottom:0px"><img id="bott-spilter-img" src="../images/datagrid_sort_desc.gif" width="13px" height="5px" onclick="doBottWin()" title="点击隐藏" border="0" style="cursor:pointer; margin-top:0px; margin-bottom:0px" /></div>
			</div>
			<div region="center" align="center" style="padding-top:5px; overflow:hidden">
				<table width="100%" height="100%" border="0">
					<tr height="12px" style="line-height:12px">
						<td align="left" valign="middle" style="border-bottom:dashed 1px #333333">
							<span style="color:#009933"> <%=SystemCfgUtil.getSpecialLetters() %></span>
						</td>
					</tr>
					<tr style="line-height:12px" height="12px">
						<td align="center" valign="top">
							版权所有&copy; 2012 常州计量. 开发单位：南京理工大学.
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<!--版权信息-->
	
	<!-- 内容区域 -->
    <div id="tt" region="center" style="background: #fafafa;overflow:hidden">
        <iframe id="MainFrame" src="../index/index.jsp" frameborder="0" width="100%" height="99.5%"></iframe>
    </div>
    <!-- 内容区域 -->
    <!-- 主界面框架 -->
</body>
</html>
<script language="javascript">
    var topExpand = true, bottExpand = true;
	function doTopWin()
	{
		if(topExpand)
		{
			$('body').layout('panel','north').panel('resize',{height:5});
			$('body').layout('resize');
			$('#top-spilter-img').attr("src","../images/datagrid_sort_desc.gif");
			$('#top-spilter-img').attr("title","点击展开");
			topExpand = false;
		}
		else
		{
			$('body').layout('panel','north').panel('resize',{height:70});
			$('body').layout('resize');
			$('#top-spilter-img').attr("src","../images/datagrid_sort_asc.gif");
			$('#top-spilter-img').attr("title","点击隐藏");
			topExpand = true;
		}
	}
	function doBottWin()
	{
		if(bottExpand)
		{
			$('body').layout('panel','south').panel('resize',{height:5});
			$('body').layout('resize');
			$('#bott-spilter-img').attr("src","../images/datagrid_sort_asc.gif");
			$('#bott-spilter-img').attr("title","点击展开");
			bottExpand = false;
		}
		else
		{
			$('body').layout('panel','south').panel('resize',{height:50});
			$('body').layout('resize');
			$('#bott-spilter-img').attr("src","../images/datagrid_sort_desc.gif");
			$('#bott-spilter-img').attr("title","点击隐藏");
			bottExpand = true;
		}
	}
</script>
