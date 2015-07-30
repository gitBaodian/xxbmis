package com.baodian.dao.user;

import java.util.List;

import com.baodian.model.user.User;
import com.baodian.model.user.User_Email;
import com.baodian.util.page.EmailPage;

public interface User_EmailDao {

//c
	/**
	 * 保存
	 */
	public void save(User_Email user_Email);
//r
	/**
	 * 收件箱
	 */
	public List<User_Email> getE_Us(EmailPage page);
	/**
	 * 未读邮件数量
	 * @return 无返回0
	 */
	public int getUnReadByUId(int uId);
	/**
	 * 是否都彻底删除此封邮件(emailst不等于3)
	 * @param uid 为0表示查找所有人，不为0表示除去自己
	 * @return 是返回true, 否返回false
	 */
	public boolean getCDelete(int uid, int eid);
	/**
	 * 取所发信的3个收件人 uid uname
	 */
	public List<User> get3UsByEId(int id);
	/**
	 * 获取新的未读邮件
	 * @param ot 上一次读取的时间
	 */
	public List<User_Email> getE_Us(int uid, String ot);
	/**
	 * 根据邮件id查找所有的收件人
	 * @return uid,uname,did,dname,state,date
	 */
	public List<User_Email> getE_UsByEId(int id);
	/**
	 * 查找此id的用户个数
	 * @return
	 */
	public int getUnum(int uid);
//u
	/**
	 * 更新收件状态
	 * @param state 0-读取状态(1未读, 2已读, 3已回复) 1-邮件状态(1正常, 2已删除, 3彻底删除) 2-收件状态(1收件, 2抄送, 3转发)
	 */
	public void updateState(int uid, int eid, int state, int st);
	/**
	 * 将全部未读更改为已读
	 * @param emailst 1正常邮件, 2已删除邮件
	 */
	public void updateUr(int uid, int emailst);
//d
	/**
	 * 删除邮件的所有收件
	 */
	public void delete(int eid);
}
