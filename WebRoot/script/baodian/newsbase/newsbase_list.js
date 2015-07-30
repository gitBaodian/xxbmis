function nbli_updateActions(index){
	$('#nbli_table').datagrid('updateRow',{index:index,row:{action:''}});
}
function nbli_editrow(id){
	$('#nbli_table').datagrid('beginEdit', $("#nbli_table").datagrid("getRowIndex", id));
}
function nbli_cancelrow(id){
	$('#nbli_table').datagrid('cancelEdit', $("#nbli_table").datagrid("getRowIndex", id));
}
function nbli_saverow(id){
	index_mess("更新中...", 0);
	var index = $("#nbli_table").datagrid("getRowIndex", id);
	var oldrow = $.extend({},$('#nbli_table').datagrid('getRows')[index]);//深度拷贝，而非引用
	$('#nbli_table').datagrid('endEdit', index);
	var newrow = $('#nbli_table').datagrid('getRows')[index];
	if(newrow.editing) {
		index_mess("标题和浏览数不能为空!", 1);
		return;
	}
	var paras = '{';
	var change = false;
	if(oldrow["title"] != newrow["title"]) {
		paras = paras + '"newsbase.title":"'
			+ newrow["title"].replace(/\\/g, '')
				.replace(/"/g, '').replace(/'/g, '')+'",';
		change = true;
	}
	if(oldrow["ncid"] != newrow["ncid"]){
		paras = paras + '"newsbase.nclass.id":"'
			+ newrow["ncid"] + '",';
		change = true;
	}
	if(oldrow["publishtime"].substring(0, 19) != newrow["publishtime"]){
		paras = paras + '"newsbase.publishtime":"'
			+ newrow["publishtime"] + '",';
		change = true;
	}
	if(oldrow["hit"] != newrow["hit"]){
		change = true;
	}
	paras = paras + '"newsbase.hit":"'
		+ newrow["hit"] + '",';
	if(oldrow["display"] != newrow["display"]){
		change = true;
	}
	paras = paras + '"newsbase.display":"'
		+ newrow["display"] + '",';
	if(oldrow["sort"] != newrow["sort"]){
		change = true;
	}
	paras = paras + '"newsbase.sort":"'
		+ newrow["sort"] + '",';
	if(oldrow["reply"] != newrow["reply"]){
		change = true;
	}
	paras = paras + '"newsbase.reply":"'
		+ newrow["reply"] + '",';
	if(change) {//有更新内容
		paras = paras + '"newsbase.id":"'+newrow["id"]+'"}';
	} else {
		index_mess("未更新内容!", 2);
		return;
	}
	$.post("newsbase_change_no.action", $.parseJSON(paras), function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
    		if(data.name != 1)
	    		$('#nbli_table').datagrid('updateRow',{
					index: index,
					row: {
						status: 0,
						reviewer: data.name,
						reviewtime : data.time
					}
				});
		} else {
			index_mess(data.mess, 1);
			oldrow.editing = false;
			$('#nbli_table').datagrid('updateRow', {index:index,row:oldrow});
			if(data.login == false) {
				index_login();
			}
		}
	}, "json");
}
function nbli_openNB(id) {//encodeURI解决中文问题
	//openTab("新闻更新",encodeURI("admin/news/newsextend.jsp?bid="+id+"&title="+title));
	window.open("news_change_rd.action?newsbase.id=" + id);
}
function nbli_reviewNb(ids) {
	index_mess("审核提交中...", 0);
	var index = 0;
	$.getJSON("newsbase_review_js.action?nbIds=" + ids.join('&nbIds=') + "&_=" + Math.random(), function(data) {
		if(data.status == 0) {
    		index_mess(data.mess, 2);
    		$.each(ids, function(k, id) {
    			index = $("#nbli_table").datagrid("getRowIndex", id);
    			$('#nbli_table').datagrid('cancelEdit', index);
    			$('#nbli_table').datagrid('updateRow',{
    				index: index,
    				row: {
    					status: 0,
    					reviewer: data.name,
    					reviewtime : data.time
    				}
    			});
    		});
		} else {
			index_mess("审核成功", 1);
			if(data.login == false) {
				index_login();
			}
		}
	});
}
function nbli_reviewNbs() {
	var rows = $("#nbli_table").datagrid("getSelections");
	if(rows.length == 0) {
		index_mess("请先选择新闻！", 4);
		return;
	}
	var ids = [];
	for(var i=0;i<rows.length;i++){
		if(rows[i].status == 1)
			ids.push(rows[i].id);
	}
	if(ids.length == 0)
		index_mess("全部已审核！", 4);
	else
		nbli_reviewNb(ids);
}
/**
 * 批量置顶
 * @param sort 0-取消 1-置顶
 */
