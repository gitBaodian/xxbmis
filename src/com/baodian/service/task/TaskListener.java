package com.baodian.service.task;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 明天23:30定时执行任务，在定时上加入随机数，可以在集群中使用
 * @author LF_eng
 */
public class TaskListener implements ServletContextListener {

	private Timer timer = null;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		timer = new Timer(true);
		Calendar cal = Calendar.getInstance();
		//在每天23:30计算后天的任务
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 30);
		//在集群中加入随机时间
		cal.add(Calendar.SECOND, (int)(Math.random()*1500));
		System.out.println("***定时任务时间：" + cal.getTime() + "***");
		timer.schedule(new TaskManager(), cal.getTime(), 86400000);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();
	}
}
