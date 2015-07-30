//splitTime邮件列表[{"kind":0,"name":"今天","list":[]}]
//splitid为splitTime建立的索引{kind值:数组位置, ch0:全选状态}
//params列表参数{"page.num":20,"page.emailst":1,"page.sortst":1}
//timeon定时器
//emst 邮件状态 login:是否登录,urnums:未读数量,alnums:总数量,cpage:当前页面,pages:总页数,prest:上一次"page.emailst"状态
//emails 邮件信息保存utree:{uid,tid}(索引), unread/user:未读取部门/用户, write:是否正在编辑
//rftm 自动刷新时间(默认为1分钟)
//dpms 部门用户  read:[已读取过的部门]
var splitTime = new Array(), splitid = {}, params = {}, timeon, emst = {}, emails, rftm = 60000, dpms = {read:[]};
$(function() {
	//打开到收件箱
	jpstatus(1, 1);
	//rdemail(17);
	//wtemail(0);
	//跳转输入框按下enter跳转
	$("#jpinput").keydown(function(e) {
		if(e.keyCode==13) jumpPage();
	});
});
/**
 * 自动刷新查找未读邮件
 */
function autorf() {
	if(!emst.login) return;
	index_mess('刷新中...', 0);
	$.getJSON("email_autorf_js.action?json=" + Today[4], function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 5);
			if(data.login == false) {//未登录处理
				emst.login = false;
				chkunread(1, "请先登录 ");
			} else chkunread(1, "请刷新页面");
		} else {
			Today[4] = data.today;
			var d = data.list.length;
			if(d > 0) {
				var dd;
				initDate(data.today);
				emst.urnums = d + emst.urnums;
				emst.alnums = d + emst.alnums;
				chkunread();
				d = params["page.emailst"];
				//打开的是第一页按时间逆序的全部或只读才添加到页面
				if(d==1 || d==2) {
					$("#econt_title").html(initETitle());
					if(params["page.sortst"]==1 && emst.cpage==1) {
						$.each(data.list, function(k, v) {
							dateTime = splitDate(v.date);
							dd = diffDate(dateTime);
							if(dd.diff == null) v.dtime = "今天" + v.date.substring(11,16);
							else v.dtime = dd.diff;
							v.date = dateToString(dateTime, v.date.substring(11,16));
							if(splitid[0] != undefined) {//'今天'这一栏存在
								splitTime[splitid[0]].list.push(v);
								v.urclass = ' e_ur_tr';
								$("#e_tab_0").prepend(addOneTopage(v, 'class="e_st0" title="未读信件"', false));
								//"今天"这栏的计数加一
								$('#etime_0 span:first').html(splitTime[splitid[0]].list.length);
							} else {
								splitTime.push({"kind": 0, "name": "今天", "list": [v]});
								splitid[0] = splitTime.length - 1;
								$("#econt_title").after(addListToPage(0, splitTime[splitid[0]]));
							}
						});
						splitid["ch0"] = true;//今天一栏
						splitid["ch-1"] = true;//全选状态
					}
				}
			}
			index_mess("刷新成功", 2);
		}
	});
}
/**
 * 载入信件
 */
function loadEmail() {
	handleUnread({
		type: "POST",
		url: "email_list_js.action",
		params: params,
		mess: ["载入中...", "载入成功！"],
		method: addAllToPage
	});
}
/**
 * 将读取的邮件写到页面上去
 */
function addAllToPage(data) {
	//设置邮件状态
	emst.alnums = data.alnums;
	emst.cpage = data.cpage;
	emst.pages = data.pages;
	//邮件分类保存
	splitTime = [];
	splitid = {"ch-1": true};
	//初始化邮件类型标题
	var str = '<div id="econt_title" class="econt_title">' + initETitle() + '</div>';
	//初始化排序状态
	var sortst = params["page.sortst"];
	//是否按发件人排序
	var sortbyus = false;
	if(params["page.emailst"] > 0) {//收件箱
		switch(sortst) {
			case 2:;//按发件人正序
			case -2:
				if(sortst < 0)
					$("#sort_u").html('<b class="e_st_up"></b><span class="mitext">发件人&nbsp;&uarr;</span>');
				else $("#sort_u").html('<b class="e_st_dn"></b><span class="mitext">发件人&nbsp;&darr;</span>');
				$("#sort_t").html('<span class="miico"></span><span class="mitext">时间</span>');
				sortbyus = true;
				break;
			default:
				if(sortst < 0)
					$("#sort_t").html('<b class="e_st_up"></b><span class="mitext">时间&nbsp;&uarr;</span>');
				else $("#sort_t").html('<b class="e_st_dn"></b><span class="mitext">时间&nbsp;&darr;</span>');
				$("#sort_u").html('<span class="miico"></span><span class="mitext">发件人</span>');
		}
	} else {
		switch(sortst) {
			case -1:
				$("#send_t").html('<b class="e_st_up"></b><span class="mitext">时间&nbsp;&uarr;</span>');
				break;
			default:
				$("#send_t").html('<b class="e_st_dn"></b><span class="mitext">时间&nbsp;&darr;</span>');
		}
	}
	//初始化时间
	initDate(data.today);
	//初始化页码
	var jPage = data.cpage + '/' + data.pages + " 页";
	if(data.pages > 1) {
		if(emst.cpage > 1) jPage = jPage.concat('<a href="javascript:;" onclick="prePage()">上一页</a>');
		else jPage = jPage.concat('<span>上一页</span>');
		if(emst.cpage < emst.pages) jPage = jPage.concat('<a href="javascript:;" onclick="nextPage()">下一页</a>');
		else jPage = jPage.concat('<span>下一页</span>');
		jPage = jPage.concat('<a href="javascript:;" onclick="jpopen(this)" class="noclose">跳转</a>');
	}
	$("#email_tb_ext").html(jPage);
	//处理列表时间，并把列表添加到splitTime
	var dateTime, dd = {}, d;
	$.each(data.list, function(k, v) {
		dateTime = splitDate(v.date);
		dd = diffDate(dateTime);
		d = dd.time;
		if(d == 0) {
			if(dd.diff == null) v.dtime = "今天" + v.date.substring(11,16);
			else v.dtime = dd.diff;
		} else if(d == 1) v.dtime = "昨天" + v.date.substring(11,16);
		else if(d == 2) v.dtime = "前天" + v.date.substring(11,16);
		else v.dtime = (dateTime.getMonth()+1) + "月" + dateTime.getDate() + "日";
		v.date = dateToString(dateTime, v.date.substring(11,16));
		if(sortbyus) d = v.uid;
		if(splitid[d] != undefined) {
			splitTime[splitid[d]].list.push(v);
		} else {
			if(sortbyus) splitTime.push({"kind":d, "name": v.uname, "list": [v]});
			else splitTime.push({"kind":d, "name": initTime[d], "list": [v]});
			splitid[d] = splitTime.length-1;
			splitid["ch" + d] = true;
		}
	});
	//将列表添加到页面
	$.each(splitTime, function(k, value) {
		str = str + addListToPage(value.kind, value);
	});
	if(data.pages < 2) jPage = "";
	$('#email_content').html(str + '<div class="e_jp_bot">' + jPage + '</div>').scrollTop(0);
	
}
/**
 * 将splitTime其中一组转变为页面元素
 */
