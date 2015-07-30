function inspection01_add() {
	$.post("inspection_inspection01_add.action", $("#inspection01_add_form")
			.serialize(), function(data) {
		if (data.status == "1") {
			alert("请将4楼机房巡检表填写完整");
		}else{
			inspection02_add();
		}
	}, "json");
}
function inspection02_add() {
	$.post("inspection_inspection02_add.action", $("#inspection02_add_form")
			.serialize(), function(data) {
		if (data.status == "1") {
			alert("请将网络机房巡检表填写完整");
		}else{
			inspection03_add();
		}
	}, "json");
}
function inspection03_add() {
	$.post("inspection_inspection03_add.action", $("#inspection03_add_form")
			.serialize(), function(data) {
		if (data.status == "1") {
			alert("请将UPS机房巡检表填写完整");
		}else{
			inspection_all_add();
		}
	}, "json");
}
function inspection_all_add() {
	$.post("inspection_inspection_all_add.action", function(data) {
		if (data.status == "0") {
			alert(data.mess);
		}
	}, "json");
}