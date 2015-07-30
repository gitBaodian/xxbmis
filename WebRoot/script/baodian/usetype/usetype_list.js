function utli_dtfm(field, title) {
	return {
		field: field,
		title: title,
		width: 25,
		align: "center",
		editor: {
			type: 'combobox',
			options: {
				valueField: 'id',
				textField: 'name',
				data: utli_data.dts,
				editable: false,
				panelHeight: 120
		}}, formatter: function(value){
		   for(var i=0; i<utli_data.dts.length; i++){
			   if (utli_data.dts[i].id == value) return utli_data.dts[i].name;
		   }
		   return value;
		}
	};
}
$('#utli_table').datagrid({
	url: 'usetype_list_js.action',
    iconCls: "icon-edit",
    title: "使用类别",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    singleSelect: true,//单选
    toolbar: '#utli_toolbar',//工具条
    columns: [[
    	{field: "id", title: "ID", width: 10, align: "center"},
    	{field: "sort", title: "顺序", width: 15, align: "center",
    		editor: {type:'numberbox',options:{required:true,min:1,max:100}}},
    	{field: "name", title: "名称", width: 30,
    		editor: {type:'validatebox',options:{required:true,validType:'length[1,20]'}}},
		{field: 'gd', title: '设备', width: 25,
    		editor: {
    			type: 'combotree',
    			options: {
    				required: true,
    				data: index_changeTree(utli_data.gds),
    				panelHeight: 150
    		}}, formatter: function(value) {
        		for(var i=0; i<utli_data.gds.length; i++){
        			if (utli_data.gds[i].id == value) return utli_data.gds[i].name;
        		}
        		return value;
        	}},
    	utli_dtfm("dtout", "来源"), utli_dtfm("dtin", "目地"),
    	{field: 'action', title: '操作', width:20, align: 'center',
			formatter: function(value, row, index){
				if(!row["id"])
					return;
				if(row["id"] == "未保存") {
					var save = '<a href="javascript:;" onclick="utli_saverow('+index+')">保存</a>&nbsp;&nbsp;';
					var canel = '<a href="javascript:;" onclick="$(\'#utli_table\').datagrid(\'deleteRow\', '+index+');">取消</a>&nbsp;&nbsp;';
					return save + canel;
				}
				//var u = '<a href="javascript:;" onclick="index_updateTab(\'新闻列表\', \'newsbase_list.action?page.ncId='+row["id"]+'&\', true)">打开</a>';
				var u = '';
				if (row.editing){
					var s = '<a href="javascript:;" onclick="utli_updaterow('+index+')">保存</a>&nbsp;&nbsp;';
					var c = '<a href="javascript:;" onclick="utli_cancelrow('+index+')">取消</a>&nbsp;&nbsp;';
					return s + c + u;
				} else {
					var e = '<a href="javascript:;" onclick="utli_editrow('+index+')">编辑</a>&nbsp;&nbsp;';
					var d = '<a href="javascript:;" onclick="utli_deleterow('+index+','+row["id"]+')">删除</a>&nbsp;&nbsp;';
					return e + d + u;
				}
			}
		}
    ]], onBeforeEdit : function(index, row) {
		row.editing = true;
		utli_updateAction(index);
	}, onAfterEdit : function(index, row) {
		row.editing = false;
		utli_updateAction(index);
	}, onCancelEdit : function(index, row) {
		row.editing = false;
		utli_updateAction(index);
	}, onDblClickRow: function(index, row) {
		utli_editrow(index);
    }
});
function utli_updateAction(index){
	$('#utli_table').datagrid('updateRow',{index:index,row:{action:''}});
}
function utli_updaterow(index){
	index_mess("更新中...", 0);
	var oldrow = $.extend({},$('#utli_table').datagrid('getRows')[index]);//深度拷贝，而非引用
	$('#utli_table').datagrid('endEdit', index);
	var newrow = $('#utli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("请正确输入!", 1);
		return;
	}
	if(oldrow.name == newrow.name &&
			oldrow.sort == newrow.sort &&
			oldrow.gd == newrow.gd &&
			oldrow.dtin == newrow.dtin &&
			oldrow.dtout == newrow.dtout) {
		index_mess("未更新内容!", 2);
		return;
	}
	var paras = {
		"ut.id": newrow.id,
		"ut.name": newrow.name,
		"ut.sort": newrow.sort,
		"ut.gd.id": newrow.gd
	};
	if(newrow.dtin != 0) {
		paras["ut.dtin.id"] = newrow.dtin;
	}
	if(newrow.dtout != 0) {
		paras["ut.dtout.id"] = newrow.dtout;
	}
	$.post("usetype_change_js.action", paras, function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
		} else {
			oldrow.editing = false;
			$('#utli_table').datagrid('updateRow', {index:index,row:oldrow});
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}
function utli_cancelrow(index){
	$('#utli_table').datagrid('cancelEdit', index);
}
function utli_editrow(index){
	$('#utli_table').datagrid('beginEdit', index);
}
function utli_deleterow(index,id){
	$.messager.confirm('删除使用类别','确定要删除此条记录吗?',function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("usetype_remove_js.action?json=" + id, function(data) {
	    		if(data.status == 0) {
	    			$('#utli_table').datagrid('load');//如果不重新载入将对其下面每一行的编辑行号产生错位
	    			//$('#utli_table').datagrid('deleteRow', index);
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
function utli_addDt() {
	var rows = $('#utli_table').datagrid('getRows');
	var length = rows.length;
	//只允许编辑一行
	if(length > 0 && rows[length-1]["id"] == "未保存") {
		length = length - 1;
	} else {
		$('#utli_table').datagrid('appendRow', {
			"id": '未保存',
			"sort": length + 1,
			"dtin": 0,
			"dtout": 0
		});
	}
	$('#utli_table').datagrid('selectRow', length);
	$('#utli_table').datagrid('beginEdit', length);
}
function utli_saverow(index) {
	index_mess("添加中...", 0);
	$('#utli_table').datagrid('endEdit', index);
	var newrow = $('#utli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("请正确输入!", 1);
		return;
	}
	var paras = {
		"ut.name" : newrow.name,
		"ut.sort" : newrow.sort,
		"ut.gd.id": newrow.gd
	};
	if(newrow.dtin != 0) {
		paras["ut.dtin.id"] = newrow.dtin;
	}
	if(newrow.dtout != 0) {
		paras["ut.dtout.id"] = newrow.dtout;
	}
	$.post("usetype_add_js.action", paras, function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
    		$('#utli_table').datagrid('load');
		} else {
			index_mess(data.mess, 1);
			$('#utli_table').datagrid('beginEdit', index);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}