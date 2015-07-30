var dtli_zNodes, dtli_treeObj;
var dtli_setting = {
	view: {
		dblClickExpand: false
	}, check: {
		enable: true,
		chkboxType: {"Y": "","N": ""}
	}, data: {
		simpleData : {
			enable : true
		}
	}, callback: {
		beforeCheck: function(treeId, treeNode) {
			dtli_addDpm(treeNode);
			return false;
		},
		onClick: function(event, treeId, treeNode) {
			dtli_addDpm(treeNode);
		},
		onCheck: function(event, treeId, treeNode) {
			dtli_addDpm(treeNode);
		}
	}
};
var dtli_duty = [
     {id: "#dtli_one", name: "一值"},
     {id: "#dtli_two", name: "二值"},
     {id: "#dtli_three", name: "三值"},
     {id: "#dtli_four", name: "四值"},
     {id: "#dtli_five", name: "五值"},
     {id: "#dtli_six", name: "六值"},
     {id: "#dtli_seven", name: "驻电信"},
     {id: "#dtli_eight", name: "驻市政府"}
];
var dtli_index = 0;
var dtli_var = {change: false};
$.getJSON("duty_list_js.action", function(data) {
	dtli_zNodes = data.dept;
	dtli_treeObj = $.fn.zTree.init($("#dtli_tree"), dtli_setting, dtli_zNodes);
	$.each(data.duty, function(k, v) {
		dtli_duty[k].dids = v.dpm;
	});
	dtli_loadDuty();
});
/**
 * 选择部门后添加到左边
 */
function dtli_addDpm(treeNode) {
	var dpm = $("#dtli_" + treeNode.id);
	if(treeNode.checked) {
		dpm.remove();
		dtli_treeObj.checkNode(treeNode, false, true);
		dtli_var.change = true;
	} else {
		//原来不存在直接添加,存在时要提示是否切换
		if(dpm[0] != null) {
			$.messager.confirm('值班部门切换', '部门 <span style="color:#E53333;">' + treeNode.name +
					'</span> 原属于 <span style="color:#337FE5;">' +
					$("#dtli_" + treeNode.id).parent().prev().html() + ' </span>，是否要将其移到 <span style="color:#337FE5;">' +
					dtli_duty[dtli_index].name + '</span> 上？', function(r){
				if(r) {
					dpm.remove();
					$(dtli_duty[dtli_index].id).append('<div id="dtli_' +
							treeNode.id + '">' + treeNode.name + '</div>');
					dtli_treeObj.checkNode(treeNode, true, true);
					dtli_var.change = true;
				}
            });
		} else {
			$(dtli_duty[dtli_index].id).append('<div id="dtli_' +
					treeNode.id + '">' + treeNode.name + '</div>');
			dtli_treeObj.checkNode(treeNode, true, true);
			dtli_var.change = true;
		}
	} 
}
/**
 * 更新到数据库
 */
function dtli_changeDuty() {
	index_mess("更新中...", 0);
	if(!dtli_var.change) {
		index_mess("未更新", 2);
		return;
	}
	var pids = [], id;
	$.each($(dtli_duty[dtli_index].id).children(), function(k, v) {
		id = v.id.substring(5);
		if(id != "") pids.push(id);
	});
	dtli_duty[dtli_index].dids = pids;
	var ids = "";
	$.each(dtli_duty, function(key, duty){
		if(duty.dids.length == 0) ids = ids.concat("0");
		else ids = ids.concat(duty.dids.join("a"));
		ids = ids.concat("A");
	});
	$.getJSON("duty_change_js.action?json=" + ids + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) index_login();
		} else {
			index_mess("更新成功！", 2);
			dtli_var.change = false;
		}
	});
}
/**
 * 初始化页面
 */
function dtli_loadDuty() {
	var str = '', treeNode;
	$.each(dtli_duty, function(key, duty){
		str = str + '<div class="dtli_a">' +
			'<div class="dtli_b">' + duty.name + '</div>' +
			'<div id="' + duty.id.substring(1) + '" class="dtli_dpm" onclick="dtli_chg(' +
			key + ')">';
		$.each(duty.dids, function(k, did) {
			treeNode = dtli_treeObj.getNodesByFilter(function(n){return(n.id==did);}, true);
			if(treeNode) {
				str = str + '<div id="dtli_' + treeNode.id + '">' + treeNode.name + '</div>';
				//只添加一值的部门
				if(key == 0) dtli_treeObj.checkNode(treeNode, true, true);
			}
		});
		str = str + '</div></div>';
	});
	$("#dtli_left").html(str);
	$(dtli_duty[0].id).addClass("dtli_c");
}
/**
 * 切换值班选择
 */
function dtli_chg(index) {
	if(dtli_index == index) return;
	$(dtli_duty[index].id).addClass("dtli_c");
	dtli_treeObj.checkAllNodes(false);
	//选中新部门
	$.each(dtli_duty[index].dids, function(k, did) {
		dtli_treeObj.checkNode(
				dtli_treeObj.getNodesByFilter(function(n){return(n.id==did);}, true), true, true);
	});
	//保存旧部门
	var ids = [], id;
	$.each($(dtli_duty[dtli_index].id)
			.removeClass("dtli_c").children(), function(k, v) {
		id = v.id.substring(5);
		if(id != "") ids.push(id);
	});
	dtli_duty[dtli_index].dids = ids;
	dtli_index = index;
	
}
