<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>原始记录模板上传</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
	<script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
	<script>
		var nodekeep="";
		$(function(){
			$('#file_upload').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'103'},	//method必须放在这里，不然会与其他的参数连着，导致出错
				'method'    :'GET',	//需要传参数必须改为GET，默认POST
//				'folder'    : '../../UploadFile',
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '支持格式:xls/xml.', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                'fileExt'   : '*.xls;*.xml',   //允许的格式
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
			  
			$('#tt').tree({
//				url:'/jlyw/ApplianceStandardNameServlet.do?method=2',
				dnd:true,
				onBeforeExpand:function(node){
					if(nodekeep.indexOf("," + node.id+",")==-1)
                    {
						$.ajax({
							type: "POST",
							url: "/jlyw/ApplianceStandardNameServlet.do?method=2&parentid=" + node.id,
							cache: false,
							async: false,
							dataType: "json",
							success: function(data) {
								append(data, node);
							}
						});
					}
				},
				onClick: function(node){
					if($(this).tree('isLeaf',node.target))  //是叶子节点
					{
						//ClickNode(node.attributes.url, node.attributes.title);
						editStandard(node);
					}
					else
					{
						$(this).tree('toggle', node.target);
					}
				}
			});
			
			$.ajax({
				type: "POST",
                url: "/jlyw/ApplianceStandardNameServlet.do?method=2",
                cache: false,
                async: false,
                dataType: "json",
                success: function(data) {
                     	$('#tt').tree('loadData',data);
                }
			});
			
		})
		
		function editStandard(node){      //右键菜单，对点中节点信息进行修改			
			
			//加载模板文件信息
			$('#uploaded_file_table').datagrid('options').queryParams={'FilesetName':node.attributes.FilesetName};
			$('#uploaded_file_table').datagrid('reload');
		}
		
		function reload(){
			var node = $('#tt').tree('getSelected');
			if (node){
				$('#tt').tree('options').url="/jlyw/ApplianceStandardNameServlet.do?method=2&parentid=" + node.id;
				$('#tt').tree('reload', node.target);
			} else {
				$('#tt').tree('options').url="/jlyw/ApplianceStandardNameServlet.do?method=2";
				$('#tt').tree('reload');
			}
		}
		
		//增加子节点
		function append(datas,cnode) {
		     var node = cnode;
		     $('#tt').tree('append', {
		        parent: node.target,
		        data: datas
		    });
		   nodekeep += "," + node.id + ",";
		}
		
	</script>
</head>
<body>
<form id="frm_export" method="post">
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="原始记录模板上传" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV" style="left:0px; margin-left:0px">
<DIV class="easyui-layout" fit="true" nohead="true"   >
	
	<div region="west" style="width:185px;height:600px;border-left:0px; border-top:0px; border-bottom:0px" class="easyui-panel" title="受检器具标准名称">
		<ul id="tt"></ul>
 	</div>

	
	
  	<div region="center"  id="add" name="add"  iconCls="icon-add" closed="false" maximizable="false" minimizable="false" collapsible="false" style="+position:relative;height:600px">
		<div id="p4" class="easyui-panel" style="width:800px;height:250px;"
				title="模板文件（Excel及XML文件）上传" collapsible="false"  closable="false" scroll="no">
			<table width="100%" height="100%" >
				<tr>
					<td width="57%" rowspan="2">
						<table id="uploaded_file_table" class="easyui-datagrid" url="/jlyw/FileDownloadServlet.do?method=4&FileType=103"></table>
					</td>
				 	<td width="43%" height="175" valign="top" align="left" style="overflow:hidden">
					  <div class="easyui-panel" fit="true" collapsible="false"  closable="false"><input id="file_upload" type="file" name="file_upload" /> </div>
					</td>
				</tr>
				<tr>
				  	<td align="center"><a class="easyui-linkbutton" iconCls="icon-save" href="javascript:doUploadByDefault()">上传文件</a> &nbsp;<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:$('#file_upload').uploadifyClearQueue()">取消上传</a> </td>
				</tr>
			</table>
	   </div>
       
	</div>
    
</DIV></DIV></DIV>

</body>

</html>
