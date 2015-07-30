package com.baodian.service.handover.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.handover.AskLeaveDao;
import com.baodian.dao.handover.SpecialTransferDao;
import com.baodian.dao.handover.TransferDao;
import com.baodian.dao.user.UserDao;
import com.baodian.dao.user.User_RoleDao;
import com.baodian.model.handover.AskLeave;
import com.baodian.model.handover.SpecialTransfer;
import com.baodian.model.handover.Transfer;
import com.baodian.model.user.User;
import com.baodian.model.user.User_Role;
import com.baodian.service.handover.ClassTableManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.util.InitDataManager;
import com.baodian.service.util.StaticDataManager;
import com.baodian.vo.DutySchedule;

@Service("classTableManager")
public class ClassTableManagerImpl implements ClassTableManager{

	private static UserDao userDao;
	private static User_RoleDao user_RoleDao;
	private static AskLeaveDao askLeaveDao;
	private static TransferDao transferDao;
	private static SpecialTransferDao specialTransferDao;
	
	/**
	 * 获取最终值班表
	 * @param i
	 * @param now   Date
	 * @param nowDate  String yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static List<DutySchedule> finalClassTable(int i ,Date now, String nowDate) throws ParseException{
		List<?> st_lists = specialTransferDao.findhql("select COUNT(*) from SpecialTransfer t where t.specialDate = '"+nowDate+"'");
		int st_size = ((Long) st_lists.get(0)).intValue();
		List<DutySchedule> classlist = new ArrayList<DutySchedule>();
		if(st_size==0){
			classlist = transfer(askleave(getClassTable(i, now) , nowDate) , nowDate);
		}else{
			classlist = special_transfer(nowDate);
		}
		return classlist;
	}
	
	/**
	 * 获取特殊排班
	 * @param date
	 * @return 
	 */
	public static List<DutySchedule> special_transfer(String nowDate){
		SpecialTransfer st = (SpecialTransfer) specialTransferDao.findById(nowDate);
		List<DutySchedule> classlist = new ArrayList<DutySchedule>();
		DutySchedule ds1 = new DutySchedule();
		ds1.setBc("后夜");
		ds1.setCrew(st.getOne());
		ds1.setClass_("一值");
		
		DutySchedule ds2 = new DutySchedule();
		ds2.setBc("白班");
		ds2.setCrew(st.getTwo());
		ds2.setClass_("二值");
		
		DutySchedule ds3 = new DutySchedule();
		ds3.setBc("前夜");
		ds3.setCrew(st.getThere());
		ds3.setClass_("三值");
		
		DutySchedule ds4 = new DutySchedule();
		ds4.setBc("休息");
		ds4.setCrew("");
		ds4.setClass_("四值");
		
		classlist.add(ds1); 
		classlist.add(ds2);
		classlist.add(ds3); 
		classlist.add(ds4);
		
		return classlist;
	}
	
