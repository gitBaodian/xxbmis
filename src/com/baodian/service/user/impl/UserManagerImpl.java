﻿package com.baodian.service.user.impl;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.baodian.dao.role.RoleDao;
import com.baodian.dao.user.DepartmentDao;
import com.baodian.dao.user.UserDao;
import com.baodian.dao.user.User_RoleDao;
import com.baodian.model.role.Role;
import com.baodian.model.user.Department;
import com.baodian.model.user.User;
import com.baodian.model.user.User_Role;
import com.baodian.service.handover.HandoverManager;
import com.baodian.service.handover.impl.ClassTableManagerImpl;
import com.baodian.service.role.RoleManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.StaticDataManager;
import com.baodian.util.JSONValue;
import com.baodian.util.page.UserPage;
import com.baodian.vo.DutySchedule;

@Service("userManager")
public class UserManagerImpl implements UserManager {

	private UserDao userDao;
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	private User_RoleDao user_RoleDao;
	@Resource(name="userRoleDao")
	public void setUser_RoleDao(User_RoleDao user_RoleDao) {
		this.user_RoleDao = user_RoleDao;
	}
	private RoleDao roleDao;
	@Resource(name="roleDao")
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	private RoleManager rm;
	@Resource(name="roleManager")
	public void setRoleManager(RoleManager rm) {
		this.rm = rm;
	}
	private DepartmentDao ddao;
	@Resource(name="departmentDao")
	public void setDdao(DepartmentDao ddao) {
		this.ddao = ddao;
	}
	private StaticDataManager sdata;
	@Resource(name="staticData")
	public void setSdata(StaticDataManager sdata) {
		this.sdata = sdata;
	}
	private PasswordEncoder passwordEncoder;
	@Resource(name="passwordEncoder")
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	@Autowired
	private SessionRegistry sessionRegistry;
	private Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//c
	public String save(User user, int[] roleId, int depmId) {
		user.setAccount(user.getAccount().replaceAll("'", "").replaceAll("\"", "")
				.replaceAll("\\\\", "").toLowerCase());
		if(userDao.getU_NumByAc(user.getAccount(), 0) != 0) {
			return "{\"status\":1,\"mess\":\"此账户已存在！\",\"account\":false}";
		}
		user.setName(user.getName().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
		user.setPassword(passwordEncoder.encodePassword(
				user.getPassword(), user.getAccount()));
		user.setDpm(new Department(depmId));
		userDao.save(user);
		if(roleId!=null) {
			for(int i=0; i<roleId.length; i++) {
				user_RoleDao.save(new User_Role(user.getId(), roleId[i]));
			}
		}
		return "{\"status\":0}";
	}
//r
	public String findU_inadOnPage(UserPage page) {
		List<User> users = userDao.getU_inadOnPage(page);
		StringBuilder json = new StringBuilder();
		json.append("{\"total\":" + page.getCountNums() + ",\"rows\":[");
		for(int i=0;i<users.size();i++) {
			List<User_Role> urs = user_RoleDao.get_U_RByUId(users.get(i).getId());
			String rids = "", rnames = "";
			for(int j=0;j<urs.size();j++) {
				Role role = urs.get(j).getRole();
				if(j < 3) {
					rnames = rnames.concat(JSONValue.escape(role.getName()) + ",");
				}
				rids = rids.concat(String.valueOf(role.getId()) + ",");
			}
			if(urs.size() > 0) {
				if(urs.size() > 3)
					rnames = rnames.substring(0, rnames.length() -1 ).concat("...");
				else
					rnames = rnames.substring(0, rnames.length() -1 );
				rids = rids.substring(0, rids.length() -1 );
			}
			json.append("{\"id\":\"" + users.get(i).getId() + "\"," +
				"\"user.name\":\"" + users.get(i).getName() + "\"," +
				"\"user.account\":\"" + users.get(i).getAccount() + "\"," +
				"\"role\":\"" + rnames + "\"," +
				"\"dpmName\":\"" + JSONValue.escape(users.get(i).getDpm().getName()) + "\"," +
				"\"roleId\":[" + rids + "]," +
				"\"depmId\":\"" + users.get(i).getDpm().getId() + "\"},");
		}
		if(users.size() != 0)
			return json.substring(0, json.length()-1) + "]}";
		return json.toString() + "]}";
	}
	public User findU_inadrById(int id) {
		User user = userDao.getU_inadById(id);
		if(user == null)
			return null;
		user.setUser_roles(user_RoleDao.get_U_RByUId(id));
		return user;
	}
	public String findU_passByAccount(String account) {
		return userDao.getU_passByAccount(account);
	}
	@SuppressWarnings("unchecked")
	public JSONArray findUByDids(int dPid,String date) {
		JSONArray array = new JSONArray();
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = fdate.parse(date);
		} catch (ParseException e1) {
			return array;
		}
		String nowDate = fdate.format(dt);
		List<DutySchedule> classlist = null;
		try {
//			classlist = handoverManager.transfer(
//					handoverManager.askleave(
//							handoverManager.getClassTable(dPid, dt) , nowDate) , nowDate);
			classlist = ClassTableManagerImpl.finalClassTable(dPid, dt, nowDate);
		} catch (ParseException e) {
			return array;
		}
		for(DutySchedule ds : classlist){
			JSONObject json = new JSONObject();
			json.put("cl", ds.getClass_());
			json.put("bc", ds.getBc());
			json.put("crew", ds.getCrew());
			array.add(json);
		}
		return array;
	}
	public String findU_Ds(int did) {
		StringBuilder json = new StringBuilder();
		json.append('[');
		List<Integer> dids = StaticDataManager.dchildren.get(did);
		if(dids != null)
			for(int id : dids) {
				Department dept = StaticDataManager.depts.get(id);
		    	json.append("{\"id\":" + dept.getId() +"," +
						"\"name\":\"" + dept.getName() +"\"");
		    	if(StaticDataManager.dchildren.get(id)!=null ||
		    			userDao.getU_NumbyD_id(id)!=0)//存在子部门或者用户
		    		json.append(",\"isParent\":true");
				json.append("},");
			}
		if(did != 0) {
			List<User> us = userDao.getU_ByDid(did);
			for(User u : us) {
				json.append("{\"id\":-" + u.getId() + "," +
						"\"name\":\"" + u.getName() + "\",\"iconSkin\":\"user\"},");
			}
		}
		if(json.length() != 1)
			return json.substring(0, json.length()-1) + ']';
		return json.toString() + ']';
	}
	public String findU_Ds(String dpms) {
		if(dpms==null || dpms.isEmpty()) return "[]";
		//已经检查过的
		Set<Integer> check = new HashSet<Integer>();
		//初步需要读取的
		List<Integer> unrd = new ArrayList<Integer>();
		String[] strs = dpms.split("A");
		int a = 0;
		for(String s : strs[0].split("a")) {
			try {
				a = Integer.parseInt(s);
				unrd.add(a);
			} catch(NumberFormatException e) {}
		}
		//最终需要读取的
		List<Integer> need = new ArrayList<Integer>();
		if(strs.length > 1)
			for(String s : strs[1].split("a")) {
				try {
					a = Integer.parseInt(s);
					check.add(a);
				} catch(NumberFormatException e) {}
			}
		//还未读取任何一个
		else need.add(0);
		for(int did : unrd) {
			addNeddPid(did, need, check);
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(int did : need) {
			json.append("{\"pid\":" + did + ",\"list\":" + findU_Ds(did) + "},");
		}
		if(json.length() != 1)
			return json.substring(0, json.length()-1) + ']';
		return json.toString() + ']';
	}
	/**
	 * 根据部门查找其父辈节点
	 * @param did 部门id
	 * @param need 保存父辈节点
	 * @param check 已经检查过的节点
	 */
	private void addNeddPid(int did, List<Integer> need, Set<Integer> check) {
		Department dpm = StaticDataManager.depts.get(did);
		if(dpm == null) return;
		if(dpm.getParent() != null) {
			int a = dpm.getParent().getId();
			if( ! check.contains(a)) {
				addNeddPid(a, need, check);
			}
		}
		if( ! check.contains(did)) {
			need.add(did);
			check.add(did);
		}
	}
	public String findDRs() {
		return "{\"roles\":" + rm.findRoles() +
				",\"dpms\":" + sdata.findDept() + "}";
	}
//u
	public String change(User user, int[] roleId, int depmId) {
		user.setAccount(user.getAccount().replaceAll("'", "").replaceAll("\"", "")
				.replaceAll("\\\\", "").toLowerCase());
		if(userDao.getU_NumByAc(user.getAccount(), user.getId()) != 0) {
			return "{\"status\":1,\"mess\":\"此账户已存在！\",\"account\":false}";
		}
		user.setName(user.getName().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
		if(!user.getAccount().isEmpty()) {
			user.setPassword(passwordEncoder.encodePassword(
					user.getPassword(), user.getAccount()));
		}
		userDao.update(user, depmId);
		if(roleId == null) {
			user_RoleDao.delete(user.getId());
		} else if(roleId[0] != -1) {
			user_RoleDao.delete(user.getId());
			for(int i=0; i<roleId.length; i++) {
				user_RoleDao.save(new User_Role(user.getId(), roleId[i]));
			}
		}
		return "{\"status\":0}";
	}
	public String changePW(User user) {
		String account = SecuManagerImpl.currentAccount();
		String password = userDao.getU_passByAccount(account);
		if(passwordEncoder.encodePassword(user.getAccount().toLowerCase(), account).equals(password)) {
			user.setAccount(account);
			user.setPassword(passwordEncoder.encodePassword(user.getPassword(), account));
			userDao.updatePW(user);
			return "{\"status\":0}";
		} else {
			return "{\"status\":1,\"mess\":\"密码错误！\",\"password\":false}";
		}
	}
	public String changeDpm(String json) {
		if(json==null || json.length()==0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		String[] ids = json.split("A");
		int did = 0;
		try {
			did = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		if(! ddao.chkExit(did)) {
			return "{\"status\":1,\"mess\":\"部门不存在！\"}";
		}
		if(ids.length > 1) {
			int uid = 0;
			Set<Integer> uids = new HashSet<Integer>();
			for(String user : ids[1].split("a")) {
				try {
					uid = Integer.parseInt(user);
				} catch(NumberFormatException e) {}
				if(uids.add(uid) && userDao.chkExit(uid)) {
					userDao.updateDpm(uid, did);
				}
			}
		}
		return "{\"status\":0}";
	}
	public String changeRole(String json) {
		if(json==null || json.length()==0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		String[] ids = json.split("A");
		if(ids.length < 3) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		int changeType = 1;
		try {
			changeType = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		int rid = 0;
		Set<Integer> rids = new HashSet<Integer>();
		List<Integer> roleIds = new ArrayList<Integer>();
		for(String role : ids[1].split("a")) {
			try {
				rid = Integer.parseInt(role);
			} catch(NumberFormatException e) {}
			if(rids.add(rid) && roleDao.chkExit(rid)) {
				roleIds.add(rid);
			}
		}
		if(roleIds.size() == 0) {
			return "{\"status\":1,\"mess\":\"角色不存在！\"}";
		}
		int uid = 0;
		Set<Integer> uids = new HashSet<Integer>();
		List<Integer> userIds = new ArrayList<Integer>();
		for(String user : ids[2].split("a")) {
			try {
				uid = Integer.parseInt(user);
			} catch(NumberFormatException e) {}
			if(uids.add(uid) && userDao.chkExit(uid)) {
				userIds.add(uid);
			}
		}
		if(userIds.size() == 0) {
			return "{\"status\":1,\"mess\":\"用户不存在！\"}";
		}
		switch(changeType) {
			case 2://增加
				for(int userId : userIds) {
					for(int roleId : roleIds) {
						user_RoleDao.delete(userId, roleId);
						user_RoleDao.save(new User_Role(userId, roleId));
					}
				}
				break;
			case 3://删除
				for(int userId : userIds) {
					for(int roleId : roleIds) {
						user_RoleDao.delete(userId, roleId);
					}
				}
				break;
			default://替换
				for(int userId : userIds) {
					user_RoleDao.delete(userId);
					for(int roleId : roleIds) {
						user_RoleDao.save(new User_Role(userId, roleId));
					}
				}
		}
		return "{\"status\":0}";
	}
//d
	public String remove(User user) {
		String account = SecuManagerImpl.currentAccount();
		String password = userDao.getU_passByAccount(account);
		if(passwordEncoder.encodePassword(user.getPassword(), account).equals(password)) {
			user_RoleDao.delete(user.getId());
			userDao.delete(user);
			return "{\"status\":0}";
		} else {
			return "{\"status\":1,\"mess\":\"密码错误！\",\"password\":false}";
		}
	}
//o
	public void makePassword(String account, String password) {
		System.out.println(account + " -> "+passwordEncoder.encodePassword(account, password));
	}
	public int loginUserNums() {
		return sessionRegistry.getUserNum();
	}
	public String findlgUser() {
		List<Object> slist = sessionRegistry.getAllPrincipals();
		StringBuilder json = new StringBuilder();
		json.append("共 " + sessionRegistry.getUserNum() + " 用户登录， " +
				sessionRegistry.getSessionNum() + " 处登录，服务器时间：" +
				ft.format(new Date()) + "<br>");
		for(int i=0; i<slist.size(); i++) {
			org.springframework.security.core.userdetails.User u = 
					(org.springframework.security.core.userdetails.User) slist.get(i);
			json.append("第 " + (i+1) + " 个用户，账号： " + u.getUsername() + "<br>");
			//包括被限制登录用户
			List<SessionInformation> ilist = sessionRegistry.getAllSessions(u, true);
			json.append("----" + ilist.size() + " 处登录<br>");
			for(int j=0; j<ilist.size(); j++) {
				SessionInformation sif = ilist.get(j);
				u = (org.springframework.security.core.userdetails.User) sif.getPrincipal();
				json.append("--------" + (j+1) +
						" 用户名：" + u.getStr()[0] +
						" 部门：" + StaticDataManager.depts.get(u.getId()[1]).getName() +
						" 最后访问时间：" + ft.format(sif.getLastRequest()) +
						" ip：" +  sif.getRemoteIp() +
						//" session:" + sif.getSessionId() +
						//" 限制登录：" + sif.isExpired() +
						"<br>");
				//强制退出1.将其限制，2.将其移除，使用第一种比较好
				//sif.expireNow();
				//sessionRegistry.removeSessionInformation(sif.getSessionId());
			}
			json.append("<br>");
		}
		return json.toString();
	}
//
	public User getUserByName(String username) {
		return userDao.getUserByName(username);
	}

	@SuppressWarnings("rawtypes")
	public String getAllUserName() {
		String json = "[";
		String user_name;
		List usernames = userDao.getAllUserName();
		Iterator it = usernames.iterator();
		while(it.hasNext()){
			user_name = (String) it.next();
			json = json.concat("{\"id\":\""+user_name+"\",\"text\":\""+user_name+"\"},");
		}
		if(!usernames.isEmpty()){
			json = json.substring(0, json.length()-1);//去掉逗号
		}
		json = json.concat("]");
		return json;
	}
	
	
	private HandoverManager handoverManager;
	
	public HandoverManager getHandoverManager() {
		return handoverManager;
	}
	
	@Resource(name = "handoverManager")
	public void setHandoverManager(HandoverManager handoverManager) {
		this.handoverManager = handoverManager;
	}
}
