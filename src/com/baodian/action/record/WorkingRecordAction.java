package com.baodian.action.record;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.notepad.Notepad;
import com.baodian.model.record.CustomerRecord;
import com.baodian.model.record.Working_Record;
import com.baodian.model.user.User;
import com.baodian.service.notepad.NotepadManager;
import com.baodian.service.page.PageManager;
import com.baodian.service.record.RecordManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.Export2ExcelManager;
import com.baodian.service.util.InitDataManager;
import com.baodian.util.JSONValue;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("WRecord")
@Scope("prototype")

public class WorkingRecordAction extends ActionSupport{
	
	@Resource(name="pageManager")
	private PageManager pagemanager;
	
	@Resource(name="recordManager")
	private RecordManager recordmanager;
	
	@Resource(name="notepadManager")
	private NotepadManager nm;
	
	@Resource(name="export2ExcelManager")
	private Export2ExcelManager export2ExcelManager;
	
	org.springframework.security.core.userdetails.User login_user =  SecuManagerImpl.currentUser();
	
	private Working_Record working_record;
	
	private String flag;    //区分是否是JQuery EasyUI DataGrid发过来的请求json数据的标记
	
	private int num;//每页显示的记录数 
    private int page;//当前第几页
    
    private int wr_id;
    private String detail;
    private String type;
    private String r_time;
    
    private String from_date;
    private String to_date;
    private String dept_name;
    private String username;
    
    private File imgFile;
    private String imgFileFileName;
    
    private String downloadFileName;
	private InputStream excelStream;
    
    private String keyword;
    
	private String json;
	
	private String addnp;//是否添加到每日记事中
	
	private static String HQL = null;    //查询记录
	private static String pagenumber_HQL = null;   //查询记录数
	
	public String list() throws Exception{
		return "success";
	}
	
	public String listTable(){
		json = recordmanager.wr_listTable(num, page);
		return "json";
	}
	
	public String searchByCondition(){
		json = recordmanager.wr_secrchByCondition(num, page, from_date, to_date, type, dept_name, username);
		return "json";
	}
	
	public String searchByKeyword(){
		if(!keyword.equals("")){
			json = recordmanager.wr_searchByKeyword(num, page, keyword);
		}
		return "json";
	}
	
	public String list_for_index(){
		String HQL = "select new Working_Record(wr.id,wr.detail,wr.time,wr.type,wr.user.id,wr.user.name,wr.user.account,wr.user.dpm.name,wr.file_name)" +
			      " from Working_Record wr order by wr.time desc";
		List<Working_Record> wr_list = (List<Working_Record>) pagemanager.getCurrentPageRecord(6, 1, HQL);
		json = WRToJson(wr_list,6);       //转换成json
		return "json";
	}
	
   public String update() throws Exception {
	   if(wr_id == 0){    //添加记录
		    working_record = new Working_Record();
		    if(addnp != null) {
		    	nm.save(new Notepad(detail));
		    }
		    //detail = detail.replaceAll("\r\n", "<br>");        //json无法解析回车，替换掉
		    //detail = JSONValue.escape(detail);
		    detail = JSONValue.escapeLtAndGt(detail);		//将'<'和'>'转义，才不会再显示的时候被解释为html标签
		    working_record.setDetail(detail);
		    working_record.setType(type);
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    working_record.setTime(sdf.parse(r_time));
		    working_record.setUser(new User(SecuManagerImpl.currentId()));
		    if(imgFile != null){  //上传附件
		    	if(upload_file()){
		    		working_record.setFile_name(imgFileFileName);
		    		recordmanager.addWorkingRecord(working_record);
				    json = "{\"status\":0,\"mess\":\"添加成功！\"}";    
		    	}
		    }else{        //无附件
		    	recordmanager.addWorkingRecord(working_record);
			    json = "{\"status\":0,\"mess\":\"添加成功！\"}";   
		    }
	   }else{                 //修改记录
		   Working_Record wr = recordmanager.findWR_by_ID(wr_id);
		   wr.setType(type);
		   //detail = JSONValue.escape(detail);
		   detail = JSONValue.escapeLtAndGt(detail);
		   wr.setDetail(detail);
		   
		   if(imgFile != null){  //上传附件
			   if(wr.getFile_name() != null){   //原来有附件，先删除旧附件
				   String basepath = "/documents/attachments/"+wr.getFile_name();
				   String realPath = ServletActionContext.getServletContext().getRealPath(basepath);
				   File file = new File(realPath);
				   if(file.exists()){
					   file.delete();
				   }
		    	}
		    	if(upload_file()){  //上传附件
		    		wr.setFile_name(imgFileFileName);
		    		recordmanager.updateWorkingRecord(wr);
		 		    json = "{\"status\":0,\"mess\":\"修改成功！\"}";  
		    	}
		    }else{        //更改无上传新附件，保持原来状态
		    	recordmanager.updateWorkingRecord(wr);
				json = "{\"status\":0,\"mess\":\"修改成功！\"}"; 
		    }
	   }
		return "jhtml";
	}	
   
