//$(function(){
			$('#tr_record').datagrid({
				onDblClickRow:function(rowIndex){
					var row = $('#tr_record').datagrid('getSelected');
					//$.messager.alert('记录详情', "故障原因："+row.detail+"<br><br> 解决方法："+row.solve_approach,'');
					$('#tr_dlg_detail').html("<strong>故障原因：</strong><br>"+row.detail+"<br><br><strong>解决方法：</strong><br>"+row.solve_approach);
					$('#tr_detail_dlg').dialog('open').dialog('setTitle','记录详情');
				}
			});
			    
//*************状态触发
		    $('#tr_state').combobox({
		    	onSelect:function(record){
		    		if(record.text == "未解决"){
		    			$('#tr_solve_approach').attr("disabled",true);
		    			$('#tr_solve_approach').validatebox({required:false});
		    			$('#tr_solve_approach').focus();
		    			$('#tr_solve_approach').attr("value","未解决");
		    			$('#tr_s_time').datetimebox('setValue','');
		    			$('#tr_s_time').datetimebox({required:false});
		    			$('#tr_s_time').datetimebox('disable');
		    			$('#tr_s_username').combobox('setValue','');
		    			$('#tr_s_username').combobox({required:false});
		    			$('#tr_s_username').combobox('disable');
		    		}
		    		if(record.text == "已解决"){
		    			$('#tr_solve_approach').attr("disabled",false);
		    			$('#tr_solve_approach').attr("value",null);
		    			$('#tr_s_time').datetimebox('enable');
		    			$('#tr_s_username').combobox('enable');
		    			
		    			$('#tr_solve_approach').validatebox({required:true});
		    			$('#tr_solve_approach').focus();
		    			$('#tr_s_time').datetimebox({required:true});
		    			$('#tr_s_username').combobox({required:true});
		    		}
		    	}
		    });
//});