function nbli_SortNbs(sort) {
	var rows = $("#nbli_table").datagrid("getSelections");
	if(rows.length == 0) {
		index_mess("请先选择新闻！", 4);
		return;
	}
	var ids = [];
	for(var i=0;i<rows.length;i++){
		if(rows[i].sort != sort)
			ids.push(rows[i].id);
	}
	if(ids.length == 0)
		index_mess("全部已置顶！", 4);
	else {
		var index = 0;
		index_mess("更改中...", 0);
		$.getJSON("newsbase_sort_js.action?json="+sort+"&nbIds=" + ids.join('&nbIds=') + "&_=" + Math.random(), function(data) {
			if(data.status == 0) {
	    		index_mess("更改成功", 2);
	    		$.each(ids, function(k, id) {
	    			index = $("#nbli_table").datagrid("getRowIndex", id);
	    			$('#nbli_table').datagrid('cancelEdit', index);
	    			$('#nbli_table').datagrid('updateRow',{
	    				index: index,
	    				row: {
	    					sort: sort,
	    					status: 0,
	    					reviewer: data.name,
	    					reviewtime : data.time
	    				}
	    			});
	    		});
			} else {
				index_mess(data.mess, 1);
				if(data.login == false) {
					index_login();
				}
			}
		});
	}
}
/**
 * 批量删除新闻
 */
function nbli_removeNb(ids){
	$.messager.confirm('提示', '删除新闻后，对应的评论也将会删除，确定要删除吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("newsbase_remove_js.action?nbIds=" + ids.join('&nbIds=') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					/*$.each(ids, function(k, id) {
		    			var index = $("#nbli_table").datagrid("getRowIndex", id);
		    			$('#nbli_table').datagrid('deleteRow', index);
		    		});*/
					$('#nbli_table').datagrid('reload');
		    		index_mess("删除成功!", 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    			if(data.login == false) {
	    				index_login();
	    			}
	    		}
	    	});
		}
	});
}
function nbli_removeNbs() {
	var rows = $("#nbli_table").datagrid("getSelections");
	if(rows.length == 0) {
		index_mess("请先选择新闻！", 4);
		return;
	}
	var ids = [];
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
	}
	nbli_removeNb(ids);
}

