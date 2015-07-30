<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var grli_data = <s:property value="json" escapeHtml="false"/>;
grli_data.dts.unshift({id: 0, name: "-"});
grli_data.uts.unshift({id: 0, name: "快捷操作"});
grli_data.utselect = function(useType) {
	if(useType.id == 0) {
		return;
	}
	var data = {
		'gr.gd.id': useType.gd,
		'gr.dtin.id': useType.dtin,
		'gr.dtout.id': useType.dtout
	};
	$('#grli_fm').form('load', data);
	var grli_tt = $("#grli_fm div:eq(1) textarea:first");
	if(grli_tt.val() == "") {
		grli_tt.val(useType.name);
	}
};
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一条记录可以进行修改；
		2.以后添加按设备和时间查找功能。</div>
</div>
<div id="grli_toolbar" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton" onclick="grli_newGr()"
			data-options="iconCls:'icon-add',plain:true">添加记录</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="grli_editGr()"
			data-options="iconCls:'icon-edit',plain:true">修改记录</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="grli_removeGr()"
			data-options="iconCls:'icon-remove',plain:true">删除记录</a>
	</div>
	<div style="padding-top: 5px; padding-left: 4px;">
		记录: <input id="grli_name" style="width:120px">
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#grli_table').datagrid('load')"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>
<table id="grli_table"></table>
<div id="grli_dlg" class="easyui-dialog" style="width:460px;height:330px;padding:10px 20px;"
	data-options="closed:true,buttons:'#grli_dlg-buttons',onClose:function(){grli_row.edit=0;}">
	<div class="usli_ftitle">
		<span>设备 记录&emsp;</span>
		<select class="easyui-combobox" style="margin-left:10px;width:150px;"
				data-options="valueField:'id',textField:'name',data:grli_data.uts,onSelect:grli_data.utselect,editable:false,panelHeight:120"></select>
	</div>
	<form id="grli_fm" method="post">
		<div class="usli_fitem">
			<label>时间:</label><input name="gr.date" class="easyui-datetimebox" style="width:150px;"
				data-options="required:true,editable:false">
		</div>
		<div class="usli_fitem">
			<label>记录:</label><textarea name="gr.name" class="easyui-validatebox" style="height:45px;width:350px;" 
				data-options="required:true,validType:'length[1,100]'"></textarea>
		</div>
		<div class="usli_fitem">
			<label>设备:</label><input id="grli_cc" name="gr.gd.id" class="easyui-combotree" style="width:200px;"
				data-options="required:true,panelHeight:150,data:index_changeTree(grli_data.gds)">
		</div>
		<div class="usli_fitem">
			<label>来源:</label><select id="grli_dtout" name="gr.dtout.id" class="easyui-combobox" style="width:200px;"
				data-options="valueField:'id',textField:'name',data:grli_data.dts,required:true,editable:false,panelHeight:120"></select>
		</div>
		<div class="usli_fitem">
			<label>目地:</label><select id="grli_dtin" name="gr.dtin.id" class="easyui-combobox" style="width:200px;"
				data-options="valueField:'id',textField:'name',data:grli_data.dts,required:true,editable:false,panelHeight:120"></select>
		</div>
		<div class="usli_fitem">
			<label>数量:</label><input name="gr.num" class="easyui-numberbox"
				data-options="required:true,min:1,max:2147483647">
		</div>
	</form>
</div>
<div id="grli_dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton" onclick="grli_saveGr()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="javascript:$('#grli_dlg').dialog('close')"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>