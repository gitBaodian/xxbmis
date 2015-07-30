package com.baodian.service.record.impl;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.record.CustomerRecordDao;
import com.baodian.model.record.CustomerRecord;
import com.baodian.model.record.Working_Record;
import com.baodian.model.user.User;
import com.baodian.service.page.PageManager;
import com.baodian.service.record.CustomerRecordManager;
import com.baodian.service.record.RecordManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.Export2ExcelManager;
import com.baodian.util.JSONValue;

@Service("customerRecordManager")
public class CustomerRecordManagerImpl implements CustomerRecordManager {
	
	@Resource(name="pageManager")
	private PageManager pagemanager;
	
	@Resource(name="userManager")
	private UserManager usermanager;
	
	@Resource(name="recordManager")
	private RecordManager recordManager;
	
	@Resource(name="export2ExcelManager")
	private Export2ExcelManager export2ExcelManager;
	
	@Resource(name="c_recordDao")
	private CustomerRecordDao crDao;

	public String listTable(int pageSize, int currentPage) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		String current_date = df.format(new Date());
		String timeHQL = " date_format(cr.time, '%Y-%m-%d')=date_format('"+current_date+"','%Y-%m-%d') ";
		String HQL = "select new CustomerRecord(cr.id,cr.time,cr.ticket_id,cr.title,cr.detail," +
					 "cr.type,cr.solve_approach,cr.operator.id,cr.operator.name,cr.r_user.id," +
					 "cr.r_user.name,cr.state) from CustomerRecord cr where " + timeHQL +
					 "order by cr.time desc";
		String totalHQL = "select count(*) from CustomerRecord cr where " + timeHQL;
		List<CustomerRecord> cr_list = (List<CustomerRecord>) pagemanager.getCurrentPageRecord(pageSize, currentPage, HQL);
		int totalNum = pagemanager.getRecordCount(totalHQL);
		return CRListToJson(totalNum,cr_list);
	}

	public String secrchByCondition(int num, int page, String time, String type, String ticket_id,String state){   //条件查询
		StringBuffer HQL = new StringBuffer("select new CustomerRecord(cr.id,cr.time,cr.ticket_id,cr.title,cr.detail," +
				 							  "cr.type,cr.solve_approach,cr.operator.id,cr.operator.name,cr.r_user.id," +
				 							  "cr.r_user.name,cr.state) from CustomerRecord cr where 1=1 ");
		StringBuffer totalHQL = new StringBuffer("select count(*) from CustomerRecord cr where 1=1 ");
		StringBuffer temp = generateConditionHQL(time,type,ticket_id,state);
		HQL = HQL.append(temp);
		totalHQL = totalHQL.append(temp);
		List<CustomerRecord> cr_list = (List<CustomerRecord>) pagemanager.getCurrentPageRecord(num, page, HQL.toString());
		int totalNum = pagemanager.getRecordCount(totalHQL.toString());
		return CRListToJson(totalNum,cr_list);
	}
	
	public String searchByKeyword(int num, int page, String keyword) {
		String HQL = "select new CustomerRecord(cr.id,cr.time,cr.ticket_id,cr.title,cr.detail," +
				 	 "cr.type,cr.solve_approach,cr.operator.id,cr.operator.name,cr.r_user.id," +
				 	 "cr.r_user.name,cr.state) from CustomerRecord cr where cr.detail like '%" + keyword + "%' " +
				 	 "order by cr.time desc";
		String totalHQL = "select count(*) from CustomerRecord cr where cr.detail like '%" + keyword + "%' ";
		List<CustomerRecord> cr_list = (List<CustomerRecord>) pagemanager.getCurrentPageRecord(num, page, HQL);
		int totalNum = pagemanager.getRecordCount(totalHQL);
		return CRListToJson(totalNum,cr_list);
	}

	public String addCustomerRecord(String ticket_id, String type,
			String title, String detail, String solve_approach,
			String op_username, String time) throws Exception {
		if(SecuManagerImpl.currentId() == 0){
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		try{
		CustomerRecord cr = new CustomerRecord();
		if(ticket_id != null && !ticket_id.equals("")){
			cr.setTicket_id(ticket_id);
		}
		if(solve_approach != null && !solve_approach.equals("")){
			solve_approach = JSONValue.escapeLtAndGt(solve_approach);
			cr.setSolve_approach(solve_approach);
			cr.setState("已处理");
		}else{
			cr.setState("未处理");
		}
		if(op_username != null && !op_username.equals("")){
			cr.setOperator(usermanager.getUserByName(op_username));
		}
		cr.setType(type);
		cr.setTitle(title);
		detail = JSONValue.escapeLtAndGt(detail);
		cr.setDetail(detail);
		cr.setR_user(new User(SecuManagerImpl.currentId()));
		cr.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
		crDao.addCustomerRecoed(cr);
		
		/**** 添加到运行记录 ****/
		Working_Record wr = new Working_Record();
		wr.setType("客服记录");
		wr.setDetail("itsm单号：" + ticket_id + "\r\n\r\n事件类型：" + type + 
					 "\r\n\r\n事件内容：\r\n" + detail + "\r\n\r\n解决方法：\r\n" + solve_approach);
		wr.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
		wr.setUser(new User(SecuManagerImpl.currentId()));
		recordManager.addWorkingRecord(wr);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}

	public String updateCustomerRecord(int cr_id, String ticket_id, String type,
			String title, String detail, String solve_approach,
			String op_username, String time) throws Exception {
		if(SecuManagerImpl.currentId() == 0){
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		String json = "";
		CustomerRecord cr = findCRById(cr_id);
		
		if(cr != null){
			/**** 更新对应的运行记录  ****/
			Working_Record wr = recordManager.findWR_by_UseridAndTime(cr.getR_user().getId(), cr.getTime());
			if(wr != null){
				wr.setType("客服记录");
				wr.setDetail(JSONValue.escapeLtAndGt("itsm单号：" + ticket_id + "\r\n\r\n事件类型：" + type + 
							 "\r\n\r\n事件内容：\r\n" + detail + "\r\n\r\n解决方法：\r\n" + solve_approach));
				wr.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
				//wr.setUser(new User(SecuManagerImpl.currentId()));
				recordManager.updateWorkingRecord(wr);
			}
			
			if(ticket_id != null && !ticket_id.equals("")){
				cr.setTicket_id(ticket_id);
			}
			if(solve_approach != null && !solve_approach.equals("")){
				solve_approach = JSONValue.escapeLtAndGt(solve_approach);
				cr.setSolve_approach(solve_approach);
				cr.setState("已处理");
			}else{
				cr.setState("未处理");
			}
			if(op_username != null && !op_username.equals("")){
				cr.setOperator(usermanager.getUserByName(op_username));
			}
			cr.setType(type);
			cr.setTitle(title);
			detail = JSONValue.escapeLtAndGt(detail);
			cr.setDetail(detail);
			//cr.setR_user(new User(SecuManagerImpl.currentId()));
			cr.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
			crDao.updateCustomerRecord(cr);
			
			json = "{\"status\":0,\"mess\":\"修改成功！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"修改失败！\"}";
		}
		
		return json;
	}

	public CustomerRecord findCRById(int cr_id) {
		return crDao.findCRById(cr_id);
	}

	public String delCustomerRecord(int cr_id) {
		if(SecuManagerImpl.currentId() == 0){
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		
		String json = "";
		CustomerRecord cr = findCRById(cr_id);
		if(cr != null){
			/**** 删除对应的运行记录  ****/
			Working_Record wr = recordManager.findWR_by_UseridAndTime(cr.getR_user().getId(), cr.getTime());
			if(wr != null){
				recordManager.deleteWorkingRecord(wr.getId());         //删对应运行记录
	    	}
			
			crDao.delCustomerRecord(cr_id);
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"删除失败！记录不存在！\"}";
		}
		return json;
	}

	public InputStream exportAll2Excel() throws Exception {
		String HQL = "select new CustomerRecord(cr.id,cr.time,cr.ticket_id,cr.title,cr.detail," +
				  	 "cr.type,cr.solve_approach,cr.operator.id,cr.operator.name,cr.r_user.id," +
				  	 "cr.r_user.name,cr.state) from CustomerRecord cr order by cr.time desc";
		String pagenumber_HQL = "select count(*) from CustomerRecord wr";
		int total = pagemanager.getRecordCount(pagenumber_HQL);
		List<CustomerRecord> cr_list = pagemanager.getCurrentPageRecord(total, 1, HQL);
		String[] columnNames = new String[]{"记录时间","itsm单号","事件标题","事件类型","事件内容","解决方法","操作人"};
		String[] columnMethods = new String[]{"getTime","getTicket_id","getTitle","getType","getDetail",
											  "getSolve_approach","acquireOp_name"};
		return export2ExcelManager.export2Excel(columnNames,columnMethods,cr_list);
	}
	
	public InputStream exportCurrent2Excel(String time, String type, String ticket_id,String state) throws Exception {
		StringBuffer HQL = new StringBuffer("select new CustomerRecord(cr.id,cr.time,cr.ticket_id,cr.title,cr.detail," +
				  							"cr.type,cr.solve_approach,cr.operator.id,cr.operator.name,cr.r_user.id," +
				  							"cr.r_user.name,cr.state) from CustomerRecord cr where 1=1 ");
		StringBuffer totalHQL = new StringBuffer("select count(*) from CustomerRecord cr where 1=1 ");
		StringBuffer temp = generateConditionHQL(time,type,ticket_id,state);
		HQL = HQL.append(temp);
		totalHQL = totalHQL.append(temp);
		int totalNum = pagemanager.getRecordCount(totalHQL.toString());
		List<CustomerRecord> cr_list = (List<CustomerRecord>) pagemanager.getCurrentPageRecord(totalNum, 1, HQL.toString());
		String[] columnNames = new String[]{"记录时间","itsm单号","事件标题","事件类型","事件内容","解决方法","操作人"};
		String[] columnMethods = new String[]{"getTime","getTicket_id","getTitle","getType","getDetail",
											  "getSolve_approach","acquireOp_name"};
		return export2ExcelManager.export2Excel(columnNames,columnMethods,cr_list);
	}
	
	public StringBuffer generateConditionHQL(String time, String type, String ticket_id, String state){
		StringBuffer temp = new StringBuffer("");
		if(time != null && !time.equals("")){
			if(time.length() == 7){		//月
				temp.append("and date_format(cr.time,'%Y-%m')=date_format('"+ time +"-01','%Y-%m') ");
			}else if(time.length() == 10){		//日	
				temp.append("and date_format(cr.time,'%Y-%m-%d')=date_format('"+ time +"','%Y-%m-%d') ");
			}
		}
		if(type != null && !type.equals("")){
			if(type.equals("全部")){
				//无
			}else{
				temp.append(" and type='" + type + "' ");
			}
		}
		if(ticket_id != null && !ticket_id.equals("")){
			temp.append(" and ticket_id like '%"+ ticket_id + "%' ");
		}
		if(state != null && !state.equals("")){
			if(state.equals("全部")){
				//无
			}else{
				temp.append(" and state='" + state + "' ");
			}
		}
		temp.append("order by cr.time desc");
		return temp;
	}
	
	public String CRListToJson(int totalNum, List<CustomerRecord> cr_list){
		String time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		obj.put("total", totalNum);
		JSONArray rows = new JSONArray();
		for(CustomerRecord cr : cr_list){
			JSONObject row = new JSONObject();
			row.put("cr_id", cr.getId());
			time = sdf.format(cr.getTime()).toString();
			String ticket_id = cr.getTicket_id();
			String solve_approach = cr.getSolve_approach();
			String op_username = cr.getOperator().getName();
			if(ticket_id == null){
				row.put("ticket_id", "");
			}else{
				row.put("ticket_id", ticket_id);
			}
			if(solve_approach == null){
				row.put("solve_approach", "");
			}else{
				row.put("solve_approach", solve_approach);
			}
			if(op_username == null){
				row.put("operator", "");
			}else{
				row.put("operator", op_username);
			}
			row.put("time", time);
			row.put("title", cr.getTitle());
			row.put("detail", cr.getDetail());
			row.put("type", cr.getType());
			row.put("r_user", cr.getR_user().getName());
			row.put("state", cr.getState());
			rows.add(row);
		}
		obj.put("rows", rows);
		return obj.toJSONString();
	}
}
