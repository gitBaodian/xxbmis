<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>
<script type="text/javascript">
$(function(){
	$('#usli_ak_zb').datagrid({
	    url: "notice_ak_zb_list.action",
	    iconCls: "icon-edit",
	    title: "请假申请在办",
	    striped: true,//条纹
	    fitColumns: true,//自动调整单元格宽度
	    rownumbers: true,//显示行号
	    singleSelect: true,//单选
	    pagination: true,//分页
	    paramsName: {
	    	page: "page.page",
	    	num: "page.num"
	    },toolbar: '#usli_toolbar_ak_zb',
	    columns: [[
	        {field: 'id', title: 'ID', width: 20},
	        {field: 'startDate', title: '起始时间', width: 20},
	        {field: 'endDate', title: '结束时间', width: 20},
	        {field: 'quot', title: '请假天数', width: 20},
	        {field: 'applicationTime',title:'申请时间', width: 30},
	        {field: 'dsh',title:'状态', width: 30},
	       /*  {field: 'action', title: '操作', width:10,
				formatter: function(value,row,index){
					return "<a href='javascript:;' onclick='remove_ak_zb(" + row.id + ")'>删除</a>";
				}
			} */
	    ]]
	});
});


function remove_ak_zb() {
		var row = $('#usli_ak_zb').datagrid('getSelected');
		if(row.dsh == "已生效"){
			alert("该申请已生效，无法删除！！");
			return;
		}
		$.messager.confirm('Confirm','你是否要删除该请假申请?',function(r){  
		    if (r){  
		        $.post("notice_remove_ak_zb.action", {"id":row.id}, function(data) {
					if(data.status == 0) {
						alert(data.mess);
						$('#usli_ak_zb').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json"); 
		    }  
			});  
}
</script>
<div id="usli_toolbar_ak_zb" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="ask_leave_dd()">请假申请</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove_ak_zb()">删除申请</a>
	</div>
</div>

<table id="usli_ak_zb"></table>

