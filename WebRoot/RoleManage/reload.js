// JavaScript Document
           $('#privilege').datagrid({
				title:'Ȩ����Ϣ',
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
					{field:'id',title:'Ȩ�ޱ��',width:120,sortable:true},
					{field:'name',title:'Ȩ������',width:150},		
					{field:'description',title:'Ȩ������',width:150},

				]],
				
				toolbar:[{
					text:'����',
					iconCls:'icon-save',
					handler:function(){
						$('#test').datagrid('acceptChanges');
						alert('��Ȩ�ɹ�');
					}
				},'-',{
					text:'ȡ��',
					iconCls:'icon-undo',
					handler:function(){
						$('#test').datagrid('rejectChanges');
						alert('cȡ��');
					}
				}
				]
				
			});