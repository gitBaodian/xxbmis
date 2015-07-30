package com.baodian.model.device;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.User;

/**
 * 设备记录
 * @author LF_eng
 */
@Entity
@Table(name="f_g_record")
public class GoodsRecord {
	private int id;
	private String name;
	private Date date;
	private Goods gd;
	private DepotType dtin;
	private DepotType dtout;
	private int num;
	private User recorder;
	
	public GoodsRecord() {}
	//GoodsRecordDaoImpl.getGsByPage
	public GoodsRecord(int id, Date date, String name, int num,
			Object dtin, Object dtout, int gd, int uid, String uname) {
		this.id = id;
		this.date = date;
		this.name = name;
		this.num = num;
		if(dtin == null) {
			this.dtin = new DepotType(0);
		} else {
			this.dtin = new DepotType((Integer) dtin);
		}
		if(dtout == null) {
			this.dtout = new DepotType(0);
		} else {
			this.dtout = new DepotType((Integer) dtout);
		}
		this.gd = new Goods(gd);
		this.recorder = new User(uid, uname);
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=100, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Goods getGd() {
		return gd;
	}
	public void setGd(Goods gd) {
		this.gd = gd;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dtin")
	public DepotType getDtin() {
		return dtin;
	}
	public void setDtin(DepotType dtin) {
		this.dtin = dtin;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dtout")
	public DepotType getDtout() {
		return dtout;
	}
	public void setDtout(DepotType dtout) {
		this.dtout = dtout;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public User getRecorder() {
		return recorder;
	}
	public void setRecorder(User recorder) {
		this.recorder = recorder;
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
}