function addListToPage(k, value) {
	var si = '', d = value.list.length, str = '';
	if(d != 0) {
		str = str + '<div id="etime_' + k + '" class="econt_time">' +
			'<a href="javascript:;" onclick="checkAll(' + k + ')">' +
				'<b>' + value.name + '</b></a>(<span>' + d + '</span>)</div>' +
			'<div id="e_tab_' + k + '" class="email_table">';
		//是否是发件箱
		var sendem = false;
		if(params["page.emailst"] < 0) sendem = true;
		$.each(value.list, function(key, v) {
			v.urclass = '';
			if(sendem) {
				//1已经发送, 2已回复
				switch(v.sendst) {
					case 2:
						si = 'class="e_st_sd2" title="已回复"';
						break;
					default:
						si = 'class="e_st_sd" title="已发送"';
				}
				d = v.addrees.length;
				switch(d) {
					case 1: v.uname = v.addrees[0].name;break;
					case 2:
						v.uname = v.addrees[0].name + ", " + v.addrees[1].name;
						break;
					case 3:
						v.uname = v.addrees[0].name + ", " + v.addrees[1].name +
							", " + v.addrees[2].name;
						break;
					default : v.uname = "未发给任何人";
				}
			} else {
				//1未读, 2已读, 3已回复
				switch(v.readst) {
					case 2: si = 'class="e_st1" title="已读信件"'; break;
					case 3: si = 'class="e_st2" title="已回复信件"'; break;
					default:
						si = 'class="e_st0" title="未读信件"';
						v.urclass = ' e_ur_tr';
				}
			}
			str = str + addOneTopage(v, si, sendem);
		});
		str = str.concat('</div>');
	}
	return str;
}
/**
 * 将splitTime其中一个转变为页面元素
 * @param v v.id v.state v.title v.date v.estate v.uid v.uname v.addrees.id,name
 * @param si 邮件状态
 * @param sendem 是否是发件
 */
function addOneTopage(v, si, sendem) {
	var name;
	if(sendem) name = [v.uname, v.uname];
	else name = [v.uname + " - " + v.dname, v.uname + "<span>&lt;" + v.dname + "&gt;</span>"];
	return '<div id="ids' + v.id + '" class="ec_false' + v.urclass + 
			'" onclick="rdemail(' + v.id + ')">' +
		'<div class="e_ch"><input type="checkbox" ' + 
				'onclick="checkBox(' + v.id + ', event)">' +
			'<b ' + si + '></b>'+
			'<div class="e_ad" title="' + name[0] + '">' + name[1] + '</div></div>' +
		'<div class="e_ti" title="' + v.title + '">' + v.title + '</div>' +
		'<div class="e_da" title="' + v.date + '">' + v.dtime + '</div></div>';
}
/**
 * 初始化邮件的标题
 */
function initETitle() {
	var str;
	switch(params["page.emailst"]) {
		case 2://未读
			str = '<a onclick="jpstatus(1)" href="javascript:;">收件箱</a>' +
				'&nbsp;&rsaquo;&rsaquo;&nbsp;<span>未读&nbsp;</span>(共<b>&nbsp;' + emst.alnums + '&nbsp;</b>封';
			if(emst.urnums > 0)
				str = str + '<a onclick="changeUnread(1)" href="javascript:;">全部设为已读</a>';
			return str + ")";
		case 3://已删除收件
			return '<a onclick="jpstatus(1)" href="javascript:;">收件箱</a>' +
				'&nbsp;&rsaquo;&rsaquo;&nbsp;<span>已删除&nbsp;</span>(共<b>&nbsp;' + emst.alnums + '&nbsp;</b>封)';
		case -1://发件箱
			return '<span>发件箱&nbsp;</span>(共<b>&nbsp;' + emst.alnums + '&nbsp;</b>封)';
		case -2://已删除发件
			return '<a onclick="jpstatus(-1)" href="javascript:;">发件箱</a>' +
				'&nbsp;&rsaquo;&rsaquo;&nbsp;<span>已删除&nbsp;</span>(共<b>&nbsp;' + emst.alnums + '&nbsp;</b>封)';
		default://收件箱
			str = '<span>收件箱&nbsp;</span>(共<b>&nbsp;' + emst.alnums + '&nbsp;</b>封';
			if(emst.urnums > 0)
				str = str + '，有<b>&nbsp;' + emst.urnums + '&nbsp;</b>封' +
						'<a onclick="jpstatus(2)" href="javascript:;">未读邮件</a>' +
						'<a onclick="changeUnread(1)" href="javascript:;">全部设为已读</a>';
			return str + ")";
	}
}
/**
 * 全选
 * @param flag 为-1时全选，其他为全选其中一栏
 */
