var editor, count, color;
$(function() {
	editor = KindEditor.create("#n_content", {
		width: "757px",
		height: "300px",
		resizeType: 1,
		uploadJson: "file_upload_no.action",
	    fileManagerJson: "file_message_no.action",
		allowFileManager: true,
		urlType: "relative",
		afterChange: function() {
			count = this.count();
			color = "003399";
			if(count > 21845)
				color = "E53333";
			$("#wordcount").html("&nbsp;已输入 <span style=\"color:#" + color + ";\">" + count + 
				"</span> 个字符，还可再输入 <span style=\"color:#" + color +
				";\">" + (21845-count)+"</span> 个。");
		}
	});
	$("#n_title").keyup(function() {
		titleWordCount();
	});
	titleWordCount();
});
function titleWordCount() {
	count = $("#n_title").val().length;
	color = "003399";
	if(count > 99)
		color = "E53333";
	$("#n_t_wc").html("&nbsp;&nbsp;还可再输入 <span style=\"color:#" + color + ";\">" + (99-count)+"</span> 个字符。");
}
function postNews() {
	index_mess("添加中...", 0);
	var newsclass = $("input[name='newsclass']:checked").val();
	if(newsclass == null) {
		index_mess("请先选择新闻类型！", 1);
		$("#newsclasses").prepend("<span style=\"color:#E53333;\">请先选择新闻类型！</span>");
		return;
	} else {
		$("#newsclasses").html("");
	}
	count = $("#n_title").val().length;
	if(count == 0) {
		index_mess("请输入标题！", 1);
		$("#n_t_wc").prepend("<span style=\"color:#E53333;\">&nbsp;&nbsp;请输入标题！</span>");
		return;
	}
	if(count > 99) {
		index_mess("标题超出限制！", 1);
		$("#n_t_wc").prepend("<span style=\"color:#E53333;\">&nbsp;&nbsp;标题超出限制！</span>");
		return;
	}
	count = editor.count();
	if(count == 0) {
		index_mess("请输入内容！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">&nbsp;请输入内容！</span>");
		return;
	}
	if(count > 21845) {
		index_mess("内容超出限制！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">&nbsp;内容超出限制！</span>");
		return;
	}
	$.post("newsbase_add_js.action", {
		"newsbase.nclass.id": newsclass,
		"newsbase.title": $("#n_title").val(),
		"newsbase.content": editor.html() }, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				alert(data.mess);
			} else {
				if(data.nstatus == 0) {
					index_mess("添加成功", 2);
					window.location.href = "forum_list_rd.action?page.nbId=" + data.id;
				} else {
					index_mess("添加成功，审核后才能查看...", 2);
					alert("新闻添加成功，目前正在审核，现在跳转到此新闻类型页面...");
					window.location.href = "news_list_rd.action?page.ncId=" + newsclass;
				}
			}
	}, "json");
}
function searchNews() {
	if($("#nbtitle").val() == "请输入搜索内容")
		$("#nbtitle").val("");
}