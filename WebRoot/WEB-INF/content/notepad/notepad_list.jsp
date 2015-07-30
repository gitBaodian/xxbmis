<%@ page contentType="text/html; charset=UTF-8" %>
<%-- <%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var npli_ = <s:property value="json" escapeHtml="false"/>;
</script> --%>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击打开或者编辑。</div>
</div>
<div id="npli_toolbar" style="padding:5px;height:auto;">
	<div style="padding-top: 5px; padding-left: 4px;">
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#npli_add').dialog('open');"
			data-options="'iconCls':'icon-add','plain':true">添加</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="npli_opench()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="npli_top(1)"
			data-options="'iconCls':'icon-edit','plain':true">批量置顶</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="npli_top(2)"
			data-options="'iconCls':'icon-edit','plain':true">取消置顶</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="npli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">批量删除</a>
	</div>
	<div style="padding-top: 5px; padding-left: 4px;">
		内容：<input id="npli_name" style="height: 18px;line-height: 18px;width: 220px;vertical-align: middle;">
		&ensp;置顶：<select id="npli_top" class="easyui-combobox" data-options="width:55, panelHeight:71,editable:false">
			<option value="0">全部</option>
			<option value="1">是</option>
			<option value="2">否</option>
		</select>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="$('#npli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>
<table id="npli_table"></table>
<div id="npli_change"></div>
<div id="npli_add"></div>