function checkAll(flag) {
	var check;
	if(flag == -1) {
		check = splitid["ch-1"];
		splitid["ch-1"] = !check;
		$.each(splitTime, function(k, v) {
			splitid["ch" + v.kind] = !check;
			checkList(v.list, check);
		});
	} else {
		check = splitid["ch" + flag];
		if(splitid["ch" + flag]) {
			splitid["ch" + flag] = false;
		} else {
			splitid["ch" + flag] = true;
			splitid["ch-1"] = true;
		}
		checkList(splitTime[splitid[flag]].list, check);
		if(check) chboxst2(flag);
	}
}
/**
 * 全选一栏
 */
function checkList(list, check) {
	$.each(list, function(key, value) {
		$("#ids" + value.id + " div:first input:first").attr("checked", check);
		if(check) $("#ids" + value.id).addClass("ec_true");
		else $("#ids" + value.id).removeClass("ec_true");
	});
}
/**
 * 单选
 */
function checkBox(id, event) {
	event.stopPropagation();//防止事件冒泡
	var cb = $("#ids" + id + " div:first input:first");
	if(cb.attr("checked")) {
		cb.attr("checked", true);
		$("#ids" + id).addClass("ec_true");
	} else {
		cb.attr("checked", false);
		$("#ids" + id).removeClass("ec_true");
	}
	chboxst();
}
/**
 * 单选一个后，检查每一栏和全选的状态
 */
function chboxst() {
	var flag, count = 0;
	$.each(splitTime, function(k, v) {
		flag = true;
		$.each(v.list, function(key, value) {
			if(!$("#ids" + value.id + " div:first input:first").attr("checked")) {
				splitid["ch" + v.kind] = true;
				flag = false;
				return false;
			}
		});
		if(flag) {
			count ++;
			splitid["ch" + v.kind] = false;
		}
	});
	if(count == splitTime.length) splitid["ch-1"] = false;
	else splitid["ch-1"] = true;
}
/**
 * 选中一栏后检查全选状态
 * 其中有一个未选，就置全选为true,全部已选是才置为false
 */
function chboxst2(list) {
	var count = 0, flag;
	$.each(splitTime, function(k, v) {
		if(v.kind == list) {
			count ++;
		} else {
			flag = true;
			$.each(v.list, function(key, value) {
				if(!$("#ids" + value.id + " div.e_ch input").attr("checked")) {
					flag = false;
					return false;
				}
			});
			if(flag) count ++;
			else return false;
		}
	});
	if(count == splitTime.length) splitid["ch-1"] = false;
	else splitid["ch-1"] = true;
}
/**
 * 改变状态(st 等同 page.emailst)
 * @param st A收件1->删除, 2->彻底删除, 3->还原, 4->已读
 *           B发件-1->删除, -2->彻底删除, -3->还原
 */
function changest(st, eid) {
	var str = null;
	switch(st) {
		case 1: str = ["删除中...", "已移动到'已删收件'"]; break;
		case -1: str = ["删除中...", "已移动到'已删发件'"]; break;
		case 2:;
		case -2:
			if(!confirm("删除邮件后将无法恢复，确定要继续吗？")) return;
			str = ["删除中...", "删除成功！"];
			break;
		case 3: str = ["还原中...", "已移动到'收件箱'"]; break;
		case -3: str = ["还原中...", "已移动到'发件箱'"]; break;
		case 4: str = ["标记中...", "标记成功！"]; break;
		default:
			index_mess("非法操作！", 3);
			return;
	}
	var list = new Array();
	if(eid == null) {
		$.each(splitTime, function(k, v) {
			$.each(v.list, function(key, value) {
				if($("#ids" + value.id + " div:first input:first").attr("checked")) {
					if(st!=4 || $("#ids" + value.id).hasClass("e_ur_tr")) {
						list.push(value.id);
					}
				}
			});
		});
		if(list.length == 0) {
			if(st == 4) index_mess("全部已标为已读！", 4);
			else index_mess("请先选择邮件！", 4);
			return;
		}
	} else {
		list.push(eid);
		params["page.emailst"] = emst.prest;
	}
	handleUnread({
		type: "POST",
		url: "email_changest_js.action?json=" + st + "A" + list.join('a'),
		params: params,
		mess: str,
		method: function(data) {
			//删除失败时，之前页面状态不可恢复
			if(eid != null) {
				initmenu(emst.prest);
			}
			addAllToPage(data);
		}
	});
}
/**
 * 将所有未读更改为已读
 * @param st 1正常中的 2已删除中的
 */
function changeUnread(st) {
	handleUnread({
		type: "POST",
		url: "email_changeur_js.action?json=" + st,
		params: params,
		mess: ["更改中...", "全部已置为已读！"],
		method: addAllToPage
	});
}
/**
 * 在访问服务器后，处理未读邮件
 * @param para {type, url, params, mess, method}
 */
function handleUnread(para) {
	clearInterval(timeon);
	index_mess(para.mess[0], 0);
	$.ajax({
		type: para.type,
		url: para.url,
		data: para.params,
		dataType: "json",
		success: function(data) {
			timeon = setInterval(autorf, rftm);
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false) {
					emst.login = false;
				}
				return;
			}
			emst.login = true;
			Today[4] = data.today;
			emst.urnums = data.urnums;
			chkunread();
			para.method(data);
			index_mess(para.mess[1], 2);
		}
	});
}
/**
 * 上一页
 */
function prePage() {
	if(emst.cpage > 1) {
		params["page.page"] = emst.cpage - 1;
		loadEmail();
	}
}
/**
 * 下一页
 */
