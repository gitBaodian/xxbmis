package com.baodian.action.record;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.dao.record.IpDao;
import com.baodian.service.record.RecordManager;
import com.opensymphony.xwork2.ActionSupport;
import com.baodian.model.record.IP;

@SuppressWarnings("serial")
@Component("TRAnalyse")
@Scope("prototype")
public class TRAnalyseAction extends ActionSupport {

	/**
	 * 这里的JFreeChart的变量名称必须是chart
	 */
	private JFreeChart chart = null;
	
	private String analyse_date;
	private String Ip;
	
	private int analyse_total = 0;   //统计图中故障次数总和
	
	@Resource(name="recordManager")
	private RecordManager recordmanager;
	
	@Resource(name="ipDao")
	private IpDao ipDao;

	public String date() throws Exception {
	
		/***************************
		*折线图
		****************************/
		//System.out.println("analyse_date="+analyse_date);
		if(analyse_date == null){            //默认当前月
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			analyse_date = df.format(new Date());
		}
		String hql = null;
		String type = "";
		if(analyse_date.length() == 7){      //日统计，analyse_date = "yyyy-mm"
			hql = "select DATE_FORMAT(r_time,'%m-%d'),count(*) from Trouble_Record "+ 
				  "where DATE_FORMAT(r_time,'%Y-%m')=DATE_FORMAT('"+analyse_date+"-01','%Y-%m') "+
				  "GROUP BY DATE_FORMAT(r_time,'%Y-%m-%d')";
			type = "日期";
		}else if(analyse_date.length() == 4){   //月统计，analyse_date = "yyyy"
			hql = "select DATE_FORMAT(r_time,'%m'),count(*) from Trouble_Record "+ 
					  "where DATE_FORMAT(r_time,'%Y')=DATE_FORMAT('"+analyse_date+"-01-01','%Y') "+
					  "GROUP BY DATE_FORMAT(r_time,'%Y-%m')";
			type = "月份";
		}else{
			return "error";
		}

		int days = 0;
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		LinkedHashMap data_map = recordmanager.count4Analyse(hql);
		if(data_map != null){
			String m = analyse_date.substring(analyse_date.length()-2, analyse_date.length());
			String key = "";
			
			Set set = data_map.entrySet();
			int year = Integer.valueOf(analyse_date.substring(0, 4));
			
			int month = Integer.valueOf(m);
			getDataset(dataset,analyse_date, data_map, type, "ALL IP");   //填充数据
		}
		
		chart = ChartFactory.createLineChart(analyse_date+" 机器故障统计结果",type+"  Total:"+analyse_total,"次数",dataset,  
                PlotOrientation.VERTICAL,true,true,false); 
		chart.getTitle().setFont(new Font("黑体",Font.BOLD,32)); 

		Font font = new Font("黑体",Font.BOLD,15); 
		chart.getLegend().setItemFont(font);    //设置引用标签的字体 
		
		CategoryPlot linePlot = chart.getCategoryPlot();   //获取图表 
		
		//设置网格的颜色(纵向和横向网格线条颜色以及可见性) 
		linePlot.setBackgroundPaint(Color.LIGHT_GRAY); 
		linePlot.setRangeGridlinesVisible(true);    //设置横向网格的可见性 
		linePlot.setRangeGridlinePaint(Color.black); 
		linePlot.setDomainGridlinesVisible(true);   //设置纵向网格的可见性 
		linePlot.setDomainGridlinePaint(Color.black); 
		
		CategoryAxis categoryAxis = linePlot.getDomainAxis();  //目录轴(即横坐标) 
		categoryAxis.setLabelPaint(Color.blue); 
		categoryAxis.setLabelFont(font);       //设置目录轴上的标签字体 
		categoryAxis.setTickLabelFont(new Font("Serif", Font.PLAIN, 12));   //设置目录轴上标记字体 
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90); // X轴上的Lable让其45度倾斜
		
