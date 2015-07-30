<%@ page contentType="text/html; charset=UTF-8"  %>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.任务列表。</div>
</div>
<div id="tasklist_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton" onclick="tasklist_editrow(true)"
			data-options="'iconCls':'icon-edit','plain':true">标为完成</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="tasklist_editrow(false)"
			data-options="'iconCls':'icon-edit','plain':true">取消完成</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="tasklist_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="index_openTask(4)">当前任务</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		标题+内容：<input id="tasklist_title" class="index_input200" />&ensp;
		状态：<select id="tasklist_status" class="easyui-combobox"
			data-options="width:56, panelHeight:76,editable:false">
			<option value="-1">全部</option>
			<option value="1">未完</option>
			<option value="2">完成</option>
		</select>&ensp;
		<select id="tasklist_time" class="easyui-combobox"
			data-options="width:100, panelHeight:120,editable:false">
			<option value="-1">全部任务</option>
			<option value="1">部门全部任务</option>
			<option value="2">部门明天任务</option>
			<option value="3">部门今天任务</option>
			<option value="4">部门历史任务</option>
		</select>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#tasklist_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="tasklist_clearData()">清除</a>
	</div>
</div>
<table id="tasklist_table"></table>
<div id="tasklist_add" style="padding:12px;">
	<div id="tasklist_addtitle"></div>
	<div style="padding-top:8px;">
		<textarea id="tasklist_addcontent" style="width:100%;height:210px;"></textarea></div>
	<div id="tasklist_adddata"></div>
</div>
<br /><br />
