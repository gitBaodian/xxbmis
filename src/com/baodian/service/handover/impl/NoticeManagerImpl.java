package com.baodian.service.handover.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.handover.AskLeaveDao;
import com.baodian.dao.handover.TransferDao;
import com.baodian.model.handover.AskLeave;
import com.baodian.model.handover.Transfer;
import com.baodian.service.handover.NoticeManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.page.Page;

@Service("noticeManager")
public class NoticeManagerImpl implements NoticeManager{
	
	private AskLeaveDao askLeaveDao;
	private TransferDao transferDao;
	
	/**
	 * 获取当前申请
	 */
	@SuppressWarnings("unchecked")
	public JSONObject notice(){
		String userName = SecuManagerImpl.currentName();
		JSONObject json = new JSONObject();
		List<?> tf_zb_list = transferDao.findhql("select COUNT(*) from Transfer t where (t.applicant = '"+userName+"' or t.object = '"+userName+"')");
		List<?> ak_zb_list = askLeaveDao.findhql("select COUNT(*) from AskLeave t where t.applicant = '"+userName+"'");
		json.put("status", 0);
		json.put("tf_zb", tf_zb_list.get(0));
		json.put("ak_zb", ak_zb_list.get(0));
		/*int tf_zb = ((Long) tf_zb_list.get(0)).intValue();
		int ak_zb = ((Long) ak_zb_list.get(0)).intValue();
		json = "{\"status\":0,\"tf_zb\":\""+tf_zb+"\",\"ak_zb\":\""+ak_zb+"\"}";*/
		return json;
	}
	
	
	/**
	 * 获取请假申请在办列表
	 */
	@SuppressWarnings("unchecked")
	public String get_ak_zb_list(String userName,Page page) throws ParseException{
		String json = "";
		List<?> ak_zb_lists = transferDao.findhql("select COUNT(*) from AskLeave t where t.applicant = '"+userName+"'");
		int ak = ((Long) ak_zb_lists.get(0)).intValue();
		if(ak == 0){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		page.countPage(ak);
		List<AskLeave> ak_zb_list = (List<AskLeave>) askLeaveDao.createQuery("from AskLeave t where t.applicant = '"+userName+"' order by t.applicationTime asc"
				,page.getFirstNum(),page.getNum());
		json = "{\"total\":" + page.getCountNums() + "," +
				"\"rows\":[";
		for(AskLeave ak_zb : ak_zb_list) {
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	        Date date2 = ft.parse( ak_zb.getStartDate() );
	        Date date1 = ft.parse( ak_zb.getEndDate() );
	        long quot = date1.getTime() - date2.getTime();
	        quot = quot / 1000 / 60 / 60 / 24 + 1;
	        
	        String com = "";
	        Date date = new Date();
	        String now = ft.format(date);
	        if(ft.parse(now).getTime() >= date2.getTime()){
	        	com = "已生效";
	        }else{
	        	com = "未生效";
	        } 
	        
			json = json.concat("{\"id\":\"" + ak_zb.getId() + "\"," +
					"\"startDate\":\"" + ak_zb.getStartDate() + "\"," +
					"\"endDate\":\"" + ak_zb.getEndDate() + "\"," +
					"\"quot\":\"" + quot + "\"," +
					"\"applicationTime\":\"" + ak_zb.getApplicationTime() + "\"," +
					"\"dsh\":\""+com+"\"},");
		}
		if(ak_zb_list.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	
	
	
	/**
	 * 调班申请在办列表
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public String get_tf_zb_list(String userName,Page page) throws ParseException{
		String json = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		List<?> tf_zb_lists = transferDao.findhql("select COUNT(*) from Transfer t where t.applicant = '"+userName+"'");
		int tf = ((Long) tf_zb_lists.get(0)).intValue();
		if(tf == 0){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		page.countPage(tf);
		List<Transfer> tf_zb_list = (List<Transfer>) transferDao.createQuery("from Transfer t where (t.applicant = '"+userName+"' or t.object = '"+userName+"') order by t.applicationTime asc"
				,page.getFirstNum(),page.getNum());
		json = "{\"total\":" + page.getCountNums() + "," +
				"\"rows\":[";
		for(Transfer tf_zb : tf_zb_list) {
			
			String com = "";
	        Date date = new Date();
	        String now = ft.format(date);
	        if(ft.parse(now).getTime() >= (ft.parse(tf_zb.getApplicantTime()).getTime())){
	        	com = "已生效";
	        }else{
	        	com = "未生效";
	        }
	        
			json = json.concat("{\"id\":\"" + tf_zb.getId() + "\"," +
					"\"applicant\":\""+ tf_zb.getApplicant() +"\"," +
					"\"applicantTime\":\"" + tf_zb.getApplicantTime() + "\"," +
					"\"object\":\"" + tf_zb.getObject() + "\"," +
					"\"applicationTime\":\"" + tf_zb.getApplicationTime() + "\"," +
					"\"dsh\":\""+com+"\"},");
		}
		if(tf_zb_list.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	
	
	
	/**
	 * 删除请假申请
	 */
	public boolean rm_ak_zb(int id){
		AskLeave ak = new AskLeave();
		ak.setId(id);
		askLeaveDao.delete(ak);
		return true;
	}
	
	/**
	 * 删除调班申请
	 */
	public boolean rm_tf_zb(int id){
		Transfer tf = new Transfer();
		tf.setId(id);
		transferDao.delete(tf);
		return true;
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
