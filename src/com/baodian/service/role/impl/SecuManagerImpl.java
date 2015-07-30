﻿package com.baodian.service.role.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.baodian.dao.role.AuthorityDao;
import com.baodian.dao.role.Role_AuthDao;
import com.baodian.dao.user.UserDao;
import com.baodian.dao.user.User_RoleDao;
import com.baodian.model.role.Authority;
import com.baodian.model.role.Role_Auth;
import com.baodian.model.user.User;
import com.baodian.service.handover.HandoverManager;
import com.baodian.service.handover.NoticeManager;
import com.baodian.service.news.NewsbaseManager;
import com.baodian.service.notepad.NotepadManager;
import com.baodian.service.role.SecuManager;
import com.baodian.service.task.TaskManager;
import com.baodian.service.user.UserManager;
import com.baodian.security.impl.SecurityMetadataSourceImpl;
import com.baodian.util.StaticMethod;

@Service("secuManager")
public class SecuManagerImpl implements SecuManager {

	private AuthorityDao authorityDao;
	@Resource(name="authorityDao")
	public void setAuthorityDao(AuthorityDao authorityDao) {
		this.authorityDao = authorityDao;
	}
	@Resource(name="handoverManager")
	private HandoverManager handoverManager;
	@Resource(name="noticeManager")
	private NoticeManager noticeManager;
	@Resource(name="taskManager")
	private TaskManager taskManager;
	@Resource(name="notepadManager")
	private NotepadManager notepadManager;
	@Resource(name="newsbaseManager")
	private NewsbaseManager newsbaseManager;
	@Resource(name="userManager")
	private UserManager userManager;
	/**
	 * 因为类交给spring管理，会映射成静态类型(Ioc容器中的单例)，所以它们加static后所指向的对象是相同的
	 */
	private static UserDao userDao;
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		SecuManagerImpl.userDao = userDao;
	}
	private static User_RoleDao user_RoleDao;
	@Resource(name="userRoleDao")
	public void setUser_RoleDao(User_RoleDao user_RoleDao) {
		SecuManagerImpl.user_RoleDao = user_RoleDao;
	}
	private static Role_AuthDao role_AuthDao;
	@Resource(name="roleAuthDao")
	public void setRole_AuthDao(Role_AuthDao role_AuthDao) {
		SecuManagerImpl.role_AuthDao = role_AuthDao;
	}
//r
	public void initAll_RA_() {
		if (SecurityMetadataSourceImpl.authorityMap == null)
			SecurityMetadataSourceImpl.authorityMap = new HashMap<String, Collection<ConfigAttribute>>();
		if (SecurityMetadataSourceImpl.authorityMap.isEmpty()) {
			Iterator<Authority> authorities = this.authorityDao.getAll_AR_().iterator();
			while (authorities.hasNext()) {
				Authority authority = authorities.next();
				Collection<ConfigAttribute> configAttributes = new HashSet<ConfigAttribute>();
				Iterator<Role_Auth> role_Auths = authority.getRole_auths().iterator();
				// 类SecurityConfig实现了equals和hashCode方法
				// 使用HashSet时，将不会添加重复项
				while (role_Auths.hasNext()) {
					int rId = role_Auths.next().getRole().getId();
					configAttributes.add(new SecurityConfig(String.valueOf(rId)));
				}
				// 封装资源的内容(url,action)和资源对应的角色(role)
				SecurityMetadataSourceImpl.authorityMap.put(authority.getUrl(), configAttributes);
			}
		}
		System.out.println("***读取角色-权限成功.SecuManagerImpl.initAll_RA_***");
	}
	@Override
	public void refreshAll_RA_() {
		SecurityMetadataSourceImpl.authorityMap.clear();
		initAll_RA_();
	}
	
	@Override
	public void initMenu() {
		if (SecurityMetadataSourceImpl.menuList == null)
			SecurityMetadataSourceImpl.menuList = new ArrayList<Authority>();
		SecurityMetadataSourceImpl.menuList = this.authorityDao.getAsOnsd();
		if(SecurityMetadataSourceImpl.defaultMenu == null)
			SecurityMetadataSourceImpl.defaultMenu = new HashSet<Integer>();
		List<Integer> aids = this.authorityDao.getA_OnNoRole();
		if(aids != null)
			for(int id : aids) {
				SecurityMetadataSourceImpl.defaultMenu.add(id);
			}
		System.out.println("***生成菜单成功.SecuManagerImpl.initMenu***");
	}
	@Override
	public void refreshMenu() {
		SecurityMetadataSourceImpl.menuList.clear();
		SecurityMetadataSourceImpl.defaultMenu.clear();
		initMenu();
		
	}
	
	@SuppressWarnings("unchecked")
	public String findIndex(boolean needMenu) {
		JSONObject json = new JSONObject();
		if(needMenu) {
			//菜单
			json.put("menu", findMenu());
		}
		//当值人员
		try {
			json.put("staff", handoverManager.duty_officer());
		} catch (ParseException e) {}
		json.put("notice", noticeManager.notice());
		json.put("task", taskManager.findNeedDoTask());
		//json.put("wrecords", );
		json.put("notepad", notepadManager.findNpForIndex());
		json.put("handover", handoverManager.handover());
		json.put("newbase", newsbaseManager.findNbsForIndex());
		json.put("duty", userManager.findUByDids(0, StaticMethod.DateToString(new Date())));
		return json.toString();
	}
