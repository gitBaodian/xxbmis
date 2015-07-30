package com.baodian.util;

import java.math.BigInteger;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.baodian.model.user.Department;
import com.baodian.service.util.InitDataManager;
import com.baodian.service.util.StaticDataManager;
import com.baodian.util.rsa.RSACoder;

public class StaticMethod {
	public static final String loginError = "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
	public static final String inputError = "{\"status\":1,\"mess\":\"输入有误！\"}";
	public static final String addSucc = "{\"status\":0,\"mess\":\"添加成功！\"}";
	public static final String changeSucc = "{\"status\":0,\"mess\":\"更新成功！\"}";
	public static final String removeSucc = "{\"status\":0,\"mess\":\"删除成功！\"}";
	public static final String authError = "{\"status\":1,\"mess\":\"没有权限！\"}";
	public static final String hessianError = "{\"status\":1,\"mess\":\"连接Hessian失败！\"}";
	
	@SuppressWarnings("unchecked")
	public static JSONObject loginError() {
		JSONObject json = new JSONObject();
		json.put("status", 1);
		json.put("mess", "请先登录");
		return json;
	}
	/**
	 * 外网代理存在时，也能正确获取ip
	 * @return ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		//System.out.println(ip);
		return ip;
	}
	/**
	 * 获取自己的倒班父部门
	 * @param did 自己的部门
	 * @return 不存在返回-1，顶层返回-2
	 */
	public static int getDPidonDt(int did) {
		//自己部门是倒班部门
		if(StaticDataManager.duty_depts.containsKey(did)) {
			Department dp = StaticDataManager.depts.get(did).getParent();
			//自己部门是顶层部门，一般不设顶层为倒班部门
			if(dp == null) {
				return -2;
			} else {
				//需要找的
				return dp.getId();
			}
		} else if(StaticDataManager.dtParent.contains(did)) {
			//自己部门是倒班父部门
			return did;
		} else {
			switch(StaticDataManager.dtParent.size()) {
				case 0 : return -1;
				case 1 ://只存在一个那就取它
					return StaticDataManager.dtParent.iterator().next();
				default:
					//一般设倒班部门为叶子节点，所以只往孩子节点查找
					int dPid = DFSofDpm(did);
					//都没有，取第一个倒班父部门
					if(dPid == 0)
						dPid = StaticDataManager.dtParent.iterator().next();
					return dPid;
					
			}
		}
	}
	/**
	 * Depth First Search深度优先搜索部门，查找值班父部门
	 */
	private static int DFSofDpm(int did) {
		List<Integer> cids = StaticDataManager.dchildren.get(did);
		if(cids == null) return 0;
		for(int id : cids) {
			if(StaticDataManager.dtParent.contains(id)) {
				return id;
			} else {
				int pid = DFSofDpm(id);
				if(pid != 0) return pid;
			}
		}
		return 0;
	}
	/**
	 * Breadth First Search 广度优先搜索
	private int BFSofDpm(LinkedList<Integer> search) {
		while(search.size() != 0) {
			int id = search.get(0);
			if(StaticDataManager.dtParent.contains(id)) {
				return id;
			} else {
				List<Integer> cids = StaticDataManager.dchildren.get(id);
				search.remove(0);
				if(cids == null) continue;
				search.addAll(cids);
			}
		}
		return 0;
	}*/
	private static Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 将时间转换为2013-02-24 22:31:13格式
	 */
	public static String DateToString(Date date) {
		return ft.format(date);
	}
	/**
	 * json字符串返回
	 */
	public static String jsonMess(int state, String str) {
		return "{\"status\":" + state + ",\"mess\":\"" + str + "\"}";
	}
	/**
	 * 返回字符串长度
	 * @return 空:-1
	 */
	public static int StrSize(String str) {
		if(str == null) {
			return -1;
		}
		return str.length();
	}
	/**
	 * 返回字符串长度，可定义最多长度
	 * @param max 最大长度
	 * @return 空:-1  超出最大长度：-2
	 */
	public static int StrSize(String str, int max) {
		if(str == null) {
			return -1;
		}
		if(str.length() > max) {
			return -2;
		}
		return str.length();
	}
	/**
	 * 字符串转换成数字
	 * * @return error: -1
	 */
	public static int Str2Int(String i) {
		try {
			return Integer.parseInt(i);
		} catch(Exception e) {
			return -1;
		}
	}
	/**
	 * 类似javascript array的join，将数组转换成字符串，中间用字符隔开
	 * @param array 数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String Array2Str(Set<Integer> array, String separator) {
		if(array.size() == 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for(int i : array) {
			str.append(i + separator);
		}
		return str.substring(0, str.length()-1);
	}
	/**
	 * 字符串转换成Date
	 * @return error: null
	 */
	public static Date str2Date(String str) {
		try {
			return (Date) ft.parseObject(str);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 格式化字符串日期，使其变成正确的Date
	 * @return error: null
	 */
	public static String formatDate(String str) {
		try {
			return ft.format((Date) ft.parseObject(str));
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 字符串转16进制，可以用于解决中文编码问题
	 */
	public static String Str2Hex(String str) {
		try {
			return new BigInteger(1, str.getBytes("UTF-8")).toString(16);
		} catch (Exception e) {
			return str;
		}
	}
	/**
	 * 16进制转字符串，可以用于解决中文编码问题
	 */
	public static String hex2Str(String str) {
		try {
			byte[] bt = new BigInteger(str, 16).toByteArray();
			if(bt[0] == 0) {
				return new String(bt, 1, bt.length-1, "UTF-8");
			} else {
				return new String(bt, "UTF-8");
			}
		} catch (Exception e) {
			return str;
		}
	}
	/**
	 * 使用公钥加密
	 */
	public static String rsaPUBEncode(String code) throws Exception {
		return RSACoder.encryptByPublicKey(code, 
				InitDataManager.datas.get("rsa.pubkey"), InitDataManager.datas.get("rsa.modkey"));
	}
	private static Format ft1 = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 将时间转换为2013-02-24格式
	 */
	public static String DateToDay(Date date) {
		return ft1.format(date);
	}
	/**
	 * 判断ip格式
	 * @param IP
	 * @return
	 */
	 public static boolean isip(String IP){//判断是否是一个IP
		 	if(IP.length()==0||IP==null) return false;
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
	 /**
	  * 处理ip 172.16.1.1..2,172.16.2.3..4
	  * @param ip
	  * @return
	  */
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

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
