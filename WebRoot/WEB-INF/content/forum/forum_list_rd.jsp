<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:property value="newsbase.title"/></title>
<%@ include file="../common/header.jsp"%>
<script src="script/jquery.pagination.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/news.css"/>

<s:if test="newsbase.reply==1">
<script src="script/baodian/forum/forum_list.js" type="text/javascript"></script>
<script src="script/kindeditor/kindeditor-min.js" type="text/javascript"></script>
<script src="script/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
</s:if>
<s:else>
<script type="text/javascript">
function reply() {
	alert("已关闭评论！");
}
</script>
</s:else>

<script type="text/javascript">
initDate(new Date());
var nbId = <s:property value="newsbase.id"/>;
$(function() {
	$("#sf-menu-li5").addClass("focus");
	$("#pagination").pagination(
			<s:property value="page.pageNums"/>, {
			current_page: <s:property value="page.page-1"/>,
			ajax_jump: false,
			link_to: "forum_list_rd.action?page.nbId=" + nbId + "&page.page=",
	});
	$("#page_bottom").pagination(
		<s:property value="page.pageNums"/>, {
		current_page: <s:property value="page.page-1"/>,
		ajax_jump: false,
		link_to: "forum_list_rd.action?page.nbId=" + nbId + "&page.page=",
	});
});
var lastPage = "forum_list_rd.action?page.nbId=" + nbId +
		"&page.page=<s:property value="page.pageNums+1"/>";
function addNews() {
	window.open("news_add_rd.action?page.ncId=<s:property value="newsbase.nclass.id"/>");
}
</script>
</head>
<body>
<%@ include file="../common/body_top.jsp"%>
<%-- <%@ include file="../common/body_menu.jsp"%> --%>
<!-- 当前位置以及搜索框 -->
<div class="navi frame">
	<div class="posi">
		<a href="index.jsp">首页</a>&nbsp;&gt;
		<a href="news_list_rd.action">新闻</a>&nbsp;&gt;
		<a href="news_list_rd.action?page.ncId=<s:property value="newsbase.nclass.id"/>">
				<s:property value="newsbase.nclass.name"/></a>
	</div>
	<div class="clear"></div>
</div>
<!-- 分页 -->
<div class="page">
	<div class="p_left">
		<button class="imgbut" onclick="addNews()">发布新闻</button>
		<button class="imgbut" onclick="reply()">评论新闻</button>
	</div>
	<div class="p_right" id="pagination"></div>
