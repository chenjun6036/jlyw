<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ϴ�ί�е�����</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
    <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
	<script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
	<script>
	$(function(){
			$('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'104'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:rar/zip/jpg/jpeg/png/bmp/gif/pdf.', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
                'fileExt'   : '*.rar;*.zip;*.jpg;*.jpeg;*.gif;*.png;*.bmp;*.pdf;',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('��ʾ',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table').datagrid('reload');
					CloseWaitingDlg();
				}
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
				$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
				$('#uploaded_file_table').datagrid('reload');
				
				$("#CommissionSheetId").val('');				
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
					$('#beizhu').val(result.CommissionObj.Remark);
					//���ظ�����Ϣ
					$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':result.CommissionObj.Attachment};
					$('#uploaded_file_table').datagrid('reload');
					
					$("#Remark_CommissionId").val(result.CommissionObj.CommissionId);
					
					
				}else{
					$.messager.alert('��ѯʧ�ܣ�',result.msg,'error');
				}
			}
		});  
	} 
	
	function OpenCancelWindow(){
		$('#del').window('open');
		$('#ff1').show();
		$('#delCommissionId').val($("#CommissionId").val());
	}
	
	function doSubmitRemark(){
		if($('#CommissionId').val()==''){
			$.messager.alert('��ʾ','���Ȳ�ѯί�е���','info');
			return false;
		}
		$('#frm_remark').form('submit',{
			url:'/jlyw/CommissionSheetServlet.do?method=11',
			onSubmit:function(){return $(this).form('validate');},
			success:function(data){
				var result = eval("("+data+")");
				$.messager.alert('��ʾ',result.msg,'info');
			}
		});
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
			<jsp:param name="TitleName" value="�ϴ�ί�е�����" />
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
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true"  value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>"  />
					</td>

					<td width="10%" align="right">��  �룺</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true"  value="<%= request.getParameter("Pwd")==null?"":request.getParameter("Pwd") %>"  />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">��ѯ</a></td>
                    <td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-remove" href="javascript:void(0)" onClick="OpenCancelWindow()">ע��</a></td>
					
				</tr >
		</table>
		</form>

		</div>
		<br />
    <%@ include file="/Common/CommissionSheetInfo.jsp"%>
	<br/>
    <div id="p2" class="easyui-panel" style="width:1005px;height:200px;padding:20px; "
				title="�޸ı�ע" collapsible="false"  closable="false" scroll="no">
                <form id="frm_remark" method="post">
                <input type="hidden" id="Remark_CommissionId" name="CommissionId" value="" />
                    <table>
                        <tr valign="middle">
                            <td align="right">ί�е���ע��</td>
                            <td align="left">
                                <textarea id="beizhu" name="Remark" cols="100" rows="8"></textarea>
                            </td>

                        <td align="right"><a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onClick="doSubmitRemark()">�޸ı�ע</a></td>	
                        </tr>
                    </table>
            </form>
	   </div>	
    <br/>
	<div id="p4" class="easyui-panel" style="width:1005px;height:200px;"
				title="ί�е������ϴ�" collapsible="false"  closable="false" scroll="no">
			<table width="100%" height="100%" >
				<tr>
					<td width="57%" rowspan="2">
						<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=104"></table>
					</td>
				 	<td width="43%" height="125" valign="top" align="left" style="overflow:hidden">
						<div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
					</td>
				</tr>
				<tr>
				  	<td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByDefault()">�ϴ��ļ�</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">ȡ���ϴ�</a> </td>
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