function nextPage() {
	if(emst.cpage < emst.pages) {
		params["page.page"] = emst.cpage + 1;
		loadEmail();
	}
}
//open window的位置
//0-跳转页面 1-排序
var opwd = [];
/**
 * 打开跳转页面窗口
 */
function jpopen(a) {
	$("body").unbind("click", jpclose);
	var lt = $(a).offset();
	//大于200为底部的跳转
	if(lt.top < 200) opwd[0] = {top: lt.top+20, left: lt.left-154};
	else opwd[0] = {top: lt.top-48, left: lt.left-63};
	$("#jumpPage").show().offset(opwd[0]);
	$("#jpinput").val("").focus();
	$("body").click(jpclose);
}
function jpclose(event) {
	if(event.pageX>opwd[0].left && event.pageX<(opwd[0].left + 184) &&
			event.pageY>opwd[0].top && event.pageY<(opwd[0].top + 45)) return;
	if(jQuery(event.target).hasClass("noclose")) return
	$("#jumpPage").hide();
	$("body").unbind("click", jpclose);
}
function jumpPage() {
	var pnum = $("#jpinput").val().replace(/\D/g,'');
	$("#jpinput").val(pnum).focus();
	if(pnum == "") return;
	if(pnum < 1) pnum = 1;
	if(pnum > emst.pages) pnum = emst.pages;
	params["page.page"] = pnum;
	loadEmail();
	$("#jumpPage").hide();
	$("body").unbind("click", jpclose);
}
/**
 * sort open排序窗口
 */
function stopen(a) {
	$("body").unbind("click", stclose);
	var lt = $(a).offset();
	opwd[1] = {top: lt.top+29, left: lt.left};
	$("#stwd").show().offset(opwd[1]);
	$("body").click(stclose);
}
function stclose(event) {
	if(event == null) {
		$("#stwd").hide();
		$("body").unbind("click", stclose);
		return;
	}
	if(event.pageX>opwd[1].left && event.pageX<(opwd[1].left + 125) &&
		event.pageY>opwd[1].top && event.pageY<(opwd[1].top + 177)) return;
	if(jQuery(event.target).attr("id") == "stbtn") return;
	$("#stwd").hide();
	$("body").unbind("click", stclose);
}
/**
 * send open 发件箱排序窗口
 */
function sdopen(a) {
	$("body").unbind("click", sdopen);
	var lt = $(a).offset();
	opwd[2] = {top: lt.top+29, left: lt.left};
	$("#sdwd").show().offset(opwd[2]);
	$("body").click(sdclose);
}
function sdclose(event) {
	if(event == null) {
		$("#sdwd").hide();
		$("body").unbind("click", sdclose);
		return;
	}
	if(event.pageX>opwd[2].left && event.pageX<(opwd[2].left + 125) &&
		event.pageY>opwd[2].top && event.pageY<(opwd[2].top + 121)) return;
	if(jQuery(event.target).attr("id") == "stbtn") return;
	$("#sdwd").hide();
	$("body").unbind("click", sdclose);
}
/**
 * 排序 相同-正逆相反 不同-改为正
 * @param st 1时间 2发件人
 */
function sortEmail(st) {
	if(params["page.sortst"]<0 || params["page.sortst"]!=st) {
		params["page.sortst"] = st;
	} else {
		params["page.sortst"] = -st;
	}
	params["page.page"] = 1;
	loadEmail();
}
/**
 * 跳转到其他状态
 * @param st A收件：1全部 2未读 3删除  B发件： -1全部 -2删除 C写信/看信 0
 * @param flag 1-不排序
 */
function jpstatus(st, flag) {
	if(checkwrite()) return;
	if(st != params["page.emailst"]) {
		initmenu(st);
	}
	if(flag == 1) params["page.sortst"] = 1;
	params["page.page"] = 1;
	loadEmail();
}
/**
 * 初始化收发信箱的菜单按钮
 */
function initmenu(st) {
	params["page.emailst"] = st;
	//中部菜单条显示
	var str = '<span onclick="checkAll(-1)">全&nbsp;选</span>&nbsp;';
	if(st >= 0) {//收件箱
		if(st != 3)
			str = str + '<span onclick="changest(1)">删&nbsp;除</span>';
		else
			str = str + '<span onclick="changest(3)">还&nbsp;原</span>';
		str = str + '<span onclick="changest(2)">彻底删除</span>' +
			'<span onclick="changest(4)">标为已读</span>' +
			'<span id="stbtn" onclick="stopen(this)">查看&nbsp;&rsaquo;</span>';
	} else {//发件箱
		if(st != -2)
			str = str + '<span onclick="changest(-1)">删&nbsp;除</span>';
		else
			str = str + '<span onclick="changest(-3)">还&nbsp;原</span>';
		str = str + '<span onclick="changest(-2)">彻底删除</span>' +
			'<span id="stbtn" onclick="sdopen(this)">查看&nbsp;&rsaquo;</span>';
	}
	str = str + '&nbsp;<span onclick="loadEmail()">刷&nbsp;新</span>';
	$("#email_tb").html(str);
	//左边导航条选中
	$("#menu_ul li").removeClass();
	switch(st) {
		case 1:;
		case 2: $("#menu_ul li:first-child").attr("class", "select");break;
		case 3: $("#menu_ul li:nth(2)").attr("class", "select");break;
		case -1: $("#menu_ul li:nth(1)").attr("class", "select");break;
		case -2: $("#menu_ul li:nth(3)").attr("class", "select");break;
	}
}
/**
 * 未读邮件处理,更改标题...
 * st1加在左边 st2加在右边 st3两边都加
 */
function chkunread(st, lstr, rstr) {
	switch(st) {
		case 1: rstr = "";break;
		case 2: lstr = "";break;
		case 3: break;
		default : lstr = "";rstr = "";
	}
	if(emst.urnums > 0) {
		document.title = lstr + "(" + emst.urnums + "封未读) 信息部邮箱" + rstr;
		$("#menu_li_b").html("(" + emst.urnums + ")");
	} else {
		document.title = "信息部邮箱";
		//$("title").html("信息部邮箱");//不兼容
		$("#menu_li_b").html("");
	}
}
/**
 * 查看邮件
 */
