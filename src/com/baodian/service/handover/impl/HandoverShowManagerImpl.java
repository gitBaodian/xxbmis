package com.baodian.service.handover.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.handover.AskLeaveDao;
import com.baodian.dao.handover.ShiftDao;
import com.baodian.dao.handover.TransferDao;
import com.baodian.model.handover.Shift;
import com.baodian.service.handover.HandoverShowManager;
import com.baodian.util.page.Page;

@Service("handoverShowManager")
public class HandoverShowManagerImpl implements HandoverShowManager{
	
	private AskLeaveDao askLeaveDao;
	private ShiftDao shiftDao;
	private TransferDao transferDao;
	
	
	
	/**
	 * 获取交接班记录
	 * @return
	 */
	public String handover_get(int id){
		Shift shift = (Shift) shiftDao.findById(id);
		String json = "";
		if(shift!=null){
			json = "{\"status\":0,\"crew\":\""+shift.getCrew().replace("#", " ")+"\"," +
					"\"remarkAccept\":\""+shift.getRemarkAccept()+"\",\"remarkShift\":\""+shift.getRemarkShift()+"\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"获取失败！\"}";
		}
		return json;
	}
	
	/**
	 * 修改交接记录
	 * @param shift
	 * @return
	 */
	public String handover_update(Shift shift){
		String json = "";
		try {
			Shift s = (Shift) shiftDao.findById(shift.getId());
			s.setCrew(shift.getCrew());
			s.setRemarkAccept(shift.getRemarkAccept());
			s.setRemarkShift(shift.getRemarkShift());
			shiftDao.attachDirty(s);
			json = "{\"status\":0,\"mess\":\"修改成功!\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"修改失败!\"}";
		}
		return json;
	}
	
	/**
	 * 增加交接记录
	 * @param shift
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public String handover_add(Shift shift) throws ParseException{
		String json = "";
		SimpleDateFormat fnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		Date b_date = HandoverManagerImpl.addDay(fnow.parse(shift.getSuccessionTime()), 0);
		Date e_date = HandoverManagerImpl.addDay(fnow.parse(shift.getSuccessionTime()), 1);
		String b = fdate.format(b_date)+" "+ "00:00:00";
		String e = fdate.format(e_date)+" "+ "00:30:00";
		List<Shift> list_shift = (List<Shift>) shiftDao.findhql("from Shift s where s.work = '"+shift.getWork()+"' and (s.successionTime between '"+b+"' and '"+e+"')");
		if(list_shift.size()!=0&&(list_shift.get(0).getShiftTime()!=null&&list_shift.get(0).getShiftTime().length()!=0)){
			json = "{\"status\":1,\"mess\":\"已存在该交接记录！\"}";
			return json;
		}
		if(list_shift.size()!=0&&(list_shift.get(0).getShiftTime()==null||list_shift.get(0).getShiftTime().length()==0)){
			Shift s = list_shift.get(0);
			s.setRemarkShift(shift.getRemarkShift());
			s.setShiftTime(shift.getShiftTime());
			try {
				shiftDao.attachDirty(s);
				json = "{\"status\":0,\"mess\":\"补签成功！\"}";
			} catch (Exception e1) {
				json = "{\"status\":1,\"mess\":\"补签失败！\"}";
			}
			return json;
		}
		try {
			shiftDao.save(shift);
			json = "{\"status\":0,\"mess\":\"补签成功！\"}";
		} catch (Exception ex) {
			json = "{\"status\":1,\"mess\":\"补签失败！\"}";
		}
		return json;
	}
	
	/**
	 * 删除交接记录
	 * @param id
	 * @return
	 */
	public String handover_delete(int id){
		String json = "";
		Shift shift = new Shift();
		shift.setId(id);
		try {
			shiftDao.delete(shift);
			json = "{\"status\":0,\"mess\":\"删除成功!\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"删除失败!\"}";
		}
		return json;
	}
	
	/**
	 * 交接记录显示
	 */
	@SuppressWarnings("unchecked")
	public String handover_show(Page page) throws ParseException{
		String json = "";
		SimpleDateFormat fnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat ftime = new SimpleDateFormat("HH:mm:ss");
		List<?> shift_lists = transferDao.findhql("select COUNT(*) from Shift s order by s.successionTime desc");
		int ak = ((Long) shift_lists.get(0)).intValue();
		if(ak == 0){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		page.countPage(ak);
		List<Shift> shift_list = (List<Shift>) askLeaveDao.createQuery("from Shift s order by s.successionTime desc"
				,page.getFirstNum(),page.getNum());
		json = "{\"total\":" + page.getCountNums() + "," +
				"\"rows\":[";
		for(Shift s : shift_list) {
			String bc = "";
			String sdate = "";
	        Date date = fnow.parse(s.getSuccessionTime());
			sdate = fdate.format(date);
	       
	        bc = s.getWork();
	        if(s.getRemarkAccept()==null){
	        	s.setRemarkAccept("");
	        }
	        if(s.getRemarkShift()==null){
	        	s.setRemarkShift("");
	        }
			json = json.concat("{\"id\":\"" + s.getId() + "\"," +
					"\"date\":\"" + sdate + "\"," +
					"\"bc\":\"" + bc + "\"," +
					"\"crew\":\"" + s.getCrew().replace("#", " ") + "\"," +
					"\"remarkAccept\":\"" + s.getRemarkAccept() + "\"," +
					"\"remarkShift\":\"" + s.getRemarkShift() + "\"," +
					"\"dsh\":\"待审核\"},");
		}
		if(shift_list.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	
	@Resource(name = "askLeaveDao")
	public void setAskLeaveDao(AskLeaveDao askLeaveDao) {
		this.askLeaveDao = askLeaveDao;
	}
	@Resource(name = "shiftDao")
	public void setShiftDao(ShiftDao shiftDao) {
		this.shiftDao = shiftDao;
	}
	@Resource(name = "transferDao")
	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}
}
