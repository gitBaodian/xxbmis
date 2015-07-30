package com.baodian.service.email.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.email.EmailDao;
import com.baodian.dao.user.User_EmailDao;
import com.baodian.model.email.Email;
import com.baodian.model.user.User;
import com.baodian.model.user.User_Email;
import com.baodian.service.email.EmailManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.JSONValue;
import com.baodian.util.page.EmailPage;

@Service("emailManager")
public class EmailManagerImpl implements EmailManager {
	private EmailDao emailDao;
	@Resource(name="emailDao")
	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}
	private User_EmailDao user_EmailDao;
	@Resource(name="user_EmailDao")
	public void setUser_EmailDao(User_EmailDao user_EmailDao) {
		this.user_EmailDao = user_EmailDao;
	}
	private Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//c
	public String saveEmail(Email email, String json) {
		int uId = SecuManagerImpl.currentId();
		if(uId == 0)
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		if(email==null || email.getTitle()==null || email.getTitle().isEmpty()) {
			email.setTitle("无标题！");
		} else if(email.getTitle().length() > 100){
			return "{\"status\":1,\"mess\":\"邮件标题超出限制！\"}";
		}
		if(email.getContent() == null) {
			email.setContent("");
		} else if(email.getContent().length() > 21845) {
			return "{\"status\":1,\"mess\":\"邮件内容超出限制！\"}";
		}
		email.setAddressor(new User(uId));
		email.setDate(new Date());
		if(email.getId() != 0) {
			//将读取状态改为已回复
			user_EmailDao.updateState(uId, email.getId(), 0, 3);
			//将发件的发送状态也改为已回复
			emailDao.updateState(0, email.getId(), 0, 2);
		}
		emailDao.save(email);
		if(json!=null && !json.isEmpty()) {
			//防止收件人重复
			Set<Integer> check = new HashSet<Integer>();
			int a = 0;
			for(String s : json.split("a")) {
				try {
					a = Integer.parseInt(s);
					if(check.add(a)) {
						if(user_EmailDao.getUnum(a) == 1)
							user_EmailDao.save(new User_Email(a, email.getId(), new Date(), 1));
					}
				} catch(NumberFormatException e) {}
			}
		}
		return "{\"status\":0,\"today\":\"" + ft.format(new Date()) +
				"\",\"urnums\":" + user_EmailDao.getUnReadByUId(uId) + "}";
	}
