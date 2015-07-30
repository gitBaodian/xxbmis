<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var gdli_zNodes = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.简要信息为此设备能区别其他的简单描述，不能超过20个字；2.详细信息是对简要进行补充，不能超过100个字；
		3.目前只支持2层的设备结构，2层以上的结构会被忽略。
	</div>
</div>
<br>
<div style="width: 110px;float: left;padding-left: 20px;">
	<br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="gdli_treeObj.expandAll(true);"
		data-options="plain:true,iconCls:'icon-undo'">展开</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="gdli_treeObj.expandAll(false);"
		data-options="plain:true,iconCls:'icon-redo'">折叠</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="gdli_addAuth()"
		data-options="plain:true,iconCls:'icon-edit'">添加到顶层</a><br><br>
	<a href="javascript:;" class="easyui-linkbutton" onclick="gdli_changeNodes();"
		data-options="plain:true,iconCls:'icon-save'">保存设备顺序</a><br><br>
</div>
<div style="float: left;padding-left: 30px;border-left: 1px solid #C2D5E3;">
	<div class="gdli_input">
		<input id="gdli_id" type="hidden">
		<input id="gdli_tid" type="hidden">
		<label>简要信息：</label>
		<input id="gdli_name" type="text"><br>
		<label>详细信息：</label>
		<textarea id="gdli_detail" class="gdli_tt"></textarea>
		<a href="javascript:;" class="easyui-linkbutton" onclick="gdli_changeAuth()"
			data-options="plain:true,iconCls:'icon-edit'">更新设备</a>
	</div>
	<div style="float: left;padding-top: 10px;">
		<label>设备列表： </label>
	</div>
	<ul id="gdli_treeDemo" class="ztree" style="padding: 10px 0 20px 65px;"></ul>
</div>

<div id="gdli_dd" style="width:400px;height:240px;padding:25px 0 0 30px">
	<form id='gdli_form' method='POST'>
		<div class="gdli_fitem">
			<label>简要信息:</label>
			<input id="gdli_gname" class="easyui-validatebox" data-options="required:true,validType:'length[1,20]'">
			<span id="gdli_n_span"></span>
		</div>
		<div class="gdli_fitem">
			<label>详细信息:</label><br />
			<textarea id="gdli_gdetail" class="gdli_tt easyui-validatebox" data-options="validType:'length[0,100]'"></textarea>
			<span id="gdli_url_span"></span>
		</div>
	</form>
</div>
