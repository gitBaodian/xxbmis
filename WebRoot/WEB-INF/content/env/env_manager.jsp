<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>


<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>

<div id="usli_toolbar_env_manager" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" id="env_add" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="env_add()">增加</a>
		<a href="javascript:;" id="env_update" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="env_update()">修改</a>
		<a href="javascript:;" id="env_del" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="env_del()">删除</a>
	</div>
</div>

<table id="usli_env_manager"></table>


<div id="env_add_dd" class="easyui-dialog" style="width:380px;height:260px;padding:10px 20px;overflow:hidden;"
		data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">环境信息</div>
	<form id='env_add_form' method='post'>
		<div class="usli_fitem">
			<label>环境代码：</label>
			<input id="env" type="text" name="env">
		</div>
		<div class="usli_fitem">
			<label>环境名：</label>
			<input id="env_name" type="text" name="env_name">
		</div>
	</form>
</div>

<div id="env_update_dd" class="easyui-dialog" style="width:380px;height:260px;padding:10px 20px;overflow:hidden;"
		data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">环境信息</div>
	<form id='env_update_form' method='post'>
		<div class="usli_fitem">
			<input id="env_id" type="text" name="id" hidden="">
		</div>
		<div class="usli_fitem">
			<label>环境代码：</label>
			<input id="env" type="text" name="env">
		</div>
		<div class="usli_fitem">
			<label>环境名：</label>
			<input id="env_name" type="text" name="env_name">
		</div>
	</form>
</div>
	