<%@ page contentType="text/html;charset=GB2312" import="com.jlyw.hibernate.*,com.jlyw.manager.*,com.jlyw.util.*" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript" src="js/main.js"></script>
		 
		<%
			String oRecordId = "";
			String fileName = "";
			String doctype = "doc";
			String modFileId = "";	//ģ���ļ�ID(����)
			String modXlsFileId = "";	//ԭʼ��¼Excelģ���ļ���ID�����ڿ���֤������ҳģ�壩
			String version = "-1";
			String staffId = "-1";	//��У��Ա�����������Ա��
			String flagEdit="";			//�޸�֤��ı�־
			if (request.getParameter("OriginalRecordId") != null
					&& !request.getParameter("OriginalRecordId").equals("")) {
				oRecordId = request.getParameter("OriginalRecordId");
			}
			if(request.getParameter("FileName") != null
					&& !request.getParameter("FileName").equals("")){
				fileName = request.getParameter("FileName");
				if(fileName.lastIndexOf('.') != -1){
					doctype = fileName.substring(fileName.lastIndexOf('.') + 1);
				}
			}
			if(request.getParameter("XlsTemplateFileId") != null
					&& !request.getParameter("XlsTemplateFileId").equals("")){
				modXlsFileId = request.getParameter("XlsTemplateFileId");
			}
			if(request.getParameter("Version") != null
					&& !request.getParameter("Version").equals("")){
				version = request.getParameter("Version");
			}
			if(request.getParameter("StaffId") != null 
					&& !request.getParameter("Version").equals("StaffId")){
				staffId = request.getParameter("StaffId");
			}
			if(request.getParameter("FlagEditCertificate") != null){
				flagEdit = request.getParameter("FlagEditCertificate");
			}
			
			//���ļ���Ϊ�գ����ȡ�ļ���
			if((fileName == null || fileName.length() == 0) && oRecordId != null && oRecordId.length() > 0){
				OriginalRecord oRecord = new OriginalRecordManager().findById(Integer.parseInt(oRecordId));
				if(oRecord != null){
					fileName = ExcelUtil.getCertificateModFileName(oRecord.getWorkType(), oRecord.getConclusion());
					if(fileName == null) fileName = "";
				}
			}
		%>
		<title>�޸�֤��</title>

		<!-- --------------------=== ����Weboffice��ʼ������ ===--------------------- -->

		<SCRIPT LANGUAGE=javascript FOR=WebOffice1 EVENT=NotifyCtrlReady>
/****************************************************
*
*	��װ����Weboffice(ִ��<object>...</object>)
*	�ؼ���ִ�� "WebOffice1_NotifyCtrlReady"����
*
****************************************************/
	WebOffice1_NotifyCtrlReady()
	
</SCRIPT>
<SCRIPT LANGUAGE=javascript FOR=WebOffice1 EVENT=NotifyWordEvent(eventname)>
<!--
 WebOffice1_NotifyWordEvent(eventname)
//-->
</SCRIPT>

		<SCRIPT LANGUAGE=javascript>
/****************************************************
*
*		�ؼ���ʼ��WebOffice����
*
****************************************************/
function WebOffice1_NotifyCtrlReady() {
	document.all.WebOffice1.OptionFlag |= 128;
	var flag=<%=flagEdit %>;
	if(flag==1||flag=='1'){
		document.all.WebOffice1.LoadOriginalFile("../FileDownloadServlet.do?method=9&OriginalRecordId=<%=oRecordId %>&DownloadType=1&Version=<%=version%>&XlsTemplateFileId=<%=modXlsFileId %>", "<%=doctype%>");
	}else{
		document.all.WebOffice1.LoadOriginalFile("../FileDownloadServlet.do?method=8&OriginalRecordId=<%=oRecordId %>&DownloadType=1&Version=<%=version%>&XlsTemplateFileId=<%=modXlsFileId %>", "<%=doctype%>");
	}
	allHideMenu();
	hideAll('hideall','hideall','hideall','hideall');
}
var flag=false;
function menuOnClick(id){
	var id=document.getElementById(id);
	var dis=id.style.display;
	if(dis!="none"){
		id.style.display="none";
		
	}else{
		id.style.display="block";
	}
}

