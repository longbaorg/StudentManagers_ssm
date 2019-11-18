package com.ischoolbar.programmer.entity;

import org.springframework.stereotype.Component;

/*
 * 用户实体类   管理员
 */
@Component  //交给容器创建
public class User {
	private long id;  //自增
	private String username;
	private String password;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
