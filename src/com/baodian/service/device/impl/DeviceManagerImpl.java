package com.baodian.service.device.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baodian.dao.device.EquipmentDao;
import com.baodian.model.device.Equipment;
import com.baodian.service.device.DeviceManager;
import com.baodian.util.page.EquPage;
import com.baodian.vo.EquipmentAll;

@Service("deviceManager")
public class DeviceManagerImpl implements DeviceManager{
	
	private EquipmentDao equipmentDao;
	
	/**
	 * 获取机器列表
	 */
	@SuppressWarnings("unchecked")
	public String get_device_list(EquPage page){
		String json = "";
		String sql = "from Equipment as e where e.env = '"+page.getEnv()+"' group by e.role,e.system,e.cpu,e.hard,e.motherboard,e.memory,e.type,e.other order by inet_aton(e.ip) ASC";
		List<Equipment> group_lists = (List<Equipment>) equipmentDao.findhql(sql);
		json = "{\"total\":" + "0" + "," +
				"\"rows\":[";
		if(group_lists.size() == 0||group_lists == null){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		for(Equipment equ : group_lists) {
			String sql2 = "from Equipment as e where e.env = '"+page.getEnv()+"' and e.role = '"+equ.getRole()+"' and e.system = '"+equ.getSystem()+"'" +
					"and e.cpu = '"+equ.getCpu()+"' and e.hard = '"+equ.getHard()+"' and e.motherboard = '"+equ.getMotherboard()+"' " +
					"and e.memory = '"+equ.getMemory()+"' and e.type = '"+equ.getType()+"' and e.other = '"+equ.getOther()+"' order by inet_aton(e.ip) ASC";
			List<Equipment> equ_list = (List<Equipment>) equipmentDao.findhql(sql2);
			
			String role = "",system = "",type = "";
			
			if(equ_list.get(0).getRole()!=null) role = equ_list.get(0).getRole();
			if(equ_list.get(0).getSystem()!=null) system = equ_list.get(0).getSystem();
			if(equ_list.get(0).getType()!=null) type = equ_list.get(0).getType();
			
			Iterator<Equipment> order_iterator=equ_list.iterator();
			
			Equipment f_equ = order_iterator.next();
			String[] f_ips = f_equ.getIp().split("\\.");
			int b=0;
			while(order_iterator.hasNext()){
				Equipment e = order_iterator.next();
				String[] ips = e.getIp().split("\\.");
				if(f_ips[0].equals(ips[0])&&f_ips[1].equals(ips[1])&&f_ips[2].equals(ips[2])
						&&(String.valueOf((Integer.valueOf(f_ips[3])+1))).equals(ips[3])){
					b++;
				}else{
					String ip = "";
					if(b==0){
						ip = f_ips[0]+"."+f_ips[1]+"."+f_ips[2]+"."+f_ips[3];
					}else{
						String[] c_ips = f_equ.getIp().split("\\.");
						int c = Integer.valueOf(c_ips[3])-b;
						ip = c_ips[0] +"."+ c_ips[1] +"."+ c_ips[2] +"."+ String.valueOf(c) +".."+ c_ips[3];
					}
					json = json.concat("{\"ip\":\"" + ip + "\"," +
							"\"role\":\"" + role + "\"," +
							"\"system\":\"" + system + "\"," +
							"\"type\":\"" + type + "\"," +
							"\"dsh\":\"待增加\"},");
					b = 0;
					if(!order_iterator.hasNext()){
						json = json.concat("{\"ip\":\"" + e.getIp() + "\"," +
								"\"role\":\"" + role + "\"," +
								"\"system\":\"" + system + "\"," +
								"\"type\":\"" + type + "\"," +
								"\"dsh\":\"待增加\"},");
					}
				}
				f_equ = e;
				f_ips = ips;
			}
			if(b!=0){
				String ip = "";
				if(b==0){
					ip = f_ips[0]+"."+f_ips[1]+"."+f_ips[2]+"."+f_ips[3];
				}else{
					String[] c_ips = f_equ.getIp().split("\\.");
					int c = Integer.valueOf(c_ips[3])-b;
					ip = c_ips[0] +"."+ c_ips[1] +"."+ c_ips[2] +"."+ String.valueOf(c) +".."+ c_ips[3];
				}
				json = json.concat("{\"ip\":\"" + ip + "\"," +
						"\"role\":\"" + role + "\"," +
						"\"system\":\"" + system + "\"," +
						"\"type\":\"" + type + "\"," +
						"\"dsh\":\"待增加\"},");
				b = 0;
			}else{
				json = json.concat("{\"ip\":\"" + f_equ.getIp() + "\"," +
						"\"role\":\"" + role + "\"," +
						"\"system\":\"" + system + "\"," +
						"\"type\":\"" + type + "\"," +
						"\"dsh\":\"待增加\"},");
			}
		}
		if(group_lists.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	

	
	/**
	 * 获取机器全部信息
	 */
	public String get_ip_equ(String ip){
		String json = "";
		Equipment equ  = (Equipment) equipmentDao.findById(ip);
		if(equ.getCpu()==null) equ.setCpu("");
		if(equ.getHard()==null) equ.setHard("");
		if(equ.getMemory()==null) equ.setMemory("");
		if(equ.getMotherboard()==null) equ.setMotherboard("");
		if(equ.getOther()==null) equ.setOther("");
		
		json = "{\"total\":0 ," +
				"\"rows\":[";
		json = json.concat("{\"property\":\"cpu\"," +
				"\"content\":\"" + equ.getCpu() + "\"" +
				"},");
		json = json.concat("{\"property\":\"硬盘\"," +
				"\"content\":\"" + equ.getHard() + "\"" +
				"},");
		json = json.concat("{\"property\":\"内存\"," +
				"\"content\":\"" + equ.getMemory() + "\"" +
				"},");
		json = json.concat("{\"property\":\"主板\"," +
				"\"content\":\"" + equ.getMotherboard() + "\"" +
				"},");
		json = json.concat("{\"property\":\"其它\"," +
				"\"content\":\"" + equ.getOther() + "\"" +
				"},");
		json = json.substring(0, json.length()-1).concat("]}");
		return json;
	}
	
	/**
	 * 获取批机器信息
	 */
	public String get_batch_equ(String ip){
		String json = "";
		Equipment equ  = (Equipment) equipmentDao.findById(ip);
		if(equ.getRole()==null) equ.setRole("");
		if(equ.getSystem()==null) equ.setSystem("");
		if(equ.getType()==null) equ.setType("");
		
		if(equ.getCpu()==null) equ.setCpu("");
		if(equ.getHard()==null) equ.setHard("");
		if(equ.getMemory()==null) equ.setMemory("");
		if(equ.getMotherboard()==null) equ.setMotherboard("");
		if(equ.getOther()==null) equ.setOther("");
		
		json = "{\"role\":\""+equ.getRole()+"\"," +
				"\"system\":\""+equ.getSystem()+"\",\"type\":\""+equ.getType()+"\"," +
				"\"other\":\""+equ.getOther()+"\", \"cpu\":\""+equ.getCpu()+"\",\"hard\":\""+equ.getHard()+"\",\"memory\":\""+equ.getMemory()+"\"," +
				"\"motherboard\":\""+equ.getMotherboard()+"\",";
		json = json.substring(0, json.length()-1).concat("}");
		return json;
	}
	
	/**
	 * 删除机器信息
	 */
	public boolean ip_equ_del(String ip){
		Equipment equ = new Equipment();
		equ.setIp(ip);
		equipmentDao.delete(equ);
		return true;
	}
	
	
	/**
	 * 批量修改
	 */
	public String equ_batch_up(List<String> ip_list ,Equipment equ){
		String json = "";
		String json_com = "";
		for(String ip:ip_list){
			Equipment equ_s = (Equipment) equipmentDao.findById(ip);
			if(equ_s != null){
				equ_s.setRole(equ.getRole());
				equ_s.setSystem(equ.getSystem());
				equ_s.setType(equ.getType());
				equ_s.setCpu(equ.getCpu());
				equ_s.setHard(equ.getHard());
				equ_s.setMemory(equ.getMemory());
				equ_s.setMotherboard(equ.getMotherboard());
				equ_s.setOther(equ.getOther());
				equipmentDao.attachDirty(equ_s);
			}else{
				json_com = json_com + ip +",";
			}
		}
		if(json_com.length()==0){
			json = "{\"status\":0,\"mess\":\"批量修改成功！\"}";
		}else{
			json_com = json_com.substring(0, json_com.length()-1);
			json = "{\"status\":0,\"mess\":\"批量修改成功！但以下IP不存在"+json_com+"\"}";
		}
		return json;
	}
	
	
	/**
	 * 批量增加
	 */
	public String equ_batch_add(List<String> ip_list ,Equipment equ){
		String json = "";
		String json_com = "";
		for(String ip:ip_list){
			Equipment equ_s = (Equipment) equipmentDao.findById(ip);
			if(equ_s == null){
				Equipment e = new Equipment();
				e.setIp(ip);
				e.setEnv(equ.getEnv());
				e.setRole(equ.getRole());
				e.setSystem(equ.getSystem());
				e.setType(equ.getType());
				e.setCpu(equ.getCpu());
				e.setHard(equ.getHard());
				e.setMemory(equ.getMemory());
				e.setMotherboard(equ.getMotherboard());
				e.setOther(equ.getOther());
				equipmentDao.save(e);
			}else{
				json_com = json_com + ip +",";
			}
		}
		if(json_com.length()==0){
			json = "{\"status\":0,\"mess\":\"批量添加成功！\"}";
		}else{
			json_com = json_com.substring(0, json_com.length()-1);
			json = "{\"status\":0,\"mess\":\"批添加改成功！但以下IP已存在"+json_com+"\"}";
		}
		return json;
	}
	
	/**
	 * 导出xml文件
	 */
	@SuppressWarnings("unchecked")
	public InputStream get_env_list_xls(String env){
		List<EquipmentAll> equ_all_list = new ArrayList<EquipmentAll>();
		List<Equipment> equ_list = (List<Equipment>) equipmentDao.findhql("from Equipment as e where e.env = '"+env+"'");
		for(Equipment equ : equ_list){
			EquipmentAll equ_all = new EquipmentAll();
			equ_all.setIp(equ.getIp());
			equ_all.setHostsname(equ.getHostsname());
			equ_all.setRole(equ.getRole());
			equ_all.setCpu(equ.getCpu());
			equ_all.setEnv(equ.getEnv());
			equ_all.setHard(equ.getHard());
			equ_all.setMemory(equ.getMemory());
			equ_all.setMotherboard(equ.getMotherboard());
			equ_all.setSystem(equ.getSystem());
			equ_all.setType(equ.getType());
			equ_all.setOther(equ.getOther());
			equ_all_list.add(equ_all);
		}
		InputStream is = create_env_list_xml(equ_all_list,env);
		return is;
	}
	
	
	
	/**
	 * 创建xml文件
	 * @param equ_all_list
	 * @param env
	 * @return
	 */
	public static InputStream create_env_list_xml(List<EquipmentAll> equ_all_list,String env) {
		//String[] order = new String[30];
		String[] order = { "IP", "主机名", "角色1", "角色2", "角色3", "角色4", "系统", "类型", "CPU", "硬盘","内存",
				"主板", "其他信息","环境"};
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(env);
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < 14; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellType(1);
			cell.setCellValue(order[i]);
		}
		int x = 1;
		for (EquipmentAll equ_all : equ_all_list) {
			HSSFRow row2 = sheet.createRow((short) x);
			String[] com = new String[16];
			if (equ_all.getIp() != null)
				com[0] = equ_all.getIp();
			else com[0] = null;
			if (equ_all.getHostsname() != null)
				com[1] = equ_all.getHostsname();
			else {
				com[1] = null;
			}
			if (equ_all.getRole() != null){
				String[] roles = (equ_all.getRole()).split(",");
				int i;
				for(i=0;i<roles.length;i++){
					com[2+i] = roles[i];
				}
				if(i==1||i==2||i==3){
					com[2+i] = null;
					com[3+i] = null;
					com[4+i] = null;
				}
			}
			else {
				com[2] = null;
				com[3] = null;
				com[4] = null;
				com[5] = null;
			}
			if (equ_all.getSystem() != null)
				com[6] = equ_all.getSystem();
			else {
				com[6] = null;
			}
			if (equ_all.getType() != null)
				com[7] = equ_all.getType();
			else {
				com[7] = null;
			}
			if (equ_all.getCpu() != null)
				com[8] = equ_all.getCpu();
			else {
				com[8] = null;
			}
			
			if (equ_all.getHard() != null)
				com[9] = equ_all.getHard();
			else {
				com[9] = null;
			}

			if (equ_all.getMemory() != null)
				com[10] = equ_all.getMemory();
			else {
				com[10] = null;
			}

			if (equ_all.getMotherboard() != null)
				com[11] = equ_all.getMotherboard();
			else {
				com[11] = null;
			}
			
			if (equ_all.getOther() != null)
				com[12] = equ_all.getOther();
			else {
				com[12] = null;
			}
			
			if (equ_all.getEnv() != null)
				com[13] = equ_all.getEnv();
			else {
				com[13] = null;
			}
			
			for (int i = 0; i < 14; i++) {
				HSSFCell cell = row2.createCell(i);
				cell.setCellType(1);
				cell.setCellValue(com[i]);
			}
			x++;
		}
		String xml_path = DeviceManagerImpl.class.getResource("/").getPath()
				.replaceAll("WEB-INF/classes/", "temp");
		try {
			if (!new File(xml_path).isDirectory())
				new File(xml_path).mkdir();
		} catch (SecurityException e) {
			System.out.println("can   not   make   directory ");
		}

		StringBuffer sb = new StringBuffer(env+"env");
		File file = new File(xml_path + "/" + sb.append(".xls").toString());
		try {
			OutputStream os = new FileOutputStream(file);
			try {
				workbook.write(os);

				os.close();
			} catch (IOException localIOException) {
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		InputStream xml_is = null;
		try {
			xml_is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return xml_is;
	}
	
	@Resource(name = "equipmentDao")
	public void setEquipmentDao(EquipmentDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}
	
}
