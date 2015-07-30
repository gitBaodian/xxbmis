/**
 * 打开页面
 * @param title 标题
 * @param href 地址
 * @param load 是否加载同名javascript脚本
 */
function index_openTab(title, href, load) {
	index_mess("载入 - " + title, 0);
	if(href.lastIndexOf("_rf.action") != -1) {
		$.get(href + "?_=" + Math.random(), function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false) {
					index_login();
				}
				return;
			}
			index_mess("刷新内存成功！", 2);
		}, "json");
		return;
	} else if(href.lastIndexOf("_rd.action") != -1) {
		window.open(href);
		index_mess("另一页面打开中...", 2);
		return;
	}
	if (index_data.tabObj.tabs("exists", title)) {
		index_mess("已经打开", 2);
		index_data.tabObj.tabs("select", title);
	} else {
		index_createTab(title, href, load);
	}
}
/**
 * 更新页面
 * newtitle不等于title时，将关闭后重新建立，否则就是刷新原来已打开的
 * 页面不存在，将新建一个
 */
function index_updateTab(title, href, load, newtitle) {
	index_mess("载入 - " + title, 0);
	if(newtitle == null)
		newtitle = title;
	if (index_data.tabObj.tabs("exists", title)) {
		if(newtitle != title && index_data.tabObj.tabs("exists", newtitle)) {
			//更新后的如果存在，要关闭
			index_data.tabObj.tabs("close", newtitle);
		}
		index_data.tabObj.tabs("select", title);
		index_createTab(title, href, load, newtitle);
	} else {
		index_createTab(newtitle, href, load);
	}
}
/**
 * 展示页面
 * ajax取数据时可能取到html页面，也可能是json出错提示，所以要特殊处理
 */
function index_createTab(title, href, load, newtitle) {
	$.get(href + "?_=" + Math.random(), function(data) {
		if(data.substring(0,8).indexOf("{") != -1) {
			data = $.parseJSON(data);
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false)index_login();
				return;
			}
		}
		var params = {
			content: data,
			tools:[{
		    	iconCls: 'icon-mini-refresh',
		    	handler: function() {
		    		index_updateTab(newtitle==null? title: newtitle, href, load);
		}}]};
		if(newtitle == null) {
			index_data.tabObj.tabs("add", {title: title, closable: true});
		} else {
			params.title = newtitle;
		}
		index_data.tabObj.tabs('update', {
			tab: index_data.tabObj.tabs('getTab', title),
			options: params
		});
		if(load) {
			var url = href.split(".")[0];
			$.ajax({
				url: "script/baodian/" + url.split("_")[0] + "/" + url + ".js",
				//cache: true,//?_20121224
				dataType: "script"
			});
		}
		index_mess("载入成功", 2);
	});
}
/**
 * 关闭页面
 */
function index_closeTab(title) {
	$("#mainbody").tabs("close", title);
}
/**
 * 菜单样式
 */