function	allHideMenu()
{
	try{
		
		var webObj=document.getElementById("WebOffice1");
		
		webObj.HideMenuAction(1,0x100000+0x200000+0x400000+0x800000+0x1000000+0x2000000+0x4000000+0x8000000+0x10000000);
		webObj. HideMenuAction(5,0);//��������
		//webObj.HideMenuItem(0x01 + 0x8000);
		
		//webObj.ShowToolBar = 0;
		document.all.WebOffice1.HideMenuArea('hideall','hideall','hideall','hideall');
		webObj.HideMenuItem(0x01+0x02+0x1000+0x4000);
	}catch(e){
		alert("�쳣\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}

function ToolBarVanish() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.ShowToolBar = 0;
	}catch(e){
		alert("�쳣\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}


function notMenu() {
	try{
		var webObj=document.getElementById("WebOffice1");
		webObj.SetToolBarButton2("Menu Bar",1,8);
	}catch(e){
		alert("�쳣\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);
	}
}

function hideSeal(){
	var obj;
	try{
        obj = new Object(document.all.WebOffice1.GetDocumentObject());
		if(obj !=null){
         obj.Application.CommandBars("����ӡ��").Visible = !obj.CommandBars("����ӡ��").Visible;
			delete obj;

        
					}
    }catch(e){
    	alert("������ʾӡ�¹���������");
    	}
}


/****************************************************
*
*		����office�¼�������
*
****************************************************/
var vNoCopy = 0;
var vNoPrint = 0;
var vNoSave = 0;
var vClose=0;
function no_copy(){
	vNoCopy = 1;
}
function yes_copy(){
	vNoCopy = 0;
}


function no_print(){
	vNoPrint = 1;
}
function yes_print(){
	vNoPrint = 0;
}


function no_save(){
	vNoSave = 1;
}
function yes_save(){
	vNoSave = 0;
}
function EnableClose(flag){
 vClose=flag;
}
function CloseWord(){
	
  document.all.WebOffice1.CloseDoc(0); 
}

function hideAll(pcExcludeBar1,pcExcludeBar2,pcExcludeBar3,pcExcludeBar4){
	document.all.WebOffice1.HideMenuArea(pcExcludeBar1,pcExcludeBar2,pcExcludeBar3,pcExcludeBar4);
}

function WebOffice1_NotifyWordEvent(eventname) {
	if(eventname=="DocumentBeforeSave"){
		if(vNoSave){
			document.all.WebOffice1.lContinue = 0;
			alert("���ĵ��Ѿ���ֹ����");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="DocumentBeforePrint"){
		if(vNoPrint){
			document.all.WebOffice1.lContinue = 0;
			alert("���ĵ��Ѿ���ֹ��ӡ");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="WindowSelectionChange"){
		if(vNoCopy){
			document.all.WebOffice1.lContinue = 0;
			//alert("���ĵ��Ѿ���ֹ����");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else   if(eventname =="DocumentBeforeClose"){
	    if(vClose==0){
	    	document.all.WebOffice1.lContinue=0;
	    } else{
	    	alert("word");
		    document.all.WebOffice1.lContinue = 1;
		}
 	}
}
</SCRIPT>

	
	</head>
	<body style="background: #ccc; height:100%" onUnload="return window_onunload()">
		<center>
			<div align="center"
				style="width: 1024; height: 100%; background: #fff; margin: 0 0 0 0; padding: 10px 0 0 0">

				<form name="myform">
					<table class="TableBlock" width="90%">
						<tr class="TableHeader">
							<td colspan="2">
								�ĵ�����
							</td>
						</tr>
						<tr>
							<td class="TableData" width="80%">
								�ļ���:
								<input name="FileName" value="<%=fileName %>" size="28" readonly="readonly">
								<span class="STYLE1"> | </span> ��ǰ�û�:
								<input name="LoginUser" value="<%=session.getAttribute("LOGIN_USER")==null?"":((SysUser)session.getAttribute("LOGIN_USER")).getName() %>" size="14" readonly="readonly">
								
								<span class="STYLE1"> </span>
							</td>
							<td align="right">
								<input name="button" type=button
									onClick="javascript:self.close()"
									value="  ����  ">&nbsp;
								<% if(session.getAttribute("LOGIN_USER")!=null && staffId.equalsIgnoreCase(((SysUser)session.getAttribute("LOGIN_USER")).getId().toString())){ %>
								<input name="button93" type="button"
									onClick="return SubmitEditCertificate('<%=fileName%>','<%=oRecordId%>','<%=version%>','<%=flagEdit%>')" value="  �ύ  "
									classs="rollout">
								<% }%>
							</td>
						</tr>
					</table>
					<br>
					<table class="TableBlock" width="89%">
						<tr>
							
							<td width="100%" valign="top" class="TableData"><!-- -----------------------------== װ��weboffice�ؼ� ==--------------------------------- -->
							    <script src="js/LoadWebOffice.js"></script></td>
						</tr>
				  </table>
				</form>
			</div>
		</center>
	</body>
</html>
