taskset_data.week = [0, '一', '二', '三', '四', '五', '六', '日'];//用来保存星期
taskset_data.change = true;//是否打开修改页面
taskset_data.id = 0;//要更新的任务id

$('#taskset_table').datagrid({
	url: 'task_set_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#taskset_toolbar',//工具条
    queryParams: {
    	"page.title": {"id":"taskset_title","type":"text"},
    	"page.status": {"id":"taskset_status","type":"combobox"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "title", title: "标题", width: 80, align: 'center'},
       {field: "type", title: "频率", width: 50, align: 'center',
    	   formatter: function(value, row ,index) {
    		   // 任务类型，提醒时间放到begin上面
    		   // 1-每天，例每天16:30(type=1 begin='16:30')
    		   // 2-每星期，把星期放到data上，例每星期一、二、三 (type=2 data="1-2-3")
    		   // 3-日期范围内，例10月1日~10月7日(type=3 begin='10-01 16:30' end="10-07")
    		   switch(value) {
    		   		case 1: return '每天';
    		   		case 2:
    		   			var week = row.data.split("-");
    		   			var str = '每星期';
    		   			for(var i=0; i<week.length; i++) {
    		   				str += taskset_data.week[week[i]];
    		   			}
    		   			return str;
    		   		case 3:
    		   			return date2String(row.begin) + "~" + date2String(row.end);
    		   		default: return value;
    		   }
    	   }},
       {field: "time", title: "提醒时间", width: 20, align: 'center',
    	   formatter: function(value, row ,index) {
    		   return row.begin.substring(11,16);
    	   }},
       {field: "depmNames", title: "执行部门", width: 50, align: 'center'},
       {field: "user", title: "添加人员", width: 25, align: 'center'},
       {field: "date", title: "时间", width: 30, align: 'center',
    	   formatter: function(value, row ,index) {
    		   return value.substring(0,10);
    	   }},
       {field: "status", title: "状态", width: 20, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '运行';
    		   } else {
    			   return '停止';
    		   }
    	   }},
	   {field: 'action', title: '操作', width: 30, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="taskset_editrow(' + index + ')">编辑</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="taskset_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
	]]
});
/**
 * 添加窗口
 */
$('#taskset_add').dialog({
	closed: true,
	resizable: true,
	maximizable: true,
	//modal: true,锁定其余部分
	//title: "添加任务",
	width: 640,
	height: 500,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			//标题
			var param = $('#taskset_addtitle').val();
			if(param.length == 0) {
				index_mess("请输入任务标题！", 4);
				$('#taskset_addtitle').focus();
				return false;
			}
			if(param.length > 250) {
				index_mess("字数超出 " + (param.length - 250) + " 个！限制 250 个！", 4);
				$('#taskset_addtitle').focus();
				return false;
			}
			params['task.title'] = param;
			//内容
			var contentNum = taskset_content.count();
			if(contentNum == 0) {
				index_mess("请输入任务内容！", 4);
				taskset_content.focus();
				return false;
			}
			if(contentNum > 21845) {
				index_mess("字数超出 " + (contentNum - 21845) + " 个！限制 21845 个！", 4);
				taskset_content.focus();
				return false;
			}
			params['task.content'] = taskset_content.html();
			//频率
			param = $('#taskset_addtype').combobox('getValue');
			params['task.type'] = param;
			switch(parseInt(param)) {
				case 2://每星期
					params['task.data'] = $('#taskset_adddata').combobox('getValues').join('-');
					if(params['task.data'] == '') {
						index_mess('请先选择星期！', 4);
						return false;
					}
					params['task.begin'] = date2String2(new Date()) + ' ' +
						$('#taskset_addtime').timespinner('getValue') + ':00';
					params['task.end'] = date2String2(new Date());
					break;
				case 3://时间范围
					params['task.begin'] = $('#taskset_addbegin').datebox('getValue');
					if(params['task.begin'] == '') {
						index_mess('请先选择开始日期！', 4);
						return false;
					}
					params['task.begin'] += ' ' + $('#taskset_addtime').timespinner('getValue') + ':00';
					params['task.end'] = $('#taskset_addend').datebox('getValue');
					if(params['task.end'] == '') {
						index_mess('请先选择结束日期！', 4);
						return false;
					}
					params['task.data'] = '';
					break;
				default:
					//每天
					params['task.begin'] = date2String2(new Date()) + ' ' +
						$('#taskset_addtime').timespinner('getValue') + ':00';
					params['task.data'] = '';
					params['task.end'] = date2String2(new Date());
			}
			params['task.status'] = $('#taskset_addstatus input:first').is(':checked')? 1: 2;
			params['json'] = $('#taskset_adddepm').combotree('getValues').join('-');
			if(params['json'] == '') {
				index_mess('请先选择执行部门！', 4);
				return false;
			}
			var url = '';
			if(taskset_data.change) {
				params['task.id'] = taskset_data.id;
				url = "task_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "task_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				//taskset_changeValues('', '', 'basket', 1, '', 'ffffff');
				$('#taskset_add').dialog('close');
				if(taskset_data.change) {
					$('#taskset_table').datagrid('reload');
				} else {
					$('#taskset_table').datagrid('load');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#taskset_add').dialog('close');
		}
	}], onOpen: function() {
		if(!taskset_data.change) {
			taskset_changeValues({
				'title':'', 'content':'', 'status':1,
				'type': 1, 'begin':'', 'end':'',
				'data': '', 'depmIds':[]
			});
		}
		$('#taskset_addname').focus();
	},
});
/**
 * 添加频率
 */
