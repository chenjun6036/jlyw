<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    <title>����ϵͳ��Դ</title>
	 <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript">
		$(function() {
	
		});
	    function cancel(){
	        $('#ResourcesName').val("");
			$('#MappingUrl').val("");
			$('#Description').val("");   
	    }
		function savereg() {
			$('#ff').form('submit', {
				url:'/jlyw/SysResourcesServlet.do?method=2',
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
			<jsp:param name="TitleName" value="����ϵͳ��Դ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	     
         <div  align="center" >
		     <div id="edit" class="easyui-panel" title="����������ϵͳ��Դ��Ϣ" style="padding: 10px;width: 320;height: 250;" 
              iconCls="icon-edit" closed="false" maximizable="false" minimizable="false" collapsible="false">
		
				<form id="ff" method="post">
				  <div>
					<label >��Դ���ƣ�</label><input id="ResourcesName" style="width:200px"  name="ResourcesName" class="easyui-validatebox" required="true"></input>
				 </div>
				 <div>
					<label >��Դ URL��</label><input id="MappingUrl" style="width:200px"  name="MappingUrl" class="easyui-validatebox" required="true"></input>
				 </div>
				 <div >
					 <table>
					 <tr>
					 	<td>
							<label style="valign:top" >��Դ������</label>
						</td>
						<td><textarea id="Description" style="width:200px;height:100px"  name="Description"  ></textarea>
						</td>
					 </tr>
					 </table>
				 </div>
	
			 </form>
				 <div  align="center">
					<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="savereg()">ȷ��</a>
					<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="cancel()">ȡ��</a>
				 </div>
			 </div>
		</div>
		

</div>
</div>
	
</body>
</html>
