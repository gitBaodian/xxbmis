<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var docrd_ = <s:property value="docStr" escapeHtml="false"/>;
if(docrd_.status == 1) {
	$("#docrd_name" + docrd_.id).html(docrd_.mess);
} else {
	$("#docrd_name" + docrd_.id).html('<div class="docrd_title" title="' + docrd_.name + '">' +
		docrd_.name + '</div><div class="docrd_user">' +
		docrd_.uName +' 于 ' + docrd_.date + ' 更新&ensp;' +
		(docrd_.url==''?'': '这是一个<a href="' + docrd_.url + '" target="_blank">外部链接</a>') + 
		'&ensp;所属目录：' + docrd_.dirName + 
		'&ensp;<a href="javascript:;" onclick="docli_edit(<s:property value="json" default="null" escapeHtml="false"/>)">编辑</a></div>');
	if(docrd_.url == "") {
		if($.browser.chrome) {
			$("#docrd_if" + docrd_.id).contents().find("body").append(docrd_.content);
		} else {
			$("#docrd_if" + docrd_.id).bind("load", function() {
				$("#docrd_if" + docrd_.id).contents().find("body").append(docrd_.content);
			});
		}
	} else {
		$("#docrd_if" + docrd_.id).attr("src", docrd_.url);
		$("#docrd_name" + docrd_.id).append();
	}
}
/**
 * 编辑文档
 */
function docli_edit(docId) {
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
</script>
<div class="docrd_body">
	<div id="docrd_name<s:property value="json" default="null" escapeHtml="false"/>" class="docrd_head"></div>
	<div class="docrd_iframe"><iframe id="docrd_if<s:property value="json" escapeHtml="false"/>" class="docrd_if"></iframe></div>
</div>
