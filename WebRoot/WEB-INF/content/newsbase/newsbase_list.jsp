<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var nbli_ncs = <s:property value="json" escapeHtml="false"/>;
var nbli_ncId = <s:if test="page.ncId"><s:property value="page.ncId" escapeHtml="false"/></s:if><s:else>0</s:else>;
var nbli_ncs2 = [{"id":0,"name":"全部"}];
if(nbli_ncId == 0)
	nbli_ncs2[0].selected = true;
$.each(nbli_ncs, function(k, v) {
	if(nbli_ncId == v.id)
		v.selected = true;
	nbli_ncs2.push(v);
});
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑；
		2.点击"操作"栏的一项进行修改；
		3.标题长度不能超过100个；
		4.浏览数必须为数字，且不能大于2147483647(2的11次方)。</div>
</div>
<div id="nbli_toolbar" style="padding:5px;height:auto;">
	<div style="padding-top: 5px; padding-left: 4px;">
		<a href="javascript:;" class="easyui-linkbutton" onclick="nbli_reviewNbs()"
			data-options="'iconCls':'icon-edit','plain':true">批量审核</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="nbli_SortNbs(1)"
			data-options="'iconCls':'icon-edit','plain':true">批量置顶</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="nbli_SortNbs(0)"
			data-options="'iconCls':'icon-edit','plain':true">取消置顶</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="nbli_removeNbs()"
			data-options="'iconCls':'icon-remove','plain':true">批量删除</a>
	</div>
	<div style="padding-top: 5px; padding-left: 4px;">
		标题：<input id="nbli_title" style="height: 18px;line-height: 18px;width: 220px;vertical-align: middle;">
		&emsp;类型：<select id="nbli_nclass" class="easyui-combobox"
			data-options="valueField:'id',textField:'name',data:nbli_ncs2,value:'全部',editable:false,width:90,panelHeight:100">
		</select>
		&ensp;显示：<select id="nbli_display" class="easyui-combobox" data-options="width:55, panelHeight:71,editable:false">
			<option value="2">全部</option>
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
		&ensp;置顶：<select id="nbli_sort" class="easyui-combobox" data-options="width:55, panelHeight:71,editable:false">
			<option value="2">全部</option>
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
		&ensp;评论：<select id="nbli_reply" class="easyui-combobox" data-options="width:55, panelHeight:71,editable:false">
			<option value="2">全部</option>
			<option value="1">允许</option>
			<option value="0">关闭</option>
		</select>
		&ensp;审核：<select id="nbli_status" class="easyui-combobox" data-options="width:55, panelHeight:71,editable:false">
			<option value="2">全部</option>
			<option value="1">未审</option>
			<option value="0">已审</option>
		</select>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="$('#nbli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>
<table id="nbli_table"></table>