function index_addDiyDom(treeId, treeNode) {
	var spaceWidth = 10;
	var switchObj = $("#" + treeNode.tId + "_switch");
	var icoObj = $("#" + treeNode.tId + "_ico");
	switchObj.remove();
	icoObj.before(switchObj);
	if (treeNode.level > 0) {
		var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
		switchObj.before(spaceStr);
	}
}
$(function() {
	//初始化数据
	index_data.setting = {
		view: {
			showLine: false,
			showIcon: false,
			selectedMulti: false,
			dblClickExpand: false,
			addDiyDom: index_addDiyDom
		}, data: {
			simpleData : {
				enable : true
			}
		}, callback: {
			beforeClick: function(treeId, treeNode, clickFlag) {
				//点击菜单前先的判断，如果是父节点，就展开或折叠树，否则响应点击
				if(treeNode.isParent) {
					index_data.treeObj.expandNode(treeNode);
					return false;
				} else {
					return true;
				}
			}, onClick: function(event, treeId, treeNode) {
				index_openTab(treeNode.name, treeNode.action, true);
			}
		}
	};
	//菜单
	var index_tree_ul = $("#index_treeDemo");
	index_data.treeObj = $.fn.zTree.init(index_tree_ul, index_data.setting, index_data.index.menu);
	index_tree_ul.hover(function () {
		index_tree_ul.toggleClass("showIcon");
	});
	//首页布局
	index_data.tabObj = $("#mainbody");
	loadIndex();
	//按下enter键登录
	$("#j_username").keydown(function(e) {
		if(e.keyCode==13) index_fmlg();
	});
	$("#j_password").keydown(function(e) {
		if(e.keyCode==13) index_fmlg();
	});
	//登陆框
	$('#index_dd').dialog({
		closed: true,
		title: '&nbsp;登录系统',
		buttons:[{
			text:'登录',
			iconCls:'icon-ok',
			handler: index_fmlg,
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#index_dd').dialog('close');
			}
		}]
	});
	//定时访问，防止掉线
	setInterval(function() {
		index_loadtask(true);
		//$('#index_pgrid').panel('expand').panel('refresh');
	}, 1080000);
});
/**
 * 首页布局
 * @param reload true表示重新加载，false表示展示
 * @param remote true表示从远程加载，false表示读取本地
 */
function loadIndex(reload) {
	index_mess("载入中...", 0);
	if(index_data.user.id == "") {
		$("#index_user").html("未登录&nbsp;");
	} else {
		$("#index_user").html("欢迎 " + index_data.user.name + " (" + index_data.user.depm + ")&ensp;");
	}
	if(reload) {
		$.each(index_data.panel, function(k, v) {
			$('#index_pp').portal('remove', v);
		});
		//重新读取数据
		$.ajax({
			async: false,
			url: "util_index_js.action?_=" + Math.random(),
			dataType: "JSON",
			success: function(data) {
				index_data.index = data;
			}
		});
	} else {
		$('#index_pp').portal({
			//指定每一列的大小，0为自动调整
			columns: [150, 0, 250],
			//设置自动调整列的最小宽度
			minwidth : 360,
		});
	}
	index_data.panel = [];
	//当值人员
	index_duty_officer(true, false);
	//快捷窗口
	index_loadwork();
	//运行记录
	index_loadwrecords(true, true);
	//每日记事
	index_loadNotepad(true, false);
	//交班记录
	index_handover(true, false);
	//新闻论坛
	index_loadnews(true, false);
	//值班人员
	index_calendar();
}
/**
 * 当值人员
 */
function index_duty_officer(reload, remote){
	if(reload) {
		index_addPanel('<div class="index_work duty_officer"></div>', {
			title: '当值人员',
			height: 90,
			content:
				'<div id="duty_officer" class="duty_officer"></div><div id="duty_officer_2" class="duty_officer_2"></div>'
		}, 0);
	}
	if(remote) {
		$("#index_pgrid5").html('<img src="script/easyui/themes/default/images/panel_loading.gif">Loading...');
		$.ajax({
			async: false,
			url: "handover_duty_officer.action?_=" + Math.random(),
			dataType: "JSON",
			success: index_make_duty_officer
		});
	} else {
		index_make_duty_officer(index_data.index.staff);
	}
}

/**
 * 将组件添加进页面
 * @param panelDiv 组件div
 * @param panelParam panel组件参数
 * @param index 列号，从0开始
 */
function index_addPanel(panelDiv, panelParam, index) {
	var pnl = $(panelDiv).appendTo('body');
	//公共的参数写在这里
	panelParam.collapsible = true;//可展开
	pnl.panel(panelParam);
	index_data.panel.push(pnl);
	$('#index_pp').portal('add', {
		panel: pnl,
		columnIndex: index
	});
}
/**
 * 处理数据-当值人员
 */