function rdemail(id) {
	if(params["page.emailst"] < 0) {//发件
		id = - id;
	}
	handleUnread({
		url: "email_read_js.action?json=" + id + "&_=" + Math.random(),
		mess: ["读取中...", "读取成功！"],
		method: function(data) {
			//urnums today myid id title content date sendst emailst uid uname did dname simms(精简信息)
			//addrees{id name state date did dname}
			//utree:{为用户和数建立的索引},unread:[未读部门],unuser:[未读用户]
			if(id < 0) {
				id = - id;
			}
			data.id = id;
			var st, gettime = '', str = '',
			addrees = [{"name":"收件:","list":[]}, {"name":"抄送:","list":[]},{"name":"转发:","list":[]}];
			if(data.addrees.length != 0) {
				$.each(data.addrees, function(k, v) {
					//1正常, 2已删除, 3彻底删除
					//switch(v.emailst) {}
					//1未读, 2已读, 3已回复
					switch(v.readst) {
						case 2: v.ststr = '已读'; break;
						case 3: v.ststr = '已回复'; break;
						default: v.ststr = '未读';
					}
					//1收件, 2抄送, 3转发
					switch(v.recest) {
						case 2: st = 1; break;
						case 3: st = 2; break;
						default: st = 0;
					}
					addrees[st].list.push(v);
					if(v.id == data.myid)
						gettime = '<div class="em_ct_date">收件时间: ' +
						dateToString(splitDate(v.date), v.date.substring(11,16)) + '</div>';
				});
				switch(data.addrees.length) {
					case 1:
						data.simms = " 发送给 " + data.addrees[0].name + "。";break;
					case 2: 
						data.simms = " 发送给 " +  data.addrees[0].name + "，" + data.addrees[1].name + "。";break;
					case 3:
						data.simms = " 发送给 " +  data.addrees[0].name + "，" + data.addrees[1].name + "，" +
						data.addrees[2].name + "。";break;
					default:
						data.simms = " 发送给 " +  data.addrees[0].name + "，" + data.addrees[1].name + "，" +
						data.addrees[2].name + " 等" + data.addrees.length + "人。";break;;
				}
				$.each(addrees, function(key, value) {
					if(value.list.length > 0) {
						str = str + '<div class="em_ct_emee"><div class="em_ct_emeea">' + value.name + '</div>';
						$.each(value.list, function(k, v) {
							str = str + '<div class="em_ct_emeeb" title="' + v.ststr +  ' : ' +
							dateToString(splitDate(v.date), v.date.substring(11,16)) + 
							'"><span class="em_ct_emeec">' +
							v.name + '</span>&lt;' + v.dname + '&gt;</div>' ;
						});
						str = str + '</div>';
					}
				});
			} else {
				data.simms = '&nbsp;创建，未发给任何人。';
				str = '<div class="em_ct_emee"><div class="em_ct_emeea">收件:</div><div class="em_ct_emeeb">未发给任何人</div></div>';
			}
			data.date = dateToString(splitDate(data.date), data.date.substring(11,16));
			//1正常, 2已删除, 3彻底删除
			//switch(data.emailst) {}
			//1已经发送, 2已回复
			switch(data.sendst) {
				case 2: data.ststr = '(已回复)'; break;
				default: data.ststr = ''; break;
			}
			$("#email_content").html('<div class="em_ct">' +
					'<div class="em_ct_title" title="' + data.ststr + data.title + '">' + data.title + '</div>' +
					'<div id="em_ct_dt"><div class="em_ct_emer"><b>' + data.uname + '</b>&lt;' + data.dname + '&gt;</div>' + str +
					'<div class="em_ct_date">发件时间: ' + data.date +
					'</div>' + gettime +
					'</div><div id="em_ct_sm" class="em_ct_sm hidden"><b>' + data.uname + '</b> 于' +
					data.date + data.simms + '</div>' +
					'<div id="em_ct_up" class="em_ct_ch"><a href="javascript:;" onclick="chdetail(false)">精简信息<b class="em_ct_up"></b></a></div>' +
					'<div id="em_ct_dw" class="em_ct_ch hidden"><a href="javascript:;" onclick="chdetail(true)">详细信息<b class="em_ct_dw"></b></a></div></div>' +
					'<div class="em_ct_ct">' + data.content + '</div><div><div class="em_ct_rp"><b>快捷回复</b>' +
					'<div class="em_ct_rh"><a href="javascript:;" onclick="wtemail(' + id + ')">完整模式回复</a></div></div>' +
					'<div id="em_ctt' + id + '" class="em_ct_tt">' +
					'<textarea class="em_ct_text" onfocus="smrponfc('+ id + ');" ' +
					'onblur="$(\'#em_ctt' + id + '\').removeClass(\'em_ct_in\');"></textarea></div>' +
					'<div id="em_ct_bt'+ id + '" class="em_ct_bt">' +
					'<span class="em_ct_sd" style="display:none;" onclick="smrpsend('+ id + ');">发&nbsp;送</span>&nbsp;&nbsp;' +
					'<span style="display:none;" onclick="smrpclose('+ id + ');">取&nbsp;消</span></div></div>');
			//<iframe id="emct_if' + id + '"></iframe>
			//$("#menu_ul li").removeClass();
			str = '<span onclick="initmenu(emst.prest);loadEmail();">&laquo;&nbsp;返回</span>&nbsp;' +
				'<span class="emtb_hl" onclick="wtemail(' + id + ')">回&nbsp;复</span>&nbsp;' +
				'<span onclick="alert(\'转发功能未实现！\')">转&nbsp;发</span>&nbsp;';
			st = params["page.emailst"];
			if(st >= 0) {//收件箱
				if(st != 3)
					str = str + '<span onclick="changest(1,' + id + ')">删&nbsp;除</span>';
				else
					str = str + '<span onclick="changest(3,' + id + ')">还&nbsp;原</span>';
				str = str + '<span onclick="changest(2,' + id + ')">彻底删除</span>';
			} else {//发件箱
				if(st != -2)
					str = str + '<span onclick="changest(-1,' + id + ')">删&nbsp;除</span>';
				else
					str = str + '<span onclick="changest(-3,' + id + ')">还&nbsp;原</span>';
				str = str + '<span onclick="changest(-2,' + id + ')">彻底删除</span>';
			}
			//str = str + '<span onclick="rdemail(' + id + ')">刷&nbsp;新</span>';
			$("#email_tb").html(str);
			$("#email_tb_ext").html('<span>上一封</span><a onclick="nextEM()" href="javascript:;">下一封</a>');
			data.utree = {};
			emails = data;
			emst.prest = st;
			params["page.emailst"] = 0;
		}
	});
}
/**
 * 精简和详细收件人信息切换
 */
