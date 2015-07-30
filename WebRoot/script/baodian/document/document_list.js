$('#docli_table').treegrid({
	title: "公共文档",
	rownumbers: true,
	//animate: true,
	fitColumns: true,
	singleSelect: false,
	toolbar: '#docli_tb',
    idField: 'id',
    treeField: 'text',
    columns:[[
        {field: 'text', title: '文档', width: 60},
        {field: 'date', title:'最后修改时间', width: 25, align:'center'},
		{field: 'action', title: '操作', width:15, align:'center',
        	formatter: function(value, row, index){
        		var str = '';
        		if(row.id > 0) {//目录
        			str = '<a href="javascript:;" onclick="docli_reopen(' + row.id + ')">打开</a>' +
        				'&emsp;<a href="javascript:;" onclick="index_openTab(\'添加公共文档\',\'document_add.action?json=' + row.id + '&_\',true);">添加</a>';
        		} else {//文档
        			str = '<a href="javascript:;" onclick="docli_read(' + (-row.id) + ')">查看</a>' +
        				'&emsp;<a href="javascript:;" onclick="docli_edit(' + (-row.id) + ')">编辑</a>';
        		}
        		return str;
        	}}
    ]], onBeforeExpand: docli_onBeforeExpand,
    onDblClickRow: function(row) {
    	if(row.id > 0) {
    		//交替打开和关闭目录
    		$('#docli_table').treegrid('toggle', row.id);
    	} else {
    		docli_read(-row.id, row.text);
    	}
    }
});
$('#docli_table').treegrid('loadData', index_changeTree(docli_zNodes, {state:"closed",isParent:true}));
$('#docli_tree').tree({
	data: index_changeTree(docli_zNodes,  {'iconCls':'eu-folder-open'}),
	onDblClick: function(node) {
		$('#docli_tree').tree('toggle', node.target);
	}
});
$('#docli_name').keydown(function(e) {
	if(e.keyCode==13){
		docli_search();
	}
});
/**
 * 打开之前先判断是否已经打开过，如果没有，读取children数据
 * @param row 添加opened属性，标记是否已经打开过
 */
function docli_onBeforeExpand(row) {
	if(row.opened != true) {
		$('#docli_table').treegrid("loading");
		index_mess("读取中...", 0);
		$.ajax({
			async: false,
			url: "document_list_js.action?json=" + row.id + "&_=" + Math.random(),
			dataType: "json",
			success: function(data) {
    			if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					return false;
				} else {
					index_mess("读取成功！", 2);
					if(data.length != 0) {
						$('#docli_table').treegrid('append',{
							parent: row.id,
							data: data
						});
					}
					row.opened = true;
				}
			}
		});
		$('#docli_table').treegrid("loaded");
	}
}
/**
 * 打开目录，如果已经打开过，就重新打开
 */
function docli_reopen(dirId) {
	var node = $('#docli_table').treegrid("find", dirId);
	if(node.opened == true) {
		$.each($.extend({}, node.children), function(k, node) {
			if(node.id < 0) {//只删除文档
				$('#docli_table').treegrid("remove", node.id);
			}
		});
		$('#docli_table').treegrid('update', {
			id: dirId,
			row: {
				opened: false,
				state: "closed"
			}
		});
	}
	$('#docli_table').treegrid('expand', dirId);
}
/**
 * 查找未打开过的目录的id
 * @param nodes 全部目录
 * @param unOpen 打开过的目录的id
 */
function docli_unopenDir(nodes, unOpen) {
	$.each(nodes, function(k, node) {
		if(node.id > 0 && node.opened!=true) {
			unOpen.push(node.id);
		}
		if(node.children != null) {
			docli_unopenDir(node.children, unOpen);
		}
	});
}
/**
 * 读取全部文档，然后打开全部目录
 */
