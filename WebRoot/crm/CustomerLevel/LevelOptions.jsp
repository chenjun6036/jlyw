<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>业务查询</title>
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

		function reset(){
			$('#query').form('clear');
			
		}
		
		
	$(function(){
				$('#query').form('submit',{
				url: '/jlyw/CrmServlet.do?method=46',
				onSubmit:function(){},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				//$.messager.alert('提示',result.Theta1,'info');
		   				$("#query").form('load',result);
		   		 }
			});
				
	
	});
	
	function sub()
	{
		$('#query').form('submit',{
				url: '/jlyw/CrmServlet.do?method=47',
				onSubmit:function(){
				
				if(parseFloat($("#level1").val())+parseFloat($("#level2").val())+parseFloat($("#level3").val())+parseFloat($("#level4").val())+parseFloat($("#level5").val())+parseFloat($("#level6").val())+parseFloat($("#level7").val())>100)
				{
				$.messager.alert('提示',"参数不符合要求，请检查后重试！",'info');
				return false;
				}
				
				return $('#query').form('validate');
				
				},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('提示',result.msg,'info');
		   				$("#query").form('load',result);
		   		 }
			});
	}
	function resub()
	{
	ShowWaitingDlg("正在更新，大约需要2分钟，请稍后......");
	$('#query').form('submit',{
				url: '/jlyw/CrmServlet.do?method=45',
				onSubmit:function(){},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			CloseWaitingDlg();
		   			$.messager.alert('提示',result.msg,'info');
		   		 }
			});
	
	}
		</script>
</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="等级管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
		<!-- <form id="frm_export" method="post" action="/jlyw/CrmExportServlet.do?method=1">
		<input id="par" name="Par" type="hidden" />
		</form>
		<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
		<input id="filePath" name="filePath" type="hidden"/>
		</form> -->
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:750px;height:500px;padding:10px;" title="参数设置" collapsible="false"  closable="false">
         <form id="query" method="post">
			<table width="700px" id="table1">
			
			<tr>
			<td align="center" colspan="4">参数设置</td>
			</tr>
			<tr>
			<td align="right">计算公式：</td>
			<td colspan="5"><img src="../../images/pingfenmoxing.JPG" alt="Theta1"></img></td>
			<!-- <td colspan="5">w[i]=theta1*lm[i].Total/tatal1+theta2*lm2[i].Avg/avg2+theta3*lm[i].Avg/avg1</td> -->
			</tr>
			
				<tr height="30px">
					<td width="20%" align="right">参数一<img src="../../images/theta1.JPG"></img>：</td>
					<td width="22%" align="left">
						<input required="true" id="theta1" class="easyui-numberbox" precision="2" max="10" min="0" name="Theta1" style="width:150px;"/>
					</td>
					<td width="20%" align="right">参数二<img src="../../images/theta2.JPG"></img>：</td>
					<td width="22%" align="left">
						<input required="true" id="theta2" class="easyui-numberbox" precision="2" max="10" min="0" name="Theta2" style="width:150px;"/>
					</td>
				</tr >
				<tr height="30px">
					<td width="20%" align="right">参数三<img src="../../images/theta3.JPG"></img>：</td>
					<td width="22%" align="left">
						<input required="true" id="theta3" class="easyui-numberbox" precision="2" max="10" min="0" name="Theta3" style="width:150px;"/>
					</td>
				</tr >
				<tr ><td colspan="8">------------------------------------------------------------------------------------------------------------------</td></tr>


				<tr height="30px">
					<td width="20%" align="right">等级一所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level1" class="easyui-numberbox" precision="2" max="30" min="0" name="Level1" style="width:150px;"/>
					</td>
					<td width="20%" align="right">等级二所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level2" class="easyui-numberbox" precision="2" max="30" min="0" name="Level2" style="width:150px;"/>
					</td>
				</tr >
				<tr height="30px">
					<td width="20%" align="right">等级三所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level3" class="easyui-numberbox" precision="2" max="30" min="0" name="Level3" style="width:150px;"/>
					</td>
					<td width="20%" align="right">等级四所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level4" class="easyui-numberbox" precision="2" max="30" min="0" name="Level4" style="width:150px;"/>
					</td>
				</tr >
				<tr height="30px">
					
					<td width="20%" align="right">等级五所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level5" class="easyui-numberbox" precision="2" max="30" min="0" name="Level5" style="width:150px;"/>
					</td>
					<td width="20%" align="right">等级六所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level6" class="easyui-numberbox" precision="2" max="30" min="0" name="Level6" style="width:150px;"/>
					</td>
				</tr >
					<tr height="30px">
					<td width="20%" align="right">等级七所占百分比：</td>
					<td width="22%" align="left">
						<input required="true" id="level7" class="easyui-numberbox" precision="2" max="100" min="70" name="Level7" style="width:150px;"/>
					</td>
				</tr >
					
					
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="sub()">修改</a></td>
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">重置</a></td>
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-reload" href="javascript:void(0)" onClick="resub()">应用</a></td>
                   
				</tr>
				<tr >
				<td colspan="8">------------------------------------------------------------------------------------------------------------------</td>
				</tr>
				<tr>
				<td  colspan="5">设置说明：</td>
				</tr>
				<tr>
				<td  colspan="5">1.参数一参数二参数三都必须是0-10内的数字，小数两位有效。</td>
				</tr>
				<tr>
				<td  colspan="5">2.各个等级所占百分比为0-100内的数字，且七个等级的百分比数字加起来不能超过100。</td>
				</tr>
				<tr>
				<td  colspan="5">3.修改按钮的作用是把以上10个参数保存到配置文件中，应用按钮的作用是从配置文件中读取参数，重新计算客户价值等级，此过程不可逆！。</td>
				</tr>
				<tr>
				<td  colspan="5">4.客户价值等级按照以上公式计算出一个w值，按w的值从大到小排序，再按百分比例给客户设定价值等级。</td>
				</tr>
		
				
		</table>
        </form>
		</div>
        <br />
      <div style="width:750px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="7500px" height="500px" ></table>
	  </div>


	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=14" target="PrintFrame" accept-charset="utf-8" >
    
    	<input id="PrintStr"  name="PrintStr" style="width:150px;" type="hidden"/>
				
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>

</DIV>
</DIV>
</body>
</html>
