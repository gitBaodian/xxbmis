$(function(){
		$('#ask_leave_dd').dialog({
			buttons:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){
					$.post("application_ask_leave.action" , $("#ask_leave_form").serialize(),function(data) {
						if(data.status == 0) {
							$('#ask_leave_dd').dialog('close');
							alert("申请成功！");
						} else {
							alert(data.reason);
						}
					}, "json");
				}
			},{
				text:'&nbsp;取消&nbsp;',
				handler:function(){
					$('#ask_leave_dd').dialog('close');
				}
			}]
		});
		
		$('#apl_transfer_dd').dialog({
			buttons:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){
					$.post("application_apl_transfer.action" , $("#apl_transfer_form").serialize(),function(data) {
						if(data.status == "0") {
							$('#apl_transfer_dd').dialog('close');
							alert("申请成功！");
						} else {
							alert(data.reason);
						}
					}, "json");
					$("#object").empty(); 
				}
			},{
				text:'&nbsp;取消&nbsp;',
				handler:function(){
					$('#apl_transfer_dd').dialog('close');
					$("#object").empty(); 
				}
			}]
		});
});


function ask_leave_dd() {
	$.post("application_getuserPost.action",function(data){
		if(data.status == "1") {
			alert(data.mess);
		} 
		else if(data.status == 2){
			index_login();
		}
		else {
			$("#username").val(data.username);
			$("#posts").val(data.posts);
			$('#ask_leave_dd').dialog('open').dialog('setTitle','请假申请');
		}
	}, "json");
}

function apl_transfer_dd() {
	$.post("application_getuserPost.action",function(data){
		if(data.status == "1") {
			alert(data.mess);
		} 
		else if(data.status == 2){
			index_login();
		}
		else {
			$("#applicant").val(data.username);
			var optionstring = "";
			$("#object").append("<option id='com' value='0'>请选择</option>");
			for(var i=0;i<data.staff.length;i++){
				$("#object").append("<option id='com' value='"+data.staff[i]+"'>"+data.staff[i]+"</option>");
			}
			$('#apl_transfer_dd').dialog('open').dialog('setTitle','调班申请');
		}
	}, "json");
}