		org.jfree.chart.axis.ValueAxis valueAxis = linePlot.getRangeAxis();   //数值轴(即纵坐标) 
		valueAxis.setAutoRange(false);  //数值轴不自动设置数值 
		valueAxis.setAutoRangeMinimumSize(30);   //自动设置数据轴数据范围时数据范围的最小跨度 
		/*valueAxis.setUpperBound(300);   //数值轴的最大值为300 
		valueAxis.setLowerBound(60);  //数值轴最小值为60 */
		valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); //数值轴显示整数 
		valueAxis.setLabelFont(font); 
		
		org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = (org.jfree.chart.renderer.category.LineAndShapeRenderer)linePlot.getRenderer();//获取线条 
		renderer.setShapesVisible(true);   //设置连接点是否显示 
		renderer.setStroke(new BasicStroke(1.8f));
		
		return SUCCESS;
	}
	
	public String IP(){   
		List ip_list = new ArrayList();
		if(Ip != null && !Ip.equals("")){   //指定IP段
			IP ip = new IP(Ip);
			ip_list.add(ip);
		}else{            //未指定IP段，查找所有IP段
			ip_list = ipDao.getAllIp();
		}
		
		if(analyse_date == null){            //默认当前月
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			analyse_date = df.format(new Date());
		}
		String hql = null;
		String type = "";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		LinkedHashMap data_map;
		for(int i = 0; i < ip_list.size(); i++){
			com.baodian.model.record.IP ip = (com.baodian.model.record.IP) ip_list.get(i);
			String ip_head = ip.getIp_head();
			//System.out.println("=============ip_head="+ip_head);
			if(analyse_date.length() == 7){      //日统计，analyse_date = "yyyy-mm"
				hql = "select DATE_FORMAT(r_time,'%m-%d'),count(*) from Trouble_Record "+ 
					  "where DATE_FORMAT(r_time,'%Y-%m')=DATE_FORMAT('"+analyse_date+"-01','%Y-%m') and ip like'"+ip_head+"%' "+
					  "GROUP BY DATE_FORMAT(r_time,'%Y-%m-%d')";
				type = "日期";
			}else if(analyse_date.length() == 4){   //月统计，analyse_date = "yyyy"
				hql = "select DATE_FORMAT(r_time,'%m'),count(*) from Trouble_Record "+ 
						  "where DATE_FORMAT(r_time,'%Y')=DATE_FORMAT('"+analyse_date+"-01-01','%Y') and ip like'"+ip_head+"%' "+
						  "GROUP BY DATE_FORMAT(r_time,'%Y-%m')";
				type = "月份";
			}else{
				return "error";
			}
			
			data_map = recordmanager.count4Analyse(hql);
			
			if(data_map != null){
				getDataset(dataset,analyse_date, data_map, type, ip_head);
			}
		}
		
		chart = ChartFactory.createLineChart(analyse_date+" 机器故障统计结果", type+"  Total:"+analyse_total, "次数", dataset, 
                PlotOrientation.VERTICAL,true,true,false); 
		chart.getTitle().setFont(new Font("华文新魏",Font.BOLD,32)); 

		Font font = new Font("黑体",Font.BOLD,15); 
		chart.getLegend().setItemFont(font);    //设置引用标签的字体 
		
		CategoryPlot linePlot = chart.getCategoryPlot();   //获取图表 
		
		//设置网格的颜色(纵向和横向网格线条颜色以及可见性) 
		linePlot.setBackgroundPaint(Color.LIGHT_GRAY); 
		linePlot.setRangeGridlinesVisible(true);    //设置横向网格的可见性 
		linePlot.setRangeGridlinePaint(Color.black); 
		linePlot.setDomainGridlinesVisible(true);   //设置纵向网格的可见性 
		linePlot.setDomainGridlinePaint(Color.black); 
		
		CategoryAxis categoryAxis = linePlot.getDomainAxis();  //目录轴(即横坐标) 
		categoryAxis.setLabelPaint(Color.blue); 
		categoryAxis.setLabelFont(font);       //设置目录轴上的标签字体 
		categoryAxis.setTickLabelFont(font);   //设置目录轴上标记字体 
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90); // X轴上的Lable让其45度倾斜
		
		org.jfree.chart.axis.ValueAxis valueAxis = linePlot.getRangeAxis();   //数值轴(即纵坐标) 
		valueAxis.setAutoRange(false);  //数值轴不自动设置数值 
		valueAxis.setAutoRangeMinimumSize(30);   //自动设置数据轴数据范围时数据范围的最小跨度 
		/*valueAxis.setUpperBound(300);   //数值轴的最大值为300 
		valueAxis.setLowerBound(60);  //数值轴最小值为60 */
		valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); //数值轴显示整数 
		valueAxis.setLabelFont(font); 
		
		org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = (org.jfree.chart.renderer.category.LineAndShapeRenderer)linePlot.getRenderer();//获取线条 
		renderer.setShapesVisible(true);   //设置连接点是否显示 
		float aa[] = {5.0f}; 
		BasicStroke realLine = new BasicStroke(1.8f);   //实线 
		BasicStroke dashLine = new BasicStroke(2.2f,     //虚线 
											   BasicStroke.CAP_ROUND,   //端点风格 
											   BasicStroke.JOIN_ROUND,  //连接点风格 
											   8f, 
											   aa, 
										       0.6f); 
		for(int i = 0,length = dataset.getColumnCount();i<length;i++){ 
			if(i%2==0){ 
				renderer.setSeriesStroke(i,dashLine); 
			}else{ 
				renderer.setSeriesStroke(i,realLine); 
			} 
		}
		
		return SUCCESS;
	}
	
	public void getDataset(DefaultCategoryDataset dataset,String analyse_date,LinkedHashMap data_map,String type,String ip_head){
		/**
		 * analyse_date: 日期(yyyy 或 yyyy-mm)
		 * data_map: 包含所查日期的数据的map
		 * type: 类型("日期" 或  "月份")  
		 * ip_head : IP段  或  "All IP"
		*/
		String key = "";
		int value = 0;
		
		if(type.equals("日期")){
			String m = analyse_date.substring(analyse_date.length()-2, analyse_date.length());
			int year = Integer.valueOf(analyse_date.substring(0, 4));
			int month = Integer.valueOf(m);
			int days = 0;
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month-1);
			days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);   //所查日期当月的天数
			//System.out.println("analyse_date="+analyse_date+",year="+year+",month="+month+",days = " + days);
			for(int i = 1; i <= days; i++){
				if(i < 10){
					key = m + "-0" + i;
				}else{
					key = m + "-" + i;
				}
				//System.out.println("date:"+key);
				if(!data_map.containsKey(key)){   //不包括此日期，加入数据 “0”
					//System.out.println("notContain key:"+key);
					dataset.setValue(0, ip_head, key);
				}else{                            //包括此日期,从data_map中取数据
					value = Integer.valueOf(data_map.get(key).toString());
					analyse_total = analyse_total + value;
					dataset.setValue(value, ip_head, key);
				}
			}
		}else if(type.equals("月份")){
			for(int i = 1; i <= 12; i++){
				if(i < 10){
					key = "0" + i;
				}else{
					key = String.valueOf(i);
				}
				//System.out.println("month:"+key);
				if(!data_map.containsKey(key)){   //不包括此日期，加入数据 “0”
					//System.out.println("notContain key:"+key);
					dataset.setValue(0, ip_head, key);
				}else{                            //包括此日期,从data_map中取数据
					value = Integer.valueOf(data_map.get(key).toString());
					analyse_total = analyse_total + value;
					dataset.setValue(value, ip_head, key);
				}
			}
		}
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	public String getAnalyse_date() {
		return analyse_date;
	}

	public void setAnalyse_date(String analyse_date) {
		this.analyse_date = analyse_date;
	}

	public String getIp() {
		return Ip;
	}

	public void setIp(String ip) {
		Ip = ip;
	}

}
