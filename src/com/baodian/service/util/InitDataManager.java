package com.baodian.service.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface InitDataManager {
	/**
	 * 所有允许上传的扩展名
	 */
	public Set<String> extSet = new HashSet<String>();
	public Set<String> imageSet = new HashSet<String>();
	/**
	 * 0-image 1-flash 2-media 3-file 4-uploadDir
	 */
	public static String[] upload = new String[5];
	//四班三倒
	//上班顺序
	public static String[] dutyName = new String[4];
	//白班顺序
	public static int[] dutyOrder = new int[4];
	/**
	 * rsa key
	 * 0-client 1-pubkey 2-modkey
	 */
	//public static List<String> rsaKey = new ArrayList<String>();
	public static Map<String, String> datas = new HashMap<String, String>();
	/**
	 * 重新读取数据
	 */
	public void reload();
	/**
	 * 输出数据
	 */
	public void output();
}