//r
	public String findE_Us(EmailPage page) {
		int uId = SecuManagerImpl.currentId();
		if(uId == 0) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		page.setUid(uId);
		StringBuilder json = new StringBuilder();
		if(page.getEmailst() < 0) {//发件箱
			List<Email> emails = emailDao.getEms(page);
			if(emails != null) {
				List<User> us;
				int length;
				for(int i=0; i<emails.size(); i++) {
					json.append("{\"id\":" + emails.get(i).getId() +
						",\"title\":\"" + JSONValue.escapeHTML(emails.get(i).getTitle()) +
						"\",\"date\":\"" + emails.get(i).getDate() +
						"\",\"sendst\":" + emails.get(i).getSendst() +
						",\"emailst\":" + emails.get(i).getEmailst() +
						",\"addrees\":[");
					us = user_EmailDao.get3UsByEId(emails.get(i).getId());
					length = us.size();
					if(length > 0) {
						for(int j=0; j<length; j++) {
							json.append("{\"id\":" + us.get(j).getId() +
									",\"name\":\"" + us.get(j).getName() + "\"},");
						}
						length = json.length();
						json.replace(length-1, length, "]},");
					} else json.append("]},");
				}
				json.deleteCharAt(json.length()-1);
			}
		} else {//收件箱
			json.append(listToStr(user_EmailDao.getE_Us(page)));
		}
		return "{\"status\":0,\"alnums\":" + page.getCountNums() +
				",\"urnums\":" + user_EmailDao.getUnReadByUId(page.getUid()) +
				",\"pages\":" + page.getPageNums() +
				",\"cpage\":" + page.getPage() +
				",\"today\":\"" + ft.format(new Date()) +
				"\",\"list\":[" + json + "]}";
	}
	/**
	 * 将User_Email收信列表转换为json格式
	 */
	private String listToStr(List<User_Email> ues) {
		StringBuilder json = new StringBuilder();
		if(ues!=null && ues.size()!=0) {
			for(int i=0; i<ues.size(); i++) {
				json.append("{\"id\":" + ues.get(i).getEmail().getId() +
					",\"title\":\"" + JSONValue.escapeHTML(ues.get(i).getEmail().getTitle()) +
					"\",\"date\":\"" + ues.get(i).getDate() +
					"\",\"readst\":" + ues.get(i).getReadst() +
					",\"recest\":" + ues.get(i).getRecest() +
					",\"emailst\":" + ues.get(i).getEmailst() +
					",\"uid\":" + ues.get(i).getEmail().getAddressor().getId()  +
					",\"uname\":\"" + ues.get(i).getEmail().getAddressor().getName() +
					"\",\"dname\":\"" + ues.get(i).getEmail().getAddressor().getDpm().getName() + "\"},");
			}
			return json.substring(0, json.length()-1);
		} else return "";
	}
	public String findNewUrE(String oldtime) {
		int uId = SecuManagerImpl.currentId();
		if(uId == 0) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		//检查输入的时间的合法性
		if(!oldtime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
			return "{\"status\":1,\"mess\":\"传入时间不合法！\"}";
		return "{\"status\":0,\"today\":\"" + ft.format(new Date()) +
				"\",\"list\":[" + listToStr(user_EmailDao.getE_Us(uId, oldtime)) + "]}";
	}
	public String fcEM(String eid) {
		int id;
		try {
			id = Integer.parseInt(eid);
		} catch(NumberFormatException e) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		Email e = emailDao.getEm(id>0 ? id : -id);
		if(e == null) return "{\"status\":1,\"mess\":\"此邮件不存在！\"}";
		int[] uids = {SecuManagerImpl.currentId(), e.getAddressor().getId(), 0};
		if(id<0 && uids[0]!=uids[1]) {//非发件人查看发件
			return "{\"status\":1,\"mess\":\"没有权限查看此封邮件！\"}";
		} else {
			uids[2] = 1;
		}
		StringBuilder json = new StringBuilder();
		List<User_Email> u_es = user_EmailDao.getE_UsByEId(e.getId());
		int readst;
		for(int j=0; j<u_es.size(); j++) {
			uids[1] = u_es.get(j).getUser().getId();
			readst = u_es.get(j).getReadst();
			if(uids[0] == uids[1]) {//自己是收件人
				uids[2] = 1;
				//读取状态未读改为已读
				if(readst==1 && id>0) {
					user_EmailDao.updateState(uids[0], e.getId(), 0, 2);
					readst = 2;
				}
			}
			json.append("{\"id\":" + uids[1] +
				",\"name\":\"" + u_es.get(j).getUser().getName() +
				"\",\"date\":\"" + u_es.get(j).getDate() +
				"\",\"readst\":" + readst +
				",\"recest\":" + u_es.get(j).getRecest() +
				",\"emailst\":" + u_es.get(j).getEmailst() +
				",\"did\":" + u_es.get(j).getUser().getDpm().getId() +
				",\"dname\":\"" + u_es.get(j).getUser().getDpm().getName() + "\"},");
		}
		if(uids[2] == 0) return "{\"status\":1,\"mess\":\"没有权限查看此封邮件！\"}";
		if(json.length() > 0)
			json.deleteCharAt(json.length()-1);
		return "{\"status\":0,\"urnums\":" + user_EmailDao.getUnReadByUId(uids[0]) +
				",\"today\":\"" + ft.format(new Date()) +
				"\",\"myid\":" + uids[0] +
				",\"title\":\"" + JSONValue.escapeHTML(e.getTitle()) +
				"\",\"content\":\"" + JSONValue.escape(e.getContent()) +
				"\",\"date\":\"" + e.getDate() +
				"\",\"sendst\":" + e.getSendst() +
				",\"emailst\":" + e.getEmailst() +
				",\"uid\":" + e.getAddressor().getId() +
				",\"uname\":\"" + e.getAddressor().getName() +
				"\",\"did\":" + e.getAddressor().getDpm().getId() +
				",\"dname\":\"" + e.getAddressor().getDpm().getName() +
				"\",\"addrees\":[" + json + "]}";
	}
//u
	public String changeSt(String json, EmailPage page) {
		int uid = SecuManagerImpl.currentId();
		if(uid == 0) return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		if(json!=null && !json.isEmpty()) {
			String[] str = json.split("A");
			if(str.length > 1) {
				Set<Integer> ems = new HashSet<Integer>();
				for(String s : str[1].split("a")) {
					try {
						ems.add(Integer.parseInt(s));
					} catch(NumberFormatException e) {}
				}
				if(ems.size() > 0) {
					try {
						switch(Integer.parseInt(str[0])) {
							case 1://将邮件状态改为删除
								for(int eid : ems)
									user_EmailDao.updateState(uid, eid, 1, 2);
								break;
							case 2://将邮件状态改为彻底删除
								for(int eid : ems) {
									if(user_EmailDao.getCDelete(uid, eid) &&
											emailDao.getCDelete(eid)) {//其他收件人和发件人都删除了，就直接删掉
										user_EmailDao.delete(eid);
										emailDao.delete(eid);
									} else {
										user_EmailDao.updateState(uid, eid, 1, 3);
									}
								}
								break;
							case 3://将邮件状态改为正常
								for(int eid : ems)
									user_EmailDao.updateState(uid, eid, 1, 1);
								break;
							case 4://将读取状态改为已读
								for(int eid : ems)
									user_EmailDao.updateState(uid, eid, 0, 2);
								break;
							case -1://将发件邮件状态改为删除
								for(int eid : ems)
									emailDao.updateState(uid, eid, 1, 2);
								break;
							case -2://将发件邮件状态改为彻底删除
								for(int eid : ems) {
									if(user_EmailDao.getCDelete(0, eid)) {//所有收件人都删除了，就直接删掉
										user_EmailDao.delete(eid);
										emailDao.delete(eid);
									} else {
										emailDao.updateState(uid, eid, 1, 3);
									}
								}
								break;
							case -3://将发件邮件状态改为正常
								for(int eid : ems)
									emailDao.updateState(uid, eid, 1, 1);
								break;
						}
					} catch(NumberFormatException e) {}
				}
			}
		}
		return findE_Us(page);
	}
	public String changeUr(String json, EmailPage page) {
		int uid = SecuManagerImpl.currentId();
		if(uid == 0) return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		try {
			int st = Integer.parseInt(json);
			if(st>0 && st<3){
				user_EmailDao.updateUr(uid, st);
			}
		} catch(NumberFormatException e) {}
		return findE_Us(page);
	}
//d
	
}
