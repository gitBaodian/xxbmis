package com.baodian.util.page;

public class ForumPage extends Page{
	
	private int nbId;
	
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
	public int getNbId() {
		return nbId;
	}
	public void setNbId(int nbId) {
		this.nbId = nbId;
	}
	
}
