<%@ page contentType="text/html; charset=UTF-8"  %>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.在这里定义哪个部门作为倒班的一个值，选中的那个部门的人员将按四班三倒上班。</div>
</div>
<div>
	<a href="javascript:;" class="dtli_top easyui-linkbutton" onclick="dtli_changeDuty()"
		data-options="iconCls:'icon-edit'">&nbsp;更新</a>
</div>
<div class="dtli_body">
<div id="dtli_left" class="dtli_left"></div>
<div class="dtli_right">
	<div class="dtli_ra">
		<a href="javascript:;" class="easyui-linkbutton" onclick="dtli_treeObj.expandAll(true);"
			data-options="plain:true,iconCls:'icon-undo'">展开</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="dtli_treeObj.expandAll(false);"
			data-options="plain:true,iconCls:'icon-redo'">折叠</a>
	</div>
	<div class="dtli_rb"><ul id="dtli_tree" class="ztree"></ul></div>
</div>
</div>