package com.baodian.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.baodian.service.file.FileManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.util.InitDataManager;

@SuppressWarnings("serial")
@Component("file")
@Scope("prototype")//必须注解为多态
public class FileAction extends ActionSupport {
	private File imgFile;// 文件
	//private String imgFileContentType;// 文件的类型
	private String imgFileFileName;// 文件的名称
	private String dir;//保存目录
	private String path;
	private String order;
	private String json;
	@Resource(name = "userManager")
	private UserManager userManager;
	@Resource(name = "fileManager")
	private FileManager fileManager;
	
	public String manager_rd() {
		return SUCCESS;
	}
	/**
	 * 读取目录
	 */
	public String message_no() {
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			json = "{\"message\":\"请先登录！\"}";
			return "json";
		}
		json = fileManager.filesMes(userId, dir, path, order);
		return "json";
	}
	/**
	 * 创建目录
	 */
	public String mkdir() {
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			json = getError("请先登录！");
			return "json";
		}
		if(dir== null || dir.isEmpty()) {
			json = getError("未选择文件或目录！");
			return "json";
		}
		if(dir.indexOf("..") >= 0) {
			json = getError("非法目录名！");
			return "json";
		}
		String basepath = InitDataManager.upload[4] + userId  + "/" + dir;
		// 获得项目路径  
		String realpath = ServletActionContext.getServletContext().getRealPath(basepath);
		File file = new File(realpath);
		if (!file.exists()) {
			if(file.mkdir()) json = "{\"error\":0}";
			else json = getError("请检查文件夹名称！");
		} else {
			json = getError("名称已存在！");
		}
		return "json";
	}
	/**
	 * 更改名称
	 */
	public String change_no() {
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			json = getError("请先登录！");
			return "json";
		}
		if(path==null || path.isEmpty()) {
			json = getError("未选择文件或目录！");
			return "json";
		}
		if(path.indexOf("..") >= 0) {
			json = getError("非法目录名！");
			return "json";
		}
		if(dir== null || dir.isEmpty()) {
			json = getError("请检查新的文件或文件夹名称！");
			return "json";
		}
		if(dir.indexOf("..") >= 0) {
			json = getError("非法目录名！");
			return "json";
		}
		// 新文件名
		String basepath = InitDataManager.upload[4] + userId  + "/" + dir;
		String realpath = ServletActionContext.getServletContext().getRealPath(basepath);
		File file = new File(realpath);
		// 原文件名
		basepath = InitDataManager.upload[4] + userId  + "/" + path;
		realpath = ServletActionContext.getServletContext().getRealPath(basepath);
		File oldFile = new File(realpath);
		if (oldFile.exists()) {
			if(file.exists()) {
				json = getError("文件或文件夹已存在！");
			} else {
				if(oldFile.isFile()) {
					if(! InitDataManager.extSet.contains(dir.substring(dir.lastIndexOf(".") + 1).toLowerCase())) {
						json = getError("文件扩展名只允许以下格式。 \\nimage: " + InitDataManager.upload[0] +
								"\\nflash: " + InitDataManager.upload[1] +
								"\\nmedia: " + InitDataManager.upload[2] +
								"\\nfile:  " + InitDataManager.upload[3]);
						return "json";
					}
				}
				if(oldFile.renameTo(file)) json = "{\"error\":0}";
				else json = getError("请检查新的文件或文件夹名称！");
			}
		} else {
			json = getError("原文件或文件夹不存在！");
		}
		return "json";
	}
	/**
	 * 删除文件
	 */
	public String remove() {
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			json = getError("请先登录！");
			return "json";
		}
		if(dir== null || dir.isEmpty()) {
			json = getError("未选择文件或目录！");
			return "json";
		}
		if(dir.indexOf("..") >= 0) {
			json = getError("非法文件或目录！");
			return "json";
		}
		String basepath = InitDataManager.upload[4] + userId + "/" + dir;
		// 获得项目路径  
		String realpath = ServletActionContext.getServletContext().getRealPath(basepath);
		File file = new File(realpath);
		if (file.exists()) {
			if(file.isDirectory())
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			else
				file.delete();
		}
		json = "{\"error\":0}";
		return "json";
	}
	/**
	 * 上传文件
	 */
	public String upload_no() {
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			json = getError("请先登录！");
			//因为上传调用的是iframe，所以不能返回x-json数据流，否则返回的数据会当做文件下载
			return "jhtml";
		}
		if (imgFile == null) {
			json = getError("请先选择文件！");
			return "jhtml";
		}
		if(dir== null) {
			json = getError("未选择上传目录！");
			return "jhtml";
		}
		//检查dir路径的合法性
		if(dir.indexOf("..") >= 0) {
			json = getError("非法目录名！");
			return "jhtml";
		}
		String basepath = InitDataManager.upload[4] + userId + "/" + dir;
		// 获得项目路径  
		String savePath = ServletActionContext.getServletContext().getRealPath(basepath);
		//创建文件夹
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		//检查目录写权限
		if(!file.canWrite()){
			json = getError("上传目录没有写权限！");
			return "jhtml";
		}
		/*//检查文件大小
		long maxSize = 1000000;
		if(imgFile.getSize() > maxSize){
			json = getError("上传文件大小超过限制！");
			return "jhtml";
		}*/
		//检查扩展名
		String fileExt = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
		if(dir.equals("image")) {
			if(! InitDataManager.imageSet.contains(fileExt)) {
				json = getError("上传的图片只允许是以下格式：\\n" + InitDataManager.upload[0]);
				return "jhtml";
			}
		} else if(! InitDataManager.extSet.contains(fileExt)) {
			json = getError("只允许上传以下格式文件。 \\nimage: " + InitDataManager.upload[0] +
					"\\nflash: " + InitDataManager.upload[1] +
					"\\nmedia: " + InitDataManager.upload[2] +
					"\\nfile:  " + InitDataManager.upload[3]);
			return "jhtml";
		}
		//SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		//imgFileFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try {
			file = new File(file, imgFileFileName);
			if(file.exists()) {
				json = getError("文件已存在！");
				return "jhtml";
			} else {
				FileUtils.copyFile(imgFile, file);
			}
			json = "{\"error\":0,\"url\":\"" +
					ServletActionContext.getServletContext().getContextPath() +
					basepath + "/" + imgFileFileName+"\"}";
			return "jhtml";
		} catch (IOException e) {
			json = getError("上传失败！");
			return "jhtml";
		}
		
	}
	private String getError(String message) {
		return "{\"error\":1,\"message\":\"" + message + "\"}";
	}
//setget
	public File getImgFile() {
		return imgFile;
	}
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public String getImgFileFileName() {
		return imgFileFileName;
	}
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
//用户登录信息
	public int getLoginNums() {
		return this.userManager.loginUserNums();
	}
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManagerImpl.currentUser();
	}
}
