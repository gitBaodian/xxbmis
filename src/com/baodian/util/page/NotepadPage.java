package com.baodian.util.page;

public class NotepadPage extends Page{
	private String name;//内容
	private int top = 0;//置顶
	private int topsort = 0;//按置顶排序
	private int idsort = 0;//按时间排序
	/**
	 * 计算页数
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100) {
			super.setNum(10);
		}
		super.countPage(countNums);
	}
	/**
	 * check name
	 * @return 空:-1, 超出100:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 100) {
			return -2;
		}
		return name.length();
	}
//set get
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getTopsort() {
		return topsort;
	}
	public void setTopsort(int topsort) {
		this.topsort = topsort;
	}
	public int getIdsort() {
		return idsort;
	}
	public void setIdsort(int idsort) {
		this.idsort = idsort;
	}
	
}
