var ddli_setting = {
	edit: {
		enable: true
	}, data: {
		simpleData: {
			enable: true
		}
	}, view: {
		addHoverDom: ddli_addHoverDom,
		removeHoverDom: ddli_removeHoverDom
	}, callback: {
		beforeRemove: ddli_beforeRemove,
		beforeRename: ddli_beforeRename
	}
};
var ddli_treeObj = $.fn.zTree.init($("#ddli_treeDemo"), ddli_setting, ddli_zNodes);

function ddli_beforeRemove(treeId, treeNode) {
	ddli_treeObj.selectNode(treeNode);
	if(treeNode.isParent) {
		index_mess("下一层存在有目录！", 3);
		return false;
	}
	var isSucc = false;
	if(confirm("确认删除《" + treeNode.name + "》吗？")) {
		index_mess("删除中...", 0);
		$.ajax({
			async: false,
			url: "docdir_remove_js.action?json=" + treeNode.id + "&_=" + Math.random(),
			dataType: "json",
			success: function(data) {
				if(data.status == 0) {
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
function ddli_beforeRename(treeId, treeNode, newName) {
	if (newName.length == 0) {
		index_mess("目录名称为空！", 4);
		ddli_cancelEdit();
		return false;
	}
	if(treeNode.name == newName) {
		return true;
	}
	if(confirm("更改《" + treeNode.name + "》为《" + newName + "》吗？")) {
		index_mess("更改中...", 0);
		var isSucc = false;
		$.ajax({
			async: false,
			url: "docdir_change_js.action?_=" + Math.random(),
			data: {
				"docdir.id": treeNode.id,
				"docdir.name": newName
			}, type: "post", dataType: "json",
			success: function(data) {
				if(data.status == "0") {
					index_mess("更改成功！", 2);
					isSucc = true;
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();				
				}
			}
		});
		if(isSucc) {
			return true;
		} else {
			ddli_cancelEdit();
			return false;
		}
	} else {
		ddli_cancelEdit();
		return false;
	}
}
/**
 * 取消更改，一定要延时更改，否则会使ddli_addHoverDom失效
 */
function ddli_cancelEdit() {
	setTimeout(function() {
		ddli_treeObj.cancelEditName();
	}, 100);
}
function ddli_addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if(treeNode.editNameFlag || $("#ddli_addBtn_" + treeNode.id).length > 0)
		return;
	var addStr = "<span id='ddli_addBtn_" + treeNode.id + "' class='button add' title='增加'></span>";
	sObj.append(addStr);
	var btn = $("#ddli_addBtn_" + treeNode.id);
	if(btn) {
		btn.bind("click", function() {
			$.messager.prompt("添加目录", "请输入添加在《"+treeNode.name+"》下的目录名称：", function(treeName){
				if(treeName){
					index_mess("添加中...", 0);
					$.ajax({
						url: "docdir_add_js.action?_=" + Math.random(),
						data: {
							"docdir.name": treeName,
							"docdir.sort": ddli_zNodes.length,
							"docdir.parent.id": treeNode.id
						},type: "post", dataType: "json",
						success: function(data) {
							if(data.status == 0) {
								//刚添加必须将isParent设置为false，否则会出现添加两个叶子节点的现象
								var newNode = {
									id: data.id,
									name: treeName,
									sort: ddli_zNodes.length,
									pId: treeNode.id,
									isParent : false
								};
								ddli_treeObj.addNodes(treeNode, newNode);
								ddli_zNodes.push(newNode);
								index_mess("添加成功！", 2);
							} else {
								index_mess(data.mess, 1);
								if(data.login == false) {
									index_login();
								}
							}
						}
					});
				}
			});
		});
	}
};
function ddli_removeHoverDom(treeId, treeNode) {
	$("#ddli_addBtn_" + treeNode.id).unbind().remove();
}
/**
 * 添加到顶层
 */
function ddli_add() {
	$.messager.prompt("添加目录", "请输入添加到根目录下的目录名称：", function(treeName){
		if(treeName) {
			index_mess("添加中...", 0);
			$.ajax({
				url: "docdir_add_js.action?_=" + Math.random(),
				data: {
					"docdir.name": treeName,
					"docdir.sort": ddli_zNodes.length,
				}, type: "post", dataType: "json",
				success: function(data) {
					if(data.status == 0) {
						var newNode = {
							id: data.id,
							name: treeName,
							sort: ddli_zNodes.length
						};
						ddli_treeObj.addNodes(null, newNode);
						ddli_zNodes.push(newNode);
						index_mess("添加成功！", 2);
					} else {
						index_mess(data.mess, 1);
						if(data.login == false)
							index_login();
					}
				}
			});
		}
	});
}
/**
 * 更新顺序
 */
function ddli_changeNodes() {
	index_mess("更新中...", 0);
	var nodes = ddli_treeObj.transformToArray(ddli_treeObj.getNodes());
	var newNodes = new Array();
	var zNodes = new Array();
	var newNode;
	$.each(nodes, function(key, node) {
		newNode = {"id": node.id, "pId": -1, "sort": -1};
		if(key<ddli_zNodes.length && node.id==ddli_zNodes[key].id) {
			if(ddli_zNodes[key].sort != key)
				newNode.sort = key;
			if(node.pId != ddli_zNodes[key].pId) {
				if(node.pId == null)
					newNode.pId = 0;
				else
					newNode.pId = node.pId;
			}
		} else {
			var oldNode = ddli_findNodeById(node.id);
			if(oldNode == null) {
				newNode.sort = key;
				newNode.pId = node.pId;
			} else {
				if(oldNode.sort != key)
					newNode.sort = key;
				if(node.pId != oldNode.pId) {
					if(node.pId == null)
						newNode.pId = 0;
					else
						newNode.pId = node.pId;
				}
			}
		}
		newNodes.push(newNode);
		zNodes.push({
			id: node.id,
			name: node.name,
			sort: key,
			pId: node.pId
		});
	});
	var str = "";
	$.each(newNodes, function(key, val) {
		if(val.pId!=-1 || val.sort!=-1) {
			str = str + "dds=" + val.id + "_" + val.pId + "_" + val.sort + "&";
		}
	});
	if(str != "") {
		$.post("docdir_changeSort_js.action?_=" + Math.random(), str, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false)
					index_login();
			} else {
				index_mess("更新成功！", 2);
				ddli_zNodes = zNodes;
			}
		}, "json");
	} else {
		index_mess("未更新！", 2);
	}
}
function ddli_findNodeById(id) {
	for(var i in ddli_zNodes) {
		if(ddli_zNodes[i].id == id) {
			return ddli_zNodes[i];
		}
	}
	return null;
}