package com.baodian.service.record.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.record.RecordDao;
import com.baodian.model.record.Trouble_Record;
import com.baodian.model.record.Working_Record;
import com.baodian.service.page.PageManager;
import com.baodian.service.record.RecordManager;
import com.baodian.service.user.UserManager;

@Service("recordManager")
public class RecordManagerImpl implements RecordManager {

	@Resource(name="recordDao")
	private RecordDao recordDao;
	
	@Resource(name="pageManager")
	private PageManager pagemanager;
	
	@Resource(name="userManager")
	private UserManager usermanager;
	
	public void addWorkingRecord(Working_Record wr) {
		recordDao.addWorkingRecord(wr);
	}
	
	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}

/*	public void setPagemanager(PageManager pagemanager) {
		this.pagemanager = pagemanager;
	}*/

	public Working_Record findWR_by_ID(int id) {
		Working_Record wr = recordDao.findWR_by_ID(id);
		return wr;
	}
	
	public Working_Record findWR_by_UseridAndTime(int u_id, Date time) {
		Working_Record wr = recordDao.findWR_by_UseridAndTime(u_id, time);
		return wr;
	}
	
	public void updateWorkingRecord(Working_Record wr) {
		recordDao.updateWorkingRecord(wr);
	}

	public void deleteWorkingRecord(int id) {
		recordDao.deleteWorkingRecord(id);
	}

	public void addTroubleRecord(Trouble_Record tr) {
		recordDao.addTroubleRecord(tr);
		
	}

	public void deleteTroubleRecord(int id) {
		recordDao.deleteTroubleRecord(id);
	}

	public Trouble_Record findTR_by_ID(int id) {
		return recordDao.findTR_by_ID(id);
	}

	public void updateTroubleRecord(Trouble_Record tr) {
		recordDao.updateTroubleRecord(tr);
	}
	
	public String wr_listTable(int num, int page){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		String current_date = df.format(new Date());
		String time_hql = "date_format(wr.time,'%Y-%m-%d')=date_format('"+ current_date +"','%Y-%m-%d') ";
		String hql = "select new Working_Record(wr.id,wr.detail,wr.time,wr.type,u.id,u.name,u.account,u.dpm.name,wr.file_name)" +
					  "from Working_Record wr,User u where wr.user.id=u.id " +
				      "and " + time_hql + "order by wr.time desc";
		String total_hql = "select count(*) from Working_Record wr where " + time_hql;
		List<Working_Record> wr_list = (List<Working_Record>) pagemanager.getCurrentPageRecord(num, page, hql);
		int total = pagemanager.getRecordCount(total_hql.toString());    //总记录数
		return WRToJson(wr_list, total);
	}

	public String wr_secrchByCondition(int num, int page, String from_date,
			String to_date, String type, String dept_name, String username) {
		StringBuffer search_hql = new StringBuffer("select new Working_Record(wr.id,wr.detail,wr.time,wr.type,u.id,u.name,u.account,u.dpm.name,wr.file_name)" +
					  	  	  	  "from Working_Record wr,User u where wr.user.id=u.id ");
		StringBuffer total_hql = new StringBuffer("select count(*) from Working_Record wr where 1=1 ");
		StringBuffer conditionHQL = wr_generateConditionHQL(from_date, to_date, type, dept_name, username);
		search_hql.append(conditionHQL).append("order by wr.time desc");
		total_hql.append(conditionHQL);
		
		List<Working_Record> wr_list = (List<Working_Record>) pagemanager.getCurrentPageRecord(num, page, search_hql.toString());
		int total = pagemanager.getRecordCount(total_hql.toString());    //总记录数
		return WRToJson(wr_list, total);
	}
	
	public String wr_searchByKeyword(int num,int page,String keyword) {
		String HQL = "select new Working_Record(wr.id,wr.detail,wr.time,wr.type,u.id,u.name,u.account,wr.user.dpm.name,wr.file_name)" +
	            " from Working_Record wr,User u where wr.user.id=u.id and wr.detail like '%"+keyword+"%' order by wr.time desc";
		String pagenumber_HQL = "select count(*)" +
	            " from Working_Record wr,User u where wr.user.id=u.id and wr.detail like '%"+keyword+"%'";
		List<Working_Record> WRList = pagemanager.getCurrentPageRecord(num, page, HQL);
		int total = pagemanager.getRecordCount(pagenumber_HQL);
		return WRToJson(WRList, total);
	}

	public String tr_searchByKeyword(int num, int page, String keyword) {
		String HQL = "select new Trouble_Record(tr.id,tr.IP,tr.detail,tr.state,tr.f_time,tr.f_user.id,tr.f_user.name," +
					  "tr.solve_approach,tr.s_time,tr.s_user.id,tr.r_time,tr.r_user.id,tr.r_user.name,tr.filename) " +
				      " from Trouble_Record tr where tr.detail like '%" + keyword +"%' order by tr.r_time desc";
		String pagenumber_HQL = "select count(*) from Trouble_Record tr where tr.detail like '%" + keyword +"%'";
		List tr_list = pagemanager.getCurrentPageRecord(num, page, HQL);
		int total = pagemanager.getRecordCount(pagenumber_HQL);
		
		String f_time;
		String s_time;
		String r_time;
		String s_user_name;
		String solve_approach;
		String detail;
		Trouble_Record trouble_record;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String json="{\"total\":"+total+",\"rows\":[";
		Iterator it = tr_list.iterator();
		while(it.hasNext()){
			s_user_name = "";
			trouble_record = (Trouble_Record) it.next();
			f_time = sdf.format(trouble_record.getF_time()).toString();
			if(trouble_record.getS_time() == null){
				s_time = "";
			}else{
				s_time = sdf.format(trouble_record.getS_time()).toString();
			}
			r_time = sdf.format(trouble_record.getR_time()).toString();
			if(trouble_record.getS_user().getId() != 0){
				s_user_name = usermanager.findU_inadrById(trouble_record.getS_user().getId()).getName();
			}
			solve_approach=((trouble_record.getSolve_approach()==null) ? "":trouble_record.getSolve_approach());
			detail = trouble_record.getDetail();
			detail = detail.replaceAll(keyword, "<font color=red>"+keyword+"</font>");
			
			json = json.concat("{\"record_id\":\""+trouble_record.getId()+
					"\",\"IP\":\""+trouble_record.getIP()+
					"\",\"detail\":\""+detail+
					"\",\"state\":\""+trouble_record.getState()+
					"\",\"f_time\":\""+f_time+
					"\",\"f_user\":\""+trouble_record.getF_user().getName()+
					"\",\"solve_approach\":\""+solve_approach+
					"\",\"s_time\":\""+s_time+
					"\",\"s_user\":\""+s_user_name+
					"\",\"r_time\":\""+r_time+
					"\",\"r_user\":\""+trouble_record.getR_user().getName()
					);
			if(trouble_record.getFilename() != null){     //附件
				String filename = trouble_record.getFilename();
				String filePath = "documents/attachments/"+filename;
				json = json.concat("\",\"attachment\":\"<a target='_balnk' title='"+filename+"' href='"+filePath+"'>附件</a>");
			}
			json = json.concat("\"},");
		}
		if(!tr_list.isEmpty()){   //有记录
			json = json.substring(0, json.length()-1);   //去掉逗号
		}
		json = json.concat("]}");
		//System.out.println(json);
		return json;
	}

	@Override
	public LinkedHashMap count4Analyse(String hql) {
		LinkedHashMap map = new LinkedHashMap();
		List list = recordDao.count4Analyse(hql);
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				Object[] object = (Object[]) list.get(i);
				String date = object[0].toString();
				String num = object[1].toString();
				//System.out.println("object[0].class = " + object[0].getClass()+",object[1].class = " + object[1].getClass());
				//System.out.println("date = " + date + ",num = " + num);
				map.put(date, num);
			}
		}else{
			map = null;
		}
		return map;
	}
	
	public StringBuffer wr_generateConditionHQL(String from_date,String to_date, String type,
			                                 String dept_name, String username){
		StringBuffer temp = new StringBuffer("");
		StringBuffer time_hql = new StringBuffer("");
		if(from_date == null){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
			from_date = df.format(new Date());
		}
		if(from_date != null && !from_date.equals("")){
			if(from_date.length() == 7){		//月
				time_hql.append("and date_format(wr.time,'%Y-%m')=date_format('"+ from_date +"-01','%Y-%m') ");
			}else if(from_date.length() == 10){		//日	
				time_hql.append("and date_format(wr.time,'%Y-%m-%d')=date_format('"+ from_date +"','%Y-%m-%d') ");
			}
		}
		if(to_date != null && !to_date.equals("")){   //自定义时间段
			time_hql.delete(0, time_hql.length());    //清空
			if(to_date !=null && to_date.length() == 7){    //月
				if(from_date != null && !from_date.equals("")){
					time_hql.append("and date_format(wr.time, '%Y-%m')>=date_format('"+from_date+"-01','%Y-%m') " +
							        "and date_format(wr.time, '%Y-%m')<=date_format('"+to_date+"-01','%Y-%m') ");
				}else{   //未指定起始时间，查出结束时间当天记录
					time_hql.append("and date_format(wr.time, '%Y-%m')=date_format('"+to_date+"-01','%Y-%m') ");
				}
				
			}else if(to_date !=null && to_date.length() == 10){   //日
				if(from_date != null && !from_date.equals("")){
					time_hql.append("and date_format(wr.time, '%Y-%m-%d')>=date_format('"+from_date+"','%Y-%m-%d') " +
					                "and date_format(wr.time, '%Y-%m-%d')<=date_format('"+to_date+"','%Y-%m-%d') ");
				}else{   //未指定起始时间，查出结束时间当天记录
					time_hql.append("and date_format(wr.time, '%Y-%m-%d')=date_format('"+to_date+"','%Y-%m-%d') ");
				}
				
			}
		}
		temp.append(time_hql);
		if(type != null && !type.equals("")){
			if(type.equals("全部")){
				//无
			}else{
				temp.append("and type='" + type + "' ");
			}
		}
		if(dept_name != null && !dept_name.equals("")){
			temp.append("and wr.user.dpm.name like '%"+dept_name+"%' ");
		}
		if(username !=null && !username.equals("")){
			temp.append("and wr.user.name='" + username + "' ");
		}
		
		return temp;
	}
	
	public String WRToJson(List<Working_Record> WRList,int total){       //Working_Record list 转换成json
		String time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		obj.put("total", total);
		JSONArray rows = new JSONArray();
		for(Working_Record wr : WRList){
			JSONObject row = new JSONObject();
			row.put("record_id", wr.getId());
			row.put("detail", wr.getDetail());
			row.put("type", wr.getType());
			time = sdf.format(wr.getTime()).toString();
			row.put("time", time);
			row.put("username", wr.getUser().getName());
			row.put("dept", wr.getUser().getDpm().getName());
			if(wr.getFile_name() != null){       //附件
				String filename = wr.getFile_name();
				String filePath = "documents/attachments/"+filename;
				row.put("attachment", "<a target='_balnk' title='"+filename+"' href='"+filePath+"'>附件</a>");
			}
			rows.add(row);
		}
		obj.put("rows", rows);
		return obj.toJSONString();
	}

}
