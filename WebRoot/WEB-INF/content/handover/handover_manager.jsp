<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>
<script type="text/javascript">
var id_com;
$(function(){
	$('#usli_handover_manager').datagrid({
	    url: "handover_handover_show.action",
	    iconCls: "icon-edit",
	    title: "交接班记录",
	    striped: true,//条纹
	    fitColumns: true,//自动调整单元格宽度
	    rownumbers: true,//显示行号
	    singleSelect: true,//单选
	    pagination: true,//分页
	    paramsName: {
	    	page: "page.page",
	    	num: "page.num"
	    },toolbar: '#usli_toolbar_handover_manager',
	    columns: [[
	        {field: 'date', title: '日期', width: 10},
	        {field: 'bc', title: '班次', width: 5},
	        {field: 'crew',title:'值班人员', width: 20},
	        {field: 'remarkAccept',title:'接班记录', width: 50},
	        {field: 'remarkShift',title:'交班记录', width: 50},
	    ]]
	});
});


$('#handover_add_dd').dialog({
	title: "交接班补签",
	closed: true,
	content: $('#handover_add_dd').html(),
	buttons:[{
		text:'添加',
		iconCls:'icon-ok',
		handler:function(){
				$.post("handover_handover_add.action", $("#handover_add_form").serialize(), function(data) {
					if(data.status == "0") {
						$('#handover_add_dd').dialog('close');
						alert(data.mess);
						$('#usli_handover_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				},"json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#handover_add_dd').dialog('close');
		}
	}]
});

$('#handover_update_dd').dialog({
	buttons:[{
		text:'修改',
		iconCls:'icon-ok',
		handler:function(){
				$.post("handover_handover_update.action", $("#handover_update_form").serialize(), function(data) {
					if(data.status == "0") {
						$('#handover_update_dd').dialog('close');
						alert(data.mess);
						$('#usli_handover_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				},"json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#handover_update_dd').dialog('close');
		}
	}]
});

function handover_add() {
	$('#handover_add_dd').dialog('open');
}

function handover_update() {
	var row = $('#usli_handover_manager').datagrid('getSelected');
	if(row){
		$.post('handover_handover_get.action?json='+Math.random(),{id:row.id},function(data){
			if (data.status == 0){   
				$("#up_id").val(row.id);
				$("#up_squad").val(data.squad);
				$("#up_crew").val(data.crew);
				$("#up_remarkAccept").val(data.remarkAccept);
				$("#up_remarkShift").val(data.remarkShift);
			} else {   
				alert(data.mess);
			}
		},"json");
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
	$('#handover_update_dd').dialog('open').dialog('setTitle','交接班修改');
}


function handover_del() {
	var row = $('#usli_handover_manager').datagrid('getSelected');
	if(row){
		$.messager.confirm('删除确定','你是否要删除此交接班信息?',function(r){
		    if (r){  
		       $.post('handover_handover_delete.action?json='+Math.random(),{id:row.id},function(data){
					if (data.status == 0){    //有权限
						alert(data.mess);
						$('#usli_handover_manager').datagrid('reload');
					} else {   
						alert(data.mess);
					}
				},"json");
		    }
		});  
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
}
</script>


<div id="usli_toolbar_handover_manager" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" id="handover_add" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="handover_add()">补签</a>
		<a href="javascript:;" id="handover_update" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="handover_update()">修改</a>
		<a href="javascript:;" id="handover_del" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="handover_del()">删除</a>
	</div>
	
</div>

<table id="usli_handover_manager"></table>

<div id="handover_add_dd" class="easyui-dialog" style="width:380px;height:340px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">补填信息</div>
	<form id='handover_add_form' method='post'>
		<div class="usli_fitem">
			<label>班值：</label>
			<select id="class_" name="class_" style="width:120px;">
			<option value="一值">一值</option>
			<option value="二值">二值</option>
			<option value="三值">三值</option>
			<option value="四值">四值</option>
			<option value="五值">五值</option>
			<option value="六值">六值</option>
		</select>
		</div>
		<div class="usli_fitem">
			<label>日期：</label>
			<input id="dd" type="text" name="date" class="easyui-datebox" data-options="required:true"></input>
		</div>
		<div class="usli_fitem">
			<label>接班记录：</label>
			<textarea id='remarkAccept' name='remark_accept' rows="2" cols="37"></textarea>
		</div>
		<div class="usli_fitem">
			<label>交班记录：</label>
			<textarea id='remarkShift' name='remark_shift' rows="2" cols="37"></textarea>
		</div>
	</form>
</div>					
	

<div id="handover_update_dd" class="easyui-dialog" style="width:380px;height:340px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<div class="usli_ftitle">修改内容</div>
	<form id='handover_update_form' method='post'>
		<div class="usli_fitem">
			<input id="up_id" type="hidden" name="class_">
		</div>
		<div class="usli_fitem">
			<label>班长：</label>
			<input id="up_squad" type="text" name="squad" readonly="readonly">
		</div>
		<div class="usli_fitem">
			<label>成员：</label>
			<input id="up_crew" type="text"  name="crew" readonly="readonly">
		</div>
		<div class="usli_fitem">
			<label>接班记录：</label>
			<textarea id='up_remarkAccept' name='remark_accept' rows="2" cols="37"></textarea>
		</div>
		<div class="usli_fitem">
			<label>交班记录：</label>
			<textarea id='up_remarkShift' name='remark_shift' rows="2" cols="37"></textarea>
		</div>
	</form>
</div>

	
	
