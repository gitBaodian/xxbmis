<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>

<script type="text/javascript">

</script>
<div id="usli_toolbar_specialtransfer" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="add_specialtransfer()">添加</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit_specialtransfer()">修改</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove_specialtransfer()">删除</a>
	</div>
</div>

				<div id="add_specialtransfer_dd" class="easyui-dialog" style="width:380px;height:280px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
					<div class="usli_ftitle">人员安排</div>
					<form id='add_specialtransfer_form' method='post'>
						<div class="usli_fitem">
							<label>日期：</label>
							<input id="specil_date" name="specil_date" class="easyui-datebox" style="width:150px">
						</div>
						&nbsp;
						<div class="usli_fitem">
							<label>后夜：</label>
							<select id="st_object1" name="one" style="width:150px;">
							</select> 
						</div>
						&nbsp;
						<div class="usli_fitem">
							<label>白班：</label>
							<select id="st_object2" name="two" style="width:150px;">
							</select> 
						</div>
						&nbsp;
						<div class="usli_fitem">
							<label>前夜：</label>
							<select id="st_object3" name="there" style="width:150px;">
							</select> 
						</div>
					</form>
				</div>

				<div id="update_specialtransfer_dd" class="easyui-dialog" style="width:420px;height:280px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
					<div class="usli_ftitle">人员修改</div>
					<form id='update_specialtransfer_form' method='post'>
						<div class="usli_fitem">
							<label>日期：</label>
							<input id="specialtransfer_date" type="text" name="specil_date" readonly="readonly" style="width:150px">
						</div>
						&nbsp;
						<div class="usli_fitem">
							<label>后夜：</label>
							<select id="specialtransfer_one" name="one" style="width:150px;">
							</select> 
						</div>
						&nbsp;
						<div class="usli_fitem">
							<label>白班：</label>
							<select id="specialtransfer_two" name="two" style="width:150px;">
							</select> 
						</div>
						&nbsp;
						<div class="usli_fitem">
							<label>前夜：</label>
							<select id="specialtransfer_there" name="there" style="width:150px;">
							</select> 
						</div>
					</form>
				</div>
