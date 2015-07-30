package com.baodian.service.notepad.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.notepad.NotepadDao;
import com.baodian.model.notepad.Notepad;
import com.baodian.model.user.Department;
import com.baodian.model.user.User;
import com.baodian.service.notepad.NotepadManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.JSONValue;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.NotepadPage;

@Service("notepadManager")
public class NotepadManagerImpl implements NotepadManager {
	private NotepadDao ndao;
	@Resource(name="notepadDao")
	public void setNdao(NotepadDao ndao) {
		this.ndao = ndao;
	}
//c
	public String save(Notepad notepad) {
		if(notepad==null || notepad.ckName()<1) {
			return StaticMethod.inputError;
		}
		int[] ids = SecuManagerImpl.currentIds();
		if(ids == null) {
			return StaticMethod.loginError;
		}
		notepad.setAuthor(new User(ids[0]));
		notepad.setDepm(new Department(ids[1]));
		notepad.setDate(new Date());
		if(notepad.getTop() != 1) {
			notepad.setTop(2);
		}
		ndao.add(notepad);
		return StaticMethod.addSucc;
	}
//r
	public String findNpByPage(NotepadPage page) {
		List<Notepad> notepads = ndao.getNpsByPage(page);
		if(notepads.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append("{\"total\":" + page.getCountNums() + ",\"rows\":[");
		for(Notepad np : notepads) {
			json.append("{\"id\":" + np.getId() +
					",\"name\":\"" + JSONValue.escape(np.getName()) +
					"\",\"date\":\"" + StaticMethod.DateToString(np.getDate()) +
					"\",\"top\":" + np.getTop() +
					",\"uid\":" + np.getAuthor().getId() +
					",\"uname\":\"" + np.getAuthor().getName() +
					"\",\"did\":" + np.getDepm().getId() +
					",\"dname\":\"" + np.getDepm().getName() + "\"},");
		}
		return json.substring(0, json.length()-1) + "]}";
	}
	@SuppressWarnings("unchecked")
	public JSONArray findNpForIndex() {
		JSONArray array = new JSONArray();
		List<Notepad> notepads = ndao.getNpForIndex();
		if(notepads.size() == 0) {
			return array;
		}
		for(Notepad np : notepads) {
			JSONObject json = new JSONObject();
			json.put("id", np.getId());
			json.put("top", np.getTop());
			json.put("name", np.getName());
			json.put("date", StaticMethod.DateToString(np.getDate()));
			array.add(json);
		}
		return array;
	}
//u
	public String change(Notepad notepad) {
		if(notepad==null || notepad.ckName()<1) {
			return StaticMethod.inputError;
		}
		if(! ndao.chkExit(notepad.getId(), "Notepad")) {
			return StaticMethod.jsonMess(1, "此记录不存在！");
		}
		if(notepad.getTop() != 1) {
			notepad.setTop(2);
		}
		ndao.update(notepad);
		return StaticMethod.changeSucc;
	}
	public String changeTop(String json) {
		if(json==null || json.isEmpty()) {
			return StaticMethod.inputError;
		}
		String[] str = json.split("A");
		if(str.length != 2) {
			return StaticMethod.inputError;
		}
		int top = 2;
		try {
			top = Integer.parseInt(str[0]);
		} catch(NumberFormatException e) {}
		if(top != 1) {
			top = 2;
		}
		Set<Integer> npIds = new HashSet<Integer>();
		int id = 0;
		for(String s : str[1].split("a")) {
			try {
				id = Integer.parseInt(s);
				if(npIds.add(id)) {
					ndao.updateTop(s, top);
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.changeSucc;
	}
//d
	public String remove(String json) {
		if(json==null || json.isEmpty()) {
			return StaticMethod.inputError;
		}
		Set<Integer> npIds = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("a")) {
			try {
				id = Integer.parseInt(s);
				if(npIds.add(id)) {
					ndao.delete(s, "Notepad");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
}
