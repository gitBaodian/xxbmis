<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击一行编辑或保存；&nbsp;&nbsp;2.点击"操作"栏的一项进行修改。</div>
</div>

<link rel="stylesheet" type="text/css" href="css/inspection.css">
 

<div class="easyui-tabs" data-options="fit:true,plain:true">
	<div title="4楼机房">
		<div id="insp">
		<form id='inspection01_add_form' method='post'>
		  <ul>
		  	<li class="insp_width1_1 insp_writing" style="text-align:center;">4楼服务器机房巡检表<li>
		  	<li class="insp_width2_1 insp_IsColor">时间</li>
			<li class="insp_width2_2">
				${inspection01.inspTime01}
			</li>
		  	
		  	<li class="insp_width1_1 insp_writing">一、机房环境<li>
		  	
			<li class="insp_width4_1 insp_IsColor">温度</li>
			<li class="insp_width4_2">
				${inspection01.inspTp01}℃
			</li>
			<li class="insp_width4_3 insp_IsColor">湿度</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspHum01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">痕迹</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspHj01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">清洁</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspQj01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">异响</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspYx01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">异味</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspYw01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width1_1 insp_writing">二、周边环境<li>
			<li class="insp_width4_1 insp_IsColor">电源</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspPw01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">电池组</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspBat01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">空调</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspAir01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">消防</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspFire01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width1_1 insp_writing">三、网络环境<li>
			<li class="insp_width4_1 insp_IsColor">汇集交换机1</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspPs101" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">汇集交换机2</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspPs201" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">汇集交换机3</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspPs301" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">汇集交换机4</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspPs401" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
		
			<li class="insp_width4_1 insp_IsColor">光收发（测试环境）</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection01.inspOttest01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_3 insp_IsColor">移动信息机</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection01.inspMiu01" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
	
			<li class="insp_width2_1 insp_IsColor">备注</li>
			<li class="insp_width2_2">
				${inspection01.inspRemark01}
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
				${inspection02.inspTime02}	 		
			</li>
		  	
		  	<li class="insp_width1_1 insp_writing">一、机房环境<li>
		  	
			<li class="insp_width4_1 insp_IsColor">温度</li>
			<li class="insp_width4_2">
				${inspection02.inspTp02}℃
			</li>
			<li class="insp_width4_3 insp_IsColor">湿度</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspHum02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">痕迹</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspHj02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">清洁</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspQj02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">异响</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspYx02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">异味</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspYw02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width1_1 insp_writing">二、周边环境<li>
			<li class="insp_width4_1 insp_IsColor">电源</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspPw02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">电池组</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspBat02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">空调</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspAir02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">消防</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspFire02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width1_1 insp_writing">三、网络环境<li>
			<li class="insp_width4_1 insp_IsColor">光收发（测试环境）</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspOttest02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">光收发（厂内网络）</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspOtfa02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">光收发(监控3)</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspOtmo302" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor"></li>
			<li class="insp_width4_4"></li>
			
			<li class="insp_width4_1 insp_IsColor">电信波分设备</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspTelwdm02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">电信UPS设备</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspTelups02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width4_1 insp_IsColor">联通波分设备</li>
			<li class="insp_width4_2">
				<s:set name="name" value="inspection02.inspUniwdm02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width4_3 insp_IsColor">联通UPS设备</li>
			<li class="insp_width4_4">
				<s:set name="name" value="inspection02.inspUniups02" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width2_1 insp_IsColor">备注</li>
			<li class="insp_width2_2">
				${inspection02.inspRemark02}
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
				${inspection03.inspTime03}
			</li>
			<li class="insp_width4_4 insp_IsColor" style="border-right:1px solid #ccc;">备注</li>
			<li class="insp_width4_3">
				${inspection03.inspRemark03}
			</li>
		  	
		  	<li class="insp_width1_1 insp_writing">一、机房环境<li>
		  	
			<li class="insp_width6_1 insp_IsColor">温度</li>
			<li class="insp_width6_2">
				${inspection03.inspTp03}℃
			</li>
			<li class="insp_width6_3 insp_IsColor">湿度</li>
			<li class="insp_width6_4">
				<!-- <input name="inspection03.insp_hum_03" style="width:30px;height:28px;">% -->
				<s:set name="name" value="inspection03.inspHum03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_5 insp_IsColor">痕迹</li>
			<li class="insp_width6_6">
				<s:set name="name" value="inspection03.inspHj03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width6_1 insp_IsColor">清洁</li>
			<li class="insp_width6_2">
				<s:set name="name" value="inspection03.inspQj03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_3 insp_IsColor">异响</li>
			<li class="insp_width6_4">
				<s:set name="name" value="inspection03.inspYx03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width6_5 insp_IsColor">异味</li>
			<li class="insp_width6_6">
				<s:set name="name" value="inspection03.inspYw03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width1_1 insp_writing">二、周边环境<li>
			<li class="insp_width6_1 insp_IsColor">电源</li>
			<li class="insp_width6_2">
				<s:set name="name" value="inspection03.inspPw03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_3 insp_IsColor">空调</li>
			<li class="insp_width6_4">
				<s:set name="name" value="inspection03.inspAir03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width6_5 insp_IsColor">消防</li>
			<li class="insp_width6_6">
				<s:set name="name" value="inspection03.inspFire03" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_1 insp_IsColor">电池组1</li>
			<li class="insp_width6_2">
				<s:set name="name" value="inspection03.inspBatpw103" />
        		<s:if test="#name == 'Y'.toString()">ON</s:if>
        		<s:else>OFF</s:else>
			</li>
			<li class="insp_width6_3">
				<s:set name="name" value="inspection03.inspBat103" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_4 insp_IsColor">电池组2</li>
			<li class="insp_width6_5">
				<s:set name="name" value="inspection03.inspBatpw203" />
        		<s:if test="#name == 'Y'.toString()">ON</s:if>
        		<s:else>OFF</s:else>
			</li>
			<li class="insp_width6_6">
				<s:set name="name" value="inspection03.inspBat203" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_1 insp_IsColor">电池组3</li>
			<li class="insp_width6_2">
				<s:set name="name" value="inspection03.inspBatpw303" />
        		<s:if test="#name == 'Y'.toString()">ON</s:if>
        		<s:else>OFF</s:else>
			</li>
			<li class="insp_width6_3">
				<s:set name="name" value="inspection03.inspBat303" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width6_4 insp_IsColor">电池组4</li>
			<li class="insp_width6_5">
				<s:set name="name" value="inspection03.inspBatpw403" />
        		<s:if test="#name == 'Y'.toString()">ON</s:if>
        		<s:else>OFF</s:else>
			</li>
			<li class="insp_width6_6">
				<s:set name="name" value="inspection03.inspBat403" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width6_1 insp_IsColor">电池组5</li>
			<li class="insp_width6_2">
				<s:set name="name" value="inspection03.inspBatpw503" />
        		<s:if test="#name == 'Y'.toString()">ON</s:if>
        		<s:else>OFF</s:else>
			</li>
			<li class="insp_width6_3">
				<s:set name="name" value="inspection03.inspBat503" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			<li class="insp_width6_4 insp_IsColor">电池组6</li>
			<li class="insp_width6_5">
				<s:set name="name" value="inspection03.inspBatpw603" />
        		<s:if test="#name == 'Y'.toString()">ON</s:if>
        		<s:else>OFF</s:else>
			</li>
			<li class="insp_width6_6">
				<s:set name="name" value="inspection03.inspBat603" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			
			<li class="insp_width1_1 insp_writing">三、网络设备<li>
			<li class="insp_width5_1 insp_IsColor">(120KVA)UPS主机1</li>
			<li class="insp_width5_2">
				<s:set name="name" value="inspection03.inspUps1Out03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>OUT</s:if>
        		<s:else><input type="checkbox" value="0">OUT</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps1By03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BY</s:if>
        		<s:else><input type="checkbox" value="0">BY</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps1Batt03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BATT</s:if>
        		<s:else><input type="checkbox" value="0">BATT</s:else>
			</li>
			<li class="insp_width5_3">
				${inspection03.inspUps1Outb03}%
			</li>
			<li class="insp_width5_4">
				${inspection03.inspUps1Battb03}%
			</li>
			<li class="insp_width5_5">
				<s:set name="name" value="inspection03.inspUps103" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			
			<li class="insp_width5_1 insp_IsColor">(160KVA)UPS主机2</li>
			<li class="insp_width5_2">
				<s:set name="name" value="inspection03.inspUps2Out03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>OUT</s:if>
        		<s:else><input type="checkbox" value="0">OUT</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps2By03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BY</s:if>
        		<s:else><input type="checkbox" value="0">BY</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps2Batt03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BATT</s:if>
        		<s:else><input type="checkbox" value="0">BATT</s:else>
			</li>
			<li class="insp_width5_3">
				${inspection03.inspUps2Outb03}%
			</li>
			<li class="insp_width5_4">
				${inspection03.inspUps2Battb03}%
			</li>
			<li class="insp_width5_5">
				<s:set name="name" value="inspection03.inspUps203" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width5_1 insp_IsColor">(200KVA(一))UPS主机3</li>
			<li class="insp_width5_2">
				<s:set name="name" value="inspection03.inspUps3Out03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>OUT</s:if>
        		<s:else><input type="checkbox" value="0">OUT</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps3By03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BY</s:if>
        		<s:else><input type="checkbox" value="0">BY</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps3Batt03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BATT</s:if>
        		<s:else><input type="checkbox" value="0">BATT</s:else>
			</li>
			<li class="insp_width5_3">
				${inspection03.inspUps3Outb03}%
			</li>
			<li class="insp_width5_4">
				${inspection03.inspUps3Battb03}%
			</li>
			<li class="insp_width5_5">
				<s:set name="name" value="inspection03.inspUps303" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
			
			<li class="insp_width5_1 insp_IsColor">(200KVA(二))UPS主机4</li>
			<li class="insp_width5_2">
				<s:set name="name" value="inspection03.inspUps4Out03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>OUT</s:if>
        		<s:else><input type="checkbox" value="0">OUT</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps4By03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BY</s:if>
        		<s:else><input type="checkbox" value="0">BY</s:else>
        		
        		<s:set name="name" value="inspection03.inspUps4Batt03" />
        		<s:if test="#name == 'Y'.toString()"><input type="checkbox" value="0" checked>BATT</s:if>
        		<s:else><input type="checkbox" value="0">BATT</s:else>
			</li>
			<li class="insp_width5_3">
				${inspection03.inspUps4Outb03}%
			</li>
			<li class="insp_width5_4">
				${inspection03.inspUps4Battb03}%
			</li>
			<li class="insp_width5_5">
				<s:set name="name" value="inspection03.inspUps403" />
        		<s:if test="#name == 'Y'.toString()">正常</s:if>
        		<s:else>异常</s:else>
			</li>
		  </ul>
		  </form>
		</div>
	</div>
</div>

