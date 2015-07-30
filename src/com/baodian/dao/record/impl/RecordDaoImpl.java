package com.baodian.dao.record.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.record.RecordDao;
import com.baodian.model.record.Trouble_Record;
import com.baodian.model.record.Working_Record;

@Repository("recordDao")
public class RecordDaoImpl implements RecordDao {
	
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
	
	public void addWorkingRecord(Working_Record wr) {
		ht.save(wr);
	}

	public Working_Record findWR_by_ID(int id) {
		Working_Record wr = null;
		List list = ht.find("select new Working_Record(wr.id,wr.detail,wr.time,wr.type,u.id,u.name,u.account,wr.user.dpm.name,wr.file_name)" +
				            " from Working_Record wr,User u where wr.user.id=u.id and wr.id=?",id);
		if(list.size() != 0){
			wr = (Working_Record) list.get(0);
		}
		return wr;
	}
	
	public Working_Record findWR_by_UseridAndTime(int u_id, Date time) {
		Working_Record wr = null;
		List list = ht.find("from Working_Record wr where wr.user.id=? and wr.time=?", u_id,time);
		if(list.size() != 0){
			wr = (Working_Record) list.get(0);
		}
		return wr;
	}

	public void updateWorkingRecord(Working_Record wr) {
		ht.bulkUpdate("update Working_Record wr set wr.type=?,wr.detail=?,wr.file_name=? where wr.id=?",wr.getType(),wr.getDetail(),wr.getFile_name(),wr.getId());
	}

	public void deleteWorkingRecord(int id) {
		//ht.delete(wr);
		Working_Record wr = findWR_by_ID(id);
		String filename = wr.getFile_name();
		if(filename != null){            //有附件的话，删除附件
			String basepath = "/documents/attachments/"+filename;
			String realPath = ServletActionContext.getServletContext().getRealPath(basepath);
			File file = new File(realPath);
			if(file.exists()){
				file.delete();
			}
		}
		ht.bulkUpdate("delete Working_Record wr where wr.id=?",id);
	}
	
	public void addTroubleRecord(Trouble_Record tr) {
		ht.save(tr);
		
	}

	@Override
	public void deleteTroubleRecord(int id) {		
		Trouble_Record tr = findTR_by_ID(id);
		String filename = tr.getFilename();
		
		if(filename != null){            //有附件的话，删除附件
			String basepath = "/documents/attachments/"+filename;
			String realPath = ServletActionContext.getServletContext().getRealPath(basepath);
			File file = new File(realPath);
			if(file.exists()){
				file.delete();
			}
		}
		ht.bulkUpdate("delete Trouble_Record tr where tr.id=?",id);
	}

	public Trouble_Record findTR_by_ID(int id) {
		Trouble_Record tr = null;
		List list = ht.find("from Trouble_Record where id=?",id);
		if(list.size() != 0){
			tr = (Trouble_Record) list.get(0);
		}
		return tr;
	}

	public void updateTroubleRecord(Trouble_Record tr) {
		ht.update(tr);
	}

	@Override
	public List count4Analyse(String hql) {
		List list = ht.find(hql);
		return list;
	}

}
