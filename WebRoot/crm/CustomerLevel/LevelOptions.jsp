<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ҵ���ѯ</title>
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
		   				//$.messager.alert('��ʾ',result.Theta1,'info');
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
				$.messager.alert('��ʾ',"����������Ҫ����������ԣ�",'info');
				return false;
				}
				
				return $('#query').form('validate');
				
				},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   				$.messager.alert('��ʾ',result.msg,'info');
		   				$("#query").form('load',result);
		   		 }
			});
	}
	function resub()
	{
	ShowWaitingDlg("���ڸ��£���Լ��Ҫ2���ӣ����Ժ�......");
	$('#query').form('submit',{
				url: '/jlyw/CrmServlet.do?method=45',
				onSubmit:function(){},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			CloseWaitingDlg();
		   			$.messager.alert('��ʾ',result.msg,'info');
		   		 }
			});
	
	}
		</script>
</head>

<body >
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ȼ�����" />
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
     <div id="p" class="easyui-panel" style="width:750px;height:500px;padding:10px;" title="��������" collapsible="false"  closable="false">
         <form id="query" method="post">
			<table width="700px" id="table1">
			
			<tr>
			<td align="center" colspan="4">��������</td>
			</tr>
			<tr>
			<td align="right">���㹫ʽ��</td>
			<td colspan="5"><img src="../../images/pingfenmoxing.JPG" alt="Theta1"></img></td>
			<!-- <td colspan="5">w[i]=theta1*lm[i].Total/tatal1+theta2*lm2[i].Avg/avg2+theta3*lm[i].Avg/avg1</td> -->
			</tr>
			
				<tr height="30px">
					<td width="20%" align="right">����һ<img src="../../images/theta1.JPG"></img>��</td>
					<td width="22%" align="left">
						<input required="true" id="theta1" class="easyui-numberbox" precision="2" max="10" min="0" name="Theta1" style="width:150px;"/>
					</td>
					<td width="20%" align="right">������<img src="../../images/theta2.JPG"></img>��</td>
					<td width="22%" align="left">
						<input required="true" id="theta2" class="easyui-numberbox" precision="2" max="10" min="0" name="Theta2" style="width:150px;"/>
					</td>
				</tr >
				<tr height="30px">
					<td width="20%" align="right">������<img src="../../images/theta3.JPG"></img>��</td>
					<td width="22%" align="left">
						<input required="true" id="theta3" class="easyui-numberbox" precision="2" max="10" min="0" name="Theta3" style="width:150px;"/>
					</td>
				</tr >
				<tr ><td colspan="8">------------------------------------------------------------------------------------------------------------------</td></tr>


				<tr height="30px">
					<td width="20%" align="right">�ȼ�һ��ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level1" class="easyui-numberbox" precision="2" max="30" min="0" name="Level1" style="width:150px;"/>
					</td>
					<td width="20%" align="right">�ȼ�����ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level2" class="easyui-numberbox" precision="2" max="30" min="0" name="Level2" style="width:150px;"/>
					</td>
				</tr >
				<tr height="30px">
					<td width="20%" align="right">�ȼ�����ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level3" class="easyui-numberbox" precision="2" max="30" min="0" name="Level3" style="width:150px;"/>
					</td>
					<td width="20%" align="right">�ȼ�����ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level4" class="easyui-numberbox" precision="2" max="30" min="0" name="Level4" style="width:150px;"/>
					</td>
				</tr >
				<tr height="30px">
					
					<td width="20%" align="right">�ȼ�����ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level5" class="easyui-numberbox" precision="2" max="30" min="0" name="Level5" style="width:150px;"/>
					</td>
					<td width="20%" align="right">�ȼ�����ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level6" class="easyui-numberbox" precision="2" max="30" min="0" name="Level6" style="width:150px;"/>
					</td>
				</tr >
					<tr height="30px">
					<td width="20%" align="right">�ȼ�����ռ�ٷֱȣ�</td>
					<td width="22%" align="left">
						<input required="true" id="level7" class="easyui-numberbox" precision="2" max="100" min="70" name="Level7" style="width:150px;"/>
					</td>
				</tr >
					
					
                <tr height="40px">
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="sub()">�޸�</a></td>
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				    <td colspan="2" align="center"><a class="easyui-linkbutton" iconCls="icon-reload" href="javascript:void(0)" onClick="resub()">Ӧ��</a></td>
                   
				</tr>
				<tr >
				<td colspan="8">------------------------------------------------------------------------------------------------------------------</td>
				</tr>
				<tr>
				<td  colspan="5">����˵����</td>
				</tr>
				<tr>
				<td  colspan="5">1.����һ��������������������0-10�ڵ����֣�С����λ��Ч��</td>
				</tr>
				<tr>
				<td  colspan="5">2.�����ȼ���ռ�ٷֱ�Ϊ0-100�ڵ����֣����߸��ȼ��İٷֱ����ּ��������ܳ���100��</td>
				</tr>
				<tr>
				<td  colspan="5">3.�޸İ�ť�������ǰ�����10���������浽�����ļ��У�Ӧ�ð�ť�������Ǵ������ļ��ж�ȡ���������¼���ͻ���ֵ�ȼ����˹��̲����棡��</td>
				</tr>
				<tr>
				<td  colspan="5">4.�ͻ���ֵ�ȼ��������Ϲ�ʽ�����һ��wֵ����w��ֵ�Ӵ�С�����ٰ��ٷֱ������ͻ��趨��ֵ�ȼ���</td>
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
