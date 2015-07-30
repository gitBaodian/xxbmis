var editor, count, color;
$(function() {
	editor = KindEditor.create("#r_content", {
		width: "756px",
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
});
function reply(floor, name) {
	var body = (window.opera)?(document.compatMode == "CSS1Compat"? $('html'): $('body')): $('html,body');
	body.stop();
	body.animate({scrollTop: $('#page_bottom').offset().top},"slow");
	if(floor == null) {
		editor.focus();
	} else {
		var str = floor==0 ? "作者 ": (floor+"# ");
		editor.appendHtml("评论 " + str + "<span style=\"color:#003399;\">" +
			name + "</span>：");
	}
}
function postReply() {
	index_mess("评论中...", 0);
	count = editor.count();
	if(count == 0) {
		index_mess("请输入内容！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">&nbsp;请输入内容！</span>");
	} else if(count > 21845) {
		index_mess("内容超出限制！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">&nbsp;内容超出限制！</span>");
	} else
		$.post("newsreply_add_js.action", {
			"newsreply.nbase.id": nbId,
			"newsreply.content": editor.html() }, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					alert(data.mess);
				} else {
					index_mess("评论成功！自动跳转中...",2);
					window.location.href = lastPage;
				}
		}, "json");
}
function removeNr(nrId) {
	if(confirm("确定删除此条评论？")) {
		index_mess("删除中...", 0);
		$.getJSON("newsreply_remove_no.action", {"newsreply.id": nrId}, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				alert(data.mess);
				if(data.login == false) {
					//未登录处理
				}
			} else {
				index_mess("删除成功！", 2);
				alert("删除成功！点击确定自动刷新此页面");
				window.location.reload();
			}
		});
	}
}
function editNr(nrId) {
	window.open("forum_change_no_rd.action?newsreply.id=" + nrId);
}