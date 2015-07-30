var url;
function wr_newRecord(){
	
	$('#wr_type').combobox('reload','script/record/wr_add_type.json');
	
	$('#wr_dlg').dialog('open').dialog('setTitle','添加记录');
	$('#wr_fm').form('clear');
	$('#wr_time_div').show();
	$('#wrli_npspan').show();
	$('#wr_time').datetimebox('setValue',GetCurrentTime());
	url = 'WRecord_update.action?json='+Math.random();
}
function wr_editRecord(){
	var row = $('#wr_record').datagrid('getSelected');
	if(row){
		$.post('WRecord_IfUserHaveAuthority_no.action?json='+Math.random(),{wr_id:row.record_id},function(result){
			if (result.status == 0){    //有权限
				$('#wr_type').combobox('reload','script/record/wr_add_type.json');
				$('#wr_fm').form('clear');
				$('#wr_time_div').hide();
				$('#wrli_npspan').hide();
				$('#wr_dlg').dialog('open').dialog('setTitle','编辑记录');
				var detail = row.detail;
				row.detail = detail.replace(/<[^>].*?>/g,"");   //编辑的时候去掉 html 标签
				$('#wr_fm').form('load',row);
				row.detail = detail;    //恢复 html 标签
				url = 'WRecord_update.action?wr_id='+row.record_id;
			} else {   
				$.messager.show({	// show error message
					title: 'Error',
					msg: result.mess
				});
			}
		},'json');
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
}
function wr_saveRecord(){
	$('#wr_fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.status == "0"){
				$('#wr_dlg').dialog('close');		// close the dialog
//				$('#wr_record').datagrid('reload');	// reload the user data
				//添加完后，刷新到当天记录，因为用户在查询状态下添加的话会停留在查询条件的数据中，看不到新添加的数据
				 wr_find("today_data");  //查找今天的记录
				 $('#wr_from_date').datebox('setValue',GetCurrentDate());
				 $('#wr_tb_dept').combotree('setValue','');
			} else {
				$.messager.show({
					title: 'Error',
					msg: result.mess
				});
				$('#wr_dlg').dialog('close');
				$('#wr_record').datagrid('reload');
			}
		}
	});
}
function wr_removeRecord(){
	var row = $('#wr_record').datagrid('getSelected');
	if(row){
		if(row.type == "机器故障"){
			$.messager.show({	
				title: 'Error',
				msg: '请先删除对应的故障记录，系统会自动删除该运行记录'
			});
			return;
		}
		$.post('WRecord_IfUserHaveAuthority_no.action?json='+Math.random(),{wr_id:row.record_id},function(result){
			if (result.status == 0){    //有权限
					$.messager.confirm('Confirm','确定删除这条记录?',function(r){
						if (r){
							$.post('WRecord_del_no.action',{wr_id:row.record_id},function(result){
								if (result.status == 0){
									$('#wr_record').datagrid('load');	// reload data
								} else {
									$.messager.show({	// show error message
										title: 'Error',
										msg: result.mess
									});
								}
							},'json');
						}
					});
			} else {
				$.messager.show({	
					title: 'Error',
					msg: result.mess
				});
			}
		},'json');
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
	
}

function wr_find(arg){	
	var from_date = $('#wr_from_date').datebox('getValue');
	var to_date = $('#wr_to_date').datebox('getValue');
	var username = $('#wr_tb_user').combobox('getValue');
	var dept_name = "";
	/*var t = $('#wr_tb_dept').combotree('tree');
	var n = t.tree('getSelected');*/
	/*if(n != null){
		dept_name = n.text;
	}*/
	dept_name = $('#wr_tb_dept').combotree('getText');
	if(dept_name == "全部"){
		dept_name = "";
	}
	
	if(arg == "today_data"){    //参数从 wr_saveRecord() 函数传过来，表示获取当天记录
		from_date = GetCurrentDate();
		dept_name = "";
	}
	//alert(dept_name);
	var type = $('#wr_tb_type').combobox('getValue');
	
	$('#wr_record').datagrid('options').url="WRecord_searchByCondition.action";
	var fields = $('#wr_record').datagrid('getColumnFields',false);
	$('#wr_record').datagrid('getColumnOption',fields[0]).formatter = '';  //清除高亮
	$('#wr_searchbox').searchbox('setValue','');
	
	//查询参数直接添加在queryParams中 
	var queryParams = $('#wr_record').datagrid('options').queryParams;
	queryParams.from_date = from_date;
	queryParams.to_date = to_date;
	queryParams.dept_name = dept_name;
	queryParams.username = username;
	queryParams.type = type;
	$('#wr_record').datagrid('options').queryParams = queryParams;
	$('#wr_record').datagrid('load');
		
}

$('#wr_todate_span').hide();
$('#wr_date_type').combobox({
	onSelect:function(record){
		//alert(record.text);
		if(record.text == "日期"){
			$('#wr_from_date').datebox('setValue',GetCurrentDate());
			$('#wr_to_date').datebox('setValue','');
			wr_find("today_data");   // 重载今日数据
			$('#wr_todate_span').hide();
		}else if(record.text == "自定义"){
			$('#wr_from_date').datebox('setValue','');
			$('#wr_to_date').datebox('setValue','');
			$('#wr_todate_span').show();
		}
	}
});

$('#wr_tb_dept').combotree({    //部门与记录人不可同时选
	onSelect:function(record){
		$('#wr_tb_user').combobox('clear');  //清除选中的记录人
	}
});

$('#wr_tb_user').combobox({    //部门与记录人不可同时选
	onSelect:function(record){
		$('#wr_tb_dept').combotree('clear');  //清除选中的部门
	}
});

$('#wr_searchbox').searchbox({   
    width:150,   
    searcher:function(keyword){   
    	//置空查询条件
    	if(keyword == ""){
    		$.messager.show({	// show error message
    			title: 'Tip',
    			msg: "请输入关键字"
    		});
    	}else{  //关键字不为空
    		$('#wr_from_date').datebox('setValue','');
        	$('#wr_tb_type').combobox('setValue','全部');
        	$('#wr_tb_dept').combotree('setValue','');
        	
            //alert(value);
        	$('#wr_record').datagrid('options').url="WRecord_searchByKeyword.action";
        	var queryParams = $('#wr_record').datagrid('options').queryParams;
        	queryParams.keyword = keyword;
        	$('#wr_record').datagrid('options').queryParams = queryParams;
        	/***** 关键字高亮  *****/
    		var fields = $('#wr_record').datagrid('getColumnFields',false);
    		$('#wr_record').datagrid('getColumnOption',fields[0]).formatter = 
    			 					function(value, rowData, rowIndex){
    									value = value.replace(keyword,"<font color=red>"+keyword+"</font>");
    									return value;
    								};
        	$('#wr_record').datagrid('load');
    	}
    },     
    prompt:'内容关键字搜索'  
}); 

function GetCurrentTime(){
	var date = new Date();

	var yyyy = date.getFullYear();
	var m = date.getMonth() + 1;
	var MM = (m < 10) ? '0' + m : m;
	var d  = date.getDate();
	var dd = (d < 10) ? '0' + d : d;
	var h = date.getHours();
	var HH = (h < 10) ? '0' + h : h;
	var n = date.getMinutes();
	var mm = (n < 10) ? '0' + n : n;
	var s  = date.getSeconds();
	var ss = (s < 10) ? '0' + s : s;
	
	var str = yyyy+"-"+MM+"-"+dd+" "+HH+":"+mm+":"+ss;
	return str;

}

function GetCurrentDate(){
	var date = new Date();
	var yyyy = date.getFullYear();
	var m = date.getMonth() + 1;
	var MM = (m < 10) ? '0' + m : m;
	var d  = date.getDate();
	var dd = (d < 10) ? '0' + d : d;
	var str = yyyy+"-"+MM+"-"+dd;
	return str;

}

var current_date = GetCurrentDate();
$('#wr_from_date').datebox({    
	onSelect: function(date){
		if(date != ""){
			current_date = date;
		}
	}
});
$('#wr_from_date').datebox('setValue',current_date);   //显示当前日期

//部门下拉树
if($('#login_user').val() != ""){
	usli_init_depm();
}

/**
 * 将ztree转换为easyui树
 */
function usli_init_depm() {
	$.getJSON("department_list_js.action?_=" + Math.random(), function(data) {
		var ztree = data;
		if(ztree.length == 0)
			return;
		var etree = [];
		etree.push({id:"全部",text:"全部"});
		$.each(ztree, function(k, v) {
			if(v.pId == null) {
				etree.push({id:v.id,text:v.name});
			}
		});
		usli_createTree(etree, ztree);
		$('#wr_tb_dept').combotree('loadData', etree);
	});
}
/**
 * 递归从ztree树取数据，然后构建easyui树
 * @param etree
 * @param ztree
 */
function usli_createTree(etree, ztree) {
	$.each(etree, function(ek, ev) {
		$.each(ztree, function(zk, zv) {
			if(zv.pId == ev.id) {
				if(ev.children == null) {
					ev.children = [];
					ev.children.push({id:zv.id,text:zv.name});
				} else {
					ev.children.push({id:zv.id,text:zv.name});
				}
			}
		});
		if(ev.children != null) {
			usli_createTree(ev.children, ztree);
		}
	});
}

//初始化 
var menu = $('#wr_export').menubutton({ menu: '#wr_export_menu' });  
  
//menubutton 依赖于 menu、linkbutton 这两个插件，所以可以用如下代码实现：
$(menu.menubutton('options').menu).menu({ 
            onClick: function (item) { 
                if(item.text == "导出全部"){
                	var form = $("<form>");  		//jquery ajax不支持文件下载，所以使用下面的方法解决文件下载问题
                    form.attr('style','display:none');  
                    form.attr('target','');  
                    form.attr('method','post');  
                    form.attr('action','WRecord_exportAll2Excel.action');  
                    $('body').append(form);  
                    form.submit();  
                    form.remove(); 
                }else if(item.text == "导出当前"){
                	var form = $("<form>");  
                    form.attr('style','display:none');  
                    form.attr('target','');  
                    form.attr('method','post');  
                    form.attr('action','WRecord_exportCurrent2Excel.action');  
                    
                    var input1 = $('<input>');  
                    input1.attr('type','hidden');  
                    input1.attr('name','from_date');  
                    input1.attr('value',$('#wr_from_date').datebox('getValue'));
                    var input2 = $('<input>');  
                    input2.attr('type','hidden');  
                    input2.attr('name','type');  
                    input2.attr('value',$('#wr_tb_type').combobox('getValue'));
                    var input3 = $('<input>');  
                    input3.attr('type','hidden');  
                    input3.attr('name','dept_name');  
                    input3.attr('value',$('#wr_tb_dept').combotree('getText'));
                    form.append(input1);
                    form.append(input2);
                    form.append(input3);
                    $('body').append(form);  
                    form.submit();  
                    form.remove();
                }
            } 
});

