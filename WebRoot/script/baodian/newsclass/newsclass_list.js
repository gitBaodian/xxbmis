$('#ncli_table').datagrid({
	url: 'newsclass_list_js.action',
    iconCls: "icon-edit",
    title: "用户列表",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    singleSelect: true,//单选
    showFooter: true,//表尾
    toolbar: '#ncli_toolbar',//工具条
    columns: [[
       {field: "newsclass.id", title: "ID", width: 10, align: "center"},
       {field: "newsclass.name", title: "名称", width: 20,
    		editor: {type:'validatebox',options:{required:true,validType:'length[1,20]'}}},
       {field: "newsclass.position", title: "顺序", width: 20, align: "center",
    		editor: {type:'numberbox',options:{required:true,min:1,max:100}}},
       {field: "newsclass.display", title: "是否显示", width: 25, align: "center", editor: {type:'checkbox',options:{on:'显示',off:''}}},
       {field: "newsclass.review", title: "需要审核", width: 25, align: "center", editor: {type:'checkbox',options:{on:'需要',off:''}}},
       {field: "num", title:"新闻数量", width: 25, align: "center"},
       {field: 'action', title: '操作', width:25, align: 'center',
			formatter: function(value,row,index){
				if(!row["newsclass.id"])
					return;
				if(row["newsclass.id"] == "未保存") {
					var save = '<a href="javascript:;" onclick="ncli_saverow('+index+')">保存</a>&nbsp;&nbsp;';
					var canel = '<a href="javascript:;" onclick="$(\'#ncli_table\').datagrid(\'deleteRow\', '+index+');">取消</a>&nbsp;&nbsp;';
					return save + canel;
				}
				var u = '<a href="javascript:;" onclick="index_updateTab(\'新闻列表\', \'newsbase_list.action?page.ncId='+row["newsclass.id"]+'&\', true)">打开</a>';
				if (row.editing){
					var s = '<a href="javascript:;" onclick="ncli_updaterow('+index+')">保存</a>&nbsp;&nbsp;';
					var c = '<a href="javascript:;" onclick="ncli_cancelrow('+index+')">取消</a>&nbsp;&nbsp;';
					return s + c + u;
				} else {
					var e = '<a href="javascript:;" onclick="ncli_editrow('+index+')">编辑</a>&nbsp;&nbsp;';
					var d = '<a href="javascript:;" onclick="ncli_deleterow('+index+','+row["newsclass.id"]+')">删除</a>&nbsp;&nbsp;';
					return e + d + u;
				}
			}
		}
    ]], onBeforeEdit : function(index, row) {
		row.editing = true;
		ncli_updateAction(index);
	}, onAfterEdit : function(index, row) {
		row.editing = false;
		ncli_updateAction(index);
	}, onCancelEdit : function(index, row) {
		row.editing = false;
		ncli_updateAction(index);
	}, onDblClickRow: function(index, row) {
		ncli_editrow(index);
    }
});
function ncli_updateAction(index){
	$('#ncli_table').datagrid('updateRow',{index:index,row:{action:''}});
}
function ncli_updaterow(index){
	index_mess("更新中...", 0);
	var oldrow = $.extend({},$('#ncli_table').datagrid('getRows')[index]);//深度拷贝，而非引用
	$('#ncli_table').datagrid('endEdit', index);
	var newrow = $('#ncli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("名称和顺序不能为空!", 1);
		return;
	}
	var paras = '{';
	var change = false;
	if(oldrow["newsclass.name"] != newrow["newsclass.name"]) {
		paras = paras + '"newsclass.name":"'
			+ newrow["newsclass.name"].replace(/\\/g, '')
				.replace(/"/g, '').replace(/'/g, '')+'",';
		change = true;
	}
	if(oldrow["newsclass.position"] != newrow["newsclass.position"]){
		change = true;
	}
	paras = paras + '"newsclass.position":"'
		+ newrow["newsclass.position"] + '",';
	if(oldrow["newsclass.display"] != newrow["newsclass.display"]){
		change = true;
	}
	if(newrow["newsclass.display"] == "")
		paras = paras + '"newsclass.display":"0",';
	else
		paras = paras + '"newsclass.display":"1",';
	if(oldrow["newsclass.review"] != newrow["newsclass.review"]){
		change = true;
	}
	if(newrow["newsclass.review"] == "")
		paras = paras + '"newsclass.review":"0",';
	else
		paras = paras + '"newsclass.review":"1",';
	if(change) {//有更新内容
		paras = paras + '"newsclass.id":"'+newrow["newsclass.id"]+'"}';
	} else {
		index_mess("未更新内容!", 2);
		return;
	}
	$.post("newsclass_change_js.action",$.parseJSON(paras),function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
		} else {
			oldrow.editing = false;
			$('#ncli_table').datagrid('updateRow', {index:index,row:oldrow});
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}
function ncli_cancelrow(index){
	$('#ncli_table').datagrid('cancelEdit', index);
}
function ncli_editrow(index){
	$('#ncli_table').datagrid('beginEdit', index);
}
function ncli_deleterow(index,id){
	$.messager.confirm('提示','确定要删除吗?',function(r){
		if (r){
			index_mess("删除中...", 0);
			var newNums = $('#ncli_table').datagrid('getRows')[index]["num"];
			if(newNums != 0) {
				index_mess("存在 "+newNums+" 条新闻，不能删除！", 1);
				return;
			}
			$.getJSON("newsclass_remove_js.action",{"newsclass.id":id},function(data) {
	    		if(data.status == 0) {
	    			$('#ncli_table').datagrid('load');//如果不重新载入将对其下面每一行的编辑行号产生错位
	    			//$('#ncli_table').datagrid('deleteRow', index);
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
function ncli_addNc() {
	var rows = $('#ncli_table').datagrid('getRows');
	var length = rows.length;
	//只允许编辑一行
	if(length > 0 && rows[length-1]["newsclass.id"] == "未保存") {
		length = length - 1;
	} else {
		$('#ncli_table').datagrid('appendRow', {
			"newsclass.id": '未保存',
			"newsclass.position": length + 1,
			"newsclass.display": '显示',
			"num": 0
		});
	}
	$('#ncli_table').datagrid('selectRow', length);
	$('#ncli_table').datagrid('beginEdit', length);
}
function ncli_saverow(index) {
	index_mess("添加中...", 0);
	$('#ncli_table').datagrid('endEdit', index);
	var newrow = $('#ncli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("名称和顺序不能为空！", 1);
		return;
	}
	var paras = '{';
	paras = paras + '"newsclass.name":"'
		+ newrow["newsclass.name"].replace(/\\/g, '')
			.replace(/"/g, '').replace(/'/g, '')+'",';
	paras = paras + '"newsclass.position":"'
		+ newrow["newsclass.position"] + '",';
	if(newrow["newsclass.display"] == "")
		paras = paras + '"newsclass.display":"0",';
	else
		paras = paras + '"newsclass.display":"1",';
	if(newrow["newsclass.review"] == "")
		paras = paras + '"newsclass.review":"0"}';
	else
		paras = paras + '"newsclass.review":"1"}';
	$.post("newsclass_add_js.action", $.parseJSON(paras), function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
    		$('#ncli_table').datagrid('load');
		} else {
			index_mess(data.mess, 1);
			$('#ncli_table').datagrid('beginEdit', index);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}