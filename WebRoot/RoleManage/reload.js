// JavaScript Document
           $('#privilege').datagrid({
				title:'权限信息',
//				iconCls:'icon-save',
				//width:800,
				//height:350,
				pagination:true,
				rownumbers:true,
				singleSelect:false, 
				fit: true,
                nowrap: false,
                striped: true,
//				collapsible:true,
				url:'privilege_data2.json',
				sortName: 'id',
			  //sortOrder: 'desc',
				remoteSort: false,
				idField:'userid',
				frozenColumns:[[
	                {field:'ck',checkbox:true}
				]],
				
				columns:[[
					{field:'id',title:'权限编号',width:120,sortable:true},
					{field:'name',title:'权限名称',width:150},		
					{field:'description',title:'权限描述',width:150},

				]],
				
				toolbar:[{
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						$('#test').datagrid('acceptChanges');
						alert('授权成功');
					}
				},'-',{
					text:'取消',
					iconCls:'icon-undo',
					handler:function(){
						$('#test').datagrid('rejectChanges');
						alert('c取消');
					}
				}
				]
				
			});