function index_make_duty_officer(data) {
	var result = "";
	result = result + '<div class="index_duty_officer">';
	for(var i=0;i<data.staff.length;i++){
		result =  result + '<span>';
		result =  result + data.staff[i] + '</span><br>';
	}
	result = result.substring(0, result.length-4) + "</div>";
	
	var result_2 = "";
	result_2 = result_2 + '<div class="index_duty_officer_2">';
	for(var i=0;i<data.staff_2.length;i++){
		result_2 =  result_2 + '<span>';
		result_2 =  result_2 + data.staff_2[i] + '</span><br>';
	}
	result_2 = result_2.substring(0, result_2.length-4) + "</div>";
	
	$("#duty_officer").html(result);
	$("#duty_officer_2").html(result_2);
}
/**
 * 快捷窗口展示
 */
function index_loadwork() {
	index_addPanel('<div class="index_work t-list"></div>', {
		title: '快捷窗口',
		height: 186,
		content:
			'<div><a href="javascript:;" title="下班" class="easyui-linkbutton" onclick="shift_dd()">交班</a></div>' +
			'<div><a href="javascript:;" title="上班" class="easyui-linkbutton" onclick="accept_dd()">接班</a></div>' +
			'<div><a href="javascript:;" class="easyui-linkbutton" onclick="ask_leave_dd()">请假</a></div>' +
			'<div><a href="javascript:;" class="easyui-linkbutton" onclick="apl_transfer_dd()">调班</a></div>'
	}, 0);
	if(index_data.user.id != "") {
		index_addPanel('<div id="index_pgrid2" class="index_work"></div>', {
			title: '个人工作',
		    height: 170,
		    tools:[{
		    	iconCls: 'icon-reload',
				handler: function() {
					$('#index_pgrid2').panel('expand');
					index_notice(true);
				}
			}]
		}, 0);
		index_notice();
	}
}
/**
 * 个人调班申请信息
 */
function index_notice(remote) {
	if(remote) {
		$("#index_pgrid2").html('<img src="script/easyui/themes/default/images/panel_loading.gif">Loading...');
		$.getJSON("notice_getnotice.action?_=" + Math.random(), function(data) {
			index_makenotice(data, remote);
		});
	} else {
		index_makenotice(index_data.index.notice);
	}
}
/**
 * 处理数据-个人调班申请信息
 */
function index_makenotice(data, remote) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		$("#index_pgrid2").html(data.mess);
		if(data.login == false) {
			index_login();
		}
		return;
	}
	$("#index_pgrid2").html('<div class="t-list">' +
		'<div><a href="javascript:;" onclick="index_openTab(\'调班申请在办\',\'notice_tf_zb.action\')">调班汇总 </a> ' +
			data.tf_zb +'件</div>' +
		'<div><a href="javascript:;" onclick="index_openTab(\'请假申请在办\',\'notice_ak_zb.action\')">请假汇总</a> ' +
			data.ak_zb + '件</div></div><div class="t-list"></div>');
	index_loadtask(remote);
}
/**
 * 任务提示
 */
function index_loadtask(remote) {
	if(remote) {
		$.getJSON('task_need_js.action?_=' + Math.random(), index_maketask);
	} else {
		index_maketask(index_data.index.task);
	}
}
/**
 * 处理数据-任务提示
 */
