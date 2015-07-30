<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>更新-<s:property value="newsbase.title"/></title>
<%@ include file="../common/header.jsp"%>
<script type="text/javascript" src="script/baodian/news/news_change.js"></script>
<script type="text/javascript" src="script/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="script/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="script/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/news.css"/>
<script type="text/javascript">
var nch_id = <s:property value="newsbase.id"/>;
</script>
</head>
<body>
<%@ include file="../common/body_top.jsp"%>
<!-- 当前位置以及搜索框 -->
<div class="navi frame">
	<div class="posi">
		<a href="index.jsp">首页</a>&nbsp;&gt;
		<a href="news_list_rd.action">新闻</a>&nbsp;&gt;
		<a href="news_list_rd.action?page.ncId=<s:property value="newsbase.nclass.id"/>">
			<s:property value="newsbase.nclass.name"/></a>&nbsp;&gt;
		<span>更新新闻</span>
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
					<span>新闻类型</span>
				</td>
				<td class="n_right">
					<div class="n_tree">
						<s:iterator value="newsclasses" var="newsclass">
							<input type="radio" name="newsclass"
								id="nc<s:property value="#newsclass.id"/>"
								value="<s:property value="#newsclass.id"/>"
								<s:if test="newsbase.nclass.id==#newsclass.id">checked="checked"</s:if>/>
							<label for="nc<s:property value="#newsclass.id"/>"><s:property value="#newsclass.name"/></label><br>
						</s:iterator>
					</div>
					<div class="n_newsclass" id="newsclasses"></div>
				</td>
			</tr>
			<!-- 分割线 -->
			<tr><td class="n_separL"></td><td class="n_separR"></td></tr>
			<tr>
				<td class="n_left">
					<div class="n_author">
						<a href="user_checkInput.action?user.id=<s:property value="newsbase.author.id"/>" target="_blank">
								<s:property value="newsbase.author.name"/></a>
					</div>
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
					<div class="n_title">
						<input id="n_title" value="<s:property value="newsbase.title"/>"/>&nbsp;&nbsp;
						<a href="forum_list_rd.action?page.nbId=<s:property value="newsbase.id"/>" target="_blank">新窗口浏览</a>
						<span id="n_t_wc"></span>
					</div>
					<div class="r_reply">
						<textarea id="n_content"><s:property value="newsbase.content"/></textarea>
					</div>
					<div class="r_button">
						<span id="wordcount"></span>
					</div>
				</td>
			</tr>
			<!-- 分割线 -->
			<tr><td class="n_separL"></td><td class="n_separR"></td></tr>
			<tr>
				<td class="n_left"></td>
				<td class="n_right">
					<table class="n_nb_table"><tbody>
						<tr>
							<td class="n_nb_td"></td>
							<td class="n_nb_mess">&dArr;下面的内容管理员才能修改&dArr;</td>
						</tr>
						<tr>
							<td class="n_nb_td">查看次数</td>
							<td>
								<input id="nch_hit" name="nch_hit" type="text" class="n_input"
									value="<s:property value="newsbase.hit"/>"
									onkeyup="this.value=this.value.replace(/\D/g,'')"/>
								<span id="n_h_mess" style="color:#E53333;"></span>
							</td>
						</tr>
						<tr>
							<td class="n_nb_td">评论次数</td>
							<td><s:property value="newsbase.replynum"/></td>
						</tr>
						<tr>
							<td class="n_nb_td">发表时间</td>
							<td>
								<input id="nch_ptime" name="nch_ptime" type="text" class="n_input Wdate"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									value="<s:date name="newsbase.publishtime" format="yyyy-MM-dd HH:mm:ss"/>"/>
								<span id="n_pt_mess" style="color:#E53333;"></span>
							</td>
						</tr>
						<tr>
							<td class="n_nb_td">审核情况</td>
							<td id="nch_status">
								<s:if test="newsbase.status==0">
					    			<s:if test="newsbase.reviewtime">
							    		已审核<br />
							    		审核人员：<s:property value="newsbase.reviewer.name"/><br />
							  			审核时间：<s:date name="newsbase.reviewtime" format="yyyy-MM-dd HH:mm:ss"/>
							  		</s:if>
							  		<s:else>直接发布</s:else>
					    		</s:if>
					    		<s:else><a class="action" href="javascript:;"
					    			onclick="checkNews()">允许发布</a></s:else>
							</td>
						</tr>
						<tr>
							<td class="n_nb_td">是否置顶</td>
							<td>
								<input type="radio" name="nch_sort" value="1" checked="checked"/>是
					    		<input type="radio" name="nch_sort" value="0"
					    		<s:if test="newsbase.sort==0">checked="checked"</s:if>/>否
							</td>
						</tr>
						<tr>
							<td class="n_nb_td">是否显示</td>
							<td>
								<input type="radio" name="nch_display" value="1" checked="checked"/>是
					    		<input type="radio" name="nch_display" value="0"
					    		<s:if test="newsbase.display==0">checked="checked"</s:if>/>否
							</td>
						</tr>
						<tr>
							<td class="n_nb_td"></td>
							<td class="n_nb_mess">&uArr;上面的内容管理员才能修改&uArr;</td>
						</tr>
						<tr>
							<td class="n_nb_td">允许评论</td>
							<td>
								<input type="radio" name="nch_reply" value="1" checked="checked"/>是
			    				<input type="radio" name="nch_reply" value="0"
			    				<s:if test="newsbase.reply==0">checked="checked"</s:if>/>否
							</td>
						</tr>
						<tr>
							<td><a class="action" href="javascript:;" onclick="removeNews()">删除</a></td>
							<td><input class="u_button" type="button" onclick="postNews()" value="更新新闻"><br><br></td>
						</tr>
					</tbody></table>
				</td>
			</tr>
		</tbody></table>
	</div>
</div>
<%@ include file="../common/body_tail.jsp"%>
</body>
</html>