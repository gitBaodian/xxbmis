$('#dtli_table').datagrid({
	url: 'depottype_list_js.action',
    iconCls: "icon-edit",
    title: "设备用途",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    singleSelect: true,//单选
    toolbar: '#dtli_toolbar',//工具条
    columns: [[
       {field: "id", title: "ID", width: 10, align: "center"},
       {field: "name", title: "名称", width: 20,
    		editor: {type:'validatebox',options:{required:true,validType:'length[1,10]'}}},
       {field: "sort", title: "顺序", width: 20, align: "center",
    		editor: {type:'numberbox',options:{required:true,min:1,max:100}}},
       {field: 'action', title: '操作', width:25, align: 'center',
			formatter: function(value,row,index){
				if(!row["id"])
					return;
				if(row["id"] == "未保存") {
					var save = '<a href="javascript:;" onclick="dtli_saverow('+index+')">保存</a>&nbsp;&nbsp;';
					var canel = '<a href="javascript:;" onclick="$(\'#dtli_table\').datagrid(\'deleteRow\', '+index+');">取消</a>&nbsp;&nbsp;';
					return save + canel;
				}
				//var u = '<a href="javascript:;" onclick="index_updateTab(\'新闻列表\', \'newsbase_list.action?page.ncId='+row["id"]+'&\', true)">打开</a>';
				var u = '';
				if (row.editing){
					var s = '<a href="javascript:;" onclick="dtli_updaterow('+index+')">保存</a>&nbsp;&nbsp;';
					var c = '<a href="javascript:;" onclick="dtli_cancelrow('+index+')">取消</a>&nbsp;&nbsp;';
					return s + c + u;
				} else {
					var e = '<a href="javascript:;" onclick="dtli_editrow('+index+')">编辑</a>&nbsp;&nbsp;';
					var d = '<a href="javascript:;" onclick="dtli_deleterow('+index+','+row["id"]+')">删除</a>&nbsp;&nbsp;';
					return e + d + u;
				}
			}
		}
    ]], onBeforeEdit : function(index, row) {
		row.editing = true;
		dtli_updateAction(index);
	}, onAfterEdit : function(index, row) {
		row.editing = false;
		dtli_updateAction(index);
	}, onCancelEdit : function(index, row) {
		row.editing = false;
		dtli_updateAction(index);
	}, onDblClickRow: function(index, row) {
		dtli_editrow(index);
    }
});
function dtli_updateAction(index){
	$('#dtli_table').datagrid('updateRow',{index:index,row:{action:''}});
}
function dtli_updaterow(index){
	index_mess("更新中...", 0);
	var oldrow = $.extend({},$('#dtli_table').datagrid('getRows')[index]);//深度拷贝，而非引用
	$('#dtli_table').datagrid('endEdit', index);
	var newrow = $('#dtli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("请正确输入名称和顺序!", 1);
		return;
	}
	if(oldrow.name == newrow.name &&
			oldrow.sort == newrow.sort) {
		index_mess("未更新内容!", 2);
		return;
	}
	$.post("depottype_change_js.action", {
		"dt.name": newrow.name,
		"dt.sort": newrow.sort,
		"dt.id": newrow.id
	},function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
		} else {
			oldrow.editing = false;
			$('#dtli_table').datagrid('updateRow', {index:index,row:oldrow});
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}
function dtli_cancelrow(index){
	$('#dtli_table').datagrid('cancelEdit', index);
}
function dtli_editrow(index){
	$('#dtli_table').datagrid('beginEdit', index);
}
function dtli_deleterow(index,id){
	$.messager.confirm('提示','确定要删除吗?',function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("depottype_remove_js.action?json=" + id, function(data) {
	    		if(data.status == 0) {
	    			$('#dtli_table').datagrid('load');//如果不重新载入将对其下面每一行的编辑行号产生错位
	    			//$('#dtli_table').datagrid('deleteRow', index);
	    			index_mess(data.mess, 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    			if(data.login == false) {
	    				index_login();
	    			}
	    		}
	    	});
		}
	});
}
function dtli_addDt() {
	var rows = $('#dtli_table').datagrid('getRows');
	var length = rows.length;
	//只允许编辑一行
	if(length > 0 && rows[length-1]["id"] == "未保存") {
		length = length - 1;
	} else {
		$('#dtli_table').datagrid('appendRow', {
			"id": '未保存',
			"sort": length + 1
		});
	}
	$('#dtli_table').datagrid('selectRow', length);
	$('#dtli_table').datagrid('beginEdit', length);
}
function dtli_saverow(index) {
	index_mess("添加中...", 0);
	$('#dtli_table').datagrid('endEdit', index);
	var newrow = $('#dtli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("请正确输入名称和顺序!", 1);
		return;
	}
	$.post("depottype_add_js.action", {
		"dt.name" : newrow.name,
		"dt.sort" : newrow.sort
	}, function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
    		$('#dtli_table').datagrid('load');
		} else {
			index_mess(data.mess, 1);
			$('#dtli_table').datagrid('beginEdit', index);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}