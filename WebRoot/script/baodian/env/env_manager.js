$(function(){
	$("#usli_env_manager").datagrid({
          url: "env_list.action",
          //method:'post',
          iconCls: "icon-edit",
	      title: "设备列表",
          loadMsg:"数据装载中....",
          striped: true,//条纹
	      fitColumns: true,//自动调整单元格宽度
	      rownumbers: true,//显示行号
	      singleSelect: true,//单选
	      pagination: false,//分页
	      //queryParams: {
	      	
	      //},
	      //paramsName: {
	    	//page: "page.page",
	    	//num: "page.num"
	      //},
	      toolbar: '#usli_toolbar_env_manager',
          columns:[[
                    {title:'Id',field:'id',width:30},    
                    {title:'环境代号',field:'env',width:40},
                    {title:'环境名',field:'env_name',width:30},
				]],
     });
	
	
	
	$('#env_add_dd').dialog({
		buttons:[{
			text:'添加',
			iconCls:'icon-ok',
			handler:function(){
				$.post("env_env_add.action", $("#env_add_form").serialize(), function(data) {
					if(data.status == 0) {
						$('#env_add_dd').dialog('close');
						alert(data.mess);
						$('#usli_env_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json");
			}
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#usli_env_manager').dialog('close');
			}
		}]
	});
	
	
	
	$('#env_update_dd').dialog({
		buttons:[{
			title: "设备信息添加",
			text:'添加',
			iconCls:'icon-ok',
			handler:function(){
				$.post("env_env_update.action", $("#env_update_form").serialize(), function(data) {
					if(data.status == 0) {
						$('#env_update_dd').dialog('close');
						alert(data.mess);
						$('#usli_env_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json");
			}
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#env_update_dd').dialog('close');
			}
		}]
	});
	
});	



function env_del() {
	var row = $('#usli_env_manager').datagrid('getSelected');
	if(row){
		$.messager.confirm('Confirm','你是否要删除该设备信息?',function(r){  
		    if (r){  
		        $.post("env_env_del.action", {"id":row.id}, function(data) {
					if(data.status == 0) {
						alert(data.mess);
						$('#usli_env_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json"); 
		    }  
		});
	}else{
		$.messager.show({	// show error message
			title: 'Tip',
			msg: "请选中一行记录"
		});
	}
}


function env_add() {
	$('#env_add_dd').dialog('open').dialog('setTitle','环境信息添加');
}

function env_update() {
	var row = $('#usli_env_manager').datagrid('getSelected');
	if(row){
		$("#env_id").val(row.id);
		$("#env").val(row.env);
		$("#env_name").val(row.env_name);
		$('#env_update_dd').dialog('open').dialog('setTitle','环境信息修改');
	}else{
		alert("请选择一栏信息");
	}
}