function index_maketask(data) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		if(data.login == false) {
			index_login();
		}
		return;
	}
	//将定时任务删除
	if(index_data.tomorrowTask) {//明天
		$.each(index_data.tomorrowTask, function(k, v) {
			clearTimeout(v.timeOut);
		});
	}
	if(index_data.todayTask) {//今天
		$.each(index_data.todayTask, function(k, v) {
			clearTimeout(v.timeOut);
		});
	}
	//初始化
	index_data.tomorrowTask = [];//明天任务
	index_data.todayTask = [];//今天现在至凌晨任务
	index_data.todayLastTask = [];//今天凌晨至现在任务
	index_data.lastTask = [];//今天未完成
	//初始化时间,位置在baodian/util/date.js
	initDate(data.date);
	//按时间分类
	//Today[] 0 当前 1凌晨 4明天凌晨
	$.each(data.task, function(k, v) {
		var doingDate = splitDate(v.doingDate);
		var diff = doingDate - Today[0];
		v.diff = diff;
		if(diff <= 0) {//今天前未完成
			index_data.lastTask.push(v);
			diff = doingDate - Today[1];
			if(diff >= 0) {//今天内未完成
				index_data.todayLastTask.push(v);
			}
		} else {
			diff = doingDate - Today[4];
			if(diff < 0) {//今天
				index_data.todayTask.push(v);
			} else {//明天以后
				index_data.tomorrowTask.push(v);
			}
		}
	});
	//加入定时任务
	$.each(index_data.tomorrowTask, function(k, v) {//明天
		if(v.diff < 1800000) {//因为有自动刷新，超过半小时的任务就不添加进定时器
			v.timeOut = setTimeout(function() {
				index_setTitle(1, '(有新任务)');
			}, v.diff);
		}
	});
	$.each(index_data.todayTask, function(k, v) {//今天
		if(v.diff < 1800000) {
			v.timeOut = setTimeout(function() {
				index_setTitle(1, '(有新任务)');
			}, v.diff);
		}
	});
	//设置标题
	if(index_data.lastTask.length != 0) {
		index_setTitle(1, '(有' + index_data.lastTask.length + '个未完成的任务)');
	} else {
		index_setTitle();
	}
	//添加进页面
	$("#index_pgrid2 div.t-list:eq(1)").html(
		'<div><a href="javascript:;" onclick="index_openTask(2)">明天任务</a> ' +
			index_data.tomorrowTask.length + ' 件</div>' +
		'<div><a href="javascript:;" onclick="index_openTask(3)">今天任务</a> ' +
			(index_data.todayTask.length + index_data.todayLastTask.length) + ' 件</div>' +
		'<div><a href="javascript:;" onclick="index_openTask(4)">当前任务</a> ' +
			index_data.lastTask.length + ' 件</div>');
	index_mess("载入成功", 2);
}
/**
 * 打开任务列表
 * 2-明天 3-今天 4-未完成
 */
function index_openTask(time) {
	if(! index_data.tabObj.tabs('exists', '任务列表')) {
		index_openTab('任务列表', 'task_list.action', true);
		setTimeout(function() {
			index_openTaskPage(time);
		}, 2000);
	} else {
		index_data.tabObj.tabs('select', '任务列表');
		tasklist_clearData();
		index_openTaskPage(time);
	}
}
/**
 * 打开任务列表页面
 */
function index_openTaskPage(time) {
	$('#tasklist_status').combobox('setValue', 1);
	$('#tasklist_time').combobox('setValue', time);
	$('#tasklist_table').datagrid('load');
}
/**
 * 更改标题...
 * st1加在左边 st2加在右边 st3两边都加
 */
function index_setTitle(st, lstr, rstr) {
	switch(st) {
		case 1: rstr = '';break;
		case 2: lstr = '';break;
		case 3: break;
		default : lstr = '';rstr = '';
	}
	document.title = lstr + '信息部MIS' + rstr;
}
/**
 * 运行记录展示
 */
function index_loadwrecords(reload, remote) {
	if(reload) {
		index_addPanel('<div id="index_pgrid" class="index_pgrid"></div>', {
			title: '运行日志',
			height: 169,
			tools:[{
				iconCls: 'icon-reload',
				handler: function() {
					$('#index_pgrid').panel('expand');
					index_loadwrecords(false, true);
				}
			}]
		}, 1);
	}
	if(remote) {
		$("#index_pgrid").html('<img src="script/easyui/themes/default/images/panel_loading.gif">Loading...');
		$.getJSON('WRecord_list_for_index.action?_=' + Math.random(), index_makewrecords);
	} else {
		index_makewrecords(index_data.index.wrecords);
	}
}
/**
 * 处理数据-运行记录展示
 */
