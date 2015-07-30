<%@ page contentType="text/html; charset=UTF-8"  %>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.点击"操作"栏的一项进行修改；
		2.名称长度不能超过10个；
		3.顺序必须为数字，且在1和100之间。</div>
</div>
<div id="dtli_toolbar" style="padding:5px;height:auto">
	<a href="javascript:;" class="easyui-linkbutton" onclick="dtli_addDt()"
		data-options="'iconCls':'icon-add','plain':true">添加</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#dtli_table').datagrid('load');"
		data-options="'iconCls':'icon-reload','plain':true">刷新</a>
</div>
<table id="dtli_table"></table>