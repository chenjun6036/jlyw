<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.sql.*,com.jlyw.hibernate.*,java.lang.*"%>
<%
	SysUser loginuser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>�ޱ����ĵ�</title>


	<link rel="stylesheet" id="easyuiTheme" type="text/css" href="../Inc/Style/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../Inc/Style/Style.css" />
	
    <script type="text/javascript" src="../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../Inc/JScript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>	
	<script type="text/javascript" src="../JScript/upload.js"></script>
	<link rel="stylesheet" type="text/css" href="../uploadify/uploadify.css" />
	<script type="text/javascript" src="../uploadify/jquery.uploadify.v2.1.4.js"></script>
	 <script type="text/javascript" src="../Inc/JScript/unback.js"></script>

	<script>
	$(function(){
		
		$('#table').datagrid({
			//title:'֪ͨ',
			singleSelect:true, 
			fit: true,
			nowrap: false,
			striped: true,
			border:false,
			url:'/jlyw/TaskServlet.do?method=0',
			rownumbers:true,
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: true,	//Ĭ�ϰ�������������
//			idField:'id',
			columns:[
				[
					{field:'Type',title:'��������',width:100,align:'center'},
					{field:'TaskTime',title:'����ʱ��',width:100,align:'center'},
					{field:'Content',title:'����',width:290,align:'left', 
						formatter:function(value, rowData, rowIndex){
							if(rowData.Url==""){
								return value;
							}else{
								return "<a style='text-decoration:underline' href='"+rowData.Url+"' target='_self'  ><span style='color: #0033FF'>"+value+"</span></a>"
							}
						}}
					
				]
			],
			pagination:true
		});
		
		$('#overdue').datagrid({
			//title:'֪ͨ',
			singleSelect:true, 
			fit: true,
			nowrap: false,
			striped: true,
			border:false,
			//url:'/jlyw/TaskServlet.do?method=11',
//			sortName: 'userid',
// 			sortOrder: 'desc',
			remoteSort: true,	//Ĭ�ϰ�������������
//			idField:'id',
			columns:[
				[
					{field:'Allotte',title:'�ɶ���',width:80,align:'center'},
					{field:'Days',title:'��������',width:60,align:'center'},
					{field:'Code',title:'ί�е���',width:80,align:'center'},
					{field:'Customer',title:'ί�е�λ',width:160,align:'center'},
					{field:'CommissionDate',title:'ί������',width:100,align:'center'},
					{field:'PromiseDate',title:'��ŵ����',width:100,align:'center'},
					{field:'ApplianceSpeciesName',title:'������Ȩ��',width:100,align:'center'},
					{field:'ApplianceName',title:'��������',width:100,align:'center'},
					{field:'Quantity',title:'����',width:100,align:'center'},
					{field:'WithQuantity',title:'��������',width:100,align:'center'},
					{field:'Status',title:'״̬',width:100,align:'center'},
					{field:'LocaleSiteManager',title:'�ֳ�������',width:100,align:'center'},
					{field:'OtherRequirements',title:'����Ҫ��',width:100,align:'center'},
					{field:'Remark',title:'��ע',width:100,align:'center'}	
				]
			],
			pagination:true,
			rownumbers:true
		});
		
		/*$('#uploaded_file_table1').datagrid({		
			iconCls:'icon-tip',
			idField:'_id',
			fit:true,
			border:false,
			rownumbers:true,
			singleSelect:true,			
			columns:[[
				{field:"filename",title:"�ļ���",width:200,align:"left", 
					formatter : function(value,rowData,rowIndex){
						return "<a style='text-decoration:underline' href='/jlyw/FileDownloadServlet.do?method=0&FileId="+rowData._id+"&FileType="+rowData.filetype+ "' target='_blank' title='������ظ��ļ�' ><span style='color: #0033FF'>"+value+"</span></a>"
					}
				},
				{field:"length",title:"��С",width:60,align:"left"},
				{field:"uploadDate",title:"�ϴ�ʱ��",width:120,align:"left"},
				{field:"uploadername",title:"�ϴ���",width:60,align:"left"}
			]],
			pagination:true
	
		});*/
	
		$.ajax({
			type: "post",
			url: "/jlyw/CrmServlet.do?method=49",
			success: function(data){
				var result = eval("("+data+")");
				if(result.IsOk && result.total > 0){
					console.log(result.total);
					 $.messager.show({
		                title: "Ԥ����ʾ",
		                msg: "<a href='/jlyw/crm/CustomerService/WarningList.jsp' id='warning'>����"+result.total+"���ػ�Ԥ����Ϣ��</a>",
		                showType: 'slide',
		                timeout: 0
		            });
				}
			}
		});
	});
	//��ȫ�˳�ϵͳ
	function doLogout(){
		var result = confirm("������ʾ��Ϣ:�Ƿ�ȷ���˳���ϵͳ��");
		if(result == false){
			return false;
		}
		$.ajax({
			type: "post",
			url: "/jlyw/UserServlet.do?method=5&time=" + new Date().getTime(),
			dataType: "json",	//�������������ݵ�Ԥ������
			beforeSend: function(XMLHttpRequest){
				//ShowLoading();
			},
			success: function(data, textStatus){
				window.parent.location.href="/jlyw/";
			},
			complete: function(XMLHttpRequest, textStatus){
				//HideLoading();
			},
			error: function(){
				//���������
			}
		});
	}
	
	</script>
	<style type="text/css">

	.right_title{
			background:url(rightbg01.gif) repeat-x;
			color:#595a5b;
			text-align:left;
			line-height:23px;
			font-weight:bold;
			padding-top:2px;
			width:99%;
			border-top:0px solid #d6e9fc;
			border-left:0px solid #d6e9fc;
			border-right:0px solid #d6e9fc;
			float:left;
			
	}	
	
	.right_con{
		float:left;
			width:100%;
			border-bottom:0px solid #d6e9fc;
			border-left:0px solid #d6e9fc;
			border-right:0px solid #d6e9fc;
			margin-bottom:10px;
			padding-bottom:0px;
			padding-top:10px;
	}
	
	.right_con div{
		float:left;
		width:80px;
		text-align:center;
	}
	
	.right_con a{
		color:#666;
		text-decoration:none;
	}
	
	.right_con img{
		border:0 none;
		height:50px;
		width:50px;
	}
	
	span.CountStatistic {color:blue}