function chdetail(st) {
	if(st) {
		$("#em_ct_sm").hide("normal");
		$("#em_ct_dt").show("slow");
		$("#em_ct_dw").hide();
		$("#em_ct_up").show();
	} else {
		$("#em_ct_dt").hide("slow");
		$("#em_ct_sm").show();
		$("#em_ct_up").hide();
		$("#em_ct_dw").show();
	}
}
/**
 * 更改自动刷新时间
 */
function chrftime() {
	var time = $("#rftime input:checked").val();
	if(time>1080000 || time<30000) time = 60000;
	rftm = time;
	clearInterval(timeon);
	timeon = setInterval(autorf, rftm);
	$('#rftime').hide();
}
/**
 * 快捷回复得到焦点触发simple reply onfocus
 */
function smrponfc(id) {
	$('#em_ct_bt' + id + ' span').show();
	$('#em_ctt' + id).animate({height: 90}).addClass('em_ct_in em_ct_on');
	$('#email_content').animate({scrollTop: $('#email_content')[0].scrollHeight}, "slow");
}
/**
 * 快捷回复关闭
 */
function smrpclose(id) {
	if($("#em_ctt" + id + " textarea:first").val()!="" &&
			!confirm("是否要放弃已编写的回复？")) {
		return;
	}
	$('#em_ctt' + id).animate({height: 28}).removeClass('em_ct_in em_ct_on');
	$('#em_ctt' + id + ' textarea').val("");
	$('#em_ct_bt' + id + ' span').hide();
}
/**
 * 快捷回复发送
 */
function smrpsend(id) {
	var content = $("#em_ctt" + id + " textarea:first").val();
	if(content.length == 0) {
		index_mess("请先输入内容！", 4);
		$("#em_ctt" + id + " textarea:first").focus();
		return;
	}
	var addrees = new Array();
	addrees.push(emails.uid);
	$.each(emails.addrees, function(k, user) {
		if(user.id!=emails.myid && user.id!=emails.uid) {
			addrees.push(user.id);
		}
	});
	sendEmail({
		"json": addrees.join('a'),
		"email.id": id,
		"email.title": prtitle(),
		"email.content": premail(content)
	});
}
/**
 * 发送邮件
 * @param params 邮件标题内容...
 */
function sendEmail(params) {
	handleUnread({
		type: "POST",
		url: "email_add_js.action",
		params: params,
		mess: ["发送中...", "发送成功！"],
		method: function(data) {
			$("#email_tb").html('<span onclick="jpstatus(1, 1)">返回收件箱</span>&nbsp;<span onclick="wtemail(0)">再写一封</span>');
			$("#email_content").html('<div class="em_sdok">' +
	    		'<span><img src="script/kindeditor/plugins/emoticons/images/0.gif"/>发送成功！</span>' +
	    		'<div>再写一封，或者返回收件箱等待回信...</div></div>');
			dropemail();
		}
	});
}
/**
 * 写信前打开的邮件内容
 * @param str 返回加入内容处理后的
 */
function premail(str) {
	var content = '<p>' + (str==undefined ? '<br />' : str) +'</p><p><br /></p><p><br /></p>' +
		'<div style="font-size:12px;font-family:Arial Narrow;padding:2px 0;">------------------ 原始邮件 ------------------</div>' +
		'<div style="font-size:12px;background:#efefef;padding:8px;"><strong>' +
			emails.uname + '</strong>&lt;' + emails.dname + '&gt;  于' + emails.date + emails.simms +
			'<div><b>主题: </b>' + emails.title + '</div></div>' +
		'<div style="font-size:14px;"><p><br /></p>' + emails.content;
	if(str!=undefined && content.length > 21839) {
		return content.substring(0, 21820) + '<p>超出部分已自动省略...</p></div>';
	}
	return content + '</div>';
}
/**
 * 写信前打开的邮件的标题
 */
function prtitle() {
	var title = "回复：" + emails.title;
	if(title.length>100) title = title.substring(0, 97) + "...";
	return title;
}
/**
 * 写信 write email
 */
