$(function(){
		$('#shift_dd').dialog({
			buttons:[{
				text:'交班',
				iconCls:'icon-ok',
				handler:function(){
					$.post("handover_shift.action", $("#shift_form").serialize(), function(data) {
						if(data.status == 0) {
							$('#shift_dd').dialog('close');
							alert(data.mess);
							$("#shift_form input[type=reset]").trigger("click");
						}
						else if(data.status == 2){
							$('#shift_dd').dialog('close');
							index_login();
						}
						else {
							$('#shift_dd').dialog('close');
							alert(data.mess);
						}
						index_duty_officer(false, true);
					}, "json");
				}
			},{
				text:'&nbsp;取消&nbsp;',
				handler:function(){
					$('#shift_dd').dialog('close');
				}
			}]
		});
		
		$('#accept_dd').dialog({
			buttons:[{
				text:'接班',
				iconCls:'icon-ok',
				handler:function(){
					$.post("handover_accept.action", $("#accept_form").serialize(), function(data) {
						if(data.status == 0) {
							$('#accept_dd').dialog('close');
							alert(data.mess);
						} 
						else if(data.status == 2){
							$('#accept_dd').dialog('close');
							index_login();
						}
						else {
							$('#accept_dd').dialog('close');
							alert(data.mess);
						}
						index_duty_officer(false, true);
					}, "json");
				}
			},{
				text:'&nbsp;取消&nbsp;',
				handler:function(){
					$('#accept_dd').dialog('close');
				}
			}]
		});
	});


function shift_dd() {
	$('#shift_dd').dialog('open').dialog('setTitle','交班登记');
}

function accept_dd() {
	$.post("handover_handover_get_remark_shift.action",function(data){
		$("#srs").parent().empty();
		$("#see_remark_shift").append('<div id="srs">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+data.remarkShift+'</div>');
	}, "json");
	$('#accept_dd').dialog('open').dialog('setTitle','接班登记');
}
