<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var grct_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.各种设备使用情况的统计；
		2.以后添加按时间统计的功能；
		3.增加时间走线图。</div>
</div>
<table id="grct_table"></table>