    public String del_no(){
    	json = "{\"status\":0,\"mess\":\"delete successfully\"}";
    	recordmanager.deleteWorkingRecord(wr_id);
		return "jhtml";
    }
    
    public String exportAll2Excel() throws Exception{
    	String HQL = "select new Working_Record(wr.id,wr.detail,wr.time,wr.type,wr.user.id,wr.user.name,wr.user.account,wr.user.dpm.name,wr.file_name)" +
			      " from Working_Record wr order by wr.time desc";
    	String pagenumber_HQL = "select count(*) from Working_Record wr";
		int total = pagemanager.getRecordCount(pagenumber_HQL);
    	List wr_list = pagemanager.getCurrentPageRecord(total, 1, HQL);
    	String[] columnNames = new String[]{"记录时间","记录内容","操作类型","记录人","部门"};
    	String[] columnMethods = new String[]{"getTime","getDetail","getType","acquireUserName","acquireDeptName"};
    	excelStream = export2ExcelManager.export2Excel(columnNames,columnMethods,wr_list);
		downloadFileName=new String( "运行记录表".getBytes("GBK"), "ISO-8859-1");
		return "execl";
    }
    
    public String exportCurrent2Excel() throws Exception{
    	int total = pagemanager.getRecordCount(pagenumber_HQL);
    	List wr_list = pagemanager.getCurrentPageRecord(total, 1, HQL);
    	String[] columnNames = new String[]{"记录时间","记录内容","操作类型","记录人","部门"};
    	String[] columnMethods = new String[]{"getTime","getDetail","getType","acquireUserName","acquireDeptName"};
    	excelStream = export2ExcelManager.export2Excel(columnNames,columnMethods,wr_list);
		String filename = "";
    	if(from_date != null && !from_date.equals("")){
    		from_date = from_date.replace("-", "");
    		filename = filename.concat(from_date + "-");
    		//System.out.println("=======from_date="+from_date);
		}
    	if(dept_name != null && !dept_name.equals("")){
    		filename = filename.concat(dept_name + "-");
		}
    	if(type != null && !type.equals("")){
    		if(!type.equals("全部")){
    			filename = filename.concat(type + "-");
    		}
		}
    	filename = filename.concat("运行记录表");
    	downloadFileName=new String( filename.getBytes("GBK"), "ISO-8859-1");
		return "execl";
    }
    
    public String IfUserHaveAuthority_no(){      //判断用户是否有权限修改或删除记录
    	int uid = SecuManagerImpl.canAccess("WRecord_del_no.action");
		if(uid == 0) {
			json = "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		} else if (uid < 0) {    //管理员在spring security中已经给他加了权限
			json = "{\"status\":0}";
		} else if(uid > 0){
			if(wr_id != 0){
				int r_userid = recordmanager.findWR_by_ID(wr_id).getUser().getId();
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
    
    public String WRToJson(List<Working_Record> WRList,int total){       //Working_Record list 转换成json
		String time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*		json="{\"total\":"+total+",\"rows\":[";
		Iterator it = WRList.iterator();
		while(it.hasNext()){
			working_record = (Working_Record) it.next();
			time = sdf.format(working_record.getTime()).toString();
			json = json.concat("{\"record_id\":\""+working_record.getId()+
								"\",\"detail\":\""+working_record.getDetail()+
								"\",\"type\":\""+working_record.getType()+
								"\",\"time\":\""+time+
								"\",\"username\":\""+working_record.getUser().getName()+
								"\",\"dept\":\""+working_record.getUser().getDpm().getName()
								);
			if(working_record.getFile_name() != null){       //附件
				String filename = working_record.getFile_name();
				String filePath = "documents/attachments/"+filename;
				json = json.concat("\",\"attachment\":\"<a target='_balnk' title='"+filename+"' href='"+filePath+"'>附件</a>");
			}
			json = json.concat("\"},");
		}
		if(!WRList.isEmpty()){   //有记录
			json = json.substring(0, json.length()-1);   //去掉逗号
		}
		json = json.concat("]}");
*/
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
		json = obj.toJSONString();
		return json;
	}
    
	public void setJson(String json) {
		this.json = json;
	}

	public String getJson() {
		return json;
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

	public void setWr_id(int wr_id) {
		this.wr_id = wr_id;
	}

	public int getWr_id() {
		return wr_id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getR_time() {
		return r_time;
	}

	public void setR_time(String r_time) {
		this.r_time = r_time;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getFrom_date() {
		return from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getAddnp() {
		return addnp;
	}

	public void setAddnp(String addnp) {
		this.addnp = addnp;
	}
}
