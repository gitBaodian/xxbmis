package com.baodian.service.user;

import org.json.simple.JSONArray;

import com.baodian.model.user.User;
import com.baodian.util.page.UserPage;

public interface UserManager {

//c
	/**
	 * 添加用户
	 * @param user 用户
	 * @param roleId 角色
	 * @param depmId 部门
	 */
	public String save(User user, int[] roleId, int depmId);
//r
	/**
	 * 分页查找用户的id name account department
	 * @param page 分页信息 部门id
	 * @return json格式
	 */
	public String findU_inadOnPage(UserPage page);
	/**
	 * 根据id查找用户id name account department role
	 * @param id 用户id
	 * @return
	 */
	public User findU_inadrById(int id);
	/**
	 * 根据账号获取密码
	 * @param account 账号
	 * @return 密码
	 */
	public String findU_passByAccount(String account);
	/**
	 * 通过部门id查找此部门的子部门中，所有倒班部门的用户
	 * @param dPid 指定查找哪个部门，为0时，查找自己部门倒班用户
	 */
	public JSONArray findUByDids(int dPid ,String date);
	/**
	 * 根据部门id查找此部门中的子部门及用户，为0时从根节点取
	 * @param depmId
	 * @return [{id,1,name:"",pId:2}]
	 */
	public String findU_Ds(int depmId);
	/**
	 * 根据已读部门读取未读部门，及其的子部门和用户
	 * @param dpms 需要读的部门A已经读取的部门
	 * @return [{pid:0,list:[{id,1,name:"",pId:2}]}]
	 */
	public String findU_Ds(String dpms);
	/**
	 * 返回全部角色和部门
	 * @return {roles:[{id,name,pId}], dpms:[{id,name,pId}]}
	 */
	public String findDRs();
//u
	/**
	 * 更改用户部门角色
	 * @param user 值为空不更改
	 * @param roleId roleId为空时删除，roleId[0]为-1时不更改
	 * @param depmId 为-1时不更改
	 */
	public String change(User user, int[] roleId, int depmId);
	/**
	 * 更改密码
	 * @param user u.account旧密码 u.password新密码
	 * @return
	 */
	public String changePW(User user);
	/**
	 * 移动用户到新部门
	 * @param json (dpmId A uid a uid)
	 */
	public String changeDpm(String json);
	/**
	 * 为用户更改角色
	 * @param json (type A roleIds A userIds) type 1替换 2增加 3删除
	 * @return
	 */
	public String changeRole(String json);
//d
	/**
	 * 删除用户，需要先核对自己的密码
	 * @param user u.id为要删除的id u.password为自己的密码
	 * @return
	 */
	public String remove(User user);
//o
	/**
	 * 输出加密密码
	 */
	public void makePassword(String account, String password);
	/**
	 * 登录用户数
	 */
	public int loginUserNums();
	/**
	 * 登录用户
	 */
	public String findlgUser();
//
	public User getUserByName(String username);
	
	public String getAllUserName();
}
