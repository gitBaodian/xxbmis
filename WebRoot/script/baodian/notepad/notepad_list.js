npli_data = {};
$('#npli_table').datagrid({
	url: 'notepad_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#npli_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"npli_name","type":"text"},
    	"page.top": {"id":"npli_top","type":"combobox"}
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[{
    	field: 'ck', checkbox:true
    }, {
    	field: "name", title: "内容", width: 50
    }, {
    	field: "uname", title: "作者", width: 9, align: "center"
    }, {
    	field: "dname", title: "部门", width: 11, align: "center"
    }, {
    	field: "date", title: "发表时间", width: 15, align: "center",
		   formatter: function(value) {
			   return value.substring(0, 19);
		   }
    }, {
    	field: "top", title: "置顶", width: 5, align: "center",
		   formatter: function(value) {
			   if(value == 1)
				   return "是";
			   return "";
		   }
    }, {
    	field: 'action', title: '操作', width: 10, align: 'center',
			formatter: function(value, row, index) {
				return '<a href="javascript:;" onclick="npli_change(' + index + ')">编辑</a>&ensp;' +
						'<a href="javascript:;" onclick="npli_remove([' + row.id + '])">删除</a>';
						return e + d + u;
			}
    }]], onDblClickRow: npli_change
});
$("#npli_name").keydown(function(e) {
	if(e.keyCode==13) {
		$('#npli_table').datagrid('load');
	}
});
/**
 * 浏览窗口
 */
$('#npli_change').dialog({
	closed: true,
	resizable: true,
	maximizable: true,
	title: "编辑每日记事",
	width: 460,
	height: 300,
	buttons: [{
		text: '更新',
		iconCls: 'icon-ok',
		handler: function() {
			var name = $('#npli_chname').val();
			if(name.length == 0) {
				index_mess("请输入内容！", 4);
				$('#npli_chname').focus();
				return false;
			}
			if(name.length > 21785) {
				index_mess("字数超出 " + (name.length - 21785) + " 个！限制 21785 个", 4);
				$('#npli_chname').focus();
				return false;
			}
			index_mess("更新中...", 0);
			$.post("notepad_change_js.action", {
				'notepad.id': npli_data.id,
				'notepad.top': ($('#npli_chtop input:first').is(':checked')? 2: 1),
				'notepad.name': name
			}, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					return false;
				}
				index_mess("更新成功！", 2);
				$('#npli_change').dialog('close');
				$('#npli_table').datagrid('reload');
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#npli_change').dialog('close');
		}
	}], content: "<div style='padding:12px;'>" +
			"<div id='npli_chuser' style='color:#666;padding-bottom:8px;'></div>" +
			"<textarea id='npli_chname' style='width:100%;height:146px;'/>" +
			"<div id='npli_chtop'><br />置顶： " +
			"<input type='radio' name='npli_chtop'>否&emsp;" +
			"<input type='radio' name='npli_chtop'>是</div></div>",
});
/**
 * 打开浏览和编辑窗口
 */
function npli_change(index, row) {
	if(row == null) {
		row = $('#npli_table').datagrid('getRows')[index];
	}
	npli_data = row;
	$('#npli_change').dialog('open');
	$('#npli_chuser').html(row.uname + "&lt;" + row.dname + "&gt; 于 " + row.date + " 发布");
	$('#npli_chname').val(row.name);
	if(row.top == 1) {
		$('#npli_chtop input:eq(1)').attr("checked",'true');
	} else {
		$('#npli_chtop input:first').attr("checked",'true');
	}
}
/**
 * 打开第一个选中行
 */
function npli_opench() {
	var np = $('#npli_table').datagrid("getSelected");
	if(np == null) {
		index_mess("请先选择一条记录！", 4);
		return;
	}
	npli_change(0, np);
}
/**
 * 添加窗口
 */
$('#npli_add').dialog({
	closed: true,
	resizable: true,
	maximizable: true,
	title: "添加每日记事",
	width: 460,
	height: 280,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var name = $('#npli_addname').val();
			if(name.length == 0) {
				index_mess("请输入内容！", 4);
				$('#npli_addname').focus();
				return false;
			}
			if(name.length > 21785) {
				index_mess("字数超出 " + (name.length - 21785) + " 个！限制 21785 个", 4);
				$('#npli_addname').focus();
				return false;
			}
			index_mess("添加中...", 0);
			$.post("notepad_add_js.action", {
				'notepad.top': ($('#npli_addtop input:first').is(':checked')? 2: 1),
				'notepad.name': name
			}, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					return false;
				}
				index_mess("添加成功！", 2);
				$('#npli_addname').val('');
				$('#npli_addtop input:first').attr("checked",'true');
				$('#npli_add').dialog('close');
				$('#npli_table').datagrid('load');
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#npli_add').dialog('close');
		}
	}], content: "<div style='padding:12px;'>" +
			"<textarea id='npli_addname' style='width:100%;height:146px;'/>" +
			"<div id='npli_addtop'><br />置顶： " +
			"<input type='radio' name='npli_addtop'>否&emsp;" +
			"<input type='radio' name='npli_addtop'>是</div></div>",
	onOpen: function() {
		if($('#npli_addname').val() == "") {
			$('#npli_addtop input:first').attr("checked",'true');
		}
		$('#npli_addname').focus();
	}
});
/**
 * 批量删除
 */
function npli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#npli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除 ' + ids.length + ' 条记录吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("notepad_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#npli_table').datagrid('reload');
		    		index_mess("删除成功!", 2);
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
function npli_top(top) {
	var ids = new Array();
	var nps = $('#npli_table').datagrid("getChecked");
	if(nps.length == 0) {
		index_mess("请先选择！", 4);
		return;
	}
	$.each(nps, function(k, np) {
		if(np.top != top) {
			ids.push(np.id);
		}
	});
	if(ids.length == 0) {
		index_mess("全部" + (top==1?"已":"未") + "置顶，不需要操作！", 4);
		return;
	}
	index_mess("操作中...", 0);
	$.getJSON("notepad_changetop_js.action?json=" + top + "A" + ids.join('a') + "&_=" + Math.random(), function(data) {
		if(data.status == 0) {
			$('#npli_table').datagrid('reload');
    		index_mess("操作成功!", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		}
	});
}