$('#nbli_table').datagrid({
	url: 'newsbase_list_js.action?_=' + Math.random(),
    iconCls: "icon-edit",
    title: "新闻列表",
    striped: true,//条纹
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#nbli_toolbar',//工具条
    queryParams: {
    	"page.title": {"id":"nbli_title","type":"text"},
    	"page.ncId": {"id":"nbli_nclass","type":"combobox"},
    	"page.display": {"id":"nbli_display","type":"combobox"},
    	"page.sort": {"id":"nbli_sort","type":"combobox"},
    	"page.reply": {"id":"nbli_reply","type":"combobox"},
    	"page.status": {"id":"nbli_status","type":"combobox"}
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    idField: "id",
    frozenColumns:[[
        {field: "id", hidden: true},
        {field: "index", title: "行号", hidden: true},
        {field: 'ck', checkbox:true},
        {field: "title", title: "标题", width: 220,
        	editor: {type:'validatebox',options:{required:true,validType:'length[1,100]'}}},
    ]],
    columns: [[
	   {title:'基本信息', colspan:4},
	   {title:'设置', colspan:4},
	   {title:'审核', colspan:3},
	   {field: 'action', title: '操作', rowspan:2, width:100, align: 'center',
			formatter: function(value,row,index){
				var u = '<a href="javascript:;" onclick="nbli_openNB('+row.id+')">打开</a>';
				if (row.editing){
					var s = '<a href="javascript:;" onclick="nbli_saverow('+row.id+')">保存</a>&nbsp;&nbsp;';
					var c = '<a href="javascript:;" onclick="nbli_cancelrow('+row.id+')">取消</a>&nbsp;&nbsp;';
					return s + c + u;
				} else {
					var e = '<a href="javascript:;" onclick="nbli_editrow('+row.id+')">编辑</a>&nbsp;&nbsp;';
					var d = '<a href="javascript:;" onclick="nbli_removeNb(['+row.id+'])">删除</a>&nbsp;&nbsp;';
					return e + d + u;
				}
			}}
	],[
	   {field: "ncid", title: "新闻类型", width: 90, align: "center",
		   editor: {type:'combobox',options:{valueField:'id',textField:'name',data:nbli_ncs,editable:false,panelHeight:120}},
		   formatter: function(value){
			   for(var i=0; i<nbli_ncs.length; i++){
				   if (nbli_ncs[i].id == value) return nbli_ncs[i].name;
			   }
			   return value;
		   }},
	   {field: "author", title: "作者", width: 70, align: "center"},
	   {field: "publishtime", title: "发表时间", width: 138, align: "center", editor: {type:'datetimebox',options:{editable:false}},
		   formatter: function(value){
			   return value.substring(0, 19);
		   }},
	   {field: "hit", title:"浏览数", width: 80, align: "center", editor:{type:'numberbox',options:{required:true,min:1,max:2147483647}}},
	   
	   {field: "display", title: "是否显示", width: 60, align: "center", editor: {type:'checkbox',options:{on:'1',off:'0'}},
		   formatter: function(value){
			   if(value == 1)
				   return "显示";
			   return "";
		   }},
	   {field: "sort", title: "是否置顶", width: 60, align: "center", editor: {type:'checkbox',options:{on:'1',off:'0'}},
		   formatter: function(value){
			   if(value == 1)
				   return "置顶";
			   return "否";
		   }},
	   {field: "reply", title: "是否评论", width: 60, align: "center", editor: {type:'checkbox',options:{on:'1',off:'0'}},
		   formatter: function(value){
			   if(value == 1)
				   return "允许";
			   return "关闭";
		   }},
	   {field: "replynum", title: "评论次数", width: 60, align: "center"},
	   
	   {field: "status", title: "是否审核", width: 60, align: "center",
		   formatter: function(value,row,index){
			   if(value == 1) {
				   return '<a href="javascript:;" onclick="nbli_reviewNb(['+row.id+'])">点击审核</a>&nbsp;&nbsp;';
			   } else return "已审";
			}},
	   {field: "reviewer", title: "审核人", width: 70, align: "center"},
	   {field: "reviewtime", title: "审核时间", width: 128, align: "center",
		   formatter: function(value){
			   if(value == "null")
				   return "";
			   return value.substring(0, 19);
		   }}
    ]],
	onDblClickRow: function(index,row) {
		if(!row.editing)
			$('#nbli_table').datagrid('beginEdit', index);
	},
	onBeforeEdit:function(index,row){
		row.editing = true;
		nbli_updateActions(index);
	},
	onAfterEdit:function(index,row){
		row.editing = false;
		nbli_updateActions(index);
	},
	onCancelEdit:function(index,row){
		row.editing = false;
		nbli_updateActions(index);
	}
});