package com.baodian.service.inspection.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.inspection.Inspection01Dao;
import com.baodian.dao.inspection.Inspection02Dao;
import com.baodian.dao.inspection.Inspection03Dao;
import com.baodian.dao.inspection.InspectionDao;
import com.baodian.model.inspection.Inspection;
import com.baodian.model.inspection.Inspection01;
import com.baodian.model.inspection.Inspection02;
import com.baodian.model.inspection.Inspection03;
import com.baodian.service.inspection.InspectionManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.page.Page;

@Service("inspectionManager")
public class InspectionManagerImpl implements InspectionManager{
	
	
	private InspectionDao inspectionDao;
	private Inspection01Dao inspection01Dao;
	private Inspection02Dao inspection02Dao;
	private Inspection03Dao inspection03Dao;
	private SimpleDateFormat fnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 添加巡检表
	 * @param inspection01
	 * @param inspection02
	 * @param inspection03
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String inspection_all_add(Inspection01 inspection01,Inspection02 inspection02,Inspection03 inspection03){
		String json = "";
		int i;
		if(SecuManagerImpl.currentName().isEmpty()==true){
			json = "{\"status\":1,\"mess\":\"请先登录系统！\"}";
			return json;
		}
		String userName = SecuManagerImpl.currentName();
		
		List<Inspection> insp_list = (List<Inspection>) inspectionDao.findhql("from Inspection i order by i.inspId desc");
		if(insp_list!=null&&insp_list.size()!=0){
			Inspection insp = insp_list.get(0);
			i = insp.getInspId();
			i++;
		}else{
			i = 0;
		}
		Date now = new Date();
		Inspection inspection = new Inspection();
		inspection.setInspId(i);
		inspection.setInspCreatetime(fnow.format(now));
		inspection.setUserName(userName);
		try {
			inspectionDao.save(inspection);
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"添加巡检记录失败！\"}";
			return json;
		}
		inspection01.setInspId(i);
		inspection02.setInspId(i);
		inspection03.setInspId(i);
		try {
			inspection01Dao.save(inspection01);
			inspection02Dao.save(inspection02);
			inspection03Dao.save(inspection03);
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"添加巡检表失败！\"}";
			return json;
		}
		json = "{\"status\":0,\"mess\":\"添加巡检表成功！\"}";
		return json;
	}

	/**
	 * 巡检列表
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String inspection_show(Page page){
		String json = "";
		List<?> insp_lists = inspectionDao.findhql("select COUNT(*) from Inspection i order by i.inspId desc");
		int insps = ((Long) insp_lists.get(0)).intValue();
		if(insps == 0){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		page.countPage(insps);
		List<Inspection> insp_list = (List<Inspection>) inspectionDao.createQuery("from Inspection i order by i.inspId desc"
				,page.getFirstNum(),page.getNum());
		json = "{\"total\":" + page.getCountNums() + "," +
				"\"rows\":[";
		for(Inspection inspection : insp_list){
			List<Inspection01> insp01_list = (List<Inspection01>) inspection01Dao.findByProperty("inspId", inspection.getInspId());
			List<Inspection02> insp02_list = (List<Inspection02>) inspection02Dao.findByProperty("inspId", inspection.getInspId());
			List<Inspection03> insp03_list = (List<Inspection03>) inspection03Dao.findByProperty("inspId", inspection.getInspId());
			json = json.concat("{\"id\":\"" + inspection.getId() + "\"," +
					"\"insp_id\":\"" + inspection.getInspId() + "\"," +
					"\"username\":\"" + inspection.getUserName() + "\"," +
					"\"insp_createtime\":\"" + inspection.getInspCreatetime() + "\"," +
					"\"inspTime01\":\"" + insp01_list.get(0).getInspTime01() + "\"," +
					"\"inspTime02\":\"" + insp02_list.get(0).getInspTime02() + "\"," +
					"\"inspTime03\":\"" + insp03_list.get(0).getInspTime03() + "\"," +
					"\"dsh\":\"待审核\"},");
		}
		if(insp_list.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	
	//获取巡检表信息
	@SuppressWarnings("unchecked")
	public Inspection01 inspection01_get(String insp_id){
		int i = Integer.valueOf(insp_id);
		List<Inspection01> insp01_list = (List<Inspection01>) inspection01Dao.findByProperty("inspId", i);
		return insp01_list.get(0);
	}
	@SuppressWarnings("unchecked")
	public Inspection02 inspection02_get(String insp_id){
		int i = Integer.valueOf(insp_id);
		List<Inspection02> insp02_list = (List<Inspection02>) inspection02Dao.findByProperty("inspId", i);
		return insp02_list.get(0);
	}
	@SuppressWarnings("unchecked")
	public Inspection03 inspection03_get(String insp_id){
		int i = Integer.valueOf(insp_id);
		List<Inspection03> insp03_list = (List<Inspection03>) inspection03Dao.findByProperty("inspId", i);
		return insp03_list.get(0);
	}
	
	
	/**
	 * 删除巡检记录
	 * @return
	 */
	public String inspection_del(String insp_id){
		String json = "";
		try {
			inspectionDao.delhql("from Inspection i where i.inspId = '"+insp_id+"'");
			inspection01Dao.delhql("from Inspection01 i where i.inspId = '"+insp_id+"'");
			inspection02Dao.delhql("from Inspection02 i where i.inspId = '"+insp_id+"'");
			inspection03Dao.delhql("from Inspection03 i where i.inspId = '"+insp_id+"'");
			json = "{\"status\":0,\"mess\":\"删除巡检表成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"删除巡检表成功！\"}";
		}
		return json;
	}
	
	
	@Resource(name="inspectionDao")
	public void setInspectionDao(InspectionDao inspectionDao) {
		this.inspectionDao = inspectionDao;
	}
	@Resource(name="inspection01Dao")
	public void setInspection01Dao(Inspection01Dao inspection01Dao) {
		this.inspection01Dao = inspection01Dao;
	}
	@Resource(name="inspection02Dao")
	public void setInspection02Dao(Inspection02Dao inspection02Dao) {
		this.inspection02Dao = inspection02Dao;
	}
	@Resource(name="inspection03Dao")
	public void setInspection03Dao(Inspection03Dao inspection03Dao) {
		this.inspection03Dao = inspection03Dao;
	}
}
