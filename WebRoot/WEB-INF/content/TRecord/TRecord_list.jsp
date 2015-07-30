<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<style type="text/css">
		#fm{
			margin:0;
			padding:10px 30px;
		}
		.tr_ftitle{
			font-size:14px;
			font-weight:bold;
			color:#666;
			padding:5px 0;
			margin-bottom:10px;
			border-bottom:1px solid #ccc;
		}
		.tr_fitem{
			margin-bottom:5px;
		}
		.tr_fitem label{
			display:inline-block;
			width:80px;
		}
</style>

<input type="hidden" id="login_user" value="<s:property value='login_user.str[0]'/>"/>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行查看记录详情；&nbsp;&nbsp;2.管理员才能修改他人的记录；&nbsp;&nbsp;3.日期格式：yyyy-mm-dd。</div>
</div>
	
	<table id="tr_record" 
			data-options="iconCls:'icon-edit',singleSelect:true,nowrap:true,idField:'record_id',fitColumns:true,
			rownumbers:true,pagination:true,striped: true,url:'TRecord_list.action?flag=list'"
			toolbar="#tr_tb">  <!-- title="Editable DataGrid">  -->
		<thead>
			<tr>
			<!--  <th data-options="field:'record_id',align:'center',width:40">ID</th>	-->
			<th data-options="field:'IP',width:110,align:'center',formatter:trli_IPFormatter">IP</th>
			<th data-options="field:'detail',width:260,align:'center'">故障原因</th>
			<th data-options="field:'state',width:80,align:'center'">故障状态</th>
			<th data-options="field:'f_time',width:135,align:'center'">发现时间</th>
			<th data-options="field:'f_user',width:80,align:'center'">发现人</th>
			<th data-options="field:'solve_approach',width:100,align:'center'">解决方法</th>
			<th data-options="field:'s_time',width:135,align:'center'">解决时间</th>
			<th data-options="field:'s_user',width:80,align:'center'">解决人</th>
			<th data-options="field:'r_time',width:135,align:'center'">记录时间</th>
			<th data-options="field:'r_user',width:80,align:'center'">记录人</th>
			<th data-options="field:'attachment',width:45,align:'center'">附件</th>
			</tr>
		</thead>
	</table>
	
	<div id="tr_tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="tr_newRecord()">添加</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="tr_editRecord()">编辑</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="tr_removeRecord()">删除</a>
			<a href="javascript:;" id='tr_export' class="easyui-menubutton" iconCls="icon-download">导出</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-set" plain="true" onclick="tr_setIP()">管理IP段</a>
			<a href="javascript:;" id='tr_analyse' class="easyui-menubutton" iconCls="icon-analyse">图表统计</a>
		</div>
		<div>
			日期: <input id="tr_from_date" name="from_date" class="easyui-datebox" style="width:90px">
			至 <input id="tr_to_date" name="tr_to_date" class="easyui-datebox" style="width:90px">
			故障状态: 
			<!-- <input id="tr_tb_type" class="easyui-combobox" style="width:100px"
					url="script/record/tr_search_state.json"
					valueField="id" textField="text" panelHeight="auto" editable="false"> -->
			<select id="tr_tb_type" class="easyui-combobox" style="width:70px;" panelHeight="auto" editable="false">
				<option value="全部" selected="selected">全部</option>
				<option value="已解决">已解决</option>
				<option value="未解决">未解决</option>
			</select>
			<%--IP：<input id="search_ip" style="width:120px"/>--%>
			IP段：<select id="tr_search_ip" name="search_ip" class="easyui-combobox" style="width:135px;"
						data-options="panelHeight:'auto',valueField:'id',textField:'text',url:'TRecord_getIpType.action'">
				</select>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" onclick="tr_find()">搜索</a>
			<span style="position: absolute;right: 0"><input id="tr_searchbox"></input></span>
		</div>
	</div>
	
	
	<div id="tr_dlg" class="easyui-dialog" style="width:700px;height:500px;padding:10px 20px"
			closed="true" buttons="#dlg-buttons">
		<div class="tr_ftitle">故障记录</div>
		<form id="tr_fm" method="post" enctype ="multipart/form-data" novalidate>
			<div class="tr_fitem">
				<div class="tr_fitem" style="float:left;width:250px;">
					 <label>IP:</label>
					 <%--<input id="ip" name="ip" class="easyui-validatebox" style="width:120px" required="true"/>  --%>
					 <select id="tr_ip" name="ip" class="easyui-combobox" style="width:135px;"
						    data-options="required:true,panelHeight:'auto',valueField:'id',textField:'text'">
					 </select>
				</div>
				<div class="tr_fitem" style="float:right;width:300px;">
					 <label>发现时间:</label>
					 <input id="tr_f_time" name="f_time" class="easyui-datetimebox" data-options="required:true,editable:false" 
					 		style="width:135px"/>  
				</div>
			</div>
			<div style="clear:both"></div>
			<div class="tr_fitem">
				<div class="tr_fitem" style="float:left;width:80px;">
				    <label>内容:</label>
				</div>
				<div class="tr_fitem" style="float:left;width:500px;">
				    <textarea name="detail" class="easyui-validatebox" style="overflow:auto;width:500px;height:100px;" rows="3" required="true"></textarea>
			    </div>
			</div>
			<div style="clear:both"></div>
			<div class="tr_fitem">
				<div class="tr_fitem" style="float:left;width:250px;">
					 <label>故障状态:</label>
					 <select id="tr_state" name="state" class="easyui-combobox" style="width:100px;" editable="false"
						    data-options="required:true,panelHeight:'auto',valueField:'id',textField:'text'">
					 </select> 
				</div>
				<div class="tr_fitem" style="float:right;width:300px;">
					 <label>发现人:</label>
					 <select id="tr_f_username" name="f_username"  class="easyui-combobox" style="width:100px;" editable="false"
						    data-options="required:true,panelHeight:'auto',valueField:'id',textField:'text'">
					 </select>  
				</div>
			</div>
			<div style="clear:both"></div>
			<div class="tr_fitem">
				<div class="tr_fitem" style="float:left;width:80px;">
				    <label>解决方法:</label>
				</div>
				<div class="tr_fitem" style="float:left;width:500px;">
				    <textarea id="tr_solve_approach" name="solve_approach" class="easyui-validatebox" style="overflow:auto;width:500px;height:100px;" rows="3"></textarea>
			    </div>
			</div>
			<br>
			<div class="tr_fitem">
				<div class="tr_fitem" style="float:left;width:250px;">
					 <label>解决时间:</label>
					 <input id="tr_s_time" name="s_time" class="easyui-datetimebox" editable="false"
					 		style="width:135px"/> 
				</div>
				<div class="tr_fitem" style="float:right;width:300px;">
					 <label>解决人:</label>
					 <select id="tr_s_username" name="s_username" class="easyui-combobox" style="width:100px;" editable="false"
						    data-options="panelHeight:'auto',valueField:'id',textField:'text'">
					 </select>  
				</div>
			</div>
			<div class="tr_fitem">
				<div class="tr_fitem" style="float:left;width:350px;">
					<label>附件:</label><input id="attachment" name="imgFile" type="file"/>
				</div>
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="tr_saveRecord()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#tr_dlg').dialog('close')">取消</a>
	</div>
	
	<!-- 管理IP段对话框 -->
	<div id="tr_setIP_dlg" class="easyui-dialog" style="width:300px;height:200px;padding:10px 20px"
			closed="true" buttons="#tr_setIP_dlg-buttons">
		<div class="tr_ftitle"> 管理IP段</div>
		<form id="tr_setIP_fm" method="post" enctype ="multipart/form-data" novalidate>
			<div class="tr_fitem">
				<label>IP段:</label>
			</div>
			<div class="tr_fitem">
				<%--<input type="text" id="tr_set_ip" name="ip" class="easyui-validatebox" style="width:180px;height:20px;" rows="3" required="true"></textarea>--%>
			    <select id="tr_set_ip" name="ip" class="easyui-combobox" style="width:180px;" 
							data-options="required:true,panelHeight:'auto',valueField:'id',textField:'text'">
				</select>
			</div>
		</form>
	</div>
	<div id="tr_setIP_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="tr_addIP()">添加</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#tr_setIP_dlg').dialog('close')">取消</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" onclick="tr_delIP()">删除</a>
	</div>
	
	<!-- 双击查看详情对话框 -->
	<div id="tr_detail_dlg" class="easyui-dialog" style="width:500px;height:350px;padding:10px 20px"
			closed="true" buttons="#tr_detail_dlg-buttons">
		<span id="tr_dlg_detail"></span>
	<div id="tr_detail_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:$('#tr_detail_dlg').dialog('close')">OK</a>
	</div>
	
	<!-- 导出下拉菜单 -->
	<div id="tr_export_menu" style="width:100px;">  
		<div>导出当前</div>
	    <div>导出全部</div>  
	</div>
	
	<!-- 图表统计下拉菜单 -->
	<div id="tr_analyse_menu" style="width:100px;">  
		<div>日期</div>
	    <div>IP段</div>  
	</div>
	
	<!-- 图表统计 -->
	<div id="tr_analyse_window" class="easyui-window" title="图表统计" data-options="iconCls:'icon-analyse',closed:true" style="width:750px;height:590px;padding:10px;">  
        <span id="tr_analyse_span">
	        IP: <select id="tr_analyse_ip" name="ip" class="easyui-combobox" style="width:120px;" 
								data-options="panelHeight:'auto',valueField:'id',textField:'text'">
				</select>
		</span>
		日期：<input id="tr_analyse_search"></input>
        <iframe id="tr_analyse_iframe"  width=720 height=520 frameborder=0 scrolling=auto ></iframe> 
    </div>