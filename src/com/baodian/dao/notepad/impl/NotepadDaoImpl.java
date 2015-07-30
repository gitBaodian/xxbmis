package com.baodian.dao.notepad.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.notepad.NotepadDao;
import com.baodian.dao.util.impl.UtilDaoImpl;
import com.baodian.model.notepad.Notepad;
import com.baodian.util.page.NotepadPage;

@Repository("notepadDao")
public class NotepadDaoImpl extends UtilDaoImpl implements NotepadDao {
//r
	public List<Notepad> getNpsByPage(NotepadPage page) {
		StringBuilder whereSql = new StringBuilder();
		StringBuilder orderSql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		if(page.getTop() != 0) {
			whereSql.append(" and np.top=" + page.getTop());
		}
		if(page.ckName() > 0 ) {
			whereSql.append(" and np.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		switch(page.getTopsort()) {
			case 1: orderSql.append("np.top,"); break;
			case 2: orderSql.append("np.top desc,"); break;
		}
		if(page.getIdsort() == 1) {
			orderSql.append("np.date ");
		} else {
			orderSql.append("np.date desc ");
		}
		return getObjsByPage("select count(np.id) from Notepad np " + (whereSql.length()==0? "": "where " + whereSql.substring(5)),
				"select new Notepad(np.id, np.name, np.date, np.top, u.id, u.name, d.id, d.name)" +
				"from Notepad np, User u, Department d " +
				"where " + (whereSql.length()==0? "": whereSql.substring(5) + " and ") +
					" np.author.id=u.id and np.depm.id=d.id " +
				"order by " + orderSql.toString(), page, params);
	}
	public List<Notepad> getNpForIndex() {
		return getObjs("select new Notepad(np.id, np.name, np.date, np.top)" +
				"from Notepad np order by np.top,np.date desc", 6);
	}
	//u
	public void update(Notepad np) {
		List<String> params = new ArrayList<String>();
		params.add(np.getName());
		bulkUpdate("update Notepad np set np.top=" + np.getTop() + ",np.name=? where np.id=" + np.getId(), params);
	}
	public void updateTop(String id, int top) {
		bulkUpdate("update Notepad np set np.top=" + top + " where np.id=" + id, null);
	}
}