</div>
<!-- 新闻及评论 -->
<div class="content frame">
	<div class="news">
		<table>
			<tr>
				<s:if test="page.page==1"><td class="n_left viewnum">
					<s:if test="newsbase.hit>999999">
						<div style="text-align:left;padding-left:30px;"><span class="xg">查看:</span>
						<span class="xi"><s:property value="newsbase.hit"/></span></div>
						<div style="text-align:left;padding-left:30px;"><span class="xg">评论:</span>
						<span class="xi"><s:property value="newsbase.replynum"/></span></div>
					</s:if>
					<s:else>
						<span class="xg">查看:</span>
						<span class="xi"><s:property value="newsbase.hit"/></span>
						<span class="xg">|</span>
						<span class="xg">评论:</span>
						<span class="xi"><s:property value="newsbase.replynum"/></span>
					</s:else>
				</td></s:if>
				<s:else><td class="n_left viewnum">
					<span>新闻作者: <s:property value="newsbase.author.name"/></span>
				</td></s:else>
				<td class="n_right n_title">
					<a href="forum_list_rd.action?page.nbId=<s:property value="newsbase.id"/>">
						<s:property value="newsbase.title"/></a></td>
			</tr>
			<!-- 分割线 -->
			<tr><td class="n_separL"></td><td class="n_separR"></td></tr>
			<s:if test="page.page==1">
				<!-- 新闻正文 -->
				<tr>
					<td class="n_left">
						<div class="n_author">
							<a href="users_checkfriend.action?user.id=<s:property value="newsbase.author.id"/>" target="_blank">
								<s:property value="newsbase.author.name"/></a></div>
						<div class="n_himage">
							<img src="<s:property value="newsbase.author.himage"/>" />
						</div>
						<dl class="n_user">
							<dt>UID</dt><dd><s:property value="newsbase.author.id"/></dd>
							<dt>所在部门</dt>
							<s:set var="tName" value="newsbase.author.dpm.name"/>
							<dd title="<s:property value="#tName"/>">
								<s:if test="#tName.length()>5">
									. . .<s:property value="#tName.substring(#tName.length()-4,#tName.length())"/>
								</s:if>
								<s:else>
									<s:property value="#tName"/>
								</s:else>
							</dd>
							<dt>注册时间</dt><dd><s:date name="newsbase.author.date" format="yyyy-MM-dd"/></dd>
						</dl>
					</td>
					<td class="n_right">
						<div class="n_time">
							发表于
							<script type="text/javascript">
								var pbtime = '<s:date name="newsbase.publishtime" format="yyyy-MM-dd HH:mm:ss"/>';
								document.write('<span title="' + pbtime + '">' + newsDate(pbtime) + '</span>');
							</script>
							<span class="floor"><s:property value="#floor"/>正文</span>
						</div>
						<div class="n_content"><s:property value="newsbase.content" escapeHtml="false"/></div>
					</td>
				</tr>
				<tr>
					<td class="n_left"></td>
					<td class="n_right">
						<div class="n_reply">
							<s:if test="newsbase.reply==1">
								<a href="javascript:;" onclick="reply(0,'<s:property value="newsbase.author.name"/>');">评论</a>
							</s:if>
							<s:else>已关闭评论</s:else>
							<span class="floor">
								<a href="news_change_rd.action?newsbase.id=<s:property value="newsbase.id"/>" target="_blank">编辑</a>
							</span>
						</div></td>
				</tr>
				<!-- 分割线 -->
				<tr>
					<td class="n_separL"></td>
					<td class="n_separR"></td>
				</tr>
			</s:if>
			<!-- 评论正文 -->
			<s:iterator value="newsbase.newsreplies" var="newsreply" status="st">
				<tr>
					<td class="n_left">
						<div class="n_author">
							<a href="users_checkfriend.action?user.id=<s:property value="#newsreply.author.id"/>" target="_blank">
								<s:property value="#newsreply.author.name"/></a></div>
						<div class="n_himage">
							<img src="<s:property value="#newsreply.author.himage"/>" />
						</div>
						<dl class="n_user">
							<dt>UID</dt><dd><s:property value="#newsreply.author.id"/></dd>
							<dt>所在部门</dt>
							<s:set var="tName" value="#newsreply.author.dpm.name"/>
							<dd title="<s:property value="#tName"/>">
								<s:if test="#tName.length()>5">
									. . .<s:property value="#tName.substring(#tName.length()-4,#tName.length())"/>
								</s:if>
								<s:else>
									<s:property value="#tName"/>
								</s:else>
							</dd>
							<dt>注册时间</dt><dd><s:date name="#newsreply.author.date" format="yyyy-MM-dd"/></dd>
						</dl>
					</td>
					<td class="n_right">
						<div class="n_time">
							发表于
							<script type="text/javascript">
								var pbtime = '<s:date name="#newsreply.reptime" format="yyyy-MM-dd HH:mm:ss"/>';
								document.write('<span title="' + pbtime + '">' + newsDate(pbtime) + '</span>');
							</script>
							<s:set var="floor" value="(page.page-1)*page.num+#st.count"></s:set>
							<span class="floor"><s:property value="#floor"/>#</span>
						</div>
						<div class="n_content"><s:property value="#newsreply.content" escapeHtml="false"/></div>
					</td>
				</tr>
				<tr>
					<td class="n_left"></td>
					<td class="n_right">
						<div class="n_reply">
							<s:if test="newsbase.reply==1">
								<a href="javascript:;" 
									onclick="reply(<s:property value="#floor"/>,'<s:property value="#newsreply.author.name"/>');">评论</a>
								|
								<a href="javascript:;" onclick="editNr(<s:property value="#newsreply.id"/>)">编辑</a>
							</s:if>
							<s:else>已关闭评论</s:else>
							<span class="floor">
								<a href="javascript:;" onclick="removeNr(<s:property value="#newsreply.id"/>)">删除</a>
							</span>
						</div></td>
				</tr>
				<!-- 分割线 -->
				<tr><td class="n_separL"></td><td class="n_separR"></td></tr>
			</s:iterator>
		</table>
	</div>
</div>
<!-- 分页 -->
<div class="page">
	<div class="p_left">
		<button class="imgbut" onclick="addNews()">发布新闻</button>
		<button class="imgbut" onclick="reply()">评论新闻</button>
	</div>
	<div class="p_right" id="page_bottom"></div>
</div>
<!-- 评论输入 -->
<s:if test="newsbase.reply==1">
<div class="reply frame">
	<div class="news">
		<table>
			<tr>
				<td class="n_left">
					<div class="n_himage">
						<s:if test="myself">
							<img alt="" src="<s:property value="myself.himage"/>"/>
						</s:if><s:else><img src="images/head.gif" /></s:else>
					</div>
				</td>
				<td class="n_right">
					<div class="r_reply">
						<textarea id="r_content"></textarea>
					</div>
					<div class="r_button">
						<input type="button" class="u_button" value="发表评论" onclick="postReply()" />
						<span id="wordcount"></span>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
</s:if>
<%@ include file="../common/body_tail.jsp"%>
</body>
</html>