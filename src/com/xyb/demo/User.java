package com.xyb.demo;

/**
 * 
 * @author CY_XYZ
 * User�ࣺ
 * ���ܣ������������ص�ÿ�����ݷ�װΪ����
 *     User����usename, password����Ӧ���ݿ�users���user, password�ֶ�
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
