package com.baodian.service.handover.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.handover.AskLeaveDao;
import com.baodian.dao.handover.TransferDao;
import com.baodian.model.handover.AskLeave;
import com.baodian.model.handover.Transfer;
import com.baodian.service.handover.ApplicationManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.vo.DutySchedule;

@Service("applicationManager")
public class ApplicationManagerImpl implements ApplicationManager{
	
	private AskLeaveDao askLeaveDao;
	private TransferDao transferDao;
	/**
	 * 获取请假调班信息
	 */
	public String userPost() throws ParseException{
		String json = "";
		if(SecuManagerImpl.currentName().isEmpty()==true){
			json = "{\"status\":2,\"mess\":\"请先登录系统！\"}";
			return json;
		}
		String userName = SecuManagerImpl.currentName();
		String susername = "";
		String sposts = "";
		Date date = new Date();
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = fdate.format(date);
		List<DutySchedule> dslist = ClassTableManagerImpl.getClassTable(0,fdate.parse(nowDate));
		for(DutySchedule ds : dslist){
			
			String[] screw = ds.getCrew().split("#");
			for(int i=0;i<screw.length;i++){
				if(screw[i].equals(userName)){
					sposts = "运维值班员";
				}
			}
		}
		susername = userName;
		String staff ="[";
		if(sposts.equals("运维值班员")||sposts.equals("运维班长")){
			for(DutySchedule ds : dslist){
				String[] screw = ds.getCrew().split("#");
				for(int i=0;i<screw.length;i++){
					if(!screw[i].equals(susername)&&screw[i]!=""){
						staff = staff + "\""+screw[i]+"\",";
					}
				}
				
			}
			staff = staff + "\"\"]";
		}
		else{
			json = "{\"status\":1,\"mess\":\"你非值班人员无需使用该功能\"}";
			return json;
		}
		json = "{\"status\":0,\"username\":\""+susername+"\",\"posts\":\""+sposts+"\",\"staff\":"+staff+"}";
		return json;
	}
	
	
	/**
	 * 请假申请
	 */
	public String ask_leave(String username,String begindate,String enddate) throws ParseException{
		String json = "";
		Date now = new Date();
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = fdate.format(now);
		if(begindate==null||enddate==null||begindate.length()==0||enddate.length()==0){
			json = "{\"status\":1,\"reason\":\"启始时间和结束时间不能为空！\"}";
			return json;
		}
		if(fdate.parse(begindate).getTime() > fdate.parse(enddate).getTime() ){
			json = "{\"status\":1,\"reason\":\"启始时间比结束时间大！\"}";
			return json;
		}
		if(fdate.parse(begindate).getTime() < fdate.parse(nowDate).getTime() ){
			json = "{\"status\":1,\"reason\":\"启始时间比当前时间小！\"}";
			return json;
		}
		AskLeave askleave = new AskLeave();
		askleave.setApplicant(username);
		askleave.setStartDate(begindate);
		askleave.setEndDate(enddate);
		askleave.setWhether("1");
		askleave.setApplicationTime(nowDate);
		askLeaveDao.save(askleave);
		json = "{\"status\":0}";
		return json;
	}
	
	/**
	 * 调班申请
	 */
	public String apl_transfer(String applicant,String applicant_time,String object,String t_or_r){
		String json = "";
		Date now = new Date();
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDate = fdate.format(now);
		if(applicant_time==null||applicant_time.length()==0){
			json = "{\"status\":1,\"reason\":\"时间不能为空\"}";
			return json;
		}
		if(object==null||object.equals("0")){
			json = "{\"status\":1,\"reason\":\"请选择调班对象！\"}";
			return json;
		}
		Transfer apl_transfer = new Transfer();
		apl_transfer.setApplicant(applicant);
		apl_transfer.setApplicantTime(applicant_time);
		apl_transfer.setObject(object);
		apl_transfer.setApplicationTime(nowDate);
		if(t_or_r.equals("t")){
			apl_transfer.setWhether("1");
		}
		if(t_or_r.equals("r")){
			apl_transfer.setWhether("2");
		}
		transferDao.save(apl_transfer);
		json = "{\"status\":0}";
		return json;
	}
	
	@Resource(name = "askLeaveDao")
	public void setAskLeaveDao(AskLeaveDao askLeaveDao) {
		this.askLeaveDao = askLeaveDao;
	}
	@Resource(name = "transferDao")
	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}
}