var url;
function tr_newRecord(){
	$('#tr_fm').form('clear');
	//添加记录对话框里的combobox获取下拉列表
	$('#tr_ip').combobox('reload','TRecord_getIpType.action');
	$('#tr_state').combobox('reload','script/record/tr_add_state.json');  
	$('#tr_f_username').combobox('reload','user_getAllUserName.action');  
	$('#tr_s_username').combobox('reload','user_getAllUserName.action');
	$('#tr_state').combobox('setValue',"未解决");
	$('#tr_f_time').datebox('setValue',GetCurrentTime());
	$('#tr_f_username').combobox('setValue',$('#login_user').val());
	
	$.post('TRecord_IfUserHaveAuthority_no.action',{tr_id:0},function(result){
		if (result.status == 0){    //有权限
			$('#tr_dlg').dialog('open').dialog('setTitle','添加记录');

			
			url = 'TRecord_update.action?json='+Math.random();
		} else {
			index_mess(result.mess);
			if(result.login == false) {
				index_login();
			}
		}
	},'json');
}
function tr_editRecord(){
	var row = $('#tr_record').datagrid('getSelected');
//	var flag = tr_if_can_change(row);   //能否修改
	if (row){
		$.post('TRecord_IfUserHaveAuthority_no.action',{tr_id:row.record_id},function(result){
			if (result.status == 0){    //有权限
				$('#tr_dlg').dialog('open').dialog('setTitle','编辑记录');
				$('#tr_fm').form('clear');
				
				//下拉列表重新加载
				$('#tr_state').combobox('reload','script/record/tr_add_state.json');  
				$('#tr_f_username').combobox('reload','user_getAllUserName.action');  
				$('#tr_s_username').combobox('reload','user_getAllUserName.action');
				
				var detail = row.detail;
				row.detail = detail.replace(/<[^>].*?>/g,"");   //编辑的时候去掉 html 标签
				var solve_approach = row.solve_approach;
				row.solve_approach = solve_approach.replace(/<[^>].*?>/g,"");   //编辑的时候去掉 html 标签
				$('#tr_fm').form('load',row);
				row.detail = detail; //恢复 html
				row.solve_approach = solve_approach;
				
				$('#tr_ip').combobox('setValue',row.IP);
				$('#tr_f_username').combobox('setValue',row.f_user);
				$('#tr_s_username').combobox('setValue',row.s_user);
				if(row.state == "未解决"){
					$('#tr_solve_approach').attr("disabled",true);
					$('#tr_solve_approach').attr("value","未解决");
					$('#tr_s_time').datetimebox('disable');
					$('#tr_s_username').combobox('disable');
				}else{
					$('#tr_solve_approach').attr("disabled",false);
					$('#tr_s_time').datetimebox('enable');
					$('#tr_s_username').combobox('enable');
				}
				url = 'TRecord_update.action?tr_id='+row.record_id;
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

function tr_saveRecord(){
	$('#tr_fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.status == 0){
				$('#tr_dlg').dialog('close');		// close the dialog
//				$('#tr_record').datagrid('reload');	// reload the user data
				tr_find("all_data");
				$('#tr_from_date').datebox('setValue',"");
				$('#tr_to_date').datebox('setValue',"");
				$('#tr_search_ip').combobox('setValue',"");
				$('#wr_record').datagrid('reload');    // 刷新运行记录， 后台添加故障记录会同时添加运行记录
			} else {
				$.messager.show({
					title: 'Error',
					msg: result.mess
				});
				$('#tr_dlg').dialog('close');
				$('#tr_record').datagrid('reload');
			}
		}
	});
}

function tr_removeRecord(){
	var row = $('#tr_record').datagrid('getSelected');
	if (row){
		$.post('TRecord_IfUserHaveAuthority_no.action',{tr_id:row.record_id},function(result){
			if (result.status == 0){    //有权限
				$.messager.confirm('Confirm','确定删除这条记录?',function(r){
					if (r){
						$.post('TRecord_del_no.action',{tr_id:row.record_id},function(result){   //删除
							if (result.status == 0){
								$('#tr_record').datagrid('load');	// reload data
								$('#wr_record').datagrid('reload');
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

function tr_find(arg){
	var from_date = $('#tr_from_date').datebox('getValue');
	var to_date = $('#tr_to_date').datebox('getValue');
	var type = $('#tr_tb_type').combobox('getValue');
	var search_ip = $('#tr_search_ip').combobox('getValue');
	
	if(arg == "all_data"){    //参数从 tr_saveRecord() 函数传过来，表示获取当天记录
		from_date = "";
		to_date = "";
		search_ip = "";
	}
	
	$('#tr_record').datagrid('options').url="TRecord_list.action?flag=list";
	$('#tr_searchbox').searchbox('setValue','');
	
	//查询参数直接添加在queryParams中 
	var queryParams = $('#tr_record').datagrid('options').queryParams;
	queryParams.from_date = from_date;
	queryParams.to_date = to_date;
	queryParams.state = type;
	queryParams.ip = search_ip;
	$('#tr_record').datagrid('options').queryParams = queryParams;
	$('#tr_record').datagrid('load');	
}

$('#tr_searchbox').searchbox({   
    width:150,   
    searcher:function(value){   
    	//置空查询条件
    	if(value == ""){
    		$.messager.show({	// show error message
    			title: 'Tip',
    			msg: "请输入关键字"
    		});
    	}else{  //关键字不为空
    		$('#tr_from_date').datebox('setValue','');
        	$('#tr_to_date').datebox('setValue','');
        	$('#tr_tb_type').combobox('setValue','全部');
        	$('#tr_search_ip').combobox('setValue','');
        	
        	$('#tr_record').datagrid('options').url="TRecord_searchByKeyword.action";
        	var queryParams = $('#tr_record').datagrid('options').queryParams;
        	queryParams.keyword = value;
        	$('#tr_record').datagrid('options').queryParams = queryParams;
        	$('#tr_record').datagrid('load');
    	}
    },     
    prompt:'故障原因关键字搜索'  
});

function GetCurrentTime(){   //获取当前时间
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

function tr_setIP(){
	$('#tr_setIP_dlg').dialog('open').dialog('setTitle','管理IP段');
	$('#tr_set_ip').combobox('reload','TRecord_getIpType.action');
	$('#tr_setIP_fm').form('clear');
}

function tr_delIP(){
	$('#tr_setIP_fm').form('submit',{
		url: 'TRecord_delIp.action',
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.status == 0){
				$('#tr_setIP_dlg').dialog('close');		// close the dialog
				$('#tr_search_ip').combobox('reload','TRecord_getIpType.action');
			} 
			$.messager.show({	// show error message
				title: 'message',
				msg: result.mess
			});
		}
	});
}

function tr_addIP(){
	$('#tr_setIP_fm').form('submit',{
		url: 'TRecord_addIp.action',
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.status == 0){
				$('#tr_setIP_dlg').dialog('close');		// close the dialog
				$('#tr_search_ip').combobox('reload','TRecord_getIpType.action');
			} 
			$.messager.show({	// show error message
				title: 'message',
				msg: result.mess
			});
		}
	});
}

var tr_export_menu = $('#tr_export').menubutton({ menu: '#tr_export_menu' });  

//menubutton 依赖于 menu、linkbutton 这两个插件，所以可以用如下代码实现：
$(tr_export_menu.menubutton('options').menu).menu({ 
          onClick: function (item) { 
              if(item.text == "导出全部"){
              	var form = $("<form>");  
                  form.attr('style','display:none');  
                  form.attr('target','');  
                  form.attr('method','post');  
                  form.attr('action','TRecord_exportAll2Excel.action');  
                  $('body').append(form);  
                  form.submit();  
                  form.remove(); 
              }else if(item.text == "导出当前"){
              	var form = $("<form>");  
                  form.attr('style','display:none');  
                  form.attr('target','');  
                  form.attr('method','post');  
                  form.attr('action','TRecord_exportCurrent2Excel.action');  
                  
                  var input1 = $('<input>');  
                  input1.attr('type','hidden');  
                  input1.attr('name','from_date');
                  input1.attr('value',$('#tr_from_date').datebox('getValue'));
                  var input2 = $('<input>');  
                  input2.attr('type','hidden');  
                  input2.attr('name','to_date');  
                  input2.attr('value',$('#tr_to_date').datebox('getValue'));
                  var input3 = $('<input>');  
                  input3.attr('type','hidden');  
                  input3.attr('name','state');  
                  input3.attr('value',$('#tr_tb_type').combobox('getValue'));
                  var input4 = $('<input>');  
                  input4.attr('type','hidden');  
                  input4.attr('name','ip');  
                  input4.attr('value',$('#tr_search_ip').combobox('getValue'));
                  form.append(input1);
                  form.append(input2);
                  form.append(input3);
                  form.append(input4);
                  $('body').append(form);  
                  form.submit();  
                  form.remove();
              }
          } 
});

var tr_analyse_type;
var tr_analyse_menu = $('#tr_analyse').menubutton({ menu: '#tr_analyse_menu' });  
//menubutton 依赖于 menu、linkbutton 这两个插件，所以可以用如下代码实现：
$(tr_analyse_menu.menubutton('options').menu).menu({ 
        onClick: function (item) { 
            if(item.text == "日期"){
            	tr_analyse_type = "日期";
            	$('#tr_analyse_span').hide();
            	$('#tr_analyse_window').window('open');
            	$('#tr_analyse_iframe').attr("src","analyse/TRAnalyse_date.action");
            }else if(item.text == "IP段"){
            	tr_analyse_type = "IP段";
            	$('#tr_analyse_span').show();
            	$('#tr_analyse_ip').combobox('setValue','');
            	$('#tr_analyse_window').window('open');
            	$('#tr_analyse_ip').combobox('reload','TRecord_getIpType.action');
            	$('#tr_analyse_iframe').attr("src","analyse/TRAnalyse_IP.action");
            }
        } 
});

$('#tr_analyse_search').searchbox({   
	width:120,   
	searcher:function(value){  
		if(tr_analyse_type == "日期"){
			$('#tr_analyse_search').searchbox('setValue','');
			$('#tr_analyse_iframe').attr("src","analyse/TRAnalyse_date.action?analyse_date="+value+"&json="+Math.random());
		}else if(tr_analyse_type == "IP段"){
			$('#tr_analyse_search').searchbox('setValue','');
			var ip = $('#tr_analyse_ip').combobox('getValue');
			//$('#tr_analyse_ip').combobox('setValue','');
			$('#tr_analyse_iframe').attr("src","analyse/TRAnalyse_IP.action?analyse_date="+value+"&Ip="+ip+"&json="+Math.random());
		}
	},   
	prompt:'yyyy 或 yyyy-mm'  
});
/**
 * IP在表格中的显示形式
 */
function trli_IPFormatter(value, row, index) {
	return '<a href="javascript:;" onclick="trli_findServer(' + index + ')">' + value + '</a>';
}
/**
 * 点击ip查看服务器查询
 */
function trli_findServer(rowIndex) {
	var title = '服务器查询';
	if(! index_data.tabObj.tabs('exists', title)) {
		index_openTab(title, 'server_list.action', true);
		setTimeout(function() {
			trli_openServer(rowIndex);
		}, 2000);
	} else {
		index_data.tabObj.tabs('select', title);
		svli_clear();
		trli_openServer(rowIndex);
	}
}
/**
 * 打开服务器查询页面
 */
function trli_openServer(rowIndex) {
	var row = $('#tr_record').datagrid('getRows')[rowIndex];
	$('#svli_ip').combobox('setValue', row.IP);
	$('#svli_table').datagrid('load');
}

