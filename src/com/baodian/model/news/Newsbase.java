package com.baodian.model.news;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.baodian.model.user.User;

@Entity
@Table(name="newsbase")
public class Newsbase {
	private int id;
	private String title;
	private Date publishtime;
	private String content;
	private int hit;//点击次数
	private int replynum;//回复次数
	private int reply;//是否允许回复
	private int status;//审核
	private int display;//显示
	private int sort;//置顶
	private Date reviewtime;
	private List<Newsreply> newsreplies;
	private User author;
	private User reviewer;
	private Newsclass nclass;
	
	private User replyer;//最后回复人
	private Date replytime;//最后回复时间
	
	public Newsbase() {}
	public Newsbase(int nbId) {
		this.id = nbId;
	}
	//NewsbaseDaoImpl.findNbsForIndex
	public Newsbase(int id, String title, Date pbtime, int rnum, Date rptime) {
		this.id = id;
		this.title = title;
		this.publishtime = pbtime;
		this.replynum = rnum;
		this.replytime = rptime;
	}
	//新闻浏览简要信息
	public Newsbase(int id, String title,int reply, int replynum,
			int aId, String aName, int ncId, String ncName) {
		this.id = id;
		this.title = title;
		this.reply = reply;
		this.replynum = replynum;
		this.nclass = new Newsclass(ncId, ncName);
		this.author = new User(aId, aName);
	}
	/**
	 * 新闻及作者_部门详细信息
	 * NewsbaseDaoImpl.getNbUById()
	 * reid为审核人id，因为它可能为空，所以用Object来接
	 */
	public Newsbase(int id, String title, String content, Date publishtime,
			int hit, int replynum,
			int status, int sort, int reply, int display,
			Object reid, Date reviewtime, int ncId, String ncName,
			int aId, String aName, Date aDate, String aHimage, int dId, String dName) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.publishtime = publishtime;
		this.hit = hit;
		this.replynum = replynum;
		this.status = status;
		this.sort = sort;
		this.reply = reply;
		this.display = display;
		if(reid != null)
			this.reviewer = new User((Integer) reid);
		if(reviewtime != null)
			this.reviewtime = reviewtime;
		this.reviewtime = reviewtime;
		this.nclass = new Newsclass(ncId, ncName);
		this.author = new User(aId, aName, aDate, aHimage, dId, dName);
	}
	/**
	 * 新闻_类型及作者简要信息(管理员级别)
	 * NewsclassManagerImpl.getNbsOnSql()
	 * reid为审核人id，因为它可能为空，所以用Object来接
	 */
	public Newsbase(int id, String title, Date publishtime,
			int hit, int replynum,
			int status, int sort, int reply, int display,
			Object reid, Date retime, int ncId, int aId, String aName) {
		this.id = id;
		this.title = title;
		this.publishtime = publishtime;
		this.hit = hit;
		this.replynum = replynum;
		this.status = status;
		this.display = display;
		this.sort = sort;
		this.reply = reply;
		if(reid != null)
			this.reviewer = new User((Integer) reid);
		if(retime != null)
			this.reviewtime = retime;
		this.nclass = new Newsclass(ncId);
		this.author = new User(aId, aName);
	}
	/**
	 * 新闻_类型及作者简要信息
	 * NewsclassManagerImpl.getNbsOnSql2()
	 * @param position 在新闻列表浏览中显示颜色
	 */
	public Newsbase(int id, String title, Date publishtime, int hit, int replynum,
			int reply, int ncId, String ncName, int position, int aid, String aname,
			int rid, String rname, Date rtime) {
		this.id = id;
		this.title = title;
		this.publishtime = publishtime;
		this.hit = hit;
		this.replynum = replynum;
		this.reply = reply;
		this.nclass = new Newsclass(ncId, ncName, position);
		this.author = new User(aid, aname);
		this.replyer = new User(rid, rname);
		this.replytime = rtime;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Basic(fetch=FetchType.LAZY)
	@Column(columnDefinition="text", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp")
	public Date getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}
	@Column(columnDefinition="int not null default'0'", insertable=false)
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	@Column(columnDefinition="int not null default'0'", insertable=false)
	public int getReplynum() {
		return replynum;
	}
	public void setReplynum(int replynum) {
		this.replynum = replynum;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(columnDefinition="tinyint(1) not null default'0'", insertable=false)
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(insertable=false)
	public User getReviewer() {
		return reviewer;
	}
	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}
	@Column(columnDefinition="timestamp not null", insertable=false)
	public Date getReviewtime() {
		return reviewtime;
	}
	public void setReviewtime(Date reviewtime) {
		this.reviewtime = reviewtime;
	}
	@OneToMany(mappedBy="nbase")
	public List<Newsreply> getNewsreplies() {
		return newsreplies;
	}
	public void setNewsreplies(List<Newsreply> newsreplies) {
		this.newsreplies = newsreplies;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="newsclass_id", nullable=false)
	public Newsclass getNclass() {
		return nclass;
	}
	public void setNclass(Newsclass nclass) {
		this.nclass = nclass;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public User getReplyer() {
		return replyer;
	}
	public void setReplyer(User replyer) {
		this.replyer = replyer;
	}
	@Column(columnDefinition="timestamp not null")
	public Date getReplytime() {
		return replytime;
	}
	public void setReplytime(Date replytime) {
		this.replytime = replytime;
	}
	/**
	 * check title
	 * @return 空:-1, 超出100:-2, 其他返回长度
	 */
	public int ckTitle() {
		if(title == null) {
			return -1;
		}
		if(title.length() > 100) {
			return -2;
		}
		return title.length();
	}
	/**
	 * check content
	 * @return 空:-1, 超出21845:-2, 其他返回长度
	 */
	public int ckContent() {
		if(content == null) {
			return -1;
		}
		if(content.length() > 21845) {
			return -2;
		}
		return content.length();
	}
}
