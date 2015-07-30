package com.baodian.service.file;

public interface FileManager {

	/**
	 * 查询文件夹信息
	 * @param userId 用户Id
	 * @param dirName 目录
	 * @param path 路径
	 * @param order 排序
	 * @return JSON格式保存的文件夹信息
	 */
	public String filesMes(int userId, String dirName, String path, String order);

}
