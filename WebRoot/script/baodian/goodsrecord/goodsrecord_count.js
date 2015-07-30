var grct_count = new Array();
grct_data.gdindex = {};//为grct_count的id和数组位移建立的索引
grct_data.dtnums = new Array();
grct_data.dtindex = {};//为grct_count
$.each(grct_data.dts, function(k, dt) {
	grct_data.dtindex[dt.id] = k;
	grct_data.dtnums.push(0);
});
grct_deep(null, 1);
/**
 * 递归构建设备信息 [{name,deep,detail:[]}]
 * @param pId 父节点id
 * @param deep 当前深度
 */
function grct_deep(pId, deep) {
	$.each( grct_data.gds, function(k, goods) {
		if(pId == goods.pId) {
			grct_data.gdindex[goods.id] = grct_count.length;
			grct_count.push({
				name: goods.name,
				deep: deep,
				nums: grct_data.dtnums.concat()
			});
			grct_deep(goods.id, deep+1);
		}
	});
}
$.each(grct_data.records, function(k, v) {
	$.each(v.detail, function(k2, v2) {
		grct_count[grct_data.gdindex[v.id]]["nums"][grct_data.dtindex[v2.id]] = v2.num;
	});
});

var grct_str = "<thead><tr><th data-options=\"field:'deep',hidden:true\">设备</th>" +
		"<th data-options=\"field:'shebei',width:15\">设备</th>";
$.each(grct_data.dts, function(k, dt) {
	grct_str += "<th data-options=\"field:'ut" + dt.id + "',width:10,align:'center'\">" + dt.name + "</th>";
});
grct_str += "<th data-options=\"field:'tongji',width:10,align:'center'\">统计</th></tr></thead><tbody>";

$.each(grct_count, function(k1, v1) {
	grct_str += "<tr><td>" + v1.deep + "</td><td>" + v1.name + "</td>";
	if(v1.deep == 1) {
		grct_str += "</tr>";
	} else {
		var num = 0;
		$.each(v1.nums, function(k2, v2) {
			num += v2;
			grct_str += "<td>" + v2 + "</td>";
		});
		grct_str += "<td>" + num + "</td></tr>";
	}
});
$("#grct_table").html(grct_str + "</tbody>");
$('#grct_table').datagrid({
    title: "设备统计",
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    singleSelect: true,//单选
    rowStyler:function(index, row){
		if(row.deep > 1){
			return 'background-color:#EEEEFF;';
		} else {
			return 'background-color:#FFFFFF;font-weight:bold;';
		}
	}
});
