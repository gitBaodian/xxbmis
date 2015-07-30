package com.baodian.dao.email;

import java.util.List;

import com.baodian.model.email.Email;
import com.baodian.util.page.EmailPage;

public interface EmailDao {

//c
	/**
	 * 保存邮件
	 * @param email
	 */
	public void save(Email email);
//r
	/**
	 * 分页获取个人的发件箱
	 */
	public List<Email> getEms(EmailPage page);
	/**
	 * 读取邮件信息title content
	 * @return 不存在返回null
	 */
	public Email getEm(int id);
	/**
	 * 发送的邮件是否已彻底删除(emailst不等于3)
	 * @return 是返回ture, 否返回false
	 */
	public boolean getCDelete(int eid);
	/**
	 * 更新发件状态
	 * @param uid 更改自己的邮件时要加上，防止更改他人邮件，为0时更改他人邮件
	 * @param state 0-发送状态(1已经发送, 2已回复) 1-邮件状态(1正常, 2已删除, 3彻底删除)
	 */
	public void updateState(int uid, int eid, int state, int st);
//d
	/**
	 * 删除发件
	 */
	public void delete(int eid);
}