function index_makewrecords(data) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		$("#index_pgrid").html(data.mess);
		if(data.login == false) {
			index_login();
		}
		return;
	}
	initDate(new Date());
	var str = '', dateTime;
	$.each(data.rows, function(k, v) {
		dateTime = splitDate(v.time);
		v.dtime = exportDate(dateTime, v.time);
		v.time = dateToString(dateTime, v.time.substring(11,16));
		str = str + '<div class="index_news" title="' + v.detail + '\n' + v.time +
			'"><span class="idnews_date">' + v.dtime + 
			'</span><span class="idnews_content">' + v.detail + '</span></div>';
	});
	$("#index_pgrid").html(str);
}
/**
 * 每日记事展示
 */
function index_loadNotepad(reload, remote) {
	if(reload) {
		index_addPanel('<div id="index_pgrid6" class="index_pgrid"></div>', {
			title: '每日记事',
		    height: 169,
		    tools:[{
		    	iconCls: 'icon-reload',
				handler: function() {
					$('#index_pgrid6').panel('expand');
					index_loadNotepad(false, true);
				}
			}]
		}, 1);
	}
	if(remote) {
		$("#index_pgrid6").html('<img src="script/easyui/themes/default/images/panel_loading.gif">Loading...');
		$.getJSON('notepad_index_js.action?_=' + Math.random(), index_makeNotepad);
	} else {
		index_makeNotepad(index_data.index.notepad);
	}
}
/**
 * 处理数据-每日记事展示
 */
function index_makeNotepad(data) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		$("#index_pgrid6").html(data.mess);
		if(data.login == false) {
			index_login();
		}
		return;
	}
	initDate(new Date());
	var str = '', dateTime;
	$.each(data, function(k, v) {
		dateTime = splitDate(v.date);
		v.dtime = exportDate(dateTime, v.date);
		v.date = dateToString(dateTime, v.date.substring(11,16));
		str = str + '<div class="index_news" title="' + v.name + '\n' + v.date +
				'"><span class="idnews_date">' + v.dtime +
				'</span><span class="idnews_content" ' + (v.top==1?'style="font-weight:bold;"':'') + '>' + v.name + '</span></div>';
	});
	$("#index_pgrid6").html(str);
}
/**
 * 交接记录展示
 */
function index_handover(reload, remote) {
	if(reload) {
		index_addPanel('<div id="index_pgrid4" class="index_pgrid"></div>', {
			title: '交接记录',
		    height: 147,
		    tools:[{
		    	iconCls: 'icon-reload',
				handler: function() {
					$('#index_pgrid4').panel('expand');
					index_handover(false, true);
				}
			}]
		}, 1);
	}
	if(remote) {
		$("#index_pgrid4").html('<img src="script/easyui/themes/default/images/panel_loading.gif">Loading...');
		$.getJSON('handover_index_handover.action?_=' + Math.random(), index_makeHandover);
	} else {
		index_makeHandover(index_data.index.handover);
	}
}
/**
 * 数据处理-交接记录展示
 */
function index_makeHandover(data) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		$("#index_pgrid4").html(data.mess);
		if(data.login == false) {
			index_login();
		}
		return;
	}
	initDate(new Date());
	var str = '', dateTime;
	$.each(data, function(k, v) {
		if(k > 4) return false;
		dateTime = splitDate(v.handover_time);
		v.dtime = exportDate(dateTime, v.handover_time);
		v.handover_time = dateToString(dateTime, v.handover_time.substring(11,16));
		str = str + '<div class="index_news" title="' + v.remark + '\n' + v.handover_time +
			'"><span class="idnews_date">' + v.dtime + 
			'</span><span class="idnews_content">'+v.handover_zhiban + ' ' + v.handover_status + ' ' + v.handover_people + '   ' + v.remark + '</span></div>';
	});
	$("#index_pgrid4").html(str);
}
/**
 * 新闻内容展示
 */
