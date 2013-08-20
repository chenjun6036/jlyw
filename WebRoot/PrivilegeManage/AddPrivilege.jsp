<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    <title>新建权限</title>
	 <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript">
	$(function() {
		$("#ResourcesName").combobox({
						url:'/jlyw/SysResourcesServlet.do?method=1',
						valueField:'id',
						textField:'name',
						
						onSelect:function(record){
							$("#MappingUrl").val(record.url);
							$("#ResourcesId").val(record.id);
						},
						onChange:function(newValue, oldValue){
							
						}
		 });
					
					
					
	
	});
	    function cancel(){
	        $('#Name').val("");
			$('#MappingUrl').val("");
			$('#Description').val("");  
			$('#Parameters').val(""); 
			$('#ResourcesName').val(""); 
	    }
		function savereg() {
			$('#ff').form('submit', {
				url:'/jlyw/PrivilegeServlet.do?method=1',
				success : function(data) {
					var result = eval("("+data+")");
		   			alert(result.msg);
		   			if(result.IsOK)
		   				cancel();
				},
				onSubmit : function() {
					return $(this).form('validate');
				}
			});
		}
	</script>
  </head>
  
 <body >
  
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="新建权限" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	     
         <div  align="center" >
		     <div id="edit" class="easyui-panel" title="请输入请增权限信息" style="padding: 50px;width: 500;height:500;align:left;" 
              iconCls="icon-edit" closed="false" maximizable="false" minimizable="false" collapsible="false">
		
				<form id="ff" method="post">
				 <div style="height: 45;">
					<label >权限名称：</label><input id="Name" style="width:200px"  name="Name" class="easyui-validatebox" required="true"></input>
				 </div>
				  <div style="height: 45;">
					<label >资源名称：</label><input id="ResourcesName" style="width:202px"  name="ResourcesName" class="easyui-combobox" required="true" ></input>
				 </div>
				 <div style="height: 45;">
					<label >资源 URL：</label><input id="MappingUrl" style="width:200px"  name="MappingUrl" class="easyui-validatebox" readonly="readonly"><input id="ResourcesId" style="width:200px"  name="ResourcesId" type="hidden" class="easyui-validatebox" required="true"></input>
				 </div>
				 <div style="height: 45;">
					<label >参&nbsp;&nbsp;&nbsp;&nbsp;数：</label><input id="Parameters" style="width:200px"  name="Parameters" ></input>
				 </div>
				 <div style="height: 120;">
				  <table>
					 <tr>
					 	<td>
							<label style="valign:top" >权限描述：</label>
						</td>
						<td>
							<textarea id="Description" style="width:200px;height:100px"  name="Description"  ></textarea>
						</td>
					</tr>
				</table>
				 </div>
		 
				 <div  style="height: 45;">
					<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="savereg()">确定</a>
					<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">取消</a>
				 </div>
			</form>
			 </div>
		</div>
		

</div>
</div>
	
</body>
</html>
