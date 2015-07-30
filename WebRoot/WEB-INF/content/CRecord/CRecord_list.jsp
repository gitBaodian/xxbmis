<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<style type="text/css">
	#cr_fm{
		margin:0;
		padding:10px 30px;
	}
	.cr_ftitle{
		font-size:14px;
		font-weight:bold;
		color:#666;
		padding:5px 0;
		margin-bottom:10px;
		border-bottom:1px solid #ccc;
	}
	.cr_fitem{
		margin-bottom:5px;
	}
	.cr_fitem label{
		padding-right: 5px;
	}
</style>

<input type="hidden" id="login_user" value="<s:property value='login_user.str[0]'/>"/>
	
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行查看记录详情；&nbsp;&nbsp;2.日期格式：yyyy-mm 或 yyyy-mm-dd；&nbsp;&nbsp;
    </div>
</div>
	
	<table id="c_record" 
			data-options="iconCls:'icon-edit',singleSelect:true,nowrap:true,fitColumns:true,rownumbers:true,
			pagination:true,striped: true,url:'CRecord_listTable.action'" toolbar="#cr_tb"
			>
		<thead>
			<tr>
				<th data-options="field:'time',width:105,align:'center'">时间</th>
				<th data-options="field:'ticket_id',width:140,align:'center'">itsm单号</th>
				<th data-options="field:'title',width:120,align:'center'">事件标题</th>
				<th data-options="field:'type',width:80,align:'center'">事件类型</th>
				<th data-options="field:'detail',width:200,align:'center'">事件内容</th>
				<th data-options="field:'solve_approach',width:200,align:'center'">解决方法</th>
				<th data-options="field:'state',width:80,align:'center'">事件进度</th>
				<th data-options="field:'operator',width:60,align:'center'">操作人</th>
				<th data-options="field:'r_user',width:60,align:'center'">记录人</th>
			</tr>
		</thead>
	</table>
	 
	<div id="cr_tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="cr_newRecord()">添加</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="cr_editRecord()">编辑</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="cr_removeRecord()">删除</a>
			<a href="javascript:;" id='cr_export' class="easyui-menubutton" iconCls="icon-download">导出</a>
		</div>
		<div>
			日期： <input id="cr_date" name="date" class="easyui-datebox" style="width:90px">
		    &nbsp;&nbsp; 事件类型：
			<input id="cr_tb_type" class="easyui-combobox" style="width:100px"
				data-options="url:'script/record/cr_search_type.json',valueField:'id',textField:'text',panelHeight:'auto',editable:false">
			&nbsp;&nbsp; itsm单号：
			<input id="cr_tb_ticket_id" style="width:170px;height:20px">
			&nbsp;&nbsp; 事件进度：
			<input id="cr_tb_state" class="easyui-combobox" style="width:100px"
				data-options="url:'script/record/cr_search_state.json',valueField:'id',textField:'text',panelHeight:'auto',editable:false">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" onclick="cr_find()">搜索</a>
			<span style="position: absolute;right: 0"><input id="cr_searchbox"></input></span>
		</div>
	</div>
	
	<div id="cr_dlg" class="easyui-dialog" style="width:650px;height:450px;padding:10px 20px"
		 closed="true" buttons="#cr_dlg-buttons">
		 <div class="cr_ftitle">客服记录</div>
		 <form id="cr_fm" method="post" novalidate>
			<div class="cr_fitem">
				<div class="cr_fitem" style="float:left;width:250px;">
					<label>itsm单号:</label>
					<input id="cr_ticket_id" name="ticket_id" style="width:170px;height:20px">
				</div>
				<div class="cr_fitem" style="float:right;width:280px;">
					<label>事件类型:</label>
					<input id="cr_type" name="type" class="easyui-combobox" style="width:100px" editable="false"
						   data-options="required:true,panelHeight:'auto',valueField:'id',textField:'text'">
				</div>
			</div>
			<div style="clear:both"></div>
			<div class="cr_fitem" >
				<label>事件标题:</label>
				<input id="cr_title" name="title" class="easyui-validatebox" style="width:450px;height:20px;" required="true">
			</div>
			<div class="cr_fitem">
				<div class="cr_fitem" style="float:left;width: 60px;">
				    <label>事件内容:</label>
				</div>
				<div class="cr_fitem" style="float:left;width:450px;">
					<textarea name="detail" class="easyui-validatebox" style="width:450px;height:100px;" rows="3" required="true"></textarea>
				</div>
			</div>
			<div style="clear:both"></div>
			<div class="cr_fitem">
				<div class="cr_fitem" style="float:left;width: 60px">
				    <label>解决方法:</label>
				</div>
				<div class="cr_fitem" style="float:left;width:450px;">
					<textarea name="solve_approach" style="width:450px;height:100px;" rows="3" required="true"></textarea>
				</div>
			</div>
			<div class="cr_fitem">
				<div class="cr_fitem" style="float:left;width:250px;">
					<label>操作人:&nbsp;&nbsp;&nbsp;&nbsp;</label>
					<input id="cr_op_username" name="op_username" class="easyui-combobox" style="width:100px;" editable="false"
						   data-options="panelHeight:'auto',valueField:'id',textField:'text',required:'true'">
				</div>
				<div class="cr_fitem" style="float:left;width:250px;">
					<label>记录时间:</label>
					<input id="cr_time" name="time" class="easyui-datetimebox" style="width:140px"
						   data-options="required:true,editable:false"/>
				</div>
			</div>
		</form>
	</div>
	
	<div id="cr_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="cr_saveRecord()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#cr_dlg').dialog('close')">取消</a>
	</div>
	
	<!-- 双击查看详情对话框 -->
	<div id="cr_detail_dlg" class="easyui-dialog" style="width:500px;height:350px;padding:10px 20px"
			closed="true" buttons="#cr_detail_dlg-buttons">
		<span id="cr_dlg_detail"></span>
	<div id="cr_detail_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:$('#cr_detail_dlg').dialog('close')">OK</a>
	</div>
	
	<!-- 导出下拉菜单 -->
	<div id="cr_export_menu" style="width:100px;">  
		<div>导出当前</div>
	    <div>导出全部</div>  
	</div>