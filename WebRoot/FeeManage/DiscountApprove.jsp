<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ۿ�����</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	$(function(){
		$('#overdue_info_table').datagrid({
			title:'�ۿ���Ϣ',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			//url:'/jlyw/DiscountServlet.do?method=0',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true},
					{field:'Id',title:'�ۿ۱��',width:120,align:'center'}
				]],
				columns:[[
					{title:'�ۿ۰�����Ϣ',colspan:4,align:'center'},
					{title:'�ۿ�������Ϣ',colspan:5,align:'center'}
					
				],[
					{field:'ExecutorName',title:'������',width:80,align:'center'},
					{field:'ExecuteTime',title:'����ʱ��',width:80,align:'center'},
					{field:'ExecutorResult',title:'������',width:90,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(0 == value) {
								return '<span style="color:red;">����</span>';
							}
							if(1 == value){
								return "ͬ���ۿ�����";
							}
							return "";
						}	
					},
					
					{field:'ExecuteMsg',title:'��ע��Ϣ',width:80,align:'center'},
					{field:'RequesterName',title:'������',width:80,sortable:true,align:'center'},
					{field:'Reason',title:'����ԭ��',width:150,align:'center'},
					{field:'RequesterTime',title:'����ʱ��',width:150,align:'center'},
					{field:'Contector',title:'ί�з�������',width:90,align:'center'},
					{field:'ContectorTel',title:'�����˵绰',width:90,align:'center'}
				]],
			pagination:false,
			rownumbers:true	
			
		});
		
		doLoadDiscount();	//����ί�е�
	});
	function doLoadDiscount(){	//����ί�е�
			
		$('#overdue_info_table').datagrid('options').url='/jlyw/DiscountServlet.do?method=0';
	
		$('#overdue_info_table').datagrid('options').queryParams={'DiscountId':$('#DiscountId').val()};
		$('#overdue_info_table').datagrid('reload');
	}
	function doSubmitDiscount(ExecutorResult){   	//�ύ�ۿ�������
		$('#ExecutorResult').val(ExecutorResult);
		$("#overdue-submit-form").form('submit', {
			url:'/jlyw/DiscountServlet.do?method=1',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				//�ж�ѡ��ļ�����Ŀ�Ƿ���Ч
				var DiscountIdValue = $('#DiscountId').val();
				if(DiscountIdValue==''){
					$.messager.alert('��ʾ��',"�ۿ����벻���ڣ�",'info');
					return false;
				}
				return $("#overdue-submit-form").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					//���¼������������Ϣ
					//$('#overdue_info_table').datagrid('options').queryParams={'DiscountId':$('#DiscountId').val()};
					$('#overdue_info_table').datagrid('reload');
					$('#discountId').val($('#DiscountId').val())
					//�������������ġ���ע����Ϣ
					$('#ExecuteMsg').val('');
					$.messager.alert('��ʾ��','�����ɹ�','info');
				}else{
					$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
				}
			}
		});  
   }  
   function printDiscount(){
   		//$('#discountId').val('9');
		if($('#discountId').val() == ''||$('#discountId').val().length==0){
			$.messager.alert('��ʾ','��ѡ����Ҫ��ӡ���ۿ۵�','error');
		}
		else{
			
			$('#formLook').submit();
		}
   }
	</script>
</head>

<body>

 <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="�ۿ�����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <div >
       
	
		<div id="p2" class="easyui-panel" style="width:1005px;height:430px;padding:10px;"
				title="�ۿ�����" collapsible="false"  closable="false">
			<table id="overdue_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="overdue-submit-form" method="post">
			<input type="hidden" name="DiscountId" id="DiscountId" value="<%= request.getParameter("DiscountId")==null?"":request.getParameter("DiscountId") %>" />
			<input type="hidden" name="ExecutorResult" id="ExecutorResult" value="" />
			<table width="855" style="margin-left:20px" >
				<tr>
					<td width="50%" align="right">��ע��Ϣ��</td>
					<td align="left"><textarea id="ExecuteMsg" style="width:350px;height:80px"  name="ExecuteMsg" class="easyui-validatebox"></textarea></td>
				</tr>
				
				<tr style="padding-top:15px" >
				  <td height="39" align="right"  style="padding-right:10px;"> <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitDiscount(1)">ͬ������</a> <a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onClick="printDiscount()">��ӡ�ۿ۵�</a></td>
				  <td width="10%"  align="center" ><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="doSubmitDiscount(0)">����</a></td>
			      <td width="52%"  align="left" style="padding-left:10px;"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="javascript:history.go(-1)">����</a></td>
			  </tr>
		  </table>
		  </form>
		</div>
	</div>
<form id="formLook" method="post" action="/jlyw/DiscountServlet.do?method=7" target="FeePrintFrame" >			
	<input type="hidden" name="discountId" id="discountId" />
</form>
<iframe id="FeePrintFrame" name="FeePrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>
</DIV></DIV>
</body>
</html>
