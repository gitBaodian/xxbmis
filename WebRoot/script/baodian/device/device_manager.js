$(function(){
	$('#one_env_list').combobox('reload','env_get_env_list.action');
	
	$("#usli_device_manager").datagrid({
          url: "device_equ_list.action",
          //method:'post',
          iconCls: "icon-edit",
	      title: "设备列表",
          loadMsg:"数据装载中....",
          striped: true,//条纹
	      fitColumns: true,//自动调整单元格宽度
	      rownumbers: true,//显示行号
	      singleSelect: true,//单选
	      pagination: false,//分页
	      queryParams: {
	    	"page.env": {"id":"hid_env","type":"text"},
	    	"page.ip": {"id":"one_env_list","type":"combobox"},
	      },
	      //paramsName: {
	    	//page: "page.page",
	    	//num: "page.num"
	      //},
	      toolbar: '#usli_toolbar_device_manager',
          columns:[[
                    {title:'IP',field:'ip',width:30},    
                    {title:'角色',field:'role',width:40},
                    {title:'系统',field:'system',width:30},
                    {title:'类型',field:'type',width:30},
				]],
				
				view: detailview,  
		         detailFormatter:function(index,row){  
		             return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';  
		         },  
		         onExpandRow: function(index,row){  
		             $('#ddv-'+index).datagrid({  
		                 url: "device_ip_equ.action?ip="+row.ip+"",  
		                 fitColumns:true,  
		                 singleSelect:true,  
		                 rownumbers:true,  
		                 height:'auto',  
		                 columns:[[  
			                 {title:'项目',field:'property',width:30},    
		                     {title:'内容',field:'content',width:30},
		                 ]],  
		                 onResize:function(){  
		                     $('#usli_device_manager').datagrid('fixDetailRowHeight',index);  
		                 },  
		                 onLoadSuccess:function(){  
		                     setTimeout(function(){  
		                         $('#usli_device_manager').datagrid('fixDetailRowHeight',index);  
		                     },0);  
		                 }  
		             });  
		             $('#usli_device_manager').datagrid('fixDetailRowHeight',index);  
		         } 
     });
	
	
	
	$('#equ_batch_up').dialog({
		buttons:[{
			text:'修改',
			iconCls:'icon-ok',
			handler:function(){
				$.post("device_equ_batch_up.action", $("#equ_batch_up_form").serialize(), function(data) {
					if(data.status == 0) {
						$('#equ_batch_up').dialog('close');
						alert(data.mess);
						$('#usli_device_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json");
			}
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#equ_batch_up').dialog('close');
			}
		}]
	});
	
	
	$('#equ_batch_add').dialog({
		buttons:[{
			title: "设备信息添加",
			text:'添加',
			iconCls:'icon-ok',
			handler:function(){
				$.post("device_equ_batch_add.action", $("#equ_batch_add_form").serialize(), function(data) {
					if(data.status == 0) {
						$('#equ_batch_add').dialog('close');
						alert(data.mess);
						$('#usli_device_manager').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json");
			}
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#equ_batch_add').dialog('close');
			}
		}]
	});
	
});	



function equ_del() {
	var row = $('#usli_device_manager').datagrid('getSelected');
	if(row){
		$.messager.confirm('Confirm','你是否要删除该设备信息?',function(r){  
		    if (r){  
		        $.post("device_ip_equ_del.action", {"ip":row.ip}, function(data) {
					if(data.status == 0) {
						alert(data.mess);
						$('#usli_device_manager').datagrid('reload');
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


function equ_batch_add() {
	$('#two_env_list').combobox('reload','env_get_env_list.action');
	$('#equ_batch_add').dialog('open').dialog('setTitle','设备增加');
}

function equ_batch_up() {
	var row = $('#usli_device_manager').datagrid('getSelected');
	if(row){
		$.post("device_batch_equ.action", {"ip":row.ip}, function(data) {
			$(".iptdiv").remove();
			$("#bip").val(row.ip);
			$("#brole").val(data.role);
			$("#bsystem").val(data.system);
			$("#bcpu").val(data.cpu);
			$("#bhard").val(data.hard);
			$("#bmemory").val(data.memory);
			$("#bmotherboard").val(data.motherboard);
			$("#type").val(data.type);
			$("#other").val(data.other);
		}, "json");
		$('#equ_batch_up').dialog('open').dialog('setTitle','设备信息修改');
	}else{
		alert("请选择一栏信息");
	}
	
}

function env_load() {
	$('#usli_device_manager').datagrid('reload');
}

function create_equ_xls() {
	var env = $("#usli_env").val();
	location.href='device_create_env_list_xls.action?env='+env+'';
}

