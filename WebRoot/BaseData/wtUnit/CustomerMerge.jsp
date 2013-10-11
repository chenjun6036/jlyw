<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>委托单位合并</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	<script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
			$('#queryname').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
				textField:'name',
				onSelect:function(){},
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
								var newValue = $('#queryname').combobox('getText');
								$('#queryname').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);

					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});

			$('#queryname1').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				valueField:'id',
				textField:'name',
				onSelect:function(){},
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
								var newValue = $('#queryname1').combobox('getText');
								$('#queryname1').combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
						}, 700);

					//$(this).combobox('reload','/jlyw/CustomerServlet.do?method=6&CustomerName='+newValue);
				}
			});
		});
		
		function Merge(){
			var result = confirm("此操作不可逆，确定注销吗？");
			if(result == false){
				return false;
			}
			$('#FId').val($('#queryname').combobox('getValue'));
			$('#LId').val($('#queryname1').combobox('getValue'));
			$('#frm_merge').form('submit',{
				url: '/jlyw/CustomerServlet.do?method=8',
				onSubmit:function(){},
		   		success:function(data){
		   			var result = eval("("+data+")");
		   			$.messager.alert('提示',result.msg,'info');		
		   		}
			});
		}
		
		</script>

</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="委托单位合并" />
		</jsp:include>
	</DIV>
	<form id="frm_merge" method="post">
		<input id="FId" name="FId" type="hidden"/>
		<input id="LId" name="LId" type="hidden"/>
	</form>
	<DIV class="JlywCenterLayoutDIV">
	<div border="true" style="width:900px;overflow:hidden;position:relative;">
		<div>
			<br />
			<table style="width:500px; height:150px; padding-top:10px; padding-left:10px" class="easyui-panel" title="委托单位合并">
            <form id="query">
				<tr>
					<td align="right">合并至单位：</td>
				  	<td align="left"><input class="easyui-combobox" style="width:152px" id="queryname" name="queryName" url="" panelHeight="150px"></input></td>
                    <td align="right">被合并单位：</td>
				  	<td align="left"><input class="easyui-combobox" style="width:152px" id="queryname1" name="queryName1" url="" panelHeight="150px"></input></td>
				</tr>
                <tr>
                	<td colspan="3"></td>
					<td width="100"><a href="javascript:void(0)" onclick="Merge()" class="easyui-linkbutton" iconCls="icon-search">合并</a></td>
				</tr>
                </form>
			</table>
		</div>
        </div>
    </DIV>
    </DIV>
</body>
</html>
