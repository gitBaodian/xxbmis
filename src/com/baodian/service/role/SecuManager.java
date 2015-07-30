package com.baodian.service.role;

public interface SecuManager {
	/**
	 * 初始化权限与角色的关系，保存在Map<url, Collection<roleId>> resourceMapHash中
	 */
	public void initAll_RA_();
	/**
	 * 刷新权限与角色的关系
	 */
	public void refreshAll_RA_();
	/**
	 * 初始化菜单
	 */
	public void initMenu();
	/**
	 * 刷新数据
	 */
	public void refreshMenu();
	/**
	 * 获取首页的全部信息
	 * @param needMenu 是否需要载入菜单
	 */
	public String findIndex(boolean needMenu);
}
