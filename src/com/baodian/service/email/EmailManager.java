package com.baodian.service.email;

import com.baodian.model.email.Email;
import com.baodian.util.page.EmailPage;

public interface EmailManager {
//c
	/**
	 * 发送邮件
	 * @param email title和content
	 * @param json "收件人a另一收件人"
	 * @return {status:0,today:"",urnums:4}
	 */
	public String saveEmail(Email email, String json);
//r
	/**
	 * 自己所收发的邮件
	 * @param page
	 * @return {status,today,countnums,urnums,pages,cpage,
	 * 	list:[id,title,date,readst,recest,emailst,uid,uname,dname]}
	 */
	public String findE_Us(EmailPage page);
	/**
	 * 查找新来的邮件
	 * @param oldtime 上一次刷新时间
	 * @return {"status":0,"list":[{}]}
	 */
	public String findNewUrE(String oldtime);
	/**
	 * 查看邮件并更改未状态
	 * @param id 小于0为发件，大于为收件
	 * @return {status,today,urnums,myid,title,content,date,sendst,emailst,uid,uname,did,dname,
	 * 	addrees:[id,name,date,readst,recest,emailst,did,dname]}
	 */
	public String fcEM(String id);
//u
	/**
	 * 更改邮件状态，A收件1->删除, 2->彻底删除, 3->还原, 4->已读
	 *          B发件-1->删除, -2->彻底删除, -3->还原
	 * @param json 状态A邮件a另一邮件
	 * @return findE_Us(page)
	 */
	public String changeSt(String json, EmailPage page);
	/**
	 * 将所有未读收件改为已读
	 * @param json 1正常中的 2已删除中的
	 * @return findE_Us(page)
	 */
	public String changeUr(String json, EmailPage page);

}
