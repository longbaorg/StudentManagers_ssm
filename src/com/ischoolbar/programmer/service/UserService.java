package com.ischoolbar.programmer.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ischoolbar.programmer.entity.User;


/*
 * @Service放到容器中
 */

public interface UserService {
	/*
	 * 通过用户名查找用户
	 */
	public User findByUserName(String username);
	/*
	 * 添加用户
	 */
	public int addUser(User user);
	/*
	 * 显示所有用户
	 */
	public List<User> get_list(Map<String, Object> querList);
	/*
	 * 搜索按钮
	 */
	public int getTotal(Map<String, Object> queryMap);
	/*
	 * 修改
	 */
	public int edit(User user);
	/*
	 * 删除
	 */
	public int delete(String ids);
	
}
