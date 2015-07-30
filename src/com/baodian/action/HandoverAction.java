package com.baodian.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.handover.Shift;
import com.baodian.service.handover.HandoverManager;
import com.baodian.service.handover.HandoverShowManager;
import com.baodian.service.handover.impl.ClassTableManagerImpl;
import com.baodian.service.handover.impl.HandoverManagerImpl;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.page.Page;
import com.baodian.vo.DutySchedule;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
@Component("handover")
@Scope("prototype")
public class HandoverAction extends ActionSupport{
	
	private String class_ = "";
	private String date = "";
	private String squad = "";
	private String crew = "";
	private String remark_accept = "";
	private String remark_shift = "";
	private String json;
	private String addnp;//是否添加进每日记事
	private Page  page;
	private int id;
	private HandoverManager handoverManager;
	private HandoverShowManager handoverShowManager;
	
	
	
	public String manager(){
		return SUCCESS;
	}
	
	/**
	 * 交接班列表
	 * @return
	 * @throws ParseException
	 */
	public String handover_show() throws ParseException{
		json = handoverShowManager.handover_show(page);
		return "json";
	}
	
	/**
	 * 获取交接班记录
	 * @return
	 */
	public String handover_get(){
		json = handoverShowManager.handover_get(id);
		return "json";
	}
	
	/**
	 * 补签交接记录
	 * @return
	 * @throws ParseException
	 */
	public String handover_add() throws ParseException{
		//SimpleDateFormat fnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat ftime = new SimpleDateFormat("HH:mm:ss");
		Date d = fdate.parse(date);
		String s = fdate.format(fdate.parse(date));
		Date now = new Date();
		Date n = fdate.parse(fdate.format(now));
		if(d.getTime()>n.getTime()){
			json = "{\"status\":1,\"mess\":\"您输入的时间超过当前时间！\"}";
			return "json";
		}
		List<DutySchedule> classlist = ClassTableManagerImpl.finalClassTable(0, d ,s);
		DutySchedule ds_com = new DutySchedule();
		String successionTime = "";
		String shiftTime = "";
		for(DutySchedule ds : classlist){
			if(ds.getClass_().length()!=ds.getClass_().replace(class_,"").length()){
				ds_com = ds;
			}
		}
		//**********后夜超过凌晨
		Date sdate = HandoverManagerImpl.addDay(d, 1);
		String date2 = fdate.format(sdate);
		//if(ds_com.getBc().equals("前夜")){successionTime = date+" "+ duskS; shiftTime = date2+" "+ duskE;}
		if(ds_com.getBc().equals("白班")){successionTime = date+" "+ HandoverManagerImpl.morningE; shiftTime = date+" "+ HandoverManagerImpl.duskE;}
		if(ds_com.getBc().equals("前夜")){successionTime = date+" "+ HandoverManagerImpl.duskE; shiftTime = date2+" "+ HandoverManagerImpl.nightE;}
		if(ds_com.getBc().equals("后夜")){successionTime = date+" "+ HandoverManagerImpl.nightE; shiftTime = date+" "+ HandoverManagerImpl.morningE;}
		if(ds_com.getBc().equals("休息")){
			json = "{\"status\":1,\"mess\":\"该值休息，无需补签！\"}";
			return "json";
		}
		
		
		String com_time = ftime.format(now);
		String[][] BC = HandoverManagerImpl.get_nowtime_bc(com_time,n);
		String com = BC[0][0];
		n = fdate.parse(BC[0][1]);

		
		if(remark_accept.length()==0){remark_accept= "应用无异常，各环境无异常。";}
		if(remark_shift.length()==0){remark_shift= "应用无异常，各环境无异常。";}
		Shift shift = new Shift();
		shift.setClass_(ds_com.getClass_());
		shift.setCrew(ds_com.getCrew());
		shift.setRemarkAccept(JSONValue.escape(remark_accept));
		shift.setWork(ds_com.getBc());
		shift.setSuccessionTime(successionTime);
		shift.setWdate(s);
		if(d.getTime()!=n.getTime()||!(ds_com.getBc().equals(com))){
			shift.setRemarkShift(JSONValue.escape(remark_shift));
			shift.setShiftTime(shiftTime);
		}
		json = handoverShowManager.handover_add(shift);
		return "json";
	}
	
	/**
	 * 交接记录修改
	 * @return
	 */
	public String handover_update(){
		if(remark_accept.length()==0){remark_accept= "应用无异常，各环境无异常。";}
		if(remark_shift.length()==0){remark_shift= "应用无异常，各环境无异常。";}
		Shift shift = new Shift();
		shift.setId(Integer.parseInt(class_));
		shift.setCrew(crew);
		shift.setRemarkAccept(JSONValue.escape(remark_accept));
		shift.setRemarkShift(JSONValue.escape(remark_shift));
		json = handoverShowManager.handover_update(shift);
		return "json";
	}
	
	/**
	 * 删除交接记录
	 * @return
	 */
	public String handover_delete(){
		json = handoverShowManager.handover_delete(id);
		return "json";
	}
	
	/**
	 * 当值人员
	 * @return
	 * @throws ParseException 
	 */
	public String duty_officer() throws ParseException{
		json = handoverManager.duty_officer().toString();
		return "json";
	}
	
	/**
	 * 获取上一值的交班记录
	 * @return
	 */
	public String handover_get_remark_shift(){
		json = handoverManager.handover_get_remark_shift();
		return "json";
	}
	
	/**
	 * 交接班日志
	 * @return
	 * @throws ParseException
	 */
	public String index_handover() {
		json = handoverManager.handover().toString();
		return "json";
	}
	
	/**
	 * 交班
	 * @return
	 * @throws ParseException
	 */
	public  String shift() throws ParseException{
		json = handoverManager.shift(remark_shift, addnp);
		return "json";
	}
	
	/**
	 * 接班
	 * @return
	 * @throws ParseException
	 */
	public String accept() throws ParseException{
		String userName = SecuManagerImpl.currentName();
		if(userName.isEmpty()==true){
			json = "{\"status\":2,\"mess\":\"请先登录系统！\"}";
			return "json";
		}
		json = handoverManager.accept(remark_accept,userName);
		return "json";
	}
	
	
	
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	
	public String getClass_() {
		return class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSquad() {
		return squad;
	}

	public void setSquad(String squad) {
		this.squad = squad;
	}

	public String getCrew() {
		return crew;
	}

	public void setCrew(String crew) {
		this.crew = crew;
	}

	public String getRemark_accept() {
		return remark_accept;
	}

	public void setRemark_accept(String remark_accept) {
		this.remark_accept = remark_accept;
	}

	public String getRemark_shift() {
		return remark_shift;
	}

	public void setRemark_shift(String remark_shift) {
		this.remark_shift = remark_shift;
	}

	public HandoverManager getHandoverManager() {
		return handoverManager;
	}
	
	@Resource(name = "handoverManager")
	public void setHandoverManager(HandoverManager handoverManager) {
		this.handoverManager = handoverManager;
	}

	public HandoverShowManager getHandoverShowManager() {
		return handoverShowManager;
	}

	@Resource(name = "handoverShowManager")
	public void setHandoverShowManager(HandoverShowManager handoverShowManager) {
		this.handoverShowManager = handoverShowManager;
	}

	public String getAddnp() {
		return addnp;
	}

	public void setAddnp(String addnp) {
		this.addnp = addnp;
	}

}
