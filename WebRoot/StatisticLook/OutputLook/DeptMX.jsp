<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="com.jlyw.hibernate.*,java.util.*,org.json.me.*" %>
<% 
		String person = request.getParameter("person");//�ж��Ƿ�ֻ��ѯ�������ڿ��Ҳ�ֵ
		String departmentid = request.getParameter("departmentid");
		String departmentname = request.getParameter("departmentname");
		String dateTimeFrom = request.getParameter("dateTimeFrom");
		String dateTimeEnd = request.getParameter("dateTimeEnd");
		String CommissionType = request.getParameter("CommissionType");
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ѯ������ϸ</title>
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/StatusInfo.js"></script>
    <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
        <script type="text/javascript" src="../../JScript/json2.js"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
	
		$(function(){
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
		    $('#departmentid').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(record){
					$('#departmentid1').val(record.Id);
				}
				
			});
			
			if('<%=person%>'=='1'){
				$('#departmentid').combobox('disable');
			}
			
			if('<%=dateTimeFrom%>'!='null'&&'<%=dateTimeEnd%>'!='null'){
				
				$("#dateTimeFrom").datebox('setValue','<%=dateTimeFrom%>');
				$("#dateTimeEnd").datebox('setValue','<%=dateTimeEnd%>');			
				for (var i = 0; i < document.getElementById("CommissionType").options.length; i++) {       
					if (document.getElementById("CommissionType").options[i].value == '<%=CommissionType%>') {       
						document.getElementById("CommissionType").options[i].selected = true;           
						break;       
					}       
				} 
				$('#departmentid').combobox('loadData',[{Id:'<%=departmentid%>',Name:'<%=departmentname%>'}]);
				$('#departmentid').combobox('setValue','<%=departmentid%>');
			 
			}   
			
		});
		
		$(function(){
		    var lastIndex;		
			$('#result').datagrid({
			    width:900,
				height:400,
				title:'���Ų�ֵ��ϸ',
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				showFooter:true,
			  	frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Code',title:'ί�е���',width:100,align:'center'},
					{field:'CommissionDate',title:'ί������',width:100,align:'center'},
				    {field:'CustomerName',title:'ί�е�λ����',width:180,align:'center'},
					
					{field:'ApplianceName',title:'��������',width:120,editor:'text',align:'center'},
					{field:'Model',title:'�ͺŹ��',width:80,editor:'text',align:'center'},
					{field:'Range',title:'������Χ',width:80,editor:'text',align:'center'},
					{field:'Accuracy',title:'��ȷ����/׼ȷ�ȵȼ�/����ʲ�',width:80,editor:'text',align:'center'},
					{field:'Manufacturer',title:'���쳧��',width:80,editor:'text',align:'center'},
					{field:'Quantity',title:'̨/����',width:70,align:'center'},
					//{field:'FinishQuantity',title:'�깤��������',width:70,align:'center'},
					{field:'WithdrawQuantity',title:'��������',width:70,align:'center'},
					{field:'CertificateCode',title:'֤���',width:100,align:'center'},
					{field:'TotalFee',title:'��ֵ',width:60,align:'center'},
					{field:'TestFee',title:'����',width:60,align:'center'},
					{field:'RepairFee',title:'������',width:60,align:'center'},
					{field:'MaterialFee',title:'���Ϸ�',width:60,align:'center'},
					{field:'CarFee',title:'��ͨ��',width:60,align:'center'},
					{field:'DebugFee',title:'���Է�',width:60,align:'center'},
					{field:'OtherFee',title:'��������',width:60,align:'center'},
					{field:'CheckOutStaff',title:'������',width:60,align:'center'},
					{field:'CheckOutTime',title:'��������',width:80,align:'center'},
					{field:'DetailListCode',title:'�����嵥��',width:80,align:'center'},
								
					{field:'FeeAlloteeName',title:'��ֵ��',width:80,align:'center'}
                ]],
				toolbar:[{
					text:'�鿴ί�е���ϸ',
					iconCls:'icon-search',
					handler:function(){
						var select = $('#result').datagrid('getSelected');
						if(select==null)
						{
							$.messager.alert('��ʾ','��ѡ��һ�����ݣ�','warning');
							return;
						}
						$('#SearchForm_Code').val(select.Code);
						$('#SearchForm1').submit();
					}
				}],
				pagination:true,
				rownumbers:true,
				onLoadSuccess:function(data){
					
					
					$('#JDFee').val(data.JDFee);
					$('#JZFee').val(data.JZFee);
					$('#JYFee').val(data.JYFee);
					$('#JCFee').val(data.JCFee);
					
				}
			});
			
			if('<%=dateTimeFrom%>'!='null'&&'<%=dateTimeEnd%>'!='null'){
				 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=7';
				 $('#result').datagrid('options').queryParams={'DepartmentId':encodeURI('<%=departmentid%>'),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'CommissionType':$('#CommissionType').val()};
				 $('#result').datagrid('reload');
				 
				 $('#departmentid1').val(encodeURI('<%=departmentid%>'));
			}
				
		});
		
		function query(){
	   		 if(!$("#searchForm").form('validate'))
				return false ;
			 $('#result').datagrid('loadData', {'total':0, 'rows':[]});
			 $('#result').datagrid('options').url='/jlyw/StatisticServlet.do?method=7';
			 $('#result').datagrid('options').queryParams={'DepartmentId':encodeURI($('#departmentid').combobox('getValue')),'StartTime':encodeURI($('#dateTimeFrom').datebox('getValue')),'EndTime':encodeURI($('#dateTimeEnd').datebox('getValue')),'CommissionType':$('#CommissionType').val()};
			 $('#result').datagrid('reload');
		}
		

		
		function reset()
		{
			if('<%=person%>'!='1'){
				$('#departmentid').combobox('setValue',"");
			}
			
			nowDate = new Date();
			$("#dateTimeFrom").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			$("#dateTimeEnd").datebox('setValue', nowDate.getFullYear()+'-'+(nowDate.getMonth()<9?('0'+(nowDate.getMonth()+1)):(nowDate.getMonth()+1))+'-'+(nowDate.getDate()<10?('0'+nowDate.getDate()):nowDate.getDate()));
			document.getElementById("CommissionType").value="";
		}
		function myPrint(){
			 if(!$("#searchForm").form('validate'))
				return false ;
			if('<%=dateTimeFrom%>'!='null'&&'<%=dateTimeEnd%>'!='null'){
				
			}
			
			$('#StartTime').val($('#dateTimeFrom').datebox('getValue'));
			$('#EndTime').val($('#dateTimeEnd').datebox('getValue'));
			
			$('#CommissionType1').val($('#CommissionType').val());
			
			$('#formLook').submit();
		}
		
		function myExport(){
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#result').datagrid('options').queryParams));
			$('#frm_export').form('submit',{
				success:function(data){
					var result = eval("("+ data +")");
					if(result.IsOK)
					{
						$('#filePath').val(result.Path);
						$('#frm_down').submit();
						CloseWaitingDlg();
					}
					else
					{
						$.messager.alert('��ʾ','����ʧ�ܣ������ԣ�','warning');
						CloseWaitingDlg();
					}
				}
			});
		}	
			
		
	</script>