function index_loadnews(reload, remote) {
	if(reload) {
		index_addPanel('<div id="index_pgrid3" class="index_pgrid"></div>', {
			title: '交流平台',
			height: 147,
		    tools:[{
		    	iconCls: 'icon-add',
				handler: function() {
					window.open("news_add_rd.action");
				}
		    },{
		    	iconCls: 'icon-reload',
				handler: function() {
					$('#index_pgrid3').panel('expand');
					index_loadnews(false, true);
				}
			}]
		}, 1);
	}
	if(remote) {
		$("#index_pgrid3").html('<img src="script/easyui/themes/default/images/panel_loading.gif">Loading...');
		$.getJSON('newsbase_index_js.action?_=' + Math.random(), index_makeNews);
	} else {
		index_makeNews(index_data.index.newbase);
	}
}
/**
 * 数据处理-新闻内容展示
 */
function index_makeNews(data) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		$("#index_pgrid3").html(data.mess);
		if(data.login == false) {
			index_login();
		}
		return;
	}
	initDate(new Date());
	var str = '', dateTime;
	$.each(data, function(k, v) {
		dateTime = splitDate(v.pbtime);
		v.dtime = exportDate(dateTime, v.pbtime);
		v.pbtime = dateToString(dateTime, v.pbtime.substring(11,16));
		if(v.rnum == 0) {
			v.rtime = '';
		} else {
			v.rtime = ' (' + v.rnum + '回复，最后于' + exportDate(splitDate(v.rptime), v.rptime) + ')';
		}
		str = str + '<div class="index_news" title="'+ v.title + v.rtime + '\n' + v.pbtime + 
				'"><span class="idnews_date">' + v.dtime +
				'</span><a target="_blank" href="forum_list_rd.action?page.nbId=' +
					v.id + '">' + v.title + '</a><span style="color:#333">' + v.rtime + '</span></div>';
	});
	$("#index_pgrid3").html(str);
}
/**
 * 日历值班信息
 */
function index_calendar() {
	index_addPanel('<div class="index_cald"></div>', {
		title: '日历',
		height: 235,
		iconCls: "icon-search",
		content: '<div id="index_cc" class="easyui-calendar"></div>'
	}, 2);
	index_addPanel('<div class="index_clda"></div>', {
		title: '值班表',
		height: 190,
		content:
			'<div id="index_duty" class="index_duty"></div>' +
			'<div id="index_dtus" class="index_dtus"></div>'
	}, 2);
	index_usonduty(index_data.index.duty);
	$('#index_cc').calendar({
		current: new Date(),
		onSelect: function(date) {
			$.getJSON("user_onduty_js.action?date="+
					date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate() +
					"&_=" + Math.random(), index_usonduty);
		}
	});
}
/**
 * 读取值班人员
 */
function index_usonduty(data) {
	if(data.status == 1) {
		index_mess(data.mess, 3);
		if(data.login == false)index_login();
		return;
	}
	var onduty = "", result = "";
	$.each(data, function(k, v) {
		if(k%2 == 0) {
			onduty = onduty + '<div class="index_dta">';
			result = result + '<div class="index_dta">';
		} else {
			onduty = onduty + '<div class="index_dtb">';
			result = result + '<div class="index_dtb">';
		}
		//onduty = onduty + v.cl + " " + v.bc + "</div>";
		onduty = onduty  + " " + v.bc + "</div>";

		if(v.crew==""||v.crew=="#") {
			result =  result + '<span class="index_dtc" style="color:#666666;">无人员</span>';
		} else {
			$.each(v.crew.split("#"), function(key, name) {
				if(name != "") {
					if(name == index_data.user.name) {
						if(name.length==2) name = name[0] + '&emsp;' + name[1];
						result =  result + '<span class="index_dtc">'+ name + '</span>';
					} else {
						if(name.length==2) name = name[0] + '&emsp;' + name[1];
						result = result + '<span class="index_dtd">'+ name + '</span>';
					}
				 }
			 });
		}
			result = result + "</div>";
		});
		$("#index_duty").html(onduty);
		$("#index_dtus").html(result);
		index_mess("载入成功", 2);
}
/**
 * 打开登录系统框
 */
