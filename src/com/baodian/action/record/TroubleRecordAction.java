package com.baodian.action.record;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.record.Trouble_Record;
import com.baodian.model.record.Working_Record;
import com.baodian.model.user.User;
import com.baodian.service.page.PageManager;
import com.baodian.service.record.IpManager;
import com.baodian.service.record.RecordManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.Export2ExcelManager;
import com.baodian.service.util.InitDataManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("TRecord")
@Scope("prototype")

public class TroubleRecordAction extends ActionSupport{
	
	@Resource(name="pageManager")
	private PageManager pagemanager;
	
	@Resource(name="userManager")
	private UserManager usermanager;
	
	@Resource(name="recordManager")
	private RecordManager recordmanager;
	
	@Resource(name="export2ExcelManager")
	private Export2ExcelManager export2ExcelManager;
	
	@Resource(name="ipManager")
	private IpManager ipmanager;
	
	org.springframework.security.core.userdetails.User login_user =  SecuManagerImpl.currentUser();
	
	private Trouble_Record trouble_record;
	
	private String flag; 
	private int num;//每页显示的记录数 
    private int page;//当前第几页
    
	private String json;
	
	private int tr_id;
	private String ip;
	private String state;
	private String detail;
	private String f_time;
	private String f_username;
	private String solve_approach;
	private String s_time;
	private String s_username;
	
	private String from_date;
	private String to_date;
	
	private File imgFile;
    private String imgFileFileName;
    
    private String downloadFileName;
   	private InputStream excelStream;
    
    private String keyword;
    
