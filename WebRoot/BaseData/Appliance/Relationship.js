// JavaScript Document
// 标准关系界面Relationship.jsp 脚本）		
		
		$(function(){
			$('#StdStdApp_StandardId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/StandardServlet.do?method=7&StandardName='+newValue);
				}
			});	
			
			$('#StdStdApp_StandardApplianceId').combobox({
				valueField:'name',
				textField:'name',
				onSelect:function(record){
					$('#div_StdApp').window('open');
					$('#table_StdStdApp_StdApp').datagrid('options').url='/jlyw/StandardApplianceServlet.do?method=13';
					$('#table_StdStdApp_StdApp').datagrid('options').queryParams={'StdAppName':encodeURI(record.name)};
					$('#table_StdStdApp_StdApp').datagrid('reload');
					
				},
				onChange:function(newValue, oldValue){
					var select = $('#StdStdApp_StandardId').combobox('getValue');
					if(select=="")
					{
						$.messager.alert('提示','请先选择一个标准！','warning');
						return;
					}
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].name){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/StandardApplianceServlet.do?method=6&QueryName='+newValue);
				}
			});	
			
			$('#StdTgtApp_StandardId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/StandardServlet.do?method=7&StandardName='+newValue);
				}
			});	
			
			$('#StdTgtApp_TargetApplianceId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/TargetApplianceServlet.do?method=6&Status=0&TgtAppName='+newValue);
				}
			});	
			
			$('#TgtApp').combobox({
				valueField:'name',
				textField:'name',
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
					$(this).combobox('reload','/jlyw/TargetApplianceServlet.do?method=6&Status=0&TgtAppName='+newValue);
				}
			});			
			
			$('#StdAppTgtApp_TargetApplianceId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){
					var id = $(this).combobox('getValue');
					$.ajax({
						type:'POST',
						url:'/jlyw/RelationShipServlet.do?method=14',
						data:'TargetApplianceId=' + id,
						dataType:'json',
						success:function(data){
							$('#StdAppTgtApp_StandardId').combobox('loadData',data);
						}
					});
				},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/TargetApplianceServlet.do?method=6&Status=0&TgtAppName='+newValue);
				}
			});
			$('#StdAppTgtApp_StandardId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(record){
					$('#div_Std_StdApp').window('open');
					$('#table_TgtAppStdApp_StdApp').datagrid('options').url='/jlyw/RelationShipServlet.do?method=15';
					$('#table_TgtAppStdApp_StdApp').datagrid('options').queryParams={'StandardId':encodeURI(record.id)};
					$('#table_TgtAppStdApp_StdApp').datagrid('reload');
				}
			});
			$('#TgtAppSpec_TargetApplianceId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/TargetApplianceServlet.do?method=6&Status=0&TgtAppName='+newValue);
				}
			});	
			
			$('#TgtAppSpec_SpecificationId').combobox({
				valueField:'id',
				textField:'name',
				onSelect:function(){},
				onChange:function(newValue, oldValue){
					var allData = $(this).combobox('getData');
					if(allData != null && allData.length > 0){
						for(var i=0; i<allData.length; i++)
						{
							if(newValue==allData[i].id){
								return false;
							}
						}
					}
					$(this).combobox('reload','/jlyw/SpecificationServlet.do?method=7&SpecificationName='+newValue);
				}
			});	
		
			$('#table_Std_StdApp').datagrid({     //标准和标准器具对应关系
				title:'计量标准和标准器具对应关系',
				width:802,
				height:400,
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
					{title:'计量标准信息',colspan:4,align:'center'},
					{title:'标准器具信息',colspan:7,align:'center'},
					{title:'关系信息',colspan:2,align:'center'}
				],[
					{field:'Std_Name',title:'计量标准名称',width:80,align:'center'},	
					{field:'Std_CertificateCode',title:'计量标准证书号',width:100,align:'center'},
					{field:'Std_StandardCode',title:'计量标准代码',width:80,align:'center'},
					{field:'Std_Status',title:'状态',width:50,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Std_Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Std_Status']=1;
								return "注销";
							}
						}},
		//		],[
					{field:'StdApp_Name',title:'器具名称',width:80,align:'center'},
					{field:'StdApp_Model',title:'规格型号',width:80,align:'center'},
					{field:'StdApp_Range',title:'测量范围',width:80,align:'center'},
					{field:'StdApp_Uncertain',title:'不确定度',width:80,align:'center'},
					{field:'StdApp_LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'StdApp_Release',title:'出厂信息',width:90,align:'center'},
					{field:'StdApp_Status',title:'器具状态',width:50,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['StdApp_Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['StdApp_Status']=1;
								return "注销";
							}
					}},
					{field:'Std_StdApp_IsMain',title:'是否主要设备',width:90,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == "0")
							{
								rowData['Std_StdApp_IsMain']=0;
								return "是";
							}
							else if(value == 0 || value == "1")
							{
								rowData['Std_StdApp_IsMain']=1;
								return "否";
							}
					}},
					{field:'Std_StdApp_Sequence',title:'器具序号',width:90,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#table_Std_StdApp-search-toolbar"
			})
	
		
	    	$('#table_StdApp_TgtApp').datagrid({
				title:'受检器具和标准器具对应关系',
				width:802,
				height:400,
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
					{title:'受检器具信息',align:'center',colspan:4},
					{title:'标准器具信息',colspan:7,align:'center'}
				],[
					{field:'TgtApp_Name',title:'受检器具名称',width:100,align:'center'},
					{field:'TgtApp_StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'TgtApp_Code',title:'器具编码',width:80,ealign:'center'},
					{field:'TgtApp_Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['TgtApp_Status']=0;
							    return "正常";
							}
							else
							{
								rowData['TgtApp_Status']=1;
								return "注销";
							}
					}},
					{field:'StdApp_Name',title:'器具名称',width:80,align:'center'},
					{field:'StdApp_Model',title:'规格型号',width:80,align:'center'},
					{field:'StdApp_Range',title:'测量范围',width:80,align:'center'},
					{field:'StdApp_Uncertain',title:'不确定度',width:80,align:'center'},
					{field:'StdApp_LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'StdApp_Release',title:'出厂信息',width:90,align:'center'},
					{field:'StdApp_Status',title:'器具状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['StdApp_Status']=0;
							    return "正常";
							}
							else
							{
								rowData['StdApp_Status']=1;
								return "注销";
							}
					}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#table_StdApp_TgtApp-search-toolbar"
			})
			
			$('#table_StdApp_TgtApp1').datagrid({
				title:'受检器具和标准器具对应关系',
				width:802,
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
					{title:'标准器具信息',colspan:7,align:'center'},
					{title:'受检器具信息',align:'center',colspan:4}					
				],[
					{field:'StdApp_LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'StdApp_Name',title:'器具名称',width:80,align:'center'},
					{field:'StdApp_Model',title:'规格型号',width:80,align:'center'},
					{field:'StdApp_Range',title:'测量范围',width:80,align:'center'},
					{field:'StdApp_Uncertain',title:'不确定度',width:80,align:'center'},
					{field:'StdApp_Release',title:'出厂信息',width:90,align:'center'},
					{field:'StdApp_Status',title:'器具状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['StdApp_Status']=0;
							    return "正常";
							}
							else
							{
								rowData['StdApp_Status']=1;
								return "注销";
							}
					}},
					{field:'TgtApp_Name',title:'受检器具名称',width:100,align:'center'},
					{field:'TgtApp_StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'TgtApp_Code',title:'器具编码',width:80,ealign:'center'},
					{field:'TgtApp_Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['TgtApp_Status']=0;
							    return "正常";
							}
							else
							{
								rowData['TgtApp_Status']=1;
								return "注销";
							}
					}}
				]],
				pagination:true,
				rownumbers:true
			})
			
			$('#table_Std_TgtApp').datagrid({  //受检器具和标准对应关系
				title:'受检器具和计量标准对应关系',
				width:802,
				height:400,
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
					{title:'受检器具信息',colspan:4,align:'center'},
					{title:'计量标准信息',colspan:4,align:'center'}
				],[
					{field:'TgtApp_Name',title:'受检器具名称',width:100,align:'center'},
					{field:'TgtApp_StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'TgtApp_Code',title:'器具编码',width:80,ealign:'center'},
					{field:'TgtApp_Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['TgtApp_Status']=0;
							    return "正常";
							}
							else
							{
								rowData['TgtApp_Status']=1;
								return "注销";
							}
					}},
					{field:'Std_Name',title:'计量标准名称',width:80,align:'center'},
					{field:'Std_CertificateCode',title:'计量标准证书号',width:100,align:'center'},
					{field:'Std_StandardCode',title:'计量标准代码',width:80,align:'center'},
					{field:'Std_Status',title:'状态',width:50,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Std_Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Std_Status']=1;
								return "注销";
							}
						}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#table_Std_TgtApp-search-toolbar"
			});
			
			$('#table_Std_TgtApp1').datagrid({  //受检器具和标准对应关系
				title:'受检器具和计量标准对应关系',
				width:802,
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
					{title:'计量标准信息',colspan:4,align:'center'},
					{title:'受检器具信息',colspan:4,align:'center'}
				],[
					{field:'Std_Name',title:'计量标准名称',width:80,align:'center'},
					{field:'Std_CertificateCode',title:'计量标准证书号',width:100,align:'center'},
					{field:'Std_StandardCode',title:'计量标准代码',width:80,align:'center'},
					{field:'Std_Status',title:'状态',width:50,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Std_Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Std_Status']=1;
								return "注销";
							}
						}},
					{field:'TgtApp_Name',title:'受检器具名称',width:100,align:'center'},
					{field:'TgtApp_StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'TgtApp_Code',title:'器具编码',width:80,ealign:'center'},
					{field:'TgtApp_Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['TgtApp_Status']=0;
							    return "正常";
							}
							else
							{
								rowData['TgtApp_Status']=1;
								return "注销";
							}
					}}
				]],
				pagination:true,
				rownumbers:true
			});
			
			$('#table_Std_Spec').datagrid({  //标准和技术规范对应关系
				title:'计量标准和技术规范对应关系',
				width:802,
				height:400,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'',
				sortName: 'Id',
				remoteSort: false,
				idField:'Id',
				/*frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],*/
				columns:[[
					{title:'计量标准信息',colspan:4,align:'center'},
					{title:'技术规范信息',colspan:6,align:'center'}
				],[
					{field:'Std_Name',title:'计量标准名称',width:80,align:'center'},		
					{field:'Std_CertificateCode',title:'计量标准证书号',width:100,align:'center'},
					{field:'Std_StandardCode',title:'计量标准代码',width:80,align:'center'},
					{field:'Std_Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Std_Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Std_Status']=1;
								return "注销";
							}
						}},
		//		],[
					{field:'Spec_Name',title:'技术规范名称',width:120,align:'center'},
					{field:'Spec_Code',title:'技术规范编号',width:100,align:'center'},
					{field:'Spec_Type',title:'类别',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{

								rowData['Spec_Type']=0;
							    return "检定规程";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_Type']=1;
								return "校准规范";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Spec_Type']=2;
								return "标准";
							}
							else if(value == 3 || value == '3')
							{
								rowData['Spec_Type']=2;
								return "自编文件";
							}
						}},
					{field:'Spec_InCharge',title:'是否受控',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_InCharge']=0;
							    return "是";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_InCharge']=1;
								return "否";
							}
						}},
					{field:'Spec_LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'Spec_Status',title:'状态',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_Status']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_Status']=1;
								return "注销";
							}
						}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#table_Std_Spec-search-toolbar"
			})
			
			$('#table_TgtApp_Spec').datagrid({  //受检器具和技术规范对应关系
				title:'受检器具和技术规范对应关系',
				width:802,
				height:400,
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
					{title:'受检器具信息',colspan:4,align:'center'},
					{title:'技术规范信息',align:'center',colspan:6}
				],[
					{field:'TgtApp_Name',title:'受检器具名称',width:100,align:'center'},
					{field:'TgtApp_StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'TgtApp_Code',title:'器具编码',width:80,ealign:'center'},
					{field:'TgtApp_Status',title:'状态',width:50,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return "注销";
							}
					}},
		//		],[
					{field:'Spec_Name',title:'技术规范名称',width:120,align:'center'},
					{field:'Spec_Code',title:'技术规范编号',width:100,align:'center'},
					{field:'Spec_Type',title:'类别',width:50,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_Type']=0;
							    return "检定规程";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_Type']=1;
								return "校准规范";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Spec_Type']=2;
								return "标准";
							}
							else if(value == 3 || value == '3')
							{
								rowData['Spec_Type']=2;
								return "自编文件";
							}
						}},
					{field:'Spec_InCharge',title:'是否受控',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_InCharge']=0;
							    return "是";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_InCharge']=1;
								return "否";
							}
						}},
					{field:'Spec_LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'Spec_Status',title:'状态',width:50,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_InCharge']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_InCharge']=1;
								return "注销";
							}
						}}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:"#table_TgtApp_Spec-search-toolbar"
			});
			
			$('#table_TgtApp_Spec1').datagrid({  //受检器具和技术规范对应关系
				title:'受检器具和技术规范对应关系',
				width:802,
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
					{title:'技术规范信息',align:'center',colspan:6},
					{title:'受检器具信息',colspan:4,align:'center'}
				],[
		//		],[
					{field:'Spec_Name',title:'技术规范名称',width:120,align:'center'},
					{field:'Spec_Code',title:'技术规范编号',width:100,align:'center'},
					{field:'Spec_Type',title:'类别',width:50,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_Type']=0;
							    return "检定规程";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_Type']=1;
								return "校准规范";
							}
							else if(value == 2 || value == '2')
							{
								rowData['Spec_Type']=2;
								return "标准";
							}
							else if(value == 3 || value == '3')
							{
								rowData['Spec_Type']=2;
								return "自编文件";
							}
						}},
					{field:'Spec_InCharge',title:'是否受控',width:80,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_InCharge']=0;
							    return "是";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_InCharge']=1;
								return "否";
							}
						}},
					{field:'Spec_LocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'Spec_Status',title:'状态',width:50,align:'center',
						formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Spec_InCharge']=0;
							    return "正常";
							}
							else if(value == 1 || value == '1')
							{
								rowData['Spec_InCharge']=1;
								return "注销";
							}
						}},
					{field:'TgtApp_Name',title:'受检器具名称',width:100,align:'center'},
					{field:'TgtApp_StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'TgtApp_Code',title:'器具编码',width:80,ealign:'center'},
					{field:'TgtApp_Status',title:'状态',width:50,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return "注销";
							}
					}}
				]],
				pagination:true,
				rownumbers:true
			});
			
			$('#table_TgtAppStdApp_UnRelated_Std').datagrid({
				title:'未建立关系的计量标准',
				width:800,
				height:400,
				singleSelect:true, 
                nowrap: false,
                striped: true,
				url:'',
				sortName: 'Id',
				remoteSort: false,
				singleSelect:true, 
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'SLocaleCode',title:'所内编号',width:80,align:'center'},
					{field:'CertificateCode',title:'计量标准证书号',width:100,align:'center'},
					{field:'Name',title:'计量标准名称',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'NameEn',title:'英文名称',width:100,align:'center'},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},				
					{field:'StandardCode',title:'计量标准代码',width:80,align:'center'},
					{field:'ProjectCode',title:'项目编号',width:80,align:'center'},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value == 0 || value == '0')
						{
							rowData['Status']=0;
						    return "正常";
						}
						else if(value == 1 || value == '1')
						{
							rowData['Status']=1;
							return '<span style="color:red">注销</span>';
						}
					}},
					{field:'CreatedBy',title:'建标单位',width:120,align:'center'},
					{field:'IssuedBy',title:'发证单位',width:120,align:'center'},
					{field:'IssueDate',title:'发证日期',width:100,align:'center'},
					{field:'ValidDate',title:'有效期',width:100,align:'center'},
					{field:'Range',title:'测量范围',width:80,align:'center'},
					{field:'Uncertain',title:'不确定度误差',width:120,align:'center'},
					{field:'SIssuedBy',title:'社会证发证机关',width:120,align:'center'},
					{field:'SCertificateCode',title:'社会证证书号',width:120,align:'center'},
					{field:'SIssueDate',title:'社会证发证日期',width:120,align:'center'},
					{field:'SValidDate',title:'社会证有效日期',width:120,align:'center'},
					{field:'SRegion',title:'社会证有效区域',width:120,align:'center'},
					{field:'SAuthorizationCode',title:'社会证授权证书号',width:120,align:'center'},
					{field:'WarnSlot',title:'有效期预警天数',width:120,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null)
							return "";
						return value+"天";
					}},
					{field:'Handler',title:'项目负责人',width:80,align:'center'},
					{field:'ProjectType',title:'项目类型',width:120,align:'center'},
					{field:'Remark',title:'备注',width:200,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'确定',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table_TgtAppStdApp_UnRelated_Std').datagrid('getSelected');
						$('#StdStdApp_StandardId').combobox('loadData',[{'id':select.Id,'name':select.Name}]);
						$('#StdStdApp_StandardId').combobox('select',select.Id);
						$('#div_UnRelated_Std').dialog('close');
					}
				}]
			});
			
			var products1 = [
				{id:0,name:'是'},
				{id:1,name:'否'}
			];
			var lastIndex1;
			$('#table_StdStdApp_StdApp').datagrid({  //标准器具信息
				title:'标准器具',
				width:800,
				height:400,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'',
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'器具名称',width:100,align:'center'},
					{field:'Model',title:'型号规格',width:100,align:'center'},
					{field:'Range',title:'测量范围',width:80,ealign:'center'},
					{field:'Uncertain',title:'不确定度',width:50,align:'center'},
					{field:'Release',title:'出厂信息',width:120,align:'center'},
					{field:'PermanentCode',title:'固定资产编号',width:120,align:'center'},
					{field:'IsMain',title:'主要器具',width:120,align:'center',editor:{
						type:'combobox',
						options:{
							valueField:'id',
							textField:'name',
							data:products1,
							required:true
						}
					},
					formatter:function(value,rowData,rowIndex){
						if(value == 1 || value == '1')
						{
							rowData['IsMain']=1;
							return "否";
						}
						else
						{
							rowData['IsMain']=0;
							return "是";
						}
					}},
					{field:'Sequence',title:'序号',width:80,editor:'numberbox',align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value==null||value=="")
						{
							rowData['Sequence']=1;
							return 1;
						}
						else
						{
							rowData['Sequence']=value;
							return value;
						}
					}}
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'提交',
					iconCls:'icon-save',
					handler:function(){
						var rows = $('#table_StdStdApp_StdApp').datagrid('getSelections');
						var stdAppStr = "";
						var IsMainStr = "";
						var SequenceStr = "";
						for(var i=0;i<rows.length;i++)
						{
							stdAppStr = stdAppStr + rows[i].Id + "|";
							IsMainStr = IsMainStr + rows[i].IsMain + "|";
							SequenceStr = SequenceStr + rows[i].Sequence + "|";
						}
						$.ajax({
							type:'POST',
							url:'/jlyw/RelationShipServlet.do?method=1',
							data:'StandardId='+$('#StdStdApp_StandardId').combobox('getValue')+'&StandardApplianceIdStr='+stdAppStr + '&IsMainStr='+IsMainStr + '&SequenceStr=' + SequenceStr,
							beforeSend: function(XMLHttpRequest){
							},
							success:function(data){
								var result = eval("("+data+")");
								$.messager.alert('提示',result.msg,'info');
								if(result.IsOK)
								{
									$('#div_StdApp').dialog('close');
									$('#table_Std_StdApp').datagrid('reload');
								}
							}
						});
						$('#table_StdStdApp_StdApp').datagrid('clearSelections');
					}
				},'-',{
					text:'完成编辑',
					iconCls:'icon-save',
					handler:function(){
						$('#table_StdStdApp_StdApp').datagrid('acceptChanges');
					}
				}],
				onBeforeLoad:function(){
					$(this).datagrid('rejectChanges');
				},
				onLoadSuccess:function(data){
					
				},
				onClickRow:function(rowIndex){
//					if (lastIndex != rowIndex){
						$('#table_StdStdApp_StdApp').datagrid('endEdit', lastIndex1);
						$('#table_StdStdApp_StdApp').datagrid('beginEdit', rowIndex);
		//			}
					lastIndex1 = rowIndex;
				}
			});
			
			
			$('#table_TgtAppStdApp_StdApp').datagrid({  //标准器具信息
				title:'标准器具',
				width:800,
				height:400,
				singleSelect:false, 
                nowrap: false,
                striped: true,
				url:'',
				idField:'Id',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				columns:[[
					{field:'Name',title:'器具名称',width:100,align:'center'},
					{field:'Model',title:'型号规格',width:100,align:'center'},
					{field:'Range',title:'测量范围',width:80,ealign:'center'},
					{field:'Uncertain',title:'不确定度',width:50,align:'center'},
					{field:'Release',title:'出厂信息',width:120,align:'center'},
					{field:'PermanentCode',title:'固定资产编号',width:120,align:'center'}
				]],
				pagination:false,
				rownumbers:true,
				toolbar:[{
					text:'提交',
					iconCls:'icon-save',
					handler:function(){
						var rows = $('#table_TgtAppStdApp_StdApp').datagrid('getSelections');
						var stdAppStr = "";
						for(var i=0;i<rows.length;i++)
						{
							stdAppStr = stdAppStr + rows[i].Id + "|";
						}
						$.ajax({
							type:'POST',
							url:'/jlyw/RelationShipServlet.do?method=4',
							data:'TgtAppId='+$('#StdAppTgtApp_TargetApplianceId').combobox('getValue')+'&StandardApplianceIdStr='+stdAppStr,
							beforeSend: function(XMLHttpRequest){
							},
							success:function(data){
								var result = eval("("+data+")");
								$.messager.alert('提示',result.msg,'info');
								if(result.IsOK)
								{
									$('#div_Std_StdApp').dialog('close');
									$('#table_StdApp_TgtApp').datagrid('reload');
								}
							}
						});
						$('#table_StdStdApp_StdApp').datagrid('clearSelections');
					}
				}]
			});
			
			$('#table_UnRelated_TgtApp').datagrid({
				title:'未建立关系的受检器具',
				width:800,
				height:400,
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
					{field:'StandardName',title:'器具标准名称',width:100,align:'center'},
					{field:'Name',title:'受检器具名称',width:100,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(rowData.Status==1)
							return '<span style="color:red">'+value+'</span>';
						else
							return value;
					}},
					{field:'Brief',title:'拼音简码',width:100,align:'center'},
					{field:'NameEn',title:'器具英文名称',width:100,align:'center'},
					{field:'Code',title:'器具编码',width:80,align:'right'},
					{field:'Fee',title:'标准费用(元)',width:100,align:'center'},
					{field:'SRFee',title:'小修费用(元)',width:100,align:'center'},
					{field:'MRFee',title:'中修费用(元)',width:100,align:'center'},
					{field:'LRFee',title:'大修费用(元)',width:100,align:'center'},
					{field:'PromiseDuration',title:'承诺检出期(天)',width:120,align:'center'},
					{field:'TestCycle',title:'检定周期(月)',width:100,align:'center'},
					{field:'Certification',title:'认证',width:150,align:'center',
					formatter:function(value,rowData,rowIndex){
						if(value=="0"||value==0)
						{
							rowData['Certification']="0";
							return "";
						}
						else if(value=="1"||value==1)
						{
							rowData['Certification']="1";
							return "计量认证";
						}
						else if(value=="2"||value==2)
						{
							rowData['Certification']="2";
							return "实验室认可";
						}
						else if(value=="3"||value==3)
						{
							rowData['Certification']="3";
							return "实验室认可、计量认证";
						}
						else if(value=="4"||value==4)
						{
							rowData['Certification']="4";
							return "法定机构授权";
						}
						else if(value=="5"||value==5)
						{
							rowData['Certification']="5";
							return "法定机构授权、计量认证";
						}
						else if(value=="6"||value==6)
						{
							rowData['Certification']="6";
							return "法定机构授权、实验室认可";
						}
						else if(value=="7"||value==7)
						{
							rowData['Certification']="7";
							return "法定机构授权、实验室认可、计量认证";
						}
					}},
					{field:'Status',title:'状态',width:80,align:'center',
					formatter:function(value,rowData,rowIndex){
							if(value == 0 || value == '0')
							{
								rowData['Status']=0;
							    return "正常";
							}
							else
							{
								rowData['Status']=1;
								return '<span style="color:red">注销</span>';
							}
					}},
					{field:'Remark',title:'备注',width:80,align:'center'}
				]],
				pagination:true,
				rownumbers:true,
				toolbar:[{
					text:'确定',
					iconCls:'icon-edit',
					handler:function(){
						var select = $('#table_UnRelated_TgtApp').datagrid('getSelected');
						var var_name = $("input[name='relation']:checked").val();
						if(var_name=="2"){
							$('#StdAppTgtApp_TargetApplianceId').combobox('loadData',[{'id':select.Id,'name':select.Name}]);
							$('#StdAppTgtApp_TargetApplianceId').combobox('select',select.Id);
							$('#div_UnRelated_TgtApp').dialog('close');
						}
						else if(var_name=="3"){
							$('#StdTgtApp_TargetApplianceId').combobox('loadData',[{'id':select.Id,'name':select.Name}]);
							$('#StdTgtApp_TargetApplianceId').combobox('select',select.Id);
							$('#div_UnRelated_TgtApp').dialog('close');
						}
						else if(var_name=="5"){
							$('#TgtAppSpec_TargetApplianceId').combobox('loadData',[{'id':select.Id,'name':select.Name}]);
							$('#TgtAppSpec_TargetApplianceId').combobox('select',select.Id);
							$('#div_UnRelated_TgtApp').dialog('close');
						}
					}
				}]
			});
			
			$('#loading').hide();

		});
		
		//window.onload =function(){$('#loading').hide();}
		//查询按钮
		function query(obj){
			var var_name = $("input[name='relation']:checked").val();
			if(obj.value=="1"){
				$('#panel_Std_StdApp').panel('open');
				$('#panel_StdApp_TgtApp').panel('close');
				$('#panel_Std_TgtApp').panel('close');
				$('#panel_Std_Spec').panel('close');
				$('#panel_TgtApp_Spec').panel('close');
				$('#panel_TgtApp').panel('close');
			}
			else if(obj.value=="2"){
				$('#panel_StdApp_TgtApp').panel('open');
				$('#panel_Std_StdApp').panel('close');
				$('#panel_Std_TgtApp').panel('close');
				$('#panel_Std_Spec').panel('close');
				$('#panel_TgtApp_Spec').panel('close');
				$('#panel_TgtApp').panel('close');
			}
			else if(obj.value=="3"){
				$('#panel_Std_TgtApp').panel('open');
				$('#panel_Std_StdApp').panel('close');
				$('#panel_StdApp_TgtApp').panel('close');
				$('#panel_Std_Spec').panel('close');
				$('#panel_TgtApp_Spec').panel('close');
				$('#panel_TgtApp').panel('close');
			}
			else if(obj.value=="4"){
				$('#panel_Std_Spec').panel('open');
				$('#panel_Std_StdApp').panel('close');
				$('#panel_StdApp_TgtApp').panel('close');
				$('#panel_Std_TgtApp').panel('close');
				$('#panel_TgtApp_Spec').panel('close');
				$('#panel_TgtApp').panel('close');
			}
			else if(obj.value=="5"){
				$('#panel_TgtApp_Spec').panel('open');
				$('#panel_Std_StdApp').panel('close');
				$('#panel_StdApp_TgtApp').panel('close');
				$('#panel_Std_TgtApp').panel('close');
				$('#panel_Std_Spec').panel('close');
				$('#panel_TgtApp').panel('close');
			}
			else if(obj.value=="6"){
				$('#panel_TgtApp').panel('open');
				$('#panel_TgtApp_Spec').panel('close');
				$('#panel_Std_StdApp').panel('close');
				$('#panel_StdApp_TgtApp').panel('close');
				$('#panel_Std_TgtApp').panel('close');
				$('#panel_Std_Spec').panel('close');
			}
			else{
				$.messager.alert('提示','请选择需要查询的标准项目','info');
			}
		}
		
		function Query_Std_StdApp(){
			var var_name = $("input[name='Radio_Std_StdApp']:checked").val();
			$('#table_Std_StdApp').datagrid('options').url='/jlyw/RelationShipServlet.do?method=2';
			$('#table_Std_StdApp').datagrid('options').queryParams={'param':encodeURI(var_name),'queryStr':encodeURI($('#Std_StdApp').val())};
			$('#table_Std_StdApp').datagrid('reload');
		}
		
		function Query_StdApp_TgtApp(){
			var var_name = $("input[name='Radio_StdApp_TgtApp']:checked").val();
			$('#table_StdApp_TgtApp').datagrid('options').url='/jlyw/RelationShipServlet.do?method=5';
			$('#table_StdApp_TgtApp').datagrid('options').queryParams={'param':encodeURI(var_name),'queryStr':encodeURI($('#StdApp_TgtApp').val())};
			$('#table_StdApp_TgtApp').datagrid('reload');
		}
		
		function Query_Std_TgtApp(){
			var var_name = $("input[name='Radio_Std_TgtApp']:checked").val();
			$('#table_Std_TgtApp').datagrid('options').url='/jlyw/RelationShipServlet.do?method=8';
			$('#table_Std_TgtApp').datagrid('options').queryParams={'param':encodeURI(var_name),'queryStr':encodeURI($('#Std_TgtApp').val())};
			$('#table_Std_TgtApp').datagrid('reload');
		}
		
		function Query_Std_Spec(){
			var var_name = $("input[name='Radio_Std_Spec']:checked").val();
			$('#table_Std_Spec').datagrid('options').url='/jlyw/RelationShipServlet.do?method=13';
			$('#table_Std_Spec').datagrid('options').queryParams={'param':encodeURI(var_name),'queryStr':encodeURI($('#Std_Spec').val())};
			$('#table_Std_Spec').datagrid('reload');
		}
		
		function Query_TgtApp_Spec(){
			var var_name = $("input[name='Radio_TgtApp_Spec']:checked").val();
			$('#table_TgtApp_Spec').datagrid('options').url='/jlyw/RelationShipServlet.do?method=11';
			$('#table_TgtApp_Spec').datagrid('options').queryParams={'param':encodeURI(var_name),'queryStr':encodeURI($('#TgtApp_Spec').val())};
			$('#table_TgtApp_Spec').datagrid('reload');
		}
		
		//取消按钮
		function cancel(){
			$(':input[name=relation]').each(function(){  //取消radio的选中
           		$(this).attr('checked','checked');     
            	this.checked = false;   
       		})
			 
			$('#panel_Std_StdApp').panel('close');  //隐藏各datagrid
			$('#panel_StdApp_TgtApp').panel('close');
			$('#panel_Std_TgtApp').panel('close');
			$('#panel_Std_Spec').panel('close');
			$('#panel_TgtApp_Spec').panel('close');
		}
		
		/*function cancel_Std_StdApp(){
			$('#StdStdApp_StandardId').combobox('setValue',"");
			$('#StdStdApp_StandardApplianceId').combobox('setValue',"");
			$('#StdStdApp_IsMain').combobox('setValue',"");
			$('#StdStdApp_Sequence').val('');
		}*/
		
		/*function cancel_StdApp_TgtApp(){
			$('#StdAppTgtApp_TargetApplianceId').combobox('setValue',"");
			$('#StdAppTgtApp_StandradId').combobox('setValue',"");
			$('#StdAppTgtApp_StandardApplianceId').combobox('setValue',"");
		}*/
		
		function cancel_Std_TgtApp(){
			$('#StdTgtApp_StandardId').combobox('setValue',"");
			$('#StdTgtApp_TargetApplianceId').combobox('setValue',"");
		}
		
		/*function cancel_Std_Spec(){
			$('#StandardId_3').val('');
			$('#SpecificationId_3').val('');
			$('#TargetApplianceId_3').val('');
		}*/
		
		function cancel_TgtApp_Spec(){
			$('#TgtApp_Spec_TargetApplianceId').combobox('setValue',"");
			$('#TgtApp_Spec_SpecificationId').combobox('setValue',"");
		}
		
		//新增按钮
		/*function Sub_Std_StdApp(){
			$('#frm_StdStdApp').form('submit',{
				url:'/jlyw/RelationShipServlet.do?method=1',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_Std_StdApp').datagrid('reload');
				}
			});
			
		}*/
		
		/*function Sub_StdApp_TgtApp(){
			$('#frm_StdAppTgtApp').form('submit',{
				url:'/jlyw/RelationShipServlet.do?method=4',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_StdApp_TgtApp').datagrid('reload');
				}
			});
			
		}*/
		
		function Sub_Std_TgtApp(){
			$('#frm_StdTgtApp').form('submit',{
				url:'/jlyw/RelationShipServlet.do?method=7',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_Std_TgtApp').datagrid('reload');
				}
			});
			
		}
		
		/*function Sub_Std_Spec(){
			
		}*/
		
		function Sub_TgtApp_Spec(){
			$('#frm_TgtApp_Spec').form('submit',{
				url:'/jlyw/RelationShipServlet.do?method=10',
				onSubmit:function(){return $(this).form('validate');},
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_TgtApp_Spec').datagrid('reload');
				}
			});
			
		}
		
		//删除按钮
		function Delete_Std_StdApp(){
			$.ajax({
				type:'POST',
				url:'/jlyw/RelationShipServlet.do?method=3',
				data:'id='+$('#table_Std_StdApp').datagrid('getSelected').id,
				dataType:"html",
				success:function(data, textStatus){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_Std_StdApp').datagrid('reload');
				}
			});
		}
		
		function Delete_StdApp_TgtApp(){
			$.ajax({
				type:'POST',
				url:'/jlyw/RelationShipServlet.do?method=6',
				data:'id='+$('#table_StdApp_TgtApp').datagrid('getSelected').id,
				dataType:"html",
				success:function(data){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_StdApp_TgtApp').datagrid('reload');
				}
			});
		}
		
		function Delete_Std_TgtApp(){
			$.ajax({
				type:'POST',
				url:'/jlyw/RelationShipServlet.do?method=9',
				data:'id='+$('#table_Std_TgtApp').datagrid('getSelected').id,
				dataType:"html",
				success:function(data, textStatus){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_Std_TgtApp').datagrid('reload');
				}
			});
			
		}
		
		function Delete_TgtApp_Spec(){
			$.ajax({
				type:'POST',
				url:'/jlyw/RelationShipServlet.do?method=12',
				data:'id='+$('#table_TgtApp_Spec').datagrid('getSelected').id,
				dataType:"html",
				success:function(data, textStatus){
					var result = eval("("+data+")");
					$.messager.alert('提示',result.msg,'info');
					$('#table_TgtApp_Spec').datagrid('reload');
				}
			});
			
		}
		
		function ShowUnRelatedStd(){
			$('#div_UnRelated_Std').window('open');
			$('#table_TgtAppStdApp_UnRelated_Std').datagrid('options').url='/jlyw/RelationShipServlet.do?method=16';
			$('#table_TgtAppStdApp_UnRelated_Std').datagrid('reload');
		}
		
		function ShowUnRelatedTgtApp(obj){
			$('#div_UnRelated_TgtApp').window('open');
			$('#table_UnRelated_TgtApp').datagrid('options').url='/jlyw/RelationShipServlet.do?method=17&Type=' + obj;
			$('#table_UnRelated_TgtApp').datagrid('reload');
		}
		
		function queryTgtApp(){
			if($('#TgtApp').combobox('getValue')==""){
				$.messager.alert('提示','请选择要查询的受检器具！','warning');
				return false;
			}
			$('#table_StdApp_TgtApp1').datagrid('options').url='/jlyw/RelationShipServlet.do?method=5';
			$('#table_StdApp_TgtApp1').datagrid('options').queryParams={'param':encodeURI(2),'queryStr':encodeURI($('#TgtApp').combobox('getValue'))};
			$('#table_StdApp_TgtApp1').datagrid('reload');
			
			$('#table_Std_TgtApp1').datagrid('options').url='/jlyw/RelationShipServlet.do?method=8';
			$('#table_Std_TgtApp1').datagrid('options').queryParams={'param':encodeURI(2),'queryStr':encodeURI($('#TgtApp').combobox('getValue'))};
			$('#table_Std_TgtApp1').datagrid('reload');
			
			$('#table_TgtApp_Spec1').datagrid('options').url='/jlyw/RelationShipServlet.do?method=11';
			$('#table_TgtApp_Spec1').datagrid('options').queryParams={'param':encodeURI(1),'queryStr':encodeURI($('#TgtApp').combobox('getValue'))};
			$('#table_TgtApp_Spec1').datagrid('reload');
		}