<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>转包信息管理</title>
<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<script type="text/javascript" src="../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../JScript/upload.js"></script>
	
	<script>
$(function(){
		   $('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'104'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../uploadify/selectfiles.png',
				'fileDesc'  : '支持格式:rar/zip/jpg/jpeg/png/bmp/gif/pdf.', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                'fileExt'   : '*.rar;*.zip;*.jpg;*.jpeg;*.gif;*.png;*.bmp;*.pdf;',   //允许的格式
				onComplete: function (event,ID,fileObj,response,data) {  
            		var retData = eval("("+response+")");
					if(retData.IsOK == false){
						$.messager.alert('提示',retData.msg,'error');
					}
			    },
				onAllComplete: function(event,data){
					$('#uploaded_file_table').datagrid('reload');
					CloseWaitingDlg();
				}
			  });
		  
		$("#SubContractor").combobox({
			valueField:'name',
			textField:'name',
			required:true,
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
				$(this).combobox('reload','/jlyw/SubContractServlet.do?method=0&QueryName='+newValue);
			}
		});
 		$("#Handler").combobox({
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
		$("#Receiver").combobox({
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
		$('#subcontract_info_table').datagrid({
			title:'转包业务信息',
//			iconCls:'icon-save',
//			width:900,
			height:200,
			singleSelect:true, 
			fit: false,
			nowrap: false,
			striped: true,
//			collapsible:true,
			url:'/jlyw/SubContractServlet.do?method=3',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: false,
			idField:'SubContractId',
			frozenColumns:[[
				{field:'ck',checkbox:true}
			]],
			columns:[
				[
					{title:'转包方信息',colspan:3,align:'center'},
					{title:'转包业务信息',colspan:8,align:'center'}
				],[
					{field:'SubContractorName',title:'转包方',width:80,sortable:true,align:'center'},
					{field:'SubContractorContactor',title:'联系人',width:60,align:'center'},
					{field:'SubContractorContactorTel',title:'联系电话',width:80,align:'center'},
					{field:'SubContractDate',title:'转包时间',width:80,align:'center'},
					{field:'Handler',title:'转包人',width:80,align:'center'},
					{field:'TotalFee',title:'转包费用',width:80,align:'center'},
					{field:'ReceiveDate',title:'接收时间',width:80,align:'center'},
					{field:'Receiver',title:'接收人',width:80,align:'center'},
					{field:'Remark',title:'备注信息',width:100,align:'center'},
					{field:'LastEditor',title:'最后编辑人',width:80,align:'center'},
					{field:'LastEditTime',title:'最后编辑时间',width:80,align:'center'}
				]
			],
			pagination:false,
			rownumbers:true	,
			onSelect:function(rowIndex, rowData){
				//更新附件表格文件信息
				$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':rowData.Attachment};
				$('#uploaded_file_table').datagrid('reload');
				//load 表单
				$('#subcontract-submit-form').form('load', rowData);
				$("#SubContractor").combobox('setValue', rowData.SubContractorName);
			},
			onLoadSuccess:function(data){
				if(data.rows.length > 0){
					$(this).datagrid('selectRow', 0);
				}
			}
		});
		doLoadCommissionSheet();
});
function doLoadCommissionSheet(){	//查找委托单
	$("#SearchForm").form('submit', {
		url:'/jlyw/CommissionSheetServlet.do?method=3',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			$("#CommissionSheetForm").form('clear');
			$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':''};
			$('#subcontract_info_table').datagrid('loadData',{total:0,rows:[]});
			
			//清空附件列表
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':''};
			$('#uploaded_file_table').datagrid('reload');
			
			
			$("#CommissionSheetId").val('');				
			//$("#Ness").removeAttr("checked");	//去勾选
			return $("#SearchForm").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				$("#CommissionSheetForm").form('load',result.CommissionObj);
				if(result.CommissionObj.Ness == 0){
					$("#Ness").attr("checked",true);	//勾选
				}
				
				//加载转包业务信息
				$('#subcontract_info_table').datagrid('options').queryParams={'CommissionId':$('#CommissionId').val()};
				$('#subcontract_info_table').datagrid('reload');
				
				$("#CommissionSheetId").val(result.CommissionObj.CommissionId);
				
			}else{
				$.messager.alert('查询失败！',result.msg,'error');
			}
		}
	});  
}
function doSubmitNewSubContract(){   	//提交录入转包记录申请
	$("#subcontract-submit-form").form('submit', {
		url:'/jlyw/SubContractServlet.do?method=1',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			var projectValue = $('#CommissionSheetId').val();
			if(projectValue==''){
				$.messager.alert('提示！',"请选择需要转包的委托单！",'info');
				return false;
			}
			return $("#subcontract-submit-form").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//重新加载任务分配信息
				$('#subcontract_info_table').datagrid('reload');
				
				//清空转包方
				$('#SubContractor').combobox('clear');
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});  
}  
function doSubmitEditSubContract(){   	//提交修改转包记录申请
	$("#subcontract-submit-form").form('submit', {
		url:'/jlyw/SubContractServlet.do?method=2',
		onSubmit: function(){
				// do some check
				// return false to prevent submit;
			var row = $("#subcontract_info_table").datagrid('getSelected');
			if(row){
				/*var result = confirm("您确定要注销该任务吗？");
				if(result == false){
					return false;
				}*/
				$('#SubContractId').val(row.SubContractId);
			}else{
				$.messager.alert('提示！',"请先选择一条转包记录",'info');
				return false;
			}
			return $("#subcontract-submit-form").form('validate');
		},
		success:function(data){
			var result = eval("("+data+")");
			if(result.IsOK){
				//重新加载任务分配信息
				$('#subcontract_info_table').datagrid('reload');
				
				//清空转包方
				$('#SubContractor').combobox('clear');
			}else{
				$.messager.alert('提交失败！',result.msg,'error');
			}
		}
	});  
}  
//function uploadifyUpload(){ 
// $('#file_upload').uploadifyUpload(); 
// var str="正在处理上传的文件";
// 
// //waiting(str) ;
// Show();
//} 

