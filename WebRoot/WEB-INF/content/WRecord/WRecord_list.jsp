<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<style type="text/css">
	#wr_fm{
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

	<script>
		
		$(function(){
			//var lastIndex;
			$('#wr_record').datagrid({
				onDblClickRow:function(rowIndex){
					var row = $('#wr_record').datagrid('getSelected');
					//var re = /\r\n+/g;
					var detail = row.detail;
					detail = detail.replace(/\r\n+/g,"<br>");
					$('#wr_dlg_detail').html(detail);
					$('#wr_detail_dlg').dialog('open').dialog('setTitle','记录详情');
					//$.messager.alert('记录详情',row.detail,'');
				}
			});
			
		});
	</script>
	
<input type="hidden" id="login_user" value="<s:property value='login_user.str[0]'/>"/>
	
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行查看记录详情；&nbsp;&nbsp;2.日期格式：yyyy-mm 或 yyyy-mm-dd；&nbsp;&nbsp;
    	 3.机器故障请记录在故障记录中，系统会自动添加到运行记录。
    </div>
</div>
	
	<table id="wr_record" 
			data-options="iconCls:'icon-edit',singleSelect:true,nowrap:true,fitColumns:true,rownumbers:true,
			pagination:true,striped: true,url:'WRecord_listTable.action'" toolbar="#wr_tb"
			>
		<thead>
			<tr>
				<!--  <th data-options="field:'record_id',align:'center',width:80">ID</th>	 -->
				<th data-options="field:'detail',width:400,align:'left'">内容</th>
				<th data-options="field:'type',width:80,align:'center'">操作类型</th>
				<th data-options="field:'time',width:120,align:'center'">记录时间</th>
				<th data-options="field:'username',width:60,align:'center'">记录人</th>
				<th data-options="field:'dept',width:60,align:'center'">部门</th>
				<th data-options="field:'attachment',width:30,align:'center'">附件</th>
			</tr>
		</thead>
	</table>
	 
	<div id="wr_tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="wr_newRecord()">添加</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="wr_editRecord()">编辑</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="wr_removeRecord()">删除</a>
			<a href="javascript:;" id='wr_export' class="easyui-menubutton" iconCls="icon-download">导出</a>
		</div>
		<div>
			<select id="wr_date_type" class="easyui-combobox" style="width:60px" data-options="panelHeight:'auto'">
				<option>日期</option>
				<option>自定义</option>
			</select>
			： <input id="wr_from_date" name="from_date" class="easyui-datebox" style="width:90px">
			<span id="wr_todate_span">
				至 <input id="wr_to_date" name="to_date" class="easyui-datebox" style="width:90px">
			</span>
			<!--  至 <input id="wr_to_date" name="to_date" class="easyui-datebox" style="width:90px">  -->
		             操作类型：
			<input id="wr_tb_type" class="easyui-combobox" style="width:100px"
				data-options="url:'script/record/wr_search_type.json',valueField:'id',textField:'text',panelHeight:'auto',editable:false">
			部门：
			<input id="wr_tb_dept" class="easyui-combotree" style="width:170px" data-options="editable:true">
			记录人：
			<input id="wr_tb_user" class="easyui-combobox" style="width:100px" 
			       data-options="url:'user_getAllUserName.action',
			                     valueField:'id',
								 textField:'text',
			                     editable:false">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" onclick="wr_find()">搜索</a>
			<span style="position: absolute;right: 0"><input id="wr_searchbox"></input></span>
		</div>
	</div>
	
	<div id="wr_dlg" class="easyui-dialog" style="width:650px;height:550px;padding:10px 20px"
			closed="true" buttons="#wr_dlg-buttons">
		<div class="cr_ftitle">运行记录</div>
		<form id="wr_fm" method="post" enctype ="multipart/form-data" novalidate>
			<div class="cr_fitem">
				<label>操作类型:</label>
				<select id="wr_type" name="type" class="easyui-combobox" style="width:150px;" editable="false"
					    data-options="required:true,panelHeight:'auto',valueField:'id',textField:'text'">
				</select>
				<span id="wrli_npspan" class="wrli_addnp">
					<input id="wrli_addnp" type="checkbox" name="addnp"/>
					<label for="wrli_addnp">添加进每日记事</label>
				</span>
			</div>
			<div class="cr_fitem">
				<label>内容:</label>
				<textarea name="detail" class="easyui-validatebox" style="width:550px;height:280px;" rows="3" required="true"></textarea>
			</div>
			<div class="cr_fitem">
				<div class="cr_fitem" style="float:left;width:300px;">
					<label>附件:</label><input id="attachment" name="imgFile" type="file"/>
				</div> 
				<div class="cr_fitem" style="float:right;width:230px;"> 
					<div id="wr_time_div">
						<label>记录时间:</label>
						<input id="wr_time" name="r_time" class="easyui-datetimebox" editable="false"
							   style="width:140px"/>
					</div>
				</div>
				
			</div>
		</form>
	</div>
	<div id="wr_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="wr_saveRecord()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#wr_dlg').dialog('close')">取消</a>
	</div>
	
	<!-- 双击查看详情对话框 -->
	<div id="wr_detail_dlg" class="easyui-dialog" style="width:500px;height:350px;padding:10px 20px"
			closed="true" buttons="#wr_detail_dlg-buttons">
		<span id="wr_dlg_detail"></span>
	<div id="wr_detail_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:$('#wr_detail_dlg').dialog('close')">OK</a>
	</div>
	
	<!-- 导出下拉菜单 -->
	<div id="wr_export_menu" style="width:100px;">  
		<div>导出当前</div>
	    <div>导出全部</div>  
	</div>