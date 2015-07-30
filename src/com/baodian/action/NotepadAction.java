package com.baodian.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.notepad.Notepad;
import com.baodian.service.notepad.NotepadManager;
import com.baodian.util.page.NotepadPage;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("notepad")
@Scope("prototype")//必须注解为多态
public class NotepadAction extends ActionSupport {
	@Resource(name="notepadManager")
	private NotepadManager nm;
	private String json;
	private NotepadPage page;
	private Notepad notepad;
//c
	public String add_js() {
		json = nm.save(notepad);
		return "json";
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new NotepadPage();
		}
		json = nm.findNpByPage(page);
		return "json";
	}
	public String index_js() {
		json = nm.findNpForIndex().toString();
		return "json";
	}
//u
	public String change_js() {
		json = nm.change(notepad);
		return "json";
	}
	public String changetop_js() {
		json = nm.changeTop(json);
		return "json";
	}
//d
	public String remove_js() {
		json = nm.remove(json);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public NotepadPage getPage() {
		return page;
	}
	public void setPage(NotepadPage page) {
		this.page = page;
	}
	public Notepad getNotepad() {
		return notepad;
	}
	public void setNotepad(Notepad notepad) {
		this.notepad = notepad;
	}
}
