package com.ischoolbar.programmer.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ischoolbar.programmer.entity.User;

/*
 * 放到容器中
 * 该文件中需要与mybatis中的mapper文件联合显示   所以这个文件写的很少
 * @Repository作用是将该文件和mapper中的xml结合起来去查询
 */
@Repository
public interface UserDao {
	public User findByUserName(String username);
	public int addUser(User user);
	public List<User> get_list(Map<String, Object> querList);
	public int getTotal(Map<String,Object> queryMap);
	public int edit(User user);
	public int delete(String ids);
}
