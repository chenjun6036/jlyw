<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>ת����/���������¼��</title>
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="../JScript/json2.js"></script>
	<script type="text/javascript" src="../JScript/NumberChanger.js"></script>
	<script>
	$(function(){
		$("#FeeAssignForm-FeeAllotee").combobox({
			valueField:'name',
			textField:'name',
			onSelect:function(record){
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
				$(this).combobox('reload','/jlyw/UserServlet.do?method=6&QueryName='+newValue);
			}
		});
		var lastIndex;
		$('#fee_assign_table').datagrid({
			title:'ת����/���������¼��',
			singleSelect:false, 
			height: 300,
//			width:800,
			nowrap: false,
			striped: true,
			url:'/jlyw/CertificateFeeAssignServlet.do?method=4',
			remoteSort: false,
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{field:'FeeAlloteeName',title:'����',width:70,sortable:true,align:'center'},
					{field:'TestFee',title:'�춨��',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'RepairFee',title:'�����',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'MaterialFee',title:'���Ϸ�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'CarFee',title:'��ͨ��',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'DebugFee',title:'���Է�',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'OtherFee',title:'������',width:50,align:'center',editor:{
						type:'numberbox',
						options:{
							precision:0,
							min:0.0
						}
					}},
					{field:'Remark',title:'��ע',width:80,align:'center',editor:{
						type:'text'
					}}
				]
			],
			onClickRow:function(rowIndex){
				$('#fee_assign_table').datagrid('endEdit', lastIndex);
				$('#fee_assign_table').datagrid('beginEdit', rowIndex);
				lastIndex = rowIndex;
			},
			rownumbers:true	,
			pagination:false,
			toolbar:"#fee_assign_table-toolbar"
		});
		
		doLoadCommissionSheet();
	});
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':''};
				$('#fee_assign_table').datagrid('loadData',{total:0,rows:[]});
				
				$("#FeeAssignForm-CommissionSheetId").val('');				
				//$("#Ness").removeAttr("checked");	//ȥ��ѡ
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//��ѡ
					}
					
					//�������������Ϣ
					$('#fee_assign_table').datagrid('options').queryParams={'CommissionSheetId':result.CommissionObj.CommissionId};
					$('#fee_assign_table').datagrid('reload');
					
					$("#FeeAssignForm-CommissionSheetId").val(result.CommissionObj.CommissionId);
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	
	
	/******************         ��ֵ������غ���             ***********************/
	function doDelFeeAssignRecord(){	//ɾ����ѡ�ķ��ü�¼
		var result = confirm("��ȷ��Ҫɾ����ѡ���ü�¼��");
		if(result == false){
			return false;
		}
		$('#fee_assign_table').datagrid('acceptChanges');
		var rows = $('#fee_assign_table').datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
			var index = $('#fee_assign_table').datagrid('getRowIndex', rows[i]);
			$('#fee_assign_table').datagrid('deleteRow', index);
		}
	}
	function doAddAnAllotee(){	//���һ�����÷�����
		if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==""){
			var result = confirm("ȷ��������������ԱΪ����");
			if(result == false){
				return false;
			}
		}
		var rows = $('#fee_assign_table').datagrid('getRows');
		for(var i = 0; i<rows.length; i++){
			if($('#FeeAssignForm-FeeAllotee').combobox('getValue')==rows[i].FeeAlloteeName){
				$.messager.alert('��ʾ��','�Ѵ���:'+ $('#FeeAssignForm-FeeAllotee').combobox('getValue') + '�Ĳ�ֵ���䣬�����ظ���ӣ�' ,'info');
				return false;
			}
		}
		$('#fee_assign_table').datagrid('insertRow', {
			index:rows.length,
			row:{
				FeeAlloteeName:$('#FeeAssignForm-FeeAllotee').combobox('getValue'),
				TestFee:getInt(0),
				RepairFee:getInt(0),
				MaterialFee:getInt(0),
				CarFee:getInt(0),
				DebugFee:getInt(0),
				OtherFee:getInt(0),
				Remark:''
			}
		});
	}
	function doFeeAssign(){	//ȷ���ύ���÷���
		$("#FeeAssignForm").form('submit', {
			url:'/jlyw/CertificateFeeAssignServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;	
				if($('#FeeAssignForm-CommissionSheetId').val()==""){
					$.messager.alert('��ʾ��','����ѡ��һ����Ч��ί�е���','info');
					return false;
				}			
				$('#fee_assign_table').datagrid('acceptChanges');
				var rows = $("#fee_assign_table").datagrid("getRows");						
				$('#FeeAssignForm-FeeAssignInfo').val(JSON.stringify(rows));
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
					$.messager.alert('��ʾ��','������ӳɹ���','info');
				}else{
					$.messager.alert('�ύʧ�ܣ�',result.msg,'error');
				}
			}
		});
	}
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="ת����/���������¼��" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="history.go(-1)">����</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
		<%@ include file="/Common/CommissionSheetInfo.jsp"%>
		<br />
		
		<table id="fee_assign_table" iconCls="icon-tip" width="1005px"></table>
		<br />
		<form id="FeeAssignForm" method="post">
			<input type="hidden" name="CommissionSheetId" id="FeeAssignForm-CommissionSheetId"  />
			<input type="hidden" name="FeeAssignInfo" id="FeeAssignForm-FeeAssignInfo" />
		</form>
		
<div id="fee_assign_table-toolbar" style="padding:2px;">
		<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
				    <td style="text-align:left;padding-left:2px"><a class="easyui-linkbutton" iconCls="icon-save" plain="true" href="javascript:void(0)" onClick="doFeeAssign()">������÷���</a>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-remove" plain="true" href="javascript:void(0)" onClick="doDelFeeAssignRecord()">ɾ��</a>
					</td>
					<td style="text-align:right;padding-right:2px">
					ѡ��Ա����<select name="FeeAllotee" id="FeeAssignForm-FeeAllotee" style="width:152px"></select>&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" plain="true" onClick="doAddAnAllotee()">�������÷���</a>
					</td>
				</tr>
		</table>			
</div>

</DIV></DIV>
</body>
</html>
