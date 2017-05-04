package com.xyb.demo;

import java.util.Scanner;

/**
 * 
 * @author CY_XYZ
 * LoginClient类：客户端，项目运行入口
 * 功能：获取用户登录信息
 *     请求数据库的用户登录服务
 */
public class LoginClient {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("请输入用户名：");
		String user = input.nextLine();
		System.out.println("请输入密码：");
		String password = input.nextLine();
		input.close();
		LoginService ls = new LoginService();
		User u = ls.findUser(user, password);
		if (null != u) {
			System.out.println("欢迎你， " + user + "!");
		} else {
			System.out.println("用户名或密码输入错误！");
		}
	}
}
