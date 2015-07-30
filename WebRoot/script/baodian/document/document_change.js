var docch_resize = function() {
		//页面被关掉后，去除自动调整大小
		if($("#docch_tt").html() == null) {
			$(window).unbind("resize", docch_resize);
			return;
		}
		var h = $(window).height();
		if(h < 600) h = 600;
		docch_editor.resize(null, h -290);
};
var docch_editor = KindEditor.create("#docch_tt", {
	width: "99%",
	resizeType: 0,
	uploadJson: "file_upload_no.action",
    fileManagerJson: "file_message_no.action",
	allowFileManager: true,
	urlType: "relative",
	afterChange: function() {
		var count = this.count();
		var color = "003399";
		if(count > 100000000)
			color = "E53333";
		$("#docch_mess").html("&nbsp;已输入 <span style=\"color:#" + color + ";\">" + count + 
			"</span> 个字符，还可再输入 <span style=\"color:#" + color +
			";\">" + (100000000-count)+"</span> 个。");
	}, beforeCreate : function() {
		if(!this.fullscreenMode) {//正常
			var h = $(window).height();
			if(h < 600) h = 600;
			this.height = h - 290;
		}
	}, afterCreate: function() {
		if(this.fullscreenMode) {//全屏
			$(window).unbind("resize", docch_resize);
		} else {//正常
			$(window).trigger("resize");//使easyUI回归正常
			$(window).resize(docch_resize);
		}
	}
});
$('#docch_name').val(docch_para.doc.name);
$('#docch_dir').combotree('setValue', docch_para.doc.dirId);
$('#docch_url').val(docch_para.doc.url);
docch_editor.html(docch_para.doc.content);
/**
 * 更新文档
 */
function docch_update(onlyCK) {
	var params = {};
	var change = true;
	var length;
	var para = $('#docch_name').val();
	length = para.length;
	if(length == 0) {
		index_mess("请先输入名称！", 4);
		$('#docch_name').focus();
		return;
	}
	if(length > 100) {
		index_mess("名称字数为 " + length + " ，超过100个！", 4);
		$('#docch_name').focus();
		return;
	}
	if(para != docch_para.doc.name) {
		params["doc.name"] = para;
		change = false;
	}
	var dir = $('#docch_dir').combotree('tree').tree('getSelected');
	if(dir == null) {
		index_mess("请先选择目录！", 4);
		return;
	}
	if(dir.id != docch_para.doc.dirId) {
		params["doc.dir.id"] = dir.id;
		change = false;
	}
	para = $('#docch_url').val();
	length = para.length;
	if(length > 100) {
		index_mess("外部链接字数为 " + length + " ，超过100个！", 4);
		$('#docch_url').focus();
		return;
	}
	if(para != docch_para.doc.url) {
		params["doc.url"] = para;
		change = false;
	}
	para = docch_editor.html();
	length = para.length;
	if(length > 100000000) {
		index_mess("内容字数超过100000000 个！", 4);
		docch_editor.focus();
		return;
	}
	if(para != docch_para.doc.content) {
		params["doc.content"] = para;
		change = false;
	}
	if(onlyCK) {
		return change;
	}
	if(change) {
		index_mess("未更新！", 4);
		return;
	}
	params["doc.id"] = docch_para.doc.id;
	index_mess("更新中...", 0);
	$.post("document_change_js.action", params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			return false;
		}
		index_mess("更新成功！打开文档中...", 2);
		params.oldName = docch_tabName(docch_para.doc.id, docch_para.doc.name);
		if(params["doc.name"] != null) {
			//同步查看文档页面和文档列表页面
			//index_data.tabObj.tabs("close", "更新文档");
			index_updateTab(params.oldName, 'document_read.action?json=' + docch_para.doc.id + "&",
					false, docch_tabName(docch_para.doc.id, params["doc.name"]));
			if(params["doc.dir.id"] != null) {
				if($("#docli_table").html() != null) {
					$('#docli_table').treegrid("remove", -docch_para.doc.id);
					docli_reopen(params["doc.dir.id"]);
				}
				docch_para.doc.dirId = params["doc.dir.id"];
			} else {
				if($("#docli_table").html() != null) {
					docli_reopen(docch_para.doc.dirId);
				}
			}
			docch_para.doc.name = params["doc.name"];
		} else {
			index_updateTab(params.oldName, 'document_read.action?json=' + docch_para.doc.id + "&");
			if(params["doc.dir.id"] != null) {
				if($("#docli_table").html() != null) {
					$('#docli_table').treegrid("remove", -docch_para.doc.id);
					docli_reopen(params["doc.dir.id"]);
				}
				docch_para.doc.dirId = params["doc.dir.id"];
			}
		}
		if(params["doc.url"] != null) {
			docch_para.doc.url = params["doc.url"];
		}
		if(params["doc.content"] != null) {
			docch_para.doc.content = params["doc.content"];
		}
		index_mess("更新成功！打开文档中...", 2);
	}, "json");
}
/**
 * 处理文档id和名称，返回tab名称
 */
function docch_tabName(docId, docName) {
	if(docName.length > 14) {
		docName = docName.substring(0, 12) + "..";
	}
	return docName + '@' + docId;
}
/**
 * 打开文档
 */
function docch_open() {
	index_updateTab(docch_tabName(docch_para.doc.id, docch_para.doc.name),
			'document_read.action?json=' + docch_para.doc.id + "&");
}
/**
 * 删除文档
 */
function docch_remove() {
	if(!confirm("确认要删除文档 - 《" + docch_para.doc.name + "》？")) {
		return;
	}
	index_mess("删除中...", 0);
	$.getJSON("document_remove_js.action?json=" + docch_para.doc.id + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			return false;
		}
		index_mess("删除成功！", 2);
		setTimeout(function() {
			index_data.tabObj.tabs("close", "更新文档");
			index_data.tabObj.tabs("close", docch_tabName(docch_para.doc.id, docch_para.doc.name));
			if($("#docli_table").html() != null) {
				$('#docli_table').treegrid("remove", -docch_para.doc.id);
			}
		}, 400);
	});
}