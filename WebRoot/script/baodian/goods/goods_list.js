var gdli_setting = {
	edit: {
		enable : true,
		showRenameBtn : false
	}, data : {
		simpleData : {
			enable : true
		}
	}, view: {
		addHoverDom: gdli_addHoverDom,
		removeHoverDom: gdli_removeHoverDom
	
	}, callback: {
		beforeRemove: gdli_beforeRemove,
		onClick: function (event, treeId, treeNode) {
		    $("#gdli_id").val(treeNode.id);
		    $("#gdli_tid").val(treeNode.tId);
		    $("#gdli_name").val(treeNode.name);
		    $("#gdli_detail").val(treeNode.detail);
		}
	}
};

var gdli_treeObj = $.fn.zTree.init($("#gdli_treeDemo"), gdli_setting, gdli_zNodes);

function gdli_beforeRemove(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.selectNode(treeNode);
	if(treeNode.isParent) {
		index_mess("下一层存在有设备！", 3);
		return false;
	}
	var isSucc = false;
	if(confirm("确认删除《" + treeNode.name + "》吗？")) {
		index_mess("删除中...", 0);
		$.ajax({
			async: false,
			url: "goods_remove_js.action?json=" + treeNode.id + "&_=" + Math.random(),
			dataType: "json",
			success: function(data) {
				if(data.status == "0") {
					$("#gdli_id").val("");
					$("#gdli_tid").val("");
					$("#gdli_name").val("");
					$("#gdli_detail").val("");
					index_mess(data.mess, 2);
					isSucc = true;
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			}
		});
	}
	return isSucc;
}
function gdli_changeAuth() {
	index_mess("更新中...", 0);
	var id = $("#gdli_id").val();
	if(id == "") {
		index_mess("请先选择设备！", 1);
		return;
	}
	var name = $("#gdli_name").val();
	if(name == "") {
		index_mess("请先输入简要信息！", 1);
		$("#gdli_name").focus();
		return;
	}
	if(name.length > 20) {
		index_mess("简要信息字数不能超过20个！", 1);
		$("#gdli_name").focus();
		return;
	}
	var detail = $("#gdli_detail").val();
	if(detail.length > 100) {
		index_mess("详细信息字数不能超过100个！！", 1);
		$("#gdli_detail").focus();
		return;
	}
	$.post("goods_change_js.action?_=" + Math.random(), {
		"goods.id": id, 
		"goods.name": name, 
		"goods.detail": detail
	}, function(data) {
		if(data.status == 0) {
			var node = gdli_treeObj.getNodeByTId($("#gdli_tid").val());
			node.name = name;
			node.detail = detail;
			gdli_treeObj.updateNode(node);
			gdli_addHoverDom(0, node);
			var old = gdli_findNodeById(id);
			old.name = name;
			old.detail = detail;
			index_mess("更改成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
function gdli_addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if($("#gdli_addBtn_" + treeNode.id).length > 0)
		return;
	var addStr = "<span id='gdli_addBtn_" + treeNode.id + "' class='button add' title='增加'></span>";
	sObj.append(addStr);
	var btn = $("#gdli_addBtn_" + treeNode.id);
	if(btn) {
		btn.bind("click", function() {
			$('#gdli_dd').dialog('open').dialog('setTitle', "添加设备到《"+treeNode.name+"》下");
			$('#gdli_form').form('clear');
			gdli_addNode = treeNode;
		});
	}
};
function gdli_removeHoverDom(treeId, treeNode) {
	$("#gdli_addBtn_" + treeNode.id).unbind().remove();
};
var gdli_addNode;
$('#gdli_form').form();
/**
 * 打开添加到顶部框
 */
function gdli_addAuth() {
	$('#gdli_dd').dialog('open').dialog('setTitle','添加设备种类到顶层');
	$('#gdli_form').form('clear');
	gdli_addNode = null;
}
/**
 * 更新顺序
 */
function gdli_changeNodes() {
	index_mess("更新中...", 0);
	var nodes = gdli_treeObj.transformToArray(gdli_treeObj.getNodes());
	var newNodes = new Array();
	var zNodes = new Array();
	var newNode;
	$.each(nodes, function(key, node) {
		//var newNode = {"id": node.id, "pid": -1, "check": -1, "sort": -1};
		newNode = {"id": node.id, "pid": -1, "sort": -1};
		if(key<gdli_zNodes.length && node.id==gdli_zNodes[key].id) {
			if(gdli_zNodes[key].sort != key)
				newNode.sort = key;
			if(node.pId != gdli_zNodes[key].pId) {
				if(node.pId == null)
					newNode.pid = 0;
				else
					newNode.pid = node.pId;
			}
			/*if(gdli_zNodes[key].checked) {
				if(!node.checked && !node.getCheckStatus().half) {
					newNode.check = 0;
				}
			} else {
				if(node.checked || node.getCheckStatus().half) {
					newNode.check = 1;
				}
			}*/
		} else {
			var oldNode = gdli_findNodeById(node.id);
			if(oldNode == null) {
				newNode.sort = key;
				newNode.pid = node.pId;
				//newNode.check = node.checked ? 1 : 0;
			} else {
				if(oldNode.sort != key)
					newNode.sort = key;
				if(node.pId != oldNode.pId) {
					if(node.pId == null)
						newNode.pid = 0;
					else
						newNode.pid = node.pId;
				}
				/*if(oldNode.checked) {
					if(!node.checked && !node.getCheckStatus().half) {
						newNode.check = 0;
					}
				} else {
					if(node.checked || node.getCheckStatus().half) {
						newNode.check = 1;
					}
				}*/
			}
		}
		newNodes.push(newNode);
		zNodes.push({
			id: node.id,
			name: node.name,
			detail: node.detail,
			sort: key,
			pId: node.pId
		});
	});
	var str = "";
	$.each(newNodes, function(key, val) {
		//if(val.pid!=-1 || val.check!=-1 || val.sort!=-1) {
			//str = str + "gds=" + val.id + "_" + val.pid + "_" + val.check + "_" + val.sort + "&";
		if(val.pid!=-1 || val.sort!=-1) {
			str = str + "gds=" + val.id + "_" + val.pid + "_" + val.sort + "&";
		}
	});
	if(str != "") {
		$.post("goods_changeSort_js.action?_=" + Math.random(), str, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false)
					index_login();
			} else {
				index_mess("更新成功！", 2);
				gdli_zNodes = zNodes;
			}
		}, "json");
	} else {
		index_mess("未更新！", 2);
	}
}
function gdli_findNodeById(id) {
	for(var i in gdli_zNodes) {
		if(gdli_zNodes[i].id == id) {
			return gdli_zNodes[i];
		}
	}
	return null;
}

$('#gdli_dd').dialog({
	closed: true,
	buttons:[{
		text:'添加',
		iconCls:'icon-ok',
		handler:function(){
			if(!$("#gdli_form").form('validate')) {
				return;
			}
			index_mess("正在添加...", 0);
			var treeName = $("#gdli_gname").val();
			var detail = $("#gdli_gdetail").val();
			var params = {
					"goods.name": treeName,
					"goods.detail": detail,
					"goods.sort": gdli_zNodes.length};
			if(gdli_addNode) {
				params["goods.parent.id"] = gdli_addNode.id;
			}
			$.post("goods_add_js.action?_=" + Math.random(), params, function(data) {
				if(data.status == 0) {
					var newNode = {
						id: data.id,
						name: treeName,
						detail: detail,
						sort: gdli_zNodes.length,
						checked: true
					};
					if(gdli_addNode) {
						//刚添加必须将isParent设置为false，否则会出现添加两个叶子节点的现象
						newNode.pId = gdli_addNode.id;
						newNode.isParent = false;
						gdli_treeObj.addNodes(gdli_addNode, newNode);
					} else {
						gdli_treeObj.addNodes(null, newNode);
					}
					gdli_zNodes.push(newNode);
					index_mess("添加成功！", 2);
					$('#gdli_dd').dialog('close');
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			}, "json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#gdli_dd').dialog('close');
		}
	}]
});