$(function(){
	$('#usli_inspection_manager').datagrid({
	    url: "inspection_inspection_show.action",
	    iconCls: "icon-edit",
	    title: "巡检记录",
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
	        {field: 'insp_createtime', title: '记录时间', width: 20},
	        {field: 'username', title: '记录人', width: 10},
	        {field: 'inspTime01', title: '4楼机房巡查时间', width: 20},
	        {field: 'inspTime02',title:'网络室巡查时间', width: 20},
	        {field: 'inspTime03',title:'UPS室巡查时间', width: 20},
	    ]] 
	});
});


function inspection_update() {
	var row = $('#usli_inspection_manager').datagrid('getSelected');
	if(row){
		index_openTab('查看巡检记录','inspection_get.action?insp_id='+row.insp_id+'&');
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
}

function inspection_del() {
	var row = $('#usli_inspection_manager').datagrid('getSelected');
	if(row){
		$.messager.confirm('删除确定','你是否要删除此巡检记录?',function(r){
		    if (r){  
		       $.post('inspection_inspection_del.action?json='+Math.random(),{insp_id:row.insp_id},function(data){
					if (data.status == 0){    //有权限
						alert(data.mess);
						$('#usli_inspection_manager').datagrid('reload');
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

