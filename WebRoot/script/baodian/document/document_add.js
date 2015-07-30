var docadd_resize = function() {
	//页面被关掉后，去除自动调整大小
	if($("#docadd_tt").html() == null) {
		$(window).unbind("resize", docadd_resize);
		return;
	}
	var h = $(window).height();
	if(h < 600) h = 600;
	docadd_editor.resize(null, h -290);
};
var docadd_editor = KindEditor.create("#docadd_tt", {
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
		$("#docadd_mess").html("&nbsp;已输入 <span style=\"color:#" + color + ";\">" + count + 
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
			$(window).unbind("resize", docadd_resize);
		} else {//正常
			$(window).trigger("resize");//使easyUI回归正常
			$(window).resize(docadd_resize);
		}
	}
});
if(docadd_dirId) {
	$('#docadd_dir').combotree('setValue', docadd_dirId);
}
/**
 * 添加文档
 */
function docadd_add() {
	var params = {};
	var length;
	params["doc.name"] = $('#docadd_name').val();
	length = params["doc.name"].length;
	if(length == 0) {
		index_mess("请先输入名称！", 4);
		$('#docadd_name').focus();
		return;
	}
	if(length > 100) {
		index_mess("名称字数为 " + length + " ，超过100个！", 4);
		$('#docadd_name').focus();
		return;
	}
	var dir = $('#docadd_dir').combotree('tree').tree('getSelected');
	if(dir == null) {
		index_mess("请先选择目录！", 4);
		return;
	}
	params["doc.dir.id"] = dir.id;
	params["doc.url"] = $('#docadd_url').val();
	length = params["doc.url"].length;
	if(length > 100) {
		index_mess("外部链接字数为 " + length + " ，超过100个！", 4);
		$('#docadd_url').focus();
		return;
	}
	length = docadd_editor.count();
	if(length > 100000000) {
		index_mess("内容字数超过100000000 个！", 4);
		docadd_editor.focus();
		return;
	}
	params["doc.content"] = docadd_editor.html();
	index_mess("添加中...", 0);
	$.post("document_add_js.action", params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			return false;
		}
		index_mess("添加成功！正在跳转...", 2);
		setTimeout(function() {
			if(params["doc.name"].length > 14) {
				params["doc.name"] = params["doc.name"].substring(0, 12) + "..";
			}
			//index_updateTab(title, href, load, newtitle)
			index_updateTab("添加公共文档", 'document_read.action?json=' + data.id + "&", false, 
					params["doc.name"] + '@' + data.id);
		}, 800);
	}, "json");
}