</style>
</head>
<body>
	<div id="portal" >
	<table >
	<tr height="80px" style="padding-bottom:10px">
		<td colspan="2">
			 <div id="p4" class="easyui-panel" style="width:1042px;"
				title="���ÿ�ݷ�ʽ" collapsible="false"  closable="false" scroll="no">

				<div class="right_con">
					<div><a href="/jlyw/SFGL/wtd/CommissionSheet.jsp" target="_self"><img src="../images/test-management.png" title="�½�ί�е�"/></a><br />
					<a href="/jlyw/SFGL/wtd/CommissionSheet.jsp" target="_self">�½�ί�е�</a></div>
		
					<div ><a href="/jlyw/TaskManage/SearchLocaleMission.jsp" target="_self"><img src="../images/train-design.png" title="�ֳ����ҵ�����"/></a><br />
					<a href="/jlyw/TaskManage/SearchLocaleMission.jsp" id="linkOnlineUsers" target="_self">�ֳ����ҵ�����</a></div>	
					
					<div><a href="/jlyw/SFGL/wtd/CXCommissionSheet.jsp" target="_self"><img src="../images/pen.png" title="�깤ȷ��"/></a><br />					
					<a href="/jlyw/SFGL/wtd/CXCommissionSheet.jsp" target="_self">�깤ȷ��</a></div>	
		
					<div><a href="/jlyw/FeeManage/GenerateDetailList.jsp" target="_self"><img src="../images/train-result.png" title="���ɷ����嵥"/></a><br />
					<a href="/jlyw/FeeManage/GenerateDetailList.jsp" target="_self">���ɷ����嵥</a></div>	
					
					<div><a href="/jlyw/FeeManage/Checkout2.jsp" target="_self"><img src="../images/resource-1.png" title="���˹���"/></a><br />
					<a href="/jlyw/FeeManage/Checkout2.jsp" target="_self">���˹���</a></div>	 
							
					<div><a href="/jlyw/TaskManage/VehicleSchedule.jsp" target="_self"><img src="../images/zhiliang-management.png" title="�������Ȱ���"/></a><br />
					<a href="/jlyw/TaskManage/VehicleSchedule.jsp" target="_self">�������Ȱ���</a></div>	
					
					<div><a href="/jlyw/FeeManage/Quotation.jsp" target="_self"><img src="../images/txt.png" title="�������۵�"/></a><br />
					<a href="/jlyw/FeeManage/Quotation.jsp" target="_self">�������۵�</a></div>	
					
					<div><a href="/jlyw/TaskManage/TaskTime.jsp" target="_self"><img src="../images/search_project.png" title="�޸ĵ�¼����" alt="��δ��ɵļ�������" /></a><br />
					<a href="/jlyw/TaskManage/TaskTime.jsp" id="linkAlarmRuleManager" target="_self">��δ��ɵļ�������</a></div>	    		  
		
					<div><a href="/jlyw/TaskManage/VerifyTask.jsp" target="_self"><img src="../images/3.png" title="���������б�"/></a><br />
					<a href="/jlyw/TaskManage/VerifyTask.jsp" target="_self">���������б�</a></div>
					

					<div><a href="/jlyw/TaskManage/AuthorizeTask.jsp" target="_self"><img src="../images/record-1.png" title="�ҵ���Ȩǩ�������б�"/></a><br />
					<a href="/jlyw/TaskManage/AuthorizeTask.jsp" target="_self">�ҵ���Ȩǩ�������б�</a></div>	
								
					<div><a href="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_self"><img src="../images/chongyong-management.png" title="����ί�е��Ų�ѯҵ��"/></a><br />
					<a href="/jlyw/StatisticLook/MissionLookByCommissionSheetCode.jsp" target="_self">����ί�е��Ų�ѯҵ��</a></div>	
					
					<div><a href="javascript:void(0)" onclick="doLogout()" target="_top" ><img src="../images/logout1.png" title="��ȫ�˳�"/></a><br />
					<a  href="javascript:void(0)" onclick="doLogout()"  target="_top" >��ȫ�˳�</a></div>	    
		
				</div>
			</div>
		</td>
	</tr>
	<tr height="300px">
		<td height="300px">
			<div id="p2" class="easyui-panel" style="width:520px;height:300px;"
				title="�ҵ������б�" collapsible="false"  closable="false" scroll="no">
				
					<table id="table" class="easyui-datagrid" ></table>
						
		    </div>	
		</td>
		<td height="300px">
			<!--<div id="p1" class="easyui-panel" style="width:520px;height:300px;"
				title="����ϴ��Ĺ����ļ�" collapsible="false"  closable="false" scroll="no">
					
					<table  class="easyui-datagrid" id="uploaded_file_table1" url="/jlyw/FileDownloadServlet.do?method=1&FileType=105&FilesetName=20120525102011109_4380"></table>
						
		    </div>-->
            <div id="p1" class="easyui-panel" style="width:520px;height:300px;"
				title="������Ϣ" collapsible="false"  closable="false" scroll="no">
					
					<table  class="easyui-datagrid" id="overdue" url="/jlyw/TaskAssignServlet.do?method=11"></table>
						
		    </div>
		</td>
	</tr>
	
	<tr height="172px" valign="top">
		<td height="150px">
			<div id="p3" class="easyui-panel" style="width:520px;height:150px;padding-top:10px;padding-left:10px"
				title="��¼��Ϣ" collapsible="false"  closable="false" scroll="no">
				<!--<table>
				<tr>
					<td colspan="2">
						��ӭ����${sessionScope.LOGIN_USER.name}
					</td>
				</tr>
				<tr>
					<td >
						�����ϴε�½��ʱ�䣺${sessionScope.LastLoginTime}
					</td>
					<td>
					    �����ϴε�½��IP��ַ��${sessionScope.LastLoginIp}
					</td>					
				</tr>
				<tr>
					<td >
						�������ε�½��ʱ�䣺${sessionScope.LoginTime}
					</td>
					<td>
					    �������ε�½��IP��ַ��${sessionScope.LoginIp}
					</td>		
				</tr>
				</table>-->
					<div style="height:22px">��ӭ����${sessionScope.LOGIN_USER.name}</div>
					<%
						//if(request.getSession().getAttribute("isOverdue").toString().equals("true")){
						String Overdue = (String)session.getAttribute("isOverdue");
						boolean isOverdue = (Overdue.equals("1")?true:false);
						if(isOverdue){
					%>
						<div style="height:22px">��<a style="text-decoration:underline" href="/jlyw/TaskManage/OverdueTask.jsp" target="_self"><span style='color: #0033FF'>��Ҫ��������������ί�е�������${sessionScope.OverdueNumber}</span></a></div>
					<%
						}
					%>
					<% 
						String Withdraw = (String)session.getAttribute("isOverdue");
						boolean isWithdraw = (Overdue.equals("1")?true:false);
						if(isOverdue){
					%>
						<div style="height:22px">��<a style="text-decoration:underline" href="/jlyw/TaskManage/WithdrawTask.jsp" target="_self"><span style='color: #0033FF'>��Ҫ��������������ί�е�������${sessionScope.WithdrawNumber}</span></a></div>
					<%
						}
					%>
					<% 
						String Discount = (String)session.getAttribute("isOverdue");
						boolean isDiscount = (Overdue.equals("1")?true:false);
						if(isOverdue){
					%>
						<div style="height:22px;">��<a style="text-decoration:underline" href="/jlyw/FeeManage/DiscountTask.jsp" target="_self"><span style='color: #0033FF'>��Ҫ�����ۿ�������ί�е�������${sessionScope.DiscountNumber}</span></a></div>	
					<%
						}
					%>		
					<div style="height:22px;">�����ϴε�½��ʱ�䣺${sessionScope.LastLoginTime}</div>
					<div style="height:22px;">�����ϴε�½��IP��ַ��${sessionScope.LastLoginIp}</div>				
					<div style="height:22px;">�������ε�½��ʱ�䣺${sessionScope.LoginTime}</div>
					<div style="height:22px;">�������ε�½��IP��ַ��${sessionScope.LoginIp}</div>			
				  	
		    </div>
	
		</td>
		<td height="150px">
			<div id="p4" class="easyui-panel" style="width:520px;height:150px;padding-top:10px;padding-left:10px"
				title="ϵͳ��Ϣ" collapsible="false"  closable="false" scroll="no">
				 <!--   <div style="height:35px">��ϵͳּ�ڸ�����Ĺ�������ҵ��,��Ա���������ɸ�Ч����ɹ���,�ٽ�������Ϣ�������<br/>&nbsp;&nbsp;��չ.</div> -->
				    <div style="height:25px">���Ƽ��ֱ���1280*800�����ϣ��Ƽ�ʹ��IE7�����ϰ汾�������</div> 
				    <div style="height:25px">���������ӣ�</div> 
					
					<div style="height:20px;padding-left:20px">�����ݼ�����OAϵͳ��<a style='text-decoration:underline' href="http://oa.czjl.net" target="_blank"><span style='color: #0033FF'>http://oa.czjl.net</span></a></div>
					<div style="height:20px;padding-left:20px">������ʡ�ʼ�֣�<a style='text-decoration:underline' href="http://www.jsqts.gov.cn/" target="_blank"><span style='color: #0033FF'>http://www.jsqts.gov.cn/</span></a></div>
<!--					<div style="height:20px;padding-left:20px">������������������<a style='text-decoration:underline' href="http://www.changzhou.gov.cn/" target="_blank"><span style='color: #0033FF'>http://www.changzhou.gov.cn/</span></a></div>-->
					<div style="height:20px;padding-left:20px">���������������ܾ֣�<a style='text-decoration:underline' href="http://www.aqsiq.gov.cn/" target="_blank"><span style='color: #0033FF'>http://www.aqsiq.gov.cn/</span></a></div>
					<!--<div style="height:20px;padding-left:20px">���Ͼ�����ѧ�Ƽ�����<a style='text-decoration:underline' href="http://kjc.njust.edu.cn" target="_blank"><span style='color: #0033FF'>http://kjc.njust.edu.cn</span></a></div>-->
		    </div>
		</td>
		
	</tr>
	
	
	
	
    </table>
 </div>
 			
</body>
</html>
