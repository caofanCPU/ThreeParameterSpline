package com.xyb.demo;

/**
 * 
 * @author CY_XYZ
 * User类：
 * 功能：将服务器返回的每行数据封装为对象
 *     User对象：usename, password，对应数据库users表的user, password字段
 */
public class User {
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + "]";
	}
}