	/**
	 * 获取当日值班表
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static List<DutySchedule> getClassTable(int dPid, Date date) throws ParseException{
		StringBuilder pids = new StringBuilder();
		//自己部门
		if(dPid == 0) {
			dPid = SecuManagerImpl.currentID(2);
			switch(dPid) {
				//case 0 : return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
				case 0 :
					if(StaticDataManager.dtParent.size() > 0) {
						dPid = StaticDataManager.dtParent.iterator().next();
						for(int i : StaticDataManager.dchildren.get(dPid))
							if(StaticDataManager.duty_depts.containsKey(i))
								pids.append(i + ",");
						break;
					} else {
						return Collections.emptyList();
					}
				case -1 ://无值班父部门，说明值班队列全部都是顶层或者无值班部门
					for(int i : StaticDataManager.duty_depts.keySet())
						pids.append(i + ",");
					break;
				case -2 ://只取顶层
					for(int i : StaticDataManager.duty_depts.keySet())
						if(StaticDataManager.depts.get(i).getParent() == null)
							pids.append(i + ",");
					break;
				default :
					for(int i : StaticDataManager.dchildren.get(dPid))
						if(StaticDataManager.duty_depts.containsKey(i))
							pids.append(i + ",");
			}
		} else {
			if(!StaticDataManager.dtParent.contains(dPid))
				return Collections.emptyList();
				//return "{\"status\":1,\"mess\":\"查询出错，不是倒班父部门！\"}";
			for(int i : StaticDataManager.dchildren.get(dPid))
				if(StaticDataManager.duty_depts.containsKey(i))
					pids.append(i + ",");
		}
		
		String[] zhiban = InitDataManager.dutyName;
		int[] shunxu = InitDataManager.dutyOrder;
		
		DutySchedule one = new DutySchedule();
		DutySchedule two = new DutySchedule();
		DutySchedule three = new DutySchedule();
		DutySchedule four = new DutySchedule();
		DutySchedule zdx = new DutySchedule();
		DutySchedule zszf = new DutySchedule();
		
		List<User> l_one = new ArrayList<User>();
		List<User> l_two = new ArrayList<User>();
		List<User> l_three = new ArrayList<User>();
		List<User> l_four = new ArrayList<User>();
		List<User> l_zdx = new ArrayList<User>();
		List<User> l_zszf = new ArrayList<User>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		int d = (int)(cal.getTimeInMillis() / (1000*60*60*24)) % 8;
		//System.out.println(d+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		switch(d) {
			//case 0: ; case 1: d = 0; break;
			//case 2: ; case 3: d = 1; break;
			//case 4: ; case 5: d = 2; break;
			//case 6: ; case 7: d = 3; break;
			case 1: ; case 2: d = 0; break;
			case 3: ; case 4: d = 1; break;
			case 5: ; case 6: d = 2; break;
			case 7: ; case 0: d = 3; break;
		}
		one.setBc(zhiban[(d+shunxu[2])%4]);
		two.setBc(zhiban[(d+shunxu[0])%4]);
		three.setBc(zhiban[(d+shunxu[1])%4]);
		four.setBc(zhiban[(d+shunxu[3])%4]);
		
		
		
		if(pids.length() != 0) {
			//System.out.println(pids);
			List<User> us = userDao.getU_ByDids(pids.substring(0, pids.length()-1));
			for(User u : us) {
				switch(StaticDataManager.duty_depts.get(u.getDpm().getId())) {
					case 0:
						l_one.add(u);
						break;
					case 1:
						l_two.add(u);
						break;
					case 2:
						l_three.add(u);
						break;
					case 3:
						l_four.add(u);
						break;
					case 6:
						l_zdx.add(u);
						break;
					case 7:
						l_zszf.add(u);
						break;
					default:
				}
			}
		}
		
		String crew = "";
		for(User u : l_one){
			List<User_Role> l_role = user_RoleDao.getU_R_ByUId(u.getId());
			for(User_Role r : l_role){
				if(r.getRole().getId()==4||r.getRole().getId()==5) crew = crew + u.getName() + "#";
			}
		}
		one.setCrew(crew);
		one.setClass_("一值");
		crew = "";
		
		for(User u : l_two){
			List<User_Role> l_role = user_RoleDao.getU_R_ByUId(u.getId());
			for(User_Role r : l_role){
				if(r.getRole().getId()==4||r.getRole().getId()==5) crew = crew + u.getName() + "#";
			}
		}
		two.setCrew(crew);
		two.setClass_("二值");
		crew = "";
		
		for(User u : l_three){
			List<User_Role> l_role = user_RoleDao.getU_R_ByUId(u.getId());
			for(User_Role r : l_role){
				if(r.getRole().getId()==4||r.getRole().getId()==5) crew = crew + u.getName() + "#";
			}
		}
		three.setCrew(crew);
		three.setClass_("三值");
		crew = "";
		
		for(User u : l_four){
			List<User_Role> l_role = user_RoleDao.getU_R_ByUId(u.getId());
			for(User_Role r : l_role){
				if(r.getRole().getId()==4||r.getRole().getId()==5) crew = crew + u.getName() + "#";
			}
		}
		four.setCrew(crew);
		four.setClass_("四值");
		crew = "";
		
		
		
		for(User u : l_zdx){
			List<User_Role> l_role = user_RoleDao.getU_R_ByUId(u.getId());
			for(User_Role r : l_role){
				if(r.getRole().getId()==4||r.getRole().getId()==5) crew = crew + u.getName() + "#";
			}
		}
		zdx.setBc("驻电信");
		zdx.setCrew(crew);
		zdx.setClass_("");
		crew = "";
		
		for(User u : l_zszf){
			List<User_Role> l_role = user_RoleDao.getU_R_ByUId(u.getId());
			for(User_Role r : l_role){
				if(r.getRole().getId()==4||r.getRole().getId()==5) crew = crew + u.getName() + "#";
			}
		}
		zszf.setBc("驻市政府");
		zszf.setCrew(crew);
		zszf.setClass_("");
		crew = "";
		
	
		List<DutySchedule> l_duty = new ArrayList<DutySchedule>();
		l_duty.add(one);
		l_duty.add(two);
		l_duty.add(three);
		l_duty.add(four);
		
		String crew_com = "";
		String class_com = "";
		
		DutySchedule bb = new DutySchedule();
		for(DutySchedule ds : l_duty){
			if(ds.getBc().equals(zhiban[0])){
				crew_com = crew_com + ds.getCrew();
				class_com = class_com + ds.getClass_() + "#";
			}
		}
		bb.setBc(zhiban[0]);
		bb.setCrew(crew_com);
		bb.setClass_(class_com);
		crew_com = "";
		class_com = "";
		
		DutySchedule hy = new DutySchedule();
		for(DutySchedule ds : l_duty){
			if(ds.getBc().equals(zhiban[1])){
				crew_com = crew_com + ds.getCrew();
				class_com = class_com + ds.getClass_() + "#";
			}
		}
		hy.setBc(zhiban[1]);
		hy.setCrew(crew_com);
		hy.setClass_(class_com);
		crew_com = "";
		class_com = "";
		
		DutySchedule qy = new DutySchedule();
		for(DutySchedule ds : l_duty){
			if(ds.getBc().equals(zhiban[2])){
				crew_com = crew_com + ds.getCrew();
				class_com = class_com + ds.getClass_() + "#";
			}
		}
		qy.setBc(zhiban[2]);
		qy.setCrew(crew_com);
		qy.setClass_(class_com);
		crew_com = "";
		class_com = "";
		
		DutySchedule xx = new DutySchedule();
		for(DutySchedule ds : l_duty){
			if(ds.getBc().equals(zhiban[3])){
				crew_com = crew_com + ds.getCrew();
				class_com = class_com + ds.getClass_() + "#";
			}
		}
		xx.setBc(zhiban[3]);
		xx.setCrew(crew_com);
		xx.setClass_(class_com);
		crew_com = "";
		class_com = "";
		
		List<DutySchedule> l_duty2 = new ArrayList<DutySchedule>();
		l_duty2.add(hy);
		l_duty2.add(bb);
		l_duty2.add(qy);
		l_duty2.add(xx);
		l_duty2.add(zdx);
		l_duty2.add(zszf);
		
		return l_duty2;
	}
	
	
	/**
	 * 调班算法
	 * @param dslist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<DutySchedule> transfer(List<DutySchedule> dslist ,String date){
		List<DutySchedule> comlist = new ArrayList<DutySchedule>();
		List<DutySchedule> comlist2 = new ArrayList<DutySchedule>();
		List<DutySchedule> comlist3 = new ArrayList<DutySchedule>();
		List<Transfer> tflist = (List<Transfer>) transferDao.findhql("from Transfer t where t.whether ='1' and (t.applicantTime = '"+date+"')");
		List<Transfer> rplist = (List<Transfer>) transferDao.findhql("from Transfer t where t.whether ='2' and (t.applicantTime = '"+date+"')");
		if((tflist.size()==0||tflist==null)&&(rplist.size()==0||rplist==null)){
			return dslist;
		}
			
		//调班循环
		for(Transfer tf : tflist){
			if(tf.getObject().equals("")||tf.getObject().length()==0) tf.setObject("x");
			if(comlist3.size()==0||comlist3==null){
				comlist.clear();
				comlist.addAll(dslist);
			}else{
				comlist.clear();
				comlist.addAll(comlist3);
				comlist3.clear();
			}
			comlist2.clear();
			for(DutySchedule ds : comlist){
				if (ds.getCrew().equals("#")||ds.getCrew()==""||ds.getCrew().length()==0) ds.setCrew("x#");
				String[] screw = ds.getCrew().split("#");
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(tf.getApplicant())){
						screw[i]=screw[i]+"a";
					}
				}
				
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(tf.getObject())){
						screw[i]=screw[i]+"o";
					}
				}
				
				String crew ="";
				for(int i=0;i<screw.length;i++){
					crew= crew+screw[i]+"#";
				}
				
				ds.setCrew(crew);
				comlist2.add(ds);
			}
			for(DutySchedule ds : comlist2){
				String[] screw = ds.getCrew().split("#");
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(tf.getApplicant()+"a")){
						screw[i]=tf.getObject();
					}
				}
				
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(tf.getObject()+"o")){
						screw[i]=tf.getApplicant();
					}
				}
				
				
				
				String crew ="";
				for(int i=0;i<screw.length;i++){
					crew= crew+screw[i]+"#";
				}
				ds.setCrew(crew.replace("x", ""));
				comlist3.add(ds);
			}
		}	
		
		//替班循环
		for(Transfer rp : rplist){
			if(rp.getObject().equals("")||rp.getObject().length()==0) rp.setObject("x");
			if(comlist3.size()==0||comlist3==null){
				comlist.clear();
				comlist.addAll(dslist);
			}else{
				comlist.clear();
				comlist.addAll(comlist3);
				comlist3.clear();
			}
			comlist2.clear();
			for(DutySchedule ds : comlist){
				if (ds.getCrew().equals("#")||ds.getCrew()==""||ds.getCrew().length()==0) ds.setCrew("x#");
				String[] screw = ds.getCrew().split("#");
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(rp.getApplicant())){
						screw[i]=screw[i]+"a";
					}
				}
				
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(rp.getObject())){
						screw[i]=screw[i]+"o";
					}
				}
				
				String crew ="";
				for(int i=0;i<screw.length;i++){
					crew= crew+screw[i]+"#";
				}
				ds.setCrew(crew);
				
				comlist2.add(ds);
			}
			for(DutySchedule ds : comlist2){
				String[] screw = ds.getCrew().split("#");
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(rp.getApplicant()+"a")){
						screw[i]=rp.getApplicant();
					}
				}
				
				for(int i=0;i<screw.length;i++){
					if(screw[i].equals(rp.getObject()+"o")){
						screw[i]=rp.getApplicant();
					}
				}
				
				
				String crew ="";
				for(int i=0;i<screw.length;i++){
					crew= crew+screw[i]+"#";
				}
				ds.setCrew(crew.replace("x", ""));
				
				comlist3.add(ds);
			}
		}
		
		return comlist3;
	}
	
	/**
	 * 请假算法
	 * @param dslist
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static List<DutySchedule> askleave(List<DutySchedule> dslist ,String date) throws ParseException{
		List<AskLeave> allist = (List<AskLeave>) askLeaveDao.findhql("from AskLeave a where a.whether = '1' and ('"+date+"' between a.startDate and a.endDate)");
		for(AskLeave al : allist){
			List<DutySchedule> comlist = new ArrayList<DutySchedule>();
				for(DutySchedule ds : dslist){
					String[] screw = ds.getCrew().split("#");
					for(int i=0;i<screw.length;i++){
						if(screw[i].equals(al.getApplicant())){
							screw[i]="";
						}
					}
					String crew ="";
					for(int i=0;i<screw.length;i++){
						crew= crew+screw[i]+"#";
					}
					ds.setCrew(crew);
					
					comlist.add(ds);
					
				}
				dslist = comlist;
			}
		return dslist;
	}

	
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		ClassTableManagerImpl.userDao = userDao;
	}
	@Resource(name="userRoleDao")
	public void setUser_RoleDao(User_RoleDao user_RoleDao) {
		ClassTableManagerImpl.user_RoleDao = user_RoleDao;
	}
	@Resource(name = "askLeaveDao")
	public void setAskLeaveDao(AskLeaveDao askLeaveDao) {
		ClassTableManagerImpl.askLeaveDao = askLeaveDao;
	}
	@Resource(name = "transferDao")
	public void setTransferDao(TransferDao transferDao) {
		ClassTableManagerImpl.transferDao = transferDao;
	}
	@Resource(name = "specialTransferDao")
	public void setSpecialTransferDao(SpecialTransferDao specialTransferDao) {
		ClassTableManagerImpl.specialTransferDao = specialTransferDao;
	}
}
