<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>信息部邮箱</title>
<%@ include file="../common/header.jsp"%>
<script type="text/javascript" src="script/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="script/ztree/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" type="text/css" href="script/ztree/css/zTreeStyle.css">
<script type="text/javascript" src="script/baodian/email/email_list.js"></script>
<script type="text/javascript" src="script/baodian/util/date.js"></script>
<link rel="stylesheet" type="text/css" href="css/email.css"/>
</head>
<body>
<div class="h_bg">
    <div class="bg hbg hbg1"></div>
    <div class="bg hbg hbg2"></div>
    <div class="bg hbg hbg3"></div>
    <div class="bg hbg hbg4"></div>
    <div class="bg hbg hbg5"></div>
</div>
<div class="w_bg">
    <div class="bg wbg wbg1"></div>
    <div class="bg wbg wbg2"></div>
    <div class="bg wbg wbg3"></div>
    <div class="bg wbg wbg4"></div>
    <div class="bg wbg wbg5"></div>
</div>
<%@ include file="../common/body_top.jsp"%>
<div id="jumpPage" class="jumpPage">
	跳转到第 <input id="jpinput" type="text" class="j_page"
			onkeyup="this.value=this.value.replace(/\D/g,'')"/> 页
	<span onclick="jumpPage()">确定</span>
</div>
<div id="stwd" class="stwd" style="height: 158px;width: 125px;">
	<div class="mi-link" onclick="jpstatus(1);stclose();">
		<span class="miico"></span>
		<span class="mitext">全部</span>
	</div>
	<div class="mi-link" onclick="jpstatus(2);stclose();">
		<b class="e_stx e_st0"></b>
		<span class="mitext">未读</span>
	</div>
	<div class="mi-link" onclick="jpstatus(3);stclose();">
		<span class="miico"></span>
		<span class="mitext">已删除</span>
	</div>
	<div class="mi-split"></div>
	<div class="mi-link" onclick="sortEmail(1);stclose();" id="sort_t"></div>
	<div class="mi-link" onclick="sortEmail(2);stclose();" id="sort_u"></div>
</div>
<div id="sdwd" class="stwd" style="height: 102px;width: 125px;">
	<div class="mi-link" onclick="jpstatus(-1);sdclose();">
		<span class="miico"></span>
		<span class="mitext">全部</span>
	</div>
	<div class="mi-link" onclick="jpstatus(-2);sdclose();">
		<span class="miico"></span>
		<span class="mitext">已删除</span>
	</div>
	<div class="mi-split"></div>
	<div class="mi-link" onclick="sortEmail(1);sdclose();" id="send_t"></div>
</div>
<div id="rftime" class="rftime stwd" style="height: 18px;width: 400px;">
	<label>30秒</label><input type="radio" value="30000" name="rftime" >
	<label>60秒</label><input type="radio" checked="checked" value="60000" name="rftime">
	<label> 3分</label><input type="radio" value="180000" name="rftime" >
	<label> 5分</label><input type="radio" value="300000" name="rftime" >
	<label>10分</label><input type="radio" value="600000" name="rftime" >
	<label>15分</label><input type="radio" value="900000" name="rftime" >
	<label>18分</label><input type="radio" value="1080000" name="rftime" >
	<input type="button" onclick="chrftime();" value="确定">
</div>
<div class="email_body">
	<div class="email_left" title="邮箱更新：2013年1月19日22:33:14">
		<div class="navi_top">
			<ul class="navi_topul">
				<li title="写信" onclick="wtemail(0);">
					<b class="email_w"></b>写信</li>
				<li title="收信" onclick="jpstatus(1, 1);">
					<b class="email_r"></b>收信</li>
				<li title="联系人">
					<b class="email_c"></b>联系人</li>
			</ul>
		</div>
		<div class="navi_middle">
			<ul id="menu_ul" class="navi_midul">
				<li title="收件箱" onclick="jpstatus(1, 1)">收件箱<b id="menu_li_b"></b></li>
				<li title="发件箱" onclick="jpstatus(-1, 1)">发件箱</li>
				<li title="已删收件" onclick="jpstatus(3, 1)">已删收件</li>
				<li title="已删发件" onclick="jpstatus(-2, 1)">已删发件</li>
				<li title="设置刷新时间" onclick="$('#rftime').toggle();">设置刷新时间</li>
				<!-- <li onclick="autorf();">手动刷新</li> -->
			</ul>
		</div>
	</div>
	<div id="email_right" class="email_right">
    	<div id="email_tb" class="email_toolbar"></div>
    	<div id="email_tb_ext" class="email_tb_ext"></div>
	    <div id="email_content" class="email_content">
	    	<!-- <div class="econt_title"><span>收件箱 </span>(共 <b>101</b> 封，有 <b>1</b> 封
	    		<a onclick="" href="javascript:;">未读邮件</a>
	    		<a onclick="" href="javascript:;">全部设为已读</a> )</div>
	    	<div class="econt_time"><b>今天</b>(4)</div>
	    	<table>
	    		<tr>
	    			<td class="e_ch"><input type="checkbox"></td>
	    			<td class="e_st"><input class="e_st1" type="button"/></td>
	    			<td class="e_ad">发件人</td>
	    			<td class="e_ti">标题</td>
	    			<td class="e_da">2012-02-01</td>
	    		</tr>
	    	</table> -->
			<!-- <div class="em_ct">
				<div class="em_ct_title" title="状态:1">第十一封邮件11111111111111111111111</div>
				<div class="em_ct_emer"><b>李杨洲</b>&lt;6&gt;</div>
				<div class="em_ct_emee">收件:
					<span class="em_ct_emeea" title="正常-未读 : 2012年12月11日 23:15 (星期二)"> <span class="em_ct_emeeb">刘碧锋</span>&lt;12&gt; </span>
				</div>
				<div>发件时间：2012年12月5日 17:02 (星期三)</div>
				<div>收件时间：2012年12月5日 17:03 (星期三)</div>
			</div> -->

	    </div>
	</div>
</div>
</body>
</html>