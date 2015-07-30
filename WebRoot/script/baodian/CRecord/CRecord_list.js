var url;
$('#c_record').datagrid({
	onDblClickRow:function(rowIndex){
		var row = $('#c_record').datagrid('getSelected');
		$('#cr_dlg_detail').html("<strong>事件标题：</strong><br>" + row.title.replace(/\r\n+/g,"<br>") +
				"<br><br><strong>事件内容：</strong><br>" + row.detail.replace(/\r\n+/g,"<br>") +
				"<br><br><strong>解决方法：</strong><br>" + row.solve_approach.replace(/\r\n+/g,"<br>"));
		$('#cr_detail_dlg').dialog('open').dialog('setTitle','记录详情');
	}
});

$('#cr_date').datebox('setValue',GetCurrentDate());

function cr_newRecord(){
	$('#cr_dlg').dialog('open').dialog('setTitle','添加记录');
	$('#cr_fm').form('clear');
	$('#cr_type').combobox('reload','script/record/cr_add_type.json');		//加载事件类型
	$('#cr_op_username').combobox('reload','user_getAllUserName.action');	//加载操作人
	$('#cr_time').datebox('setValue',GetCurrentTime());						//记录时间，显示当前时间
	url = 'CRecord_addCustomerRecord.action?json='+Math.random();
}

function cr_editRecord(){
	var row = $('#c_record').datagrid('getSelected');
	if(row){
		$('#cr_dlg').dialog('open').dialog('setTitle','编辑记录');
		$('#cr_fm').form('clear');
		$('#cr_type').combobox('reload','script/record/cr_add_type.json');
		$('#cr_op_username').combobox('reload','user_getAllUserName.action');
		$('#cr_op_username').combobox('setValue',row.operator);
		var detail = row.detail;
		var solve_approach = row.solve_approach;
		row.detail = detail.replace(/<[^>].*?>/g,"");
		row.solve_approach = solve_approach.replace(/<[^>].*?>/g,"");
		$('#cr_fm').form('load',row);
		row.detail = detail;
		row.solve_approach = solve_approach;
		url = 'CRecord_updateCustomerRecord.action?cr_id='+row.cr_id;
	}else{
		show_msg("Tip","请选中一行记录");
	}
}

function cr_removeRecord(){
	var row = $('#c_record').datagrid('getSelected');
	if(row){
		$.messager.confirm('Confirm','确定删除这条记录?',function(r){
			if (r){
				$.post('CRecord_delCustomerRecord.action',{cr_id:row.cr_id},function(result){
					if (result.status == 0){
						$('#c_record').datagrid('load');	// reload data
						$('#wr_record').datagrid('reload');    // 刷新运行记录， 后台删除客服记录会同时删除运行记录
						show_msg("Success",result.mess);
					} else {
						show_msg("Error",result.mess);
						$('#c_record').datagrid('load');
					}
				},'json');
			}
		});
	}else{
		show_msg("Tip","请选中一行记录");
	}
}

function cr_saveRecord(){
	$('#cr_fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			$('#cr_dlg').dialog('close');
			var result = eval('('+result+')');	
			if (result.status == "0"){
				$('#c_record').datagrid('reload');
				show_msg('Success',result.mess);
				$('#wr_record').datagrid('reload');    // 刷新运行记录， 后台添加客服记录会同时添加运行记录
			}else{
				show_msg('Error',result.mess);
			}
		}
	});
}

$('#cr_searchbox').searchbox({   
    width:150,
    prompt:'内容关键字搜索',
    searcher:function(keyword){   
    	//置空查询条件
    	if(keyword == ""){
    		show_msg("Tip","请输入关键字");
    	}else{  //关键字不为空
    		$('#c_record').datagrid('options').url = 'CRecord_searchByKeyWord.action';
    		var queryParams = $('#c_record').datagrid('options').queryParams;
    		queryParams.keyword = keyword;
    		$('#c_record').datagrid('options').queryParams = queryParams;
    		
    		/***** 关键字高亮  *****/
    		var fields = $('#c_record').datagrid('getColumnFields',false);
    		$('#c_record').datagrid('getColumnOption',fields[4]).formatter = 
    			 					function(value, rowData, rowIndex){
    									value = value.replace(keyword,"<font color=red>"+keyword+"</font>");
    									return value;
    								};
    								
        	$('#c_record').datagrid('load');
    	}
    }
}); 

function cr_find(){
	var date = $('#cr_date').datebox('getValue');
	var type = $('#cr_tb_type').combobox('getValue');
	var ticket_id = $('#cr_tb_ticket_id').val();
	var state = $('#cr_tb_state').combobox('getValue');
	if(date.length != 0 && date.length != 7 && date.length != 10){
		show_msg("Tip","时间格式不正确");
		return;
	}
	var queryParams = $('#c_record').datagrid('options').queryParams;
	queryParams.time = date;
	queryParams.type = type;
	queryParams.ticket_id = ticket_id;
	queryParams.state = state;
	//$('#c_record').datagrid('options').queryParams = queryParams;
	$('#c_record').datagrid('options').url = 'CRecord_searchByCondition.action';
	/**** 清除搜索框做过的设置 ****/
	$('#cr_searchbox').searchbox('setValue','');
	var fields = $('#c_record').datagrid('getColumnFields',false);
	$('#c_record').datagrid('getColumnOption',fields[4]).formatter = '';  //清除高亮
	
	$('#c_record').datagrid('load');
}

var cr_memu = $('#cr_export').menubutton({ menu: '#cr_export_menu' });
$(cr_memu.menubutton('options').menu).menu({ 
    onClick: function (item) { 
        if(item.text == "导出全部"){
        	var form = $("<form>");  		//jquery ajax不支持文件下载，所以使用下面的方法解决文件下载问题
            form.attr('style','display:none');  
            form.attr('target','');  
            form.attr('method','post');  
            form.attr('action','CRecord_exportAll2Excel.action');  
            $('body').append(form);  
            form.submit();  
            form.remove(); 
        }else if(item.text == "导出当前"){
        	var form = $("<form>");  
            form.attr('style','display:none');  
            form.attr('target','');  
            form.attr('method','post');  
            form.attr('action','CRecord_exportCurrent2Excel.action');  
            
            var input1 = $('<input>');  
            input1.attr('type','hidden');  
            input1.attr('name','time');  
            input1.attr('value',$('#cr_date').datebox('getValue'));
            var input2 = $('<input>');  
            input2.attr('type','hidden');  
            input2.attr('name','type');  
            input2.attr('value',$('#cr_tb_type').combobox('getValue'));
            var input3 = $('<input>');  
            input3.attr('type','hidden');  
            input3.attr('name','ticket_id');  
            input3.attr('value',$('#cr_tb_ticket_id').val());
            var input4 = $('<input>');  
            input4.attr('type','hidden');  
            input4.attr('name','state');  
            input4.attr('value',$('#cr_tb_state').combobox('getValue'));
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

function show_msg(title,msg){
	$.messager.show({
		title: title,
		msg: msg
	});
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