package com.baodian.action.device;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.device.Equipment;
import com.baodian.service.device.DeviceManager;
import com.baodian.util.page.EquPage;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("device")
@Scope("prototype")
public class DeviceAction extends ActionSupport{
	
	private DeviceManager deviceManager;
	private String json;
	private EquPage page;
	
	private String ip;
	private String env;
	
	private String hostsname;
	private String role;
	private String system;
	private String type;
	private String cpu;
	private String hard;
	private String memory;
	private String motherboard;
	private String other;

	
	private String downloadFileName;
	private InputStream excelStream;
	private File upload;
	
	public String manager() {
		return SUCCESS;
	}
	/*
	 * 获取列表
	 */
	public String equ_list(){
		if(page == null)
			page = new EquPage();
		if(page.getIp()!=null&&page.getIp().length()!=0){
			page.setEnv(page.getIp());
		}
		json = deviceManager.get_device_list(page);
		return "json";
	}

	
	/**
	 * 获取机器全部信息
	 * @return
	 */
	public String ip_equ(){
		String c_ip = "";
		if(!isip(ip)){
			c_ip = ip.split("\\.\\.")[0];
		}else{
			c_ip = ip;
		}
		json = deviceManager.get_ip_equ(c_ip);
		return "json";
	}
	
	//批机器信息
	public String batch_equ(){
		String c_ip = "";
		if(!isip(ip)){
			c_ip = ip.split("\\.\\.")[0];
		}else{
			c_ip = ip;
		}
		json = deviceManager.get_batch_equ(c_ip);
		return "json";
	}
	
	/**
	 * 删除机器信息
	 * @return
	 */
	public String ip_equ_del(){
		List<String> ip_list = doip(ip);
		if(ip_list==null){
			json = "{\"status\":1,\"mess\":\"IP格式不正确！\"}";
			return "json";
		}
		for(String ip:ip_list){
			deviceManager.ip_equ_del(ip);
		}
		json = "{\"status\":0,\"mess\":\"已删除！\"}";
		return "json";
	}
	
	
	/**
	 * 批量修改机器信息
	 * @return
	 */
	public String equ_batch_up(){
		if(ip.length()==0||ip==null){
			json = "{\"status\":1,\"mess\":\"IP不能为空！\"}";
			return "json";
		}
		List<String> ip_list = doip(ip);
		if(ip_list==null){
			json = "{\"status\":1,\"mess\":\"IP格式不正确！\"}";
			return "json";
		}
		Equipment equ = new Equipment();
		
		if(role.length()!=0||role!=null) equ.setRole(role);
		if(system.length()!=0||system!=null) equ.setSystem(system);
		if(type.length()!=0||type!=null) equ.setType(type);
		if(cpu.length()!=0||cpu!=null) equ.setCpu(cpu);
		if(hard.length()!=0||hard!=null) equ.setHard(hard);
		if(memory.length()!=0||memory!=null) equ.setMemory(memory);
		if(motherboard.length()!=0||motherboard!=null) equ.setMotherboard(motherboard);
		if(other.length()!=0||other!=null) equ.setOther(other);
		json = deviceManager.equ_batch_up(ip_list , equ);
		return "json";
	}
	
	
	/**
	 * 增加机器
	 * @return
	 */
	public String equ_batch_add(){
		if(ip.length()==0||ip==null){
			json = "{\"status\":1,\"mess\":\"IP不能为空！\"}";
			return "json";
		}
		if(env.equals("0")){
			json = "{\"status\":1,\"mess\":\"请选择环境！\"}";
			return "json";
		}
		List<String> ip_list = doip(ip);
		if(ip_list==null){
			json = "{\"status\":1,\"mess\":\"IP格式不正确！\"}";
			return "json";
		}
		
		Equipment equ = new Equipment();
		if(env.length()!=0||env!=null) equ.setEnv(env);
		if(role.length()!=0||role!=null) equ.setRole(role);
		if(system.length()!=0||system!=null) equ.setSystem(system);
		if(type.length()!=0||type!=null) equ.setType(type);
		if(cpu.length()!=0||cpu!=null) equ.setCpu(cpu);
		if(hard.length()!=0||hard!=null) equ.setHard(hard);
		if(memory.length()!=0||memory!=null) equ.setMemory(memory);
		if(motherboard.length()!=0||motherboard!=null) equ.setMotherboard(motherboard);
		if(other.length()!=0||other!=null) equ.setOther(other);
		json = deviceManager.equ_batch_add(ip_list , equ);
		return "json";
	}
	
	
	/**
	 * 导出xls
	 * @return
	 */
	public String create_env_list_xls(){
		excelStream = deviceManager.get_env_list_xls(env);
		downloadFileName = env+"env";
		return "execl";
	}
	
	
	
	/**
	 * 判断ip格式
	 * @param IP
	 * @return
	 */
	 public static boolean isip(String IP){//判断是否是一个IP 
	        boolean b = false; 
	        while(IP.startsWith(" ")){ 
	            IP= IP.substring(1,IP.length()).trim(); 
	        } 
	        while(IP.endsWith(" ")){ 
	            IP= IP.substring(0,IP.length()-1).trim(); 
	        }
	        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){ 
	            String s[] = IP.split("\\."); 
	            if(Integer.parseInt(s[0])<255) 
	                if(Integer.parseInt(s[1])<255) 
	                    if(Integer.parseInt(s[2])<255) 
	                        if(Integer.parseInt(s[3])<255) 
	                            b = true; 
	        } 
	        return b; 
	 }
	 
	 //处理ip
	 public static List<String> doip(String ip){
		 List<String> ip_list=new ArrayList<String>();
			String[] ip_com = ip.split(",");
			for(int i=0;i<ip_com.length;i++){
				String[] ips = ip_com[i].split("\\.\\.");
				if(ips.length==2){
					boolean is = isip(ips[0]);
					if(is==false){
						return null;
					}
					String[] com = ips[0].split("\\.");
					int n = Integer.valueOf(ips[1]) - Integer.valueOf(com[3]);
					if(n<0){
						return null;
					}
					for(int b=0;b<=n;b++){
						ip_list.add(com[0]+"."+com[1]+"."+com[2]+"."+(Integer.valueOf(com[3])+b));
					}
				}else{
					boolean is = isip(ips[0]);
					if(is==false){
						return null;
					}
					ip_list.add(ips[0]);
				}
			}
			return ip_list;
	 }
	
	
	@Resource(name = "deviceManager")
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public EquPage getPage() {
		return page;
	}
	public void setPage(EquPage page) {
		this.page = page;
	}
	public String getHostsname() {
		return hostsname;
	}
	public void setHostsname(String hostsname) {
		this.hostsname = hostsname;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getHard() {
		return hard;
	}
	public void setHard(String hard) {
		this.hard = hard;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getMotherboard() {
		return motherboard;
	}
	public void setMotherboard(String motherboard) {
		this.motherboard = motherboard;
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
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}
