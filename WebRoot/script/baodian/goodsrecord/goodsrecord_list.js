var grli_dtfm = function(value){
   for(var i=0; i<grli_data.dts.length; i++){
	   if (grli_data.dts[i].id == value) return grli_data.dts[i].name;
   }
   return '-';
};
$('#grli_table').datagrid({
    url: 'goodsrecord_list_js.action?_=' + Math.random(),
    iconCls: "icon-edit",
    title: "设备记录",
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    singleSelect: true,//单选
    pagination: true,//分页
    toolbar: '#grli_toolbar',
    queryParams: {
    	//"page.name": {"id":"grli_name","type":"text"},
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
        {field: 'id', hidden: true},
        {field: 'gr.date', title: '时间', width: 25},
        {field: 'gr.name', title: '记录', width: 30},
        {field: 'gr.gd.id', title: '设备', width: 20,
        	formatter: function(value) {
        		for(var i=0; i<grli_data.gds.length; i++){
        			if (grli_data.gds[i].id == value) return grli_data.gds[i].name;
        		}
        		return value;
        	}},
        {field: 'gr.dtout.id', title:'来源', width: 20, align: "center", formatter: grli_dtfm},
        {field: 'gr.dtin.id', title:'目地', width: 20, align: "center", formatter: grli_dtfm},
        {field: 'gr.num', title:'数量', width: 20, align: "center"},
        {field: 'recorder', title:'记录人', width: 20, align: "center"}
    ]], onDblClickRow: function(index, data) {
    	grli_editGr(data);
    }
});

/**
 * 弹出添加记录窗
 */
function grli_newGr() {
	$('#grli_dlg').dialog('open').dialog('setTitle','添加记录');
	grli_row = {'gr.date': ' ', 'gr.name': null, 'gr.dtin.id': '0', 'gr.dtout.id': '0', 'gr.num': null};
	$('#grli_cc').combotree('clear');
	$("#grli_dlg div:first select:first").combobox('setValue', '0');
	$('#grli_fm').form('load', grli_row);
	grli_row.url = 'goodsrecord_save_js.action';
	grli_row.edit = 1;
}
var grli_row;//更改前row数据
/**
 * 弹出修改记录窗
 */
function grli_editGr(data) {
	if(data == null) grli_row = $('#grli_table').datagrid('getSelected');
	else grli_row = data;
	if (grli_row){
		$('#grli_dlg').dialog('open').dialog('setTitle','修改记录');
		$("#grli_dlg div:first select:first").combobox('setValue', '0');
		$('#grli_fm').form('load', grli_row);
		grli_row.url = 'goodsrecord_change_js.action?gr.id=' + grli_row.id;
		grli_row.edit = 2;
	} else {
		index_mess("请先选择！", 4);
	}
}
function grli_removeGr() {
	if(grli_row.edit == 2) {
		index_mess("正在修改，不能删除！", 4);
		return;
	}
	var row =  $('#grli_table').datagrid('getSelected');
	if (row){
		$.messager.confirm('删除记录', '确定要删除这条记录吗？', function(r){
			if (r){
				index_mess("删除中...", 0);
				$.getJSON("goodsrecord_remove_js.action?json=" + row.id + "&_=" + Math.random(), function(data) {
					if(data.status == 0) {
						index_mess(data.mess, 2);
						$('#grli_table').datagrid('reload');
					} else {
						index_mess(data.mess, 1);
						if(data.login == false) {
							index_login();
						}
					}
				});
			}
		});
	} else {
		index_mess("请先选择！", 4);
	}
}
/**
 * 添加或更改记录到数据库
 */
function grli_saveGr() {
	if(!$("#grli_fm").form('validate')) {
		return;
	}
	//0:gr.date 1:gr.name 2:gr.gd.id 3:gr.dtout.id 4:gr.dtin.id 5:gr.num
	var params = $("#grli_fm").serializeArray();
	if(params[3].value==0 || params[3].value=="") {
		params[3] = undefined;
	}
	if(params[4].value==0 || params[4].value=="") {
		params[4] = undefined;
	}
	index_mess("操作中...", 0);
	$.post(grli_row.url, params, function(data) {
		if(data.status == 0) {
			$('#grli_dlg').dialog('close');
			index_mess(data.mess, 2);
			$('#grli_table').datagrid('reload');
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}