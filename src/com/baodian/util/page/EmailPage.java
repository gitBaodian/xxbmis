package com.baodian.util.page;

public class EmailPage extends Page{
	
	private int uid;
	private int emailst;//邮件状态
	private int sortst;//排序状态
	/**
	 * 计算页数
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(20);
		super.countPage(countNums);
	}
//set get
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * A收件：1全部 2未读 3删除
	 * B发件： -1全部 -2删除
	 */
	public int getEmailst() {
		return emailst;
	}
	public void setEmailst(int emailst) {
		this.emailst = emailst;
	}
	/**
	 * 1时间    2发件人
	 * 1正数为逆序 -1负数为正序
	 */
	public int getSortst() {
		return sortst;
	}
	public void setSortst(int sortst) {
		this.sortst = sortst;
	}
}
