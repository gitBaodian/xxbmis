<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div style="width:100%">
		提示：点击提交按钮，提交巡检记录
		<a href="javascript:;" id="inspection_add" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="inspection01_add()">提交</a>
	</div>
 </div>

<script type="text/javascript" src="script/baodian/inspection/inspection_add.js"></script>
<link rel="stylesheet" type="text/css" href="css/inspection.css">
 

<div class="easyui-tabs" data-options="fit:true,plain:true">
	<div title="4楼机房">
		<div id="insp">
		<form id='inspection01_add_form' method='post'>
		  <ul>
		  	<li class="insp_width1_1 insp_writing" style="text-align:center;">4楼服务器机房巡检表<li>
		  	<li class="insp_width2_1 insp_IsColor">时间</li>
			<li class="insp_width2_2">
				<input id="insp_time_01" name="inspection01.inspTime01" class="easyui-datetimebox" data-options="required:true,editable:false" 
					 		style="width:200px;border-bottom:1px solid #ccc;"/>  
			</li>
		  	
		  	<li class="insp_width1_1 insp_writing">一、机房环境<li>
		  	
			<li class="insp_width4_1 insp_IsColor">温度</li>
			<li class="insp_width4_2">
				<input name="inspection01.inspTp01" style="width:30px;height:20px;">℃
			</li>
			<li class="insp_width4_3 insp_IsColor">湿度</li>
			<li class="insp_width4_4">
				<!-- <input name="inspection01.insp_hum_01" style="width:30px;height:28px;">% -->
				<input type="radio" name="inspection01.inspHum01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspHum01" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">痕迹</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspHj01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspHj01" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">清洁</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspQj01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspQj01" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">异响</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspYx01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspYx01" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">异味</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspYw01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspYw01" value="N">异常
			</li>
			
			<li class="insp_width1_1 insp_writing">二、周边环境<li>
			<li class="insp_width4_1 insp_IsColor">电源</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspPw01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspPw01" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">电池组</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspBat01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspBat01" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">空调</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspAir01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspAir01" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">消防</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspFire01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspFire01" value="N">异常
			</li>
			
			<li class="insp_width1_1 insp_writing">三、网络环境<li>
			<li class="insp_width4_1 insp_IsColor">汇集交换机1</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspPs101" value="Y" checked>正常
				<input type="radio" name="inspection01.inspPs101" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">汇集交换机2</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspPs201" value="Y" checked>正常
				<input type="radio" name="inspection01.inspPs201" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">汇集交换机3</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspPs301" value="Y" checked>正常
				<input type="radio" name="inspection01.inspPs301" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">汇集交换机4</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspPs401" value="Y" checked>正常
				<input type="radio" name="inspection01.inspPs401" value="N">异常
			</li>
			
		
			<li class="insp_width4_1 insp_IsColor">光收发（测试环境）</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection01.inspOttest01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspOttest01" value="N">异常
			</li>
			
			<li class="insp_width4_3 insp_IsColor">移动信息机</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection01.inspMiu01" value="Y" checked>正常
				<input type="radio" name="inspection01.inspMiu01" value="A">异常
			</li>
	
			<li class="insp_width2_1 insp_IsColor">备注</li>
			<li class="insp_width2_2">
				<textarea id='insp_remark_01' name='inspection01.inspRemark01' rows="2" cols="70"></textarea>
			</li>
		  </ul>
		  </form>
		  <div style="clear:both"></div>
		</div>
	</div>
	
	
	<div title="网络室">		
		<div id="insp">
		<form id='inspection02_add_form' method='post'>
		  <ul>
		  	<li class="insp_width1_1 insp_writing" style="text-align:center;">网络室机房巡检表<li>
		  	<li class="insp_width2_1 insp_IsColor">时间</li>
			<li class="insp_width2_2">
				<input id="insp_time_02" name="inspection02.inspTime02" class="easyui-datetimebox" data-options="required:true,editable:false" 
					 		style="width:200px;border-bottom:1px solid #ccc;"/>  
			</li>
		  	
		  	<li class="insp_width1_1 insp_writing">一、机房环境<li>
		  	
			<li class="insp_width4_1 insp_IsColor">温度</li>
			<li class="insp_width4_2">
				<input name="inspection02.inspTp02" style="width:30px;height:20px;">℃
			</li>
			<li class="insp_width4_3 insp_IsColor">湿度</li>
			<li class="insp_width4_4">
				<!-- <input name="inspection02.insp_hum_02" style="width:30px;height:28px;">% -->
				<input type="radio" name="inspection02.inspHum02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspHum02" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">痕迹</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspHj02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspHj02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">清洁</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspQj02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspQj02" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">异响</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspYx02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspYx02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">异味</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspYw02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspYw02" value="N">异常
			</li>
			
			<li class="insp_width1_1 insp_writing">二、周边环境<li>
			<li class="insp_width4_1 insp_IsColor">电源</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspPw02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspPw02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">电池组</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspBat02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspBat02" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">空调</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspAir02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspAir02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">消防</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspFire02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspFire02" value="N">异常
			</li>
			
			<li class="insp_width1_1 insp_writing">三、网络环境<li>
			<li class="insp_width4_1 insp_IsColor">光收发（测试环境）</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspOttest02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspOttest02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">光收发（厂内网络）</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspOtfa02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspOtfa02" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">光收发(监控3)</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspOtmo302" value="Y" checked>正常
				<input type="radio" name="inspection02.inspOtmo302" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor"></li>
			<li class="insp_width4_4"></li>
			
			<li class="insp_width4_1 insp_IsColor">电信波分设备</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspTelwdm02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspTelwdm02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">电信UPS设备</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspTelups02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspTelups02" value="N">异常
			</li>
			
			<li class="insp_width4_1 insp_IsColor">联通波分设备</li>
			<li class="insp_width4_2">
				<input type="radio" name="inspection02.inspUniwdm02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspUniwdm02" value="N">异常
			</li>
			<li class="insp_width4_3 insp_IsColor">联通UPS设备</li>
			<li class="insp_width4_4">
				<input type="radio" name="inspection02.inspUniups02" value="Y" checked>正常
				<input type="radio" name="inspection02.inspUniups02" value="A">异常
			</li>
			
			<li class="insp_width2_1 insp_IsColor">备注</li>
			<li class="insp_width2_2">
				<textarea id='insp_remark_02' name='inspection02.inspRemark02' rows="2" cols="70"></textarea>
			</li>
		  </ul>
		  </form>
		  <div style="clear:both"></div>
		</div>
	</div>
	
	
	
	<div title="待添加" hidden="">
		<div id="insp">
		<form id='inspection03_add_form' method='post'>
		  <ul>
		  	<li class="insp_width1_1 insp_writing" style="text-align:center;">UPS机房巡检表<li>
		  	<li class="insp_width4_2 insp_IsColor">时间</li>
			<li class="insp_width4_1">
				<input id="insp_time_03" name="inspection03.inspTime03" class="easyui-datetimebox" data-options="required:true,editable:false" 
					 		style="width:200px;border-bottom:1px solid #ccc;"/>  
			</li>
			<li class="insp_width4_4 insp_IsColor" style="border-right:1px solid #ccc;">备注</li>
			<li class="insp_width4_3">
				<textarea id='insp_remark_03' name='inspection03.inspRemark03' rows="2" cols="40"></textarea>
			</li>
		  	
		  	<li class="insp_width1_1 insp_writing">一、机房环境<li>
		  	
			<li class="insp_width6_1 insp_IsColor">温度</li>
			<li class="insp_width6_2">
				<input name="inspection03.inspTp03" style="width:30px;height:20px;">℃
			</li>
			<li class="insp_width6_3 insp_IsColor">湿度</li>
			<li class="insp_width6_4">
				<!-- <input name="inspection03.insp_hum_03" style="width:30px;height:28px;">% -->
				<input type="radio" name="inspection03.inspHum03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspHum03" value="N">异常
			</li>
			
			<li class="insp_width6_5 insp_IsColor">痕迹</li>
			<li class="insp_width6_6">
				<input type="radio" name="inspection03.inspHj03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspHj03" value="N">异常
			</li>
			<li class="insp_width6_1 insp_IsColor">清洁</li>
			<li class="insp_width6_2">
				<input type="radio" name="inspection03.inspQj03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspQj03" value="N">异常
			</li>
			
			<li class="insp_width6_3 insp_IsColor">异响</li>
			<li class="insp_width6_4">
				<input type="radio" name="inspection03.inspYx03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspYx03" value="N">异常
			</li>
			<li class="insp_width6_5 insp_IsColor">异味</li>
			<li class="insp_width6_6">
				<input type="radio" name="inspection03.inspYw03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspYw03" value="N">异常
			</li>
			
			<li class="insp_width1_1 insp_writing">二、周边环境<li>
			<li class="insp_width6_1 insp_IsColor">电源</li>
			<li class="insp_width6_2">
				<input type="radio" name="inspection03.inspPw03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspPw03" value="N">异常
			</li>
			
			<li class="insp_width6_3 insp_IsColor">空调</li>
			<li class="insp_width6_4">
				<input type="radio" name="inspection03.inspAir03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspAir03" value="N">异常
			</li>
			<li class="insp_width6_5 insp_IsColor">消防</li>
			<li class="insp_width6_6">
				<input type="radio" name="inspection03.inspFire03" value="Y" checked>正常
				<input type="radio" name="inspection03.inspFire03" value="N">异常
			</li>
			
			<li class="insp_width6_1 insp_IsColor">电池组1</li>
			<li class="insp_width6_2">
				<input type="radio" name="inspection03.inspBatpw103" value="Y" checked>ON
				<input type="radio" name="inspection03.inspBatpw103" value="N">OFF
			</li>
			<li class="insp_width6_3">
				<input type="radio" name="inspection03.inspBat103" value="Y" checked>正常
				<input type="radio" name="inspection03.inspBat103" value="N">异常
			</li>
			
			<li class="insp_width6_4 insp_IsColor">电池组2</li>
			<li class="insp_width6_5">
				<input type="radio" name="inspection03.inspBatpw203" value="Y" checked>ON
				<input type="radio" name="inspection03.inspBatpw203" value="N">OFF
			</li>
			<li class="insp_width6_6">
				<input type="radio" name="inspection03.inspBat203" value="Y" checked>正常
				<input type="radio" name="inspection03.inspBat203" value="N">异常
			</li>
			
			<li class="insp_width6_1 insp_IsColor">电池组3</li>
			<li class="insp_width6_2">
				<input type="radio" name="inspection03.inspBatpw303" value="Y" checked>ON
				<input type="radio" name="inspection03.inspBatpw303" value="N">OFF
			</li>
			<li class="insp_width6_3">
				<input type="radio" name="inspection03.inspBat303" value="Y" checked>正常
				<input type="radio" name="inspection03.inspBat303" value="N">异常
			</li>
			<li class="insp_width6_4 insp_IsColor">电池组4</li>
			<li class="insp_width6_5">
				<input type="radio" name="inspection03.inspBatpw403" value="Y" checked>ON
				<input type="radio" name="inspection03.inspBatpw403" value="N">OFF
			</li>
			<li class="insp_width6_6">
				<input type="radio" name="inspection03.inspBat403" value="Y" checked>正常
				<input type="radio" name="inspection03.inspBat403" value="N">异常
			</li>
			
			<li class="insp_width6_1 insp_IsColor">电池组5</li>
			<li class="insp_width6_2">
				<input type="radio" name="inspection03.inspBatpw503" value="Y" checked>ON
				<input type="radio" name="inspection03.inspBatpw503" value="N">OFF
			</li>
			<li class="insp_width6_3">
				<input type="radio" name="inspection03.inspBat503" value="Y" checked>正常
				<input type="radio" name="inspection03.inspBat503" value="N">异常
			</li>
			<li class="insp_width6_4 insp_IsColor">电池组6</li>
			<li class="insp_width6_5">
				<input type="radio" name="inspection03.inspBatpw603" value="Y" checked>ON
				<input type="radio" name="inspection03.inspBatpw603" value="A">OFF
			</li>
			<li class="insp_width6_6">
				<input type="radio" name="inspection03.inspBat603" value="Y" checked>正常
				<input type="radio" name="inspection03.inspBat603" value="A">异常
			</li>
			
			
			<li class="insp_width1_1 insp_writing">三、网络设备<li>
			<li class="insp_width5_1 insp_IsColor">(120KVA)UPS主机1</li>
			<li class="insp_width5_2">
				<input type="checkbox" name="inspection03.inspUps1Out03" value="Y" checked>OUT
				<input type="checkbox" name="inspection03.inspUps1By03" value="Y">BY
				<input type="checkbox" name="inspection03.inspUps1Batt03" value="Y">BATT
			</li>
			<li class="insp_width5_3">
				<input name="inspection03.inspUps1Outb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_4">
				<input name="inspection03.inspUps1Battb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_5">
				<input type="radio" name="inspection03.inspUps103" value="Y" checked>正常
				<input type="radio" name="inspection03.inspUps103" value="N">异常
			</li>
			
			
			<li class="insp_width5_1 insp_IsColor">(160KVA)UPS主机2</li>
			<li class="insp_width5_2">
				<input type="checkbox" name="inspection03.inspUps2Out03" value="Y" checked>OUT
				<input type="checkbox" name="inspection03.inspUps2By03" value="Y">BY
				<input type="checkbox" name="inspection03.inspUps2Batt03" value="Y">BATT
			</li>
			<li class="insp_width5_3">
				<input name="inspection03.inspUps2Outb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_4">
				<input name="inspection03.inspUps2Battb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_5">
				<input type="radio" name="inspection03.inspUps203" value="Y" checked>正常
				<input type="radio" name="inspection03.inspUps203" value="N">异常
			</li>
			
			<li class="insp_width5_1 insp_IsColor">(200KVA(一))UPS主机3</li>
			<li class="insp_width5_2">
				<input type="checkbox" name="inspection03.inspUps3Out03" value="Y" checked>OUT
				<input type="checkbox" name="inspection03.inspUps3By03" value="Y">BY
				<input type="checkbox" name="inspection03.inspUps3Batt03" value="Y">BATT
			</li>
			<li class="insp_width5_3">
				<input name="inspection03.inspUps3Outb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_4">
				<input name="inspection03.inspUps3Battb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_5">
				<input type="radio" name="inspection03.inspUps303" value="Y" checked>正常
				<input type="radio" name="inspection03.inspUps303" value="N">异常
			</li>
			
			<li class="insp_width5_1 insp_IsColor">(200KVA(二))UPS主机4</li>
			<li class="insp_width5_2">
				<input type="checkbox" name="inspection03.inspUps4Out03" value="Y" checked>OUT
				<input type="checkbox" name="inspection03.inspUps4By03" value="Y">BY
				<input type="checkbox" name="inspection03.inspUps4Batt03" value="Y">BATT
			</li>
			<li class="insp_width5_3">
				<input name="inspection03.inspUps4Outb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_4">
				<input name="inspection03.inspUps4Battb03" style="width:30px;height:20px;">%
			</li>
			<li class="insp_width5_5">
				<input type="radio" name="inspection03.inspUps403" value="Y" checked>正常
				<input type="radio" name="inspection03.inspUps403" value="N">异常
			</li>
		  </ul>
		  </form>
		</div>
	</div>
</div>

	


			
	



	
	