function wtemail(id) {
	if(id == 0) {
		if(checkwrite()) return;
		emails = {id:0,utree:{},write:true};
	} else emails.write = true;
	$("#menu_ul li").removeClass();
	str = '<span class="em_ct_sd" onclick="dtrpsend('+ id + ');">发&nbsp;送</span>&nbsp;&nbsp;' +
		'<span onclick="dtrpclose('+ id + ');">取&nbsp;消</span>';
	$("#email_tb").html(str);
	$("#email_tb_ext").html("");
	$("#email_content").html('<div class="em_write"><div class="em_wt_ip">' +
				'<div class="em_wt_sj">' +
					'<label class="em_wt_lb">收件人</label>' +
					'<div id="em_wtsj' + id + '" class="em_ct_tt em_wt_sjb"></div></div>' +
						//'<input class="em_ct_text" onfocus="$(\'#em_wtsj' + id + '\').addClass(\'em_ct_in\');"' +
						//	'onblur="$(\'#em_wtsj' + id + '\').removeClass(\'em_ct_in\');"/></div></div>' +
				//'<div class="em_wt_cs"><label class="em_wt_lb">抄&emsp;送</label><div id="em_wtcs' + id + '" class="em_ct_tt em_wt_sjb"></div></div>' +
				'<div class="em_wt_sj">' +
					'<label class="em_wt_lb">标&emsp;题</label>' +
					'<div id="em_wttt' + id + '" class="em_ct_tt em_wt_sja">' +
						'<input class="em_ct_text" value="' +  (id==0 ? "" : prtitle()) + '" onfocus="$(\'#em_wttt' + id + '\').addClass(\'em_ct_in\');"' +
							'onblur="$(\'#em_wttt' + id + '\').removeClass(\'em_ct_in\');"/></div></div>' +
				'<div id="em_wttx' + id + '" class="em_wt_tx"><textarea id="emcontent' + id + '"></textarea></div>'+
				'<div id="em_wtwc' + id + '" class="em_wt_txa"></div></div>' +
			'<div class="em_wt_us"><div class="em_wt_usa"></div><div id="em_users' + id + '" class="em_wt_usb"><ul id="em_wtus' + id + '" class="ztree"></ul></div></div>' +
			'<div class="em_ct_bt em_wt_bt"><span class="em_ct_sd" onclick="dtrpsend(11);">发 送</span>&nbsp;&nbsp;' +
				'<span onclick="dtrpclose(11);">取 消</span></div></div>');
	emails.resize = function() {
		var h = $(window).height();
		if(h < 600) {
			h = 600;
		}
		$("#em_users" + id).height(h - 160);
		editor.resize(null, h - 250);
	};
	editor = KindEditor.create("#emcontent" + id, {
		width: "100%",
		resizeType: 0,
		uploadJson: "file_upload_no.action",
	    fileManagerJson: "file_message_no.action",
		allowFileManager: true,
		urlType: "relative",
		afterChange: function() {
			count = this.count();
			color = "003399";
			if(count > 21845)
				color = "E53333";
			$("#em_wtwc" + id).html("&nbsp;已输入 <span style=\"color:#" + color + ";\">" + count + 
				"</span> 个字符，还可再输入 <span style=\"color:#" + color +
				";\">" + (21845-count)+"</span> 个。");
		},
		afterFocus: function() {
			$("#em_wttx" + id + " div:first").addClass("em_wt_in");
		},
		afterBlur: function() {
			$("#em_wttx" + id + " div:first").removeClass("em_wt_in");
		}, beforeCreate : function() {
			if(!this.fullscreenMode) {//正常
				var h = $(window).height();
				if(h < 600) {
					h = 600;
				}
				$("#em_users" + id).height(h - 160);
				this.height = h - 250;
			}
		}, afterCreate: function() {
			if(this.fullscreenMode) {//全屏
				$(window).unbind("resize", emails.resize);
			} else {//正常
				$(window).resize(emails.resize);
			}
		}
	});
	//内容要另外添加，直接集成会使HTML标签转码后仍然生效
	if(id != 0) {
		editor.html(premail());
	}
	//用户列表
	if(zNodes == null) {
		if(id == 0) treeObj = $.fn.zTree.init($("#em_wtus" + id), setting);
		else chkread();
	} else {
		treeObj = $.fn.zTree.init($("#em_wtus" + id), setting, zNodes);
		$.each(treeObj.transformToArray(treeObj.getNodes()), function(k, v) {
			emails.utree["id" + v.id] = v.tId;
		});
		treeObj.checkAllNodes(false);
		if(id != 0) chkread();
	}
	if(params["page.emailst"] != 0) {
		emst.prest = params["page.emailst"];
	} else {//从查看邮件切换过来时，此状态是0的
		params["page.emailst"] = 0;
	}
}
/**
 * 回复时选中全部发件人和收件人
 */
function chkread() {
	emails.unread = [];
	emails.unuser = [];
	chkuser(emails.uid, emails.did);
	writeuser(emails.uid, emails.uname, emails.dname);
	$.each(emails.addrees, function(k, user) {
		if(user.id!=emails.myid && user.id!=emails.uid) {
			chkuser(user.id, user.did);
			writeuser(user.id, user.name, user.dname);
		}
	});
	if(emails.unuser.length != 0) {
		$.ajax({
			url: "user_dpm_js.action?json=" + emails.unread.join('a') + 'A' + dpms.read.join('a'),
			dataType: "json",
			success: function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 3);
					if(data.login == false) {
						emst.login = false;
					}
					return;
				}
				emst.login = true;
				var pnode = null;
				$.each(data, function(k, v) {
					dpms.read.push(v.pid);
					if(v.pid != 0) {
						pnode = treeObj.getNodeByTId(emails.utree["id" + v.pid]);
						pnode.zAsync = true;
						treeObj.addNodes(pnode, v.list);
						$.each(pnode.children, function(key, value) {
							emails.utree["id" + value.id] = value.tId;
						});
					} else {//根节点
						treeObj = $.fn.zTree.init($("#em_wtus" + emails.id), setting, v.list);
						$.each(treeObj.getNodes(), function(key, value) {
							emails.utree["id" + value.id] = value.tId;
						});
					}
				});
				zNodes = treeObj.getNodes();
				$.each(emails.unuser, function(k, uid) {
					treeObj.checkNode(treeObj.getNodeByTId(emails.utree["id-" + uid]), true, true);
				});
			}
		});
	}
}
/**
 * 在部门用户树中选中用户,并把未读取的记录起来
 */
