<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var docli_zNodes = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.关键字可以用'_'代替一个字符。</div>
</div>
<div id="docli_tb" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="docli_expandAll()"
			data-options="plain:true,iconCls:'icon-undo'">展开</a>
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#docli_table').treegrid('collapseAll')"
			data-options="plain:true,iconCls:'icon-redo'">折叠</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#docli_dlg').dialog('open');"
			data-options="plain:true,iconCls:'icon-edit'">移动文档</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="index_openTab('添加公共文档','document_add.action',true);"
			data-options="plain:true,iconCls:'icon-add'">添加文档</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="index_openTab('公共目录','docdir_list.action',true);"
			data-options="plain:true,iconCls:'icon-edit'">管理目录</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="docli_reload()"
			data-options="plain:true,iconCls:'icon-reload'">重新载入</a>
	</div>
	<div style="padding:5px 0 0 4px;">
		<select id="docli_area" class="easyui-combobox" data-options="width:80, panelHeight:71,editable:false">
			<option value="0">标题+内容</option>
			<option value="1">标题</option>
			<option value="2">内容</option>
		</select>
		<input id="docli_name" style="height: 21px;line-height: 21px;width: 180px;vertical-align: middle;">
		<a href="javascript:;" class="easyui-linkbutton" onclick="docli_search()"
			data-options="iconCls:'icon-search'">搜索</a>
	</div>
</div>

<table id="docli_table"></table>
<br /><br />
<div id="docli_dlg" title="移动选中文档" class="easyui-dialog"
	style="width:460px;height:300px;padding:10px 20px;"
	data-options="closed:true,buttons:'#docli_dlg-buttons'">
	<div class="usli_ftitle">
		<span>请选择要移动到的目录</span>
	</div>
	<ul id="docli_tree" style="margin-bottom: 18px;"></ul>
</div>
<div id="docli_dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton"
		onclick="docli_saveDir()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton"
		onclick="$('#docli_dlg').dialog('close')"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
