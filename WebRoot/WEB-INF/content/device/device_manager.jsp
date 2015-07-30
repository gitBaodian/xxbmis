<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>

<script type="text/javascript" src="script/easyui/datagrid-detailview.js"></script>

<div id="usli_toolbar_device_manager" style="padding:5px;height:auto">
	<div>
		<!-- <a href="javascript:;" id="equ_execl_up" class="easyui-linkbutton" iconCls="icon-add" plain="true" >导入</a> -->
		<a href="javascript:;" id="equ_batchup" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="equ_batch_add()">增加</a>
		<a href="javascript:;" id="equ_batchup" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="equ_batch_up()">修改</a>
		<a href="javascript:;" id="equ_ip_del" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="equ_del()">删除</a>
		<a href="javascript:;" id="equ_execldl" class="easyui-linkbutton" iconCls="icon-download" plain="true" onclick="create_equ_xls()">导出</a>
		<a href="javascript:;" id="env_manager" class="easyui-linkbutton" iconCls="icon-role" plain="true" onclick="index_openTab('环境列表','env_manager.action','env_manager.js')">管理</a>
		<input id="hid_env" value="500" hidden="">
		<select id="one_env_list" name="env"  class="easyui-combobox" style="width:100px;" editable="false"
			data-options="valueField:'env',textField:'env_name'">
		</select>  
		<a href="javascript:;" class="easyui-linkbutton" onclick="env_load()" data-options="iconCls:'icon-search'">查找</a>	
	</div>
</div>

<table id="usli_device_manager"></table>

	<div id="equ_batch_add" class="easyui-dialog" style="width:350px;height:400px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
		<div class="usli_ftitle">设备信息</div>
		<form id='equ_batch_add_form' method='post'>
			<div class="usli_fitem">
				<label>I  P：</label>
				<input type="text" name="ip">
				<label>（例：172.16.1.11..90,172.16.2.31..44）</label>
			</div>
			<div class="usli_fitem">
				<label>角色：</label>
				<input type="text" name="role">
			</div>
			<div class="usli_fitem" style="width:150px;">
				<label>环境：</label>
				<select id="two_env_list" name="env"  class="easyui-combobox" style="width:100px;" editable="false"
					data-options="panelHeight:'auto',valueField:'env',textField:'env_name'">
				</select>  
			</div>
			<div class="usli_fitem">
				<label>系统：</label>
				<input type="text" name="system">
			</div>
			<div class="usli_fitem">
				<label>CPU：</label>
				<input type="text" name="cpu">
			</div>
			<div class="usli_fitem">
				<label>硬盘：</label>
				<input type="text" name="hard">
			</div>
			<div class="usli_fitem">
				<label>内存：</label>
				<input type="text" name="memory">
			</div>
			<div class="usli_fitem">
				<label>主板：</label>
				<input type="text" name="motherboard">
			</div>
			<div class="usli_fitem">
				<label>类型：</label>
				<input type="text" name="type">
			</div>
			<div class="usli_fitem">
				<label>其它：</label>
				<input type="text" name="other">
			</div>
			</form>
	</div>
	
	<div id="equ_batch_up" class="easyui-dialog" style="width:380px;height:400px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
		<div class="usli_ftitle">信息修改</div>
		<form id='equ_batch_up_form' method='post'>
			<div class="usli_fitem">
				<label>I  P：</label>
				<input id="bip" type="text" name="ip">
				<label>（例：172.16.1.11..90,172.16.2.31..44）</label>
			</div>
			<div class="usli_fitem">
				<label>角色：</label>
				<input id="brole" type="text" name="role">
			</div>
			
			<div class="usli_fitem">
				<label>系统：</label>
				<input id="bsystem" type="text" name="system">
			</div>
			<div class="usli_fitem">
				<label>CPU：</label>
				<input id="bcpu" type="text" name="cpu">
			</div>
			<div class="usli_fitem">
				<label>硬盘：</label>
				<input id="bhard" type="text" name="hard">
			</div>
			<div class="usli_fitem">
				<label>内存：</label>
				<input id="bmemory" type="text" name="memory">
			</div>
			<div class="usli_fitem">
				<label>主板：</label>
				<input id="bmotherboard" type="text" name="motherboard">
			</div>
			<div class="usli_fitem">
				<label>类型：：</label>
				<input id="type" type="text" name="type">
			</div>
			<div class="usli_fitem">
				<label>其它：</label>
				<input id="other" type="text" name="other">
			</div>
			</form>
	</div>
