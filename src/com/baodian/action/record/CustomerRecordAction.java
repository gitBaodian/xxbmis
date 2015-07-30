package com.baodian.action.record;

import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.service.record.CustomerRecordManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("CRecord")
@Scope("prototype")

public class CustomerRecordAction extends ActionSupport {
	
	private int num;//每页显示的记录数 
    private int page;//当前第几页
    
    private int cr_id;
    private String ticket_id;
    private String type;
    private String title;
    private String detail;
    private String solve_approach;
    private String op_username;
    private String time; 
    private String state;
    
    private String keyword;
    
    private String json;
    
    private String downloadFileName;
	private InputStream excelStream;
	
	@Resource(name="customerRecordManager")
	private CustomerRecordManager crManager;

	public String list(){
		return "success";
	}
	
	public String listTable(){		//展示当天记录
		json = crManager.listTable(num, page);
		return "json";
	}
	
	public String addCustomerRecord() throws Exception{
		json = crManager.addCustomerRecord(ticket_id, type, title, detail, solve_approach, op_username, time);
		return "jhtml";
	}
	
	public String updateCustomerRecord() throws Exception{
		json = crManager.updateCustomerRecord(cr_id, ticket_id, type, title, detail, solve_approach, op_username, time);
		return "jhtml";
	}
	
	public String delCustomerRecord(){
		json = crManager.delCustomerRecord(cr_id);
		return "json";
	}
	
	public String searchByKeyWord(){
		json = crManager.searchByKeyword(num, page, keyword);
		return "json";
	}
	
	public String searchByCondition(){
		json = crManager.secrchByCondition(num,page,time,type,ticket_id,state);
		return "json";		
	}
	
	public String exportAll2Excel() throws Exception{
		excelStream = crManager.exportAll2Excel();
		downloadFileName=new String( "客服记录表".getBytes("GBK"), "ISO-8859-1");
		return "execl";
    }
	
	public String exportCurrent2Excel() throws Exception{
		StringBuffer title = new StringBuffer("");
		if(time != null && !time.equals("")){
			title = title.append("——" + time);
		}
		if(type != null && !type.equals("")){
			title = title.append("——" + type);
		}
		String filename = new StringBuffer("客服记录表").append(title).toString();
		excelStream = crManager.exportCurrent2Excel(time, type, ticket_id,state);
		downloadFileName=new String( filename.getBytes("GBK"), "ISO-8859-1");
		return "execl";
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
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTicket_id() {
		return ticket_id;
	}

	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSolve_approach() {
		return solve_approach;
	}

	public void setSolve_approach(String solve_approach) {
		this.solve_approach = solve_approach;
	}

	public String getR_username() {
		return op_username;
	}

	public void setOp_username(String op_username) {
		this.op_username = op_username;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public int getCr_id() {
		return cr_id;
	}

	public void setCr_id(int cr_id) {
		this.cr_id = cr_id;
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
