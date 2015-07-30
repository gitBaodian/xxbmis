<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻列表</title>
<%@ include file="../common/header.jsp"%>
<script src="script/jquery.pagination.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/news.css"/>
<script type="text/javascript">
initDate(new Date());
var ncId = <s:property value="page.ncId"/>;
var jumpUrl = "news_list_rd.action?page.ncId=" + ncId +
	"&page.title=<s:property value="page.title" escapeHtml="false"/>" +
	"&page.num=<s:property value="page.num"/>&page.page=";
$(function() {
	$("#pagination").pagination(
			<s:property value="page.pageNums"/>, {
			current_page: <s:property value="page.page-1"/>,
			ajax_jump: false,
			link_to: jumpUrl
	});
	$("#page_bottom").pagination(
		<s:property value="page.pageNums"/>, {
		current_page: <s:property value="page.page-1"/>,
		ajax_jump: false,
		link_to: jumpUrl
	});
});
function searchNews() {
	if($("#nbtitle").val() == "请输入搜索内容")
		$("#nbtitle").val("");
}
function addNews() {
	if(ncId != 0) {
		window.open("news_add_rd.action?page.ncId=" + ncId);
	} else {
		window.open("news_add_rd.action");
	}
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
		<a href="news_list_rd.action">新闻</a>
	</div>
	<div class="search">
		<form id="searchform" action="news_list_rd.action" method="post" onsubmit="searchNews()">
			<select name="page.ncId">
				<option value="0" selected="<s:if test="page.ncId==0">selected</s:if>">全部</option>
				<s:iterator value="newsclasses" var="newsclass">
					<option value="<s:property value="#newsclass.id"/>"
						<s:if test="page.ncId==#newsclass.id">selected</s:if>>
							<s:property value="#newsclass.name"/></option>
				</s:iterator>
			</select>
			<input id="nbtitle" name="page.title" type="text"
					 onblur="if(this.value==''){this.value='请输入搜索内容';this.className='seinput noinput';}"
					 onfocus="if(this.value=='请输入搜索内容'){this.value='';this.className='seinput';}"
					 <s:if test="page.title!=null&&!page.title.isEmpty()">
					 	class="seinput" value="<s:property value="page.title"/>"
					 </s:if>
					 <s:else>class="seinput noinput" value="请输入搜索内容"</s:else> />
			<input class="sebutton" type="submit" value="搜索">
		</form>
	</div>
	<div class="clear"></div>
</div>
<!-- 分页 -->
<div class="page">
	<div class="p_left">
		<button class="imgbut" onclick="addNews()">发布新闻</button>
	</div>
	<div class="p_right" id="pagination"></div>
</div>
<!-- 新闻类型 -->
<div class="newsclass fr_top">
	<ul>
		<li <s:if test="page.ncId==0">class="focus"</s:if>>
			<a href="news_list_rd.action">全部</a>
		</li>
		<s:iterator value="newsclasses" var="newsclass">
			<li <s:if test="page.ncId==#newsclass.id">class="focus"</s:if>>
				<a href="news_list_rd.action?page.ncId=<s:property value="#newsclass.id"/>">
					<s:property value="#newsclass.name"/></a></li>
		</s:iterator>
	</ul>
</div>
<div class="clear"></div>
<!-- 新闻 -->
<div class="content frame">
	<div class="newshead">
		<table class="n_table">
			<tbody>
				<tr>
					<td class="image"></td>
					<td class="type">类型</td>
					<td class="title">标题</td>
					<td class="author">作者</td>
					<td class="read">评论/查看</td>
					<td class="time">最后发布</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="n_body">
		<table class="n_table"><tbody>
			<s:iterator value="newsbaseList[0]" var="newsbase">
				<tr class="n_tr">
					<td class="image">
						<img src="images/news/nb_order.gif">
					</td>
					<td class="type">
						<a href="news_list_rd.action?page.ncId=<s:property value="#newsbase.nclass.id"/>"
							class="nc_t_color<s:property value="#newsbase.nclass.position"/>"
							title="<s:property value="#newsbase.nclass.newsTree.name"/>-<s:property value="#newsbase.nclass.name"/>">
							[<s:property value="#newsbase.nclass.name"/>]
						</a>
					</td>
					<td class="title">
						<s:set var="nbTitle" value="#newsbase.title"/>
						<a href="forum_list_rd.action?page.nbId=<s:property value="#newsbase.id"/>"
							title="<s:property value="#nbTitle"/>" target="_blank">
							<s:if test="#nbTitle.length()>34">
								<s:property value="#nbTitle.substring(0,33)"/>...
							</s:if>
							<s:else>
								<s:property value="#nbTitle"/>
							</s:else>
						</a>
						<s:if test="#newsbase.reply==0">
							<img src="images/news/news_noreply.gif" title="锁定评论新闻" class="n_title_img">
						</s:if>
					</td>
					<td class="author">
						<cite><a href="users_checkfriend.action?user.id=<s:property value="#newsbase.author.id"/>" target="_blank">
							<s:property value="#newsbase.author.name"/>
						</a></cite>
						<em>
							<script type="text/javascript">
								document.write(newsDate('<s:date name="#newsbase.publishtime" format="yyyy-MM-dd HH:mm:ss"/>', 2));
							</script>
						</em>
					<td class="read">
						<cite><s:property value="#newsbase.replynum"/></cite>
						<em><s:property value="#newsbase.hit"/></em>
		  			</td>
					<td class="time">
						<cite><a href="users_checkfriend.action?user.id=<s:property value="#newsbase.replyer.id"/>" target="_blank">
							<s:property value="#newsbase.replyer.name"/>
						</a></cite>
						<em>
							<script type="text/javascript">
								document.write(newsDate('<s:date name="#newsbase.replytime" format="yyyy-MM-dd HH:mm:ss"/>', 1));
							</script>
						</em>
				</tr>
			</s:iterator>
			<s:if test="newsbaseList[0].size!=0">
				<tr class="n_separ"><td colspan="6">&uArr;置顶&nbsp;&nbsp;&dArr;全部</td></tr>
			</s:if>
			<s:iterator value="newsbaseList[1]" var="newsbase" status="st">
				<tr class="n_tr">
					<td class="image">
						<img src="images/news/newsclass.gif">
					</td>
					<td class="type">
						<a href="news_list_rd.action?page.ncId=<s:property value="#newsbase.nclass.id"/>"
							class="nc_t_color<s:property value="#newsbase.nclass.position"/>"
							title="<s:property value="#newsbase.nclass.newsTree.name"/>-<s:property value="#newsbase.nclass.name"/>">
							[<s:property value="#newsbase.nclass.name"/>]
						</a>
					</td>
					<td class="title">
						<s:set var="nbTitle" value="#newsbase.title"/>
						<a href="forum_list_rd.action?page.nbId=<s:property value="#newsbase.id"/>"
							title="<s:property value="#nbTitle"/>" target="_blank">
							<s:if test="#nbTitle.length()>34">
								<s:property value="#nbTitle.substring(0,33)"/>...
							</s:if>
							<s:else>
								<s:property value="#nbTitle"/>
							</s:else>
						</a>
						<s:if test="#newsbase.reply==0">
							<img src="images/news/news_noreply.gif" title="锁定评论新闻" class="n_title_img">
						</s:if>
					</td>
					<td class="author">
						<cite><a href="users_checkfriend.action?user.id=<s:property value="#newsbase.author.id"/>" target="_blank">
							<s:property value="#newsbase.author.name"/>
						</a></cite>
						<em>
							<script type="text/javascript">
								document.write(newsDate('<s:date name="#newsbase.publishtime" format="yyyy-MM-dd HH:mm:ss"/>', 2));
							</script>
						</em>
					</td>
					<td class="read">
						<cite><s:property value="#newsbase.replynum"/></cite>
						<em><s:property value="#newsbase.hit"/></em>
					</td>
					<td class="time">
						<cite><a href="users_checkfriend.action?user.id=<s:property value="#newsbase.replyer.id"/>" target="_blank">
							<s:property value="#newsbase.replyer.name"/>
						</a></cite>
						<em>
							<script type="text/javascript">
								document.write(newsDate('<s:date name="#newsbase.replytime" format="yyyy-MM-dd HH:mm:ss"/>', 1));
							</script>
						</em>
					</td>
				</tr>
			</s:iterator>
		</tbody></table>
	</div>
</div>
<!-- 分页 -->
<div class="page">
	<div class="p_left">
		<button class="imgbut" onclick="addNews()">发布新闻</button>
	</div>
	<div class="p_right" id="page_bottom"></div>
</div>
<%@ include file="../common/body_tail.jsp"%>
</body>
</html>