    private static String HQL;
    private static String pagenumber_HQL;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String list() {
		String result = null;
		if(flag == null){
			result = "success";
		}else{        //处理DataGrid发送过来请求
			HQL = "select new Trouble_Record(tr.id,tr.IP,tr.detail,tr.state,tr.f_time,tr.f_user.id,tr.f_user.name," +
		  			 "tr.solve_approach,tr.s_time,tr.s_user.id,tr.r_time,tr.r_user.id,tr.r_user.name,tr.filename) " +
		  			 " from Trouble_Record tr";
			pagenumber_HQL = "select count(*) from Trouble_Record tr";
			if(flag.equals("list")){
				if(from_date!=null && to_date!=null && state!=null && ip!=null){  //条件查询
					String startdate =  from_date.concat(" 00:00:00");
					String enddate = to_date.concat(" 23:59:59");
					boolean fd_flag = from_date.equals("");
					boolean td_flag = to_date.equals("");
					boolean ip_flag = ip.equals("");
					boolean s_flag  = state.equals("全部");
					if(!fd_flag && !td_flag && !ip_flag){         // 1、指定   日期、状态、IP
						if(s_flag){
							HQL=HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'"+
									" and IP like'"+ip+"%'");
							pagenumber_HQL = pagenumber_HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'"+
									" and IP like'"+ip+"%'");
						}else{
							HQL=HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'"+
									" and IP like'"+ip+"%' and state='"+state+"'");
							pagenumber_HQL = pagenumber_HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'"+
									" and IP like'"+ip+"%' and state='"+state+"'");
						}
					}else if(!fd_flag && !td_flag && ip_flag){    // 2、指定   日期、状态
						if(s_flag){
							HQL=HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'");
							pagenumber_HQL = pagenumber_HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'");
						}else{
							HQL=HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'"+
									" and state='"+state+"'");
							pagenumber_HQL = pagenumber_HQL.concat(" where f_time between '"+startdate+"' and '"+enddate+"'"+
									" and state='"+state+"'");
						}
					}else if(fd_flag && td_flag && ip_flag){     // 3、指定               状态
						if(s_flag){     //全部
							
						}else{
							HQL=HQL.concat(" where state='"+state+"'");
							pagenumber_HQL = pagenumber_HQL.concat(" where state='"+state+"'");
						}
					}else if(fd_flag && td_flag && !ip_flag){    // 4、指定               状态、IP
						if(s_flag){
							HQL=HQL.concat(" where IP like'"+ip+"%'");
							pagenumber_HQL = pagenumber_HQL.concat(" where IP like'"+ip+"%'");
						}else{
							HQL=HQL.concat(" where IP like'"+ip+"%' and state='"+state+"'");
							pagenumber_HQL = pagenumber_HQL.concat(" where IP like'"+ip+"%' and state='"+state+"'");
						}
					}
				}
				HQL = HQL.concat(" order by tr.r_time desc");
				List tr_list = (List<Trouble_Record>) pagemanager.getCurrentPageRecord(num, page, HQL);				
				int total = pagemanager.getRecordCount(pagenumber_HQL);    //总记录数
				String f_time;
				String s_time;
				String r_time;
				String s_user_name;
				String solve_approach;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				json="{\"total\":"+total+",\"rows\":[";
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
					
					json = json.concat("{\"record_id\":\""+trouble_record.getId()+
							"\",\"IP\":\""+trouble_record.getIP()+
							"\",\"detail\":\""+trouble_record.getDetail()+
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
			}
			result = "json";
		}
		return result;
	}
	
	public String searchByKeyword(){
		if(!keyword.equals("")){
			json = recordmanager.tr_searchByKeyword(num, page, keyword);
		}
		return "json";
	}
	
   public String update() throws Exception{
	    if(tr_id == 0){     //添加记录
	    	if(login_user == null){
	 	    	json = "{\"status\":1,\"mess\":\"亲，请先登录\"}";
	 			return "jhtml";
	 	    }
	    	trouble_record = new Trouble_Record();
	 	    trouble_record.setIP(ip);
	 	    trouble_record.setState(state);
	 	    //detail = detail.replaceAll("\r\n", "<br>");        //json无法解析回车，替换掉
	 	    //detail = JSONValue.escape(detail); 
	 	    trouble_record.setDetail(JSONValue.escape(detail));
	 	    trouble_record.setF_time(sdf.parse(f_time));
	 	    trouble_record.setF_user(usermanager.getUserByName(f_username));
	 	    Date date = new Date();
	 	    trouble_record.setR_time(date);
	 	    User r_user = usermanager.findU_inadrById(login_user.getId()[0]);
	 	    trouble_record.setR_user(r_user);
	 	    if(solve_approach!=null && !solve_approach.equals("")){
	 	    	//solve_approach = solve_approach.replaceAll("\r\n", "<br>"); 
	 	    	//solve_approach = JSONValue.escape(solve_approach);
	 	    	trouble_record.setSolve_approach(JSONValue.escape(solve_approach));
	 	    }
	 	    if(s_time!=null && !s_time.equals("")){
	 	    	trouble_record.setS_time(sdf.parse(s_time));
	 	    }
	 	    if(!s_username.equals("")){
	 	    	trouble_record.setS_user(usermanager.getUserByName(s_username));
	 	    }
	 	   if(imgFile != null){  //上传附件
		    	if(upload_file()){
		    		trouble_record.setFilename(imgFileFileName);
		    		recordmanager.addTroubleRecord(trouble_record);
				    json = "{\"status\":0,\"mess\":\"添加成功！\"}";    
		    	}
		    }else{        //无附件
		    	recordmanager.addTroubleRecord(trouble_record);
			    json = "{\"status\":0,\"mess\":\"添加成功！\"}";   
		    }
	 	   
	 	   //同时添加运行记录
	 	   Working_Record working_record = new Working_Record();
	 	   String wr_detail = "Ip:" + ip + "\r\n\r\n故障原因：\r\n" + detail;
	 	   if(solve_approach!=null && !solve_approach.equals("")){
	 		  wr_detail = wr_detail + "\r\n\r\n解决方法：\r\n" + solve_approach;
	 	   }
	 	   working_record.setDetail(wr_detail);
		   working_record.setType("机器故障");
		   working_record.setTime(date);
		   working_record.setUser(r_user);
		   if(trouble_record.getFilename() != null){
			   working_record.setFile_name(trouble_record.getFilename());
		   }
		   recordmanager.addWorkingRecord(working_record);
	 	   
	    }else{   //修改记录
    		trouble_record = recordmanager.findTR_by_ID(tr_id);
	    	trouble_record.setIP(ip);
	    	trouble_record.setState(state);
	    	//detail = JSONValue.escape(detail);
	    	trouble_record.setDetail(JSONValue.escape(detail));
	    	trouble_record.setF_time(sdf.parse(f_time));
	    	if(!f_username.equals("")){
	    		trouble_record.setF_user(usermanager.getUserByName(f_username));
	    	}
	    	trouble_record.setSolve_approach(JSONValue.escape(solve_approach));
	    	if(s_time!=null){
	    		trouble_record.setS_time(sdf.parse(s_time));
	    	}else{
	    		trouble_record.setS_time(null);
	    	}
	    	if(s_username != null){
	    		if(s_username.equals("")){
	    			trouble_record.setS_user(null);
	    		}else{
	    			trouble_record.setS_user(usermanager.getUserByName(s_username));
	    		}
	    	}
	    	if(imgFile != null){  //上传附件
	    		if(trouble_record.getFilename() != null){//1、原来有附件，先删除旧附件
	    			String basepath = "/documents/attachments/"+trouble_record.getFilename();
				    String realPath = ServletActionContext.getServletContext().getRealPath(basepath);
				    File file = new File(realPath);
				    if(file.exists()){
					   file.delete();
				    }
	    		}
		    	if(upload_file()){
		    		trouble_record.setFilename(imgFileFileName);
		    		recordmanager.updateTroubleRecord(trouble_record);
		 		    json = "{\"status\":0,\"mess\":\"修改成功！\"}";  
		    	}
		    }else{        //2、更改无上传新附件，保持原来状态
		    	//trouble_record.setFilename(null);
		    	recordmanager.updateTroubleRecord(trouble_record);
				json = "{\"status\":0,\"mess\":\"修改成功！\"}"; 
		    }
	    	
	    	// 同时修改运行记录
	    	Working_Record wr = recordmanager.findWR_by_UseridAndTime(trouble_record.getR_user().getId(), 
	    			trouble_record.getR_time());
	    	if(wr != null){
	    		String wr_detail = "Ip:" + ip + "\r\n\r\n故障原因：\r\n" + detail;
	    		if(solve_approach!=null && !solve_approach.equals("")){
	  	 		  wr_detail = wr_detail + "\r\n\r\n解决方法：\r\n" + solve_approach;
	  	 	    }
	    		wr.setDetail(wr_detail);                 //修改对应运行记录
	    		if(trouble_record.getFilename() != null){  //故障记录有附件，运行记录添加附件文件名
	 			   wr.setFile_name(trouble_record.getFilename());
	 		    }else{
	 		    	//wr.setFile_name(null);
	 		    }
	    		recordmanager.updateWorkingRecord(wr);
	    	}
	    }
	    	
		return "jhtml";
	}	
   
   public String exportAll2Excel() throws Exception{
	   	HQL = "select new Trouble_Record(tr.id,tr.IP,tr.detail,tr.state,tr.f_time,tr.f_user.id,tr.f_user.name," +
	  			 "tr.solve_approach,tr.s_time,tr.s_user.id,tr.r_time,tr.r_user.id,tr.r_user.name,tr.filename) " +
	  			 " from Trouble_Record tr order by tr.r_time desc";
		pagenumber_HQL = "select count(*) from Trouble_Record tr";
		
		int total = pagemanager.getRecordCount(pagenumber_HQL);
	   	List tr_list = pagemanager.getCurrentPageRecord(total, 1, HQL);
	   	String[] columnNames = new String[]{"发现时间","机器IP","故障原因","解决方法"};
	   	String[] columnMethods = new String[]{"getR_time","getIP","getDetail","getSolve_approach"};
	   	excelStream = export2ExcelManager.export2Excel(columnNames,columnMethods,tr_list);
		downloadFileName=new String( "故障记录表".getBytes("GBK"), "ISO-8859-1");
		return "execl";
   }
   
   public String exportCurrent2Excel() throws Exception{
        //System.out.println("=========from_date="+from_date+",to_date="+to_date+",state="+state+",ip="+ip);
	   	//System.out.println("=========HQL="+HQL);
	   	//System.out.println("=========pagenumber_HQL="+pagenumber_HQL);
	   	int total = pagemanager.getRecordCount(pagenumber_HQL);
	   	List wr_list = pagemanager.getCurrentPageRecord(total, 1, HQL);
	   	String[] columnNames = new String[]{"发现时间","机器IP","故障原因","解决方法"};
	   	String[] columnMethods = new String[]{"getR_time","getIP","getDetail","getSolve_approach"};
	   	excelStream = export2ExcelManager.export2Excel(columnNames,columnMethods,wr_list);
		String filename = "";
	   	if(from_date != null && !from_date.equals("")){
	   		from_date = from_date.replace("-", "");
	   		filename = filename.concat(from_date + "至");
		}
	   	if(to_date != null && !to_date.equals("")){
	   		to_date = to_date.replace("-", "");
	   		filename = filename.concat(to_date + "-");
		}
	   	if(state != null && !state.equals("")){
	   		if(!state.equals("全部"))
	   			filename = filename.concat(state + "-");
		}
	   	if(ip != null && !ip.equals("")){
	   		filename = filename.concat(ip + "-");
		}
	   	filename = filename.concat("故障记录表");
	   	downloadFileName=new String( filename.getBytes("GBK"), "ISO-8859-1");
		return "execl";
   }
   
    public String del_no() {
    	//先根据用户id和时间删掉对应的运行记录
    	trouble_record = recordmanager.findTR_by_ID(tr_id);
    	Working_Record wr = recordmanager.findWR_by_UseridAndTime(trouble_record.getR_user().getId(), 
    			trouble_record.getR_time());
    	if(wr != null){
    		recordmanager.deleteWorkingRecord(wr.getId());         //删对应运行记录
    	}
    	recordmanager.deleteTroubleRecord(tr_id);
    	json = "{\"status\":0,\"mess\":\"delete successfully\"}";
		return "jhtml";
    }
    
    public boolean upload_file(){               //上传文件
		String basepath = "/documents/attachments/";
		// 获得项目路径  
		String savePath = ServletActionContext.getServletContext().getRealPath(basepath);
		//创建文件夹
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		//检查目录写权限
		if(!file.canWrite()){
			json = "{\"status\":1,\"mess\":\"上传目录没有写权限！\"}";
			return false;
		}
		//检查扩展名
		String fileExt = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
		
		if(! InitDataManager.extSet.contains(fileExt)) {
			json = "{\"status\":1,\"mess\":\"文件格式不正确！\"}";
			return false;
		}
		try {
			file = new File(file, imgFileFileName);
			if(file.exists()) {
				json = "{\"status\":1,\"mess\":\"文件已存在！\"}";
				return false;
			} else {
				FileUtils.copyFile(imgFile, file);
			}
			json = "{\"status\":0,\"mess\":\"" +
					ServletActionContext.getServletContext().getContextPath() +
					basepath + "/" + imgFileFileName+"\"}";
			return true;
		} catch (IOException e) {
			json = "{\"status\":1,\"mess\":\"上传失败！\"}";
			return false;
		}
	}
    
    public String IfUserHaveAuthority_no(){      //判断用户是否有权限修改或删除记录
    	int uid = SecuManagerImpl.canAccess("TRecord_del_no.action");
		if(uid == 0) {
			json = "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		} else if (uid < 0) {    //管理员在spring security中已经给他加了权限
			json = "{\"status\":0}";
		} else if(uid > 0){
			if(tr_id != 0){
				int r_userid = recordmanager.findTR_by_ID(tr_id).getR_user().getId();
				if(uid == r_userid) { //判断是否是自己的记录，若是自己的记录才能删除
					json = "{\"status\":0}";
				}else{
					json = "{\"status\":1,\"mess\":\"无权限！\"}";
				}
			}else{       //添加运行记录的时候判断是否登录
				json = "{\"status\":0,\"mess\":\"已登录\"}";
			}
		}
		return "json";
    }
    
    public String delIp(){  //删除IP段
    	json = ipmanager.delIP(ip);
		return "jhtml";
    }
    
    public String addIp(){   //添加IP段
    	json = ipmanager.addIP(ip);
    	return "jhtml";
    }
    
    public String getIpType(){
    	json = ipmanager.getAllIp();
    	return "json";
    }
    
	public void setJson(String json) {
		this.json = json;
	}

	public String getJson() {
		return json;
	}
	
	public int getTr_id() {
		return tr_id;
	}

	public void setTr_id(int tr_id) {
		this.tr_id = tr_id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return flag;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getF_time() {
		return f_time;
	}

	public void setF_time(String fTime) {
		f_time = fTime;
	}

	public String getSolve_approach() {
		return solve_approach;
	}

	public void setSolve_approach(String solveApproach) {
		solve_approach = solveApproach;
	}

	public String getS_time() {
		return s_time;
	}

	public void setS_time(String sTime) {
		s_time = sTime;
	}

	public String getF_username() {
		return f_username;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public void setF_username(String fUsername) {
		f_username = fUsername;
	}

	public String getS_username() {
		return s_username;
	}

	public void setS_username(String sUsername) {
		s_username = sUsername;
	}
	
	public org.springframework.security.core.userdetails.User getLogin_user() {
		return login_user;
	}

	public void setLogin_user(
			org.springframework.security.core.userdetails.User login_user) {
		this.login_user = login_user;
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
}
