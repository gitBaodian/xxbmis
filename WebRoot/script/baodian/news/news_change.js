var editor, count, color, oldnews = {}, news = {};
$(function() {
	editor = KindEditor.create("#n_content", {
		width: "757px",
		height: "400px",
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
			$("#wordcount").html("已输入 <span style=\"color:#" + color + ";\">" + count + 
				"</span> 个字符，还可再输入 <span style=\"color:#" + color +
				";\">" + (21845-count)+"</span> 个。");
		}
	});
	$("#n_title").keyup(function() {
		titleWordCount();
	});
	titleWordCount();
	getNews();
});
function getNews() {
	oldnews["newsbase.nclass.id"] = $("input[name='newsclass']:checked").val();
	oldnews["newsbase.title"] = $("#n_title").val();
	oldnews["newsbase.content"] = editor.html();
	oldnews["newsbase.hit"] = $("#nch_hit").val();
	oldnews["newsbase.publishtime"] = $("#nch_ptime").val();
	oldnews["newsbase.sort"] = $("input[name='nch_sort']:checked").val();
	oldnews["newsbase.reply"] = $("input[name='nch_reply']:checked").val();
	oldnews["newsbase.display"] = $("input[name='nch_display']:checked").val();
}
function titleWordCount() {
	count = $("#n_title").val().length;
	color = "003399";
	if(count > 99)
		color = "E53333";
	$("#n_t_wc").html("&nbsp;&nbsp;还可再输入 <span style=\"color:#" + color + ";\">" + (99-count)+"</span> 个字符。");
}
function postNews() {
	index_mess("更改中...", 0);
	var str = $("input[name='newsclass']:checked").val();
	var ischange = false;
	news = {"newsbase.id": nch_id};
	if(str == null) {
		index_mess("请先选择新闻类型！", 1);
		$("#newsclasses").prepend("<span style=\"color:#E53333;\">请先选择新闻类型！</span>");
		return;
	} else {
		$("#newsclasses").html("");
	}
	if(oldnews["newsbase.nclass.id"] != str) {
		news["newsbase.nclass.id"] = str;
		ischange = true;
	}
	str = $("#n_title").val();
	count = str.length;
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
	if(oldnews["newsbase.title"] != str) {
		news["newsbase.title"] = str;
		ischange = true;
	}
	count = editor.count();
	if(count == 0) {
		index_mess("请输入内容！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">请输入内容！</span>");
		return;
	}
	if(count > 21845) {
		index_mess("内容超出限制！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">内容超出限制！</span>");
		return;
	}
	str = editor.html();
	if(oldnews["newsbase.content"] != str) {
		news["newsbase.content"] = str;
		ischange = true;
	}
	str = $("#nch_hit").val();
	if(! str.match(/^(0|[1-9]\d*)$/)) {
		index_mess("查看次数必须为数字！", 1);
		$("#n_h_mess").html("请输入数字！");
		return;
	}
	if(str.length > 9) {
		index_mess("查看次数太大！", 1);
		$("#n_h_mess").html("请输入9位数以下的数字！");
		return;
	}
	$("#n_h_mess").html("");
	if(oldnews["newsbase.hit"] != str) {
		news["newsbase.hit"] = str;
		ischange = true;
	}
	str = $("#nch_ptime").val();
	if(str == "") {
		index_mess("发表时间不能为空！", 1);
		$("#n_pt_mess").html("时间不能为空！");
		return;
	}
	$("#n_pt_mess").html("");
	if(oldnews["newsbase.publishtime"] != str) {
		news["newsbase.publishtime"] = str;
		ischange = true;
	}
	str = $("input[name='nch_sort']:checked").val();
	news["newsbase.sort"] = str;
	if(oldnews["newsbase.sort"] != str) {
		ischange = true;
	}
	str = $("input[name='nch_reply']:checked").val();
	news["newsbase.reply"] = str;
	if(oldnews["newsbase.reply"] != str) {
		ischange = true;
	}
	str = $("input[name='nch_display']:checked").val();
	news["newsbase.display"] = str;
	if(oldnews["newsbase.display"] != str) {
		ischange = true;
	}
	if(ischange) {
		$.post("newsbase_change_no.action", news, function(data) {
			if(data.status == 0) {
				//data.name为1表示是作者自己更改的
				if(data.name != 1) {
					index_mess("更改成功！", 2);
					$("#nch_status").html("已审核<br />审核人员：" + data.name + "<br />审核时间：" + data.time);
				} else {
					index_mess("作者身份，更改成功！", 2);
				}
				getNews();
			} else {
				index_mess(data.mess, 1);
				alert(data.mess);
				if(data.login == false) {
					//未登录处理
				}
			}
		}, "json");
	} else {
		index_mess("未更新内容！", 2);
	}
}
function searchNews() {
	if($("#nbtitle").val() == "请输入搜索内容")
		$("#nbtitle").val("");
}
function removeNews() {
	if(confirm("删除新闻后，对应的评论也将会删除，确定要删除吗?")) {
		index_mess("删除中...", 0);
		$.getJSON("newsbase_remove_js.action?nbIds=" + nch_id, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				alert(data.mess);
				if(data.login == false) {
					//未登录处理
				}
			} else {
				index_mess("删除成功！", 2);
				alert("删除成功！");
				window.location.href = "news_list_rd.action";
			}
		});
	}
}
function checkNews() {
	if(confirm("确定允许此新闻发布?")) {
		index_mess("更改中...", 0);
		$.getJSON("newsbase_review_js.action?nbIds=" + nch_id, function(data) {
			if(data.status == 0) {
				index_mess("审核成功！", 2);
				$("#nch_status").html("已审核<br />审核人员：" + data.name + "<br />审核时间：" + data.time);
			} else {
				index_mess(data.mess, 1);
				alert(data.mess);
				if(data.login == false) {
					//未登录处理
				}
			}
		});
	}
}