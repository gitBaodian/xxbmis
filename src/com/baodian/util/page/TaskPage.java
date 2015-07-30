package com.baodian.util.page;

public class TaskPage extends Page {
	private String title;//标题+内容
	private int status = -1;//状态
	//1部门全部任务 2部门明天完成 3部门今天完成 4部门未完成
	private int time = -1;//查询状态
	/**
	 * 计算页数
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100) {
			super.setNum(10);
		}
		super.countPage(countNums);
	}
//set get
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
