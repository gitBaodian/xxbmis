$(function(){
	$('#usli_toolbar_specialtransfer').datagrid({
	    url: "specialtransfer_specialtransfer_show.action",
	    iconCls: "icon-edit",
	    title: "假期排班",
	    striped: true,//条纹
	    fitColumns: true,//自动调整单元格宽度
	    rownumbers: true,//显示行号
	    singleSelect: true,//单选
	    pagination: true,//分页
	    paramsName: {
	    	page: "page.page",
	    	num: "page.num"
	    },toolbar: '#usli_toolbar_specialtransfer',
	    columns: [[
	        {field: 'specialDate', title: '起始时间', width: 30},
	        {field: 'one', title: '后夜', width: 20},
	        {field: 'two', title: '白班', width: 20},
	        {field: 'there',title:'前夜', width: 20},
	        {field: 'dsh',title:'状态', width: 30},
	    ]]
	});
});


	$('#add_specialtransfer_dd').dialog({
			buttons:[{
				text:'添加',
				iconCls:'icon-ok',
				handler:function(){
					$.post("specialtransfer_specialtransfer_add.action", $("#add_specialtransfer_form").serialize(), function(data) {
						if(data.status == 0) {
							$('#add_specialtransfer_dd').dialog('close');
							$('#usli_toolbar_specialtransfer').datagrid('reload');
							alert(data.mess);
						} 
						else {
							alert(data.mess);
						}
					}, "json");
					$("#st_object1").empty();
					$("#st_object2").empty();
					$("#st_object3").empty();
				}
			},{
				text:'&nbsp;取消&nbsp;',
				handler:function(){
					$('#add_specialtransfer_dd').dialog('close');
					$("#st_object1").empty();
					$("#st_object2").empty();
					$("#st_object3").empty();
				}
			}]
	});
	
	$('#update_specialtransfer_dd').dialog({
			buttons:[{
				text:'修改',
				iconCls:'icon-ok',
				handler:function(){
					$.post("specialtransfer_specialtransfer_update.action", $("#update_specialtransfer_form").serialize(), function(data) {
						if(data.status == 0) {
							$('#update_specialtransfer_dd').dialog('close');
							$('#usli_toolbar_specialtransfer').datagrid('reload');
							$("#specialtransfer_one").empty();
							$("#specialtransfer_two").empty();
							$("#specialtransfer_there").empty();
							alert(data.mess);
						} 
						else {
							alert(data.mess);
						}
					}, "json");
				}
			},{
				text:'&nbsp;取消&nbsp;',
				handler:function(){
					$('#update_specialtransfer_dd').dialog('close');
					$("#specialtransfer_one").empty();
					$("#specialtransfer_two").empty();
					$("#specialtransfer_there").empty();
				}
			}]
	});
		

	function add_specialtransfer() {
		$.post("specialtransfer_getuser.action",function(data){
			if(data.status == "1") {
				alert(data.mess);
			} 
			else {
				$("#st_object1").append("<option id='st_com' value='0'>请选择</option>");
				for(var i=0;i<data.staff.length;i++){
					$("#st_object1").append("<option id='st_com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
				}
				$("#st_object2").append("<option id='st_com' value='0'>请选择</option>");
				for(var i=0;i<data.staff.length;i++){
					$("#st_object2").append("<option id='st_com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
				}
				$("#st_object3").append("<option id='st_com' value='0'>请选择</option>");
				for(var i=0;i<data.staff.length;i++){
					$("#st_object3").append("<option id='st_com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
				}
				$('#add_specialtransfer_dd').dialog('open').dialog('setTitle','排班添加');
			}
		}, "json");
	}
	
	function edit_specialtransfer() {
		var row = $('#usli_toolbar_specialtransfer').datagrid('getSelected');
		if(row){
			$.post("specialtransfer_getuser.action",function(data){
				if(data.status == "1") {
					alert(data.mess);
				} 
				else {
					for(var i=0;i<data.staff.length;i++){
						if(data.staff[i] == row.one)
							$("#specialtransfer_one").append("<option id='st_com' value='"+data.staff[i]+"' selected='selected'>"+data.staff[i]+"</option>");
						$("#specialtransfer_one").append("<option id='st_com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
					}
					
					for(var i=0;i<data.staff.length;i++){
						if(data.staff[i] == row.two)
							$("#specialtransfer_two").append("<option id='st_com' value='"+data.staff[i]+"' selected='selected'>"+data.staff[i]+"</option>");
						$("#specialtransfer_two").append("<option id='st_com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
					}
					
					for(var i=0;i<data.staff.length;i++){
						if(data.staff[i] == row.there)
							$("#specialtransfer_there").append("<option id='st_com' value='"+data.staff[i]+"' selected='selected'>"+data.staff[i]+"</option>");
						$("#specialtransfer_there").append("<option id='st_com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
					}
					
				}
			}, "json");
				$("#specialtransfer_date").val(row.specialDate);
				$('#update_specialtransfer_dd').dialog('open').dialog('setTitle','排班修改');	
		}else{
			$('#update_specialtransfer_dd').dialog('close');
			$.messager.show({	// show error message
				title: 'Tip',
				msg: "请选中一行记录"
			});
		}
	}
	
	function remove_specialtransfer() {
		var row = $('#usli_toolbar_specialtransfer').datagrid('getSelected');
		if(row.dsh == "已生效"){
			alert("该申请已生效，无法删除！！");
			return;
		}
		$.messager.confirm('Confirm','你是否要删除该排班?',function(r){  
		    if (r){  
		        $.post("specialtransfer_specialtransfer_del.action", {"specil_date":row.specialDate}, function(data) {
					if(data.status == 0) {
						alert(data.mess);
						$('#usli_toolbar_specialtransfer').datagrid('reload');
					} else {
						alert(data.mess);
					}
				}, "json"); 
		    }  
		});  
	}