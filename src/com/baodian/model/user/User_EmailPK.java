package com.baodian.model.user;

import java.io.Serializable;

import com.baodian.model.email.Email;

@SuppressWarnings("serial")
public class User_EmailPK implements Serializable {
	private User user;
	private Email email;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof User_EmailPK) {
			User_EmailPK pk = (User_EmailPK)o;
			if(this.email.getId() == pk.email.getId() && this.user.getId() == pk.user.getId()) {
			  return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.user.hashCode() + this.email.hashCode();
	}
}