function chkuser(uid, did) {
	var tid = emails.utree["id-" + uid];
	if(tid != null) {
		treeObj.checkNode(treeObj.getNodeByTId(tid), true, true);
	} else {
		emails.unread.push(did);
		emails.unuser.push(uid);
	}
}
/**
 * 将收件人写入输入框中
 */
function writeuser(uid, uname, dname) {
	$("#em_wtsj" + emails.id).append('<span class="em_wt_sjc" id="em' + emails.id + 'us_' +
			uid + '" title="' + uname + ' (' + dname + ')">' + uname + 
			'<span class="em_wt_sjd" onclick="removeuser(' + uid + ',true)">&times;</span></span>');
}
/**
 * 将收件人去除
 * @param flage 是否将部门用户树中的也去除
 */
function removeuser(uid, flag) {
	$('#em' + emails.id + 'us_' + uid).remove();
	if(flag) {
		var tid = emails.utree["id-" + uid];
		if(tid != null) {
			treeObj.checkNode(treeObj.getNodeByTId(tid), false, true);
		} 
	}
}
var editor, zNodes, treeObj;
var setting = {
	async: {
		enable: true,
		type: "get",
		url: "user_indpm_js.action",
		autoParam: ["id=depmId"],
		dataType: "json",
		dataFilter: function(tid, treeNode, data) {
			if(data.status == 1) {
				index_mess(data.mess, 3);
				if(treeNode == null) {//根节点
					$("#em_wtus" + emails.id).html("请重新进入此页面！");
				} else {
					treeNode.zAsync = false;
					treeObj.updateNode(treeNode);
				}
				if(data.login == false) {
					emst.login = false;
				}
				return null;
			}
			emst.login = true;
			return data;
		}
	}, view: {
		fontCss: function(tid, node) {
			if(node.iconSkin == "user")
				return {"color": "#000000"};
			else return {};
		}
	}, check: {
		enable: true
	}, data: {
		simpleData : {
			enable : true
		}
	}, callback: {
		beforeCheck: function(treeId, treeNode) {
			addAddrees(treeNode);
			return false;
		//}, onClick: function(event, treeId, treeNode) {addAddrees(treeNode);
		}, onAsyncSuccess: function(event, treeId, treeNode, data) {
			if(data.status == 1) treeObj.expandNode(treeNode, false);
			else {
				zNodes = treeObj.getNodes();
				if(treeNode != null) {
					dpms.read.push(treeNode.id);
					$.each(treeNode.children, function(k, v) {
						emails.utree["id" + v.id] = v.tId;
					});
				} else {
					dpms.read.push(0);
					$.each(zNodes, function(key, value) {
						emails.utree["id" + value.id] = value.tId;
					});
				}
			}
		}
	}
};
/**
 * 写信中添加收件人
 */
function addAddrees(treeNode) {
	var addrees = new Array();
	if(treeNode.isParent) {
		if(!treeNode.zAsync) treeObj.reAsyncChildNodes(treeNode, "refresh");
		//要传入拷贝，如果是引用，会被修改
		BFSofAddr(addrees, treeNode.children.concat());
	} else addrees.push(treeNode);
	if(treeNode.checked) {
		treeObj.checkNode(treeNode, false, true);
		$.each(addrees, function(k, v) {
			if(v.id < 0) {
				removeuser(-v.id);
			}
		});
	} else {
		treeObj.checkNode(treeNode, true, true);
		$.each(addrees, function(k, v) {
			if(v.id < 0) {
				writeuser(-v.id, v.name, v.getParentNode()["name"]);
			}
		});
	}
}
/**
 * 广度优先搜索一部门下的所有人员
 */
function BFSofAddr(addrees, nodes) {
	var node = null;
	while(nodes.length != 0) {
		node = nodes[0];
		addrees.push(node);
		if(node.isParent) {
			if(!node.zAsync) treeObj.reAsyncChildNodes(node, "refresh");
			nodes = nodes.concat(node.children);
		}
		nodes.shift();
	}
}
/**
 * 完整回复发送 detail reply send
 */
function dtrpsend(id) {
	var users = $("#em_wtsj" + emails.id + " >span"), 
	idstr, addrees = new Array();
	$.each(users, function(k, v) {
		idstr = v.id;
		addrees.push(idstr.substring(idstr.indexOf('_') + 1));
	});
	if(addrees.length == 0) {
		index_mess("请先选择收件人！", 4);
		return;
	}
	var title = $("#em_wttt" + emails.id + " input:first").val();
	var count = title.length - 100;
	if(count == -100) {
		index_mess("请先输入标题！", 4);
		$("#em_wttt" + emails.id + " input:first").focus();
		return;
	}
	if(count > 0) {
		index_mess("标题多出 " + count + " 个字！", 3);
		$("#em_wttt" + emails.id + " input:first").focus();
		return;
	}
	count = editor.count();
	if(count == 0) {
		index_mess("请先输入内容！", 4);
		editor.focus();
		return;
	}
	if(count > 21845) {
		index_mess("内容超出限制！", 3);
		editor.focus();
		return;
	}
	sendEmail({
		"json": addrees.join('a'),
		"email.id": emails.id,
		"email.title": title,
		"email.content": editor.html()
	});
}
/**
 * 取消写信
 */
function dtrpclose() {
	dropemail();
	initmenu(emst.prest);
	loadEmail();
}
/**
 * 去除写信状态
 */
function dropemail() {
	emails.write = false;
	$(window).unbind("resize", emails.resize);
}
/**
 * 检查写信状态
 * @returns true 正在进行 false 放弃写信或者未写
 */
function checkwrite() {
	if(emails!=null && emails.write) {
		if($("#em_wtsj" + emails.id + " >span").length == 0 &&
				$("#em_wttt" + emails.id + " input:first").val().length == 0 &&
				editor.count() == 0) {
			dropemail();
			return false;
		}
		if(!confirm("是否要放弃正在编写的邮件？")) return true;
		else {
			dropemail();
			return false;
		}
	} else return false;
}
