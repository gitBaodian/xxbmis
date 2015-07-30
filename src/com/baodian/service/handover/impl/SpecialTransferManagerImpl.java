package com.baodian.service.handover.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.handover.SpecialTransferDao;
import com.baodian.dao.user.UserDao;
import com.baodian.model.handover.SpecialTransfer;
import com.baodian.model.user.User;
import com.baodian.service.handover.SpecialTransferManager;
import com.baodian.util.page.Page;

@Service("specialTransferManager")
public class SpecialTransferManagerImpl implements SpecialTransferManager{
	
	private UserDao userDao;
	private SpecialTransferDao specialTransferDao;
	
	/**
	 * 获取排班对象
	 */
	public String specialTransfer_getuser(){
		String json = "";
		String staff ="[";
		try {
			List<User> us = userDao.getU_ByDids("2,3,4,5,6,7");
			
			for(User user : us){
				staff = staff + "\""+user.getName()+"\",";
			}
			staff = staff + "\"\"]";
			json = "{\"status\":0,\"staff\":"+staff+"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"获取用户失败！！\"}";
		}
		return json;
	}
	
	/**
	 * 特殊排班添加
	 * @param specialtransfer
	 * @return 
	 */
	public String specialTransfer_add(SpecialTransfer specialtransfer){
		String json = "";
		try {
			specialTransferDao.save(specialtransfer);
			json = "{\"status\":0,\"mess\":\"添加成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"添加失败，日期重复！\"}";
		}
		return json;
	}
	
	/**
	 * 特殊排班删除
	 * @param specialtransfer
	 */
	public String specialTransfer_del(SpecialTransfer specialtransfer){
		String json = "";
		try {
			specialTransferDao.delete(specialtransfer);
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"删除失败！\"}";
		}
		return json;
	}
	
	/**
	 * 特殊排班修改
	 * @param specialtransfer
	 */
	public String specialTransfer_update(SpecialTransfer specialtransfer){
		String json = "";
		try {
			specialTransferDao.attachDirty(specialtransfer);
			json = "{\"status\":0,\"mess\":\"修改成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"修改失败！\"}";
		}
		return json;
	}

	/**
	 * 特殊排班查询
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public String specialTransfer_show(Page page) throws ParseException{
		String json = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		List<?> st_lists = specialTransferDao.findhql("select COUNT(*) from SpecialTransfer s");
		int sts = ((Long) st_lists.get(0)).intValue();
		if(sts == 0){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		page.countPage(sts);
		List<SpecialTransfer> st_list = (List<SpecialTransfer>) specialTransferDao.createQuery("from SpecialTransfer s order by s.specialDate asc"
				,page.getFirstNum(),page.getNum());
		json = "{\"total\":" + page.getCountNums() + "," +
				"\"rows\":[";
		for(SpecialTransfer st : st_list) {
	        String com = "";
	        Date date = new Date();
	        String now = ft.format(date);
	        if(ft.parse(now).getTime() >= ft.parse(st.getSpecialDate()).getTime()){
	        	com = "已生效";
	        }else{
	        	com = "未生效";
	        } 
	        
			json = json.concat("{\"specialDate\":\"" + st.getSpecialDate() + "\"," +
					"\"one\":\"" + st.getOne() + "\"," +
					"\"two\":\"" + st.getTwo() + "\"," +
					"\"there\":\"" + st.getThere() + "\"," +
					"\"dsh\":\""+com+"\"},");
		}
		if(st_list.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	@Resource(name = "specialTransferDao")
	public void setSpecialTransferDao(SpecialTransferDao specialTransferDao) {
		this.specialTransferDao = specialTransferDao;
	}
}
