package com.baodian.service.handover.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import com.baodian.dao.handover.ShiftDao;
import com.baodian.dao.record.RecordDao;
import com.baodian.dao.user.UserDao;
import com.baodian.model.handover.Shift;
import com.baodian.service.handover.HandoverManager;
import com.baodian.service.notepad.NotepadManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.vo.DutySchedule;
import com.baodian.model.notepad.Notepad;
import com.baodian.model.record.Working_Record;

@Service("handoverManager")
public class HandoverManagerImpl implements HandoverManager{
	
	private UserDao userDao;
	private RecordDao recordDao;
	private ShiftDao shiftDao;
	
	public static String morningS;
	public static String morningE;
	public static String duskS;
	public static String duskE;
	public static String nightS;
	public static String nightE;
	
	private static SimpleDateFormat fnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat ftime = new SimpleDateFormat("HH:mm:ss");

	private NotepadManager nm;//每日记事
	@Resource(name="notepadManager")
	public void setNm(NotepadManager nm) {
		this.nm = nm;
	}
	
	@PostConstruct
	public void init() {
		InputStream in = this.getClass().getResourceAsStream("/config.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
			morningS = properties.getProperty("morningS");
			morningE = properties.getProperty("morningE");
			duskS = properties.getProperty("duskS");
			duskE = properties.getProperty("duskE");
			nightS = properties.getProperty("nightS");
			nightE = properties.getProperty("nightE");
			System.out.println(morningS+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (IOException e) {
			System.out.println("------------------------------------------");
		}
	}
	
	
	
	/**
	 * 当值人员
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public JSONObject duty_officer() throws ParseException{
		JSONObject json = new JSONObject();
		JSONArray staff = new JSONArray();
		JSONArray staff_2 = new JSONArray();
		//json = "{\"status\":0,\"staff\":[待优化],\"staff_2\":[待优化]}";
		//return json;
		List<Shift> list_shift = (List<Shift>) shiftDao.findhql("from Shift s where s.shiftTime is null  order by s.successionTime desc");
		if(list_shift!=null&&list_shift.size()!=0){
			Iterator<Shift> shift_iterator=list_shift.iterator();
			Shift shift = shift_iterator.next();
			String sd = shift.getSuccessionTime().substring(0, 10);
			Date now = new Date();
			String s_now = fdate.format(now);
			String s_time = ftime.format(now);
			if(ftime.parse(s_time).getTime()>=ftime.parse(nightS).getTime()&&ftime.parse(s_time).getTime()<=ftime.parse("23:59:59").getTime()){
				s_now = fdate.format(addDay(now,+1));
			}
			if(s_now.equals(sd)){
				
				String[] screw = shift.getCrew().split("#");
				for(int i=0;i<screw.length;i++){
					staff.add(screw[i]);
				}
			}else{
				staff_2.add(sd.substring(5,10) + " " + shift.getWork() + "未交班");
			}
			
			while(shift_iterator.hasNext()){
				Shift shift_2 = shift_iterator.next();
				String sd_2 = shift_2.getSuccessionTime().substring(0, 10);
				sd_2 = sd_2.substring(5,10);
				staff_2.add(sd_2 + " " + shift_2.getWork() + "未交班");
			}
		}else{
			staff.add("未接班");
		}
		json.put("status", 0);
		json.put("staff", staff);
		json.put("staff_2", staff_2);
		return json;
	}
	
	
	/**
	 * 获取上一值的交班记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String handover_get_remark_shift(){
		String json = "";
		List<Shift> list_shift = (List<Shift>) shiftDao.createQuery("from Shift s order by s.successionTime desc", 0, 1);
		String com = list_shift.get(0).getRemarkShift();
		if(com == null){
			com = "";
		}
		json = "{\"status\":0,\"remarkShift\":\""+com+"\"}";
		return json;
	}
	
	/**
	 * 交接班记录显示
	 */
	@SuppressWarnings("unchecked")
	public JSONArray handover() {
		List<Shift> list_shift = (List<Shift>) shiftDao.createQuery("from Shift s order by s.successionTime desc", 0, 5);
		JSONArray json = new JSONArray();
		for(Shift s : list_shift){
			if(s.getShiftTime()!=null&&s.getShiftTime().length()!=0){
				if(s.getCrew()==null) s.setCrew("");
				if(s.getRemarkShift()==null) s.setRemarkShift("");
				JSONObject shiftJSON = new JSONObject();
				shiftJSON.put("handover_time", s.getShiftTime());
				shiftJSON.put("handover_people", s.getCrew().replaceAll("#", " "));
				shiftJSON.put("handover_zhiban", s.getWork());
				shiftJSON.put("handover_status", "交班");
				shiftJSON.put("remark", s.getRemarkShift());
				json.add(shiftJSON);
			}
			if(s.getCrew()==null) s.setCrew("");
			if(s.getRemarkAccept()==null) s.setRemarkAccept("");
			JSONObject shiftJSON = new JSONObject();
			shiftJSON.put("handover_time", s.getSuccessionTime());
			shiftJSON.put("handover_people", s.getCrew().replaceAll("#", " "));
			shiftJSON.put("handover_zhiban", s.getWork());
			shiftJSON.put("handover_status", "接班");
			shiftJSON.put("remark", s.getRemarkAccept());
			json.add(shiftJSON);
		}
		return json;
	}
	
	/**
	 * 交班
	 * @return
	 * @throws ParseException
	 */
	public  String shift(String remark_shift, String addnp) throws ParseException{
		String user_name = SecuManagerImpl.currentName();
		if(user_name.isEmpty()){
			return "{\"status\":2,\"mess\":\"请先登录系统！\"}";
		}
		DutySchedule dsnow = new DutySchedule();
		Date now = new Date();
		Date now_now = now;
		String nowDate = fdate.format(now);
		String nowTime = ftime.format(now);
		
		String[][] BC = get_shift_bc(nowTime, now);
		String cn = BC[0][0];
		nowDate = BC[0][1];
		if(nowDate!=null){now = fdate.parse(nowDate);}
		if(cn.equals("1")){
			return "{\"status\":1,\"mess\":\"当前不是交班时间！\"}";
		}
		List<DutySchedule> classlist = ClassTableManagerImpl.finalClassTable(0,now,nowDate);
		for(DutySchedule ds : classlist){
			if(cn.equals("白班")&&ds.getBc().equals("白班")){
				dsnow = ds;
			}
			if(cn.equals("前夜")&&ds.getBc().equals("前夜")){
				dsnow = ds;
			}
			if(cn.equals("后夜")&&ds.getBc().equals("后夜")){
				dsnow = ds;
			}
		}
		String[] screw = dsnow.getCrew().split("#");
		int is=0;
		for(int i=0;i<screw.length;i++){
			if(screw[i].equals(user_name)){
				is=1;
			}
		}
		
		if(is==1){
			@SuppressWarnings("unchecked")
			List<Shift> shiftlist = (List<Shift>) shiftDao.findhql("from Shift s where s.class_ = '"+dsnow.getClass_()+"' and s.work = '"+cn+"' order by s.successionTime desc");
			if(shiftlist.size() == 0||shiftlist ==null){
				return "{\"status\":1,\"mess\":\"未接班，请先补签！\"}";
			}
			Shift nowShift = shiftlist.get(0);
			if(nowShift.getShiftTime()!=null){
				return "{\"status\":1,\"mess\":\"已经交班，你可以休息了！(*^__^*)~\"}";
			}
			
			nowShift.setShiftTime(fnow.format(now_now));
			nowShift.setRemarkShift("应用无异常，各环境无异常。");
			if(remark_shift.length() !=0 && remark_shift!=null){
				
				nowShift.setRemarkShift(JSONValue.escape(remark_shift));
				
				Working_Record wr = new Working_Record();
				wr.setTime(now_now);
				
				wr.setDetail("交班,"+JSONValue.escape(remark_shift));
				wr.setType("值班交接");
				wr.setUser(userDao.getUserByName(user_name));
				recordDao.addWorkingRecord(wr);
			}
			shiftDao.attachDirty(nowShift);
		}else{
			return "{\"status\":1,\"mess\":\"你不是值班人员！\"}";
		}
		if(addnp != null) {//添加进每日记事
			nm.save(new Notepad(remark_shift));
		}
		return "{\"status\":0,\"mess\":\"交班成功！\"}";
	}
	
	
	
	/**
	 * 接班
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public String accept(String remark_accept,String user_name) throws ParseException{
		String json = "";
		DutySchedule dsnext = new DutySchedule();
		Date now = new Date();
		Date now_now = now;
		String nowDate = fdate.format(now);
		String nowTime = ftime.format(now);
		
		String BC[][] = get_shift_bc(nowTime, now);
		String cn = BC[0][0];
		String ct = BC[1][1];
		if(cn.equals("1")){
			json = "{\"status\":1,\"mess\":\"当前不是接班时间！\"}";
			return json;
		}
		if((ftime.parse(nowTime).getTime() >= ftime.parse(nightS).getTime()&&ftime.parse(nowTime).getTime() <= ftime.parse("23:59:59").getTime())){
			now = addDay(now, +1);
			nowDate = fdate.format(now);
			now = fnow.parse(nowDate+" "+"00:00:00");
		}
		List<DutySchedule> classlist = ClassTableManagerImpl.finalClassTable(0,now,nowDate);
		
		for(DutySchedule ds : classlist){
			if(ct.equals("白班")&&ds.getBc().equals("白班")){
				dsnext = ds;
			}
			if(ct.equals("前夜")&&ds.getBc().equals("前夜")){
				dsnext = ds;
			}
			if(ct.equals("后夜")&&ds.getBc().equals("后夜")){
				dsnext = ds;
			}
		}
		String[] screw = dsnext.getCrew().split("#");
		int is=0;
		for(int i=0;i<screw.length;i++){
			if(screw[i].equals(user_name)){
				is=1;
			}
		}
		if(is==1){
			List<Shift> acceptlist = (List<Shift>) shiftDao.findhql("from Shift s where s.work = '"+ct+"' and s.shiftTime is null");
			if(acceptlist.size()!=0){
				//bug
				json = "{\"status\":0,\"mess\":\""+user_name+"你已接班成功，请努力工作！O(∩_∩)O~\"}";
				return json;
			}else{
				List<Shift> shiftlist = (List<Shift>) shiftDao.findhql("from Shift s where s.work = '"+cn+"' and s.shiftTime is null");
				Shift accept = new Shift();
				accept.setSuccessionTime(fnow.format(now));
				accept.setClass_(dsnext.getClass_());
				accept.setWork(dsnext.getBc());
				accept.setCrew(dsnext.getCrew());
				accept.setWdate(BC[0][1]);
				
				accept.setRemarkAccept("应用无异常，各环境无异常。");
				if(remark_accept.length()!=0&&remark_accept!=null){
					
					accept.setRemarkAccept(JSONValue.escape(remark_accept));
					
					Working_Record wr = new Working_Record();
					wr.setTime(now_now);
					wr.setDetail("接班,"+JSONValue.escape(remark_accept));
					wr.setType("值班交接");
					wr.setUser(userDao.getUserByName(user_name));
					recordDao.addWorkingRecord(wr);
				}
				shiftDao.save(accept);
				json = "{\"status\":0,\"mess\":\""+user_name+"你已接班成功，请努力工作！O(∩_∩)O~\"}";
				if(shiftlist.size()!=0){
					json = "{\"status\":0,\"mess\":\""+user_name+"你已接班成功，上一个班未交班，请通知其补签！\"}";
				}
				return json;
			}
		}else{
			json = "{\"status\":1,\"mess\":\"你不是接班人员！\"}";
			return json;
		}
	}
	
	
	/**
	 * 获取当前时间所属的班次
	 * @param check_time
	 * @return
	 * @throws ParseException
	 */
	public static String[][] get_nowtime_bc(String check_time,Date now) throws ParseException{
		String[][] bc = new String[2][2];
		if(ftime.parse(HandoverManagerImpl.morningE).getTime()<=ftime.parse(check_time).getTime() && ftime.parse(HandoverManagerImpl.duskE).getTime()>=ftime.parse(check_time).getTime()){
			bc[0][0] = "白班";
			bc[0][1] = fdate.format(now);
		}
		else if(ftime.parse(HandoverManagerImpl.duskE).getTime()<=ftime.parse(check_time).getTime() && ftime.parse("23:59:59").getTime()>=ftime.parse(check_time).getTime()){
			bc[0][0] = "前夜";
			bc[0][1] = fdate.format(now);
		}
		else if(ftime.parse("00:00:00").getTime()<=ftime.parse(check_time).getTime() && ftime.parse(HandoverManagerImpl.nightE).getTime()>=ftime.parse(check_time).getTime()){
			bc[0][0] = "前夜";
			if(now == null){
				bc[0][1] = fdate.format(now);
			}
			else{
				now = addDay(now, -1);
				bc[0][1] = fdate.format(now);
			}
		}
		else if(ftime.parse(HandoverManagerImpl.nightE).getTime()<=ftime.parse(check_time).getTime() && ftime.parse(HandoverManagerImpl.morningE).getTime()>=ftime.parse(check_time).getTime()){
			bc[0][0] = "后夜";
			bc[0][1] = fdate.format(now);
		}
		else{
			bc[0][0] = "1";
			bc[0][1] = fdate.format(now);
		}
		return bc;
	}
	
	/**
	 * 交接班的当前时间班次，已经修正时间
	 * @param check_time
	 * @param now
	 * @return
	 * @throws ParseException
	 */
	public static String[][] get_shift_bc(String check_time ,Date now) throws ParseException{
		String[][] bc = new String[2][2];
		if(ftime.parse(check_time).getTime() >= ftime.parse(morningS).getTime()&&ftime.parse(check_time).getTime() <= ftime.parse(morningE).getTime()){
			bc[0][0] = "后夜";
			bc[1][1] = "白班";
			bc[0][1] = fdate.format(now);
		}
		else if(ftime.parse(check_time).getTime() >= ftime.parse(duskS).getTime()&&ftime.parse(check_time).getTime() <= ftime.parse(duskE).getTime()){
			bc[0][0] = "白班";
			bc[1][1] = "前夜";
			bc[0][1] = fdate.format(now);
		}
		else if((ftime.parse(check_time).getTime() >= ftime.parse(nightS).getTime()&&ftime.parse(check_time).getTime() <= ftime.parse("23:59:59").getTime())){
			bc[0][0] = "前夜";
			bc[1][1] = "后夜";
			bc[0][1] = fdate.format(now);
		}
		else if(ftime.parse(check_time).getTime()>=ftime.parse("00:00:00").getTime() && ftime.parse(check_time).getTime()<=ftime.parse(HandoverManagerImpl.nightE).getTime()){
			bc[0][0] = "前夜";
			bc[1][1] = "后夜";
			
			if(now == null){
				bc[0][1] = fdate.format(now);
			}
			else{
				now = addDay(now, -1);
				bc[0][1] = fdate.format(now);
			}
		}
		else{
			bc[0][0] = "1";
			bc[1][1] = "1";
			bc[0][1] = fdate.format(now);
		}
		return bc;
	}
	
	
	
	/**
	 * 日期差
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuot(String time1, String time2){
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
        Date date1 = ft.parse( time2 );
        Date date2 = ft.parse( time1 );
        quot = date1.getTime() - date2.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return quot;
    }
	
	 //实现日期加一天的方法
    public static Date addDay(Date date, int n) {   
        try {   
                 Calendar cd = Calendar.getInstance();   
                 cd.setTime(date);   
                 cd.add(Calendar.DATE, n);//增加一天   
                 return cd.getTime();   
             } catch (Exception e) {   
                 return null;   
             }   
     }  
    
	
	
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	@Resource(name="recordDao")
	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}
	@Resource(name = "shiftDao")
	public void setShiftDao(ShiftDao shiftDao) {
		this.shiftDao = shiftDao;
	}
	
}
