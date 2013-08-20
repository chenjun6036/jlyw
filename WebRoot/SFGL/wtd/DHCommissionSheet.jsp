<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��λ����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<script>
	function doLoadCommissionSheet(){	//����ί�е�
		$("#SearchForm").form('submit', {
			url:'/jlyw/CommissionSheetServlet.do?method=3',
			onSubmit: function(){
					// do some check
					// return false to prevent submit;
				$("#CommissionSheetForm").form('clear');
				
				$("#CommissionSheetId").val('');				
				//$("#Ness").removeAttr("checked");	//ȥ��ѡ
				return $("#SearchForm").form('validate');
			},
			success:function(data){
				var result = eval("("+data+")");
				if(result.IsOK){
/*					if(result.CommissionObj.Status==0||result.CommissionObj.Status==1||result.CommissionObj.Status==2||result.CommissionObj.Status==3)
					{
						$.messager.alert('��ʾ','��ί�е�����״̬����ִ�й�λ���ز�����','warning');
						return;
					}*/
					$("#CommissionSheetForm").form('load',result.CommissionObj);
					if(result.CommissionObj.Ness == 0){
						$("#Ness").attr("checked",true);	//��ѡ
					}					
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	}
	
	function TurnBack(){
		if($('#CommissionId').val()==''){
			$.messager.alert('��ʾ','���Ȳ�ѯί�е���','info');
			return false;
		}
		var result = confirm("ȷ�ϵ��ظ�ί�е���");
		if(result == false){
			return false;
		}
		if($('#type').val()=="0"){
			$.messager.alert('��ʾ','��ѡ�񵲻����ͣ�','info');
			return false;
		}
		$.ajax({
			url:$('#type').val()=="1"?'/jlyw/CommissionSheetServlet.do?method=13':'/jlyw/CommissionSheetServlet.do?method=14',
			type:'post',
			data:'CommissionId='+$('#CommissionId').val(),
			dataType:'html',
			success:function(data){
				var result = eval("("+data+")");
				$.messager.alert('��ʾ',result.msg,'info');
			}
		});
	}
	
	function OpenCancelWindow(){
		$('#del').window('open');
		$('#ff1').show();
		$('#delCommissionId').val($("#CommissionId").val());
	}
	
	function del(){
		if($('#CommissionId').val()==''){
			$.messager.alert('��ʾ','���Ȳ�ѯί�е���','info');
			return false;
		}
		$('#ff1').form('submit',{
			url:'/jlyw/CommissionSheetServlet.do?method=12',
			onSubmit:function(){
				var result = confirm("�˲��������棬ȷ��ע����");
				if(result == false){
					return false;
				}
				return $('#ff1').form('validate');
			},
			success:function(data){
				var result = eval("(" + data + ")");
		   		$.messager.alert('��ʾ',result.msg,'info');
		   		if(result.IsOK)
		   		 	$('#del').dialog('close');	
			}
		});
	}
	
	</script>
</head>

<body>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��λ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">


          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="ί�е���ѯ" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="990px" id="table1">
				<tr >
					<td width="10%" align="right" >ί�е���ţ�</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
    <%@ include file="/Common/CommissionSheetInfo.jsp"%>
    <br/>
    <div id="p2" class="easyui-panel" style="width:1005px;height:40px;padding:20px; "
				title="���ز���" collapsible="false"  closable="false" scroll="no">
                    <table width="900px">
                    <tr>
                        <td align="center">�������ͣ�</td>
                        <td align="left">
                        	<select id="type" name="type" style="width:152px">
                            	<option value="0" selected="selected">��ѡ��....</option>
                                <option value="1" >�ѽ���--->���깤</option>
                                <option value="2" >��ע��--->���ռ�</option>
                            </select>
                        </td>
                        <td align="left"><a class="easyui-linkbutton" iconCls="icon-undo" href="javascript:void(0)" onClick="TurnBack()">��λ����</a></td>
                        <td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-remove" href="javascript:void(0)" onClick="OpenCancelWindow()">ע��</a></td>	
                    </tr>
                    </table>
	   </div>
        <div id="del" class="easyui-window" title="ע��" style="width:280px;height:150px;" 
        iconCls="icon-remove" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <form id="ff1" method="post">
            <table style="width:250px;height:100px;" >
            <input id="delCommissionId" name="CommissionId" type="hidden"/>
            <tr>
                <td align="center">ע��ԭ��</td>
                <td align="left"><input id="reason" class="easyui-combobox" name="Reason" url="/jlyw/ReasonServlet.do?method=0&type=14" style="width:152px;" valueField="name" textField="name" panelHeight="auto" required="true" /></td>
            </tr>
            <tr height="30px">	
                <td width="125" align="right"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="del()">ע��</a></td>
                <td width="125" align="center"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="javascript:$('#del').dialog('close');">ȡ��</a></td>
            </tr>
            </table>
            </form>
        </div>
</DIV></DIV>
</body>
</html>
