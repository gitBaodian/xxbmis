<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>


<div id="usli_toolbar_inspection_manager" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" id="inspection_add" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="index_openTab('添加巡检记录','inspection_add.action')">添加</a>
		<a href="javascript:;" id="inspection_update" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="inspection_update()">查看</a>
		<a href="javascript:;" id="inspection_del" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="inspection_del()">删除</a>
	</div>
	
</div>

<table id="usli_inspection_manager"></table>

			
	



	
	
