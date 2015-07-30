<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var docch_para = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.一次只能更新一个文档；
		2.名称字数不能超过100；
		3.外部链接，格式为：http://www.baidu.com，字数不能超过100，填写时，使用此页面代替文档内容。</div>
</div>
<div class="docadd_input">
	<label>文档名称：</label>
	<input id="docch_name" class="easyui-validatebox">
	<a href="javascript:;" class="easyui-linkbutton" style="margin-left: 20px;"
		data-options="iconCls:'icon-edit'" onclick="docch_update()">更新</a>
	<a href="javascript:;" class="easyui-linkbutton" 
		data-options="iconCls:'icon-redo'" onclick="docch_open()">查看</a>
	<a href="javascript:;" class="easyui-linkbutton" style="margin-left: 5px;"
		onclick="docch_remove()">删除</a>
</div>
<div class="docadd_input">
	<label>所在目录：</label>
	<select id="docch_dir" class="easyui-combotree" style="width: 330px;"
		data-options="data:index_changeTree(docch_para.dirs, {'iconCls':'eu-folder-open'})"></select>
</div>
<div class="docadd_input">
	<label>外部链接：</label>
	<input id="docch_url">&emsp;如果不填，表示使用自己输入的内容。
</div>
<div style="padding: 10px 0 15px 0;">
	<textarea id="docch_tt"></textarea>
	<span id="docch_mess"></span>
</div>