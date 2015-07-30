var tasklist_data = {};

$('#tasklist_table').datagrid({
	url: 'task_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#tasklist_toolbar',//工具条
    queryParams: {
    	"page.title": {"id":"tasklist_title", "type":"text"},
    	"page.status": {"id":"tasklist_status", "type":"combobox"},
    	"page.time": {"id":"tasklist_time", "type":"combobox"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "title", title: "标题", width: 28, align: 'center'},
       //{field: "taskDate", title: "添加时间", width: 20, align: 'center'},
       {field: "doingDate", title: "提醒时间", width: 10, align: 'center'},
       {field: "depmNames", title: "执行部门", width: 11, align: 'center'},
       {field: "status", title: "状态", width: 3, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '未完';
    		   } else {
    			   return '完成';
    		   }
    	   }},
       {field: "user", title: "执行人员", width: 4, align: 'center'},
       {field: "doneDate", title: "执行时间", width: 10, align: 'center'},
	   {field: 'action', title: '操作', width: 5, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="tasklist_showrow(' + index + ')">查看</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="tasklist_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
	]], onLoadSuccess: function() {
		/*if(tasklist_data.reload) {
			tasklist_showrow(tasklist_data.rowId);
		}
		tasklist_data.reload = false;*/
		index_loadtask(true);//更新首页任务提示
	}
});
/**
 * 查看窗口
 */
$('#tasklist_add').dialog({
	closed: true,
	resizable: true,
	maximizable: true,
	//modal: true,锁定其余部分
	//title: "添加任务",
	width: 640,
	height: 500,
	buttons: [{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#tasklist_add').dialog('close');
		}
	}]
});

/**
 * 批量删除
 */
function tasklist_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#tasklist_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除的任务！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个任务吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("task_removeList_js.action?json=" + ids.join('-') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#tasklist_table').datagrid('reload');
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
 * @param status true:设为完成 false:取消完成
 */
function tasklist_editrow(status, ids, reload) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#tasklist_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要更新的任务！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	index_mess("更新中...", 0);
	$.getJSON('task_changeStatus_js.action?json=' +
			(status? '2': '1') + 'A' + ids.join('-') + '&_=' + Math.random(), function(data) {
		if(data.status == 0) {
			/*if(reload) {
				tasklist_data.reload = true;
			}*/
			$('#tasklist_add').dialog('close');
			$('#tasklist_table').datagrid('reload');
    		index_mess(data.mess, 2);
		} else {
			index_mess(data.mess, 1);
		}
	});
}
/**
 * 设置状态为完成
 */
function tasklist_changeStatus() {
	tasklist_editrow(true, [tasklist_data.id], true);
}
/**
 * 设置提醒时间
 */
function tasklist_changeDoingDate() {
	var date = $('#tasklist_adddate').datetimebox('getValue');
	if(date == tasklist_data.doingDate) {
		index_mess('时间未更改！', 4);
		return;
	}
	if(! /^(\d{4})-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/.test(date)) {
		index_mess('时间输入有误！', 3);
		return;
	}
	index_mess('更新中...', 0);
	$.getJSON('task_changeDoingDate_js.action?json=' + tasklist_data.id + 'A' + date, function(data) {
		if(data.status == 0) {
			//tasklist_data.reload = true;
			$('#tasklist_add').dialog('close');
			$('#tasklist_table').datagrid('reload');
    		index_mess(data.mess, 2);
		} else {
			index_mess(data.mess, 1);
		}
	});
}
/**
 * 打开查看窗口
 */
function tasklist_showrow(rowId) {
	tasklist_data.rowId = rowId;
	var row = $('#tasklist_table').datagrid('getRows')[rowId];
	tasklist_data.id = row.id;
	tasklist_data.doingDate = row.doingDate;
	$('#tasklist_add').dialog('open').dialog('setTitle','查看任务 - ' + row.title);
	$('#tasklist_addtitle').html('标题：<strong>' + row.title + '</strong>');
	tasklist_content.html(row.content);
	var str = '<div style="padding-top:8px;">任务时间：' + row.taskDate + '</div>' +
		'<div style="padding-top:8px;">提醒时间：' + row.doingDate + '</div>' +
		'<div style="padding-top:8px;">执行部门：' + row.depmNames + '</div>';
	if(row.status == 1) {//未完成
		$('#tasklist_adddata').html(str + '<div style="padding-top:8px;">完成状态：未完成</div>' +
			'<div style="padding-top:8px;"><input id="tasklist_adddate">' +
				'&emsp;<a id="tasklist_addbt1" href="javascript:;" onclick="tasklist_changeDoingDate()">重设提醒时间</a>' +
				'&emsp;<a id="tasklist_addbt2" href="javascript:;" onclick="tasklist_changeStatus()">设置为完成</a></div>');
	    $('#tasklist_adddate').datetimebox({
	    	width: 140,
	        value: row.doingDate,
	    });
	    $('#tasklist_addbt1').linkbutton();
		$('#tasklist_addbt2').linkbutton({
			iconCls: 'icon-edit'
		});
	} else {
		$('#tasklist_adddata').html(str + '<div style="padding-top:8px;">完成状态：已完成</div>' +
			'<div style="padding-top:8px;">执行人员：' + row.user + '</div>' +
			'<div style="padding-top:8px;">执行部门：' + row.department + '</div>' +
			'<div style="padding-top:8px;">执行时间：' + row.doneDate + '</div>');
	}
}
/**
 * 清除查询输入
 */
function tasklist_clearData() {
	$('#tasklist_title').val('');
	$('#tasklist_status').combobox('setValue', '-1');
	$('#tasklist_time').combobox('setValue', '-1');
}
//防止在重新载入页面的时候，无法更新编辑框
KindEditor.remove("#tasklist_addcontent");
//内容编辑框
var tasklist_content = KindEditor.create("#tasklist_addcontent", {
	width: "99%",
	resizeType: 1,
	cssData: 'border-color:#3366CC',
	items: [
	    'source', '|', 'fontname', 'fontsize', '|',
	    'forecolor', 'hilitecolor', 'bold', 'italic',
	    'underline', 'removeformat', '|', 'fullscreen', 'about']
});