function docli_expandAll() {
	$('#docli_table').treegrid("loading");
	var unOpen = new Array();
	docli_unopenDir($('#docli_table').treegrid("getData"), unOpen);
	if(unOpen.length > 0) {
		index_mess("打开中...", 0);
		$.ajax({
			async: false,
			url: "document_list_js.action?json=" + unOpen.join("a") + "&_=" + Math.random(),
			dataType: "json",
			success: function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					return false;
				} else {
					index_mess("打开成功！", 2);
					if(data.length != 0) {
						var dirs = {};
						$.each(data, function(k, doc) {
							if(dirs[doc.dirId] == null) {
								dirs[doc.dirId] = [doc];
							} else {
								dirs[doc.dirId].push(doc);
							}
						});
						$.each(unOpen, function(k, dirId) {
							$('#docli_table').treegrid('update', {
								id: dirId,
								row: {
									opened: true
								}
							});
							if(dirs[dirId] != null) {
								$('#docli_table').treegrid('append',{
									parent: dirId,
									data: dirs[dirId]
								});
							}
						});
					} else {
						$.each(unOpen, function(k, dirId) {
							$('#docli_table').treegrid('update', {
								id: dirId,
								row: {
									opened: true
								}
							});
						});
					}
				}
			}
		});
	}
	$('#docli_table').treegrid('expandAll');
	$('#docli_table').treegrid("loaded");
}
/**
 * 查看文档
 */
function docli_read(docId, docName) {
	if(docName == null) {
		var node = $('#docli_table').treegrid('find', -docId);
		if(node==null) {
			alert("文档错误，请刷新后重试！");
			return;
		}
		docName = node.text;
	}
	if(docName.length > 14) {
		docName = docName.substring(0, 12) + "..";
	}
	index_openTab(docName + '@' + docId, 'document_read.action?json=' + docId + "&");
}
/**
 * 编辑文档
 */
function docli_edit(docId, docName) {
	if(index_data.tabObj.tabs("exists", "更新文档")) {
		if(docch_update(true)) {//未更新
			index_updateTab("更新文档", 'document_change.action?json=' + docId + "&", true);
		} else {
			index_data.tabObj.tabs("select", "更新文档");
			alert("有文档未保存，请先保存后再进行更新！");
		}
	} else {
		index_updateTab("更新文档", 'document_change.action?json=' + docId + "&", true);
	}
}
/**
 * 移动文档到新目录
 */
function docli_saveDir() {
	var dir = $('#docli_tree').tree('getSelected');
	if(dir == null) {
		index_mess("请先选择目录！", 4);
		return;
	}
	var docs = $('#docli_table').treegrid('getSelections');
	var documents = new Array();
	$.each(docs, function(k, doc) {
		if(doc.id < 0) {
			documents.push(-doc.id);
		}
	});
	if(documents.length == 0) {
		index_mess("请先选择文档！", 4);
		return;
	}
	index_mess("移动中...", 0);
	$.getJSON("document_changeDir_js.action?json=" + dir.id + "A" + documents.join("a") + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("移动成功！", 2);
			$.each(documents, function(k, docId) {
				$('#docli_table').treegrid("remove", -docId);
			});
			$('#docli_table').treegrid('expandTo', dir.id);
			docli_reopen(dir.id);
			$('#docli_dlg').dialog('close');
		}
	});
}
/**
 * 搜索文档
 */
function docli_search() {
	var params = {};
	params.docStr = $('#docli_name').val();
	if(params.docStr == "") {
		index_mess("请输入要搜索的关键字！", 4);
		$('#docli_name').focus();
		return;
	}
	if(params.docStr.length<2 || params.docStr.length>30) {
		index_mess("关键字的字数要在2~30之间！", 4);
		$('#docli_name').focus();
		return;
	}
	params.json = $('#docli_area').combobox('getValue');
	$('#docli_table').treegrid("loading");
	index_mess("搜索中...", 0);
	$.ajax({
		async: false,
		url: "document_search_js.action?_=" + Math.random(),
		data: params,
		type: "POST",
		dataType: "json",
		success: function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false) {
					index_login();
				}
				return false;
			}
			//重新载入目录，去除文档
			$('#docli_table').treegrid('loadData', index_changeTree(docli_zNodes, {state:"closed",isParent:true,opened:true}));
			if(data.length != 0) {
				var dirs = {};
				$.each(data, function(k, doc) {
					if(dirs[doc.dirId] == null) {
						dirs[doc.dirId] = [doc];
					} else {
						dirs[doc.dirId].push(doc);
					}
				});
				$.each(dirs, function(dirId, docs) {
					$('#docli_table').treegrid('append',{
							parent: dirId,
							data: docs
						});
				});
			}
			$('#docli_table').treegrid('expandAll');
			index_mess("搜索到 " + data.length + " 个文档！", 2);
		}
	});
	$('#docli_table').treegrid("loaded");
}
/**
 * 重新载入
 */
function docli_reload() {
	$('#docli_table').treegrid('loadData', index_changeTree(docli_zNodes, {state:"closed",isParent:true}));
	docli_expandAll();
}
