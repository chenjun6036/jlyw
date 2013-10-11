<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>����Ա����ѯ�ۿ���Ϣ</title>
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
		$(function(){
			nowDate = new Date();
			$("#DateFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#DateEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			
			$('#Employee').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
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
					$(this).combobox('reload','/jlyw/UserServlet.do?method=16&QueryName='+newValue);
				}
			});
				
			$('#table6').datagrid({
			    width:1000,
				height:500,
				title:'�ۿ���Ϣ',
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
					{field:'Code',title:'ί�е���',width:100,align:'center'},
					{field:'Requester',title:'������',width:80,align:'center'},
					{field:'CommissionDate',title:'ί������',width:80,align:'center'},
					{field:'CustomerName',title:'ί�е�λ',width:80,align:'center'},
					{field:'OldTotalFee',title:'�ܷ��ã�ԭ�ۣ�',width:80,align:'center'},
					{field:'TotalFee',title:'�ܷ��ã��ּۣ�',width:80,align:'center'},
					{field:'OldTestFee',title:'���ѣ�ԭ�ۣ�',width:80,align:'center'},
					{field:'TestFee',title:'���ѣ��ּۣ�',width:80,align:'center'},
					{field:'OldRepairFee',title:'����ѣ�ԭ�ۣ�',width:80,align:'center'},
					{field:'RepairFee',title:'����ѣ��ּۣ�',width:80,align:'center'},
					{field:'OldMaterialFee',title:'���Ϸѣ�ԭ�ۣ�',width:80,align:'center'},
					{field:'MaterialFee',title:'���Ϸѣ��ּۣ�',width:80,align:'center'},
					{field:'OldCarFee',title:'��ͨ�ѣ�ԭ�ۣ�',width:80,align:'center'},
					{field:'CarFee',title:'��ͨ�ѣ��ּۣ�',width:80,align:'center'},
					{field:'OldDebugFee',title:'���Էѣ�ԭ�ۣ�',width:80,align:'center'},
					{field:'DebugFee',title:'���Էѣ��ּۣ�',width:80,align:'center'},
					{field:'OldOtherFee',title:'�������ã�ԭ�ۣ�',width:80,align:'center'},
					{field:'OtherFee',title:'�������ã��ּۣ�',width:80,align:'center'}
                ]],
				pagination:true,
				rownumbers:true
			});
			
		});
		function query(){
			if($('#Employee').combobox('getValue')==""){
				$.messager.alert('��ʾ','��ѡ��һ��Ա��','warning');
				return;
			}
			$('#table6').datagrid('options').url='/jlyw/QueryServlet.do?method=6';
			$('#table6').datagrid('options').queryParams={'EmpId':encodeURI($('#Employee').combobox('getValue')),'DateFrom':encodeURI($('#DateFrom').datebox('getValue')),'DateEnd':encodeURI($('#DateEnd').datebox('getValue'))};
			$('#table6').datagrid('reload');
		}
		</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="����Ա����ѯ�ۿ���Ϣ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:1000px;height:150px;padding:10px;"
				title="��ѯ����" collapsible="false">
			<table width="950px" id="table1">
				<tr height="30px">
					<td align="right">Ա&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����</td>
					<td align="left" colspan="3">
						<input id="Employee" class="easyui-combobox" name="Employee" url="" style="width:150px;" valueField="id" textField="name" panelHeight="150px" /></td>
				</tr >
                <tr height="30px">
                	<td align="right">��ʼʱ�䣺</td>
				  	<td align="left">
						<input name="DateFrom" id="DateFrom" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                    <td align="right">����ʱ�䣺</td>
					<td align="left">
						<input name="DateEnd" id="DateEnd" type="text" style="width:152px;" class="easyui-datebox" />
					</td>
                </tr> 
				<tr height="40px">
                	<td colspan="3"></td>
				    <td align="left"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
				</tr>
				
		</table>
		</div>
        <br />
      <div style="width:1000px;height:500px;">
	     <table id="table6" iconCls="icon-tip" width="1000px" height="500px" ></table>
	  </div>
	  <!--<div id="p2" class="easyui-panel" style="width:900px;height:120px;padding:10px;"
				title="������" collapsible="false"  closable="false">
			<form id="allot" method="post">
			<table width="850px" >
				
				<tr >
				     <td width="33%"  align="right" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search1()">�鿴ԭʼ��¼</a>
	                     
					</td>
					<td  width="33%" align="center" style="padding-top:15px;">
						 <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="search2()">����֤��</a>
	                     
					</td>
					<td  width="33%" align="left" style="padding-top:15px;">
						 
	                     <a class="easyui-linkbutton" iconCls="icon-back" href="javascript:void(0)" onClick="goback()">����</a>
					</td>
					
				</tr>
		  </table>
		  </form>
		</div>-->

</div>
</DIV>
</DIV>
</body>
</html>
