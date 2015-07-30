<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>更新评论-<s:property value="newsreply.nbase.title"/></title>
<%@ include file="../common/header.jsp"%>
<script src="script/baodian/forum/forum_change.js" type="text/javascript"></script>
<script src="script/kindeditor/kindeditor-min.js" type="text/javascript"></script>
<script src="script/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/news.css"/>
<script type="text/javascript">
var nrId = <s:property value="newsreply.id"/>;
var nbId = <s:property value="newsreply.nbase.id"/>;
</script>
</head>
<body>
<%@ include file="../common/body_top.jsp"%>
<!-- 当前位置以及搜索框 -->
<div class="navi frame">
	<div class="posi">
		<a href="index.jsp">首页</a>&nbsp;&gt;
		<a href="news_list_rd.action">新闻</a>&nbsp;&gt;
		<a href="news_list_rd.action?page.ncId=<s:property value="newsreply.nbase.nclass.id"/>">
				<s:property value="newsreply.nbase.nclass.name"/></a>&nbsp;&gt;
		<span>更新评论</span>
	</div>
	<div class="clear"></div>
</div>
<!-- 新闻及评论 -->
<div class="content frame">
	<div class="news">
		<table><tbody>
			<tr>
				<td class="n_left viewnum">
					<span>新闻作者: <s:property value="newsreply.nbase.author.name"/></span>
				</td>
				<td class="n_right n_title">
					<a target="_blank" href="forum_list_rd.action?page.nbId=<s:property value="newsreply.nbase.id"/>">
						<s:property value="newsreply.nbase.title"/></a></td>
			</tr>
			<!-- 分割线 -->
			<tr><td class="n_separL"></td><td class="n_separR"></td></tr>
			<tr>
				<td class="n_left">
					<div class="n_author">
						<a href="users_checkfriend.action?user.id=<s:property value="newsreply.author.id"/>" target="_blank">
							<s:property value="newsreply.author.name"/></a></div>
					<div class="n_himage">
						<img src="<s:property value="newsreply.author.himage"/>" />
					</div>
					<dl class="n_user">
						<dt>UID</dt><dd><s:property value="newsreply.author.id"/></dd>
						<dt>所在部门</dt>
						<s:set var="tName" value="newsreply.author.dpm.name"/>
						<dd title="<s:property value="#tName"/>">
							<s:if test="#tName.length()>5">
								. . .<s:property value="#tName.substring(#tName.length()-4,#tName.length())"/>
							</s:if>
							<s:else>
								<s:property value="#tName"/>
							</s:else>
						</dd>
						<dt>注册时间</dt><dd><s:date name="newsreply.author.date" format="yyyy-MM-dd"/></dd>
					</dl>
				</td>
				<td class="n_right">
					<div class="n_time">
						<span><s:date name="newsreply.reptime" format="yyyy-MM-dd HH:mm:ss"/></span>
						<span class="floor">
							<a href="javascript:;" class="action" onclick="removeNr()">删除</a></span>
					</div>
					<div class="n_content r_reply">
						<textarea id="r_content"><s:property value="newsreply.content"/></textarea>
					</div>
					<div class="r_button">
						<input type="button" class="u_button" value="更新评论" onclick="postReply()" />
						<span id="wordcount"></span>
					</div>
				</td>
			</tr>
		</tbody></table>
	</div>
</div>
<%@ include file="../common/body_tail.jsp"%>
</body>
</html>