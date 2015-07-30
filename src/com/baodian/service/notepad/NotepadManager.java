package com.baodian.service.notepad;

import org.json.simple.JSONArray;

import com.baodian.model.notepad.Notepad;
import com.baodian.util.page.NotepadPage;

public interface NotepadManager {

//c
	public String save(Notepad notepad);
//r
	/**
	 * 分页以及查询
	 * @param page
	 * @return
	 */
	public String findNpByPage(NotepadPage page);
	/**
	 * 首页展示的五条记录
	 * @return
	 */
	public JSONArray findNpForIndex();
//u
	public String change(Notepad notepad);
	/**
	 * 批量置顶
	 */
	public String changeTop(String json);
//d
	public String remove(String json);
}
