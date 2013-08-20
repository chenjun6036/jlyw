<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>新建角色</title>
   <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
     <script type="text/javascript"
			src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		
	<script>

		$(function() {
			$("#Parent").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
			   $("#ParentId").val(record.id);
			},
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
				$(this).combobox('reload','/jlyw/RoleServlet.do?method=8&QueryName='+encodeURI(newValue));
			}	
		})
			
		});
		function savereg(){
			$('#ff').form('submit', {
				url:'/jlyw/RoleServlet.do?method=1',
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
		
		function cancel(){
		   $('#Parent').val(" ");
		   $('#name').val("");
		   $('#Description').val("");
		}
		
	</script>
</head>

<body >
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="新建角色" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div   align="center"  style="padding: 10px;width: 320;height: 550;">
	     
         <div  align="center" >
		     <div id="edit" class="easyui-window" title="请输入请增角色信息" style="padding: 10px;width: 320;height: 250;" 
              iconCls="icon-edit" closed="false" maximizable="false" minimizable="false" collapsible="false">
		
				<form id="ff" method="post">
				   <input type="hidden" name="ParentId" id="ParentId" value="" />
					 <div>
						角  &nbsp;  色&nbsp; 名:<input id="name" style="width:200px"  name="Name" class="easyui-validatebox" required="true"></input>
					 </div>
					 <div>
						 父角色 名称:<input id="Parent"  name="Parent"  style="width:205px;"></input>
					 </div>
					 <div >
					 <table>
					 <tr>
					 	<td>
							<label style="valign:top" >权 限 描 述:</label>
						</td>
						<td>
							<textarea id="Description" style="width:200px;height:100px"  name="Description"  ></textarea>
					 	</td>
					 </tr>
					 </table>
					 </div>
		
				 </form>
				 <div  align="center">
					<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="savereg()">确定</a>
					<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
				 </div>
			 </div>
		</div>
		
	</div>
	
	
</DIV></DIV>	
</body>
</html>