//static method
	/**
	 * 根据账号查找用户的详细信息及角色
	 * @param account
	 * @return
	 */
	public static User findUserR_ByU_a(String account) {
		User user = userDao.getUserByU_a(account);
		user.setUser_roles(user_RoleDao.getU_R_ByUId(user.getId()));
		return user;
	}
	/**
	 * 根据角色id查找菜单(权限)id
	 * @param rids
	 * @return
	 */
	public static List<Integer> findMenuByRIds(String rids) {
		return role_AuthDao.getAIdsByRIds(rids);
	}
	/**
	 * 获取属于自己的菜单
	 * @return [{id:1,name:"",action:"",pId:2}]
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray findMenu() {
		JSONArray array = new JSONArray();
		Set<Integer> userMenu;
		try {
			userMenu = ((org.springframework.security.core.userdetails.User)
					SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal())
						.getUserMenu();
		} catch(Exception e) {
			userMenu = new HashSet<Integer>();
		}
		for(Authority a : SecurityMetadataSourceImpl.menuList) {
			if(SecurityMetadataSourceImpl.defaultMenu.contains(a.getId())
					|| userMenu.contains(a.getId())) {
				JSONObject menu = new JSONObject();
				menu.put("id", a.getId());
				menu.put("name", a.getName());
				menu.put("action", a.getUrl());
				if(a.getParent() != null) {
					menu.put("pId", a.getParent().getId());
				}
				array.add(menu);
			}
		}
		return array;
	}
	/**
	 * 当前用户id
	 * @return 未登录返回  0
	 */
	public static int currentId() {
		try {
			return ((org.springframework.security.core.userdetails.User) 
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId()[0];
		} catch(ClassCastException e) {
			return 0;
		}
	}
	/**
	 * 当前用户账号
	 * @return 未登录返回  ""
	 */
	public static String currentAccount() {
		try {
			return ((org.springframework.security.core.userdetails.User)
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUsername();
		} catch(ClassCastException e) {
			return "";
		}
	}
	/**
	 * 当前用户姓名
	 * @return 未登录返回  ""
	 */
	public static String currentName() {
		try {
			return ((org.springframework.security.core.userdetails.User) 
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getStr()[0];
		} catch(ClassCastException e) {
			return "";
		}
	}
	/**
	 * 当前用户
	 * @return 未登录返回  null
	 */
	public static org.springframework.security.core.userdetails.User currentUser() {
		try {
			return (org.springframework.security.core.userdetails.User)
					SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch(ClassCastException e) {
			return null;
		}
	}
	/**
	 * 当前用户ip
	 * @return 未登录返回  ""
	 */
	public static String currentIp() {
		try {
			return ((WebAuthenticationDetails) SecurityContextHolder.getContext()
					.getAuthentication().getDetails()).getRemoteAddress();
		} catch(Exception e) {
			return "";
		}
	}
	/**
	 * 当前用户id[]组
	 * @param i 0->id. 1->部门 . 2->值班父部门(不存在返回-1，顶层返回-2)
	 * @return 未登录返回 0
	 */
	public static int currentID(int i) {
		try {
			return ((org.springframework.security.core.userdetails.User) 
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId()[i];
		} catch(ClassCastException e) {
			return 0;
		}
	}
	/**
	 * 当前用户id[]组
	 * @return i 0->id. 1->部门 . 2->值班父部门(不存在返回-1，顶层返回-2) 未登录返回null
	 */
	public static int[] currentIds() {
		try {
			return ((org.springframework.security.core.userdetails.User) 
					SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId();
		} catch(ClassCastException e) {
			return null;
		}
	}
	/**(此种静态方法无效，放在userManager.java上有效)
	@Autowired
	private static SessionRegistry sessionRegistry;
 	//登录用户数
	public static int loginUserNums() {
		return sessionRegistry.getAllPrincipals().size();
	}
	*/
	/**
	 * 是否有权限访问
	 * @param url 访问的地址
	 * @return 0->未登录 	负数id号->有权限	正数id号->无权限
	 */
	public static int canAccess(String url) {
		try {
			org.springframework.security.core.userdetails.User u = 
					(org.springframework.security.core.userdetails.User) 
						SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Collection<? extends GrantedAuthority> auths = u.getAuthorities();
			if(!auths.isEmpty()) {
				Collection<ConfigAttribute> clt = SecurityMetadataSourceImpl.authorityMap.get(url);
				if(clt == null) {
//System.out.println("[ " + url + " ] -> 未配置**");
					return u.getId()[0] * -1;
				}
				Iterator<ConfigAttribute> ite = clt.iterator();
				while(ite.hasNext()){
					String attr = ite.next().getAttribute();
					Iterator<? extends GrantedAuthority> itee = auths.iterator();
					while(itee.hasNext()) {
						if(itee.next().getAuthority().equals(attr)) {
//System.out.println("[ " + url + " ] -> 有权限访问**");
							return u.getId()[0] * -1;
						}
					}
				}
			}
//System.out.println("[ " + url + " ] -> 无权限访问**");
			return u.getId()[0];
		} catch(ClassCastException e) {
			return 0;
		}
	}
}