</head>
<form id="SearchForm1" method="post" action="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_blank">
        <input id="SearchForm_Code" type="hidden" name="Code"/>
</form>
<form id="frm_export" method="post" action="/jlyw/StatisticServlet.do?method=12">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��ѯ������ϸ" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <br />
<div style="+position:relative;">
     <div id="p" class="easyui-panel" style="width:900px;height:135px;padding:10px;"
				title="��ѯ����" collapsible="false"  closable="false">
			<form id="searchForm">
			<table width="850px" id="table1">
				<tr >
					<td colspan="2" align="right" >
					 ��  �ң�<input id="departmentid" class="easyui-combobox" name="department" url="/jlyw/DepartmentServlet.do?method=6" style="width:150px;" valueField="Id" textField="Name" panelHeight="auto"  required="true"/>&nbsp;
				     ��ʼʱ�䣺<input name="date1" id="dateTimeFrom" type="text" style="width:152px;"  class="easyui-datebox" required="true" />&nbsp;
					 ����ʱ�䣺<input name="date2" id="dateTimeEnd" type="text" style="width:152px;"  class="easyui-datebox" required="true" />&nbsp;
					 ί����ʽ��<select name="CommissionType" id="CommissionType" style="width:100px"><option value="" selected="selected">ȫ��</option><option value="1">�������</option><option value="2" >�ֳ����</option><option value="3" >��������</option><option value="4" >��ʽ����</option><option value="5" >����ҵ��</option><option value="6" >�Լ�ҵ��</option><option value="7" >�ֳ�����</option></select>&nbsp;
					</td>
					
				</tr >
				<tr height="40px">
				    <td width="10%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="query()">��ѯ</a></td>
				    <td width="21%"  align="center"><a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onClick="reset()">����</a></td>
				</tr>
				
		</table>
		</form>
		</div>
		
      <div style="width:900px;height:400px;">
	     <table id="result" iconCls="icon-tip" width="900px" height="400px" ></table>
	  </div>
	 
	  <div id="p1" class="easyui-panel" style="width:900px;padding:10px;"
				title="ͳ�ƽ��" collapsible="false"  closable="false" align="center">
                <label id="statistics"></label>
            <table width="870px" >
				<tr height="30px" >
					<td  width="24%" align="center">
					�춨�ѣ�<input id="JDFee" type="text" name="JDFee" value="" style="width:75px;" readonly="readonly" />Ԫ
					</td>
					<td  width="24%" align="center">
					У׼�ѣ�<input id="JZFee" type="text" name="JZFee" value="" style="width:75px;" readonly="readonly"/>Ԫ
					</td>
					<td  width="24%" align="center">
					���ѣ�<input id="JCFee" type="text" name="JCFee" value="" style="width:75px;" readonly="readonly"/>Ԫ
					</td>
					<td  width="24%" align="center">
					����ѣ�<input id="JYFee" type="text" name="JYFee" value="" style="width:75px;" readonly="readonly"/>Ԫ
					</td>					
				</tr>
				<tr height="50px">
					<td   align="center" colspan="2">
					<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onClick="myExport()">����</a>
					</td>	
					<td   align="center" colspan="2">
					<a class="easyui-linkbutton" iconCls="icon-print" href="javascript:void(0)" onClick="myPrint()">��ӡ</a>
					</td>					
				</tr>
		  </table>
		</div>
		
	<form id="formLook" method="post" action="/jlyw/StatisticServlet.do?method=8" target="PrintFrame" accept-charset="utf-8" >
			
		<input id="departmentid1" name="DepartmentId"  type="hidden"/>
		<input name="StartTime" id="StartTime" type="hidden"/>
		<input name="EndTime" id="EndTime" type="hidden" />
		<input name="CommissionType" id="CommissionType1" type="hidden" />
	</form>
	<iframe id="PrintFrame" name="PrintFrame" src="" frameborder="0" width="1px" height="1px" scrolling="no"></iframe>

</div>
</DIV>
</DIV>
</body>
</html>