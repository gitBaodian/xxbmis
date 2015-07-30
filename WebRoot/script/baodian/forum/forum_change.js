var editor, count, color, oldForum = {}, forum = {};
$(function() {
	editor = KindEditor.create("#r_content", {
		width: "755px",
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
			$("#wordcount").html("&nbsp;已输入 <span style=\"color:#" + color + ";\">" + count + 
				"</span> 个字符，还可再输入 <span style=\"color:#" + color +
				";\">" + (21845-count)+"</span> 个。");
		}
	});
	getForum();
});
function getForum() {
	oldForum["newsreply.content"] = editor.html();
}
function postReply() {
	index_mess("更改评论中...", 0);
	count = editor.count();
	if(count == 0) {
		index_mess("请输入内容！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">&nbsp;请输入内容！</span>");
		return;
	} else if(count > 21845) {
		index_mess("内容超出限制！", 1);
		$("#wordcount").prepend("<span style=\"color:#E53333;\">&nbsp;内容超出限制！</span>");
		return;
	}
	var str = editor.html();
	if(oldForum["newsreply.content"] != str) {
		forum = {"newsreply.id": nrId, "newsreply.content": str};
		$.post("newsreply_change_no.action", forum, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				alert(data.mess);
			} else {
				index_mess("更改评论成功！", 2);
				getForum();
			}
		}, "json");
	} else {
		index_mess("未更改内容！", 2);
	}
}
function removeNr() {
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
				alert("删除成功！跳转到此新闻页面...");
				window.location.href = "forum_list_rd.action?page.nbId=" + nbId;
			}
		});
	}
}