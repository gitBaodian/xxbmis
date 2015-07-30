<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>添加新闻</title>
<%@ include file="../common/header.jsp"%>
<script type="text/javascript" src="script/baodian/news/news_add.js"></script>
<script type="text/javascript" src="script/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="script/kindeditor/lang/zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="css/news.css"/>
<script type="text/javascript">
</script>
</head>
<body>
<%@ include file="../common/body_top.jsp"%>
<!-- 当前位置以及搜索框 -->
<div class="navi frame">
	<div class="posi">
		<a href="index.jsp">首页</a>&nbsp;&gt;
		<a href="news_list_rd.action">新闻</a>&nbsp;&gt;
		<a href="news_add_rd.action">添加新闻</a>
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
					 class="seinput noinput" value="请输入搜索内容"/>
			<input class="sebutton" type="submit" value="搜索">
		</form>
	</div>
	<div class="clear"></div>
</div>
<!-- 评论输入 -->
<div class="reply frame">
	<div class="news">
		<table><tbody>
			<tr>
				<td class="n_left viewnum">
					<span>请先选择新闻类型</span>
				</td>
				<td class="n_right">
					<div class="n_tree">
						<s:iterator value="newsclasses" var="newsclass">
							<input type="radio" name="newsclass"
								id="nc<s:property value="#newsclass.id"/>"
								value="<s:property value="#newsclass.id"/>"
								<s:if test="page.ncId==#newsclass.id">checked="checked"</s:if>/>
							<label for="nc<s:property value="#newsclass.id"/>"><s:property value="#newsclass.name"/></label><br>
						</s:iterator>
					</div>
					<div class="n_newsclass" id="newsclasses"></div>
				</td>
			</tr>
			<tr><td class="n_separL"></td><td class="n_separR"></td></tr>
			<tr>
				<td class="n_left">
					<div class="n_himage">
						<s:if test="user">
							<img alt="" src="<s:property value="user.str[1]"/>"/>
						</s:if><s:else><img src="images/head.gif" /></s:else>
					</div>
				</td>
				<td class="n_right">
					<div class="n_title">
						<input id="n_title"/><span id="n_t_wc"></span>
					</div>
					<div class="r_reply">
						<textarea id="n_content"></textarea>
					</div>
					<div class="r_button">
						<input class="u_button" type="button" onclick="postNews()" value="发布新闻">
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