$('#taskset_addtype').combobox({
	width: 102,
	panelHeight: 80,
	editable: false,
	onSelect: function(row) {
		taskset_setaddtype(row.value);
	}
});
/**
 * 添加部门树
 */
$('#taskset_adddepm').combotree({
	width: 200,
	multiple: true,
	cascadeCheck: false,
	data: index_changeTree(taskset_data.depms),
});
/**
 * 设置添加频率
 */
function taskset_setaddtype(type) {
	switch(parseInt(type)) {
		case 2://每星期
			$('#taskset_addbegin').datebox('disable');
			$('#taskset_addend').datebox('disable');
			$('#taskset_adddata').combobox('enable');
			break;
		case 3://时间范围
			$('#taskset_addbegin').datebox('enable');
			$('#taskset_addend').datebox('enable');
			$('#taskset_adddata').combobox('disable');
			break;
		default://每天
			$('#taskset_addbegin').datebox('disable');
			$('#taskset_addend').datebox('disable');
			$('#taskset_adddata').combobox('disable');
	}
}
/**
 * 更新添加页面的值
 */
function taskset_changeValues(row) {
	if(taskset_data.change) {
		$('#taskset_adduser').html(row.user + '<' + row.department + '>于 ' +
				row.date + ' 添加');
		$('#taskset_addtime').timespinner('setValue', row.begin.substring(11,16));
		$('#taskset_adddata').combobox('setValues', row.data.split("-"));
	} else {
		$('#taskset_adduser').html('请录入数据');
		$('#taskset_addtime').timespinner('setValue', 0);
		$('#taskset_adddata').combobox('setValues', []);
	}
	$('#taskset_addtitle').val(row.title);
	taskset_content.html(row.content);
	$('#taskset_addtype').combobox('setValue', row.type);
	if(row.status == 1) {
		$('#taskset_addstatus input:first').attr("checked",'true');
	} else {
		$('#taskset_addstatus input:eq(1)').attr("checked",'true');
	}
	$('#taskset_addbegin').datebox('setValue', row.begin.substring(0,10));
	$('#taskset_addend').datebox('setValue', row.end.substring(0,10));
	$('#taskset_adddepm').combotree('setValues', row.depmIds);
	taskset_setaddtype(row.type);
}
/**
 * 批量删除
 */
function taskset_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#taskset_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除任务！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个任务吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("task_remove_js.action?json=" + ids.join('-') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#taskset_table').datagrid('reload');
		    		index_mess(data.mess, 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    		}
	    	});
		}
	});
}
/**
 * 更新任务
 */
function taskset_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#taskset_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个任务！", 4);
			return;
		}
	} else {
		row = $('#taskset_table').datagrid('getRows')[rowId];
	}
	taskset_data.id = row.id;
	taskset_data.change = true;
	$('#taskset_add').dialog('open').dialog('setTitle','更新任务 - ' + row.title);
	taskset_changeValues(row);
}
//防止在重新载入页面的时候，无法更新编辑框
KindEditor.remove("#taskset_addcontent");
//内容编辑框
var taskset_content = KindEditor.create("#taskset_addcontent", {
	width: "99%",
	resizeType: 1,
	items: [
	    'source', 'preview', '|', 'undo', 'redo', '|',
		'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
		'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
		'insertunorderedlist', '|', 'link', 'fullscreen', 'about']
});