function ok(){
	 $('#allot').form('submit',{
		//url: 'userAdd.action',
		onSubmit:function(){ return $('#allot').form('validate');},
		success:function(){
			 close1();
		 }
	});
}
	   // document.getElementById('waiting').style.visibility='hidden'; 
    
	  // function wait() { 
//	    //document.all.ly.style.display="block";
//
//		document.getElementById('waiting').style.visibility='visible'; 
//	   } 
//	    function complete() { 
//		document.getElementById('waiting').style.visibility='hidden'; 
//	   } <br />
//        var c = $(window.parent.document.body) 
//       function wait() { 
//          parent.document.getElementById('waiting2').style.visibility='visible'; 
//	   } 
//	    function complete() { 
//		//$(window.parent.document.body).getElementById('waiting2').style.visibility='hidden'; 
//		parent.document.getElementById('waiting2').style.visibility='hidden'; 
//	   } 
	</script>
</head>

<body>
  <DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="转包信息管理" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
   <div >
          <div id="p" class="easyui-panel" style="width:1005px;padding:10px;"
				title="委托单查询" collapsible="false"  closable="false">
		<form id="SearchForm" method="post" >
			<table width="850px" id="table1">
				<tr >
					<td width="10%" align="right" >委托单编号：</td>
					<td width="22%" align="left" >
						<input id="Code" class="easyui-validatebox" name="Code" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Code") %>" />
					</td>

					<td width="10%" align="right">密  码：</td>
					<td width="22%" align="left">
						<input id="Pwd" class="easyui-validatebox" name="Pwd" style="width:150px;" required="true" value="<%= request.getParameter("Code")==null?"":request.getParameter("Pwd") %>" />
					</td>
					<td width="25%"  align="center"><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onClick="doLoadCommissionSheet()">查询</a></td>
					
				</tr >
		</table>
		</form>

	 </div>
	 <br/>	
     <div id="p" class="easyui-panel" style=" padding-bottom:10px;width:1005px;padding-top:20px;"
				title="委托单信息" collapsible="false"  closable="false">
			<form id="CommissionSheetForm" method="post">
			<input type="hidden" id="CommissionId" name="CommissionId" value="" />
			<input type="hidden" id="CommissionCode" name="CommissionCode" value="" />
			<input type="hidden" id="CommissionPwd" name="CommissionPwd" value="" />
			<table width="1000px" id="table1">
				<tr>

				  <td width="77" align="right">委托形式：</td>
				  <td width="185"  align="left"><select name="CommissionType" style="width:152px">
                    		<option value="1">送样检测</option>
							<option value="5">其它业务</option>
							<option value="6">自检业务</option>
							<option value="7">现场带回</option>
							<option value="3">公正计量</option>
							<option value="4">形式评价</option>
							<option value="2">现场检测</option>
                  </select></td>
				  <td width="68" align="right">委托日期：</td>
				  <td width="650"  align="left"><input style="width:151px;" class="easyui-datebox" name="CommissionDate" id="CommissionDate" type="text" /></td>
				</tr>
		</table>
		<table width="1000">
			<tr>

			  <td width="77"  align="right">委托单位：</td>
              <td width="187" align="left"><input type="text" name="CustomerName" id="CustomerName" style="width:152px;" /></td>
                <td width="64" align="right">电&nbsp;&nbsp;&nbsp;&nbsp;话：</td>
			  <td width="171"  align="left"><input name="CustomerTel" id="CustomerTel" type="text" /></td>
				<td width="64" align="right">地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
			  <td width="168" align="left"><input name="CustomerAddress" id="CustomerAddress" type="text" /></td>

				<td width="65" align="right">邮政编码：</td>
			  <td width="168" align="left"><input name="CustomerZipCode" id="CustomerZipCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">联&nbsp;系&nbsp;人：</td>
				<td align="left"><input name="ContactPerson" id="ContactPerson" type="text" /></td>
				<td align="right">手机号码：</td>

				<td align="left"><input name="ContactorTel" id="ContactorTel" type="text" /></td>
				<td align="right">证书单位：</td>
				<td align="left"><input name="SampleFrom" id="SampleFrom" type="text" /></td>
                <td align="right">开票单位：</td>
				<td align="left"><input name="BillingTo" id="BillingTo" type="text" /></td>
			</tr>
		</table><br/>
 		 <table id="table2" width="1000">

			<tr>
				<td width="77" align="right">器具名称：</td>
                <td width="187" align="left"><input id="ApplianceName" name="ApplianceName" type="text" /></td>
                <td width="64" align="right">型号规格：</td>
			  <td width="171"  align="left"><input id="Model" name="Model" type="text" /></td>
				<td width="64" align="right">出厂编号：</td>
			  <td width="168" align="left"><input id="ApplianceCode" name="ApplianceCode" type="text" /></td>

				<td width="65" align="right">管理编号：</td>
			  <td width="168" align="left"><input id="ApplianceManageCode" name="ApplianceManageCode" type="text" /></td>
			</tr>
			<tr>
				<td align="right">制 造 厂：</td>
				<td align="left"><input id="Manufacturer" name="Manufacturer" type="text"  /></td>
				<td align="right">数&nbsp;&nbsp;&nbsp;&nbsp;量：</td>

				<td align="left"><input id="Quantity" name="Quantity" type="text"/>件</td>
				<td align="right">是否强检：</td>
				<td align="left">
					<select id="Mandatory" name="Mandatory" style="width:152px">
						<option selected="selected" value="1" >非强制检定</option>
						<option value="0">强制检定</option>
					</select>

				</td>
                <td align="left"><input id="Ness" name="Ness" type="checkbox" />加&nbsp;&nbsp;急</td>
				<td align="left">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">外观附件：</td>
				<td align="left"><input id="Appearance" name="Appearance" type="text"  /></td>

				<td align="right">其他要求：</td>
				<td align="left"><input id="OtherRequirements" name="OtherRequirements" type="text"  /></td>
				<td align="right"></td>
				<td align="left"></td>
                <td align="left"></td>
				<td align="left">&nbsp;</td>
			</tr>
	 	 </table><br/>

		</form>
		</div>
		<br/>
		<div id="p2" class="easyui-panel" style="width:1005px;padding:10px;"
				title="转包业务信息" collapsible="false"  closable="false">
			<table id="subcontract_info_table" iconCls="icon-tip" width="1000px" height="150px"></table>
			<br />
			<form id="subcontract-submit-form" method="post">
			<input type="hidden" name="CommissionSheetId" id="CommissionSheetId" value="" />
			<input type="hidden" name="SubContractId" id="SubContractId" value="" />
			<table width="950">
				<tr>
				    <td width="60" align="right" >转包方：</td>
					<td width="150" align="left"><select name="SubContractor" id="SubContractor" style="width:152px"></select></td>
					<td width="60" align="right" >转包时间：</td>
					<td width="168"  align="left"><input id="SubContractDate"   name="SubContractDate" type="text" class="easyui-datebox" style="width:152px;" /></td>
					<td width="80" align="right" >转包人：</td>
					<td width="150" align="left"><select name="Handler" id="Handler" style="width:152px"></select></td>
					<td width="60" align="right" >转包费用：</td>
					<td width="150"  align="left"><input id="TotalFee" name="TotalFee" type="text" style="width:110px" class="easyui-numberbox" precision="2">元</td>
				</tr>
				<tr>
					<td align="right" >接收时间：</td>
					<td align="left"><input id="ReceiveDate"   name="ReceiveDate" type="text" class="easyui-datebox" style="width:152px;" /></td>
					<td align="right" >接收人：</td>
					<td align="left"><select name="Receiver" id="Receiver" style="width:152px"></select></td>
					<td align="right" >备注信息：</td>
					<td colspan="3" rowspan="2" align="left"><textarea id="Remark" style="width:350px;height:80px"  name="Remark" class="easyui-validatebox" ></textarea></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="2" align="right"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitNewSubContract()">增加转包记录</a>&nbsp;&nbsp;</td>
					<td colspan="2" align="left">&nbsp;&nbsp;<a class="easyui-linkbutton" iconCls="icon-edit" href="javascript:void(0)" onClick="doSubmitEditSubContract()">修改转包记录</a></td>
					<td colspan="4" align="left">  </td>
				</tr>
		  </table>
		  </form>
		</div>
		<br/>
		<div id="p4" class="easyui-panel" style="width:1005px;height:200px;"
				title="文件上传" collapsible="false"  closable="false" scroll="no">
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
				  	<td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:var rowSelected=$('#subcontract_info_table').datagrid('getSelected');if(rowSelected==null || rowSelected.Attachment!=$('#uploaded_file_table').datagrid('options').queryParams.FilesetName){$.messager.alert('提示', '请先选择附件所属的转包记录！','info'); return false;} doUploadByDefault()">上传文件</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">取消上传</a> </td>
				</tr>
			</table>
	   </div>	 
	</div>
	 

</DIV></DIV>
</body>
</html>
