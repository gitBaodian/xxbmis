package com.baodian.dao.notepad;

import java.util.List;

import com.baodian.dao.util.UtilDao;
import com.baodian.model.notepad.Notepad;
import com.baodian.util.page.NotepadPage;

public interface NotepadDao extends UtilDao {

//c
//r
	/**
	 * 分页以及查询
	 * @param page
	 */
	public List<Notepad> getNpsByPage(NotepadPage page);
	/**
	 * 读取首页展示的记录
	 */
	public List<Notepad> getNpForIndex();
//u
	public void update(Notepad notepad);
	public void updateTop(String id, int top);
//d

}
