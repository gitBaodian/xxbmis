package com.baodian.service.file.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.service.file.FileManager;
import com.baodian.service.util.InitDataManager;
import com.baodian.util.file.NameComparator;
import com.baodian.util.file.SizeComparator;
import com.baodian.util.file.TypeComparator;

@Service("fileManager")
public class FileManagerImpl implements FileManager {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String filesMes(int userId, String dirName, String path, String order) {
		//根目录路径，可以指定绝对路径，比如 /var/www/documents/
		String basepath = InitDataManager.upload[4] + userId + "/";
		String rootPath = ServletActionContext.getServletContext().getRealPath(basepath) + "/";
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/documents/
		String rootUrl  = ServletActionContext.getServletContext().getContextPath() + basepath;
		File currentPathFile = new File(rootPath);
		if (!currentPathFile.exists()) {
			currentPathFile.mkdirs();
		}
		if (dirName != null) {
			rootPath += dirName + "/";
			rootUrl += dirName + "/";
		}
		//根据path参数，设置各路径和URL
		path = path != null ? path : "";//-----------------------------
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}
		//排序形式，name or size or type
		order = order != null ? order.toLowerCase() : "name";
		//不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			return "{\"message\":\"不允许访问！\"}";
		}
		//最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			return "{\"message\":\"参数有误！\"}";
		}
		//目录不存在或不是目录
		currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			return "{\"message\":\"文件夹不存在，请先上传文件！\"}";
		}
		//遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			String fileName, fileExt;
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.list().length != 0));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", InitDataManager.imageSet.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}
		JSONObject result = new JSONObject();
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		return result.toJSONString();
	}
}
