<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>信息部MIS</title>
<base href="<%=basePath%>"/>

<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" /> 
<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="script/ztree/css/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" type="text/css" href="script/kindeditor/themes/default/default.css" >


<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="script/ztree/jquery.ztree.all.min.js"></script>

<script type="text/javascript" src="script/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="script/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="script/baodian/util/mess.js"></script>
<script type="text/javascript" src="script/baodian/util/date.js"></script>
<script type="text/javascript" src="script/index.js"></script>

<script type="text/javascript" src="script/baodian/handover/handover.js"></script>
<script type="text/javascript" src="script/baodian/handover/applicant.js"></script>
<script type="text/javascript" src="script/easyui/portal/jquery.portal.js"></script>
<script type="text/javascript" src="script/jquery/jquery.sortable.min.js"></script>

<script type="text/javascript">
var index_data = {};
index_data.index = <s:property value="json" escapeHtml="false"/>;
index_data.user = {
	"id": "<s:property value="user.id[0]" escapeHtml="false"/>",
	"name": "<s:property value="user.str[0]" escapeHtml="false"/>",
	"depm": "<s:property value="user.department" escapeHtml="false"/>"
};
</script>
<style type="text/css">
</style>
</head>
<body>
<div class="index_prompt" id="index_prompt" style="display: none"></div>
<!-- 首页主体 begin 分为上 下左 下右 -->
<div class="easyui-layout" data-options="fit: true">
	<div style="height: 80px;padding: 0 30px;" data-options="region:'north',border: false">
		<div style="float: left;margin-top: 10px;">
			<!-- <a href="http://www.baodian.com" target="_blank"></a> -->
			<img src="images/logo0.gif" style="border: 0;"/>
			<!-- <img src="images/yaan.gif" style="border: 0;"/> -->
		</div>
		<div style="float: right;">
			<div style="float: left;padding-top: 45px;">
				<span id="index_user"></span>
				<a href="javascript:;" class="easyui-linkbutton" onclick="index_login()">登录</a>
				<a id="index_loa" href="j_spring_security_logout" class="easyui-linkbutton" >退出</a>
				<a id="index_cpa" href="javascript:;" class="easyui-linkbutton" onclick="index_changePS()">修改密码</a>
			</div>
		</div>
	</div>
	<div title="菜单栏" style="width:178px;overflow-y:scroll;"
		data-options="region:'west',split:true">
		<ul id="index_treeDemo" class="ztree"></ul>
	</div>
	<div data-options="region:'center'">
		<div id="mainbody" class="easyui-tabs" data-options="fit:true,border:false">
			<div title="主体部分" class="tabcontent"
				data-options="tools:[{
			        iconCls:'icon-mini-refresh',
			        handler:function(){loadIndex(true);}}]">
				<div id="index_pp" style="padding:8px 0;overflow-x:hidden;"><div></div><div></div><div></div></div>
			</div>
		</div>
	</div>
</div>
<!-- 首页主体 end -->
<!-- 登录框 -->
<div id="index_dd" style="width:350px;height:200px;padding:25px 0 30px 30px">
	<form id='index_form' action='j_spring_security_check' method='POST'>
	<table>
		<tr><td>用户名：</td>
			<td>
				<input id="j_username" type='text' name='j_username' class="index_input160"/>
				<input type="hidden" name="j_ajax"/>
			</td>
		</tr>
		<tr style="height: 30px;"><td>密&emsp;码：</td>
			<td><input id="j_password" type='password' name='j_password' class="index_input160"/></td>
		</tr>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='_spring_security_remember_me' /></td>
			<td>记住密码</td>
		</tr>
	</table>
	</form>
</div>
<!-- 更改密码框 begin-->
<div id="index_ps" class="easyui-dialog" style="width:400px;height:210px;padding:25px 20px 30px 20px"
	data-options="closed:true,title:'更改密码',buttons:'#index_dlg-buttons'">
	<form id="index_fm" method="post">
		<div class="usli_fitem">
			<label>原始密码：</label><input name="user.account" type="password" style="width:150px;" class="easyui-validatebox" data-options="required:true">
			<span id="index_pw_span"></span>
		</div>
		<div class="usli_fitem">
			<label>新的密码：</label><input name="user.password" type="password" style="width:150px;">
			<span>为空，使用123456。</span>
		</div>
		<div class="usli_fitem">
			<label>确定密码：</label><input name="user.name" type="password" style="width:150px;">
		</div>
	</form>
</div>
<div id="index_dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton" onclick="index_changePW()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="javascript:$('#index_ps').dialog('close')"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<!-- 更改密码框 end-->

<div id="shift_dd"  class="easyui-dialog" style="width:380px;height:250px;padding:10px 20px;overflow:hidden;"
		data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">交班备注</div>	
	<form id='shift_form' method='POST'>
		<textarea name='remark_shift' rows="4" cols="37"></textarea>
		<div class="index_addnp">
			<span>其他选项：&emsp;</span>
			<input id="index_addnp" type="checkbox" name="addnp"/>
			<label for="index_addnp">添加进每日记事</label>
		</div>
		<input type="reset" style="display:none;" /> 
	</form>
</div>
<div id="accept_dd"  class="easyui-dialog" style="width:380px;height:280px;padding:10px 20px;overflow:hidden;"
		data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<form id='accept_form' method='POST'>
		<div class="usli_ftitle">交班备注</div>
		<div id='see_remark_shift' class="usli_fitem"></div>
		&nbsp;
		<div class="usli_ftitle">接班备注</div>
		<div class="usli_fitem"><textarea name='remark_accept' rows="2" cols="37"></textarea></div>
	</form>
</div>
<div id="ask_leave_dd" class="easyui-dialog" style="width:380px;height:260px;padding:10px 20px;overflow:hidden;"
		data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">请假信息</div>
	<form id='ask_leave_form' method='post'>
		<div class="usli_fitem">
			<label>申请人：</label>
			<input id="username" type="text" name="username" readonly>
		</div>
		<div class="usli_fitem">
			<label>申请人职位：</label>
			<input id="posts" type="text" name="posts" readonly>
		</div>
		<div class="usli_fitem">
			<label>启始时间：</label>
			<input id="dd" type="text" name="begindate" class="easyui-datebox" required="required"></input>  
		</div>
		<div class="usli_fitem">
			<label>结束时间：</label>
			<input id="dd" type="text" name="enddate" class="easyui-datebox" required="required"></input>  
		</div>
	</form>
</div>
<div id="apl_transfer_dd" class="easyui-dialog" style="width:380px;height:250px;padding:10px 20px;overflow:hidden;"
		data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">调班信息</div>
	<form id='apl_transfer_form' method='post'>
		<div class="usli_fitem">
			<label> 申请人：</label>
			<input id="applicant" type="text" name="applicant" readonly>
		</div>
		<div class="usli_fitem">
			<label>班值时间：</label>
			<input id="dd" type="text" name="applicant_time" class="easyui-datebox" required="required"></input>
		</div>
		<div class="usli_fitem">
			<label>申请对象：</label>
			<select id="object" name="object" style="width:150px;">
			</select> 
		</div>
		<div class="fitem">
			<label>类型：</label>
			<input type="radio" name="t_or_r" value="t" checked>调班
			<input type="radio" name="t_or_r" value="r">替班
		</div>
	</form>
</div>
</body>
</html>