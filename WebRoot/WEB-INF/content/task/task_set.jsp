<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var taskset_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.任务是添加到部门上的。</div>
</div>
<div id="taskset_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="taskset_data.change=false;$('#taskset_add').dialog('open').dialog('setTitle','添加任务');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="taskset_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="taskset_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		标题+内容：<input id="taskset_title" class="index_input200" />
		&ensp;状态：<select id="taskset_status" class="easyui-combobox"
			data-options="width:56, panelHeight:76,editable:false">
			<option value="-1">全部</option>
			<option value="1">运行</option>
			<option value="2">停止</option>
		</select>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="$('#taskset_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>
<table id="taskset_table"></table>
<div id="taskset_add" style="padding:12px;">
	<div id="taskset_adduser" style="color:#666;padding-bottom:8px;"></div>
	<div>标题：<input id="taskset_addtitle" class="index_input320"/></div>
	<div style="padding-top:8px;">
		<textarea id="taskset_addcontent" style="width:100%;height:170px;"></textarea></div>
	<div style="padding-top:8px;">
		频率：<select id="taskset_addtype">
			<option value="1">每天</option>
			<option value="2">每星期</option>
			<option value="3">日期范围</option>
		</select></div>
	<div style="padding-top:8px;">
		&emsp;从：<input id="taskset_addbegin" class="easyui-datebox">
		到：<input id="taskset_addend" class="easyui-datebox"></div>
	<div style="padding-top:8px;">
		星期：<select id="taskset_adddata" class="easyui-combobox"
				data-options="editable:false,multiple:true,width:200,panelHeight:160,">
			    <option value="1">星期一</option>
			    <option value="2">星期二</option>
			    <option value="3">星期三</option>
			    <option value="4">星期四</option>
			    <option value="5">星期五</option>
			    <option value="6">星期六</option>
			    <option value="7">星期日</option>
		    </select></div>
	<div style="padding-top:8px;">
		提醒时间： <input id="taskset_addtime" class="easyui-timespinner" style="width:80px;" /></div>
	<div style="padding-top:8px;">
		执行部门： <input id="taskset_adddepm"/></div>
	<div id="taskset_addstatus" style="padding-top:8px;">任务状态：
		<input type="radio" name="taskset_addstatus" checked="checked">运行&emsp;
		<input type="radio" name="taskset_addstatus">停止</div>
</div>
<br /><br />
