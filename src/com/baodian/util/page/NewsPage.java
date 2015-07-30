package com.baodian.util.page;

public class NewsPage extends Page{
	
	private String title;//标题
	private int ncId;
	private String ncName;
	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(40);
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
	public String getNcName() {
		return ncName;
	}
	public void setNcName(String ncName) {
		this.ncName = ncName;
	}
	
}
