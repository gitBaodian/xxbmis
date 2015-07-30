package com.baodian.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.inspection.Inspection01;
import com.baodian.model.inspection.Inspection02;
import com.baodian.model.inspection.Inspection03;
import com.baodian.service.inspection.InspectionManager;
import com.baodian.util.page.Page;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("inspection")
@Scope("prototype")
public class InspectionAction extends ActionSupport{
	
	private InspectionManager inspectionManager;
	private static Inspection01 sinspection01;
	private static Inspection02 sinspection02;
	private static Inspection03 sinspection03;
	private Inspection01 inspection01;
	private Inspection02 inspection02;
	private Inspection03 inspection03;
	private String json;
	private Page  page;
	private String insp_id;
	
	public String manager(){
		return SUCCESS;
	}
	
	public String add(){
		return SUCCESS;
	}
	
	//4楼表单
	public String inspection01_add(){
		if(inspection01.getInspTime01()!=null&&inspection01.getInspTime01().length()!=0
				&&inspection01.getInspTp01()!=null&&inspection01.getInspTp01().length()!=0){
			sinspection01 = inspection01;
			json = "{\"status\":0,\"mess\":\"YES！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"NO！\"}";
		}
		return "json";
	}
	//网络室表单
	public String inspection02_add(){
		if(inspection02.getInspTime02()!=null&&inspection02.getInspTime02().length()!=0
				&&inspection02.getInspTp02()!=null&&inspection02.getInspTp02().length()!=0){
			sinspection02 = inspection02;
			json = "{\"status\":0,\"mess\":\"YES！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"NO！\"}";
		}
		return "json";
	}
	//UPS表单
	public String inspection03_add(){
		if(true){
			//inspection03.getInspTime03()!=null&&inspection03.getInspTime03().length()!=0
			//&&inspection03.getInspTp03()!=null&&inspection03.getInspTp03().length()!=0
			//&&inspection03.getInspUps1Outb03()!=null&&inspection03.getInspUps1Outb03().length()!=0&&inspection03.getInspUps1Battb03()!=null&&inspection03.getInspUps1Battb03().length()!=0
			//&&inspection03.getInspUps2Outb03()!=null&&inspection03.getInspUps2Outb03().length()!=0&&inspection03.getInspUps2Battb03()!=null&&inspection03.getInspUps2Battb03().length()!=0
			//&&inspection03.getInspUps3Outb03()!=null&&inspection03.getInspUps3Outb03().length()!=0&&inspection03.getInspUps3Battb03()!=null&&inspection03.getInspUps3Battb03().length()!=0
			//&&inspection03.getInspUps4Outb03()!=null&&inspection03.getInspUps4Outb03().length()!=0&&inspection03.getInspUps4Battb03()!=null&&inspection03.getInspUps4Battb03().length()!=0
			sinspection03 = inspection03;
			json = "{\"status\":0,\"mess\":\"YES！\"}";
		}
		//else{
		//	json = "{\"status\":1,\"mess\":\"NO！\"}";
		//}
		return "json";
	}
	//添加全部巡检记录
	public String inspection_all_add(){
		if(sinspection03.getInspUps1Batt03()==null) sinspection03.setInspUps1Batt03("N");
		if(sinspection03.getInspUps1By03()==null) sinspection03.setInspUps1By03("N");
		if(sinspection03.getInspUps1Out03()==null) sinspection03.setInspUps1Out03("N");
		if(sinspection03.getInspUps2Batt03()==null) sinspection03.setInspUps2Batt03("N");
		if(sinspection03.getInspUps2By03()==null) sinspection03.setInspUps2By03("N");
		if(sinspection03.getInspUps2Out03()==null) sinspection03.setInspUps2Out03("N");
		if(sinspection03.getInspUps3Batt03()==null) sinspection03.setInspUps3Batt03("N");
		if(sinspection03.getInspUps3By03()==null) sinspection03.setInspUps3By03("N");
		if(sinspection03.getInspUps3Out03()==null) sinspection03.setInspUps3Out03("N");
		if(sinspection03.getInspUps4Batt03()==null) sinspection03.setInspUps4Batt03("N");
		if(sinspection03.getInspUps4By03()==null) sinspection03.setInspUps4By03("N");
		if(sinspection03.getInspUps4Out03()==null) sinspection03.setInspUps4Out03("N");
		
		json = inspectionManager.inspection_all_add(sinspection01, sinspection02, sinspection03);
		return "json";
	}
	
	//巡检记录列表
	public String inspection_show(){
		json = inspectionManager.inspection_show(page);
		return "json";
	}
	
	//获取巡检表
	public String get(){
		inspection01 = inspectionManager.inspection01_get(insp_id);
		inspection02 = inspectionManager.inspection02_get(insp_id);
		inspection03 = inspectionManager.inspection03_get(insp_id);
		return SUCCESS;
	}
	
	//删除巡检表
	public String inspection_del(){
		json = inspectionManager.inspection_del(insp_id);
		return "json";
	}
	
	
	
	@Resource(name = "inspectionManager")
	public void setInspectionManager(InspectionManager inspectionManager) {
		this.inspectionManager = inspectionManager;
	}
	
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Inspection01 getInspection01() {
		return inspection01;
	}
	public void setInspection01(Inspection01 inspection01) {
		this.inspection01 = inspection01;
	}
	public Inspection02 getInspection02() {
		return inspection02;
	}
	public void setInspection02(Inspection02 inspection02) {
		this.inspection02 = inspection02;
	}
	public Inspection03 getInspection03() {
		return inspection03;
	}
	public void setInspection03(Inspection03 inspection03) {
		this.inspection03 = inspection03;
	}
	public String getInsp_id() {
		return insp_id;
	}
	public void setInsp_id(String insp_id) {
		this.insp_id = insp_id;
	}
}
