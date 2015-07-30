<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var docadd_zNodes = <s:property value="docStr" escapeHtml="false"/>;
var docadd_dirId = <s:property value="json" default="null" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.名称字数不能超过100；
		2.外部链接，格式为：http://www.baidu.com，字数不能超过100，填写时，使用此页面代替文档内容。</div>
</div>
<div class="docadd_input">
	<label>文档名称：</label>
	<input id="docadd_name" class="easyui-validatebox">
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'"
		style="padding-left: 20px;" onclick="docadd_add()">添加</a>
</div>
<div class="docadd_input">
	<label>所在目录：</label>
	<select id="docadd_dir" class="easyui-combotree" style="width: 330px;"
		data-options="data:index_changeTree(docadd_zNodes, {'iconCls':'eu-folder-open'})"></select>
</div>
<div class="docadd_input">
	<label>外部链接：</label>
	<input id="docadd_url">&emsp;如果不填，表示使用自己输入的内容。
</div>
<div style="padding: 10px 0 15px 0;">
	<textarea id="docadd_tt"></textarea>
	<span id="docadd_mess"></span>
</div>