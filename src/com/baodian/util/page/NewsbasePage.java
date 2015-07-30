package com.baodian.util.page;

public class NewsbasePage extends Page{
	
	private String title;//标题
	private int ncId;
	private int reply = 2;//是否允许回复2为忽略
	private int status = 2;//审核
	private int display = 2;//显示
	private int sort = 2;//置顶
	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(10);
		super.countPage(countNums);
	}
//set get
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getNcId() {
		return ncId;
	}
	public void setNcId(int ncId) {
		this.ncId = ncId;
	}
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
