<%@ page contentType="text/html; charset=gbk" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>��Ա��Ϣ����������</title>
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../../Inc/Style/themes/icon2.css" />
    <link rel="stylesheet" type="text/css" href="../../Inc/Style/Style.css" />
    <link rel="stylesheet" type="text/css" href="../../uploadify/uploadify.css" />
    <script type="text/javascript" src="../../Inc/JScript/jquery-1.6.min.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../uploadify/swfobject.js"></script>
    <script type="text/javascript" src="../../uploadify/jquery.uploadify.v2.1.4.js"></script>
    <script type="text/javascript" src="../../Inc/JScript/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="../../JScript/upload.js"></script>
    <script type="text/javascript" src="../../JScript/letter.js"></script>
    <script type="text/javascript" src="../../JScript/ExportToExcel.js"></script>
    <script type="text/javascript" src="../../JScript/json2.js"></script>
	<script>
	 	$(function(){
			$('#Signature').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//			'folder'    : '../../UploadFile',
				'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:jpg/jpeg/png', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
				'fileExt'   : '*.jpg;*.jpeg;*.png;',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
					var retData = eval("("+response+")");
					if(retData.IsOK == false)
						$.messager.alert('��ʾ',retData.msg,'error');
				},
				onAllComplete: function(event,data){
				}
			});
				
			$('#Photograph').uploadify({
				'script'    : '/jlyw/FileUploadServlet.do',
				'scriptData':{'method':'1','FileType':'106'},	//method������������Ȼ���������Ĳ������ţ����³���
				'method'    :'GET',	//��Ҫ�����������ΪGET��Ĭ��POST
		//			'folder'    : '../../UploadFile',
				'queueSizeLimit': 1,//һ��ֻ�ܴ�һ���ļ�
				'buttonImg' : '../../uploadify/selectfiles.png',
				'fileDesc'  : '֧�ָ�ʽ:jpg/jpeg/png', //������������µ�'fileExt'���ԣ���ô��������Ǳ���� 
				'fileExt'   : '*.jpg;*.jpeg;*.png;',   //����ĸ�ʽ
				onComplete: function (event,ID,fileObj,response,data) {  
					var retData = eval("("+response+")");
					if(retData.IsOK == false)
						$.messager.alert('��ʾ',retData.msg,'error');
				},
				onAllComplete: function(event,data){
				}
			});
			
			$('#queryname').combobox({
			//	url:'/jlyw/CustomerServlet.do?method=6',
				onSelect:function(){},
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
			
			$('#DepartmentId').combobox({
				onSelect:function(record){
					$('#ProjectTeamId').combobox('setValue',"");
					$('#ProjectTeamId').combobox('reload','/jlyw/ProjectTeamServlet.do?method=8&DepartmentId='+record.Id);
				}
			});
			
			$('#querystatus').combobox('setValue',"");
			$('#querytype').combobox('setValue',"");
			
		});
		
		function query(){
			$('#table2').datagrid('options').url='/jlyw/UserServlet.do?method=0';
			$('#table2').datagrid('options').queryParams={'queryGender':encodeURI($("input[name='queryGender']:checked").val()),'queryname':encodeURI($('#queryname').combobox('getValue')),'queryJobTitle':encodeURI($('#queryJobTitle').val()),'queryDepartment': encodeURI($('#querydept').val()),'queryProjectTeam': encodeURI($('#queryproteam').val()),'queryStatus': encodeURI($('#querystatus').combobox('getValue')),'queryPolStatus': encodeURI($('#querypolstatus').combobox('getValue')),'queryType': encodeURI($('#querytype').combobox('getValue')),'queryTel': encodeURI($('#querytel').val()),'queryIDNum': encodeURI($('#queryidnum').val())};
			$('#table2').datagrid('reload');
			$(':input[name=queryGender]').each(function(){  //ȡ��radio��ѡ��
           		$(this).attr('checked','checked');     
            	this.checked = false;   
       		});
			$('#querystatus').combobox('setValue',"");
			$('#querytype').combobox('setValue',"");
		}
				
		function edit(){
			$('#form1').form('submit',{
				url:'/jlyw/UserServlet.do?method=2',
				onSubmit:function(){return $('#form1').form('validate');},
				success:function(data){
					var result = eval("("+data+")");
		   			$.messager.alert('��ʾ',result.msg,'info');
		   			if(result.IsOK)
					{
						var signature = result.signature_filesetname;
						var photo = result.photo_filesetname;
						var num = $('#Signature').uploadifySettings('queueSize');
						if (num > 0) { //û��ѡ���ļ�
							doUploadByUploadify(signature,'Signature', false);
						}
						var num1 = $('#Photograph').uploadifySettings('queueSize');
						if (num1 > 0) { //û��ѡ���ļ�
							doUploadByUploadify(photo,'Photograph', false);
						}
		   				$('#edit').dialog('close');
						$('#table2').datagrid('reload');
					}
				}
			});
		}
	 
		$(function(){
			$('#table2').datagrid({
				title:'��Ա������Ϣ��ѯ',
				width:900,
				height:500,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'/jlyw/UserServlet.do?method=0',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'JobNum',title:'����',width:80,align:'center'},
					{field:'Name',title:'����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'Gender',title:'�Ա�',width:60,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == true)
							{
								rowData['Gender']=0;
							    return "��";
							}
							else if(value == false)
							{
								rowData['Gender']=1;
								return "Ů";
							}
					}},
					{field:'WorkLocation',title:'���ڵ�λ',width:160,align:'center',
					formatter:function(value,rowData,rowIndex){
						rowData['WorkLocation']=value;
						var datas = $('#WorkLocation').combobox('getData');
						for(var i = 0; i < datas.length; i++)
						{
							if(datas[i].id==value)
								return datas[i].headname;
						}
					}},
					{field:'Birthplace',title:'������',width:80,align:'center'},
					{field:'Birthday',title:'����������',width:100,align:'center'},
					{field:'IDNum',title:'���֤��',width:100,align:'center'},
					{field:'PoliticsStatus',title:'������ò',width:80,align:'center'},
					{field:'Nation',title:'����',width:80,align:'center'},
					{field:'WorkSince',title:'�μӹ�������',width:120,align:'center'},
					{field:'WorkHereSince',title:'��������ʱ��',width:120,ealign:'center'},
					{field:'Education',title:'ѧ��',width:80,align:'center'},
					{field:'EducationDate',title:'ȡ��ѧ��ʱ��',width:120,align:'center'},
					{field:'EducationFrom',title:'ѧ����ҵԺУ',width:120,align:'center'},
					{field:'Degree',title:'ѧλ',width:80,align:'center'},
					{field:'DegreeDate',title:'ȡ��ѧλʱ��',width:120,align:'center'},
					{field:'DegreeFrom',title:'ѧλ��ҵԺУ',width:120,align:'center'},
					{field:'JobTitle',title:'ְ��',width:80,align:'center'},
					{field:'Specialty',title:'��ѧרҵ',width:80,align:'center'},
					{field:'AdministrationPost',title:'����ְ��',width:80,align:'center'},
					{field:'PartyPost',title:'����ְ��',width:80,align:'center'},
					{field:'PartyDate',title:'�뵳ʱ��',width:80,align:'center'},
					{field:'HomeAdd',title:'��ͥסַ',width:80,align:'center'},
					{field:'WorkAdd',title:'�����ص�',width:120,align:'center'},
					{field:'Tel',title:'�칫�绰',width:80,align:'center'},
					{field:'Cellphone1',title:'�ֻ�1',width:80,align:'center'},
					{field:'Cellphone2',title:'�ֻ�2',width:80,align:'center'},
					{field:'Email',title:'����',width:120,align:'center'},
					{field:'ProjectTeamName',title:'������Ŀ��',width:80,align:'center'},
					{field:'Status',title:'״̬',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "����";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Status']=1;
								return '<span style="color:red">'+'ע��'+'</span>';
							}
						}},
					{field:'Type',title:'��Ա����',width:80,align:'center'},
					{field:'CancelDate',title:'ע��ʱ��',width:80,align:'center'},
					{field:'Signature',title:'ǩ��ͼƬ',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
						}
					},
					{field:'Photograph',title:'��Ƭ',width:80,align:'center',
						formatter : function(value,rowData,rowIndex){
							if(value=="")
								return "";
							//var res = eval("("+value+")");
							return "<a href='/jlyw/FileDownloadServlet.do?method=0&FileId="+value._id+"&FileType="+value.filetype+ "' target='_blank' title='������ظ��ļ�' >"+value.filename+"</a>";
						}
					},
					{field:'Remark',title:'��ע',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table2').datagrid('getSelected');
						if(select){
						$('#edit').window('open');
						$('#form1').show();
						/*$('#Name').val(select.Name);
						$('#Brief').val(select.Brief);
						$('#Gender').combobox('setValue',select.Gender);
						$('#JobNum').val(select.JobNum);
						$('#WorkLocation').combobox('setValue',select.WorkLocation);
						$('#Birthplace').val(select.Birthplace);
						$('#Birthday').datebox('setValue',select.Birthday);
						$('#IDNum').val(select.IDNum);
						$('#PoliticsStatus').val(select.PoliticsStatus);
						$('#Nation').val(select.Nation);
						$('#WorkSince').datebox('setValue',select.WorkSince);
						$('#WorkHereSince').datebox('setValue',select.WorkHereSince);
						$('#Education').val(select.Education);
						$('#EducationDate').datebox('setValue',select.EducationDate);
						$('#EducationFrom').val(select.EducationFrom);
						$('#Degree').val(select.Degree);
						$('#DegreeDate').datebox('setValue',select.DegreeDate);
						$('#DegreeFrom').val(select.DegreeFrom);
						$('#JobTitle').val(select.JobTitle);
						$('#Specialty').val(select.Specialty);
						$('#AdministrationPost').val(select.AdministrationPost);
						$('#PartyPost').val(select.PartyPost);
						$('#PartyDate').datebox('setValue',select.PartyDate);
						$('#HomeAdd').val(select.HomeAdd);
						$('#WorkAdd').val(select.WorkAdd);
						$('#Tel').val(select.Tel);
						$('#Cellphone1').val(select.Cellphone1);
						$('#Cellphone2').val(select.Cellphone2);
						$('#Email').val(select.Email);
						$('#DepartmentId').combobox('select',select.DepartmentId);
						$('#ProjectTeamId').combobox('loadData',[{'Id':select.ProjectTeamId,'Name':select.ProjectTeamName}]);
						$('#ProjectTeamId').combobox('select',select.ProjectTeamId);
						$('#Status').combobox('setValue',select.Status);
						$('#Type').combobox('setValue',select.Type);
						$('#CancelDate').datebox('setValue',select.CancelDate);
						$('#Remark').val(select.Remark);*/
						$('#form1').form('load',select);
						$('#Signature1').attr('src',"/jlyw/FileServlet.do?method=3&FileId="+select.Signature._id+"&FileType="+select.Signature.filetype);
						$('#Photograph1').attr('src',"/jlyw/FileServlet.do?method=3&FileId="+select.Photograph._id+"&FileType="+select.Photograph.filetype);
						
						$('#Id').val(select.Id);
						$('#form1').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ��Ա��','warning');
						}
					}
				},'-',{
						text:'ע��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#table2').datagrid('getSelected');
							if(select){	
							$.messager.confirm('��ʾ','ȷ��ע����',function(r){
								if(r){						
								$.ajax({
									type:'POST',
									url:'/jlyw/UserServlet.do?method=8',
									data:'id='+select.Id,
									dataType:"html",
									success:function(data){
										$('#table2').datagrid('reload');
									}
								});
								}
								});
							}
							else
							{
								$.messager.alert('��ʾ','��ѡ��һ��Ա��','warning');
							}
						}
				},'-',{
						text:'����',
						iconCls:'icon-save',
						handler:function(){
								myExport();
						}
				}],
				onClickRow:function(rowIndex, rowData)
				{
					$('#empid').val(encodeURI(rowData.Id));
					$('#profile_info_table').datagrid('options').url='/jlyw/UserProfileServlet.do?method=2';
					$('#profile_info_table').datagrid('options').queryParams={'EmpId':encodeURI(rowData.Id)};
					$('#profile_info_table').datagrid('reload');
					
					$('#Empid').val(encodeURI(rowData.Id));
					$('#qual_info_table').datagrid('options').url='/jlyw/UserQualServlet.do?method=2';
					$('#qual_info_table').datagrid('options').queryParams={'EmpId':encodeURI(rowData.Id)};
					$('#qual_info_table').datagrid('reload');
				}
			});
			
			$('#profile_info_table').datagrid({
	 			title:'������Ϣ',
	 			width:850,
	 			height:200,
				singleSelect:true, 
				fit: false,
				nowrap: false,
				striped: true,
				url:'',
				remoteSort: false,
				frozenColumns:[[
					{field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Type',title:'��������',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==1||value=='1')
						{
							rowData['Type'] = 1;
							return "ѧ��ѧλ";
						}
						else if(value==2||value=='2')
						{
							rowData['Type'] = 2;
							return "����ְ��䶯";
						}
						else if(value==3||value=='3')
						{
							rowData['Type'] = 3;
							return "����ְ��Ƹ��";
						}
						else if(value==4||value=='4')
						{
							rowData['Type'] = 4;
							return "��������";
						}
						else if(value==5||value=='5')
						{
							rowData['Type'] = 5;
							return "��������";
						}
						else if(value==6||value=='6')
						{
							rowData['Type'] = 6;
							return "��������";
						}
						else if(value==7||value=='7')
						{
							rowData['Type'] = 7;
							return "����";
						}
						else if(value==8||value=='8')
						{
							rowData['Type'] = 8;
							return "�ɹ�";
						}
						else if(value==9||value=='9')
						{
							rowData['Type'] = 9;
							return "����";
						}
						else if(value==0||value=='0')
						{
							rowData['Type'] = 0;
							return "����";
						}
					}},
					{field:'F1',title:'��ϸ��Ϣ1',width:120,align:'center'},
					{field:'F2',title:'��ϸ��Ϣ2',width:120,align:'center'},
					{field:'F3',title:'��ϸ��Ϣ3',width:120,align:'center'},
					{field:'F4',title:'��ϸ��Ϣ4',width:120,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#profile_info_table_toolbar"
	 		});
			
			$('#qual_info_table').datagrid({
	 			title:'Ա��������Ϣ��ѯ',
				width:850,
				height:200,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'QualNum',title:'֤���',width:100,align:'center'},
					{field:'Type',title:'����',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value == 0||value == '0')
						{
							rowData['Type']=0;
							return "�춨Ա";
						}
						else if(value == 1||value == '1')
						{
							rowData['Type']=1;
							return "����Ա";
						}
						else if(value == 2||value == '2')
						{
							rowData['Type']=2;
							return "������׼����Ա";
						}
						else if(value == 3||value == '3')
						{
							rowData['Type']=3;
							return "���֤����Ա";
						}
						else
							return value;
					}},
					{field:'AuthItems',title:'��Ȩ��Ŀ',width:100,align:'center'},
					{field:'AuthDate',title:'��֤ʱ��',width:100,align:'center'},
					{field:'Expiration',title:'��Ч��',width:80,align:'center'},
					{field:'AuthDept',title:'��֤����',width:100,align:'center'},
					{field:'Remark',title:'��ע',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'����',
					iconCls:'icon-add',
					handler:function(){
						if($('#table2').datagrid('getSelected')==null)
						{
							$.messager.alert('��ʾ','��ѡ����Ҫ�༭��Ա����','warning');
							return;
						}
						$('#add_qual').window('open');
						$('#frm_add_qual').show();
						$('#EmpId').val($('#qual_info_table').datagrid('options').queryParams.EmpId);
					}
				},'-',{
					text:'�޸�',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#qual_info_table').datagrid('getSelected');
						if(select){
						$('#edit_qual').window('open');
						$('#frm_edit_qual').show();
						
						$('#Type').combobox('setValue',select.Type);
						$('#QualNum').val(select.QualNum);
						$('#AuthItems').val(select.AuthItems);
						$('#AuthDate').datebox('setValue',select.AuthDate);
						$('#Expiration').datebox('setValue',select.Expiration);
						$('#AuthDept').val(select.AuthDept);
						$('#Remark').val(select.Remark);
						$('#qual_Id').val(select.Id);
						$('#frm_edit_qual').form('validate');
					}else{
						$.messager.alert('��ʾ','��ѡ��һ������','warning');
						}
					}
				},'-',{
						text:'ɾ��',
						iconCls:'icon-remove',
						handler:function(){
							var select = $('#qual_info_table').datagrid('getSelected');
							if(select){
							$.messager.confirm('��ʾ','ȷ��ע����',function(r){
								if(r){	
									$.ajax({
										type:'POST',
										url:'/jlyw/UserQualServlet.do?method=4',
										data:'id='+select.Id,
										dataType:"json",
										success:function(data){
											$('#qual_info_table').datagrid('reload');
										}
									});
								}
							});
							}
							else
							{
								$.messager.alert('��ʾ','��ѡ��һ������','warning');
							}
						}
				}]
	 		});
			
		});
		
		function closed(){
			$('#Signature').uploadifyClearQueue();
			$('#Photograph').uploadifyClearQueue();
			$('#edit').dialog('close');
		}
		function closed2(){
			$('#add-profile').dialog('close');
			$('#edit-profile').dialog('close');
			$('#add_qual').dialog('close');
			$('#edit_qual').dialog('close');
		}

		$(function(){
			$('#profileType').combobox({
				onChange:function(newValue, oldValue){
						if(newValue==1||newValue=='1')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��ֹ���£�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "�ں�ԺУ��רҵ��ҵ��";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "֤��ţ�";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
							
						}
						else if(newValue==2||newValue=='2')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "������£�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "��רҵ����ְ���ʸ���׼��λ��";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "֤���ţ�";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
						}
						else if(newValue==3||newValue=='3')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��Ƹ���ڣ�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "��רҵ����ְ��Ƹ�ε�λ��";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "֤����ļ���ţ�";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
						}
						else if(newValue==4||newValue=='4')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "������£�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "��֯��λ����Ŀ���ƣ�";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "֤���ţ�";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
						}
						else if(newValue==5||newValue=='5')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��ֹ���£�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "�ںε�λ�β��źθ�λ������";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "������Ҫרҵ����������";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "�κ�ְ��";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "";
						}
						else if(newValue==6||newValue=='6')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��ֹ���ڣ�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "�����������ݣ�";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "��֯��λ��";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "���������";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "";
						}
						else if(newValue==7||newValue=='7')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "�������ڣ�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "���ġ���������Ҫ����������Ŀ��";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "���漰���������";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "����λ�Σ�";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "";
						}
						else if(newValue==8||newValue=='8')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��ʱ�䣺";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "����Ŀ��";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "���������";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
						}
						else if(newValue==9||newValue=='9')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��ȣ�";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "���ۣ�";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "none";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
						}
						else if(newValue==0||newValue=='0')
						{
							var f1 = document.getElementById("lab_f1");
							f1.innerHTML = "��ϸ�����";
							var f2 = document.getElementById("lab_f2");
							f2.innerHTML = "";
							var f3 = document.getElementById("lab_f3");
							f3.innerHTML = "";
							var f4 = document.getElementById("lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("f2");
							input_f2.style.display = "none";
							var input_f3 = document.getElementById("f3");
							input_f3.style.display = "none";
							var input_f4 = document.getElementById("f4");
							input_f4.style.display = "none";
						}
				}
			});

			$('#edit-profileType').combobox({
				onChange:function(newValue, oldValue){
						if(newValue==1||newValue=='1')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��ֹ���£�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "�ں�ԺУ��רҵ��ҵ��";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "֤��ţ�";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
							
						}
						else if(newValue==2||newValue=='2')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "������£�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "��רҵ����ְ���ʸ���׼��λ��";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "֤���ţ�";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
						}
						else if(newValue==3||newValue=='3')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��Ƹ���ڣ�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "��רҵ����ְ��Ƹ�ε�λ��";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "֤����ļ���ţ�";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
						}
						else if(newValue==4||newValue=='4')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "������£�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "��֯��λ����Ŀ���ƣ�";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "֤���ţ�";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
						}
						else if(newValue==5||newValue=='5')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��ֹ���£�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "�ںε�λ�β��źθ�λ������";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "������Ҫרҵ����������";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "�κ�ְ��";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "";
						}
						else if(newValue==6||newValue=='6')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��ֹ���ڣ�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "�����������ݣ�";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "��֯��λ��";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "���������";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "";
						}
						else if(newValue==7||newValue=='7')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "�������ڣ�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "���ġ���������Ҫ����������Ŀ��";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "���漰���������";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "����λ�Σ�";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "";
						}
						else if(newValue==8||newValue=='8')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��ʱ�䣺";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "����Ŀ��";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "���������";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
						}
						else if(newValue==9||newValue=='9')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��ȣ�";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "���ۣ�";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "none";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
						}
						else if(newValue==0||newValue=='0')
						{
							var f1 = document.getElementById("edit-lab_f1");
							f1.innerHTML = "��ϸ�����";
							var f2 = document.getElementById("edit-lab_f2");
							f2.innerHTML = "";
							var f3 = document.getElementById("edit-lab_f3");
							f3.innerHTML = "";
							var f4 = document.getElementById("edit-lab_f4");
							f4.innerHTML = "";
							var input_f1 = document.getElementById("edit-f1");
							input_f1.style.display = "";
							var input_f2 = document.getElementById("edit-f2");
							input_f2.style.display = "none";
							var input_f3 = document.getElementById("edit-f3");
							input_f3.style.display = "none";
							var input_f4 = document.getElementById("edit-f4");
							input_f4.style.display = "none";
						}
				}
			});
		});
		
		$(function(){
			$('#SearchType').combobox({
				onChange:function(newValue, oldValue){
					$('#profile_info_table').datagrid('options').url='/jlyw/UserProfileServlet.do?method=2';
					$('#profile_info_table').datagrid('options').queryParams={'EmpId':$('#empid').val(),'Type':$('#SearchType').combobox('getValue')};
					$('#profile_info_table').datagrid('reload');
				}
			});
		});
		
		function doSubmitProfile(){
			$('#frm_add_profile').form('submit',{
				url:'/jlyw/UserProfileServlet.do?method=1',
				onSubmit:function(){return true;},
				success:function(data){
					$('#profile_info_table').datagrid('reload');
					$("#f1").val("");
					$("#f2").val("");
					$("#f3").val("");
					$("#f4").val("");
					closed2();
				}
			});
		}
		
		function OpenAddProfileWindow(){
				if($('#table2').datagrid('getSelected')==null)
				{
					$.messager.alert('��ʾ','��ѡ����Ҫ�༭��Ա����','warning');
					return;
				}
				$('#add-profile').window('open');
				$('#frm_add_profile').show();
		}
		
		function OpenEditProfileWindow(){
			var select = $('#profile_info_table').datagrid('getSelected');
			if(select){
				$('#edit-profile').window('open');
				$('#frm_edit_profile').show();
				$('#edit-Id').val(select.Id);
				$('#edit-profileType').combobox('setValue',select.Type);
				$('#edit-f1').val(select.F1);
				$('#edit-f2').val(select.F2);
				$('#edit-f3').val(select.F3);
				$('#edit-f4').val(select.F4);
				
			}else{
				$.messager.alert('��ʾ','��ѡ��һ��������¼','warning');
			}
		}
		
		function doEditProfile(){
			$('#frm_edit_profile').form('submit',{
				url:'/jlyw/UserProfileServlet.do?method=3',
				onSubmit:function(){},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
						closed2();
					$('#profile_info_table').datagrid('reload');
				}
			});
		}
		
		function DeleteProfileRow(){
			var select = $('#profile_info_table').datagrid('getSelected');
			if(select){
				$.messager.confirm('��ʾ','ȷ��ɾ����',function(r){
				if(r){
				$.ajax({
					type:'POST',
					url:'/jlyw/UserProfileServlet.do?method=4',
					data:'id='+select.Id,
					dataType:"html",
					success:function(data){
						$('#profile_info_table').datagrid('reload');
					}
				});
				}
			});	
			}
			else{
				$.messager.alert('��ʾ','��ѡ��һ��������¼','warning');
			}
		}
		
		function doAddQual(){
			$('#frm_add_qual').form('submit',{
				url:'/jlyw/UserQualServlet.do?method=1',
				onSubmit:function(){
					var time1 = $('#AuthDate').datebox('getValue');
					var time2 = $('#Expiration').datebox('getValue');
					if(time2<time1)
					{
						$.messager.alert('��ʾ',"��Ч�����ڷ�֤���ڣ�����������������룡",'warning');
						return false;
					}
					return $('#frm_add_qual').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
						closed2();
					$('#qual_info_table').datagrid('reload');
				}
			});
		}
		
		function doEditQual(){
			$('#frm_edit_qual').form('submit',{
				url:'/jlyw/UserQualServlet.do?method=3',
				onSubmit:function(){
					var time1 = $('#AuthDate').datebox('getValue');
					var time2 = $('#Expiration').datebox('getValue');
					if(time2<time1)
					{
						$.messager.alert('��ʾ',"��Ч�����ڷ�֤���ڣ�����������������룡",'warning');
						return false;
					}
					return $('#frm_edit_qual').form('validate');
				},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('��ʾ',result.msg,'info');
					if(result.IsOK)
						closed2();
					$('#qual_info_table').datagrid('reload');
				}
			});
		}
		
		function getBrief(){
			$('#Brief').val(makePy($('#Name').val()));
		}
		
		function myExport(){
			
			ShowWaitingDlg("���ڵ��������Ժ�......");
			$('#paramsStr').val(JSON.stringify($('#table2').datagrid('options').queryParams));
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

<body>
<form id="frm_export" method="post" action="/jlyw/UserServlet.do?method=10">
<input id="paramsStr" name="paramsStr" type="hidden" />
</form>
<form id="frm_down" method="post" action="/jlyw/Export.do?" target="_self">
<input id="filePath" name="filePath" type="hidden" />
</form>
<DIV class="JlywMainLayoutDiv">
	<DIV class="JlywTopLayoutDIV">
		<jsp:include page="/Common/Title.jsp" flush="true">
			<jsp:param name="TitleName" value="��Ա������Ϣ��ѯ��������ѯ����" />
		</jsp:include>
	</DIV>
	<DIV class="JlywCenterLayoutDIV">
	<div style="width:900px"  region="center">
		<div>
			<br />
			<table id="table1" style="width:900px">
				<tr>
					<td align="right">Ա����</td>
				  	<td align="left"><input id="queryname" name="queryname" class="easyui-combobox"  url="" style="width:150px;" valueField="name" textField="name" panelHeight="150px" /></td>
                    <td align="right">���ţ�</td>
                    <td align="left"><input id="querydept" name="querydept" type="text"/></td>
                    <td align="right">��Ŀ�飺</td>
                    <td align="left"><input id="queryproteam" name="queryproteam" type="text"/></td>
				</tr>
                    <tr>
                        <td align="right">״̬��</td>
                        <td align="left">
                        		<select id="querystatus" name="Status" class="easyui-combobox" style="width:150px" panelHeight="auto" editable="false">
									<option value="0">����</option>
									<option value="1">ע��</option>
								</select>
                        </td>
                        <td align="right">ְ�ƣ�</td>
                        <td align="left"><input id="queryJobTitle" name="queryJobTitle" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=18"/></td>
                        <td align="right">������ò��</td>
                        <td align="left"><input id="querypolstatus" name="querypolstatus" class="easyui-combobox"  panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=16"/></td>
                        <td align="right">�Ա�</td>
                        <td align="left"><input id="male" name="queryGender" type="radio" value="0"/>��<input id="female" name="queryGender" type="radio" value="1"/>Ů</td>
                    </tr>
                    <tr>
                        <td align="right">��Ա���ʣ�</td>
                        <td align="left">
                        		<select id="querytype" name="querytype" class="easyui-combobox" style="width:150px" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=17">
								</select>
                        </td>
                        <td align="right">��ϵ��ʽ��</td>
                        <td align="left"><input id="querytel" name="querytel" type="text"/></td>
                        <td align="right">���֤�ţ�</td>
                        <td align="left"><input id="queryidnum" name="queryidnum" type="text"/></td>
                        <td width="80px" align="left" colspan="2"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="query()">��ѯ</a></td>
                    </tr>
                    <tr>
                    </tr>
                </table>
		</div>
		
        <div>
		<table id="table2" style="height:500px; width:900px"></table>
        	<div id="edit" class="easyui-window" title="�޸�" style="padding: 10px;width: 800;height: 500;"
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="form1" method="post">
				<div>
					<table id="table3" style="padding: 10px;width: 800;height: 500;">
					<input id="Id" name="Id" type="hidden"/>
						<tr> 
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;����</td> 
							<td align="left"><input id="Name" name="Name" type="text" class="easyui-validatebox" required="true" onchange="getBrief()"/></td>
                            <td align="right">ƴ�����룺</td>
							<td align="left"><input name="Brief" id="Brief" type="text" class="easyui-validatebox" required="true"/></td>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��</td>
							<td align="left">
								<select id="Gender" name="Gender" class="easyui-combobox" style="width:150px" panelHeight="auto" editable="false">
									<option value="0">��</option>
									<option value="1">Ů</option>
								</select>
							</td>
						</tr>
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ţ�</td>
							<td align="left"><input id="JobNum" name="JobNum" type="text"/></td>
							<td align="right">���ڵ�λ��</td>
							<td align="left"><select name="WorkLocation" id="WorkLocation" style="width:150px" class="easyui-combobox" valueField="id" textField="headname" panelHeight="auto" mode="remote" url="/jlyw/AddressServlet.do?method=1" required="true" editable="false"/></td>
						</tr> 
						<tr> 
							<td align="right">��&nbsp;��&nbsp;�أ�</td>
							<td align="left"><input id="Birthplace" name="Birthplace" type="text"/></td>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br />��&nbsp;��&nbsp;�գ�</td>
							<td align="left"><input id="Birthday" name="Birthday" type="text" class="easyui-datebox" style="width:150px" editable="false"/></td>
							<td align="right">���֤�ţ�</td>
							<td align="left"><input id="IDNum" name="IDNum" type="text"/></td>
						</tr>
						<tr>
							<td align="right">������ò��</td>
							<td align="left"><input id="PoliticsStatus" name="PoliticsStatus" style="width:150px" class="easyui-combobox"  panelHeight="auto" required="true" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=16"/></td>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�壺</td>
                            <td align="left"><input id="Nation" name="Nation" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=27"/></td>
                            <td align="right">��Ա���ʣ�</td>
                            <td align="left">
                                <select id="Type" name="Type" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=17">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">�μӹ���&nbsp;&nbsp;<br/>��&nbsp;&nbsp;&nbsp;&nbsp;�ڣ�</td>
                            <td align="left"><input id="WorkSince" name="WorkSince" type="text" class="easyui-datebox" style="width:150px"  required="true"  editable="false"/></td>
                            <td align="right">��������&nbsp;&nbsp;<br/>ʱ&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                            <td align="left"><input id="WorkHereSince" name="WorkHereSince" type="text" class="easyui-datebox" style="width:150px" required="true" editable="false"/></td>
                            <td align="right">ְ&nbsp;&nbsp;&nbsp;&nbsp;�ƣ�</td>
                            <td align="left"><input id="JobTitle" name="JobTitle" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=18"/></td>
                        </tr>
                        <tr>
                            <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;����</td>
                            <td align="left"><input id="Education" name="Education" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=19"/></td>
                            <td align="right">ȡ��ѧ��&nbsp;&nbsp;<br/>ʱ&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                            <td align="left"><input id="EducationDate" name="EducationDate" type="text" class="easyui-datebox" style="width:150px" class="easyui-validatebox"/></td>
                            <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;��&nbsp;&nbsp;<br/>��ҵԺУ��</td>
                            <td align="left"><input id="EducationFrom" name="EducationFrom" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;λ��</td>
                            <td align="left"><input id="Degree" name="Degree" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=20"/></td>
                            <td align="right">ȡ��ѧλ&nbsp;&nbsp;<br/>ʱ&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                            <td align="left"><input id="DegreeDate" name="DegreeDate" type="text" class="easyui-datebox" style="width:150px"/></td>
                            <td align="right">ѧ&nbsp;&nbsp;&nbsp;&nbsp;λ&nbsp;&nbsp;<br/>��ҵԺУ��</td>
                            <td align="left"><input id="DegreeFrom" name="DegreeFrom" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
                        <tr>
                            <td align="right">��ѧרҵ��</td>
                            <td align="left"><input id="Specialty" name="Specialty" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=21"/></td>
                            <td align="right">����ְ��</td>
                            <td align="left"><input id="AdministrationPost" name="AdministrationPost" style="width:150px" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=22"/></td>
                            <td align="right">����ְ��</td>
                            <td align="left"><input id="PartyPost" name="PartyPost" style="width:150px;" class="easyui-combobox" required="true" panelHeight="auto" valueField="name" textField="name" url="/jlyw/BaseTypeServlet.do?method=4&type=23"/></td>
                        </tr>
                        <tr>
                            <td align="right">�뵳ʱ�䣺</td>
                            <td align="left"><input id="PartyDate" name="PartyDate" type="text" class="easyui-datebox" style="width:150px" editable="false"/></td>
                            <td align="right">��ͥסַ��</td>
                            <td align="left"><input id="HomeAdd" name="HomeAdd" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">�����ص㣺</td>
                            <td align="left"><input id="WorkAdd" name="WorkAdd" type="text" class="easyui-validatebox" required="true"/></td>
                        </tr>
						<tr>
							<td align="right">�칫�绰��</td>
							<td align="left"><input id="Tel" name="Tel" type="text" /></td>
							<td align="right">�ֻ�����1��</td>
							<td align="left"><input id="Cellphone1" name="Cellphone1" type="text" /></td>
							<td align="right">�ֻ�����2��</td>
							<td align="left"><input id="Cellphone2" name="Cellphone2" type="text" /></td>
						</tr>
						<tr>
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�䣺</td>
                            <td align="left"><input id="Email" name="Email" type="text" class="easyui-validatebox" required="true"/></td>
                            <td align="right">�������ţ�</td>
                            <td align="left"><select id="DepartmentId" name="DepartmentId" class="easyui-combobox" style="width:150px" valueField="Id" textField="Name" panelHeight="150px" mode="remote" url="/jlyw/DepartmentServlet.do?method=6" required="true" editable="false"/></td>
                            <td align="right">������Ŀ�飺</td>
                            <td align="left"><select id="ProjectTeamId" name="ProjectTeamId" class="easyui-combobox" style="width:150px" valueField="Id" textField="Name" panelHeight="150px" mode="remote" url="" required="true" editable="false"/></td>
                        </tr>
                        <tr>
                            <td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
                            <td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="2"></textarea></td>
                            <td align="right">״&nbsp;&nbsp;&nbsp;&nbsp;̬��</td>
                            <td align="left">
                                <select id="Status" name="Status" class="easyui-combobox" style="width:150px" required="true" panelHeight="auto" editable="false">
                                    <option value="0">����</option>
                                    <option value="1">ע��</option>
                                </select>
                            </td>
                        </tr>
						<tr>
							<td align="right">ǩ��ͼƬ��</td>
							<td align="left" colspan="3"><input id="Signature" name="Signature" type="file" style="width:420px"/></td>
							<td align="left" colspan="2"><img id="Signature1" name="Signature1" src="" alt="����ǩ��" style="width:200px; height:80px"/></td>
                        </tr>
                        <tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;Ƭ��</td>
							<td align="left" colspan="3"><input id="Photograph" name="Photograph" type="file" style="width:450px"/></td>							
							<td align="left" colspan="2"><img id="Photograph1" name="Photograph1" src="" alt="������Ƭ" style="width:120px; height:150px"/></td>
						</tr>
						<tr>	
							<td></td>
							<td align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="edit()">�޸�</a></td>
							<td></td>
							<td></td>
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed()">ȡ��</a></td>
							<td></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
        </div>
		<br/>
		<div id="p2" class="easyui-panel" style="width:900px;height:250px;padding:10px;"
				title="��������" collapsible="false"  closable="false">
			<table id="profile_info_table" iconCls="icon-tip" width="780px" height="150px"></table>
            <div id="add-profile" class="easyui-window" title="�޸�" style="padding: 10px;width: 800;height: 500;" 
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
        <form id="frm_add_profile" method="post">
			<input type="hidden" id="empid" name="EmpId" value="" />
			<table width="750">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >�������ͣ�</td>
					<td width="25%" style="padding-top:15px;" align="left">
						<select id="profileType" name="ProfileType" class="easyui-combobox" style="width:150px;" required="true" editable="false" panelHeight="auto">
                        <option value="1">ѧ��ѧλ</option>
                        <option value="2">����ְ��䶯</option>
                        <option value="3">����ְ��Ƹ��</option>
                        <option value="4">��������</option>
                        <option value="5">��������</option>
                        <option value="6">��������</option>
                        <option value="7">����</option>
                        <option value="8">�ɹ�</option>
                        <option value="9">����</option>
                        <option value="0">����</option>
					</select></td>
					</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right"><label id="lab_f1">��ֹ���£�</label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="f1" name="F1" style="width:500px"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" ><label id="lab_f2">�ں�ԺУ��רҵ��ҵ��</label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="f2" name="F2" style="width:500px"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" ><label id="lab_f3">֤��ţ�</label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="f3" name="F3" style="width:500px"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" ><label id="lab_f4"></label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="f4" name="F4" style="width:500px;display:none"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr >
				  <td height="39" align="right" style="padding-top:15px;"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doSubmitProfile()">����</a></td>
				 <td height="39" align="left" style="padding-top:15px;"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="closed2()">ȡ��</a></td>
			  </tr>
		  </table>
		  </form>
          </div>
        
		<div id="edit-profile" class="easyui-window" title="�޸�" style="padding: 10px;width: 800;height: 500;" 
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_profile" method="post">
			<input type="hidden" id="edit-Id" name="Id" value="" />
			<table width="780">
				<tr>
					<td width="15%" style="padding-top:15px;" align="right" >�������ͣ�</td>
					<td width="25%" style="padding-top:15px;" align="left">
						<select id="edit-profileType" name="ProfileType" class="easyui-combobox" style="width:150px;" required="true" editable="false" disabled="disabled" panelHeight="auto">
					<option value="1">ѧ��ѧλ</option>
					<option value="2">����ְ��䶯</option>
					<option value="3">����ְ��Ƹ��</option>
					<option value="4">��������</option>
					<option value="5">��������</option>
					<option value="6">��������</option>
					<option value="7">����</option>
					<option value="8">�ɹ�</option>
					<option value="9">����</option>
					<option value="0">����</option>
					</select></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right"><label id="edit-lab_f1">��ֹ���£�</label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit-f1" name="F1" style="width:500px"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" ><label id="edit-lab_f2">�ں�ԺУ��רҵ��ҵ��</label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit-f2" name="F2" style="width:500px"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" ><label id="edit-lab_f3">֤��ţ�</label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit-f3" name="F3" style="width:500px"></input></td>
				</tr>
				<tr>
					<td width="25%" style="padding-top:15px;" align="right" ><label id="edit-lab_f4"></label></td>
					<td width="25%" style="padding-top:15px;" align="left"><input id="edit-f4" name="F4" style="width:500px;display:none"></input></td>
				</tr>
				<tr >
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
				  <td height="39" align="right" style="padding-top:15px;"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onClick="doEditProfile()">�޸�</a></td>
				  <td height="39" align="left" style="padding-top:15px;"><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onClick="closed2()">ȡ��</a></td>
			  	</tr>
		  </table>
			</form>
		</div>
		</div>
        <br />
		<div id="p2" class="easyui-panel" style="width:900px;height:250px;padding:10px;"
				title="��Ա���ʹ���" collapsible="false"  closable="false">
			<table id="qual_info_table" iconCls="icon-tip" width="780px" height="150px"></table>
        
        <div id="add_qual" class="easyui-window" title="�½�" style="padding: 10px;width: 800;height: 500;" 
		iconCls="icon-add" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_add_qual" method="post">
				<div>
					<table id="table3" style="padding: 10px;width: 800;height: 500;">
					<input id="EmpId" name="EmpId" type="hidden"/>
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ͣ�</td>
							<td align="left">
								<select id="addType" name="Type" class="easyui-combobox" url="EmpQualType.json" valueField="id" textField="text" style="width:150px" editable="false" panelHeight="auto">
								</select>
							</td>
							<td align="right">֤&nbsp;��&nbsp;�ţ�</td>
							<td align="left"><input id="addQualNum" name="QualNum" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">��Ȩ��Ŀ��</td>
							<td align="left"><input id="addAuthItems" name="AuthItems" type="text"/></td>
							<td align="right">��֤ʱ�䣺</td>
							<td align="left"><input id="addAuthDate" name="AuthDate" type="text" class="easyui-datebox" style="width:150px" required="true"/></td>
						</tr>
						<tr>
							<td align="right">��&nbsp;Ч&nbsp;�ڣ�</td>
							<td align="left"><input id="addExpiration" name="Expiration" type="text" class="easyui-datebox" style="width:150px" required="true"/></td>
							<td align="right">��֤���أ�</td>
							<td align="left"><input id="addAuthDept" name="AuthDept" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
							<td align="left" colspan="3"><textarea id="addRemark" name="Remark" cols="56" rows="4"></textarea></td>
						</tr>
						<tr>	
							<td></td>
							<td align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doAddQual()">����</a></td>
							<td></td>
							
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed2()">ȡ��</a></td>
							<td></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
        
        <div id="edit_qual" class="easyui-window" title="�޸�" style="padding: 10px;width: 800;height: 500;" 
		iconCls="icon-edit" closed="true" maximizable="false" minimizable="false" collapsible="false" modal="true">
			<form id="frm_edit_qual" method="post">
				<div>
					<table id="table3" style="padding: 10px;width: 800;height: 500;">
					<input id="qual_Id" name="Id" type="hidden"/>
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;�ͣ�</td>
							<td align="left">
								<input id="Type" name="Type" class="easyui-combobox" url="EmpQualType.json" valueField="id" textField="text" style="width:150px" editable="false" panelHeight="auto"/>
							</td>
							<td align="right">֤&nbsp;��&nbsp;�ţ�</td>
							<td align="left"><input id="QualNum" name="QualNum" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">��Ȩ��Ŀ��</td>
							<td align="left"><input id="AuthItems" name="AuthItems" type="text"/></td>
							<td align="right">��֤ʱ�䣺</td>
							<td align="left"><input id="AuthDate" name="AuthDate" type="text" class="easyui-datebox" style="width:150px" required="true"/></td>
						</tr>
						<tr>
							<td align="right">��&nbsp;Ч&nbsp;�ڣ�</td>
							<td align="left"><input id="Expiration" name="Expiration" type="text" class="easyui-datebox" style="width:150px" required="true"/></td>
							<td align="right">��֤���أ�</td>
							<td align="left"><input id="AuthDept" name="AuthDept" type="text" class="easyui-validatebox" required="true"/></td>
						</tr>
						<tr>
							<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;ע��</td>
							<td align="left" colspan="3"><textarea id="Remark" name="Remark" cols="56" rows="4"></textarea></td>
						</tr>
						<tr>	
							<td></td>
							<td align="center"><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="doEditQual()">�޸�</a></td>
							<td></td>
							
							<td><a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="closed2()">ȡ��</a></td>
							<td></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		</div>
        <div id="profile_info_table_toolbar" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td>
                    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="OpenAddProfileWindow()">����</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="OpenEditProfileWindow()">�޸�</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="DeleteProfileRow()">ɾ��</a>
					</td>
					<td style="text-align:right;padding-right:2px">
						<label>�������ͣ�</label><select type="text" id="SearchType" class="easyui-combobox" panelHeight="auto">
                            <option value="1">ѧ��ѧλ</option>
                            <option value="2">����ְ��䶯</option>
                            <option value="3">����ְ��Ƹ��</option>
                            <option value="4">��������</option>
                            <option value="5">��������</option>
                            <option value="6">��������</option>
                            <option value="7">����</option>
                            <option value="8">�ɹ�</option>
                            <option value="9">����</option>
                            <option value="0">����</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
	</div>
    </DIV>
    </DIV>
</body>
</html>