function index_login() {
	$('#index_dd').dialog('open');
	$("#j_username").val("").focus();
	$("#j_password").val("");
}
/**
 * 登录系统
 */
function index_fmlg() {
	if($("#j_username").val() == "") {
		index_mess("用户名为空", 3);
		$("#j_username").focus();
		return;
	}
	if($("#j_password").val() == "") {
		index_mess("密码为空", 3);
		$("#j_password").focus();
		return;
	}
	index_mess("登录中...", 0);
	$('#index_form').form('submit',{
		url: "j_spring_security_check",
		success: function(data){
			data = $.parseJSON(data);
			if(data.status == 0) {
				index_mess("登录成功！", 2);
				$('#index_dd').dialog('close');
				$.fn.zTree.destroy("index_treeDemo");
				index_data.index.menu =data.menu;
				index_data.treeObj = $.fn.zTree.init($("#index_treeDemo"), index_data.setting, index_data.index.menu);
				index_data.user.id = data.id;
				index_data.user.name = data.name;
				index_data.user.depm = data.depm;
				loadIndex(true);
			} else {
				index_mess(data.mess, 1);
			}
		}
	});
	/*$.post("j_spring_security_check", $("#index_form").serialize(), function(data) {}, "json");*/
}
/**
 * 弹出更改密码框
 */
function index_changePS() {
	$("#index_pw_span").html("");
	$('#index_fm div input').val("");
	//使用easyUI的清除方法会出现提示框不消失的情况
	//$('#index_fm').form('clear');
	$('#index_ps').dialog('open');
}
/**
 * 更改密码
 */
function index_changePW() {
	if(!$("#index_fm").form('validate')) {
		return;
	}
	index_mess("更改中...", 0);
	//0旧密码 1新密码 2确定密码
	var params = $("#index_fm").serializeArray();
	if(params[1].value != "" && (params[1].value != params[2].value)) {
		index_mess("两次密码不相同！", 1);
		return;
	}
	if(index_data.user.id == "") {
		index_mess("请先登录", 2);
		index_login();
		return;
	}
	$.post("user_changePW_js.action", params, function(data) {
		if(data.status == 0) {
			index_mess("更改成功", 2);
			$('#index_ps').dialog('close');
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) index_login();
			if(data.password == false) {
				$("#index_pw_span").html(data.mess);
			}
		}
		
	}, "json");
}
/**
 * 将ztree转换为easyui树
 * @param ztree [{id,name,pId}]
 * @returns etree [{id,text,children:[{..}]}]
 */
function index_changeTree(ztree, data) {
	if(ztree.length == 0)
		return [];
	var etree = [];
	$.each(ztree, function(k, v) {
		if(v.pId == null) {
			etree.push(index_node(v, data));
		}
	});
	index_createTree(etree, ztree, data);
	return etree;
}
/**
 * 生成一个节点
 * @param data {state:"closed"} 为null时从znode取，否则直接用
 */
function index_node(znode, data) {
	var node = {
		id: znode.id,
		text: znode.name
	};
	if(data != null) {
		$.each(data, function(k, v) {
			if(v == null) {
				node[k] = znode.k;
			} else {
				node[k] = v;
			}
		});
	}
	return node;
}
/**
 * 递归从ztree树取数据，然后构建easyui树
 */
function index_createTree(etree, ztree, data) {
	$.each(etree, function(ek, ev) {
		$.each(ztree, function(zk, zv) {
			if(zv.pId == ev.id) {
				if(ev.children == null) {
					ev.children = [index_node(zv, data)];
				} else {
					ev.children.push(index_node(zv, data));
				}
			}
		});
		if(ev.children != null) {
			index_createTree(ev.children, ztree, data);
		}
	});
}